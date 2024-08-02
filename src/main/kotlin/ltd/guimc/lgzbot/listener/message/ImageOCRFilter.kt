package ltd.guimc.lgzbot.listener.message

import io.github.mymonstercat.Model
import io.github.mymonstercat.ocr.InferenceEngine
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.adPinyinRegex
import ltd.guimc.lgzbot.PluginMain.adRegex
import ltd.guimc.lgzbot.PluginMain.disableImageCheck
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.listener.message.MessageFilter.messagesHandled
import ltd.guimc.lgzbot.listener.message.MessageFilter.mute
import ltd.guimc.lgzbot.listener.message.MessageFilter.recalledMessage
import ltd.guimc.lgzbot.listener.message.MessageFilter.riskList
import ltd.guimc.lgzbot.listener.message.MessageFilter.setVl
import ltd.guimc.lgzbot.utils.AsciiUtil.sbc2dbcCase
import ltd.guimc.lgzbot.utils.HttpUtils
import ltd.guimc.lgzbot.utils.ImageUtils
import ltd.guimc.lgzbot.utils.LL4JUtils
import ltd.guimc.lgzbot.utils.MemberUtils.mute
import ltd.guimc.lgzbot.utils.RegexUtils
import ltd.guimc.lgzbot.utils.TextUtils.removeInterference
import ltd.guimc.lgzbot.utils.TextUtils.removeNonVisible
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.toMessageChain
import org.apache.commons.lang3.time.StopWatch
import java.io.ByteArrayInputStream
import java.io.File
import java.security.MessageDigest
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.concurrent.locks.ReentrantLock


object ImageOCRFilter {
    lateinit var engine: InferenceEngine
    lateinit var connection: Connection
    private val lock = ReentrantLock()
    var supported: Boolean = false

    suspend fun filter(e: GroupMessageEvent) {
        if (!supported) return
        findImageToFilter(e, e.message)
    }

    suspend fun findImageToFilter(e: GroupMessageEvent, m: MessageChain): Boolean {
        for (message in m) {
            if (message is Image) {
                if (filterImage(e, message)) {
                    return true
                }
            } else if (message is ForwardMessage) {
                if (findImageToFilter(e, message.toMessageChain())) {
                    return true
                }
            }
        }
        return false
    }

    suspend fun filterImage(e: GroupMessageEvent, m: Image): Boolean {
        var muted = false
        try {
            var content = sbc2dbcCase(recognizeAndStoreImage(m.cast<Image>().queryUrl()))
                .lowercase()
                .removeInterference()
                .removeNonVisible()
            if (!(e.sender.permission.level < e.group.botPermission.level || ModuleStateConfig.slientmute)) return false
            if (e.group.permitteeId.hasPermission(disableImageCheck)) return false

            val predictedResult = LL4JUtils.predictAllResult(content)
            val predicted = predictedResult[1] > predictedResult[0]
            if (RegexUtils.matchRegex(adRegex, content) && content.length >= 30) {
                try {
                    recalledMessage++
                    e.message.recall()
                    if (predicted) {
                        e.group.mute(e.sender, "非法发言内容 (图片OCR识别) (模型证实)")
                        riskList.add(e.sender)
                        setVl(e.sender.id, 99.0)
                    } else {
                        e.sender.mute(60, "非法发言内容 (图片OCR识别)")
                    }
                    muted = true
                } catch (_: Exception) {
                }
                messagesHandled++
            }

            // 拼音检查发言
            if (!muted && riskList.indexOf(e.sender) != -1 && RegexUtils.matchRegexPinyin(adPinyinRegex, content)) {
                try {
                    recalledMessage++
                    e.message.recall()
                    e.group.mute(e.sender, "非法发言内容 (图片OCR识别)")
                    muted = true
                } catch (_: Exception) {
                }
                setVl(e.sender.id, 99.0)
                messagesHandled++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return muted
    }

    fun calculateSha256(fileData: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(fileData)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun recognizeAndStoreImage(imageUrl: String): String {
        try {
            // 获取图片数据
            val imageRaw = HttpUtils.getBytesResponse(imageUrl)
            if (imageRaw == null) {
                throw RuntimeException("Image data is null")
            }

            // 计算 SHA-256 哈希值
            val sha256Hash = calculateSha256(imageRaw)

            // 检查哈希是否已存在
            val querySql = "SELECT image_content FROM recognized_images WHERE sha256_hash = ?"
            val pstmt: PreparedStatement = connection.prepareStatement(querySql)
            pstmt.setString(1, sha256Hash)
            val rs: ResultSet = pstmt.executeQuery()
            var content: String = "";

            if (rs.next()) {
                // 如果哈希已存在，获取内容
                content = rs.getString("image_content")
            } else {
                // 如果哈希不存在，进行OCR处理并存储
                val imageData = ByteArrayInputStream(imageRaw.clone())
                val imageType = ImageUtils.imgType(imageData)
                if (imageType.equals("gif") || imageType.equals("webp") || imageType.equals("tif")) return ""
                val tempFile = File.createTempFile("temp", ".$imageType")
                tempFile.writeBytes(imageRaw)

                // OCR 识别
                val stopWatch = StopWatch()
                stopWatch.start()
                content = engine.runOcr(tempFile.path).strRes.trim()
                stopWatch.stop()
                logger.info("OCR已完成 耗时: ${stopWatch.time} ms")

                // 删除临时文件
                tempFile.delete()

                // 插入识别内容到数据库
                lock.lock()
                val insertSql = "INSERT INTO recognized_images (sha256_hash, image_content) VALUES (?, ?)"
                val insertPstmt: PreparedStatement = connection.prepareStatement(insertSql)
                insertPstmt.setString(1, sha256Hash)
                insertPstmt.setString(2, content)
                insertPstmt.executeUpdate()
                insertPstmt.close()
                lock.unlock()
            }

            rs.close()
            pstmt.close()
            return content
        } finally {
            // 确保锁被释放
            lock.unlock()
        }
    }


    fun init() {
        try {
            engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3)
            connection = initializeDatabase(
                "jdbc:sqlite:" + File(
                    PluginMain.dataFolderPath.toFile(),
                    "recognized_images.db"
                ).absolutePath
            )
            supported = true
        } catch (e: Exception) {
            logger.warning(e)
            supported = false
        }
    }

    fun initializeDatabase(dbUrl: String): Connection {
        val dbFile = File(dbUrl.substringAfter("jdbc:sqlite:"))
        val isNewDatabase = !dbFile.exists()
        val conn = DriverManager.getConnection(dbUrl)

        if (isNewDatabase) {
            val stmt = conn.createStatement()
            val createTableSql = """
            CREATE TABLE IF NOT EXISTS recognized_images (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sha256_hash TEXT UNIQUE,
                image_content TEXT
            )
        """
            stmt.execute(createTableSql)
            stmt.close()
        }

        return conn
    }
}