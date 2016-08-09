package com.amap.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import temp.CloudConnection;
import temp.HttpResult;
import temp.UserHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.Query;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMyLocationChangeListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.bean.DataProvider;
import com.amap.bean.Fence;
import com.amap.bean.History;
import com.amap.bean.Info;
import com.amap.bean.User;
import com.amap.protocol.HTTPService;
import com.amap.protocol.HTTPUtils;
import com.amap.protocol.Util;

public class MainActivity extends Activity implements OnCloudSearchListener,
		OnClickListener, OnMarkerClickListener, OnInfoWindowClickListener,
		AMapLocationListener, OnCheckedChangeListener, InfoWindowAdapter,
		LocationSource, OnMyLocationChangeListener, OnMapLoadedListener,
		OnMapClickListener {
	private MapView mMapView = null;
	private AMap aMap = null;
	private Button get = null;
	private Button sdkget = null;
	private Button post = null;
	private static EditText et_status = null;
	private DrawerLayout drawerLayout;
	private ImageView leftMenu;
	
	private static ArrayList<User> users = new ArrayList<User>();;
	private static ArrayList<Fence> fences = new ArrayList<Fence>();;
	private static ArrayList<History> historys = new ArrayList<History>();;
	private static Info responseInfo = null;
	private HTTPServiceReceiver httpServiceReceiver = null;

	private LocationManagerProxy aMapManager;
	private UiSettings mUiSettings;
	private boolean initMapCenter = true;
	private AMapLocation aMapLocation = null;
	private Marker currentMarker = null;
	
	/**
	 * ��ʾ���������ӶԻ���
	 */
	private void showNetworkDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("������ʾ");
		builder.setMessage(getResources().getString(R.string.noNetwork));
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.confirm,  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void checkBeforeUpload() {
		if (!Util.isNetworkConnected(this)) {
			showNetworkDialog();
			return;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(savedInstanceState);
		checkBeforeUpload();
		new Thread(){
			public void run() {
				CloudConnection ch = new UserHelper();
				/*Map<String, String> map = new HashMap<String, String>();
				map.put("userid", "18743300991");
				map.put("status", "0");
				System.out.println(ch.query(map));*/
				User user = new User("","2","3","","","","","山东省烟台市福山区昆仑山路靠近浪潮乐金数字移动通信有限公司(长江路)");
				HttpResult result = new HttpResult(ch.delete("25"));
				System.out.println(result.getInfo());
			};
		}.start();
	}

	private void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		registReveiver();
		initView(savedInstanceState);
		setListner();
	}

	private void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		get = (Button) findViewById(R.id.get);
		sdkget = (Button) findViewById(R.id.sdkget);
		post = (Button) findViewById(R.id.post);
		et_status = (EditText) findViewById(R.id.contexts);
		
		getActionBar().hide(); 
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		leftMenu = (ImageView) findViewById(R.id.leftmenu);
		leftMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		});
		
		initMap(savedInstanceState);
	}

	private void initMap(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.map);
		if (aMap == null) {
			aMap = mMapView.getMap();
		}
		mMapView.onCreate(savedInstanceState);
		locationSettings();
	}

	private void locationSettings() {
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(true);// ���õ�ͼĬ�ϵı������Ƿ���ʾ
		mUiSettings.setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		mUiSettings.setScaleControlsEnabled(true);
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	public void registReveiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.broadcasttest.http");
		registerReceiver(httpServiceReceiver, intentFilter);
	}

	private void setListner() {
		get.setOnClickListener(this);
		sdkget.setOnClickListener(this);
		post.setOnClickListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setOnMyLocationChangeListener(this);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		aMap.setOnMapClickListener(this);
		aMap.setInfoWindowAdapter(this); // �����Զ���InfoWindow��ʽ
	}

	public static class HTTPServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				int action = intent.getIntExtra("action", 0);
				String func = intent.getStringExtra("func");
				String datas = intent.getStringExtra("datas");
				parseJSONInfo(new JSONObject(datas));

				switch (action) {
				case HTTPUtils.DO_GET_USER:
					if (func.equalsIgnoreCase(HTTPUtils.LOCAL_QUERY))
						parseJSONUsers(new JSONObject(datas)
								.getJSONArray("datas"));
					break;
				case HTTPUtils.DO_GET_FENCE:
					if (func.equalsIgnoreCase(HTTPUtils.LOCAL_QUERY))
						parseJSONFences(new JSONObject(datas)
								.getJSONArray("datas"));
					break;
				case HTTPUtils.DO_GET_HISTORY:
					if (func.equalsIgnoreCase(HTTPUtils.LOCAL_QUERY))
						parseJSONHistorys(new JSONObject(datas)
								.getJSONArray("datas"));
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void parseJSONInfo(JSONObject info) {
		try {
			if (responseInfo == null)
				responseInfo = new Info();

			if (info.has("info"))
				responseInfo.setInfo(info.getString("info"));
			if (info.has("infocode"))
				responseInfo.setInfocode(info.getString("infocode"));
			if (info.has("status"))
				responseInfo.setStatus(info.getString("status"));
			if (info.has("count"))
				responseInfo.setCount(info.getInt("count"));
			if (info.has("_id"))
				responseInfo.setId(info.getString("_id"));
			if (info.has("tableid"))
				responseInfo.setTableId(info.getString("tableid"));

			et_status.setText(responseInfo.getInfo());
			System.out.println(responseInfo.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void parseJSONUsers(JSONArray jsonItems) {
		users.clear();
		try {
			for (int i = 0; i < responseInfo.getCount(); i++) {
				JSONObject jsonItem = jsonItems.getJSONObject(i);
				User user = new User(jsonItem.getString("_id"),
						jsonItem.getString("_image"),
						jsonItem.getString("_name"),
						jsonItem.getString("userid"),
						jsonItem.getString("password"),
						jsonItem.getString("status"),
						jsonItem.getString("_location"),
						jsonItem.getString("_address"));
				users.add(user);
				System.out.println(i + 1 + "=>" + user.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("INVALID_USER_KEY");
			e.printStackTrace();
		}
	}

	public static void parseJSONFences(JSONArray jsonItems) {
		fences.clear();
		try {
			for (int i = 0; i < responseInfo.getCount(); i++) {
				JSONObject jsonItem = jsonItems.getJSONObject(i);
				Fence fence = new Fence(jsonItem.getString("_name"),
						jsonItem.getString("_location"),
						jsonItem.getString("_address"),
						jsonItem.getString("userid1"),
						jsonItem.getString("userid2"),
						jsonItem.getString("radius"));
				fences.add(fence);
				System.out.println(i + 1 + "=>" + fence.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("INVALID_USER_KEY");
			e.printStackTrace();
		}
	}

	public static void parseJSONHistorys(JSONArray jsonItems) {
		historys.clear();
		try {
			for (int i = 0; i < responseInfo.getCount(); i++) {
				JSONObject jsonItem = jsonItems.getJSONObject(i);
				History history = new History(jsonItem.getString("_id"),
						jsonItem.getString("_name"),
						jsonItem.getString("_location"),
						jsonItem.getString("userid"),
						jsonItem.getString("_address"));
				historys.add(history);
				System.out.println(i + 1 + "=>" + history.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("INVALID_USER_KEY");
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		HTTPService httpService = null;
		if (v.getId() == R.id.get) {
			httpService = new HTTPService(HTTPUtils.DO_GET_HISTORY, this);
			httpService.setUrl(HTTPUtils.LOCAL_QUERY);
			httpService.setTableId(HTTPUtils.HISTORY_TABLEID);
			httpService.setKeywords(new String[] { "18743300991" });
			new Thread(httpService).start();
		} else if (v.getId() == R.id.sdkget) {
			doSDKGet("15543300991", HTTPUtils.USER_TABLEID);
		} else if (v.getId() == R.id.post) {
			if(aMapLocation!=null){
			httpService = new HTTPService(HTTPUtils.DO_POST, this);
			httpService.setUrl(HTTPUtils.INSERT);
//			httpService.setPostDatas(DataProvider.insertData(HTTPUtils.HISTORY_TABLEID,new String[]{ aMapLocation.getPoiName(), aMapLocation.getAddress(), "18743300991"}));
			httpService.setPostDatas(DataProvider.insertData(HTTPUtils.USER_TABLEID,new String[]{ "testName", aMapLocation.getAddress(), "18743300991","123","1"}));
//			httpService.setPostDatas(DataProvider.insertData(HTTPUtils.FENCE_TABLEID,new String[]{  aMapLocation.getPoiName(), aMapLocation.getAddress(), "18743300991","15543300991","1000"}));
//			httpService.setUrl(HTTPUtils.UPDATE);
//			httpService.setPostDatas(DataProvider.updateData(HTTPUtils.HISTORY_TABLEID,new String[]{ "1", aMapLocation.getPoiName(), aMapLocation.getAddress(), "18743300991"}));
//			httpService.setPostDatas(DataProvider.updateData(HTTPUtils.FENCE_TABLEID,new String[]{  "1",aMapLocation.getPoiName(), aMapLocation.getAddress(), "18743300991","15543300991","1000"}));
//			httpService.setPostDatas(DataProvider.updateData(HTTPUtils.USER_TABLEID,new String[]{ "13","testName", aMapLocation.getAddress(), "18743300991","123","1"}));
//			httpService.setUrl(HTTPUtils.DELETE);
//			httpService.setPostDatas(DataProvider.deleteData(HTTPUtils.HISTORY_TABLEID, "1"));
//			httpService.setPostDatas(DataProvider.deleteData(HTTPUtils.FENCE_TABLEID, "1"));
//			httpService.setPostDatas(DataProvider.deleteData(HTTPUtils.USER_TABLEID, "13"));
			new Thread(httpService).start();
			}
		}
	}

	public void doSDKGet(String keyword, String tableId) {

		// ��ʼ����ѯ��
		CloudSearch mCloudSearch = new CloudSearch(this);
		// ���ûص�����
		mCloudSearch.setOnCloudSearchListener(this);
		// �������ĵ㼰������Χ
		SearchBound bound = new SearchBound("ȫ��");
		try {
			// ���ò�ѯ���� mTableID�ǽ����ݴ洢�����ݹ���̨���á�
			Query mQuery = new CloudSearch.Query(tableId, keyword, bound);
			mCloudSearch.searchCloudAsyn(mQuery);
		} catch (AMapCloudException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail detail, int code) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCloudSearched(CloudResult result, int code) {
		// TODO Auto-generated method stub
		if (code == 0) {
			ArrayList<CloudItem> cloudItems = result.getClouds();
			for (int i = 0; i < cloudItems.size(); i++) {
				CloudItem cloudItem = cloudItems.get(i);
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
						cloudItem.getLatLonPoint().getLatitude(), cloudItem
								.getLatLonPoint().getLongitude())));
				CameraUpdateFactory.zoomTo(14);
				aMap.addMarker(setMarkerOptions(cloudItem));

			}
		} else
			Toast.makeText(this, "Not found!", 0).show();
	}

	private MarkerOptions setMarkerOptions(CloudItem cloudItem) {
		System.out.println(cloudItem.toString());
		MarkerOptions markerOption = new MarkerOptions();
		markerOption.position(new LatLng(cloudItem.getLatLonPoint()
				.getLatitude(), cloudItem.getLatLonPoint().getLongitude()));
		markerOption.title(cloudItem.getTitle());
		markerOption.draggable(true);
		markerOption.snippet("what is that!");
		// ������Դ洢�û�����
		// marker.setObject("11");
		return markerOption;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		marker.showInfoWindow();
		currentMarker=marker;
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	public void render(Marker marker, View view) {
		((ImageView) view.findViewById(R.id.custom_info_contents_badge))
				.setImageResource(R.drawable.touxiang);
		String titleString = marker.getTitle();// ���title�ı�
		TextView titleTextView = (TextView) view
				.findViewById(R.id.custom_info_contents_title);

		if (titleString != null) {
			SpannableString spannableString = new SpannableString(titleString);// ���ø���
			spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
					titleString.length(), 0);
			titleTextView.setText(spannableString);
		} else {
			titleTextView.setText("");
		}

		String snippet = marker.getSnippet();// ���ժҪ
		TextView snippetTextView = (TextView) view
				.findViewById(R.id.custom_info_contents_snippet);
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
					snippetText.length(), 0);
			snippetTextView.setText(snippetText);
		} else {
			snippetTextView.setText("");
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		System.out.println("onInfoWindowClick");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(httpServiceReceiver);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		mMapView.onDestroy();
	}

	/*
	 * �����λ��ťʱ����
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		if (aMapLocation != null) {
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
			CameraUpdateFactory.zoomTo(14);
//			MarkerOptions markerOption = new MarkerOptions();
//			markerOption.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
//			markerOption.title("��");
//			markerOption.draggable(true);
//			markerOption.snippet("�Ѷ�λ���ҵ�λ��!");
//			aMap.addMarker(markerOption);
			listener.onLocationChanged(aMapLocation);
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		if (aMapManager != null) {
			aMapManager.removeUpdates(this);
			aMapManager.destroy();
		}
		aMapManager = null;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		System.out.println("onCheckedChanged");
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (aLocation != null&& aLocation.getAMapException().getErrorCode() == 0) {
			aMapLocation = aLocation;
			if (initMapCenter) {
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aLocation.getLatitude(), aLocation.getLongitude())));
				CameraUpdateFactory.zoomTo(14);
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()));
				markerOption.title("��");
				markerOption.draggable(true);
				markerOption.snippet("�Ѷ�λ���ҵ�λ��!");
				aMap.addMarker(markerOption);
				initMapCenter = false;
			}
			System.out.println(aLocation.toString());
		}
	}

	@Override
	public void onMyLocationChange(Location aLocation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		/*if (aMapManager == null) {
			aMapManager = LocationManagerProxy.getInstance(this);
			aMapManager.requestLocationData(LocationProviderProxy.AMapNetwork,3 * 1000, 10, this);
		}*/
	}

	@Override
	public void onMapClick(LatLng latng) {
		// TODO Auto-generated method stub
		if(currentMarker!=null)
			currentMarker.hideInfoWindow();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		aMapLocation=aMapManager.getLastKnownLocation(provider);
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
