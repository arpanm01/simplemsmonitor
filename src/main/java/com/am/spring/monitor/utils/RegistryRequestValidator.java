package com.am.spring.monitor.utils;

import com.am.spring.monitor.transferobjects.RegistryRequest;
import com.am.spring.monitor.transferobjects.RegistryResponse;
import com.am.spring.monitor.transferobjects.ServiceError;

public class RegistryRequestValidator {
	
	public static RegistryResponse validateRequest (RegistryRequest newRegistry)
	{
		if(CommonUtils.isEmpty(newRegistry.getProtocol()))
		{
			newRegistry.setProtocol("http");
		}

		if(CommonUtils.isEmpty(newRegistry.getHost()) || CommonUtils.isEmpty(newRegistry.getPort()))
		{
			ServiceError err = new ServiceError ("ERR01-HOSTPORT","Host & Port cant be empty. Required to access Actuator");
			return new RegistryResponse(err);
		}
		
		else return null;
	}

}
