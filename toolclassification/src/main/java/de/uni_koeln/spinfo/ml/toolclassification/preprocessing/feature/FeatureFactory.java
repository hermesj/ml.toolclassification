package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature;

import java.util.ArrayList;
import java.util.List;

import de.uni_koeln.spinfo.ml.toolclassification.workflow.FeatureEnum;

/**
 * Feature factory for singleton creation of feature components
 * 
 * @author hanna, vetemi
 *
 */
public class FeatureFactory {

	private static List<WordFeature> allFeatures;

	private static WordFeature wordFeature;
	private static Lemmas lemmaFeature;
	private static Ngrams ngramFeature;
	private static Stems stemFeature;

	/**
	 * Returns a feature based on the given FeatureEnum. Always as Singleton.
	 * 
	 * @param featureToCreate
	 * @return Feature component as Singleton
	 */
	public static WordFeature createFeature(FeatureEnum featureToCreate) {
		WordFeature feature = null;
		switch (featureToCreate) {
		case WORD:
			feature = getWordFeature();
			break;
		case LEMMA:
			feature = getLemmaFeature();
			break;
		case NGRAM:
			feature = getNgramFeature();
			break;
		case STEM:
			feature = getStemFeature();
			break;
		default:
			System.out.println("ERROR: Unknown feature");
		}
		return feature;
	}

	/**
	 * Collects all Singleton feature components
	 * @return List of all feature components as Singletons
	 */
	public static List<WordFeature> getAllFeatures() {
		if (allFeatures != null) {
			return allFeatures;
		}
		allFeatures = new ArrayList<>();
		allFeatures.add(getNgramFeature());
		allFeatures.add(getLemmaFeature());
		allFeatures.add(getStemFeature());

		return allFeatures;
	}

	public static WordFeature getWordFeature() {
		if (wordFeature == null) {
			wordFeature = new WordFeature();
		}
		return wordFeature;
	}

	public static Lemmas getLemmaFeature() {
		if (lemmaFeature == null) {
			lemmaFeature = new Lemmas();
		}
		return lemmaFeature;
	}

	public static Ngrams getNgramFeature() {
		if (ngramFeature == null) {
			ngramFeature = new Ngrams();
		}
		return ngramFeature;
	}

	public static Stems getStemFeature() {
		if (stemFeature == null) {
			stemFeature = new Stems();
		}
		return stemFeature;
	}

}
