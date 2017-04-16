package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.components;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.spinfo.ml.toolclassification.data.Model;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.data.ToolPart;

public class Cooccurrence {
	private Set<Tool> toolSet;
	Set<ToolPart> toolPartList;
	private int referenceListLength = 3;

	public Cooccurrence(Set<Tool> toolset) {
		this.toolSet = toolset;
	}

	public void countCooccurrence(Tool tool) {
		Set<Tool> coCount = new HashSet<>();
		Map<String, Double> wordMap = tool.getWordMap();
		for (Tool otherTool : this.toolSet) {
			List<String> otherName = otherTool.getFeaturedName();
			if (wordMap.keySet().containsAll(otherName)) {
				coCount.add(otherTool);
			}
		}
		tool.setCooccurrenceCounts(coCount);
	}

	// gibt die anderen Tools, die in ihrem Kontext dieses Tool nennen zurück
	private void getReferencingTools(Tool tool) {
		Map<Tool, Double> referencingToolsWithNumber = new HashMap<>();
		Map<Tool, Double> sortedReferencingToolsWithNumber = new HashMap<>();
		for (Tool otherTool : this.toolSet) {
			if (otherTool != tool) {
				// ein referenzierendes Tool, soll nur aufgenommen werden,
				// wenn
				int countMatches = 0;
				double actualOccurence = 0.;
				for (String pieceOfTitle : tool.getFeaturedName()) {
					if (otherTool.getWordMap().containsKey(pieceOfTitle)) {
						countMatches++;
						actualOccurence += otherTool.getWordMap().get(pieceOfTitle);
					}
				}
				// 1. der gefeaturte Titel aus mehr als 2 Teilwörtern
				// besteht, aber min 2 davon im Kontext wiedergefunden
				// werden, oder
				if (tool.getFeaturedName().size() > 2 && countMatches > 1) {
					referencingToolsWithNumber.put(otherTool, actualOccurence);
					// 2. der gefeaturte Titel aus höchstens 2 Wörtern
					// besteht und davon mindestens eins gefunden wird
				} else if (tool.getFeaturedName().size() <= 2 && countMatches >= 1) {
					referencingToolsWithNumber.put(otherTool, actualOccurence);
				}

			}
		}
		sortedReferencingToolsWithNumber = ChiSquareCalculator.sort(referencingToolsWithNumber);
		List<Tool> allReferencesList = new ArrayList<Tool>(sortedReferencingToolsWithNumber.keySet());

		int len = 0;
		if (allReferencesList.size() < this.referenceListLength) {
			len = allReferencesList.size();
		} else {
			len = this.referenceListLength;
		}
		Set<Tool> bestReferences = new HashSet<>(len);
		for (int i = 0; i < len; i++) {
			bestReferences.add(allReferencesList.get(i));
		}
		tool.setReferencingTools(bestReferences);

	}

	public int enrichContextWithReferencingTools(List<Tool> toolsWoutContext) {
		int contextFound = 0;
		for (Tool toolagain : toolsWoutContext) {
			getReferencingTools(toolagain);
			if (!toolagain.getReferencingTools().isEmpty()) {
				contextFound++;

				for (Tool refTool : toolagain.getReferencingTools()) {
					toolagain.addFeaturedContext(refTool.getFeaturedContext());
					toolagain.setWordMap(refTool.getWordMap());
				}
			}
		}
		return contextFound;
	}

}
