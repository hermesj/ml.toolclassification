package de.uni_koeln.spinfo.ml.toolclassification.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolSub {

	private String id;

	private String name;

	private ToolPart toolpart;
	
	private Set<Tool> toolsList;
	
	//brauchen wir unbedingt diesen Konstruktor?
	public ToolSub(String id, String name, ToolPart tp) {
		this.id = id;
		this.name = name;
		this.toolpart = tp;
		this.toolsList = new HashSet<>();
	}
	//im Prinzip würde ja dieser reichen:
	public ToolSub(String id, String name) {
		this.id = id;
		this.name = name;
		this.toolpart = new ToolPart(Character.getNumericValue(id.charAt(0)));
		this.toolsList = new HashSet<>();
	}
	public String getName(){
		return this.name;
	}
	public ToolPart getToolPart(){
		return this.toolpart;
	}
	
	public Set<Tool> getToolList(){
		return this.toolsList;
	}
	public void addTool(Tool tool){
		toolsList.add(tool);
	}
	
	public String getID(){
		return this.id;
	}
	@Override
	public int hashCode(){
		return this.id.hashCode();
	}
	//evtl noch + this.name.hashCode();
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (this.getToolPart().getID() !=( (ToolSub)o).getToolPart().getID() ){
			return false;
		}
		if (!this.id.equals(((ToolSub)o).id ) ){
			return false;
		}
		if (!this.name.equals(((ToolSub)o).name )) {
			return false;
		}
	
		else{
			return true;
		}
	}
}