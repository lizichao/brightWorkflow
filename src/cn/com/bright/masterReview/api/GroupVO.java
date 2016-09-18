package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 课题情况
 * @ClassName: SubjectVO
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午4:48:45
 */
public class GroupVO {
    private String group_id;

    private String group_pid;

    private String group_name;

    private String valid;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_pid() {
        return group_pid;
    }

    public void setGroup_pid(String group_pid) {
        this.group_pid = group_pid;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

}
