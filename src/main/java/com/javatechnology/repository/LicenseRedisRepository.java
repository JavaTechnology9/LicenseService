package com.javatechnology.repository;

import com.javatechnology.service.model.Organization;

public interface LicenseRedisRepository {
	void saveLicense(Organization organization);
	void updateLicense(Organization organization);
	Organization findLicense(String organizationId);

}
