package de.uni_koeln.spinfo.ml.toolclassification.classification;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import de.uni_koeln.spinfo.ml.toolclassification.workflow.ClassifierEnum;
import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.trees.J48;
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

	private int dataDim;

	private BOWContainer bow;

	private Classifier classifier;

	private Evaluation evaluation;

	// class values
	private Attribute classValues;

	public WEKAHandler(String string, BOWContainer bow, ArrayList<String> classVal, ClassifierEnum classifier) {
		this.bow = bow;
		wekaAttributes = new ArrayList<Attribute>();
		initialize(string, bow, classVal, classifier);
	}

	/**
	 * initializes the class values and the attributes for all data sets
	 * 
	 * @param bow
	 *            d the dimension of the data sets
	 * @param feature
	 *            the classification values
	 */
	private void initialize(String dataSetName, BOWContainer bow, ArrayList<String> classVal,
			ClassifierEnum classifierEnum) {
		// initialize classVal
		classValues = new Attribute("classVal", classVal);

		// initialize attributes
		for (int i = 0; i < bow.getVectorsDim(); i++) {
			wekaAttributes.add(new Attribute("Attribut" + i));
		}
		wekaAttributes.add(classValues);

		// initialize weka data dimension -> +1 for classification attribute
		dataDim = bow.getVectorsDim() + 1;

		// the data set to process
		dataSet = new Instances(dataSetName, wekaAttributes, bow.getToolsVector().size());
		dataSet.setClassIndex(dataSet.numAttributes() - 1);

		// Pick up classifier
		switch (classifierEnum) {
		case NAIVE_BAYES:
			this.classifier = (Classifier) new NaiveBayes();
			break;
		case KNN:
			this.classifier = (Classifier) new IBk();
			break;
		case SUPPORT_VECTOR_MACHINES:
			this.classifier = (Classifier) new SMO();
			break;
		case J48:
			this.classifier = (Classifier) new J48();
			break;
		case KSTAR:
			this.classifier = (Classifier) new KStar();
			break;
		case NEURAL_NETWORK:
			this.classifier = (Classifier) new MultilayerPerceptron();
			break;
		default:
			// TODO: Throw exception
			System.out.println("ERROR: Unknown Classifier!");
		}
	}

	/**
	 * converts a given tool vector in to a weka data set with respect of the
	 * data dimension and class values
	 * 
	 * @param bow.getToolsVector()
	 *            contains the data to convert
	 * @param dataSetName
	 *            name of the data set
	 * @return weka represented data set
	 */
	public void createWekaModel() {

		// convert and save result in data set
		bow.getToolsVector().forEach((t, v) -> {
			Instance record = new SparseInstance(dataDim);

			for (int i = 0; i < v.length; i++) {
				record.setValue(wekaAttributes.get(i), v[i]);
			}
			// set data and class values
//			String i = t.getParentClassId() + "";
//			record.setValue(classValues, i);
			dataSet.add(record);
		});
	}

	public void processTestClassification() {
		try {
			// Split into training and testing
			this.evaluation = new Evaluation(dataSet);
			evaluation.crossValidateModel(classifier, dataSet, 10, new Random());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showTestResult() {
		System.out.println(evaluation.toSummaryString("\nResults\n======\n", false));
	}

}
