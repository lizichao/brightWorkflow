package cn.com.bright.workflow.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SayHelloClient {
    public static String endpoint = "http://203.91.55.69/services/Sms?wsdl";
    public static String username = "128";
    public static String password = "Szjyj_Sms@2015";
    public static String batch = "01";

    /**
     * 发送短信
     * @param mobile 接收短信的手机号码
     * @param content 短信内容
     * @return <?xml version="1.0" encoding="utf-8"?> <response> <head>
     *         <code>0</code> <message/> </head> <body>
     *         <msgid>15012631828,201309161226244740</msgid> <reserve/> </body>
     *         </response>
     */
    @SuppressWarnings("unchecked")
    public static Element getElementSendSMS(String mobile, String content) {
        // 将解析结果存储在Element中
        Element el = null;
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();

            // 发送短信
            call.setOperationName("InsertDownSms");
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            StringBuffer bodysb = new StringBuffer();
            bodysb.append("<sendbody><message><orgaddr></orgaddr>");
            bodysb.append("<mobile>").append(mobile).append("</mobile>");
            bodysb.append("<content>").append(content).append("</content>");
            bodysb
                .append("<sendtime></sendtime><needreport>0</needreport></message><publicContent></publicContent></sendbody>");
            String results = (String) call.invoke(new Object[] { Azdg.getJiami(username),
                    Azdg.getJiami(password), batch, bodysb.toString() });
            results.length();
            System.out.println("return value is " + results);
            el = parseXml(results);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return el;
    }

    /**
     * 发送短信
     * @param mobile 接收短信的手机号码
     * @param content 短信内容
     * @return msgid 短信唯一标识
     */
    @SuppressWarnings("unchecked")
    public static String sendSMS(String mobile, String content) {
        String msgid = "";
        try {
            Element el = getElementSendSMS(mobile, content);
            String s = el.element("body").element("msgid").getTextTrim();
            msgid = s.split(",")[1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return msgid;
    }

    /**
     * 根据短信唯一标识获取短信发送状态
     * @param msgid
     * @return <?xml version="1.0" encoding="utf-8"?> <response> <head>
     *         <code>0</code> <message/> </head> <body>
     *         <smsresultcnt>1</smsresultcnt> <smsresult>
     *         <msgid>201309161226244740</msgid> <status>0</status>
     *         <msgstatus>0</msgstatus> <resultmsg>0,3,CMPP2.0</resultmsg>
     *         <senttime>2013-09-16 12:29:04.74</senttime> <reserve/>
     *         </smsresult> </body> </response>
     */
    @SuppressWarnings("unchecked")
    public static Element getElementSMSStatus(String msgid) {
        // 将解析结果存储在Element中
        Element el = null;
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();

            // 查询发送状态
            call.setOperationName("getSpecialDownSmsResult");
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            String results = (String) call.invoke(new Object[] { Azdg.getJiami(username),
                    Azdg.getJiami(password), batch, msgid });
            results.length();
            // System.out.println("return value is " + results);
            el = parseXml(results);
        } catch (Exception ex) {
            ex.printStackTrace();
            el = null;
        }
        return el;
    }

    /**
     * 根据短信唯一标识获取短信发送状态
     * @param msgid
     * @return status 0是成功，其他失败
     */
    @SuppressWarnings("unchecked")
    public static String getSMSStatus(String msgid) {
        String status = "-1";
        try {
            Element el = getElementSMSStatus(msgid);
            Element smsresult = el.element("body").element("smsresult");
            if (smsresult != null) {
                status = smsresult.element("status").getTextTrim();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * 根据短信唯一标识获取短信发送时间
     * @param msgid
     * @return senttime
     */
    @SuppressWarnings("unchecked")
    public static String getSMSSenttime(String msgid) {
        String senttime = null;
        try {
            Element el = getElementSMSStatus(msgid);
            Element smsresult = el.element("body").element("smsresult");
            if (smsresult != null) {
                String status = smsresult.element("status").getTextTrim();
                if ("0".equals(status)) {
                    senttime = smsresult.element("senttime").getTextTrim();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return senttime;
    }

    /**
     * 解析返回回来的的请求（XML）
     * @param results
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Element parseXml(String results) {
        // 将解析结果存储在Element中
        Element el = null;
        InputStream inputStream = null;
        try {
            // 从results字符串中取得输入流
            inputStream = new ByteArrayInputStream(results.getBytes());
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            el = document.getRootElement();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            // 释放资源
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
                inputStream = null;
            }
        }
        return el;
    }

    public static void main(String[] args) {
        try {
            /*
             * String el = sendSMS("15012631828","您好！");
             * System.out.println(getSMSStatus(el));
             * System.out.println(getSMSSenttime(el));
             */
            /*
             * Service service = new Service(); Call call = null; call = (Call)
             * service.createCall(); //采用AZDG方式加密 String username =
             * Azdg.getJiami("08661"); String password =
             * Azdg.getJiami("Szjyj_Sms@2015"); //发送短信
             * call.setOperationName("InsertDownSms");
             * call.setTargetEndpointAddress(new java.net.URL(endpoint)); String
             * boay =
             * "<sendbody><message><orgaddr></orgaddr><mobile>15012631828</mobile><content>测试</content><sendtime></sendtime><needreport>0</needreport></message><publicContent></publicContent></sendbody>"
             * ; String ret1 = (String) call.invoke(new Object[] { username,
             * password, "", boay }); ret1.length();
             * System.out.println("return value is " + ret1); //<?xml
             * version="1.0" encoding="GBK"
             * ?><response><head><code>0</code><message
             * ></message></head><body><msgid
             * >15012631828,201309161226244740</msgid
             * ><reserve></reserve></body></response>
             */
            // 查询发送状态
            // call.setOperationName("getSpecialDownSmsResult");
            // call.setTargetEndpointAddress (new java.net.URL(endpoint));
            // String ret2 = (String) call.invoke(new Object[]
            // {username,password,"","201309161226244740"});
            // ret2.length();
            // System.out.println("return value is " + ret2);
            // <?xml version="1.0" encoding="GBK"
            // ?><response><head><code>0</code><message></message></head><body><smsresultcnt>1</smsresultcnt><smsresult><msgid>201309161226244740</msgid><status>0</status><msgstatus>0</msgstatus><resultmsg>0,3,CMPP2.0</resultmsg><senttime>2013-09-16
            // 12:29:04.74</senttime><reserve></reserve></smsresult></body></response>

            // 下发短信状态报告
            // call.setOperationName("getDownSmsResult");
            // call.setTargetEndpointAddress (new java.net.URL(endpoint));
            //
            // String ret2 = (String) call.invoke(new Object[]
            // {username,password,"","10"});
            // ret2.length();
            // System.out.println("return value is " + ret2);
            // <?xml version="1.0" encoding="GBK"
            // ?><response><head><code>0</code><message></message></head><body><smsresultcnt>0</smsresultcnt></body></response>

            // //批量获取上行
            // call.setOperationName("getUpSms");
            // call.setTargetEndpointAddress (new java.net.URL(endpoint));
            //
            // String ret2 = (String) call.invoke(new Object[]
            // {username,password,""});
            // ret2.length();
            // System.out.println("return value is " + ret2);
            // <?xml version="1.0" encoding="GBK"
            // ?><response><head><code>0</code><message></message></head><body><sms><telno>13922208741</telno><destaddr>1065730102480000</destaddr><content>内容</content><msgid>30</msgid><receivetime>20130621175622</receivetime><reserve></reserve></sms></body></response>
            /*
             * //上行确认 call.setOperationName("RspUpSms");
             * call.setTargetEndpointAddress (new java.net.URL(endpoint));
             * String ret2 = (String) call.invoke(new Object[]
             * {username,password,"30"}); ret2.length();
             * System.out.println("return value is " + ret2); //<?xml
             * version="1.0" encoding="GBK"
             * ?><response><head><code>0</code><message
             * ></message></head></response>
             */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
