package cn.brightcom.system.pcmc.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.dialect.DataTypeInfo;
import cn.brightcom.jraf.dialect.Dialect;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;


/**
 * <p>Title: 数据库表维护</p>
 * <p>Description: 数据库表维护</p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class TableDesigner
{
	private Log log = new Log(TableDesigner.class.getName());
	
	private XmlDocPkgUtil xmlDocUtil = null;
	//private String[] tbl_flds = {"name","title","type","maxlen","minlen","notnull","pk","gen"};
	
    public Document doPost(Document xmlDoc)
    {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        
        if("alltables".equals(action))
        {
        	getAllTables();
        }
        else if("droptable".equals(action))
        {
        	dropTable();
        }
        else if("datatypes".equals(action))
        {
        	getDataTypes();
        }
        else if("tablecolumns".equals(action))
        {
        	getTableColumns();
        }
        else if("dbXMLcolumns".equals(action))
        {
        	getDbXMlColumns();
        }
        else if("dropcol".equals(action))
        {
        	dropColumn();
        }
        else if("uptcol".equals(action))
        {
        	updateColumn();
        }
        else if("addcol".equals(action))
        {
        	addColumn();
        }
                
        return xmlDoc;
    }
    
    private final void getAllTables()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
    		
    		pDao.setSql(pDao.getDialect().getAllTablesSql());
    		Element resData = pDao.executeQuerySql(-1, 1);
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10202","查询数据库表异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void getTableColumns()
    {
    	try
    	{
    		Element resData = queryColumns();
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10205","查询表字段异常");
    	}
    } 
    
    private final void getDbXMlColumns()
    {
    	try
    	{
    		Element resData = getDbXMlDetail();
    		
    		xmlDocUtil.setResult("0"); 
    		xmlDocUtil.getResponse().addContent(resData);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10213","查询系统Table字段定义异常");
    	}
    }
    
    private final void getDataTypes()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
    		
            Element data = new Element("Data");
        	Element meta = new Element("Metadata");
        	Element fields = new Element("Fields");
        	fields.addContent(new Element("Field").setAttribute("fieldname", "data_type"));
        	data.addContent(meta);
        	meta.addContent(fields);
        	String[] types = pDao.getDialect().getDataTypes();
        	if(null != types)
        	{
        		for(int i=0; i<types.length; i++)
        		{
        			Element record = new Element("Record");
        			record.addContent(new Element("data_type").setText(types[i]));
                	data.addContent(record);
        		}
        	}
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.getResponse().addContent(data);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10201","取数据类型异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void dropTable()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		String table_name = reqData.getChildText("table_name");
    		String owner = reqData.getChildText("owner");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
    		pDao.setSql(pDao.getDialect().dropTable(owner, table_name));
    		pDao.executeTransactionSql();
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10203","删除数据表成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10204","删除数据表异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void dropColumn()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		String tblname = reqData.getChildText("__table_name");
    		String owner = reqData.getChildText("__owner");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
            pDao.beginTransaction();
            Dialect dialect = pDao.getDialect();            
            
            Element qColData = queryColumns();
            List qList = qColData.getChildren("Record");
            List reqList = reqColumnRecs();
            
            HashMap map = mergeColumns(reqList, qList, "drop", dialect);
            int pkFlag = ((Integer)map.get("pkFlag")).intValue();
            String oldCons_name = (String)map.get("oldCons_name");
            //删除主键约束
            if(11 == pkFlag)
            {
            	pDao.setSql(dialect.dropTableConstraint(owner, tblname, oldCons_name));
            	pDao.executeTransactionSql();
            }
            //删除字段
            List colList = (List)map.get("colList");
            for(int i=0; i<colList.size();i++)
            {
            	Element r = (Element)colList.get(i);
            	String colname = r.getChildText("column_id");
            	pDao.setSql(dialect.dropColumn(owner, tblname, colname));
            	pDao.executeTransactionSql();
            }
            //添加主键约束
            List pkList = (List)map.get("pkList");
            if((10==pkFlag||11==pkFlag) && 0<pkList.size())
            {
            	pDao.setSql(dialect.alterTablePk(owner, tblname, oldCons_name, pkList));
            	pDao.executeTransactionSql();
            }
    		
    		pDao.commitTransaction();
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10207","删除数据表字段成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10208","删除数据表字段异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void updateColumn()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		String tblname = reqData.getChildText("__table_name");
    		String owner = reqData.getChildText("__owner");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
            pDao.beginTransaction();
            Dialect dialect = pDao.getDialect();
            
            Element qColData = queryColumns();
            List qList = qColData.getChildren("Record");
            List reqList = reqColumnRecs();
            
            HashMap map = mergeColumns(reqList, qList, "update", dialect);
            int pkFlag = ((Integer)map.get("pkFlag")).intValue();
            String oldCons_name = (String)map.get("oldCons_name");
            //删除主键约束
            if(11 == pkFlag)
            {
            	pDao.setSql(dialect.dropTableConstraint(owner, tblname, oldCons_name));
            	pDao.executeTransactionSql();
            }
            //修改字段
            List colList = (List)map.get("colList");
            for(int i=0; i<colList.size();i++)
            {
            	Element r = (Element)colList.get(i);
            	pDao.setSql(dialect.midifyColumn(owner, tblname, r));
            	pDao.executeTransactionSql();
            }
            //添加主键约束
            List pkList = (List)map.get("pkList");
            if((10==pkFlag||11==pkFlag) && 0<pkList.size())
            {
            	pDao.setSql(dialect.alterTablePk(owner, tblname, oldCons_name, pkList));
            	pDao.executeTransactionSql();
            }
            //字段描述
            List descList = (List)map.get("descList");
            for(int i=0; i<descList.size(); i++)
            {
            	Element rec = (Element)descList.get(i);
            	String colname = rec.getChildText("column_name");
            	String coldesc = rec.getChildText("column_desc");
            	String desc_name = rec.getChildText("__desc_name");
            	if(StringUtil.isEmpty(desc_name))
            		dialect.addColumnComment(owner, tblname, colname, coldesc);
            	else
            		dialect.updateColumnComment(owner, tblname, colname, coldesc);
            }            
    		
    		pDao.commitTransaction();
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10211","修改数据表字段成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10212","修改数据表字段异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final void addColumn()
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		String tblname = reqData.getChildText("__table_name");
    		String owner = reqData.getChildText("__owner");
    		String newtblflag = reqData.getChildText("__newtblflag");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
            pDao.beginTransaction();
            Dialect dialect = pDao.getDialect();
            
            List qList = null;
            if(!"true".equals(newtblflag))
            {
            	Element qColData = queryColumns();
            	qList = qColData.getChildren("Record");
            }
            List reqList = reqColumnRecs();            
            HashMap map = mergeColumns(reqList, qList, "add", dialect);
            
            List colList = (List)map.get("colList");
            int pkFlag = ((Integer)map.get("pkFlag")).intValue();
            String oldCons_name = (String)map.get("oldCons_name");
            //新建表
            if("true".equals(newtblflag))
            {
            	pDao.setSql(dialect.createTable(owner, tblname, colList));
            	pDao.executeTransactionSql();
            	
            	String tbldesc = reqData.getChildText("__table_desc");
            	if(StringUtil.isNotEmpty(tbldesc))
            		dialect.addTableComment(owner, tblname, tbldesc);
            }
            else
            {
                //删除主键约束
                if(11 == pkFlag)
                {
                	pDao.setSql(dialect.dropTableConstraint(owner, tblname, oldCons_name));
                	pDao.executeTransactionSql();
                }
                //新增字段
                pDao.setSql(dialect.addColumn(owner, tblname, colList));
                pDao.executeTransactionSql();
            }
            
            //添加主键约束
            List pkList = (List)map.get("pkList");
            if((10==pkFlag||11==pkFlag) && 0<pkList.size())
            {
            	pDao.setSql(dialect.alterTablePk(owner, tblname, oldCons_name, pkList));
            	pDao.executeTransactionSql();
            }
            //字段描述
            List descList = (List)map.get("descList");
            for(int i=0; i<descList.size(); i++)
            {
            	Element rec = (Element)descList.get(i);
            	String colname = rec.getChildText("column_name");
            	String coldesc = rec.getChildText("column_desc");
            	dialect.addColumnComment(owner, tblname, colname, coldesc);
            }
    		
    		pDao.commitTransaction();
    		
    		Element resData = new Element("Data");
    		resData.addContent(colList);
    		xmlDocUtil.getResponse().addContent(resData);
    		
    		xmlDocUtil.setResult("0");
    		xmlDocUtil.writeHintMsg("10209","新增数据表字段成功");
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		log.logError(ex);
    		xmlDocUtil.setResult("-1");
    		xmlDocUtil.writeErrorMsg("10210","新增数据表字段异常");
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    private final Element queryColumns()
    	throws Exception
    {
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
    	try
    	{
    		String sysName = reqData.getChildText("__sysname");
    		String table_name = reqData.getChildText("__table_name");
    		String owner = reqData.getChildText("__owner");
    		//注册子系统名
    		ApplicationContext.regSubSys(sysName);
            pDao = new PlatformDao();
    		
    		pDao.setSql(pDao.getDialect().getColumnsByTblSql());
    		ArrayList bvals = new ArrayList();
    		bvals.add(table_name);
    		bvals.add(owner);
    		pDao.setBindValues(bvals);
    		Element resData = pDao.executeQuerySql(-1, 1);
    		return resData;
    	}
    	finally
    	{
    		pDao.releaseConnection();
    	}
    }
    
    /**
     * edit by zhangj 
     * end dada:2010-06-20
     * 获取数据库表字段（名称、描述、类型、最长、是否主键、是否为空）以及db-xml中原有部分字段（最短、自动生成）
     */
    private final Element getDbXMlDetail()
    {
    	Element xmlreqData = xmlDocUtil.getRequestData();
    	String sysName = xmlreqData.getChildText("__sysname");
		String tblName = xmlreqData.getChildText("__table_name");
		String __owner = xmlreqData.getChildText("__owner");
    	Element reqData = null ;
    	
    	//获取数据库表字段描述
    	Document qxmlDoc = HttpProcesser.createRequestPackage("pcmc", "tbldesigner", "tablecolumns");
    	qxmlDoc.getRootElement().getChild("Request").getChild("Data").addContent((new Element("__sysname")).setText(sysName));
    	qxmlDoc.getRootElement().getChild("Request").getChild("Data").addContent((new Element("__table_name")).setText(tblName));
    	qxmlDoc.getRootElement().getChild("Request").getChild("Data").addContent((new Element("__owner")).setText(__owner));
		
//        SessionFactory.getSession().put("Ext.loadFields.response", "true");
		Document rxmlDoc = SwitchCenter.doPost(qxmlDoc,true);
		XmlDocPkgUtil qxmlDocUtil = new XmlDocPkgUtil(rxmlDoc);
		

		//获取db-xml中部分字段配置（若有该db-xml）
    	Document xmlqxmlDoc = HttpProcesser.createRequestPackage("pcmc", "sys", "getTblDetail");

    	xmlqxmlDoc.getRootElement().getChild("Request").getChild("Data").addContent((new Element("__sysName")).setText(sysName));
    	xmlqxmlDoc.getRootElement().getChild("Request").getChild("Data").addContent((new Element("__tblName")).setText(tblName));
		Document xmlrxmlDoc = SwitchCenter.doPost(xmlqxmlDoc,true);
		XmlDocPkgUtil xmlqxmlDocUtil = new XmlDocPkgUtil(xmlrxmlDoc);
		Element xmlmetaFlds = null;
		if("0".equals(xmlqxmlDocUtil.getResult()))
		{
			 xmlmetaFlds = xmlqxmlDocUtil.getResponse()
				.getChild("Data");		
		}
		Element resdata = null;
		if("0".equals(qxmlDocUtil.getResult()))
		{
			resdata = (Element)qxmlDocUtil.getResponse()
				.getChild("Data").clone();
			Element metaEle = resdata.getChild("Metadata");
			metaEle.getChild("Fields").addContent(XmlDocPkgUtil.createFieldEle("gen"));
			metaEle.getChild("Fields").addContent(XmlDocPkgUtil.createFieldEle("minlen"));
			
			List recList = resdata.getChildren("Record");
			ApplicationContext.regSubSys(sysName);
			PlatformDao pDao=new PlatformDao();
			for(int i=0;i<recList.size();i++)
			{
				Element rec2 = null;
				Element rec = (Element)recList.get(i);
				try{
					XPath xpath = XPath.newInstance("Record[name=\""+rec.getChildText("column_name").toLowerCase()+"\"]");
					rec2 = (Element)xpath.selectSingleNode(xmlmetaFlds);
				}
				catch(Exception e){
					
				}
				XmlDocPkgUtil.setChildText(rec,"data_type",pDao.getDialect().getJrafTypeByDataType(rec.getChildText("data_type")));
				if(null!=rec2){
					String type = 	rec2.getChildText("type");
					String title = rec2.getChildText("title");
					if(StringUtil.isNotEmpty(type)){
						XmlDocPkgUtil.setChildText(rec,"data_type",type);				
					}
					if(StringUtil.isNotEmpty(title))
						XmlDocPkgUtil.setChildText(rec,"column_desc",title);
					XmlDocPkgUtil.setChildText(rec,"gen",rec2.getChildText("gen"),true);
					XmlDocPkgUtil.setChildText(rec,"minlen",rec2.getChildText("minlen"),true);
				}				
			}
			pDao.releaseConnection();
			//xmlDocUtil.getResponse().addContent(resdata);
		}
		
		return resdata;
    }
    
    private final HashMap mergeColumns(List reqList,List qList,String act,Dialect dialect)
    	throws Exception
    {
    	HashMap map = new HashMap();
    	ArrayList renList = new ArrayList();
    	ArrayList colList = new ArrayList();
    	ArrayList pkList = new ArrayList();
    	ArrayList descList = new ArrayList();
    	
    	boolean pkFlag = false;
    	boolean oldPk = false;
    	String oldCons_name = null;
    	//新建表
		if(null == qList || 0 == qList.size())
    	{
    		colList.addAll(reqList);
    		for(int i=0; i<reqList.size();i++)
    		{
    			Element rec = (Element)reqList.get(i);
    			String column_id = rec.getChildText("column_id");
    			String ispk = rec.getChildText("ispk");
    			String column_desc = rec.getChildText("column_desc");
    			if("true".equals(ispk))
    			{
    				pkList.add(column_id);
    				pkFlag = true;
    			}
    			if(StringUtil.isNotEmpty(column_desc))
    			{
    				descList.add(rec);
    			}
    		}
    	}
    	else
    	{
    		//已有主键
    		for(int i=0; i< qList.size(); i++)
    		{
    			Element rec = (Element)qList.get(i);
    			String column_id = rec.getChildText("column_id");
    			String ispk = rec.getChildText("ispk");
    			if("true".equals(ispk))
    			{
    				pkList.add(column_id);
    				oldPk = true;
    				oldCons_name = rec.getChildText("constraint_name");
    			}
    		}
    		for(int i=0; i<reqList.size();i++)
    		{
    			Element rec = (Element)reqList.get(i);
    			String column_id = rec.getChildText("column_id");
    			String column_name = rec.getChildText("column_name");
    			String ispk = rec.getChildText("ispk");
    			String column_desc = rec.getChildText("column_desc");
    			//删除字段
    			if("drop".equals(act))
    			{
    				colList.add(rec);
    				int idx = pkList.indexOf(column_id);
    				if(-1!=idx)
        			{
        				pkList.remove(idx);
        				pkFlag = true;
        			}
    				continue;
    			}
    			//修改字段名
    			if(!column_id.equals(column_name))
    			{
    				renList.add(rec);
    			}
    			//新增字段
    			if("add".equals(act))
    			{
    				colList.add(rec);
    				if("true".equals(ispk))
        			{
        				pkList.add(column_id);
        				pkFlag = true;
        			}
        			if(StringUtil.isNotEmpty(column_desc))
        			{
        				descList.add(rec);
        			}
    			}
    			//修改字段
    			else
    			{
    				String data_type = rec.getChildText("data_type");
    				String data_width = rec.getChildText("data_width");
    				String data_scale = rec.getChildText("data_scale");
    				String allownull = rec.getChildText("allownull");
    				String data_default = rec.getChildText("data_default");
    				for(int j=0; j<qList.size(); j++)
    				{
    					Element qRec = (Element)qList.get(j);
    					String q_column_id = qRec.getChildText("column_id");
    					if(!column_id.equals(q_column_id))
    						continue;
    	    			String q_ispk = qRec.getChildText("ispk");
    	    			String q_desc_name = qRec.getChildText("desc_name");
    	    			String q_column_desc = qRec.getChildText("column_desc");
    	    			
    	    			String q_data_type = qRec.getChildText("data_type");
        				String q_data_width = qRec.getChildText("data_width");
        				String q_data_scale = qRec.getChildText("data_scale");
        				String q_allownull = qRec.getChildText("allownull");
        				String q_data_default = qRec.getChildText("data_default");
        				
        				DataTypeInfo dfinfo = dialect.getDataTypeInfoByDataType(data_type);
        				
        				//是否修改字段
        				if(!data_type.equals(q_data_type)
        					|| !allownull.equals(q_allownull)
        					|| !data_default.equals(q_data_default)
        					|| (dfinfo.len && !data_width.equals(q_data_width))
        					|| (dfinfo.scale && !data_scale.equals(q_data_scale))
        				)
        				{
        					colList.add(rec);
        				}
        				//主键变更
        				if("true".equals(ispk) && "false".equals(q_ispk))
        				{
        					pkList.add(column_id);
        					pkFlag = true;
        				}
        				else if("false".equals(ispk) && "true".equals(q_ispk))
        				{
        					int idx = pkList.indexOf(column_id);
            				pkList.remove(idx);
                			pkFlag = true;
        				}
        				if(!column_desc.equals(q_column_desc))
        				{
        					XmlDocPkgUtil.setChildText(rec, "__desc_name", q_desc_name);
        					descList.add(rec);
        				}
    				}
    			}
    		}
    	}
		int pk = 0;
		if(oldPk) pk++;
		if(pkFlag) pk=pk+10;
		map.put("pkFlag", new Integer(pk));
		map.put("oldCons_name",oldCons_name);
		map.put("renList", renList);
		map.put("colList", colList);
		map.put("pkList", pkList);
		map.put("descList", descList);
		
		return map;
    }
    
    private final ArrayList reqColumnRecs()
    {
    	ArrayList list = new ArrayList();
    	Element reqData = xmlDocUtil.getRequestData();
    	
    	List cList = reqData.getChildren("column_name");
		List idList = reqData.getChildren("column_id");
		int size=cList.size();
		if(0==size) size = idList.size();
    	for(int i=0; i<size; i++)
    	{
    		Element record = new Element("Record");
    		String colname = "";
    		if(0<cList.size())
    			colname = ((Element)cList.get(i)).getText();
    		String colid = colname;
    		if(0<idList.size())
    		{
    			colid = ((Element)idList.get(i)).getText();
    		}
    		XmlDocPkgUtil.setChildText(record, "column_id",colid);
    		XmlDocPkgUtil.setChildText(record, "column_name",colname);
    		setColumn(record,reqData,"data_type",i);
    		setColumn(record,reqData,"data_width",i);
    		setColumn(record,reqData,"data_scale",i);
    		setColumn(record,reqData,"allownull",i);
    		setColumn(record,reqData,"data_default",i);
    		setColumn(record,reqData,"ispk",i);
    		setColumn(record,reqData,"column_desc",i);
    		
    		list.add(record);
    	}
    	
    	return list;
    }
    
    private final void setColumn(Element record,Element reqData,String name,int i)
    {
    	List list = reqData.getChildren(name);
    	String val = "";
    	if(0<list.size()) val = ((Element)list.get(i)).getText();
    	XmlDocPkgUtil.setChildText(record, name, val);
    }
}
