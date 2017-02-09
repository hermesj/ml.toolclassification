package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import java.util.List;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.BagOfWords;


public abstract class AbstractWeightCalculator {
	
	protected List<BagOfWords> modelBOW;
	protected BOWContainer weightedBOW;

	public AbstractWeightCalculator(List<BagOfWords> modelBOW) {
		this.modelBOW = modelBOW;
		createBOWContainer();
	}
	
	public BOWContainer getBow() {
		return weightedBOW;
	}

	public abstract BOWContainer calculateWeight();
	
	private void createBOWContainer() {
		
		weightedBOW = new BOWContainer(vector, dim)
	}
	

}
