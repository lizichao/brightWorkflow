package cn.com.bright.masterReview;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.masterReview.api.AccidentVO;
import cn.com.bright.masterReview.api.BookPublishVO;
import cn.com.bright.masterReview.api.EducationVO;
import cn.com.bright.masterReview.api.GradeEvaluateVO;
import cn.com.bright.masterReview.api.MasterReviewVO;
import cn.com.bright.masterReview.api.PaperVO;
import cn.com.bright.masterReview.api.PersonalAwardVO;
import cn.com.bright.masterReview.api.PersonnelLeaderGradeVO;
import cn.com.bright.masterReview.api.ProfessionalTitleVO;
import cn.com.bright.masterReview.api.ProfessorGradeVO;
import cn.com.bright.masterReview.api.PunishmentVO;
import cn.com.bright.masterReview.api.SchoolAwardVO;
import cn.com.bright.masterReview.api.SchoolReformVO;
import cn.com.bright.masterReview.api.SocialDutyVO;
import cn.com.bright.masterReview.api.StudyTrainVO;
import cn.com.bright.masterReview.api.SubjectVO;
import cn.com.bright.masterReview.api.WorkExperienceVO;
import cn.com.bright.masterReview.api.WorkHistoryVO;
import cn.com.bright.masterReview.util.MasterReviewConstant;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.service.ProcessOperateService;
import cn.com.bright.workflow.service.TaskOperateService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MasterReviewService {
    
    
    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ProcessOperateService processOperateService;

    @Resource
    TaskOperateService taskOperateService;
    
    public String startMasterReviewProcess(MasterReviewVO masterReviewVO, ProcessStartVO processStartVO) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String headerMasterName = masterReviewVO.getHeaderMasterName();
        String mobile = masterReviewVO.getMobile();
        String identitycard = masterReviewVO.getIdentitycard();
        String schoolId = masterReviewVO.getSchoolId();
        String schoolName = masterReviewVO.getSchoolName();
        String presentOccupation = masterReviewVO.getPresentOccupation();
        String applylevel = masterReviewVO.getApplylevel();
        String schoolType = masterReviewVO.getSchoolType();
        String schoolCount = masterReviewVO.getSchoolCount();
        String studentNumber = masterReviewVO.getStudentNumber();
        String schoolTypeAgo = masterReviewVO.getSchoolTypeAgo();
        String schoolCountAgo = masterReviewVO.getSchoolCountAgo();
        String studentNumberAgo = masterReviewVO.getStudentNumberAgo();
        
        String businessKey = masterReviewVO.getId();
        
        insertHeadMaster(masterReviewVO);
        updateHeadmasterBaseInfo(masterReviewVO);
        
        optionTabDeal(masterReviewVO,masterReviewVO.getOption_tab_type());
        
       // insertWorkExperience(masterReviewVO,businessKey);
      //  inserEducation(masterReviewVO,businessKey);
       // inserProfessionalTitle(masterReviewVO,businessKey);
        
        
        //inserPaper(masterReviewVO,businessKey);
        //inserWorkPublish(masterReviewVO,businessKey);
        //inserSubject(masterReviewVO,businessKey);
       // inserPersonalAward(masterReviewVO,businessKey);
       // inserSchoolAward(masterReviewVO,businessKey);
        String processInstanceId = processOperateService.startProcessInstance(processStartVO, businessKey);
        return processInstanceId;
    }

    /**
     * 学校获奖
     * @Title: inserSchoolAward 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertSchoolAward(List<SchoolAwardVO> schoolAwardVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_school_award(");
        sql.append(" id");
        sql.append(" ,businessKey");
        sql.append(",awards_name");
        sql.append(",work_school");
        sql.append(",awards_company");
        sql.append(",awards_level");
        sql.append(" ,awards_time");
        sql.append(" ,awards_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");     
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (SchoolAwardVO schoolAwardVO : schoolAwardVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_school_award"));
            Object[] param = new Object[12];
            param[0] = id;
            param[1] = businessKey;
            param[2] = schoolAwardVO.getAwardsName();
            param[3] = schoolAwardVO.getWorkSchool();
            param[4] = schoolAwardVO.getAwardsCompany();
            param[5] = schoolAwardVO.getAwardsLevel();
            param[6] = schoolAwardVO.getAwardsTime();
            param[7] = schoolAwardVO.getAwardsAttachmentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userid;
            param[11] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 个人获奖
     * @Title: inserPersonalAward 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertPersonalAward(List<PersonalAwardVO> personalAwardVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_personal_award(");
        sql.append(" id");
        sql.append(" ,businessKey");
        sql.append(" ,awards_name");
        sql.append(" ,awards_company");
        sql.append(",awards_level");
        sql.append(",awards_time");
        sql.append(" ,awards_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ,awards_type");
        sql.append("  ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )"); 
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (PersonalAwardVO personalAwardVO : personalAwardVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_personal_award"));
            Object[] param = new Object[12];
            param[0] = id;
            param[1] = businessKey;
            param[2] = personalAwardVO.getAwardsName();
            param[3] = personalAwardVO.getAwardsCompany();
            param[4] = personalAwardVO.getAwardsLevel();
            param[5] = personalAwardVO.getAwardsTime();
            param[6] = personalAwardVO.getAwardsAttachmentId();
            param[7] = "1";
            param[8] = "1";
            param[9] = userid;
            param[10] = DatetimeUtil.getNow("");
            param[11] =personalAwardVO.getAwards_type();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 课题情况
     * @Title: inserSubject 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertSubject(List<SubjectVO> subjectVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_subject(");
        sql.append("id");
        sql.append(",businessKey");
        sql.append(",subject_name");
        sql.append(" ,subject_company");
        sql.append(" ,subject_level");
        sql.append(",subject_responsibility");
        sql.append(" ,is_finish_subject");
        sql.append(" ,finish_result");
        sql.append(",finish_time");
        sql.append(" ,subject_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (SubjectVO subjectVO : subjectVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_subject"));
            Object[] param = new Object[14];
            param[0] = id;
            param[1] = businessKey;
            param[2] = subjectVO.getSubjectName();
            param[3] = subjectVO.getSubjectCompany();
            param[4] = subjectVO.getSubjectLevel();
            param[5] = subjectVO.getSubjectRresponsibility();
            param[6] = subjectVO.getIsfinishSubject();
            param[7] = subjectVO.getFinishResult();
            param[8] = subjectVO.getFinishTime();
            param[9] = subjectVO.getSubjectAttachmentId();
            param[10] = "1";
            param[11] = "1";
            param[12] = userid;
            param[13] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 著作发表情况
     * @Title: inserWorkPublish 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertWorkPublish(List<BookPublishVO> bookPublishVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_work_publish(");
        sql.append(" id");
        sql.append(",businessKey");
        sql.append(" ,book_name");
        sql.append(" ,complete_way");
        sql.append(" ,publish_time");
        sql.append(",complete_chapter");
        sql.append(",complete_word");
        sql.append(",author_order");
        sql.append(",cover_attachment_id");
        sql.append(" ,contents_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(") VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (BookPublishVO bookPublishVO : bookPublishVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_work_publish"));
            Object[] param = new Object[14];
            param[0] = id;
            param[1] = businessKey;
            param[2] = bookPublishVO.getBook_name();
            param[3] = bookPublishVO.getComplete_way();
            param[4] = bookPublishVO.getPublish_time();
            param[5] = bookPublishVO.getComplete_chapter();
            param[6] = bookPublishVO.getComplete_word();
            param[7] = bookPublishVO.getAuthor_order();
            param[8] = bookPublishVO.getCoverAttachmentId();
            param[9] = bookPublishVO.getContentsAttachmentId();
            param[10] = "1";
            param[11] = "1";
            param[12] = userid;
            param[13] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 论文情况
     * @Title: inserPaper 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertPaper(List<PaperVO> paperVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_paper(");
        sql.append(" id");
        sql.append(" ,businessKey");
        sql.append(" ,title");
        sql.append(" ,publish_time");
        sql.append(" ,magazine_meet_name");
        sql.append(" ,paper_meet_name");
        sql.append(" ,paper_number");
        sql.append(" ,organizers");
        sql.append(" ,organizers_level");
        sql.append(" ,personal_part");
        sql.append(" ,paper_attachment_id");
        sql.append(" ,publish_company");
        sql.append(" ,complete_way");
        sql.append(" ,author_order");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append("  ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(PaperVO paperVO : paperVOs){
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_paper"));
            Object[] param = new Object[18];
            param[0] = id;
            param[1] = businessKey;
            param[2] = paperVO.getTitle();
            param[3] = paperVO.getPublishTime();
            param[4] = paperVO.getMagazineMeetName();
            param[5] = paperVO.getPaperMeetName();
            param[6] = paperVO.getPaperNumber();
            param[7] = paperVO.getOrganizers();
            param[8] = paperVO.getOrganizersLevel();
            param[9] = paperVO.getPersonalPart();
            param[10] = paperVO.getPaperAttachmentId();
            param[11] = paperVO.getPublish_company();
            param[12] = paperVO.getComplete_way();
            param[13] = paperVO.getAuthor_order();
            param[14] = "1";
            param[15] = "1";
            param[16] = userid;
            param[17] =  DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 职称情况
     * @Title: inserProfessionalTitle 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertProfessionalTitle(List<ProfessionalTitleVO> professionalTitleVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_professional_title(");
        sql.append("  id");
        sql.append(" ,businessKey");
        sql.append(" ,professionaltitle_name");
        sql.append(" ,obtain_time");
        sql.append(" ,obtain_school");
        sql.append(" ,professionalAttachId");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append("  ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");   
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(ProfessionalTitleVO professionalTitleVO : professionalTitleVOs){
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_professional_title"));
            Object[] param = new Object[10];
            param[0] = id;
            param[1] = businessKey;
            param[2] = professionalTitleVO.getProfessionaltitle_name();
            param[3] = professionalTitleVO.getObtainTime();
            param[4] = professionalTitleVO.getObtainSchool();
            param[5] = professionalTitleVO.getProfessionalAttachId();
            param[6] = "1";
            param[7] = "1";
            param[8] = userid;
            param[9] =  DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 学历情况
     * @Title: inserEducation 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertEducation(List<EducationVO> educationVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_education(");
        sql.append("id");
        sql.append(" ,businessKey");
        sql.append(" ,start_date");
        sql.append(" ,end_date");
        sql.append(",study_school");
        sql.append(",study_profession");
        sql.append(" ,education_attachment_id");
        sql.append(" ,degree_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ,study_form");
        sql.append(" ,education_type");
        sql.append("  ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");    
        
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (EducationVO educationVO : educationVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_education"));
            Object[] param = new Object[14];
            param[0] = id;
            param[1] = businessKey;
            param[2] = educationVO.getStartTime();
            param[3] = educationVO.getEndTime();
            param[4] = educationVO.getStudySchool();
            param[5] = educationVO.getStudyProfession();
            param[6] = educationVO.getEducationAttachmentId();
            param[7] = educationVO.getDegreeAttachmentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userid;
            param[11] = DatetimeUtil.getNow("");
            param[12] = educationVO.getStudy_form();
            param[13] = educationVO.getEducation_type();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }
    
    /**
     * 工作年限
     * @Title: insertWorkExperience 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void insertWorkExperience(List<WorkExperienceVO> workExperienceVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_work_experience(");
        sql.append(" id");
        sql.append(",businessKey");
        sql.append(",start_date");
        sql.append(",end_date");
        sql.append(",work_school");
        sql.append(",work_profession");
        sql.append(" ,work_year");
        sql.append(" ,prove_attachment_id");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )"); 
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (WorkExperienceVO workExperienceVO : workExperienceVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_work_experience"));
            Object[] param = new Object[12];
            param[0] = id;
            param[1] = businessKey;
            param[2] = workExperienceVO.getStartTime();
            param[3] = workExperienceVO.getEndTime();
            param[4] = workExperienceVO.getWorkSchool();
            param[5] = workExperienceVO.getWorkProfession();
            param[6] = workExperienceVO.getWorkYear();
            param[7] = workExperienceVO.getProveAttachMentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userid;
            param[11] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }
    
    /**
     * 新增学校改革
     * @Title: addSchoolReform 
     * @Description: TODO
     * @return: void
     */
     private void addSchoolReform(List<SchoolReformVO> schoolReformVOs,String businessKey) {
         String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_school_reform(");
        sql.append("  id");
        sql.append(" ,businessKey");
        sql.append(" ,implement_time");
        sql.append(",project_name");
        sql.append(",project_level");
        sql.append(",charge_department");
        sql.append(",performance");
        sql.append(",approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(",create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
         
         List<Object[]> batchArgs = new ArrayList<Object[]>();
         for(SchoolReformVO schoolReformVO : schoolReformVOs){
             String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_school_reform"));
             Object[] param = new Object[11];
             param[0] = id;
             param[1] = businessKey;
             param[2] = schoolReformVO.getImplement_time();
             param[3] = schoolReformVO.getProject_name();
             param[4] = schoolReformVO.getProject_level();
             param[5] = schoolReformVO.getCharge_department();
             param[6] = schoolReformVO.getPerformance();
             param[7] = "1";
             param[8] = "1";
             param[9] = userid;
             param[10] =  DatetimeUtil.getNow("");
             batchArgs.add(param);
         }
         ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
     }
     
     /**
      * 学校等级评估
      * @Title: addGradeEvaluate 
      * @Description: TODO
      * @return: void
      */
     private void addGradeEvaluate(List<GradeEvaluateVO> gradeEvaluateVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("  INSERT INTO headmaster_grade_evaluate(");
        sql.append("  id");
        sql.append(" ,businessKey");
        sql.append(" ,compulsory_education");
        sql.append(" ,high_school");
        sql.append(" ,secondary_school");
        sql.append(" ,isvalid");
        sql.append("  ,approve_result");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
          
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (GradeEvaluateVO gradeEvaluateVO : gradeEvaluateVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_grade_evaluate"));
            Object[] param = new Object[9];
            param[0] = id;
            param[1] = businessKey;
            param[2] = gradeEvaluateVO.getCompulsory_education();
            param[3] = gradeEvaluateVO.getHigh_school();
            param[4] = gradeEvaluateVO.getSecondary_school();
            param[5] = "1";
            param[6] = "1";
            param[7] = userid;
            param[8] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
     }

     /**
      * 新增工作经历
      * @Title: addWorkHistory 
      * @Description: TODO
      * @return: void
      */
     private void addWorkHistory(List<WorkHistoryVO> workHistoryVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_work_history(");
        sql.append("id");
        sql.append(",businessKey");
        sql.append(",start_date");
        sql.append(",end_date");
        sql.append(" ,prove_people");
        sql.append(",isvalid");
        sql.append(",approve_result");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (WorkHistoryVO workHistoryVO : workHistoryVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_work_history"));
            Object[] param = new Object[9];
            param[0] = id;
            param[1] = businessKey;
            param[2] = workHistoryVO.getStart_date();
            param[3] = workHistoryVO.getEnd_date();
            param[4] = workHistoryVO.getProve_people();
            param[5] = "1";
            param[6] = "1";
            param[7] = userid;
            param[8] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);       
     }



     /**
      * 减分处分
      * @Title: addPunishment 
      * @Description: TODO
      * @return: void
      */
     private void addPunishment(List<PunishmentVO> punishmentVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_punishment(");
        sql.append(" id");
        sql.append(",businessKey");
        sql.append(",implement_time");
        sql.append(",description");
        sql.append(",people");
        sql.append(",department");
        sql.append(",process_result");
        sql.append(" ,approve_result");
        sql.append(" ,isvalid");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (PunishmentVO punishmentVO : punishmentVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_punishment"));
            Object[] param = new Object[11];
            param[0] = id;
            param[1] = businessKey;
            param[2] = punishmentVO.getImplement_time();
            param[3] = punishmentVO.getDescription();
            param[4] = punishmentVO.getPeople();
            param[5] = punishmentVO.getDepartment();
            param[6] = punishmentVO.getProcess_result();
            param[7] = "1";
            param[8] = "1";
            param[9] = userid;
            param[10] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);      
     }



     /**
      * 减分责任事故
      * @Title: addAccident 
      * @Description: TODO
      * @return: void
      */
     private void addAccident(List<AccidentVO> accidentVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_accident(");
        sql.append("  id");
        sql.append(" ,businessKey");
        sql.append(" ,implement_time");
        sql.append(",accident_name");
        sql.append(",description");
        sql.append(",process_result");
        sql.append(" ,isvalid");
        sql.append(" ,approve_result");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (AccidentVO accidentVO : accidentVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_accident"));
            Object[] param = new Object[10];
            param[0] = id;
            param[1] = businessKey;
            param[2] = accidentVO.getImplement_time();
            param[3] = accidentVO.getAccident_name();
            param[4] = accidentVO.getDescription();
            param[5] = accidentVO.getProcess_result();
            param[6] = "1";
            param[7] = "1";
            param[8] = userid;
            param[9] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);          
     }

      /**
       * 加分社会责任
       * @Title: addSocialDuty 
       * @Description: TODO
       * @return: void
       */
     private void addSocialDuty(List<SocialDutyVO> socialDutyVOs,String businessKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO headmaster_social_duty(");
        sql.append(" id");
        sql.append(",businessKey");
        sql.append(" ,implement_time");
        sql.append(",superior_task");
        sql.append(",arrange_department");
        sql.append(" ,complete_state");
        sql.append(" ,approve_result");
        sql.append(",isvalid");
        sql.append(",create_by");
        sql.append(" ,create_date");
        sql.append(" ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (SocialDutyVO socialDutyVO : socialDutyVOs) {
            String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_social_duty"));
            Object[] param = new Object[10];
            param[0] = id;
            param[1] = businessKey;
            param[2] = socialDutyVO.getImplement_time();
            param[3] = socialDutyVO.getSuperior_task();
            param[4] = socialDutyVO.getArrange_department();
            param[5] = socialDutyVO.getComplete_state();
            param[6] = "1";
            param[7] = "1";
            param[8] = userid;
            param[9] = DatetimeUtil.getNow("");
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);        
     }

    private String insertHeadMaster(MasterReviewVO masterReviewVO) {
       // String businessKey = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster"));
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster(");
        sql.append(" id");
        sql.append(",headerMasterId");
        sql.append(",headerMasterName");
        sql.append(",mobile");
        sql.append(",identitycard");
        sql.append(",school_id");
        sql.append(",school_name");
        sql.append(" ,present_occupation");
        sql.append(" ,apply_level");
        
        sql.append(" ,school_name_space");
        sql.append(" ,student_number");
        sql.append(",school_count");
        sql.append(" ,school_type");
        sql.append(" ,manage_difficulty_attachment_id");
        
        sql.append(" ,school_name_space_ago");
        sql.append(" ,student_number_ago");
        sql.append(",school_count_ago");
        sql.append(" ,school_type_ago");
        sql.append(" ,manage_difficulty_ago_attachment_id");
        
        sql.append(" ,run_school");
        sql.append("  ,school_management");
        sql.append("  ,education_science");
        sql.append(" ,external_environment");
        sql.append("  ,student_development ");
        sql.append("  ,teacher_development");
        
        sql.append(" ,work_report");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append(" ,apply_status");
        sql.append(" ,ispositive");
        sql.append("  ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
        
        Object[] param = new Object[] { masterReviewVO.getId(), masterReviewVO.getHeaderMasterId(),
                masterReviewVO.getHeaderMasterName(), masterReviewVO.getMobile(),
                masterReviewVO.getIdentitycard(), masterReviewVO.getSchoolId(),
                masterReviewVO.getSchoolName(), masterReviewVO.getPresentOccupation(),
                masterReviewVO.getApplylevel(), masterReviewVO.getSchoolNameSpace(),
                masterReviewVO.getStudentNumber(), masterReviewVO.getSchoolCount(),
                masterReviewVO.getSchoolType(), masterReviewVO.getManage_difficulty_attachment_id(),
                masterReviewVO.getSchoolNameSpaceAgo(), masterReviewVO.getStudentNumberAgo(),
                masterReviewVO.getSchoolCountAgo(), masterReviewVO.getSchoolTypeAgo(),
                masterReviewVO.getManage_difficulty_ago_attachment_id(), masterReviewVO.getRun_school(),
                masterReviewVO.getSchool_management(), masterReviewVO.getEducation_science(),
                masterReviewVO.getExternal_environment(), masterReviewVO.getStudent_development(),
                masterReviewVO.getTeacher_development(), masterReviewVO.getWork_report(), userid,
                DatetimeUtil.getNow(""), masterReviewVO.getIsSaveDraft(), masterReviewVO.getIsPositive() };
        jdbcTemplate.update(sql.toString(), param);
        return masterReviewVO.getId();
    }
    

    /**
     * 完成一般任务审批
     * @Title: completeMasterReviewTask 
     * @Description: TODO
     * @param masterReviewVO
     * @param taskCompleteVO
     * @throws TaskDelegateException
     * @return: void
     */
    public void completeMasterReviewTask(MasterReviewVO masterReviewVO, TaskCompleteVO taskCompleteVO)throws TaskDelegateException {
        taskOperateService.completeTask(taskCompleteVO);
    }
    
    /**
     * updateHeadmasterBaseInfo是更新校长个人信息，这个每个校长只有一条记录
     * headmaster表是每个校长发起一条申请就有一个记录
     * @Title: completeRefillTask 
     * @Description: TODO
     * @param masterReviewVO
     * @param taskCompleteVO
     * @throws TaskDelegateException
     * @return: void
     */
    public void completeRefillTask(MasterReviewVO masterReviewVO, TaskCompleteVO taskCompleteVO)throws TaskDelegateException {
        //updateHeadmasterBaseInfo(masterReviewVO);
        updateApproveResult("",masterReviewVO.getId(),masterReviewVO.getId(),true);
       
       
       // masterReviewVO.setApply_status(MasterReviewConstant.APPLY_RUNNING);
        updateHeadmasterSingleProperty(masterReviewVO,"apply_status",MasterReviewConstant.APPLY_RUNNING);
        optionTabDeal(masterReviewVO,masterReviewVO.getOption_tab_type());
        taskOperateService.completeTask(taskCompleteVO);
    }
    
    private void updateHeadmasterSingleProperty(MasterReviewVO masterReviewVO,String propertyName,String propertyValue) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster");
        sql.append(" SET ");

        // sql.append(" apply_result = 0,");
        sql.append(propertyName);
        sql.append("  = ?,");
        sql.append(" modify_by = ?,");
        sql.append("modify_date = ?");
        sql.append(" WHERE id = ?");

        Object[] param = new Object[] {propertyValue, userid, DatetimeUtil.getNow(""),
                masterReviewVO.getId() };
        jdbcTemplate.update(sql.toString(), param);
    }

    private void updateHeadmaster(MasterReviewVO masterReviewVO) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster");
        sql.append(" SET ");
      //  sql.append(" processInstanceId = ?,");
      //  sql.append("headerMasterId = ?,");
      //  sql.append("headerMasterName = ?,");
      //  sql.append("mobile = ?,");
      //  sql.append(" identitycard = ?,");
       // sql.append(" school_id = ?,");
      //  sql.append("  school_name = ?,");
         sql.append("  present_occupation = ?,");
        sql.append("   apply_level = ?,");
        
        sql.append(" school_name_space = ?,");
        sql.append(" student_number = ?,");
        sql.append(" school_count = ?,");
        sql.append("school_type = ?,");
        sql.append(" manage_difficulty_attachment_id = ?,");
        
        sql.append(" school_name_space_ago = ?,");
        sql.append(" student_number_ago = ?,");
        sql.append("  school_count_ago = ?,");
        sql.append("  school_type_ago = ?,");
        sql.append(" manage_difficulty_ago_attachment_id = ?,");
        
        sql.append(" run_school = ?,");
        sql.append("  school_management = ?,");
        sql.append("  education_science = ?,");
        sql.append(" external_environment = ?,");
        sql.append("  student_development = ?,");
        sql.append("  teacher_development = ?,");
        
       // sql.append(" apply_result = 0,");
        sql.append(" apply_status = ?,");
        //sql.append(" apply_total_point = ?,");
        sql.append(" modify_by = ?,");
        sql.append("modify_date = ?,");
        sql.append("work_report = ?,");
        sql.append("ispositive = ?");
        sql.append(" WHERE id = ?");
        
        Object[] param = new Object[] { masterReviewVO.getPresentOccupation(),
                masterReviewVO.getApplylevel(), masterReviewVO.getSchoolNameSpace(),
                masterReviewVO.getStudentNumber(), masterReviewVO.getSchoolCount(),
                masterReviewVO.getSchoolType(), masterReviewVO.getManage_difficulty_attachment_id(),
                masterReviewVO.getSchoolNameSpaceAgo(), masterReviewVO.getStudentNumberAgo(),
                masterReviewVO.getSchoolCountAgo(), masterReviewVO.getSchoolTypeAgo(),
                masterReviewVO.getManage_difficulty_ago_attachment_id(), masterReviewVO.getRun_school(),
                masterReviewVO.getSchool_management(), masterReviewVO.getEducation_science(),
                masterReviewVO.getExternal_environment(), masterReviewVO.getStudent_development(),
                masterReviewVO.getTeacher_development(), masterReviewVO.getApply_status(), userid, DatetimeUtil.getNow(""),
                masterReviewVO.getWork_report(), masterReviewVO.getIsPositive(), masterReviewVO.getId() };
        jdbcTemplate.update(sql.toString(), param);
    }
    
    /**
     * 人事干部审核打分
     * @Title: completePersonnelLeaderTask 
     * @Description: TODO
     * @param personnelLeaderGradeVO
     * @param taskCompleteVO
     * @throws TaskDelegateException
     * @return: void
     */
    public void completePersonnelLeaderTask(PersonnelLeaderGradeVO personnelLeaderGradeVO, TaskCompleteVO taskCompleteVO) throws TaskDelegateException{
        insertPersonnelLeaderGrade(personnelLeaderGradeVO,taskCompleteVO.getProcessInstanceId());
        taskOperateService.completeTask(taskCompleteVO);
    }
    
    private void insertPersonnelLeaderGrade(PersonnelLeaderGradeVO personnelLeaderGradeVO,String processInstanceId) {
        String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_personnel_leader_grade"));
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        
        
        sql.append("INSERT INTO headmaster_personnel_leader_grade(");
        sql.append("   id");
        sql.append(" ,businessKey");
        sql.append("  ,baseinfo_grade");
        sql.append(" ,work_experience_grade");
        sql.append("   ,education_grade");
        sql.append("   ,professional_title_grade");
        sql.append("   ,management_difficulty_grade");
        sql.append("   ,management_difficulty_grade_ago");
        sql.append("  ,paper_grade");
        sql.append("  ,work_publish_grade");
        sql.append("  ,subject_grade");
        sql.append("  ,personal_award_grade");
        sql.append("  ,school_award_grade");
        sql.append("  ,personnel_leader");
        sql.append("   ,apply_headmaster");
        sql.append("   ,processInstanceId");
        sql.append("   ,sumGrade");
        sql.append("   ,studyTrain_grade");
        sql.append("   ,schoolReform_grade");
        sql.append("   ,socialDuty_grade");
        sql.append("   ,accident_grade");
        sql.append("   ,punishment_grade");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append("    ) VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )");
        
        Object[] param = new Object[24];
        param[0] = id;
        param[1] = personnelLeaderGradeVO.getBusinessKey();
        param[2] = personnelLeaderGradeVO.getBaseinfoGrade();
        param[3] = personnelLeaderGradeVO.getWorkExperienceGrade();
        param[4] = personnelLeaderGradeVO.getEducationGrade();
        param[5] = personnelLeaderGradeVO.getProfessionalTitleGrade();
        param[6] = personnelLeaderGradeVO.getManagementDifficultyGrade();
        param[7] = personnelLeaderGradeVO.getManagementDifficultyGradeAgo();
        param[8] = personnelLeaderGradeVO.getPaperGrade();
        param[9] = personnelLeaderGradeVO.getWorkPublishGrade();
        param[10] = personnelLeaderGradeVO.getSubjectGrade();
        param[11] = personnelLeaderGradeVO.getPersonalAwardGrade();
        param[12] = personnelLeaderGradeVO.getSchoolAwardGrade();
        param[13] = userid;
        param[14] = personnelLeaderGradeVO.getHeaderMasterId();
        param[15] = processInstanceId;
        param[16] = personnelLeaderGradeVO.getSumGrade();
        param[17] = personnelLeaderGradeVO.getStudyTrain_grade();
        param[18] = personnelLeaderGradeVO.getSchoolReform_grade();
        param[19] = personnelLeaderGradeVO.getSocialDuty_grade();
        param[20] = personnelLeaderGradeVO.getAccident_grade();
        param[21] = personnelLeaderGradeVO.getPunishment_grade();
        param[22] = userid;
        param[23] = DatetimeUtil.getNow("");
      
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
    }

    /**
     * 专家审核评分
     * @Title: completeProfessorApproveTask 
     * @Description: TODO
     * @param professorGradeVO
     * @param taskCompleteVO
     * @throws TaskDelegateException
     * @return: void
     */
    public void completeProfessorApproveTask(ProfessorGradeVO professorGradeVO, TaskCompleteVO taskCompleteVO) throws TaskDelegateException{
        insertProfessorApproveGrade(professorGradeVO,taskCompleteVO);
        taskOperateService.completeTask(taskCompleteVO);
    }
    
    private void insertProfessorApproveGrade(ProfessorGradeVO professorGradeVO,TaskCompleteVO taskCompleteVO) {
        String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_professor_grade"));
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO headmaster_professor_grade(");
        sql.append(" id");
        sql.append(",businessKey");
        sql.append(",apply_headmaster");
        sql.append(",professor");
        sql.append(",report_grade");
        sql.append(",processInstanceId");
        sql.append(" ,create_by");
        sql.append(" ,create_date");
        sql.append("   ,sumGrade");
        sql.append(") VALUES (");
        sql.append(" ?");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" )"); 
        
        Object[] param = new Object[9];
        param[0] = id;
        param[1] = professorGradeVO.getBusinessKey();
        param[2] = professorGradeVO.getApply_headmaster();
        param[3] = userid;
        param[4] = professorGradeVO.getReport_grade();
        param[5] = taskCompleteVO.getProcessInstanceId();
        param[6] = userid;
        param[7] = DatetimeUtil.getNow("");
        param[8] = professorGradeVO.getSumGrade();
      
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
    }
    
    /**
     * 区级管理员审批
     * @Title: completeAreaCadresTask 
     * @Description: TODO
     * @param refuseType
     * @param refuseTypeId
     * @param taskCompleteVO
     * @throws TaskDelegateException
     * @return: void
     */
    public void completeAreaCadresTask( String id,String refuseType,String refuseTypeId,TaskCompleteVO taskCompleteVO) throws TaskDelegateException{
        if(!StringUtils.isEmpty(refuseType)){
            updateRefuseStatus(id,refuseType,refuseTypeId);
        }
        taskOperateService.completeTask(taskCompleteVO);
    }

    private void updateRefuseStatus(String id, String refuseType, String refuseTypeId) {
        StringBuffer sql1 = new StringBuffer("");
        sql1.append("UPDATE headmaster");
        sql1.append(" SET apply_status = '0'");
        sql1.append("  WHERE id = ?    ");
        ApplicationContextHelper.getJdbcTemplate().update(sql1.toString(), id);

        updateApproveResult(refuseType, refuseTypeId, id, false);
    }

    private void updateApproveResult(String refuseType, String refuseTypeId,String bussinessKey,boolean isAll) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String nowDate = DatetimeUtil.getNow("");
        String approveResult = isAll? "1" :"0";
        if(refuseType.equals("allRefuse") ){//全部驳回
            isAll = true;
            refuseTypeId= bussinessKey;
        }else if(refuseType.equals("baseInfo") || isAll){//基础信息
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster");
            sql.append(" SET base_info_approve_result = ?");
         
                sql.append("  WHERE id = ?    ");
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),approveResult,refuseTypeId);
        }else if(refuseType.equals("workExperience") || isAll){//任职年限
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_work_experience");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("education") || isAll){//学历情况
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_education");
            sql.append(" SET approve_result = ?, modify_by = ?, modify_date = ?");
            sql.append(" WHERE id = ?");
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("professionalTitle") || isAll){//职称情况
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE headmaster_professional_title");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[3];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("managementDifficulty") || isAll){//现任管理难度
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster");
            sql.append("SET manage_difficulty_approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[2];
            param[0] = approveResult;
            param[1] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("managementDifficultyAgo") || isAll){//近八年管理难度
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster");
            sql.append(" SET manage_difficulty_ago_approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[2];
            param[0] = approveResult;
            param[1] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("paper") || isAll){//论文情况
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE headmaster_paper");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("workPublish") || isAll){//著作发表情况
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE headmaster_work_publish");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("subject") || isAll){//课题发表情况
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_subject");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("personalAward") || isAll){//个人获奖情况
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_personal_award");
            sql.append(" SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("schoolAward") || isAll){// 学校获得荣誉
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_school_award");
            sql.append("SET approve_result = ?,  modify_by = ?, modify_date = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
            
            Object[] param = new Object[4];
            param[0] = approveResult;
            param[1] = userId;
            param[2] = nowDate;
            param[3] = refuseTypeId;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("workReport") || isAll){//述职报告
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster");
            sql.append(" SET  modify_by = ?, modify_date = ?, work_report_approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("studyTrain") || isAll){//进修学习
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_studytrain");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("schoolReform") || isAll){//学校特色及改革
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_school_reform");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("socialDuty") || isAll){//社会责任
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_social_duty");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("accident") || isAll){//责任事故 
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_accident");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("punishment") || isAll){//处分
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_punishment");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("workHistory") ){//工作经历
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_work_history");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }else if(refuseType.equals("gradeEvaluate") ){//登记评估
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE headmaster_grade_evaluate");
            sql.append(" SET  modify_by = ?, modify_date = ?, approve_result = ?");
            if(isAll){
                sql.append("  WHERE businessKey = ?    ");
            }else{
                sql.append("  WHERE id = ?    ");
            }
          
            Object[] param = new Object[4];
            param[0] = userId;
            param[1] = nowDate;
            param[2] = refuseTypeId;
            param[3] = approveResult;
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
        }
    }
    
    private void updateHeadmasterBaseInfo(MasterReviewVO masterReviewVO) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String nowDate = DatetimeUtil.getNow("");
        StringBuffer sql = new StringBuffer("");
        
        sql.append(" UPDATE headmaster_base_info");
        sql.append(" SET");
        //sql.append(" id = ?,");
        //sql.append(" userid = ?,");
        //sql.append("username = ?,");
       // sql.append("usercode = ?,");
        sql.append(" usersex = ?,");
     //   sql.append("userpwd = ?,");
       // sql.append("deptid = ?,");
        sql.append(" school_name = ?,");
        sql.append("idnumber = ?,");
        sql.append("ispositive = ?,");
        sql.append("email = ?,");
        sql.append(" mobile = ?,");
        sql.append(" address = ?,");
       // sql.append(" phone_valid = ?,");
        sql.append(" phasestudy = ?,");
        sql.append("school_class = ?,");
        sql.append("join_work_time = ?,");
        sql.append("join_educate_work_time = ?,");
        sql.append("politics_status = ?,");
        sql.append("teach_age = ?,");
        sql.append("census_register = ?,");
        sql.append("nation = ?,");
        sql.append(" present_occupation = ?,");
        sql.append("present_major_occupation = ?,");
        sql.append("person_img_attachId = ?,");
       // sql.append("create_by = ?,");
       // sql.append("create_date = ?,");
        sql.append(" modify_by = ?,");
        sql.append("     modify_date = ?,");
        sql.append("     native_place = ?");
        sql.append(" WHERE userid = ?");
        
      
        Object[] param = new Object[22];
        param[0] = masterReviewVO.getUsersex();
        param[1] = masterReviewVO.getSchoolName();
        param[2] = masterReviewVO.getIdentitycard();
        param[3] = masterReviewVO.getIsPositive();
        param[4] = masterReviewVO.getEmail();
        param[5] = masterReviewVO.getMobile();
        param[6] = masterReviewVO.getAddress();
        param[7] = masterReviewVO.getPhasestudy();
        param[8] = masterReviewVO.getSchool_class();
        param[9] = masterReviewVO.getJoin_work_time();
        param[10] = masterReviewVO.getJoin_educate_work_time();
        param[11] = masterReviewVO.getPolitics_status();
        param[12] = masterReviewVO.getTeach_age();
        param[13] = masterReviewVO.getCensus_register();
        param[14] = masterReviewVO.getNation();
        param[15] = masterReviewVO.getPresentOccupation();
        param[16] = masterReviewVO.getPresent_major_occupation();
        param[17] = masterReviewVO.getPerson_img_attachId();
        param[18] = userId;
        param[19] = nowDate;
        param[20] = masterReviewVO.getNative_place();
        param[21] = userId;
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),param);
    }
    
    

    /**
     * 学校获奖，这些更新要么是刚申请时的填写好之后更新，要么是被驳回之后修改数据再更新
     * @Title: inserSchoolAward 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateSchoolAward(List<SchoolAwardVO> schoolAwardVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
       // String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_school_award ");
        sql.append("SET");
       // sql.append("  id = ?");
       // sql.append(",businessKey = ?");
        sql.append(" awards_name = ?");
        sql.append(" ,work_school = ?");
        sql.append(",awards_company = ?");
        sql.append(",awards_level = ?");
        sql.append(",awards_time = ?");
        sql.append(",awards_attachment_id = ?");
        sql.append(",isvalid = ?");
        sql.append(" ,approve_result = ?");
       // sql.append(",create_people = ?");
      //  sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append(" WHERE id =?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(SchoolAwardVO schoolAwardVO : schoolAwardVOs){
            Object[] param = new Object[11];
          //  param[0] = schoolAwardVO.getId();
          //  param[1] = businessKey;
            param[0] =schoolAwardVO.getAwardsName();
            param[1] =schoolAwardVO.getWorkSchool();
            param[2] = schoolAwardVO.getAwardsCompany();
            param[3] =schoolAwardVO.getAwardsLevel();
            param[4] =schoolAwardVO.getAwardsTime();
            param[5] = schoolAwardVO.getAwardsAttachmentId();
            param[6] = "1";
            param[7] = "1";
            param[8] = userId;
            param[9] = DatetimeUtil.getNow("");
            param[10] = schoolAwardVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 个人获奖
     * @Title: inserPersonalAward 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updatePersonalAward(List<PersonalAwardVO> personalAwardVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        //String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_personal_award ");
        sql.append("SET");
        sql.append("  awards_name = ?");
        sql.append(" ,awards_company = ?");
        sql.append(" ,awards_level = ?");
        sql.append(" ,awards_time = ?");
        sql.append(" ,awards_attachment_id = ?");
         sql.append(" ,isvalid = ?");
         sql.append(" ,approve_result = ?");
       // sql.append(",create_people = ?");
       // sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append(",awards_type = ?");
        sql.append("  WHERE id = ?");
        
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(PersonalAwardVO personalAwardVO : personalAwardVOs){
            Object[] param = new Object[11];
            param[0] =personalAwardVO.getAwardsName();
            param[1] =personalAwardVO.getAwardsCompany();
            param[2] = personalAwardVO.getAwardsLevel();
            param[3] =personalAwardVO.getAwardsTime();
            param[4] = personalAwardVO.getAwardsAttachmentId();
            param[5] = "1";
            param[6] = "1";
            param[7] = userId;
            param[8] = DatetimeUtil.getNow("");
            param[9] = personalAwardVO.getAwards_type();
            param[10] = personalAwardVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 课题情况
     * @Title: inserSubject 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateSubject(List<SubjectVO> subjectVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        
        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster_subject");
        sql.append(" SET");
        // sql.append("  id = ?");
        // sql.append(" ,businessKey = ?");
        sql.append("  subject_name = ?");
        sql.append(" ,subject_company = ?");
        sql.append(" ,subject_level = ?");
        sql.append(",subject_responsibility = ?");
        sql.append(" ,is_finish_subject = ?");
        sql.append(" ,finish_result = ?");
        sql.append(" ,finish_time = ?");
        sql.append(" ,subject_attachment_id = ?");
        sql.append(" ,isvalid = ?");
        sql.append("  ,approve_result = ?");
        // sql.append(",create_people = ?");
        // sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append(" WHERE id = ?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(SubjectVO subjectVO : subjectVOs){
            Object[] param = new Object[13];
            param[0] =subjectVO.getSubjectName();
            param[1] =subjectVO.getSubjectCompany();
            param[2] = subjectVO.getSubjectLevel();
            param[3] =subjectVO.getSubjectRresponsibility();
            param[4] = subjectVO.getIsfinishSubject();
            param[5] = subjectVO.getFinishResult();
            param[6] = subjectVO.getFinishTime();
            param[7] = subjectVO.getSubjectAttachmentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userId;
            param[11] = DatetimeUtil.getNow("");
            param[12] = subjectVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 著作发表情况
     * @Title: inserWorkPublish 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateWorkPublish(List<BookPublishVO> bookPublishVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
     //   String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_work_publish ");
        sql.append("SET");
        sql.append(" book_name = ?");
        sql.append(",complete_way = ?");
        sql.append(" ,publish_time = ?");
        sql.append(" ,complete_chapter = ?");
        sql.append(" ,complete_word = ?");
        sql.append(" ,author_order = ?");
        sql.append(" ,cover_attachment_id = ?");
        sql.append(" ,contents_attachment_id = ?");
        sql.append(" ,isvalid = ?");
        sql.append(",approve_result =?");
        //sql.append(",create_people = ?");
       // sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append("WHERE id = ?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(BookPublishVO bookPublishVO : bookPublishVOs){
            Object[] param = new Object[13];
            param[0] =bookPublishVO.getBook_name();
            param[1] =bookPublishVO.getComplete_way();
            param[2] = bookPublishVO.getPublish_time();
            param[3] = bookPublishVO.getComplete_chapter();
            param[4] = bookPublishVO.getComplete_word();
            param[5] = bookPublishVO.getAuthor_order();
            param[6] = bookPublishVO.getCoverAttachmentId();
            param[7] = bookPublishVO.getContentsAttachmentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userId;
            param[11] = DatetimeUtil.getNow("");
            param[12] = bookPublishVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 论文情况
     * @Title: inserPaper 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updatePaper(List<PaperVO> paperVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        //String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_paper ");
        sql.append("SET");
        sql.append(" title = ?");
        sql.append(" ,publish_time = ?");
        sql.append(",magazine_meet_name = ?");
        sql.append(",paper_meet_name = ?");
        sql.append(",paper_number = ?");
        sql.append(",organizers = ?");
        sql.append(",organizers_level = ?");
        sql.append(",personal_part = ?");
        sql.append(",paper_attachment_id = ?");
        sql.append(",isvalid = ?");
        sql.append(" ,approve_result = ?");
       // sql.append(",create_people = ?");
       // sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append(",publish_company = ?");
        sql.append(",complete_way = ?");
        sql.append(",author_order = ?");
        sql.append(" WHERE id = ?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(PaperVO paperVO : paperVOs){
            Object[] param = new Object[17];
            param[0] = paperVO.getTitle();
            param[1] = paperVO.getPublishTime();
            param[2] = paperVO.getMagazineMeetName();
            param[3] = paperVO.getPaperMeetName();
            param[4] = paperVO.getPaperNumber();
            param[5] = paperVO.getOrganizers();
            param[6] = paperVO.getOrganizersLevel();
            param[7] = paperVO.getPersonalPart();
            param[8] = paperVO.getPaperAttachmentId();
            param[9] = "1";
            param[10] = "1";
            param[11] = userId;
            param[12] = DatetimeUtil.getNow("");
            param[13] = paperVO.getPublish_company();
            param[14] = paperVO.getComplete_way();
            param[15] = paperVO.getAuthor_order();
            param[16] = paperVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 职称情况
     * @Title: inserProfessionalTitle 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateProfessionalTitle(List<ProfessionalTitleVO> professionalTitleVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_professional_title");
        sql.append(" SET");
        sql.append(" obtain_time = ?");
        sql.append(" ,obtain_school = ?");
        sql.append(" ,professionalAttachId = ?");
        sql.append(" ,isvalid = ?");
        sql.append(" ,approve_result = ?");
      //  sql.append(" ,approve_result =?");
      //  sql.append(",create_people = ?");
      //  sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append("  WHERE id =?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(ProfessionalTitleVO professionalTitleVO : professionalTitleVOs){
          // String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_professional_title"));
            Object[] param = new Object[8];
            param[0] = professionalTitleVO.getObtainTime();
            param[1] = professionalTitleVO.getObtainSchool();
            param[2] = professionalTitleVO.getProfessionalAttachId();
            param[3] = "1";
            param[4] = "1";
            param[5] = userId;
            param[6] = DatetimeUtil.getNow("");
            param[7] = professionalTitleVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 学历情况
     * @Title: inserEducation 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateEducation(List<EducationVO> educationVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        // String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";

        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster_education");
        sql.append(" SET ");
        sql.append(" start_date = ?");
        sql.append(" ,end_date = ?");
        sql.append(" ,study_school = ?");
        sql.append(" ,study_profession = ?");
        sql.append(" ,education = ?");
        sql.append(" ,degree = ?");
        sql.append(" ,education_attachment_id = ?");
        sql.append(" ,degree_attachment_id = ?");
        sql.append(" ,isvalid = ?");
        sql.append(" ,approve_result = ?");
        // sql.append(" ,approve_result = ?");
        // sql.append(",create_people = ?");
        // sql.append(" ,create_time = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append(",study_form = ?");
        sql.append(",education_type = ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (EducationVO educationVO : educationVOs) {
          //  String id = String.valueOf(DBOprProxy.getNextSequenceNumber("headmaster_education"));
            Object[] param = new Object[15];
            param[0] = educationVO.getStartTime();
            param[1] = educationVO.getEndTime();
            param[2] = educationVO.getStudySchool();
            param[3] = educationVO.getStudyProfession();
            param[4] = educationVO.getEducation();
            param[5] = educationVO.getDegree();
            param[6] = educationVO.getEducationAttachmentId();
            param[7] = educationVO.getDegreeAttachmentId();
            param[8] = "1";
            param[9] = "1";
            param[10] = userId;
            param[11] = DatetimeUtil.getNow("");
            param[12] = educationVO.getStudy_form();
            param[13] = educationVO.getEducation_type();
            param[14] = educationVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }
    
    /**
     * 工作年限
     * @Title: insertWorkExperience 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateWorkExperience(List<WorkExperienceVO> workExperienceVOs,String businessKey) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
       // String approve_result = masterReviewVO.isIdRefill() ? "1" : "0";
        
        StringBuffer sql = new StringBuffer("");
        sql.append("    UPDATE headmaster_work_experience");
        sql.append("  SET");
       // sql.append("  businessKey = ?");
        sql.append("  start_date = ?");
        sql.append("  ,end_date = ?");
        sql.append("  ,work_school = ?");
        sql.append("  ,work_profession = ?");
        sql.append(" ,work_year = ?");
        sql.append("  ,prove_attachment_id = ?");
        sql.append("  ,isvalid = ?");
        sql.append(" ,approve_result = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append("  WHERE id = ?");
        
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(WorkExperienceVO workExperienceVO : workExperienceVOs){
            Object[] param = new Object[11];
          //  param[0] = id;
           // param[1] = businessKey;
            param[0] = workExperienceVO.getStartTime();
            param[1] = workExperienceVO.getEndTime();
            param[2] = workExperienceVO.getWorkSchool();
            param[3] = workExperienceVO.getWorkProfession();
            param[4] = workExperienceVO.getWorkYear();
            param[5] = workExperienceVO.getProveAttachMentId();
            param[6] = "1";
            param[7] = "1";
            param[8] = userId;
            param[9] = DatetimeUtil.getNow("");
            param[10] = workExperienceVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 减分责任事故
     * @Title: updateAccident 
     * @Description: TODO
     * @param masterReviewVO
     * @return: void
     */
    private void updateAccident(List<AccidentVO> accidentVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append("  UPDATE headmaster_accident");
        sql.append(" SET");
        sql.append(" implement_time =  ?");
        sql.append(",accident_name =  ?");
        sql.append(",description =  ?");
        sql.append(",process_result =  ?");
        sql.append(",approve_result =  ?");
        sql.append(",modify_by =  ?");
        sql.append(" ,modify_date =  ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (AccidentVO accidentVO : accidentVOs) {
            Object[] param = new Object[8];
            param[0] = accidentVO.getImplement_time();
            param[1] = accidentVO.getAccident_name();
            param[2] = accidentVO.getDescription();
            param[3] = accidentVO.getProcess_result();
            param[4] = "1";
            param[5] = userid;
            param[6] = DatetimeUtil.getNow("");
            param[7] = accidentVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 更新学校等级评估
     * @Title: updateGradeEvaluate 
     * @Description: TODO
     * @return: void
     */
    private void updateGradeEvaluate(List<GradeEvaluateVO> gradeEvaluateVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster_grade_evaluate");
        sql.append(" SET");
        sql.append(" compulsory_education = ?");
        sql.append(" ,high_school =  ?");
        sql.append(",secondary_school =  ?");
        sql.append(",isvalid =  ?");
        sql.append(" ,approve_result =  ?");
        // sql.append(" ,create_by =  ?");
        // sql.append(",create_date =  ?");
        sql.append(",modify_by =  ?");
        sql.append(" ,modify_date =  ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (GradeEvaluateVO gradeEvaluateVO : gradeEvaluateVOs) {
            Object[] param = new Object[8];
            param[0] = gradeEvaluateVO.getCompulsory_education();
            param[1] = gradeEvaluateVO.getHigh_school();
            param[2] = gradeEvaluateVO.getSecondary_school();
            param[3] = "1";
            param[4] = "1";
            param[5] = userid;
            param[6] = DatetimeUtil.getNow("");
            param[7] = gradeEvaluateVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 更新工作经历
     * @Title: updateWorkHistory 
     * @Description: TODO
     * @return: void
     */
    private void updateWorkHistory(List<WorkHistoryVO> workHistoryVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_work_history");
        sql.append(" SET");
        sql.append(" start_date = ?");
        sql.append(",end_date = ?");
        sql.append(",prove_people = ?");
        sql.append(",isvalid = ?");
        sql.append(",approve_result = ?");
        sql.append("          ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (WorkHistoryVO workHistoryVO : workHistoryVOs) {
            Object[] param = new Object[8];
            param[0] = workHistoryVO.getStart_date();
            param[1] = workHistoryVO.getEnd_date();
            param[2] = workHistoryVO.getProve_people();
            param[3] = "1";
            param[4] = "1";
            param[5] = userid;
            param[6] = DatetimeUtil.getNow("");
            param[7] = workHistoryVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

     /**
      * 减分处分
      * @Title: updatePunishment 
      * @Description: TODO
      * @return: void
      */
    private void updatePunishment(List<PunishmentVO> punishmentVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append("  UPDATE headmaster_punishment");
        sql.append("  SET");
        sql.append("  implement_time =  ?");
        sql.append("  ,description = ?");
        sql.append(" ,people =  ?");
        sql.append(" ,department = ?");
        sql.append("  ,process_result =  ?");
        sql.append("  ,approve_result =  ?");
        sql.append(" ,isvalid =  ?");
        sql.append("  ,modify_by =  ?");
        sql.append("  ,modify_date = ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (PunishmentVO punishmentVO : punishmentVOs) {
            Object[] param = new Object[10];
            param[0] = punishmentVO.getImplement_time();
            param[1] = punishmentVO.getDescription();
            param[2] = punishmentVO.getPeople();
            param[3] = punishmentVO.getDepartment();
            param[4] = punishmentVO.getProcess_result();
            param[5] = "1";
            param[6] = "1";
            param[7] = userid;
            param[8] = DatetimeUtil.getNow("");
            param[9] = punishmentVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    /**
     * 社会责任
     * @Title: updateSocialDuty 
     * @Description: TODO
     * @return: void
     */
    private void updateSocialDuty(List<SocialDutyVO> socialDutyVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster_social_duty");
        sql.append(" SET");
        sql.append(" implement_time = ?");
        sql.append(",superior_task = ?");
        sql.append(",arrange_department =?");
        sql.append(",complete_state =?");
        sql.append(",approve_result = ?");
        sql.append(" ,modify_by =?");
        sql.append(" ,modify_date = ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (SocialDutyVO socialDutyVO : socialDutyVOs) {
            Object[] param = new Object[8];
            param[0] = socialDutyVO.getImplement_time();
            param[1] = socialDutyVO.getSuperior_task();
            param[2] = socialDutyVO.getArrange_department();
            param[3] = socialDutyVO.getComplete_state();
            param[4] = "1";
            param[5] = userid;
            param[6] = DatetimeUtil.getNow("");
            param[7] = socialDutyVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }


    /**
     * 学校改革
     * @Title: updateSchoolReform 
     * @Description: TODO
     * @return: void
     */
    private void updateSchoolReform(List<SchoolReformVO> schoolReformVOs) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        StringBuffer sql = new StringBuffer("");
        sql.append("  UPDATE headmaster_school_reform");
        sql.append("  SET");
        sql.append("  implement_time = ?");
        sql.append(" ,project_name =  ?");
        sql.append("  ,project_level =  ?");
        sql.append(" ,charge_department = ?");
        sql.append(" ,performance =  ?");
        sql.append(" ,approve_result =  ?");
        sql.append(" ,modify_by =  ?");
        sql.append("  ,modify_date = ?");
        sql.append("  WHERE id = ?");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (SchoolReformVO schoolReformVO : schoolReformVOs) {
            Object[] param = new Object[9];
            param[0] = schoolReformVO.getImplement_time();
            param[1] = schoolReformVO.getProject_name();
            param[2] = schoolReformVO.getProject_level();
            param[3] = schoolReformVO.getCharge_department();
            param[4] = schoolReformVO.getPerformance();
            param[5] = "1";
            param[6] = userid;
            param[7] = DatetimeUtil.getNow("");
            param[8] = schoolReformVO.getId();
            batchArgs.add(param);
        }
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }
    
    public void saveMasterReviewData(MasterReviewVO masterReviewVO) {
        
    }
    
    public void optionTabDeal(MasterReviewVO masterReviewVO, String option_tab_type){
        if (option_tab_type.equals("baseinfo")) {//基础信息
            updateHeadmasterBaseInfo(masterReviewVO);
            updateHeadmaster(masterReviewVO);
        } else if (option_tab_type.equals("workExperience")) {// 任职年限
             List<WorkExperienceVO> addWorkExperienceVOs = new ArrayList<WorkExperienceVO>();
             List<WorkExperienceVO> updateWorkExperienceVOs = new ArrayList<WorkExperienceVO>();
            for(WorkExperienceVO workExperienceVO:  masterReviewVO.getWorkExperienceVOs()){
                if(StringUtil.isEmpty(workExperienceVO.getId())){
                    addWorkExperienceVOs.add(workExperienceVO);
                }else{
                    updateWorkExperienceVOs.add(workExperienceVO);
                }
            }
            updateWorkExperience(updateWorkExperienceVOs,masterReviewVO.getId());
            insertWorkExperience(addWorkExperienceVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("education")) {// 学历情况
            List<EducationVO> addEducationVOs = new ArrayList<EducationVO>();
            List<EducationVO> updateEducationVOs = new ArrayList<EducationVO>();
            for (EducationVO educationVO : masterReviewVO.getEducationVOs()) {
                if(StringUtil.isEmpty(educationVO.getId())){
                    addEducationVOs.add(educationVO);
                }else{
                    updateEducationVOs.add(educationVO);
                }
            }
            updateEducation(updateEducationVOs,masterReviewVO.getId());
            insertEducation(addEducationVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("professionalTitle")) {// 职称情况
            List<ProfessionalTitleVO> addProfessionalTitleVOs = new ArrayList<ProfessionalTitleVO>();
            List<ProfessionalTitleVO> updateProfessionalTitleVOs = new ArrayList<ProfessionalTitleVO>();
            for (ProfessionalTitleVO professionalTitleVO : masterReviewVO.getProfessionalTitleVOs()) {
                if(StringUtil.isEmpty(professionalTitleVO.getId())){
                    addProfessionalTitleVOs.add(professionalTitleVO);
                }else{
                    updateProfessionalTitleVOs.add(professionalTitleVO);
                }
            }
            updateProfessionalTitle(updateProfessionalTitleVOs,masterReviewVO.getId());
            insertProfessionalTitle(addProfessionalTitleVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("managementDifficulty")) {// 现任管理难度
            updateHeadmasterManagementDiff(masterReviewVO);
        } else if (option_tab_type.equals("paper")) {// 论文情况
            List<PaperVO> addPaperVOs = new ArrayList<PaperVO>();
            List<PaperVO> updatePaperVOs = new ArrayList<PaperVO>();
            for (PaperVO paperVO : masterReviewVO.getPaperVOs()) {
                if(StringUtil.isEmpty(paperVO.getId())){
                    addPaperVOs.add(paperVO);
                }else{
                    updatePaperVOs.add(paperVO);
                }
            }
            updatePaper(updatePaperVOs,masterReviewVO.getId());
            insertPaper(addPaperVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("workPublish")) {// 著作发表情况
            List<BookPublishVO> addBookPublishVOs = new ArrayList<BookPublishVO>();
            List<BookPublishVO> updateBookPublishVOs = new ArrayList<BookPublishVO>();
            for (BookPublishVO bookPublishVO : masterReviewVO.getBookPublishVOs()) {
                if(StringUtil.isEmpty(bookPublishVO.getId())){
                    addBookPublishVOs.add(bookPublishVO);
                }else{
                    updateBookPublishVOs.add(bookPublishVO);
                }
            }
            updateWorkPublish(updateBookPublishVOs,masterReviewVO.getId());
            insertWorkPublish(addBookPublishVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("subject")) {// 课题发表情况
            List<SubjectVO> addSubjectVOs = new ArrayList<SubjectVO>();
            List<SubjectVO> updateSubjectVOs = new ArrayList<SubjectVO>();
            for (SubjectVO subjectVO : masterReviewVO.getSubjectVOs()) {
                if(StringUtil.isEmpty(subjectVO.getId())){
                    addSubjectVOs.add(subjectVO);
                }else{
                    updateSubjectVOs.add(subjectVO);
                }
            }
            updateSubject(updateSubjectVOs,masterReviewVO.getId());
            insertSubject(addSubjectVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("personalAward")) {// 个人获奖情况
            List<PersonalAwardVO> addPersonalAwardVOs = new ArrayList<PersonalAwardVO>();
            List<PersonalAwardVO> updatePersonalAwardVOs = new ArrayList<PersonalAwardVO>();
            for (PersonalAwardVO personalAwardVO : masterReviewVO.getPersonalAwardVOs()) {
                if(StringUtil.isEmpty(personalAwardVO.getId())){
                    addPersonalAwardVOs.add(personalAwardVO);
                }else{
                    updatePersonalAwardVOs.add(personalAwardVO);
                }
            }
            updatePersonalAward(updatePersonalAwardVOs,masterReviewVO.getId());
            insertPersonalAward(addPersonalAwardVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("schoolAward")) {// 学校获得荣誉
            List<SchoolAwardVO> addSchoolAwardVOs = new ArrayList<SchoolAwardVO>();
            List<SchoolAwardVO> updateSchoolAwardVOs = new ArrayList<SchoolAwardVO>();
            for (SchoolAwardVO schoolAwardVO : masterReviewVO.getSchoolAwardVOs()) {
                if(StringUtil.isEmpty(schoolAwardVO.getId())){
                    addSchoolAwardVOs.add(schoolAwardVO);
                }else{
                    updateSchoolAwardVOs.add(schoolAwardVO);
                }
            }
            updateSchoolAward(updateSchoolAwardVOs,masterReviewVO.getId());
            insertSchoolAward(addSchoolAwardVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("studyTrain")) {// 进修学习
            List<StudyTrainVO> addStudyTrainVOs = new ArrayList<StudyTrainVO>();
            List<StudyTrainVO> updateStudyTrainVOs = new ArrayList<StudyTrainVO>();
            for (StudyTrainVO studyTrainVO : masterReviewVO.getStudyTrainVOs()) {
                if(StringUtil.isEmpty(studyTrainVO.getId())){
                    addStudyTrainVOs.add(studyTrainVO);
                }else{
                    updateStudyTrainVOs.add(studyTrainVO);
                }
            }
         //   updateProfessionalTitle(updateProfessionalTitleVOs,masterReviewVO.getId());
         //   insertProfessionalTitle(addProfessionalTitleVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("schoolReform")) {// 学校特色及改革
            List<SchoolReformVO> addStudyTrainVOs = new ArrayList<SchoolReformVO>();
            List<SchoolReformVO> updateStudyTrainVOs = new ArrayList<SchoolReformVO>();
            for (SchoolReformVO schoolReformVO : masterReviewVO.getSchoolReformVOs()) {
                if(StringUtil.isEmpty(schoolReformVO.getId())){
                    addStudyTrainVOs.add(schoolReformVO);
                }else{
                    updateStudyTrainVOs.add(schoolReformVO);
                }
            }
            updateSchoolReform(updateStudyTrainVOs);
            addSchoolReform(addStudyTrainVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("socialDuty")) {// 社会责任
            List<SocialDutyVO> addSocialDutyVOs = new ArrayList<SocialDutyVO>();
            List<SocialDutyVO> updateSocialDutyVOs = new ArrayList<SocialDutyVO>();
            for (SocialDutyVO socialDutyVO : masterReviewVO.getSocialDutyVOs()) {
                if(StringUtil.isEmpty(socialDutyVO.getId())){
                    addSocialDutyVOs.add(socialDutyVO);
                }else{
                    updateSocialDutyVOs.add(socialDutyVO);
                }
            }
            updateSocialDuty(updateSocialDutyVOs);
            addSocialDuty(addSocialDutyVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("accident")) {// 责任事故
            List<AccidentVO> addAccidentVOs = new ArrayList<AccidentVO>();
            List<AccidentVO> updateAccidentVOs = new ArrayList<AccidentVO>();
            for (AccidentVO accidentVO : masterReviewVO.getAccidentVOs()) {
                if(StringUtil.isEmpty(accidentVO.getId())){
                    addAccidentVOs.add(accidentVO);
                }else{
                    updateAccidentVOs.add(accidentVO);
                }
            }
            updateAccident(updateAccidentVOs);
            addAccident(addAccidentVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("punishment")) {// 处分
            List<PunishmentVO> addPunishmentVOs = new ArrayList<PunishmentVO>();
            List<PunishmentVO> updatePunishmentVOs = new ArrayList<PunishmentVO>();
            for (PunishmentVO punishmentVO : masterReviewVO.getPunishmentVOs()) {
                if(StringUtil.isEmpty(punishmentVO.getId())){
                    addPunishmentVOs.add(punishmentVO);
                }else{
                    updatePunishmentVOs.add(punishmentVO);
                }
            }
            updatePunishment(updatePunishmentVOs);
            addPunishment(addPunishmentVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("workHistory")) {// 工作经历
            List<WorkHistoryVO> addWorkHistoryVOs = new ArrayList<WorkHistoryVO>();
            List<WorkHistoryVO> updateWorkHistoryVOs = new ArrayList<WorkHistoryVO>();
            for (WorkHistoryVO workHistoryVO : masterReviewVO.getWorkHistoryVOs()) {
                if(StringUtil.isEmpty(workHistoryVO.getId())){
                    addWorkHistoryVOs.add(workHistoryVO);
                }else{
                    updateWorkHistoryVOs.add(workHistoryVO);
                }
            }
            updateWorkHistory(updateWorkHistoryVOs);
            addWorkHistory(addWorkHistoryVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("gradeEvaluate")) {// 登记评估
            List<GradeEvaluateVO> addGradeEvaluateVOs = new ArrayList<GradeEvaluateVO>();
            List<GradeEvaluateVO> updateGradeEvaluateVOs = new ArrayList<GradeEvaluateVO>();
            for (GradeEvaluateVO gradeEvaluateVO : masterReviewVO.getGradeEvaluateVOs()) {
                if(StringUtil.isEmpty(gradeEvaluateVO.getId())){
                    addGradeEvaluateVOs.add(gradeEvaluateVO);
                }else{
                    updateGradeEvaluateVOs.add(gradeEvaluateVO);
                }
            }
            updateGradeEvaluate(updateGradeEvaluateVOs);
            addGradeEvaluate(addGradeEvaluateVOs,masterReviewVO.getId());
        } else if (option_tab_type.equals("run_school")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"run_school",masterReviewVO.getRun_school());
        } else if (option_tab_type.equals("school_management")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"school_management",masterReviewVO.getSchool_management());
        } else if (option_tab_type.equals("education_science")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"education_science",masterReviewVO.getEducation_science());
        } else if (option_tab_type.equals("external_environment")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"external_environment",masterReviewVO.getExternal_environment());
        } else if (option_tab_type.equals("student_development")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"student_development",masterReviewVO.getStudent_development());
        } else if (option_tab_type.equals("teacher_development")) {// 办学思想
            updateHeadmasterSingleProperty(masterReviewVO,"teacher_development",masterReviewVO.getTeacher_development());
        }
    }

    private void updateHeadmasterManagementDiff(MasterReviewVO masterReviewVO) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster");
        sql.append(" SET ");

        sql.append(" school_name_space = ?,");
        sql.append(" student_number = ?,");
        sql.append(" school_count = ?,");
        sql.append("school_type = ?,");
        sql.append(" manage_difficulty_attachment_id = ?,");

        sql.append(" school_name_space_ago = ?,");
        sql.append(" student_number_ago = ?,");
        sql.append("  school_count_ago = ?,");
        sql.append("  school_type_ago = ?,");
        sql.append(" manage_difficulty_ago_attachment_id = ?,");

        sql.append(" modify_by = ?,");
        sql.append("modify_date = ?");
        sql.append(" WHERE id = ?");

        Object[] param = new Object[] { masterReviewVO.getSchoolNameSpace(),
                masterReviewVO.getStudentNumber(), masterReviewVO.getSchoolCount(),
                masterReviewVO.getSchoolType(), masterReviewVO.getManage_difficulty_attachment_id(),
                masterReviewVO.getSchoolNameSpaceAgo(), masterReviewVO.getStudentNumberAgo(),
                masterReviewVO.getSchoolCountAgo(), masterReviewVO.getSchoolTypeAgo(),
                masterReviewVO.getManage_difficulty_ago_attachment_id(), userid, DatetimeUtil.getNow(""),
                masterReviewVO.getId() };
        jdbcTemplate.update(sql.toString(), param);
    }
}
