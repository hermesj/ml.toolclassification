package de.uni_koeln.spinfo.ml.toolclassification.components;

import java.util.ArrayList;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.core.*;

public class WEKAConverter {

	public Instances convertToolsVectorToWekaModel(Map<Tool, int[]> vectorToConvert, String dataSetName) {
		
		ArrayList<String> classValues = new ArrayList<String>();
		classValues.add("1");
		classValues.add("2");
		classValues.add("3");
		classValues.add("4");
		classValues.add("5");
		classValues.add("6");
		classValues.add("7");
		classValues.add("8");
		
		Attribute wordCount = new Attribute("wordCount");
		Attribute classID = new Attribute("classID", classValues);
		
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		attributeList.add(wordCount);
		attributeList.add(classID);

		Instances dataSet = new Instances(dataSetName, attributeList, 10);

		// necessary for classifier
		dataSet.setClassIndex(1);

		// set vector elements to instances
		vectorToConvert.forEach((t, v) -> {
			for (int i : v) {
				Instance record = new SparseInstance(2);
				record.setValue(attributeList.get(0), i);
				String cID = ""+t.getParentClassId();
				record.setValue(attributeList.get(1), cID);
				dataSet.add(record);
//				record.setDataset(dataSet);
			}
		});
		
		
		return dataSet;
	}
}
