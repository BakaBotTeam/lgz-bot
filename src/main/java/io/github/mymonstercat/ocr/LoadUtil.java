package io.github.mymonstercat.ocr;

import io.github.mymonstercat.loader.LibraryLoader;
import io.github.mymonstercat.loader.ModelsLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.util.Properties;

/**
 * 库文件加载工具类
 */
@Slf4j
public class LoadUtil {
    private LoadUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String loaders = "# ONNX\n" +
        "onnx.mac-arm64=io.github.mymonstercat.OnnxMacArm64LibraryLoader\n" +
        "onnx.mac-x86_64=io.github.mymonstercat.OnnxMacX8664LibraryLoader\n" +
        "onnx.win-x86=io.github.mymonstercat.OnnxWindowsX86LibraryLoader\n" +
        "onnx.win-x86_64=io.github.mymonstercat.OnnxWindowsX8664LibraryLoader\n" +
        "onnx.linux-x86_64=io.github.mymonstercat.OnnxLinuxX8664LibraryLoader\n" +
        "onnx.linux-arm64=io.github.mymonstercat.OnnxLinuxArm64LibraryLoader\n" +
        "onnx.model=io.github.mymonstercat.OnnxModelsLoader\n" +
        "# NCNN\n" +
        "ncnn.mac-arm64=io.github.mymonstercat.NcnnMacArm64LibraryLoader\n" +
        "ncnn.mac-x86_64=io.github.mymonstercat.NcnnMacX8664LibraryLoader\n" +
        "ncnn.win-x86_64=io.github.mymonstercat.NcnnWindowsX8664LibraryLoader\n" +
        "ncnn.linux-x86_64=io.github.mymonstercat.NcnnLinuxX8664LibraryLoader\n" +
        "ncnn.model=io.github.mymonstercat.NcnnModelsLoader\n";

    static LibraryLoader findLibLoader(String engine) {
        Properties props = new Properties();
        try {
            props.load(new StringReader(loaders));

            String osName = System.getProperty("os.name").toLowerCase();
            String osArch = System.getProperty("os.arch").toLowerCase();
            log.debug("osName: {}, osArch: {}", osName, osArch);
            String loaderClassName = null;
            if (osName.contains("win")) {
                if (osArch.contains("amd64")) {
                    loaderClassName = props.getProperty(engine + ".win-x86_64");
                } else if (osArch.contains("x86")) {
                    loaderClassName = props.getProperty(engine + ".win-x86");
                }
            } else if (osName.contains("mac")) {
                if (osArch.contains("arch64")) {
                    loaderClassName = props.getProperty(engine + ".mac-arm64");
                } else {
                    loaderClassName = props.getProperty(engine + ".mac-x86_64");
                }
            } else if (osName.contains("linux")) {
                if (osArch.contains("x86") || osArch.contains("amd64")) {
                    loaderClassName = props.getProperty(engine + ".linux-x86_64");
                } else if (osArch.contains("arm") || osArch.contains("arch64")) {
                    loaderClassName = props.getProperty(engine + ".linux-arm64");  //Note: only support onnx,  not support ncnn now
                }
            }
            if (loaderClassName != null) {
                return (LibraryLoader) Class.forName(loaderClassName).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            log.error("获取库文件加载器 {} 失败", e.getMessage());
        }
        return null;
    }

    public static ModelsLoader findModelsLoader(String engine) {
        Properties props = new Properties();
        try {
            props.load(new StringReader(loaders));

            return (ModelsLoader) Class.forName(props.getProperty(engine + ".model")).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("获取模型文件加载器 {} 失败", e.getMessage());
        }
        return null;
    }
}
