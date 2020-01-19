package com.am.spring.monitor.transferobjects;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RegistryResponse {

	private ServiceError serviceError;
	private String responseMsg;
	List<RegistryInfo> registryInfo;

	public RegistryResponse(ServiceError serviceError) {
		this.responseMsg = "Input Fault Occurred";
		this.serviceError = serviceError;
		this.registryInfo = new ArrayList<RegistryInfo>();
	}

}
