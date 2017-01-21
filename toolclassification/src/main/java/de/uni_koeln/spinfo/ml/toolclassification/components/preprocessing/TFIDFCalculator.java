package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

public class TFIDFCalculator extends AbstractWeightCalculator {

	private int[] termDocumentFrequency;
	private int[] totalContextTerms;

	public TFIDFCalculator(BOWContainer bow) {
		super(bow);

		termDocumentFrequency = new int[bow.getVectorsDim()];
		totalContextTerms = new int[bow.getToolsVector().size()];
	}

	@Override
	public BOWContainer calculateWeight() {

		// Step 1: calculate base for tf-idf calculation
		createTFIDFBase();

		// Step 2: actual calculation of tf-idf
		calculateTFIDF();
		
		return getBow();
	}

	private void createTFIDFBase() {

		// counts all the amount of terms in a context (basis for terms
		// frequency
		int contextTermCounter = 0;

		// placeholder for the current term (basis for document frequency)
		int currentDocumentTerm = 0;

		Iterator<Entry<Tool, double[]>> iter = bow.getToolsVector().entrySet().iterator();

		for (int i = 0; i < bow.getToolsVector().size(); i++) {
			double[] nextVector = iter.next().getValue();
			for (int j = 0; j < bow.getVectorsDim(); j++) {
				if (nextVector[j] != 0) {
					currentDocumentTerm = termDocumentFrequency[j];
					currentDocumentTerm++;
					termDocumentFrequency[j] = currentDocumentTerm;

					contextTermCounter += nextVector[j];
					if (j + 1 == bow.getVectorsDim()) {
						totalContextTerms[i] = contextTermCounter;
						contextTermCounter = 0;
					}
				}
			}
		}

	}

	private void calculateTFIDF() {
		Iterator<Entry<Tool, double[]>> iter = bow.getToolsVector().entrySet().iterator();

		int totalTools = bow.getToolsVector().size();
		for (int i = 0; i < bow.getToolsVector().size(); i++) {
			double[] nextVector = iter.next().getValue();
			for (int j = 0; j < bow.getVectorsDim(); j++) {
				if (nextVector[j] != 0) {
					double tf = nextVector[j] / totalContextTerms[i];
					double idf = Math.log(totalTools / termDocumentFrequency[j]);
					nextVector[j] = tf * idf;
				}
			}
		}
	}

}
