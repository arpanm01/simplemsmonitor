/**
 * 
 */
package com.am.spring.monitor.entityobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author ARPANM
 *
 */

@Entity
public class Registry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Id;

	@Column(unique = true)
	private String appName;

	@Column(unique = true)
	private String statusUri;
	private String state;
	private String appApiUri;
	private String dateRegistered;
	private String startTime;
	private long upTime;
	private String lastDownTime;
	private long downtime;
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getStatusUri() {
		return statusUri;
	}

	public void setStatusUri(String statusUri) {
		this.statusUri = statusUri;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getDowntime() {
		return downtime;
	}

	public void setDowntime(long downtime) {
		this.downtime = downtime;
	}

	public String getAppApiUri() {
		return appApiUri;
	}

	public void setAppApiUri(String appApiUri) {
		this.appApiUri = appApiUri;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}

	public String getLastDownTime() {
		return lastDownTime;
	}

	public void setLastDownTime(String lastDownTime) {
		this.lastDownTime = lastDownTime;
	}

}
