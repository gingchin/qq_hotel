package com.ischoolbar.programmer.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.admin.Role;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.admin.RoleService;
import com.ischoolbar.programmer.service.admin.UserService;
import com.ischoolbar.programmer.util.ExportExcelUtil;
import com.ischoolbar.programmer.util.ExportExcelWrapper;
import com.ischoolbar.programmer.vo.UserVO;

/**
 * �û����������
 * @author quanqian
 *
 */
@RequestMapping("/admin/user")
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * �û��б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		model.addObject("roleList", roleService.findList(queryMap));
		model.setViewName("user/list");
		return model;
	}
	
	/**
	 * ��ȡ�û��б�
	 * @param page
	 * @param username
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name="username",required=false,defaultValue="") String username,
			@RequestParam(name="roleId",required=false) Long roleId,
			@RequestParam(name="sex",required=false) Integer sex
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", username);
		queryMap.put("roleId", roleId);
		queryMap.put("sex", sex);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userService.findList(queryMap));
		ret.put("total", userService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * ����û�
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���û���Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "����д�û�����");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type", "error");
			ret.put("msg", "����д���룡");
			return ret;
		}
		if(user.getRoleId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������ɫ��");
			return ret;
		}
		if(isExist(user.getUsername(), 0l)){
			ret.put("type", "error");
			ret.put("msg", "���û����Ѿ����ڣ����������룡");
			return ret;
		}
		if(userService.add(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�û����ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ɫ��ӳɹ���");
		return ret;
	}
	
	/**
	 * �༭�û�
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���û���Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "����д�û�����");
			return ret;
		}
//		if(StringUtils.isEmpty(user.getPassword())){
//			ret.put("type", "error");
//			ret.put("msg", "����д���룡");
//			return ret;
//		}
		if(user.getRoleId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������ɫ��");
			return ret;
		}
		if(isExist(user.getUsername(), user.getId())){
			ret.put("type", "error");
			ret.put("msg", "���û����Ѿ����ڣ����������룡");
			return ret;
		}
		if(userService.edit(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�û����ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ɫ��ӳɹ���");
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
		if(userService.delete(ids) <= 0){
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
		ret.put("msg", "�û�ɾ���ɹ���");
		ret.put("filepath",request.getServletContext().getContextPath() + "/resources/upload/" + filename );
		return ret;
	}
	/**
	 * �жϸ��û����Ƿ����
	 * @param username
	 * @param id
	 * @return
	 */
	private boolean isExist(String username,Long id){
		User user = userService.findByUsername(username);
		if(user == null)return false;
		if(user.getId().longValue() == id.longValue())return false;
		return true;
	}
	
	/**
     * Excel����
     * @return
     */
    @RequestMapping(value = "/exportUserList")
    @ResponseBody
    public Map<String, Object> exportUserList(HttpServletResponse response,HttpServletRequest request,Page page,
			@RequestParam(name="username",required=false,defaultValue="") String username,
			@RequestParam(name="roleId",required=false) Long roleId,
			@RequestParam(name="sex",required=false) Integer sex
			) {
    	Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", username);
		queryMap.put("roleId", roleId);
		queryMap.put("sex", sex);
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 1000);
		ret.put("rows", userService.findList(queryMap));
        List<User> list = userService.findList(queryMap);
        
        // ���������ֺ���ת��
        List listVO = this.changeUserToVO(list);
        
        String[] columnNames = {"�û���", " ����"," ������ɫ "," �Ա�","����","��ַ"};
        String fileName = "userList"+ System.currentTimeMillis();
        ExportExcelWrapper<User> util = new ExportExcelWrapper<User>();
        try {
            util.exportExcel(fileName, fileName, columnNames, listVO, response, ExportExcelUtil.EXCEL_FILE_2003);
            ret.put("type", "success");
        } catch (Exception e) {
        	ret.put("type", "fail");
           throw new RuntimeException("����ʧ��");
        }
        	return ret;
    }

	private List<UserVO> changeUserToVO(List<User> list) {
		List<UserVO> excelList = new ArrayList();
		// ��ɫת��map
		Map<String,String> roleMap = new HashMap<>();
		roleMap.put("1", "��������Ա");
		roleMap.put("2", "��ͨ�û�");
		roleMap.put("3", "���ֲ�����");
		roleMap.put("4", "�ͷ�����");
		roleMap.put("5", "����������");
		roleMap.put("6", "��������Ա��ʽ");
		roleMap.put("", "δ����");
		
		// �Ա�ת��map
		Map<String,String> sexMap = new HashMap<>();
		sexMap.put("0", "δ֪");
		sexMap.put("1", "��");
		sexMap.put("2", "Ů");
		sexMap.put("", "δ֪");
		
		// ����ת��
		if (null != list && list.size() >0) {
            for (User one : list) {
            	UserVO vo = new UserVO();
                vo.setAddress(one.getAddress());
                vo.setAge(one.getAge());
                vo.setPassword(one.getPassword());
                String roleId = one.getRoleId().toString();
                vo.setRoleId(roleMap.get(roleId));
                String sex = String.valueOf(one.getSex());
                vo.setSex(sexMap.get(sex));
                vo.setUsername(one.getUsername());
                // ������
                excelList.add(vo);
            }
        }
		
		return excelList;
	}
	
	
}
