package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordFeature {

	protected boolean needsTokenizing;

	public WordFeature() {
		super();
		this.needsTokenizing = true;
	}
	
	public List<String> processWords(List<String> text) {
		return text;
	}
	
	public List<String> filterStopwords(List<String> featuredText){
		List<String> stopwords = new ArrayList<>();
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/stopwords.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				stopwords.add(line);
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		List<String> featuredStopwords = processWords(stopwords);
		featuredText.removeAll(featuredStopwords);
		return featuredText;
	}
	
	public boolean needsTokenizing() {
		return needsTokenizing;
	}

	public void setNeedsTokenizing(boolean needsTokenizing) {
		this.needsTokenizing = needsTokenizing;
	}

	

}
