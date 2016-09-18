package cn.com.bright.workflow.form;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import cn.com.bright.workflow.api.vo.ComponentVO;
import cn.com.bright.workflow.api.vo.FlowVO;
import cn.com.bright.workflow.api.vo.NodeVO;
import cn.com.bright.workflow.api.vo.ProcessStartFormVO;
import cn.com.bright.workflow.api.vo.UserTaskConfigVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessStartFormEngine extends BaseFormEngine<ProcessStartFormVO> {

    public String getName() {
        return "processStartFormEngine";
    }

    public Object renderStartForm(StartFormData startForm) {
        StartFormDataImpl startFormDataImpl = (StartFormDataImpl) startForm;
        String formKey = startFormDataImpl.getFormKey();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) startFormDataImpl.getProcessDefinition();

        ProcessStartFormVO processStartFormVO = new ProcessStartFormVO();
        processStartFormVO.setProcessDefinitionId(processDefinition.getId());
        processStartFormVO.setProcessDefinitionKey(processDefinition.getKey());
        processStartFormVO.setProcessDefinitionName(processDefinition.getName());
        processStartFormVO.setFormKey(formKey);
        processStartFormVO.setComponents(getComponents(processDefinition));
        processStartFormVO.setFlows(getFlowList(processDefinition.getInitial(), processDefinition.getKey()));
        return processStartFormVO;
    }

    private List<ComponentVO> getComponents(ProcessDefinitionEntity processDefinition) {
        List<ComponentVO> componentVOs = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowComponentConfig(processDefinition.getKey(), processDefinition.getInitial().getId());
        return componentVOs;
    }

    private List<FlowVO> getFlowList(ActivityImpl initial, String processKey) {
        List<FlowVO> flowList = new ArrayList<FlowVO>();

        for (PvmTransition pvmTransition : initial.getOutgoingTransitions()) {
            FlowVO flowVO = new FlowVO();
            flowVO.setId(pvmTransition.getId());
            flowVO.setName((String) pvmTransition.getProperty("name"));

            PvmActivity flowTargetPvmActivity = pvmTransition.getDestination();
            String flowTargetType = (String) flowTargetPvmActivity.getProperty("type");
            NodeVO flowTargetNodeVO = new NodeVO();
            if (flowTargetType.equals(WorkflowConstant.USERTASKTYPE)) {
                UserTaskConfigVO userTaskVO = new UserTaskConfigVO();
                userTaskVO.setId(flowTargetPvmActivity.getId());
                userTaskVO.setName((String) flowTargetPvmActivity.getProperty("name"));
                userTaskVO.setType("userTask");

                String multiInstance = (String) flowTargetPvmActivity.getProperty("multiInstance");
                if (multiInstance != null) {
                    userTaskVO.setMultiType(multiInstance);
                    userTaskVO.setMultiDepartments(ApplicationContextHelper.getWorkflowDefExtService().findWorkflowDepartmentConfig(processKey, flowTargetPvmActivity.getId()));
                } else {
                    userTaskVO.setConfigHandlers(ApplicationContextHelper.getWorkflowDefExtService().getUserTaskHandlerVOs(processKey, flowTargetPvmActivity.getId()));
                }
                flowTargetNodeVO = userTaskVO;
            } else {
                flowTargetNodeVO.setId(flowTargetPvmActivity.getId());
                flowTargetNodeVO.setName((String) flowTargetPvmActivity.getProperty("name"));
                flowTargetNodeVO.setType(flowTargetType);
            }
            flowVO.setDest(flowTargetNodeVO);

            flowList.add(flowVO);
        }
        return flowList;
    }

}
