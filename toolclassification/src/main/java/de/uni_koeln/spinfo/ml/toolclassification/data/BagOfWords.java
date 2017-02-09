package de.uni_koeln.spinfo.ml.toolclassification.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BagOfWords {
	private Integer id;
	private Map<String, Double> wordMap;
	private Set<Tool> toolSet;
	private Set<ToolSub> toolSubSet;
	private double totalCount =0;

	
	public BagOfWords( int id){
		this.id = id;
		this.wordMap = new HashMap<>();
		this.toolSet = new HashSet<>();
		this.toolSubSet = new HashSet<>();
	}
	public void setTotalWordCount(int count){
		this.totalCount = count;
	}
	public double getTotalWordCount(){
		if(this.totalCount==0.){
			processWordMap();
		}
		return this.totalCount;
	}
	public int getID(){
		return this.id;
	}
	public Set<ToolSub> getToolSubSet(){
		return this.toolSubSet;
	}
	public void addTool(Tool tool){
		this.toolSet.add(tool);		
	}
	public Set<Tool> getTools(){
		return this.toolSet;
	}
	public void addToolSub(ToolSub toolsub){
		this.toolSubSet.add(toolsub);		
	}
	
	public Set<String> getWords(){
		if(this.wordMap.isEmpty()){
			processWordMap();
		}
		return wordMap.keySet();
	}
	public ToolSub getToolSubWithId(String id){
		ToolSub result = null;
		for(ToolSub toolsub: this.toolSubSet){
			if(toolsub.getID().equals(id)){
				result = toolsub;
			}
		}
		return result;
	}
	
	public Map<String, Double> getWordMap(){
		if(this.wordMap.isEmpty()){
			processWordMap();
		}
		return this.wordMap;
	}
	
	private void processWordMap(){
		for(Tool tool: toolSet){
			this.totalCount+= tool.getWordCount();
			for(String s : tool.getWordMap().keySet()){
				Double counter = this.wordMap.get(s);
				if(counter == null){
					counter = 0.;
				}
				this.wordMap.put(s, counter+tool.getWordMap().get(s));
			}
		}
	}
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(String word: this.wordMap.keySet()){
			s.append(word +" : "+wordMap.get(word)+"\n");
		}
		return s.toString();
	}
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(this.id != ((BagOfWords)o).id){
			return false;
		}
	
		else{
			return true;
		}
	}
	@Override
	public int hashCode(){
		int res= this.getID();
		return res;
	}

	
	
	
	
}
