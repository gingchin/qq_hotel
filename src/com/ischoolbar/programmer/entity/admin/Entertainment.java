package com.ischoolbar.programmer.entity.admin;

import org.springframework.stereotype.Component;

/**
 * �û�ʵ����
 * @author quanqian
 *
 */
@Component
public class Entertainment {

	private long id;
	private String ename;
	private String photo;
	private String state;// 0-������,1-����,2-ά����
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
	

	
	
	
}
