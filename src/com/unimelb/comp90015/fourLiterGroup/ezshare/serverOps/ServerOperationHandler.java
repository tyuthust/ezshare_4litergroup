package com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.unimelb.comp90015.fourLiterGroup.ezshare.utils.utils;

public class ServerOperationHandler {

	public static Resource publish(JSONObject jsonObject) throws OperationRunningException {

		System.out.println("Publish function");

		// create a json object to save the map in resource
		JSONObject shareResourceJsonObj = new JSONObject();
		// If the resource field was not given or not of the correct type
		if (null == jsonObject.get("resource")) {
			throw new OperationRunningException("missing resource");
		}
		shareResourceJsonObj.putAll((Map) jsonObject.get("resource"));
		Map shareResourceMap = new HashMap();
		shareResourceMap = (Map) shareResourceJsonObj.clone();
		System.out.println(shareResourceMap);

		// check if the the json data break the rule
		// The URI must be present, must be absolute and cannot be a file
		// scheme.

		// URI The URI must be present
		if (shareResourceMap.get("uri") != null) {
			String uriString = shareResourceJsonObj.get("uri").toString();

			if (uriString != "") {
				URI resourceUri = URI.create(uriString);
				// cannot be a file scheme and must be an absolute path
				if (resourceUri.isAbsolute()) {
					if (resourceUri.getScheme().contains("file")) {
						throw new OperationRunningException("cannot publish resource");
					}
				} else {
					throw new OperationRunningException("cannot publish resource");
				}

				// The Owner field must not be the single character "*".
				if (shareResourceMap.get("owner") == null) {
					shareResourceJsonObj.replace("onwer", "");
				} else if (shareResourceMap.get("owner") == ("*")) {
					throw new OperationRunningException("cannot publish resource");
				}
			} else {
				throw new OperationRunningException("cannot publish resource");
			}
		} else {
			throw new OperationRunningException("cannot publish resource");
		}

		return generatingResourceHandler(shareResourceJsonObj);
	}

	public static String[] exchange(JSONObject jsonObject) throws OperationRunningException {

		System.out.println("Exchange function");
		// create a json object to save the map in resource
		JSONArray jsonArray = new JSONArray();
		jsonArray = (JSONArray) jsonObject.get("serverList");

		String[] ezservers = null;

		// a pointer which is used to assign
		int counter = 0;

		if (jsonArray != null) {
			ezservers = new String[jsonArray.size()];
			// do exchange function
			List<JSONObject> jsonobjectList = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonobjectList.add((JSONObject) jsonArray.get(i));
			}
			// Looking at each element in the jsonobjectList
			for (JSONObject jsonOb : jsonobjectList) {
				ezservers[counter] = jsonOb.get("hostname").toString() + ":" + jsonOb.get("port").toString();
				counter++;
			}
			// invalid server list
			for (String string : ezservers) {
				if (!utils.isIPandPort(string)) {
					throw new OperationRunningException("missing or invalide server List");
				}
			}
		} else {
			// server list is missing
			throw new OperationRunningException("missing or invalide server List");
		}
		return ezservers;
	}
	
	public static Resource remove(JSONObject jsonObject) throws OperationRunningException {
		System.out.println("Remove function");//TODO: add logger
		JSONObject removeResourceJsonObj = new JSONObject();
		removeResourceJsonObj.putAll((Map) jsonObject.get("resource"));
		
		// If the resource field was not given or not of the correct type
		System.out.println(removeResourceJsonObj);//TODO: add logger
		
		if (removeResourceJsonObj.isEmpty()) {
			if(removeResourceJsonObj.get("uri")==null || removeResourceJsonObj.get("uri").equals("")){
				throw new OperationRunningException("invalide resource");
			}
			if(removeResourceJsonObj.get("channel")==null || removeResourceJsonObj.get("owner")==null){
				throw new OperationRunningException("invalide resource");
			}
			String uriString = removeResourceJsonObj.get("uri").toString();
			URI resourceUri = URI.create(uriString);
			// cannot be a file scheme and must be an absolute path
			if (!resourceUri.isAbsolute()) {
				throw new OperationRunningException("cannot publish resource");
			}
		} else{
			throw new OperationRunningException("missing resource");
		}
		

		return generatingResourceHandler(removeResourceJsonObj);
	}
	
	public static Resource share(JSONObject jsonObject) throws OperationRunningException {
		System.out.println("Share function");
		// TODO: Check rules breaker

		JSONObject shareResourceJsonObj = new JSONObject();
		// If the resource field was not given or not of the correct type
		if (null == jsonObject.get("resource") || null == jsonObject.get("secret")) {
			throw new OperationRunningException("missing resource and/or secret");
		}
		shareResourceJsonObj.putAll((Map) jsonObject.get("resource"));
		System.out.println(shareResourceJsonObj);

		// check if the the json data break the rule

		// The URI must be present, must be absolute and must be a file scheme.
		String uriString = shareResourceJsonObj.get("uri").toString();

		// URI The URI must be present
		if (null == uriString || uriString.equals("")) {
			throw new OperationRunningException("cannot share resource");
		}
		URI resourceUri = URI.create(uriString);

		// must be absolute and must be a file scheme
		if (resourceUri.isAbsolute()) {
			if (!resourceUri.getScheme().contains("file")) {
				throw new OperationRunningException("cannot share resource");
			}
		} else {
			throw new OperationRunningException("cannot share resource");
		}

		// The Owner field must not be the single character "*".
		if (shareResourceJsonObj.get("owner").toString().equals("*")) {
			throw new OperationRunningException("cannot share resource");
		}

		return generatingResourceHandler(shareResourceJsonObj);
	}
	
	public static Resource query(JSONObject jsonObject) throws OperationRunningException{
		System.out.println("query function");
		System.out.println(jsonObject.toString());
		JSONObject queryResourceJsonObj = new JSONObject();

		if(null == jsonObject.get("resourceTemplate")){
			throw new OperationRunningException("missing resourceTemplate");			
		}
		queryResourceJsonObj.putAll((Map) jsonObject.get("resourceTemplate"));


		if (queryResourceJsonObj.isEmpty()) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		
		//TODO: check info!
		//there are other stuff for checking
		// The URI must be present, must be absolute and must be a file scheme.
		String uriString = queryResourceJsonObj.get("uri").toString();
		String chanString = queryResourceJsonObj.get("channel").toString();


		// URI The URI must be present
		if (null == uriString) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		if (null == chanString) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		return generatingResourceHandler(queryResourceJsonObj);
	}
	
	public static Resource fetch(JSONObject jsonObject) throws OperationRunningException {
		System.out.println("fetch function");

		JSONObject fetchResourceJsonObj = new JSONObject();
		fetchResourceJsonObj.putAll((Map) jsonObject.get("resourceTemplate"));

		if (fetchResourceJsonObj.isEmpty()) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		// The URI must be present, must be absolute and must be a file scheme.
		String uriString = fetchResourceJsonObj.get("uri").toString();
		String chanString = fetchResourceJsonObj.get("channel").toString();

		// URI The URI must be present
		if (null == uriString || uriString.equals("")) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		if (null == chanString || chanString.equals("")) {
			throw new OperationRunningException("missing resourceTemplate");
		}
		return generatingResourceHandler(fetchResourceJsonObj);

	}

	private static Resource generatingResourceHandler(JSONObject ResourceJsonObj) throws OperationRunningException {

		// create a new resource and set its value
		Resource resource = new Resource();
		if(null == ResourceJsonObj){
			new OperationRunningException("invalid resource");
		}
		
		//name
		if (!ResourceJsonObj.containsKey("name")) {
			throw new OperationRunningException("missing resource");
		}
		if(null != ResourceJsonObj.get("name")){
			resource.setName(ResourceJsonObj.get("name").toString());
		}
		else{
			resource.setName("");
		}

		System.out.println("The resource name:" + resource.getName());

		// clone the jsonobject to a hashmap
		Map map = new HashMap();
		map = (Map) ResourceJsonObj.clone();
		// TODO: whether name is needed when creating resource
		if (map.get("channel") != null) {// otherwise, there is an exception
			// when channel is null
			resource.setChannel(ResourceJsonObj.get("channel").toString());
		} else {
			throw new OperationRunningException("missing resource");
		}
		System.out.println("The resource channel:" + resource.getChannel());

		resource.setDescription(ResourceJsonObj.get("description").toString());
		System.out.println("The resource description:" + resource.getDescription());

		if (map.containsKey("owner")) {
			if(null != ResourceJsonObj.get("owner")){
				resource.setOwner(ResourceJsonObj.get("owner").toString());
			}
			else {
				resource.setOwner(ResourceJsonObj.get("owner").toString());
			}

		} else {
			throw new OperationRunningException("missing resource");
		}
		System.out.println("The resource owner:" + resource.getOwner());

		resource.setURI(ResourceJsonObj.get("uri").toString());
		System.out.println("The resource uri:" + resource.getURI());



		if (map.containsKey("ezserver")) {
			JSONArray jsonArray = new JSONArray();
			jsonArray = (JSONArray) ResourceJsonObj.get("ezserver");
			if(null!= jsonArray){
				String[] servers = new String[jsonArray.size()];
				for (int i = 0; i < jsonArray.size(); i++) {
					String r = jsonArray.get(i).toString();
					servers[i] = r;
				}
				resource.setEZServer(servers);
			}

		} else {
			throw new OperationRunningException("missing resource");
		}
		
		
		if (map.containsKey("tags")) {
			JSONArray jsonArray = new JSONArray();
			jsonArray = (JSONArray) ResourceJsonObj.get("tags");
			if(null!= jsonArray){
				String[] tags = new String[jsonArray.size()];
				for (int i = 0; i < jsonArray.size(); i++) {
					String r = jsonArray.get(i).toString();
					tags[i] = r;
				}
				resource.setTags(tags);
				List<String> tagList = new ArrayList<String>();
				for (String string : resource.getTags()) {
					tagList.add(string);
				}
				System.out.println("The resource:" + tagList.toString());
			}
			else{
				System.out.println("The resource:" + null);
			}

		} else {
			throw new OperationRunningException("missing resource");
		}



		// JSONObject result = new JSONObject();
		// if (true) {
		// result.put("response", "successful");
		// }

		return resource;
	}

}
