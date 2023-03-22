package com.wojto;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TomcatInitializer {

    public static void main(String[] args) {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
//        tomcat.setBaseDir("target/tomcat");
//        tomcat.getHost().setAppBase("eventapp");

        String contextPath = "/";
        String webappDir = new File("src/main/resources").getAbsolutePath();
        StandardContext context = (StandardContext) tomcat.addWebapp(contextPath, webappDir);

//        Context tomcatContex = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
//        tomcatContex.addParameter("contextConfigLocation", "/WEB-INF/conf/applicationContext.xml");
//        tomcatContex.addApplicationListener(ContextLoaderListener.class.getName());

//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.register(EventAppConfig.class);
//
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
//        Tomcat.addServlet(tomcatContex, "dispatcherServlet", (Servlet) dispatcherServlet);
//        tomcatContex.addServletMappingDecoded("/", "dispatcherServlet");

        try {
            tomcat.start();
        } catch (
                LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }
}
