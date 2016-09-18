package cn.com.bright.yuexue.video;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.teach.ImageMarkLogoUtil;
import cn.com.bright.yuexue.util.MD5FileUtil;

/**
 * <p>Title:��Ƶ�μ�����</p>
 * <p>Description: ��Ƶ�μ�����</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * ��ѯ��ʦ��ѧ������Ƶ�б�ķ���
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> jiangyh    2015/03/09       1.0          build this moudle </p>
 *     
 */
public class Video {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());

	/**
	 * ��̬ί�����
	 * 
	 * @param request
	 *            xmlDoc
	 * @return response xmlDoc
	 */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();

		if ("queryStudVideo".equals(action)) {
			queryStudVideo();
		} else if ("queryTechVideo".equals(action)) {
			queryTechVideo();
		} else if ("uploadVideoList".equals(action)) {
			uploadVideoList();
		} else if ("addVideoLog".equals(action)) {
			addVideoLog();
		} else if ("updaeShareVideo".equals(action)) {
			updaeShareVideo();
		} else if ("updateVideodz".equals(action)) {
			updateVideodz();
		} else if ("delVideoRes".equals(action)) {
			delVideoRes();
		} else if ("updateVideoInfo".equals(action)) {
			updateVideoInfo();
		} else if ("queryNotLoginVideo".equals(action)) {
			queryNotLoginVideo();
		} else if ("queryVideoByUser".equals(action)) {
			queryVideoByUser();
		} 
		return xmlDoc;
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ�����
	 */
	public void queryVideoByUser(){	
		Element reqElement = xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		strSQL.append(" select * from sell_read_log t1 where t1.userid=? and t1.paper_info_id=? and t1.praise='Y' ");
		paramList.add(userid);
		paramList.add(paper_id);
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(0, -1);

			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-��Ƶ������]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}	
	}
	
	/**
	 *������Ƶ�μ�������Դ�� 
	 */
	public void delVideoRes(){
		Element reqElement = xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");

		String[] returnData = { "paper_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();

			strSQL.append(" update learn_examination_paper lep ");
			strSQL.append(" set lep.resource_id = ? ");
			strSQL.append(" where lep.paper_id = ? ");
			paramList.add("");
			paramList.add(paper_id);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_id }));

			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600112", "������Ƶ����Դ��");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-������Ƶ����Դ��]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
		
	}
	/**
	 * �޸ĵ���
	 */
	public void updateVideodz(){
		String userid   = xmlDocUtil.getSession().getChildTextTrim("userid");
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String praise = reqElement.getChildText("praise");
		PlatformDao pdao = new PlatformDao();
		try {		
			pdao.beginTransaction();
			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();

			strSQL.append(" update sell_read_log ll ");
			strSQL.append(" set ll.praise ='N' ");
			strSQL.append(" where ll.userid = ? ");
			paramList.add(userid);
			strSQL.append(" and ll.paper_info_id = ? ");
			paramList.add(paper_id);
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();

			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[��Դ����-ȡ������]"+e.getMessage());
			xmlDocUtil.writeHintMsg("��Դ����-ȡ������]ʧ��");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ������Ƶ�Ĺ�����
	 */
	public void updaeShareVideo() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String paper_id = reqElement.getChildText("paper_id");
		String share_level = reqElement.getChildText("share_level");

		String[] returnData = { "paper_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();

			strSQL.append(" update learn_examination_paper lep ");
			strSQL.append(" set lep.share_level = ? ");
			strSQL.append(" where lep.paper_id = ? ");
			paramList.add(share_level);
			paramList.add(paper_id);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_id }));

			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600112", "������Ƶ�Ĺ�����");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-������Ƶ�Ĺ�����]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * �������һ�β��ŵ���־�Ĳ���ʱ����������Ƶ��־
	 */
	public void addVideoLog() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String attachment_id = reqElement.getChildText("attachment_id");

		String[] returnData = { "log_id", "play_time" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		String play_time = "0";
		try {
			pdao.beginTransaction();

			StringBuffer strSQL = new StringBuffer();
			ArrayList<Object> paramList = new ArrayList<Object>();

			strSQL.append(" select * from learn_attachment_log lal ");
			strSQL.append(" where lal.userid = ? ");
			strSQL.append(" and lal.attachment_id = ? ");
			strSQL.append(" order by lal.create_date desc ");
			paramList.add(userid);
			paramList.add(attachment_id);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(0, -1); // ֻȡ��һ��
			List queryList = result.getChildren("Record");

			if (queryList.size() > 0) {
				Element lalRec = (Element) queryList.get(0);
				play_time = lalRec.getChildText("play_time");
			}

			Element learnAttachmentLogRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment_log");
			XmlDocPkgUtil.copyValues(reqElement, learnAttachmentLogRec, 0, true);
			XmlDocPkgUtil.setChildText(learnAttachmentLogRec, "attachment_id", attachment_id);

			// �������ݵ�learn_examination_paper
			String log_id = pdao.insertOneRecordSeqPk(learnAttachmentLogRec).toString();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { log_id, play_time }));

			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600111", "�������һ�β��ŵ���־�Ĳ���ʱ����������Ƶ��־!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-�ϴ���Ƶ�μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ��ȡѧ������Ƶ�μ��б�
	 */
	public void queryStudVideo() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String deptid = xmlDocUtil.getSession().getChildText("deptid");
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		strSQL.append(" select t2.folder_id, t2.subject_id,t8.subjname, t5.subject_book_id, t2.paper_id, t2.resource_type, t2.choose_type, t2.paper_name ");
		strSQL.append(" , t7.file_name, t7.attachment_id, t7.file_path, t7.file_type, t7.access_path,t7.create_by ");
		strSQL.append(" , t7.cover_path, t7.watch_scale, t7.watch_count, t7.watch_again_count, t7.avg_score,t9.gradename ");
		strSQL.append(" , (select count(1) from learn_attachment_log al where al.attachment_id=t7.attachment_id) as playcount ");
		strSQL.append(" from learn_my_examination t1, learn_examination_paper t2, base_course t3, base_studentinfo t4, base_book_folder t5, learn_paper_attachment t6, learn_attachment t7,base_subject t8,base_grade t9 ");
		strSQL.append(" where t1.paper_id = t2.paper_id and t2.subject_id = t3.subjectid and t2.folder_id = t5.folder_id ");
		strSQL.append(" and t2.paper_id = t6.paper_id and t6.attachment_id = t7.attachment_id and t8.subjectid=t2.subject_id and t2.grade_code=t9.gradecode ");
		strSQL.append(" and t2.grade_code = t3.gradecode and t1.valid = 'Y' and t2.valid = 'Y' ");
		strSQL.append(" and t1.userid = t4.userid and t4.state > '0' ");
		if(deptid!=null){
			strSQL.append(" and t3.deptid = ? ");
			paramList.add(deptid);
		}
		if(userid!=null){
			strSQL.append(" and t1.userid = ? ");
			paramList.add(userid);
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)) {
			strSQL.append(" and t2.resource_type like ? ");
			paramList.add(qry_resource_type + "%");
		}
		String qry_folder_id = reqElement.getChildText("qry_folder_id"); // ���ٹ���folder_id
		if (StringUtil.isNotEmpty(qry_folder_id)) {
			 strSQL.append(" and t5.folder_code like ? ");
			 paramList.add("%" + qry_folder_id + "%");
		}

		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)) {
			strSQL.append(" and t2.paper_id = ? ");
			paramList.add(qry_paper_id);
		}
		// ���ݴ���ʱ������
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t7.create_date desc ");
		}

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-ȡѧ������Ƶ(�μ�)�б�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ��ȡ����¼״̬����Ƶ�μ��б�
	 */
	public void queryNotLoginVideo() {
		Element reqElement = xmlDocUtil.getRequestData();

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		strSQL.append(" select DISTINCT t2.paper_id, t2.folder_id,t2.create_by, t2.subject_id,t8.subjname, t5.subject_book_id, t2.resource_type, t2.choose_type, t2.paper_name ");
		strSQL.append(" , t7.file_name, t7.attachment_id, t7.file_path, t7.file_type, t7.access_path,t9.gradename ");
		strSQL.append(" , t7.cover_path, t7.watch_scale, t7.watch_count, t7.watch_again_count, t7.avg_score ");
		strSQL.append(" , (select count(1) from learn_attachment_log al where al.attachment_id=t7.attachment_id) as playcount ");
		strSQL.append(" from learn_examination_paper t2, base_course t3, base_book_folder t5, learn_paper_attachment t6, learn_attachment t7,base_subject t8,base_grade t9 ");
		strSQL.append(" where t2.subject_id = t3.subjectid and t2.folder_id = t5.folder_id ");
		strSQL.append(" and t2.paper_id = t6.paper_id and t6.attachment_id = t7.attachment_id and t2.subject_id=t8.subjectid and t2.grade_code=t9.gradecode ");
		strSQL.append(" and t2.grade_code = t3.gradecode and t2.valid = 'Y' ");

		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)) {
			strSQL.append(" and t2.resource_type like ? ");
			paramList.add(qry_resource_type + "%");
		}
		String qry_folder_id = reqElement.getChildText("qry_folder_id"); // ���ٹ���folder_id
		if (StringUtil.isNotEmpty(qry_folder_id)) {
			// strSQL.append(" and t5.folder_code like ? ");
			// paramList.add("%" + qry_folder_id + "%");
		}

		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)) {
			strSQL.append(" and t2.paper_id = ? ");
			paramList.add(qry_paper_id);
		}
		// ���ݴ���ʱ������
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t7.create_date desc ");
		}

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			//Element result = pdao.executeQuerySql(0, -1);

			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-ȡ����¼״̬�µ���Ƶ(�μ�)�б�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * ��ȡ��ʦ����Ƶ�μ��б�
	 */
	public void queryTechVideo() {
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");

		Element reqElement = xmlDocUtil.getRequestData();
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append(" select  distinct t1.paper_id,t1.resource_id,t1.folder_id,t2.subjname,t3.folder_name, t1.subject_id,t1.grade_code,t3.subject_book_id, t1.resource_type, t1.choose_type, t1.paper_name, t1.remark ");
		strSQL.append(" , t7.file_name, t4.gradename, t7.attachment_id, t7.file_path,t7.video_time,t1.create_by, t7.file_type, t7.access_path,t1.create_date ");
		strSQL.append(" , t7.cover_path, t7.watch_scale, t7.watch_count, t7.watch_again_count, t7.avg_score, ");
		strSQL.append(" (select count(1) from learn_attachment_log al where al.attachment_id=t7.attachment_id) as playcount, ");
		strSQL.append(" (select count(*) FROM sell_read_log t2 WHERE t1.paper_id = t2.paper_info_id) AS readcount,");
		strSQL.append(" (select count(1) from learn_comment tabcomment where   tabcomment.valid ='Y' and tabcomment.paper_id = t1.paper_id) AS commentcount");
		strSQL.append(" from learn_examination_paper t1, base_subject t2, base_book_folder t3, base_grade t4, learn_paper_attachment t6, learn_attachment t7 ");
		strSQL.append(" where t1.valid = 'Y' and t1.subject_id = t2.subjectid  and t1.grade_code=t4.gradecode and t1.folder_id = t3.folder_id");
		strSQL.append(" and t1.paper_id = t6.paper_id and t6.attachment_id = t7.attachment_id ");

		// �����ҵ��ϴ������� 1-�ҵ��ϴ�; 0-ȫ��(����) ���ݴ���ʱ������
		String qry_all = reqElement.getChildText("qry_all");

		if (StringUtil.isEmpty(qry_all)) {
			qry_all = "0";
		}
		if(userid!=null){
			if ("0".equals(qry_all)) {
				// ˽��
				// strSQL.append(" and ( (t1.share_level = 10 and t1.user_id = ? ) ");
				strSQL.append(" and ( (t1.user_id = ? ) ");
				paramList.add(userid);
				// У�ڹ���
				strSQL.append("       or (t1.share_level = 50 and t1.deptcode = ? ) ");
				paramList.add(deptcode);
				// ��ȫ����
				strSQL.append("       or (t1.share_level = 90) ");
				strSQL.append(" ) ");
			} else {
				// �ҵ��ϴ�
				strSQL.append(" and ( t1.user_id = ? ) ");
				paramList.add(userid);
			}
		}
		String qry_resource_type = reqElement.getChildText("qry_resource_type");
		if (StringUtil.isNotEmpty(qry_resource_type)) {
			strSQL.append(" and t1.resource_type like ? ");
			paramList.add(qry_resource_type + "%");
		}
		String qry_user_id = reqElement.getChildText("qry_user_id");
		if (StringUtil.isNotEmpty(qry_user_id)) {
			strSQL.append(" and t1.user_id = ? ");
			paramList.add(qry_user_id);
		}
		String qry_folder_id = reqElement.getChildText("qry_folder_id");
		if (StringUtil.isNotEmpty(qry_folder_id)) { 
			 strSQL.append(" and t3.folder_code like ? ");
			 paramList.add("%" + qry_folder_id + "%");
		}
		String qry_paper_name = reqElement.getChildText("qry_paper_name");
		if (StringUtil.isNotEmpty(qry_paper_name)) { 
			 strSQL.append(" and ((t1.paper_name like ?) or (concat(t4.gradename,t2.subjname ) like ? )) ");
			 paramList.add("%"+qry_paper_name+"%");
			 paramList.add("%"+qry_paper_name+"%");
		}
		String qry_paper_id = reqElement.getChildText("qry_paper_id");
		if (StringUtil.isNotEmpty(qry_paper_id)) { 
			 strSQL.append(" and t1.paper_id = ? ");
			 paramList.add(qry_paper_id);
		}
        String qry_subject_id =reqElement.getChildText("qry_subject_id");
        if (StringUtil.isNotEmpty(qry_subject_id)) {
			strSQL.append(" and t1.subject_id = ? ");
			paramList.add(qry_subject_id);
		}
		// ���ݴ���ʱ������
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(orderBy);
		} else {
			strSQL.append(" order by t1.create_date desc,readcount desc ");
		}

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			//Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			Element result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-ȡ��ʦ����Ƶ(�μ�)�б�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * �޸���Ƶ�μ�
	 */
	public void updateVideoInfo(){
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildText("userid");
		String paper_id = reqElement.getChildText("paper_id");
		String paper_name = reqElement.getChildText("paper_name");
		String cover_path = reqElement.getChildText("cover_path");
		String resource_id = reqElement.getChildText("resource_id");
		List coverList = reqElement.getChildren("cover_attachment");

		String[] returnData = { "paper_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			//����·��
			if (coverList.size() > 0) {
				Element coverPathRec = (Element) coverList.get(0);
				cover_path = getUploadFilePath(coverPathRec, "video");
				
				StringBuffer strSQL = new StringBuffer();
				ArrayList<Object> paramList = new ArrayList<Object>();

				strSQL.append(" update learn_attachment t1,learn_examination_paper t2,learn_paper_attachment t3 set t1.cover_path=?  ");
				paramList.add(cover_path);
				strSQL.append(" where t1.attachment_id=t3.attachment_id and t2.paper_id = t3.paper_id and t3.paper_id=? ");
				paramList.add(paper_id);

				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList); 
				pdao.executeTransactionSql();

				resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_id }));
				
			}
			Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_paper");
			XmlDocPkgUtil.copyValues(reqElement, commentRec, 0, true);
			
			XmlDocPkgUtil.setChildText(commentRec, "paper_id",paper_id);
			XmlDocPkgUtil.setChildText(commentRec, "paper_name",URLDecoder.decode(paper_name,"utf-8"));
			XmlDocPkgUtil.setChildText(commentRec, "cover_path",cover_path);
			XmlDocPkgUtil.setChildText(commentRec, "resource_id",resource_id);

			pdao.updateOneRecord(commentRec);
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[�μ�����-������Դ��]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
		
	}
	
	/**
	 * �ϴ���Ƶ�μ�
	 * ��ر�
	 * learn_examination_paper - ��Ϣ
	 * learn_paper_attachment - �м��
	 * learn_attachment - ������
	 */
	public void uploadVideoList() {
		Element reqElement = xmlDocUtil.getRequestData();
 		String resource_type = reqElement.getChildText("resource_type");
		if (StringUtil.isEmpty(resource_type)) {
			resource_type = "2010"; // ��Դ����Ϊ΢��Ƶ
		}
		String choose_type = reqElement.getChildText("choose_type");
		if (StringUtil.isEmpty(choose_type)) {
			choose_type = "10"; //
		}
		String share_level = reqElement.getChildText("share_level");
		if (StringUtil.isEmpty(share_level)) {
			// 10˽�� 50 ��λ 90��ȫ����
			share_level = "90"; // ����Χ
		}
		String subject_id = reqElement.getChildText("subject_id");
		String grade_code = reqElement.getChildText("grade_code");
		String folder_id = reqElement.getChildText("folder_id");
		String paper_name = reqElement.getChildText("paper_name");
		
		// �����б� ���
		List videoList = reqElement.getChildren("video_attachment");
		// ���� 0������1��
		List coverList = reqElement.getChildren("cover_attachment");
		
		String[] returnData = { "paper_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			paper_name = URLDecoder.decode(paper_name,"utf-8");
			//����·��
			String cover_path = reqElement.getChildText("cover_path");
			if (coverList.size() > 0) {
				Element coverPathRec = (Element) coverList.get(0);
				cover_path = getUploadFilePath(coverPathRec, "video");
			}else{
				
				StringBuffer strSQL = new StringBuffer();
				ArrayList<Object> paramList = new ArrayList<Object>();

				strSQL.append(" SELECT t3.gradename, t2.subjname  FROM	pcmc_dept t1,	base_subject t2,base_grade t3,base_book_folder t4,base_subject_book t5  ");
				strSQL.append(" where t2.subjectid= t5.subjectid AND t3.gradecode = t5.grade_code ");
				strSQL.append(" and t4.subject_book_id = t5.subject_book_id and t1.deptid = t3.deptid ");
				strSQL.append(" and t3.gradeid and t4.folder_id=? and t2.subjectid =? and t3.gradecode=? ");
				paramList.add(folder_id);
				paramList.add(subject_id);
				paramList.add(grade_code);

				pdao.setSql(strSQL.toString());
				pdao.setBindValues(paramList);
				Element result = pdao.executeQuerySql(0, -1); // ֻȡ��һ��
				List queryList = result.getChildren("Record");
				String gradename="";
				String subjname="";
				if (queryList.size() > 0) {
					Element lalRec = (Element) queryList.get(0);
					gradename = lalRec.getChildText("gradename");
					subjname = lalRec.getChildText("subjname");
				}
				if(subjname.equals("˼��Ʒ������")){
					subjname="˼Ʒ";
				}
				String username = xmlDocUtil.getSession().getChildTextTrim("username");
				String deptname = xmlDocUtil.getSession().getChildTextTrim("deptname");
				String srcImgPath = FileUtil.getWebPath() + "images/pic/bg.png";
				cover_path = "/images/pic/" + DatetimeUtil.getNow("yyyyMMddHHmmss") + ".jpg";
				String targerPath = FileUtil.getWebPath() + cover_path;
				ImageMarkLogoUtil.markImageByText(srcImgPath, targerPath , paper_name,gradename+subjname, username, deptname);
			}
			
			Element examPaperRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_paper");
			XmlDocPkgUtil.copyValues(reqElement, examPaperRec, 0, true);
			XmlDocPkgUtil.setChildText(examPaperRec, "subject_id",subject_id);
			XmlDocPkgUtil.setChildText(examPaperRec, "grade_code", grade_code);
			XmlDocPkgUtil.setChildText(examPaperRec, "folder_id",folder_id);
			XmlDocPkgUtil.setChildText(examPaperRec, "resource_type", resource_type);
			XmlDocPkgUtil.setChildText(examPaperRec, "choose_type", choose_type);
			XmlDocPkgUtil.setChildText(examPaperRec, "share_level", share_level);
			XmlDocPkgUtil.setChildText(examPaperRec, "cover_path", cover_path);
			
			XmlDocPkgUtil.setChildText(examPaperRec, "paper_name", paper_name);

			// �������ݵ�learn_examination_paper
			String paper_id = pdao.insertOneRecordSeqPk(examPaperRec).toString();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_id }));

			if (videoList.size() > 0) {
				// ���渽��
				for (int i = 0; i < videoList.size(); i++) {
					Element videoRec = (Element) videoList.get(i);
					if (videoRec != null) {
						//д�븽����Ϣ�͸�����
						//String attachment_id = AttachmentUtil.moveFile(pdao, videoRec, "video");
						String attachment_id = moveFile(pdao, videoRec, "video", cover_path);
						
						//д���м��learn_paper_attachment
						Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_attachment");
						XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", attachment_id);
						XmlDocPkgUtil.setChildText(attachmentRec, "paper_id", paper_id);
						XmlDocPkgUtil.setChildText(attachmentRec, "valid", "Y");

						pdao.insertOneRecordSeqPk(attachmentRec);
					}
				}
			}
			String attachmentId = reqElement.getChildText("attachment_id");
			if(!attachmentId.equals("")){
				//д���м��learn_paper_attachment
				Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_attachment");
				XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", attachmentId);
				XmlDocPkgUtil.setChildText(attachmentRec, "paper_id", paper_id);
				XmlDocPkgUtil.setChildText(attachmentRec, "valid", "Y");

				pdao.insertOneRecordSeqPk(attachmentRec);					
			}
			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600101", "�ϴ���Ƶ�μ��ɹ�!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-�ϴ���Ƶ�μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	public static String getUploadFilePath(Element fileElement, String destFile) throws IOException {
		String srcFile = FileUtil.getPhysicalPath(fileElement.getText());
		String desFileName = FileUtil.getFileName(srcFile);
		//ÿ��ÿ�´���һ���ļ���
		String currDate = DatetimeUtil.getCurrentDate();
		String desPath = "upload/" + destFile + "/" + currDate.substring(0, 4) + "/" + currDate.substring(5, 7) + "/";

		FileUtil.createDirs(FileUtil.getWebPath() + desPath, true);
		FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath() + desPath + desFileName));
		FileUtil.deleteFile(srcFile);

		return "/" + desPath + desFileName;
	}

	public static String moveFile(PlatformDao pdao, Element fileElement, String destFile, String coverPath) throws Exception {
		if (fileElement == null) {
			return null;
		} else {
			Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment");
			XmlDocPkgUtil.copyValues(fileElement, attachmentRec, 0, true);
			String file_path = getUploadFilePath(fileElement, destFile);
			String file_type = fileElement.getAttributeValue("ext").toLowerCase();
			String file_src = FileUtil.getWebPath()+file_path;
			String file_md5 = MD5FileUtil.getFileMD5String(new File(file_src));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_path", file_path);
			XmlDocPkgUtil.setChildText(attachmentRec, "file_name", fileElement.getAttributeValue("name"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_size", fileElement.getAttributeValue("size"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_type", file_type);
			XmlDocPkgUtil.setChildText(attachmentRec, "cover_path", coverPath);
			XmlDocPkgUtil.setChildText(attachmentRec, "file_md5", file_md5);

			// ����ʱ�� swf������
			if ("mp3,mp4,avi,mov".indexOf(file_type) > -1) {
				File source = new File(FileUtil.getWebPath() + file_path);
				Encoder encoder = new Encoder();
				MultimediaInfo m = encoder.getInfo(source);
				long duration = m.getDuration();
				// ת��Ϊ��
				long video_time = (duration / 1000);
				XmlDocPkgUtil.setChildText(attachmentRec, "video_time", "" + video_time);
			}

			return pdao.insertOneRecordSeqPk(attachmentRec).toString();
		}
	}

}
