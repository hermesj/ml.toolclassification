package de.uni_koeln.spinfo.ml.toolclassification.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

public class TsvParserTest {

	@Test
	public void testParseToolsTsv() {
		File testToolFile = new File("src/main/resources/test/toolstest.tsv");
		TsvParser tsvParser = new TsvParser();
		
		String toolName1 = "abisolierwerkzeug";
		String contextTool1 = "Erfahrung im Umgang mit Abisolierwerkzeugen und Crimpzangen";
		int parentCategoryTool1 = 1;
		
		String toolName2 = "abroller";
		String contextTool2 = "Erfahrung mit Abroller und Hänger wünschenswert  Erfahrung mit Abroller und Hänger";
		int parentCategoryTool2 = 7;
		
		tsvParser.parseToolsTsv(testToolFile);
		
		Map<String,Tool> tools = tsvParser.getTools();
		
		assertTrue("contains tool", tools.get(toolName1) != null);
		assertTrue("contains tool", tools.get(toolName2) != null);
		
		assertTrue("contains correct context", tools.get(toolName1).getContext().contains(contextTool1));
		assertTrue("contains correct context", tools.get(toolName2).getContext().contains(contextTool2));
		
		Set<Integer> parentNumbers = tsvParser.getParentClasses();
		
		assertTrue("Contains parent category number", parentNumbers.contains(parentCategoryTool1));
		assertTrue("Contains parent category number", parentNumbers.contains(parentCategoryTool2));
	}

	@Test
	public void testParseWikiArticles() {
		File testToolFile = new File("src/main/resources/test/wikitest.tsv");
		TsvParser tsvParser = new TsvParser();
		
		String wikiArticle1 = "hammer";
		String wikiContent1 = "das ist ein hammer";
		
		String wikiArticle2 = "schraubenzieher";
		String wikiContent2 = "das ist ein schraubenzieher";
		
		String wikiContent3 = "und nochmal ein hammer";
		
		tsvParser.parseWikiArticles(testToolFile);
		tsvParser.setMAX_WIKI_ARTICLES(100);
		
		Map<String, String> wikiMap = tsvParser.getWikiArticles();
		
		assertTrue("contains article", wikiMap.get(wikiArticle1) != null);
		assertTrue("contains article", wikiMap.get(wikiArticle2) != null);
		
		assertTrue("contains correct text", wikiMap.get(wikiArticle1).equals(wikiContent1+" "+wikiContent3));
		assertTrue("contains correct text", wikiMap.get(wikiArticle2).equals(wikiContent2));
		
	}

}
