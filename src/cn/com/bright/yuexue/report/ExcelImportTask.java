package cn.com.bright.yuexue.report;

import java.util.TimerTask;

import cn.brightcom.jraf.context.ApplicationContext;

public class ExcelImportTask extends TimerTask{
	@Override
	public void run() {
		//ApplicationContext.regSubSys("yuexue");
		ExcelImport importExcel = ExcelImport.getInstance();
		importExcel.importExcle();
	}
}
