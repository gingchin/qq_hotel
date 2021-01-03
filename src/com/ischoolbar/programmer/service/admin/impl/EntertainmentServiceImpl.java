package com.ischoolbar.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.dao.admin.EntertainmentDao;
import com.ischoolbar.programmer.entity.admin.Entertainment;
import com.ischoolbar.programmer.service.admin.EntertainmentService;
/**
 * entertainment”√ªßserviceimpl
 * @author quanqian
 *
 */
@Service
public class EntertainmentServiceImpl implements EntertainmentService {

	@Autowired
	private EntertainmentDao entertainmentDao;
	
	@Override
	public int findByEntertainmentname(String entertainmentname) {
		
		return entertainmentDao.findByEntertainmentname(entertainmentname);
	}

	@Override
	public int add(Entertainment entertainment) {
		
		return entertainmentDao.add(entertainment);
	}

	@Override
	public int edit(Entertainment entertainment) {
		
		return entertainmentDao.edit(entertainment);
	}

	@Override
	public int delete(String ids) {
		
		return entertainmentDao.delete(ids);
	}

	@Override
	public List<Entertainment> findList(Map<String, Object> queryMap) {
		
		return entertainmentDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		
		return entertainmentDao.getTotal(queryMap);
	}


}
