package de.uni_koeln.spinfo.ml.toolclassification.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import de.uni_koeln.spinfo.ml.toolclassification.data.*;

public class TsvParser {

	private Map<String, Tool> tools = new HashMap<>();
	private Set<Integer> parentClasses = new HashSet<>();

	public void parseTsv(File file) {
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
					
					//collect parent tool IDs 
					if (!parentClasses.contains(toolNumber)) {
						parentClasses.add(toolNumber);
					}	
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
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
}
