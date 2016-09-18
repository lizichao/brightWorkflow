package cn.com.bright.workflow.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class WorkflowAttachMentDownload extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        try {
            String attachmentId = request.getParameter("attachmentId");
            AttachMentVO attachMentVO = getAttachmentVO(attachmentId);

            // String attachmentName = request.getParameter("attachmentName");
            // String filepath = request.getParameter("path");
            // filepath ="upload/publish/402881ec506e97f701506e9919b80005.doc";
            // downFilename = "测试";
            String basePath = getServletContext().getRealPath("/");
            // path是指欲下载的文件的路径。
            File file = new File(basePath + attachMentVO.getFilePath());
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            // String ext = filename.substring(filename.lastIndexOf(".") +
            // 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition",
                "attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            //
            // response.setContentType("text/plain");
            // response.setHeader("Location", downFilename);
            // response.setHeader("Content-Disposition",
            // "attachment; filename="+ downFilename);
            // OutputStream outputStream = response.getOutputStream();
            // InputStream inputStream = new FileInputStream(basePath+filepath);
            // byte[] buffer = new byte[1024];
            // int i = -1;
            // while ((i = inputStream.read(buffer)) != -1) {
            // outputStream.write(buffer, 0, i);
            // }
            // outputStream.flush();
            // outputStream.close();
        } catch (FileNotFoundException e1) {
            System.out.println("没有找到您要的文件");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("系统错误，请及时与管理员联系");
        }
    }

    private AttachMentVO getAttachmentVO(String attachmentId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select * from workflow_attachment ");
        sql.append("where attachment_id=?");

        Map<String, Object> map = ApplicationContextHelper.getJdbcTemplate().queryForMap(sql.toString(),
            attachmentId);

        AttachMentVO attachMentVO = new AttachMentVO();
        attachMentVO.setAttachmentId(attachmentId);
        attachMentVO.setFileName(map.get("file_name").toString());
        attachMentVO.setFilePath(map.get("file_path").toString());
        attachMentVO.setFileSize(map.get("file_size").toString());
        attachMentVO.setFileType(map.get("file_type").toString());
        return attachMentVO;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doPost(request, response);
    }
}
