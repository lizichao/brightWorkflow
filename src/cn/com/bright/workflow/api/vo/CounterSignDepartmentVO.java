package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;

public class CounterSignDepartmentVO extends CounterSignVO {

	private List<DepartmentVO> departmentVOs = new ArrayList<DepartmentVO>();

	public List<DepartmentVO> getDepartmentVOs() {
		return departmentVOs;
	}

	public void setDepartmentVOs(List<DepartmentVO> departmentVOs) {
		this.departmentVOs = departmentVOs;
	}
}
