package com.tomcat.servlet;

import com.alibaba.fastjson.JSON;
import com.tomcat.http.Request;
import com.tomcat.http.Response;
import com.tomcat.http.Servlet;


public class SecondServlet extends Servlet {

	@Override
	public void doGet(Request request, Response response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(Request request, Response response) {
	    String str = JSON.toJSONString(request.getParameters(),true);
	    response.write(str,200);
	}
	
}
