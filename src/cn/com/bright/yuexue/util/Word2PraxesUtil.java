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
 * <p>Title:导入Word版整套试卷</p>
 * <p>Description: 批量上传试题</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author lhbo
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
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
     * 清除一些不需要的html标记 
     *  
     * @param htmlStr  带有复杂html标记的html语句 
     * @return 去除了不需要html标记的语句 
     */
    protected static String clearHtmlFormat(String htmlStr) {
    	// 替换字符以及回车换行
    	//htmlStr = htmlStr.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
    	htmlStr = htmlStr.replaceAll("(\r\n|\r|\n|\n\r)", "");
        // 获取body内容的正则
        String bodyReg = "<(BODY|body)(.*?)</(BODY|body)>";
        Pattern bodyPattern = Pattern.compile(bodyReg);  
        Matcher bodyMatcher = bodyPattern.matcher(htmlStr);
        if (bodyMatcher.find()) {
            htmlStr = bodyMatcher.group();  
        }
        
        // 把<P></P>转换成</div></div>保留样式  
        // htmlStr = htmlStr.replaceAll("(<P)([^>]*>.*?)(<\\/P>)","<div$2</div>");  
        // 把<P></P>中的属性全部删除，并删除样式
        //htmlStr = htmlStr.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
        htmlStr = htmlStr.replaceAll("<P[^>]*?>", "<p>").replaceAll("</P>", "</p>");
        
        // 删除不需要的标签  
        htmlStr = htmlStr.replaceAll("<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|SCRIPT|script|COL|col|dir|DIR|[ovwxpOVWXP]:\\w+)[^>]*?>","");
        // 将BR标签格式化
        htmlStr = htmlStr.replaceAll("<[/]?(br|BR)[^>]*?>","<br>");
        // 中文双引号转义
        htmlStr = htmlStr.replaceAll("&ldquo;","“").replaceAll("&rdquo;", "”");
        // 把<U></U>标签转换为8跟下划线
        htmlStr = htmlStr.replaceAll("(<U>)\\s*(<\\/U>)", "________");
        // 删除不需要的属性
        //String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"[^\"]*\"|[^>]+)([^>]*)>";
        String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE)=(?:'[^']*'|\"[^\"]*\"|[^\\s]*)([^>]*)>";
        htmlStr = fiterHtmlTagArr(htmlStr, regxpForHtml);
        
        return htmlStr;
    }
    
    /**
     *  
     * 清除html标签中一些不需要的属性
     *  
     * @param htmlStr 带有复杂html标记的html字符
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
     * 移动图片位置，将转换之后的图片移到指定的images文件夹下
     *  
     * @param htmlCode 带有复杂html标记的html字符
     * @param sourcePicDir 源图片路径
     * @param targetPicDir 目标图片路径
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
     * 图片宽度超过100px时，自动加上P标签自动换行显示
     *  
     * @param htmlCode 带有复杂html标记的html字符
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
            		if(Integer.parseInt(width) > 100){ // 图片宽度超过100px时增加换行
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
    	String regular="<p>(.*?)【题文】(.*?)【结束】</p>";
        String label_pre="(?i)<p>(.*?)【题文】";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null;
        while (m.find()) {
        	question = new PraxesBean();
        	src = m.group();
        	src = "<p>【题文】" + src.replaceAll(label_pre, "").trim();
        	question.setTitle(htmlToQuestions(src,"【题文】","【答案】"));
        	question.setAnswer(htmlToQuestions(src,"【答案】","【解析】"));
        	question.setDesc(htmlToQuestions(src,"【解析】","【结束】"));
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
        	text = text.replaceAll("<p>(<br>|\\s+|)</p>" ,"").trim(); //去掉空行
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
    	String s = "<P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><TABLE DIR=\"LTR\" ALIGN=LEFT WIDTH=320 HSPACE=6 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0><COL WIDTH=89><COL WIDTH=58><COL WIDTH=58><COL WIDTH=58><TR>	<TD WIDTH=89 HEIGHT=7>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>实验次数</FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><SPAN CLASS=\"sd-abs-pos\" STYLE=\"position: absolute; top: 0.08cm; left: 2.07cm; width: 163px\"><IMG SRC=\"2c9ca2865316ca96015316cb1c8d0002_html_688879fe.png\" NAME=\"图片 106\" WIDTH=163 HEIGHT=67 BORDER=0></SPAN><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3</SPAN></FONT></FONT></P>	</TD></TR><TR>	<TD WIDTH=89 HEIGHT=7>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>电压</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><I>U</I></FONT></FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>/V</FONT></FONT></SPAN></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.0</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.0</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3<SPAN ID=\"框2\" DIR=\"LTR\" STYLE=\"float: left; width: 0.82cm; height: 0.6cm; border: none; padding: 0cm; background: #ffffff\">			<P CLASS=\"cjk\" ALIGN=CENTER>&mdash;</P>		</SPAN>.8</SPAN></FONT></FONT>		</P>	</TD></TR><TR>	<TD WIDTH=89 HEIGHT=6>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT SIZE=3>电流</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><I>I</I></FONT></FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>/A</FONT></FONT></SPAN></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.33</SPAN></FONT></FONT></P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><BR>		</P>	</TD>	<TD WIDTH=58>		<P CLASS=\"cjk\" ALIGN=CENTER><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.40</SPAN></FONT></FONT></P>	</TD></TR></TABLE><BR></P>".replaceAll("<P[^>]*?>", "<p>");;
    	System.out.println(s);
    	System.out.println(clearHtmlFormat(s));
    	/*String htmlStr ="";
    	htmlStr = "<HTML><HEAD></HEAD><BODY LANG=\"zh-CN\" TEXT=\"#000000\" DIR=\"LTR\"><P CLASS=\"cjk\" ALIGN=CENTER STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=4 STYLE=\"font-size: 15pt\"><B>2015</B></FONT></SPAN></FONT><FONT SIZE=4 STYLE=\"font-size: 15pt\"><B>深圳市中考物理模拟题（一）</B></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><BR></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3><B>一、单选题（以下各小题有四个选项，其中只有一个选项符合题意，每题</B></FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\"><B>1.5</B></SPAN></FONT></FONT><FONT SIZE=3><B>分，共</B></FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\"><B>24</B></SPAN></FONT></FONT><FONT SIZE=3><B>分）</B></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">15</SPAN></FONT></FONT><FONT SIZE=3>．质量相同的</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>、</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>两件货物在两台吊车钢索的牵引力作用下竖直向上运动，它们运动的</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">s&mdash;t</SPAN></FONT></FONT><FONT SIZE=3>图象分别如图甲、乙所示，则</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <IMG SRC=\"402881215234630b0152346447a00002_html_m6640ba60.png\" NAME=\"图形1\" ALIGN=LEFT HSPACE=12 WIDTH=269 HEIGHT=133 BORDER=0><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>．两件货物都做匀速直线运动</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>．前</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">6</SPAN></FONT></FONT><FONT SIZE=3>秒</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>货物的平均速度小于</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>货物的平均速度   </FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">C</SPAN></FONT></FONT><FONT SIZE=3>．吊车钢索对</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>货物的拉力大于</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">A</SPAN></FONT></FONT><FONT SIZE=3>货物的重力</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">D</SPAN></FONT></FONT><FONT SIZE=3>．</FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">B</SPAN></FONT></FONT><FONT SIZE=3>货物在整个运动过程中受到一对平衡力作用</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>1</FONT></FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>6</FONT></FONT></SPAN></FONT><FONT SIZE=3>．质量为</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>M</FONT></FONT></SPAN></FONT><FONT SIZE=3>的木块</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>在水平桌面上，用轻绳跨过定滑轮与质量为</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>m</FONT></FONT></SPAN></FONT><FONT SIZE=3>的钩码相连，在轻绳的拉力作用下沿桌面做匀速运动，若突然剪断轻绳</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>(</FONT></FONT></SPAN></FONT><FONT SIZE=3>不考虑绳重和绳子勺滑轮的摩擦</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>)</FONT></FONT></SPAN></FONT><FONT SIZE=3>，则</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" NAME=\"图形2\" ALIGN=LEFT HSPACE=12 WIDTH=200 HEIGHT=108 BORDER=0><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>．绳子没有剪断前</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>所受的摩擦力为</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>mg</FONT></FONT></SPAN></FONT><FONT SIZE=3>，方向向右</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>B</FONT></FONT></SPAN></FONT><FONT SIZE=3>．绳子剪断后</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>所受的摩擦力为（</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>M-m)</FONT></FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>g</FONT></FONT></SPAN></FONT><FONT SIZE=3>，方向向右</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>C</FONT></FONT></SPAN></FONT><FONT SIZE=3>．绳子剪断时</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>所受摩擦力为</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>mg</FONT></FONT></SPAN></FONT><FONT SIZE=3>，方向向左</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>D</FONT></FONT></SPAN></FONT><FONT SIZE=3>．绳子剪断后</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>受的摩擦乃将越来越大</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>1</FONT></FONT><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>7</FONT></FONT></SPAN></FONT><FONT SIZE=3>．歼</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>-20</FONT></FONT></SPAN></FONT><FONT SIZE=3>是成都飞机工业集团为中国人民解放军研制的最新一代</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>(</FONT></FONT></SPAN></FONT><FONT SIZE=3>欧美叫标准为第四代，新标准以及俄罗斯标准为第五代</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>)</FONT></FONT></SPAN></FONT><FONT SIZE=3>双发重型隐形战斗机，下列说法中正确的是</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_m34337222.png\" NAME=\"图形3\" ALIGN=LEFT HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>A</FONT></FONT></SPAN></FONT><FONT SIZE=3>．歼</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>具有隐身功能，是因为雷达发出的电磁波会被它表面反射</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>B</FONT></FONT></SPAN></FONT><FONT SIZE=3>．歼</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>在加速升空时，机械能守恒</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>C</FONT></FONT></SPAN></FONT><FONT SIZE=3>．歼</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>在空中匀速飞行时，机械能不变，不消耗能量</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>D</FONT></FONT></SPAN></FONT><FONT SIZE=3>．歼</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>20</FONT></FONT></SPAN></FONT><FONT SIZE=3>在空中加速俯冲投弹时，能量守恒</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-left: 0.18cm; margin-bottom: 0cm; line-height: 0.55cm; widows: 2; orphans: 2\"><BR></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">22</SPAN></FONT></FONT><FONT SIZE=3>、灯泡</FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">L</SPAN></FONT></FONT><FONT SIZE=3>上标有&ldquo;</FONT><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.5V&rdquo;</SPAN></FONT></FONT><FONT SIZE=3>的字样，它的电阻随它两端电压的变化关系如图所示。下列说法中正确的是</FONT></P><P CLASS=\"cjk\" ALIGN=CENTER STYLE=\"margin-bottom: 0cm\"><IMG SRC=\"402881215234630b0152346447a00002_html_84bea23.png\" NAME=\"图形4\" ALIGN=BOTTOM WIDTH=308 HEIGHT=223 BORDER=0></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>A</FONT></SPAN></FONT><FONT SIZE=3>．灯泡</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>的额定电流是</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>0.2A    B</FONT></SPAN></FONT><FONT SIZE=3>．灯泡</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>正常发光时的电阻是</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>10</FONT><FONT SIZE=3>&Omega;</FONT></SPAN></FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"> <FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>C</FONT></SPAN></FONT><FONT SIZE=3>．灯泡</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>L</FONT></SPAN></FONT><FONT SIZE=3>的额定功率是</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>6.25W   D</FONT></SPAN></FONT><FONT SIZE=3>．灯丝的电阻随电压的增大而减小</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><SPAN LANG=\"en-US\">23</SPAN></FONT></FONT><FONT SIZE=3>．如图所示电路，闭合开关Ｓ后，甲乙两表是电压表，示数之比是３：２，当开关Ｓ断开，甲乙两表是电流表，则两表的示数之比为</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><TABLE DIR=\"LTR\" ALIGN=LEFT WIDTH=592 HSPACE=6 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0>	<COL WIDTH=99>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=66>	<COL WIDTH=52>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=9>			<P CLASS=\"cjk\"><FONT SIZE=3>电压</FONT><FONT SIZE=3> </FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>U/V</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>1.0</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1.5</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.0</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">2.5</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.0</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">3.8</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">4.0</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<P CLASS=\"cjk\"><FONT SIZE=3>电流</FONT><FONT SIZE=3> </FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>I/A			</FONT></FONT></SPAN></FONT>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.18</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.2</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.22</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.25</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.28</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm; margin-bottom: 0cm\"><BR>			</P>			<P CLASS=\"cjk\"><U>          </U>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.32</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<P CLASS=\"cjk\"><FONT SIZE=3>电功率</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3>P/</FONT></FONT></SPAN></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.42cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.18</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.3</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.44</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.625</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">0.84</SPAN></FONT></FONT></P>		</TD>		<TD WIDTH=66>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm; margin-bottom: 0cm\"><BR>			</P>			<P CLASS=\"cjk\"><U>        </U>			</P>		</TD>		<TD WIDTH=52>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT FACE=\"宋体, SimSun\"><FONT SIZE=3><SPAN LANG=\"en-US\">1.28</SPAN></FONT></FONT></P>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=31>			<P CLASS=\"cjk\"><FONT SIZE=3>灯泡发光情况</FONT></P>		</TD>		<TD COLSPAN=7 WIDTH=463>			<P CLASS=\"cjk\" STYLE=\"text-indent: 0.21cm\"><FONT SIZE=3>很暗</FONT><FONT SIZE=3>			</FONT><FONT SIZE=3>&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;</FONT><FONT SIZE=3>			</FONT><FONT SIZE=3>暗</FONT><FONT SIZE=3>   </FONT><FONT SIZE=3>&mdash;&mdash;&mdash;&mdash;&rarr;			 </FONT><FONT SIZE=3>    </FONT><FONT SIZE=3>正常发光</FONT><FONT SIZE=3>			 </FONT><FONT SIZE=3>&rarr;</FONT><FONT SIZE=3> </FONT><FONT SIZE=3>很亮</FONT></P>		</TD>	</TR></TABLE> </P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3>Ａ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>　</FONT><FONT SIZE=3><IMG SRC=\"402881215234630b0152346447a00002_html_2e1b9152.gif\" NAME=\"对象1\" ALIGN=ABSMIDDLE WIDTH=88 HEIGHT=40></FONT><FONT SIZE=3>　     Ｂ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>３：１　　　　</FONT></P><P CLASS=\"cjk\" STYLE=\"margin-bottom: 0cm\"><FONT SIZE=3>Ｃ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>２：３　　　　　     Ｄ</FONT><FONT FACE=\"Times New Roman, serif\"><SPAN LANG=\"en-US\"><FONT SIZE=3>.</FONT></SPAN></FONT><FONT SIZE=3>１：３</FONT></P><P CLASS=\"cjk\" ALIGN=LEFT STYLE=\"margin-bottom: 0cm; background: #ffffff; line-height: 150%; widows: 2; orphans: 2\"><BR></P><DIV TYPE=FOOTER>	<P ALIGN=CENTER STYLE=\"margin-top: 0.69cm; margin-bottom: 0cm\"><SDFIELD TYPE=PAGE SUBTYPE=RANDOM FORMAT=PAGE><FONT FACE=\"Times New Roman, serif\">2</FONT></SDFIELD></P>	<P ALIGN=LEFT STYLE=\"margin-bottom: 0cm\"><BR>	</P></DIV></BODY></HTML>";
    	//System.out.println(clearFormat(htmlStr,"D:\1"));
    	
    	htmlStr = "<DIV LANG=\"zh-CN\" TEXT=\"#000000\" ><p><B>2015</B><B>深圳市中考物理模拟题（一）</B></p><p><BR></p><p><B>一、单选题（以下各小题有四个选项，其中只有一个选项符合题意，每题</B><B>1.5</B><B>分，共</B><B>24</B><B>分）</B></p><p>15．【题文】质量相同的A、B两件货物在两台吊车钢索的牵引力作用下竖直向上运动，它们运动的s&mdash;t图象分别如图甲、乙所示，则</p><p> <IMG SRC=\"402881215234630b0152346447a00002_html_m6640ba60.png\" NAME=\"图形1\" ALIGN=LEFT HSPACE=12 WIDTH=269 HEIGHT=133 BORDER=0>A．两件货物都做匀速直线运动</p><p> B．前6秒A货物的平均速度小于B货物的平均速度   </p><p> C．吊车钢索对A货物的拉力大于A货物的重力</p><p> D．B货物在整个运动过程中受到一对平衡力作用</p><p>【答案】A</p><p>【解析】第15题解析</p><p>【结束】</p><p>16．【题文】质量为M的木块A在水平桌面上，用轻绳跨过定滑轮与质量为m的钩码相连，在轻绳的拉力作用下沿桌面做匀速运动，若突然剪断轻绳(不考虑绳重和绳子勺滑轮的摩擦)，则</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" NAME=\"图形2\" ALIGN=LEFT HSPACE=12 WIDTH=200 HEIGHT=108 BORDER=0>A．绳子没有剪断前A所受的摩擦力为mg，方向向右</p><p>B．绳子剪断后A所受的摩擦力为（M-m)g，方向向右</p><p>C．绳子剪断时A所受摩擦力为mg，方向向左</p><p>D．绳子剪断后A受的摩擦乃将越来越大</p><p>【答案】D</p><p>【解析】第16题解析</p><p>【结束】</p><p>17．【题文】歼-20是成都飞机工业集团为中国人民解放军研制的最新一代(欧美叫标准为第四代，新标准以及俄罗斯标准为第五代)双发重型隐形战斗机，下列说法中正确的是</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_m34337222.png\" NAME=\"图形3\" ALIGN=LEFT HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0>A．歼20具有隐身功能，是因为雷达发出的电磁波会被它表面反射</p><p>B．歼20在加速升空时，机械能守恒</p><p>C．歼20在空中匀速飞行时，机械能不变，不消耗能量</p><p>D．歼20在空中加速俯冲投弹时，能量守恒</p><p><BR></p><p>【答案】B</p><p>【解析】第17题解析</p><p>【结束】</p><p>22、【题文】灯泡L上标有&ldquo;2.5V&rdquo;的字样，它的电阻随它两端电压的变化关系如图所示。下列说法中正确的是</p><p><IMG SRC=\"402881215234630b0152346447a00002_html_84bea23.png\" NAME=\"图形4\" ALIGN=BOTTOM WIDTH=308 HEIGHT=223 BORDER=0></p><p> A．灯泡L的额定电流是0.2A    B．灯泡L正常发光时的电阻是10&Omega;</p><p> C．灯泡L的额定功率是6.25W   D．灯丝的电阻随电压的增大而减小</p><p>【答案】C</p><p>【解析】第22题解析</p><p>【结束】</p><p>23．【题文】如图所示电路，闭合开关Ｓ后，甲乙两表是电压表，示数之比是３：２，当开关Ｓ断开，甲乙两表是电流表，则两表的示数之比为</p><p><TABLE ORDER>	<COL WIDTH=99>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=52>	<COL WIDTH=66>	<COL WIDTH=52>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=9>			<P >电压 U/V</p>		</TD>		<TD WIDTH=52>			<p>1.0</p>		</TD>		<TD WIDTH=52>			<p>1.5</p>		</TD>		<TD WIDTH=52>			<p>2.0</p>		</TD>		<TD WIDTH=52>			<p>2.5</p>		</TD>		<TD WIDTH=52>			<p>3.0</p>		</TD>		<TD WIDTH=66>			<p>3.8</p>		</TD>		<TD WIDTH=52>			<p>4.0</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<p>电流 I/A						</p>		</TD>		<TD WIDTH=52>			<p>0.18</p>		</TD>		<TD WIDTH=52>			<p>0.2</p>		</TD>		<TD WIDTH=52>			<p>0.22</p>		</TD>		<TD WIDTH=52>			<p>0.25</p>		</TD>		<TD WIDTH=52>			<p>0.28</p>		</TD>		<TD WIDTH=66>			<p><BR>			</p>			<p><U>          </U>			</p>		</TD>		<TD WIDTH=52>			<p>0.32</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=33>			<p>电功率P/</p>		</TD>		<TD WIDTH=52>			<p>0.18</p>		</TD>		<TD WIDTH=52>			<p>0.3</p>		</TD>		<TD WIDTH=52>			<p>0.44</p>		</TD>		<TD WIDTH=52>			<p>0.625</p>		</TD>		<TD WIDTH=52>			<p>0.84</p>		</TD>		<TD WIDTH=66>			<p><BR>			</p>			<p><U>        </U>			</p>		</TD>		<TD WIDTH=52>			<p>1.28</p>		</TD>	</TR>	<TR VALIGN=TOP>		<TD WIDTH=99 HEIGHT=31>			<p>灯泡发光情况</p>		</TD>		<TD COLSPAN=7 WIDTH=463>			<p>很暗			&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;			暗   &mdash;&mdash;&mdash;&mdash;&rarr;			     正常发光			 &rarr; 很亮</p>		</TD>	</TR></TABLE> </P><p>Ａ.　<IMG SRC=\"402881215234630b0152346447a00002_html_2e1b9152.gif\" NAME=\"对象1\" ALIGN=ABSMIDDLE WIDTH=88 HEIGHT=40>　     Ｂ.３：１　　　　</p><p>Ｃ.２：３　　　　　     Ｄ.１：３</p><p>【答案】A</p><p>【解析】第23题解析</p><p>【结束】</p></DIV>";
    	List<PraxesBean> questionList = htmlToQuestionList(htmlStr);
    	for(PraxesBean question : questionList){
    		System.out.println("Title:"+question.getTitle());
    		System.out.println("Type:"+question.getType());
    		System.out.println("Answer:"+question.getAnswer());
    		System.out.println("Desc:"+question.getDesc());
    	}*/
    	
    	//String s="text:<p>一个四棱锥和一．设四棱锥、三棱锥、三棱柱的高分别为，，，<p>sdfd</p>则（　）</p><p><A></A>【答案】";
    	//System.out.println(s.replaceAll("<p>(?!.*(</p>)).*【答案】", ""));
    	
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
		System.out.println("A,B.C、D.e。f<p><br></p>".replaceAll("(<p><br></p>$)" ,""));
		String html = "<IMG SRC=\"402881215234630b0152346447a00002_html_m15663f5b.png\" name=\"图形2\" align=\"LEFT\" hspace=\"12\" width=\"200\" height=\"108\" border=\"0\" />";
		System.out.println(html.replaceAll("<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|hspace|HSPACE|name:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>","<$1$2>"));
		html = html.replaceAll("<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|hspace|HSPACE|name|align:\\w+)=(?:'\")","<$1$2>");
		System.out.println(html);
		
		String html = "<p>如图所示电路，闭合开关Ｓ后，甲乙两表是电压表，示数之比是３：２，当开关Ｓ断开，甲乙两表是电流表，则两表的示数之比为</p>   <p></p>   <table order=\"\"> <br>    <colgroup><br>     <col width=\"99\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"52\" /> <br>     <col width=\"66\" /> <br>     <col width=\"52\" /> <br>    </colgroup><br>    <tBODY><br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"9\"> <p>电压 U/V</p> </td> <br>      <td width=\"52\"> <p>1.0</p> </td> <br>      <td width=\"52\"> <p>1.5</p> </td> <br>      <td width=\"52\"> <p>2.0</p> </td> <br>      <td width=\"52\"> <p>2.5</p> </td> <br>      <td width=\"52\"> <p>3.0</p> </td> <br>      <td width=\"66\"> <p>3.8</p> </td> <br>      <td width=\"52\"> <p>4.0</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"33\"> <p>电流 I/A </p> </td> <br>      <td width=\"52\"> <p>0.18</p> </td> <br>      <td width=\"52\"> <p>0.2</p> </td> <br>      <td width=\"52\"> <p>0.22</p> </td> <br>      <td width=\"52\"> <p>0.25</p> </td> <br>      <td width=\"52\"> <p>0.28</p> </td> <br>      <td width=\"66\"> <p><br> </p> <p><u> </u> </p> </td> <br>      <td width=\"52\"> <p>0.32</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"33\"> <p>电功率P/</p> </td> <br>      <td width=\"52\"> <p>0.18</p> </td> <br>      <td width=\"52\"> <p>0.3</p> </td> <br>      <td width=\"52\"> <p>0.44</p> </td> <br>      <td width=\"52\"> <p>0.625</p> </td> <br>      <td width=\"52\"> <p>0.84</p> </td> <br>      <td width=\"66\"> <p><br> </p> <p><u> </u> </p> </td> <br>      <td width=\"52\"> <p>1.28</p> </td> <br>     </tr> <br>     <tr valign=\"TOP\"> <br>      <td width=\"99\" height=\"31\"> <p>灯泡发光情况</p> </td> <br>      <td colspan=\"7\" width=\"463\"> <p>很暗 ——————→ 暗 ————→ 正常发光 → 很亮</p> </td> <br>     </tr><br>    </tBODY><br>   </table> <br>   <p></p>   <p>Ａ.　<IMG SRC=\"/images/praxe/201601/402881215234630b0152346447a00002_html_2e1b9152.gif\">　 Ｂ.３：１　　　　</p>   <p>Ｃ.２：３　　　　　 Ｄ.１：３</p>";
		//System.out.println(stripHtml(html));
		System.out.println("---\n");
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		html = stripHtml(html);
		System.out.println(stripHtml(html));
		
		String htmlStr2 = "<IMG SRC=\"/images/praxe/201601/402881215234630b0152346447a00002_html_m34337222.png\" class='s c' NAME=\"图形3\" ALIGN='LEFT' HSPACE=12 WIDTH=204 HEIGHT=137 BORDER=0>";
		String regxpForHtml = "<([^>]*)\\s+(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|dir|DIR|name|NAME|align|ALIGN|hspace|HSPACE)=(?:'[^']*'|\"[^\"]*\"|[^\\s]*)([^>]*)>";
        htmlStr2 = fiterHtmlTagArr(htmlStr2, regxpForHtml);*/
    	
    	/*
    	String text = "<p><TABLE WIDTH=592 BORDER=1 BORDERCOLOR=\"#000000\" CELLPADDING=7 CELLSPACING=0><br>	<COL WIDTH=99><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=52><br>	<COL WIDTH=66><br>	<COL WIDTH=52><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=9><br>			<P>电压 U/V</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.0</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.5</p>		</TD><br>		<TD WIDTH=52><br>			<p>2.0</p>		</TD><br>		<TD WIDTH=52><br>			<p>2.5</p>		</TD><br>		<TD WIDTH=52><br>			<p>3.0</p>		</TD><br>		<TD WIDTH=66><br>			<p>3.8</p>		</TD><br>		<TD WIDTH=52><br>			<p>4.0</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=33><br>			<p>电流 I/A<br>			<br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.18</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.2</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.22</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.25</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.28</p>		</TD><br>		<TD WIDTH=66><br>			<p><br><br>			</p>			<p><U>          </U><br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.32</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=33><br>			<p>电功率P/</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.18</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.3</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.44</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.625</p>		</TD><br>		<TD WIDTH=52><br>			<p>0.84</p>		</TD><br>		<TD WIDTH=66><br>			<p><br><br>			</p>			<p><U>        </U><br>			</p>		</TD><br>		<TD WIDTH=52><br>			<p>1.28</p>		</TD><br>	</TR><br>	<TR VALIGN=TOP><br>		<TD WIDTH=99 HEIGHT=31><br>			<p>灯泡发光情况</p>		</TD><br>		<TD COLSPAN=7 WIDTH=463><br>			<p>很暗<br>			&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&rarr;<br>			暗   &mdash;&mdash;&mdash;&mdash;&rarr;<br>			     正常发光<br>			 &rarr; 很亮</p>		</TD><br>	</TR><br></TABLE> <br></P><br><p><IMG SRC=\"/images/praxe/201601/402882ea525edc8601525ee193e40024_html_14f417b8.gif\" WIDTH=88 HEIGHT=40></p>";
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
