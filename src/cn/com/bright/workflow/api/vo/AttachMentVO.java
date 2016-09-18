package cn.com.bright.workflow.api.vo;

public class AttachMentVO {
	private String attachmentId;
	private String fileName;
	private String fileSize;
	private String fileType;
	private String filePath;
	private String file_upload_type;
	private String row_number;

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

    public String getFile_upload_type() {
        return file_upload_type;
    }

    public void setFile_upload_type(String file_upload_type) {
        this.file_upload_type = file_upload_type;
    }

    public String getRow_number() {
        return row_number;
    }

    public void setRow_number(String row_number) {
        this.row_number = row_number;
    }
	
	
}
