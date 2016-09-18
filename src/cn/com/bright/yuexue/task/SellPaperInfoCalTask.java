package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;

/**
 * <p>Title: 计算云商城的阅读人次</p>
 * <p>Description: 计算云商城的阅读人次 </p>
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
 * <p> jiangyh    2015/03/24      1.0          build this moudle </p>
 *     
 */
public class SellPaperInfoCalTask extends TimerTask {
	private Log log4j = new Log(this.getClass().toString());

	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" SellPaperInfoCalTask run ----------");
		String attachment_id = "";
		try {
			List logList = getCalSellPaperInfo();
			for (int i = 0; i < logList.size(); i++) {
				attachment_id = ((Element) logList.get(i)).getChildText("attachment_id");
				updateSellPaperInfo(attachment_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[云商城-计算阅读人次]" + e.getMessage());
		}
	}

	//  
	public void updateSellPaperInfo(String attachment_id) {
		PlatformDao pdao = new PlatformDao(true);
		String watch_count = "";
		try {
			//  
			StringBuffer querySQL = new StringBuffer();
			querySQL.append(" select attachment_id, count(attachment_id) watch_count ");
			querySQL.append(" from learn_book_read_his ");
			querySQL.append(" where attachment_id = ? group by attachment_id ");

			//  
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append(" update sell_paper_info set watch_count = ? ");
			updateSQL.append(" where attachment_id = ? ");

			ArrayList<Object> queryParamList = new ArrayList<Object>();
			queryParamList.add(attachment_id);
			pdao.setSql(querySQL.toString());
			pdao.setBindValues(queryParamList);
			Element result = pdao.executeQuerySql(0, -1);
			List queryList = result.getChildren("Record");

			if (queryList.size() > 0) {
				Element logRec = (Element) queryList.get(0);
				watch_count = logRec.getChildText("watch_count");

				ArrayList<Object> updateParamList = new ArrayList<Object>();
				updateParamList.add(watch_count);
				updateParamList.add(attachment_id);

				pdao.setSql(updateSQL.toString());
				pdao.setBindValues(updateParamList);
				pdao.executeTransactionSql();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[云商城-计算阅读人次并写入]" + e.getMessage());
		}
	}

	/**
	 * 得到要计算的云商城的资源信息
	 */
	public List getCalSellPaperInfo() {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select distinct attachment_id ");
		strSQL.append(" from sell_paper_info ");
		strSQL.append(" where attachment_id is not null ");

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(20, 1);
			return result.getChildren("Record");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[云商城-得到要计算的云商城的资源信息]" + e.getMessage());
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}
}
