package com.amap.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.protocol.HTTPUtils;

public class DataProvider {

	private final static String[] LOCATION_FIELD = new String[]{"_id","_name","_address","userid"};
	private final static String[] USER_FIELD = new String[]{"_id","_name","_address","userid","password","status"};
	private final static String[] FENCE_FIELD = new String[]{"_id","_name","_address","userid1","userid2","radius"};
	
	
	
	public static List<NameValuePair> insertData(String tableId, String[] datas) {
		try {
			JSONObject data = new JSONObject();
			if (tableId.equalsIgnoreCase(HTTPUtils.HISTORY_TABLEID)) {
				for (int i = 1; i < LOCATION_FIELD.length; i++)
					data.put(LOCATION_FIELD[i], datas[i - 1]);
			} else if (tableId.equalsIgnoreCase(HTTPUtils.USER_TABLEID)) {
				for (int i = 1; i < USER_FIELD.length; i++)
					data.put(USER_FIELD[i], datas[i - 1]);
			}else if(tableId.equalsIgnoreCase(HTTPUtils.FENCE_TABLEID)){
				for (int i = 1; i < FENCE_FIELD.length; i++)
					data.put(FENCE_FIELD[i], datas[i - 1]);
			}
			return toNameValuePairData(data.toString(), tableId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<NameValuePair> updateData(String tableId, String[] datas) {
		try {
			JSONObject data = new JSONObject();
			if (tableId.equalsIgnoreCase(HTTPUtils.HISTORY_TABLEID)) {
				for (int i = 0; i < LOCATION_FIELD.length; i++)
					data.put(LOCATION_FIELD[i], datas[i]);
			} else if (tableId.equalsIgnoreCase(HTTPUtils.USER_TABLEID)) {
				for (int i = 0; i < USER_FIELD.length; i++)
					data.put(USER_FIELD[i], datas[i]);
			} else if (tableId.equalsIgnoreCase(HTTPUtils.FENCE_TABLEID)) {
				for (int i = 0; i < FENCE_FIELD.length; i++)
					data.put(FENCE_FIELD[i], datas[i]);
			}
			return toNameValuePairData(data.toString(), tableId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<NameValuePair> deleteData(String tableId, String id) {
		List<NameValuePair>  formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("key", HTTPUtils.WEBAPIKEY));
		formparams.add(new BasicNameValuePair("tableid",tableId));
		formparams.add(new BasicNameValuePair("ids", id));
		return formparams;
	}

	public static List<NameValuePair> toNameValuePairData(String data,String tableid ){
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("key", HTTPUtils.WEBAPIKEY));
		formparams.add(new BasicNameValuePair("tableid",tableid));
		formparams.add(new BasicNameValuePair("loctype", "2"));
		formparams.add(new BasicNameValuePair("data", data.toString()));
		return formparams;
	}
}
