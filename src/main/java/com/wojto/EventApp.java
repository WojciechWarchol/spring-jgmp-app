package com.wojto;

import com.wojto.facade.BookingFacadeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Configuration
@ComponentScan("com.wojto.*")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.wojto.dao")
@EntityScan("com.wojto.model")
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableCaching
public class EventApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventApp.class);

    private static ApplicationContext context;

    @Autowired
    DataSource dataSource;

    BookingFacadeImpl bookingFacade;

    public static void main(String[] args) throws ParseException {

        LOGGER.info("Initializing Application Context");
        context = new AnnotationConfigApplicationContext(EventApp.class);
        LOGGER.info("Initialized");

        LOGGER.info("Creating BookingFacade and performing autowiring");
        BookingFacadeImpl bookingFacade = context.getBean(BookingFacadeImpl.class);
        LOGGER.info("Initialized BookingFacade with dependencies");
        System.out.println(bookingFacade.getEventById(1).getTitle());
        System.out.println(bookingFacade.getUserById(1).getName());
        System.out.println(bookingFacade.getEventsByTitle("IT", 1, 0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(bookingFacade.getEventsForDay(dateFormat.parse("13-04-2023"), 1, 0));

        LOGGER.info("Ending Application");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        EntityManagerFactory manager = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.wojto.model");

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setDatabase(Database.MYSQL);
//        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
//        jpaVendorAdapter.setShowSql(true);

        emf.setJpaVendorAdapter(jpaVendorAdapter);
//        emf.setJpaProperties(additionalProperties());

        return emf;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_jgmp");
        dataSource.setUsername("jgmpuser");
        dataSource.setPassword("password1234");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
        return transactionManager;
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManager() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factory.setShared(true);
        return factory;
    }

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("events");
//    }

//    @Bean public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//    }
}
