package com.fh.controller.record;

import cn.hutool.core.convert.Convert;
import com.fh.controller.base.BaseController;
import com.fh.dao.RedisDao;
import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Recharge_recordManager;
import com.fh.util.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** 
 * 说明：usdt充值记录
 * 创建人：
 * 创建时间：2020-03-25
 */
@Controller
@RequestMapping(value="/recharge_record")
public class Recharge_recordController extends BaseController {
	
	String menuUrl = "recharge_record/list.do"; //菜单地址(权限用)
	@Resource(name="recharge_recordService")
	private Recharge_recordManager recharge_recordService;
	// 用户管理
	@Resource(name = "accountService")
	private AccountManager accountService;
	// 系统参数
	@Resource(name = "sys_configService")
	private Sys_configManager sys_configService;
	// Redis
	@Resource(name = "redisDaoImpl")
	private RedisDao redisDaoImpl;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Recharge_record");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		String info = "success";
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 根据用户名查询信息
		MemUser user = accountService.findByName(pd);
		if (user == null) {
			info = "不存在此用户";
		} else {
			pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
			accountService.updateAdminRechargeMoneyAndNewRec(pd);
			logger.info("返回的主键ID：" + pd.get("RECHARGE_RECORD_ID"));
			// 清空Redis所有缓存
			redisDaoImpl.deleteAll();
			// 执行一次会员状态检查任务
			QuartzManager.runAJobNow(Const.USER_STATUS_CHECK_TASK);
		}
		mv.addObject("msg",info);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Recharge_record");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		recharge_recordService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Recharge_record");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 状态
		String state = pd.getString("is_adopt");
		if (Tools.notEmpty(state)) {
			// 清空Redis所有缓存
			redisDaoImpl.deleteAll();
			if ("0".equals(state)) {
				// 通过
				PageData par = sys_configService.findById(new PageData());
				pd.put("additionalUsdt", par.get("EACH_FULL_FUND"));
				accountService.updateOffLineRechargeRecSuccess(pd);
			} else {
				accountService.updateOffLineRechargeRecFail(pd);
			}
			// 执行一次会员状态检查任务
			QuartzManager.runAJobNow(Const.USER_STATUS_CHECK_TASK);
		}
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Recharge_record");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = recharge_recordService.list(page);	//列出Recharge_record列表
		mv.setViewName("record/recharge_record/recharge_record_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("record/recharge_record/recharge_record_add");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = recharge_recordService.findById(pd);	//根据ID读取
		mv.setViewName("record/recharge_record/recharge_record_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Recharge_record");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			recharge_recordService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Recharge_record到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建时间");	//1
		titles.add("更新时间");	//2
		titles.add("金额");	//4
		titles.add("用户名");	//5
		titles.add("用户id");	//6
		titles.add("状态");	//7
		titles.add("钱包地址");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = recharge_recordService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));	    //1
			vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));	    //2
			vpd.put("var4", varOList.get(i).getString("MONEY"));	    //4
			vpd.put("var5", varOList.get(i).getString("USER_NAME"));	    //5
			vpd.put("var6", varOList.get(i).get("USER_ID").toString());	//6
			vpd.put("var7", varOList.get(i).getString("STATUS"));	    //7
			vpd.put("var8", varOList.get(i).getString("WALLET_ADDRESS"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
