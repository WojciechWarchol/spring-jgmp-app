package com.wojto;

import com.wojto.exception.handler.CustomHandlerExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.wojto.dao")
@EntityScan("com.wojto.model")
@EnableTransactionManagement
@EnableCaching
@EnableJms
public class EventAppConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAppConfig.class);

//    @Autowired
//    private ConnectionFactory connectionFactory;

    public static void main(String[] args) {
        SpringApplication.run(EventAppConfig.class, args);
    }

    @Bean
    public HandlerExceptionResolver customHandlerExceptionResolver() {
        return new CustomHandlerExceptionResolver();
    }

//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        return factory;
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return new ActiveMQConnectionFactory("tcp://localhost:61616");
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        return new JmsTemplate(connectionFactory());
//    }

/*    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setErrorHandler(new MyErrorHandler());
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }*/

//    @Bean
//    public DefaultMessageListenerContainer messageListener() {
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setConnectionFactory(this.connectionFactory);
//        container.setDestinationName("spring-jgmp-app.ticket.book.queue");
//        container.setMessageListener(new JmsTicketBookingReceiver());
//        return container;
//    }
}
