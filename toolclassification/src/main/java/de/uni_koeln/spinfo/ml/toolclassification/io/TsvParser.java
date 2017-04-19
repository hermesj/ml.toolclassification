package de.uni_koeln.spinfo.ml.toolclassification.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import de.uni_koeln.spinfo.ml.toolclassification.data.*;

/**
 * Imports and parses TSV files.
 * 
 * @author hanna, vetemi
 *
 */
public class TsvParser {
	/**
	 * Currently necessary because performance is too low when using all
	 * Wikipedia articles
	 */
	private int MAX_WIKI_ARTICLES = 0;

	/**
	 * Stores all tools from file in map with tool name and tool itself.
	 */
	private Map<String, Tool> tools = new HashMap<>(4500);
	/**
	 * parent class IDs
	 */
	private Set<Integer> parentClasses = new HashSet<>();
	/**
	 * Stores all Wikipedia articles with content in map.
	 */
	private Map<String, String> wikiArticles = new HashMap<>(MAX_WIKI_ARTICLES);

	public void parseToolsTsv(File file) {
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {

			while (bReader.ready()) {

				String line = bReader.readLine();
				String[] lineSplit = line.split("\t");
				// zb Z.978 quasi leer, 1498 Kontext leer
				if (!lineSplit[0].isEmpty() && !lineSplit[1].isEmpty()) {
					List<String> context = new ArrayList<>();
					// LOWERCASE
					String name = lineSplit[0].toLowerCase();
					Integer toolNumber = Integer.valueOf(Character.toString(lineSplit[2].charAt(1)));
					String toolSubId = lineSplit[2].replace(")", "").replace("(", "");
					String toolSubName = lineSplit[3];
					if (!lineSplit[11].equalsIgnoreCase("na")) {
						context.add(lineSplit[11]);
					}

					ToolPart tp = new ToolPart(toolNumber);

					ToolSub ts = new ToolSub(toolSubId, toolSubName, tp);

					Tool tool = new Tool(name, context, ts);

					if (!tools.containsKey(name)) {
						tools.put(name, tool);
					}

					// collect parent tool IDs
					if (!parentClasses.contains(toolNumber)) {
						parentClasses.add(toolNumber);
					}
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Imports and stores all Wikipedia articles in a map.
	 * 
	 * @param wikiArticlesFile
	 *            File which contains the Wikipedia articles
	 */
	public void parseWikiArticles(File wikiArticlesFile) {
		long start = System.currentTimeMillis();
		System.out.println("Start");
		StringBuilder sbContent = new StringBuilder();

		String[] lineSplit;
		String line;
		try {
			BufferedReader bReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(wikiArticlesFile), "UTF-8"));
			while (bReader.ready()) {
				// wikiArticles = (Map<String,String>) bReader.lines()
				// .filter(string -> !string.isEmpty())
				// .map(string -> string.split("\t"))
				// .collect(Collectors.toMap(e -> e[0], e -> e[1]));
				if (wikiArticles.size() == MAX_WIKI_ARTICLES) {
					break;
				}
				line = bReader.readLine();
				if (!line.isEmpty()) {
					// Can be split with tab. Articles has been stored with tab
					// delimiter.
					lineSplit = StringUtils.split(line, "\t");
					if (lineSplit.length == 2) {
						// Check if contains and if yes add article content to
						// the already containing one.
						if (wikiArticles.containsKey(lineSplit[0])) {
							sbContent.append(wikiArticles.get(lineSplit[0]));
							sbContent.append(" ");
							sbContent.append(lineSplit[1]);
							wikiArticles.put(lineSplit[0], sbContent.toString());
							sbContent.setLength(0);
						} else {
							wikiArticles.put(lineSplit[0], lineSplit[1]);
						}
					}
				}
			}
			bReader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		long end = System.currentTimeMillis();

		System.out.println("Runtime Wiki read: " + (end - start));
		System.out.println("--Info: Wikipedia Dump besteht aus " + wikiArticles.size() + " Artikeln--");

	}

	public Set<Integer> getParentClasses() {
		return parentClasses;
	}

	public void setParentClasses(Set<Integer> parentClasses) {
		this.parentClasses = parentClasses;
	}

	public void setTools(Map<String, Tool> tools) {
		this.tools = tools;
	}

	public Map<String, Tool> getTools() {
		return tools;
	}

	public Map<String, String> getWikiArticles() {
		return wikiArticles;
	}

	public void setWikiArticles(Map<String, String> wikiArticles) {
		this.wikiArticles = wikiArticles;
	}

	public int getMAX_WIKI_ARTICLES() {
		return MAX_WIKI_ARTICLES;
	}

	public void setMAX_WIKI_ARTICLES(int mAX_WIKI_ARTICLES) {
		MAX_WIKI_ARTICLES = mAX_WIKI_ARTICLES;
	}
}
