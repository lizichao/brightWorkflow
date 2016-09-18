package cn.com.bright.yuexue.sell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;

import com.sun.org.apache.xpath.internal.operations.Bool;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:���̳ǹ��ܹ���</p>
 * <p>Description: ���̳ǹ��ܹ���</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * �ϴ����̳ǿμ� ��ʦ�Ƽ��μ� ѧ����ȡ�Ƽ��μ� ѧ������μ�
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> jiangyh   2015/03/16       1.0          build this moudle </p>
 *     
 */
public class Sell {
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

		if ("uploadPaperInfoList".equals(action)) {
			// �ϴ����̳ǿμ�
			uploadPaperInfoList();
		} else if ("addPaperRecommended".equals(action)) {
			// ���̳�-��ʦ�Ƽ��μ�
			addPaperRecommended();
		} else if ("queryPaperRecommended".equals(action)) {
			// ���̳�-ѧ����ȡ�Ƽ��μ�
			queryPaperRecommended();
		} else if ("buyPaperInfo".equals(action)) {
			// ���̳�-ѧ������μ�
			buyPaperInfo();
		} else if ("queryPaperInfo".equals(action)) {
			// ���̳�-��ʦ��ȡ�μ�
			queryPaperInfo();
		} else if ("querySellIntegralLog".equals(action)) {
			// ��ѯ���ֵ��������
			querySellIntegralLog();
		} else if ("addPayLog".equals(action)) {
			//��ֵ
			addPayLog();
		} else if ("uptPayLog".equals(action)) {
			uptPayLog();
		}else if ("addShareByUser".equals(action)) {
			addShareByUser();
		}
		return xmlDoc;
	}

	/**
	 * �õ��ͻ������Ѽ�¼�б�
	 */
	public void querySellIntegralLog() {
		//Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		// act = 1 ������
		// act = 2 ��������
		// ����ʾ����Ϊ0�ļ�¼
		strSQL.append(" select * from ( ");
		strSQL.append("     select sil.*, '1' act from sell_integral_log sil where touserid = ? ");
		strSQL.append("     union ");
		strSQL.append("     select sil.*, '2' act from sell_integral_log sil where userid = ? ");
		strSQL.append(" ) sil ");
		strSQL.append(" where consumption_num <> 0 ");
		strSQL.append(" order by sil.consumption_time desc ");
		paramList.add(userid);
		paramList.add(userid);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result;
			result = pdao.executeQuerySql(0, -1);
			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���̳�-�õ��ͻ������Ѽ�¼�б�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ��ʦ��ȡ�μ�
	 */
	public void queryPaperInfo() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		//String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");
		//String deptid = xmlDocUtil.getSession().getChildTextTrim("deptid");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		strSQL.append(" select spi.* ");
		strSQL.append(" , (select buy_record_id from sell_buy_record sbr where sbr.paper_info_id = spi.paper_info_id and sbr.userid = ? limit 0,1) as buy_record_id ");
		strSQL.append(" , date_format((select buy_time from sell_buy_record sbr where sbr.paper_info_id = spi.paper_info_id and sbr.userid = ? limit 0,1), '%Y-%m-%d') as buy_time ");
		strSQL.append(" , (select count(t1.my_book_id) from learn_my_book t1 where t1.valid='Y' and t1.attachment_id=spi.attachment_id and t1.userid=?) as my_book_count");
		strSQL.append(" , getParamDesc('c_book_type', spi.book_type) as book_type_desc ");
		strSQL.append(" from sell_paper_info spi ");
		strSQL.append(" where spi.valid='Y' ");
		paramList.add(userid);
		paramList.add(userid);
		paramList.add(userid);

		String qry_book_type = reqElement.getChildText("qry_book_type");
		if (StringUtil.isNotEmpty(qry_book_type)) {
			strSQL.append(" and spi.book_type = ? ");
			paramList.add(qry_book_type);
		}

		String qry_subjectid = reqElement.getChildText("qry_subjectid");
		if (StringUtil.isNotEmpty(qry_subjectid)) {
			strSQL.append(" and spi.subjectid = ? ");
			paramList.add(qry_subjectid);
		}

		String qry_paper_info_name = reqElement.getChildText("qry_paper_info_name");
		if (StringUtil.isNotEmpty(qry_paper_info_name)) {
			strSQL.append(" and spi.paper_info_name like ? ");
			paramList.add("%" + qry_paper_info_name + "%");
		}

		String qry_paper_info_id = reqElement.getChildText("qry_paper_info_id");
		if (StringUtil.isNotEmpty(qry_paper_info_id)) {
			strSQL.append(" and spi.paper_info_id = ? ");
			paramList.add(qry_paper_info_id);
		}

		String qry_grade_code = reqElement.getChildText("qry_grade_code");
		if (StringUtil.isNotEmpty(qry_grade_code)) {
			strSQL.append(" and spi.grade_code = ? ");
			paramList.add(qry_grade_code);
		}

		String qry_book_volume = reqElement.getChildText("qry_book_volume");
		if (StringUtil.isNotEmpty(qry_book_volume)) {
			strSQL.append(" and spi.book_volume = ? ");
			paramList.add(qry_book_volume);
		}

		String qry_book_big_category = reqElement.getChildText("qry_book_big_category");
		if (StringUtil.isNotEmpty(qry_book_big_category)) {
			strSQL.append(" and spi.book_big_category = ? ");
			paramList.add(qry_book_big_category);
		}

		String qry_book_sma_category = reqElement.getChildText("qry_book_sma_category");
		if (StringUtil.isNotEmpty(qry_book_sma_category)) {
			strSQL.append(" and spi.book_sma_category = ? ");
			paramList.add(qry_book_sma_category);
		}

		// ���ݴ���ʱ������
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by spi.create_date desc ");
		}

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result;
			result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���̳�-ѧ����ȡ�Ƽ��μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ѧ����ȡ�Ƽ��μ�
	 */
	public void queryPaperRecommended() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		//String deptcode = xmlDocUtil.getSession().getChildTextTrim("deptcode");

		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();

		// ѧ��ͨ��sell_paper_recommended�õ���ʦ�Ƽ��Ŀμ�
		strSQL.append(" select spi.*, (select buy_record_id from sell_buy_record sbr where sbr.paper_info_id = spi.paper_info_id  and sbr.userid = ? limit 0,1) as buy_record_id ");
		strSQL.append(" , getParamDesc('c_book_type', spi.book_type) as book_type_desc ");
		strSQL.append(" from sell_paper_info spi ");
		strSQL.append(" where spi.valid='Y' and spi.paper_info_id in (select spr.paper_info_id from sell_paper_recommended spr "); // ��ֹ����Ƽ����������ظ�
		strSQL.append("     where 1 = 1 and spr.userid = ? "); // ѧ��
		strSQL.append("         or exists ( select classid from base_studentinfo bs where spr.classid = bs.classid and userid = ? ) "); // �༶
		strSQL.append("         or exists ( select group_id from learn_group_member lgm where spr.group_id = lgm.group_id and userid = ? ) "); // С��
		strSQL.append("     ) ");
		paramList.add(userid);
		paramList.add(userid);
		paramList.add(userid);
		paramList.add(userid);

		String qry_book_type = reqElement.getChildText("qry_book_type");
		if (StringUtil.isNotEmpty(qry_book_type)) {
			strSQL.append(" and spi.book_type = ? ");
			paramList.add(qry_book_type);
		}

		String qry_subjectid = reqElement.getChildText("qry_subjectid");
		if (StringUtil.isNotEmpty(qry_subjectid)) {
			strSQL.append(" and spi.subjectid = ? ");
			paramList.add(qry_subjectid);
		}

		String qry_paper_info_name = reqElement.getChildText("qry_paper_info_name");
		if (StringUtil.isNotEmpty(qry_paper_info_name)) {
			strSQL.append(" and spi.paper_info_name like ? ");
			paramList.add("%" + qry_paper_info_name + "%");
		}

		String qry_paper_info_id = reqElement.getChildText("qry_paper_info_id");
		if (StringUtil.isNotEmpty(qry_paper_info_id)) {
			strSQL.append(" and spi.paper_info_id = ? ");
			paramList.add(qry_paper_info_id);
		}

		// ���ݴ���ʱ������
		String orderBy = xmlDocUtil.getOrderBy();
		if (StringUtil.isNotEmpty(orderBy)) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by spi.create_date desc ");
		}

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			Element result;
			result = pdao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
			xmlDocUtil.getResponse().addContent(result);
			xmlDocUtil.setResult("0");

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���̳�-ѧ����ȡ�Ƽ��μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ѧ������μ�
	 */
	public void buyPaperInfo() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");

		String paper_info_id = reqElement.getChildText("paper_info_id");
		//String buy_record_id = reqElement.getChildText("buy_record_id");

		String[] returnData = { "my_book_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		String integral = "0"; // Ҫ�۳��Ļ���
		String subjectid = "";
		//String classid = "";

		String subject_book_id = "";
		String upload_userid = "";
		String integral_num = "0";
		String grade_code = "";

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			// �õ��μ���Ϣ �۸���Żݼ۸�
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" select spi.*,sbr.buy_record_id from sell_paper_info spi ");
			strSQL.append(" left join sell_buy_record sbr on sbr.paper_info_id=spi.paper_info_id ");
			strSQL.append(" and sbr.valid='Y' and sbr.userid=? ");
			strSQL.append(" where spi.paper_info_id = ? ");
			ArrayList<Object> qryParam = new ArrayList<Object>();
			qryParam.add(userid);
			qryParam.add(paper_info_id);

			pdao.setSql(strSQL.toString());
			pdao.setBindValues(qryParam);
			Element sellPaperInfoResult = pdao.executeQuerySql(0, -1);
			Element _rec = sellPaperInfoResult.getChild("Record");
			String pref_price = _rec.getChildTextTrim("pref_price");
			String price = _rec.getChildTextTrim("price");
			subjectid = _rec.getChildTextTrim("subjectid");
			grade_code = _rec.getChildTextTrim("grade_code");
			//classid = _rec.getChildTextTrim("classid");
			upload_userid = _rec.getChildTextTrim("upload_userid");
			String book_type = _rec.getChildTextTrim("book_type"); // �鱾����
			String paper_info_name = _rec.getChildTextTrim("paper_info_name");
			//String preview_path = _rec.getChildTextTrim("preview_path");
			String buy_record_id = _rec.getChildTextTrim("buy_record_id");
			String book_attachment_id = _rec.getChildTextTrim("book_attachment_id");
			String attachment_id = _rec.getChildTextTrim("attachment_id");	

			if (StringUtil.isEmpty(buy_record_id)){//û�й����
				// �۸���Żݼ۸� �����Żݼ۸�
				if (StringUtil.isNotEmpty(pref_price)) {
					integral = pref_price;
				} else {
					integral = price;
				}				
				//�õ������ߵĻ���
				StringBuffer userSQL = new StringBuffer();
				userSQL.append(" select * from sell_integral si ");
				userSQL.append(" where si.userid = ? ");
				ArrayList<Object> userParam = new ArrayList<Object>();
				userParam.add(userid);
				pdao.setSql(userSQL.toString());
				pdao.setBindValues(userParam);
				Element userResult = pdao.executeQuerySql(0, -1);
				List userList = userResult.getChildren("Record");
				if (userList.size() > 0) {
					integral_num = userResult.getChild("Record").getChildTextTrim("integral_num");
				}			
	
				// �Ƿ����㹻�Ļ���
				if (Integer.parseInt(integral_num) >= Integer.parseInt(integral)) {
	
					// ��סbook_attachment_id��attachment_id subject_book_id ����ÿ��д��learn_attachment��base_book_attachment
					book_attachment_id = _rec.getChildTextTrim("book_attachment_id");
					attachment_id = _rec.getChildTextTrim("attachment_id");
					subject_book_id = _rec.getChildTextTrim("subject_book_id");
	
					//����sell_buy_record
					Element sellBuyRecordRec = ConfigDocument.createRecordElement("yuexue", "sell_buy_record");
					XmlDocPkgUtil.copyValues(reqElement, sellBuyRecordRec, 0, true);
					XmlDocPkgUtil.setChildText(sellBuyRecordRec, "status", "20");
					XmlDocPkgUtil.setChildText(sellBuyRecordRec, "buy_time", DatetimeUtil.getNow(""));
					//XmlDocPkgUtil.setChildText(sellBuyRecordRec, "modify_by", userName);
					//XmlDocPkgUtil.setChildText(sellBuyRecordRec, "modify_date", DatetimeUtil.getNow(""));
					pdao.insertOneRecordSeqPk(sellBuyRecordRec);
	
					// ��������ߵ����ϴ��� ����¼���ֵ�����
					if (!userid.equals(upload_userid)) {
						//���¹����ߵ�sell_integral
						StringBuffer updateSQL = new StringBuffer();
						ArrayList<Object> paramList = new ArrayList<Object>();
						updateSQL.append(" update sell_integral set integral_num = integral_num - ?, modify_by = ?, modify_date = now() ");
						updateSQL.append(" where userid = ? ");
						paramList.add(integral);
						paramList.add(userName);
						paramList.add(userid);
						pdao.setSql(updateSQL.toString());
						pdao.setBindValues(paramList);
						pdao.executeTransactionSql();
	
						//�����ϴ��ߵ�sell_integral
						StringBuffer uploadSQL = new StringBuffer();
						uploadSQL.append(" select * from sell_integral si ");
						uploadSQL.append(" where si.userid = ? ");
						ArrayList<Object> uploadParam = new ArrayList<Object>();
						uploadParam.add(upload_userid);
	
						pdao.setSql(uploadSQL.toString());
						pdao.setBindValues(uploadParam);
						Element uploadResult = pdao.executeQuerySql(0, -1);
						List uploadList = uploadResult.getChildren("Record");
						// ������������ û��������
						if (uploadList.size() > 0) {
							StringBuffer uploadSISQL = new StringBuffer();
							ArrayList<Object> uploadSIList = new ArrayList<Object>();
							uploadSISQL.append(" update sell_integral set integral_num = integral_num + ?, modify_by = ?, modify_date = now() ");
							uploadSISQL.append(" where userid = ? ");
							uploadSIList.add(integral);
							uploadSIList.add(userName);
							uploadSIList.add(upload_userid);
							pdao.setSql(uploadSISQL.toString());
							pdao.setBindValues(uploadSIList);
							pdao.executeTransactionSql();
						} else {
							Element uploadRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
							XmlDocPkgUtil.copyValues(reqElement, uploadRec, 0, true);
							XmlDocPkgUtil.setChildText(uploadRec, "userid", upload_userid);
							XmlDocPkgUtil.setChildText(uploadRec, "integral_num", integral);
							XmlDocPkgUtil.setChildText(uploadRec, "valid", "Y");
							pdao.insertOneRecordSeqPk(uploadRec).toString();
						}
	
						// ���ӻ���������־sell_integral_log
						Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral_log");
						XmlDocPkgUtil.copyValues(reqElement, sellIntegralLogRec, 0, true);
						XmlDocPkgUtil.setChildText(sellIntegralLogRec, "paper_info_id", paper_info_id);
						XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", userid);
						XmlDocPkgUtil.setChildText(sellIntegralLogRec, "touserid", upload_userid);
						XmlDocPkgUtil.setChildText(sellIntegralLogRec, "consumption_num", integral);
						pdao.insertOneRecordSeqPk(sellIntegralLogRec);
					}
	
					if (StringUtil.isEmpty(subject_book_id)) {
						if ("30".equals(book_type)) {
							subject_book_id = "0"; // �������
						} else {
							strSQL = new StringBuffer();
							strSQL.append(" select * ");
							strSQL.append(" from base_subject_book t ");
							strSQL.append(" where valid = 'Y' ");
							strSQL.append(" and t.grade_code = ? ");
							strSQL.append(" and t.subjectid = ? ");
	
							qryParam = new ArrayList<Object>();
							qryParam.add(grade_code);
							qryParam.add(subjectid);
	
							pdao.setSql(strSQL.toString());
							pdao.setBindValues(qryParam);
							Element result = pdao.executeQuerySql(0, -1);
	
							subject_book_id = result.getChild("Record").getChildTextTrim("subject_book_id");
						}
					}
	
					// ˳��Ϊ learn_attachment base_book_attachment learn_my_book
					if (StringUtil.isEmpty(attachment_id)) {
						// learn_attachment
						Element learnAttachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment");
						XmlDocPkgUtil.copyValues(sellPaperInfoResult.getChild("Record"), learnAttachmentRec, 0, true);
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "file_name", paper_info_name);
						// ����дaccess_path ����Ļ�pdf��ҳ���ܲ鿴
						// XmlDocPkgUtil.setChildText(learnAttachmentRec, "access_path", preview_path);
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "trans_status", "10");
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "trans_error", "");
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "userid", userid);
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "create_date", DatetimeUtil.getNow(""));
						XmlDocPkgUtil.setChildText(learnAttachmentRec, "valid", "Y");
						attachment_id = pdao.insertOneRecordSeqPk(learnAttachmentRec).toString();
	
						// base_book_attachment
						Element baseBookAttachmentRec = ConfigDocument.createRecordElement("yuexue", "base_book_attachment");
						XmlDocPkgUtil.setChildText(baseBookAttachmentRec, "userid", userid);
						XmlDocPkgUtil.setChildText(baseBookAttachmentRec, "attachment_id", attachment_id);
						XmlDocPkgUtil.setChildText(baseBookAttachmentRec, "subject_book_id", subject_book_id);
						XmlDocPkgUtil.setChildText(baseBookAttachmentRec, "book_type", book_type);
						XmlDocPkgUtil.setChildText(baseBookAttachmentRec, "valid", "Y");
						book_attachment_id = pdao.insertOneRecordSeqPk(baseBookAttachmentRec).toString();
					}	
					// ���book_attachment_id attachment_id subject_book_idд�뵽sell_paper_info
					// ���ش���+1
					StringBuffer updatePaperInfoSQL = new StringBuffer();
					ArrayList<Object> updatePaperInfoParamList = new ArrayList<Object>();
					updatePaperInfoSQL.append(" update sell_paper_info ");
					updatePaperInfoSQL.append(" set book_attachment_id = ?, attachment_id = ?, subject_book_id = ?, down_count = down_count + 1, modify_by = ?, modify_date = now() ");
					updatePaperInfoSQL.append(" where paper_info_id = ? ");
					updatePaperInfoParamList.add(book_attachment_id);
					updatePaperInfoParamList.add(attachment_id);
					updatePaperInfoParamList.add(subject_book_id);
					updatePaperInfoParamList.add(userid);
					updatePaperInfoParamList.add(paper_info_id);
					pdao.setSql(updatePaperInfoSQL.toString());
					pdao.setBindValues(updatePaperInfoParamList);
					pdao.executeTransactionSql();
			    }
				else{
					xmlDocUtil.setResult("600901");
					xmlDocUtil.writeErrorMsg("600901", "��û���㹻�Ļ���,�����еĻ���Ϊ" + integral_num + ",�뼰ʱ��ֵ��");
					return;					
				}
			}
			// learn_my_book
			Element learnMyBookRec = ConfigDocument.createRecordElement("yuexue", "learn_my_book");
			XmlDocPkgUtil.setChildText(learnMyBookRec, "userid", userid);
			XmlDocPkgUtil.setChildText(learnMyBookRec, "attachment_id", attachment_id);
			XmlDocPkgUtil.setChildText(learnMyBookRec, "book_attachment_id", book_attachment_id);
			XmlDocPkgUtil.setChildText(learnMyBookRec, "valid", "Y");
			String my_book_id = pdao.insertOneRecordSeqPk(learnMyBookRec).toString();

			pdao.commitTransaction();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { my_book_id }));

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
		    xmlDocUtil.writeHintMsg("600201", "���سɹ�!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[���̳�-ѧ������μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ��ʦ�Ƽ��μ�
	 */
	public void addPaperRecommended() {
		Element reqElement = xmlDocUtil.getRequestData();
		//String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		//String userName = xmlDocUtil.getSession().getChildTextTrim("username");

		// ȷ���Ƕ��ѧ�� ����༶ ���С��
		String userids = reqElement.getChildText("userids");
		String classids = reqElement.getChildText("classids");
		String group_ids = reqElement.getChildText("group_ids");

		String[] returnData = { "paper_info_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			// ���� ѧ��\�༶\С��
			// ����sell_paper_recommended

			// ѧ��
			if (StringUtil.isNotEmpty(userids)) {
				String[] userids_arr = userids.split(",");
				for (int i = 0; i < userids_arr.length; i++) {
					Element sellPaperRecommendedRec = ConfigDocument.createRecordElement("yuexue", "sell_paper_recommended");
					XmlDocPkgUtil.copyValues(reqElement, sellPaperRecommendedRec, 0, true);
					XmlDocPkgUtil.setChildText(sellPaperRecommendedRec, "userid", userids_arr[i]);
					pdao.insertOneRecordSeqPk(sellPaperRecommendedRec).toString();
				}
			}

			// �༶
			if (StringUtil.isNotEmpty(classids)) {
				String[] classids_arr = classids.split(",");
				for (int i = 0; i < classids_arr.length; i++) {
					Element sellPaperRecommendedRec = ConfigDocument.createRecordElement("yuexue", "sell_paper_recommended");
					XmlDocPkgUtil.copyValues(reqElement, sellPaperRecommendedRec, 0, true);
					XmlDocPkgUtil.setChildText(sellPaperRecommendedRec, "classid", classids_arr[i]);
					pdao.insertOneRecordSeqPk(sellPaperRecommendedRec).toString();
				}
			}

			//Ⱥ��
			if (StringUtil.isNotEmpty(group_ids)) {
				String[] group_ids_arr = group_ids.split(",");
				for (int i = 0; i < group_ids_arr.length; i++) {
					Element sellPaperRecommendedRec = ConfigDocument.createRecordElement("yuexue", "sell_paper_recommended");
					XmlDocPkgUtil.copyValues(reqElement, sellPaperRecommendedRec, 0, true);
					XmlDocPkgUtil.setChildText(sellPaperRecommendedRec, "group_id", group_ids_arr[i]);
					pdao.insertOneRecordSeqPk(sellPaperRecommendedRec).toString();
				}
			}

			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600202", "��ʦ�Ƽ��μ�!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[���̳�-��ʦ�Ƽ��μ�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}

	
	
	/**
	 * �������׵�
	 */
	public void addPayLog() {
		Element reqElement = xmlDocUtil.getRequestData();
		String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String integral_num = reqElement.getChildText("integral_num");
		String order_id = reqElement.getChildText("order_id");
		PlatformDao pdao = new PlatformDao();
		try {
			//�ж϶����Ƿ���ڣ����ظ�
			pdao.setSql("select * from pay_integral where order_id = ?");
			ArrayList<Object> li =new ArrayList<Object>();
			li.add(order_id);
			pdao.setBindValues(li);
	    	Element result = pdao.executeQuerySql(1, 1);
	    	List ls = result.getChildren("Record");
			if (ls.size() > 0) {
				xmlDocUtil.writeErrorMsg("10642", "�ñʽ��״��ڣ������ظ�������");
				return;
			}
			pdao.beginTransaction();
			Element sellPaperRecommendedRec = ConfigDocument.createRecordElement("yuexue", "pay_integral");
			XmlDocPkgUtil.copyValues(reqElement, sellPaperRecommendedRec, 0, true);
			pdao.insertOneRecordSeqPk(sellPaperRecommendedRec).toString();
			pdao.commitTransaction();
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600202", "�������׵�!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[�û���ֵ-�������׵�]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	
	/**
	 * �޸Ľ��׵�(ͬ��)
	 */
	public void uptPayLog() {
		Element reqElement = xmlDocUtil.getRequestData();
		String order_id = reqElement.getChildText("order_id");
		String state = StringUtil.isEmail(reqElement.getChildText("state")) ? "0" : reqElement.getChildText("state");
		PlatformDao pdao = new PlatformDao();
		try {
			//�ж϶����Ƿ����
			pdao.setSql("select * from pay_integral where order_id = ? and state = 0 ");
			ArrayList<Object> li =new ArrayList<Object>();
			li.add(order_id);
			pdao.setBindValues(li);
	    	Element result = pdao.executeQuerySql(1, 1);
	    	List ls = result.getChildren("Record");
			if (ls.size() > 0) {
				String integral_num = ((Element)ls.get(0)).getChildText("integral_num");
				String userid = ((Element)ls.get(0)).getChildText("userid");
				pdao.beginTransaction();
				//�û����䶯
				String SQL = "UPDATE pcmc_user set money_num = (IFNULL(money_num,0) + ?) WHERE userid = ? ";
		        pdao.setSql(SQL);
		        li = new ArrayList<Object>();
		        li.add(integral_num);
		        li.add(userid);
		        pdao.setBindValues(li);
		        pdao.executeTransactionSql();
		        SQL = "update pay_integral set state = ? where order_id = ?";
		        pdao.setSql(SQL);
		        li = new ArrayList<Object>();
		        li.add(state);
		        li.add(order_id);
		        pdao.setBindValues(li);
		        pdao.executeTransactionSql();
				/*Element sellPaperRecommendedRec = ConfigDocument.createRecordElement("yuexue", "pay_integral");
				XmlDocPkgUtil.copyValues(reqElement, sellPaperRecommendedRec, 0, true);
				pdao.insertOneRecordSeqPk(sellPaperRecommendedRec).toString();*/
				pdao.commitTransaction();
				xmlDocUtil.setResult("0");
				xmlDocUtil.writeHintMsg("600202", "��ֵ�ɹ�!");
			} else {
				xmlDocUtil.setResult("0");
				xmlDocUtil.writeHintMsg("600202", "��ֵ�ɹ�!");
			}
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[�̳�-�û���ֵ]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * �޸Ľ��׵�(�첽)
	 */
	public static boolean uptPayLog(String order_id,String state) {
		PlatformDao pdao = new PlatformDao();
		try {
			//�ж϶����Ƿ����
			pdao.setSql("select * from pay_integral where order_id = ? and state = 0 ");
			ArrayList<Object> li =new ArrayList<Object>();
			li.add(order_id);
			pdao.setBindValues(li);
	    	Element result = pdao.executeQuerySql(1, 1);
	    	List ls = result.getChildren("Record");
			if (ls.size() > 0) {
				String integral_num = ((Element)ls.get(0)).getChildText("integral_num");
				String userid = ((Element)ls.get(0)).getChildText("userid");
				pdao.beginTransaction();
				//�û����䶯
				String SQL = "UPDATE pcmc_user set money_num = (IFNULL(money_num,0) + ?) WHERE userid = ? ";
		        pdao.setSql(SQL);
		        li = new ArrayList<Object>();
		        li.add(integral_num);
		        li.add(userid);
		        pdao.setBindValues(li);
		        pdao.executeTransactionSql();
		        SQL = "update pay_integral set state = ? where order_id = ?";
		        pdao.setSql(SQL);
		        li = new ArrayList<Object>();
		        li.add(state);
		        li.add(order_id);
		        pdao.setBindValues(li);
		        pdao.executeTransactionSql();
				pdao.commitTransaction();
				return true;
			}
			return false;
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			return false;
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * �޸Ľ��׵�(�첽)
	 */
	public static void uptPayLog(String order_id,String state,HttpServletResponse response,String href) {
			//΢��֧���ɹ���ʵ��ҳ����ת
			if (uptPayLog(order_id,state)) {
				try {
					response.sendRedirect(href);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	
	/**
	 * �ϴ����̳ǿμ�
	 * sell_paper_info
	 */
	public void uploadPaperInfoList() {
		Element reqElement = xmlDocUtil.getRequestData();
		//String userid = xmlDocUtil.getSession().getChildTextTrim("userid");
		String userName = xmlDocUtil.getSession().getChildTextTrim("username");

		// ����1��
		List paperList = reqElement.getChildren("paper_attachment");
		// ���� 0������1��
		List coverList = reqElement.getChildren("cover_attachment");

		String[] returnData = { "paper_info_id" };
		Element resData = XmlDocPkgUtil.createMetaData(returnData);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();

			Element sellPaperInfoRec = ConfigDocument.createRecordElement("yuexue", "sell_paper_info");
			XmlDocPkgUtil.copyValues(reqElement, sellPaperInfoRec, 0, true);

			//����·��
			String cover_path = "";
			if (coverList.size() > 0) {
				Element coverPathRec = (Element) coverList.get(0);
				cover_path = getUploadFilePath(coverPathRec, "sell");
			}

			//����·��
			String attachment_path = "";
			if (paperList.size() > 0) {
				Element paperPathRec = (Element) paperList.get(0);
				attachment_path = getUploadFilePath(paperPathRec, "sell");

				// ������Ϣ
				XmlDocPkgUtil.setChildText(sellPaperInfoRec, "file_path", attachment_path);
				XmlDocPkgUtil.setChildText(sellPaperInfoRec, "file_name", paperPathRec.getAttributeValue("name"));
				XmlDocPkgUtil.setChildText(sellPaperInfoRec, "file_size", paperPathRec.getAttributeValue("size"));
				XmlDocPkgUtil.setChildText(sellPaperInfoRec, "file_type", paperPathRec.getAttributeValue("ext").toLowerCase());
			}

			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "cover_path", cover_path);
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "author", userName);

			// �������ݵ�sell_paper_info
			String paper_info_id = pdao.insertOneRecordSeqPk(sellPaperInfoRec).toString();

			resData.addContent(XmlDocPkgUtil.createRecord(returnData, new String[] { paper_info_id }));

			pdao.commitTransaction();

			xmlDocUtil.getResponse().addContent(resData);
			xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("600203", "�ϴ����̳ǿμ�!");
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-�ϴ����̳ǿμ�]" + e.getMessage());
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

	/**
	 * �����û������¼
	 */
	public void addShareByUser(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paper_id = reqElement.getChildText("paper_id");
		String userid = reqElement.getChildText("share_userid");
		StringBuffer strSQL=new StringBuffer();
		//��ѯlearn_attachment_share�Ƿ��Ѵ��ڸü�¼
		strSQL.append("select * from learn_attachment_share t1 where t1.userid=? and t1.paper_id=? ");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(userid);
		paramList.add(paper_id);
		
		//��ѯ���ֱ��Ƿ������ӻ���
		StringBuffer integralSQL=new StringBuffer();
		integralSQL.append("SELECT ifnull(sum(t1.integral_num),0) AS integral_num, t1.userid FROM sell_integral t1 WHERE t1.userid = ? AND t1.integral_type = '10'");
		ArrayList<Object> integralParam = new ArrayList<Object>();
		integralParam.add(userid);
		
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			
			pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	pdao.setSql(integralSQL.toString());
	    	pdao.setBindValues(integralParam);
	    	Element integralResult = pdao.executeQuerySql(0, -1);
	    	
			Element shareRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment_share");
			XmlDocPkgUtil.copyValues(reqElement, shareRec, 0, true);
			XmlDocPkgUtil.setChildText(shareRec, "paper_id", paper_id);
			XmlDocPkgUtil.setChildText(shareRec, "userid", userid);
			XmlDocPkgUtil.setChildText(shareRec, "create_by", "�ο�");
			
			// �������ݵ�sell_paper_info
			String share_id = pdao.insertOneRecordSeqPk(shareRec).toString();
			
			List queryList = result.getChildren("Record");
			List integralList=integralResult.getChildren("Record");
			Integer integral_num= Integer.parseInt(((Element)integralList.get(0)).getChildText("integral_num")); 
			
		
			if(queryList.size() <=0){
				//���ӻ�����Ϣsell_integral
				 Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
				 XmlDocPkgUtil.copyValues(result, sellIntegralLogRec, 0, true);
				 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", userid);
				 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "source_id", share_id);
				 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "create_by", "����Ա");
				 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "valid", "Y");
				if(integral_num>=20){
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "0");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "ת�������Ѵ�����");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "10");
				}else{
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "5");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "ת��");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "10");
				}
				
				pdao.insertOneRecordSeqPk(sellIntegralLogRec);
			}
			
			pdao.commitTransaction();
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��Ƶ�μ�-���ӷ����¼]" + e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		}finally{
			pdao.releaseConnection();
		}
		
	}
}
