package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class FavoriteOutfit extends OutfitFather implements Serializable {

	private int favoriteId;
	private String outfitDescription,clothDescription;
	private String uploadTime;
	private String uploaderName;

	
	public FavoriteOutfit(int favoriteId, int outfitID, String outfitUrl,
			String uploadTime, String uploaderName, String outfitDescription,
			String clothDescription) {
		// TODO Auto-generated constructor stub
		super(outfitID, outfitUrl);
		this.setFavoriteId(favoriteId);
		this.outfitDescription = outfitDescription;
		this.clothDescription = clothDescription;
		this.uploaderName = uploaderName;
		this.uploadTime = uploadTime;
	}
	
	public FavoriteOutfit(int outfitID, String outfitUrl, String uploadTime2,
			String uploaderName2, String outfitDescription2,
			String clothDescription2) {
		// TODO Auto-generated constructor stub
		super(outfitID, outfitUrl);
		this.setFavoriteId(favoriteId);
		this.outfitDescription = outfitDescription2;
		this.clothDescription = clothDescription2;
		this.uploaderName = uploaderName2;
		this.uploadTime = uploadTime2;
	}

	public String getUploadTime() {
		return uploadTime;
	}


	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}


	public String getUploaderName() {
		return uploaderName;
	}


	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
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

	public int getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(int favoriteId) {
		this.favoriteId = favoriteId;
	}

}
