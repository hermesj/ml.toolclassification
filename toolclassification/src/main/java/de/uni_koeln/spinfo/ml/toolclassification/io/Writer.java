package de.uni_koeln.spinfo.ml.toolclassification.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;

import de.uni_koeln.spinfo.ml.toolclassification.data.BagOfWords;
import de.uni_koeln.spinfo.ml.toolclassification.data.Model;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.data.ToolSub;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Writer {

	public static void writeB(Model model) {
		String feature = model.getFeature();
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/json/" + feature + ".json", false), "UTF-8"));

			JSONObject obj1 = new JSONObject();
			obj1.put("Feature", feature);
			JSONArray jsClassList = new JSONArray();

			for (BagOfWords bow : model.getBagOfWordList()) {
				JSONObject obj2 = new JSONObject();
				JSONArray jsBowList = new JSONArray();
				for (String bowWord : bow.getWords()) {
					jsBowList.add(bowWord);
				}

				obj2.put("Bag_Of_Words", jsBowList);

				JSONArray jsTool = new JSONArray();
				for (Tool tool : bow.getTools()) {
					JSONObject toolInfo = new JSONObject();

					JSONArray jsWordList = new JSONArray();
					for (String word : tool.getWordMap().keySet()) {
						JSONObject jsWord = new JSONObject();
						// jsWord.put(word, tool.getWordMap().get(word));
						// Neu:
						jsWord.put("Word", word);
						jsWord.put("Value", tool.getWordMap().get(word));
						jsWordList.add(jsWord);
					}
					// damit man es später wieder einlesen kann
					toolInfo.put("WordMap", jsWordList);
					toolInfo.put("ParentClass", tool.getToolSub().getID());
					toolInfo.put("Context", tool.getContext());
					toolInfo.put("ToolName", tool.getName());
					toolInfo.put("TotalToolCount", tool.getWordCount());
					jsTool.add(toolInfo);
				}
				obj2.put("Tools", jsTool);

				JSONArray toolSubArray = new JSONArray();
				for (ToolSub toolsub : bow.getToolSubSet()) {
					JSONObject jsToolSub = new JSONObject();
					jsToolSub.put("ID", toolsub.getID());
					jsToolSub.put("Name", toolsub.getName());
					toolSubArray.add(jsToolSub);
				}
				obj2.put("ToolSubs", toolSubArray);
				obj2.put("ClassId", bow.getID());
				jsClassList.add(obj2);
			}
			obj1.put("Classes", jsClassList);
			bWriter.write(obj1.toJSONString());
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public static void writeToJSON(Model model) {
		String feature = model.getFeature();
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/json/MINI_" + feature + ".json", false), "UTF-8"));

			JSONObject obj1 = new JSONObject();
			obj1.put("Feature", feature);
			JSONArray jsClassList = new JSONArray();
			for (BagOfWords bow : model.getBagOfWordList()) {
				JSONObject obj2 = new JSONObject();
//				JSONArray jsBowList = new JSONArray();
//				for (String bowWord : bow.getWords()) {
//					jsBowList.add(bowWord);
//				}
//
//				obj2.put("Bag_Of_Words", jsBowList);

				JSONArray jsTool = new JSONArray();
				for (Tool tool : bow.getTools()) {
					JSONObject toolInfo = new JSONObject();

					JSONArray jsWordList = new JSONArray();
					for (String word : tool.getWordMap().keySet()) {
						JSONObject jsWord = new JSONObject();
						jsWord.put("Word", word);
						jsWord.put("Value", tool.getWordMap().get(word));
						jsWordList.add(jsWord);
					}
					// damit man es später wieder einlesen kann
//					toolInfo.put("WordMap", jsWordList);
					toolInfo.put("ParentClass", tool.getToolSub().getID());
					toolInfo.put("Context", tool.getFeaturedContext());
					toolInfo.put("ToolName", tool.getName());
					toolInfo.put("TotalToolCount", tool.getWordCount());
					jsTool.add(toolInfo);
				}
				obj2.put("Tools", jsTool);

//				JSONArray toolSubArray = new JSONArray();
//				for (ToolSub toolsub : bow.getToolSubSet()) {
//					JSONObject jsToolSub = new JSONObject();
//					jsToolSub.put("ID", toolsub.getID());
//					jsToolSub.put("Name", toolsub.getName());
//					toolSubArray.add(jsToolSub);
//				}
//				obj2.put("ToolSubs", toolSubArray);
				obj2.put("ClassId", bow.getID());
				jsClassList.add(obj2);
			}
			obj1.put("Classes", jsClassList);
			bWriter.write(obj1.toJSONString());
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	// momentan nicht in Gebrauch, da exception
	public static void writeBagOfWords(Model model) {
		String feature = model.getFeature();
		Gson gson = new Gson();
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/json/" + feature + ".json", false), "UTF-8"));
			for (BagOfWords bow : model.getBagOfWordList()) {
				bWriter.write(gson.toJson(bow.getWordMap()));
			}
			// bWriter.write(gson.toJson(model));

			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
