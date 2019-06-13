package com.tomcat.servlet;


import com.tomcat.http.Request;
import com.tomcat.http.Response;
import com.tomcat.http.Servlet;

public class FirstServlet extends Servlet {

	
	@Override
	public void doGet(Request request, Response response) {
		doPost(request, response);
	}

	
	@Override
	public void doPost(Request request, Response response) {
		String param = "name";
	    String str = request.getParameter(param);
	    response.write(param + ":" + str,200);
	}
	
}
