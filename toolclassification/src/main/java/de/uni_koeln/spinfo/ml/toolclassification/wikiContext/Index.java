package de.uni_koeln.spinfo.ml.toolclassification.wikiContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Index {

	public static void makeIndexFile() {
		String fileOutputName= "src/main/resources/sortedWiki/indexMini.txt";
		File writeIn = new File(fileOutputName);
		if(writeIn.exists()){
			writeIn.renameTo(new File("src/main/resources/sortedWiki/INDEX_alt.txt"));
		}
		File f = new File("src/main/resources/extractedWiki");
		
		for (File folder : f.listFiles()) {
			for (String datafile : folder.list()) {
				makeIndex(datafile, folder, fileOutputName);
			}
		}
	}

	private static void makeIndex(String datafile, File folder, String fileOutputName) {
	
		String title = "";
		try (BufferedWriter fWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileOutputName, true), "UTF-8"))) {
			try (BufferedReader bReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(folder.toString() + "/" + datafile), "UTF8"))) {
				while (bReader.ready()) {
					String line = bReader.readLine();
					if (line.startsWith("<doc id")) {
						title = line.split("title=")[1].toLowerCase().replace("\"", "").replace(">", "");
						fWriter.write(title + "\t" + folder.toString() + "/" + datafile+"\n");
					}
					
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			fWriter.flush();
			fWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		
		Index.makeIndexFile();
	}

}
