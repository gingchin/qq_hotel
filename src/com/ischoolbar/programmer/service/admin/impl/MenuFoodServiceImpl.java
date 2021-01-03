package com.ischoolbar.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.dao.admin.MenuFoodDao;
import com.ischoolbar.programmer.entity.admin.MenuFood;
import com.ischoolbar.programmer.service.admin.MenuFoodService;
/**
 * menuFood”√ªßserviceimpl
 * @author quanqian
 *
 */
@Service
public class MenuFoodServiceImpl implements MenuFoodService {

	@Autowired
	private MenuFoodDao menuFoodDao;
	
	@Override
	public int findByMenuFoodname(String menuFoodname) {
		
		return menuFoodDao.findByMenuFoodname(menuFoodname);
	}

	@Override
	public int add(MenuFood menuFood) {
		
		return menuFoodDao.add(menuFood);
	}

	@Override
	public int edit(MenuFood menuFood) {
		
		return menuFoodDao.edit(menuFood);
	}

	@Override
	public int delete(String ids) {
		
		return menuFoodDao.delete(ids);
	}

	@Override
	public List<MenuFood> findList(Map<String, Object> queryMap) {
		
		return menuFoodDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		
		return menuFoodDao.getTotal(queryMap);
	}


}
