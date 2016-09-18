package cn.com.bright.workflow.receiveFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.WorkflowLogVO;

public class ReceiveFileVO {

    private String id;

    private String receiveTitle;

    private String receiveOffice;

    private String receiveWord;

    private String registerNumber;

    private String fileType;

    private Date receiveDate;

    private String urgent;
    private String securityLevel;

    private Date finishDate;

    private String receiveRemark;

    private Map<String, List<WorkflowLogVO>> approveLogs = new HashMap<String, List<WorkflowLogVO>>();

    private List<AttachMentVO> attachMents = new ArrayList<AttachMentVO>();

    private String officeApprover;

    private String attachMentStr;

    private List<ReceiveFileSeparateVO> separateDepts = new ArrayList<ReceiveFileSeparateVO>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiveTitle() {
        return receiveTitle;
    }

    public void setReceiveTitle(String receiveTitle) {
        this.receiveTitle = receiveTitle;
    }

    public String getReceiveOffice() {
        return receiveOffice;
    }

    public void setReceiveOffice(String receiveOffice) {
        this.receiveOffice = receiveOffice;
    }

    public String getReceiveWord() {
        return receiveWord;
    }

    public void setReceiveWord(String receiveWord) {
        this.receiveWord = receiveWord;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getReceiveRemark() {
        return receiveRemark;
    }

    public void setReceiveRemark(String receiveRemark) {
        this.receiveRemark = receiveRemark;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
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

    public String getOfficeApprover() {
        return officeApprover;
    }

    public void setOfficeApprover(String officeApprover) {
        this.officeApprover = officeApprover;
    }

    public String getAttachMentStr() {
        return attachMentStr;
    }

    public void setAttachMentStr(String attachMentStr) {
        this.attachMentStr = attachMentStr;
    }

    public List<ReceiveFileSeparateVO> getSeparateDepts() {
        return separateDepts;
    }

    public void setSeparateDepts(List<ReceiveFileSeparateVO> separateDepts) {
        this.separateDepts = separateDepts;
    }

}
