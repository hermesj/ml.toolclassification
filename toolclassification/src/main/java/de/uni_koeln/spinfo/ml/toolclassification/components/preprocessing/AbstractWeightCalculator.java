package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

public abstract class AbstractWeightCalculator {
	
	protected BOWContainer bow;

	public AbstractWeightCalculator(BOWContainer bow) {
		this.bow = bow;
	}
	
	public BOWContainer getBow() {
		return bow;
	}

	public abstract BOWContainer calculateWeight();
	
	

}
