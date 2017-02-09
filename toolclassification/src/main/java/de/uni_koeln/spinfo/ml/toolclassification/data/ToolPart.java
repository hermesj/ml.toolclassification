package de.uni_koeln.spinfo.ml.toolclassification.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolPart {

	private int id;
	private Set<ToolSub> toolSubList;

	// private String name;

	public ToolPart(int id) {
		this.id = id;
		this.toolSubList = new HashSet<>();
		// this.name = name;
	}
	
	public int getID(){
		return this.id;
	}
	
	public Set<ToolSub> getToolSubSet(){
		return this.toolSubList;
	}
	public void addToolSub(ToolSub toolsub){
		this.toolSubList.add(toolsub);
	}
	
	
	public Set<Tool> getTools(){
		Set<Tool> toolsList = new HashSet<>();
		for(ToolSub toolsub : this.toolSubList){
			toolsList.addAll(toolsub.getToolList());
		}
		return toolsList;
	}
	
	@Override
	public int hashCode(){
		return this.id;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (this.id == ((ToolPart)o).id  ){
			return true;
		}
	
		else{
			return false;
		}
	}
}