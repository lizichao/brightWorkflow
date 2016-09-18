package cn.com.bright.edu.weixin.main;

import cn.com.bright.edu.weixin.pojo.AccessToken;
import cn.com.bright.edu.weixin.pojo.Group;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * 用户组管理器类
 * 
 * @author lhbo
 * @date 2014-04-03
 */
public class GroupManager {
	private Group[] groups;
	private static GroupManager gm;
	
	public static GroupManager getInstance(){
		if(gm == null){
			gm = new GroupManager();
			gm.genGroupList();
		}
		return gm;
	}
	
	/**
	 * 获取所有分组
	 * 
	 * @param accessToken 有效的access_token
	 * @return Groups[]
	 */
	public void genGroupList(){
		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken();
		
		if (null != at) {
			// 调用接口获取所有分组
			groups = WeixinUtil.getAllGroups(at.getToken());
			if(groups == null){
				System.out.println("获取所有分组失败！");
			}
		}
	}
	
	/**
	 * 根据用户组名称返回用户组编号
	 * 
	 * @param groupName 用户组名称
	 * @return -1 表示没有找到相应的用户组，其他值表示成功返回用户组编号
	 */
	public int getGroupIdByName(String groupName){
		int groupid = -1;
		
		if (null != groups) {
			for(Group g : groups){
				if(g.getName().indexOf(groupName) > -1){
					groupid = g.getId();
					break;
				}
			}
		}
		return groupid;
	}
	
	public static void main(String[] args) {
		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken();
		
		if (null != at) {
			// 调用接口获取所有分组
			System.out.println(at.getToken());
			Group[] groups = WeixinUtil.getAllGroups(at.getToken());
			if(groups == null){
				System.out.println("获取所有分组失败！");
			} else {
				for(Group group : groups){
					System.out.println(group.getName()+":"+group.getId());
				}
			}
		}
	}
}
