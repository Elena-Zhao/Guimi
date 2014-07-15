package com.guimi.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.string;
import android.widget.ArrayAdapter;

public class MatchTag {
	private String singleName;
	private List<String> tags;
	
	
	public MatchTag(String singleName) {
		super();
		this.singleName = singleName;
		this.tags = new ArrayList<String>();
	}
	public String getSingleName() {
		return singleName;
	}
	public void setSingleName(String singleName) {
		this.singleName = singleName;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String tagToString() {
		String tag = new String();
		for(Iterator<String> iterator = tags.iterator();iterator.hasNext();){
			String singletag = iterator.next();
			tag = tag + " "+singletag; 
		}
		return tag;
	}
	@Override
	public String toString() {
		return "单品名称 " + singleName + ", 标签 " + tagToString();
	}
	
	
}
