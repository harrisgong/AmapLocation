package com.amap.bean;

public class User {
	private String id = null;
	private String image = null;
	private String name = null;
	private String userId = null;
	private String userPassword = null;
	private String status = null;
	private String currentLocation = null;
	private String currentAddress = null;

	public User() {
	}

	public User(String id, String image, String name, String userId,
			String userPassword, String status, String currentLocation,
			String currentAddress) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.userId = userId;
		this.userPassword = userPassword;
		this.status = status;
		this.currentLocation = currentLocation;
		this.currentAddress = currentAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", image=" + image + ", name=" + name
				+ ", userId=" + userId + ", userPassword=" + userPassword
				+ ", status=" + status + ", currentLocation=" + currentLocation
				+ ", currentAddress=" + currentAddress + "]";
	}

}
