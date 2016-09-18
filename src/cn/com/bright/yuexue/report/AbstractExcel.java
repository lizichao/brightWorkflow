package cn.com.bright.yuexue.report;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.Configuration;
import cn.brightcom.jraf.conf.MenuViewConfiguration;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;


public abstract class AbstractExcel {
	public  Configuration menuViewConfig = MenuViewConfiguration.getXMLConfiguration();
	public static Log log4j = new Log(AbstractExcel.class.getName());

         /**
          * ����EXLEÿ������
          *  @return 
		  */
		public abstract void saveExcle(Element reqData)throws Exception;
		/**
		 * ѧ��������
		 * @param c_pooluuid
		 * @param filenm
		 * @return
		 * @throws Exception
		 */
		public Sheet parseExcel(String c_pooluuid,String filenm)throws Exception{
			WorkbookSettings ws = new WorkbookSettings();
		    ws.setLocale(new Locale("zh", "CN"));
		    ws.setEncoding("ISO-8859-1");
		    Workbook wb = Workbook.getWorkbook(new File(filenm), ws);
		    Sheet sheet = wb.getSheet("ѧ��������Ϣ");
		    return sheet;
		}
		/**
		 * ��ʦ������
		 * @param c_pooluuid
		 * @param filenm
		 * @return
		 * @throws Exception
		 */
		public Sheet parseTacherExcel(String c_pooluuid,String filenm)throws Exception{
			WorkbookSettings ws = new WorkbookSettings();
		    ws.setLocale(new Locale("zh", "CN"));
		    ws.setEncoding("ISO-8859-1");
		    Workbook wb = Workbook.getWorkbook(new File(filenm), ws);
		    Sheet sheet = wb.getSheet("��ʦ��Ϣ¼��");
		    return sheet;
		}
		/**
		 * ��ʦ������
		 * @param c_pooluuid
		 * @param filenm
		 * @return
		 * @throws Exception
		 */
		public Sheet parseScoreExcel(String c_pooluuid,String filenm)throws Exception{
			WorkbookSettings ws = new WorkbookSettings();
		    ws.setLocale(new Locale("zh", "CN"));
		    ws.setEncoding("ISO-8859-1");
		    Workbook wb = Workbook.getWorkbook(new File(filenm), ws);
		    Sheet sheet = wb.getSheet("ѧ���ɼ�¼��");
		    return sheet;
		}
		
		/**
		 * ת����ȡ���뼯
		 * @param paramName 
		 * @param paramMean
		 * @return String
		 * @throws Exception
		 */
		public  static String convesionStr(String paramName, String paramMean) {
			PlatformDao pDao = null;
			String str = null;
			try {
				pDao = new PlatformDao(true);
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("SELECT pd.PARAMMEANINGS,pd.PARAMCODE FROM param_detail pd, param_master pm ");
				sqlBuf.append("WHERE pd.PARAMID = pm.PARAMID AND pm.paramname =? and pd.PARAMMEANINGS=?");
				ArrayList bvals = new ArrayList();
				bvals.add(paramName);
				bvals.add(paramMean);
				pDao.setSql(sqlBuf.toString());
				pDao.setBindValues(bvals);
				Element resData = pDao.executeQuerySql(-1,1);
				List resDataList=resData.getChildren("Record");
			    if(resDataList.size()>0){
			    	Element data =(Element) resDataList.get(0);
			    	str = data.getChildText("paramcode");
			    }
			} catch (Exception ex) {
				ex.printStackTrace();
				log4j.logError(ex);
			} finally {
				pDao.releaseConnection();
			}
			return str;
		}
		
		/**
		 * �����ļ�����״̬
		 * @return
		 * @throws Exception
		 */
		public synchronized void upFlag(String doc_id, String err) {
			if (null == doc_id) return;
			PlatformDao pDao = null;
			try {
				pDao = new PlatformDao(true);
				ArrayList bvals = new ArrayList();
				bvals.add(err);
				bvals.add(doc_id);
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("update base_doc set doc_status='Y',doc_remark=? where doc_id=?");
				pDao.setSql(sqlBuf.toString());
				pDao.setBindValues(bvals);
				pDao.executeTransactionSql();
			} catch (Exception ex) {
				ex.printStackTrace();
				log4j.logError(ex);
			} finally {
				pDao.releaseConnection();
			}
		}
		
		/**
		 * ��⵼���ļ������Ƿ��ظ�
		 * @param tabName���� par�� valueֵ
		 * @return
		 * @throws Exception
		 */
		public List getExlElement(String str){
			Document doc = (Document)menuViewConfig.getConfig();
			List propsList = doc.getRootElement().getChild(str+"Exl").getChildren();
            return propsList;
		}
	
		/**
		 * ��⵼���ļ������Ƿ��ظ�
		 * @param tabName���� par�� valueֵ
		 * @return
		 * @throws SQLException 
		 * @throws Exception
		 */
		public boolean checkRow(String tabName,String params,String value) throws SQLException {
			PlatformDao pDao = null;
			try {
				pDao = new PlatformDao(true);
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append("select * from "+tabName+" where 1=1 ");
				ArrayList bvals = new ArrayList();
				if(!"".equals(value)){
					sqlBuf.append(" and "+params+" =?");
					bvals.add(value);
				}
				pDao.setSql(sqlBuf.toString());
				pDao.setBindValues(bvals);

				Element data = pDao.executeQuerySql(1, 1);
				return data.getChild("Record") == null;
			} finally {
				pDao.releaseConnection();
			}
		}
		
}
