package de.uni_koeln.spinfo.ml.toolclassification.components.workflow;

public class Configuration {
	
	private Weight weight;
	private ClassifierEnum classifier;
	private Feature feature;
	
	public Configuration(Weight weight, ClassifierEnum classifier, Feature feature) {
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
	public Feature getFeature() {
		return feature;
	}
	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}
