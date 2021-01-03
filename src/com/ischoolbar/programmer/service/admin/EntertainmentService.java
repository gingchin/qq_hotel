package com.ischoolbar.programmer.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.entity.admin.Entertainment;
import com.ischoolbar.programmer.entity.admin.Entertainment;

/**
 * entertainment”√ªßservice
 * @author quanqian
 *
 */
@Service
public interface EntertainmentService {
	public int findByEntertainmentname(String entertainmentname);
	public int add(Entertainment entertainment);
	public int edit(Entertainment entertainment);
	public int delete(String ids);
	public List<Entertainment> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
}
