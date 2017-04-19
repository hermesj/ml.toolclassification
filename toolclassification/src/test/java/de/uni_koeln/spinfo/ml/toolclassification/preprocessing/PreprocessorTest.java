package de.uni_koeln.spinfo.ml.toolclassification.preprocessing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.data.ToolSub;
import de.uni_koeln.spinfo.ml.toolclassification.io.TsvParser;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.Lemmas;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.Ngrams;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.Stems;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.feature.WordFeature;

public class PreprocessorTest {

	private TsvParser tsvParser;
	private Map<String, Tool> tools;
	private Preprocessor preprocessor;
	private Map<String, String> wikiArticles;

	@Before
	public void setUp() throws Exception {
		File testToolFile = new File("src/main/resources/test/toolstest.tsv");
		File testWikiArticlesFile = new File("src/main/resources/test/wikitest.tsv");
		tsvParser = new TsvParser();
		tsvParser.parseToolsTsv(testToolFile);
		tools = tsvParser.getTools();
		tsvParser.parseWikiArticles(testWikiArticlesFile);
		wikiArticles = tsvParser.getWikiArticles();
	}

	@Test
	public void testFeatureToolName() {
		preprocessor = new Preprocessor(tools, new WordFeature(), wikiArticles);
		
		String name = "hammer";
		
		String resultReference1 = "hamm";
		String resultReference2 = "amme";
		String resultReference3 = "mmer";
		
		List<String> featuredNames = preprocessor.featureToolName(name);
		
		assertTrue("Contains referenceList", featuredNames.size() == 3);
		assertTrue("Contains element", featuredNames.contains(resultReference1));
		assertTrue("Contains element", featuredNames.contains(resultReference2));
		assertTrue("Contains element", featuredNames.contains(resultReference3));
	}

	@Test
	public void testFeatureToolContext() {
		preprocessor = new Preprocessor(tools, new WordFeature(), wikiArticles);
		
		List<String> testContextList = new ArrayList<String>();
		testContextList.add("Das");
		testContextList.add("waren");
		testContextList.add("viele");
		testContextList.add("Bücher");
		
		List<String> stemmedList = preprocessor.featureToolContext(new Stems(), testContextList);
		List<String> testStemReferenceList = new ArrayList<String>();
		testStemReferenceList.add("das");
		testStemReferenceList.add("war");
		testStemReferenceList.add("viel");
		testStemReferenceList.add("buch");
		
		for (String testString : testStemReferenceList) {
			assertTrue("contains reference list", stemmedList.contains(testString));
		}
		
		List<String> lemmasList = preprocessor.featureToolContext(new Lemmas(), testContextList);
		List<String> testLemmaReferenceList = new ArrayList<String>();
		testLemmaReferenceList.add("der");
		testLemmaReferenceList.add("sein");
		testLemmaReferenceList.add("vieler");
		testLemmaReferenceList.add("büch");
		
		for (String testString : testLemmaReferenceList) {
			assertTrue("contains reference list", lemmasList.contains(testString));
		}
		
		List<String> ngramList = preprocessor.featureToolContext(new Ngrams(), testContextList);
		List<String> testNgramReferenceList = new ArrayList<String>();
		testNgramReferenceList.add("ware");
		testNgramReferenceList.add("aren");
		testNgramReferenceList.add("viel");
		testNgramReferenceList.add("iele");
		testNgramReferenceList.add("büch");
		testNgramReferenceList.add("üche");
		testNgramReferenceList.add("cher");
		
		for (String testString : testNgramReferenceList) {
			assertTrue("contains reference list", ngramList.contains(testString));
		}
		
	}

	@Test
	public void testMakeWordMap() {
		preprocessor = new Preprocessor(tools, new WordFeature(), wikiArticles);
		
		List<String> testFeatureList = new ArrayList<String>();
		testFeatureList.add("das");
		testFeatureList.add("war");
		testFeatureList.add("viel");
		testFeatureList.add("war");
		
		Tool tool = new Tool("test", testFeatureList, new ToolSub("1", "TestSub"));
		tool.addFeaturedContext(testFeatureList);
		
		preprocessor.makeWordMap(tool);
		
		assertTrue("contains correct word and count", tool.getWordMap().get("das") == 1.0);
		assertTrue("contains correct word and count", tool.getWordMap().get("war") == 2.0);
		assertTrue("contains correct word and count", tool.getWordMap().get("viel") == 1.0);
	}
	
	@Test
	public void testCreateBOWContainer() {
		preprocessor = new Preprocessor(tools, new WordFeature(), wikiArticles);
		BOWContainer bowContainer = preprocessor.createBowModel();
		
		assertTrue("bag of words correctly built", bowContainer.getVectorsDim() == 17);
		bowContainer.getToolsVector().forEach((tool,bow) -> {
			assertTrue("correct bag of words for each tool", bow.length == 17);
		});		
	}

}
