package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OutfitItem implements Serializable {
	protected String itemId;
	protected String clothId;
	protected String itemType;
	protected List<Tag> tags;
	protected String description;

	public OutfitItem(String itemId, String clothId, String itemType,
			String description) {
		super();
		this.itemId = itemId;
		this.clothId = clothId;
		this.itemType = itemType;
		this.tags = new ArrayList<Tag>();
		this.description = description;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", clothId=" + clothId
				+ ", itemType=" + itemType + ", tags=" + tags
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

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public List<Tag> getTags() {
		return tags;
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
