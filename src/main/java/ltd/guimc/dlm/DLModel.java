package ltd.guimc.dlm;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.onnxruntime.engine.OrtEngineProvider;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DLModel {
    private static Model model;
    private static boolean inited = false;

    public static void init() {
        Engine.registerEngine(new OrtEngineProvider());
        System.out.println("Available engines: " + Engine.getAllEngines());
        try {
            // 从资源文件中获取模型
            InputStream modelStream = DLModel.class.getResourceAsStream("/Nailong.onnx");

            if (modelStream == null) {
                throw new IOException("无法找到Nailong.onnx模型文件");
            }

            // 创建临时文件来存储ONNX模型
            File tempFile = File.createTempFile("Nailong", ".onnx");
            tempFile.deleteOnExit();

            // 将模型文件从JAR中复制到临时文件
            Files.copy(modelStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 使用 DJL (Deep Java Library) 加载 ONNX 模型
            model = Model.newInstance("Onnx");
            model.load(tempFile.toPath());

            // 初始化模型
            inited = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedModelException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkImage(byte[] imageData) {
        if (!inited) {
            return false;
        }

        try {
            // 将字节数组转换为 DJL 的 Image 对象
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            Image djlImage = ImageFactory.getInstance().fromInputStream(bais);

            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder().build();

            // 使用模型进行推理
            try (Predictor<Image, Classifications> predictor = model.newPredictor(translator)) {
                Classifications classifications = predictor.predict(djlImage);

                // 获取预测的结果类
                String predictedClass = classifications.best().getClassName();

                // 如果预测类为10，返回True，否则返回False
                return "10".equals(predictedClass);
            }
        } catch (IOException | TranslateException e) {
            e.printStackTrace();
            return false;
        }
    }
}
