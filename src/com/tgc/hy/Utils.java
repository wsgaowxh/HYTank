package com.tgc.hy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Utils {
	// 亮、暗色阶映射表
	private static int[] mLightColorTable, mDarkColorTable;

	public static BufferedImage getImage(String path) {
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (fis != null) {
				return ImageIO.read(fis);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage setImageOne(String pathOne) {
		return getImage(pathOne);
	}

	public static BufferedImage setImageTwo(String pathTwo) {
		return getImage(pathTwo);
	}
	
	// 图片大小不同时设置大小
	public static List<BufferedImage> setPicSize(BufferedImage targetTop, BufferedImage targetBottom) {
		int topWidth = targetTop.getWidth(), topHeight = targetTop.getHeight();
		int bottomWidth = targetBottom.getWidth(), bottomHeight = targetBottom.getHeight();
		int width = 0, height = 0;
		width = topWidth - bottomWidth;
		height = topHeight - bottomHeight;
		List<BufferedImage> targetList = new ArrayList<>();
		if (width > 0 && height > 0) {
			BufferedImage result = new BufferedImage(targetBottom.getWidth() + width, targetBottom.getHeight() + height,
					BufferedImage.TYPE_INT_ARGB);
			targetList.add(null);
			targetList.add(setPicHelper(targetBottom, result, targetBottom.getWidth() + width, targetBottom.getHeight() + height));
		} else if (width < 0 && height < 0) {
			BufferedImage result = new BufferedImage(targetTop.getWidth() - width, targetTop.getHeight() - height,
					BufferedImage.TYPE_INT_ARGB);
			targetList.add(setPicHelper(targetTop, result, targetTop.getWidth() - width, targetTop.getHeight() - height));
			targetList.add(null);
		} else if (width > 0 && height < 0) {
			BufferedImage resultBottom = new BufferedImage(targetBottom.getWidth() + width, targetBottom.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			BufferedImage resultTop = new BufferedImage(targetTop.getWidth(), targetTop.getHeight() - height,
					BufferedImage.TYPE_INT_ARGB);
			targetList.add(setPicHelper(targetTop, resultTop, targetTop.getWidth(), targetTop.getHeight() - height));
			targetList.add(setPicHelper(targetBottom, resultBottom, targetBottom.getWidth() + width, targetBottom.getHeight()));
		} else if (width < 0 && height > 0) {
			BufferedImage resultBottom = new BufferedImage(targetBottom.getWidth(), targetBottom.getHeight() + height,
					BufferedImage.TYPE_INT_ARGB);
			BufferedImage resultTop = new BufferedImage(targetTop.getWidth() - width, targetTop.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			targetList.add(setPicHelper(targetTop, resultTop, targetTop.getWidth() - width, targetTop.getHeight()));
			targetList.add(setPicHelper(targetBottom, resultBottom, targetBottom.getWidth(), targetBottom.getHeight() + height));
		}
		return targetList;
	}
	
	private static BufferedImage setPicHelper(BufferedImage src, BufferedImage result, int width, int height) {
		int[] targetPixels = new int[width * height];
		getBitmapPixelColor(src, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				targetPixels[x + y * width] = getIntFromColor(a, r, g, b);
			}
		});
		result.setRGB(0, 0, width, height, targetPixels, 0, width);
		return result;
	}

	//去色
	public static void setGray(BufferedImage target) {
		final int width = target.getWidth(), height = target.getHeight();
		int[] targetPixels = new int[width * height];
		getBitmapPixelColor(target, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				int gray = (r + g + b) / 3;
				targetPixels[x + y * width] = getIntFromColor(a, gray, gray, gray);
			}
		});
		target.setRGB(0, 0, width, height, targetPixels, 0, width);
	}

	//红色通道
	public static BufferedImage redChannles(BufferedImage target) {
		final int width = target.getWidth(), height = target.getHeight();
		int[] targetPixels = new int[width * height];
		final BufferedImage result = new BufferedImage(target.getWidth(), target.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		getBitmapPixelColor(target, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				targetPixels[x + y * width] = getIntFromColor(r, r, g, b);
			}
		});
		result.setRGB(0, 0, width, height, targetPixels, 0, width);
		// 理论可以省略
//		 opposition(result);
//		 for (int i = 0; i < 5; i++) {
//			 changeColorLevel(result, false);
//		 }
		return result;
	}

	//蒙版
	public static BufferedImage masking(BufferedImage src, BufferedImage target) {
		final int width = src.getWidth(), height = src.getHeight();
		int[] srcPixels = new int[width * height];
		final BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		getBitmapPixelColor(src, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				int dstA, dstPixelColor;
				dstPixelColor = target.getRGB(x, y);
				dstA = (dstPixelColor & 0xFF000000) >> 24;
				srcPixels[x + y * width] = getIntFromColor(dstA, r, g, b);
			}
		});
		result.setRGB(0, 0, width, height, srcPixels, 0, width);
		return result;
	}

	//反相
	public static void opposition(BufferedImage target) {
		final int width = target.getWidth(), height = target.getHeight();
		int[] targetPixels = new int[width * height];
		getBitmapPixelColor(target, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				int max = 255;
				targetPixels[x + y * width] = getIntFromColor(a, max - r, max - g, max - b);
			}
		});
		target.setRGB(0, 0, width, height, targetPixels, 0, width);
	}

	//划分
	public static BufferedImage divide(BufferedImage src, BufferedImage target) {
		final int width = src.getWidth(), height = src.getHeight();
		int[] srcPixels = new int[width * height];
		final BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		getBitmapPixelColor(src, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				int dstR, dstG, dstB, dstPixelColor, resultA, resultR, resultG, resultB;
				dstPixelColor = target.getRGB(x, y);
				dstR = (dstPixelColor & 0xFF0000) >> 16;
				dstG = (dstPixelColor & 0xFF00) >> 8;
				dstB = (dstPixelColor & 0xFF);
				resultA = 255;
				resultR = (int) (255 / (((float) r / (float) dstR)));
				resultG = (int) (255 / (((float) g / (float) dstG)));
				resultB = (int) (255 / (((float) b / (float) dstB)));
				srcPixels[x + y * width] = getIntFromColor(resultA, resultR, resultG, resultB);
			}
		});
		result.setRGB(0, 0, width, height, srcPixels, 0, width);
		return result;
	}

	//线性减淡
	public static BufferedImage linearDodge(BufferedImage src, BufferedImage target) {
		final int width = src.getWidth(), height = src.getHeight();
		int[] srcPixels = new int[width * height];
		final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		getBitmapPixelColor(src, new PixelColorHandler() {
			
			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				int dstR, dstG, dstB, dstPixelColor, resultA, resultR, resultG, resultB;
				dstPixelColor = target.getRGB(x, y);
				dstR = (dstPixelColor & 0xFF0000) >> 16;
				dstG = (dstPixelColor & 0xFF00) >> 8;
				dstB = (dstPixelColor & 0xFF);
				resultA = 255;
				resultR = r + dstR;
				resultG = g + dstG;
				resultB = b + dstB;
				// 防止色值溢出
				if (resultR > 255)
					resultR = 255;
				if (resultG > 255)
					resultG = 255;
				if (resultB > 255)
					resultB = 255;
				srcPixels[x + y * width] = getIntFromColor(resultA, resultR, resultG, resultB);
			}
		});
		result.setRGB(0, 0, width, height, srcPixels, 0, width);
		return result;
	}

	// 调整色阶, true->up, false->down
	public static void changeColorLevel(BufferedImage target, boolean isToLight) {
		final int width = target.getWidth(), height = target.getHeight();
		int[] targetPixels = new int[width * height];
		int[] table = isToLight ? getLightColorTable() : getDarkColorTable();
		getBitmapPixelColor(target, new PixelColorHandler() {

			@Override
			public void onHandle(int x, int y, int a, int r, int g, int b) {
				// TODO Auto-generated method stub
				targetPixels[x + y * width] = getIntFromColor(a, table[r], table[g], table[b]);
			}
		});
		target.setRGB(0, 0, width, height, targetPixels, 0, width);
	}

	private static int[] getLightColorTable() {
		if (mLightColorTable == null)
			initLightColorTable();
		return mLightColorTable;
	}

	private static int[] getDarkColorTable() {
		if (mDarkColorTable == null)
			initDarkColorTable();
		return mDarkColorTable;
	}

	private static void getBitmapPixelColor(BufferedImage target, PixelColorHandler handler) {
		int a, r, g, b, pixelColor;
		for (int y = 0; y < target.getHeight(); y++) {
			for (int x = 0; x < target.getWidth(); x++) {
				pixelColor = target.getRGB(x, y);
				a = (pixelColor & 0xFF000000) >> 24;
				r = (pixelColor & 0xFF0000) >> 16;
				g = (pixelColor & 0xFF00) >> 8;
				b = (pixelColor & 0xFF);
				handler.onHandle(x, y, a, r, g, b);
			}
		}
	}

	// Color.argb
	private static int getIntFromColor(int alpha, int red, int green, int blue) {
		alpha = (alpha << 24) & 0xFF000000;
		red = (red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
		green = (green << 8) & 0x0000FF00; // Shift Green 8-bits and mask out other stuff
		blue = blue & 0x000000FF; // Mask out anything not blue.

		return 0x00000000 | alpha | red | green | blue;
//		int color = ((red*256) + green) * 256 + blue;
//		if (color > 8388608) {
//			color = color - 16777216;
//		}
//		return color;
	}

	private static void initLightColorTable() {
		// 输出色阶 120 ～ 255 的映射表
		// 由 getColorLevelTable(120, 255); 得来
		mLightColorTable = new int[] { 120, 120, 121, 121, 122, 122, 123, 123, 124, 124, 125, 125, 126, 126, 127, 127,
				128, 128, 129, 129, 130, 130, 131, 132, 132, 133, 133, 134, 134, 135, 135, 136, 136, 137, 137, 138, 138,
				139, 139, 140, 140, 141, 142, 142, 143, 143, 144, 144, 145, 145, 146, 146, 147, 147, 148, 148, 149, 149,
				150, 150, 151, 152, 152, 153, 153, 154, 154, 155, 155, 156, 156, 157, 157, 158, 158, 159, 159, 160, 161,
				161, 162, 162, 163, 163, 164, 164, 165, 165, 166, 166, 167, 167, 168, 168, 169, 170, 170, 171, 171, 172,
				172, 173, 173, 174, 174, 175, 175, 176, 176, 177, 177, 178, 179, 179, 180, 180, 181, 181, 182, 182, 183,
				183, 184, 184, 185, 185, 186, 186, 187, 188, 188, 189, 189, 190, 190, 191, 191, 192, 192, 193, 193, 194,
				194, 195, 195, 196, 197, 197, 198, 198, 199, 199, 200, 200, 201, 201, 202, 202, 203, 203, 204, 205, 205,
				206, 206, 207, 207, 208, 208, 209, 209, 210, 210, 211, 211, 212, 212, 213, 214, 214, 215, 215, 216, 216,
				217, 217, 218, 218, 219, 219, 220, 220, 221, 222, 222, 223, 223, 224, 224, 225, 225, 226, 226, 227, 227,
				228, 228, 229, 229, 230, 231, 231, 232, 232, 233, 233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 239,
				239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 245, 245, 246, 247, 247, 248, 248, 249, 249, 250,
				250, 251, 251, 252, 252, 253, 253, 254, 255, };
	}

	private static void initDarkColorTable() {
		// 输出色阶 0 ～ 135 的映射表
		// 由 getColorLevelTable(0, 135); 得来
		mDarkColorTable = new int[] { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 12, 12,
				13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21, 22, 22, 23, 23, 24, 24, 25, 25, 26,
				26, 27, 27, 28, 28, 29, 29, 30, 30, 31, 32, 32, 33, 33, 34, 34, 35, 35, 36, 36, 37, 37, 38, 38, 39, 39,
				40, 41, 41, 42, 42, 43, 43, 44, 44, 45, 45, 46, 46, 47, 47, 48, 48, 49, 50, 50, 51, 51, 52, 52, 53, 53,
				54, 54, 55, 55, 56, 56, 57, 57, 58, 59, 59, 60, 60, 61, 61, 62, 62, 63, 63, 64, 64, 65, 65, 66, 66, 67,
				68, 68, 69, 69, 70, 70, 71, 71, 72, 72, 73, 73, 74, 74, 75, 75, 76, 77, 77, 78, 78, 79, 79, 80, 80, 81,
				81, 82, 82, 83, 83, 84, 85, 85, 86, 86, 87, 87, 88, 88, 89, 89, 90, 90, 91, 91, 92, 92, 93, 94, 94, 95,
				95, 96, 96, 97, 97, 98, 98, 99, 99, 100, 100, 101, 102, 102, 103, 103, 104, 104, 105, 105, 106, 106,
				107, 107, 108, 108, 109, 109, 110, 111, 111, 112, 112, 113, 113, 114, 114, 115, 115, 116, 116, 117, 117,
				118, 119, 119, 120, 120, 121, 121, 122, 122, 123, 123, 124, 124, 125, 125, 126, 127, 127, 128, 128, 129,
				129, 130, 130, 131, 131, 132, 132, 133, 133, 134, 135, };
	}

	private static int[] getColorLevelTable(int outputMin, int outputMax) {
		int[] data = new int[256];
		int inputMin = 0, inputMiddle = 128, inputMax = 255;
		if (outputMin < 0)
			outputMin = 0;
		if (outputMin > 255)
			outputMin = 255;
		if (outputMax < 0)
			outputMax = 0;
		if (outputMax > 255)
			outputMax = 255;
		for (int index = 0; index <= 255; index++) {
			double temp = index - inputMin;
			if (temp < 0) {
				temp = outputMin;
			} else if (temp + inputMin > inputMax) {
				temp = outputMax;
			} else {
				double gamma = Math.log(0.5) / Math.log((double) (inputMiddle - inputMin) / (inputMax - inputMin));
				temp = outputMin + (outputMax - outputMin) * Math.pow((temp / (inputMax - inputMin)), gamma);
			}
			if (temp > 255)
				temp = 255;
			else if (temp < 0)
				temp = 0;
			data[index] = (int) temp;
		}
		return data;
	}

}
