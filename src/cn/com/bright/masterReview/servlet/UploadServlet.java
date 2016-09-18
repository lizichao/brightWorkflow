package cn.com.bright.masterReview.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.masterReview.util.ImageUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadServlet() {
		super();
	}
	
	/**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }
    

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Enumeration<String> parameterNames = request.getParameterNames();
		response.setContentType("text/html;charset=GBK");
		request.setCharacterEncoding("GBK");
		while(parameterNames.hasMoreElements()){
			String name = parameterNames.nextElement();
			String value = request.getParameter(name);
			System.out.println(name+":"+value);
		}


	    String  basePath = getServletContext().getRealPath("/");
		BufferedInputStream fileIn = new BufferedInputStream(request.getInputStream());
		//String fileName = new String(request.getParameter("fileName").getBytes(),"gbk");
		String fileName = request.getParameter("fileName");
		
		fileName = new String(fileName.getBytes("iso-8859-1"), "utf-8") ;
		fileName = URLDecoder.decode(fileName,"UTF-8");
	
		//String fileName = fn;
		String fileSize = request.getParameter("size");
		//文件后缀名
		String ext ="";
		if (fileName.lastIndexOf(".") != -1){
			ext = fileName.substring(fileName.lastIndexOf("."));			
		}
		
		String fn="";
		//实际路径不包含中文用UUID代替
		try {
			fn = new UUIDHexGenerator().generate(null)+ext;
		} catch (Exception e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] buf = new byte[1024];
		
		//文件路径，目录
        FileUtil.createDirs(basePath + "upload/tmp/" + getNow("yyyyMM"), true);
        String saveFileName =  "upload/tmp/" + getNow("yyyyMM") + "/" + fn;
          
		//String dbpath = "/upload/weixin/teacherUploadKJ/";
		//String path = cn.brightcom.jraf.util.FileUtil.getWebPath()+"upload/tmp/" + getNow("yyyyMM");
		//文件路径,文件名
//		String filepath = path + saveFileName; 
//		File ml = new File(path);
    	File file = new File(basePath +saveFileName);
//		if (!ml.exists()) { 
//			ml.mkdirs();
//		}
		BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
		while (true) {
		      // 读取数据
		     int bytesIn = fileIn.read(buf, 0, 1024);
		     //System.out.println(bytesIn);
		     if (bytesIn == -1) {
		        break;
		     } else {
		        fileOut.write(buf, 0, bytesIn);
		     }
		}
		fileOut.flush();
		fileOut.close();
		
        String headImg = request.getParameter("headImg");
        if (!StringUtil.isEmpty(headImg)) {
            String fname_out = "";
            try {
                fn = new UUIDHexGenerator().generate(null) + ext;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            fname_out = "upload/tmp/" + getNow("yyyyMM") + "/" + fn;
            fileSize = String.valueOf(ImageUtil.resize(saveFileName, fname_out, 100, 100, false));
            saveFileName = fname_out;
        }
		
		String attachmentId = insertAttachMent(fileName, fileSize, ext, saveFileName);
		PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("filePath", "/"+saveFileName);
        jsonObject.put("fileName", fn);
        jsonObject.put("fileSize", fileSize);
        jsonObject.put("attachmentId", attachmentId);
        jsonObject.put("id", attachmentId);
        out.write(jsonObject.toString());
	}
	
    private String insertAttachMent(String fileName, String fileSize, String fileType, String saveFileName) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        long seq = DBOprProxy.getNextSequenceNumber("workflow_attachment");

        sql.append(" INSERT INTO workflow_attachment(");
        sql.append("   attachment_id");
        sql.append("  ,file_name");
        sql.append("  ,file_size");
        sql.append("  ,file_type");
        sql.append("  ,file_path");
         sql.append("  ,create_by");
         sql.append("  ,create_date");
        // sql.append("  ,modify_by");
        // sql.append("  ,modify_date");
        sql.append("    ) VALUES (");
        sql.append("   ? ");
        sql.append("  ,? ");
        sql.append("  ,? ");
        sql.append("  ,? ");
        sql.append("  ,? ");
        sql.append("  ,? ");
        sql.append("  ,? ");
        // sql.append("  ,? ");
        // sql.append("  ,? ");
        sql.append(")");

        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),
            new String[] { String.valueOf(seq), fileName, fileSize, fileType, saveFileName,userid, DatetimeUtil.getNow("") });
        return String.valueOf(seq);
    }

	
    public String getNow(String format) {
        if ((null == format) || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }
}
