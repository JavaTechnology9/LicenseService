package com.javatechnology;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import com.javatechnology.config.SystemConfig;
import com.javatechnology.events.OrganizationServiceModel;
import com.javatechnology.service.model.Organization;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
//@EnableBinding(Sink.class)
@EnableResourceServer
public class LicensingServiceApplication {
	private static final Logger logger=LoggerFactory.getLogger(LicensingServiceApplication.class);
	@Autowired
	private SystemConfig config;
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	//@StreamListener(Sink.INPUT)
	public void loggerSink(OrganizationServiceModel orgChange) {
		logger.debug("received the event for organization id {}",orgChange.getOrganizationId());
		
	}
	/*@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		 RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
		    redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));
		    return new JedisConnectionFactory(redisStandaloneConfiguration);
		}*/
	
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setDatabase(0);
        //redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));
        

        JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return jedisConFactory;
    }
	
	@Bean
	public RedisTemplate<String, Organization> getRedisTemplate(){
		RedisTemplate<String, Organization> template=new RedisTemplate<String, Organization>();
		template.setEnableTransactionSupport(true);
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

}
