package com.javatechnology.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javatechnology.config.SystemConfig;
import com.javatechnology.service.LicenseService;
import com.javatechnology.service.model.License;
import com.javatechnology.service.model.Organization;

@RestController
@RequestMapping(value="v1/Organizations/{organizationId}")
public class LicesneController {
	@Autowired
	private LicenseService service;
	@Autowired
	private SystemConfig config;
	
	//@RequestMapping(value="/{licenseId}")
	public List<License> getLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		System.out.println("System Config info :" +config.getProperty());
		//return service.getLicense(licenseId);
		return service.getLicensesByOrg(licenseId);
		
	}
	@RequestMapping(value="/{licenseId}")
	public Organization getOrganizationFromCache(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		return service.getOrganizationFromCache(licenseId);
	}
	
	
	@RequestMapping(value="/{licenseId}/{clientType}")
	public Optional<License> getLicenseFromOrgination(
			@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,
			@PathVariable("clientType") String clientType) {
		System.out.println("System Config info :" +config.getProperty());
		return service.getLicense(licenseId,clientType,organizationId);
		
	}
	
	
	
	
	
	@RequestMapping(value="/saveLicense", method = RequestMethod.POST)
	public String saveLicense(@RequestBody License license){
		service.saveLicense(license);
		return String.format("This is the post");
	}
	@RequestMapping(value="/updateLicense", method = RequestMethod.PUT)
	public String updateLicense(@RequestBody License license){
		service.updateLicense(license);
		return String.format("This is the put");
	}
	
	@RequestMapping(value="/deleteLicense", method = RequestMethod.DELETE)
	public String deleteLicense(@RequestBody License license){
		service.deleteLicense(license);
		return String.format("This is the delete");
	}
}
