package cn.com.bright.workflow.publish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.WorkflowLogVO;

public class PublishVO {

    private String id;
    /*
     * 发文标题
     */
    private String fwtm;
    // 发文公文类型
    private String fldgwlx;
    // 主办部门名称
    private String fldzbbmmc;
    // 主办部门（拟稿部门id）
    private String fldzbbmid;
    // 拟稿人
    private String fldngr;
    // 紧急程度
    private String fldjjcd;
    // 密级
    private String fldmj;
    // 文种
    private String fldwz;
    private String flddjh;
    private String officeApprover;
    // 部门会签节点主办人
    private String departmentPrincipalHandler;

    private String originalAttachmentId;
    private AttachMentVO originalAttachment;

    private String attachMentStr;
    private List<AttachMentVO> attachMents = new ArrayList<AttachMentVO>();

    private Map<String, List<WorkflowLogVO>> approveLogs = new HashMap<String, List<WorkflowLogVO>>();

    public String getFwtm() {
        return fwtm;
    }

    public void setFwtm(String fwtm) {
        this.fwtm = fwtm;
    }

    public String getFldgwlx() {
        return fldgwlx;
    }

    public void setFldgwlx(String fldgwlx) {
        this.fldgwlx = fldgwlx;
    }

    public String getFldzbbmmc() {
        return fldzbbmmc;
    }

    public void setFldzbbmmc(String fldzbbmmc) {
        this.fldzbbmmc = fldzbbmmc;
    }

    public String getFldngr() {
        return fldngr;
    }

    public void setFldngr(String fldngr) {
        this.fldngr = fldngr;
    }

    public String getFldjjcd() {
        return fldjjcd;
    }

    public void setFldjjcd(String fldjjcd) {
        this.fldjjcd = fldjjcd;
    }

    public String getFldmj() {
        return fldmj;
    }

    public void setFldmj(String fldmj) {
        this.fldmj = fldmj;
    }

    public String getFldwz() {
        return fldwz;
    }

    public void setFldwz(String fldwz) {
        this.fldwz = fldwz;
    }

    public String getFlddjh() {
        return flddjh;
    }

    public void setFlddjh(String flddjh) {
        this.flddjh = flddjh;
    }

    public String getFldzbbmid() {
        return fldzbbmid;
    }

    public void setFldzbbmid(String fldzbbmid) {
        this.fldzbbmid = fldzbbmid;
    }

    public Map<String, List<WorkflowLogVO>> getApproveLogs() {
        return approveLogs;
    }

    public void setApproveLogs(Map<String, List<WorkflowLogVO>> approveLogs) {
        this.approveLogs = approveLogs;
    }

    public List<AttachMentVO> getAttachMents() {
        return attachMents;
    }

    public void setAttachMents(List<AttachMentVO> attachMents) {
        this.attachMents = attachMents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeApprover() {
        return officeApprover;
    }

    public void setOfficeApprover(String officeApprover) {
        this.officeApprover = officeApprover;
    }

    public AttachMentVO getOriginalAttachment() {
        return originalAttachment;
    }

    public void setOriginalAttachment(AttachMentVO originalAttachment) {
        this.originalAttachment = originalAttachment;
    }

    public String getOriginalAttachmentId() {
        return originalAttachmentId;
    }

    public void setOriginalAttachmentId(String originalAttachmentId) {
        this.originalAttachmentId = originalAttachmentId;
    }

    public String getAttachMentStr() {
        return attachMentStr;
    }

    public void setAttachMentStr(String attachMentStr) {
        this.attachMentStr = attachMentStr;
    }

    public String getDepartmentPrincipalHandler() {
        return departmentPrincipalHandler;
    }

    public void setDepartmentPrincipalHandler(String departmentPrincipalHandler) {
        this.departmentPrincipalHandler = departmentPrincipalHandler;
    }
}
