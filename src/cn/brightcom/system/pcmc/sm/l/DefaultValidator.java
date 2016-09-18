package cn.brightcom.system.pcmc.sm.l;

import java.util.ArrayList;

import org.jdom.Element;

import cn.brightcom.jraf.util.DaoUtil;

public class DefaultValidator extends Validator
{
	public DefaultValidator()
	{
		super();
	}
	
	@Override
	public boolean isCheck(String user)
	{
		return true;
	}

	public DefaultValidator(String name, String regex)
	{
		super(name, regex);
	}

	@Override
	public Element query(String user,String username)
	{
//		StringBuffer sql = new StringBuffer(getBaseSql())
//    		.append("where usercode=? and username = ?");
//		ArrayList bvals = new ArrayList();
//		bvals.add(user);
//		bvals.add(username.trim());
		
		  StringBuffer sql = new StringBuffer(getBaseSql())
          .append("where usercode=? ");
      ArrayList bvals = new ArrayList();
      bvals.add(user);
		
		try
		{
			return DaoUtil.getOneRecord(sql.toString(), bvals);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

}
