package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature;

import de.uni_koeln.spinfo.ml.toolclassification.workflow.FeatureEnum;

public class FeatureFactory {
	
	public WordFeature createFeature (FeatureEnum featureToCreate){
		WordFeature feature = null;
		switch(featureToCreate) {
		case WORD:
			feature = new WordFeature();
			break;
		case LEMMA:
			feature = new Lemmas();
			break;
		case NGRAM:
			feature = new Ngrams();
			break;
		case STEM:
			feature = new Stems();
		default:
			// TODO: Throw exception
			System.out.println("ERROR: Unknown feature");
		}
		return feature;
	}

}
