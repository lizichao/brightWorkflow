package cn.com.bright.workflow.web.scheduler;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.util.SayHelloClient;

@Component
public class TaskTimeoutJob {

    public static final int MAIL_REMIND = 1;
    public static final int NOTE_REMIND = 2;
    public static final int ALL_REMIND = 3;

    private String title = "����ʱ�����ʼ�";// �������ʼ��ı���

    // private String from = "longlingbz@163.com";// �����﷢��
    // private String from = "233561831@qq.com";// �����﷢��

    // @Scheduled(cron = "0/20 * * * * ?")ÿ��20��
    // @Scheduled(cron = "0 0 12 * * ?")ÿ������12��
    // @Scheduled(cron = "0 0 */1 * * ?")һСʱ
   // @Scheduled(cron = "0 0/2 * * * ?")
    public void execute() {
        List<Task> tasks = ApplicationContextHelper.getTaskService().createTaskQuery().list();
        // Map<String,Task> taskTemp = new HashMap<String,Task>();
        for (Task task : tasks) {
            Date taskCreateTime = task.getCreateTime();
            if (!DateUtil.dateToDateString(taskCreateTime).equals(DateUtil.dateToDateString(new Date()))) {
                continue;
            }
            // if(taskTemp.containsKey(task.getId())){
            // continue;
            // }
            // taskTemp.put(task.getId(), task);
            // if (!task.getId().equals("800030")) {
            // continue;
            // }
            UserTaskRemindVO userTaskRemindVO = getUserTaskRemindVO(task);
            try {
                if (isSatisfyRemind(task, userTaskRemindVO)) {
                    sendRemind(task, userTaskRemindVO.getRemindMode());
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Error e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void sendRemind(Task task, int remindMode) throws MessagingException {
        Set<UserVO> userVOs = ApplicationContextHelper.getTaskQueryService().searchTaskhandler(task);
        switch (remindMode) {
            case NOTE_REMIND:
              //  sendNote(task, userVOs);
                break;
            case MAIL_REMIND:
                sendMail(task, userVOs);
                break;
            case ALL_REMIND:
              //  sendNote(task, userVOs);
                sendMail(task, userVOs);
                break;
            default:
                break;
        }
    }

    private void sendMail(Task task, Set<UserVO> userVOs) throws MessagingException {
        // ���Դ������ļ���ȡ��Ӧ�Ĳ���
        Properties props = new Properties();

        String smtp = "smtp.163.com"; // ���÷����ʼ����õ���smtp
        String servername = "njitlzc@163.com";
        String serverpaswd = "lzc121532";
        String from = "njitlzc@163.com";

        // String smtp = "smtp.qq.com"; // ���÷����ʼ����õ���smtp
        // String servername = "233561831@qq.com";
        // String serverpaswd = "lzc121532";
        // String from = "233561831@qq.com";

        javax.mail.Session mailSession; // �ʼ��Ự����
        javax.mail.internet.MimeMessage mimeMsg; // MIME�ʼ�����

        props = java.lang.System.getProperties(); // ���ϵͳ���Զ���
        props.put("mail.smtp.host", smtp); // ����SMTP����
        props.put("mail.smtp.auth", "true"); // �Ƿ񵽷������û�����������֤
        // ����������֤���͵��û����������Ƿ���ȷ
        Email_Autherticatorbean myEmailAuther = new Email_Autherticatorbean(servername, serverpaswd);
        // �����ʼ��Ự
        mailSession = javax.mail.Session.getInstance(props, (Authenticator) myEmailAuther);
        // ���ô���Э��
        javax.mail.Transport transport = mailSession.getTransport("smtp");
        // ����from��to����Ϣ
        mimeMsg = new javax.mail.internet.MimeMessage(mailSession);
        if (from != null && !from.equals("")) {
            InternetAddress sentFrom = new InternetAddress(from);
            mimeMsg.setFrom(sentFrom); // ���÷����˵�ַ
        }

        Set<String> sendToSet = getTaskHendlerEmail(task, userVOs);
        if (CollectionUtils.isEmpty(sendToSet)) {
            return;
        }

        String[] sendToArray = sendToSet.toArray(new String[] {});
        // �ռ���
        InternetAddress[] sendTo = new InternetAddress[sendToArray.length];
        for (int i = 0; i < sendToArray.length; i++) {
            System.out.println("���͵�:" + sendToArray[i]);
            sendTo[i] = new InternetAddress(sendToArray[i]);
        }

        // ����
        InternetAddress[] sendToCC = new InternetAddress[1];
        sendToCC[0] = new InternetAddress("njitlzc@163.com");
        // sendToCC[1] = new InternetAddress("szeb@sz.edu.cn");

        /*
         * // ���� InternetAddress[] sendToBCC = new InternetAddress[2];
         * sendToBCC[0] = new InternetAddress("username@126.com"); sendToBCC[1]
         * = new InternetAddress("username@gmail.com");
         */
        mimeMsg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, sendTo);
        mimeMsg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.CC, sendToCC);
        /*
         * mimeMsg.setRecipients(
         * javax.mail.internet.MimeMessage.RecipientType.BCC, sendToBCC);
         */

        mimeMsg.setSubject(title, "gb2312");

        MimeBodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setContent(getMailContent(task), "text/html;charset=gb2312");

        Multipart multipart = new MimeMultipart();// ���������ʽ
        multipart.addBodyPart(messageBodyPart1);
        /*
         * for (int i = 0; i < filenames.length; i++) { MimeBodyPart
         * messageBodyPart2 = new MimeBodyPart(); // ѡ���ÿһ�������� String filename =
         * filenames[i].split(",")[0]; System.out.println("��������" + filename);
         * String displayname = filenames[i].split(",")[1]; // �õ�����Դ
         * FileDataSource fds = new FileDataSource(filename); //
         * �õ������������õ�BodyPart messageBodyPart2.setDataHandler(new
         * DataHandler(fds)); // �õ��ļ���ͬ�����õ�BodyPart
         * messageBodyPart2.setFileName(MimeUtility.encodeText(displayname));
         * multipart.addBodyPart(messageBodyPart2); }
         */
        mimeMsg.setContent(multipart);

        // �����ż�ͷ�ķ�������
        mimeMsg.setSentDate(new Date());
        mimeMsg.saveChanges();

        // �����ʼ�
        transport.send(mimeMsg);
        transport.close();
    }

    private Set<String> getTaskHendlerEmail(Task task, Set<UserVO> userVOs) {
        Set<String> taskHendlerEmails = new HashSet<String>();
        int i = 0;
        for (Iterator<UserVO> iterator = userVOs.iterator(); iterator.hasNext();) {
            UserVO userVO = iterator.next();
            if (!StringUtils.isEmpty(userVO.getEmail())) {
                taskHendlerEmails.add(userVO.getEmail());
                i++;
            }
        }
        return taskHendlerEmails;
    }

    private String getMailContent(Task task) {
        HistoricProcessFormEntity historicProcessFormEntity = ApplicationContextHelper.getProcessFormService().findHistoricProcessForm(task.getProcessInstanceId());
        String title = historicProcessFormEntity.getTitle();
        Set<UserVO> users = ApplicationContextHelper.getTaskQueryService().searchTaskhandler(task);
        String taskHandlers = "";
        int i = 0;
        for (UserVO userVO : users) {
            taskHandlers += userVO.getUserName();
            if (i < (users.size() - 1)) {
                taskHandlers += ",";
            }
            i++;
        }

        String contentTemp = taskHandlers + "����ã�����[" + historicProcessFormEntity.getProcessDefName()+ "],����Ϊ[" + title + "],����";
        contentTemp += "<a href='http://192.168.1.65:8080/workflow/template/completeTaskForm.jsp?taskId=" + task.getId() + "' title='" + task.getName() + "' target='_blank'>" + task.getName() + "</a>";
        contentTemp += "��û����꣬�뾡�����!";
        return contentTemp;
    }

    /**
     * ��֤�ࣨ�ڲ��ࣩ
     * @author Administrator
     */
    class Email_Autherticatorbean extends Authenticator {
        private String m_username = null;
        private String m_userpass = null;

        public void setUsername(String username) {
            m_username = username;
        }

        public void setUserpass(String userpass) {
            m_userpass = userpass;
        }

        public Email_Autherticatorbean(String username, String userpass) {
            super();
            setUsername(username);
            setUserpass(userpass);
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(m_username, m_userpass);
        }
    }

    private void sendNote(Task task, Set<UserVO> userVOs) {
        for (UserVO userVO : userVOs) {
            String noteContent = getNoteContent(task, userVO);
            String msgid = SayHelloClient.sendSMS(userVO.getMobile(), noteContent);
        }
    }

    private String getNoteContent(Task task, UserVO userVO) {
        HistoricProcessFormEntity historicProcessFormEntity = ApplicationContextHelper.getProcessFormService().findHistoricProcessForm(task.getProcessInstanceId());
        String title = historicProcessFormEntity.getTitle();

        // HistoricProcessInstance historicProcessInstance =
        // ApplicationContextHelper.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        StringBuffer sql = new StringBuffer();
        sql.append(userVO.getUserName());
        // sql.append(",��ã�����<a href='/workflow/template/completeTaskForm.jsp?'  target='_blank'>������</a>");
        sql.append(",��ã�����[");
        sql.append(historicProcessFormEntity.getProcessDefName());
        sql.append("],����Ϊ[");
        sql.append(title);
        sql.append("],����[");
        sql.append(task.getName());
        // sql.append("<a href='/workflow/template/completeTaskForm.jsp?taskId="+task.getId()+"' title='"+task.getName()+"' target='_blank'>"+task.getName()+"</a>");
        sql.append("]�Ѿ���ʱ���뼰ʱ��¼ϵͳ�鿴�ҵĴ���������");
        return sql.toString();
    }

    private boolean isSatisfyRemind(Task task, UserTaskRemindVO userTaskRemindVO) throws ParseException {
        if (userTaskRemindVO.getIsRemind() == 0) {
            return false;
        }
        return true;

        // int dueDate = userTaskRemindVO.getDueDate();

        // if (userTaskRemindVO.getDueDate() == 0) {
        // return false;
        // }
        // Date taskCreateTime = task.getCreateTime();
        // Date nowDate = new Date();
        // // DatetimeUtil.getNow();
        // int workDayDuration = 0;
        // while (taskCreateTime.before(nowDate)) {
        // if (isWorkDay(taskCreateTime)) {
        // workDayDuration++;
        // }
        // taskCreateTime = getNextDay(taskCreateTime);
        // }
        //
        // if (workDayDuration >= dueDate) {
        // return true;
        // }
        // return false;
    }

    private Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private boolean isWorkDay(Date taskCreateTime) throws ParseException {
        // taskCreateTime = DateUtil.stringToDate(
        // DateUtil.dateToDateString(taskCreateTime,"yyyy-MM-dd")+" 00:00:00");
        String taskCreateTimeStr = DateUtil.dateToDateString(taskCreateTime, "yyyy-MM-dd") + " 00:00:00";
        return ApplicationContextHelper.getWorkcalRuleManager().isWorkDay(taskCreateTimeStr);
    }

    private UserTaskRemindVO getUserTaskRemindVO(Task task) {
        ProcessDefinition processDefinition = ApplicationContextHelper.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
        UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService().findUserTaskRemindConfig(processDefinition.getKey(), task.getTaskDefinitionKey());
        return userTaskRemindVO;
    }
}
