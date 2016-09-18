/* Demo Note:  This demo uses a FileProgress class that handles the UI for displaying the file name and percent complete.
The FileProgress class is not part of SWFUpload.
*/


/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */
function preLoad() {
	if (!this.support.loading) {
		alert("����Ҫ��װFlash Player 9.0�����ϰ汾���");
		return false;
	}
}
function loadFailed() {
	alert("�����ϴ����ʱ��������");
}

function fileQueued(file) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("���ϴ�...");
		progress.toggleCancel(true, this);

	} catch (ex) {
		this.debug(ex);
	}

}

function fileQueueError(file, errorCode, message) {
	try {
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("��ѡ����ļ�������������.\n" + (message === 0 ? "���������ļ��ϴ���������." : "��ֻ��ѡ��" + (message > 1 ? "��" + message + "�ļ�" : "һ���ļ�.")));
			return;
		}

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			progress.setStatus("�ļ�̫��.");
			this.debug("������Ϣ: �ļ�̫��, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			progress.setStatus("�����ϴ�0�ֽڵ��ļ�.");
			this.debug("������Ϣ:�����ϴ�0�ֽڵ��ļ�, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			progress.setStatus("��Ч���ļ�����.");
			this.debug("������Ϣ:��Ч���ļ�����, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		default:
			if (file !== null) {
				progress.setStatus("δ֪�쳣");
			}
			this.debug("������Ϣ: " + errorCode + ", �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		/**
		if (numFilesSelected > 0) {
			document.getElementById(this.customSettings.cancelButtonId).disabled = false;
		}
		*/
		/* I want auto start the upload and I can do that here */
		//this.startUpload(); //ȡ������������ϴ�
	} catch (ex)  {
        this.debug(ex);
	}
}

function uploadStart(file) {
	try {
		/* I don't want to do any file validation or anything,  I'll just update the UI and
		return true to indicate that the upload should start.
		It's important to update the UI here because in Linux no uploadProgress events are called. The best
		we can do is say we are uploading.
		 */
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("�ϴ���...");
		progress.toggleCancel(true, this);
	}
	catch (ex) {}
	
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setProgress(percent);
		progress.setStatus("�ϴ���...");
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setComplete();
		progress.setStatus("�ϴ����.");
		progress.toggleCancel(false);

	} catch (ex) {
		this.debug(ex);
	}
}

function uploadError(file, errorCode, message) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("�ϴ�����: " + message);
			this.debug("������Ϣ: HTTP����, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("�ϴ�ʧ��.");
			this.debug("������Ϣ:�ϴ�ʧ��, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("������ IO ����");
			this.debug("������Ϣ:IO ����, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("��ȫ����");
			this.debug("������Ϣ:��ȫ����, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			progress.setStatus("�ϴ����ƴ���.");
			this.debug("������Ϣ:�ϴ����ƴ���, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			progress.setStatus("Failed Validation.  Upload skipped.");
			this.debug("������Ϣ:File Validation Failed, �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			// If there aren't any files left (they were all cancelled) disable the cancel button
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			}
			progress.setStatus("ȡ��");
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("ֹͣ");
			break;
		default:
			progress.setStatus("δ֪�쳣 " + errorCode);
			this.debug("������Ϣ: " + errorCode + ", �ļ���: " + file.name + ", �ļ���С: " + file.size + ", ������Ϣ: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete(file) {
	if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
		this.startUpload();
	}
	if (this.getStats().files_queued == 0)
	{
		allUploadComplete();
	}
}