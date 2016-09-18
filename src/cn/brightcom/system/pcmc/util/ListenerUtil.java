package cn.brightcom.system.pcmc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DaoUtil;

public class ListenerUtil {

	public String[][] deptName = {{"Сѧ��","44030010"},{"���в�","44030020"},{"���в�","44030030"},{"ְ�߲�","44030040"},{"����Ա(����)","44030090"}};
	
	//����subjectid��classid��ȡѧ����Ϣ
	public Map<String, String> getSbuject(String classid,String subjectid){
		Map<String,String> map = new HashMap<String, String>();
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	String subName = "";
    	try {
    		strBuffer.append("select * from (");
			strBuffer.append("select bts.userid,ps.usercode,bc.classid,bc.gradecode,bc.classcode,bc.classnm,bts.subjectid,bs.subjname from (select * from base_class where classid = ?) bc "); 
			strBuffer.append("LEFT JOIN base_teacher_subject bts ON bc.classid = bts.classid ");
			strBuffer.append("LEFT JOIN base_subject bs ON bts.subjectid = bs.subjectid ");
			strBuffer.append("LEFT JOIN pcmc_user ps ON bts.userid = ps.userid");
			strBuffer.append(") aa where aa.subjectid = ?");
			ArrayList bvals = new ArrayList();
			bvals.add(classid);
        	bvals.add(subjectid);
        	Element data = DaoUtil.getOneRecordData(strBuffer.toString(), bvals);
        	List nodes = data.getChildren("Record");
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	Element elm = (Element) it.next();
            	map.put("usercode", elm.getChildTextTrim("usercode"));
            	map.put("subjname", elm.getChildTextTrim("subjname"));
            	map.put("gradecode", elm.getChildTextTrim("gradecode"));
            	map.put("classcode", elm.getChildTextTrim("classcode"));
            	break;
            }
            return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
	
	//����usercode��username��ȡ�û���Ϣ
	public Map<String,String> getStudenInfoList(String usercode,String username){
		Map<String,String> map = new HashMap<String, String>();
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
			strBuffer.append("select * from pcmc_user where usercode = ? and username = ?");
			ArrayList bvals = new ArrayList();
        	bvals.add(usercode);
        	bvals.add(username);
        	Element data = DaoUtil.getOneRecordData(strBuffer.toString(), bvals);
        	List nodes = data.getChildren("Record");
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	Element elm = (Element) it.next();
            	map.put("userid", elm.getChildTextTrim("userid"));
            	map.put("userpwd", elm.getChildTextTrim("userpwd"));
            	break;
            }
            return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
	
	//����teasubjid(�ڿ�ID)��ȡ�û���Ϣ
	public Map<String,String> getUserInfoAgin(String teasubjid){
		//��ȡ�����û�
    	Map<String,String> map = new HashMap<String, String>();
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	StringBuffer sqlBuf = new StringBuffer("select ps.userid,ps.username,ps.usercode,ps.usertype,ps.userpwd,bs.subjectid,bs.subjname,bc.gradecode,bc.classcode,bc.classnm,bc.classid from ")
    		.append("(select * from pcmc_user where userid = (select userid from base_teacher_subject where teasubjid = ?)) ps ")
        	.append("LEFT JOIN (select * from base_teacher_subject where state = '1') bts ON bts.userid = ps.userid ")
    		.append("LEFT JOIN base_subject bs ON bts.subjectid = bs.subjectid ")
    		.append("LEFT JOIN base_class bc ON bts.classid = bc.classid");
        	ArrayList bvals = new ArrayList();
        	bvals.add(teasubjid);
        	Element data = DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
        	List nodes = data.getChildren("Record");
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	Element elm = (Element) it.next();
            	map.put("userid", elm.getChildTextTrim("userid"));
            	map.put("classid", elm.getChildTextTrim("classid"));//�꼶id
            	map.put("usercode", elm.getChildTextTrim("usercode"));//��¼��
            	map.put("username",  elm.getChildTextTrim("username"));//����
            	map.put("usertype",  elm.getChildTextTrim("usertype"));//�˺�����(9������Ա,1ѧ��,2��ʦ,3�ҳ�)
            	map.put("userpwd",  elm.getChildTextTrim("userpwd"));//����
            	map.put("subjectid",  elm.getChildTextTrim("subjectid"));//ѧ��ID
            	map.put("subjname",  elm.getChildTextTrim("subjname"));//ѧ����
            	map.put("gradecode",  elm.getChildTextTrim("gradecode"));//�꼶ID
            	map.put("classcode",  elm.getChildTextTrim("classcode"));//�༶ID
            	map.put("classnm",  elm.getChildTextTrim("classnm"));//�༶��
            	if("".equals(elm.getChildTextTrim("classid"))||elm.getChildTextTrim("classid")==null){
            		map.put("flag", "false");//��ʶ �Ƿ��а༶�γ�
            	}else{
            		map.put("flag", "true");
            	}
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return map;
    }
	
	//����userid��ȡ�û���Ϣ
	public Map<String,String> getUserInfo(String userid){
		//��ȡ�����û�
    	Map<String,String> map = new HashMap<String, String>();
    	
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	StringBuffer sqlBuf = new StringBuffer("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid,bc.classnm,bc.classid ")
    		.append("from (select * from pcmc_user p where p.state=1 and p.usertype = 2 and userid =?) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON ts.classid = bc.classid ")
    		.append("UNION ALL ")
    		.append("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid,bc.classnm,bc.classid ")
    		.append("from (select * from pcmc_user p where p.state=1 and p.usertype <> 2 and userid =?) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON bcs.classid = bc.classid");
        	ArrayList bvals = new ArrayList();
        	bvals.add(userid);
        	bvals.add(userid);
        	Element data = DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
        	List nodes = data.getChildren("Record");
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	Element elm = (Element) it.next();
            	map.put("userid", elm.getChildTextTrim("userid"));
            	map.put("classid", elm.getChildTextTrim("classid"));//�꼶id
            	map.put("usercode", elm.getChildTextTrim("usercode"));//��¼��
            	map.put("username",  elm.getChildTextTrim("username"));//����
            	map.put("usertype",  elm.getChildTextTrim("usertype"));//�˺�����(9������Ա,1ѧ��,2��ʦ,3�ҳ�)
            	map.put("userpwd",  elm.getChildTextTrim("userpwd"));//����
            	map.put("gender",  elm.getChildTextTrim("gender"));//�Ա�
            	map.put("mobile",  elm.getChildTextTrim("mobile"));//�绰
            	map.put("subjectid",  elm.getChildTextTrim("subjectid"));//ѧ��ID
            	map.put("gradecode",  elm.getChildTextTrim("gradecode"));//�꼶ID
            	map.put("classcode",  elm.getChildTextTrim("classcode"));//�༶ID
            	map.put("classnm",  elm.getChildTextTrim("classnm"));//�༶��
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return map;
    }
	
	//��װ����ID
	public String getDeptId(String gcode,String subjectid,String ccode){
		String ucode = "";
		String pcode = "";
		if("".equals(gcode)||gcode==null){//û���꼶
      		pcode = deptName[4][1];//������������
      	}else{
      		int gradecode = Integer.parseInt(gcode);
          	if((gradecode>=1 && gradecode<=6) || gradecode==97){//Сѧ��
          		pcode = deptName[0][1];//������ID
          	}else if((gradecode>6 && gradecode<10) || gradecode==98){//���в�
          		pcode = deptName[1][1];//������ID
          	}else if((gradecode>=10 && gradecode<=12) || gradecode==99){//���в�
          		pcode = deptName[2][1];//������ID
          	}else if((gradecode>20 && gradecode<24)){//ְ�߲�
          		pcode = deptName[3][1];//������ID
          	}
      	}
      	if("".equals(subjectid)||subjectid==null){//û��ѧ��
      		if("".equals(gcode)||gcode==null){//������û��ѧ�ơ��꼶��
      			ucode = pcode;
      		}else{//û��ѧ�ơ����꼶
      			ucode = pcode+"|"+gcode+"|"+ccode;//�û�����ID = (Сѧ������...)ID+�꼶ID+�༶ID
      		}
      	}else{//��ѧ��
      		ucode = pcode+subjectid;//�û�����ID = (Сѧ������...)ID+ѧ��ID
      	}
      	return ucode;
	}
	
	//��װ����ID
	public String getDeptId(boolean flag,String gcode,String subjectid,String ccode){
		String ucode = "";
		String pcode = "";
		if("".equals(gcode)||gcode==null){//û���꼶
      		pcode = deptName[4][1];//������������
      	}else{
      		int gradecode = Integer.parseInt(gcode);
          	if((gradecode>=1 && gradecode<=6) || gradecode==97){//Сѧ��
          		pcode = deptName[0][1];//������ID
          	}else if((gradecode>6 && gradecode<10) || gradecode==98){//���в�
          		pcode = deptName[1][1];//������ID
          	}else if((gradecode>=10 && gradecode<=12) || gradecode==99){//���в�
          		pcode = deptName[2][1];//������ID
          	}else if((gradecode>20 && gradecode<24)){//ְ�߲�
          		pcode = deptName[3][1];//������ID
          	}
      	}
		if(flag){//��ǰ����or�û������� (true) 
			if("".equals(subjectid)||subjectid==null){//û��ѧ��
	      		if("".equals(gcode)||gcode==null){//������û��ѧ�ơ��꼶��
	      			ucode = pcode;
	      		}else{//û��ѧ�ơ����꼶
	      			ucode = pcode+"|"+gcode+"|"+ccode;//�û�����ID = (Сѧ������...)ID+�꼶ID+�༶ID
	      		}
	      	}else{//��ѧ��
	      		ucode = pcode+subjectid;//�û�����ID = (Сѧ������...)ID+ѧ��ID
	      	}
		}else{//���ŵĸ�����(false)
			ucode = pcode;
		}
      	return ucode;
	}

	//����ѧУ��deptid����ȡ�û���Ϣ
	public List<Map<String,String>> getGradeList(String deptid,String gradeid,String classid){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
    		ArrayList bvals = new ArrayList();
			if(!"".equals(gradeid)){
				strBuffer.append("select * from base_class where deptid = ? and gradecode in (select gradecode from base_grade where gradeid = ?)");
				bvals.add(deptid);
				bvals.add(gradeid);
				
				pdao.setSql(strBuffer.toString());
				pdao.addBatch(bvals);
				Element gradeTmpData = pdao.executeQuerySql(-1, 1);
				List list1 = gradeTmpData.getChildren("Record");
	            for (int i=0;i<list1.size();i++) {
	            	map = new HashMap<String, String>();
	            	Element elm = (Element)list1.get(i);
            		map.put("state", elm.getChildText("state"));
            		map.put("gradecode", elm.getChildText("gradecode"));
            		map.put("classcode", elm.getChildText("classcode"));
            		map.put("classnm", elm.getChildText("classnm"));
	            	list.add(map);
	            }
			}else if(!"".equals(classid)){
				strBuffer.append("select * from base_class where classid = ?");
				bvals.add(classid);
				pdao.setSql(strBuffer.toString());
				pdao.addBatch(bvals);
				Element gradeTmpData = pdao.executeQuerySql(-1, 1);
				List list1 = gradeTmpData.getChildren("Record");
	            for (int i=0;i<list1.size();i++) {
	            	map = new HashMap<String, String>();
	            	Element elm = (Element)list1.get(i);
	            	map.put("state", elm.getChildTextTrim("state"));
	            	map.put("gradecode", elm.getChildTextTrim("gradecode"));
	            	map.put("classcode", elm.getChildTextTrim("classcode"));
	            	map.put("classnm", elm.getChildTextTrim("classnm"));
	            	list.add(map);
	            }
			}
        	
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
	
	//����gradecode��ȡ�꼶
	public List<Map<String,String>> getClassList(String deptid,String gradecode){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
			strBuffer.append("select classid,gradecode,classcode,classnm from base_class where state = 1 and deptid = ? and gradecode = ?");
			ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	bvals.add(gradecode);
        	pdao.setSql(strBuffer.toString());
			pdao.addBatch(bvals);
			Element gradeTmpData = pdao.executeQuerySql(-1, 1);
			List list1 = gradeTmpData.getChildren("Record");
            for (int i=0;i<list1.size();i++) {
            	map = new HashMap<String, String>();
            	Element elm = (Element)list1.get(i);
        		map.put("gradecode", elm.getChildText("gradecode"));
        		map.put("classcode", elm.getChildText("classcode"));
        		map.put("classnm", elm.getChildText("classnm"));
            	list.add(map);
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
	
	//����ѧ��userid��ȡ�û���
	public List<Map<String,String>> getStuInfo(String[] userid){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	String usercode = "";
    	try {
    		ArrayList bvals = new ArrayList();
			strBuffer.append("select userid,usercode from pcmc_user where userid = ?");
			bvals.add(userid[0]);
			if(userid.length>1){
				for(int i=1;i<userid.length;i++){
					strBuffer.append(" or userid = ?");
					bvals.add(userid[i]);
				}
			}
        	pdao.setSql(strBuffer.toString());
			pdao.addBatch(bvals);
			Element gradeTmpData = pdao.executeQuerySql(-1, 1);
			List list1 = gradeTmpData.getChildren("Record");
            for (int i=0;i<list1.size();i++) {
            	map = new HashMap<String, String>();
            	Element elm = (Element)list1.get(i);
            	map.put("usercode", elm.getChildText("usercode"));
            	map.put("userid", elm.getChildText("userid"));
            	list.add(map);
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
    }
	
	/** ��Ϣ���� **/
	
	//��ȡ��ǰ�û���������Ϣ��¼
	public List<Map<String,String>> getUserMsg(String userid,String datetime){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
			strBuffer.append("select lep.paper_name,lps.send_id,lps.userid,lps.create_by,lps.receiver_ids,lps.receiver_names,lps.send_content ")
			.append("from learn_paper_send lps,learn_examination_paper lep where lps.paper_id = lep.paper_id and lps.userid = ? and lps.create_date > ? and lps.public_status = 2 and lps.valid = 'Y'");
        	
			ArrayList bvals = new ArrayList();
        	bvals.add(userid);
        	bvals.add(datetime);
        	pdao.setSql(strBuffer.toString());
			pdao.addBatch(bvals);
			Element data = pdao.executeQuerySql(-1, 1);
        	List nodes = data.getChildren("Record");
            for (int i=0;i<nodes.size();i++) {
            	map = new HashMap<String, String>();
            	Element elm = (Element)nodes.get(i);
            	map.put("userid", elm.getChildTextTrim("userid"));
            	map.put("send_id", elm.getChildTextTrim("send_id"));
            	map.put("create_by", elm.getChildTextTrim("create_by"));
            	map.put("receiver_ids", elm.getChildTextTrim("receiver_ids"));
            	map.put("receiver_names", elm.getChildTextTrim("receiver_names"));
            	map.put("send_content", elm.getChildTextTrim("send_content"));
            	map.put("paper_name", elm.getChildTextTrim("paper_name"));
            	list.add(map);
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}
	}
	
	//����receiver_names(�༶��)��ȡ�༶��Ϣ
	public List<Map<String,String>> getMsgUser(String receiver_names[],String deptid){
		if("".equals(receiver_names)||receiver_names==null){return null;}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
    		ArrayList bvals = new ArrayList();
			strBuffer.append("select * from base_class where deptid = ? and (classnm = ?");
			bvals.add(deptid);
			bvals.add(receiver_names[0]);
			if(receiver_names.length>1){
				for(int i=1;i<receiver_names.length;i++){
					strBuffer.append(" or classnm = ?");
					bvals.add(receiver_names[i]);
				}
			}
			strBuffer.append(")");
        	pdao.setSql(strBuffer.toString());
			pdao.addBatch(bvals);
			Element data = pdao.executeQuerySql(-1, 1);
        	List nodes = data.getChildren("Record");
        	for (int i=0;i<nodes.size();i++) {
            	map = new HashMap<String, String>();
            	Element elm = (Element)nodes.get(i);
            	map.put("classid", elm.getChildTextTrim("classid"));
            	map.put("gradecode", elm.getChildTextTrim("gradecode"));
            	map.put("classcode", elm.getChildTextTrim("classcode"));
            	map.put("classnm", elm.getChildTextTrim("classnm"));
            	list.add(map);
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}	
    }
	
	//����sendid��ȡ������Ϣ���û�
	public List<Map<String,String>> getMsgUser(String sendid){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
    	StringBuffer strBuffer = new StringBuffer();
    	PlatformDao pdao = new PlatformDao();
    	try {
			strBuffer.append("select lme.paper_id,ps.* from pcmc_user ps,learn_my_examination lme where ps.userid = lme.userid and lme.send_id = ?");
			ArrayList bvals = new ArrayList();
        	bvals.add(sendid);
        	Element data = DaoUtil.getOneRecordData(strBuffer.toString(), bvals);
        	List nodes = data.getChildren("Record");
        	for (int i=0;i<nodes.size();i++) {
        		map = new HashMap<String, String>();
            	Element elm = (Element)nodes.get(i);
            	map.put("userid", elm.getChildTextTrim("userid"));
            	map.put("usercode", elm.getChildTextTrim("usercode"));
            	map.put("userpwd", elm.getChildTextTrim("userpwd"));
            	map.put("username", elm.getChildTextTrim("username"));
            	map.put("paper_id", elm.getChildTextTrim("paper_id"));
            	list.add(map);
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			pdao.releaseConnection();
		}	
    }
	
	//����paperid��ȡ��װurl��Ϣ
	public Map<String,String> getUrlMsgByPaperid(String paperid){
    	Map<String,String> map = new HashMap<String, String>();
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	
        	StringBuffer sqlBuf = new StringBuffer("select t1.*,t2.folder_code,t3.attachment_id,t4.`status` from ")
        	.append("(select paper_id,subject_id,folder_id,paper_name,grade_code from learn_examination_paper where paper_id = ?) t1 ")
        	.append("LEFT JOIN base_book_folder t2 ON t1.folder_id = t2.folder_id ")
        	.append("LEFT JOIN learn_paper_attachment t3 ON t1.paper_id = t3.paper_id ")
        	.append("LEFT JOIN learn_my_examination t4 ON t1.paper_id = t4.paper_id");
        	
        	ArrayList bvals = new ArrayList();
        	bvals.add(paperid);
        	Element data = DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
        	List nodes = data.getChildren("Record");
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	Element elm = (Element) it.next();
            	map.put("paper_id", elm.getChildTextTrim("paper_id"));
            	map.put("subject_id", elm.getChildTextTrim("subject_id"));
            	map.put("folder_id", elm.getChildTextTrim("folder_id"));
            	map.put("paper_name",  elm.getChildTextTrim("paper_name"));
            	map.put("grade_code",  elm.getChildTextTrim("grade_code"));
            	map.put("folder_code",  elm.getChildTextTrim("folder_code"));
            	map.put("attachment_id",  elm.getChildTextTrim("attachment_id"));
            	map.put("status",  elm.getChildTextTrim("'status'"));
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return map;
    }
}
