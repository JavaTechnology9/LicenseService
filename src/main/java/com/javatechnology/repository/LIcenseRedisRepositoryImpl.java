package com.javatechnology.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.javatechnology.service.model.License;
import com.javatechnology.service.model.Organization;
@Repository
public class LIcenseRedisRepositoryImpl implements LicenseRedisRepository{
	private static final String HASH_NAME="License";
	@Autowired
	private RedisTemplate<String, Organization> template;
	private HashOperations<String, String, Organization> operations;
	
	@PostConstruct
	private void init() {
		operations=template.opsForHash();
	}

	@Override
	public void saveLicense(Organization organization) {
		operations.put(HASH_NAME, organization.getId(), organization);
		
	}

	@Override
	public void updateLicense(Organization organization) {
		operations.put(HASH_NAME, organization.getId(), organization);
		
		
	}

	@Override
	public Organization findLicense(String licenseId) {
		operations.get(HASH_NAME, licenseId);
		return null;
	}

}
