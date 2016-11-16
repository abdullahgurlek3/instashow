package com.agrlek.instashow.serialize;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.json.JSONObject;

public class serializeObject implements Serializable {
	public LinkedHashMap<String, JSONObject> followedAr;
	public LinkedHashMap<String, JSONObject> followsAr;
	public LinkedHashMap<String, JSONObject> fansAr;
	public LinkedHashMap<String, JSONObject> mutualsAr;
	public LinkedHashMap<String, JSONObject> nonFollowersAr;

}
