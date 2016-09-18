Date.prototype.getElapsed = function(A) {
	return Math.abs((A || new Date()).getTime() - this.getTime())
};
/**
SWFUpload.FILE_STATUS = {
	QUEUED		 : -1,
	IN_PROGRESS	 : -2,
	ERROR		 : -3,
	SUCCESS	 	 : -4,
	CANCELLED	 : -5
};
*/
UploadPanel = Ext.extend(Ext.Panel, {
	fileList : null,
	swfupload : null,
	progressBar : null,
	progressInfo : null,
	uploadInfoPanel : null,
	placeHolderId : null,
	fileSelectFn : null,
	progressFn : null,
	successFn : null,
	notoolbar : null,
	addBtn:null,	
	upBtn:null,
	stopBtn:null,
	crossBtn:null,
	delBtn:null,
	constructor : function(config) {
		this.progressInfo = {
			filesTotal : 0,
			filesUploaded : 0,
			bytesTotal : 0,
			bytesUploaded : 0,
			currentCompleteBytes : 0,
			lastBytes : 0,
			lastElapsed : 1,
			lastUpdate : null,
			timeElapsed : 1
		};
		this.uploadInfoPanel = new Ext.Panel({
			region : 'north',
			height : 65,
			baseCls : '',
			collapseMode : 'mini',
			collapsed : true,
			split : true,
			border : false
		});
		this.progressBar = new Ext.ProgressBar({
			text : '上传中 0 %',
			animate : true
		});
		var autoExpandColumnId = Ext.id('fileName');
		this.uploadInfoPanel.on('render', function() {
			this.getProgressTemplate().overwrite(this.uploadInfoPanel.body, {
				filesUploaded : 0,
				filesTotal : 0,
				bytesUploaded : '0 bytes',
				bytesTotal : '0 bytes',
				timeElapsed : '00:00:00',
				timeLeft : '00:00:00',
				speedLast : '0 bytes/s',
				speedAverage : '0 bytes/s'
			});
		}, this);
		this.fileList = new Ext.grid.GridPanel({
			border : false,
			enableColumnMove : false,
			enableHdMenu : false,
			columns : [new Ext.grid.RowNumberer(), {
				header : '文件名',
				width : 80,
				dataIndex : 'fileName',
				sortable : false,
				renderer : this.formatFileName,
				id : autoExpandColumnId
			}, {
				header : '大小',
				width : 80,
				dataIndex : 'fileSize',
				sortable : false,
				renderer : this.formatFileSize,
				align : 'right'
			}, {
				header : '类型',
				width : 40,
				dataIndex : 'fileType',
				sortable : false,
				renderer : this.formatExt,
				align : 'center'
			}, {
				header : '进度',
				width : 120,
				dataIndex : 'filePercent',
				sortable : false,
				renderer : this.formatProgressBar,
				align : 'center',
				scope:this
			}, {
				header : '点击取消',
				width : 70,
				dataIndex : 'fileState',
				renderer : this.formatState,
				sortable : false,
				align : 'center',
				scope : this
			}],
			autoExpandColumn : autoExpandColumnId,
			ds : new Ext.data.SimpleStore({
				fields : ['fileId', 'fileName', 'fileSize', 'fileType', 'fileState', 'filePercent']
			}),
			bbar : [this.progressBar],
			tbar : [{
				text : '添加文件',
				iconCls : 'add',
				ref:'../addBtn'
			}, {
				text : '开始上传',
				iconCls : 'arrow-up',
				handler : this.startUpload,
				ref:'../upBtn',
				scope : this
			}, {
				text : '停止上传',
				iconCls : 'stop',
				handler : this.stopUpload,
				ref:'../stopBtn',
				scope : this
			}, {
				text : '取消队列',
				iconCls : 'cross',
				handler : this.cancelQueue,
				ref:'../crossBtn',
				scope : this
			}, {
				text : '清空列表',
				iconCls : 'delete',
				handler : this.clearList,
				ref:'../delBtn',
				scope : this
			}],
			listeners : {
				cellclick : {
					fn : function(grid, rowIndex, columnIndex, e) {
						if (columnIndex == 5) {
							var record = grid.getSelectionModel().getSelected();
							var fileId = record.data.fileId;
							var file = this.swfupload.getFile(fileId);
							if (file) {
								if (file.filestatus != SWFUpload.FILE_STATUS.CANCELLED) {
									this.swfupload.cancelUpload(fileId);
									if (record.data.fileState != SWFUpload.FILE_STATUS.CANCELLED) {
										record.set('fileState', SWFUpload.FILE_STATUS.CANCELLED);
										record.commit();
										this.onCancelQueue(fileId);
									}
								}
							}
						}
					},
					scope : this
				},
				render : {
					fn : function() {
						var grid = this.get(1).get(0);
						var em = grid.getTopToolbar().get(0).el.child('em');
						var placeHolderId = Ext.id();
						em.setStyle({
							position : 'relative',
							display : 'block'
						});
						em.createChild({
							tag : 'div',
							id : placeHolderId
						});
						if(this.notoolbar===true)
							grid.getTopToolbar().hide();
						if(this.upBtn===false)
							grid.upBtn.hide();
						if(this.delBtn===false)
							grid.delBtn.hide();
						if(this.stopBtn===false)
							grid.stopBtn.hide();
						if(this.addBtn===false)
							grid.addBtn.hide();
						if(this.crossBtn===false)
							grid.crossBtn.hide();
						var settings = {
							upload_url : this.uploadUrl,
							post_params : this.postParams||{},
							flash_url : Ext.isEmpty(this.flashUrl)
									? '/js/swfupload.swf'
									: this.flashUrl,
							file_post_name : Ext.isEmpty(this.filePostName) ? 'myUpload' : this.filePostName,
							file_size_limit : Ext.isEmpty(this.fileSize) ? '100 MB' : this.fileSize,
							file_types : Ext.isEmpty(this.fileTypes) ? '*.*' : this.fileTypes,
							file_types_description : this.fileTypesDescription,
							file_upload_limit : this.file_upload_limit||"0",  //限定用户一次性最多上传多少个文件，在上传过程中，该数字会累加，如果设置为“0”，则表示没有限制
        					//file_queue_limit : "10",//上传队列数量限制，该项通常不需设置，会根据file_upload_limit自动赋值
							use_query_string : false,
							debug : false,
							button_width : '73',
							button_height : '20',
							button_placeholder_id : this.placeHolderId||placeHolderId,
							button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
							button_cursor : SWFUpload.CURSOR.HAND,
							custom_settings : {
								scope_handler : this
							},
							file_queued_handler : this.onFileQueued,
							file_queue_error_handler : this.onFileQueueError,
							file_dialog_complete_handler : this.onFileDialogComplete,
							upload_start_handler : this.onUploadStart,
							upload_progress_handler : this.onUploadProgress,
							upload_error_handler : this.onUploadError,
							upload_success_handler : this.onUploadSuccess,
							upload_complete_handler : this.onUploadComplete
						};
						this.swfupload = new SWFUpload(settings);
						this.swfupload.uploadStopped = false;
						Ext.get(this.swfupload.movieName).setStyle({
							position : 'absolute',
							top : 0,
							left : -2
						});
						this.resizeProgressBar();
						this.on('resize', this.resizeProgressBar, this);
					},
					scope : this,
					delay : 50
				}
			}
		});
		UploadPanel.superclass.constructor.call(this, Ext.applyIf(config || {}, {
			layout : 'border',
			width : 500,
			height : 500,
			minWidth : 450,
			minHeight : 500,
			split : true,
			items : [this.uploadInfoPanel, {
				region : 'center',
				layout : 'fit',
				margins : '0 -1 -1 -1',
				items : [this.fileList]
			}]
		}));
	},
	resizeProgressBar : function() {
		this.progressBar.setWidth(this.el.getWidth() - 5);
	},
	startUpload : function(scope,p1,pps) {
		if (this.swfupload) {
			this.swfupload.uploadStopped = false;
			if(pps){
				var post_params = this.swfupload.settings.post_params;
				this.swfupload.settings.post_params=pps||post_params;
				this.swfupload.setPostParams(pps||post_params);
			}
			this.swfupload.startUpload();
		}
	},
	stopUpload : function() {
		if (this.swfupload) {
			this.swfupload.uploadStopped = true;
			this.swfupload.stopUpload();
		}
	},
	cancelQueue : function() {
		if (this.swfupload) {
			this.swfupload.stopUpload();
			var stats = this.swfupload.getStats();
			while (stats.files_queued > 0) {
				this.swfupload.cancelUpload();
				stats = this.swfupload.getStats();
			}
			this.fileList.getStore().each(function(record) {
				switch (record.data.fileState) {
					case SWFUpload.FILE_STATUS.QUEUED :
					case SWFUpload.FILE_STATUS.IN_PROGRESS :
						record.set('fileState', SWFUpload.FILE_STATUS.CANCELLED);
						record.commit();
						this.onCancelQueue(record.data.fileId);
						break;
					default :
						break;
				}
			}, this);
		}
	},
	clearList : function() {
		var store = this.fileList.getStore();
		store.each(function(record) {
			if (record.data.fileState != SWFUpload.FILE_STATUS.QUEUED
					&& record.data.fileState != SWFUpload.FILE_STATUS.IN_PROGRESS) {
				store.remove(record);
			}
		});
	},
	getProgressTemplate : function() {
		var tpl = new Ext.Template('<table class="upload-progress-table"><tbody>',
				'<tr><td class="upload-progress-label"><nobr>已上传:</nobr></td>',
				'<td class="upload-progress-value"><nobr>{filesUploaded} / {filesTotal}</nobr></td>',
				'<td class="upload-progress-label"><nobr>上传状态</nobr></td>',
				'<td class="upload-progress-value"><nobr>{bytesUploaded} / {bytesTotal}</nobr></td></tr>',
				'<tr><td class="upload-progress-label"><nobr>已用时间</nobr></td>',
				'<td class="upload-progress-value"><nobr>{timeElapsed}</nobr></td>',
				'<td class="upload-progress-label"><nobr>剩余时间</nobr></td>',
				'<td class="upload-progress-value"><nobr>{timeLeft}</nobr></td></tr>',
				'<tr><td class="upload-progress-label"><nobr>当前速度</nobr></td>',
				'<td class="upload-progress-value"><nobr>{speedLast}</nobr></td>',
				'<td class="upload-progress-label"><nobr>平均速度</nobr></td>',
				'<td class="upload-progress-value"><nobr>{speedAverage}</nobr></td></tr>', '</tbody></table>');
		tpl.compile();
		return tpl;
	},
	updateProgressInfo : function() {
		this.getProgressTemplate().overwrite(this.uploadInfoPanel.body, this.formatProgress(this.progressInfo));
	},
	formatProgress : function(info) {
		var r = {};
		r.filesUploaded = String.leftPad(info.filesUploaded, 3, '&nbsp;');
		r.filesTotal = info.filesTotal;
		r.bytesUploaded = String.leftPad(Ext.util.Format.fileSize(info.bytesUploaded), 6, '&#160;');
		r.bytesTotal = Ext.util.Format.fileSize(info.bytesTotal);
		r.timeElapsed = this.formatTime(info.timeElapsed);
		r.speedAverage = Ext.util.Format.fileSize(Math.ceil(1000 * info.bytesUploaded / info.timeElapsed)) + '/s';
		r.timeLeft = this.formatTime((info.bytesUploaded === 0) ? 0 : info.timeElapsed
				* (info.bytesTotal - info.bytesUploaded) / info.bytesUploaded);
		var caleSpeed = 1000 * info.lastBytes / info.lastElapsed;
		r.speedLast = Ext.util.Format.fileSize(caleSpeed < 0 ? 0 : caleSpeed) + '/s';
		var p = info.bytesUploaded / info.bytesTotal;
		p = p || 0;
		this.progressBar.updateProgress(p, "上传中" + Math.ceil(p * 100) + " %");
		return r;
	},
	formatTime : function(milliseconds) {
		var seconds = parseInt(milliseconds / 1000, 10);
		var s = 0;
		var m = 0;
		var h = 0;
		if (3599 < seconds) {
			h = parseInt(seconds / 3600, 10);
			seconds -= h * 3600;
		}
		if (59 < seconds) {
			m = parseInt(seconds / 60, 10);
			seconds -= m * 60;
		}
		m = String.leftPad(m, 2, '0');
		h = String.leftPad(h, 2, '0');
		s = String.leftPad(seconds, 2, '0');
		return h + ':' + m + ':' + s;
	},
	formatFileSize : function(_v, celmeta, record) {
		return '<div id="fileSize_' + record.data.fileId + '">' + Ext.util.Format.fileSize(_v) + '</div>';
	},
	formatFileName : function(_v, cellmeta, record) {
		return '<div id="fileName_' + record.data.fileId + '">' + _v + '</div>';
	},
	formatExt : function(_v, cellmeta, record) {
		var extensionName = _v.substring(1);
		if (_v) {
			return extensionName.toUpperCase();
		}
		return '';
	},
	formatProgressBar : function(v, cellmeta, record) {
		var progressBarTmp = this.getTplStr(v);
		return progressBarTmp;
	},
	getTplStr : function(v){
		var bgColor = "orange";
	    var borderColor = "#008000";
		return String.format(
			'<div>'+
				'<div style="border:1px solid {0};height:10px;width:{1}px;margin:4px 0px 1px 0px;float:left;">'+
					'<div style="float:left;background:{2};width:{3}%;height:10px;"><div></div></div>'+
				'</div>'+
			'<div style="text-align:center;float:right;width:30px;margin:3px 0px 1px 0px;height:10px;font-size:12px;">{3}%</div>'+
		'</div>', borderColor,(70),bgColor, v);
	},
	formatState : function(_v, cellmeta, record) {
		switch(_v){
			case -1 : return '未上传';
			break;
			case -2 : return '正在上传';
			break;
			case -3 : return '<div style="color:red;">上传失败</div>';
			break;
			case -4 : return '上传成功';
			break;
			case -5 : return '取消上传';
			break;
			default: return _v;
		}
	},
	onClose : function() {
		this.close();
	},
	onCancelQueue : function(fileId) {
		Ext.getDom('fileName_' + fileId).className = 'ux-cell-color-gray';
		Ext.getDom('fileSize_' + fileId).className = 'ux-cell-color-gray';
		Ext.DomHelper.applyStyles('fileType_' + fileId, 'font-style:italic;text-decoration: line-through;color:gray');
	},
	onFileQueued : function(file) {
		var thiz = this.customSettings.scope_handler;
		var frec=new UploadPanel.FileRecord({
			fileId : file.id,
			fileName : file.name,
			fileSize : file.size,
			fileType : file.type,
			fileState : file.filestatus,
			filePercent : 0
		});
		thiz.fileList.getStore().add(frec);
		thiz.progressInfo.filesTotal += 1;
		thiz.progressInfo.bytesTotal += file.size;
		thiz.updateProgressInfo();
		if(Ext.isFunction(thiz.fileSelectFn)){
			thiz.fileSelectFn.call(thiz,frec);
		}
	},
	onQueueError : function(file, errorCode, message) {
		var thiz = this.customSettings.scope_handler;
		try {
			if (errorCode != SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
				thiz.progressInfo.filesTotal -= 1;
				thiz.progressInfo.bytesTotal -= file.size;
			}
			thiz.progressInfo.bytesUploaded -= fpg.getBytesCompleted();
			thiz.updateProgressInfo();
		} catch (ex) {
			this.debug(ex);
		}
	},
	onFileDialogComplete : function(selectedFilesCount, queuedFilesCount) {
		// alert("selectedFilesCount:" + selectedFilesCount + "
		// queuedFilesCount:" + queuedFilesCount );
	},
	onUploadStart : function(file) {

	},
	onUploadProgress : function(file, completeBytes, bytesTotal) {
		var percent = Math.ceil((completeBytes / bytesTotal) * 100);

		var thiz = this.customSettings.scope_handler;
		var bytes_added = completeBytes - thiz.progressInfo.currentCompleteBytes;
		thiz.progressInfo.bytesUploaded += Math.abs(bytes_added < 0 ? 0 : bytes_added);
		thiz.progressInfo.currentCompleteBytes = completeBytes;
		if (thiz.progressInfo.lastUpdate) {
			thiz.progressInfo.lastElapsed = thiz.progressInfo.lastUpdate.getElapsed();
			thiz.progressInfo.timeElapsed += thiz.progressInfo.lastElapsed;
		}
		thiz.progressInfo.lastBytes = bytes_added;
		thiz.progressInfo.lastUpdate = new Date();
		thiz.updateProgressInfo();

		var ds = thiz.get(1).get(0).store;
		var percent = Math.ceil((completeBytes / bytesTotal) * 100);
		for(var i=0;i<ds.getCount();i++){
			var record =ds.getAt(i);
			if(record.get('fileId')==file.id){
				record.set('filePercent', percent);
				record.set('fileState', file.filestatus);
				record.commit();
			}
		}
		if(Ext.isFunction(thiz.progressFn)){
			thiz.progressFn.call(thiz,percent,file.filestatus);
		}
	},
	onUploadError : function(file, errorCode, message) {
		var thiz = this.customSettings.scope_handler;
		switch (errorCode) {
			case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED :
				thiz.progressInfo.filesTotal -= 1;
				thiz.progressInfo.bytesTotal -= file.size;
				thiz.updateProgressInfo();
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED :
		}
	},
	onUploadSuccess : function(file, serverData,resp) {
		var thiz = this.customSettings.scope_handler;
		var ds = thiz.get(1).get(0).store;
		var xr = new JrafXmlReader(null,null,null,false);
		var a = xr.readRecords(JrafUTIL.parseXmlString(serverData));
		if (a.success) {
			for(var i=0;i<ds.getCount();i++){
				var rec =ds.getAt(i);
				if(rec.get('fileId')==file.id){
					rec.set('fileState', file.filestatus);
					rec.commit();
				}
			}
		}else{
			for(var i=0;i<ds.getCount();i++){
				var rec =ds.getAt(i);
				if(rec.get('fileId')==file.id){
					rec.set('percent', 0);
					rec.set('fileState', '<div style="color:red;" title="'+a.errmsg+'">处理错误</div>');
					rec.commit();
				}
			}
		}
		thiz.progressInfo.filesUploaded += 1;
		thiz.updateProgressInfo();		
		if(Ext.isFunction(thiz.successFn)){
			thiz.successFn.call(thiz,a);
		}
	},
	onUploadComplete : function(file) {
		if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
			this.startUpload();
		}
	},
	beforeDestroy : function(){
        this.swfupload.destroy();
		UploadPanel.superclass.beforeDestroy.call(this);
    }
});

UploadPanel.FileRecord = Ext.data.Record.create([{
	name : 'fileId'
}, {
	name : 'fileName'
}, {
	name : 'fileSize'
}, {
	name : 'fileType'
}, {
	name : 'fileState'
}]);

Ext.reg('uploadpanel', UploadPanel);
