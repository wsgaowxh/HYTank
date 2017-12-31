package com.tgc.hy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class SaveFile {
	private static final String SAVE_FILE_TITLE = "选择保存文件夹";
	
	public static String chooseFile() {
		int result = 0;
		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle(SAVE_FILE_TITLE);
		
		fileChooser.setApproveButtonText("确定");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == result) {
		    	path = fileChooser.getSelectedFile().getPath();
		}
		return path;
	}
	
	public static void saveFile(String savePath, BufferedImage result) {
		File file = new File(savePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
				ImageIO.write(result, "png", file);
				System.out.println("OK!");
			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
