package cn.com.bright.yuexue.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import cn.brightcom.jraf.util.FileUtil;


/**
 * <p>Title:����Word�������Ծ�</p>
 * <p>Description: �����ϴ�����</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author lhbo
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> lhbo    2016/1/14       1.0          build this moudle </p>
 *     
 */

public class Word2PraxesUtil {
    
    public static List<PraxesBean> getQuestionListToHtml(File srcFile) {
    	List<PraxesBean> questionList = null;
    	if(srcFile.exists()){
    		questionList = getQuestionListToHtml(srcFile.getPath());
    	}
    	return questionList;
    }
    
    public static List<PraxesBean> getQuestionListToHtml(String srcFilePath) {
    	List<PraxesBean> questionList = null;
    	try {
    		File file = new File(srcFilePath);
    		if(file.exists()){
    			String sourcePicDir = file.getParent();
    			String content = FileUtils.readFileToString(file);
    			String targetPicDir = genPicDirs();
    			String htmlCode = clearHtmlFormat(content);
    			htmlCode = movePicAndChangePicPath(htmlCode,sourcePicDir,targetPicDir);
    			htmlCode = addPicLabelHtm(htmlCode);
    			questionList = htmlToQuestionList(htmlCode);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return questionList;
    }
    
	/** 
     *  
     * ���һЩ����Ҫ��html��� 
     *  
     * @param htmlStr  ���и���html��ǵ�html��� 
     * @return ȥ���˲���Ҫhtml��ǵ���� 
     */
    protected static String clearHtmlFormat(String htmlStr) {
    	// �滻�ַ��Լ��س�����
    	//htmlStr = htmlStr.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
    	htmlStr = htmlStr.replaceAll("(\r\n|\r|\n|\n\r)", "");
        // ��ȡbody���ݵ�����
        String bodyReg = "<(BODY|body)(.*?)</(BODY|body)>";
        Pattern bodyPattern = Pattern.compile(bodyReg);  
        Matcher bodyMatcher = bodyPattern.matcher(htmlStr);
        if (bodyMatcher.find()) {
            htmlStr = bodyMatcher.group();  
        }
        
        // ��<P></P>ת����</div></div>������ʽ  
        // htmlStr = htmlStr.replaceAll("(<P)([^>]*>.*?)(<\\/P>)","<div$2</div>");  
        // ��<P></P>�е�����ȫ��ɾ������ɾ����ʽ
        //htmlStr = htmlStr.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
        htmlStr = htmlStr.replaceAll("<P[^>]*?>", "<p>").replaceAll("</P>", "</p>");
        
        // ɾ������Ҫ�ı�ǩ  
        htmlStr = htmlStr.replaceAll("<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|SCRIPT|script|COL|col|dir|DIR|[ovwxpOVWXP]:\\w+)[^>]*?>","");
        // ��BR��ǩ��ʽ��
        htmlStr = htmlStr.replaceAll("<[/]?(br|BR)[^>]*?>","<br>");
        // ����˫����ת��
        htmlStr = htmlStr.replaceAll("&ldquo;","��").replaceAll("&rdquo;", "��");
        // ��<U></U>��ǩת��Ϊ8���»���
        htmlStr = htmlStr.replaceAll("(<U>)\\s*(<\\/U>)", "________");
        // ɾ������Ҫ������
        //String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"[^\"]*\"|[^>]+)([^>]*)>";
        String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE)=(?:'[^']*'|\"[^\"]*\"|[^\\s]*)([^>]*)>";
        htmlStr = fiterHtmlTagArr(htmlStr, regxpForHtml);
        
        return htmlStr;
    }
    
    /**
     *  
     * ���html��ǩ��һЩ����Ҫ������
     *  
     * @param htmlStr ���и���html��ǵ�html�ַ�
     * @return 
     */
    public static String fiterHtmlTagArr(String htmlStr,String regxpForHtml) {
    	Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(htmlStr);
        while (matcher.find()) {
        	String oldChar = matcher.group();
        	String newChar = oldChar.replaceAll(regxpForHtml, "<$1$2>").trim();
        	htmlStr = htmlStr.replace(oldChar, newChar);
        	matcher = pattern.matcher(htmlStr);
        }
        return htmlStr;   
    }
    
    /**
     *  
     * �ƶ�ͼƬλ�ã���ת��֮���ͼƬ�Ƶ�ָ����images�ļ�����
     *  
     * @param htmlCode ���и���html��ǵ�html�ַ�
     * @param sourcePicDir ԴͼƬ·��
     * @param targetPicDir Ŀ��ͼƬ·��
     * @return 
     */
    protected static String movePicAndChangePicPath(String htmlCode, String sourcePicDir, String targetPicDir) {
    	String regular="<IMG(.*?)SRC=\"(.*?)\"";  
        String img_pre="(?i)<IMG(.*?)SRC=\"";
        String img_sub="\"";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null,picPath="";  
        while (m.find()) {
        	src=m.group();
        	src=src.replaceAll(img_pre, "").replaceAll(img_sub, "").trim();
        	picPath = moveFile(src, sourcePicDir, targetPicDir);
        	htmlCode = htmlCode.replace("\""+src, "\""+picPath);
        }
        return htmlCode;
    }
    
    /**
     *  
     * ͼƬ��ȳ���100pxʱ���Զ�����P��ǩ�Զ�������ʾ
     *  
     * @param htmlCode ���и���html��ǵ�html�ַ�
     * @return 
     */
    protected static String addPicLabelHtm(String htmlCode) {
    	String imgReg ="<IMG(.*?)>";  
        Pattern p=Pattern.compile(imgReg,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        while (m.find()) {
        	String imgCode = m.group();
        	String widthReg = "<IMG(.*?)WIDTH=(.*?)\\s";  
            String width_pre = "(?i)<IMG(.*?)WIDTH=";
            String width_sub ="(\\s|\")";
            Pattern width_p = Pattern.compile(widthReg,Pattern.CASE_INSENSITIVE);
            Matcher width_m = width_p.matcher(imgCode);
            if (width_m.find()) {
            	String widthCode = width_m.group();
            	String width = widthCode.replaceAll(width_pre, "").replaceAll(width_sub, "").trim();
            	if(StringUtils.isNotEmpty(width)){
            		if(Integer.parseInt(width) > 100){ // ͼƬ��ȳ���100pxʱ���ӻ���
            			htmlCode = htmlCode.replace("<p>"+imgCode+"</p>" ,imgCode).replace(imgCode, "<p>"+imgCode+"</p>");
            		}
            	}
            }
        }
        return htmlCode;
    }
    
    protected static String moveFile(String sourcePicPath, String sourcePicDir, String targetPicDir) {
    	String basePath = FileUtil.getWebPath();
    	//basePath = "D:\\DeveloperSite\\woclassroom\\web";
    	sourcePicPath = sourcePicDir + File.separatorChar + sourcePicPath;
    	File sourcePicFile = new File(FileUtil.getPhysicalPath(sourcePicPath));
    	String targetPicPath = "";
    	if(sourcePicFile.exists()){
	    	targetPicPath = targetPicDir + sourcePicFile.getName();
	    	try {
				FileUtil.moveFile(sourcePicFile, new File(basePath + targetPicPath));
				sourcePicFile.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return targetPicPath.replace("\\", "/");
    }
    
    protected static List<PraxesBean> htmlToQuestionList(String htmlCode) {
    	PraxesBean question = null;
    	List<PraxesBean> questionList = new ArrayList<PraxesBean>();
    	String regular="<p>(.*?)�����ġ�(.*?)��������</p>";
        String label_pre="(?i)<p>(.*?)�����ġ�";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null;
        while (m.find()) {
        	question = new PraxesBean();
        	src = m.group();
        	src = "<p>�����ġ�" + src.replaceAll(label_pre, "").trim();
        	question.setTitle(htmlToQuestions(src,"�����ġ�","���𰸡�"));
        	question.setAnswer(htmlToQuestions(src,"���𰸡�","��������"));
        	question.setDesc(htmlToQuestions(src,"��������","��������"));
        	questionList.add(question);
        }
    	return questionList;
    }
    
    protected static String htmlToQuestions(String htmlCode,String startCode,String endCode) {
    	String text = "";
    	String regular=startCode + "(.*?)" + endCode;
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        while (m.find()) {
        	text = m.group().replaceAll(startCode, "<p>").replaceAll("<p>(?!.*(</p>)).*"+endCode, "").trim();
        	text = text.replaceAll("<p>(<br>|\\s+|)</p>" ,"").trim(); //ȥ������
        }
        return text;
    }
    
    protected static String genPicDirs(){
    	String basePath = FileUtil.getWebPath();
    	//basePath = "D:\\DeveloperSite\\woclassroom\\web";
		String picDirs = "/images/praxe/" + getNow("yyyyMM") + File.separatorChar;
		try {
		    File dirFile = new File(basePath + picDirs);		    
		    if (!dirFile.exists() && !dirFile.isDirectory()){
		    	dirFile.mkdirs();
		    }
		}catch (Exception e)  {
			e.printStackTrace();
            return null;
		}
		return picDirs;
    }
    
    protected static String getNow(String format) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(new Date());
        return str;
    }
    
    public static void main(String a[]){
    	String s = "<P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><TABLE DIR=\"LTR\" ALIGN=LEFT WIDTH=320 HSPACE=6 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0><COL WIDTH=89><COL WIDTH=58><COL WIDTH=58><COL WIDTH=58><TR>	<TD WIDTH=89 HEIGHT=7>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>ʵ�����</FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><SPAN CLASS=\"sd-abs-pos\" STYLE=\"position: absolute; top: 0.08cm; left: 2.07cm; width: 163px\"><IMG SRC=\"2c9ca2865316ca96015316cb1c8d0002_html_688879fe.png\" NAME=\"ͼƬ 106\" WIDTH=163 HEIGHT=67 BORDER=0></SPAN><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3</SPAN></FONT></FONT></P>	</TD></TR><TR>	<TD WIDTH=89 HEIGHT=7>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>��ѹ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><I>U</I></FONT></FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3>/V</FONT></FONT></SPAN></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.0</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.0</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3<SPAN ID=\"��2\" DIR=\"LTR\" STYLE=\"float: left; width: 0.82cm; height: 0.6cm; border: none; padding: 0cm; background: #ffffff\">			<P CLASS=\"cjk\" ALIGN=CENTER>&mdash;</P>		</SPAN>.8</SPAN></FONT></FONT>		</P>	</TD></TR><TR>	<TD WIDTH=89 HEIGHT=6>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><I>I</I></FONT></FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3>/A</FONT></FONT></SPAN></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.33</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><BR>		</P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.40</SPAN></FONT></FONT></P>	</TD></TR></TABLE><BR></P>".replaceAll("<P[^>]*?>", "<p>");;
    	System.out.println(s);
    	System.out.println(clearHtmlFormat(s));
    	/*String htmlStr ="";
    	htmlStr = "<HTML><HEAD></HEAD><BODY LANG=\"zh-CN\" TEXT=\"#000000\" DIR=\"LTR\"><P CLASS=\"cjk\" ALIGN=CENTER STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=4 STYLE=\"font-size: 15pt\"><B>2015</B></FONT></SPAN></FONT><FONT SIZE=4 STYLE=\"font-size: 15pt\"><B>�������п�����ģ���⣨һ��</B></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><BR></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3><B>һ����ѡ�⣨���¸�С�����ĸ�ѡ�����ֻ��һ��ѡ��������⣬ÿ��</B></FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\"><B>1.5</B></SPAN></FONT></FONT><FONT SIZE=3><B>�֣���</B></FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\"><B>24</B></SPAN></FONT></FONT><FONT SIZE=3><B>�֣�</B></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">15</SPAN></FONT></FONT><FONT SIZE=3>��������ͬ��</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>��</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>������������̨����������ǣ������������ֱ�����˶��������˶���</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">s&mdash;t</SPAN></FONT></FONT><FONT SIZE=3>ͼ��ֱ���ͼ�ס�����ʾ����</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <IMG SRC=\"402881215234630b0152346447a00002_html_m6640ba60.png\" NAME=\"ͼ��1\" ALIGN=LEFT HSPACE=12 WIDTH=269 HEIGHT=133 BORDER=0><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>���������ﶼ������ֱ���˶�</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>��ǰ</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">6</SPAN></FONT></FONT><FONT SIZE=3>��</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>�����ƽ���ٶ�С��</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>�����ƽ���ٶ�   </FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">C</SPAN></FONT></FONT><FONT SIZE=3>������������</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>�������������</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>���������</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">D</SPAN></FONT></FONT><FONT SIZE=3>��</FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>�����������˶��������ܵ�һ��ƽ��������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>1</FONT></FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3>6</FONT></FONT></SPAN></FONT><FONT SIZE=3>������Ϊ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>M</FONT></FONT></SPAN></FONT><FONT SIZE=3>��ľ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>��ˮƽ�����ϣ����������������������Ϊ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>m</FONT></FONT></SPAN></FONT><FONT SIZE=3>�Ĺ����������������������������������������˶�����ͻȻ��������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>(</FONT></FONT></SPAN></FONT><FONT SIZE=3>���������غ������׻��ֵ�Ħ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>)</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" NAME=\"ͼ��2\" ALIGN=LEFT HSPACE=12 WIDTH=200 HEIGHT=108 BORDER=0><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>������û�м���ǰ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>���ܵ�Ħ����Ϊ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>mg</FONT></FONT></SPAN></FONT><FONT SIZE=3>����������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>B</FONT></FONT></SPAN></FONT><FONT SIZE=3>�����Ӽ��Ϻ�</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>���ܵ�Ħ����Ϊ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>M-m)</FONT></FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3>g</FONT></FONT></SPAN></FONT><FONT SIZE=3>����������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>C</FONT></FONT></SPAN></FONT><FONT SIZE=3>�����Ӽ���ʱ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>����Ħ����Ϊ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>mg</FONT></FONT></SPAN></FONT><FONT SIZE=3>����������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>D</FONT></FONT></SPAN></FONT><FONT SIZE=3>�����Ӽ��Ϻ�</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>�ܵ�Ħ���˽�Խ��Խ��</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>1</FONT></FONT><FONT FACE=\"����, SimSun\"><FONT SIZE=3>7</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>-20</FONT></FONT></SPAN></FONT><FONT SIZE=3>�ǳɶ��ɻ���ҵ����Ϊ�й������ž����Ƶ�����һ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>(</FONT></FONT></SPAN></FONT><FONT SIZE=3>ŷ���б�׼Ϊ���Ĵ����±�׼�Լ�����˹��׼Ϊ�����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>)</FONT></FONT></SPAN></FONT><FONT SIZE=3>˫����������ս����������˵������ȷ����</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_m34337222.png\" NAME=\"ͼ��3\" ALIGN=LEFT HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>���������ܣ�����Ϊ�״﷢���ĵ�Ų��ᱻ�����淴��</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>B</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>�ڼ�������ʱ����е���غ�</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>C</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>�ڿ������ٷ���ʱ����е�ܲ��䣬����������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>D</FONT></FONT></SPAN></FONT><FONT SIZE=3>����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>�ڿ��м��ٸ���Ͷ��ʱ�������غ�</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-left: 0.18cm; margin-bottom: 0cm; line-height: 0.55cm; widows: 2; orphans: 2\"><BR></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">22</SPAN></FONT></FONT><FONT SIZE=3>������</FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">L</SPAN></FONT></FONT><FONT SIZE=3>�ϱ���&ldquo;</FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.5V&rdquo;</SPAN></FONT></FONT><FONT SIZE=3>�����������ĵ����������˵�ѹ�ı仯��ϵ��ͼ��ʾ������˵������ȷ����</FONT></P><P CLASS=\"cjk\" ALIGN=CENTER STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_84bea23.png\" NAME=\"ͼ��4\" ALIGN=BOTTOM WIDTH=308 HEIGHT=223 BORDER=0></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>A</FONT></SPAN></FONT><FONT SIZE=3>������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>�Ķ������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>0.2A    B</FONT></SPAN></FONT><FONT SIZE=3>������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>��������ʱ�ĵ�����</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>10</FONT><FONT SIZE=3>&Omega;</FONT></SPAN></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>C</FONT></SPAN></FONT><FONT SIZE=3>������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>�Ķ������</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>6.25W   D</FONT></SPAN></FONT><FONT SIZE=3>����˿�ĵ������ѹ���������С</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">23</SPAN></FONT></FONT><FONT SIZE=3>����ͼ��ʾ��·���պϿ��أӺ󣬼��������ǵ�ѹ��ʾ��֮���ǣ������������أӶϿ������������ǵ������������ʾ��֮��Ϊ</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><TABLE DIR=\"LTR\" ALIGN=LEFT WIDTH=592 HSPACE=6 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0>	<COL WIDTH=99>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=66>	<COL WIDTH=52>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=9>			<P CLASS=\"cjk\"><FONT SIZE=3>��ѹ</FONT><FONT SIZE=3> </FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>U/V</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>1.0</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1.5</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.0</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.5</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.0</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.8</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">4.0</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<P CLASS=\"cjk\"><FONT SIZE=3>����</FONT><FONT SIZE=3> </FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>I/A			</FONT></FONT></SPAN></FONT>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.18</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.2</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.22</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.25</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.28</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm; margin-bottom: 0cm\"><BR>			</P>			<P CLASS=\"cjk\"><U>          </U>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.32</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<P CLASS=\"cjk\"><FONT SIZE=3>�繦��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3>P/</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.18</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.3</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.44</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.625</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.84</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm; margin-bottom: 0cm\"><BR>			</P>			<P CLASS=\"cjk\"><U>        </U>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"����, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1.28</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=31>			<P CLASS=\"cjk\"><FONT SIZE=3>���ݷ������</FONT></P>		</TD>		<TD COLSPAN=7 WIDTH=463>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT SIZE=3>�ܰ�</FONT><FONT SIZE=3>			</FONT><FONT SIZE=3>&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;</FONT><FONT SIZE=3>			</FONT><FONT SIZE=3>��</FONT><FONT SIZE=3>   </FONT><FONT SIZE=3>&mdash;&mdash;&mdash;&mdash;&rarr;			 </FONT><FONT SIZE=3>    </FONT><FONT SIZE=3>��������</FONT><FONT SIZE=3>			 </FONT><FONT SIZE=3>&rarr;</FONT><FONT SIZE=3> </FONT><FONT SIZE=3>����</FONT></P>		</TD>	</TR></TABLE> </P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3>��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>��</FONT><FONT SIZE=3><IMG SRC=\"402881215234630b0152346447a00002_html_2e1b9152.gif\" NAME=\"����1\" ALIGN=ABSMIDDLE WIDTH=88 HEIGHT=40></FONT><FONT SIZE=3>��     ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>��������������</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3>��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>����������������     ��</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>������</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm; background: #ffffff; line-height: 150%; widows: 2; orphans: 2\"><BR></P><DIV TYPE=FOOTER>	<P ALIGN=CENTER STYLE=\"margin-top: 0.69cm; margin-bottom: 0cm\"><SDFIELD TYPE=PAGE SUBTYPE=RANDOM FORMAT=PAGE><FONT FACE=\"Times New Roman, serif\">2</FONT></SDFIELD></P>	<P ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><BR>	</P></DIV></BODY></HTML>";
    	//System.out.println(clearFormat(htmlStr,"D:\1"));
    	
    	htmlStr = "<DIV LANG=\"zh-CN\" TEXT=\"#000000\" ><p><B>2015</B><B>�������п�����ģ���⣨һ��</B></p><p><BR></p><p><B>һ����ѡ�⣨���¸�С�����ĸ�ѡ�����ֻ��һ��ѡ��������⣬ÿ��</B><B>1.5</B><B>�֣���</B><B>24</B><B>�֣�</B></p><p>15�������ġ�������ͬ��A��B������������̨����������ǣ������������ֱ�����˶��������˶���s&mdash;tͼ��ֱ���ͼ�ס�����ʾ����</p><p> <IMG SRC=\"402881215234630b0152346447a00002_html_m6640ba60.png\" NAME=\"ͼ��1\" ALIGN=LEFT HSPACE=12 WIDTH=269 HEIGHT=133 BORDER=0>A���������ﶼ������ֱ���˶�</p><p> B��ǰ6��A�����ƽ���ٶ�С��B�����ƽ���ٶ�   </p><p> C������������A�������������A���������</p><p> D��B�����������˶��������ܵ�һ��ƽ��������</p><p>���𰸡�A</p><p>����������15�����</p><p>��������</p><p>16�������ġ�����ΪM��ľ��A��ˮƽ�����ϣ����������������������Ϊm�Ĺ����������������������������������������˶�����ͻȻ��������(���������غ������׻��ֵ�Ħ��)����</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" NAME=\"ͼ��2\" ALIGN=LEFT HSPACE=12 WIDTH=200 HEIGHT=108 BORDER=0>A������û�м���ǰA���ܵ�Ħ����Ϊmg����������</p><p>B�����Ӽ��Ϻ�A���ܵ�Ħ����Ϊ��M-m)g����������</p><p>C�����Ӽ���ʱA����Ħ����Ϊmg����������</p><p>D�����Ӽ��Ϻ�A�ܵ�Ħ���˽�Խ��Խ��</p><p>���𰸡�D</p><p>����������16�����</p><p>��������</p><p>17�������ġ���-20�ǳɶ��ɻ���ҵ����Ϊ�й������ž����Ƶ�����һ��(ŷ���б�׼Ϊ���Ĵ����±�׼�Լ�����˹��׼Ϊ�����)˫����������ս����������˵������ȷ����</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_m34337222.png\" NAME=\"ͼ��3\" ALIGN=LEFT HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0>A����20���������ܣ�����Ϊ�״﷢���ĵ�Ų��ᱻ�����淴��</p><p>B����20�ڼ�������ʱ����е���غ�</p><p>C����20�ڿ������ٷ���ʱ����е�ܲ��䣬����������</p><p>D����20�ڿ��м��ٸ���Ͷ��ʱ�������غ�</p><p><BR></p><p>���𰸡�B</p><p>����������17�����</p><p>��������</p><p>22�������ġ�����L�ϱ���&ldquo;2.5V&rdquo;�����������ĵ����������˵�ѹ�ı仯��ϵ��ͼ��ʾ������˵������ȷ����</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_84bea23.png\" NAME=\"ͼ��4\" ALIGN=BOTTOM WIDTH=308 HEIGHT=223 BORDER=0></p><p> A������L�Ķ������0.2A    B������L��������ʱ�ĵ�����10&Omega;</p><p> C������L�Ķ������6.25W   D����˿�ĵ������ѹ���������С</p><p>���𰸡�C</p><p>����������22�����</p><p>��������</p><p>23�������ġ���ͼ��ʾ��·���պϿ��أӺ󣬼��������ǵ�ѹ��ʾ��֮���ǣ������������أӶϿ������������ǵ������������ʾ��֮��Ϊ</p><p><TABLE ORDER>	<COL WIDTH=99>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=66>	<COL WIDTH=52>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=9>			<P >��ѹ U/V</p>		</TD>		<TD WIDTH=52>			<p>1.0</p>		</TD>		<TD WIDTH=52>			<p>1.5</p>		</TD>		<TD WIDTH=52>			<p>2.0</p>		</TD>		<TD WIDTH=52>			<p>2.5</p>		</TD>		<TD WIDTH=52>			<p>3.0</p>		</TD>		<TD WIDTH=66>			<p>3.8</p>		</TD>		<TD WIDTH=52>			<p>4.0</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<p>���� I/A						</p>		</TD>		<TD WIDTH=52>			<p>0.18</p>		</TD>		<TD WIDTH=52>			<p>0.2</p>		</TD>		<TD WIDTH=52>			<p>0.22</p>		</TD>		<TD WIDTH=52>			<p>0.25</p>		</TD>		<TD WIDTH=52>			<p>0.28</p>		</TD>		<TD WIDTH=66>			<p><BR>			</p>			<p><U>          </U>			</p>		</TD>		<TD WIDTH=52>			<p>0.32</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<p>�繦��P/</p>		</TD>		<TD WIDTH=52>			<p>0.18</p>		</TD>		<TD WIDTH=52>			<p>0.3</p>		</TD>		<TD WIDTH=52>			<p>0.44</p>		</TD>		<TD WIDTH=52>			<p>0.625</p>		</TD>		<TD WIDTH=52>			<p>0.84</p>		</TD>		<TD WIDTH=66>			<p><BR>			</p>			<p><U>        </U>			</p>		</TD>		<TD WIDTH=52>			<p>1.28</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=31>			<p>���ݷ������</p>		</TD>		<TD COLSPAN=7 WIDTH=463>			<p>�ܰ�			&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;			��   &mdash;&mdash;&mdash;&mdash;&rarr;			     ��������			 &rarr; ����</p>		</TD>	</TR></TABLE> </P><p>��.��<IMG SRC=\"402881215234630b0152346447a00002_html_2e1b9152.gif\" NAME=\"����1\" ALIGN=ABSMIDDLE WIDTH=88 HEIGHT=40>��     ��.��������������</p><p>��.����������������     ��.������</p><p>���𰸡�A</p><p>����������23�����</p><p>��������</p></DIV>";
    	List<PraxesBean> questionList = htmlToQuestionList(htmlStr);
    	for(PraxesBean question : questionList){
    		System.out.println("Title:"+question.getTitle());
    		System.out.println("Type:"+question.getType());
    		System.out.println("Answer:"+question.getAnswer());
    		System.out.println("Desc:"+question.getDesc());
    	}*/
    	
    	//String s="text:<p>һ������׶��һ��������׶������׶���������ĸ߷ֱ�Ϊ������<p>sdfd</p>�򣨡���</p><p><A></A>���𰸡�";
    	//System.out.println(s.replaceAll("<p>(?!.*(</p>)).*���𰸡�", ""));
    	
    	/*List<PraxesBean> questionList = getQuestionListToHtml("D:\\DeveloperSite\\woclassroom\\web\\upload\\weixin\\teacherUploadKJ\\402880f352e318860152e344785a0038.html");
    	System.out.println("questionList:"+questionList.size());
    	for(PraxesBean question : questionList){
    		System.out.println("Title:"+question.getTitle());
    		System.out.println("Type:"+question.getType());
    		System.out.println("Answer:"+question.getAnswer());
    		System.out.println("Desc:"+question.getDesc());
    	}*/
    	/*
		String option = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int num = option.indexOf("G");
		System.out.println("A,B.C��D.e��f<p><br></p>".replaceAll("(<p><br></p>$)" ,""));
		String html = "<IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" name=\"ͼ��2\" align=\"LEFT\" hspace=\"12\" width=\"200\" height=\"108\" border=\"0\" />";
		System.out.println(html.replaceAll("<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|hspace|HSPACE|name:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>","<$1$2>"));
		html = html.replaceAll("<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|hspace|HSPACE|name|align:\\w+)=(?:'\")","<$1$2>");
		System.out.println(html);
		
		String html = "<p>��ͼ��ʾ��·���պϿ��أӺ󣬼��������ǵ�ѹ��ʾ��֮���ǣ������������أӶϿ������������ǵ������������ʾ��֮��Ϊ</p>   <p></p>   <table order=\"\"> <br>    <colgroup><br>     <col width=\"99\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"66\" /> <br>     <col width=\"52\" /> <br>    </colgroup><br>    <tBODY><br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"9\"> <p>��ѹ U/V</p> </td> <br>      <td width=\"52\"> <p>1.0</p> </td> <br>      <td width=\"52\"> <p>1.5</p> </td> <br>      <td width=\"52\"> <p>2.0</p> </td> <br>      <td width=\"52\"> <p>2.5</p> </td> <br>      <td width=\"52\"> <p>3.0</p> </td> <br>      <td width=\"66\"> <p>3.8</p> </td> <br>      <td width=\"52\"> <p>4.0</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"33\"> <p>���� I/A </p> </td> <br>      <td width=\"52\"> <p>0.18</p> </td> <br>      <td width=\"52\"> <p>0.2</p> </td> <br>      <td width=\"52\"> <p>0.22</p> </td> <br>      <td width=\"52\"> <p>0.25</p> </td> <br>      <td width=\"52\"> <p>0.28</p> </td> <br>      <td width=\"66\"> <p><br> </p> <p><u> </u> </p> </td> <br>      <td width=\"52\"> <p>0.32</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"33\"> <p>�繦��P/</p> </td> <br>      <td width=\"52\"> <p>0.18</p> </td> <br>      <td width=\"52\"> <p>0.3</p> </td> <br>      <td width=\"52\"> <p>0.44</p> </td> <br>      <td width=\"52\"> <p>0.625</p> </td> <br>      <td width=\"52\"> <p>0.84</p> </td> <br>      <td width=\"66\"> <p><br> </p> <p><u> </u> </p> </td> <br>      <td width=\"52\"> <p>1.28</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"31\"> <p>���ݷ������</p> </td> <br>      <td colspan=\"7\" width=\"463\"> <p>�ܰ� �������������� �� ���������� �������� �� ����</p> </td> <br>     </tr><br>    </tBODY><br>   </table> <br>   <p></p>   <p>��.��<IMG SRC=\"/images/praxe/201601/402881215234630b0152346447a00002_html_2e1b9152.gif\">�� ��.��������������</p>   <p>��.���������������� ��.������</p>";
		//System.out.println(stripHtml(html));
		System.out.println("---\n");
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		
		String htmlStr2 = "<IMG SRC=\"/images/praxe/201601/402881215234630b0152346447a00002_html_m34337222.png\" class='s c' NAME=\"ͼ��3\" ALIGN='LEFT' HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0>";
		String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE)=(?:'[^']*'|\"[^\"]*\"|[^\\s]*)([^>]*)>";
        htmlStr2 = fiterHtmlTagArr(htmlStr2, regxpForHtml);*/
    	
    	/*
    	String text = "<p><TABLE WIDTH=592 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0><br>	<COL WIDTH=99><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=66><br>	<COL WIDTH=52><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=9><br>			<P>��ѹ U/V</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.0</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.5</p>		</TD><br>		<TD WIDTH=52><br>			<p>2.0</p>		</TD><br>		<TD WIDTH=52><br>			<p>2.5</p>		</TD><br>		<TD WIDTH=52><br>			<p>3.0</p>		</TD><br>		<TD WIDTH=66><br>			<p>3.8</p>		</TD><br>		<TD WIDTH=52><br>			<p>4.0</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=33><br>			<p>���� I/A<br>			<br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.18</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.2</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.22</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.25</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.28</p>		</TD><br>		<TD WIDTH=66><br>			<p><br><br>			</p>			<p><U>          </U><br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.32</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=33><br>			<p>�繦��P/</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.18</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.3</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.44</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.625</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.84</p>		</TD><br>		<TD WIDTH=66><br>			<p><br><br>			</p>			<p><U>        </U><br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.28</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=31><br>			<p>���ݷ������</p>		</TD><br>		<TD COLSPAN=7 WIDTH=463><br>			<p>�ܰ�<br>			&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;<br>			��   &mdash;&mdash;&mdash;&mdash;&rarr;<br>			     ��������<br>			 &rarr; ����</p>		</TD><br>	</TR><br></TABLE> <br></P><br><p><IMG SRC=\"/images/praxe/201601/402882ea525edc8601525ee193e40024_html_14f417b8.gif\" WIDTH=88 HEIGHT=40></p>";
    	text = "<TD WIDTH=52><br>			<p>4.0</p>		</TD><br0><TD WIDTH=52>2</TD><br1>	</TR><br2>	<TR VALIGN=TOP><br3>		<TD WIDTH=99 HEIGHT=33>";
    	System.out.println("0text:"+text);
    	//text = text.replaceAll("</(td|TD)>([^>].*?)</(tr|TR)" ,"</TD></TR").trim();
    	text = text.replaceAll("</(td|TD)>(.*?)[^>](.*?)</(tr|TR)" ,"</TD></TR").trim();
    	System.out.println("1text:"+text);
    	text = text.replaceAll("</(tr|TR)>([^>].*?)<(tr|TR)" ,"</TR><TR").trim();
    	System.out.println("2text:"+text);
    	text = text.replaceAll("<(tr|TR)(.*?>)([^>].*?)(<(td|TD))" ,"<$1$2<TD").trim();
    	System.out.println("3text:"+text);
    	*/
	}
}
