package cn.com.bright.yuexue.report;

import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;

public class ExcelImport {
	private static ExcelImport instance = null; 
    private static Object classLock = ExcelImport.class;
    public  String operType = "";
    public static ExcelImport getInstance(){
    	synchronized(classLock){
			if (instance==null){
				instance = new ExcelImport();
			}
			return instance;
    	}
    }
		
	private static Log log4j = new Log(ExcelImport.class.getName());
	
	
	/**
	 * 查询未处理文件
	 * @return
	 * @throws Exception
	 */
	private Element queryData() throws Exception {
		PlatformDao pDao = null;
		try {
			pDao = new PlatformDao(true);
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("SELECT * FROM base_doc WHERE doc_status='N' AND doc_type='xls' ORDER BY upload_date DESC");
			pDao.setSql(sqlBuf.toString());
			Element data = pDao.executeQuerySql(1, 1);
			return data;
		} finally {
			pDao.releaseConnection();
		}
	}
	
	/**
	 * 导入初始化
	 * @param actEle
	 * @param c_cardno
	 * @return
	 */
	public static void checkExcel(Element exEle)
	{
		String doc_flag = exEle.getChildText("doc_flag").trim();
		try{
			AbstractExcel obj = (AbstractExcel)Class.forName("cn.com.bright.yuexue.report."+doc_flag)
				.newInstance();
			obj.saveExcle(exEle);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void importExcle(){
		System.out.println("信息导入"+DatetimeUtil.getNow(null) + ", start.");
		try {
			Element data = queryData();
			List excelList = data.getChildren("Record");
			if (excelList.size()>0) {
				for(int i=0;i<excelList.size();i++){
					Element record = (Element)excelList.get(i);
					 checkExcel(record);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log4j.logError(ex);
		} finally {
		}
		System.out.println(DatetimeUtil.getNow(null) + ",导入 end.");
	}
	
}
