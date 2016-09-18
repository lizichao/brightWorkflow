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
     * �ڵ����õĿ���ѡ������л�ǩ���ż���
     */
    private Set<DepartmentVO> multiDepartments;

    /*
     * �ڵ��Ѿ�ѡ���Ļ�ǩ���ż��ϣ���ʾ�û�ǩ�ڵ��Ѿ����ɣ��Ǹû�ǩ�ڵ㷢���˵�ʱѡ��Ļ�ǩ���ż���
     */
    private List<DepartmentVO> selectedMultiDepartments;

    /*
     * ��ǩ����ѡ��Ĳ���
     */
    private List<DepartmentVO> addCounterDepartments;

    /*
     * ��ǩ����ѡ��Ĳ���
     */
    private List<DepartmentVO> removeCounterDepartments;

    /*
     * �ڵ��Ѿ�ѡ�����˼��ϣ���ʾ�û�ǩ�ڵ��Ѿ����ɣ��Ǹû�ǩ�ڵ㷢���˵�ʱѡ����˼���
     */
    private List<UserVO> selectedMultiUsers;

    /*
     * ��ǩ����ѡ�����
     */
    private List<UserVO> addCounterUsers;

    /*
     * ��ǩ����ѡ�����
     */
    private List<UserVO> removeCounterUsers;

    /*
     * �����Ǵ��л�ǩ���ǲ��л�ǩ
     */
    private String multiType;

    private String collectionVariable;

    private String collectionElementVariable;

    /*
     * ��������Ա��ǩ���ǲ��Ż�ǩ
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
