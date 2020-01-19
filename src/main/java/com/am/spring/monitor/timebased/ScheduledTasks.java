package com.am.spring.monitor.timebased;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.am.spring.monitor.controller.AppDetailsFetchService;
import com.am.spring.monitor.controller.MonitoringDBService;
import com.am.spring.monitor.entityobjects.Registry;
import com.am.spring.monitor.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class ScheduledTasks {

	private final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Autowired
	MonitoringDBService mdbs;
	@Autowired
	AppDetailsFetchService actConsumer;

	@Scheduled(fixedRate = 5000)
	public void RefreshRegistryInfo() {

		LOGGER.info("Executed at : {}", dateFormat.format(new Date()));

		List<Integer> allRegEntAppId = mdbs.getAllRegisteredAppIds();

		if (!allRegEntAppId.isEmpty()) {

			allRegEntAppId.forEach(entry -> fetchAndSave(entry));

		} else {
			LOGGER.info("No Registered Entries at " + dateFormat.format(new Date()));
		}
	}

	private void fetchAndSave(Integer entry) {

		if(entry>0)
		{
			Registry regEntry  = mdbs.getRegistryById(entry);

			Registry copyRegistry=regEntry;

			if(!regEntry.getStatusUri().isEmpty()) {


				try {
					String oldState = regEntry.getState();
					String newState = actConsumer.fetchServiceState(regEntry.getStatusUri());

					String upTimeUri = regEntry.getStatusUri().substring(0, regEntry.getStatusUri().indexOf("/health"))+"/metrics/process.uptime";

					copyRegistry.setId(regEntry.getId());
					copyRegistry.setState(newState);
					copyRegistry.setDateRegistered(regEntry.getDateRegistered());

					if("UP".equals(newState))
					{copyRegistry.setUpTime(actConsumer.fetchUpTime(upTimeUri));}
					
					if (("DOWN".equals(oldState) || "OUT_OF_SERVICE".equals(oldState)) && "UP".equals(newState))
					{
						copyRegistry.setLastDownTime(null);
						copyRegistry.setDowntime(regEntry.getDowntime() + CommonUtils.dateDifferenceinLong(regEntry.getLastDownTime(),CommonUtils.Date2String(new Date())));
					}	

					if (("DOWN".equals(newState) || "OUT_OF_SERVICE".equals(newState)) && "UP".equals(oldState))
					{
						copyRegistry.setLastDownTime(CommonUtils.Date2String(new Date()));
					}		

					
					mdbs.delete(entry);						
					mdbs.saveOrUpdate(copyRegistry);
					LOGGER.info("All Scheduled executed### " );

				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}



}