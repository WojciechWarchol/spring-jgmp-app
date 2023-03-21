package com.wojto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class EventAppInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAppInitializer.class);

    private static AnnotationConfigWebApplicationContext context;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        LOGGER.info("Initializing Application Context");
        context = new AnnotationConfigWebApplicationContext();
        context.register(EventAppConfig.class);

        LOGGER.info("Servlet Registration");


        ServletRegistration.Dynamic servletRegistration =
                servletContext.addServlet("mvc", new DispatcherServlet(context));
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

        LOGGER.info("Initialized");
    }
}
