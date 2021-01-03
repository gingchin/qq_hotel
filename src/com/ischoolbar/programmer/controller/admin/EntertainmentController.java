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
 * 用户管理控制器
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
	 * 用户列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/entertainment_list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		model.setViewName("entertainment/entertainment_index");
		return model;
	}
	
	/**
	 * 获取用户列表
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
	 * 添加用户
	 * @param entertainment
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Entertainment entertainment){
		Map<String, String> ret = new HashMap<String, String>();
		if(entertainment == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的用户信息！");
			return ret;
		}
		if(StringUtils.isEmpty(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "请填写娱乐项目名称！");
			return ret;
		}
		
		if(isExist(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "该娱乐项目名称已经存在，请重新输入！");
			return ret;
		}
		if(entertainmentService.add(entertainment) <= 0){
			ret.put("type", "error");
			ret.put("msg", "添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功！");
		return ret;
	}
	
	/**
	 * 编辑用户
	 * @param entertainment
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Entertainment entertainment){
		Map<String, String> ret = new HashMap<String, String>();
		if(entertainment == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的信息！");
			return ret;
		}
		if(StringUtils.isEmpty(entertainment.getEname())){
			ret.put("type", "error");
			ret.put("msg", "请填写娱乐项目名称！");
			return ret;
		}
		if(entertainmentService.edit(entertainment) <= 0){
			ret.put("type", "error");
			ret.put("msg", "修改失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "修改成功！");
		return ret;
	}
	
	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(String ids){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(ids)){
			ret.put("type", "error");
			ret.put("msg", "选择要删除的数据！");
			return ret;
		}
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		if(entertainmentService.delete(ids) <= 0){
			ret.put("type", "error");
			ret.put("msg", "用户删除失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "用户删除成功！");
		return ret;
	}
	
	/**
	 * 上传图片
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
			ret.put("msg", "选择要上传的文件！");
			return ret;
		}
		if(photo.getSize() > 1024*1024*1024){
			ret.put("type", "error");
			ret.put("msg", "文件大小不能超过10M！");
			return ret;
		}
		//获取文件后缀
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
		if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "请选择jpg,jpeg,gif,png格式的图片！");
			return ret;
		}
		String savePath = request.getServletContext().getRealPath("/") + "/resources/upload/";
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()){
			//若不存在改目录，则创建目录
			savePathFile.mkdir();
		}
		String filename = new Date().getTime()+"."+suffix;
		try {
			//将文件保存至指定目录
			photo.transferTo(new File(savePath+filename));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			ret.put("type", "error");
			ret.put("msg", "保存文件异常！");
			e.printStackTrace();
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		String ssssss = request.getContextPath();
		ret.put("filepath",request.getContextPath() + "/resources/upload/" + filename );
		return ret;
	}
	/**
	 * 判断该娱乐项目名称是否存在
	 * @param entertainmentname
	 * @return
	 */
	private boolean isExist(String entertainmentname){
		int num = entertainmentService.findByEntertainmentname(entertainmentname);
		
		// 查询到娱乐项目名称数量大于0,说明娱乐项目名称存在,不允许添加
		if(num > 0) {
			return true;
		}
		// 允许加
		return false;
	}
}
