package cn.com.bright.edu.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.message.resp.Article;
import cn.com.bright.edu.weixin.message.resp.MusicMessage;
import cn.com.bright.edu.weixin.message.resp.NewsMessage;
import cn.com.bright.edu.weixin.message.resp.RespTextMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * ������Ϣ��
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class MessageUtil {
	private Log log4j = new Log(this.getClass().toString());
	// ������
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	public static final String REQ_MESSAGE_TYPE_LINK = "link";
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	
	// ������
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public static final String EVENT_TYPE_CLICK = "CLICK";
	public static final String EVENT_TYPE_TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";

	/**
	 * ƴװ��Ϣ�����
	 * @param request
	 * @return
	 */
	@Deprecated
	public Map<String, String> parseXml2(HttpServletRequest request){
		Map<String, String> requestMap = new HashMap<String, String>();
		ServletInputStream in = null;
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader=null;
		try{
			in = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line=null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
	    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=reader){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String postStr = buffer.toString();
		if (StringUtil.isEmpty(postStr)){
			Document document=null;
			try{
				document = DocumentHelper.parseText(postStr);
				if(document==null) return requestMap;
			}catch(Exception e){
				log4j.logError("[ƴװ��Ϣ�����]:"+postStr);
				log4j.logError(e);
				e.printStackTrace();
			}
			Element root = document.getRootElement();
			for(Iterator its =  root.elements().iterator();its.hasNext();){   
	            Element chileEle = (Element)its.next();
	            //System.out.println(chileEle.getName()+" <==> "+chileEle.getTextTrim());
	            requestMap.put(chileEle.getName(), chileEle.getTextTrim());
	        }
		}
		return requestMap;
	}
	
	/**
	 * ����΢�ŷ���������XML��
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// ����������洢��HashMap��
		Map<String, String> map = new HashMap<String, String>();

		// ��request��ȡ��������
		InputStream inputStream = request.getInputStream();
		// ��ȡ������
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// �õ�xml��Ԫ��
		Element root = document.getRootElement();
		// �õ���Ԫ�ص������ӽڵ�
		List<Element> elementList = root.elements();

		// ���������ӽڵ�
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
		
		// �ͷ���Դ
		inputStream.close();
		inputStream = null;
		
		return map;
	}
	
	/**
	 * ȥ���ַ����еĿո񡢻س������з����Ʊ��
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * �������utf-8���뷽ʽʱ�ַ�����ռ�ֽ���
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// ���ֲ���utf-8����ʱռ3���ֽ�
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	/**
     * ������ת��Ϊxml��ʽ�ַ���
     * @param textMessage �ı���Ϣ����
     * @return
     */
	@Deprecated
	public static String textMessageToXml2(RespTextMessage textMessage){
		String textTpl = "<xml>"+
		"<ToUserName><![CDATA[%1$s]]></ToUserName>"+
		"<FromUserName><![CDATA[%2$s]]></FromUserName>"+
		"<CreateTime>%3$s</CreateTime>"+
		"<MsgType><![CDATA[%4$s]]></MsgType>"+
		"<Content><![CDATA[%5$s]]></Content>"+
		"<FuncFlag>0</FuncFlag>"+
		"</xml>";
    	String resultStr = textTpl.format(textTpl, textMessage.getToUserName(), textMessage.getFromUserName(), textMessage.getCreateTime(), textMessage.getMsgType(), textMessage.getContent());
    	return resultStr;
	}
	
    /**
     * ����XStream��ʽ��װXML
     * @param textMessage �ı���Ϣ����
     * @return
     */
	public static String textMessageToXml(RespTextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
     * ����XStream��ʽ��װXML
     * @param musicMessage ������Ϣ����
     * @return
     */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	/**
     * ����XStream��ʽ��װXML
     * @param newsMessage ͼ����Ϣ����
     * @return
     */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
     * ��չxstream��ʹ��֧��CDATA��
     * @return
     */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// ������xml�ڵ��ת��������CDATA���
				boolean cdata = true;
				
				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
