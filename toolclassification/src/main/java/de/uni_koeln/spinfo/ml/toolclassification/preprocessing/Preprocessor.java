package de.uni_koeln.spinfo.ml.toolclassification.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.uni_koeln.spinfo.ml.toolclassification.data.Context;
import de.uni_koeln.spinfo.ml.toolclassification.data.Model;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.components.Cooccurrence;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.WordFeature;
import de.uni_koeln.spinfo.ml.toolclassification.wikiContext.ContextSearcher;

/**
 * @author vetemi
 *
 */
public class Preprocessor {

	private WordFeature feature;
	private ContextSearcher contextSearcher;
	Map<String, String> wikipediaIndex;

	private Map<String, Tool> tools;

	// private List<String> referenceVector;
	// private Tokenizer tokenizer;

	public Preprocessor(Map<String, Tool> tools, WordFeature feature, Map<String, String> wikipediaIndex) {
		this.tools = tools;
		this.feature = feature;
		this.wikipediaIndex = wikipediaIndex;
	}

	public Model createModel() {

		// initialize statistics
		int contextFoundinTsv = 0;
		int contextFoundinTsvAndWiki = 0;
		int contextFoundinWiki = 0;
		int contextFoundInOtherTool = 0;
		
		Model model = new Model(feature.toString());

		// Set contextSearcher with tool names and wiki index
		if (contextSearcher == null) {
			contextSearcher = new ContextSearcher(tools.keySet(), wikipediaIndex);
		}

		// Kontexte aus Wikipedia-Dump auslesen
		List<Context> contextList = contextSearcher.getContext(feature);

		// Kontexte der Tsv-Datei featuren
		featureToolContext(feature);

		// Context-Objekte mit entsprechenden Tool-Objekten zusammenführen
		List<Tool> toolsWoutContext = new ArrayList<>();
		for (Context contextObj : contextList) {
			Tool tool = this.tools.get(contextObj.getTitle());

			List<String> contextFromWiki = contextObj.getContext();
			List<String> contextFromTsv = tool.getContext();

			// Statistik erstellen und ausgeben
			if (!contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsvAndWiki++;
			}
			if (!contextFromWiki.isEmpty() && contextFromTsv.isEmpty()) {
				contextFoundinWiki++;

			}
			if (contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsv++;
			}
			if (contextFromWiki.isEmpty() && contextFromTsv.isEmpty()) {
				toolsWoutContext.add(tool);
			}
			tool.addFeaturedContext(contextFromWiki);
			tool.setFeaturedName(contextObj.getFeaturedTitle());
			makeWordMap(tool);
			model.addToolToBagOfWordsWithID(tool);

		}
		Cooccurrence cooccurrence = new Cooccurrence(new HashSet<Tool>(this.tools.values()));
		contextFoundInOtherTool = cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);

		System.out.println("-----Statistik-----");
		System.out.println(feature + "\n");
		System.out.println("Insgesamt gibt es " + this.tools.size() + " Tools \n");
		System.out.println("Für " + contextFoundinTsv + " Tools wurde die Kontext nur in der Tsv-Datei gefunden \n");
		System.out.println("Für " + contextFoundinWiki + " Tools wurde Kontext nur im Wiki-Dump gefunden \n");
		System.out.println("Für " + contextFoundinTsvAndWiki
				+ " Tools wurde sowohl Kontext in der Tsv-Datei als auch im Wiki-Dump gefunden \n");
		System.out.println(
				"Für " + contextFoundInOtherTool + " Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für " + (toolsWoutContext.size() - contextFoundInOtherTool)
				+ " Tools wurde gar keine Kontexte gefunden \n");
		
		
		return model;

	}

	private void featureToolContext(WordFeature feature) {
		for (String word : this.tools.keySet()) {
			Tool tool = this.tools.get(word);
			List<String> wordsToBeProcessed = new ArrayList<>();
			tool.clearFeaturedContext();
			if (feature.needsTokenizing()) {
				if (tool.getTokenizedTsvcontext().isEmpty()) {
					try {
						tool.setTokenizedTsvcontext(contextSearcher.tokenizeWords(tool.getContext()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				wordsToBeProcessed = tool.getTokenizedTsvcontext();
			} else {
				wordsToBeProcessed.addAll(tool.getContext());
			}
			List<String> bla = feature.processWords(wordsToBeProcessed);
			tool.addFeaturedContext(bla);
			tools.put(word, tool);
		}
	}

	private void makeWordMap(Tool tool) {
		Map<String, Double> bagOfWords = new HashMap<>();
		double wordCount = 0.;
		for (String word : tool.getFeaturedContext()) {
			wordCount++;
			Double count = bagOfWords.get(word);
			if (count == null) {
				count = 0.;
			}
			bagOfWords.put(word, ++count);
		}
		// System.out.println(bagOfWords);
		tool.setWordMap(bagOfWords);
		tool.setWordCount(wordCount);
	}

	// private void initialize(List<Tool> tools) {
	// // Clean tool set, i.e. remove all tools without context
	// tools.forEach((tool) -> {
	// // add only tools with context to list
	// if (tool.getContext() != null) {
	// this.tools.add(tool);
	// }
	// });
	// }
	//
	// public BOWContainer createWordVector() {
	// // Create reference vector which holds all characters
	// List<String> typesVec = getTokenizedTypesVector(tokenizer);
	//
	// // calculate vector
	// Map<Tool, double[]> vector = buildVectors(tools, typesVec);
	//
	// // Create bag of words container for storing vectors and their dimension
	// return new BOWContainer(vector, typesVec.size());
	// }
	//
	// public BOWContainer createLemmaVector() {
	// return null;
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public BOWContainer createNGramVector() {
	// return null;
	// // TODO Auto-generated method stub
	// }
	//
	// private List<String> getTokenizedTypesVector(Tokenizer tokenizer) {
	// // Tree sets are ordered
	// Set<String> types = new TreeSet<String>();
	//
	// // create reference vector
	// tools.forEach((tool) -> {
	// // TODO: Suggestion: use param tokenizer which is a WEKA tokenizer
	// List<String> words = Tokenizer.tokenize(tool.getContext());
	// words.forEach((word) -> types.add(word));
	// });
	// return new ArrayList<String>(types);
	// }
	//
	// private Map<Tool, double[]> buildVectors(List<Tool> tools, List<String>
	// types) {
	// // contains for every tool a vector for term frequency in tool's context
	// Map<Tool, double[]> toolVectors = new HashMap<Tool, double[]>();
	//
	// // create vectors map
	// tools.forEach((tool) -> {
	// Map<String, Integer> typeCounts =
	// Tokenizer.getTypeCounts(tool.getContext());
	// // vector for each tool
	// double[] vector = new double[types.size()];
	// for (int i = 0; i < types.size(); i++) {
	// Integer count = typeCounts.get(types.get(i));
	// if (count != null) {
	// // add term frequency to vector
	// vector[i] = count;
	// }
	// }
	// toolVectors.put(tool, vector);
	// });
	// return toolVectors;
	// }

}
