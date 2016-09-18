package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;

public class EditCounterSignUserVO extends EditCounterSignVO {

    private List<UserVO> addUsers = new ArrayList<UserVO>();
    private List<UserVO> removeUsers = new ArrayList<UserVO>();
    private List<UserVO> unChangeUsers = new ArrayList<UserVO>();

    private List<String> addUserStrs = new ArrayList<String>();
    private List<String> removeUserStrs = new ArrayList<String>();
    private List<String> unChangeUserStrs = new ArrayList<String>();
    
    private String majorUser;
    private String submitUsers;

    public List<String> getAddUserStrs() {
        return addUserStrs;
    }

    public void setAddUserStrs(List<String> addUserStrs) {
        this.addUserStrs = addUserStrs;
    }

    public List<String> getRemoveUserStrs() {
        return removeUserStrs;
    }

    public void setRemoveUserStrs(List<String> removeUserStrs) {
        this.removeUserStrs = removeUserStrs;
    }

    public List<UserVO> getAddUsers() {
        return addUsers;
    }

    public void setAddUsers(List<UserVO> addUsers) {
        this.addUsers = addUsers;
    }

    public List<UserVO> getRemoveUsers() {
        return removeUsers;
    }

    public void setRemoveUsers(List<UserVO> removeUsers) {
        this.removeUsers = removeUsers;
    }

    public String getMajorUser() {
        return majorUser;
    }

    public void setMajorUser(String majorUser) {
        this.majorUser = majorUser;
    }

    public String getSubmitUsers() {
        return submitUsers;
    }

    public void setSubmitUsers(String submitUsers) {
        this.submitUsers = submitUsers;
    }

    public List<String> getUnChangeUserStrs() {
        return unChangeUserStrs;
    }

    public void setUnChangeUserStrs(List<String> unChangeUserStrs) {
        this.unChangeUserStrs = unChangeUserStrs;
    }

    public List<UserVO> getUnChangeUsers() {
        return unChangeUsers;
    }

    public void setUnChangeUsers(List<UserVO> unChangeUsers) {
        this.unChangeUsers = unChangeUsers;
    }
}
