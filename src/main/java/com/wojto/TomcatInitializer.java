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

        String contextPath = "/";
        String webappDir = new File("src/main/resources").getAbsolutePath();
        StandardContext context = (StandardContext) tomcat.addWebapp(contextPath, webappDir);

        try {
            tomcat.start();
        } catch (
                LifecycleException e) {
            throw new RuntimeException(e);
        }

        tomcat.getServer().await();
    }
}
