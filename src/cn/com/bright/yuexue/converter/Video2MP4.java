package cn.com.bright.yuexue.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;

public class Video2MP4 implements AbstractConverter{
	
	private static String Mencoder = (String) BrightComConfig.getConfiguration().getProperty("mencoder.cmd");
	private Log log4j = new Log(this.getClass().toString());
	
    /**
     * 启动转换
     * @param srcPath
     * @param destPath
     */	
	public void process(String srcPath,String destPath)throws Exception{
		String folderPath = srcPath.substring(0,srcPath.lastIndexOf("/"));
		String fileName = FileUtil.getFileName(srcPath);
		String fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));

		StringBuffer cmd = new StringBuffer();
		   cmd.append(Mencoder).append(" ").append(srcPath);
		   cmd.append(" ").append(folderPath);
		   cmd.append(" ").append(fileNameNoExt+"_s");
		   cmd.append(" ").append(folderPath+"/"+fileNameNoExt+"_s.log");
		
		String strCmd = cmd.toString();
		strCmd = strCmd.replaceAll("/", "\\\\");		  
		try
		{
			System.out.println(DatetimeUtil.getNow() + "视频转换 Start：["+srcPath+"]");
			Runtime runtime = Runtime.getRuntime();
			Process p = runtime.exec(strCmd);
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");            
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");
            errorGobbler.start();
            outputGobbler.start();
			p.waitFor();
			System.out.println(DatetimeUtil.getNow() + "视频转换 End: ["+srcPath+ "]");
		}
		catch (Exception ex)
		{
			log4j.logError("视频转换异常：[" + cmd + "]");
			log4j.logError(ex);
			throw ex;
		}		
	}
	
	class StreamGobbler extends Thread
	{
		InputStream is;

		String type;

		StreamGobbler(InputStream is, String type)
		{
			this.is = is;
			this.type = type;
		}

		public void run() 
		{
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				while (br.readLine() != null) {

				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}	
	
}
