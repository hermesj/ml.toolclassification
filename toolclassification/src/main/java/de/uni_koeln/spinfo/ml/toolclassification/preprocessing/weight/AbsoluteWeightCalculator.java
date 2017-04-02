package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.BagOfWords;


public class AbsoluteWeightCalculator {
	
	protected List<BagOfWords> classBOW;
	protected BOWContainer weightedBOW;
	protected List<String> types;
	
	private int[] termDocumentFrequency;
	private int[] totalContextTerms;

	public AbsoluteWeightCalculator(List<BagOfWords> classBOW) {
		this.classBOW = classBOW;
		weightedBOW = new BOWContainer();
		createBOWContainer();
	}
	
	public BOWContainer getBow() {
		return weightedBOW;
	}

	public BOWContainer calculateWeight() {
		return getBow();
	}
	
	private void createBOWContainer() {
		
		TreeSet<String> sortedTypes = new TreeSet<>();
		classBOW.forEach((tools) -> {
			tools.getTools().forEach((tool) -> {
				sortedTypes.addAll(tool.getWordMap().keySet());
			});
		});
		
		types = new ArrayList<String>(sortedTypes);
		int typeSize = types.size();
		
		weightedBOW.setVectorsDim(typeSize);
		classBOW.forEach((bow) -> {
			bow.getTools().forEach((tool) -> {
				double[] values = new double[typeSize];
				tool.getWordMap().forEach((s,d) -> {
					for (int i = 0; i < typeSize; i++) {
						if (types.get(i).equals(s)) {
}
					}
				});
				weightedBOW.getToolsVector().put(tool, values);
			});
		});
	}
	

}
 