package cn.com.bright.yuexue.contact;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.ImageUtils;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:留言管理</p>
 * <p>Description: 留言管理</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/16       1.0          build this moudle </p>
 *     
 */
public class Comment {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	
    public void setXmlDocUtil(XmlDocPkgUtil _xmlDocUtil){
    	this.xmlDocUtil = _xmlDocUtil;
    }
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		
        if ("queryComment".equals(action)){
        	queryComment();
		}
        else if ("addComment".equals(action)){
        	addComment();
        }
        else if ("getCommentMenuInfo".equals(action)){
        	getCommentMenuInfo();
        }
        else if ("getMyCommentGroup".equals(action)){
        	getMyCommentGroup();
        }
        else if ("praiseComment".equals(action)){
        	praiseComment();
        }
		return xmlDoc;
	}
	/**
	 * 点赞
	 *
	 */
	public void praiseComment(){
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		Element reqElement =  xmlDocUtil.getRequestData();
		String comment_id = reqElement.getChildText("comment_id");

		PlatformDao pdao = new PlatformDao();
		try{
			StringBuffer upSQL = new StringBuffer();
			upSQL.append("update learn_comment set praise_count=praise_count+1,");
			upSQL.append("praise_user=concat(ifnull(praise_user,''),',"+username+"')");
			upSQL.append(" where comment_id=?");
			upSQL.append(" and (praise_user not like ? or praise_user is null)");
			
			ArrayList<Object> paramList = new ArrayList<Object>();
			paramList.add(comment_id);	
			paramList.add("%"+username+"%");
			
			pdao.setSql(upSQL.toString());
			pdao.setBindValues(paramList);
			
			long upCount = pdao.executeTransactionSql();
			if (upCount==0){
				xmlDocUtil.writeErrorMsg("90103", "您已经点过赞了!");
			}
			else{
				StringBuffer strSQL = new StringBuffer();
				strSQL.append(" select comment_id,praise_count,praise_user from learn_comment");
				strSQL.append(" where comment_id=?");
				
				ArrayList<Object> qryParam = new ArrayList<Object>();
				qryParam.add(comment_id);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(qryParam);
				
				Element result = pdao.executeQuerySql(-1,0);
				xmlDocUtil.getResponse().addContent(result);
				
	    	    xmlDocUtil.setResult("0");	
	    	    xmlDocUtil.writeHintMsg("00103", "点赞成功!");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[留言管理-点赞]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 取留言数据
	 * @param pdao
	 * @param strSQL
	 * @return 留言分组信息
	 */
	/**
	public Element getCommentGroup(PlatformDao pdao,String strSQL) throws Exception{
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		String qry_end_date = reqElement.getChildText("qry_end_date");		
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select  * from ( ");
		sqlBuf.append(strSQL);
		paramList.add(userid);
		if (StringUtil.isNotEmpty(qry_begin_date)){
			sqlBuf.append(" and t1.create_date >= ?");
			paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd HH:mm:ss"));
		}			
		if (StringUtil.isNotEmpty(qry_end_date)){
			sqlBuf.append(" and t1.create_date <= ?");
			paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd HH:mm:ss"));
		}
		sqlBuf.append(" order by t1.create_date desc ");
		sqlBuf.append(") gc group BY group_id");
		
    	pdao.setSql(sqlBuf.toString());
    	pdao.setBindValues(paramList);
    	return pdao.executeQuerySql(0, -1);		
	}
	*/
	/**
	 * 取自己的消息组
	 *
	 */
	public void getMyCommentGroup(){
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String usertype = xmlDocUtil.getSession().getChildTextTrim("usertype");//用户类型
		Element reqElement =  xmlDocUtil.getRequestData();
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		String qry_end_date = reqElement.getChildText("qry_end_date");
		
		if (StringUtil.isEmpty(qry_end_date) && StringUtil.isEmpty(qry_end_date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-60);			
			qry_begin_date = sdf.format(calendar.getTime());
		}
		
		PlatformDao pdao = new PlatformDao();
		try {			
			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("select  * from ( ");	
			if ("1".equals(usertype)){
			   sqlBuf.append(" select * from comment_group_student t1");
			}
			else{
				sqlBuf.append(" select * from comment_group_teacher t1");
			}
			sqlBuf.append(" where  t1.userid=?");
			paramList.add(userid);
			if (StringUtil.isNotEmpty(qry_begin_date)){
				sqlBuf.append(" and t1.create_date >= ?");
				paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd HH:mm:ss"));
			}			
			if (StringUtil.isNotEmpty(qry_end_date)){
				sqlBuf.append(" and t1.create_date <= ?");
				paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd HH:mm:ss"));
			}	
			
			sqlBuf.append(" order by t1.create_date desc ");
			sqlBuf.append(") gc group BY comment_group_id order by create_date desc");
			
			
	    	pdao.setSql(sqlBuf.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);	
	    	
			xmlDocUtil.getResponse().addContent(result);			
	    	xmlDocUtil.setResult("0");				
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[留言管理-取我的留言组]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 取值大的userid
	 * @param userid1
	 * @param userid2
	 * @return
	 */
	public static String getMaxUser(String userid1,String userid2){
		if (userid1.compareTo(userid2)>0){
			return userid1;
		}
		else{
			return userid2;
		}
	}
	/**
	 * 增加留言
	 *
	 */
	public void addComment(){
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String portrait = xmlDocUtil.getSession().getChildTextTrim("portrait");
		String username = xmlDocUtil.getSession().getChildTextTrim("username");
		
		Element reqElement =  xmlDocUtil.getRequestData();		
		List attachList = reqElement.getChildren("attachment");
		
		String reqComment =  reqElement.getChildText("comment");
		if (StringUtil.isEmpty(reqComment) && attachList.size()==0){
			xmlDocUtil.writeErrorMsg("800102", "没有消息内容!");
			return;
		}
		else{
			if (StringUtil.isEmpty(reqComment)){
			   reqComment="";
			}
		}
		StringBuffer comment = new StringBuffer(reqComment);
		
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_comment");
			XmlDocPkgUtil.copyValues(reqElement, commentRec, 0, true);
			XmlDocPkgUtil.setChildText(commentRec, "comment", reqComment);
			
			String commentid = pdao.insertOneRecordSeqPk(commentRec).toString();
			
			for (int i=0;i<attachList.size();i++){
				Element attachRec = (Element)attachList.get(i);

				Element attachmentRec = ConfigDocument.createRecordElement("yuexue","learn_attachment");
				XmlDocPkgUtil.copyValues(reqElement, attachmentRec, 0 , true);
				attachmentRec.removeChild("attachment_id");
			    String srcFile =  FileUtil.getPhysicalPath(attachRec.getText());	
			    String desFile = FileUtil.getFileName(srcFile);
			    //每年每月创建一个文件夹
			    String currDate = DatetimeUtil.getCurrentDate();			    
			    String desPath = "upload/comment/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
			    

			    FileUtil.createDirs(FileUtil.getWebPath()+desPath, true);			    
				FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath()+desPath+desFile));
				FileUtil.deleteFile(srcFile);				

			    XmlDocPkgUtil.setChildText(attachmentRec, "file_path", "/"+desPath+desFile);
				XmlDocPkgUtil.setChildText(attachmentRec, "file_name", attachRec.getAttributeValue("name"));
				XmlDocPkgUtil.setChildText(attachmentRec, "file_size", attachRec.getAttributeValue("size"));
				XmlDocPkgUtil.setChildText(attachmentRec, "file_type", attachRec.getAttributeValue("ext"));
				
				String file_type = attachRec.getAttributeValue("ext");
				if (i>0 || StringUtil.isNotEmpty(reqComment)){
				     comment.append("<br>");
				}
				if (ImageUtils.isImage(file_type)){
					comment.append("<a href='/"+desPath+desFile+"' target='_blank'>");
					comment.append("<img src='/"+desPath+desFile+"' width='300' />");
					//comment.append(attachRec.getAttributeValue("name"));
					comment.append("</a>");					
				}
				else{
					comment.append("<a href='/"+desPath+desFile+"' target='_blank'>");
					comment.append("<img src='/images/file/"+file_type+".png' width='45' />");
					comment.append(attachRec.getAttributeValue("name"));
					comment.append("</a>");
				}
                //如果是图片就生成缩略图
				if (ImageUtils.isImage(file_type)){
					ImageUtils.createPreviewImage(FileUtil.getWebPath()+desPath+desFile);					
					XmlDocPkgUtil.setChildText(attachmentRec, "trans_status", "30");
					XmlDocPkgUtil.setChildText(attachmentRec, "access_path", desPath+desFile+"_s.jpg");
				}				
			
				String attachment_id = pdao.insertOneRecordSeqPk(attachmentRec).toString();
				
				if (StringUtil.isNotEmpty(commentid)){
					Element commAttachRec = ConfigDocument.createRecordElement("yuexue","learn_comment_attachment");
					XmlDocPkgUtil.copyValues(reqElement, commAttachRec, 0 , true);
					XmlDocPkgUtil.setChildText(commAttachRec, "attachment_id", attachment_id);
					XmlDocPkgUtil.setChildText(commAttachRec, "comment_id", commentid);
					
					pdao.insertOneRecordSeqPk(commAttachRec);
				}				
			}
			pdao.commitTransaction();	
			
			StringBuffer eventType= new StringBuffer("/comment");
			
			
			String classid = reqElement.getChildText("classid");
			String group_id = reqElement.getChildText("group_id");
			String attachment_id = reqElement.getChildText("attachment_id");
			String paper_id = reqElement.getChildText("paper_id");
			String receive_id = reqElement.getChildText("receive_id");	
			String paper_praxes_id = reqElement.getChildText("paper_praxes_id");
			if (StringUtil.isNotEmpty(group_id)){
				eventType.append("-group-"+group_id);
			}
			else if (StringUtil.isNotEmpty(paper_id)){
				if (StringUtil.isEmpty(classid)){
				   eventType.append("-paper-"+paper_id);
				}
				else{
				   eventType.append("-cp-"+classid+"-"+paper_id);
				}
			}			
			else if (StringUtil.isNotEmpty(receive_id)){
				eventType.append("-user-"+getMaxUser(userid,receive_id));
			}
			else if (StringUtil.isNotEmpty(attachment_id)){
				if (StringUtil.isEmpty(classid)){
				   eventType.append("-attachment-"+attachment_id);
				}
				else{
					eventType.append("-ca-"+classid+"-"+attachment_id);
				}				
			}
			else if (StringUtil.isNotEmpty(paper_praxes_id)){
				if (StringUtil.isEmpty(classid)){
					eventType.append("-praxesid-"+paper_praxes_id);
				}
				else{
					eventType.append("-cpp-"+classid+"-"+paper_praxes_id);
				}
			}
			else if (StringUtil.isNotEmpty(classid)){
				eventType.append("-class-"+classid);
			} 
			Event event = Event.createDataEvent(eventType.toString());
			event.setField("comment_id", commentid);//发送人
			event.setField("userid", userid);//发送人			
			event.setField("receive_id", reqElement.getChildText("receive_id"));//接收人
			event.setField("group_id", reqElement.getChildText("group_id"));//接受组
			event.setField("paper_id", reqElement.getChildText("paper_id"));//试卷ID
			event.setField("paper_praxes_id", reqElement.getChildText("paper_praxes_id"));//试题ID
			event.setField("attachment_id", reqElement.getChildText("attachment_id"));//教材附件ID
			event.setField("comment", comment.toString());//内容
			event.setField("create_by", username);	
			event.setField("send_portrait", portrait);	
			event.setField("create_date", DatetimeUtil.getNow(""));
			
			String p_comment_id = reqElement.getChildText("p_comment_id");
			if (StringUtil.isEmpty(p_comment_id)){
			    Dispatcher.getInstance().multicast(event);			
				//query
				StringBuffer attSQL = new StringBuffer();
		    	attSQL.append("select t2.comment_attachment_id,t1.* from learn_attachment t1,learn_comment_attachment t2");
		    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.comment_id=?");
		    	attSQL.append(" order by t2.create_date");
		    	
				StringBuffer strSQL = new StringBuffer();			
				strSQL.append("select t1.*,t2.username as send_name,t2.portrait as send_portrait,t2.gender,t2.usertype,t3.username as receive_name,t3.portrait as receive_portrait,");
				strSQL.append(" (select count(st.comment_attachment_id) from learn_comment_attachment st");
				strSQL.append("  where st.valid='Y' and st.comment_id=t1.comment_id) as attachment_count");
				strSQL.append(" from learn_comment t1 left join pcmc_user t3 on t3.userid = t1.receive_id");
				strSQL.append(" ,pcmc_user t2");
				strSQL.append(" where t1.valid='Y' and t1.userid=t2.userid and t1.comment_id=?");
				
				ArrayList<Object> paramList = new ArrayList<Object>();
				paramList.add(commentid);
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				Element result = pdao.executeQuerySql(-1, 1);
				List list = result.getChildren("Record");
		    	for (int i = 0; i < list.size() ;i++){
		    		Element rec = (Element)list.get(i);
		    		String attachment_count = rec.getChildText("attachment_count");
		    		if (!"0".equals(attachment_count)){
		    			ArrayList<Object> commentParam = new ArrayList<Object>();
		    			commentParam.add(rec.getChildText("comment_id"));
		    			
				    	pdao.setSql(attSQL.toString());
				    	pdao.setBindValues(commentParam); 
				    	
				    	Element attResult = pdao.executeQuerySql(0, -1);
				    	Element att = new Element("Attachments");
				    	att.addContent(attResult);
				    	rec.addContent(att);
		    		}
		    	}	    	
	    	    xmlDocUtil.getResponse().addContent(result);
			}
			else{
				
				ArrayList<Object> qryParam = new ArrayList<Object>();
				qryParam.add(p_comment_id);					
				/**
				StringBuffer upSQL = new StringBuffer();
				upSQL.append("update learn_comment set create_date=now()");
				upSQL.append(" where comment_id=?");
				pdao.setSql(upSQL.toString());
				pdao.setBindValues(qryParam);
				pdao.executeTransactionSql();
				*/
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("select comment_id,p_comment_id,comment,create_by as send_name,create_date");
				strSQL.append(" from learn_comment where p_comment_id=? order by create_date desc");
				
				pdao.setSql(strSQL.toString());
				pdao.setBindValues(qryParam);
				
				Element result = pdao.executeQuerySql(10,1);
				xmlDocUtil.getResponse().addContent(result);				
			}
	    	xmlDocUtil.setResult("0");	
	    	xmlDocUtil.writeHintMsg("00103", "留言成功!");			
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[留言管理-增加留言]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);            			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * 检索留言
	 *
	 */
	public void queryComment(){
		String myuserid = xmlDocUtil.getSession().getChildTextTrim("userid");
		//String usertype = xmlDocUtil.getSession().getChildTextTrim("usertype");
		Element reqElement =  xmlDocUtil.getRequestData();
		//String qry_show_group = reqElement.getChildText("qry_show_group");
		
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		strSQL.append("select t1.*,t2.username as send_name,t2.portrait as send_portrait,t2.gender,");
		strSQL.append(" t2.usertype,t3.username as receive_name,t3.portrait as receive_portrait,");
		strSQL.append(" if(t1.userid='"+myuserid+"','1','0') as selfcomment,");
		strSQL.append(" (select count(st.comment_attachment_id) from learn_comment_attachment st");
		strSQL.append("  where st.valid='Y' and st.comment_id=t1.comment_id) as attachment_count,");
		strSQL.append(" (select count(sd.comment_id) from learn_comment sd");
		strSQL.append("  where sd.valid='Y' and sd.p_comment_id=t1.comment_id) as replay_count");
		strSQL.append(" from learn_comment t1 left join pcmc_user t3 on t3.userid = t1.receive_id");
		strSQL.append(" ,pcmc_user t2");
		strSQL.append(" where t1.valid='Y' and t1.userid=t2.userid and (p_comment_id is null or p_comment_id='')");
		
		String qry_userid = reqElement.getChildText("qry_userid");
		
		if (StringUtil.isNotEmpty(qry_userid)){
			strSQL.append(" and t1.userid=?");	
			paramList.add(qry_userid);
		}
		/** v2.0起，取消点对点的交流
		if (StringUtil.isNotEmpty(qry_userid)){
			//他发给我的
		    strSQL.append(" and ((t1.userid=? and t1.receive_id=? ) or ");
		    paramList.add(qry_userid);
		    paramList.add(myuserid);
		    //我发给他的
		    strSQL.append("       (t1.userid=? and t1.receive_id=?))");
		    paramList.add(myuserid);
		    paramList.add(qry_userid);
		}
		*/
		String qry_classid = reqElement.getChildText("qry_classid");
		if (StringUtil.isNotEmpty(qry_classid)){
		    strSQL.append(" and t1.classid=?");
		    paramList.add(qry_classid);
		}
		String qry_group_id = reqElement.getChildText("qry_group_id");
		if (StringUtil.isNotEmpty(qry_group_id)){
		    strSQL.append(" and t1.group_id=?");
		    paramList.add(qry_group_id);
		}		
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)){
		    strSQL.append(" and t1.paper_id=?");
		    paramList.add(qry_paper_id);
		}
		String qry_paper_praxes_id = reqElement.getChildText("qry_paper_praxes_id");
		if (StringUtil.isNotEmpty(qry_paper_praxes_id)){
		    strSQL.append(" and t1.paper_praxes_id=?");
		    paramList.add(qry_paper_praxes_id);
		}		
		String qry_send_id = reqElement.getChildText("qry_send_id");
		if (StringUtil.isNotEmpty(qry_send_id)){
		    strSQL.append(" and t1.send_id=?");
		    paramList.add(qry_send_id);
		}		
		String qry_attachment_id = reqElement.getChildText("qry_attachment_id");
		if (StringUtil.isNotEmpty(qry_attachment_id)){
		    strSQL.append(" and t1.attachment_id=?");
		    paramList.add(qry_attachment_id);
		}		
		String qry_comment = reqElement.getChildText("qry_comment");
		if (StringUtil.isNotEmpty(qry_comment)){
		    strSQL.append(" and t1.comment like ?");
		    paramList.add("%"+qry_comment+"%");
		}
		
		String qry_begin_date = reqElement.getChildText("qry_begin_date");
		if (StringUtil.isNotEmpty(qry_begin_date)){
		    strSQL.append(" and t1.create_date >= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_begin_date, "yyyy-MM-dd HH:mm:ss"));
		}		
		String qry_end_date = reqElement.getChildText("qry_end_date");
		if (StringUtil.isNotEmpty(qry_end_date)){
		    strSQL.append(" and t1.create_date <= ?");
		    paramList.add(SqlTypes.getConvertor("Timestamp").convert(qry_end_date, "yyyy-MM-dd HH:mm:ss"));
		}
		String qry_show_top = reqElement.getChildText("qry_show_top");
		if ("Y".equals(qry_show_top)){
		    strSQL.append(" and t1.show_top=?");
		    paramList.add(qry_show_top);			
		}
		
		if (StringUtil.isNotEmpty(qry_begin_date)){
			strSQL.append(" order by t1.create_date");
		}
		else{
			strSQL.append(" order by t1.create_date desc");
		}		
		
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result=pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
	    	
	    	StringBuffer attSQL = new StringBuffer();
	    	attSQL.append("select t2.comment_attachment_id as file_id,t1.* from learn_attachment t1,learn_comment_attachment t2");
	    	attSQL.append(" where t2.valid='Y' and t1.attachment_id=t2.attachment_id and t2.comment_id=?");
	    	attSQL.append(" order by t2.create_date");
	    	
	    	StringBuffer replaySQL = new StringBuffer();
	    	replaySQL.append("select t1.*,t2.username as send_name,t2.portrait as send_portrait,t2.gender,t2.usertype");
	    	replaySQL.append(" from learn_comment t1,pcmc_user t2 ");
	    	replaySQL.append(" where t1.valid='Y' and t1.userid=t2.userid");
	    	replaySQL.append(" and t1.p_comment_id=?");
	    	replaySQL.append(" order by t1.create_date");
	    	
	    	
	    	List list = result.getChildren("Record");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element commentRec = (Element)list.get(i); 
    			ArrayList<Object> childParam = new ArrayList<Object>();
    			childParam.add(commentRec.getChildText("comment_id"));
    			
	    		String attachment_count = commentRec.getChildText("attachment_count");	    		
	    		if (!"0".equals(attachment_count)){	    			
			    	pdao.setSql(attSQL.toString());
			    	pdao.setBindValues(childParam); 			    	
			    	Element attResult = pdao.executeQuerySql(0, -1);
			    	Element att = new Element("Attachments");
			    	att.addContent(attResult);
			    	commentRec.addContent(att);				    	
	    		}
	    		
	    		String replay_count = commentRec.getChildText("replay_count");
	    		if (!"0".equals(replay_count)){
			    	pdao.setSql(replaySQL.toString());
			    	pdao.setBindValues(childParam); 			    	
			    	Element replayResult = pdao.executeQuerySql(0, -1);
			    	Element Replay = new Element("Replays");
			    	Replay.addContent(replayResult);
			    	commentRec.addContent(Replay);		    			
	    		}
	    	}
	    	
    	    xmlDocUtil.getResponse().addContent(result);
	    	
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[留言管理-检索留言]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * 获得群组、教师、同学栏目
	 */
	public void getCommentMenuInfo(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");//用户ID
		String deptid   = xmlDocUtil.getSession().getChildTextTrim("deptid");//部门编号
		String usertype = xmlDocUtil.getSession().getChildTextTrim("usertype");//用户类型
		Element reqElement =  xmlDocUtil.getRequestData();
		String menuFlag= reqElement.getChildText("flag");
        
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		if("G".equals(menuFlag)){//群组
			strSQL.append("SELECT DISTINCT 'G' as menu_flag,t.group_id,'' as classid,t.group_subject,t.group_name,'' as userid,t3.subjectid,t4.subjname,t5.classnm ");
			strSQL.append(" FROM learn_group t,learn_group_member t2 ,base_teacher_subject t3,base_subject t4,base_class t5");
			strSQL.append(" WHERE t.group_id = t2.group_id AND t.valid='Y' AND t3.state>'0' AND t3.subjectid = t4.subjectid AND t.classid = t3.classid and t.classid = t5.classid AND ( t.userid=? or t2.userid=? ) ORDER BY group_id ");
			paramList.add(userid);
			paramList.add(userid);
		}else if ("T".equals(menuFlag)){//教师
			if ("1".equals(usertype)){
				strSQL.append("SELECT 'T' as menu_flag, '' as group_id,'' as classid,'' as group_subject,t3.username AS group_name,t3.userid,t3.portrait,t3.gender,t3.usertype,t4.subjectid,t1.studentno,t4.subjname,'' as classnm ");
				strSQL.append("FROM base_studentinfo t1,base_teacher_subject t2,pcmc_user t3,base_subject t4 ");
				strSQL.append(" where t1.classid=t2.classid AND t2.state>'0' AND t2.subjectid=t4.subjectid AND t2.userid=t3.userid and t1.userid=?");			
				paramList.add(userid);
			}
			else{//教师身份则取本校所有教师
				strSQL.append("select 'T' as menu_flag, '' as group_id,'' as classid,'' as group_subject,t1.username AS group_name,t1.userid,t1.portrait,t1.gender,t1.usertype,'' as subjectid,'' as studentno,'' as subjname,'' as classnm");
				strSQL.append(" from  pcmc_user t1,pcmc_user_dept t2");
				strSQL.append(" where t1.userid = t2.userid and t1.usertype='2' and t2.deptid=?");			
				paramList.add(deptid);				
			}
		}else if("S".equals(menuFlag)){//同学
			if ("1".equals(usertype)){
				strSQL.append("SELECT 'S' as menu_flag,'' AS group_id,'' as classid,'' AS group_subject,t3.username AS group_name,t3.userid,t3.portrait,t3.gender,t3.usertype,t5.subjectid,t1.studentno,t4.subjname,'' as classnm ");
				strSQL.append(" FROM base_studentinfo t1,base_studentinfo t2,pcmc_user t3,base_subject t4,base_teacher_subject t5 ");
				strSQL.append(" WHERE t1.classid = t2.classid AND t2.userid = t3.userid AND t2.classid = t5.classid ");
				strSQL.append(" AND t4.subjectid = t5.subjectid AND t5.state>'0' and t1.deptid=? AND t1.userid=?");
				paramList.add(deptid);
				paramList.add(userid);
			}
			else{//教师身份则取本人任教的所有班级学生
				strSQL.append("select 'S' as menu_flag,'' AS group_id,t2.classid,'' AS group_subject,t1.username AS group_name,t1.userid,t1.portrait,t1.gender,t1.usertype,t3.subjectid,t2.studentno,'' as subjname,t4.classnm");
				strSQL.append(" from  pcmc_user t1,base_studentinfo t2,base_teacher_subject t3,base_class t4 ");
				strSQL.append(" where t1.userid = t2.userid and   t1.usertype='1' and   t2.state>'0' and   t3.state>'0' and t4.state>'0'");
				strSQL.append(" and   t2.classid = t3.classid and   t2.classid = t4.classid and   t3.userid =?");
				paramList.add(userid);
			}
		}
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[讨论组-获取群组、教师、同学栏目列表]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
}
