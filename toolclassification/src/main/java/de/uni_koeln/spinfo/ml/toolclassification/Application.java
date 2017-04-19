package de.uni_koeln.spinfo.ml.toolclassification;

import java.io.IOException;
import java.util.ArrayList;

import de.uni_koeln.spinfo.ml.toolclassification.workflow.ClassifierEnum;
import de.uni_koeln.spinfo.ml.toolclassification.workflow.FeatureEnum;
import de.uni_koeln.spinfo.ml.toolclassification.workflow.Weight;
import de.uni_koeln.spinfo.ml.toolclassification.workflow.WorkflowHandler;
import de.uni_koeln.spinfo.ml.toolclassification.workflow.Configuration;

/**
 * This is the main class of tools classification.
 * 
 * @author vetemi
 *
 */
public class Application {

	public static void main(String[] args) throws IOException {
		System.out.println("Tool Classifying Application");
		System.out.println("----------------------------");

		ArrayList<Configuration> configList = new ArrayList<Configuration>();
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.NAIVE_BAYES, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.NAIVE_BAYES, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.KNN, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.KNN, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.SUPPORT_VECTOR_MACHINES, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.SUPPORT_VECTOR_MACHINES, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.KSTAR, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.KSTAR, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.J48, FeatureEnum.WORD));
		configList.add(new Configuration(Weight.TFIDF, ClassifierEnum.J48, FeatureEnum.WORD));

		WorkflowHandler wf = new WorkflowHandler();
		// wf.processWorkflow(configList);

		//
		ArrayList<Configuration> configList2 = new ArrayList<Configuration>();
		// configList2.add(new Configuration(Weight.ABSOLUTE,
		// ClassifierEnum.KNN, Feature.WORD));
		// configList2.add(new Configuration(Weight.TFIDF, ClassifierEnum.KNN,
		// Feature.WORD));
		configList2.add(new Configuration(Weight.ABSOLUTE, ClassifierEnum.NEURAL_NETWORK, FeatureEnum.WORD));
		configList2.add(new Configuration(Weight.TFIDF, ClassifierEnum.NEURAL_NETWORK, FeatureEnum.STEM));

		// wf.processWorkflow(configList2);

		wf = new WorkflowHandler(
				new Configuration(Weight.LOGLIKELIHOOD, ClassifierEnum.SUPPORT_VECTOR_MACHINES, FeatureEnum.WORD));
		wf.processWorkflow();
	}

}
