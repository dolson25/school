package cs355.model.image;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by David on 4/17/2017.
 */
public class MyImage extends CS355Image {

    private BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

    public MyImage(int width, int height) {
        super(width, height);
    }

    @Override
    public BufferedImage getImage() {
        return bi;
    }

    public void makeBufferedImage(){

        WritableRaster r = WritableRaster.createBandedRaster(0, getWidth(), getHeight(), 3, new Point(0,0));

        int[] preArray = new int[3];

        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                r.setPixel(x,y,getPixel(x,y, preArray));
            }
        }

        bi.setData(r);
    }

    @Override
    public void edgeDetection() {

        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        int[] results = new int[getWidth() * getHeight()];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){

                if(x != 0 && y !=0 && x != getWidth() - 1 && y != getHeight() - 1)
                {
                    double dx = 0;
                    double dy = 0;

                    //upleft
                    getPixel(x - 1, y - 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx -= hsb[2];
                    dy -= hsb[2];

                    //up
                    getPixel(x , y - 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dy -= hsb[2] * 2;

                    //upright
                    getPixel(x + 1, y - 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx += hsb[2];
                    dy -= hsb[2];

                    //left
                    getPixel(x - 1, y , rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx -= hsb[2] * 2;

                    //right
                    getPixel(x + 1, y , rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx += hsb[2] * 2;

                    //downleft
                    getPixel(x - 1, y + 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx -= hsb[2];
                    dy += hsb[2];

                    //down
                    getPixel(x, y + 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dy += hsb[2] * 2;

                    //downright
                    getPixel(x + 1, y + 1, rgb);
                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                    dx += hsb[2];
                    dy += hsb[2];

                    dx /= 8;
                    dy /= 8;

                    double gradient = Math.max(Math.abs(dx), Math.abs(dy));
                    if(gradient < 0)
                        gradient = 0;
                    else if (gradient > 1)
                        gradient = 1;

                    gradient *= 255;

                    results[getWidth() * y + x] = (int)(gradient);
                }
            }
        }

        //set all the pixels
        for(int x=0; x<getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                rgb[0] = results[getWidth() * y + x];
                rgb[1] = results[getWidth() * y + x];
                rgb[2] = results[getWidth() * y + x];
                setPixel(x, y, rgb);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void sharpen() {
        // Preallocate the array.
        int[] rgb = new int[3];

        //the new pixels, but don't want to mess with the old version until the end
        int[][] pixels = new int[getWidth() * getHeight()][3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){

                int red = 0;
                int green = 0;
                int blue = 0;

                for(int h=-1; h<2; h++){
                    for(int v=-1; v<2; v++){
                        try {
                            getPixel(x + h, y + v, rgb);

                            if(h==0){
                                if(v==0){
                                    red += rgb[0] * 6;
                                    green += rgb[1] * 6;
                                    blue += rgb[2] * 6;
                                } else {
                                    red += rgb[0] * -1;
                                    green += rgb[1] * -1;
                                    blue += rgb[2] * -1;
                                }
                            } else if(v==0){
                                red += rgb[0] * -1;
                                green += rgb[1] * -1;
                                blue += rgb[2] * -1;
                            }

                        } catch (IndexOutOfBoundsException e){}
                    }
                }

                // Set the pixel.
                int redPixel = (int)Math.round(red / 2.0D);
                int greenPixel = (int)Math.round(green / 2.0D);
                int bluePixel = (int)Math.round(blue /2.0D);

                if(redPixel < 0)
                    redPixel = 0;
                else if(redPixel > 255)
                    redPixel = 255;
                if(greenPixel < 0)
                    greenPixel = 0;
                else if(greenPixel > 255)
                    greenPixel = 255;
                if(bluePixel < 0)
                    bluePixel = 0;
                else if(bluePixel > 255)
                    bluePixel = 255;

                pixels[getWidth() * y + x] = new int[]{redPixel, greenPixel, bluePixel};

            }
        }

        //set all the pixels
        for(int x=0; x<getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setPixel(x, y, pixels[getWidth() * y + x]);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void medianBlur() {
        // Preallocate the array.
        int[] rgb = new int[3];

        //the new pixels, but don't want to mess with the old version until the end
        int[][] pixels = new int[getWidth() * getHeight()][3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){

                ArrayList<Integer> red = new ArrayList<Integer>();
                ArrayList<Integer> green = new ArrayList<Integer>();
                ArrayList<Integer> blue = new ArrayList<Integer>();

                for(int h=-1; h<2; h++){
                    for(int v=-1; v<2; v++){
                        try {
                            getPixel(x + h, y + v, rgb);
                            red.add(rgb[0]);
                            green.add(rgb[1]);
                            blue.add(rgb[2]);
                        } catch (IndexOutOfBoundsException e){
                            red.add(0);
                            green.add(0);
                            blue.add(0);
                        }
                    }
                }

                Collections.sort(red);
                Collections.sort(green);
                Collections.sort(blue);

                int[] medianColor = new int[]{red.get(4), green.get(4), blue.get(4)};

                //now find the closest "true" color

                int[] trueColor = new int[3];
                int smallestDistance = Integer.MAX_VALUE;

                for(int h=-1; h<2; h++){
                    for(int v=-1; v<2; v++){
                        try {
                            getPixel(x + h, y + v, rgb);
                            int distance = Math.abs(rgb[0]-medianColor[0]) + Math.abs(rgb[1]-medianColor[1]) + Math.abs(rgb[2] - medianColor[2]);
                            if(distance < smallestDistance){
                                smallestDistance = distance;
                                trueColor[0] = rgb[0];
                                trueColor[1] = rgb[1];
                                trueColor[2] = rgb[2];
                            }

                        } catch (IndexOutOfBoundsException e){

                        }
                    }
                }

                // Set the pixel.
                pixels[getWidth() * y + x] = trueColor;
            }
        }

        //set all the pixels
        for(int x=0; x<getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setPixel(x, y, pixels[getWidth() * y + x]);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void uniformBlur() {

        // Preallocate the array.
        int[] rgb = new int[3];

        //the new pixels, but don't want to mess with the old version until the end
        int[][] pixels = new int[getWidth() * getHeight()][3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){

                int red = 0;
                int green = 0;
                int blue = 0;

                for(int h=-1; h<2; h++){
                    for(int v=-1; v<2; v++){
                        try {
                            getPixel(x + h, y + v, rgb);
                            red += rgb[0];
                            green += rgb[1];
                            blue += rgb[2];
                        } catch (IndexOutOfBoundsException e){}
                    }
                }

                // Set the pixel.
                pixels[getWidth() * y + x] = new int[]{(int)Math.round(red / 9.0D),
                                                       (int)Math.round(green / 9.0D),
                                                               (int)Math.round(blue /9.0D)};

            }
        }

        //set all the pixels
        for(int x=0; x<getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setPixel(x, y, pixels[getWidth() * y + x]);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void grayscale() {

        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){
                // Get the color from the image.
                getPixel(x, y, rgb);

                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

                hsb[1] = 0.0F;


                // Convert back to RGB.
                Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                rgb[0] = c.getRed();

                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void contrast(int amount) {
        double doubleAmount = amount / 1.0;

        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){
                // Get the color from the image.
                getPixel(x, y, rgb);

                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

                // Do whatever operation you’re supposed to do...
                float oldBrightness = hsb[2];

                float newBrightness = (float)((Math.pow((doubleAmount + 100) / 100,4)))*(oldBrightness - 0.5F) + 0.5F;

                if(newBrightness < 0)
                    newBrightness = 0;
                else if(newBrightness > 1)
                    newBrightness = 1;
                hsb[2] = newBrightness;


                // Convert back to RGB.
                Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                rgb[0] = c.getRed();

                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

        makeBufferedImage();
    }

    @Override
    public void brightness(int amount) {

        float floatAmount = amount / 100.0F;

        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        // Do the following for each pixel:

        for(int x=0; x<getWidth(); x++){
            for(int y=0; y<getHeight(); y++){
                // Get the color from the image.
                getPixel(x, y, rgb);

                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

                // Do whatever operation you’re supposed to do...
                float oldBrightness = hsb[2];
                float newBrightness = oldBrightness + floatAmount;
                if(newBrightness < 0)
                    newBrightness = 0;
                else if(newBrightness > 1)
                    newBrightness = 1;
                hsb[2] = newBrightness;


                // Convert back to RGB.
                Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                rgb[0] = c.getRed();

                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

        makeBufferedImage();
    }
}
