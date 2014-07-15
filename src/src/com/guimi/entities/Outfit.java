package com.guimi.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Outfit extends OutfitFather implements Serializable {
	private String uploadTime;
	private String uploaderId;
	private String uploaderName;
	private String outfitDescription, clothDescription;
	private int likes;
	private int dislikes;
	private boolean isLiked;
	private boolean isDisliked;
	private boolean isFavorited;

	public Outfit(int outfitID, String outfitUrl, String uploaderId,
			String uploaderName, String uploadTime, int likes, int dislikes,
			String outfitDescription, String clothDescription) {
		// TODO Auto-generated constructor stub
		super(outfitID, outfitUrl);
		this.uploaderId = uploaderId;
		this.uploaderName = uploaderName;
		this.uploadTime = uploadTime;
		this.likes = likes;
		this.dislikes = dislikes;
		this.clothDescription = clothDescription;
		this.outfitDescription = outfitDescription;
		this.setDisliked(isDisliked);
		this.setLiked(isLiked);
	}

	public String getOutfitUrl() {
		return outfitUrl;
	}

	public void setOutfitUrl(String outfitUrl) {
		this.outfitUrl = outfitUrl;
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

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
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

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public boolean isDisliked() {
		return isDisliked;
	}

	public void setDisliked(boolean isDisliked) {
		this.isDisliked = isDisliked;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public String getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}
}
