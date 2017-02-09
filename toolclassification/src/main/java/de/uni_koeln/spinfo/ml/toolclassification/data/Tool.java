package de.uni_koeln.spinfo.ml.toolclassification.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class to represent tools extracted from job ads
 *
 */
public class Tool {
	private ToolSub toolsub;
	private String name;
	private List<String> tsvContext;
	private List<String> tokenizedTsvcontext;
	private List<String> featuredContext;
	private Map<String, Double> wordMap;
	private Set<Tool> coCounts;
	private Set<Tool> referencingTools;
	private Double wordCount;
	private List<String> featuredName;
	
	
	public Tool(String name, List<String> context, ToolSub tsc) {
		this.toolsub = tsc;		
		this.name = name;
		this.tsvContext = context;
		this.featuredContext=new ArrayList<>();
		this.tokenizedTsvcontext = new ArrayList<>();
	}
		
	public List<String> getTokenizedTsvcontext() {
		return tokenizedTsvcontext;
	}

	public void setTokenizedTsvcontext(List<String> tokenizedTsvcontext) {
		this.tokenizedTsvcontext = tokenizedTsvcontext;
	}

	public List<String> getFeaturedContext() {
		return featuredContext;
	}
	public void clearFeaturedContext() {
		this.featuredContext.clear();;
	}

	public void addFeaturedContext(List<String> featuredContext) {
		this.featuredContext.addAll(featuredContext);
	}

	public ToolSub getToolSub(){
		return this.toolsub;
	}
	public String getName(){
		return this.name;
	}

	public List<String> getContext() {
		return this.tsvContext;
	}

	public void addContext(List<String> newcontext) {
		this.tsvContext.addAll(newcontext);
	}

	public void setWordMap (Map<String, Double> wordMap){
		this.wordMap = wordMap;
	}
	public Map<String, Double> getWordMap (){
		return this.wordMap;
	}

	
	public void setCooccurrenceCounts (Set<Tool> coCounts){
		this.coCounts = coCounts;
	}
	public Set<Tool> getCooccurrenceCounts (){
		return this.coCounts;
	}

	
	public Double getWordCount(){
		return this.wordCount;
	}
	public void setWordCount(Double wordCount){
		this.wordCount = wordCount;
	}
	public void setFeaturedName(List<String> fn){
		this.featuredName = fn;
	}
	public List<String> getFeaturedName(){
		return this.featuredName;
	}
	public Set<Tool> getReferencingTools(){
		return this.referencingTools;
	}
	public void setReferencingTools(Set<Tool> refTools){
		this.referencingTools = refTools;
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (this.name.equals( ((Tool)o).name ) ){
			return true;
		}
	
		else{
			return false;
		}
	}
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder(this.name+": "+this.toolsub.getID()+"\n"+"Bag of Words: ");
		for(String str : this.wordMap.keySet()){
			result.append(str+" : "+this.wordMap.get(str)+",\n");
		}
		return result.toString();
	}
}
