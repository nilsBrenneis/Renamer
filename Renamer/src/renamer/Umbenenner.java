package renamer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

public class Umbenenner {

	private final String lineSeperator = "\\r?\\n";

	private File[] files;
	private String path;

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public File[] getFilesAsArray(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> fileArLi = new ArrayList<File>();

		for (int i = 0; i < listOfFiles.length; i++) {
			File currentFile = listOfFiles[i];
			if (currentFile.isFile()) {
				fileArLi.add(currentFile);
			}
		}
		File[] fileAr = fileArLi.toArray(new File[fileArLi.size()]);
		return fileAr;
	}

	public String[] renameFiles(String fileNamesText) throws UnevenCountException {
		int countfFileNamesFromJtf = countLineFeeds(fileNamesText);
		String[] fileNames = new String[files.length];

		if (countfFileNamesFromJtf == files.length) {
			fileNames = fileNamesText.split(lineSeperator);

			String absFilePath = files[0].getPath();
			String filePath = absFilePath.substring(0,absFilePath.lastIndexOf(File.separator)) + "\\";
			
			for (int i = 0; i < fileNames.length; i++) {
				files[i].renameTo(new File(filePath + fileNames[i]));
			}
			return fileNames;
		} else {
			throw new UnevenCountException();
		}
	}

	private int countLineFeeds(String str) {
		int counter = str.split("\n").length;
		return counter;
	}

	public boolean containsNotUsableChar(String str) {
		String[] notAllowedChars = new String[] {"/",":","*","\\","?","\"", "<", ">", "|"};
		for (String c : notAllowedChars) {
			if (str.contains(c)){
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		Umbenenner u = new Umbenenner();
		GUI g = new GUI("Umbenenner", u);

		try {
			g.initFrame();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		g.openFolder();
		g.setActionListeners();
	}
}
