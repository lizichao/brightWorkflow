package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;

/**
 * <p>Title:计算视频的播放记录</p>
 * <p>Description: 计算视频的播放记录</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * <p>计算视频的播放记录并更新到附件表</p>
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author     time            version      desc</p>
 * <p> jiangyh    2015/03/10      1.0          build this moudle </p>
 *     
 */
public class VideoLogCalTask extends TimerTask {
	private Log log4j = new Log(this.getClass().toString());

	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" VideoLogCalTask run ----------");
		String attachment_id = "";
		try {
			List logList = getStatVideoLog();
			for (int i = 0; i < logList.size(); i++) {
				attachment_id = ((Element) logList.get(i)).getChildText("attachment_id");
				updateAttachmentLogStat(attachment_id);
				setStatLogStatus(attachment_id, "Y");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-计算视频的播放记录]" + e.getMessage());
		}
	}

	// 得到点击量、重点量、观看百分比 更新到learn_attachment的watch_count watch_scale watch_again_count
	public void updateAttachmentLogStat(String attachment_id) {
		PlatformDao pdao = new PlatformDao(true);
		String watch_count = "";
		String watch_again_count = "";
		String watch_scale = "";
		try {
			StringBuffer querySQL = new StringBuffer();
			// video_time
			querySQL.append(" select t1.attachment_id, sum(t1.watch_count) watch_count, sum(t1.watch_again_count) watch_again_count, sum(t1.watch_scale) / sum(t1.video_time) watch_scale ");
			querySQL.append(" from ( ");
			querySQL.append("    select t1.userid, t1.attachment_id, (count(t1.attachment_id)) watch_count, (count(t1.attachment_id) - 1) watch_again_count ");
			// 如果是在页面上的停留时间超过视频时长 时间等于视频时长 
			querySQL.append("    , sum( ( case ");
			querySQL.append("            when timestampdiff(SECOND, t1.begin_time, t1.end_time) is null then 0 ");
			querySQL.append("            when timestampdiff(SECOND, t1.begin_time, t1.end_time) < t2.video_time then timestampdiff(SECOND, t1.begin_time, t1.end_time) ");
			querySQL.append("            else t2.video_time end ) ) watch_scale, sum(t2.video_time) video_time ");
			querySQL.append("    from learn_attachment_log t1, learn_attachment t2 ");
			querySQL.append("    where 1 = 1 ");
			querySQL.append("    and t1.end_time is not null ");
			querySQL.append("    and t1.attachment_id = t2.attachment_id ");
			querySQL.append("    and t1.attachment_id = ? ");
			querySQL.append("    group by t1.userid, t1.attachment_id ");
			querySQL.append(" ) t1 ");
			querySQL.append(" group by t1.attachment_id ");

			// 更新语句
			StringBuffer updateSQL = new StringBuffer();
			updateSQL.append(" update learn_attachment ");
			updateSQL.append(" set watch_count = ?, watch_again_count = ?, watch_scale = ? ");
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
				watch_again_count = logRec.getChildText("watch_again_count");
				watch_scale = logRec.getChildText("watch_scale");

				ArrayList<Object> updateParamList = new ArrayList<Object>();
				updateParamList.add(watch_count);
				updateParamList.add(watch_again_count);
				// watch_scale double(2.2) 长度2位 2位小数
				updateParamList.add(watch_scale);
				updateParamList.add(attachment_id);

				pdao.setSql(updateSQL.toString());
				pdao.setBindValues(updateParamList);
				pdao.executeTransactionSql();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-计算视频的播放记录]" + e.getMessage());
		}
	}

	/**
	 * 取得要统计的视频播放列表
	 * 结束时间不能为空
	 * @return
	 */
	public List getStatVideoLog() {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select distinct attachment_id ");
		strSQL.append(" from learn_attachment_log ");
		strSQL.append(" where stat_status = 'N' ");
		strSQL.append(" and end_time is not null ");

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(20, 1);
			return result.getChildren("Record");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-取得要统计的视频播放列表]" + e.getMessage());
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * 更新播放日志的状态 标识日志已经做了统计
	 * 更新数据的结束时间不能为空 因为用户可能是还在看
	 */
	public void setStatLogStatus(String attachment_id, String status) {
		StringBuffer updateSQL = new StringBuffer();
		updateSQL.append(" update learn_attachment_log ");
		updateSQL.append(" set stat_status = ?, stat_time = now()");
		updateSQL.append(" where attachment_id = ? and stat_status <> ? and end_time is not null ");

		ArrayList<Object> updateParamList = new ArrayList<Object>();
		updateParamList.add(status);
		updateParamList.add(attachment_id);
		updateParamList.add(status);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(updateParamList);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[视频播放-更新播放日志的状态]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
}
