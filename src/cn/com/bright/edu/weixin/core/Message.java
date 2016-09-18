package cn.com.bright.edu.weixin.core;

import cn.com.bright.edu.weixin.pojo.WeiXinUser;

public interface Message {	
	/**
	 * ������Ϣ����
	 * @param commandCode
	 * @return
	 */
	public void setCommandCode(String commandCode);
	/**
	 * ����΢���û�
	 */
	public void setFromUserName(String fromUserName);
	/**
	 * ��Ϣ��ʾ
	 * @return
	 */
	public String getTipsInfo();
	
	/**
	 * ����˻��Ƿ��Ѱ�
	 * @param userName
	 * @param contents
	 * @return
	 */
	public String checkBindAccount(String userName,String[] contents);
	
	/**
	 * ��֤�˻��Ƿ���Ч
	 * @param contents
	 * @return
	 */
	public String validationAccount(String[] contents);	
	
	/**
	 * ���˻���Ϣ
	 * @param userName
	 * @param usertype
	 * @param idcard
	 * @return
	 */
	public boolean bindAccount(String userName,String usertype,String idcard);
	
	/**
	 * ����˻���
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public String unbindAccount(String userName,String idcard);
	
	/**
	 * ��ȡ����Ϣ�б�
	 * @param userName
	 * @return
	 */
	public WeiXinUser[] getBindAccountList(String userName);
	
	/**
	 * ����֤�������ȡ����Ϣ
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public WeiXinUser getBindAccount(String userName,String idcard);
	
	/**
	 * ��ȡ������Ϣ
	 * @param idCard
	 * @return
	 */
	public String getBaseInfo(String idCard);
	
	/**
	 * ��ȡ������
	 * @param idCard
	 * @return
	 */
	public String getApproveResult(String idCard);
	
	/**
	 * ��ȡ����������
	 * @param idCard
	 * @return
	 */
	public String getApproveResultList(String idCard);
	
	/**
	 * ��������
	 * @param idCard
	 * @return
	 */
	public String resetPW(String idCard);
	
}
