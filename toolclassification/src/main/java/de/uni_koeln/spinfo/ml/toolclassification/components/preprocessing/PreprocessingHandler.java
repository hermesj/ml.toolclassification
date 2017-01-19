package de.uni_koeln.spinfo.ml.toolclassification.components.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import weka.core.tokenizers.Tokenizer;
import weka.core.tokenizers.WordTokenizer;

/**
 * @author vetemi
 *
 */
public class PreprocessingHandler {
	
	private Tokenizer tokenizer;
	
	public PreprocessingHandler() {
		tokenizer = new WordTokenizer();		
	}
	
	/**
	 * @param tools
	 * @return
	 */
	public BOWContainer preprocessData(List<Tool> tools) {
		//Step 1: filter tools without context
		List<Tool> toolsWithContext = removeEmptyContext(tools);
		
		//Step 2: tokenize and build the vector
		BOWContainer toolsVector = buildVector(toolsWithContext);
		
		//TODO Step 3: calculate weights 
		
		return toolsVector;		
	}

	private List<Tool> removeEmptyContext(List<Tool> tools) {
		List<Tool> toolsWithContext = new ArrayList<Tool>();
		tools.forEach((tool) -> {
			//add only tools with context to list
			if(tool.getContext()!=null){
				toolsWithContext.add(tool);
			}
		});
		return toolsWithContext;
	}
	
	private BOWContainer buildVector(List<Tool> tools) {
		//calculate vector and its dimension
		Map<Tool,int[]> vector = VectorBuilder.getToolsWithVector(tools);
		int dim = VectorBuilder.calculateDimension(vector);

		//Create bag of words container for storing data 
		BOWContainer bowContainer = new BOWContainer(vector, dim);
		return bowContainer;
		
	}
}
