package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

public class BOWContainer {
	
	private Map<Tool, int[]> toolsVector;
	
	private int vectorsDim;
	
	public BOWContainer(Map<Tool, int[]> vector, int dim) {
		toolsVector = vector;
		vectorsDim = dim;
	}

	public Map<Tool, int[]> getToolsVector() {
		return toolsVector;
	}

	public void setToolsVector(Map<Tool, int[]> toolsVector) {
		this.toolsVector = toolsVector;
	}

	public int getVectorsDim() {
		return vectorsDim;
	}

	public void setVectorsDim(int vectorsDim) {
		this.vectorsDim = vectorsDim;
	}
}
