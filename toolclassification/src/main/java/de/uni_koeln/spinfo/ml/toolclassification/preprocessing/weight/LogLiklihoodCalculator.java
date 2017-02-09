package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import java.util.List;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.BagOfWords;
import de.uni_koeln.spinfo.ml.toolclassification.data.Model;

public class LogLiklihoodCalculator extends AbstractWeightCalculator {

	public LogLiklihoodCalculator(List<BagOfWords> model) {
		super(model);
	}

	@Override
	public BOWContainer calculateWeight() {
		// TODO Auto-generated method stub
		return null;
	}

}
