package com.ischoolbar.programmer.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.admin.MenuFood;
import com.ischoolbar.programmer.entity.admin.Role;
import com.ischoolbar.programmer.entity.admin.MenuFood;
import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.admin.MenuFoodService;
import com.ischoolbar.programmer.service.admin.RoleService;
import com.ischoolbar.programmer.service.admin.MenuFoodService;

/**
 * �û����������
 * @author quanqian
 *
 */
@RequestMapping("/admin/menu_food")
@Controller
public class MenuFoodController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuFoodService menuFoodService;
	
	/**
	 * �û��б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/food_list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		model.setViewName("menu_food/menu_food_index");
		return model;
	}
	
	/**
	 * ��ȡ�û��б�
	 * @param page
	 * @param menuFoodname
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value="/food_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name="foodname",required=false,defaultValue="") String foodname ){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("foodname", foodname);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		
		List<MenuFood> list = menuFoodService.findList(queryMap);
		ret.put("rows", list);
		ret.put("total", menuFoodService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * ����û�
	 * @param menuFood
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(MenuFood menuFood){
		Map<String, String> ret = new HashMap<String, String>();
		if(menuFood == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���û���Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(menuFood.getFoodname())){
			ret.put("type", "error");
			ret.put("msg", "����д������");
			return ret;
		}
		
		if(isExist(menuFood.getFoodname())){
			ret.put("type", "error");
			ret.put("msg", "�ò����Ѿ����ڣ����������룡");
			return ret;
		}
		if(menuFoodService.add(menuFood) <= 0){
			ret.put("type", "error");
			ret.put("msg", "���ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ӳɹ���");
		return ret;
	}
	
	/**
	 * �༭�û�
	 * @param menuFood
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(MenuFood menuFood){
		Map<String, String> ret = new HashMap<String, String>();
		if(menuFood == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(menuFood.getFoodname())){
			ret.put("type", "error");
			ret.put("msg", "����д������");
			return ret;
		}
		if(menuFoodService.edit(menuFood) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�޸ĳɹ���");
		return ret;
	}
	
	/**
	 * ����ɾ���û�
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(String ids){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(ids)){
			ret.put("type", "error");
			ret.put("msg", "ѡ��Ҫɾ�������ݣ�");
			return ret;
		}
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		if(menuFoodService.delete(ids) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�û�ɾ��ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�û�ɾ���ɹ���");
		return ret;
	}
	
	/**
	 * �ϴ�ͼƬ
	 * @param photo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadPhoto(MultipartFile photo,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		if(photo == null){
			ret.put("type", "error");
			ret.put("msg", "ѡ��Ҫ�ϴ����ļ���");
			return ret;
		}
		if(photo.getSize() > 1024*1024*1024){
			ret.put("type", "error");
			ret.put("msg", "�ļ���С���ܳ���10M��");
			return ret;
		}
		//��ȡ�ļ���׺
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
		if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��jpg,jpeg,gif,png��ʽ��ͼƬ��");
			return ret;
		}
		String savePath = request.getServletContext().getRealPath("/") + "/resources/upload/";
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()){
			//�������ڸ�Ŀ¼���򴴽�Ŀ¼
			savePathFile.mkdir();
		}
		String filename = new Date().getTime()+"."+suffix;
		try {
			//���ļ�������ָ��Ŀ¼
			photo.transferTo(new File(savePath+filename));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			ret.put("type", "error");
			ret.put("msg", "�����ļ��쳣��");
			e.printStackTrace();
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "ɾ���ɹ���");
		ret.put("filepath",request.getServletContext().getContextPath() + "/resources/upload/" + filename );
		return ret;
	}
	/**
	 * �жϸò����Ƿ����
	 * @param menuFoodname
	 * @return
	 */
	private boolean isExist(String menuFoodname){
		int num = menuFoodService.findByMenuFoodname(menuFoodname);
		
		// ��ѯ��������������0,˵����������,���������
		if(num > 0) {
			return true;
		}
		// �����
		return false;
	}
}
