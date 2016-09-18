package cn.com.bright.yuexue.sell.preview;

import java.io.File;
import java.net.ConnectException;
import java.util.Date;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.Log;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class DOC2PDFUtil extends java.lang.Thread {
	private Log log4j = new Log(this.getClass().toString());
	private static String openofficeport = (String) BrightComConfig.getConfiguration().getProperty("sell.openofficeport");

	private File inputFile;// ��Ҫת�����ļ�   
	private File outputFile;// ������ļ�   

	public DOC2PDFUtil(File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void docToPdf() {
		Date start = new Date();

		OpenOfficeConnection connection = new SocketOpenOfficeConnection(Integer.parseInt(openofficeport));
		try {
			connection.connect();
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputFile, outputFile);
		} catch (ConnectException cex) {
			cex.printStackTrace();
		} finally {
			// close the connection   
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	/**  
	  * ���ڷ������̲߳���ȫ�ģ����ԡ�����Ҫ�����߳�  
	  */
	public void run() {
		this.docToPdf();
	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	/**
	  * ����main����
	  * @param args
	  */
	public static void main(String[] args) {
		File inputFile = new File("c://1.doc");
		File outputFile = new File("c://1.pdf");
		DOC2PDFUtil dp = new DOC2PDFUtil(inputFile, outputFile);
		dp.start();
	}
}