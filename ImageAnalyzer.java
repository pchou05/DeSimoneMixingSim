import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageAnalyzer {

    public static void main(String[] args) {
        String[] paths = {"C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\Test1.png", "C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\Test2.png", "C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\Test3.png", "C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\Test2.png", "C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\TestV2.png", "E:\\idealSample.png", "C:\\Users\\DeSimoneLab\\Downloads\\MixingFluidRenders\\TestV2.2.png"};  // Replace with your image path
        Color idealMixedColor = new Color(64, 0, 83);

        for(String path : paths){
            double averageDeviation = calculateAverageDeviation(path, idealMixedColor);
            double varianceOfDeviation = varianceOfDeviation(path, averageColor(path));
            System.out.println("Properties of " + path);
            if(overConcentration(averageColor(path))){    
                System.out.println("This Might Be The Source? Over Saturation of B/R");
            }
            System.out.println("Average Deviation from Ideal Mixed Color: " + averageDeviation);
            System.out.println("Variance of Deviation: " + varianceOfDeviation);
            System.out.println("Mixing Ratio B/R: " + (averageColor(path).getBlue()/averageColor(path).getRed()));
            System.out.println("\n");
        }
    }



    static class Color {
        private double red;
        private double green;
        private double blue;

        public Color(int rgb) {
            this.red = (rgb >> 16) & 0xFF;
            this.green = (rgb >> 8) & 0xFF;
            this.blue = rgb & 0xFF;
        }

        public Color(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public double getRed() {
            return red;
        }

        public double getGreen() {
            return green;
        }

        public double getBlue() {
            return blue;
        }
    }
    
    public static boolean overConcentration(Color avgColor){
        if(avgColor.getRed()/avgColor.getBlue()>1.6 || avgColor.getRed()/avgColor.getBlue()<.7){
            return true;
        }
        return false;
    }



    public static double calculateColorDistance(Color c1, Color c2) {
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        double b = c1.getBlue() - c2.getBlue();
        return Math.sqrt(r * r + g * g + b * b);
    }

    public static double calculateAverageDeviation(String path, Color idealMixedColor) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));

            int width = image.getWidth();
            int height = image.getHeight();

            double totalDeviation = 0.0;
            int pixelCount = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    totalDeviation += calculateColorDistance(pixelColor, idealMixedColor);
                    pixelCount++;
                }
            }

            return totalDeviation / pixelCount;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public static Color averageColor(String path) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int pixelCount = 0;
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));

            int width = image.getWidth();
            int height = image.getHeight();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    redSum += pixelColor.getRed();
                    greenSum += pixelColor.getGreen();
                    blueSum += pixelColor.getBlue();
                    pixelCount++;
                }
            }
            return new Color(redSum / pixelCount, greenSum / pixelCount, blueSum / pixelCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double varianceOfDeviation(String path, Color mean) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));

            int width = image.getWidth();
            int height = image.getHeight();

            double variance = 0.0;
            int pixelCount = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    variance += Math.pow(calculateColorDistance(pixelColor, mean), 2);
                    pixelCount++;
                }
            }
            return variance / pixelCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
