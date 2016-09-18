package cn.com.bright.workflow.bpmn.listener.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.bpmn.graph.ActivitiHistoryGraphBuilder;
import cn.com.bright.workflow.bpmn.graph.Edge;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.TaskServiceImplExt;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

@Transactional(rollbackFor = Exception.class)
// @Component
public class RollbackPreviousListener implements IFunctionListener {
    // private static RollbackPreviousListener instance = new
    // RollbackPreviousListener();
    // private Log log4j = new Log(this.getClass().toString());

    // @Resource
    // TaskServiceImplExt taskServiceImplExt;

    @Resource
    HistoryService historyService;

    @Resource
    WorkflowLogService workflowLogService;

    @Resource
    JdbcTemplate jdbcTemplate;

    public void notifyEvent(Map<String, String> componentParamMap) throws Exception {
        // String processInstanceId =
        // componentParamMap.get("processInstanceId");
        final String taskId = componentParamMap.get("taskId");
        // final String currentUserId =
        // componentParamMap.get(WorkflowConstant.CURRENT_USERID);
        // String transferUserId =
        // componentParamMap.get(WorkflowConstant.TRANSFER_USERID);

        final Task task = ApplicationContextHelper.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        // String historicActivityInstanceId
        // =historyService.createHistoricActivityInstanceQuery().
        // String historicActivityInstanceId = "";
        // final String previousHistoricActivityName = "";

        // final Graph graph = null;
        ApplicationContextHelper.getManagementService().executeCommand(new Command<Object>() {
            public Object execute(CommandContext commandContext) {
                String historicActivityInstanceId = jdbcTemplate.queryForObject(
                    "select t.ID_ from act_hi_actinst t where t.TASK_ID_=?", String.class, taskId);

                Graph graph = new ActivitiHistoryGraphBuilder(task.getProcessInstanceId()).build();

                Node node = graph.findById(historicActivityInstanceId);
                String previousHistoricActivityInstanceId = findIncomingNode(graph, node);
                Map<String, Object> map = jdbcTemplate.queryForMap(
                    "select t.* from act_hi_actinst t where t.id_=?", previousHistoricActivityInstanceId);

                String previousHistoricActivityId = map.get("act_id_").toString();
                String previousHistoricActivityName = map.get("act_name_").toString();
                String previousTaskId = map.get("task_id_").toString();
                /*
                 * try { bvals = new ArrayList<String>(); sql.setLength(0); dao
                 * = new PlatformDao();
                 * sql.append(" select t.* from act_hi_actinst t where t.id_=?"
                 * ); bvals.add(previousHistoricActivityInstanceId);
                 * dao.setSql(sql.toString()); dao.setBindValues(bvals); Element
                 * resultElement = dao.executeQuerySql(-1, 1); Element mRecord =
                 * resultElement.getChild("Record"); previousHistoricActivityId
                 * = mRecord.getChildText("act_id_");
                 * previousHistoricActivityName =
                 * mRecord.getChildText("act_name_"); previousTaskId =
                 * mRecord.getChildText("task_id_"); } catch (Exception e) {
                 * e.printStackTrace() ; } finally { dao.releaseConnection(); }
                 */
                // HistoricActivityInstance historicActivityInstance =
                // historyService.createHistoricActivityInstanceQuery().(previousHistoricActivityInstanceId).singleResult();
                // String sourceActivityInstanceKey =
                // (String)runtimeService.getVariable(processInstanceId,
                // "to->"+task.getTaskDefinitionKey());
                // sourceActivityInstanceKey =
                // sourceActivityInstanceKey.substring(7,sourceActivityInstanceKey.length());
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(previousTaskId).singleResult();
                List<String> nextHandlers = new ArrayList<String>();
                nextHandlers.add(historicTaskInstance.getAssignee());

                TaskServiceImplExt taskServiceImplExt = (TaskServiceImplExt) ApplicationContextHelper.getBean("taskServiceImplExt");
                // taskServiceImplExt.setVariableLocal(taskId,WorkflowConstant.IS_AUTO_LOG,"0");
                Map<String, Object> variables = new HashMap<String, Object>();
                // variables.put(WorkflowConstant.IS_AUTO_LOG, "0");
                variables.put(WorkflowConstant.NEXT_HANDLERS, nextHandlers);
                // taskServiceImplExt.claim(taskId, currentUserId);
                taskServiceImplExt.rollBackCompleteNoLog(taskId, previousHistoricActivityId, variables);
                StringBuffer logOperate = new StringBuffer();
                logOperate.append("驳回到上一节点:");
                logOperate.append(previousHistoricActivityName);
                workflowLogService.recordFunctionComponentLog(taskId, logOperate.toString());
                return null;
            }
        });
    }

    private String findIncomingNode(Graph graph, Node node) {
        for (Edge edge : graph.getEdges()) {
            Node src = edge.getSrc();
            Node dest = edge.getDest();
            String srcType = src.getType();

            if (!dest.getId().equals(node.getId())) {
                continue;
            }

            if ("userTask".equals(srcType)) {
                // boolean isSkip = isSkipActivity(src.getId());

                // if (isSkip) {
                // return this.findIncomingNode(graph, src);
                // } else {
                return src.getId();
                // }
            } else if (srcType.endsWith("Gateway")) {
                return this.findIncomingNode(graph, src);
            } else {
                // log4j.logInfo("cannot rollback, previous node is not userTask : "
                // + src.getId() + " " + srcType + "(" + src.getName()
                // + ")");

                return null;
            }
        }
        return null;
    }
}
