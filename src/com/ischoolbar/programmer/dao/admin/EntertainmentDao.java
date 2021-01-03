package com.ischoolbar.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.admin.Entertainment;

/**
 * entertainment”√ªßdao
 * @author quanqian
 *
 */
@Repository
public interface EntertainmentDao {
	public int findByEntertainmentname(String entertainmentname);
	public int add(Entertainment entertainment);
	public int edit(Entertainment entertainment);
	public int delete(String ids);
	public List<Entertainment> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
}
