package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title: 将learn_attachment我的课本的附件信息导入到sell_paper_info</p>
 * <p>Description: 将learn_attachment我的课本的附件信息导入到sell_paper_info</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * <p></p>
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author     time            version      desc</p>
 * <p> jiangyh    2015/04/08      1.0          build this moudle </p>
 *     
 */
public class SellPaperInfoImpTask extends TimerTask {
	private Log log4j = new Log(this.getClass().toString());

	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" SellPaperInfoImpTask run ----------");
		try {
			List logList = getLearnAttachmentImpList();
			for (int i = 0; i < logList.size(); i++) {
				insertSellPaperInfo((Element) logList.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[附件信息导入-导入附件]" + e.getMessage());
		}
	}

	public void insertSellPaperInfo(Element attachment_info) {
		PlatformDao pdao = new PlatformDao(true);
		try {
			Element sellPaperInfoRec = ConfigDocument.createRecordElement("yuexue", "sell_paper_info");
			XmlDocPkgUtil.copyValues(attachment_info, sellPaperInfoRec, 0, true);

			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "file_path", attachment_info.getChildText("access_path"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "book_volume", attachment_info.getChildText("file_size"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "preview_path", attachment_info.getChildText("access_path"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "paper_info_name", attachment_info.getChildText("file_name"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "intro", attachment_info.getChildText("file_name"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "price", "0");
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "pref_price", "0");
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "resource_type", "");
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "upload_userid", "1");
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "upload_time", attachment_info.getChildText("create_time"));
			XmlDocPkgUtil.setChildText(sellPaperInfoRec, "author", "admin");

			// 插入数据到sell_paper_info
			String paper_info_id = pdao.insertOneRecordSeqPk(sellPaperInfoRec).toString();

			ArrayList<Object> paramList = new ArrayList<Object>();
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append(" update sell_paper_info set upload_time = create_date where paper_info_id = ? ");
			paramList.add(paper_info_id);
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(paramList);
			pdao.executeTransactionSql();

		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[附件信息导入-插入附件信息]" + e.getMessage());
		}
	}

	// '/upload/book/%' and la.file_size < 20
	// 注意条件 
	public List getLearnAttachmentImpList() {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select bba.book_attachment_id, bba.subject_book_id, bsb.subjectid, bsb.grade_code, bba.book_type, la.* ");
		strSQL.append(" from base_book_attachment bba, learn_attachment la, base_subject_book bsb ");
		strSQL.append(" where 1 = 1 ");
		strSQL.append(" and bba.attachment_id = la.attachment_id ");
		strSQL.append(" and bba.subject_book_id = bsb.subject_book_id ");
		strSQL.append(" and la.file_path like '/upload/book/%' and la.file_size < 20 ");
		strSQL.append(" and not exists (select attachment_id from sell_paper_info spi where la.attachment_id = spi.attachment_id ) ");

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(150, 1);
			return result.getChildren("Record");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[附件信息导入-得到附件信息列表]" + e.getMessage());
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}
}
