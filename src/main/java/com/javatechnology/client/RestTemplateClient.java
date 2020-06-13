package com.javatechnology.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.javatechnology.repository.LicenseRedisRepository;
import com.javatechnology.service.model.License;
import com.javatechnology.service.model.Organization;
@Component
public class RestTemplateClient {
	private static final Logger logger=LoggerFactory.getLogger(RestTemplateClient.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LicenseRedisRepository repository;
	
	public Organization getOranization(String organizationId) {
		ResponseEntity<Organization> exchange = restTemplate.exchange("http://organzationservice/v1/organizations/{organizationId}", HttpMethod.GET, null, Organization.class, organizationId);
		return exchange.getBody();
		//http://organzationservice/v1/organizations/
		//http://		192.168.0.7/v1/organization
		
	}
	
	private  Organization getRedisCache(String licenseId) {
		return repository.findLicense(licenseId);
		
	}
	public Organization getOrganizationFromCache(String licenseId) {
		Organization redisCache = getRedisCache(licenseId);
		if(redisCache!=null) {
			logger.debug("i have successfully retrived an license{} object from Redis cache{}",licenseId,redisCache);
			return redisCache;
		}
		 redisCache = getOranization(licenseId);
		 if(redisCache!=null) {
			 cacheOrganizationObject(redisCache);
		 }
		return redisCache;
		
	}

	private void cacheOrganizationObject(Organization redisCache) {
		repository.saveLicense(redisCache);
		
	}

}
