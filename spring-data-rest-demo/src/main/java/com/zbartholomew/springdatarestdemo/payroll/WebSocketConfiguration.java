package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

// https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#websocket

/**
 * Configure WebSocket support for server side
 */
@Component
@EnableWebSocketMessageBroker // turns on WebSocket support.
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    // Prefix prepended to every message’s route.
    static final String MESSAGE_PREFIX = "/topic";

    /**
     * used to configure the endpoint on the backend for clients and server to link /payroll
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/payroll").withSockJS();
    }

    /**
     * used to configure the broker used to relay messages between server and client.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(MESSAGE_PREFIX);
        registry.setApplicationDestinationPrefixes("/app");
    }
}
