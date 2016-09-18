package cn.com.bright.yuexue.task;

import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:沃课堂积分规则</p>
 * <p>Description: 沃课堂积分规则</p>
 * @author LZY
 *
 */
public class SellIntegralTask extends TimerTask{

	private Log log4j = new Log(this.getClass().toString());
	
	public void run() {
		getLoginUserList();
		getReadvideoLogList();
		/*getUploadVideoList();
		getPaperList();
		getSubmitExamResultList();*/
	}
	
	/**
	 * 查询一天内登录的用户(同一用户多次登录只取一条)
	 */
	public void getLoginUserList(){
		StringBuffer strSQL = new StringBuffer();
		/*strSQL.append("SELECT t1.userid,t1.logid,t1.begintime, count(*) AS login_num FROM pcmc_log t1, pcmc_user t2");
		strSQL.append(" WHERE t1.userid = t2.userid AND t1.issuccess='1' AND (t2.usertype = '1' OR t2.usertype = '2') AND t1.actions = 'login'  AND t1.oprid = 'sm_permission'");
		strSQL.append(" AND t1.sysname='pcmc' AND t1.begintime BETWEEN CURDATE() - INTERVAL 1 DAY AND CURDATE() GROUP BY t1.userid");*/
		strSQL.append("SELECT t1.userid,t1.logid,t2.username,t1.begintime, count(*) AS login_num FROM pcmc_log t1, pcmc_user t2");
		strSQL.append(" WHERE  t1.userid = t2.userid AND t1.issuccess = '1' AND (t2.usertype = '1' OR t2.usertype = '2') AND t1.actions = 'login'");
		strSQL.append(" AND t1.oprid = 'sm_permission' AND t1.sysname = 'pcmc'  AND TO_DAYS(t1.begintime) = TO_DAYS(now()) AND NOT EXISTS ( ");
		strSQL.append("  SELECT * FROM sell_integral si WHERE     si.userid = t1.userid and si.integral_type='2'  AND TO_DAYS(si.create_date) = TO_DAYS(now()))");
		strSQL.append(" GROUP BY t1.userid");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0,-1);
			insertIntegral(result,"2","1","登录");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-获取登录用户]"+e.getMessage());
		}
		
	}
	
	/**
	 * 查询视频观看记录
	 */
	public void getReadvideoLogList(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT t1.paper_info_id,t1.create_by as read_username,t1.read_log_id,t1.userid as read_userid,t3.userid AS create_userid, t1.begin_time,t1.end_time,");
		strSQL.append(" (UNIX_TIMESTAMP(t1.end_time) - UNIX_TIMESTAMP(t1.begin_time)) AS read_time,t3.video_time * 0.2 AS video_time");
		strSQL.append(" FROM sell_read_log t1, learn_paper_attachment t2, learn_attachment t3");
		strSQL.append(" WHERE t1.paper_info_id = t2.paper_id AND t2.attachment_id = t3.attachment_id AND t1.end_time IS NOT NULL");
		strSQL.append(" AND NOT EXISTS (SELECT * FROM sell_integral si WHERE si.source_id = t1.read_log_id AND si.integral_type = '5' AND TO_DAYS(si.create_date) = TO_DAYS(now()))");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0,-1);
           
            List userList = result.getChildren("Record");
			for (int i=0;i<userList.size();i++){
				 Element userRec = (Element)userList.get(i);
				 String userid=userRec.getChildText("create_userid");
				 String source_id=userRec.getChildText("read_log_id");
				 String create_by=userRec.getChildText("read_username");
				 Double read_time=Double.parseDouble(userRec.getChildText("read_time"));
				 Double video_time=Double.parseDouble(userRec.getChildText("video_time"));
				 if(read_time>=video_time){
				    //增加积分信息sell_integral
					Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
					XmlDocPkgUtil.copyValues(result, sellIntegralLogRec, 0, true);
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", userid);
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "source_id", source_id);
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "1");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "5");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "create_by", create_by);
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "点击量");
					XmlDocPkgUtil.setChildText(sellIntegralLogRec, "valid", "Y");
					pdao.insertOneRecordSeqPk(sellIntegralLogRec);
				 }
				
			}
			
			pdao.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-获取点击量]"+e.getMessage());
		}
	}
	
	/**
	 * 查询一天内教师上传的课件
	 */
	public void getUploadVideoList(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT t1.userid,t1.logid,t1.begintime FROM pcmc_log t1, pcmc_user t2");
		strSQL.append(" WHERE t1.userid = t2.userid AND t1.issuccess='1' AND t2.usertype = '2' AND t1.sysname='yuexue' AND t1.oprid = 'Video' AND t1.actions = 'uploadVideoList'");
		strSQL.append("  AND t1.begintime BETWEEN CURDATE() - INTERVAL 1 DAY AND CURDATE() ORDER BY t1.begintime");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0,-1);
			insertIntegral(result,"3","10","上传课件");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-获取教师上传课件]"+e.getMessage());
		}
	}
	
	/**
	 * 查询一天内教师上传的试卷
	 */
	public void getPaperList(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT t1.userid,t1.logid,t1.begintime FROM pcmc_log t1, pcmc_user t2");
		strSQL.append(" WHERE t1.userid = t2.userid AND t1.issuccess='1' AND t2.usertype = '2' AND t1.sysname='yuexue' AND t1.oprid = 'Problem' AND t1.actions = 'savePaper'");
		strSQL.append("  AND t1.begintime BETWEEN CURDATE() - INTERVAL 1 DAY AND CURDATE() ORDER BY t1.begintime");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0,-1);
			insertIntegral(result,"4","5","上传试卷");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-获取教师上传试卷]"+e.getMessage());
		}
	}
	
	/**
	 * 查询一天内学生提交的试卷
	 */
	public void getSubmitExamResultList(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT t1.userid,t1.logid,t1.begintime FROM pcmc_log t1, pcmc_user t2");
		strSQL.append(" WHERE t1.userid = t2.userid AND t1.issuccess='1' AND t2.usertype = '1' AND t1.sysname='yuexue' AND t1.oprid = 'Learning' AND t1.actions = 'submitExamResult'");
		strSQL.append("  AND t1.begintime BETWEEN CURDATE() - INTERVAL 1 DAY AND CURDATE() ORDER BY t1.begintime");
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(0,-1);
			insertIntegral(result,"7","1","学生提交试卷");
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-学生提交试卷]"+e.getMessage());
		}
	}
	
	/**
	 * 增加积分信息
	 * @param result 
	 * @param integralType 积分类型
	 * @param integralNum 积分数量
	 * @param remark 备注信息
	 */
	public void insertIntegral(Element resultRec,String integralType,String integralNum,String remark){
		PlatformDao pdao = new PlatformDao();
		List userList = resultRec.getChildren("Record");
		try {
			pdao.beginTransaction();
			
			for (int i=0;i<userList.size();i++){
				 Element userRec = (Element)userList.get(i);
				 String userid=userRec.getChildText("userid");
				 String source_id=userRec.getChildText("logid");
				 //增加积分信息sell_integral
				Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
				XmlDocPkgUtil.copyValues(resultRec, sellIntegralLogRec, 0, true);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", userid);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "source_id", source_id);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", integralNum);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", integralType);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "create_by", "管理员");
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", remark);
				XmlDocPkgUtil.setChildText(sellIntegralLogRec, "valid", "Y");
				pdao.insertOneRecordSeqPk(sellIntegralLogRec);
			}
			
			pdao.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[沃课堂积分规则-插入积分]"+e.getMessage());
		}
	}
}
