package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;

/**
 * Base class for all weight calculation components.
 * 
 * @author vetemi
 *
 */
public class AbsoluteWeightCalculator {

	/**
	 * Bag of words model on which calculation runs
	 */
	protected BOWContainer weightedBOW;

	/**
	 * Depending on the actual calculator, this stores base values in context of
	 * all words in corpus
	 */
	protected double[] termDocumentFrequency;
	/**
	 * Depending on the actual calculator, this stores base values in context of
	 * words one document
	 */
	protected double[] totalContextTerms;

	public AbsoluteWeightCalculator(BOWContainer classBOW) {
		weightedBOW = classBOW;
		termDocumentFrequency = new double[classBOW.getVectorsDim()];
		totalContextTerms = new double[classBOW.getToolsVector().size()];
	}

	public BOWContainer getBow() {
		return weightedBOW;
	}

	/**
	 * Calculates the weight for a given bag of words model
	 * 
	 * @return
	 */
	public BOWContainer calculateWeight() {
		return getBow();
	}
}
