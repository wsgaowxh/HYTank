package com.tgc.hy;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

public class OpenFile {
	private static final String CHOOSE_FILE_TITLE_ONE = "选择表层图片";
	private static final String CHOOSE_FILE_TITLE_TWO = "选择幻影图片";
	private static final String PIC_FILE_TEXT = "图片文件(*.jpg/*.png)";
	private static final String ALL_FILE = "所有文件(*.*)";
	private static final String PNG= ".png";
	private static final String JPG = ".jpg";
	
	public static String chooseFile(int type) {
		int result = 0;
		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		if (type == 1) {
			fileChooser.setDialogTitle(CHOOSE_FILE_TITLE_ONE);
		} else if (type == 2) {
			fileChooser.setDialogTitle(CHOOSE_FILE_TITLE_TWO);
		}
		fileChooser.setApproveButtonText("确定");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ALL_FILE;
			}
			
			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				if (f.getName().endsWith(PNG) || f.getName().endsWith(JPG)) {
					return true;
				} else {
					return false;
				}
			}
		});
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return PIC_FILE_TEXT;
			}
			
			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				if (f.getName().endsWith(PNG) || f.getName().endsWith(JPG)) {
					return true;
				} else {
					return false;
				}
			}
		});
		if (type == 2) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == result) {
		    	path = fileChooser.getSelectedFile().getPath();
		}
		return path;
	}
}
