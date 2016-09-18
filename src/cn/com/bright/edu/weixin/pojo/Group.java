package cn.com.bright.edu.weixin.pojo;

/**
 * 用户组的基类
 * 
 * @author lhbo
 * @date 2014-04-03
 */
public class Group {
	private int id;
	private String name;
	private int count;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
