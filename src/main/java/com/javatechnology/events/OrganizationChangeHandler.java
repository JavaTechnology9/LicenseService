package com.javatechnology.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@EnableBinding(CustomChannel.class)
public class OrganizationChangeHandler {
	private static final Logger logger=LoggerFactory.getLogger(OrganizationChangeHandler.class);
	//@StreamListener("inboundOrgChanges")
	public void loggerSink(OrganizationServiceModel model) {
		logger.debug("received the event for organization id {}",model.getOrganizationId());
		
	}

}
