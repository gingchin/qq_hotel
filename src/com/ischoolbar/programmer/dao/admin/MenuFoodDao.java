package com.ischoolbar.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.admin.MenuFood;

/**
 * menuFood”√ªßdao
 * @author quanqian
 *
 */
@Repository
public interface MenuFoodDao {
	public int findByMenuFoodname(String menuFoodname);
	public int add(MenuFood menuFood);
	public int edit(MenuFood menuFood);
	public int delete(String ids);
	public List<MenuFood> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
}
