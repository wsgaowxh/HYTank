package com.tgc.hy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class WorkThread extends Thread{
	private String pathOne = null;
	private String pathTwo = null;
	private String savePath = null;
	
	public WorkThread(String pathOne, String pathTwo, String savePath) {
		this.pathOne = pathOne;
		this.pathTwo = pathTwo;
		this.savePath = savePath + File.separator + "Tank-" + System.currentTimeMillis() + ".png";
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		BufferedImage picA = Utils.setImageOne(pathOne);
		BufferedImage picB = Utils.setImageTwo(pathTwo);
		List<BufferedImage> targetList = Utils.setPicSize(picA, picB);
		if (targetList.size() != 0) {
			if (targetList.get(0) != null) {
				picA = targetList.get(0);
			}
			if (targetList.get(1) != null) {
				picB = targetList.get(1);
			}
		}
		Utils.setGray(picA);
		Utils.changeColorLevel(picA, true);
		Utils.opposition(picA);
		Utils.setGray(picB);
		Utils.changeColorLevel(picB, false);
		picA = Utils.linearDodge(picA, picB);
		BufferedImage temp = Utils.redChannles(picA);
		picA = Utils.divide(picA, picB);
		BufferedImage result = Utils.masking(picA, temp);
		SaveFile.saveFile(savePath,  result);
	}
}
