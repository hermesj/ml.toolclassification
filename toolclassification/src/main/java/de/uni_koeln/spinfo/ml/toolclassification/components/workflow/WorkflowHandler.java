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

	private Configuration config;

	private DataImporter dataImporter;
	private WEKAHandler wekaHandler;
	private Preprocessor preprocessor;

	public WorkflowHandler(Configuration config) {
		super();
		this.config = config;
	}
	
	public WorkflowHandler() {
		super();
	}

	public void processWorkflow() {
		
		System.out.println("Start Process with the following parameters: ");
		System.out.println("Classifier: " + config.getClassifier());
		System.out.println("Weight: " + config.getWeight());
		System.out.println("Feature: " + config.getFeature());

		// Step 1: Import Data
		importData();
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
		
		System.out.println("======================================================");
		System.out.println("========================   END   =====================");
		System.out.println("======================================================");
		System.out.println();
	}
	
	public void processWorkflow(List<Configuration> config) {
		
		//Step 1: Import data for all runs
		importData();
		List<Tool> tools = new ArrayList<Tool>(dataImporter.getTools().values());
		
		config.forEach((c) -> {
			setConfig(c);
			System.out.println("Start Process with the following parameters: ");
			System.out.println("Classifier: " + c.getClassifier());
			System.out.println("Weight: " + c.getWeight());
			System.out.println("Feature: " + c.getFeature());
			
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
			
			System.out.println("======================================================");
			System.out.println("========================   END   =====================");
			System.out.println("======================================================");
			System.out.println();
			
		});
	}

	private void importData() {
		dataImporter = new DataImporter();
		try {
			dataImporter.parseToolsAndClassesFromFile("src/main/resources/data/DatenTools.tsv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BOWContainer preprocess(List<Tool> tools) {
		preprocessor = new Preprocessor(tools);
		switch (config.getFeature()) {
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
		AbstractWeightCalculator weightCalc = null;
		switch (config.getWeight()) {
		case TFIDF:
			weightCalc = new TFIDFCalculator(bow);
			break;
		case LOGLIKELIHOOD:
			weightCalc = new LogLiklihoodCalculator(bow);
			break;
		case ABSOLUTE:
			// "Absolute" means no weight and in that case just return bow
			return bow;
		default:
			// TODO: Throw exception
			System.out.println("ERROR: Unknown Weight!");

		}
		return weightCalc.calculateWeight();

	}

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
		wekaHandler = new WEKAHandler("ToolContextDataSet", weightedBOW, classVal, config.getClassifier());
		
		// create model for classifier
		wekaHandler.createWekaModel();
	}
	
	public void setConfig(Configuration config) {
		this.config = config;
	}

}
