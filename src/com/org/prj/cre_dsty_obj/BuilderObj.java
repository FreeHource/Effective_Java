package com.org.prj.cre_dsty_obj;

import com.org.prj.common.IDUtil;

public class BuilderObj {
	private String name;
	private String type;
	private int num;
	private int count;
	private long id;
	
	public BuilderObj name(String name){
		this.name=name;
		return this;
	}
	public BuilderObj type(String type){
		this.type=type;
		return this;
	} 
	public BuilderObj num(int num){
		this.num=num;
		return this;
	}
	public BuilderObj count(int count) throws Exception{
		this.count=count;
		if(count<0) throw new Exception("数量必须大于0");
		return this;
	}
	public BuilderObj build(){
		this.id = IDUtil.newUUID();
		return this;
	}
	
	public String toString(){
		return "id:"+this.id+";name:"+this.name+";type:"+this.type+";num:"+this.num+";count:"+this.count;
	}
/*
	private final String name;
	private final String type;
	private final int num;
	private final int count;
	
	public static class Builder{
		private String name;
		private String type;
		private int num=0;
		private int count=10;
		
		public Builder(String name,String type){
			this.name=name;
			this.type=type;
		}
		public Builder num(int n){
			num=n;
			return this;
		}
		public Builder count(int c){
			count=c;
			return this;
		}
		
		public BuilderObj build(){
			return new BuilderObj(this);
		}
		
	}
	
	private BuilderObj(Builder builder){
		name=builder.name;
		type=builder.type;
		num=builder.num;
		count=builder.count;
	}
	
*/	
}
