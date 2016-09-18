package cn.brightcom.system.pcmc.sm.l;

import java.util.ArrayList;

import org.jdom.Element;

import cn.brightcom.jraf.util.DaoUtil;

public class EmailValidator extends Validator
{

	public EmailValidator()
	{
		super();
	}

	public EmailValidator(String name, String regex)
	{
		super(name, regex);
	}

	@Override
	public Element query(String user,String username)
	{
		StringBuffer sql = new StringBuffer(getBaseSql())
    		.append("where email=? and emailbind=1");
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
