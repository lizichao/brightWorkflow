package cn.com.bright.edu.weixin.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.com.bright.edu.weixin.core.Message;
import cn.com.bright.edu.weixin.pojo.WeiXinUser;

public class MessageImpl implements Message{
	String commandCode = "";
	String fromUserName="";//微信ID
	public String getCommandCode() {
		return commandCode;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	
	/**
	 * 设置消息命令
	 * @param commandCode
	 * @return
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	
	/**
	 * 消息提示
	 * @param commandCode
	 * @return
	 */
	public String getTipsInfo(){
		String tips = "";
		if (commandCode.equals("11")) {
			tips = "基本信息查询操作提示\n";
			tips += "请输入11#注册时所使用的学生证件号码\n例如：11#110101XXXXXX1010";
		} else if (commandCode.equals("12")) {
			tips = "核验结果查询操作提示\n";
			tips += "请输入12#注册时所使用的学生证件号码\n例如：12#110101XXXXXX1010";
		} else if (commandCode.equals("13")) {
			tips = "核验详情查询操作提示\n";
			tips += "请输入13#注册时所使用的学生证件号码\n例如：13#110101XXXXXX1010";
		} else if (commandCode.equals("21")) {
			tips = "账户绑定操作提示\n";
			tips += "请输入21#身份代码#注册时所使用的学生证件号码#申请时所填写的家长手机号码或电话号码\n";
			tips += "身份代码：\n";
			tips += "    00代表儿童\n";
			tips += "    01代表中小学生\n";
			tips += "    02代表教师\n";
			tips += "例如身份为儿童时输入：\n21#00#110101XXXXXX1010#150XXXX1828";
			tips += "\n\n取消账户绑定操作提示\n";
			tips += "请输入21#已绑定证件号码\n";
			tips += "例如：21#110101XXXXXX1010";
		} else if (commandCode.equals("22")) {
			tips = "重置密码操作提示\n";
			tips += "请输入22#证件号码\n例如：22#110101XXXXXX1010";
		} else if (commandCode.equals("31")) {
			tips = "点击相应链接查看相应操作指引！\n\n";
			tips += "<a href='http://ertong.sz.edu.cn/cics/default.jsp'>儿童填报操作指引</a>\n\n";
			tips += "<a href='http://minban.sz.edu.cn/cics/default.jsp'>中小学填报操作指引</a>\n\n";
			tips += "<a href='http://ertong.sz.edu.cn/tics/default.jsp'>教师填报操作指引</a>";
		}else if (commandCode.equals("33")) {
			tips = "问卷调查！\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=student'>学生问卷</a>\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=teacher'>教师问卷</a>";
		
		} else if (commandCode.equals("32")) {
			tips = "系统无法识别您的命令,请点击微信公众号下面的菜单,查看操作提示,按照提示内容进行输入,谢谢您的支持！";
		} else if (commandCode.equals("33")) {
			tips = "问卷调查！\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=student'>学生问卷</a>\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=teacher'>教师问卷</a>";		
		} else if (commandCode.equals("35")) {
			tips = "欢迎参与[德体艺处]问卷调查\n\n";
			tips += "请回复【】内的字母确认您的身份：\n 【A】代表教师;\n【B】代表学生;";
		} else if (commandCode.equals("Company")) {
			tips = "亮信科技竭诚为您服务！\n";
			tips += "公司网址:\n";
			tips += "   www.szbrightcom.cn\n";
			tips += "服务热线:\n";
			tips += "   4000833876";
		}else if (commandCode.equals("vote-2")) {
			tips = "尊敬的家长：\n\n";
			tips += "       您好!我们正在您孩子所在的学校进行评估,需要您的配合和参与,现请您抽出一点时间认真阅读并回答问卷。问卷无记名，仅供评估人员参考。评估问卷发放、统计由评估组完成，与校方无关。感谢您对我们工作的支持！每题只能选择一项，谢谢！\n\n";
			tips += "  深圳市义务教育阶段学校\n\n";
			tips += "    办学水平评估评估组\n\n";
			tips += "　<a href='http://121.34.248.213/weixin/index.jsp?fromUserName="+fromUserName+"'>开始微信问卷调查</a>\n\n";
		} else {
			tips = "系统无法识别您的命令,请点击微信公众号下面的菜单,查看操作提示,按照提示内容进行输入,谢谢您的支持!";
		}
		return tips;
	}
	
	/**
	 * 检查账户是否已绑定
	 * @param contents
	 * @param userName
	 * @return
	 */
	public String checkBindAccount(String userName,String[] contents){
		if(contents.length ==1){
			return "身份代码、证件号码、联系电话不能为空！\n\n"+getTipsInfo();
		} else if(contents.length ==2){
			return "证件号码、联系电话不能为空！\n\n"+getTipsInfo();
		} else if(contents.length ==3){
			return "联系电话不能为空！\n\n"+getTipsInfo();
		}
		String usertype = contents[1];
		String idcard = contents[2];
		if("00,01,02".indexOf(usertype)<0){
			return "身份代码输入信息有误！\n\n"+getTipsInfo();
		}
		
		String respContent = "";
		boolean isBind = false;
		int bindNum = 0;
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			sql.append("SELECT DECODE(usertype,'00','儿童','01','中小学生','教师') AS usertype,t.idcard ");
			sql.append("  FROM busi_winxin_user t");
			sql.append(" WHERE t.status = 'Y' AND t.winxincode =?");
			val.add(userName);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");			
			if(ls.size()>0){
				respContent += "当前已绑定账户如下：\n";
				for(int i=0;i<ls.size();i++){
					bindNum ++;
					Element childInfo = (Element)ls.get(i);
					if(idcard.equals(childInfo.getChildTextTrim("idcard"))){
						isBind = true;
					}
					respContent+= "身份："+childInfo.getChildTextTrim("usertype");
					respContent+= "\n证件号码：\n"+childInfo.getChildTextTrim("idcard");
					if(i < (ls.size()-1)) respContent+= "\n--------\n";
				}
				if(isBind){
					respContent = "该证件号码已绑定！\n\n" + respContent;
				} else if(bindNum >= 3) {
					respContent = "绑定账户数最大支持3个！\n\n" + respContent + "\n\n" + getTipsInfo();
				} else {
					respContent = "OK";
				}
			} else {
				respContent = "OK";
			}
		} catch (Exception e) {
			return "输入信息有误！\n\n"+getTipsInfo();
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * 验证账户是否有效
	 * @param contents
	 * @return
	 */
	public String validationAccount(String[] contents){
		String respContent = "";
		String usertype = contents[1];
		try {
			Message message = null;
			if("00".equals(usertype)){
				message = new ChildrenMessageImpl();
			} else if("01".equals(usertype)){
				message = new MinBanMessageImpl();
			} else {
				message = new TeacherMessageImpl();
			}
			respContent = message.validationAccount(contents);
		} catch (Exception e) {
			return "输入信息有误！\n\n"+getTipsInfo();
		}
		return respContent;
	}
	
	/**
	 * 绑定账户信息
	 * @param contents
	 * @param userName
	 * @return
	 */
	public boolean bindAccount(String userName,String usertype,String idcard){
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			val.add((String)new UUIDHexGenerator().generate(null));
			val.add(userName);
			val.add(usertype);
			val.add(idcard);
			pdao.setSql("INSERT INTO busi_winxin_user(winxinid, winxincode, usertype, idcard, status, createdate) VALUES (?,?,?,?,'Y',SYSDATE)");
			pdao.setBindValues(val);
			try {
				pdao.executeTransactionSql();
			} catch (Exception e) {
				val.clear();
				val.add(usertype);
				val.add(idcard);
				val.add(userName);
				pdao.setSql("UPDATE busi_winxin_user SET status='Y',usertype=?,modifydate=SYSDATE WHERE idcard=? AND winxincode=?");
				pdao.setBindValues(val);
				pdao.executeTransactionSql();
			}
		} catch (Exception e) {
			return false;
		} finally {
			pdao.releaseConnection();
		}
		return true;
	}
	
	/**
	 * 解除账户绑定
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public String unbindAccount(String userName,String idcard){
		String respContent = "";
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			val.add(idcard);
			val.add(userName);
			pdao.setSql("UPDATE busi_winxin_user SET status='N',modifydate=SYSDATE WHERE idcard=? AND winxincode=?");
			pdao.setBindValues(val);
	    	long updateNum = pdao.executeTransactionSql();
	    	if(updateNum==0){
	    		respContent = "您输入的证件号码不在账户绑定列表中，请检查证件号码输入是否有误！";
	    	} else {
	    		respContent = "取消账户绑定成功！";
	    	}
		} catch (Exception e) {
			return "输入信息有误！\n\n"+getTipsInfo();
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * 获取绑定信息列表
	 * @param userName
	 * @return
	 */
	public WeiXinUser[] getBindAccountList(String userName){
		List<WeiXinUser> userList = new ArrayList<WeiXinUser>();
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		sql.append("SELECT winxincode,usertype,idcard,status,to_char(createdate,'YYYY-MM-DD') AS createdate,to_char(modifydate,'YYYY-MM-DD') AS modifydate ");
		sql.append("  FROM busi_winxin_user t");
		sql.append(" WHERE status = 'Y' AND t.winxincode =?");
		try {
			pdao = new PlatformDao();
			val.add(userName);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			int num = 0;
			for(int i=0;i<ls.size();i++,num++){
				Element info = (Element)ls.get(i);
				WeiXinUser user = new WeiXinUser();
				user.setWinXinCode(info.getChildTextTrim("winxincode"));
				user.setUserType(info.getChildTextTrim("usertype"));
				user.setIdCard(info.getChildTextTrim("idcard"));
				user.setStatus(info.getChildTextTrim("status"));
				user.setCreateDate(info.getChildTextTrim("createdate"));
				user.setModifyDate(info.getChildTextTrim("modifydate"));
				userList.add(user);
			}
			if(num!=0){
				return userList.toArray(new WeiXinUser[1]);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 根据证件号码获取绑定信息
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public WeiXinUser getBindAccount(String userName,String idcard){
		WeiXinUser user = null;
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		sql.append("SELECT winxincode,usertype,idcard,status,to_char(createdate,'YYYY-MM-DD') AS createdate,to_char(modifydate,'YYYY-MM-DD') AS modifydate ");
		sql.append("  FROM busi_winxin_user t");
		sql.append(" WHERE status = 'Y' AND idcard=? AND t.winxincode =?");
		try {
			pdao = new PlatformDao();
			val.add(idcard);
			val.add(userName);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			if(ls.size()>0){
				Element info = (Element)ls.get(0);
				user = new WeiXinUser();
				user.setWinXinCode(info.getChildTextTrim("winxincode"));
				user.setUserType(info.getChildTextTrim("usertype"));
				user.setIdCard(info.getChildTextTrim("idcard"));
				user.setStatus(info.getChildTextTrim("status"));
				user.setCreateDate(info.getChildTextTrim("createdate"));
				user.setModifyDate(info.getChildTextTrim("modifydate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			user = null;
		} finally {
			pdao.releaseConnection();
		}
		return user;
	}
	
	public String getBaseInfo(String idCard){return "";}
	public String getApproveResult(String idCard){return "";}
	public String getApproveResultList(String idCard){return "";}
	public String resetPW(String idCard){return "";}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
}
