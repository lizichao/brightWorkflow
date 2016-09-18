package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;

/**
 * <p>Title:计算视频的分数</p>
 * <p>Description: 计算视频的分数</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * <p>计算视频的平均分数并写入到附件表</p>
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author     time            version      desc</p>
 * <p> jiangyh    2015/03/10      1.0          build this moudle </p>
 *     
 */
public class VideoScoreCalTask extends TimerTask {
	private Log log4j = new Log(this.getClass().toString());

	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" VideoScoreCalTask run ----------");
		String attachment_id = "";
		try {
			List logList = getStatVideoScore();
			for (int i = 0; i < logList.size(); i++) {
				attachment_id = ((Element) logList.get(i)).getChildText("attachment_id");
				updateAttachmentScoreStat(attachment_id);
				setStatScoreStatus(attachment_id, "Y");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-计算视频的平均分数]" + e.getMessage());
		}
	}

	// 得到分数并更新到learn_attachment的avg_score
	public void updateAttachmentScoreStat(String attachment_id) {
		PlatformDao pdao = new PlatformDao(true);
		String avg_score = "";
		try {
			// 查询观后评分
			StringBuffer querySQL = new StringBuffer();
			querySQL.append(" select attachment_id, sum(score) / count(attachment_id) avg_score ");
			querySQL.append(" from learn_attachment_score ");
			querySQL.append(" where attachment_id = ? group by attachment_id ");

			// 更新观后评分
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append(" update learn_attachment set avg_score = ? ");
			updateSQL.append(" where attachment_id = ? ");

			ArrayList<Object> queryParamList = new ArrayList<Object>();
			queryParamList.add(attachment_id);
			pdao.setSql(querySQL.toString());
			pdao.setBindValues(queryParamList);
			Element result = pdao.executeQuerySql(0, -1);
			List queryList = result.getChildren("Record");

			if (queryList.size() > 0) {
				Element logRec = (Element) queryList.get(0);
				avg_score = logRec.getChildText("avg_score");

				ArrayList<Object> updateParamList = new ArrayList<Object>();
				updateParamList.add(avg_score);
				updateParamList.add(attachment_id);

				pdao.setSql(updateSQL.toString());
				pdao.setBindValues(updateParamList);
				pdao.executeTransactionSql();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-计算视频的分数]" + e.getMessage());
		}
	}

	/**
	 * 取得要统计的视频分数列表
	 */
	public List getStatVideoScore() {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select distinct attachment_id ");
		strSQL.append(" from learn_attachment_score ");
		strSQL.append(" where stat_status = 'N' ");

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(20, 1);
			return result.getChildren("Record");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-取得要统计的视频分数列表]" + e.getMessage());
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}

	/** 
	 * 更新分数日志的状态 标识日志已经做了统计
	 */
	public void setStatScoreStatus(String attachment_id, String status) {
		StringBuffer updateSQL = new StringBuffer();
		updateSQL.append(" update learn_attachment_score ");
		updateSQL.append(" set stat_status = ?, stat_time = now()");
		updateSQL.append(" where attachment_id = ? and stat_status <> ? ");

		ArrayList<Object> updateParam = new ArrayList<Object>();
		updateParam.add(status);
		updateParam.add(attachment_id);
		updateParam.add(status);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(updateParam);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-设置分数日志的状态]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
}
