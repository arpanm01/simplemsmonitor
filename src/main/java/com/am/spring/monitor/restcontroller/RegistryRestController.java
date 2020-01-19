package com.am.spring.monitor.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.am.spring.monitor.transferobjects.RegistryRequest;
import com.am.spring.monitor.transferobjects.RegistryResponse;


/**
 * @author ARPANM
 *
 */
@RestController
@RequestMapping(value = "/service", headers = "Accept=application/json")
class RegistryRestController {

	@Autowired
	RegistryBackingController RegistryBackingController;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public RegistryResponse newEntry(@RequestBody RegistryRequest newRegistry) {
		return RegistryBackingController.newEntry(newRegistry);
	}

	
	@RequestMapping(value = "/deregister", method = RequestMethod.POST)
	public void deleteEntry(@RequestBody String applicationId) {
		RegistryBackingController.deleteEntry(applicationId);
	}

	
	@RequestMapping(value = "/getallregistry", method = RequestMethod.GET)
	public RegistryResponse getAllEntry() {
		return RegistryBackingController.getAllEntry();
		
	}



}