package com.amap.protocol;

public class HTTPUtils {
	public final static String USER_TABLEID = "5795c9e67bbf1978ba6916a4";
	public final static String FENCE_TABLEID = "579a9bafafdf52627ba21f48";
	public final static String HISTORY_TABLEID = "5799bc78afdf52627b9dd3ac";
	public final static String TABLEID = "5795c9e67bbf1978ba6916a4";
	public final static String WEBAPIKEY = "9a69c4d5e45bae5fb1e17adf8c5b2ce7";

	// CREATE
	public final static String CREATE_TABLE = "http://yuntuapi.amap.com/datamanage/table/create?";
	// QUERY
	public final static String LOCAL_QUERY = "http://yuntuapi.amap.com/datasearch/local?";
	public final static String AROUND_QUERY = "http://yuntuapi.amap.com/datasearch/around?";
	public final static String POLYGON_QUERY = "http://yuntuapi.amap.com/datasearch/polygon?";
	public final static String ID_QUERY = "http://yuntuapi.amap.com/datasearch/id?";
	// INSERT
	public final static String INSERT = "http://yuntuapi.amap.com/datamanage/data/create?";
	public final static String INSERT_BATCH_= "http://yuntuapi.amap.com/datamanage/data/batchcreate?";
	public final static String INSERT_BATCH_IMPORTSTATUS = "http://yuntuapi.amap.com/datamanage/batch/importstatus?";
	// UPDATE
	public final static String UPDATE = "http://yuntuapi.amap.com/datamanage/data/update?";
	// DELETE
	public final static String DELETE = "http://yuntuapi.amap.com/datamanage/data/delete?";

	public final static int DO_SDKGET = 1;
	public final static int DO_POST = 2;
	public final static int DO_GET_USER = 3;
	public final static int DO_GET_HISTORY = 4;
	public final static int DO_GET_FENCE = 5;

}
