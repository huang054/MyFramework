package com.springMvc;

import com.springMvc.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServcer {
    public static void main(String[] args) throws LifecycleException, ServletException {
        String path=System.getProperty("user.dir")+ File.separator+"target"+ File.separator+"classes"+ File.separator+"web.xml";
        System.out.println(path);
        Tomcat tomcat = new Tomcat();
       // tomcat.setPort(9000);

        Host host =tomcat.getHost();
        host.setAppBase("webapps");
        host.setName("localhost");

        Context context=tomcat.addWebapp("/",System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"webapp");
        if(context instanceof StandardContext){
            StandardContext standardContext=(StandardContext)context;

            standardContext.setDefaultContextXml(path);
         Wrapper wrapper= tomcat.addServlet("/","myServlet",new MyServlet());
         wrapper.addMapping("/hello");
            Connector connector = new Connector();
            connector.setURIEncoding("UTF-8");
            connector.setPort(9090);
         tomcat.getService().addConnector(connector);
        }
       tomcat.start();
       tomcat.getServer().await();
    }
}
