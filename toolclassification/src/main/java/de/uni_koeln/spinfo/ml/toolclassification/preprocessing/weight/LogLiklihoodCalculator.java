package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import java.util.Iterator;
import java.util.Map.Entry;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

/**
 * Calculates the Log Likelihood weight of a give bag of words model.
 * 
 * @author vetemi
 *
 */
public class LogLiklihoodCalculator extends AbsoluteWeightCalculator {

	private double totalWordsInAllTools = 0.0;

	public LogLiklihoodCalculator(BOWContainer bowContainer) {
		super(bowContainer);
	}

	@Override
	public BOWContainer calculateWeight() {

		// Step 1: calculate base for log likelihood calculation
		createLogLikeBase();

		// Step 2: actual calculation of log likelihood
		calculateLogLikelihood();

		return getBow();
	}

	/**
	 * Creates the base for Log Likelihood calculation, i.e. creates vector for
	 * term document frequency and total word in document frequency.
	 */
	private void createLogLikeBase() {

		// counts all the amount of terms in a context (basis for terms
		// frequency)
		double contextTermCounter = 0.0;

		// placeholder for the current term (basis for document frequency)
		double currentDocumentTerm = 0.0;

		Iterator<Entry<Tool, double[]>> iter = weightedBOW.getToolsVector().entrySet().iterator();

		for (int i = 0; i < weightedBOW.getToolsVector().size(); i++) {
			double[] nextVector = iter.next().getValue();
			for (int j = 0; j < weightedBOW.getVectorsDim(); j++) {
				if (nextVector[j] != 0) {
					currentDocumentTerm = nextVector[j];
					termDocumentFrequency[j] = termDocumentFrequency[j] + currentDocumentTerm;

					contextTermCounter += nextVector[j];
				}
				if (j + 1 == weightedBOW.getVectorsDim()) {
					totalContextTerms[i] = contextTermCounter;
					totalWordsInAllTools += contextTermCounter;
					contextTermCounter = 0;
				}
			}
		}

	}

	/**
	 * Actual calculation of Log Likelihood, based on the given bag of words and
	 * the previously created base vectors.
	 */
	private void calculateLogLikelihood() {
		Iterator<Entry<Tool, double[]>> iter = weightedBOW.getToolsVector().entrySet().iterator();

		int totalTools = weightedBOW.getToolsVector().size();
		for (int i = 0; i < totalTools; i++) {
			double[] nextVector = iter.next().getValue();
			for (int j = 0; j < weightedBOW.getVectorsDim(); j++) {
				if (nextVector[j] != 0) {
					double tf = nextVector[j] / totalContextTerms[i];
					double df = termDocumentFrequency[j] / totalWordsInAllTools;
					nextVector[j] = nextVector[j] * (Math.log(tf / df));
				}
			}
		}
	}

}
