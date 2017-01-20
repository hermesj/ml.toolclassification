package de.uni_koeln.spinfo.ml.toolclassification.components.workflow;

import java.util.List;

import de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing.Preprocessor;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;

public class WorkflowHandler {
	
	private Weight weight;
	
	private Classifier classifier;
	
	private Feature feature;

	public WorkflowHandler(Weight weight, Classifier classifier, Feature features) {
		super();
		this.weight = weight;
		this.classifier = classifier;
		this.feature = feature;
	}
	
	public void processWorkflow(List<Tool> tools) {
		
		//Step 1: preprocess Context
		BOWContainer bow = preprocess(tools);
		
		// Step 2: calculate weight
		BOWContainer weightedBOW = calculateWeight(bow);
		
	}

	private void calculateWeight() {
		switch(weight) {
		
		}
		
	}

	private BOWContainer preprocess(List<Tool> tools) {
		Preprocessor preprocessor = new Preprocessor(tools);
		switch(feature) {
		case WORD:
			return preprocessor.preprocessWordFeature();
		case LEMMA:
			return preprocessor.preprocessLemmaFeature();
		case NGRAM:
			return preprocessor.preprocessNGramFeature();
		default:
			//TODO: Throw exception
			System.out.println("ERROR: Unknown feature");
			return null;
		}

		
	}

}
