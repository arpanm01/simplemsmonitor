package com.am.spring.monitor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.am.spring.monitor.entityobjects.Registry;
import com.am.spring.monitor.entityobjects.RegistryRepository;
import com.am.spring.monitor.transferobjects.RegistryInfo;

@Service
public class MonitoringDBService{

	@Autowired
	RegistryRepository registryRepository;
	
    public List<RegistryInfo> getAllRegistrys() {
        List<Registry> registrys = new ArrayList<Registry>();
        List<RegistryInfo> registryInfos = new ArrayList<RegistryInfo>();
        
        registryRepository.findAll().forEach(registry -> registrys.add(registry));
        
        if (!registrys.isEmpty())
        {
        	registrys.forEach(registry->registryInfos.add(getRegistryInfo(registry)));
        }
        return registryInfos;
    }

   	public Registry getRegistryById(int id) {
        return registryRepository.findById(id).get();
    }

    public List<RegistryInfo> saveOrUpdate(Registry registry) {
        Registry saved = registryRepository.save(registry);
        return Stream.of(getRegistryInfo(saved)).collect(Collectors.toList());
    }

    public void delete(int id) {
        registryRepository.deleteById(id);
   }
	
    public List<Integer> getAllRegisteredAppIds()
    {
    	List <Integer> lstAppIds = new ArrayList<Integer>();
    	registryRepository.findAll().forEach(e->lstAppIds.add(e.getId()));
    	return lstAppIds;
    }
    
    private RegistryInfo getRegistryInfo(Registry registry) {
		RegistryInfo rI = new RegistryInfo();  
		rI.setId(registry.getId());
		rI.setAppName(registry.getAppName());
		rI.setStatusUri(registry.getStatusUri());
		rI.setAppApiUri(registry.getAppApiUri());
		rI.setDowntime(registry.getDowntime());
		rI.setState(registry.getState());
		rI.setDateRegistered(registry.getDateRegistered());
		rI.setUpTime(registry.getUpTime());
		rI.setStartTime(registry.getStartTime());
		return rI;
	}
}
