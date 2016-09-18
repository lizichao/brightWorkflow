package cn.com.bright.masterReview.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.bright.masterReview.MasterReviewAction;
import cn.com.bright.masterReview.api.MasterReviewVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@SuppressWarnings("serial")
public class ExportWordServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExportWordServlet() {
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
        String businessKey = request.getParameter("businessKey");
        String processInstanceId = request.getParameter("processInstanceId");
        
        String contextPath = request.getSession().getServletContext().getRealPath("");
        //System.out.println("contextPath:"+contextPath);
        String path = contextPath+File.separator+"WEB-INF"+File.separator+"config"+File.separator+"headmaster"+File.separator+"template";
        //System.out.println(path);
        
        MasterReviewAction masterReviewAction = new MasterReviewAction();
        MasterReviewVO masterReviewVO = masterReviewAction.findMasterReviewVO(businessKey,processInstanceId);
        
        Configuration cfg = null;
        Template temp = null;
        StringWriter sw = null;
        
        PrintWriter out = null;
        try {
            cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File(path));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = cfg.getTemplate("headmaster.ftl");
            sw = new StringWriter();
            
//            Map root = new HashMap();
//            root.put("KSXXList", list);
//            root.put("user", user);//作者信息
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String createTime = format.format(calendar.getTime());
//            root.put("createTime", createTime);//文件创建时间
//            root.put("BCDXCCMap", BCDXCCMap);
//            root.put("ksjbCodeMap", ksjbCodeMap);
//            root.put("kszlCodeMap", kszlCodeMap);
//            root.put("sexMap", sexMap);
            temp.process(masterReviewVO, sw);
            
            response.setContentType("application/msword;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\""+System.currentTimeMillis()+".doc\";");
            out = response.getWriter();
            out.print(sw.toString());
            out.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if (sw!=null) {
                try {
                    sw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null) out.close();
        }
	}
}
