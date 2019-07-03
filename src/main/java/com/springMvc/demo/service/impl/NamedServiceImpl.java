package com.springMvc.demo.service.impl;


import com.springMvc.annotation.Autowired;
import com.springMvc.annotation.Service;
import com.springMvc.demo.service.INamedService;
import com.springMvc.demo.service.IService;

@Service("myName")
public class NamedServiceImpl implements INamedService {

	@Autowired
	IService service;
	
}
