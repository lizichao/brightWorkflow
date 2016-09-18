package cn.com.bright.workflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.PasswordEncoder;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.UriUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;


/**
 * 
 * @author lzc
 *
 */
@Controller
public class ProcessMonitorBean
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
   // private static Log log4j = new Log(WorkflowBean.class.getName());
    
    @Autowired
    protected RepositoryService repositoryService;
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
        if ("queryProcessInstance".equals(action))
        {
        	queryProcessInstance();
        }
        else if ("updateUser".equals(action))
        {
            updateUser(xmlDoc);
        }
    	return xmlDoc;
    }
    
    private final void queryProcessInstance() {
        PlatformDao dao = null;
    	 try
         {
         	dao = new PlatformDao() ;
         	ArrayList bvals = new ArrayList();
         	ApplicationContextHelper.getBean("repositoryService");
        	StringBuffer sqlBuf = new StringBuffer("select distinct RES.* from ACT_RE_PROCDEF RES order by RES.DEPLOYMENT_ID_ desc  ");
	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
	      // List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
	        
	        dao.setSql(sqlBuf.toString()) ;
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
            
	      //  for (ProcessDefinition processDefinition : processDefinitionList) {
	         //   String deploymentId = processDefinition.getDeploymentId();
	           // Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	           // objects.add(new Object[]{processDefinition, deployment});
	        //}	
         }
    	 catch (Exception e)
         {
             e.printStackTrace() ;
             xmlDocUtil.setResult("-1");
         }
         finally
         {
             dao.releaseConnection();
         }
	}
    
    
    
    private final void queryUser()
    {
        Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
    	String curUsercode = session.getChildText("usercode");
    	String curDeptid = session.getChildText("deptid");
    	
        String query = reqData.getChildTextTrim("query");
        String usertype = reqData.getChildText("usertype");
        
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao() ;
        	ArrayList bvals = new ArrayList();
        	
        	StringBuffer sqlBuf = new StringBuffer("select a.*,d.deptid,d.deptcode,d.deptname from pcmc_user a ")
	    		.append("left join (select c.userid,b.* from pcmc_dept b,pcmc_user_dept c ")
	    		.append("where b.deptid=c.deptid and c.state=?");
        	bvals.add("1");
        	if(!PcmcUtil.isSysManager(curUsercode))
        	{
        		sqlBuf.append(" and c.deptid=?");
        		bvals.add(curDeptid);
        	}
        	sqlBuf.append(") d on a.userid=d.userid ")
	    		.append("where 1=1");
        	if (StringUtil.isNotEmpty(query))
        	{
        		sqlBuf.append(" and (a.username like ? or a.usercode like ? or a.mobile like ?)");
        		bvals.add("%"+query+"%");
                bvals.add(query+"%");
                bvals.add("%"+query+"%");
        	}
        	if(StringUtil.isNotEmpty(usertype))
        	{
        		sqlBuf.append(" and a.usertype=?");
        		bvals.add(usertype);
        	}
            sqlBuf.append(xmlDocUtil.getOrderBy(" order by a.usercode"));
            
            dao.setSql(sqlBuf.toString()) ;
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.setResult("-1");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

	private final void addUser()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String createuser = session.getChildText("userid");
        String deptid = dataElement.getChildTextTrim("deptid");
        if (StringUtil.isEmpty(deptid)){
        	deptid = session.getChildText("deptid");
        }
        String usercode = dataElement.getChildTextTrim("usercode");
        List roles = dataElement.getChildren("roleid");
        String urole = dataElement.getChildTextTrim("urole");
        if(null==urole) urole="";
        List rlist = new ArrayList();
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao();
        	for (int i=0;i<roles.size();i++) {
                Element role = (Element)roles.get(i);
                rlist.add(role.getText());
        	}
        	StringTokenizer st = new StringTokenizer(urole,",");
        	while(st.hasMoreTokens())
        	{
        		String role = st.nextToken();
        		if(StringUtil.isNotEmpty(role))
        		{
        			rlist.add(role);
        		}
        	}
            StringBuffer sql = new StringBuffer("select * from pcmc_user where usercode=?");
            ArrayList uList = new ArrayList();
            uList.add(usercode);
            dao.setSql(sql.toString());
            dao.setBindValues(uList);
            Element tmp = dao.executeQuerySql(-1,1);
            if (tmp.getChildren("Record").size()>0) {
                xmlDocUtil.writeErrorMsg("10607","用户名已经存在");
                xmlDocUtil.setResult("-1");
                return;
            }
            Element userRec = ConfigDocument.createRecordElement("pcmc","pcmc_user");
            XmlDocPkgUtil.copyValues(dataElement,userRec,0);
            XmlDocPkgUtil.setChildText(userRec, "userpwd", PasswordEncoder.encode(dataElement.getChildTextTrim("userpwd")));
            XmlDocPkgUtil.setChildText(userRec, "createuser", createuser);
            XmlDocPkgUtil.setChildText(userRec, "state", "1");
            
            
            dao.beginTransaction();
            /**
            List pkList = dao.insertOneRecord(ele);
            Object userid = ((Object[])pkList.get(0))[1];
            */
            String userid = dao.insertOneRecordSeqPk(userRec).toString();
            
            Element userDeptRec = ConfigDocument.createRecordElement("pcmc","pcmc_user_dept");
            XmlDocPkgUtil.copyValues(dataElement,userDeptRec,0);
            XmlDocPkgUtil.setChildText(userDeptRec, "deptid", deptid);
            XmlDocPkgUtil.setChildText(userDeptRec, "userid", userid);
            XmlDocPkgUtil.setChildText(userDeptRec, "state", "1");
            XmlDocPkgUtil.setChildText(userDeptRec, "indate", DatetimeUtil.getNow(""));
            dao.insertOneRecordSeqPk(userDeptRec);
            
            if(0<rlist.size())
            {
            	dao.setSql("insert into pcmc_user_role values(?,?,?)");
                for (int i=0;i<rlist.size();i++) {
	                String role = (String)rlist.get(i);
	                long seq = DBOprProxy.getNextSequenceNumber("pcmc_user_role");
	                ArrayList bvals = new ArrayList();
	                bvals.add(new Long(seq));
	                bvals.add(userid);
	                bvals.add(SqlTypes.getConvertor("long").convert(role,null));
	                dao.addBatch(bvals);
	            }
                dao.executeBatch();
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增用户成功");
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "userid", ""+userid);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
         //   log4j.logError("[新增用户发生异常.]"+e.getMessage());
           // log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增用户失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private final void updateUser(Document xmlDoc)
    {
        Element dataElement = xmlDocUtil.getRequestData();
        PlatformDao dao = null;
        String userid = dataElement.getChildTextTrim("userid");
        String userpwd = dataElement.getChildTextTrim("userpwd");
        String userpwd1 = dataElement.getChildTextTrim("userpwd1");
        List roles = dataElement.getChildren("roleid");
        String urole = dataElement.getChildTextTrim("urole");
        List rlist = new ArrayList();
        StringBuffer sql = new StringBuffer("");
        try
        {
        	dao = new PlatformDao();
        	for (int i=0;i<roles.size();i++) {
                Element role = (Element)roles.get(i);
                rlist.add(role.getText());
        	}
        	StringTokenizer st = new StringTokenizer(urole,",");
        	while(st.hasMoreTokens())
        	{
        		String role = st.nextToken();
        		if(StringUtil.isNotEmpty(role))
        		{
        			rlist.add(role);
        		}
        	}
            dao.beginTransaction();
            if (StringUtil.isNotEmpty(userpwd))
            {
                sql.append("select * from pcmc_user where userid=?");
                ArrayList bvals = new ArrayList();
                bvals.add(userid);
                dao.setSql(sql.toString());
                dao.setBindValues(bvals);
                Element resultElement = null;
                resultElement = dao.executeQuerySql(-1,1);
                String dbuserpwd = Crypto.decodeByKey(Constants.DEFAULT_KEY,resultElement.getChild("Record").getChildTextTrim("userpwd"));
                if (!(userpwd.equals((dbuserpwd))))
                {
                    xmlDocUtil.setResult("1");
                    xmlDocUtil.writeErrorMsg("10611","原密码不正确");
                    return;
                }
            }
            //else
            {
                Element ele = ConfigDocument.createRecordElement("pcmc","pcmc_user");
                XmlDocPkgUtil.copyValues(dataElement,ele,0,true);
                if(StringUtil.isEmpty(userpwd) || StringUtil.isEmpty(userpwd1))
                {
                	ele.removeChild("userpwd");
                }
                else
                {
                	ele.getChild("userpwd").setText(Crypto.encodeByKey(Constants.DEFAULT_KEY,userpwd1));
                }
                dao.updateOneRecord(ele);
            }
            if (rlist.size()>0) {
                sql.setLength(0);
                sql.append("delete from pcmc_user_role where userid=?");
                ArrayList bvals = new ArrayList();
                bvals.add(userid);
                dao.setSql(sql.toString());
                dao.setBindValues(bvals);
                dao.executeTransactionSql();
                dao.setSql("insert into pcmc_user_role values(?,?,?)");
                for (int i = 0; i < rlist.size(); i++) {
                    String role = (String) rlist.get(i);
                    long seq = DBOprProxy.getNextSequenceNumber("pcmc_user_role");
                    bvals.clear();
                    bvals.add(new Long(seq));
                    bvals.add(userid);
                    bvals.add(SqlTypes.getConvertor("long").convert(role, null));
                    dao.addBatch(bvals);
                }
                dao.executeBatch();
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改用户信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
           // log4j.logError("[更新用户发生异常.]"+e.getMessage());
          //  log4j.logInfo(JDomUtil.toXML(xmlDoc, "GBK", true));
            xmlDocUtil.writeErrorMsg("10610","修改用户信息失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    /**
     * 删除操作员,同时删除操作员角色表
     * */
    private final void deleteUser()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        List userList = dataElement.getChildren("userid");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update pcmc_user set state=? where userid=?");
            for (int i=0;i<userList.size();i++) {
            	Element userid = (Element)userList.get(i);
                ArrayList<String> bvals = new ArrayList<String>();                
                bvals.add("9");
                bvals.add(userid.getText());
                
                dao.setSql(sql.toString());
                dao.setBindValues(bvals);
                dao.executeTransactionSql();
            }
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603","删除用户成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
          //  log4j.logError("[删除用户发生错误]"+e.getMessage()) ;
           // log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除用户失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    private final void getUserDetail()
    {
        PlatformDao dao = null;
        Element dataElement = xmlDocUtil.getRequestData();
        String userid = dataElement.getChildTextTrim("userid");

        Element masterData;
        Element detailData;
        try
        {
        	dao = new PlatformDao();
        	//取主表结果
            StringBuffer sql = new StringBuffer("select a.*,")
            	.append("b.deptcode,b.deptname,'' as urole from pcmc_user a,pcmc_dept b where a.deptid = b.deptid ") ;
            sql.append("and a.userid = " + userid) ;
            dao.setSql(sql.toString()) ;
            masterData = dao.executeQuerySql( -1, 1) ;
            sql.setLength(0) ;
            sql.append("select a.*,b.cnname from pcmc_role a left join pcmc_subsys b on b.subsysid=a.subsysid ")
            	.append("where roleid in (select roleid from pcmc_user_role where userid=").append(userid).append(") ")
            	.append("order by b.cnname,a.rolename");
            dao.setSql(sql.toString()) ;
            detailData = dao.executeQuerySql( -1, 1) ;
            Element mRecord = masterData.getChild("Record");
            mRecord.addContent(detailData);
            //masterData.removeChild("PageInfo") ;
            //masterData.addContent( (Element) detailData.getChild("PageInfo").clone()) ;
            List rlist = detailData.getChildren("Record");
            StringBuffer urole = new StringBuffer();
            for(int i=0; i<rlist.size(); i++)
            {
            	Element rec = (Element)rlist.get(i);
            	urole.append(rec.getChildText("roleid")).append(",");
            }
            XmlDocPkgUtil.setChildText(mRecord, "urole", urole.toString());
            xmlDocUtil.getResponse().addContent(masterData) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.setResult("-1");
         //   log4j.logError(e.getMessage()) ;
         //   log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    /**
     * 已被覆盖,使用基本交易
     *
     */
    private final void getUserList()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String deptid = dataElement.getChildTextTrim("deptid") ;
        String usercode = dataElement.getChildTextTrim("usercode") ;
        String username = dataElement.getChildTextTrim("username") ;
        String deptname = dataElement.getChildTextTrim("deptname");
        
        String deptcode = (String)xmlDocUtil.getSession().getChildText("deptcode");
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao() ;
        	StringBuffer sql = new StringBuffer() ;
        	ArrayList bvals = new ArrayList();
            sql.append(" select a.*,b.deptname,b.deptcode from pcmc_user a,pcmc_dept b where a.deptid = b.deptid ") ;
            if (StringUtil.isNotEmpty(deptid))
            {
                sql.append("and a.deptid=? ") ;
                bvals.add(SqlTypes.getConvertor("long").convert(deptid,null));
            }
            if (StringUtil.isNotEmpty(usercode))
            {
                sql.append("and a.usercode=? ") ;
                bvals.add(usercode);
            }
            if (StringUtil.isNotEmpty(username))
            {
                sql.append(" and a.username like ?") ;
                bvals.add("%"+username+"%");
            }
            if (deptname != null)
            {
                sql.append(" and b.deptname like ?");
                bvals.add("%"+deptname+"%");
            }            
            
            if(deptcode != null){
            	//4403020代表市直属
            	int dclen = deptcode.length();

                if (dclen==4 || (dclen>=12 && deptcode.substring(4, 12).equals("00000000"))) {
                	//sql.append(" and ").append(dao.subStr()).append("(b.deptcode,1,4) ='").append(deptcode.substring(0, 4) + "'");
                	sql.append(" and b.deptcode like '").append(deptcode.substring(0, 4)).append("%'");
                }
                else if (dclen==6 || (dclen>=12 && deptcode.substring(6, 12).equals("000000"))) {
                	//sql.append(" and ").append(dao.subStr()).append("(b.deptcode,1,6) ='").append(deptcode.substring(0, 6) + "'");
                	sql.append(" and b.deptcode like '").append(deptcode.substring(0, 6)).append("%'");
                }
                else if (dclen==9 || (dclen>=12 && deptcode.substring(9, 12).equals("000"))) {
                	//sql.append(" and ").append(dao.subStr()).append("(b.deptcode,1,9) ='").append(deptcode.substring(0, 9) + "'");
                	sql.append(" and b.deptcode like '").append(deptcode.substring(0, 9)).append("%'");
                }
                else if (dclen==12)
                {
                	sql.append(" and deptcode like '").append(deptcode).append("'");
                }
                else {
                	sql.append(" and deptcode='").append(deptcode).append("'");
                }
            }
            sql.append(xmlDocUtil.getOrderBy(" order by a.usercode"));
            
            dao.setSql(sql.toString()) ;
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.setResult("-1");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void getRoleActExits()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String roleid = dataElement.getChildTextTrim("roleid");
        String oprid = dataElement.getChildTextTrim("__oprid");
        String action = dataElement.getChildTextTrim("action");
        StringBuffer sql = new StringBuffer() ;
        ArrayList bvals = new ArrayList();
        PlatformDao dao = new PlatformDao() ;
        try
        {
            sql.append(" select * from pcmc_role_act where roleid=? and oprid=? and action=? ") ;
            dao.setSql(sql.toString()) ;
            bvals.add(SqlTypes.getConvertor("long").convert(roleid,null));
            bvals.add(oprid);
            bvals.add(action);
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1) ;
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void getRoleList()
    {
        Element dataElement = xmlDocUtil.getRequestData();
        String sysname = dataElement.getChildTextTrim("__sysname") ;
        String userid = dataElement.getChildTextTrim("userid");        
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao();
        	ArrayList bvals = new ArrayList();
        	StringBuffer sql = new StringBuffer() ;
            sql.append(" select a.*,b.subsysid,b.shortname,b.cnname from pcmc_role a left join pcmc_subsys b on b.subsysid=a.subsysid ");
            sql.append(" where (createuser=? or roleid in (select roleid from pcmc_user_role");
            sql.append(" where userid=?)) ");
            bvals.add(userid);
            bvals.add(userid);
            if (StringUtil.isNotEmpty(sysname))
            {
                sql.append(" and b.shortname=? ") ;
                bvals.add(sysname);
            }
            sql.append(xmlDocUtil.getOrderBy(" order by b.cnname,a.rolename"));
            dao.setSql(sql.toString()) ;
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo()) ;
            xmlDocUtil.getResponse().addContent(resultElement) ;
            xmlDocUtil.setResult("0");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.writeErrorMsg("10601","查询角色列表异常");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    private final void getMRoleList()
    {        
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");
        String usercode = session.getChildTextTrim("usercode");
        
        PlatformDao dao = null;
        try
        {
        	dao = new PlatformDao();
        	StringBuffer sql = new StringBuffer() ;
            sql.append(" select distinct a.*,b.cnname from pcmc_role a, pcmc_subsys b ")
            	.append(" where b.subsysid=a.subsysid ");
        	if(!PcmcUtil.isSysManager(usercode))
        	{
        		sql.append("and (a.createuser=? or a.roleid in (select roleid from pcmc_user_role where userid=?)) ");
        	}
        	sql.append("order by b.cnname,a.rolename");
        	ArrayList bvals = new ArrayList();
        	bvals.add(userid);
        	bvals.add(userid);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1) ;
            
            List rList = resultElement.getChildren("Record");
            StringBuffer wSql = new StringBuffer("");
            bvals.clear();
            boolean mflag = false;
            for(int i=0; i<rList.size(); i++)
            {
            	Element rRec = (Element)rList.get(i);
            	String cu = rRec.getChildText("createuser");
            	String rid = rRec.getChildText("roleid");
            	String mr = rRec.getChildText("mrole");
            	if(userid.equals(cu))
            	{
            		wSql.append(mflag?",?":"(?");
            		bvals.add(SqlTypes.getConvertor("long").convert(rid, null));
            		mflag=true;
            	}
            	else if(null != mr)
            	{
            		java.util.StringTokenizer st = new java.util.StringTokenizer(mr,",");
    				while(st.hasMoreTokens())
    				{
    					rid = st.nextToken();
    					wSql.append(mflag?",?":"(?");
                		bvals.add(SqlTypes.getConvertor("long").convert(rid, null));
                		mflag=true;
    				}
            	}
            }
            if(bvals.size()>0)
            {
            	StringBuffer mSql = new StringBuffer();
            	mSql.append("select a.*,b.cnname from pcmc_role a, pcmc_subsys b where b.subsysid=a.subsysid ")
            		.append("and a.roleid in ").append(wSql).append(") ")
            		.append("order by b.cnname,a.rolename ");
            	
            	dao.setSql(mSql.toString());
            	dao.setBindValues(bvals);
                resultElement = dao.executeQuerySql(-1,1) ;
                
                xmlDocUtil.getResponse().addContent(resultElement) ;
            }
            
            xmlDocUtil.setResult("0") ;
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            xmlDocUtil.writeErrorMsg("10602","查询用户管理角色异常");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

   
    
    private final void changePwd()
    {
    	Element dataElement = xmlDocUtil.getRequestData();
        PlatformDao dao = null;
        String usercode = xmlDocUtil.getSession().getChildText("usercode");
        String userid = dataElement.getChildTextTrim("userid");
        String userpwd = dataElement.getChildTextTrim("userpwd");
        String userpwd1 = dataElement.getChildTextTrim("userpwd1");
        StringBuffer sql = new StringBuffer("");
        try
        {
        	dao = new PlatformDao();
            //管理员修改密码
            if (StringUtil.isNotEmpty(userpwd))
            {
            	ArrayList<String> bvals = new ArrayList<String>();
                sql.append("select * from pcmc_user where userid=?");
                bvals.add(userid);
                dao.setSql(sql.toString());
                dao.setBindValues(bvals);
                Element resultElement = null;
                resultElement = dao.executeQuerySql(-1,1);
                String dbuserpwd = Crypto.decodeByKey(Constants.DEFAULT_KEY,resultElement.getChild("Record").getChildTextTrim("userpwd"));
                if (!(userpwd.equals((dbuserpwd))))
                {
                    xmlDocUtil.setResult("1");
                    xmlDocUtil.writeErrorMsg("10615","原密码不正确");
                    return;
                }
            }
            else if(!PcmcUtil.isSysManager(usercode))
            {
            	xmlDocUtil.setResult("1");
                xmlDocUtil.writeErrorMsg("10615","原密码不正确");
                return;
            }
            if(StringUtil.isNotEmpty(userpwd1))
            {
            	sql.setLength(0);
            	sql.append("update pcmc_user set userpwd=? where userid=?");
            	ArrayList<String> bvals = new ArrayList<String>();
            	bvals.add(Crypto.encodeByKey(Constants.DEFAULT_KEY,userpwd1));
            	bvals.add(userid);
            	
            	dao.setSql(sql.toString());
            	dao.setBindValues(bvals);
            	dao.executeTransactionSql();
            }
            
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10613","修改密码成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
          //  log4j.logError("[更新用户发生异常.]"+e.getMessage());
          //  log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10614","修改密码失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    public static int userRoleIDQuery(StringBuffer buf)
    {
    	buf.append("(")
			.append("select roleid from pcmc_user_role where userid=? ")
			.append("union select roleid from pcmc_ustype_role ut inner join pcmc_user u on ut.usertype=u.usertype where u.userid=?")
			.append(")");
    	
    	return 2;
    	
    }
    
    private void getCurDetail()
    {
        Element session = xmlDocUtil.getSession();
        String curuser = session.getChildText("userid");
        try
        {        	
        	StringBuffer sqlBuf = new StringBuffer("select a.userid,a.usercode,a.username,a.nickname,")
        		.append("a.portrait,a.description,a.email,a.phone,a.mobile,")
        		.append("a.emailbind,a.mobilebind,a.gender,a.usertype,")
        		.append("b.pubname,b.pubmail,b.pubphone,")
        		.append("d.deptid,d.deptcode,d.deptname ")
        		.append("from pcmc_user a ")
        		.append("left join pcmc_user_ext b on a.userid = b.userid ")
	    		.append("left join (select c.userid,b.* from pcmc_dept b,pcmc_user_dept c ")
	    		.append("where b.deptid=c.deptid and c.state=?) d on a.userid=d.userid ")
	    		.append("where a.userid=?");
        	ArrayList bvals = new ArrayList();
        	bvals.add("1");
        	bvals.add(curuser);
        	Element data = DaoUtil.getOneRecordData(sqlBuf.toString(), bvals);
        	Element userRec = data.getChild("Record");
        	UriUtil.formatUri(userRec, "portrait");
        	
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(data);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        //	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10607", "查询当前用户信息错误");
        }
    }
}
