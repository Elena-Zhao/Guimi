package com.guimi.entities;

public class TagIndex {
	private int title;
	private int content;
	
	public TagIndex(int title, int content) {
		super();
		this.title = title;
		this.content = content;
	}
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	public int getContent() {
		return content;
	}
	public void setContent(int content) {
		this.content = content;
	}
	
	

}
