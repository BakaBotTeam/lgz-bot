package ltd.guimc.dlm;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DLModel {
    private static ComputationGraph model;
    private static boolean inited = false;

    // 加载ONNX模型
    public static void init() {
        try {
            // 从资源文件中获取模型
            InputStream modelStream = ImageProcessor.class.getResourceAsStream("/Nailong.onnx");

            if (modelStream == null) {
                throw new IOException("无法找到Nailong.onnx模型文件");
            }

            // 创建临时文件来存储ONNX模型
            File tempFile = File.createTempFile("Nailong", ".onnx");
            tempFile.deleteOnExit();

            // 将模型文件从JAR中复制到临时文件
            Files.copy(modelStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 加载ONNX模型
            model = ComputationGraph.load(tempFile, false);
            model.init();
            inited = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkImage(Mat image) {
        if (!inited) {
            return false;
        }
        // 将图像调整为32x32
        Mat resizedImage = new Mat();
        opencv_imgproc.resize(image, resizedImage, new Size(32, 32));

        // 转换图像为INDArray格式
        INDArray transformedImage = preprocessImage(resizedImage);

        // 添加批次维度（1, channels, height, width）
        INDArray inputImage = transformedImage.reshape(1, 3, 32, 32);

        // 执行推理
        INDArray output = model.outputSingle(inputImage);

        // 获取预测标签
        int predictedClass = Nd4j.argMax(output, 1).getInt(0);

        // 如果预测类为10，返回True，否则返回False
        return predictedClass == 10;
    }

    private static INDArray preprocessImage(Mat image) {
        // 创建INDArray以存储像素值
        INDArray imgArray = Nd4j.create(3, 32, 32);

        // 将OpenCV的Mat图像转换为NDArray
        byte[] data = new byte[32 * 32 * 3];
        image.data().get(data);

        // 归一化并重塑图像
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int pixelIndex = y * 32 * 3 + x * 3;
                imgArray.putScalar(new int[]{0, y, x}, (data[pixelIndex] & 0xFF) / 255.0 - 0.5);   // 红色通道
                imgArray.putScalar(new int[]{1, y, x}, (data[pixelIndex + 1] & 0xFF) / 255.0 - 0.5); // 绿色通道
                imgArray.putScalar(new int[]{2, y, x}, (data[pixelIndex + 2] & 0xFF) / 255.0 - 0.5); // 蓝色通道
            }
        }

        return imgArray;
    }
}
