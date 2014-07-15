package com.guimi.entities;

import java.io.Serializable;

public class Match implements Serializable {
	private int matchId;
	private String imageUrl;
	private float imageWidth;
	private float imageHeight;
	private String userId;
	private String userName;
	private int likeNum;
	private int dislikeNum;
	private boolean isMyLike;
	private boolean isMyDislike;
	private boolean isMyFavorite;

	public Match(int matchId, String imageUrl, int imageWidth, int imageHeight,
			String userId, String userName, int likeNum, int dislikeNum) {
		super();
		this.matchId = matchId;
		this.imageUrl = imageUrl;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.userId = userId;
		this.userName = userName;
		this.likeNum = likeNum;
		this.dislikeNum = dislikeNum;
		this.isMyLike = false;
		this.isMyDislike = false;
		this.isMyFavorite = false;
	}

	public String toString() {
		return "Match:[matchId = " + matchId + ",imageUrl = " + imageUrl
				+ ",likeNum = " + likeNum + ",imageWidth = " + imageWidth
				+ ",imageHeight = " + imageHeight + ",dislikeNum = "
				+ dislikeNum + ",isMyLike = " + isMyLike + ", isMyDIslike = "
				+ isMyDislike + ", isMyFavorite = " + isMyFavorite + "]";
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public float getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(float imageWidth) {
		this.imageWidth = imageWidth;
	}

	public float getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(float imageHeight) {
		this.imageHeight = imageHeight;
	}

	public void setPictureSize(int x, int y) {
		this.imageWidth = x;
		this.imageHeight = y;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public int getDislikeNum() {
		return dislikeNum;
	}

	public void setDislikeNum(int dislikeNum) {
		this.dislikeNum = dislikeNum;
	}

	public boolean isMyLike() {
		return isMyLike;
	}

	public void setMyLike(boolean isMyLike) {
		this.isMyLike = isMyLike;
	}

	public boolean isMyDislike() {
		return isMyDislike;
	}

	public void setMyDislike(boolean isMyDislike) {
		this.isMyDislike = isMyDislike;
	}

	public boolean isMyFavorite() {
		return isMyFavorite;
	}

	public void setMyFavorite(boolean isMyFavorite) {
		this.isMyFavorite = isMyFavorite;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
