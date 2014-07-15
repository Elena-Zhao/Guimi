package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyOutfit extends OutfitFather implements Serializable {
	private List<String> clothes;
	private boolean isPublished;

	public MyOutfit(int outfitID, String outfitUrl) {
		// TODO Auto-generated constructor stub
		super(outfitID, outfitUrl);
		this.clothes = new ArrayList<String>();
		this.isPublished = false;
	}

	public String toString() {
		return "Outfit [outfitId=" + outfitId + ", outfitUrl="
				+ outfitUrl + ", clothes=" + clothes.get(0) + clothes.get(1) + "]";
	}
	
	public List<String> getClothes() {
		return clothes;
	}
	
	public void addCloth(String clothId)
	{
		clothes.add(clothId);
	}
	
	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}
}
