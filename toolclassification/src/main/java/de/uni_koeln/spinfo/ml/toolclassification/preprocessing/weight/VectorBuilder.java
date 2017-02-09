package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

/**
 * Builds feature vectors for Tools from their contexts
 * @author jhermes
 *
 */
public class VectorBuilder {
	

	private static List<String> getTypesList(List<Tool> toolsWithContexts){
		Set<String> types = new TreeSet<String>();
//		for (Tool tool : toolsWithContexts) {
//			List<String> words = Tokenizer.tokenize(tool.getContext());
//			for (String word : words) {
//				types.add(word);
//			}
//		}
		return new ArrayList<String>(types);
	} 
	
	private static Map<Tool, int[]> buildVectors(List<Tool> toolsWithContexts, List<String> types){
		Map<Tool, int[]> toolsWithVectors = new HashMap<Tool, int[]>();
		for (Tool tool : toolsWithContexts) {
			Map<String, Integer> typeCounts = null; //Tokenizer.getTypeCounts(tool.getContext());
			int[] vector = new int[types.size()];
			for (int i=0; i<types.size(); i++) {
				Integer count = typeCounts.get(types.get(i));
				if(count!=null){
					vector[i] = count;
				}
			}
			toolsWithVectors.put(tool, vector);
		}
		return toolsWithVectors;
	}	

	/**
	 * Returns the feature vectors
	 * @param tool 
	 * @return feature vector for the specified Tool
	 */
	public static Map<Tool, int[]> getToolsWithVector(List<Tool> toolsWithContexts) {
		return buildVectors(toolsWithContexts, getTypesList(toolsWithContexts));
	}
	
	/**
	 * calculates the dimension of the vector
	 * @param toolsVector the tool vector to be calculated 
	 * @return dimension
	 */
	public static int calculateDimension(Map<Tool, int[]> toolsVector) {
		//as they have all the same dimension, it's enough to take
		//dimension from first array
		return toolsVector.entrySet().iterator().next().getValue().length;
	}
}
