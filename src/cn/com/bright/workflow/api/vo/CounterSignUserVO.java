package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;

public class CounterSignUserVO extends CounterSignVO {

	private List<String> users = new ArrayList<String>();

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

}
