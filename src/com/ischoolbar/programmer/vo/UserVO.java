package com.ischoolbar.programmer.vo;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * �û�ʵ����
 * @author quanqian
 *
 */
@Component
public class UserVO  implements Serializable {
	private String username;//�û�������¼��
	private String password;//��¼����
	private String roleId;//������ɫid
	private String sex;//�Ա�0,����δ֪��1�����У�2����Ů
	private Integer age;//����
	private String address;//��ͥסַ
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}
