package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Item implements Serializable{
	private String itemId;
	private String clothId;
	private String itemUrl;
	private String itemType;
	private List<Tag> tags;
	private String description;
	
	public Item(String itemId, String clothId, String itemUrl, String itemType, 
			String description) {
		super();
		this.itemId = itemId;
		this.clothId = clothId;
		this.itemUrl = itemUrl;
		this.itemType = itemType;
		this.tags = new ArrayList<Tag>();
		this.description = description;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", clothId=" + clothId + ", itemUrl="
				+ itemUrl + ", itemType=" + itemType + ", tags=" + tags
				+ ", description=" + description + "]";
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getClothId() {
		return clothId;
	}
	
	public void setClothIdId(String clothId) {
		this.clothId = clothId;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public List<Tag> getTags() {
		return tags;
	}
	
	public String[] getTagsString() {
		if(tags != null){
			String[] tagStrings = new String[tags.size()];
			int i = 0;
			for(Iterator<Tag> it = tags.iterator(); it.hasNext();i++){
				Tag tag = it.next();
				tagStrings[i] = tag.getContent();  
			}
			return tagStrings;
		}
		return new String[0];
	}
	
	public String printTag(){
		String tagsPrint = new String();
		for(Iterator<Tag> it = tags.iterator(); it.hasNext(); ){
			Tag tag = it.next();
			tagsPrint = tagsPrint + " " + tag.getContent();  
		}
		return tagsPrint;
	}

	public void addTag(Tag tag) {
		tags.add(tag);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
