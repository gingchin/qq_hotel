package com.ischoolbar.programmer.vo;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * 用户实体类
 * @author quanqian
 *
 */
@Component
public class UserVO  implements Serializable {
	private String username;//用户名，登录名
	private String password;//登录密码
	private String roleId;//所属角色id
	private String sex;//性别0,代表未知，1代表男，2代表女
	private Integer age;//年龄
	private String address;//家庭住址
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
