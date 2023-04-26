package com.akp.ws.soap.config;

import org.apache.cxf.Bus;

import java.sql.SQLException;

import javax.xml.ws.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akp.ws.soap.CustomerOrderWsImpl;

@Configuration
public class WebServiceConfig {
	@Autowired
	private Bus bus;
	
	@Bean
	public Endpoint endPoint() throws ClassNotFoundException, SQLException {
		Endpoint endpoint = new EndpointImpl(bus, new CustomerOrderWsImpl());
		endpoint.publish("/customerorderws");
		return endpoint;
	}
	
	
}
