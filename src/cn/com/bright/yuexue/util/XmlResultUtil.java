package cn.com.bright.yuexue.util;

import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.util.XmlDocPkgUtil;
/**
 * <p>Title:XmlResult复制操作</p>
 * <p>Description: XmlResult复制操作</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/30       1.0          build this moudle </p>
 *     
 */
public class XmlResultUtil {
	/**
	 * 将一个record复制到data
	 * @param data
	 * @param record
	 * @throws Exception
	 */	
	public static void addRecordToData(Element data,Element record) throws Exception{
		addRecordToData(data,record,true);
	}
    /**
     * 将一个record复制到data
     * @param data
     * @param record
     * @param append
     * @throws Exception
     */
	public static void addRecordToData(Element data,Element record,boolean append) throws Exception{		
		Element newRec = new Element("Record");
		List fieldList = record.getChildren();
		for (int j=0;j<fieldList.size();j++){
			Element fieldElement = (Element) fieldList.get(j);
			String fieldName  = fieldElement.getName().trim();
			String fieldValue = fieldElement.getText();
			XmlDocPkgUtil.setChildText(newRec, fieldName, fieldValue);
		}
		if (append){
		    data.addContent(newRec);
		}
		else{
			data.addContent(0, newRec);
		}
	}
	/**
	 * 将List中的Record复制到data节点
	 * @param data
	 * @param list
	 * @throws Exception
	 */
	public static void addListToData(Element data,List list) throws Exception{	
		for (int i=0;i<list.size();i++){
			Element _rec = (Element) list.get(i);
			addRecordToData(data,_rec);			
		}		
	}
}
