package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;

public class EditCounterSignDepartmentVO extends EditCounterSignVO {

    private List<DepartmentVO> addDepartmentVOs = new ArrayList<DepartmentVO>();
    private List<DepartmentVO> removeDepartmentVOs = new ArrayList<DepartmentVO>();

    private String majorDept;

    private String submitDepts;

    public String getSubmitDepts() {
        return submitDepts;
    }

    public void setSubmitDepts(String submitDepts) {
        this.submitDepts = submitDepts;
    }

    public List<DepartmentVO> getAddDepartmentVOs() {
        return addDepartmentVOs;
    }

    public void setAddDepartmentVOs(List<DepartmentVO> addDepartmentVOs) {
        this.addDepartmentVOs = addDepartmentVOs;
    }

    public List<DepartmentVO> getRemoveDepartmentVOs() {
        return removeDepartmentVOs;
    }

    public void setRemoveDepartmentVOs(List<DepartmentVO> removeDepartmentVOs) {
        this.removeDepartmentVOs = removeDepartmentVOs;
    }

    public String getMajorDept() {
        return majorDept;
    }

    public void setMajorDept(String majorDept) {
        this.majorDept = majorDept;
    }

}
