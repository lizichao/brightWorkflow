package cn.brightcom.system.pcmc.widetel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.jdom.Document;
import org.jdom.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject; 

import cn.brightcom.jraf.conf.Configuration;
import cn.brightcom.jraf.conf.MenuViewConfiguration;
import cn.brightcom.jraf.util.StringUtil;

public class RealinkInterface {
	
	//读取配置文件
	public static String URL;
	public static String MSGURL;
	public static Configuration viewCfg = MenuViewConfiguration.getXMLConfiguration();
	public static HashMap ht = new HashMap();
	public static Object lock = new Object();
	public static void init(){
		synchronized(lock){
			if(ht.size()>0) return;
			Document doc = (Document)viewCfg.getConfig();
			List feedback = doc.getRootElement().getChild("huixun").getChildren();
			for(int i=0; i<feedback.size(); i++){
				Element cfgData = (Element)feedback.get(i);
				String subject = cfgData.getAttributeValue("method");
				String code = cfgData.getAttributeValue("parameters");
				if("url".equals(cfgData.getText())){
					URL = subject;
				}
				if("sendMsgUrl".equals(cfgData.getText())){
					MSGURL = subject;
				}
				ht.put(cfgData.getText(), subject+";"+code);
				//System.out.println(cfgData.getText()+":method="+subject+";code="+code);
			}
		}
	}
	public static Map<String,String> genHuiXunMessage(String subject){
		if(ht.isEmpty()) init();
		Map<String, String> map = new HashMap<String, String>();
		String message = (String)ht.get(subject);
		if(StringUtil.isEmpty(message)){
			message = "";
		}else{
			String[] mthPar = message.split(";");//(0方法名 1参数名)
			String[] parameters = mthPar[1].split(",");
			map.put("method", mthPar[0]);
			for(int i=0; i<parameters.length; i++){
				map.put("value"+i, parameters[i]);
			}
		}
		return map;
	}
	
	/**
	 * 公共接口(str:json字符串)
	 * 传入参数：json字符串
	 */
	public Map<String, String> commonServiceByJson(String action,String str){
		Map<String, String> backMap = new HashMap<String, String>();
		try{  
            ServiceClient sc = new ServiceClient();  
            Options opts = new Options();
            //获取接口地址
            Map<String,String> map = genHuiXunMessage(action);
            String methods = map.get("method").toString();
            
            EndpointReference end = new EndpointReference(URL);  
            opts.setTo(end);  
            opts.setAction("http://v2.fax.service.sag.bnet.cn/"+methods);  
            sc.setOptions(opts);  
              
            OMFactory fac = OMAbstractFactory.getOMFactory();    
            OMNamespace omNs = fac.createOMNamespace("http://v2.fax.service.sag.bnet.cn/", "");
            //方法名
            OMElement method = fac.createOMElement(methods,omNs);
            System.out.println("调用接口："+methods);
            //参数
            OMElement value = fac.createOMElement(map.get("value0").toString(),omNs);//参数名
            value.setText(str);//参数值
            method.addChild(value); 
           
            OMElement res = sc.sendReceive(method);  
            //调用解析返回值
            backMap = getResults(res);
        }catch(AxisFault e){  
            e.printStackTrace();
            backMap.put("msg", "调用出错");
            return backMap;
        }
        return backMap;
	}
	
	/**
	 * 公共接口(infoMap:各参数的值)
	 * 传入多个参数
	 */
	public Map<String, String> commonServiceByStr(String action,Map<String,Object> infoMap){
		Map<String, String> backMap = new HashMap<String, String>();
		try{  
            ServiceClient sc = new ServiceClient();  
            Options opts = new Options();
            //获取接口地址
            Map<String,String> map = genHuiXunMessage(action);
            //对比 参数 与 参数值 个数是否一致
            if((map.size()-1) != infoMap.size()){
            	backMap.put("msg", "参数有误！");
    			return backMap;
    		}
            String methods = map.get("method").toString();
            
            EndpointReference end = new EndpointReference(URL);  
            opts.setTo(end);  
            opts.setAction("http://v2.fax.service.sag.bnet.cn/"+methods);  
            sc.setOptions(opts);  
              
            OMFactory fac = OMAbstractFactory.getOMFactory();    
            OMNamespace omNs = fac.createOMNamespace("http://v2.fax.service.sag.bnet.cn/", "");
            //方法名
            OMElement method = fac.createOMElement(methods,omNs);
            System.out.println("调用接口："+methods);
            //传入参数
            for(int i =0;i<map.size()-1;i++){
            	String parameter = map.get("value"+i).toString();//获取参数名
            	OMElement parameters = fac.createOMElement(parameter,omNs);//写入参数名
            	parameters.setText(infoMap.get(parameter).toString());//写入参数对应的值
            	method.addChild(parameters);//将参数写入方法
            }
            OMElement res = sc.sendReceive(method);  
            //调用解析返回值
            backMap = getResults(res);
        }catch(AxisFault e){  
            e.printStackTrace();
            backMap.put("msg", "调用出错！");
            return backMap;
        } 
        return backMap;
	}
	
	//解析接口返回值
	public Map<String,String> getResults(OMElement element){
		Map<String, String> map = new HashMap<String, String>();
		if(element==null || "".equals(element)){
			return null;
		}
		Iterator iterator = element.getChildElements();
		Iterator innerItr;
		List<String> list = new ArrayList<String>();
		OMElement result = null;
		while(iterator.hasNext()){
			result = (OMElement)iterator.next();
			innerItr = result.getChildElements();
			while(innerItr.hasNext()){
				OMElement result1 = (OMElement)innerItr.next();
				map.put(result1.getLocalName(), result1.getText());
			}
		}
		return map;	
	}
	
	//java对象转换成json
	public String Object2Json(Object obj){  
        JSONObject json = JSONObject.fromObject(obj);//将java对象转换为json对象  
        String str = json.toString();//将json对象转换为字符串     
        return str;  
    }
	
	//list转换成json
	public String List2Json(Object obj){  
		JSONArray json = JSONArray.fromObject(obj);//将java对象转换为json对象  
        String str = json.toString();//将json对象转换为字符串     
        return str;  
    }
	

}
