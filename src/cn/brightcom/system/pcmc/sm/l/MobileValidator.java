package cn.brightcom.system.pcmc.sm.l;

import java.util.ArrayList;

import org.jdom.Element;

import cn.brightcom.jraf.util.DaoUtil;

public class MobileValidator extends Validator
{

	public MobileValidator()
	{
		super();
	}

	public MobileValidator(String name, String regex)
	{
		super(name, regex);
	}

	@Override
	public Element query(String user,String username)
	{
		StringBuffer sql = new StringBuffer(getBaseSql())
			.append("where mobile=? and mobilebind=1");
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
