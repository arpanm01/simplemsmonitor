/**
 * 
 */
package com.am.spring.monitor.transferobjects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ARPANM
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RegistryInfo {
	
    private int Id;
	private String appName;
	private String state;
	private long downtime;
	private String statusUri;
	private String appApiUri;
	private String dateRegistered;
	private String startTime;
	private long upTime;
	
}
