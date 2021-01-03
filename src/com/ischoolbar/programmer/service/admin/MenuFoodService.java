package com.ischoolbar.programmer.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.entity.admin.MenuFood;
import com.ischoolbar.programmer.entity.admin.MenuFood;

/**
 * menuFood”√ªßservice
 * @author quanqian
 *
 */
@Service
public interface MenuFoodService {
	public int findByMenuFoodname(String menuFoodname);
	public int add(MenuFood menuFood);
	public int edit(MenuFood menuFood);
	public int delete(String ids);
	public List<MenuFood> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
}
