package cn.com.bright.yuexue.upload;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.io.PrintWriter;
import java.io.PushbackInputStream;  
import java.io.RandomAccessFile;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.net.URLDecoder;
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Date;  
import java.util.HashMap;  
import java.util.List;
import java.util.Map;  
import java.util.Properties;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  

import net.sf.json.JSONObject;

import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.MD5FileUtil;

public class SocketServer  {


	private String uploadPath=cn.brightcom.jraf.util.FileUtil.getWebPath();  
	private ExecutorService executorService;// �̳߳�  
	private ServerSocket ss = null;  
	private int port;// �����˿�  
	private boolean quit;// �Ƿ��˳�  
	private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();// ��Ŷϵ����ݣ���ø�Ϊ���ݿ���  

	public SocketServer(int port) {  
		this.port = port;  
		// ��ʼ���̳߳�  
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()  
				.availableProcessors() * 5);  
	}  

	// ��������  
	public void start() throws Exception {  
		ss = new ServerSocket(port);  
		while (!quit) {  
			Socket socket = ss.accept();// ���ܿͻ��˵�����  
			// Ϊ֧�ֶ��û��������ʣ������̳߳ع���ÿһ���û�����������  
			executorService.execute(new SocketTask(socket));// ����һ���߳�����������  
		}  
	}  

	// �˳�  
	public void quit() {  
		this.quit = true;  
		try {  
			ss.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}  

	public static void main(String[] args) throws Exception {  
		SocketServer server = new SocketServer(7858);  
		server.start();  
	}  

	private class SocketTask implements Runnable {  
		private Socket socket;  

		public SocketTask(Socket socket) {  
			this.socket = socket;  
		}  

		@Override  
		public void run() {  
			try {  
				System.out.println("accepted connenction from "  
						+ socket.getInetAddress() + " @ " + socket.getPort());  
			 System.out.println(" ��ǰ�̣߳�"+Thread.currentThread().getName());
				PushbackInputStream inStream = new PushbackInputStream(  
						socket.getInputStream());  
				// �õ��ͻ��˷����ĵ�һ��Э�����ݣ�Content-Length=143253434;filename=xxx.3gp;sourceid=  
				// ����û������ϴ��ļ���sourceid��ֵΪ�ա�  
				String head = StreamTool.readLine(inStream);  
				System.out.println(head);  
				if (head != null) {  
					// �����Э�������ж�ȡ���ֲ���ֵ  
					String[] items = head.split(";");  
					String request = items[0].substring(items[0].indexOf("?") + 1);  
					String[] parameterNames=request.split("&");
					Element reqData = new Element("Data");
					for (int i = 0; i < parameterNames.length; i++) {
						String name = parameterNames[i].substring(0,parameterNames[i].indexOf("="));
						String value =new String(parameterNames[i].substring(parameterNames[i].indexOf("=")+1).getBytes("iso-8859-1"),"utf-8");
						XmlDocPkgUtil.setChildText(reqData, name,  URLDecoder.decode(value,"UTF-8"));
					} 
					File file = null;  
					int position = 0;
					String fn = reqData.getChildText("fileName");		
					String fileName = fn;
					//�ļ���׺��
					String ext ="";
					String file_type="";
					if (fn.lastIndexOf(".") != -1){
						ext = fn.substring(fn.lastIndexOf("."));	
						file_type=fn.substring(fn.lastIndexOf(".")+1);	
					}
					
					String sourceid = items[1].substring(items[1].indexOf("=") + 1);  
					Long id = System.currentTimeMillis();  
					FileLog log = null;  
					if (null != sourceid && !"".equals(sourceid)) {  
						id = Long.valueOf(sourceid);  
						log = find(id);//�����ϴ����ļ��Ƿ�����ϴ���¼  
					} 
					//�ļ�·��,�ļ���
					String filepath = ""; 
					if(log==null){//����ϴ����ļ��������ϴ���¼,Ϊ�ļ���Ӹ��ټ�¼  
						//ÿ��ÿ�´���һ���ļ���
					    String currDate = DatetimeUtil.getCurrentDate();
						//�ļ�·����Ŀ¼
						String path ="";
						//ʵ��·��������������UUID����
						try {
							fn = new UUIDHexGenerator().generate(null)+ext;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						 if ("comment".equals(reqData.getChildText("uploadsource"))){
							 path = "/upload/comment/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
					     }else if("exam".equals(reqData.getChildText("uploadsource"))){
					    	path = "/upload/MP3/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
					     }else{
					    	path = "/upload/video/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
					     }
						
						File dir = new File(uploadPath+ path);  
						if(!dir.exists()) dir.mkdirs();
						filepath=path+fn;
						file = new File(dir, fn);    
						save(id, file);  
					}else{// ����ϴ����ļ������ϴ���¼,��ȡ�ϴεĶϵ�λ��  
						file = new File(log.getPath());//���ϴ���¼�еõ��ļ���·��  
						filepath=log.getPath().replaceAll("\\\\", "/");
						filepath=filepath.substring(filepath.indexOf("/upload"));
						if(file.exists()){  
							File logFile = new File(file.getParentFile(), file.getName()+".log");  
							if(logFile.exists()){  
								Properties properties = new Properties();  
								properties.load(new FileInputStream(logFile));  
								position = Integer.valueOf(properties.getProperty("length"));//��ȡ�ϵ�λ��  
							}  
						}  
					}  

					OutputStream outStream = socket.getOutputStream();  
					String response = "sourceid="+ id+ ";position="+ position+ "\r\n";  
					//�������յ��ͻ��˵�������Ϣ�󣬸��ͻ��˷�����Ӧ��Ϣ��sourceid=1274773833264;position=0  
					//sourceid�ɷ������ɣ�Ψһ��ʶ�ϴ����ļ���positionָʾ�ͻ��˴��ļ���ʲôλ�ÿ�ʼ�ϴ�  
					outStream.write(response.getBytes());  

					RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");  
					if(position==0) fileOutStream.setLength(Integer.valueOf(reqData.getChildText("size")));//�����ļ�����  
					fileOutStream.seek(position);//�ƶ��ļ�ָ����λ�ÿ�ʼд������  
					byte[] buffer = new byte[1024];  
					int len = -1;  
					int length = position;  
					while( (len=inStream.read(buffer)) != -1){//���������ж�ȡ����д�뵽�ļ���  
						fileOutStream.write(buffer, 0, len);  
						length += len;  
						Properties properties = new Properties();  
						properties.put("length", String.valueOf(length));  
						FileOutputStream logFile = new FileOutputStream(new File(file.getParentFile(), file.getName()+".log"));  
						properties.store(logFile, null);//ʵʱ��¼�ļ�����󱣴�λ��  
						logFile.close();  
					}  
					if(length==fileOutStream.length()) {
						XmlDocPkgUtil.setChildText(reqData, "filepath",  filepath);
						XmlDocPkgUtil.setChildText(reqData, "fileName",  fileName);
						XmlDocPkgUtil.setChildText(reqData, "file_type",  file_type);
						delete(id,reqData);  	
					}
					fileOutStream.close();                    
					inStream.close();  
					outStream.close();  
					file = null;  
				}  
			} catch (Exception e) {  
				e.printStackTrace();  
			} finally {  
				try {  
					if(socket != null && !socket.isClosed()) socket.close();  
				} catch (IOException e) {}  
			}  
		}  

	}  

	/**
	 * д������
	 * @param reqElement
	 * @param pdao
	 * @throws Exception
	 */
	private String addComment(Element reqElement,PlatformDao pdao) throws Exception{
		Element commentRec = ConfigDocument.createRecordElement("yuexue", "learn_comment");
		XmlDocPkgUtil.copyValues(reqElement, commentRec, 0, true);
		commentRec.removeChild("attachment_id");
		XmlDocPkgUtil.setChildText(commentRec, "create_by",reqElement.getChildText("username"));
		XmlDocPkgUtil.setChildText(commentRec, "userid", reqElement.getChildText("userid"));
		String commentid =  pdao.insertOneRecordSeqPk(commentRec).toString();
		
		Element commAttachRec = ConfigDocument.createRecordElement("yuexue","learn_comment_attachment");
		XmlDocPkgUtil.copyValues(reqElement, commAttachRec, 0 , true);
		XmlDocPkgUtil.setChildText(commAttachRec, "attachment_id", reqElement.getChildText("attachment_id"));
		XmlDocPkgUtil.setChildText(commAttachRec, "comment_id", commentid);
		XmlDocPkgUtil.setChildText(commAttachRec, "create_by",reqElement.getChildText("username"));
		
		String comm_attid=pdao.insertOneRecordSeqPk(commAttachRec).toString();
		
		
		return commentid+","+comm_attid;
		
	}

	private String addVideo(Element reqElement,PlatformDao pdao) throws Exception{
		
		Element videoPaperRec = ConfigDocument.createRecordElement("yuexue", "learn_examination_paper");
		XmlDocPkgUtil.copyValues(reqElement, videoPaperRec, 0, true);
		XmlDocPkgUtil.setChildText(videoPaperRec, "create_by",reqElement.getChildText("username"));
		XmlDocPkgUtil.setChildText(videoPaperRec, "deptcode",reqElement.getChildText("deptcode"));
		XmlDocPkgUtil.setChildText(videoPaperRec, "user_id", reqElement.getChildText("userid"));
		String paperid= pdao.insertOneRecordSeqPk(videoPaperRec).toString();
		
		//д���м��learn_paper_attachment
		Element attachmentRec = ConfigDocument.createRecordElement("yuexue", "learn_paper_attachment");
		XmlDocPkgUtil.copyValues(reqElement, attachmentRec, 0, true);
		XmlDocPkgUtil.setChildText(attachmentRec, "valid", "Y");
		XmlDocPkgUtil.setChildText(attachmentRec, "paper_id", paperid);
		XmlDocPkgUtil.setChildText(attachmentRec, "create_by",reqElement.getChildText("username"));
		String comm_attid = pdao.insertOneRecordSeqPk(attachmentRec).toString();
		
		 //���ӻ�����Ϣsell_integral_log
		 Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral_log");
		 XmlDocPkgUtil.copyValues(reqElement, sellIntegralLogRec, 0, true);
		 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", "1");
		 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "touserid", reqElement.getChildText("userid"));
		 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "paper_info_id", paperid);
		 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "create_by", "����Ա");
		 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "valid", "Y");
		 
		 StringBuffer strSQL = new StringBuffer();
		 String integral_num="0";
		 String remark="";
		 ArrayList<Object> paramList = new ArrayList<Object>();
	       strSQL.append("SELECT * FROM learn_attachment tt WHERE file_md5 = (SELECT file_md5 FROM learn_paper_attachment t1, learn_attachment t2");
	       strSQL.append("  WHERE t1.attachment_id = t2.attachment_id  AND t1.paper_id = ?");
	       paramList.add(paperid);
	       strSQL.append(" )  AND userid = ?");
	       paramList.add( reqElement.getChildText("userid"));
	       strSQL.append(" order by tt.create_date desc");
	    
	       pdao.setSql(strSQL.toString());
	       pdao.setBindValues(paramList);
	       Element result = pdao.executeQuerySql(0, -1);
	       List queryList = result.getChildren("Record");
	
		    if(queryList.size()>1){
			    remark="�ظ��ϴ��μ�";
	    	}else{
			    remark="�ϴ��μ�";
				integral_num="5";
		    }
	    
	      XmlDocPkgUtil.setChildText(sellIntegralLogRec, "consumption_num", integral_num);
		  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", remark);
		   pdao.insertOneRecordSeqPk(sellIntegralLogRec);
		  if(!"0".equals(integral_num)){
			  StringBuffer uploadSQL = new StringBuffer();
				uploadSQL.append(" select * from sell_integral si ");
				uploadSQL.append(" where si.userid = ? ");
				ArrayList<Object> uploadParam = new ArrayList<Object>();
				uploadParam.add(reqElement.getChildText("userid"));

				pdao.setSql(uploadSQL.toString());
				pdao.setBindValues(uploadParam);
				Element uploadResult = pdao.executeQuerySql(0, -1);
				List uploadList = uploadResult.getChildren("Record");
				// ������������ û��������
				if (uploadList.size() > 0) {
					StringBuffer uploadSISQL = new StringBuffer();
					ArrayList<Object> uploadSIList = new ArrayList<Object>();
					uploadSISQL.append(" update sell_integral set integral_num = integral_num + ?, modify_by = ?, modify_date = now() ");
					uploadSISQL.append(" where userid = ? ");
					uploadSIList.add(integral_num);
					uploadSIList.add(reqElement.getChildText("username"));
					uploadSIList.add(reqElement.getChildText("userid"));
					pdao.setSql(uploadSISQL.toString());
					pdao.setBindValues(uploadSIList);
					pdao.executeTransactionSql();
				} else {
					Element uploadRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
					XmlDocPkgUtil.copyValues(reqElement, uploadRec, 0, true);
					XmlDocPkgUtil.setChildText(uploadRec, "userid", reqElement.getChildText("userid"));
					XmlDocPkgUtil.setChildText(uploadRec, "integral_num",integral_num);
					XmlDocPkgUtil.setChildText(uploadRec, "valid", "Y");
					pdao.insertOneRecordSeqPk(uploadRec).toString();
				}
		 }
		return paperid+","+comm_attid;
	}
	
	public FileLog find(Long sourceid) {  
		return datas.get(sourceid);  
	}  

	// �����ϴ���¼  
	public void save(Long id, File saveFile) {  
		// �պ���Ըĳ�ͨ�����ݿ���  
		datas.put(id, new FileLog(id, saveFile.getAbsolutePath()));  
	}  

	// ���ļ��ϴ���ϣ�ɾ����¼  
	public void delete(long sourceid,Element reqData) {  
		if (datas.containsKey(sourceid)){
			datas.remove(sourceid);  
			ApplicationContext.regSubSys("yuexue");
			//�ϴ��ɹ�����������
			PlatformDao pdao = new PlatformDao();
			try{
				pdao.beginTransaction();
				if ("paper".equals(reqData.getChildText("uploadsource"))){
					StringBuffer deptSQL=new StringBuffer();
					deptSQL.append(" select t1.userid,t3.deptid,t3.deptcode from pcmc_user t1,pcmc_user_dept t2,pcmc_dept t3 where t1.userid = t2.userid ");
					deptSQL.append(" and t2.deptid = t3.deptid and t1.state > '0' and t1.userid= ?");
					ArrayList<Object> paramList = new ArrayList<Object>();
					paramList.add(reqData.getChildText("userid"));
					pdao.setSql(deptSQL.toString());
					pdao.setBindValues(paramList);
					Element result = pdao.executeQuerySql(0, -1); // ֻȡ��һ��
					List queryList = result.getChildren("Record");
					Element deptRec = (Element) queryList.get(0);
					XmlDocPkgUtil.setChildText(reqData, "deptcode", deptRec.getChildText("deptcode"));
				}
				Element attachRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment");
				XmlDocPkgUtil.copyValues(reqData, attachRec, 0, true);
				XmlDocPkgUtil.setChildText(attachRec, "file_path", reqData.getChildText("filepath"));
				XmlDocPkgUtil.setChildText(attachRec, "file_name", reqData.getChildText("fileName"));
				XmlDocPkgUtil.setChildText(attachRec, "file_size", reqData.getChildText("size"));
				XmlDocPkgUtil.setChildText(attachRec, "file_md5", MD5FileUtil.getFileMD5String(new File(uploadPath+reqData.getChildText("filepath"))));
				XmlDocPkgUtil.setChildText(attachRec, "create_by", reqData.getChildText("username"));
				// ����ʱ�� swf������
				if ("mp3,mp4,avi,mov".indexOf(reqData.getChildText("file_type")) > -1) {
					File source = new File(uploadPath+reqData.getChildText("filepath"));
					Encoder encoder = new Encoder();
					MultimediaInfo m = encoder.getInfo(source);
					long duration = m.getDuration();
					// ת��Ϊ��
					long video_time = (duration / 1000);
					XmlDocPkgUtil.setChildText(attachRec, "video_time", "" + video_time);
				}
				String att_id = pdao.insertOneRecordSeqPk(attachRec).toString();
				XmlDocPkgUtil.setChildText(reqData, "attachment_id", att_id);

			     
				/** ���ݴ���Ĳ���,д�벻ͬ���м��  */
				if ("comment".equals(reqData.getChildText("uploadsource"))){
					String comment_id = addComment(reqData,pdao);
						
				}
				else if ("paper".equals(reqData.getChildText("uploadsource"))){
					String paper_id=addVideo(reqData,pdao);
				}
				pdao.commitTransaction();
				
				
			}catch (Exception e) {
				pdao.rollBack();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally { 
				pdao.releaseConnection();
			}
		}
			
	}  

	private class FileLog {  
		private Long id;  
		private String path;  

		public FileLog(Long id, String path) {  
			super();  
			this.id = id;  
			this.path = path;  
		}  

		@SuppressWarnings("unused")
		public Long getId() {  
			return id;  
		}  

		@SuppressWarnings("unused")
		public void setId(Long id) {  
			this.id = id;  
		}  

		public String getPath() {  
			return path;  
		}  

		@SuppressWarnings("unused")
		public void setPath(String path) {  
			this.path = path;  
		}  

	}

	
}  
