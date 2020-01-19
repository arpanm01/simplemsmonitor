/**
 * 
 */
package com.am.spring.monitor.transferobjects;

import lombok.Data;

/**
 * @author ARPANM
 *
 */
@Data
public class RegistryRequest {

	private String protocol;
	private String host;
	private String port;
	private String path;
	private String appName;
	private String state;

}
