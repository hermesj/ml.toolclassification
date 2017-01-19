package de.uni_koeln.spinfo.ml.toolclassification.components.classification;

import java.util.ArrayList;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.core.*;

/**
 * Handles and builds up data sets for weka classifiers
 * 
 * @author vetemi
 *
 */
public class WEKAHandler {

	// represents the dimension of data
	private ArrayList<Attribute> wekaAttributes;

	// class values
	private Attribute classValues;

	/**
	 * @param attrDim
	 * @param classVal
	 */
	public WEKAHandler(int attrDim, ArrayList<String> classVal) {
		wekaAttributes = new ArrayList<Attribute>();
		initialize(attrDim, classVal);
	}

	/**
	 * initializes the class values and the attributes for all data sets
	 * 
	 * @param attrDim
	 *            the dimension of the data sets
	 * @param feature
	 *            the classification values
	 */
	private void initialize(int attrDim, ArrayList<String> classVal) {
		// initialize classVal
		classValues = new Attribute("classVal", classVal);

		// initialize attributes
		wekaAttributes.add(classValues);
		for (int i = 0; i < attrDim; i++) {
			wekaAttributes.add(new Attribute("Attribut" + i));
		}

		System.out.println(wekaAttributes);
	}

	/**
	 * converts a given tool vector in to a wek data set with respect of the
	 * data dimension and class values
	 * 
	 * @param vectorToConvert
	 *            contains the data to convert
	 * @param dataSetName
	 *            name of the data set
	 * @return weka represented data set
	 */
	public Instances convertToolsVectorToWekaModel(Map<Tool, int[]> vectorToConvert, String dataSetName) {

		Instances dataSet = new Instances(dataSetName, wekaAttributes, vectorToConvert.size());

		// necessary for classifiers
		dataSet.setClassIndex(0);

		// set vector elements to instances

		for (Map.Entry<Tool, int[]> entry : vectorToConvert.entrySet()) {
			System.out.println("key" + entry.getKey().getName());
		}

		vectorToConvert.forEach((t, v) -> {
			Instance record = new SparseInstance(v.length + 1);

			// set data and class values
			record.setValue(classValues, t.getParentClassId());
			System.out.println(v.length);
			for (int i = 0; i < v.length; i++) {
				record.setValue(wekaAttributes.get(i + 1), v[i]);
			}
			dataSet.add(record);
		});

		return dataSet;
	}
}
