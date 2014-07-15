package com.guimi.entities;

import java.io.Serializable;

public class Tag implements Serializable{
	private String tagTitle;
	private String tagContent;
	
	public Tag(String tagTitle, String tagContent)
	{
		this.tagTitle = tagTitle;
		this.tagContent = tagContent;
	}
	
	public String getTitle()
	{
		return tagTitle;
	}
	
	public String getContent()
	{
		return tagContent;
	}

	@Override
	public String toString() {
		return tagTitle + "：" + tagContent;
	}
	
}
