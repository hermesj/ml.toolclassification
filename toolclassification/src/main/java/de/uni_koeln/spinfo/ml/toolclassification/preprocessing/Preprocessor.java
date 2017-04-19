package de.uni_koeln.spinfo.ml.toolclassification.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Model;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.components.Cooccurrence;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.FeatureFactory;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.Tokenizer;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.WordFeature;

/**
 * Handles all pre processing tasks like featuring and bag of words creation.
 * 
 * @author vetemi
 *
 */
public class Preprocessor {

	/**
	 * the feature component to feature the tools name and context
	 */
	private WordFeature feature;
	/**
	 * the tokenizer used for some feature components
	 */
	private Tokenizer tokenizer;
	/**
	 * the Wikipedia articles as map for data enrichment
	 */
	private Map<String, String> wikiArticles;
	/**
	 * All distinct words of the whole corpus
	 */
	private TreeSet<String> types;

	/**
	 * tools on which pre processing tasks apply
	 */
	private Map<String, Tool> tools;

	public Preprocessor(Map<String, Tool> tools, WordFeature feature, Map<String, String> wikiArticles) {
		this.tools = tools;
		this.feature = feature;
		this.wikiArticles = wikiArticles;
		this.tokenizer = new Tokenizer();
		this.types = new TreeSet<>();
	}

	/**
	 * Creates the bag of words model, based on the given tools and Wikipedia
	 * articles.
	 * 
	 * @return bag of words model
	 */
	public BOWContainer createBowModel() {

		// initialize statistics
		int contextFoundinTsv = 0;
		int contextFoundinWiki = 0;
		int contextFoundInOtherTool = 0;

		StringBuilder contextStringBuilder = new StringBuilder();
		String context = "";
		List<String> totalContextList = new ArrayList<>();
		List<Tool> toolsWoutContext = new ArrayList<>();

		for (Entry<String, Tool> toolEntry : tools.entrySet()) {

			// get context from wiki
			// try first perfect match
			context = wikiArticles.get(toolEntry.getKey());

			List<String> featuredToolName = featureToolName(toolEntry.getKey());

			// check if something found
			if (context == null) {
				// if not, try with stemmed and lemmatized name
				if (!featuredToolName.isEmpty()) {
					toolEntry.getValue().setFeaturedName(new ArrayList<>(featuredToolName));
					for (String featuredName : featuredToolName) {
						context = wikiArticles.get(featuredName);
						if (context != null) {
							// append found context
							contextStringBuilder.append(context);
						}
					}
				}

			} else {
				// perfect match
				contextStringBuilder.append(context);

				// Store featured name here. Important for later coocurrence
				// search
				if (!featuredToolName.isEmpty()) {
					toolEntry.getValue().setFeaturedName(featuredToolName);
				}
			}

			// something found in Wikipedia?
			if (!contextStringBuilder.toString().isEmpty()) {
				// this can be done because tokenization has been done for wiki
				// articles when created file
				totalContextList = new ArrayList<>(Arrays.asList(contextStringBuilder.toString().split(" ")));
				contextFoundinWiki++;
			}

			// combine possibly Wikipedia article with existing context
			if (!toolEntry.getValue().getContext().isEmpty()) {
				totalContextList.addAll(toolEntry.getValue().getContext());
				contextFoundinTsv++;
			}

			if (totalContextList.isEmpty()) {
				// no context at all
				toolsWoutContext.add(toolEntry.getValue());
			} else {
				totalContextList = featureToolContext(feature, totalContextList);
				toolEntry.getValue().addFeaturedContext(totalContextList);

				makeWordMap(toolEntry.getValue());
			}

			// clear all
			contextStringBuilder.setLength(0);
			totalContextList.clear();
			featuredToolName.clear();
		}

		// Try to find coocurrence tool context
		Cooccurrence cooccurrence = new Cooccurrence(new HashSet<Tool>(this.tools.values()));
		contextFoundInOtherTool = cooccurrence.enrichContextWithReferencingTools(toolsWoutContext);

		System.out.println("-----Statistik-----");
		System.out.println(feature + "\n");
		System.out.println("Insgesamt gibt es " + this.tools.size() + " Tools \n");
		System.out.println("Für " + contextFoundinTsv + " Tools wurde die Kontext nur in der Tsv-Datei gefunden \n");
		System.out.println("Für " + contextFoundinWiki + " Tools wurde Kontext nur im Wiki-Dump gefunden \n");
		System.out.println(
				"Für " + contextFoundInOtherTool + " Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für " + (toolsWoutContext.size() - contextFoundInOtherTool)
				+ " Tools wurde gar keine Kontexte gefunden \n");

		// create and return bag of words
		return createBOWContainer();
	}

	/**
	 * Creates the bag of words, based on the types of the whole context corpus.
	 * For every tool there will be a vector in the same size.
	 * 
	 * @return
	 */
	public BOWContainer createBOWContainer() {
		// for better ID access, convert to list
		ArrayList<String> typesList = new ArrayList<>(types);
		types = null;
		Map<Tool, double[]> bow = new HashMap<>();
		Map<String, Double> wordMap;
		double[] vector;
		String currentWord = "";
		Double value = 0.0;

		for (Entry<String, Tool> toolEntry : tools.entrySet()) {
			// only for tools with context
			if (!toolEntry.getValue().getWordMap().isEmpty()) {

				vector = new double[typesList.size()];
				wordMap = toolEntry.getValue().getWordMap();

				// iterate through all words and merge own bag of words map on
				// that. Otherwise entry is 0.0
				for (int i = 0; i < typesList.size(); i++) {
					currentWord = typesList.get(i);
					value = wordMap.get(currentWord);
					if (value != null) {
						vector[i] = value;
					}
				}
				bow.put(toolEntry.getValue(), vector);
			}
		}
		BOWContainer bowContainer = new BOWContainer(bow, typesList.size());

		return bowContainer;
	}

	/**
	 * Based on the given string, it will call all possible feature components
	 * and return a distinct list of all feature outputs.
	 * 
	 * @param toolName
	 *            String to feature
	 * @return distinct list of all possible feature of String
	 */
	public List<String> featureToolName(String toolName) {

		Set<String> featuredNames = new HashSet<String>();

		// necessary because feature components are designed to take lists only
		List<String> namesList = new ArrayList<String>();
		namesList.add(toolName);

		for (WordFeature feature : FeatureFactory.getAllFeatures()) {
			featuredNames.addAll(feature.processWords(namesList));
		}
		return new ArrayList<String>(featuredNames);
	}

	/**
	 * Features the context (list of Strings) of a tool, based on the desired
	 * feature component.
	 * 
	 * @param feature
	 *            the feature component to feature context
	 * @param contextList
	 *            the context as list of Strings
	 * @return the context as featured list of Strings
	 */
	public List<String> featureToolContext(WordFeature feature, List<String> contextList) {
		if (feature.needsTokenizing()) {
			contextList = tokenizer.tokenize(contextList);
		}
		return feature.processWords(contextList);
	}

	/**
	 * Creates for a given tool its own bag of words map. Stores the bag of
	 * words model in tools word map attribute. Furthermore, collects
	 * distinctively all types of the tools corpus.
	 * 
	 * @param tool
	 *            to create bag of words model.
	 */
	public void makeWordMap(Tool tool) {
		Map<String, Double> bagOfWords = new HashMap<>();
		double wordCount = 0.;
		for (String word : tool.getFeaturedContext()) {
			// fill bag of words of tool
			bagOfWords.merge(word, 1.0, Double::sum);
			wordCount++;

			// fill types vector for vector building
			types.add(word);
		}
		tool.setWordMap(bagOfWords);
		tool.setWordCount(wordCount);
	}

}
