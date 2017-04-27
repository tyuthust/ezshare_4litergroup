package com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerOpHandlerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest1() throws Exception{
		//file scheme
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
//		ServerOperationHandler serverOpHandler1=new ServerOperationHandler();
		jsonObject2.put("channel","");
		jsonObject2.put("owner","");
		jsonObject2.put("url","file://www.bilibili.com");
		jsonObject1.put("resource",jsonObject2);
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}

	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest2() throws Exception{
		//null resource
		JSONObject jsonObject1=new JSONObject();
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}
	
	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest3() throws Exception{
		//file scheme
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
		jsonObject2.put("owner","");
		jsonObject2.put("url","git\\Ezshare_4LiterGroup");
		jsonObject1.put("resource",jsonObject2);
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}
	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest4() throws Exception{
		//owner with *
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
		jsonObject2.put("owner","four*liter");
		jsonObject2.put("url","git\\Ezshare_4LiterGroup");
		jsonObject1.put("resource",jsonObject2);
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}
	
	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest5() throws Exception{
		//url==null
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
		jsonObject2.put("owner","four*liter");
		jsonObject1.put("resource",jsonObject2);
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}
	
	@Test(expected=com.unimelb.comp90015.fourLiterGroup.ezshare.serverOps.OperationRunningException.class)
	public void publishTest6() throws Exception{
		//url is ""
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
		jsonObject2.put("owner","four*liter");
		jsonObject2.put("url","");
		jsonObject1.put("resource",jsonObject2);
		ServerOperationHandler.publish(jsonObject1);
		//assertEquals("cannot publish resource",serverOpHandler1.publish(jsonObject1));
	}
}