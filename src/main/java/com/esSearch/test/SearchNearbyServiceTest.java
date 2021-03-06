package com.esSearch.test;

import com.esSearch.model.SearchResult;
import com.esSearch.search.SearchNearbyService;


import java.util.Map;


public class SearchNearbyServiceTest {
	
   SearchNearbyService service=new SearchNearbyService();
	
	//这是我所在的坐标值，湖南长沙
	private double myLon = 112.938815;//东经
	private double myLat = 28.227878; //北纬
	private String myName = "Tom老师";//我的名字	
	
//	@Before

	public void initData(){
		int total = 100000;//10W
		try{
			//建库建表
			service.recreateIndex();
			//随机产生10W条数据
			service.addDataToIndex(myLat, myLon, total);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\n========= 数据初始化工作完毕,共随机产生" + total + "条数据 =========\n");
	}
	

//	@Ignore
	public void searchNearby(){
		
		int size = 10, r = 100;
		
		System.out.println("开始获取距离" + myName + r + "m以内人");
		
		SearchResult result = service.search(myLon, myLat, r, size, null);
		
		System.out.println("共找到" + result.getTotal() + "个人,优先显示" + size + "人，查询耗时" + result.getUseTime() + "秒");
		for (Map<String,Object> people : result.getData()) {
			
			String nickName = people.get("nickName").toString();
			
			String location = people.get("location").toString();
			Object geo = people.get("geoDistance");
			
			System.out.println(nickName + "，" + 
							"微信号:" + people.get("wxNo") + 
							"，性别:" + people.get("sex") + 
							",距离" + myName + geo + "米" +
							"(坐标：" + location + ")");
		}
		
		System.out.println("以上" + size + "人显示在列表中......");
		System.out.println("各位老司机们尽管使出你们的撩妹技巧！！！");
		
	}
	
	
}
