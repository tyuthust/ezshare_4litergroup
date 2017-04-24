package com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.jndi.toolkit.url.Uri;
import com.unimelb.comp90015.fourLiterGroup.ezshare.utils.utils;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class ServerOperationHandler {

	public static Resource publish(JSONObject jsonObject) throws OperationRunningException {

		System.out.println("Publish function");

		// create a json object to save the map in resource
		JSONObject shareResourceJsonObj = new JSONObject();
		//If the resource field was not given or not of the correct type
		if(null == jsonObject.get("resource")){
			throw new OperationRunningException("missing resource");
		}
		shareResourceJsonObj.putAll((Map) jsonObject.get("resource"));
		System.out.println(shareResourceJsonObj);
		
		//check if the the json data break the rule
		
		//The URI must be present, must be absolute and cannot be a file scheme.
		String uriString = shareResourceJsonObj.get("uri").toString();
		
		//URI The URI must be present
		if(null ==uriString || uriString.equals("")){
			throw new OperationRunningException("cannot publish resource");
		}
		URI resourceUri = URI.create(uriString);
		
		// cannot be a file scheme
		// TODO: must be absolute
		if (resourceUri.getScheme().contains("file:")) {
			throw new OperationRunningException("cannot publish resource");
		}
		
		//The Owner field must not be the single character "*". 
		if(shareResourceJsonObj.get("owner").toString().equals("*")){
			throw new OperationRunningException("cannot publish resource");
		}
				
		return generatingResourceHandler(shareResourceJsonObj);
	}

	public static Resource share(JSONObject jsonObject) throws OperationRunningException {
		System.out.println("Share function");
		JSONObject result = new JSONObject();

		// TODO: Check rules breaker
		
		JSONObject shareResourceJsonObj = new JSONObject();
		//If the resource field was not given or not of the correct type
		if(null == jsonObject.get("resource")||null == jsonObject.get("secret")){
			throw new OperationRunningException("missing resource and/or secret");
		}
		shareResourceJsonObj.putAll((Map) jsonObject.get("resource"));
		System.out.println(shareResourceJsonObj);
		
		//check if the the json data break the rule
		
		//The URI must be present, must be absolute and must be a file scheme.
		String uriString = shareResourceJsonObj.get("uri").toString();
		
		//URI The URI must be present
		if(null ==uriString || uriString.equals("")){
			throw new OperationRunningException("cannot share resource");
		}
		URI resourceUri = URI.create(uriString);
		
		// must be a file scheme
		// TODO: must be absolute
		if (!resourceUri.getScheme().contains("file:")) {
			throw new OperationRunningException("cannot share resource");
		}
		
		//The Owner field must not be the single character "*". 
		if(shareResourceJsonObj.get("owner").toString().equals("*")){
			throw new OperationRunningException("cannot share resource");
		}

		return generatingResourceHandler(shareResourceJsonObj);
	}
	
	private static Resource generatingResourceHandler(JSONObject shareResourceJsonObj) throws OperationRunningException {
		
		// String values must not contain the "\0" character,
		// nor start or end with whitespace.
		// The server may silently remove such characters
		
		//remove all start and end whitespace
		//remove "\0"
		shareResourceJsonObj.forEach((key, value) -> {
			utils.trimFirstAndLastChar((String) value, " ");
			((String) value).replaceAll("\\0", "");

		});

		// create a new resource and set its value
		Resource resource = new Resource();
		if(null == shareResourceJsonObj.get("name")){
			throw new OperationRunningException("missing resource");
		}
		resource.setName(shareResourceJsonObj.get("name").toString());
		System.out.println("The resource name:" + resource.getName());

		// clone the jsonobject to a hashmap
		Map map = new HashMap();
		map = (Map) shareResourceJsonObj.clone();

		if (map.get("channel") != null) {// otherwise, there is an exception
			// when channel is null
			resource.setChannel(shareResourceJsonObj.get("channel").toString());
		} else {
			throw new OperationRunningException("missing resource");
		}
		System.out.println("The resource channel:" + resource.getChannel());

		resource.setDescription(shareResourceJsonObj.get("description").toString());
		System.out.println("The resource description:" + resource.getDescription());

		if (map.get("owner") != null) {
			resource.setOwner(shareResourceJsonObj.get("owner").toString());
		} else {
			throw new OperationRunningException("missing resource");
		}
		System.out.println("The resource owner:" + resource.getOwner());

		resource.setURI(shareResourceJsonObj.get("uri").toString());
		System.out.println("The resource uri:" + resource.getURI());

		// ezserver will not be transported when using publish command
		System.out.println("The resource ezserver:" + "null");

		if (map.get("tags") != null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray = (JSONArray) shareResourceJsonObj.get("tags");
			String[] tags = new String[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				String r = jsonArray.get(i).toString();
				tags[i] = r;
			}
			resource.setTags(tags);
		}
		else{
			throw new OperationRunningException("missing resource");
		}

		List<String> tagList = new ArrayList<String>();
		for (String string : resource.getTags()) {
			tagList.add(string);
		}
		System.out.println("The resource:" + tagList.toString());

//		JSONObject result = new JSONObject();
//		if (true) {
//			result.put("response", "successful");
//		}

		return resource;
	}


	


}
