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

import com.ischoolbar.programmer.entity.admin.Entertainment;
import com.ischoolbar.programmer.entity.admin.Role;
import com.ischoolbar.programmer.entity.admin.Entertainment;
import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.admin.EntertainmentService;
import com.ischoolbar.programmer.service.admin.RoleService;
import com.ischoolbar.programmer.service.admin.EntertainmentService;

/**
 * �û����������
 * @author quanqian
 *
 */
@RequestMapping("/admin/entertainment")
@Controller
public class EntertainmentController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private EntertainmentService entertainmentService;
	
	/**
	 * �û��б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/entertainment_list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		model.setViewName("entertainment/entertainment_index");
		return model;
	}
	
	/**
	 * ��ȡ�û��б�
	 * @param page
	 * @param entertainmentname
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value="/entertainment_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name="ename",required=false,defaultValue="") String ename ,
			@RequestParam(name="state",required=false,defaultValue="") String state){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ename", ename);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		
		List<Entertainment> list = entertainmentService.findList(queryMap);
		ret.put("rows", list);
		ret.put("total", entertainmentService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * ����û�
	 * @param entertainment
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Entertainment entertainment){
		Map<String, String> ret = new HashMap<String, String>();
		if(entertainment == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���û���Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "����д������Ŀ���ƣ�");
			return ret;
		}
		
		if(isExist(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "��������Ŀ�����Ѿ����ڣ����������룡");
			return ret;
		}
		if(entertainmentService.add(entertainment) <= 0){
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
	 * @param entertainment
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Entertainment entertainment){
		Map<String, String> ret = new HashMap<String, String>();
		if(entertainment == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "����д������Ŀ���ƣ�");
			return ret;
		}
		if(entertainmentService.edit(entertainment) <= 0){
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
		if(entertainmentService.delete(ids) <= 0){
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
		String ssssss = request.getContextPath();
		ret.put("filepath",request.getContextPath() + "/resources/upload/" + filename );
		return ret;
	}
	/**
	 * �жϸ�������Ŀ�����Ƿ����
	 * @param entertainmentname
	 * @return
	 */
	private boolean isExist(String entertainmentname){
		int num = entertainmentService.findByEntertainmentname(entertainmentname);
		
		// ��ѯ��������Ŀ������������0,˵��������Ŀ���ƴ���,���������
		if(num > 0) {
			return true;
		}
		// �����
		return false;
	}
}
