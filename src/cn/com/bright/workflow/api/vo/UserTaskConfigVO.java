package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserTaskConfigVO extends NodeVO {

    private Set<UserVO> configHandlers;

    /**
     * 
     */
    private List<FlowVO> incomingFlows = new ArrayList<FlowVO>();

    /**
     * 
     */
    private List<FlowVO> outgoingFlows = new ArrayList<FlowVO>();

    /*
     * 节点配置的可以选择的所有会签部门集合
     */
    private Set<DepartmentVO> multiDepartments;

    /*
     * 节点已经选过的会签部门集合，表示该会签节点已经生成，是该会签节点发起人当时选择的会签部门集合
     */
    private List<DepartmentVO> selectedMultiDepartments;

    /*
     * 加签可以选择的部门
     */
    private List<DepartmentVO> addCounterDepartments;

    /*
     * 减签可以选择的部门
     */
    private List<DepartmentVO> removeCounterDepartments;

    /*
     * 节点已经选过的人集合，表示该会签节点已经生成，是该会签节点发起人当时选择的人集合
     */
    private List<UserVO> selectedMultiUsers;

    /*
     * 加签可以选择的人
     */
    private List<UserVO> addCounterUsers;

    /*
     * 减签可以选择的人
     */
    private List<UserVO> removeCounterUsers;

    /*
     * 区分是串行会签还是并行会签
     */
    private String multiType;

    private String collectionVariable;

    private String collectionElementVariable;

    /*
     * 区分是人员会签还是部门会签
     */
    private int multi_kind;

    public Set<UserVO> getConfigHandlers() {
        return configHandlers;
    }

    public void setConfigHandlers(Set<UserVO> configHandlers) {
        this.configHandlers = configHandlers;
    }

    public List<FlowVO> getIncomingFlows() {
        return incomingFlows;
    }

    public void setIncomingFlows(List<FlowVO> incomingFlows) {
        this.incomingFlows = incomingFlows;
    }

    public List<FlowVO> getOutgoingFlows() {
        return outgoingFlows;
    }

    public void setOutgoingFlows(List<FlowVO> outgoingFlows) {
        this.outgoingFlows = outgoingFlows;
    }

    public String getMultiType() {
        return multiType;
    }

    public void setMultiType(String multiType) {
        this.multiType = multiType;
    }

    public Set<DepartmentVO> getMultiDepartments() {
        return multiDepartments;
    }

    public void setMultiDepartments(Set<DepartmentVO> multiDepartments) {
        this.multiDepartments = multiDepartments;
    }

    public String getCollectionVariable() {
        return collectionVariable;
    }

    public void setCollectionVariable(String collectionVariable) {
        this.collectionVariable = collectionVariable;
    }

    public String getCollectionElementVariable() {
        return collectionElementVariable;
    }

    public void setCollectionElementVariable(String collectionElementVariable) {
        this.collectionElementVariable = collectionElementVariable;
    }

    public List<DepartmentVO> getSelectedMultiDepartments() {
        return selectedMultiDepartments;
    }

    public void setSelectedMultiDepartments(List<DepartmentVO> selectedMultiDepartments) {
        this.selectedMultiDepartments = selectedMultiDepartments;
    }

    public List<DepartmentVO> getAddCounterDepartments() {
        return addCounterDepartments;
    }

    public void setAddCounterDepartments(List<DepartmentVO> addCounterDepartments) {
        this.addCounterDepartments = addCounterDepartments;
    }

    public List<DepartmentVO> getRemoveCounterDepartments() {
        return removeCounterDepartments;
    }

    public void setRemoveCounterDepartments(List<DepartmentVO> removeCounterDepartments) {
        this.removeCounterDepartments = removeCounterDepartments;
    }

    public int getMulti_kind() {
        return multi_kind;
    }

    public void setMulti_kind(int multi_kind) {
        this.multi_kind = multi_kind;
    }

    public List<UserVO> getSelectedMultiUsers() {
        return selectedMultiUsers;
    }

    public void setSelectedMultiUsers(List<UserVO> selectedMultiUsers) {
        this.selectedMultiUsers = selectedMultiUsers;
    }

    public List<UserVO> getAddCounterUsers() {
        return addCounterUsers;
    }

    public void setAddCounterUsers(List<UserVO> addCounterUsers) {
        this.addCounterUsers = addCounterUsers;
    }

    public List<UserVO> getRemoveCounterUsers() {
        return removeCounterUsers;
    }

    public void setRemoveCounterUsers(List<UserVO> removeCounterUsers) {
        this.removeCounterUsers = removeCounterUsers;
    }
}
