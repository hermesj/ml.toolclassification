package de.uni_koeln.spinfo.ml.toolclassification.components.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.components.*;
import de.uni_koeln.spinfo.ml.toolclassification.components.classification.*;
import de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing.*;
import de.uni_koeln.spinfo.ml.toolclassification.data.*;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;

public class WorkflowHandler {

	private Weight weight;
	private Classifier classifier;
	private Feature feature;

	private DataImporter dataImporter;
	private WEKAHandler wekaHandler;
	private Preprocessor preprocessor;

	public WorkflowHandler(Weight weight, Classifier classifier, Feature feature) {
		super();
		this.weight = weight;
		this.classifier = classifier;
		this.feature = feature;
	}

	public void processWorkflow() {

		// Step 1: Import Data
		dataImporter = new DataImporter();
		try {
			dataImporter.parseToolsAndClassesFromFile("src/main/resources/data/DatenTools.tsv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Tool> tools = new ArrayList<Tool>(dataImporter.getTools().values());

		// Step 2: preprocess Context
		BOWContainer bow = preprocess(tools);

		// Step 3: calculate weight
		BOWContainer weightedBOW = calculateWeight(bow);

		// Step 4: initialize WEKA Handler
		initWekaHandler(weightedBOW);

		// Step 5: run training and testing
		wekaHandler.processTestClassification();

		// Step 6: Show results of classification
		wekaHandler.showTestResult();

	}

	private BOWContainer preprocess(List<Tool> tools) {
		preprocessor = new Preprocessor(tools);
		switch (feature) {
		case WORD:
			return preprocessor.createWordVector();
		case LEMMA:
			return preprocessor.createLemmaVector();
		case NGRAM:
			return preprocessor.createNGramVector();
		default:
			// TODO: Throw exception
			System.out.println("ERROR: Unknown feature");
			return null;
		}
	}

	private BOWContainer calculateWeight(BOWContainer bow) {
		AbstractWeightCalculator weightCalc;
		switch (weight) {
		case TFIDF:
			weightCalc = new TFIDFCalculator(bow);
			break;
		case LOGLIKELIHOOD:
			weightCalc = new LogLiklihoodCalculator(bow);
			break;
		case ABSOLUTE:
		default:
			// "Absolute" means no weight and in that case just return bow
			return bow;
		}
		return weightCalc.calculateWeight();

	}
s
	private void initWekaHandler(BOWContainer weightedBOW) {
		// Create class attribute list. Can be extracted from DataImporter
		Map<Integer, ToolParentClass> parentClasses = dataImporter.getParentClasses();

		// transform classValues
		ArrayList<String> classVal = new ArrayList<String>();
		parentClasses.forEach((i, parent) -> {
			String classID = Integer.toString(parent.getClassID());
			classVal.add(classID);
		});

		// initialize WEKA handler
		wekaHandler = new WEKAHandler("ToolContextDataSet", weightedBOW, classVal, classifier);
	}

}
