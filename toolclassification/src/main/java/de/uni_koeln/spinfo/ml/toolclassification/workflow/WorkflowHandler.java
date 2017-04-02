package de.uni_koeln.spinfo.ml.toolclassification.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import de.uni_koeln.spinfo.ml.toolclassification.io.WikiReader;

public class WorkflowHandler {

	private Configuration config;

	private WEKAHandler wekaHandler;
	private Preprocessor preprocessor;

	private TsvParser tsvParser;
	private File toolsFile;
	private File wikiArticlesFile;
	private FeatureFactory featureFactory;

	public WorkflowHandler(Configuration config) {
		super();
		this.config = config;
		initialize();
	}

	public WorkflowHandler() {
		super();
		initialize();
	}
	
	private void initialize() {
		tsvParser = new TsvParser();
		toolsFile = new File("src/main/resources/tools.tsv");
		wikiArticlesFile = new File("src/main/resources/germanWikiArticles.tsv");
		featureFactory = new FeatureFactory();
	}

	public void processWorkflow() {
		
		System.out.println("Start Process with the following parameters: ");
		System.out.println("Classifier: " + config.getClassifier());
		System.out.println("Weight: " + config.getWeight());
		System.out.println("Feature: " + config.getFeature());
		
		// Step 1: Import Data
		importData();
		Map<String, Tool> tools = tsvParser.getTools();

		// Step 2: create model
		Model model = createModel(tools);

		// Step 3: calculate weight
		BOWContainer weightedBOW = calculateWeight(model.getBagOfWordList());

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
		Map<String, Tool> tools = tsvParser.getTools();
		
		config.forEach((c) -> {
			setConfig(c);
			System.out.println("Start Process with the following parameters: ");
			System.out.println("Classifier: " + c.getClassifier());
			System.out.println("Weight: " + c.getWeight());
			System.out.println("Feature: " + c.getFeature());
			
			// Step 2: Create Model
			Model model = createModel(tools);
			
			System.out.println(model);

			// Step 3: calculate weight
//			BagOfWords weightedBOWList = calculateWeight(model.getBagOfWordList());

//			// Step 4: initialize WEKA Handler
//			initWekaHandler(weightedBOWList);

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
		
		// import tools from TSV
		if (tsvParser == null) {
			tsvParser = new TsvParser();
			System.out.println("-- Info: Tsv eingelesen --");
		}
		tsvParser.parseToolsTsv(toolsFile);
		tsvParser.parseWikiArticles(wiki)
		
		// read index list
		wikipediaIndex = WikiReader.readIndexFile(indexFile);
	}

	private Model createModel(Map<String, Tool> tools) {
		
		// Create necessary feature
		WordFeature feature = featureFactory.createFeature(config.getFeature());
		
		preprocessor = new Preprocessor(tools, feature, wikipediaIndex);
		return preprocessor.createModel();

	}

	private BOWContainer calculateWeight(List<BagOfWords> bowList) {
		
		AbsoluteWeightCalculator weightCalc = null;
		switch (config.getWeight()) {
		case TFIDF:
			weightCalc = new TFIDFCalculator(bowList);
			break;
		case LOGLIKELIHOOD:
			weightCalc = new LogLiklihoodCalculator(bowList);
			break;
		case ABSOLUTE:
			weightCalc = new AbsoluteWeightCalculator(bowList);
			break;
		default:
			// TODO: Throw exception
			System.out.println("ERROR: Unknown Weight!");

		}
		return weightCalc.calculateWeight();

	}

	private void initWekaHandler(BOWContainer weightedBOW) {
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
	}
	
	public void setConfig(Configuration config) {
		this.config = config;
	}

}
