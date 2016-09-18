package cn.com.bright.yuexue.sell.preview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.Log;

public class PDF2SWFUtil {

	private Log log4j = new Log(this.getClass().toString());
	private static String swfpagesarg = (String) BrightComConfig.getConfiguration().getProperty("sell.swfpagesarg");
	private static String swftoolspath = (String) BrightComConfig.getConfiguration().getProperty("sell.swftoolspath");
	public static final Pattern PDF_PAGE_PATTERN = Pattern.compile("page=\\d+");
	
	/**
	  * ����SWFTools���߽�pdfת����swf��ת������swf�ļ���pdfͬ��
	  * @author jiangyh
	  * @param fileDir PDF�ļ����·���������ļ�����
	  * @param exePath ת������װ·��
	  * @throws IOException
	  */
	public static synchronized void pdf2swf(String fileDir, boolean issetpage) throws IOException {
		//�ļ�·��
		String filePath = fileDir.substring(0, fileDir.lastIndexOf("/"));
		//�ļ�����������׺
		String fileName = fileDir.substring((filePath.length() + 1), fileDir.lastIndexOf("."));
		Process pro = null;

		String pagesarg = "";
		if (issetpage) {
			pagesarg = swfpagesarg;
		}

		if (isWindowsSystem()) {
			//�����windowsϵͳ
			//����������
			String cmd = swftoolspath + " \"" + fileDir + "\" -o \"" + filePath + "/" + fileName + ".swf\" " + pagesarg;
			//Runtimeִ�к󷵻ش����Ľ��̶���
			pro = Runtime.getRuntime().exec(cmd);
		} else {
			//�����linuxϵͳ,·�������пո񣬶���һ��������˫���ţ������޷���������
			String[] cmd = new String[4];
			cmd[0] = swftoolspath;
			cmd[1] = fileDir;
			cmd[2] = filePath + "/" + fileName + ".swf";
			cmd[4] = pagesarg;
			//Runtimeִ�к󷵻ش����Ľ��̶���
			pro = Runtime.getRuntime().exec(cmd);
		}
		//��Ҫ��ȡһ��cmd�������Ҫ������flush�����ļ������̣߳�
		new DoOutput(pro.getInputStream()).start();
		new DoOutput(pro.getErrorStream()).start();
		try {
			//����waitFor��������Ϊ��������ǰ���̣�ֱ��cmdִ����
			pro.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	  * �ж��Ƿ���windows����ϵͳ
	  * @author jiangyh
	  * @return
	  */
	private static boolean isWindowsSystem() {
		String p = System.getProperty("os.name");
		return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
	}

	/**
	  * ���߳��ڲ���
	  * ��ȡת��ʱcmd���̵ı�׼������ʹ��������������������Ϊ�������ȡ�������̽�����
	  * @author jiangyh
	  */
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
	  * ����main����
	  * @param args
	  */
	public static void main(String[] args) {
		//ת������װ·��
		try {
			PDF2SWFUtil.pdf2swf("c:/1.pdf", true);
		} catch (IOException e) {
			System.err.println("ת������");
			e.printStackTrace();
		}
	}
}