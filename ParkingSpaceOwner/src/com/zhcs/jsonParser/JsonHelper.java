package com.zhcs.jsonParser;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
	public static Map toMap(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		Map result = new HashMap();
		Iterator iter = jsonObject.keys();
		String key = null;
		String value = null;
		
		while(iter.hasNext()){
			key = (String)iter.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}
}
