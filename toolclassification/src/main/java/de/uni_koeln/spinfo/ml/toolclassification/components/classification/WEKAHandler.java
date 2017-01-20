package de.uni_koeln.spinfo.ml.toolclassification.components.classification;

import java.util.ArrayList;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing.BOWContainer;
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

	private Instances dataSet;

	// class values
	private Attribute classValues;

	private int dataDim;

	/**
	 * @param attrDim
	 * @param classVal
	 */
	public WEKAHandler(int attrDim, ArrayList<String> classVal) {
		wekaAttributes = new ArrayList<Attribute>();
	}

	/**
	 * initializes the class values and the attributes for all data sets
	 * 
	 * @param bow
	 *            d the dimension of the data sets
	 * @param feature
	 *            the classification values
	 */
	private void initialize(String dataSetName, BOWContainer bow, ArrayList<String> classVal) {
		// initialize classVal
		classValues = new Attribute("classVal", classVal);

		// initialize attributes
		wekaAttributes.add(classValues);
		for (int i = 0; i < bow.getVectorsDim(); i++) {
			wekaAttributes.add(new Attribute("Attribut" + i));
		}

		// initialize weka data dimension -> +1 for classification attribute
		dataDim = bow.getVectorsDim() + 1;
		
		// the data set to process
		dataSet = new Instances(dataSetName, wekaAttributes, bow.getToolsVector().size());

		// necessary for classifiers
		dataSet.setClassIndex(0);
	}

	/**
	 * converts a given tool vector in to a wek data set with respect of the
	 * data dimension and class values
	 * 
	 * @param bow.getToolsVector()
	 *            contains the data to convert
	 * @param dataSetName
	 *            name of the data set
	 * @return weka represented data set
	 */
	public void createWekaModel(String dataSetName, BOWContainer bow, ArrayList<String> classVal) {

		// Step 1: initialize data for conversion
		initialize(dataSetName, bow, classVal);

		// Step 2: convert and save result in data set
		bow.getToolsVector().forEach((t, v) -> {
			Instance record = new SparseInstance(dataDim);

			// set data and class values
			record.setValue(classValues, t.getParentClassId());
			for (int i = 0; i < v.length; i++) {
				record.setValue(wekaAttributes.get(i + 1), v[i]);
			}
			dataSet.add(record);
		});
	}
	
	
}
