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
	String fromUserName="";//΢��ID
	public String getCommandCode() {
		return commandCode;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	
	/**
	 * ������Ϣ����
	 * @param commandCode
	 * @return
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	
	/**
	 * ��Ϣ��ʾ
	 * @param commandCode
	 * @return
	 */
	public String getTipsInfo(){
		String tips = "";
		if (commandCode.equals("11")) {
			tips = "������Ϣ��ѯ������ʾ\n";
			tips += "������11#ע��ʱ��ʹ�õ�ѧ��֤������\n���磺11#110101XXXXXX1010";
		} else if (commandCode.equals("12")) {
			tips = "��������ѯ������ʾ\n";
			tips += "������12#ע��ʱ��ʹ�õ�ѧ��֤������\n���磺12#110101XXXXXX1010";
		} else if (commandCode.equals("13")) {
			tips = "���������ѯ������ʾ\n";
			tips += "������13#ע��ʱ��ʹ�õ�ѧ��֤������\n���磺13#110101XXXXXX1010";
		} else if (commandCode.equals("21")) {
			tips = "�˻��󶨲�����ʾ\n";
			tips += "������21#��ݴ���#ע��ʱ��ʹ�õ�ѧ��֤������#����ʱ����д�ļҳ��ֻ������绰����\n";
			tips += "��ݴ��룺\n";
			tips += "    00�����ͯ\n";
			tips += "    01������Сѧ��\n";
			tips += "    02�����ʦ\n";
			tips += "�������Ϊ��ͯʱ���룺\n21#00#110101XXXXXX1010#150XXXX1828";
			tips += "\n\nȡ���˻��󶨲�����ʾ\n";
			tips += "������21#�Ѱ�֤������\n";
			tips += "���磺21#110101XXXXXX1010";
		} else if (commandCode.equals("22")) {
			tips = "�������������ʾ\n";
			tips += "������22#֤������\n���磺22#110101XXXXXX1010";
		} else if (commandCode.equals("31")) {
			tips = "�����Ӧ���Ӳ鿴��Ӧ����ָ����\n\n";
			tips += "<a href='http://ertong.sz.edu.cn/cics/default.jsp'>��ͯ�����ָ��</a>\n\n";
			tips += "<a href='http://minban.sz.edu.cn/cics/default.jsp'>��Сѧ�����ָ��</a>\n\n";
			tips += "<a href='http://ertong.sz.edu.cn/tics/default.jsp'>��ʦ�����ָ��</a>";
		}else if (commandCode.equals("33")) {
			tips = "�ʾ���飡\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=student'>ѧ���ʾ�</a>\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=teacher'>��ʦ�ʾ�</a>";
		
		} else if (commandCode.equals("32")) {
			tips = "ϵͳ�޷�ʶ����������,����΢�Ź��ں�����Ĳ˵�,�鿴������ʾ,������ʾ���ݽ�������,лл����֧�֣�";
		} else if (commandCode.equals("33")) {
			tips = "�ʾ���飡\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=student'>ѧ���ʾ�</a>\n\n";
			tips += "<a href='http://vote.szbrightcom.cn/vote/login.jsp?voteobject=teacher'>��ʦ�ʾ�</a>";		
		} else if (commandCode.equals("35")) {
			tips = "��ӭ����[�����մ�]�ʾ����\n\n";
			tips += "��ظ������ڵ���ĸȷ��������ݣ�\n ��A�������ʦ;\n��B������ѧ��;";
		} else if (commandCode.equals("Company")) {
			tips = "���ſƼ��߳�Ϊ������\n";
			tips += "��˾��ַ:\n";
			tips += "   www.szbrightcom.cn\n";
			tips += "��������:\n";
			tips += "   4000833876";
		}else if (commandCode.equals("vote-2")) {
			tips = "�𾴵ļҳ���\n\n";
			tips += "       ����!�����������������ڵ�ѧУ��������,��Ҫ������ϺͲ���,���������һ��ʱ�������Ķ����ش��ʾ��ʾ��޼���������������Ա�ο��������ʾ��š�ͳ������������ɣ���У���޹ء���л�������ǹ�����֧�֣�ÿ��ֻ��ѡ��һ�лл��\n\n";
			tips += "  ��������������׶�ѧУ\n\n";
			tips += "    ��ѧˮƽ����������\n\n";
			tips += "��<a href='http://121.34.248.213/weixin/index.jsp?fromUserName="+fromUserName+"'>��ʼ΢���ʾ����</a>\n\n";
		} else {
			tips = "ϵͳ�޷�ʶ����������,����΢�Ź��ں�����Ĳ˵�,�鿴������ʾ,������ʾ���ݽ�������,лл����֧��!";
		}
		return tips;
	}
	
	/**
	 * ����˻��Ƿ��Ѱ�
	 * @param contents
	 * @param userName
	 * @return
	 */
	public String checkBindAccount(String userName,String[] contents){
		if(contents.length ==1){
			return "��ݴ��롢֤�����롢��ϵ�绰����Ϊ�գ�\n\n"+getTipsInfo();
		} else if(contents.length ==2){
			return "֤�����롢��ϵ�绰����Ϊ�գ�\n\n"+getTipsInfo();
		} else if(contents.length ==3){
			return "��ϵ�绰����Ϊ�գ�\n\n"+getTipsInfo();
		}
		String usertype = contents[1];
		String idcard = contents[2];
		if("00,01,02".indexOf(usertype)<0){
			return "��ݴ���������Ϣ����\n\n"+getTipsInfo();
		}
		
		String respContent = "";
		boolean isBind = false;
		int bindNum = 0;
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			sql.append("SELECT DECODE(usertype,'00','��ͯ','01','��Сѧ��','��ʦ') AS usertype,t.idcard ");
			sql.append("  FROM busi_winxin_user t");
			sql.append(" WHERE t.status = 'Y' AND t.winxincode =?");
			val.add(userName);
			pdao.setSql(sql.toString());
			pdao.setBindValues(val);
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");			
			if(ls.size()>0){
				respContent += "��ǰ�Ѱ��˻����£�\n";
				for(int i=0;i<ls.size();i++){
					bindNum ++;
					Element childInfo = (Element)ls.get(i);
					if(idcard.equals(childInfo.getChildTextTrim("idcard"))){
						isBind = true;
					}
					respContent+= "��ݣ�"+childInfo.getChildTextTrim("usertype");
					respContent+= "\n֤�����룺\n"+childInfo.getChildTextTrim("idcard");
					if(i < (ls.size()-1)) respContent+= "\n--------\n";
				}
				if(isBind){
					respContent = "��֤�������Ѱ󶨣�\n\n" + respContent;
				} else if(bindNum >= 3) {
					respContent = "���˻������֧��3����\n\n" + respContent + "\n\n" + getTipsInfo();
				} else {
					respContent = "OK";
				}
			} else {
				respContent = "OK";
			}
		} catch (Exception e) {
			return "������Ϣ����\n\n"+getTipsInfo();
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * ��֤�˻��Ƿ���Ч
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
			return "������Ϣ����\n\n"+getTipsInfo();
		}
		return respContent;
	}
	
	/**
	 * ���˻���Ϣ
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
	 * ����˻���
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
	    		respContent = "�������֤�����벻���˻����б��У�����֤�����������Ƿ�����";
	    	} else {
	    		respContent = "ȡ���˻��󶨳ɹ���";
	    	}
		} catch (Exception e) {
			return "������Ϣ����\n\n"+getTipsInfo();
		} finally {
			pdao.releaseConnection();
		}
		return respContent;
	}
	
	/**
	 * ��ȡ����Ϣ�б�
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
	 * ����֤�������ȡ����Ϣ
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
