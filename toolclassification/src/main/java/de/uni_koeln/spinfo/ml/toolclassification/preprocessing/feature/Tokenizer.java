package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {

	private SentenceDetectorME sentencer;
	private TokenizerME tokenizer;

	public Tokenizer() throws Exception {
		this.sentencer = new SentenceDetectorME(new SentenceModel(new File("src/main/resources/opennlp/de-sentence.bin")));
		this.tokenizer = new TokenizerME(new TokenizerModel(new File("src/main/resources/opennlp/de-token.bin")));
	}

	
//	public List<String> tokenize(List<String> toTokenize){
//		List<String> result = new ArrayList<>();
//		for(String string : toTokenize){
//			result.addAll(tokenize(string));
//		}
//		return result;
//	}
	public List<String> tokenize(String string) {
		List<String> result = new ArrayList<String>();
		// Klammern hinzugefügt
		Pattern punctuation = Pattern.compile("[,.;:!?&\\)(]");
		// Pattern punctuation = Pattern.compile("\\p{Punct}");

		for (String sentence : sentencer.sentDetect(string)) {
			for (String token : tokenizer.tokenize(sentence)) {
				 token = token.toLowerCase();
				// strip punctuation
				if (!punctuation.matcher(token).find()) {

					Set<String> zwischenresult = new HashSet<>();
					zwischenresult.add(token);
					if (token.contains("/") && !token.equals("/") && !token.equals("//")) {
						Set<String> tmpresult = new HashSet<>();
						for (String tok : zwischenresult) {
							String[] split = tok.split("/");
							int len = split.length;
							// gibt zb start/amadeus/merlin
							for (int i = 0; i < len; i++) {
								// zb bei Lehrer/in "in" kein eigenständiges
								// Token
								if (!split[i].startsWith("in") && (split[i].length() > 1)) {
									tmpresult.add(split[i]);
								}
							}
						}
						zwischenresult.clear();
						zwischenresult = tmpresult;
					}
					if (token.contains("-") && !token.equals("-")) {
						Set<String> tmpresult2 = new HashSet<>();
						for (String tok : zwischenresult) {
							String[] split = tok.split("-");
							int len = split.length;
							for (int i = 0; i < len; i++) {
								if (split[i].length() > 1) {
									tmpresult2.add(split[i]);
								}
							}
						}
						zwischenresult.clear();
						zwischenresult = tmpresult2;
					}

					
				
					for (String tok : zwischenresult) {
						if (!tok.isEmpty()) {
							result.add(tok);
						}
					}

				}
			}
		}

		return result;

	}

	public static void main(String[] args) throws Exception {
		Tokenizer tok = new Tokenizer();
		System.out.println(tok.tokenize(""));
	}

}
