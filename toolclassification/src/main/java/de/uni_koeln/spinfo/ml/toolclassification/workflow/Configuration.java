package de.uni_koeln.spinfo.ml.toolclassification.workflow;

/**
 * Container for different configurations
 * @author vetemi
 *
 */
public class Configuration {
	
	private Weight weight;
	private ClassifierEnum classifier;
	private FeatureEnum feature;
	
	public Configuration(Weight weight, ClassifierEnum classifier, FeatureEnum feature) {
		super();
		this.weight = weight;
		this.classifier = classifier;
		this.feature = feature;
	}
	
	public Weight getWeight() {
		return weight;
	}
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	public ClassifierEnum getClassifier() {
		return classifier;
	}
	public void setClassifier(ClassifierEnum classifier) {
		this.classifier = classifier;
	}
	public FeatureEnum getFeature() {
		return feature;
	}
	public void setFeature(FeatureEnum feature) {
		this.feature = feature;
	}

}
