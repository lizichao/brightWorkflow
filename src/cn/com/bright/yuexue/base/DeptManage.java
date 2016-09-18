package cn.com.bright.yuexue.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.ListenerUtil;
import cn.brightcom.system.pcmc.widetel.RealinkInterface;

/**
 * 
 * <p>Title: 初始化机构基础数据</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class DeptManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(DeptManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("init".equals(action))
    	{
    		init();
    	}else if("initDept".equals(action))//初始化用户结构
        {
        	initUser();
        }
    	
		return xmlDoc;
	}
    
    private void init()
    {
    	try
    	{
    		initGrade();
    		initCourse();
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10600", "初始化机构数据成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log4j.logError(ex);
    		xmlDocUtil.writeErrorMsg("10601", "初始化机构数据失败");
    	}
    }
    
    /**
     * 初始化年级
     */
    private void initGrade()
    	throws Exception
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	String deptid = reqData.getChildText("deptid");
        	//查询是否有记录
        	StringBuffer sqlBuf = new StringBuffer("select 1 as gg from base_grade where deptid=?");
        	ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	pDao.setSql(sqlBuf.toString());
        	pDao.setBindValues(bvals);
        	Element gradeData = pDao.executeQuerySql(-1, 1);
        	//初始化
        	if(null == gradeData.getChild("Record"))
        	{
        		sqlBuf.setLength(0);
        		sqlBuf.append("select a.paramcode,a.parammeanings ")
        			.append("from param_detail a,param_master b ")
        			.append("where a.paramid=b.paramid and b.paramname='c_grade'");
        		pDao.setSql(sqlBuf.toString());
        		Element gradeTmpData = pDao.executeQuerySql(-1, 1);
        		
        		sqlBuf.setLength(0);
        		sqlBuf.append("insert into base_grade ")
        			.append("(gradeid,deptid,gradecode,gradename,finished,enabled) ")
        			.append("values (?,?,?,?,?,?)");
        		pDao.setSql(sqlBuf.toString());
        		List list = gradeTmpData.getChildren("Record");
        		UUIDHexGenerator uuidGen = new UUIDHexGenerator();
        		for(int i=0; i<list.size(); i++)
        		{
        			Element gradeEle = (Element)list.get(i);
        			bvals.clear();
        			bvals.add(uuidGen.generate(null));
        			bvals.add(deptid);
        			bvals.add(gradeEle.getChildText("paramcode"));
        			bvals.add(gradeEle.getChildText("parammeanings"));
        			bvals.add(new Integer(0));
        			bvals.add(new Integer(0));
        			
        			pDao.addBatch(bvals);
        		}
        		pDao.executeBatch();
        	}
        	
        	pDao.commitTransaction();
        }
        catch(Exception ex)
        {
        	pDao.rollBack();
        	throw ex;
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
    
    /**
     * 初始化学科
     */
    private void initCourse() throws Exception
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	String deptid = reqData.getChildText("deptid");
        	//查询是否有记录
        	StringBuffer sqlBuf = new StringBuffer("select 1 as cc from base_course where deptid=?");
        	ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	pDao.setSql(sqlBuf.toString());
        	pDao.setBindValues(bvals);
        	Element courseData = pDao.executeQuerySql(-1, 1);
        	//初始化
        	if(null == courseData.getChild("Record"))
        	{
        		sqlBuf.setLength(0);
        		sqlBuf.append("select * from base_course t where t.state = '2'");
        		pDao.setSql(sqlBuf.toString());
        		Element courseTmpData = pDao.executeQuerySql(-1, 1);
        		
        		sqlBuf.setLength(0);
        		sqlBuf.append("insert into base_course ")
        			.append("(courseid,deptid,gradecode,subjectid,coursenm,courseicon,state,subject_book_id,orderidx) ")
        			.append("values (?,?,?,?,?,?,?,?,?)");
        		pDao.setSql(sqlBuf.toString());
        		List list = courseTmpData.getChildren("Record");
        		UUIDHexGenerator uuidGen = new UUIDHexGenerator();
        		for(int i=0; i<list.size(); i++)
        		{
        			Element courseEle = (Element)list.get(i);
        			bvals.clear();
        			bvals.add(uuidGen.generate(null));
        			bvals.add(deptid);
        			bvals.add(courseEle.getChildText("gradecode"));
        			bvals.add(courseEle.getChildText("subjectid"));
        			bvals.add(courseEle.getChildText("coursenm"));
        			bvals.add(courseEle.getChildText("courseicon"));
        			bvals.add("1");
        			bvals.add(courseEle.getChildText("subject_book_id"));
        			bvals.add(courseEle.getChildText("orderidx"));        			
        			pDao.addBatch(bvals);
        		}
        		pDao.executeBatch();
        	}
        	
        	pDao.commitTransaction();
        }
        catch(Exception ex)
        {
        	pDao.rollBack();
        	throw ex;
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
    
    /**
     * 初始化用户结构(同步至其他平台) 2016.03.21
     */
    private final void initUser(){
    	RealinkInterface rt = new RealinkInterface();
    	ListenerUtil listenerUtil = new ListenerUtil();
    	Map<String, Object> map = new HashMap<String, Object>();
        Element dataElement = xmlDocUtil.getRequestData();
        boolean flag1 = true;
        boolean flag2 = true;
        String deptid = dataElement.getChildTextTrim("deptid");
        Map<String,String> xmlMap = rt.genHuiXunMessage("companyId");
        String compId = xmlMap.get("method").toString();
        //增加 固定部门
        String[][] deptName = listenerUtil.deptName;
        for(int i =0;i<deptName.length;i++){
    		map.put("depId",deptName[i][1]);//要增加的部门id(syncDepId)
    		map.put("depName",deptName[i][0]);//当前部门名称
    		map.put("syncDepId",compId);//父部门id(syncDepId)
    		map.put("LoginName","admin");//当前操作人
    		map.put("keycode","gzRN53VWRF9BYUXo");
    		//将map对象转换成json数据
    		String str = rt.Object2Json(map);
    		Map<String, String> backMap = rt.commonServiceByJson("insertDept", str);
    		//有一个调用错误则跳出循环
    		if("调用出错".equals(backMap.get("msg").toString())){
    			flag1 = false;
    			break;
    		}else{
    			map.clear();
    		}
        }
        //增加 教师用户 上级部门
        List<Map<String,String>> list = getSubject(deptid);//获取 所有学科(即：老师用户的上级部门)
    	String tcode = "";
    	for(int j =0;j<list.size();j++){
        	Map<String,String> sbjMap = list.get(j);
        	String ccode = sbjMap.get("subjectid").toString();
        	String cname = sbjMap.get("subjname").toString();
        	String gcode = sbjMap.get("gradecode").toString();
        	int gradecode = Integer.parseInt(gcode);
        	if((gradecode>=1 && gradecode<=6) || gradecode==97){//小学部
        		tcode = deptName[0][1];//父部门ID
        	}else if((gradecode>6 && gradecode<10) || gradecode==98){//初中部
        		tcode = deptName[1][1];//父部门ID
        	}else if((gradecode>=10 && gradecode<=12) || gradecode==99){//高中部
        		tcode = deptName[2][1];//父部门ID
        	}else if(gradecode>20 && gradecode<24){//职高部
        		tcode = deptName[3][1];//父部门ID
        	}else if(gradecode ==100){
        		tcode = deptName[4][1];//父部门ID
        		boolean back = addOtherDept(rt,deptName);//增加“其他”部门(小学、初中、高中、职高都加一个 其他)
        		if(!back){//调用出错
        			flag1 = false;
        			break;
        		}
        	}
        	ccode = tcode+ccode;//教师用户上级部门ID = 父部门ID+学科ID
    		map.put("depId",ccode);//要增加的部门id(syncDepId)
    		map.put("depName",cname);//当前部门名称
    		map.put("syncDepId",tcode);//父部门id(syncDepId)
    		map.put("LoginName","admin");//当前操作人
    		map.put("keycode","gzRN53VWRF9BYUXo");
    		//将map对象转换成json数据
    		String str = rt.Object2Json(map);
    		Map<String, String> backMap = rt.commonServiceByJson("insertDept", str);
    		//有一个调用错误则跳出循环
    		if("调用出错".equals(backMap.get("msg").toString())){
    			flag1 = false;
    			break;
    		}else{
    			map.clear();
    		}
        }
        //增加 学生用户 上级部门
        List<Map<String,String>> listStu = getClassAll(deptid);//获取所有班级（即：学生用户的上级部门）
        String pcode = "";
        for(int j =0;j<listStu.size();j++){
        	Map<String,String> sbjMap = listStu.get(j);
        	String ccode = sbjMap.get("classcode").toString();
        	String gcode = sbjMap.get("gradecode").toString();
        	String cname = sbjMap.get("classnm").toString();
        	int gradecode = Integer.parseInt(gcode);
        	if((gradecode>=1 && gradecode<=6) || gradecode==97){//小学部
        		pcode = deptName[0][1];//父部门ID
        	}else if((gradecode>6 && gradecode<10) || gradecode==98){//初中部
        		pcode = deptName[1][1];//父部门ID
        	}else if((gradecode>=10 && gradecode<=12) || gradecode==99){//高中部
        		pcode = deptName[2][1];//父部门ID
        	}else if((gradecode>20 && gradecode<24)){//职高部
        		pcode = deptName[3][1];//父部门ID
        	}
        	ccode = pcode+"|"+gcode+"|"+ccode;//学生用户上级部门ID = 父部门ID+年级ID+班级ID
    		map.put("depId",ccode);//要增加的部门id(syncDepId)
    		map.put("depName",cname);//当前部门名称
    		map.put("syncDepId",pcode);//父部门id(syncDepId)
    		map.put("LoginName","admin");//当前操作人
    		map.put("keycode","gzRN53VWRF9BYUXo");
    		//将map对象转换成json数据
    		String str = rt.Object2Json(map);
    		Map<String, String> backMap = rt.commonServiceByJson("insertDept", str);
    		//有一个调用错误则跳出循环
    		if("调用出错".equals(backMap.get("msg").toString())){
    			flag1 = false;
    			break;
    		}else{
    			map.clear();
    		}
        }
	    if(!flag1){
	    	xmlDocUtil.writeErrorMsg("10607","初始化用户失败!");
	    }
	    
        //判断 增加部门是否失败
        if(flag1){
          //添加用户
          List<Map<String,String>> listUse = getUserAll(deptid);//获取所有用户
          for(int i =0;i<listUse.size();i++){
        	String sex = "未知";
            String title = "";
            String ucode = "";
            int isAdmin = 0;
          	Map<String,String> sbjMap = listUse.get(i);
          	String subjectid = sbjMap.get("subjectid").toString();//学科
          	String gcode = sbjMap.get("gradecode").toString();//年级
          	String ccode = sbjMap.get("classcode").toString();//班级
          	String usertype = sbjMap.get("usertype").toString();//账号类型
          	String gender = sbjMap.get("gender").toString();//性别
          	
          	//性别
          	if("0".equals(gender)){
          		sex = "女";
          	}else if("1".equals(gender)){
          		sex = "男";
          	}
          	//管理员、职业
          	if("9".equals(usertype)){
          		isAdmin = 1;
          		title = "管理员";
          	}else if("2".equals(usertype)){
          		title = "老师";
          	}else if("1".equals(usertype)){
          		title = "学生";
          	}
          	//部门id
          	if("".equals(gcode)||gcode==null){//没有年级
          		pcode = deptName[4][1];//“其他”部门
          	}else{
          		int gradecode = Integer.parseInt(gcode);
              	if((gradecode>=1 && gradecode<=6) || gradecode==97){//小学部
              		pcode = deptName[0][1];//父部门ID
              	}else if((gradecode>6 && gradecode<10) || gradecode==98){//初中部
              		pcode = deptName[1][1];//父部门ID
              	}else if((gradecode>=10 && gradecode<=12) || gradecode==99){//高中部
              		pcode = deptName[2][1];//父部门ID
              	}else if((gradecode>20 && gradecode<24)){//职高部
              		pcode = deptName[3][1];//父部门ID
              	}
          	}
          	if("".equals(subjectid)||subjectid==null){//没有学科
          		if("".equals(gcode)||gcode==null){//其他（没有学科、年级）
          			ucode = pcode;
          		}else{//没有学科、有年级
          			ucode = pcode+"|"+gcode+"|"+ccode;//用户部门ID = (小学、初中...)ID+年级ID+班级ID
          		}
          	}else{//有学科
          		ucode = pcode+subjectid;//用户部门ID = (小学、初中...)ID+学科ID
          	}
          	
          	System.out.println("ucode=="+ucode);
          	String pasword = "123456";
			try {
				pasword = Crypto.decodeByKey("SUNLINES", sbjMap.get("userpwd").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
        	//增加部门成功后，调用 增加用户 接口
    		map.put("loginName","admin");//当前操作账户
    		map.put("name",sbjMap.get("usercode").toString());//登录名称
    		map.put("UserName",sbjMap.get("username").toString());//员工姓名
    		map.put("isAdmin",isAdmin);//是否管理员
    		map.put("depid",ucode);//部门ID(syncDepId)
    		map.put("password",pasword);//密码
    		map.put("md5",false);
    		map.put("gender",sex);//性别
    		map.put("mobile",sbjMap.get("mobile").toString());//手机
    		map.put("title",title);//职位名
    		map.put("syncUserId",sbjMap.get("userid").toString());//人员ID
    		map.put("keycode","gzRN53VWRF9BYUXo");
    		//将UserInfoUtils对象转换成json数据
    		String user_info = rt.Object2Json(map);
    		Map<String, String> backMap = rt.commonServiceByJson("insertUser", user_info);
    		//有一个调用错误则跳出循环
    		if("调用出错".equals(backMap.get("msg").toString())){
    			flag2 = false;
    			break;
    		}else{
    			map.clear();
    		}
          }
	      if(flag2){
	    	xmlDocUtil.setResult("0");
			xmlDocUtil.writeHintMsg("初始化用户成功!");
	      }else{
	      	xmlDocUtil.writeErrorMsg("10601","初始化用户失败!");
	      }
       }
    }
    
    //增加“其他”部门
    public boolean addOtherDept(RealinkInterface rt,String[][] deptName){
    	Map<String,Object> map = new HashMap<String, Object>();
    	for(int i =0;i<deptName.length-1;i++){
    		map.put("depId",deptName[i][1]+"100");//要增加的部门id(syncDepId)
    		map.put("depName","其他");//当前部门名称
    		map.put("syncDepId",deptName[i][1]);//父部门id(syncDepId)
    		map.put("LoginName","admin");//当前操作人
    		map.put("keycode","gzRN53VWRF9BYUXo");
    		//将map对象转换成json数据
    		String str = rt.Object2Json(map);
    		Map<String, String> backMap = rt.commonServiceByJson("insertDept", str);
    		//有一个调用错误则跳出循环
    		if("调用出错".equals(backMap.get("msg").toString())){
    			return false;
    		}else{
    			map.clear();
    		}
        }
    	return true;
    }
    //获取所有学科
    public List<Map<String,String>> getSubject(String deptid){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	Map<String,String> map = null;
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	/*
            StringBuffer sqlBuf = new StringBuffer("select aa.subjectid,aa.subjname,bb.gradecode from ")
    		.append("(select tss.userid,tss.username,IFNULL(bs.subjectid,100) subjectid,IFNULL(bs.subjname,'其他') subjname from (select th.userid,th.username,ts.subjectid from (select * from pcmc_user where usertype = 2) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid) tss LEFT JOIN base_subject bs ON bs.subjectid = tss.subjectid) aa,")
    		.append("(select tss.userid,IFNULL(bc.gradecode,100) gradecode,bc.classnm from (select th.userid,th.username,ts.classid from (select * from pcmc_user where usertype = 2) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid) tss LEFT JOIN base_class bc ON bc.classid = tss.classid) bb ")
    		.append("where aa.userid = bb.userid GROUP BY aa.subjname,bb.gradecode");
            */
            StringBuffer sqlBuf = new StringBuffer("select aa.subjectid,aa.subjname,bb.gradecode from ")
    		.append("(select tss.userid,tss.username,IFNULL(bs.subjectid,100) subjectid,IFNULL(bs.subjname,'其他') subjname from (select th.userid,th.username,ts.subjectid from (select * from base_teacher_subject bts where deptid = ? group by bts.userid) ts LEFT JOIN (select * from pcmc_user where usertype = 2) th ON th.userid = ts.userid) tss LEFT JOIN base_subject bs ON bs.subjectid = tss.subjectid) aa,")
    		.append("(select tss.userid,IFNULL(bc.gradecode,100) gradecode,bc.classnm from (select th.userid,th.username,ts.classid from (select * from pcmc_user where usertype = 2) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid) tss LEFT JOIN base_class bc ON bc.classid = tss.classid) bb ")
    		.append("where aa.userid = bb.userid GROUP BY aa.subjname,bb.gradecode");
            
            ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	dao.setSql(sqlBuf.toString()) ;
        	dao.setBindValues(bvals);
            
            Element resultElement = dao.executeQuerySql(-1,1) ;
            
            List nodes = resultElement.getChildren("Record");
            System.out.println(nodes.size());
            for (Iterator it = nodes.iterator(); it.hasNext();) {
            	map = new HashMap<String, String>();
            	Element elm = (Element) it.next();
            	String code = elm.getChildTextTrim("subjectid");
            	String name = elm.getChildTextTrim("subjname");
            	String grade = elm.getChildTextTrim("gradecode");
            	map.put("subjname", name);
            	map.put("subjectid", code);
            	map.put("gradecode", grade);
            	list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return list;
    }
    //获取有用户账号的班级
    public List<Map<String,String>> getClassAll(String deptid){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	Map<String,String> map = new HashMap<String, String>();
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	/*
        	StringBuffer sqlBuf = new StringBuffer("select bc.classid,bc.gradecode,bc.classcode,bc.classnm ")
    		.append("from (select bs.classid from (select * from pcmc_user where usertype = 1) pu,base_studentinfo bs where pu.userid = bs.userid and bs.classid is not null group by bs.classid) cl ")
    		.append("LEFT JOIN base_class bc ON bc.state = 1 and bc.classid = cl.classid ")
    		.append("group by bc.classnm order by bc.gradecode asc,bc.classcode asc");
    		*/
        	StringBuffer sqlBuf = new StringBuffer("select bc.classid,bc.gradecode,bc.classcode,bc.classnm ")
    		.append("from (select bs.classid from (select * from pcmc_user where usertype = 1) pu,(select * from base_studentinfo where deptid = ? and valid = 'Y') bs where pu.userid = bs.userid and bs.classid is not null group by bs.classid) cl ")
    		.append("LEFT JOIN base_class bc ON bc.state = 1 and bc.classid = cl.classid ")
    		.append("group by bc.classnm order by bc.gradecode asc,bc.classcode asc");
        	
        	ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	dao.setSql(sqlBuf.toString()) ;
        	dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1) ;
            
            List nodes = resultElement.getChildren("Record");
            System.out.println(nodes.size());
            for (Iterator it = nodes.iterator(); it.hasNext();) { 
            	map = new HashMap<String, String>();
            	Element elm = (Element) it.next();
            	String gradecode = elm.getChildTextTrim("gradecode");
            	String classcode = elm.getChildTextTrim("classcode");
            	String classnm = elm.getChildTextTrim("classnm");
            	map.put("classnm", classnm);//班级部门名称
            	map.put("classcode", classcode);//班级部门ID
            	map.put("gradecode", gradecode);//年级ID
            	list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return list;
    }
    //获取所有用户
    public List<Map<String,String>> getUserAll(String deptid){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	Map<String,String> map = new HashMap<String, String>();
    	String gender = "";
    	String mobile = "";
    	String subjectid = "";
    	String gradecode = "";
    	String classcode = "";
    	PlatformDao dao = null;
        try{
        	dao = new PlatformDao();
        	/*
        	StringBuffer sqlBuf = new StringBuffer("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid ")
    		.append("from (select * from pcmc_user p where p.state=1 and p.usertype = 2) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON ts.classid = bc.classid ")
    		.append("UNION ALL ")
    		.append("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid ")
    		.append("from (select * from pcmc_user p where p.state=1 and p.usertype <> 2) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON bcs.classid = bc.classid");
        	*/
        	StringBuffer sqlBuf = new StringBuffer("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid ")
    		.append("from (select p.* from pcmc_user p,pcmc_user_dept pd where p.userid = pd.userid and p.state=1 and p.usertype = 2 and pd.deptid = ?) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON ts.classid = bc.classid ")
    		.append("UNION ALL ")
    		.append("select th.userid,th.usercode,th.username,th.usertype,th.userpwd,th.gender,th.mobile,bc.gradecode,bc.classcode,ts.subjectid ")
    		.append("from (select p.* from pcmc_user p,pcmc_user_dept pd where p.userid = pd.userid and p.state=1 and p.usertype <> 2 and pd.deptid = ?) th LEFT JOIN (select * from base_teacher_subject bts group by bts.userid) ts ON th.userid = ts.userid ")
    		.append("LEFT JOIN (select bs.userid,bs.classid from base_studentinfo bs) bcs ON th.userid = bcs.userid ")
    		.append("LEFT JOIN base_class bc ON bcs.classid = bc.classid");
        	
        	ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	bvals.add(deptid);
        	dao.setSql(sqlBuf.toString()) ;
        	dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1) ;
            
            List nodes = resultElement.getChildren("Record");
            
            for (Iterator it = nodes.iterator(); it.hasNext();) { 
            	map = new HashMap<String, String>();
            	Element elm = (Element) it.next();
            	String userid = elm.getChildTextTrim("userid");
            	String usercode = elm.getChildTextTrim("usercode");
            	String username = elm.getChildTextTrim("username");
            	String usertype = elm.getChildTextTrim("usertype");
            	String userpwd = elm.getChildTextTrim("userpwd");
            	gender = elm.getChildTextTrim("gender");
            	mobile = elm.getChildTextTrim("mobile");
            	subjectid = elm.getChildTextTrim("subjectid");
            	gradecode = elm.getChildTextTrim("gradecode");
            	classcode = elm.getChildTextTrim("classcode");
            	
            	map.put("userid", userid);//用户ID
            	map.put("usercode", usercode);//登录名
            	map.put("username", username);//姓名
            	map.put("usertype", usertype);//账号类型(9：管理员,1学生,2老师,3家长)
            	map.put("userpwd", userpwd);//密码
            	map.put("gender", gender);//性别
            	map.put("mobile", mobile);//电话
            	map.put("subjectid", subjectid);//学科ID
            	map.put("gradecode", gradecode);//年级ID
            	map.put("classcode", classcode);//班级ID
            	list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace() ;
        }finally{
            dao.releaseConnection();
        }
        return list;
    }
}
