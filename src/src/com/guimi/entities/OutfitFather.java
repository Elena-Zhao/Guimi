package com.guimi.entities;

import java.io.Serializable;

public class OutfitFather implements Serializable {
	protected int outfitId;
	protected String outfitUrl;
	protected int pictureWidth;
	protected int pictureHeight;

	public OutfitFather(int outfitID, String outfitUrl) {
		// TODO Auto-generated constructor stub
		this.outfitId = outfitID;
		this.outfitUrl = outfitUrl;
	}

	public String toString() {
		return "Outfit [outfitId=" + outfitId + ", outfitUrl="
				+ outfitUrl + "]";
	}
	
	public int getOutfitID() {
		return outfitId;
	}

	public void setOutfitID(int outfitID) {
		this.outfitId = outfitID;
	}

	public String getOutfitUrl() {
		return outfitUrl;
	}

	public void setOutfitUrl(String outfitUrl) {
		this.outfitUrl = outfitUrl;
	}

	public int getPictureWidth() {
		return this.pictureWidth;
	}

	public void setPictureWidth(int pictureWidth) {
		this.pictureWidth = pictureWidth;
	}

	public int getPictureHeight() {
		return this.pictureHeight;
	}

	public void setPictureHeight(int pictureHeight) {
		this.pictureWidth = pictureHeight;
	}

	public void setPictureSize(int x, int y) {
		this.pictureWidth = x;
		this.pictureHeight = y;
	}
}
