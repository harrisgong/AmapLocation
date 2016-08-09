package temp;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResult {
	private String info = null;
	private String infocode = null;
	private String status = null;
	private Integer count = 0;
	private String id = null;

	public HttpResult(String result) {
		try {
			parseJSONInfo(new JSONObject(result));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getInfo() {
		return info;
	}

	public String getInfocode() {
		return infocode;
	}

	public String getStatus() {
		return status;
	}

	public Integer getCount() {
		return count;
	}

	public String getId() {
		return id;
	}

	public void parseJSONInfo(JSONObject result) {
		try {
			if (result.has("info"))
				info = result.getString("info");
			if (result.has("infocode"))
				infocode = result.getString("infocode");
			if (result.has("status"))
				status = result.getString("status");
			if (result.has("count"))
				count = result.getInt("count");
			if (result.has("_id"))
				id = result.getString("_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "HttpResult [info=" + info + ", infocode=" + infocode
				+ ", status=" + status + ", count=" + count + ", id=" + id
				+ "]";
	}
}
