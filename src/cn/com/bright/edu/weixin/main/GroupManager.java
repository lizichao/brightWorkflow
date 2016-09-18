package cn.com.bright.edu.weixin.main;

import cn.com.bright.edu.weixin.pojo.AccessToken;
import cn.com.bright.edu.weixin.pojo.Group;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * �û����������
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
	 * ��ȡ���з���
	 * 
	 * @param accessToken ��Ч��access_token
	 * @return Groups[]
	 */
	public void genGroupList(){
		// ���ýӿڻ�ȡaccess_token
		AccessToken at = WeixinUtil.getAccessToken();
		
		if (null != at) {
			// ���ýӿڻ�ȡ���з���
			groups = WeixinUtil.getAllGroups(at.getToken());
			if(groups == null){
				System.out.println("��ȡ���з���ʧ�ܣ�");
			}
		}
	}
	
	/**
	 * �����û������Ʒ����û�����
	 * 
	 * @param groupName �û�������
	 * @return -1 ��ʾû���ҵ���Ӧ���û��飬����ֵ��ʾ�ɹ������û�����
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
		// ���ýӿڻ�ȡaccess_token
		AccessToken at = WeixinUtil.getAccessToken();
		
		if (null != at) {
			// ���ýӿڻ�ȡ���з���
			System.out.println(at.getToken());
			Group[] groups = WeixinUtil.getAllGroups(at.getToken());
			if(groups == null){
				System.out.println("��ȡ���з���ʧ�ܣ�");
			} else {
				for(Group group : groups){
					System.out.println(group.getName()+":"+group.getId());
				}
			}
		}
	}
}
