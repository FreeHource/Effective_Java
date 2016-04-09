package com.org.prj.cre_dsty_obj;
/**@category 简单的静态工厂方法*/
public class CreateObj {
	private String name="obj";
	private String type="CreateObj";
	
	/**简单的静态工厂方法*/
	public static CreateObj getInstance(){
		
		return new CreateObj();
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
