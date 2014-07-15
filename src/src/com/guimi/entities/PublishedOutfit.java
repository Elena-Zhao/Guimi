package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class PublishedOutfit extends OutfitFather implements Serializable {

	private String outfitDescription, clothDescription;
	private String uploadTime;
	private int likes;

	public PublishedOutfit(int outfitID, String outfitUrl,
			String uploadTime, int likes, String outfitDescription,
			String clothDescription) {
		super(outfitID, outfitUrl);
		// TODO Auto-generated constructor stub
		this.uploadTime = uploadTime;
		this.setLikes(likes);
		this.clothDescription = clothDescription;
		this.outfitDescription = outfitDescription;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getOutfitDescription() {
		return outfitDescription;
	}

	public void setOutfitDescription(String outfitDescription) {
		this.outfitDescription = outfitDescription;
	}

	public String getClothDescription() {
		return clothDescription;
	}

	public void setClothDescription(String clothDescription) {
		this.clothDescription = clothDescription;
	}

}
