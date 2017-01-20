package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.uni_koeln.spinfo.ml.toolclassification.components.Tokenizer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.core.stemmers.Stemmer;

/**
 * @author vetemi
 *
 */
public class Preprocessor {

	private Tokenizer tokenizer;

	private Stemmer stemmer;

	private ArrayList<Tool> tools;

	private List<String> referenceVector;

	public Preprocessor(List<Tool> tools) {
		referenceVector = new ArrayList<String>();
		initialize(tools);
	}

	private void initialize(List<Tool> tools) {
		// Clean tool set, i.e. remove all tools without context
		tools.forEach((tool) -> {
			// add only tools with context to list
			if (tool.getContext() != null) {
				this.tools.add(tool);
			}
		});
	}

	public BOWContainer createWordVector() {
		// Create reference vector which holds all characters
		List<String> typesVec = getTokenizedTypesVector(tokenizer);

		// calculate vector
		Map<Tool, int[]> vector = buildVectors(tools, typesVec);

		// Create bag of words container for storing vectors and their dimension
		return new BOWContainer(vector, typesVec.size());
	}

	public BOWContainer createLemmaVector() {
		return null;
		// TODO Auto-generated method stub

	}

	public BOWContainer createNGramVector() {
		return null;
		// TODO Auto-generated method stub
	}

	private List<String> getTokenizedTypesVector(Tokenizer tokenizer) {
		// Tree sets are ordered
		Set<String> types = new TreeSet<String>();

		// create reference vector
		tools.forEach((tool) -> {
			// TODO: Suggestion: use param tokenizer which is a WEKA tokenizer
			List<String> words = Tokenizer.tokenize(tool.getContext());
			words.forEach((word) -> types.add(word));
		});
		return new ArrayList<String>(types);
	}

	private Map<Tool, int[]> buildVectors(List<Tool> tools, List<String> types) {
		// contains for every tool a vector for term frequency in tool's context
		Map<Tool, int[]> toolVectors = new HashMap<Tool, int[]>();

		// create vectors map
		tools.forEach((tool) -> {
			Map<String, Integer> typeCounts = Tokenizer.getTypeCounts(tool.getContext());
			// vector for each tool
			int[] vector = new int[types.size()];
			for (int i = 0; i < types.size(); i++) {
				Integer count = typeCounts.get(types.get(i));
				if (count != null) {
					// add term frequency to vector
					vector[i] = count;
				}
			}
			toolVectors.put(tool, vector);
		});
		return toolVectors;
	}

}
