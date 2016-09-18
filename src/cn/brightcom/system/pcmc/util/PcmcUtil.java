package cn.brightcom.system.pcmc.util;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.StringUtil;


public class PcmcUtil
{
	private static String sysManager = BrightComConfig.getConfiguration().getString("brightcom.sysmgr","");
	
	public static boolean isSysManager(String usercode)
	{
		if(StringUtil.isEmpty(usercode))
			return false;
		return sysManager.indexOf("~"+usercode+"~")>-1;
	}
}
