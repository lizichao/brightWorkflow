package cn.com.bright.yuexue.sell.preview;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.yuexue.converter.OfficeToHtml;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/*
 * office��ʽת��pdf
 * @author jiangyh
 */
public class DocConverter {
	private Log log4j = new Log(this.getClass().toString());
	private static String swfpagespercent = (String) BrightComConfig.getConfiguration().getProperty("sell.swfpagespercent");
	private static String swfpagesarg = (String) BrightComConfig.getConfiguration().getProperty("sell.swfpagesarg");
	private static String openofficeport = (String) BrightComConfig.getConfiguration().getProperty("sell.openofficeport");
	private static String swftoolspath = (String) BrightComConfig.getConfiguration().getProperty("sell.swftoolspath");
	public static final Pattern PDF_PAGE_PATTERN = Pattern.compile("page=\\d+");

	private static final int environment = 1;//����1��windows 2:linux(�漰pdf2swf·������)
	private String fileString;
	private String outputPath = "";//����·������������þ������Ĭ��λ��
	private String fileName;
	private String fileType;
	private File pdfFile;
	private File swfFile;
	private File docFile;
	private File odtFile;//ת��txt�ļ�ʱ�����Ƚ��ļ��޸�Ϊodt��ʽ����ת�������������
	public boolean ispdf = false;
	
	public DocConverter() {
		
	}
	
	public DocConverter(String fileString) {
		ini(fileString);
	}

	public DocConverter(String fileString, boolean ispdf) {
		ini(fileString);
		this.ispdf = ispdf;
	}

	/*
	 * �������� file
	 * @param fileString
	 */
	public void setFile(String fileString) {
		ini(fileString);
	}

	/*
	 * ��ʼ��
	 * @param fileString
	 */
	private void ini(String fileString) {
		this.fileString = fileString;
		fileName = fileString.substring(0, fileString.lastIndexOf("."));
		
		docFile = new File(fileString);
		pdfFile = new File(fileName + ".pdf");
		swfFile = new File(fileName + ".swf");
		
		fileType = FileUtil.getFileExt(fileString);
		if (fileType.equals("txt") || fileType.equals("TXT")){
			odtFile = new File(fileName + ".odt");
			try{
			   this.copyFile(docFile, odtFile);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if ("pdf".equals(fileType.toLowerCase())){
			this.ispdf = true;
		}
	}

	/*
	 * תΪPDF
	 * @param file
	 */
	private void doc2pdf() throws Exception {
		if (docFile.exists()) {
			if (!pdfFile.exists()) {
				// OpenOffice�ķ���
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(Integer.parseInt(openofficeport));
				try {
					connection.connect();
					DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
					if (fileType.equals("txt") || fileType.equals("TXT")){
						converter.convert(odtFile, pdfFile);
					}
					else{
					    converter.convert(docFile, pdfFile);
					}
					//close the connection
					connection.disconnect();
					log4j.logInfo("pdfת���ɹ���PDF�����" + pdfFile.getPath() + "");
				} catch (java.net.ConnectException e) {
					e.printStackTrace();
					log4j.logError("pdfת���쳣��openoffice����δ������");
					throw e;
				} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
					e.printStackTrace();
					log4j.logError("pdfת�����쳣����ȡת���ļ�ʧ��");
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			} else {
				log4j.logInfo("�Ѿ�ת��Ϊpdf������Ҫ�ٽ���ת��");
			}
		} else {
			log4j.logInfo("pdfת�����쳣����Ҫת�����ĵ������ڣ��޷�ת��");
		}
	}
	
	/*
	 * תΪhtml
	 * @param file
	 */
	public boolean doc2html(File docFile,File htmlFile) throws Exception {
		if (docFile.exists()) {
			String fileName = FileUtil.getFileName(docFile.getPath());
			String fileType = FileUtil.getFileExt(fileName).toLowerCase();
			if("docx".equals(fileType)){
				String fileNameNoExt = docFile.getName();
				fileNameNoExt = fileNameNoExt.substring(0, fileNameNoExt.lastIndexOf("."));
		    	OfficeToHtml converter = new OfficeToHtml();
	    		File docFileTepm = new File(docFile.getParent() + File.separatorChar + fileNameNoExt +".doc");
	    		boolean isSucc = converter.docxTodoc(docFile.getPath(), docFileTepm.getPath());
	    		if(isSucc){ // ת���ɹ�
	    			docFile = docFileTepm;
	    		}
	    	}
			
			// OpenOffice�ķ���
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(Integer.parseInt(openofficeport));
			try {
				connection.connect();
				DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				converter.convert(docFile, htmlFile);
				connection.disconnect();
				log4j.logInfo("htmlת���ɹ���html�����" + htmlFile.getPath() + "");
			} catch (java.net.ConnectException e) {
				e.printStackTrace();
				log4j.logError("htmlת���쳣��openoffice����δ������");
			} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
				e.printStackTrace();
				log4j.logError("htmlת�����쳣����ȡת���ļ�ʧ��");
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			log4j.logInfo("htmlת�����쳣����Ҫת�����ĵ������ڣ��޷�ת��");
			return false;
		}
		return true;
	}
	
	/*
	 * docxתΪdoc
	 * @param file
	 */
	public boolean docx2doc(File docxFile,File docFile) throws Exception {
		if (docxFile.exists()) {
			ComThread.InitSTA();  
	        ActiveXComponent activexcomponent = new ActiveXComponent("Word.Application");
	        boolean flag = false;  
	        try {  
	            activexcomponent.setProperty("Visible", new Variant(false));  
	            Dispatch dispatch = activexcomponent.getProperty("Documents").toDispatch();  
	            Dispatch dispatch1 = Dispatch.invoke(dispatch, 
	            		                             "Open", 
	            		                             1,  
	                                                 new Object[] { docxFile.getPath(), new Variant(false), new Variant(true) },  
	                                                new int[1]).toDispatch();  
	            Dispatch.invoke(dispatch1, "SaveAs", 1, new Object[] { docFile.getPath(),new Variant(8) },new int[1]);  
	            Variant variant = new Variant(false);  
	            Dispatch.call(dispatch1, "Close", variant);  
	            flag = true;  
	        } catch (Exception exception) {  
	            exception.printStackTrace();  
	        } finally {  
	            activexcomponent.invoke("Quit", new Variant[0]);  
	            ComThread.Release();  
	            ComThread.quitMainSTA();  
	        }
		} else {
			log4j.logInfo("htmlת�����쳣����Ҫת�����ĵ������ڣ��޷�ת��");
			return false;
		}
		return true;
	}
	
	/*
	 * ת����swf
	 */
	private void pdf2swf(boolean issetpage) throws Exception {
		Runtime r = Runtime.getRuntime();

		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1) {//windows��������
					try {
						Process p = null;
						String preview_page = "";
						if (issetpage) {
							preview_page = swfpagesarg;
							String pageinfo = "";
							// �õ�PDF����Ϣ ֻ��ҳ�����߶ȡ���ȵ���Ϣ page=132 width=595.00 height=842.00
							p = r.exec(swftoolspath + " " + pdfFile.getPath() + " -I");
							String pdfinfo = loadStream(p.getInputStream());
							Matcher pdfmatcher = PDF_PAGE_PATTERN.matcher(" " + pdfinfo);
							while (pdfmatcher.find()) {
								pageinfo = pdfmatcher.group(); // ��ʽ page=1
							}
							if (StringUtil.isNotEmpty(pageinfo) && StringUtil.isNotEmpty(swfpagespercent)) {
								String[] pages = pageinfo.split("=");
								preview_page = " -p 1-" + String.valueOf(Math.ceil(Integer.parseInt(pages[1]) * (new Double(swfpagespercent))));
							}
						}

						// Process p = r.exec("pdf2swf " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 -p 1-3");
						p = r.exec(swftoolspath + " " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 " + preview_page);
						new DoOutput(p.getInputStream()).start();
						new DoOutput(p.getErrorStream()).start();

						// ����waitFor��������Ϊ��������ǰ���̣�ֱ��cmdִ����
						p.waitFor();
						
						log4j.logInfo("swfת���ɹ����ļ������" + swfFile.getPath() + "");

						if (pdfFile.exists()) {
							if (!ispdf) {
								pdfFile.delete();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						log4j.logError("swfת���쳣" + e.getMessage());
						throw e;
					}
				} else if (environment == 2) {//linux��������
					try {
						Process p = null;
						String preview_page = "";
						if (issetpage) {
							preview_page = swfpagesarg;
							String pageinfo = "";
							// �õ�PDF����Ϣ ֻ��ҳ�����߶ȡ���ȵ���Ϣ page=132 width=595.00 height=842.00
							p = r.exec(swftoolspath + " " + pdfFile.getPath() + " -I");
							String pdfinfo = loadStream(p.getInputStream());
							Matcher pdfmatcher = PDF_PAGE_PATTERN.matcher(" " + pdfinfo);
							while (pdfmatcher.find()) {
								pageinfo = pdfmatcher.group(); // ��ʽ page=1
							}
							if (StringUtil.isNotEmpty(pageinfo) && StringUtil.isNotEmpty(swfpagespercent)) {
								String[] pages = pageinfo.split("=");
								preview_page = " -p 1-" + String.valueOf(Math.ceil(Integer.parseInt(pages[1]) * (new Double(swfpagespercent))));
							}
						}

						// Process p = r.exec("pdf2swf " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 -p 1-3");
						p = r.exec(swftoolspath + " " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 " + preview_page);
						new DoOutput(p.getInputStream()).start();
						new DoOutput(p.getErrorStream()).start();

						// ����waitFor��������Ϊ��������ǰ���̣�ֱ��cmdִ����
						p.waitFor();
						
						log4j.logInfo("swfת���ɹ����ļ������" + swfFile.getPath() + "");

						if (pdfFile.exists()) {
							if (!ispdf) {
								pdfFile.delete();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						log4j.logError("swfת���쳣" + e.getMessage());
						throw e;
					}
				}
			} else {
				log4j.logInfo("pdf�����ڣ��޷�ת��");
			}
		} else {
			log4j.logInfo("swf�Ѵ��ڲ���Ҫת��");
		}
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();

		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}

	/*
	 * ת��������
	 */
	public boolean conver(boolean issetpage) throws Exception {
		// ֻҪ״̬�и��Ķ�Ҫ��������swf 
		if (swfFile.exists()) {
			swfFile.delete();
		}

		if (swfFile.exists()) {
			log4j.logInfo("swfת������ʼ���������ļ��Ѿ�ת��Ϊswf");
			return true;
		}

		if (environment == 1) {
			log4j.logInfo("swfת������ʼ��������ǰ�������л���windows");
		} else {
			log4j.logInfo("swfת������ʼ��������ǰ�������л���linux");
		}

		try {
			if (!ispdf) {
				doc2pdf();
			}
			pdf2swf(issetpage);
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logInfo("ת���쳣 " + e.getMessage());
			throw e;
			// return false;
		}

		if (swfFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * �����ļ�·��
	 * @param
	 */
	public String getswfPath() {
		if (swfFile.exists()) {
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}
	}

	/*
	 * �������·��
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		if (!outputPath.equals("")) {
			String realName = fileName.substring(fileName.lastIndexOf("/"), fileName.lastIndexOf("."));
			if (outputPath.charAt(outputPath.length()) == '/') {
				swfFile = new File(outputPath + realName + ".swf");
			} else {
				swfFile = new File(outputPath + realName + ".swf");
			}
		}
	}

	private static class DoOutput extends Thread {
		public InputStream is;

		//���췽��
		public DoOutput(InputStream is) {
			this.is = is;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			String str = null;
			try {
				//���ﲢû�ж��������ݽ��д���ֻ�Ƕ���һ��
				while ((str = br.readLine()) != null)
					;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * �ļ�����
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public void copyFile(File sourceFile,File targetFile)throws IOException{
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		
		byte[] b = new byte[1024*5];
		int len;
		while ((len=inBuff.read(b))!=-1){
			outBuff.write(b, 0, len);
		}
		outBuff.flush();
		outBuff.close();
		output.close();
		input.close();
	}

	public static void main(String s[]) throws Exception {
		DocConverter d = new DocConverter("E:/ftp_test/testdata/sjmb.doc");
		d.doc2pdf();
		//d.conver(false);
	}
}