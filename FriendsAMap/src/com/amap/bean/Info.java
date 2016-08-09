package com.amap.bean;

public class Info {

	private String info = null;
	private String infocode = null;
	private String status = null;
	private Integer count = 0;
	private String tableId = null;
	private String id = null;
	public Info(){
		
	}
	public Info(String info, String infocode, String status, Integer count,
			String id, String tableId) {
		super();
		this.info = info;
		this.infocode = infocode;
		this.status = status;
		this.count = count;
		this.id = id;
		this.tableId = tableId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getInfocode() {
		return infocode;
	}

	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Info [info=" + info + ", infocode=" + infocode + ", status="
				+ status + ", count=" + count + ", tableId=" + tableId
				+ ", id=" + id + "]";
	}

}
