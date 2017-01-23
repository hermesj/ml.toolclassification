package de.uni_koeln.spinfo.ml.toolclassification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.spinfo.ml.toolclassification.components.DataImporter;
import de.uni_koeln.spinfo.ml.toolclassification.components.classification.WEKAHandler;
import de.uni_koeln.spinfo.ml.toolclassification.components.crossvalidation.CrossvalidationGroupBuilder;
import de.uni_koeln.spinfo.ml.toolclassification.components.crossvalidation.TrainingTestSets;
import de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing.VectorBuilder;
import de.uni_koeln.spinfo.ml.toolclassification.components.workflow.ClassifierEnum;
import de.uni_koeln.spinfo.ml.toolclassification.components.workflow.Feature;
import de.uni_koeln.spinfo.ml.toolclassification.components.workflow.Weight;
import de.uni_koeln.spinfo.ml.toolclassification.components.workflow.WorkflowHandler;
import de.uni_koeln.spinfo.ml.toolclassification.components.workflow.Configuration;
import de.uni_koeln.spinfo.ml.toolclassification.data.BayesModel;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;


public class Application {

	public static void main(String[] args) throws IOException {
		System.out.println("Tool Classifying Application");
		System.out.println("----------------------------");
		
		ArrayList<Configuration> configList = new ArrayList<Configuration>();
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.NAIVE_BAYES, Feature.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.NAIVE_BAYES, Feature.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.KNN, Feature.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.KNN, Feature.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.SUPPORT_VECTOR_MACHINES, Feature.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.SUPPORT_VECTOR_MACHINES, Feature.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.KSTAR, Feature.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.KSTAR, Feature.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.J48, Feature.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.J48, Feature.WORD));
		
		WorkflowHandler wf = new WorkflowHandler();
//		wf.processWorkflow(configList);
		
//		
		ArrayList<Configuration> configList2 = new ArrayList<Configuration>();
//		configList2.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.KNN, Feature.WORD));
//		configList2.add(new Configuration(Weight.TFIDF, ClassifierEnum.KNN, Feature.WORD));
		configList2.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.NEURAL_NETWORK, Feature.WORD));
		configList2.add(new Configuration(Weight.TFIDF, ClassifierEnum.NEURAL_NETWORK, Feature.WORD));
		
		wf.processWorkflow(configList2);
//		
//		
//		DataImporter di = new DataImporter();
//		di.parseToolsAndClassesFromFile("src/main/resources/data/DatenTools.tsv");
//		
//		List<Tool> tools = new ArrayList<Tool>(di.getTools().values());
//
//
//		
//		
//		int cvgroups = 10;
//		CrossvalidationGroupBuilder<Tool> cvgb = new CrossvalidationGroupBuilder<Tool>(tools, cvgroups);
//		double overallResult = 0.0;
//		for (TrainingTestSets<Tool> tts : cvgb) {
//			//System.out.println(tts.getTrainingSet().size()); //--> Build model / choose classifier
//			
//			//System.out.println(tts.getTestSet().size()); //--> Classify 
//			//--> Evaluate
//			System.out.println();
//			try {
//				performKKNClassification(tts, di.getParentClasses().keySet());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			double singleResult = performBayesClassification(tts);
////			overallResult += singleResult;
//		}
//		//--> Calculate mean
//		overallResult = overallResult/cvgroups;
//		System.out.println("Overall accuracy: " + overallResult);
//		
//		//End
	}
	
	private static void performKKNClassification(TrainingTestSets<Tool> tts, Set<Integer> classValues) throws Exception {
		
//		//training vector
//		Map<Tool,int[]> trainingSet = VectorBuilder.getToolsWithVector(tts.getTrainingSet().subList(0, 3));
//		
//		//test vector
//		Map<Tool,int[]> testSet = VectorBuilder.getToolsWithVector(tts.getTestSet().subList(0, 100));
//		
//		//transform classValues 
//		ArrayList<String> stringClassVal = new ArrayList<String>();
//		classValues.forEach((i) -> stringClassVal.add(i.toString()));
//		
//		WEKAHandler wekaHandler = new WEKAHandler(VectorBuilder.calculateDimension(trainingSet), stringClassVal);
//		
//		//build weka training set
//		Instances wekaTrainingSet = wekaHandler.convertToolsVectorToWekaModel(trainingSet, "training");
//				
//		Classifier cModel = (Classifier)new NaiveBayes();
//		cModel.buildClassifier(wekaTrainingSet);
//		
//		//build wek test set
//		Instances wekaTestSet = wekaHandler.convertToolsVectorToWekaModel(testSet, "training");
//		
//		 Evaluation eTest = new Evaluation(wekaTestSet);
//		 eTest.evaluateModel(cModel, wekaTestSet);
//		 
//		 String strSummary = eTest.toSummaryString();
//		 System.out.println("Summary" + strSummary);
	}

	private static double performBayesClassification(TrainingTestSets<Tool> tts) {
		
		BayesModel bayes = new BayesModel();
		
		
		//Train
		List<Tool> trainingSet = tts.getTrainingSet();
		for (Tool tool : trainingSet) {
			String context = tool.getContext();
			String[] words = context.split("\\W+");
			for (String word : words) {
				bayes.addWordToClass(word, tool.getParentClassId());
			}			
		}
		
		int matches = 0;
		int misses = 0;
		int notClassified = 0;
		
		//Test
		List<Tool> testSet = tts.getTestSet();
		for (Tool tool : testSet) {
			String context = tool.getContext();
			String[] words = context.split("\\W+");
			
			List<String> features = new ArrayList<String>();
			for (String string : words) {
				features.add(string);
			}
			
			int guessedClass = bayes.getClassification(features);
			int realClass = tool.getParentClassId();
			//System.out.println(guessedClass + " " + realClass);
			
			if(guessedClass==0){
				notClassified++;
			}
			else{
				if(guessedClass==realClass){
					matches++;
				}
				else{
					misses++;
				}
			}
		}
			
		System.out.println("Not classified: " + notClassified);
		System.out.println("Correctly classified: " + matches);
		System.out.println("Incorrectly classified: " + misses);
		double accuracy = matches/((double)matches+misses);
		System.out.println("Accuracy: " + accuracy);
		System.out.println();
		return accuracy;
	}

}
