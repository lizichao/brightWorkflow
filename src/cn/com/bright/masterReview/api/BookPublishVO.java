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
    private String complete_way_desc;
    private Date publish_time;
    private String publish_company;
    private String complete_chapter;
    private String complete_word;
    private String author_order;
    private String author_order_desc;
    private String coverAttachmentId;
    private String contentsAttachmentId;
    private String approve_result;
    private String proveAttachMentId;
    
    private AttachMentVO coverVO = new AttachMentVO();
    
    private AttachMentVO contentVO = new AttachMentVO();
    
    private AttachMentVO proveAttachMentVO = new AttachMentVO();;
    
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
    public String getComplete_way_desc() {
		return complete_way_desc;
	}
	public void setComplete_way_desc(String complete_way_desc) {
		this.complete_way_desc = complete_way_desc;
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
    public String getAuthor_order_desc() {
		return author_order_desc;
	}
	public void setAuthor_order_desc(String author_order_desc) {
		this.author_order_desc = author_order_desc;
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
    public AttachMentVO getProveAttachMentVO() {
		return proveAttachMentVO;
	}
	public void setProveAttachMentVO(AttachMentVO proveAttachMentVO) {
		this.proveAttachMentVO = proveAttachMentVO;
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
	public String getProveAttachMentId() {
		return proveAttachMentId;
	}
	public void setProveAttachMentId(String proveAttachMentId) {
		this.proveAttachMentId = proveAttachMentId;
	}
    
}
