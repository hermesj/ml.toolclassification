package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

public abstract class AbstractWeightCalculator {
	
	private BOWContainer bow;

	public abstract BOWContainer calculateWeight();
	
	

}
