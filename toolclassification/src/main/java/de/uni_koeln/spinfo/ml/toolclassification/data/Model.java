package de.uni_koeln.spinfo.ml.toolclassification.data;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private List<BagOfWords> bowList;
	protected String feature;

	public Model(String feature) {
		this.feature = feature;
		bowList = new ArrayList<>();
	}

	public void addBoW(BagOfWords bow) {
		if (this.bowList.contains(bow)) {
			this.bowList.remove(bow);
		}
		this.bowList.add(bow);
	}

	public List<BagOfWords> getBagOfWordList() {
		return this.bowList;
	}

	public void addToolToBagOfWordsWithID(Tool tool) {
		int toolID = tool.getToolSub().getToolPart().getID();
		boolean found = false;
		for (BagOfWords bowTmp : this.bowList) {
			if (bowTmp.getID() == toolID) {
				found = true;
				bowTmp.addTool(tool);
//				ToolSub ts = bowTmp.getToolSubWithId(tool.getToolSub().getID());
//				ts.addTool(tool);
//				bowTmp.addToolSub(ts);
//				bowTmp.addToolSub(tool.getToolSub());
			}
		}
		if(found == false){
			BagOfWords bow = new BagOfWords(toolID);
			bow.addTool(tool);
//			ToolSub ts = tool.getToolSub();
//			ts.addTool(tool);
//			bow.addToolSub(tool.getToolSub());
			this.bowList.add(bow);
		}
		
	}

	public String getFeature() {
		return this.feature;
	}



}
