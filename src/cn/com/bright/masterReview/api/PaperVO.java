package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 论文发表情况
 * @ClassName: PaperVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:26:15
 */
public class PaperVO {
    private String id;
    private String businessKey;
    
    private String title;
    
    private String magazineMeetName;
    
    private String paperMeetName;
    
    private String paperNumber;
    
    private Date publishTime;
    
    private String organizers;
    
    private String organizersLevel;
    
    private String organizersLevelDesc;
    
    private String personalPart;
    
    private String publish_company;
    private String complete_way;
    private String complete_way_desc;
    private String author_order;
    private String author_order_desc;
    private String paperAttachmentId;
    
    private AttachMentVO paperAttachMentVO;
    
    private String approve_result;

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagazineMeetName() {
        return magazineMeetName;
    }

    public void setMagazineMeetName(String magazineMeetName) {
        this.magazineMeetName = magazineMeetName;
    }

    public String getPaperMeetName() {
        return paperMeetName;
    }

    public void setPaperMeetName(String paperMeetName) {
        this.paperMeetName = paperMeetName;
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
    }

  

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getOrganizers() {
        return organizers;
    }

    public void setOrganizers(String organizers) {
        this.organizers = organizers;
    }

    public String getOrganizersLevel() {
        return organizersLevel;
    }

    public void setOrganizersLevel(String organizersLevel) {
        this.organizersLevel = organizersLevel;
    }
    
    public String getOrganizersLevelDesc() {
		return organizersLevelDesc;
	}

	public void setOrganizersLevelDesc(String organizersLevelDesc) {
		this.organizersLevelDesc = organizersLevelDesc;
	}

	public String getPersonalPart() {
        return personalPart;
    }

    public void setPersonalPart(String personalPart) {
        this.personalPart = personalPart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getPaperAttachmentId() {
        return paperAttachmentId;
    }

    public void setPaperAttachmentId(String paperAttachmentId) {
        this.paperAttachmentId = paperAttachmentId;
    }

    public AttachMentVO getPaperAttachMentVO() {
        return paperAttachMentVO;
    }

    public void setPaperAttachMentVO(AttachMentVO paperAttachMentVO) {
        this.paperAttachMentVO = paperAttachMentVO;
    }

    public String getPublish_company() {
        return publish_company;
    }

    public void setPublish_company(String publish_company) {
        this.publish_company = publish_company;
    }

    public String getComplete_way() {
        return complete_way;
    }

    public void setComplete_way(String complete_way) {
        this.complete_way = complete_way;
    }
    
    public String getComplete_way_desc() {
		return complete_way_desc;
	}

	public void setComplete_way_desc(String complete_way_desc) {
		this.complete_way_desc = complete_way_desc;
	}

	public String getAuthor_order() {
        return author_order;
    }

    public void setAuthor_order(String author_order) {
        this.author_order = author_order;
    }

	public String getAuthor_order_desc() {
		return author_order_desc;
	}

	public void setAuthor_order_desc(String author_order_desc) {
		this.author_order_desc = author_order_desc;
	}
    
}
