package com.springMvc.demo.mvc.action;

import com.springMvc.annotation.Autowired;
import com.springMvc.annotation.Controller;
import com.springMvc.annotation.RequestMapping;
import com.springMvc.annotation.RequestParam;
import com.springMvc.demo.service.INamedService;
import com.springMvc.demo.service.IService;
import com.springMvc.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Controller
@RequestMapping("/web")
public class FirstAction {

	@Autowired
	private IService service;
	
	@Autowired("myName") private INamedService namedService;
	
	
	@RequestMapping("/query/.*.json")
//	@GPResponseBody
	public ModelAndView query(HttpServletRequest request, HttpServletResponse response,
							  @RequestParam(value="name",required=false) String name,
							  @RequestParam("addr") String addr){
		//out(response,"get params name = " + name);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", name);
		model.put("addr", addr);
		return new ModelAndView("first.pgml",model);
	}
	
	
	@RequestMapping("/add.json")
	public ModelAndView add(HttpServletRequest request,HttpServletResponse response){
		out(response,"this is json string");
		return null;
	}
	
	
	
	public void out(HttpServletResponse response,String str){
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
