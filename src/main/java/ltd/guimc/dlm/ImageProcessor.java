package ltd.guimc.dlm;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.core.CvType;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageProcessor {
    public static Mat processImage(byte[] imageData) throws IOException {
        BufferedImage image = null;
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(iis);

            // Check if the image is an animated GIF
            if ("gif".equalsIgnoreCase(reader.getFormatName()) && reader.getNumImages(true) > 1) {
                image = reader.read(0); // Read the first frame of the animated GIF
            } else {
                // For non-GIF or single-frame GIF, read the image normally
                image = ImageIO.read(new ByteArrayInputStream(imageData));
            }
        }

        // Convert the image to OpenCV Mat
        assert image != null;
        Mat mat = bufferedImageToMat(image);

        // Convert the Mat from RGB to BGR if it's a 3-channel image
        if (mat.channels() == 3) {
            opencv_imgproc.cvtColor(mat, mat, opencv_imgproc.COLOR_RGB2BGR);
        }

        return mat;
    }

    private static Mat bufferedImageToMat(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Mat mat = new Mat(height, width, CvType.CV_8UC3); // 3-channel image

        byte[] data = new byte[width * height * 3]; // RGB or BGR
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        // Convert BufferedImage to byte array (BGR format)
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            data[i * 3] = (byte) ((pixel >> 16) & 0xFF);  // Red channel
            data[i * 3 + 1] = (byte) ((pixel >> 8) & 0xFF); // Green channel
            data[i * 3 + 2] = (byte) (pixel & 0xFF);       // Blue channel
        }

        // Put pixel data into the Mat object row by row
        mat.data().put(data);
        return mat;
    }
}
