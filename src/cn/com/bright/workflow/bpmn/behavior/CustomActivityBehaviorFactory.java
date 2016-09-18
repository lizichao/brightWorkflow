package cn.com.bright.workflow.bpmn.behavior;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bright.workflow.bpmn.behavior.usertask.CustomParallelMultiInstanceBehavior;
import cn.com.bright.workflow.bpmn.behavior.usertask.CustomSequentialMultiInstanceBehavior;
import cn.com.bright.workflow.bpmn.behavior.usertask.CustomUserTaskActivityBehavior;

public class CustomActivityBehaviorFactory extends DefaultActivityBehaviorFactory {
    private static Logger log = LoggerFactory.getLogger(CustomUserTaskActivityBehavior.class);

    // test
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask,TaskDefinition taskDefinition) {
        log.info("change usertask Behavior : {}  ", userTask);

        return new CustomUserTaskActivityBehavior(taskDefinition);
    }

    // test multiInstance
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(ActivityImpl activity,
                                                                             AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new CustomParallelMultiInstanceBehavior(activity, innerActivityBehavior);
    }

    // ͬ�����Ը��Ǳ�ķ���,��������Ԫ�ص��Զ�����Ϊ,�ο� @see ActivityBehaviorFactory
    // �������ִ�е�ĳһԪ��ʱ����

    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(ActivityImpl activity,
                                                                                 AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new CustomSequentialMultiInstanceBehavior(activity, innerActivityBehavior);
    }
}
