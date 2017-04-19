package de.uni_koeln.spinfo.ml.toolclassification.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.uni_koeln.spinfo.ml.toolclassification.data.BagOfWords;
import de.uni_koeln.spinfo.ml.toolclassification.data.Model;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;

public class JSONReader {

	public static Model readJSONFile(String filename) {
		JSONParser jparser = new JSONParser();
		Model model = null;
		try {

			Object obj = jparser.parse(new FileReader(filename));

			JSONObject jsonObject = (JSONObject) obj;

			String feature = (String) jsonObject.get("Feature");
			JSONArray classList = (JSONArray) jsonObject.get("Classes");
			model = new Model(feature);
			for (int i = 0; i < classList.size(); i++) {
				JSONObject obj2 = (JSONObject) classList.get(i);
				JSONArray boWArray = (JSONArray) obj2.get("Bag_Of_Words");
				Long longValue = (Long) obj2.get("ClassId");
				int classID = longValue.intValue();
				BagOfWords bow = new BagOfWords(classID);

//				JSONArray jsToolSubs = (JSONArray) obj2.get("ToolSubs");
//				for (int l = 0; l < jsToolSubs.size(); l++) {
//					JSONObject toolSubObj = (JSONObject) jsToolSubs.get(l);
//					String id = (String) toolSubObj.get("ID");
//					String name = (String) toolSubObj.get("Name");
//					ToolSub toolsub = new ToolSub(id, name);
//					bow.addToolSub(toolsub);
//				}

				JSONArray jsTool = (JSONArray) obj2.get("Tools");
				for (int m = 0; m < jsTool.size(); m++) {
					JSONObject toolInfo = (JSONObject) jsTool.get(m);
					String name = (String) toolInfo.get("ToolName");
					String id = (String) toolInfo.get("ParentClass");
					Double totalToolCount = (Double) toolInfo.get("TotalToolCount");
					List<String> contextList = new ArrayList<>();
					JSONArray contextArray = (JSONArray) toolInfo.get("Context");
					for(int o =0; o<contextArray.size(); o++){
						String context = (String) contextArray.get(o);
						contextList.add(context);
					}
					Tool tool = new Tool(name, contextList, bow.getToolSubWithId(id));
					tool.setWordCount(totalToolCount);
//					JSONArray jsWordList = (JSONArray) toolInfo.get("WordMap");
//					Map<String, Double> wordMap = new HashMap<>();
//					for (int n = 0; n < jsWordList.size(); n++) {
//						JSONObject wordValuePair = (JSONObject) jsWordList.get(n);
//						String word = (String) wordValuePair.get("Word");
//						Double value = (Double) wordValuePair.get("Value");
//						wordMap.put(word, value);
//					}
//					tool.setWordMap(wordMap);
					bow.addTool(tool);
				}

				model.addBoW(bow);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	
	
	

}
