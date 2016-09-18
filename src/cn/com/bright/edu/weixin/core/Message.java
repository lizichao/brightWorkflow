package cn.com.bright.edu.weixin.core;

import cn.com.bright.edu.weixin.pojo.WeiXinUser;

public interface Message {	
	/**
	 * 设置消息命令
	 * @param commandCode
	 * @return
	 */
	public void setCommandCode(String commandCode);
	/**
	 * 设置微信用户
	 */
	public void setFromUserName(String fromUserName);
	/**
	 * 消息提示
	 * @return
	 */
	public String getTipsInfo();
	
	/**
	 * 检查账户是否已绑定
	 * @param userName
	 * @param contents
	 * @return
	 */
	public String checkBindAccount(String userName,String[] contents);
	
	/**
	 * 验证账户是否有效
	 * @param contents
	 * @return
	 */
	public String validationAccount(String[] contents);	
	
	/**
	 * 绑定账户信息
	 * @param userName
	 * @param usertype
	 * @param idcard
	 * @return
	 */
	public boolean bindAccount(String userName,String usertype,String idcard);
	
	/**
	 * 解除账户绑定
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public String unbindAccount(String userName,String idcard);
	
	/**
	 * 获取绑定信息列表
	 * @param userName
	 * @return
	 */
	public WeiXinUser[] getBindAccountList(String userName);
	
	/**
	 * 根据证件号码获取绑定信息
	 * @param userName
	 * @param idcard
	 * @return
	 */
	public WeiXinUser getBindAccount(String userName,String idcard);
	
	/**
	 * 获取基本信息
	 * @param idCard
	 * @return
	 */
	public String getBaseInfo(String idCard);
	
	/**
	 * 获取核验结果
	 * @param idCard
	 * @return
	 */
	public String getApproveResult(String idCard);
	
	/**
	 * 获取核验结果详情
	 * @param idCard
	 * @return
	 */
	public String getApproveResultList(String idCard);
	
	/**
	 * 重置密码
	 * @param idCard
	 * @return
	 */
	public String resetPW(String idCard);
	
}
