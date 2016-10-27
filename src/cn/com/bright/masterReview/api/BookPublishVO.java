package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 著作出版情况
 * @ClassName: BookPublishVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:31:15
 */
public class BookPublishVO {
    private String id;
    private String businessKey;
    private String book_name;
    private String complete_way;
    private Date publish_time;
    private String publish_company;
    private String complete_chapter;
    private String complete_word;
    private String author_order;
    private String coverAttachmentId;
    private String contentsAttachmentId;
    private String approve_result;
    
    private AttachMentVO coverVO = new AttachMentVO();
    
    private AttachMentVO contentVO = new AttachMentVO();
    
    
    public String getBook_name() {
        return book_name;
    }
    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
    public String getComplete_way() {
        return complete_way;
    }
    public void setComplete_way(String complete_way) {
        this.complete_way = complete_way;
    }
    public Date getPublish_time() {
        return publish_time;
    }
    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }
    public String getComplete_chapter() {
        return complete_chapter;
    }
    public void setComplete_chapter(String complete_chapter) {
        this.complete_chapter = complete_chapter;
    }
    public String getComplete_word() {
        return complete_word;
    }
    public void setComplete_word(String complete_word) {
        this.complete_word = complete_word;
    }
    public String getAuthor_order() {
        return author_order;
    }
    public void setAuthor_order(String author_order) {
        this.author_order = author_order;
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
    public String getCoverAttachmentId() {
        return coverAttachmentId;
    }
    public void setCoverAttachmentId(String coverAttachmentId) {
        this.coverAttachmentId = coverAttachmentId;
    }
    public String getContentsAttachmentId() {
        return contentsAttachmentId;
    }
    public void setContentsAttachmentId(String contentsAttachmentId) {
        this.contentsAttachmentId = contentsAttachmentId;
    }
    public AttachMentVO getCoverVO() {
        return coverVO;
    }
    public void setCoverVO(AttachMentVO coverVO) {
        this.coverVO = coverVO;
    }
    public AttachMentVO getContentVO() {
        return contentVO;
    }
    public void setContentVO(AttachMentVO contentVO) {
        this.contentVO = contentVO;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
	public String getPublish_company() {
		return publish_company;
	}
	public void setPublish_company(String publish_company) {
		this.publish_company = publish_company;
	}
    
}
