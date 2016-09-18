package cn.com.bright.workflow.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RuntimeService;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.axis.utils.StringUtils;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class ProcessDiagramServlet extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 1L;

    public ProcessDiagramServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        RuntimeService runtimeService = ApplicationContextHelper.getRuntimeService();
        String processInstanceId = request.getParameter("processInstanceId");
        String processDefinitionId = request.getParameter("processDefinitionId");
        String isProcessEnd = request.getParameter("isProcessEnd");

        // ProcessInstance processInstance =
        // runtimeService.createProcessInstanceQuery()
        // .processInstanceId(processInstanceId).singleResult();
        /*
         * ProcessDefinition processDefinition = ApplicationContextHelper
         * .getRepositoryService().getProcessDefinition(
         * processInstance.getProcessDefinitionId());
         */

        BpmnModel bpmnModel = ApplicationContextHelper.getRepositoryService().getBpmnModel(
            processDefinitionId);
        List<String> activeActivityIds = new ArrayList<String>();
        if (StringUtils.isEmpty(isProcessEnd)) {
            activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        }

        ProcessDiagramGenerator diagramGenerator = ApplicationContextHelper.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds,
            Collections.<String> emptyList(), "ËÎÌו", "ËÎÌו", null, 1.0);
        response.setContentType("image/png");

        int len = 0;
        byte[] b = new byte[1024];

        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

}
