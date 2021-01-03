package com.ischoolbar.programmer.controller.home;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.Account;
import com.ischoolbar.programmer.service.AccountService;

/**
 * ǰ̨��ҳ������
 * @author Administrator
 *
 */
@RequestMapping("/home")
@Controller
public class HomeController {
	
		@Autowired
	private AccountService accountService;
	
	
	
	/**
	 * ��¼ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model
			){
		model.setViewName("home/index/login");
		return model;
	}
	
	/**
	 * ע��ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/reg",method=RequestMethod.GET)
	public ModelAndView reg(ModelAndView model
			){
		model.setViewName("home/index/reg");
		return model;
	}
	
	/**
	 * ��¼��Ϣ�ύ
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> loginAct(Account account,HttpServletRequest request){
		Map<String,String> retMap = new HashMap<String, String>();
		if(account == null){
			retMap.put("type", "error");
			retMap.put("msg", "����д��ȷ���û���Ϣ��");
			return retMap;
		}
		if(StringUtils.isEmpty(account.getName())){
			retMap.put("type", "error");
			retMap.put("msg", "�û�������Ϊ�գ�");
			return retMap;
		}
		if(StringUtils.isEmpty(account.getPassword())){
			retMap.put("type", "error");
			retMap.put("msg", "���벻��Ϊ�գ�");
			return retMap;
		}
		Account findByName = accountService.findByName(account.getName());
		if(findByName == null){
			retMap.put("type", "error");
			retMap.put("msg", "�û��������ڣ�");
			return retMap;
		}
		if(!account.getPassword().equals(findByName.getPassword())){
			retMap.put("type", "error");
			retMap.put("msg", "�������");
			return retMap;
		}
		if(findByName.getStatus() == -1){
			retMap.put("type", "error");
			retMap.put("msg", "���û��ѱ����ã�����ϵ����Ա��");
			return retMap;
		}
		request.getSession().setAttribute("account", findByName);
		request.getSession().setAttribute("accountLoginCpacha", null);
		retMap.put("type", "success");
		retMap.put("msg", "��¼�ɹ���");
		return retMap;
	}
	
	/**
	 * ע����Ϣ�ύ
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/reg",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> regAct(Account account){
		Map<String,String> retMap = new HashMap<String, String>();
		if(account == null){
			retMap.put("type", "error");
			retMap.put("msg", "����д��ȷ���û���Ϣ��");
			return retMap;
		}
		if(StringUtils.isEmpty(account.getName())){
			retMap.put("type", "error");
			retMap.put("msg", "�û�������Ϊ�գ�");
			return retMap;
		}
		if(StringUtils.isEmpty(account.getPassword())){
			retMap.put("type", "error");
			retMap.put("msg", "���벻��Ϊ�գ�");
			return retMap;
		}
		if(StringUtils.isEmpty(account.getMobile())){
			retMap.put("type", "error");
			retMap.put("msg", "�ֻ��Ų���Ϊ�գ�");
			return retMap;
		}
		if(isExist(account.getName())){
			retMap.put("type", "error");
			retMap.put("msg", "���û����Ѿ����ڣ�");
			return retMap;
		}
		if(accountService.add(account) <= 0){
			retMap.put("type", "error");
			retMap.put("msg", "ע��ʧ�ܣ�����ϵ����Ա��");
			return retMap;
		}
		retMap.put("type", "success");
		retMap.put("msg", "ע��ɹ���");
		return retMap;
	}
	
	/**
	 * �˳���¼
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request){
		request.getSession().setAttribute("account", null);
		return "redirect:login";
	}
	
	private boolean isExist(String name){
		Account account = accountService.findByName(name);
		if(account == null)return false;
		return true;
	}
}
