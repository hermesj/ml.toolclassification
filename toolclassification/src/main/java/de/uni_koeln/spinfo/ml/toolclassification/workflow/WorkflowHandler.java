package de.uni_koeln.spinfo.ml.toolclassification.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.spinfo.ml.toolclassification.classification.*;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.*;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.FeatureFactory;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.WordFeature;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight.AbsoluteWeightCalculator;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight.LogLiklihoodCalculator;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight.TFIDFCalculator;
import de.uni_koeln.spinfo.ml.toolclassification.data.*;
import de.uni_koeln.spinfo.ml.toolclassification.io.TsvParser;

/**
 * Controls work flow of tools classification. Calls methods in the needed orde.
 * Allows to run multiple classification runs via configuration list.
 * 
 * @author vetemi
 *
 */
public class WorkflowHandler {

	/** Stores configuration parameters for classification */
	private Configuration config;

	/** Actual configuration tool handler */
	private WEKAHandler wekaHandler;
	/**
	 * Handles pre processing tasks like featuring, bag of words creation, etc.
	 */
	private Preprocessor preprocessor;

	/** Imports and parses TSV files */
	private TsvParser tsvParser;
	/** File containing all tools */
	private File toolsFile;
	/** File containing Wikipedia articles */
	private File wikiArticlesFile;

	public WorkflowHandler(Configuration config) {
		super();
		this.config = config;
		initialize();
	}

	public WorkflowHandler() {
		super();
		initialize();
	}

	/**
	 * initializes files and parser. Different file locations must be changed
	 * here.
	 */
	private void initialize() {
		tsvParser = new TsvParser();
		toolsFile = new File("src/main/resources/tools.tsv");
		wikiArticlesFile = new File("src/main/resources/GermanWikiArticles.tsv");
	}

	/**
	 * Runs the classification with the previously set parameters.
	 */
	public void processWorkflow() {

		System.out.println("Start Process with the following parameters: ");
		System.out.println("Classifier: " + config.getClassifier());
		System.out.println("Weight: " + config.getWeight());
		System.out.println("Feature: " + config.getFeature());

		// Step 1: Import Data
		importData();
		Map<String, Tool> tools = tsvParser.getTools();
		Map<String, String> wikiArticles = tsvParser.getWikiArticles();

		// Step 2: run actual work flow process
		runClassificationProcess(tools, wikiArticles);

		System.out.println("======================================================");
		System.out.println("========================   END   =====================");
		System.out.println("======================================================");
		System.out.println();
	}

	/**
	 * runs each configuration as a process.
	 * 
	 * @param config
	 *            the configurations for processing the classification
	 */
	public void processWorkflow(List<Configuration> config) {

		// Step 1: Import data for all runs
		importData();
		Map<String, String> wikiArticles = tsvParser.getWikiArticles();
		Map<String, Tool> tools = tsvParser.getTools();

		config.forEach((c) -> {
			setConfig(c);
			System.out.println("Start Process with the following parameters: ");
			System.out.println("Classifier: " + c.getClassifier());
			System.out.println("Weight: " + c.getWeight());
			System.out.println("Feature: " + c.getFeature());

			// Step 2: run actual work flow process
			runClassificationProcess(tools, wikiArticles);

			System.out.println("======================================================");
			System.out.println("========================   END   =====================");
			System.out.println("======================================================");
			System.out.println();

		});
	}

	/**
	 * Actual classification process handling method. Needs the tools map and
	 * Wikipedia articles to run classification with the previously set
	 * configuration.
	 * 
	 * @param tools
	 * @param wikiArticles
	 */
	private void runClassificationProcess(Map<String, Tool> tools, Map<String, String> wikiArticles) {
		// Step 1: create bow model
		BOWContainer bow = createBowModel(tools, wikiArticles);

		// Step 2: calculate weight
		BOWContainer weightedBOW = calculateWeight(bow);

		// Step 3: initialize WEKA Handler
		initWekaHandler(weightedBOW);

		// Step 4: run training and testing
		System.out.println("-- Start classification --");
		wekaHandler.processTestClassification();
		System.out.println("-- End classification --");

		// Step 5: Show results of classification
		wekaHandler.showTestResult();
	}

	/**
	 * Calls importer for Wikipedia articles and tools
	 */
	public void importData() {
		System.out.println("-- Start import --");

		// import tools from TSV
		if (tsvParser == null) {
			tsvParser = new TsvParser();
		}
		tsvParser.parseToolsTsv(toolsFile);
		tsvParser.parseWikiArticles(wikiArticlesFile);
		System.out.println("-- End import --");
	}

	/**
	 * Calls preprocessing component for creating bag of words model
	 * 
	 * @param tools
	 *            tools to process classification
	 * @param wikiArticles
	 *            Wikipedia articles for data enrichment
	 * @return the bag of words based on tools and Wikipedia articles
	 */
	public BOWContainer createBowModel(Map<String, Tool> tools, Map<String, String> wikiArticles) {
		System.out.println("-- Start creating bag of words model --");
		// Create necessary feature
		WordFeature feature = FeatureFactory.createFeature(config.getFeature());

		preprocessor = new Preprocessor(tools, feature, wikiArticles);
		System.out.println("-- End creating bag of words model --");
		return preprocessor.createBowModel();
	}

	/**
	 * Calls weight calculation components, based on the previously built bag of
	 * words
	 * 
	 * @param bow
	 *            the bag of words for tools
	 * @return weighted bag of words
	 */
	public BOWContainer calculateWeight(BOWContainer bow) {
		System.out.println("-- Start calculating weight --");
		AbsoluteWeightCalculator weightCalc = null;
		switch (config.getWeight()) {
		case TFIDF:
			weightCalc = new TFIDFCalculator(bow);
			break;
		case LOGLIKELIHOOD:
			weightCalc = new LogLiklihoodCalculator(bow);
			break;
		case ABSOLUTE:
			weightCalc = new AbsoluteWeightCalculator(bow);
			break;
		default:
			System.out.println("ERROR: Unknown Weight!");

		}
		System.out.println("-- End calculating weight --");
		return weightCalc.calculateWeight();
	}

	/**
	 * Executes initialization tasks for the classification component
	 * 
	 * @param weightedBOW
	 *            the bag of words with weights
	 */
	public void initWekaHandler(BOWContainer weightedBOW) {
		System.out.println("-- Start initializing classification --");
		// Create class attribute list. Can be extracted from TSVParser
		Set<Integer> parentClasses = tsvParser.getParentClasses();

		// transform classValues
		ArrayList<String> classVal = new ArrayList<String>();
		parentClasses.forEach((i) -> {
			String classID = Integer.toString(i);
			classVal.add(classID);
		});

		// initialize WEKA handler
		wekaHandler = new WEKAHandler("ToolContextDataSet", weightedBOW, classVal, config.getClassifier());

		// create model for classifier
		wekaHandler.createWekaModel();
		System.out.println("-- End initializing classification --");
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

}
