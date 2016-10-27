package cn.com.bright.masterReview.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.HistoryActinstVO;

/**
 * ����VO
 * @ClassName: MasterReviewVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015��12��18�� ����3:22:39
 */
public class MasterReviewVO {
    
    private String id;
    
    /**
     * У��id
     */
    private String headerMasterId;
    
    /**
     * У��code
     */
    private String headerMasterCode;
    
    /**
     * У������
     */
    private String headerMasterName;

    private String mobile;
    /**
     * ���֤
     */
    private String identitycard;
    private String schoolId;
    private String schoolName;
    /**
     * ����ְ��
     */
    private String present_occupation;
    
    //����רҵ����ְ��
    private String present_major_occupation;
    private String applylevel;
    
    private String usersex;
    
    private String email;
    
    //ѧ��
    private String phasestudy;
    
    //ѧУ����
    private String school_class;
    
    //�μӹ���ʱ��
    private Date join_work_time;
    
    //�μӽ�������ʱ��
    private Date join_educate_work_time;
    
    //������ò
    private String politics_status;
    
    //����
    private String teach_age;
    
    ///����
    private String native_place;
    
    //����
    private String census_register;
    
    //����
    private String nation;
    
    //����ͷ�񸽼�id
    private String person_img_attachId;
    
    //����ͷ�񸽼�Vo
    private AttachMentVO person_img_attachMentVO;
    
    //������ѧУ
    private String lodge_school;
    
    
    
    
    
    /**
     * ��ѧУ����
     */
    private String schoolNameSpace;
    
    /**
     * ��ѧУ����
     */
    private String schoolType;
    
    /**
     * ��У������
     */
    private String schoolCount;
    
    /**
     * ��ѧ������
     */
    private String studentNumber;
    
    /**
     * ������ѧУ����
     */
    private String schoolNameSpaceAgo;
    
    /**
     * ������ѧУ����
     */
    private String schoolTypeAgo;
    
    /**
     * ������У������
     */
    private String schoolCountAgo;
    
    /**
     * ������ѧ������
     */
    private String studentNumberAgo;
    
    
    private String manage_difficulty_approve_result;
    
    private String manage_difficulty_attachment_id;
    
    private AttachMentVO manageDifficultyAttachMentVO;
    
    
  private String manage_difficulty_ago_approve_result;
    
    private String manage_difficulty_ago_attachment_id;
    
    private AttachMentVO manageDifficultyAgoAttachMentVO;
    
    

  

    //��ͨ������ûͨ��
    private String apply_result;
    //�����ǽ����л��Ǳ�����
    private String apply_status;
    private String apply_total_point;
    
    private String work_report;
    private String work_report_approve_result;
    
    private String base_info_approve_result;
    
  //�Ƿ�����У��
    private String isPositive;
    
    private String isSaveDraft;
    
    private String address;
    
    //�Ƿ��ǲ�������ڵ�
    private boolean idRefill;
    
   //��ѧ˼��
    private String run_school;
    
    private String run_school_approve_result;
  //ѧУ����
    private String school_management;
    
    private String school_management_approve_result;
  //������ѧ
    private String education_science;
    
    private String education_science_approve_result;
  //�ⲿ����
    private String external_environment;
    
    private String external_environment_approve_result;
  //ѧ����չ
    private String student_development;
    
    private String student_development_approve_result;
  //��ʦ��չ
    private String teacher_development;
    
    private String teacher_development_approve_result;
    
    //�ύ��ѡ�����
    private String option_tab_type;
    private String current_option_num;

    
    private String run_school_attachment_id;
    
    private AttachMentVO run_school_attachMentVO;
    
    private String school_management_attachment_id;
    
    private AttachMentVO school_management_attachMentVO;
    
    
    private String education_science_attachment_id;
    
    private AttachMentVO education_science_attachMentVO;
    
    
    private String external_environment_attachment_id;
    
    private AttachMentVO external_environment_attachMentVO;
    
    
    private String student_development_attachment_id;
    
    private AttachMentVO student_development_attachMentVO;
    
    
    private String teacher_development_attachment_id;
    
    private AttachMentVO teacher_development_attachMentVO;

    /**
     * ��ְ����
     */
    private List<WorkExperienceVO> workExperienceVOs = new ArrayList<WorkExperienceVO>();
    
    /**
     * ѧ�����
     */
    private List<EducationVO> educationVOs= new ArrayList<EducationVO>();;
    
    /**
     * ְ�����
     */
    private List<ProfessionalTitleVO> professionalTitleVOs= new ArrayList<ProfessionalTitleVO>();;
    
    /**
     *���ķ������
     */
    private List<PaperVO> paperVOs= new ArrayList<PaperVO>();;
    
    /**
     *  �����������
     */
    private List<BookPublishVO> bookPublishVOs= new ArrayList<BookPublishVO>();;
    
    /**
     * �������
     */
    private List<SubjectVO> subjectVOs= new ArrayList<SubjectVO>();;
    
    /**
     * ���˻����
     */
    private List<PersonalAwardVO> personalAwardVOs= new ArrayList<PersonalAwardVO>();;
    
    /**
     * ѧУ�������
     */
    private List<SchoolAwardVO> schoolAwardVOs= new ArrayList<SchoolAwardVO>();;
    
    
    private List<HistoryActinstVO> historyActinstVOs;
    
    /**
     * ����ѧϰVO
     */
    private List<StudyTrainVO> studyTrainVOs= new ArrayList<StudyTrainVO>();;
    
    /**
     * �ӷ�ѧУ��ɫ�͸ĸ�
     */
    private List<SchoolReformVO> schoolReformVOs= new ArrayList<SchoolReformVO>();;
    
    /**
     * �ӷ��������
     */
    private List<SocialDutyVO> socialDutyVOs= new ArrayList<SocialDutyVO>();;
    
    /**
     * ���������¹�
     */
    private List<AccidentVO> accidentVOs= new ArrayList<AccidentVO>();;
    
    /**
     * ���ִ���
     */
    private List<PunishmentVO> punishmentVOs= new ArrayList<PunishmentVO>();;
    
    /**
     * ѧУ�ȼ�����
     */
    private List<GradeEvaluateVO> gradeEvaluateVOs= new ArrayList<GradeEvaluateVO>();;
    
    /**
     * ��������
     */
    private List<WorkHistoryVO> workHistoryVOs= new ArrayList<WorkHistoryVO>();;
    
    public String getHeaderMasterId() {
        return headerMasterId;
    }

    public void setHeaderMasterId(String headerMasterId) {
        this.headerMasterId = headerMasterId;
    }

    public String getHeaderMasterName() {
        return headerMasterName;
    }

    public void setHeaderMasterName(String headerMasterName) {
        this.headerMasterName = headerMasterName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(String identitycard) {
        this.identitycard = identitycard;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    

    public String getPresent_occupation() {
		return present_occupation;
	}

	public void setPresent_occupation(String present_occupation) {
		this.present_occupation = present_occupation;
	}

	public String getApplylevel() {
        return applylevel;
    }

    public void setApplylevel(String applylevel) {
        this.applylevel = applylevel;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getSchoolCount() {
        return schoolCount;
    }

    public void setSchoolCount(String schoolCount) {
        this.schoolCount = schoolCount;
    }

    public String getSchoolCountAgo() {
        return schoolCountAgo;
    }

    public void setSchoolCountAgo(String schoolCountAgo) {
        this.schoolCountAgo = schoolCountAgo;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getSchoolTypeAgo() {
        return schoolTypeAgo;
    }

    public void setSchoolTypeAgo(String schoolTypeAgo) {
        this.schoolTypeAgo = schoolTypeAgo;
    }

    public String getStudentNumberAgo() {
        return studentNumberAgo;
    }

    public void setStudentNumberAgo(String studentNumberAgo) {
        this.studentNumberAgo = studentNumberAgo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WorkExperienceVO> getWorkExperienceVOs() {
        return workExperienceVOs;
    }

    public void setWorkExperienceVOs(List<WorkExperienceVO> workExperienceVOs) {
        this.workExperienceVOs = workExperienceVOs;
    }

    public List<EducationVO> getEducationVOs() {
        return educationVOs;
    }

    public void setEducationVOs(List<EducationVO> educationVOs) {
        this.educationVOs = educationVOs;
    }

    public List<ProfessionalTitleVO> getProfessionalTitleVOs() {
        return professionalTitleVOs;
    }

    public void setProfessionalTitleVOs(List<ProfessionalTitleVO> professionalTitleVOs) {
        this.professionalTitleVOs = professionalTitleVOs;
    }

    public List<PaperVO> getPaperVOs() {
        return paperVOs;
    }

    public void setPaperVOs(List<PaperVO> paperVOs) {
        this.paperVOs = paperVOs;
    }

    public List<BookPublishVO> getBookPublishVOs() {
        return bookPublishVOs;
    }

    public void setBookPublishVOs(List<BookPublishVO> bookPublishVOs) {
        this.bookPublishVOs = bookPublishVOs;
    }

    public List<SubjectVO> getSubjectVOs() {
        return subjectVOs;
    }

    public void setSubjectVOs(List<SubjectVO> subjectVOs) {
        this.subjectVOs = subjectVOs;
    }

    public List<PersonalAwardVO> getPersonalAwardVOs() {
        return personalAwardVOs;
    }

    public void setPersonalAwardVOs(List<PersonalAwardVO> personalAwardVOs) {
        this.personalAwardVOs = personalAwardVOs;
    }

    public List<SchoolAwardVO> getSchoolAwardVOs() {
        return schoolAwardVOs;
    }

    public void setSchoolAwardVOs(List<SchoolAwardVO> schoolAwardVOs) {
        this.schoolAwardVOs = schoolAwardVOs;
    }

    public String getManage_difficulty_approve_result() {
        return manage_difficulty_approve_result;
    }

    public void setManage_difficulty_approve_result(String manage_difficulty_approve_result) {
        this.manage_difficulty_approve_result = manage_difficulty_approve_result;
    }

    public String getManage_difficulty_ago_approve_result() {
        return manage_difficulty_ago_approve_result;
    }

    public void setManage_difficulty_ago_approve_result(String manage_difficulty_ago_approve_result) {
        this.manage_difficulty_ago_approve_result = manage_difficulty_ago_approve_result;
    }

    public String getApply_result() {
        return apply_result;
    }

    public void setApply_result(String apply_result) {
        this.apply_result = apply_result;
    }

    public String getApply_status() {
        return apply_status;
    }

    public void setApply_status(String apply_status) {
        this.apply_status = apply_status;
    }

    public String getApply_total_point() {
        return apply_total_point;
    }

    public void setApply_total_point(String apply_total_point) {
        this.apply_total_point = apply_total_point;
    }

    public String getWork_report() {
        return work_report;
    }

    public void setWork_report(String work_report) {
        this.work_report = work_report;
    }

    public String getWork_report_approve_result() {
        return work_report_approve_result;
    }

    public void setWork_report_approve_result(String work_report_approve_result) {
        this.work_report_approve_result = work_report_approve_result;
    }

    public String getBase_info_approve_result() {
        return base_info_approve_result;
    }

    public void setBase_info_approve_result(String base_info_approve_result) {
        this.base_info_approve_result = base_info_approve_result;
    }

    public List<HistoryActinstVO> getHistoryActinstVOs() {
        return historyActinstVOs;
    }

    public void setHistoryActinstVOs(List<HistoryActinstVO> historyActinstVOs) {
        this.historyActinstVOs = historyActinstVOs;
    }

    public String getIsSaveDraft() {
        return isSaveDraft;
    }

    public void setIsSaveDraft(String isSaveDraft) {
        this.isSaveDraft = isSaveDraft;
    }

    public String getManage_difficulty_attachment_id() {
        return manage_difficulty_attachment_id;
    }

    public void setManage_difficulty_attachment_id(String manage_difficulty_attachment_id) {
        this.manage_difficulty_attachment_id = manage_difficulty_attachment_id;
    }

    public AttachMentVO getManageDifficultyAttachMentVO() {
        return manageDifficultyAttachMentVO;
    }

    public void setManageDifficultyAttachMentVO(AttachMentVO manageDifficultyAttachMentVO) {
        this.manageDifficultyAttachMentVO = manageDifficultyAttachMentVO;
    }

    public String getManage_difficulty_ago_attachment_id() {
        return manage_difficulty_ago_attachment_id;
    }

    public void setManage_difficulty_ago_attachment_id(String manage_difficulty_ago_attachment_id) {
        this.manage_difficulty_ago_attachment_id = manage_difficulty_ago_attachment_id;
    }

    public AttachMentVO getManageDifficultyAgoAttachMentVO() {
        return manageDifficultyAgoAttachMentVO;
    }

    public void setManageDifficultyAgoAttachMentVO(AttachMentVO manageDifficultyAgoAttachMentVO) {
        this.manageDifficultyAgoAttachMentVO = manageDifficultyAgoAttachMentVO;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(String isPositive) {
        this.isPositive = isPositive;
    }

    public List<StudyTrainVO> getStudyTrainVOs() {
        return studyTrainVOs;
    }

    public void setStudyTrainVOs(List<StudyTrainVO> studyTrainVOs) {
        this.studyTrainVOs = studyTrainVOs;
    }

    public List<SchoolReformVO> getSchoolReformVOs() {
        return schoolReformVOs;
    }

    public void setSchoolReformVOs(List<SchoolReformVO> schoolReformVOs) {
        this.schoolReformVOs = schoolReformVOs;
    }

    public List<SocialDutyVO> getSocialDutyVOs() {
        return socialDutyVOs;
    }

    public void setSocialDutyVOs(List<SocialDutyVO> socialDutyVOs) {
        this.socialDutyVOs = socialDutyVOs;
    }

    public List<AccidentVO> getAccidentVOs() {
        return accidentVOs;
    }

    public void setAccidentVOs(List<AccidentVO> accidentVOs) {
        this.accidentVOs = accidentVOs;
    }

    public List<PunishmentVO> getPunishmentVOs() {
        return punishmentVOs;
    }

    public void setPunishmentVOs(List<PunishmentVO> punishmentVOs) {
        this.punishmentVOs = punishmentVOs;
    }

    public boolean isIdRefill() {
        return idRefill;
    }

    public void setIdRefill(boolean idRefill) {
        this.idRefill = idRefill;
    }

    public List<GradeEvaluateVO> getGradeEvaluateVOs() {
        return gradeEvaluateVOs;
    }

    public void setGradeEvaluateVOs(List<GradeEvaluateVO> gradeEvaluateVOs) {
        this.gradeEvaluateVOs = gradeEvaluateVOs;
    }

    public List<WorkHistoryVO> getWorkHistoryVOs() {
        return workHistoryVOs;
    }

    public void setWorkHistoryVOs(List<WorkHistoryVO> workHistoryVOs) {
        this.workHistoryVOs = workHistoryVOs;
    }

    public String getSchoolNameSpace() {
        return schoolNameSpace;
    }

    public void setSchoolNameSpace(String schoolNameSpace) {
        this.schoolNameSpace = schoolNameSpace;
    }

    public String getSchoolNameSpaceAgo() {
        return schoolNameSpaceAgo;
    }

    public void setSchoolNameSpaceAgo(String schoolNameSpaceAgo) {
        this.schoolNameSpaceAgo = schoolNameSpaceAgo;
    }

    public String getRun_school() {
        return run_school;
    }

    public void setRun_school(String run_school) {
        this.run_school = run_school;
    }

    public String getSchool_management() {
        return school_management;
    }

    public void setSchool_management(String school_management) {
        this.school_management = school_management;
    }

    public String getEducation_science() {
        return education_science;
    }

    public void setEducation_science(String education_science) {
        this.education_science = education_science;
    }

    public String getExternal_environment() {
        return external_environment;
    }

    public void setExternal_environment(String external_environment) {
        this.external_environment = external_environment;
    }

    public String getStudent_development() {
        return student_development;
    }

    public void setStudent_development(String student_development) {
        this.student_development = student_development;
    }

    public String getTeacher_development() {
        return teacher_development;
    }

    public void setTeacher_development(String teacher_development) {
        this.teacher_development = teacher_development;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeaderMasterCode() {
        return headerMasterCode;
    }

    public void setHeaderMasterCode(String headerMasterCode) {
        this.headerMasterCode = headerMasterCode;
    }

    public String getPresent_major_occupation() {
        return present_major_occupation;
    }

    public void setPresent_major_occupation(String present_major_occupation) {
        this.present_major_occupation = present_major_occupation;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getPhasestudy() {
        return phasestudy;
    }

    public void setPhasestudy(String phasestudy) {
        this.phasestudy = phasestudy;
    }


    public String getSchool_class() {
        return school_class;
    }

    public void setSchool_class(String school_class) {
        this.school_class = school_class;
    }

    public Date getJoin_work_time() {
        return join_work_time;
    }

    public void setJoin_work_time(Date join_work_time) {
        this.join_work_time = join_work_time;
    }

    public Date getJoin_educate_work_time() {
        return join_educate_work_time;
    }

    public void setJoin_educate_work_time(Date join_educate_work_time) {
        this.join_educate_work_time = join_educate_work_time;
    }

    public String getPolitics_status() {
        return politics_status;
    }

    public void setPolitics_status(String politics_status) {
        this.politics_status = politics_status;
    }

    public String getTeach_age() {
        return teach_age;
    }

    public void setTeach_age(String teach_age) {
        this.teach_age = teach_age;
    }

    public String getNative_place() {
        return native_place;
    }

    public void setNative_place(String native_place) {
        this.native_place = native_place;
    }

    public String getCensus_register() {
        return census_register;
    }

    public void setCensus_register(String census_register) {
        this.census_register = census_register;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPerson_img_attachId() {
        return person_img_attachId;
    }

    public void setPerson_img_attachId(String person_img_attachId) {
        this.person_img_attachId = person_img_attachId;
    }

    public AttachMentVO getPerson_img_attachMentVO() {
        return person_img_attachMentVO;
    }

    public void setPerson_img_attachMentVO(AttachMentVO person_img_attachMentVO) {
        this.person_img_attachMentVO = person_img_attachMentVO;
    }

    public String getOption_tab_type() {
        return option_tab_type;
    }

    public void setOption_tab_type(String option_tab_type) {
        this.option_tab_type = option_tab_type;
    }

    public String getCurrent_option_num() {
        return current_option_num;
    }

    public void setCurrent_option_num(String current_option_num) {
        this.current_option_num = current_option_num;
    }

	public String getRun_school_attachment_id() {
		return run_school_attachment_id;
	}

	public void setRun_school_attachment_id(String run_school_attachment_id) {
		this.run_school_attachment_id = run_school_attachment_id;
	}

	public AttachMentVO getRun_school_attachMentVO() {
		return run_school_attachMentVO;
	}

	public void setRun_school_attachMentVO(AttachMentVO run_school_attachMentVO) {
		this.run_school_attachMentVO = run_school_attachMentVO;
	}

	public String getSchool_management_attachment_id() {
		return school_management_attachment_id;
	}

	public void setSchool_management_attachment_id(
			String school_management_attachment_id) {
		this.school_management_attachment_id = school_management_attachment_id;
	}

	public AttachMentVO getSchool_management_attachMentVO() {
		return school_management_attachMentVO;
	}

	public void setSchool_management_attachMentVO(
			AttachMentVO school_management_attachMentVO) {
		this.school_management_attachMentVO = school_management_attachMentVO;
	}

	public String getEducation_science_attachment_id() {
		return education_science_attachment_id;
	}

	public void setEducation_science_attachment_id(
			String education_science_attachment_id) {
		this.education_science_attachment_id = education_science_attachment_id;
	}

	public AttachMentVO getEducation_science_attachMentVO() {
		return education_science_attachMentVO;
	}

	public void setEducation_science_attachMentVO(
			AttachMentVO education_science_attachMentVO) {
		this.education_science_attachMentVO = education_science_attachMentVO;
	}

	public String getExternal_environment_attachment_id() {
		return external_environment_attachment_id;
	}

	public void setExternal_environment_attachment_id(
			String external_environment_attachment_id) {
		this.external_environment_attachment_id = external_environment_attachment_id;
	}

	public AttachMentVO getExternal_environment_attachMentVO() {
		return external_environment_attachMentVO;
	}

	public void setExternal_environment_attachMentVO(
			AttachMentVO external_environment_attachMentVO) {
		this.external_environment_attachMentVO = external_environment_attachMentVO;
	}

	public String getStudent_development_attachment_id() {
		return student_development_attachment_id;
	}

	public void setStudent_development_attachment_id(
			String student_development_attachment_id) {
		this.student_development_attachment_id = student_development_attachment_id;
	}

	public AttachMentVO getStudent_development_attachMentVO() {
		return student_development_attachMentVO;
	}

	public void setStudent_development_attachMentVO(
			AttachMentVO student_development_attachMentVO) {
		this.student_development_attachMentVO = student_development_attachMentVO;
	}

	public String getTeacher_development_attachment_id() {
		return teacher_development_attachment_id;
	}

	public void setTeacher_development_attachment_id(
			String teacher_development_attachment_id) {
		this.teacher_development_attachment_id = teacher_development_attachment_id;
	}

	public AttachMentVO getTeacher_development_attachMentVO() {
		return teacher_development_attachMentVO;
	}

	public void setTeacher_development_attachMentVO(
			AttachMentVO teacher_development_attachMentVO) {
		this.teacher_development_attachMentVO = teacher_development_attachMentVO;
	}

	public String getLodge_school() {
		return lodge_school;
	}

	public void setLodge_school(String lodge_school) {
		this.lodge_school = lodge_school;
	}

	public String getRun_school_approve_result() {
		return run_school_approve_result;
	}

	public void setRun_school_approve_result(String run_school_approve_result) {
		this.run_school_approve_result = run_school_approve_result;
	}

	public String getSchool_management_approve_result() {
		return school_management_approve_result;
	}

	public void setSchool_management_approve_result(
			String school_management_approve_result) {
		this.school_management_approve_result = school_management_approve_result;
	}

	public String getEducation_science_approve_result() {
		return education_science_approve_result;
	}

	public void setEducation_science_approve_result(
			String education_science_approve_result) {
		this.education_science_approve_result = education_science_approve_result;
	}

	public String getExternal_environment_approve_result() {
		return external_environment_approve_result;
	}

	public void setExternal_environment_approve_result(
			String external_environment_approve_result) {
		this.external_environment_approve_result = external_environment_approve_result;
	}

	public String getStudent_development_approve_result() {
		return student_development_approve_result;
	}

	public void setStudent_development_approve_result(
			String student_development_approve_result) {
		this.student_development_approve_result = student_development_approve_result;
	}

	public String getTeacher_development_approve_result() {
		return teacher_development_approve_result;
	}

	public void setTeacher_development_approve_result(
			String teacher_development_approve_result) {
		this.teacher_development_approve_result = teacher_development_approve_result;
	}
    
    
}
