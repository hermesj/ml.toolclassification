package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature;

import java.util.ArrayList;
import java.util.List;

import org.tartarus.snowball.ext.germanStemmer;

public class Stems extends WordFeature {
	
	private germanStemmer stemmer;

	public Stems() {
		super();
		this.stemmer = new germanStemmer();
	}

//	public List<String> processWords(String word) {
//		List<String> stemmedList = new ArrayList<>();
//
//		try {
//			stemmer.setCurrent(word);
//			if (stemmer.stem()) {
//				word = stemmer.getCurrent();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		stemmedList.add(word);
//		return stemmedList;
//	}

	public List<String> processWords(List<String> text) {
		List<String> stemmedList = new ArrayList<>();
		for (String word : text) {
			try {
				this.stemmer.setCurrent(word);
				if (this.stemmer.stem()) {
					word = this.stemmer.getCurrent();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			stemmedList.add(word);
		}

		return stemmedList;
	}

}
