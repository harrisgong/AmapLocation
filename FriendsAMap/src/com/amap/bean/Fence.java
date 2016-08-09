package com.amap.bean;

public class Fence {
	private String id = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name = null;
	private String location = null;
	private String address = null;
	private String userId1 = null;
	private String userId2 = null;
	private String radius = null;

	public Fence() {
	}

	public Fence(String name, String location, String address, String userId1,
			String userId2, String radius) {
		super();
		this.name = name;
		this.location = location;
		this.address = address;
		this.userId1 = userId1;
		this.userId2 = userId2;
		this.radius = radius;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserId1() {
		return userId1;
	}

	public void setUserId1(String userId1) {
		this.userId1 = userId1;
	}

	public String getUserId2() {
		return userId2;
	}

	public void setUserId2(String userId2) {
		this.userId2 = userId2;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Fence [name=" + name + ", location=" + location + ", address="
				+ address + ", userId1=" + userId1 + ", userId2=" + userId2
				+ ", radius=" + radius + "]";
	}

}
