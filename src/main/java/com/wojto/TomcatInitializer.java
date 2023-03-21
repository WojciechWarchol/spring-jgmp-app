package com.wojto;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

public class TomcatInitializer {

    public static void main(String[] args) {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context tomcatContex = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
        tomcatContex.addApplicationListener(ContextLoaderListener.class.getName());

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(EventAppConfig.class);


        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        Tomcat.addServlet(tomcatContex, "dispatcherServlet", (Servlet) dispatcherServlet);
        tomcatContex.addServletMappingDecoded("/", "dispatcherServlet");

        try {
            tomcat.start();
        } catch (
                LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }
}
