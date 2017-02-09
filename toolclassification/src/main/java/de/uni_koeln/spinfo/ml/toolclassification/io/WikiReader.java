package de.uni_koeln.spinfo.ml.toolclassification.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiReader {
	
	public static Map<String, String> readIndexFile(File indexFile) {
		Map<String, String> index = new HashMap<>();
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(indexFile), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				String[] lineSplit = line.split("\t");
				index.put(lineSplit[0], lineSplit[1]+"\t"+lineSplit[0]);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("--Info: Wikipedia Dump besteht aus "+index.size()+" Artikeln--");
		return index;
	}
	
	
	
	// liest den Artikel mit dem Titel titel aus dem Pfad path aus
		public static List<String> readContextFromIndex(String title, String path) {
			// oder return-Objekt besser ein String? -> was verbraucht weniger
			// Speicher?/geht schneller?
			List<String> context = new ArrayList<>();
			boolean contextFound = false;
			try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"))) {
				while (bReader.ready()) {
					String line = bReader.readLine();
					line = line.trim();
					line = line.toLowerCase();
					if (!line.isEmpty()) {
						if (contextFound == true) {
							context.add(line);
						}
						if (line.startsWith("<doc id") && line.contains(title)) {
							contextFound = true;

						}
						if (line.equals("</doc>")) {
							contextFound = false;
							context.remove(line);
						}

					}

				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			return context;

		}

}
