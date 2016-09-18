package cn.brightcom.system.pcmc.sm.l;

import java.util.regex.Pattern;

import org.jdom.Element;

import cn.brightcom.jraf.util.StringUtil;

public abstract class Validator
{
	protected String name;
	protected String regex;
	
	public Validator()
	{
		
	}
	
	public Validator(String name, String regex)
	{
		super();
		this.name = name;
		this.regex = regex;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public void setRegex(String regex)
	{
		this.regex = regex;
	}

	public boolean isCheck(String user)
	{
		return match(user,regex);
	}
	
	public String getBaseSql()
	{
		StringBuffer sql = new StringBuffer("select a.userid,a.userpwd,a.state,")
			.append("b.userid as extuid,b.lastlogon,b.lastlogin,b.attempttimes ")
			.append("from pcmc_user a left outer join pcmc_user_login b on a.userid=b.userid ");
		return sql.toString();
	}
	
	public abstract Element query(String user,String username);
	
	protected final static boolean match(String text,String regex) {
        if (StringUtil.isEmpty(text) || StringUtil.isEmpty(regex))
            return false;
        return Pattern.compile(regex).matcher(text).matches();
    }

	@Override
	public String toString()
	{
		return "Validator [name=" + name + ", regex=" + regex + "]";
	}	
}
