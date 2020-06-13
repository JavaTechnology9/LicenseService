package com.javatechnology.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatechnology.client.OrganizationDiscoveryClient;
import com.javatechnology.client.OrganizationFeignClient;
import com.javatechnology.client.RestTemplateClient;
import com.javatechnology.repository.LicenseRepository;
import com.javatechnology.service.model.License;
import com.javatechnology.service.model.Organization;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
@Service
public class LicenseService {
	@Autowired
	private LicenseRepository repository;
	@Autowired
	private OrganizationDiscoveryClient discoveryClient;
	@Autowired
	private RestTemplateClient restTemplateClient;
	@Autowired
	private OrganizationFeignClient feignClient;
	
	public Optional<License> getLicense(String licenseId,String clientType,String organizationId) {
		Optional<License> findById = repository.findById(licenseId);
		Set<Organization> organizationInfo=new HashSet<>();
		organizationInfo.add(retriveOrganizationInfo(clientType,organizationId));
		findById.get().setOrganization(organizationInfo);
		return findById;
		
	}
	
	@HystrixCommand(//fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"),
                     @HystrixProperty(name="maxQueueSize", value="10")},
            commandProperties={
                     @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                     @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
                     @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                     @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                     @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}
    )
	// Thread SEMAPHORE
	public List<License> getLicensesByOrg(String organizationId){
		//randomRunLog();
		List<License> lic=new ArrayList<>();
		//Optional<License> findById = repository.findById(organizationId);
		 License license=new License().withId(organizationId)
				.withOrganizationId(UUID.randomUUID().toString())
				.withProductName("Test Product Name")
				.withLicenseType("TestLicenseType");
		 lic.add(license);
		return lic;
		
	}
	public List<License> buildFallbackMehtod(String organizationId){
		List<License>lic=new ArrayList<>();
		License license=new License();
		license.setId("Random Id");
		license.setLicenseType("fallback type");
		license.setOrganizationId(organizationId);
		license.setProductName("Sorry NO Licenses are available ");
		lic.add(license);
		return lic;
	}
	private void randomRunLog() {
		Random random=new Random();
		int ran=random.nextInt((3-1)+1)+1;
		if(ran==3)sleep();
		
	}
	private void sleep() {
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Organization retriveOrganizationInfo(String clientType,String organizationId) {
		Organization oranization =null;
		switch(clientType) {
		case "discovery":
			System.out.println("i am using discovery client");
			 oranization = discoveryClient.getOranization(clientType,organizationId);
			 break;
		case "template":
			System.out.println("i am using template client");
			 oranization = restTemplateClient.getOranization(organizationId);
			 break;
			 
		case "feignClient":
			System.out.println("i am using Feign client");
			 oranization = feignClient.getOranization(organizationId);
			 break;
			
		}
		return oranization;
		
	}
	public Organization getOrganizationFromCache(String organizationId) {
		return restTemplateClient.getOrganizationFromCache(organizationId);
	}
	
	public Optional<License> getLicense(String licenseId) {
		/*return new License().withId(licenseId)
				.withOrganizationId(UUID.randomUUID().toString())
				.withProductName("Test Product Name")
				.withLicenseType("TestLicenseType");*/
		return repository.findById(licenseId);
		
	}
	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());
		repository.save(license);
		
	}
	public void updateLicense(License licesne) {
		repository.save(licesne);
		
	}
	public void deleteLicense(License license) {
		repository.delete(license);
		
	}
}
