package cn.com.bright.workflow.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class WorkflowUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private File saveDir;
    private File tempDir;
    private String basePath;
    private String savePath;

    public WorkflowUploadServlet() {
        saveDir = null;
        tempDir = null;
    }

    public void init() throws ServletException {
        basePath = getServletContext().getRealPath("/");
        savePath = "upload";
        saveDir = new File(basePath + savePath);

        if (!saveDir.isDirectory()) {
            saveDir.mkdir();
        }

        tempDir = new File(basePath + "upload/tmp");

        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String row_number = request.getParameter("row_number") == null ? "" : request.getParameter("row_number");
        String file_upload_type = request.getParameter("file_upload_type") == null ? "" : request.getParameter("file_upload_type");
        String selectDivId = request.getParameter("selectDivId") == null ? "" : request.getParameter("selectDivId");
        
        String message = null;
        String basePath = "";
        String saveFileName = "";

        String fileName = "";
        String fileSize = "";
        String fileType = "";
        String attachmentId = "";

        try {
            basePath = getServletContext().getRealPath("/");
            request.setCharacterEncoding("GBK");

            if (ServletFileUpload.isMultipartContent(request)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(4096);
                factory.setRepository(tempDir);

                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");

                List fileList = upload.parseRequest(request);
                Iterator it = fileList.iterator();
                while (it.hasNext()) {
                    FileItem fi = (FileItem) it.next();
                    if (!fi.isFormField() && (fi.getName().length() > 0)) {
                        fileName = fi.getName();
                        fileSize = String.valueOf(fi.getSize());
                        fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

                        String destFileName = fileName;
                        FileUtil.createDirs(basePath + "upload/tmp/" + getNow("yyyyMM"), true);
                        saveFileName = "upload/tmp/" + getNow("yyyyMM") + "/" + destFileName;
                        fi.write(new File(basePath + saveFileName));

                        attachmentId = insertAttachMent(fileName, fileSize, fileType, saveFileName);
                    }
                }
            }

            message = "{success:'success',message:'upload success',fileName:'" + fileName + "',fileSize:'"
                + fileSize + "',attachmentId:'" + attachmentId + "',path:'/" + saveFileName + "',row_number:'" + row_number + "',file_upload_type:'" + file_upload_type+ "',selectDivId:'"+selectDivId+"'}";
            response.getWriter().println(message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "{'success':'failure','message':'upload failure'}";
            response.getWriter().println(message);
        }
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
        sql.append("	) VALUES (");
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

        ApplicationContextHelper.getJdbcTemplate().update(
            sql.toString(),
            new String[] { String.valueOf(seq), fileName, fileSize, fileType, saveFileName, userid,
                    DatetimeUtil.getNow("") });

        /*
         * List rlist = new ArrayList(); PlatformDao dao = null; String
         * attachmentId =""; try { dao = new PlatformDao();
         * dao.beginTransaction(); Element attachmentRec =
         * ConfigDocument.createRecordElement("workflow",
         * "workflow_attachment"); XmlDocPkgUtil.setChildText(attachmentRec,
         * "file_path", saveFileName); XmlDocPkgUtil.setChildText(attachmentRec,
         * "file_name", fileName); XmlDocPkgUtil.setChildText(attachmentRec,
         * "file_size",fileSize); XmlDocPkgUtil.setChildText(attachmentRec,
         * "file_type", fileType); attachmentId =
         * dao.insertOneRecordSeqPk(attachmentRec).toString(); } catch
         * (Exception e) { e.printStackTrace(); dao.rollBack(); } finally {
         * dao.releaseConnection(); }
         */
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
