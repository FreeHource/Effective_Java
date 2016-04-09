package com.org.prj.cre_dsty_obj;

/**私有构造器*/
public  class Noninstantiable {
	
	private String name;
	private String type;
	private Noninstantiable(){
		this.name = "obj";
		this.type = "Object";
	}
	
	public static String toString(String name, String type){
		return "name:"+name+"; type:"+type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
