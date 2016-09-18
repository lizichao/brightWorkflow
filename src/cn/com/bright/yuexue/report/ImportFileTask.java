package cn.com.bright.yuexue.report;

import cn.brightcom.jraf.util.TimerTask;


public class ImportFileTask extends TimerTask {
	 public void run(){	 
		ImportFile importFile = new ImportFile(); 
		importFile.importData();
	 }

}
