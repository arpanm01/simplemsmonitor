package com.am.spring.monitor.restcontroller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.am.spring.monitor.controller.AppDetailsFetchService;
import com.am.spring.monitor.controller.MonitoringDBService;
import com.am.spring.monitor.entityobjects.Registry;
import com.am.spring.monitor.transferobjects.RegistryInfo;
import com.am.spring.monitor.transferobjects.RegistryRequest;
import com.am.spring.monitor.transferobjects.RegistryResponse;
import com.am.spring.monitor.transferobjects.ServiceError;
import com.am.spring.monitor.utils.CommonUtils;
import com.am.spring.monitor.utils.RegistryRequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;



@Service
public class RegistryBackingController {

	@Autowired
	MonitoringDBService dbService;
	@Autowired
	AppDetailsFetchService actConsumer;

	private final Logger LOGGER = LoggerFactory.getLogger(RegistryBackingController.class);

	public RegistryResponse newEntry(RegistryRequest newRegistry) {

		LOGGER.info("Input to Register service : " + newRegistry );

		RegistryResponse rs = RegistryRequestValidator.validateRequest(newRegistry); // TODO : Spring validation can be used for host and port.

		if (rs == null) {
			String responseStatus = "SUCCESS";
			ServiceError err = new ServiceError();
			List<RegistryInfo> lst = null;
			String state="";

			Registry registry = new Registry();
			registry.setAppName(newRegistry.getAppName());
			registry.setStatusUri(createHealthUri(newRegistry));
			registry.setAppApiUri(createBaseUri(newRegistry).append(newRegistry.getPath()).toString().trim());
			registry.setDateRegistered(CommonUtils.Date2String(new Date()));

			//Fetch STATE from clients Actuator ep
			try {
				state = actConsumer.fetchServiceState(registry.getStatusUri());
				LOGGER.info("State of App " + registry.getAppName() + " : "  + state);
				registry.setState(state);
			} catch (Exception e) {
				registry.setState("NOT ACCESSIBLE");
				responseStatus = "FAILED";
				err.setErrorCode("ERR-FETCH-001");
				err.setErrorMsg(e.getLocalizedMessage());
				LOGGER.error("Failed while getting the Health Info.Try Again.");
				e.printStackTrace();
			}
			
			
			//Fetch Metrics from clients actuator ep
			if("UP".equals(state)) {
				
				registry.setLastDownTime(null);
				registry.setDowntime(new Long(0));
				
				try {
					LOGGER.info("START TIME CHECK URL: " + createStartTimeUri(newRegistry));
					registry.setStartTime(actConsumer.fetchStartTime(createStartTimeUri(newRegistry)));
					LOGGER.info("START TIME CHECK Date: " + actConsumer.fetchStartTime(createStartTimeUri(newRegistry)));
					
					LOGGER.info("UPTIME CHECK URL: " + createUpTimeUri(newRegistry));
					registry.setUpTime(actConsumer.fetchUpTime(createUpTimeUri(newRegistry)));
					LOGGER.info("UPTIME CHECK Time in Minutes: " + createUpTimeUri(newRegistry));
				
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			
			else if ("DOWN".equals(state) || "OUT_OF_SERVICE".equals(state) )
			{
				Long downtime= new Long(0);
				
				registry.setDowntime(downtime);
				
				registry.setLastDownTime(CommonUtils.Date2String(new Date()));
				
			}
			
			
			//Save registry to DB
			try {
				lst = dbService.saveOrUpdate(registry);

			} catch (Exception e) {
				responseStatus = "FAILED TO REGISTER";
				err.setErrorCode("ERR-PERSIST-001");
				err.setErrorMsg(e.getLocalizedMessage());
				LOGGER.error("Failed while persisiting the registry.Try Again.");
				e.printStackTrace();
			}

			
			//sampler
			//LOGGER.info("CURL CHECK" + executeCurlCheck(registry.getAppApiUri()));

			return new RegistryResponse(err, responseStatus, lst);

		} else
			return rs;
	}

	public void deleteEntry(String applicationId) {
		LOGGER.info("Input to Deregister service: " + applicationId);
		if (applicationId != null && !applicationId.isEmpty()) {
			try {
				dbService.delete(Integer.parseInt(applicationId));
			} catch (Exception e) {
				LOGGER.error("Failed while deleting the registry.Try Again.");
				e.printStackTrace();
			}
		}
	}

	public RegistryResponse getAllEntry() {
		LOGGER.info("Inside getAllEntry");
		RegistryResponse response = new RegistryResponse();
		try {
			List<RegistryInfo> lstRegistry = dbService.getAllRegistrys();
			response.setRegistryInfo(lstRegistry);
		} catch (Exception e) {
			response.setResponseMsg("FAILED TO FETCH");
			response.setServiceError(new ServiceError("ERR-FETCH-002", e.getLocalizedMessage()));
			LOGGER.error("Failed while fetching all saved registry.Try Again.");
			e.printStackTrace();
		}
		return response;
	}

	
	protected StringBuffer createBaseUri(final RegistryRequest newRegistry) {
		return new StringBuffer().append(newRegistry.getProtocol()).append("://").append(newRegistry.getHost()).append(":").append(newRegistry.getPort()).append("/");
	}

	protected String createHealthUri(final RegistryRequest newRegistry) {
		return createBaseUri(newRegistry).append("actuator/health").toString().trim();
	}

	protected String createStartTimeUri(final RegistryRequest newRegistry) {
		return createBaseUri(newRegistry).append("actuator/metrics/process.start.time").toString().trim();
	}

	protected String createUpTimeUri(final RegistryRequest newRegistry) {
		return createBaseUri(newRegistry).append("actuator/metrics/process.uptime").toString().trim();
	}


	//Probably will be unused. Future enhancement. TODO
	public String executeCurlCheck(String url)
	{
		Process process=null;
		String command = "curl -k " + url;
		try {
			process = Runtime.getRuntime().exec(command);

			StringBuffer s = new StringBuffer("");
			for (int i=0;i< process.getInputStream().available();i++)
			{
				s.append(process.getInputStream().read());
			}

			return s.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(process!=null)
				process.destroy();
		}
		return null;


	}

}
