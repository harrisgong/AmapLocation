package com.amap.bean;

public class History {
	private String id = null;
	private String name = null;
	private String location = null;
	private String address = null;
	private String userid = null;

	public History() {
	}

	public History(String id, String name, String location, String userid,
			String address) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.address = address;
		this.userid = userid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "History [id=" + id + ", name=" + name + ", location="
				+ location + ", address=" + address + ", userid=" + userid
				+ "]";
	}

}
