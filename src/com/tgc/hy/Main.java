package com.tgc.hy;

import java.awt.image.BufferedImage;

import javax.swing.SwingWorker;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathOne = OpenFile.chooseFile(1);
		String pathTwo = OpenFile.chooseFile(2);
		String savePath = SaveFile.chooseFile();
		WorkThread shoot = new WorkThread(pathOne, pathTwo, savePath);
		shoot.start();
	}

}
