package cn.com.bright.masterReview;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
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
import cn.com.bright.masterReview.api.SituationVO;
import cn.com.bright.masterReview.api.SocialDutyVO;
import cn.com.bright.masterReview.api.StudyTrainVO;
import cn.com.bright.masterReview.api.SubjectVO;
import cn.com.bright.masterReview.api.WorkExperienceVO;
import cn.com.bright.masterReview.api.WorkHistoryVO;
import cn.com.bright.masterReview.util.MasterReviewConstant;
import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.HistoryActinstVO;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.NoApproveUsersException;
import cn.com.bright.workflow.exception.PermissionValidateException;
import cn.com.bright.workflow.exception.ProcessInstanceStartException;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.exception.TaskNoExistException;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.util.JsonUtil;
import cn.com.bright.workflow.web.action.BaseWorkflowAction;

public class MasterReviewAction extends BaseWorkflowAction {
    private Log log4j = new Log(this.getClass().toString());

    private MasterReviewService masterReviewService;

    public Document doPost(Document xmlDoc) throws ParseException, IOException {
        this.setMasterReviewService(ApplicationContextHelper.getBean(MasterReviewService.class));
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();

        if ("startMasterReviewProcess".equals(action)) {
            startMasterReviewProcess();
        } else if ("completeMasterReviewTask".equals(action)) {
            completeMasterReviewTask();
        } else if ("completeAreaCadresTask".equals(action)) {
            completeAreaCadresTask();
        } else if ("completePersonnelLeaderTask".equals(action)) {
            completePersonnelLeaderTask();
        } else if ("completeProfessorApproveTask".equals(action)) {
            completeProfessorApproveTask();
        } else if ("completeRefillTask".equals(action)) {
            completeRefillTask();
        }else if ("viewMasterReview".equals(action)) {
            viewMasterReview();
        } else if ("saveMasterReviewData".equals(action)) {
            saveMasterReviewData();
        }else if ("addWorkExperience".equals(action)) {
            addWorkExperience();
        }else if ("addEducation".equals(action)) {
            addEducation();
        }else if ("addProfessionalTitle".equals(action)) {
            addProfessionalTitle();
        }else if ("addPaper".equals(action)) {
            addPaper();
        }else if ("addWorkPublish".equals(action)) {
            addWorkPublish();
        }else if ("addSubject".equals(action)) {
            addSubject();
        }else if ("addPersonalAward".equals(action)) {
            addPersonalAward();
        }else if ("addSchoolAward".equals(action)) {
            addSchoolAward();
        }else if ("addSchoolReform".equals(action)) {
            addSchoolReform();
        } else if ("addSocialDuty".equals(action)) {
            addSocialDuty();
        }else if ("addAccident".equals(action)) {
            addAccident();
        }else if ("addPunishment".equals(action)) {
            addPunishment();
        }else if ("addWorkHistory".equals(action)) {
            addWorkHistory();
        }else if ("addGradeEvaluate".equals(action)) {
            addGradeEvaluate();
        }else if ("getMasterReviewBusinessKey".equals(action)) {
            getMasterReviewBusinessKey();
        }else if ("deleteWorkExperience".equals(action)) {
            deleteWorkExperience();
        }else if ("deleteEducation".equals(action)) {
            deleteEducation();
        }else if ("deleteProfessionalTitle".equals(action)) {
            deleteProfessionalTitle();
        }else if ("deletePaper".equals(action)) {
            deletePaper();
        }else if ("deleteWorkPublish".equals(action)) {
            deleteWorkPublish();
        }else if ("deleteSubject".equals(action)) {
            deleteSubject();
        }else if ("deletePersonalAward".equals(action)) {
            deletePersonalAward();
        }else if ("deleteSchoolAward".equals(action)) {
            deleteSchoolAward();
        }else if ("deleteSchoolReform".equals(action)) {
            deleteSchoolReform();
        } else if ("deleteSocialDuty".equals(action)) {
            deleteSocialDuty();
        }else if ("deleteAccident".equals(action)) {
            deleteAccident();
        }else if ("deletePunishment".equals(action)) {
            deletePunishment();
        }else if ("deleteWorkHistory".equals(action)) {
            deleteWorkHistory();
        }else if ("deleteGradeEvaluate".equals(action)) {
            deleteGradeEvaluate();
        }else if ("deleteStudyTrain".equals(action)) {
        	deleteStudyTrain();
        }else if ("updateWorkExperience".equals(action)) {
            updateWorkExperience();
        }else if ("updateEducation".equals(action)) {
            updateEducation();
        }else if ("updateProfessionalTitle".equals(action)) {
            updateProfessionalTitle();
        }else if ("updatePaper".equals(action)) {
            updatePaper();
        }else if ("updateWorkPublish".equals(action)) {
            updateWorkPublish();
        }else if ("updateSubject".equals(action)) {
            updateSubject();
        }else if ("updatePersonalAward".equals(action)) {
            updatePersonalAward();
        }else if ("updateSchoolAward".equals(action)) {
            updateSchoolAward();
        }else if ("updateSchoolReform".equals(action)) {
            updateSchoolReform();
        } else if ("updateSocialDuty".equals(action)) {
            updateSocialDuty();
        }else if ("updateAccident".equals(action)) {
            updateAccident();
        }else if ("updatePunishment".equals(action)) {
            updatePunishment();
        }else if ("updateWorkHistory".equals(action)) {
            updateWorkHistory();
        }else if ("updateGradeEvaluate".equals(action)) {
            updateGradeEvaluate();
        }else if ("saveUpdateRefillData".equals(action)) {
            saveUpdateRefillData();
        }else if ("findCurrentOptionNum".equals(action)){
            findCurrentOptionNum();
        }else if ("saveCurrentOptionNum".equals(action)){
            saveCurrentOptionNum();
        }
        return xmlDoc;
    }







	private void saveUpdateRefillData() throws ParseException, IOException {
        Element dataElement = xmlDocUtil.getRequestData();
        String option_tab_type = dataElement.getChildText("option_tab_type");
        String option_tab_values = dataElement.getChildText("option_tab_values");
        String id = dataElement.getChildText("businessKey");
        
        MasterReviewVO masterReviewVO = new MasterReviewVO();
        masterReviewVO.setId(id);
       
        
        masterReviewVO = buildOptionTabVO(masterReviewVO,option_tab_type,option_tab_values);
        masterReviewVO.setApply_status(MasterReviewConstant.APPLY_REFUSE);
        optionTabDeal(masterReviewVO,option_tab_type);
        
        xmlDocUtil.setResult("0");
    }




    /**
     * 获取主表下个id
     * @Title: getMasterReviewBusinessKey 
     * @Description: TODO
     * @return: void
     */
    private void getMasterReviewBusinessKey() {
        String seq = (String)DBOprProxy.getNextSequence("uuid", "headmaster");
        Element data = new Element("Data");
        Element record = new Element("Record");
        data.addContent(record);
        XmlDocPkgUtil.setChildText(record, "mainBusinessKey", "" + seq);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }



    private void startMasterReviewProcess() throws ParseException, IOException {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildText("id"); //生成的主键id
        String headerMasterId = dataElement.getChildText("headerMasterId");   
        String headerMasterName = dataElement.getChildText("headerMasterName");   
        String mobile = dataElement.getChildText("mobile");   
        String identitycard = dataElement.getChildText("identitycard");
        String email = dataElement.getChildText("email");
        String districtid = dataElement.getChildText("districtid");
        String schoolId = dataElement.getChildText("school_id");   
        String deptid = schoolId; 
        String schoolName = dataElement.getChildText("school_name");   
        
        
        String present_occupation = dataElement.getChildText("present_occupation"); //现任职务
        String applylevel = dataElement.getChildText("apply_level");
        
        
        String school_name_space = dataElement.getChildText("school_name_space"); //学校类型
        String schoolType = dataElement.getChildText("schoolType"); //学校类型
        String schoolCount = dataElement.getChildText("schoolCount"); //校区数量
        String studentNumber = dataElement.getChildText("studentNumber"); //学生人数
        String manage_difficulty_attachment_id = dataElement.getChildText("manage_difficulty_attachment_id"); //学生人数
        
        String school_name_space_ago = dataElement.getChildText("school_name_space_ago"); //近八年学校类型
        String schoolTypeAgo = dataElement.getChildText("schoolTypeAgo"); //近八年学校类型
        String schoolCountAgo = dataElement.getChildText("schoolCountAgo"); //近八年校区数量
        String studentNumberAgo = dataElement.getChildText("studentNumberAgo"); //近八年学生人数
        String manage_difficulty_ago_attachment_id = dataElement.getChildText("manage_difficulty_ago_attachment_id"); //近八年学生人数
        
        String manage_difficulty_approve_result = dataElement.getChildText("manage_difficulty_approve_result"); 
        String manage_difficulty_ago_approve_result = dataElement.getChildText("manage_difficulty_ago_approve_result");
       // String apply_result = dataElement.getChildText("apply_result"); 
      //  String apply_status = dataElement.getChildText("apply_status");
       // String apply_total_point = dataElement.getChildText("apply_total_point"); 
        String work_report = dataElement.getChildText("work_report");
        String isSaveDraft = dataElement.getChildText("isSaveDraft");
        String ispositive = dataElement.getChildText("ispositive");
       // String work_report_approve_result = dataElement.getChildText("work_report_approve_result");
      //  String base_info_approve_result = dataElement.getChildText("base_info_approve_result");
        
        String address = dataElement.getChildText("address");
        String school_class = dataElement.getChildText("school_class");
        String usersex = dataElement.getChildText("usersex");
        String present_major_occupation = dataElement.getChildText("present_major_occupation");
        String phasestudy = dataElement.getChildText("phasestudy");   
        String join_work_time = dataElement.getChildText("join_work_time");
        String join_educate_work_time = dataElement.getChildText("join_educate_work_time");
        String politics_status = dataElement.getChildText("politics_status");
        String teach_age = dataElement.getChildText("teach_age");
        String native_place = dataElement.getChildText("native_place");
        String census_register = dataElement.getChildText("census_register");
        String nation = dataElement.getChildText("nation");
        String person_img_attachId = dataElement.getChildText("person_img_attachId");
        String lodge_school = dataElement.getChildText("lodge_school");
        
        
        String option_tab_type = dataElement.getChildText("option_tab_type");
        String option_tab_values = dataElement.getChildText("option_tab_values");
   
        
        MasterReviewVO masterReviewVO= new MasterReviewVO();
        masterReviewVO.setId(id);
        masterReviewVO.setHeaderMasterId(headerMasterId);
        masterReviewVO.setHeaderMasterName(headerMasterName);
        masterReviewVO.setMobile(mobile);
        masterReviewVO.setIdentitycard(identitycard);
        masterReviewVO.setEmail(email);
        masterReviewVO.setDistrictid(districtid);
        masterReviewVO.setDeptid(deptid);
        masterReviewVO.setSchoolId(schoolId);
        masterReviewVO.setSchoolName(schoolName);
        masterReviewVO.setPresent_occupation(present_occupation);
        masterReviewVO.setApplylevel(applylevel);
        
        masterReviewVO.setSchoolNameSpace(school_name_space);
        masterReviewVO.setSchoolType(schoolType);
        masterReviewVO.setSchoolCount(schoolCount);
        masterReviewVO.setStudentNumber(studentNumber);
        
        masterReviewVO.setSchoolNameSpaceAgo(school_name_space_ago);
        masterReviewVO.setSchoolTypeAgo(schoolTypeAgo);
        masterReviewVO.setSchoolCountAgo(schoolCountAgo);
        masterReviewVO.setStudentNumberAgo(studentNumberAgo);
        masterReviewVO.setWork_report(work_report);
        
        masterReviewVO.setManage_difficulty_ago_approve_result(manage_difficulty_ago_approve_result);
        masterReviewVO.setManage_difficulty_approve_result(manage_difficulty_approve_result);
        masterReviewVO.setManage_difficulty_attachment_id(manage_difficulty_attachment_id);
        masterReviewVO.setManage_difficulty_ago_attachment_id(manage_difficulty_ago_attachment_id);
        masterReviewVO.setIsSaveDraft(isSaveDraft);
        masterReviewVO.setIsPositive(ispositive);
        
        masterReviewVO.setUsersex(usersex);
        masterReviewVO.setSchool_class(school_class);
        masterReviewVO.setPresent_major_occupation(present_major_occupation);
        masterReviewVO.setPhasestudy(phasestudy);
        masterReviewVO.setJoin_work_time(DateUtil.stringToDate(join_work_time));
        masterReviewVO.setJoin_educate_work_time(DateUtil.stringToDate(join_educate_work_time));
        masterReviewVO.setPolitics_status(politics_status);
        masterReviewVO.setTeach_age(teach_age);
        masterReviewVO.setNative_place(native_place);
        masterReviewVO.setCensus_register(census_register);
        masterReviewVO.setNation(nation);
        masterReviewVO.setPerson_img_attachId(person_img_attachId);
        masterReviewVO.setOption_tab_type(option_tab_type);
        masterReviewVO.setApply_status(isSaveDraft);
        masterReviewVO.setLodge_school(lodge_school);
        
        
        buildOptionTabVO(masterReviewVO,option_tab_type,option_tab_values);
       // optionTabDeal(masterReviewVO,option_tab_type,option_tab_values);
        
        ProcessStartVO processStartVO = super.createProcessStartVO();
        try {
            String processInstanceId= masterReviewService.startMasterReviewProcess(masterReviewVO, processStartVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "发起校长职级评审流程成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "processInstanceId", ""+processInstanceId);
            xmlDocUtil.getResponse().addContent(data);
        }  catch (ProcessInstanceStartException e) {
            e.printStackTrace();
            log4j.logError("[发起校长职级评审流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10607", "您没权限发起该流程");
        }catch (Exception e) {
            e.printStackTrace();
            log4j.logError("[发起校长职级评审流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "发起校长职级评审流程失败");
        }
    }
    
    
    private MasterReviewVO buildOptionTabVO(MasterReviewVO masterReviewVO, String option_tab_type,
                               String option_tab_values) throws ParseException, IOException {
        masterReviewVO.setOption_tab_type(option_tab_type);
        if(StringUtil.isEmpty(option_tab_type) || StringUtil.isEmpty(option_tab_values)){
            return masterReviewVO;
        }
        List<Map<String, String>> optionTabList = new ArrayList<Map<String, String>>();
        List<String> optionTabStrs = new ArrayList<String>();
        JSONArray jSONArray = new JSONArray(option_tab_values);
        for (int i = 0; i < jSONArray.length(); i++) {
            Map<String, String> optionTabMap  = new HashMap<String, String>();
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String jsonStr = jSONObject.toString();
            optionTabStrs.add(jsonStr);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (StringUtils.isNotEmpty(jSONObject.getString(key))) {
                    optionTabMap.put(key, jSONObject.getString(key));
                }
            }
            optionTabList.add(optionTabMap);
        }

        masterReviewVO = setAttrValue(masterReviewVO, option_tab_type,
				optionTabList, optionTabStrs);
        return masterReviewVO;
    }







	private MasterReviewVO setAttrValue(MasterReviewVO masterReviewVO,
			String option_tab_type, List<Map<String, String>> optionTabList,
			List<String> optionTabStrs) throws IOException {
		if (option_tab_type.equals("baseinfo")) {// 现任管理难度
            masterReviewVO = JsonUtil.stringToObject(optionTabStrs.get(0),MasterReviewVO.class);
            if (null != masterReviewVO.getJoin_work_time()) {
                masterReviewVO.setJoin_work_time(DateUtil.stringToDate(DateUtil.dateToDateString(masterReviewVO.getJoin_work_time())));
            }
            if (null != masterReviewVO.getJoin_educate_work_time()) {
                masterReviewVO.setJoin_educate_work_time(DateUtil.stringToDate(DateUtil.dateToDateString(masterReviewVO.getJoin_educate_work_time())));
            }
        }else if (option_tab_type.equals("workExperience")) {// 任职年限
            List<WorkExperienceVO> workExperienceVOs = new ArrayList<WorkExperienceVO>();
            for (Map<String, String> eachMap : optionTabList) {
                WorkExperienceVO workExperienceVO = new WorkExperienceVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        workExperienceVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        workExperienceVO.setBusinessKey(value);
                    } else if (key.equals("workSchool")) {
                        workExperienceVO.setWorkSchool(value);
                    } else if (key.equals("workProfession")) {
                        workExperienceVO.setWorkProfession(value);
                    } else if (key.equals("workYear")) {
                        workExperienceVO.setWorkYear(value);
                    } else if (key.equals("proveAttachMentId")) {
                        workExperienceVO.setProveAttachMentId(value);
                    } else if (key.equals("manage_level")) {
                        workExperienceVO.setManage_level(value);
                    }  else if (key.equals("startTime")) {
                        workExperienceVO.setStartTime(DateUtil.stringToDate(value));
                    } else if (key.equals("endTime")) {
                        workExperienceVO.setEndTime(DateUtil.stringToDate(value));
                    }
                }
                workExperienceVOs.add(workExperienceVO);
            }
            masterReviewVO.setWorkExperienceVOs(workExperienceVOs);
        } else if (option_tab_type.equals("education")) {// 学历情况
            List<EducationVO> educationVOs = new ArrayList<EducationVO>();
            for (Map<String, String> eachMap : optionTabList) {
                EducationVO educationVO = new EducationVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        educationVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        educationVO.setBusinessKey(value);
                    } else if (key.equals("studySchool")) {
                        educationVO.setStudySchool(value);
                    } else if (key.equals("studyProfession")) {
                        educationVO.setStudyProfession(value);
                    } else if (key.equals("education")) {
                        educationVO.setEducation(value);
                    } else if (key.equals("degree")) {
                        educationVO.setDegree(value);
                    } else if (key.equals("degreeAttachmentId")) {
                        educationVO.setDegreeAttachmentId(value);
                    } else if (key.equals("educationAttachmentId")) {
                        educationVO.setEducationAttachmentId(value);
                    } else if (key.equals("startTime")) {
                        educationVO.setStartTime(DateUtil.stringToDate(value));
                    } else if (key.equals("endTime")) {
                        educationVO.setEndTime(DateUtil.stringToDate(value));
                    }else if (key.equals("study_form")) {
                        educationVO.setStudy_form(value);
                    }else if (key.equals("education_type")) {
                        educationVO.setEducation_type(value);
                    }
                }
                educationVOs.add(educationVO);
            }
            masterReviewVO.setEducationVOs(educationVOs);
        } else if (option_tab_type.equals("professionalTitle")) {// 职称情况
            List<ProfessionalTitleVO> professionalTitleVOs = new ArrayList<ProfessionalTitleVO>();
            for (Map<String, String> eachMap : optionTabList) {
                ProfessionalTitleVO professionalTitleVO = new ProfessionalTitleVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        professionalTitleVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        professionalTitleVO.setBusinessKey(value);
                    } else if (key.equals("professionaltitle_name")) {
                        professionalTitleVO.setProfessionaltitle_name(value);
                    } else if (key.equals("obtainTime")) {
                        professionalTitleVO.setObtainTime(DateUtil.stringToDate(value));
                    } else if (key.equals("obtainSchool")) {
                        professionalTitleVO.setObtainSchool(value);
                    }else if (key.equals("professionalAttachId")) {
                        professionalTitleVO.setProfessionalAttachId(value);
                    }
                }
                professionalTitleVOs.add(professionalTitleVO);
            }
            masterReviewVO.setProfessionalTitleVOs(professionalTitleVOs);
        } else if (option_tab_type.equals("managementDifficulty")) {// 现任管理难度
            Map<String, String> managementMap = optionTabList.get(0);
            for (Map.Entry<String, String> entry : managementMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("id")) {
                } else if (key.equals("schoolNameSpace")) {
                    masterReviewVO.setSchoolNameSpace(value);
                } else if (key.equals("schoolType")) {
                    masterReviewVO.setSchoolType(value);
                } else if (key.equals("schoolCount")) {
                    masterReviewVO.setSchoolCount(value);
                } else if (key.equals("studentNumber")) {
                    masterReviewVO.setStudentNumber(value);
                } else if (key.equals("manage_difficulty_attachment_id")) {
                    masterReviewVO.setManage_difficulty_attachment_id(value);
                } else if (key.equals("schoolNameSpaceAgo")) {
                    masterReviewVO.setSchoolNameSpaceAgo(value);
                } else if (key.equals("schoolTypeAgo")) {
                    masterReviewVO.setSchoolTypeAgo(value);
                } else if (key.equals("schoolCountAgo")) {
                    masterReviewVO.setSchoolCountAgo(value);
                } else if (key.equals("studentNumberAgo")) {
                    masterReviewVO.setStudentNumberAgo(value);
                } else if (key.equals("manage_difficulty_ago_attachment_id")) {
                    masterReviewVO.setManage_difficulty_ago_attachment_id(value);
                }
            }
        } else if (option_tab_type.equals("paper")) {// 论文情况
            List<PaperVO> paperVOs = new ArrayList<PaperVO>();
            for (Map<String, String> eachMap : optionTabList) {
                PaperVO paperVO = new PaperVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        paperVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        paperVO.setBusinessKey(value);
                    } else if (key.equals("title")) {
                        paperVO.setTitle(value);
                    } else if (key.equals("magazineMeetName")) {
                        paperVO.setMagazineMeetName(value);
                    } else if (key.equals("paperMeetName")) {
                        paperVO.setPaperMeetName(value);
                    } else if (key.equals("paperNumber")) {
                        paperVO.setPaperNumber(value);
                    } else if (key.equals("publishTime")) {
                        paperVO.setPublishTime(DateUtil.stringToDate(value));
                    } else if (key.equals("organizers")) {
                        paperVO.setOrganizers(value);
                    } else if (key.equals("organizersLevel")) {
                        paperVO.setOrganizersLevel(value);
                    } else if (key.equals("personalPart")) {
                        paperVO.setPersonalPart(value);
                    } else if (key.equals("paperAttachmentId")) {
                        paperVO.setPaperAttachmentId(value);
                    }else if (key.equals("publish_company")) {
                        paperVO.setPublish_company(value);
                    }else if (key.equals("complete_way")) {
                        paperVO.setComplete_way(value);
                    }else if (key.equals("author_order")) {
                        paperVO.setAuthor_order(value);
                    }
                }
                paperVOs.add(paperVO);
            }
            masterReviewVO.setPaperVOs(paperVOs);
        } else if (option_tab_type.equals("workPublish")) {// 著作发表情况
            List<BookPublishVO> bookPublishVOs = new ArrayList<BookPublishVO>();
            for(String optionTabStr : optionTabStrs){
                BookPublishVO bookPublishVO = JsonUtil.stringToObject(optionTabStr,BookPublishVO.class);
                bookPublishVOs.add(bookPublishVO);
            }
//            for (Map<String, String> eachMap : optionTabList) {
//                BookPublishVO bookPublishVO = new BookPublishVO();
//                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
//
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    if (key.equals("id")) {
//                    } else if (key.equals("businessKey")) {
//                    } else if (key.equals("book_name")) {
//
//                    } else if (key.equals("complete_way")) {
//                    } else if (key.equals("publish_time")) {
//                    } else if (key.equals("complete_chapter")) {
//                    } else if (key.equals("complete_word")) {
//                    } else if (key.equals("author_order")) {
//                    } else if (key.equals("coverAttachmentId")) {
//                    } else if (key.equals("contentsAttachmentId")) {
//                    }
//
//                }
//                bookPublishVOs.add(bookPublishVO);
//            }
           masterReviewVO.setBookPublishVOs(bookPublishVOs);
        } else if (option_tab_type.equals("subject")) {// 课题发表情况
            List<SubjectVO> subjectVOs = new ArrayList<SubjectVO>();
            for (Map<String, String> eachMap : optionTabList) {
                SubjectVO subjectVO = new SubjectVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        subjectVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        subjectVO.setBusinessKey(value);
                    } else if (key.equals("subject_name")) {
                        subjectVO.setSubjectName(value);
                    } else if (key.equals("subject_company")) {
                        subjectVO.setSubjectCompany(value);
                    } else if (key.equals("subject_level")) {
                        subjectVO.setSubjectLevel(value);
                    } else if (key.equals("subject_responsibility")) {
                        subjectVO.setSubjectRresponsibility(value);
                    } else if (key.equals("finish_result")) {
                        subjectVO.setFinishResult(value);
                    } else if (key.equals("is_finish_subject")) {
                        subjectVO.setIsfinishSubject(value);
                    } else if (key.equals("subjectAttachmentId")) {
                        subjectVO.setSubjectAttachmentId(value);
                    } else if (key.equals("finish_time")) {
                        subjectVO.setFinishTime(DateUtil.stringToDate(value));
                    } else if ("project_time".equals(key)) {
                        subjectVO.setProjectTime(DateUtil.stringToDate(value));
                    }
                }
                subjectVOs.add(subjectVO);
            }
            masterReviewVO.setSubjectVOs(subjectVOs);
        } else if (option_tab_type.equals("personalAward")) {// 个人获奖情况
            List<PersonalAwardVO> personalAwardVOs = new ArrayList<PersonalAwardVO>();
            for (Map<String, String> eachMap : optionTabList) {
                PersonalAwardVO personalAwardVO = new PersonalAwardVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        personalAwardVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        personalAwardVO.setBusinessKey(value);
                    } else if (key.equals("awards_name")) {
                        personalAwardVO.setAwardsName(value);
                    } else if (key.equals("awards_company")) {
                        personalAwardVO.setAwardsCompany(value);
                    } else if (key.equals("awards_level")) {
                        personalAwardVO.setAwardsLevel(value);
                    } else if (key.equals("awards_attachment_id")) {
                        personalAwardVO.setAwardsAttachmentId(value);
                    } else if (key.equals("awards_type")) {
                        personalAwardVO.setAwards_type(value);
                    } else if (key.equals("awards_time")) {
                        personalAwardVO.setAwardsTime(DateUtil.stringToDate(value));
                    } else if (key.equals("awards_attachment_id1")) {
                        personalAwardVO.setAwardsAttachmentId1(value);
                    } else if (key.equals("awards_type1")) {
                        personalAwardVO.setAwards_type1(value);
                    } 
                }
                personalAwardVOs.add(personalAwardVO);
            }
            masterReviewVO.setPersonalAwardVOs(personalAwardVOs);
        } else if (option_tab_type.equals("schoolAward")) {// 学校获得荣誉
            List<SchoolAwardVO> schoolAwardVOs = new ArrayList<SchoolAwardVO>();
            for (Map<String, String> eachMap : optionTabList) {
                SchoolAwardVO schoolAwardVO = new SchoolAwardVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        schoolAwardVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        schoolAwardVO.setBusinessKey(value);
                    } else if (key.equals("awards_name")) {
                        schoolAwardVO.setAwardsName(value);
                    } else if (key.equals("work_school")) {
                        schoolAwardVO.setWorkSchool(value);
                    } else if (key.equals("awards_company")) {
                        schoolAwardVO.setAwardsCompany(value);
                    } else if (key.equals("awards_level")) {
                        schoolAwardVO.setAwardsLevel(value);
                    } else if (key.equals("awards_attachment_id")) {
                        schoolAwardVO.setAwardsAttachmentId(value);
                    } else if (key.equals("awards_time")) {
                        schoolAwardVO.setAwardsTime(DateUtil.stringToDate(value));
                    }
                }
                schoolAwardVOs.add(schoolAwardVO);
            }
            masterReviewVO.setSchoolAwardVOs(schoolAwardVOs);
        } else if (option_tab_type.equals("studyTrain")) {// 进修学习
            List<StudyTrainVO> studyTrainVOs = new ArrayList<StudyTrainVO>();
            for (Map<String, String> eachMap : optionTabList) {
                StudyTrainVO studyTrainVO = new StudyTrainVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        studyTrainVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        studyTrainVO.setBusinessKey(value);
                    } else if (key.equals("start_date")) {
                        studyTrainVO.setStart_date(DateUtil.stringToDate(value));
                    } else if (key.equals("end_date")) {
                        studyTrainVO.setEnd_date(DateUtil.stringToDate(value));
                    } else if (key.equals("title")) {
                        studyTrainVO.setTitle(value);
                    } else if (key.equals("content")) {
                        studyTrainVO.setContent(value);
                    } else if (key.equals("class_hour")) {
                        studyTrainVO.setClass_hour(value);
                    } else if (key.equals("study_place")) {
                        studyTrainVO.setStudy_place(value);
                    } else if (key.equals("organizers")) {
                        studyTrainVO.setOrganizers(value);
                    } else if ("proveAttachMentId".equals(key)) {
                        studyTrainVO.setProveAttachMentId(value);
                    }
                }
                studyTrainVOs.add(studyTrainVO);
            }
            masterReviewVO.setStudyTrainVOs(studyTrainVOs);
        } else if (option_tab_type.equals("schoolReform")) {// 学校特色及改革
            List<SchoolReformVO> schoolReformVOs = new ArrayList<SchoolReformVO>();
            for (Map<String, String> eachMap : optionTabList) {
                SchoolReformVO schoolReformVO = new SchoolReformVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        schoolReformVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        schoolReformVO.setBusinessKey(value);
                    } else if (key.equals("project_name")) {
                        schoolReformVO.setProject_name(value);
                    } else if (key.equals("project_level")) {
                        schoolReformVO.setProject_level(value);
                    } else if (key.equals("implement_time")) {
                        schoolReformVO.setImplement_time(DateUtil.stringToDate(value));
                    } else if (key.equals("charge_department")) {
                        schoolReformVO.setCharge_department(value);
                    } else if (key.equals("performance")) {
                        schoolReformVO.setPerformance(value);
                    } else if (key.equals("proveAttachId")) {
                    	schoolReformVO.setProve_attachment_id(value);
                    } 
                }
                schoolReformVOs.add(schoolReformVO);
            }
            masterReviewVO.setSchoolReformVOs(schoolReformVOs);
        } else if (option_tab_type.equals("socialDuty")) {// 社会责任
            List<SocialDutyVO> socialDutyVOs = new ArrayList<SocialDutyVO>();
            for (Map<String, String> eachMap : optionTabList) {
                SocialDutyVO socialDutyVO = new SocialDutyVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        socialDutyVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        socialDutyVO.setBusinessKey(value);
                    } else if (key.equals("superior_task")) {
                        socialDutyVO.setSuperior_task(value);
                    } else if (key.equals("arrange_department")) {
                        socialDutyVO.setArrange_department(value);
                    } else if (key.equals("implement_time")) {
                        socialDutyVO.setImplement_time(DateUtil.stringToDate(value));
                    } else if (key.equals("complete_state")) {
                        socialDutyVO.setComplete_state(value);
                    } else if (key.equals("proveAttachId")) {
                    	socialDutyVO.setProve_attachment_id(value);
                    } 
                }
                socialDutyVOs.add(socialDutyVO);
            }
            masterReviewVO.setSocialDutyVOs(socialDutyVOs);
        } else if (option_tab_type.equals("accident")) {// 责任事故
        	//获取第一个元素数据，表示的是有无责任事故情况信息
            if (optionTabList!=null||optionTabList.size()>0) {
            	List<SituationVO> situationVOs = new ArrayList<SituationVO>();
            	Map<String, String> eachMap = optionTabList.get(0);
            	optionTabList.remove(0);//移除第一个元素
            	SituationVO situationVO = new SituationVO();
            	for (Map.Entry<String, String> entry : eachMap.entrySet()) {
            		String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("situation_id")) {
                    	situationVO.setId(value);
                    } else if (key.equals("situation_businessKey")) {
                    	situationVO.setBusinessKey(value);
                    } else if (key.equals("situation_hasSituation")) {
                    	situationVO.setHasSituation(value);
                    } 
                    situationVO.setTableName("headmaster_accident");
            	}
            	situationVOs.add(situationVO);
            	masterReviewVO.setSituationVOs(situationVOs);
            }
            
            List<AccidentVO> accidentVOs = new ArrayList<AccidentVO>();
            for (Map<String, String> eachMap : optionTabList) {
                AccidentVO accidentVO = new AccidentVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        accidentVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        accidentVO.setBusinessKey(value);
                    } else if (key.equals("accident_name")) {
                        accidentVO.setAccident_name(value);
                    } else if (key.equals("description")) {
                        accidentVO.setDescription(value);
                    } else if (key.equals("process_result")) {
                        accidentVO.setProcess_result(value);
                    } else if (key.equals("implement_time")) {
                        accidentVO.setImplement_time(DateUtil.stringToDate(value));
                    }else if (key.equals("proveAttachId")) {
                    	accidentVO.setProve_attachment_id(value);
                    } 
                }
                accidentVOs.add(accidentVO);
            }
            masterReviewVO.setAccidentVOs(accidentVOs);
        } else if (option_tab_type.equals("punishment")) {// 处分
        	//获取第一个元素数据，表示的是有无责任事故情况信息
            if (optionTabList!=null||optionTabList.size()>0) {
            	List<SituationVO> situationVOs = new ArrayList<SituationVO>();
            	Map<String, String> eachMap = optionTabList.get(0);
            	optionTabList.remove(0);//移除第一个元素
            	SituationVO situationVO = new SituationVO();
            	for (Map.Entry<String, String> entry : eachMap.entrySet()) {
            		String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("situation_id")) {
                    	situationVO.setId(value);
                    } else if (key.equals("situation_businessKey")) {
                    	situationVO.setBusinessKey(value);
                    } else if (key.equals("situation_hasSituation")) {
                    	situationVO.setHasSituation(value);
                    } 
                    situationVO.setTableName("headmaster_punishment");
            	}
            	situationVOs.add(situationVO);
            	masterReviewVO.setSituationVOs(situationVOs);
            }
            
            List<PunishmentVO> punishmentVOs = new ArrayList<PunishmentVO>();
            for (Map<String, String> eachMap : optionTabList) {
                PunishmentVO punishmentVO = new PunishmentVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        punishmentVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        punishmentVO.setBusinessKey(value);
                    } else if (key.equals("description")) {
                        punishmentVO.setDescription(value);
                    } else if (key.equals("people")) {
                        punishmentVO.setPeople(value);
                    } else if (key.equals("department")) {
                        punishmentVO.setDepartment(value);
                    } else if (key.equals("process_result")) {
                        punishmentVO.setProcess_result(value);
                    } else if (key.equals("implement_time")) {
                        punishmentVO.setImplement_time(DateUtil.stringToDate(value));
                    }else if (key.equals("proveAttachId")) {
                    	punishmentVO.setProve_attachment_id(value);
                    } 
                }
                punishmentVOs.add(punishmentVO);
            }
            masterReviewVO.setPunishmentVOs(punishmentVOs);
        } else if (option_tab_type.equals("workHistory")) {// 工作经历
            List<WorkHistoryVO> workHistoryVOs = new ArrayList<WorkHistoryVO>();
            for (Map<String, String> eachMap : optionTabList) {
                WorkHistoryVO workHistoryVO = new WorkHistoryVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        workHistoryVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        workHistoryVO.setBusinessKey(value);
                    } else if (key.equals("start_date")) {
                        workHistoryVO.setStart_date(DateUtil.stringToDate(value));
                    } else if (key.equals("end_date")) {
                        workHistoryVO.setEnd_date(DateUtil.stringToDate(value));
                    } else if (key.equals("prove_people")) {
                        workHistoryVO.setProve_people(value);
                    } else if (key.equals("work_company")) {
                        workHistoryVO.setWork_company(value);
                    } else if (key.equals("prove_people_duty")) {
                        workHistoryVO.setProve_people_duty(value);
                    }
                }
                workHistoryVOs.add(workHistoryVO);
            }
            masterReviewVO.setWorkHistoryVOs(workHistoryVOs);
        } else if (option_tab_type.equals("gradeEvaluate")) {// 等级评估
            List<GradeEvaluateVO> gradeEvaluateVOs = new ArrayList<GradeEvaluateVO>();
            for (Map<String, String> eachMap : optionTabList) {
                GradeEvaluateVO gradeEvaluateVO = new GradeEvaluateVO();
                for (Map.Entry<String, String> entry : eachMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        gradeEvaluateVO.setId(value);
                    } else if (key.equals("businessKey")) {
                        gradeEvaluateVO.setBusinessKey(value);
                    } else if (key.equals("compulsory_education")) {
                        gradeEvaluateVO.setCompulsory_education(value);
                    } else if (key.equals("high_school")) {
                        gradeEvaluateVO.setHigh_school(value);
                    } else if (key.equals("secondary_school")) {
                        gradeEvaluateVO.setSecondary_school(value);
                    } else if (key.equals("special_education")) {
                        gradeEvaluateVO.setSpecial_education(value);
                    } else if (key.equals("proveAttachId")) {
                    	gradeEvaluateVO.setProve_attachment_id(value);
                    } 
                }
                gradeEvaluateVOs.add(gradeEvaluateVO);
            }
            masterReviewVO.setGradeEvaluateVOs(gradeEvaluateVOs);
        }else if (option_tab_type.equals("run_school")) {// 办学思想
            masterReviewVO.setRun_school(optionTabList.get(0).get("run_school"));
            masterReviewVO.setRun_school_attachment_id(optionTabList.get(0).get("run_school_attachId"));
        }else if (option_tab_type.equals("school_management")) {//学校管理
            masterReviewVO.setSchool_management(optionTabList.get(0).get("school_management"));
            masterReviewVO.setSchool_management_attachment_id(optionTabList.get(0).get("school_management_attachId"));
        }else if (option_tab_type.equals("education_science")) {// 教育教学
            masterReviewVO.setEducation_science(optionTabList.get(0).get("education_science"));
            masterReviewVO.setEducation_science_attachment_id(optionTabList.get(0).get("education_science_attachId"));
        }else if (option_tab_type.equals("external_environment")) {// 外部环境
            masterReviewVO.setExternal_environment(optionTabList.get(0).get("external_environment"));
            masterReviewVO.setExternal_environment_attachment_id(optionTabList.get(0).get("external_environment_attachId"));
        }else if (option_tab_type.equals("student_development")) {// 学生发展
            masterReviewVO.setStudent_development(optionTabList.get(0).get("student_development"));
            masterReviewVO.setStudent_development_attachment_id(optionTabList.get(0).get("student_development_attachId"));
        }else if (option_tab_type.equals("teacher_development")) {// 教师发展
            masterReviewVO.setTeacher_development(optionTabList.get(0).get("teacher_development"));
            masterReviewVO.setTeacher_development_attachment_id(optionTabList.get(0).get("teacher_development_attachId"));
        }
		return masterReviewVO;
	}
    
    private  void optionTabDeal(MasterReviewVO masterReviewVO,String option_tab_type){
        masterReviewService.optionTabDeal(masterReviewVO,option_tab_type);
    }



    /**
     * 一般任务的审批
     * @Title: completeMasterReviewTask 
     * @Description: TODO
     * @return: void
     */
    private void completeMasterReviewTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildText("id");

        MasterReviewVO masterReviewVO= new MasterReviewVO();
        masterReviewVO.setId(id);

        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            masterReviewService.completeMasterReviewTask(masterReviewVO, taskCompleteVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批校长职级评审流程成功");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (TaskDelegateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (PermissionValidateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause().getCause() instanceof NoApproveUsersException){
                xmlDocUtil.writeErrorMsg("106099", "下个节点没有找到审批人，请联系技术支持人员！");
            }else{
                xmlDocUtil.writeErrorMsg("106099", "任务提交失败！");
            }
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        }        
    }
    
    
    /**
     * 驳回重填节点审批
     * @Title: completeRefillTask 
     * @Description: TODO
     * @return: void
     * @throws IOException 
     * @throws ParseException 
     */
    private void completeRefillTask() throws ParseException, IOException {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildText("id"); //生成的主键id
        String headerMasterId = dataElement.getChildText("headerMasterId");   
        String headerMasterName = dataElement.getChildText("headerMasterName");   
        String mobile = dataElement.getChildText("mobile");   
        String identitycard = dataElement.getChildText("identitycard");   
        String schoolId = dataElement.getChildText("school_id");   
        String schoolName = dataElement.getChildText("school_name");   
        
        
        String present_occupation = dataElement.getChildText("present_occupation"); //现任职务
        String applylevel = dataElement.getChildText("apply_level");
        String ispositive = dataElement.getChildText("ispositive");
        
        String school_name_space = dataElement.getChildText("school_name_space"); //学校类型
        String schoolType = dataElement.getChildText("schoolType"); //学校类型
        String schoolCount = dataElement.getChildText("schoolCount"); //校区数量
        String studentNumber = dataElement.getChildText("studentNumber"); //学生人数
        String manage_difficulty_attachment_id = dataElement.getChildText("manage_difficulty_attachment_id"); //学生人数
        
        String school_name_space_ago = dataElement.getChildText("school_name_space_ago"); //近八年学校类型
        String schoolTypeAgo = dataElement.getChildText("schoolTypeAgo"); //近八年学校类型
        String schoolCountAgo = dataElement.getChildText("schoolCountAgo"); //近八年校区数量
        String studentNumberAgo = dataElement.getChildText("studentNumberAgo"); //近八年学生人数
        String manage_difficulty_ago_attachment_id = dataElement.getChildText("manage_difficulty_ago_attachment_id"); //近八年学生人数
        
        String manage_difficulty_approve_result = dataElement.getChildText("manage_difficulty_approve_result"); 
        String manage_difficulty_ago_approve_result = dataElement.getChildText("manage_difficulty_ago_approve_result");
        
        String work_report = dataElement.getChildText("work_report");
        
        String usersex = dataElement.getChildText("usersex");
        String address = dataElement.getChildText("address");
        String school_class = dataElement.getChildText("school_class");
        String present_major_occupation = dataElement.getChildText("present_major_occupation");
        String join_work_time = dataElement.getChildText("join_work_time");
        String join_educate_work_time = dataElement.getChildText("join_educate_work_time");
        String politics_status = dataElement.getChildText("politics_status");
        String teach_age = dataElement.getChildText("teach_age");
        String native_place = dataElement.getChildText("native_place");
        String census_register = dataElement.getChildText("census_register");
        String nation = dataElement.getChildText("nation");
        String person_img_attachId = dataElement.getChildText("person_img_attachId");
        
        
        String option_tab_type = dataElement.getChildText("option_tab_type");
        String option_tab_values = dataElement.getChildText("option_tab_values");

        MasterReviewVO masterReviewVO= new MasterReviewVO();
        masterReviewVO.setId(id);
        masterReviewVO.setHeaderMasterId(headerMasterId);
        masterReviewVO.setHeaderMasterName(headerMasterName);
     //   masterReviewVO.setApply_status(MasterReviewConstant.APPLY_RUNNING);
//        masterReviewVO.setMobile(mobile);
//        masterReviewVO.setIdentitycard(identitycard);
//        masterReviewVO.setSchoolId(schoolId);
//        masterReviewVO.setSchoolName(schoolName);
//        masterReviewVO.setPresentOccupation(presentOccupation);
//        masterReviewVO.setApplylevel(applylevel);
//        masterReviewVO.setSchoolType(schoolType);
//        masterReviewVO.setSchoolCount(schoolCount);
//        masterReviewVO.setStudentNumber(studentNumber);
//        masterReviewVO.setSchoolTypeAgo(schoolTypeAgo);
//        masterReviewVO.setSchoolCountAgo(schoolCountAgo);
//        masterReviewVO.setStudentNumberAgo(studentNumberAgo);
//        masterReviewVO.setWork_report(work_report);
//        masterReviewVO.setManage_difficulty_ago_approve_result(manage_difficulty_ago_approve_result);
//        masterReviewVO.setManage_difficulty_approve_result(manage_difficulty_approve_result);
//        masterReviewVO.setManage_difficulty_attachment_id(manage_difficulty_attachment_id);
//        masterReviewVO.setManage_difficulty_ago_attachment_id(manage_difficulty_ago_attachment_id);
//        masterReviewVO.setIsPositive(ispositive);
//        
//        masterReviewVO.setUsersex(usersex);
//        masterReviewVO.setSchool_class(school_class);
//        masterReviewVO.setPresent_major_occupation(present_major_occupation);
//        masterReviewVO.setJoin_work_time(DateUtil.stringToDate(join_work_time));
//        masterReviewVO.setJoin_educate_work_time(DateUtil.stringToDate(join_educate_work_time));
//        masterReviewVO.setPolitics_status(politics_status);
//        masterReviewVO.setTeach_age(teach_age);
//        masterReviewVO.setNative_place(native_place);
//        masterReviewVO.setCensus_register(census_register);
//        masterReviewVO.setNation(nation);
//        masterReviewVO.setPerson_img_attachId(person_img_attachId);
//        masterReviewVO.setOption_tab_type(option_tab_type);
        
        buildOptionTabVO(masterReviewVO,option_tab_type,option_tab_values);

        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            masterReviewService.completeRefillTask(masterReviewVO, taskCompleteVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批校长职级评审流程成功");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (TaskDelegateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (PermissionValidateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause().getCause() instanceof NoApproveUsersException){
                xmlDocUtil.writeErrorMsg("106099", "下个节点没有找到审批人，请联系技术支持人员！");
            }else{
                xmlDocUtil.writeErrorMsg("106099", "任务提交失败！");
            }
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        }            
    }

    
    /**
     * 区级干部审核
     * @Title: completeAreaCadresTask 
     * @Description: TODO
     * @return: void
     * @throws IOException 
     * @throws ParseException 
     */
    private void completeAreaCadresTask() throws ParseException, IOException {
        Element dataElement = xmlDocUtil.getRequestData();
       // String refuseType = dataElement.getChildText("refuseType");
       // String refuseTypeId = dataElement.getChildText("refuseTypeId");
        String id = dataElement.getChildText("id");
        String isRefillFlag = dataElement.getChildText("isRefillFlag");
        
        MasterReviewVO masterReviewVO = new MasterReviewVO();
        masterReviewVO.setId(id);
        
        if(!StringUtils.isEmpty(isRefillFlag)){
        	 
        	  String workExperienceStr = dataElement.getChildText("workExperience");
              String education = dataElement.getChildText("education");
              String professionalTitle = dataElement.getChildText("professionalTitle");
            //  String managementDifficulty = dataElement.getChildText("managementDifficulty");
              String paper = dataElement.getChildText("paper");
              String workPublish = dataElement.getChildText("workPublish");
              String subject = dataElement.getChildText("subject");
              String personalAward = dataElement.getChildText("personalAward");
              String schoolAward = dataElement.getChildText("schoolAward");
              String studyTrain = dataElement.getChildText("studyTrain");
              String schoolReform = dataElement.getChildText("schoolReform");
              String socialDuty = dataElement.getChildText("socialDuty");
              String accident = dataElement.getChildText("accident");
              String punishment = dataElement.getChildText("punishment");
              String workHistory = dataElement.getChildText("workHistory");
              String gradeEvaluate = dataElement.getChildText("gradeEvaluate");
              
              
              String base_info_approve_result = dataElement.getChildText("base_info_approve_result");
              String manage_difficulty_approve_result = dataElement.getChildText("manage_difficulty_approve_result");
              String manage_difficulty_ago_approve_result = dataElement.getChildText("manage_difficulty_ago_approve_result");
              String run_school_approve_result = dataElement.getChildText("run_school_approve_result");
              String school_management_approve_result = dataElement.getChildText("school_management_approve_result");
              String education_science_approve_result = dataElement.getChildText("education_science_approve_result");
              String external_environment_approve_result = dataElement.getChildText("external_environment_approve_result");
              String student_development_approve_result = dataElement.getChildText("student_development_approve_result");
              String teacher_development_approve_result = dataElement.getChildText("teacher_development_approve_result");
              
              
              
              masterReviewVO.setBase_info_approve_result(base_info_approve_result);
              masterReviewVO.setRun_school_approve_result(run_school_approve_result);
              masterReviewVO.setSchool_management_approve_result(school_management_approve_result);
              masterReviewVO.setEducation_science_approve_result(education_science_approve_result);
              masterReviewVO.setExternal_environment_approve_result(external_environment_approve_result);
              masterReviewVO.setStudent_development_approve_result(student_development_approve_result);
              masterReviewVO.setTeacher_development_approve_result(teacher_development_approve_result);
              masterReviewVO.setManage_difficulty_approve_result(manage_difficulty_approve_result);
              masterReviewVO.setManage_difficulty_ago_approve_result(manage_difficulty_ago_approve_result);
              
              buildOptionTabVO(masterReviewVO,"workExperience",workExperienceStr);
              buildOptionTabVO(masterReviewVO,"education",education);
              buildOptionTabVO(masterReviewVO,"professionalTitle",professionalTitle);
            //  buildOptionTabVO(masterReviewVO,"managementDifficulty",managementDifficulty);
              buildOptionTabVO(masterReviewVO,"paper",paper);
              buildOptionTabVO(masterReviewVO,"workPublish",workPublish);
              buildOptionTabVO(masterReviewVO,"subject",subject);
              buildOptionTabVO(masterReviewVO,"personalAward",personalAward);
              buildOptionTabVO(masterReviewVO,"schoolAward",schoolAward);
              buildOptionTabVO(masterReviewVO,"studyTrain",studyTrain);
              buildOptionTabVO(masterReviewVO,"schoolReform",schoolReform);
              buildOptionTabVO(masterReviewVO,"socialDuty",socialDuty);
              buildOptionTabVO(masterReviewVO,"accident",accident);
              buildOptionTabVO(masterReviewVO,"punishment",punishment);
              buildOptionTabVO(masterReviewVO,"workHistory",workHistory);
              buildOptionTabVO(masterReviewVO,"gradeEvaluate",gradeEvaluate);
        }
      


        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            masterReviewService.completeAreaCadresTask(  masterReviewVO,"","", taskCompleteVO,isRefillFlag);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批校长职级评审流程成功");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (TaskDelegateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (PermissionValidateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause().getCause() instanceof NoApproveUsersException){
                xmlDocUtil.writeErrorMsg("106099", "下个节点没有找到审批人，请联系技术支持人员！");
            }else{
                xmlDocUtil.writeErrorMsg("106099", "任务提交失败！");
            }
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        }                 
    }
    
//    private MasterReviewVO buildAreaVO(MasterReviewVO masterReviewVO,String option_tab_values) throws ParseException, IOException {
//    	// Map<String,List<Map<String, String>>>  resultMap= new HashMap<String,List<Map<String, String>>>();
//    	 
//    	 List<Map<String, String>> optionTabList = new ArrayList<Map<String, String>>();
//    	 
//        /// List<String> optionTabStrs = new ArrayList<String>();
//         JSONArray jSONArray = new JSONArray(option_tab_values);
//         for (int i = 0; i < jSONArray.length(); i++) {
//             Map<String, String> optionTabMap  = new HashMap<String, String>();
//             JSONObject jSONObject = jSONArray.getJSONObject(i);
//           //  String jsonStr = jSONObject.toString();
//         //    optionTabStrs.add(jsonStr);
//             Iterator keys = jSONObject.keys();
//             while (keys.hasNext()) {
//                 String key = (String) keys.next();
//                 if (StringUtils.isNotEmpty(jSONObject.getString(key))) {
//                     optionTabMap.put(key, jSONObject.getString(key));
//                 }
//             }
//             optionTabList.add(optionTabMap);
//         }
//         
//         
//         buildOptionTabVO();
//         
//         
//         
//         return masterReviewVO;
//
//    }

    
    /**
     * 人事干部评分审核
     * @Title: completePersonnelLeaderTask 
     * @Description: TODO
     * @return: void
     */
     private void completePersonnelLeaderTask() {
         Element dataElement = xmlDocUtil.getRequestData();
        
         String id = dataElement.getChildText("id");
         String headerMasterId = dataElement.getChildText("headerMasterId");
         String baseinfo_grade = dataElement.getChildText("baseinfo_grade");
         String work_experience_grade = dataElement.getChildText("work_experience_grade");
         String education_grade = dataElement.getChildText("education_grade");
         String professional_title_grade = dataElement.getChildText("professional_title_grade");
         String management_difficulty_grade = dataElement.getChildText("management_difficulty_grade");
         String management_difficulty_grade_ago = dataElement.getChildText("management_difficulty_grade_ago");
         String paper_grade = dataElement.getChildText("paper_grade");
         String work_publish_grade = dataElement.getChildText("work_publish_grade");
         String subject_grade = dataElement.getChildText("subject_grade");
         String personal_award_grade = dataElement.getChildText("personal_award_grade");
         String school_award_grade = dataElement.getChildText("school_award_grade");
         String sumGrade = dataElement.getChildText("sumGrade");
         
         String studyTrain_grade = dataElement.getChildText("studyTrain_grade");
         String schoolReform_grade = dataElement.getChildText("schoolReform_grade");
         String socialDuty_grade = dataElement.getChildText("socialDuty_grade");
         String accident_grade = dataElement.getChildText("accident_grade");
         String punishment_grade = dataElement.getChildText("punishment_grade");
         
       

         PersonnelLeaderGradeVO personnelLeaderGradeVO= new PersonnelLeaderGradeVO();
         personnelLeaderGradeVO.setBusinessKey(id);
         personnelLeaderGradeVO.setHeaderMasterId(headerMasterId);
         personnelLeaderGradeVO.setBaseinfoGrade(baseinfo_grade);
         personnelLeaderGradeVO.setEducationGrade(education_grade);
         personnelLeaderGradeVO.setManagementDifficultyGrade(management_difficulty_grade);
         personnelLeaderGradeVO.setManagementDifficultyGradeAgo(management_difficulty_grade_ago);
         personnelLeaderGradeVO.setPaperGrade(paper_grade);
         personnelLeaderGradeVO.setPersonalAwardGrade(personal_award_grade);
         personnelLeaderGradeVO.setProfessionalTitleGrade(professional_title_grade);
         personnelLeaderGradeVO.setSchoolAwardGrade(school_award_grade);
         personnelLeaderGradeVO.setSubjectGrade(subject_grade);
         personnelLeaderGradeVO.setWorkExperienceGrade(work_experience_grade);
         personnelLeaderGradeVO.setWorkPublishGrade(work_publish_grade);
         
         personnelLeaderGradeVO.setStudyTrain_grade(studyTrain_grade);
         personnelLeaderGradeVO.setSchoolReform_grade(schoolReform_grade);
         personnelLeaderGradeVO.setSocialDuty_grade(socialDuty_grade);
         personnelLeaderGradeVO.setAccident_grade(accident_grade);
         personnelLeaderGradeVO.setPunishment_grade(punishment_grade);
         try {
             TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
             masterReviewService.completePersonnelLeaderTask(personnelLeaderGradeVO, taskCompleteVO);
             xmlDocUtil.setResult("0");
             xmlDocUtil.writeHintMsg("10601", "审批校长职级评审流程成功");
         } catch (TaskNoExistException e) {
             e.printStackTrace();
             xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
             xmlDocUtil.setResult("-1");
             log4j.logError(e);
         } catch (TaskDelegateException e) {
             e.printStackTrace();
             xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
             xmlDocUtil.setResult("-1");
             log4j.logError(e);
         } catch (PermissionValidateException e) {
             e.printStackTrace();
             xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
             xmlDocUtil.setResult("-1");
             log4j.logError(e);
         } catch (Exception e) {
             e.printStackTrace();
             if(e.getCause().getCause() instanceof NoApproveUsersException){
                 xmlDocUtil.writeErrorMsg("106099", "下个节点没有找到审批人，请联系技术支持人员！");
             }else{
                 xmlDocUtil.writeErrorMsg("106099", "任务提交失败！");
             }
             xmlDocUtil.setResult("-1");
             log4j.logError(e);
         }         
     }

    

    /**
     * 专家评分审核
     * @Title: completeProfessorApproveTask 
     * @Description: TODO
     * @return: void
     */
    private void completeProfessorApproveTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildText("id");
        String headerMasterId = dataElement.getChildText("headerMasterId");
        String report_grade = dataElement.getChildText("report_grade");
        String sumGrade = dataElement.getChildText("sumGrade");
        

        ProfessorGradeVO professorGradeVO= new ProfessorGradeVO();
        professorGradeVO.setBusinessKey(id);
        professorGradeVO.setApply_headmaster(headerMasterId);
        professorGradeVO.setReport_grade(report_grade);
        professorGradeVO.setSumGrade(sumGrade);

        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            masterReviewService.completeProfessorApproveTask(professorGradeVO, taskCompleteVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批校长职级评审流程成功");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (TaskDelegateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (PermissionValidateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause().getCause() instanceof NoApproveUsersException){
                xmlDocUtil.writeErrorMsg("106099", "下个节点没有找到审批人，请联系技术支持人员！");
            }else{
                xmlDocUtil.writeErrorMsg("106099", "任务提交失败！");
            }
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        }         
    }
    
    
    public MasterReviewVO findMasterReviewVO(String businessKey,String processInstanceId){
        MasterReviewVO masterReviewVO = new MasterReviewVO();

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList<String> bvals = new ArrayList<String>();
            sql.append(" SELECT t.id,");
            sql.append(" t.processInstanceId,");
            sql.append("t.headerMasterId,");
            sql.append("t.headerMasterName,");
           // sql.append("t.ispositive,");
            //sql.append(" t.mobile,");
           // sql.append(" t.identitycard,");
            sql.append(" c.districtid,");
            sql.append(" k.deptname as districtname,");
            sql.append(" c.deptid as school_id,");
            sql.append(" l.deptname as school_name,");
            //sql.append(" t.present_occupation,");
            sql.append(" t.apply_level,");
            sql.append(" t.school_name_space,");
            sql.append("t.student_number,");
            sql.append("t.school_count,");
            sql.append(" t.school_type,");
            sql.append("t.manage_difficulty_attachment_id,");
            sql.append("t.manage_difficulty_approve_result,");
            sql.append(" t.school_name_space_ago,");
            sql.append("t.student_number_ago,");
            sql.append("t.school_count_ago,");
            sql.append("t.school_type_ago,");
            sql.append("t.manage_difficulty_ago_attachment_id,");
            sql.append(" t.manage_difficulty_ago_approve_result,");
            sql.append(" t.apply_result,");
            sql.append(" t.apply_status,");
            sql.append("t.apply_total_point,");
            sql.append("t.run_school,");
            sql.append("t.current_option_num,");
            sql.append(" t.school_management,");
            sql.append("t.education_science,");
            sql.append("t.external_environment,");
            sql.append(" t.student_development,");
            sql.append("t.teacher_development,");
            sql.append(" t.work_report,");
            sql.append(" t.work_report_approve_result,");
           // sql.append("c.id,");
            sql.append("c.userid,");
            sql.append("c.username,");
            sql.append(" c.usercode,");
            sql.append("c.usersex,");
            sql.append("c.userpwd,");
            sql.append("c.deptid,");
            sql.append("c.idnumber,");
            sql.append("  c.ispositive,");
            sql.append(" c.email,");
            sql.append(" c.mobile,");
            sql.append(" c.address,");
            sql.append(" c.phone_valid,");
            sql.append("c.phasestudy,");
            sql.append("c.school_class,");
            sql.append(" c.join_work_time,");
            sql.append(" c.join_educate_work_time,");
            sql.append("c.politics_status,");
            sql.append(" c.teach_age,");
            sql.append("c.native_place,");
            sql.append("c.census_register,");
            sql.append(" c.nation,");
            sql.append("c.present_occupation,");
            sql.append("c.present_major_occupation,");
            sql.append(" c.person_img_attachId,");
            sql.append(" c.lodge_school,");
            sql.append(" t.base_info_approve_result,");
            sql.append("a.attachment_id AS manage_difficulty_attachId,");
            sql.append(" a.file_name AS manage_difficulty_filename,");
            sql.append(" b.attachment_id AS manage_difficulty_ago_attachId,");
            sql.append(" b.file_name AS manage_difficulty_ago_filename,");
            //sql.append(" d.attachment_id AS person_img_attachId,");
            sql.append(" d.file_name AS person_img_filename,");
            sql.append("d.file_path AS person_img_filepath,");
            
            sql.append("e.attachment_id AS run_school_attachId,");
            sql.append(" e.file_name AS run_school_filename ,");
            
            sql.append("f.attachment_id AS school_management_attachId,");
            sql.append(" f.file_name AS school_management_filename,");
            
            sql.append("g.attachment_id AS education_science_attachId,");
            sql.append(" g.file_name AS education_science_filename,");
            
            sql.append("h.attachment_id AS external_environment_attachId,");
            sql.append(" h.file_name AS external_environment_filename,");
            
            sql.append("i.attachment_id AS student_development_attachId,");
            sql.append(" i.file_name AS student_development_filename,");
            
            sql.append("j.attachment_id AS teacher_development_attachId,");
            sql.append(" j.file_name AS teacher_development_filename");
            
            sql.append("  FROM headmaster t");
            sql.append(" LEFT JOIN headmaster_base_info c ON t.headerMasterId = c.userid");
            
            sql.append(" LEFT JOIN pcmc_dept k ON c.districtid = k.deptid");
            sql.append(" LEFT JOIN pcmc_dept l ON c.deptid = l.deptid");
            
            sql.append(" LEFT JOIN workflow_attachment a");
            sql.append("  ON t.manage_difficulty_attachment_id = a.attachment_id");
            sql.append(" LEFT JOIN workflow_attachment b");
            sql.append(" ON t.manage_difficulty_ago_attachment_id = b.attachment_id");
            sql.append(" LEFT JOIN workflow_attachment d");
            sql.append(" ON c.person_img_attachId = d.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment e");
            sql.append(" ON t.run_school_attachId = e.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment f");
            sql.append(" ON t.school_management_attachId = f.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment g");
            sql.append(" ON t.education_science_attachId = g.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment h");
            sql.append(" ON t.external_environment_attachId = h.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment i");
            sql.append(" ON t.student_development_attachId = i.attachment_id");
            
            sql.append(" LEFT JOIN workflow_attachment j");
            sql.append(" ON t.teacher_development_attachId = j.attachment_id");
            sql.append(" WHERE t.id = ?");
      
            if (StringUtil.isNotEmpty(businessKey)) {
                bvals.add(businessKey);
            }
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);


            List rlist = resultElement.getChildren("Record");
            for (int i = 0; i < rlist.size(); i++) {
                Element rec = (Element) rlist.get(i);
                String bussinessKey = rec.getChildText("id");
                String headerMasterId = rec.getChildText("headermasterid");
                String headerMasterName = rec.getChildText("headermastername");
                String mobile = rec.getChildText("mobile");
                String identitycard = rec.getChildText("idnumber");
                String districtId = rec.getChildText("districtid");
                String districtName = rec.getChildText("districtname");
                String schoolId = rec.getChildText("school_id");
                String schoolName = rec.getChildText("school_name");
                String present_occupation = rec.getChildText("present_occupation");
                String applyLevel = rec.getChildText("apply_level");
                
                String present_major_occupation = rec.getChildText("present_major_occupation");
                String usersex = rec.getChildText("usersex");
                String phasestudy = rec.getChildText("phasestudy");
                String email = rec.getChildText("email");
                String school_class = rec.getChildText("school_class");
                String join_work_time = rec.getChildText("join_work_time");
                String join_educate_work_time = rec.getChildText("join_educate_work_time");
                String politics_status = rec.getChildText("politics_status");
                String teach_age = rec.getChildText("teach_age");
                String native_place = rec.getChildText("native_place");
                String census_register = rec.getChildText("census_register");
                String nation = rec.getChildText("nation");
                String address = rec.getChildText("address");
                String ispositive = rec.getChildText("ispositive");
                
                String run_school = rec.getChildText("run_school");
                String school_management = rec.getChildText("school_management");
                String education_science = rec.getChildText("education_science");
                String external_environment = rec.getChildText("external_environment");
                String student_development = rec.getChildText("student_development");
                String teacher_development = rec.getChildText("teacher_development");
                
                String school_name_space = rec.getChildText("school_name_space");
                String studentNumber = rec.getChildText("student_number");
                String schoolCount = rec.getChildText("school_count");
                String schoolType = rec.getChildText("school_type");
                String manage_difficulty_approve_result = rec.getChildText("manage_difficulty_approve_result");
                String manage_difficulty_attachId = rec.getChildText("manage_difficulty_attachid");
                String manage_difficulty_filename = rec.getChildText("manage_difficulty_filename");
                
                AttachMentVO  attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(manage_difficulty_attachId)){
                    attachMentVO.setAttachmentId(manage_difficulty_attachId);
                    attachMentVO.setFileName(manage_difficulty_filename);
                }
                
                String school_name_space_ago = rec.getChildText("school_name_space_ago");
                String studentNumberAgo = rec.getChildText("student_number_ago");
                String schoolCountAgo = rec.getChildText("school_count_ago");
                String schoolTypeAgo = rec.getChildText("school_type_ago");
                String manage_difficulty_ago_approve_result = rec.getChildText("manage_difficulty_ago_approve_result");
                String manage_difficulty_ago_attachId = rec.getChildText("manage_difficulty_ago_attachid");
                String manage_difficulty_ago_filename = rec.getChildText("manage_difficulty_ago_filename");
                
                AttachMentVO  attachMentVO1= new AttachMentVO();
                if(!StringUtil.isEmpty(manage_difficulty_ago_attachId)){
                    attachMentVO1.setAttachmentId(manage_difficulty_ago_attachId);
                    attachMentVO1.setFileName(manage_difficulty_ago_filename);
                }
                
                String apply_result = rec.getChildText("apply_result"); 
                String apply_status = rec.getChildText("apply_status");
                String apply_total_point = rec.getChildText("apply_total_point"); 
                String work_report = rec.getChildText("work_report");
                String work_report_approve_result = rec.getChildText("work_report_approve_result");
                String base_info_approve_result = rec.getChildText("base_info_approve_result");
                String current_option_num = rec.getChildText("current_option_num");

                String person_img_attachId = rec.getChildText("person_img_attachid");
                String person_img_filename = rec.getChildText("person_img_filename");
                String person_img_filepath = rec.getChildText("person_img_filepath");
                AttachMentVO  personImgAttachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(person_img_attachId)){
                    personImgAttachMentVO.setAttachmentId(person_img_attachId);
                    personImgAttachMentVO.setFileName(person_img_filename);
                    personImgAttachMentVO.setFilePath(person_img_filepath);
                }
                
                String run_school_attachId = rec.getChildText("run_school_attachid");
                String run_school_filename = rec.getChildText("run_school_filename");
                
                AttachMentVO  run_school_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(run_school_attachId)){
                	run_school_attachMentVO.setAttachmentId(run_school_attachId);
                	run_school_attachMentVO.setFileName(run_school_filename);
                }
                
                
                String school_management_attachId = rec.getChildText("school_management_attachid");
                String school_management_filename = rec.getChildText("school_management_filename");
                
                AttachMentVO  school_management_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(school_management_attachId)){
                	school_management_attachMentVO.setAttachmentId(school_management_attachId);
                	school_management_attachMentVO.setFileName(school_management_filename);
                }
                
                String education_science_attachId = rec.getChildText("education_science_attachid");
                String education_science_filename = rec.getChildText("education_science_filename");
                
                AttachMentVO  education_science_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(education_science_attachId)){
                	education_science_attachMentVO.setAttachmentId(education_science_attachId);
                	education_science_attachMentVO.setFileName(education_science_filename);
                }
                
                String external_environment_attachId = rec.getChildText("external_environment_attachid");
                String external_environment_filename = rec.getChildText("external_environment_filename");
                
                AttachMentVO  external_environment_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(external_environment_attachId)){
                	external_environment_attachMentVO.setAttachmentId(external_environment_attachId);
                	external_environment_attachMentVO.setFileName(external_environment_filename);
                }
                
                String student_development_attachId = rec.getChildText("student_development_attachid");
                String student_development_filename = rec.getChildText("student_development_filename");
                
                AttachMentVO  student_development_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(student_development_attachId)){
                	student_development_attachMentVO.setAttachmentId(student_development_attachId);
                	student_development_attachMentVO.setFileName(student_development_filename);
                }
                
                
                String teacher_development_attachId = rec.getChildText("teacher_development_attachid");
                String teacher_development_filename = rec.getChildText("teacher_development_filename");
                
                AttachMentVO  teacher_development_attachMentVO= new AttachMentVO();
                if(!StringUtil.isEmpty(teacher_development_attachId)){
                	teacher_development_attachMentVO.setAttachmentId(teacher_development_attachId);
                	teacher_development_attachMentVO.setFileName(teacher_development_filename);
                }
                
                String lodge_school = rec.getChildText("lodge_school");
                
                masterReviewVO.setPerson_img_attachMentVO(personImgAttachMentVO);
           
                masterReviewVO.setId(bussinessKey);
                masterReviewVO.setHeaderMasterId(headerMasterId);
                masterReviewVO.setHeaderMasterName(headerMasterName);
                masterReviewVO.setMobile(mobile);
                masterReviewVO.setIdentitycard(identitycard);
                masterReviewVO.setDistrictid(districtId);
                masterReviewVO.setDistrictName(districtName);
                masterReviewVO.setSchoolId(schoolId);
                masterReviewVO.setSchoolName(schoolName);
                masterReviewVO.setPresent_occupation(present_occupation);
                masterReviewVO.setApplylevel(applyLevel);
                
                masterReviewVO.setPresent_major_occupation(present_major_occupation);
                masterReviewVO.setUsersex(usersex);
                masterReviewVO.setPhasestudy(phasestudy);
                masterReviewVO.setEmail(email);
                masterReviewVO.setSchool_class(school_class);
                masterReviewVO.setJoin_work_time(DateUtil.stringToDate(join_work_time));
                masterReviewVO.setJoin_educate_work_time(DateUtil.stringToDate(join_educate_work_time));
                masterReviewVO.setPolitics_status(politics_status);
                masterReviewVO.setTeach_age(teach_age);
                masterReviewVO.setNative_place(native_place);
                masterReviewVO.setCensus_register(census_register);
                masterReviewVO.setNation(nation);
                masterReviewVO.setPerson_img_attachId(person_img_attachId);
                masterReviewVO.setAddress(address);
                masterReviewVO.setIsPositive(ispositive);
                
                masterReviewVO.setRun_school(run_school);
                masterReviewVO.setSchool_management(school_management);
                masterReviewVO.setEducation_science(education_science);
                masterReviewVO.setExternal_environment(external_environment);
                masterReviewVO.setStudent_development(student_development);
                masterReviewVO.setTeacher_development(teacher_development);
                
                masterReviewVO.setSchoolNameSpace(school_name_space);
                masterReviewVO.setStudentNumber(studentNumber);
                masterReviewVO.setSchoolCount(schoolCount);
                masterReviewVO.setSchoolType(schoolType);
                masterReviewVO.setManageDifficultyAttachMentVO(attachMentVO);
                masterReviewVO.setManage_difficulty_approve_result(manage_difficulty_approve_result);
                
                masterReviewVO.setSchoolNameSpaceAgo(school_name_space_ago);
                masterReviewVO.setStudentNumberAgo(studentNumberAgo);
                masterReviewVO.setSchoolCountAgo(schoolCountAgo);
                masterReviewVO.setSchoolTypeAgo(schoolTypeAgo);
                masterReviewVO.setManageDifficultyAgoAttachMentVO(attachMentVO1);
                masterReviewVO.setManage_difficulty_ago_approve_result(manage_difficulty_ago_approve_result);
                
                masterReviewVO.setApply_result(apply_result);
                masterReviewVO.setApply_status(apply_status);
                masterReviewVO.setApply_total_point(apply_total_point);
                masterReviewVO.setWork_report(work_report);
                masterReviewVO.setWork_report_approve_result(work_report_approve_result);
                masterReviewVO.setBase_info_approve_result(base_info_approve_result);
                masterReviewVO.setCurrent_option_num(current_option_num);
                
                masterReviewVO.setRun_school_attachMentVO(run_school_attachMentVO);
                masterReviewVO.setSchool_management_attachMentVO(school_management_attachMentVO);
                masterReviewVO.setEducation_science_attachMentVO(education_science_attachMentVO);
                masterReviewVO.setExternal_environment_attachMentVO(external_environment_attachMentVO);
                masterReviewVO.setStudent_development_attachMentVO(student_development_attachMentVO);
                masterReviewVO.setTeacher_development_attachMentVO(teacher_development_attachMentVO);
               
                
                masterReviewVO.setWorkExperienceVOs(getWorkExperienceVOs(bussinessKey));
                masterReviewVO.setEducationVOs(getEducationVOs(bussinessKey));
                masterReviewVO.setProfessionalTitleVOs(getProfessionalTitleVOs(bussinessKey));
                
                masterReviewVO.setPaperVOs(getPaperVOs(bussinessKey));
                masterReviewVO.setBookPublishVOs(getBookPublishVOs(bussinessKey));
                masterReviewVO.setSubjectVOs(getSubjectVOs(bussinessKey));
                masterReviewVO.setPersonalAwardVOs(getPersonalAwardVOs(bussinessKey));
                masterReviewVO.setSchoolAwardVOs(getSchoolAwardVOs(bussinessKey));
                
                masterReviewVO.setStudyTrainVOs(getStudyTrainVOs(bussinessKey));
                masterReviewVO.setSchoolReformVOs(getSchoolReformVOs(bussinessKey));
                masterReviewVO.setSocialDutyVOs(getSocialDutyVOs(bussinessKey));
                masterReviewVO.setSituationVOs(getSituationVOs(bussinessKey));
                masterReviewVO.setAccidentVOs(getAccidentVOs(bussinessKey));
                masterReviewVO.setPunishmentVOs(getPunishmentVOs(bussinessKey));
                masterReviewVO.setWorkHistoryVOs(getWorkHistoryVOs(bussinessKey));
                masterReviewVO.setGradeEvaluateVOs(getGradeEvaluateVOs(bussinessKey));
                masterReviewVO.setLodge_school(lodge_school);
               
                List<HistoryActinstVO> historyActinstVOs = new ArrayList<HistoryActinstVO>();
                
                Map<String,String> hisMap= new HashMap<String,String>();
                List<HistoricActivityInstance> historicActivityInstances = ApplicationContextHelper
                    .getHistoryService().createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).orderByHistoricActivityInstanceEndTime().desc().list();
                for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
                    String activityId = historicActivityInstance.getActivityId();
                    Date endTime = historicActivityInstance.getEndTime();
                    if(StringUtil.isEmpty(hisMap.get(activityId)) ){
                        HistoryActinstVO historyActinstVO= new HistoryActinstVO();
                        historyActinstVO.setAct_id(activityId);
                        historyActinstVO.setEnd_time(endTime);
                        historyActinstVOs.add(historyActinstVO);
                        //XmlDocPkgUtil.setChildText(recordHandler, "activityId", "" + activityId);
                       // XmlDocPkgUtil.setChildText(recordHandler, "endTime", "" + DateUtil.date24ToString(endTime));
                    }
                    hisMap.put(activityId, activityId);
                }
                masterReviewVO.setHistoryActinstVOs(historyActinstVOs);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }   
        return masterReviewVO;
    }
    
    
    private void viewMasterReview() throws IOException {
        Element dataElement = xmlDocUtil.getRequestData();
        String businessKey = dataElement.getChildTextTrim("query_businessKey");
        String processInstanceId = dataElement.getChildTextTrim("processInstanceId");
        String processDefKey = dataElement.getChildTextTrim("processDefKey");

        Element data = new Element("Data");
        Element record = new Element("Record");
        MasterReviewVO masterReviewVO = this.findMasterReviewVO(businessKey, processInstanceId);
        XmlDocPkgUtil.setChildText(record, "masterReviewVO", JsonUtil.objectToJson(masterReviewVO));
        data.addContent(record);

        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }
    
    /**
     * 得到学校等级评估集合
     * @Title: getWorkHistoryVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<PunishmentVO>
     */
    private List<GradeEvaluateVO> getGradeEvaluateVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,a.*");
        sql.append(" FROM headmaster_grade_evaluate t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<GradeEvaluateVO> gradeEvaluateVOs = new ArrayList<GradeEvaluateVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String compulsory_education = (String)map.get("compulsory_education");
            String high_school = (String)map.get("high_school");
            String secondary_school = (String)map.get("secondary_school");
            String special_education = (String)map.get("special_education");
            String approve_result = (String)map.get("approve_result");
            

            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            
            GradeEvaluateVO gradeEvaluateVO = new GradeEvaluateVO();
            gradeEvaluateVO.setId(id);
            gradeEvaluateVO.setBusinessKey(businessKey);
            gradeEvaluateVO.setCompulsory_education(compulsory_education);
            gradeEvaluateVO.setHigh_school(high_school);
            gradeEvaluateVO.setSecondary_school(secondary_school);
            gradeEvaluateVO.setSpecial_education(special_education);
            gradeEvaluateVO.setApprove_result(approve_result);
            gradeEvaluateVO.setProveAttachMentVO(proveAttachMentVO);
          
            gradeEvaluateVOs.add(gradeEvaluateVO);
        }
        return gradeEvaluateVOs;      
    }
    
    
    /**
     * 得到工作经历集合
     * @Title: getWorkHistoryVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<PunishmentVO>
     */
    private List<WorkHistoryVO> getWorkHistoryVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*");
        sql.append(" FROM headmaster_work_history t");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<WorkHistoryVO> workHistoryVOs = new ArrayList<WorkHistoryVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            Date start_date = (Date)map.get("start_date");
            Date end_date = (Date)map.get("end_date");
            String prove_people = (String)map.get("prove_people");
            String prove_people_duty = (String)map.get("prove_people_duty");
            String work_company = (String)map.get("work_company");
            String approve_result =(String) map.get("approve_result");
            

            WorkHistoryVO workHistoryVO = new WorkHistoryVO();
            workHistoryVO.setId(id);
            workHistoryVO.setBusinessKey(businessKey);
            workHistoryVO.setStart_date(start_date);
            workHistoryVO.setProve_people(prove_people);
            workHistoryVO.setProve_people_duty(prove_people_duty);
            workHistoryVO.setWork_company(work_company);
            workHistoryVO.setEnd_date(end_date);
            workHistoryVO.setApprove_result(approve_result);
          
            workHistoryVOs.add(workHistoryVO);
        }
        return workHistoryVOs;      
    }
    
    /**
     * 减分处分
     * @Title: getPunishmentVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<PunishmentVO>
     */
    private List<PunishmentVO> getPunishmentVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,a.*");
        sql.append(" FROM headmaster_punishment t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<PunishmentVO> punishmentVOs = new ArrayList<PunishmentVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            Date implement_time = (Date)map.get("implement_time");
            String people = (String)map.get("people");
            String department = (String)map.get("department");
            String description = (String)map.get("description");
            String process_result =(String) map.get("process_result");
            String approve_result = (String)map.get("approve_result");
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            
            PunishmentVO punishmentVO = new PunishmentVO();
            punishmentVO.setId(id);
            punishmentVO.setBusinessKey(businessKey);
            punishmentVO.setImplement_time(implement_time);
            punishmentVO.setDepartment(department);
            punishmentVO.setDescription(description);
            punishmentVO.setProcess_result(process_result);
            punishmentVO.setPeople(people);
            punishmentVO.setApprove_result(approve_result);
            punishmentVO.setProveAttachMentVO(proveAttachMentVO);
          
            punishmentVOs.add(punishmentVO);
        }
        return punishmentVOs;      
    }
    
    public List<SituationVO> getSituationVOs (String bussinessKey) {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT * ");
    	sql.append(" FROM headmaster_situation ");
    	sql.append(" WHERE businessKey = ?");
    	List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);
    	
    	List<SituationVO> situationVOs = new ArrayList<SituationVO>();
    	SituationVO situationVO = null;
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey =(String) map.get("businessKey");
            String hasSituation = (String)map.get("hasSituation");
            String tableName = (String)map.get("tableName");
            
            situationVO = new SituationVO();
            situationVO.setId(id);
            situationVO.setBusinessKey(businessKey);
            situationVO.setHasSituation(hasSituation);
            situationVO.setTableName(tableName);
            situationVOs.add(situationVO);
        }
        return situationVOs;      
    }
    /**
     * 减分责任事故
     * @Title: getAccidentVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<AccidentVO>
     */
    private List<AccidentVO> getAccidentVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,a.*");
        sql.append(" FROM headmaster_accident t");
        sql.append(" LEFT JOIN workflow_attachment a");
       sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<AccidentVO> accidentVOs = new ArrayList<AccidentVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey =(String) map.get("businessKey");
            Date implement_time = (Date)map.get("implement_time");
            String process_result = (String)map.get("process_result");
            String accident_name = (String)map.get("accident_name");
            String description =(String) map.get("description");
            String approve_result =(String) map.get("approve_result");
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            
            

            AccidentVO accidentVO = new AccidentVO();
            accidentVO.setId(id);
            accidentVO.setBusinessKey(businessKey);
            accidentVO.setImplement_time(implement_time);
            accidentVO.setAccident_name(accident_name);
            accidentVO.setDescription(description);
            accidentVO.setProcess_result(process_result);
            accidentVO.setApprove_result(approve_result);
            accidentVO.setProveAttachMentVO(proveAttachMentVO);
            accidentVOs.add(accidentVO);
        }
        return accidentVOs;      
    }
    
    /**
     * 加分社会责任
     * @Title: getSchoolReformVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<SchoolReformVO>
     */
    private List<SocialDutyVO> getSocialDutyVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,a.*");
        sql.append(" FROM headmaster_social_duty t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<SocialDutyVO> socialDutyVOs = new ArrayList<SocialDutyVO>();
        for (Map<String, Object> map : list) {
            String id =(String) map.get("id");
            String businessKey = (String)map.get("businessKey");
            Date implement_time = (Date)map.get("implement_time");
            String superior_task = (String)map.get("superior_task");
            String arrange_department = (String)map.get("arrange_department");
            String complete_state =(String) map.get("complete_state");
            String approve_result = (String)map.get("approve_result");
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            

            SocialDutyVO socialDutyVO = new SocialDutyVO();
            socialDutyVO.setId(id);
            socialDutyVO.setBusinessKey(businessKey);
            socialDutyVO.setImplement_time(implement_time);
            socialDutyVO.setSuperior_task(superior_task);
            socialDutyVO.setArrange_department(arrange_department);
            socialDutyVO.setComplete_state(complete_state);
            socialDutyVO.setApprove_result(approve_result);
            socialDutyVO.setProveAttachMentVO(proveAttachMentVO);
          
            socialDutyVOs.add(socialDutyVO);
        }
        return socialDutyVOs;      
    }
    
    /**
     * 加分学校特色和改革
     * @Title: getSchoolReformVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<SchoolReformVO>
     */
    private List<SchoolReformVO> getSchoolReformVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*, a.*, getParamDesc('headmaster_approve_level',t.project_level) as project_level_desc");
        sql.append(" FROM headmaster_school_reform t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<SchoolReformVO> schoolReformVOs = new ArrayList<SchoolReformVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            Date implement_time = (Date)map.get("implement_time");
            String project_name =(String) map.get("project_name");
            String project_level =(String) map.get("project_level");
            String project_level_desc =(String) map.get("project_level_desc");
            String charge_department =(String) map.get("charge_department");
            String performance =(String) map.get("performance");
            String approve_result =(String) map.get("approve_result");
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            

            SchoolReformVO schoolReformVO = new SchoolReformVO();
            schoolReformVO.setId(id);
            schoolReformVO.setBusinessKey(businessKey);
            schoolReformVO.setImplement_time(implement_time);
            schoolReformVO.setProject_name(project_name);
            schoolReformVO.setProject_level(project_level);
            schoolReformVO.setProject_level_desc(project_level_desc);
            schoolReformVO.setCharge_department(charge_department);
            schoolReformVO.setPerformance(performance);
            schoolReformVO.setApprove_result(approve_result);
            schoolReformVO.setProveAttachMentVO(proveAttachMentVO);
          
            schoolReformVOs.add(schoolReformVO);
        }
        return schoolReformVOs;      
    }
    
    /**
     * 专业发展进修学习
     * @Title: getStudyTrainVOs 
     * @Description: TODO
     * @param bussinessKey
     * @return
     * @return: List<StudyTrainVO>
     */
    private List<StudyTrainVO> getStudyTrainVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,a.*");
        sql.append(" FROM headmaster_studytrain t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<StudyTrainVO> studyTrainVOs = new ArrayList<StudyTrainVO>();
        for (Map<String, Object> map : list) {
            String id =(String) map.get("id");
            String businessKey = (String)map.get("businessKey");
            Date start_date = (Date)map.get("start_date");
            Date end_date = (Date)map.get("end_date");
            String title = (String)map.get("title");
            String content = (String)map.get("content");
            String class_hour =(String) map.get("class_hour");
            String study_place = (String)map.get("study_place");
            String organizers =(String) map.get("organizers");
            String approve_result = (String)map.get("approve_result");
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                proveAttachMentVO.setAttachmentId(attachment_id);
                proveAttachMentVO.setFileName(file_name);
            }
            
            StudyTrainVO studyTrainVO = new StudyTrainVO();
            studyTrainVO.setId(id);
            studyTrainVO.setBusinessKey(businessKey);
            studyTrainVO.setStart_date(start_date);
            studyTrainVO.setEnd_date(end_date);
            studyTrainVO.setTitle(title);
            studyTrainVO.setContent(content);
            studyTrainVO.setClass_hour(class_hour);
            studyTrainVO.setStudy_place(study_place);
            studyTrainVO.setOrganizers(organizers);
            studyTrainVO.setApprove_result(approve_result);
            
            studyTrainVO.setProveAttachMentVO(proveAttachMentVO);
            
            studyTrainVOs.add(studyTrainVO);
        }
        return studyTrainVOs;      
    }

    private List<SchoolAwardVO> getSchoolAwardVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*, a.*,getParamDesc('headmaster_approve_level',t.awards_level) as awards_level_desc");
        sql.append(" FROM headmaster_school_award t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.awards_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<SchoolAwardVO> schoolAwardVOs = new ArrayList<SchoolAwardVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String awards_name = (String)map.get("awards_name");
            String work_school = (String)map.get("work_school");
            String awards_company =(String) map.get("awards_company");
            String awards_level =(String) map.get("awards_level");
            String awards_level_desc =(String) map.get("awards_level_desc");
            Date awards_time = (Date)map.get("awards_time");
            String approve_result = (String)map.get("approve_result");
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }
            

            SchoolAwardVO schoolAwardVO = new SchoolAwardVO();
            schoolAwardVO.setId(id);
            schoolAwardVO.setBusinessKey(businessKey);
            schoolAwardVO.setAwardsName(awards_name);
            schoolAwardVO.setWorkSchool(work_school);
            schoolAwardVO.setAwardsCompany(awards_company);
            schoolAwardVO.setAwardsLevel(awards_level);
            schoolAwardVO.setAwardsLevelDesc(awards_level_desc);
            schoolAwardVO.setAwardsTime(awards_time);
            schoolAwardVO.setSchoolAttachVO(attachMentVO);
            schoolAwardVO.setApprove_result(approve_result);
            schoolAwardVOs.add(schoolAwardVO);
        }
        return schoolAwardVOs;      
    }

    private List<PersonalAwardVO> getPersonalAwardVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*, a.*,b.attachment_id as attachment_id1,b.file_name as file_name1,");
        sql.append(" getParamDesc('headmaster_approve_level',t.awards_level) as awards_level_personal_desc,");
        sql.append(" getParamDesc('headmaster_awards_type',t.awards_type) as awards_type_desc,");
        sql.append(" getParamDesc('headmaster_awards_type',t.awards_type1) as awards_type1_desc");
        sql.append(" FROM headmaster_personal_award t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.awards_attachment_id = a.attachment_id");
        sql.append(" LEFT JOIN workflow_attachment b");
        sql.append(" ON t.awards_attachment_id1 = b.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<PersonalAwardVO> personalAwardVOs = new ArrayList<PersonalAwardVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String awards_name = (String)map.get("awards_name");
            String awards_company =(String) map.get("awards_company");
            String awards_level =(String) map.get("awards_level");
            String awards_level_desc =(String) map.get("awards_level_personal_desc");
            Date awards_time = (Date)map.get("awards_time");
            String approve_result = (String)map.get("approve_result");
            String awards_type = (String)map.get("awards_type");
            String awards_type_desc = (String)map.get("awards_type_desc");
            String awards_type1 = (String)map.get("awards_type1");
            String awards_type1_desc = (String)map.get("awards_type1_desc");
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }
            
            
            AttachMentVO  attachMentVO1= new AttachMentVO();
            if(null !=map.get("attachment_id1")){
                String attachment_id1 = (String)map.get("attachment_id1");
                String file_name1 = (String)map.get("file_name1");
                attachMentVO1.setAttachmentId(attachment_id1);
                attachMentVO1.setFileName(file_name1);
            }

            PersonalAwardVO personalAwardVO = new PersonalAwardVO();
            personalAwardVO.setId(id);
            personalAwardVO.setBusinessKey(businessKey);
            personalAwardVO.setAwardsName(awards_name);
            personalAwardVO.setAwardsCompany(awards_company);
            personalAwardVO.setAwardsLevel(awards_level);
            personalAwardVO.setAwardsLevelDesc(awards_level_desc);
            personalAwardVO.setAwardsTime(awards_time);
            personalAwardVO.setPersonalAttachVO(attachMentVO);
            personalAwardVO.setApprove_result(approve_result);
            personalAwardVO.setAwards_type(awards_type);
            personalAwardVO.setAwards_type_desc(awards_type_desc);
            personalAwardVO.setAwards_type1(awards_type1);
            personalAwardVO.setAwards_type1_desc(awards_type1_desc);
            personalAwardVO.setPersonalAttachVO1(attachMentVO1);
            personalAwardVOs.add(personalAwardVO);
        }
        return personalAwardVOs;   
    }

    private List<SubjectVO> getSubjectVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*, a.*,getParamDesc('headmaster_approve_level',t.subject_level) as subject_level_desc,");
        sql.append(" getParamDesc('headmaster_is_finish_subject',t.is_finish_subject) as is_finish_subject_desc ");
        sql.append(" FROM headmaster_subject t ");
        sql.append("LEFT JOIN workflow_attachment a");
        sql.append("  ON t.subject_attachment_id = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<SubjectVO> subjectVOs = new ArrayList<SubjectVO>();
        for (Map<String, Object> map : list) {
            String id =(String) map.get("id");
            String businessKey = (String)map.get("businessKey");
            String subjectName = (String)map.get("subject_name");
            String subject_company = (String)map.get("subject_company");
            String subject_level = (String)map.get("subject_level");
            String subject_level_desc = (String)map.get("subject_level_desc");
            String subject_responsibility =(String) map.get("subject_responsibility");
            String is_finish_subject =(String) map.get("is_finish_subject");
            String is_finish_subject_desc =(String) map.get("is_finish_subject_desc");
            String finish_result = (String)map.get("finish_result");
            Date finish_time = (Date)map.get("finish_time");
            Date project_time = (Date)map.get("project_time");
            String approve_result = (String)map.get("approve_result");
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id =(String) map.get("attachment_id");
                String file_name =(String) map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }

            SubjectVO subjectVO = new SubjectVO();
            subjectVO.setId(id);
            subjectVO.setBusinessKey(businessKey);
            subjectVO.setSubjectName(subjectName);
            subjectVO.setSubjectCompany(subject_company);
            subjectVO.setSubjectLevel(subject_level);
            subjectVO.setSubjectLevelDesc(subject_level_desc);
            subjectVO.setSubjectRresponsibility(subject_responsibility);
            subjectVO.setIsfinishSubject(is_finish_subject);
            subjectVO.setIsfinishSubjectDesc(is_finish_subject_desc);
            subjectVO.setFinishResult(finish_result);
            subjectVO.setFinishTime(finish_time);
            subjectVO.setProjectTime(project_time);
            subjectVO.setSubjectAttachVO(attachMentVO);
            subjectVO.setApprove_result(approve_result);
            subjectVOs.add(subjectVO);
        }
        return subjectVOs;   
    }

    private List<BookPublishVO> getBookPublishVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT t.*,getParamDesc('headmaster_complete_form',t.complete_way) as complete_way_desc,");
        sql.append(" getParamDesc('headmaster_author_sort',t.author_order) as author_order_desc,");
        sql.append(" a.attachment_id AS coverid,");
        sql.append(" a.file_name AS covername,");
        sql.append(" b.attachment_id AS contentid,");
        sql.append(" c.file_name AS provename,");
        sql.append(" c.attachment_id AS proveid,");
        sql.append("b.file_name AS contentname");
        sql.append(" FROM headmaster_work_publish t");
        sql.append(" LEFT JOIN workflow_attachment a ");
        sql.append(" ON t.cover_attachment_id = a.attachment_id ");
        sql.append(" LEFT JOIN workflow_attachment b ");
        sql.append("  ON t.contents_attachment_id = b.attachment_id ");
        sql.append(" LEFT JOIN workflow_attachment c");
        sql.append(" ON t.prove_attachment_id = c.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<BookPublishVO> bookPublishVOs = new ArrayList<BookPublishVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey =(String) map.get("businessKey");
            String book_name =(String) map.get("book_name");
            String complete_way = (String)map.get("complete_way");
            String complete_way_desc = (String)map.get("complete_way_desc");
            Date publish_time = (Date)map.get("publish_time");
            String complete_chapter =(String) map.get("complete_chapter");
            String complete_word = (String)map.get("complete_word");
            String author_order = (String)map.get("author_order");
            String author_order_desc = (String)map.get("author_order_desc");
            String approve_result =(String) map.get("approve_result");
            String publish_company =(String) map.get("publish_company");

            BookPublishVO bookPublishVO = new BookPublishVO();
            
            AttachMentVO  educationAttachMentVO= new AttachMentVO();
            if(null !=map.get("coverid")){
                String coverid = (String)map.get("coverid");
                String covername = (String)map.get("covername");
              
                educationAttachMentVO.setAttachmentId(coverid);
                educationAttachMentVO.setFileName(covername);
              
            }
         
            AttachMentVO  degreeAttachMentVO= new AttachMentVO();
            if(null !=map.get("degreeid")){
                String contentid =(String) map.get("contentid");
                String contentname =(String) map.get("contentname");
            
                degreeAttachMentVO.setAttachmentId(contentid);
                degreeAttachMentVO.setFileName(contentname);
               
            }
            
            AttachMentVO  proveAttachMentVO= new AttachMentVO();
            if(null !=map.get("proveid")){
                String proveid =(String) map.get("proveid");
                String provename =(String) map.get("provename");
            
                proveAttachMentVO.setAttachmentId(proveid);
                proveAttachMentVO.setFileName(provename);
               
            }
           
            
            bookPublishVO.setId(id);
            bookPublishVO.setBusinessKey(businessKey);
            bookPublishVO.setBook_name(book_name);
            bookPublishVO.setComplete_way(complete_way);
            bookPublishVO.setComplete_way_desc(complete_way_desc);
            bookPublishVO.setPublish_time(publish_time);
            bookPublishVO.setComplete_chapter(complete_chapter);
            bookPublishVO.setComplete_word(complete_word);
            bookPublishVO.setAuthor_order(author_order);
            bookPublishVO.setAuthor_order_desc(author_order_desc);
            bookPublishVO.setApprove_result(approve_result);
            bookPublishVO.setPublish_company(publish_company);
            bookPublishVOs.add(bookPublishVO);
            
            bookPublishVO.setCoverVO(educationAttachMentVO);
            bookPublishVO.setContentVO(degreeAttachMentVO);
            bookPublishVO.setProveAttachMentVO(proveAttachMentVO);
        }
        return bookPublishVOs;   
    }

    private List<PaperVO> getPaperVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.*, a.*,getParamDesc('headmaster_approve_level',t.organizers_level) as organizers_level_desc,");
        sql.append(" getParamDesc('headmaster_complete_form',t.complete_way) as complete_way_desc,getParamDesc('headmaster_author_sort',t.author_order) as author_order_desc");
        sql.append(" FROM headmaster_paper t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.paper_attachment_id = a.attachment_id");
        sql.append(" WHERE t.businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);
      //  list= new ArrayList<Map<String, Object>>();

        List<PaperVO> paperVOs = new ArrayList<PaperVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String title = (String)map.get("title");
            //String publish_number = map.get("publish_number").toString();
            Date publish_time = (Date)map.get("publish_time");
            String magazine_meet_name = (String)map.get("magazine_meet_name");
            String paper_meet_name = (String)map.get("paper_meet_name");
            String paper_number =(String) map.get("paper_number");
            String organizers =(String) map.get("organizers");
            String organizers_level =(String) map.get("organizers_level");
            String organizers_level_desc =(String) map.get("organizers_level_desc");
            String personal_part = (String)map.get("personal_part");
            String approve_result = (String)map.get("approve_result");
            String publish_company = (String)map.get("publish_company");
            String complete_way = (String)map.get("complete_way");
            String complete_way_desc = (String)map.get("complete_way_desc");
            String author_order = (String)map.get("author_order");
            String author_order_desc = (String)map.get("author_order_desc");
           
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }

            PaperVO paperVO = new PaperVO();
            paperVO.setId(id);
            paperVO.setBusinessKey(businessKey);
            paperVO.setTitle(title);
            paperVO.setPublishTime(publish_time);
            paperVO.setMagazineMeetName(magazine_meet_name);
            paperVO.setPaperMeetName(paper_meet_name);
            paperVO.setPaperNumber(paper_number);
            paperVO.setOrganizers(organizers);
            paperVO.setOrganizersLevel(organizers_level);
            paperVO.setOrganizersLevelDesc(organizers_level_desc);
            paperVO.setPersonalPart(personal_part);
            paperVO.setPaperAttachMentVO(attachMentVO);
            paperVO.setApprove_result(approve_result);
            paperVO.setPublish_company(publish_company);
            paperVO.setComplete_way(complete_way);
            paperVO.setComplete_way_desc(complete_way_desc);
            paperVO.setAuthor_order(author_order);
            paperVO.setAuthor_order_desc(author_order_desc);
            paperVOs.add(paperVO);
        }
        return paperVOs;   
    }

    private List<ProfessionalTitleVO> getProfessionalTitleVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.*, a.*");
        sql.append(" FROM headmaster_professional_title t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.professionalAttachId = a.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<ProfessionalTitleVO> professionalTitleVOs = new ArrayList<ProfessionalTitleVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String professionaltitle_name =(String) map.get("professionaltitle_name");
            String obtain_school = (String)map.get("obtain_school");
            Date obtain_time = (Date)map.get("obtain_time");
            String approve_result =(String) map.get("approve_result");
            
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id =(String) map.get("attachment_id");
                String file_name =(String) map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }

            ProfessionalTitleVO professionalTitleVO = new ProfessionalTitleVO();
            professionalTitleVO.setId(id);
            professionalTitleVO.setProfessionaltitle_name(professionaltitle_name);
            professionalTitleVO.setBusinessKey(businessKey);
            professionalTitleVO.setAttachMentVO(attachMentVO);
            professionalTitleVO.setObtainSchool(obtain_school);
            professionalTitleVO.setObtainTime(obtain_time);
            professionalTitleVO.setApprove_result(approve_result);
            professionalTitleVOs.add(professionalTitleVO);
        }
        return professionalTitleVOs;   
    }

    private List<EducationVO> getEducationVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.*,getParamDesc('headmaster_education',t.education) as education_desc,");
        sql.append("getParamDesc('headmaster_degree',t.degree) as degree_desc,");
        sql.append(" a.attachment_id AS educationid,");
        sql.append("a.file_name AS educationname,");
        sql.append("b.attachment_id AS degreeid,");
        sql.append("b.file_name AS degreename");
        sql.append(" FROM headmaster_education t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.education_attachment_id = a.attachment_id");
        sql.append(" LEFT JOIN workflow_attachment b");
        sql.append("  ON t.degree_attachment_id = b.attachment_id");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<EducationVO> educationVOs = new ArrayList<EducationVO>();
        for (Map<String, Object> map : list) {
            String id = (String)map.get("id");
            String businessKey = (String)map.get("businessKey");
            String study_school = (String)map.get("study_school");
            String study_profession = (String)map.get("study_profession");
            String approve_result = (String)map.get("approve_result");
            Date start_date = (Date)map.get("start_date");
            Date end_date = (Date)map.get("end_date");
            
            String degree = (String)map.get("degree");
            String degree_desc = (String)map.get("degree_desc");
            String education = (String)map.get("education");
            String education_desc = (String)map.get("education_desc");
            String education_type = (String)map.get("education_type");
            String study_form = (String)map.get("study_form");
            
            EducationVO educationVO = new EducationVO();
            
            if(null !=map.get("educationid")){
                String educationid = (String)map.get("educationid");
                String educationname = (String)map.get("educationname");
                AttachMentVO  educationAttachMentVO= new AttachMentVO();
                educationAttachMentVO.setAttachmentId(educationid);
                educationAttachMentVO.setFileName(educationname);
                educationVO.setEducationAttachMentVO(educationAttachMentVO);
            }
         
            if(null !=map.get("degreeid")){
                String degreeid =(String) map.get("degreeid");
                String degreename = (String)map.get("degreename");
                
                AttachMentVO  degreeAttachMentVO= new AttachMentVO();
                degreeAttachMentVO.setAttachmentId(degreeid);
                degreeAttachMentVO.setFileName(degreename);
                educationVO.setDegreeAttachMentVO(degreeAttachMentVO);
            }
           
           
            educationVO.setId(id);
            educationVO.setBusinessKey(businessKey);
            educationVO.setStudySchool(study_school);
            educationVO.setStudyProfession(study_profession);
            educationVO.setStartTime(start_date);
            educationVO.setEndTime(end_date);
            educationVO.setApprove_result(approve_result);
            educationVO.setDegree(degree);
            educationVO.setDegreeDesc(degree_desc);
            educationVO.setEducation(education);
            educationVO.setEducationDesc(education_desc);
            educationVO.setEducation_type(education_type);
            educationVO.setStudy_form(study_form);
            educationVOs.add(educationVO);
        }
        return educationVOs;   
    }

    private List<WorkExperienceVO> getWorkExperienceVOs(String bussinessKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.*, a.*");
        sql.append(" FROM headmaster_work_experience t");
        sql.append(" LEFT JOIN workflow_attachment a");
        sql.append(" ON t.prove_attachment_id = a.attachment_id");
        sql.append(" WHERE t.businessKey = ?");
        sql.append(" and t.isvalid = '1'");
        
       
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<WorkExperienceVO> workExperienceVOs = new ArrayList<WorkExperienceVO>();
        for (Map<String, Object> map : list) {
            String id =(String) map.get("id");
            String businessKey = (String)map.get("businessKey");
            String work_school =(String) map.get("work_school");
            String work_profession = (String)map.get("work_profession");
            String manage_level = (String)map.get("manage_level");
            String work_year = (String)map.get("work_year");
            Date start_date = (Date)map.get("start_date");
            Date end_date = (Date)map.get("end_date");
            String approve_result = (String) map.get("approve_result");
            
            
            AttachMentVO  attachMentVO= new AttachMentVO();
            if(null !=map.get("attachment_id")){
                String attachment_id = (String)map.get("attachment_id");
                String file_name = (String)map.get("file_name");
                attachMentVO.setAttachmentId(attachment_id);
                attachMentVO.setFileName(file_name);
            }
        

            WorkExperienceVO workExperienceVO = new WorkExperienceVO();
            workExperienceVO.setId(id);
            workExperienceVO.setBusinessKey(businessKey);
            workExperienceVO.setStartTime(start_date);
            workExperienceVO.setEndTime(end_date);
            workExperienceVO.setWorkSchool(work_school);
            workExperienceVO.setWorkProfession(work_profession);
            workExperienceVO.setManage_level(manage_level);
            workExperienceVO.setWorkYear(work_year);
            workExperienceVO.setProveAttachMentVO(attachMentVO);
            workExperienceVO.setApprove_result(approve_result);
            workExperienceVOs.add(workExperienceVO);
        }
        return workExperienceVOs;   
    }
    
    /**
     * 驳回重填更新数据
     * @Title: saveMasterReviewData 
     * @Description: TODO
     * @return: void
     */
    private void saveMasterReviewData() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String present_occupation = dataElement.getChildText("present_occupation");
        String apply_level = dataElement.getChildText("apply_level");
        String schoolType = dataElement.getChildText("schoolType");
        String schoolCount = dataElement.getChildText("schoolCount");
        String studentNumber = dataElement.getChildText("studentNumber");
        String schoolTypeAgo = dataElement.getChildText("schoolTypeAgo");
        String schoolCountAgo = dataElement.getChildText("schoolCountAgo");
        String studentNumberAgo = dataElement.getChildText("studentNumberAgo");
        String work_report = dataElement.getChildText("work_report");
        String id = dataElement.getChildText("id");

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
        sql.append("  apply_level = ?,");
        sql.append(" student_number = ?,");
        sql.append(" school_count = ?,");
        sql.append("school_type = ?,");
        sql.append(" student_number_ago = ?,");
        sql.append("  school_count_ago = ?,");
        sql.append("  school_type_ago = ?,");
       // sql.append(" apply_result = 0,");
        //sql.append(" apply_total_point = ?,");
        sql.append(" modify_by = ?,");
        sql.append("modify_date = ?,");
        sql.append("work_report = ?");
        
        sql.append("manage_difficulty_approve_result = '1'");
        sql.append("manage_difficulty_ago_approve_result = '1'");
        sql.append("work_report_approve_result = '1'");
        sql.append("base_info_approve_result = '1'");
        sql.append(" WHERE id = ?");

        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),
            new String[] { present_occupation, apply_level, studentNumber, schoolCount, schoolType, studentNumberAgo,
            schoolCountAgo, schoolTypeAgo,updateUser, DatetimeUtil.getNow(""), work_report,id });
        xmlDocUtil.setResult("0");        
    }
    
    /**
     * 任职年限
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addWorkExperience() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String startTime = dataElement.getChildTextTrim("startTime");
        String endTime = dataElement.getChildTextTrim("endTime");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String workSchool = dataElement.getChildTextTrim("workSchool");
        String workProfession = dataElement.getChildTextTrim("workProfession");
        String workYear = dataElement.getChildTextTrim("workYear");
        String proveAttachMentId = dataElement.getChildTextTrim("proveAttachMentId");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
   
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_work_experience");
           // XmlDocPkgUtil.copyValues(dataElement,userDeptRec,0);
            XmlDocPkgUtil.setChildText(workExperienceRec, "start_date", startTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "end_date", endTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "work_school",workSchool);
            XmlDocPkgUtil.setChildText(workExperienceRec, "work_profession", workProfession);
            XmlDocPkgUtil.setChildText(workExperienceRec, "work_year", workYear);
            XmlDocPkgUtil.setChildText(workExperienceRec, "prove_attachment_id", proveAttachMentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增任职年限成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增任职年限发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增任职年限失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 学历情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addEducation() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String startTime = dataElement.getChildTextTrim("startTime");
        String endTime = dataElement.getChildTextTrim("endTime");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String studySchool = dataElement.getChildTextTrim("studySchool");
        String studyProfession = dataElement.getChildTextTrim("studyProfession");
        String education = dataElement.getChildTextTrim("education");
        String degree = dataElement.getChildTextTrim("degree");
        String educationAttachmentId = dataElement.getChildTextTrim("educationAttachmentId");
        String degreeAttachmentId = dataElement.getChildTextTrim("degreeAttachmentId");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
   
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_education");
            XmlDocPkgUtil.setChildText(workExperienceRec, "start_date", startTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "end_date", endTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey",businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "study_school",studySchool);
            XmlDocPkgUtil.setChildText(workExperienceRec, "study_profession", studyProfession);
            XmlDocPkgUtil.setChildText(workExperienceRec, "education", education);
            XmlDocPkgUtil.setChildText(workExperienceRec, "degree", degree);
            XmlDocPkgUtil.setChildText(workExperienceRec, "education_attachment_id", educationAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "degree_attachment_id", degreeAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增学历情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增学历情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增学历情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 职称情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addProfessionalTitle() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String professionaltitle_name = dataElement.getChildTextTrim("professionaltitle_name");
        String obtainTime = dataElement.getChildTextTrim("obtainTime");
        String obtainSchool = dataElement.getChildTextTrim("obtainSchool");
        String professionalAttachId = dataElement.getChildTextTrim("professionalAttachId");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_professional_title");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "professionaltitle_name", professionaltitle_name);
            XmlDocPkgUtil.setChildText(workExperienceRec, "obtain_time", obtainTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "obtain_school", obtainSchool);
            XmlDocPkgUtil.setChildText(workExperienceRec, "professionalAttachId", professionalAttachId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增职称情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增职称情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增职称情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 论文情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addPaper() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String title = dataElement.getChildTextTrim("title");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String publishTime = dataElement.getChildTextTrim("publish_time");
        String magazineMeetName = dataElement.getChildTextTrim("magazine_meet_name");
        String paperMeetName = dataElement.getChildTextTrim("paper_meet_name");
        String paperNumber = dataElement.getChildTextTrim("paper_number");
        String organizers = dataElement.getChildTextTrim("organizers");
        String organizersLevel = dataElement.getChildTextTrim("organizers_level");
        String personalPart = dataElement.getChildTextTrim("personal_part");
        String paperAttachmentId = dataElement.getChildTextTrim("paper_attachment_id");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_paper");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "title", title);
            XmlDocPkgUtil.setChildText(workExperienceRec, "publish_time", publishTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "magazine_meet_name", magazineMeetName);
            XmlDocPkgUtil.setChildText(workExperienceRec, "paper_meet_name", paperMeetName);
            XmlDocPkgUtil.setChildText(workExperienceRec, "paper_number", paperNumber);
            XmlDocPkgUtil.setChildText(workExperienceRec, "organizers", organizers);
            XmlDocPkgUtil.setChildText(workExperienceRec, "organizers_level", organizersLevel);
            XmlDocPkgUtil.setChildText(workExperienceRec, "personal_part", personalPart);
            XmlDocPkgUtil.setChildText(workExperienceRec, "paper_attachment_id", paperAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增论文发表情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增论文发表情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增论文发表情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 著作发表情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addWorkPublish() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String book_name = dataElement.getChildTextTrim("book_name");
        String complete_way = dataElement.getChildTextTrim("complete_way");
        String publish_time = dataElement.getChildTextTrim("publish_time");
        String complete_chapter = dataElement.getChildTextTrim("complete_chapter");
        String complete_word = dataElement.getChildTextTrim("complete_word");
        String author_order = dataElement.getChildTextTrim("author_order");
        String coverAttachmentId = dataElement.getChildTextTrim("cover_attachment_id");
        String contentsAttachmentId = dataElement.getChildTextTrim("contents_attachment_id");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_work_publish");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "book_name", book_name);
            XmlDocPkgUtil.setChildText(workExperienceRec, "complete_way", complete_way);
            XmlDocPkgUtil.setChildText(workExperienceRec, "publish_time", publish_time);
            XmlDocPkgUtil.setChildText(workExperienceRec, "complete_chapter", complete_chapter);
            XmlDocPkgUtil.setChildText(workExperienceRec, "complete_word", complete_word);
            XmlDocPkgUtil.setChildText(workExperienceRec, "author_order", author_order);
            XmlDocPkgUtil.setChildText(workExperienceRec, "cover_attachment_id", coverAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "contents_attachment_id", contentsAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增著作发表情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增著作发表情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增著作发表情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    /**
     * 课题发表情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addSubject() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String subjectName = dataElement.getChildTextTrim("subject_name");
        String subjectCompany = dataElement.getChildTextTrim("subject_company");
        String subjectLevel = dataElement.getChildTextTrim("subject_level");
        String subjectRresponsibility = dataElement.getChildTextTrim("subject_responsibility");
        String isfinishSubject = dataElement.getChildTextTrim("is_finish_subject");
        String finishResult = dataElement.getChildTextTrim("finish_result");
        String finishTime = dataElement.getChildTextTrim("finish_time");
        String subjectAttachmentId = dataElement.getChildTextTrim("subjectAttachmentId");
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_subject");
            XmlDocPkgUtil.setChildText(workExperienceRec, "subject_name", subjectName);
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "subject_company", subjectCompany);
            XmlDocPkgUtil.setChildText(workExperienceRec, "subject_level", subjectLevel);
            XmlDocPkgUtil.setChildText(workExperienceRec, "subject_responsibility", subjectRresponsibility);
            XmlDocPkgUtil.setChildText(workExperienceRec, "is_finish_subject", isfinishSubject);
            XmlDocPkgUtil.setChildText(workExperienceRec, "finish_result", finishResult);
            XmlDocPkgUtil.setChildText(workExperienceRec, "finish_time", finishTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增课题发表情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增课题发表情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增课题发表情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 个人获奖情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addPersonalAward() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String awardsName = dataElement.getChildTextTrim("awards_name");
        String awardsCompany = dataElement.getChildTextTrim("awards_company");
        String awardsLevel = dataElement.getChildTextTrim("awards_level");
        String awardsTime = dataElement.getChildTextTrim("awards_time");
        String awardsAttachmentId = dataElement.getChildTextTrim("awards_attachment_id");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_personal_award");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_name", awardsName);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_company", awardsCompany);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_level", awardsLevel);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_time", awardsTime);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_attachment_id", awardsAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增个人获奖情况成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增个人获奖情况发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增个人获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     *  学校获得荣誉
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void addSchoolAward() {
        Element dataElement = xmlDocUtil.getRequestData();
 
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String awardsName = dataElement.getChildTextTrim("awards_name");
        String awardsCompany = dataElement.getChildTextTrim("awards_company");
        String workSchool = dataElement.getChildTextTrim("work_school");
        String awardsLevel = dataElement.getChildTextTrim("awards_level");
        String awardsTime = dataElement.getChildTextTrim("awards_time");
        String awardsAttachmentId = dataElement.getChildTextTrim("awards_attachment_id");
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_school_award");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", businessKey);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_name", awardsName);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_company", awardsCompany);
            XmlDocPkgUtil.setChildText(workExperienceRec, "work_school", workSchool);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_level", awardsLevel);
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_time",awardsTime );
            XmlDocPkgUtil.setChildText(workExperienceRec, "awards_attachment_id", awardsAttachmentId);
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增学校获得荣誉成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增学校获得荣誉发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增学校获得荣誉失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
   /**
    * 新增学校改革
    * @Title: addSchoolReform 
    * @Description: TODO
    * @return: void
    */
    private void addSchoolReform() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_school_reform");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "implement_time", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "project_name", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "project_level", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "charge_department", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "performance","" );
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增学校改革成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增学校改革发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增学校改革失败");
        }
        finally
        {
            dao.releaseConnection();
        }        
    }
    
    /**
     * 学校等级评估
     * @Title: addGradeEvaluate 
     * @Description: TODO
     * @return: void
     */
    private void addGradeEvaluate() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_grade_evaluate");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "compulsory_education", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "high_school", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "secondary_school", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增学校等级评估成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增学校等级评估发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增学校等级评估失败");
        }
        finally
        {
            dao.releaseConnection();
        }         
    }

    /**
     * 新增工作经历
     * @Title: addWorkHistory 
     * @Description: TODO
     * @return: void
     */
    private void addWorkHistory() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_work_history");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "start_date", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "end_date", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "prove_people", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增工作经历成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增工作经历发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增工作经历失败");
        }
        finally
        {
            dao.releaseConnection();
        }             
    }



    /**
     * 减分处分
     * @Title: addPunishment 
     * @Description: TODO
     * @return: void
     */
    private void addPunishment() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_punishment");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "implement_time", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "description", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "people", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "department", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "process_result", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增减分处分成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增工作经历发生异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增减分处分失败");
        }
        finally
        {
            dao.releaseConnection();
        }           
    }



    /**
     * 减分责任事故
     * @Title: addAccident 
     * @Description: TODO
     * @return: void
     */
    private void addAccident() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_accident");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "implement_time", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "accident_name", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "description", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "process_result", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增减分责任事故成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增减分责任事故异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增减分责任事故失败");
        }
        finally
        {
            dao.releaseConnection();
        }            
    }

     /**
      * 加分社会责任
      * @Title: addSocialDuty 
      * @Description: TODO
      * @return: void
      */
    private void addSocialDuty() {
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            Element workExperienceRec = ConfigDocument.createRecordElement("headmaster","headmaster_social_duty");
            XmlDocPkgUtil.setChildText(workExperienceRec, "businessKey", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "implement_time", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "superior_task", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "arrange_department", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "complete_state", "");
            XmlDocPkgUtil.setChildText(workExperienceRec, "isvalid", "1");
            String id = dao.insertOneRecordSeqPk(workExperienceRec).toString();
        
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605","新增加分社会责任成功");
            
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "id", ""+id);
            xmlDocUtil.getResponse().addContent(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增加分社会责任异常.]"+e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10606","新增加分社会责任失败");
        }
        finally
        {
            dao.releaseConnection();
        }           
    }
    
   
    
    /**
     * 删除任职年限
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteWorkExperience() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_work_experience set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除任职年限成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除任职年限错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除任职年限失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 删除学历情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteEducation() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_education set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除学历情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除学历情况错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除学历情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 删除职称情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteProfessionalTitle() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_professional_title set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除职称情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除职称情况错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除职称情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 论文发表情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deletePaper() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_paper set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除论文发表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除论文发表错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除论文发表失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 著作发表情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteWorkPublish() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_work_publish set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除著作发表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除著作发表错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除著作发表失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 课题发表情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteSubject() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_subject set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除课题发表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除课题发表错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除课题发表失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 个人获奖情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deletePersonalAward() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_personal_award set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除个人获奖情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除个人获奖情况错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除个人获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    
    /**
     * 学校获奖情况
     * @Title: deleteWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void deleteSchoolAward() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_school_award set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除学校获奖情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除学校获奖情况错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除学校获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
   /**
    * 学校等级评估
    * @Title: deleteGradeEvaluate 
    * @Description: TODO
    * @return: void
    */
    private void deleteGradeEvaluate() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_grade_evaluate set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除学校等级评估成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除学校等级评估错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删 学校等级评估失败");
        }
        finally
        {
            dao.releaseConnection();
        }        
    }
    

   /**
    * 进修学习
    */
    private void deleteStudyTrain() {
    	   Element dataElement = xmlDocUtil.getRequestData();
           String id = dataElement.getChildTextTrim("id");
           PlatformDao dao = new PlatformDao();
           
           try {
               dao.beginTransaction();
               StringBuffer sql = new StringBuffer("update headmaster_studytrain set isvalid=? where id=?");
               ArrayList<String> bvals = new ArrayList<String>();
               bvals.add("0");
               bvals.add(id);

               dao.setSql(sql.toString());
               dao.setBindValues(bvals);
               dao.executeTransactionSql();
               dao.commitTransaction();
               xmlDocUtil.setResult("0");
               xmlDocUtil.writeHintMsg("10603", "删除进修学习成功");
           }
           catch (Exception e)
           {
               e.printStackTrace() ;
               dao.rollBack();
               log4j.logError("[删除进修学习错误]"+e.getMessage()) ;
               log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
               xmlDocUtil.writeErrorMsg("10604","删进修学习失败");
           }
           finally
           {
               dao.releaseConnection();
           }   		
	}


    /**
     * 删除工作经历
     * @Title: deleteWorkHistory 
     * @Description: TODO
     * @return: void
     */
    private void deleteWorkHistory() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_work_history set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除工作经历成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除工作经历错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除工作经历失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }


    /**
     * 减分处分
     * @Title: deletePunishment 
     * @Description: TODO
     * @return: void
     */
    private void deletePunishment() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_punishment set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除减分处分成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除减分处分错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除减分处分失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }


    /**
     * 减分责任事故
     * @Title: deleteAccident 
     * @Description: TODO
     * @return: void
     */
    private void deleteAccident() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_accident set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除减分责任事故成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除减分责任事故错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除减分责任事故失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }


     /**
      * 社会责任
      * @Title: deleteSocialDuty 
      * @Description: TODO
      * @return: void
      */
    private void deleteSocialDuty() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_social_duty set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除社会责任估成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除社会责任错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删社会责任失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }


    /**
     * 加分学校特色和改革
     * @Title: deleteSchoolReform 
     * @Description: TODO
     * @return: void
     */
    private void deleteSchoolReform() {
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        PlatformDao dao = new PlatformDao();
        
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("update headmaster_school_reform set isvalid=? where id=?");
            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add("0");
            bvals.add(id);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除加分学校特色和改革成功");
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
            dao.rollBack();
            log4j.logError("[删除加分学校特色和改革错误]"+e.getMessage()) ;
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true)) ;
            xmlDocUtil.writeErrorMsg("10604","删除加分学校特色和改革失败");
        }
        finally
        {
            dao.releaseConnection();
        }          
    }

    
    
    /**
     * 更新任职年限
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateWorkExperience() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();

        String id = dataElement.getChildTextTrim("id");
        String startTime = dataElement.getChildTextTrim("startTime");
        String endTime = dataElement.getChildTextTrim("endTime");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String workSchool = dataElement.getChildTextTrim("workSchool");
        String workProfession = dataElement.getChildTextTrim("workProfession");
        String workYear = dataElement.getChildTextTrim("workYear");
        String proveAttachMentId = dataElement.getChildTextTrim("proveAttachMentId");
        String isRefill = dataElement.getChildTextTrim("isRefill");
        
        
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";

        
        
        StringBuffer sql = new StringBuffer("");
        sql.append("    UPDATE headmaster_work_experience");
        sql.append("  SET");
        sql.append("  businessKey = ?");
        sql.append("  ,start_date = ?");
        sql.append("  ,end_date = ?");
        sql.append("  ,work_school = ?");
        sql.append("  ,work_profession = ?");
        sql.append(" ,work_year = ?");
       // sql.append("  ,prove_attachment_id = ?");
        sql.append("  ,isvalid = ?");
        sql.append(" ,approve_result = ?");
        sql.append(" ,modify_by = ?");
        sql.append(",modify_date = ?");
        sql.append("  WHERE id = ?");
        PlatformDao dao = null;
        ArrayList bvals = new ArrayList();
        bvals.add(businessKey);
        bvals.add(startTime);
        bvals.add(endTime);
        bvals.add(workSchool);
        bvals.add(workProfession);
        bvals.add(workYear);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改任职年限成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新任职年限异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改任职年限失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 更新学历情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateEducation() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String startTime = dataElement.getChildTextTrim("startTime");
        String endTime = dataElement.getChildTextTrim("endTime");
        String studySchool = dataElement.getChildTextTrim("studySchool");
        String studyProfession = dataElement.getChildTextTrim("studyProfession");
        String education = dataElement.getChildTextTrim("education");
        String degree = dataElement.getChildTextTrim("degree");
        String educationAttachmentId = dataElement.getChildTextTrim("educationAttachmentId");
        String degreeAttachmentId = dataElement.getChildTextTrim("degreeAttachmentId");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
       
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
      //  sql.append(" ,approve_result = ?");
       // sql.append(",create_people = ?");
        //sql.append(" ,create_time = ?");
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append("  WHERE id = ?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(startTime);
        bvals.add(endTime);
        bvals.add(studySchool);
        bvals.add(studyProfession);
        bvals.add(education);
        bvals.add(degree);
        bvals.add(educationAttachmentId);
        bvals.add(degreeAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改学历情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新学历情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改学历情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    
    /**
     * 更新职称情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateProfessionalTitle() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String obtainTime = dataElement.getChildTextTrim("obtainTime");
        String obtainSchool = dataElement.getChildTextTrim("obtainSchool");
        String professionalAttachId = dataElement.getChildTextTrim("professionalAttachId");
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_professional_title");
        sql.append(" SET");
       // sql.append(" ,businessKey = ?");
        sql.append(" obtain_time = ?");
        sql.append(" ,obtain_school = ?");
        sql.append(" ,professionalAttachId = ?");
        sql.append(" ,isvalid = ?");
        sql.append(" ,approve_result = ?");
      //  sql.append(" ,approve_result =?");
      //  sql.append(",create_people = ?");
      //  sql.append(" ,create_time = ?");
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append("  WHERE id =?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(obtainTime);
        bvals.add(obtainSchool);
        bvals.add(professionalAttachId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改职称情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新职称情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改职称情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 更新论文情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updatePaper() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();

        String id = dataElement.getChildTextTrim("id");
        String title = dataElement.getChildTextTrim("title");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String publishTime = dataElement.getChildTextTrim("publish_time");
        String magazineMeetName = dataElement.getChildTextTrim("magazine_meet_name");
        String paperMeetName = dataElement.getChildTextTrim("paper_meet_name");
        String paperNumber = dataElement.getChildTextTrim("paper_number");
        String organizers = dataElement.getChildTextTrim("organizers");
        String organizersLevel = dataElement.getChildTextTrim("organizers_level");
        String personalPart = dataElement.getChildTextTrim("personal_part");
        String paperAttachmentId = dataElement.getChildTextTrim("paper_attachment_id");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
  
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id = ?");
        
        
        ArrayList bvals = new ArrayList();
        bvals.add(title);
        bvals.add(publishTime);
        bvals.add(magazineMeetName);
        bvals.add(paperMeetName);
        bvals.add(paperNumber);
        bvals.add(organizers);
        bvals.add(organizersLevel);
        bvals.add(personalPart);
        bvals.add(paperAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改论文情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新论文情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改论文情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /**
     * 更新著作发表情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateWorkPublish() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String businessKey = dataElement.getChildTextTrim("businessKey");
        String book_name = dataElement.getChildTextTrim("book_name");
        String complete_way = dataElement.getChildTextTrim("complete_way");
        String publish_time = dataElement.getChildTextTrim("publish_time");
        String complete_chapter = dataElement.getChildTextTrim("complete_chapter");
        String complete_word = dataElement.getChildTextTrim("complete_word");
        String author_order = dataElement.getChildTextTrim("author_order");
        String coverAttachmentId = dataElement.getChildTextTrim("cover_attachment_id");
        String contentsAttachmentId = dataElement.getChildTextTrim("contents_attachment_id");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
      
    
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_work_publish ");
        sql.append("SET");
       // sql.append(" id = ?");
      //  sql.append(",businessKey = ?");
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append("WHERE id = ?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(book_name);
        bvals.add(complete_way);
        bvals.add(publish_time);
        bvals.add(complete_chapter);
        bvals.add(complete_word);
        bvals.add(author_order);
        bvals.add(coverAttachmentId);
        bvals.add(contentsAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改著作发表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新著作发表异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改著作发表失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 更新课题情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateSubject() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
       
        String id = dataElement.getChildTextTrim("id");
        String subjectName = dataElement.getChildTextTrim("subject_name");
        String subjectCompany = dataElement.getChildTextTrim("subject_company");
        String subjectLevel = dataElement.getChildTextTrim("subject_level");
        String subjectRresponsibility = dataElement.getChildTextTrim("subject_responsibility");
        String isfinishSubject = dataElement.getChildTextTrim("is_finish_subject");
        String finishResult = dataElement.getChildTextTrim("finish_result");
        String finishTime = dataElement.getChildTextTrim("finish_time");
        String subjectAttachmentId = dataElement.getChildTextTrim("subjectAttachmentId");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
       
        
        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE headmaster_subject");
        sql.append(" SET");
       // sql.append("  id = ?");
      //  sql.append(" ,businessKey = ?");
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
      //  sql.append(" ,create_time = ?");
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id = ?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(subjectName);
        bvals.add(subjectCompany);
        bvals.add(subjectLevel);
        bvals.add(subjectRresponsibility);
        bvals.add(isfinishSubject);
        bvals.add(finishResult);
        bvals.add(finishTime);
        bvals.add(subjectAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改课题情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新课题情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改课题情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 更新个人获奖情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updatePersonalAward() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String awardsName = dataElement.getChildTextTrim("awards_name");
        String awardsCompany = dataElement.getChildTextTrim("awards_company");
        String awardsLevel = dataElement.getChildTextTrim("awards_level");
        String awardsTime = dataElement.getChildTextTrim("awards_time");
        String awardsAttachmentId = dataElement.getChildTextTrim("awards_attachment_id");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
      
        
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_personal_award ");
        sql.append("SET");
       // sql.append(" id = ?");
      //  sql.append(" ,businessKey = ?");
        sql.append("  awards_name = ?");
        sql.append(" ,awards_company = ?");
        sql.append(" ,awards_level = ?");
        sql.append(" ,awards_time = ?");
        sql.append(" ,awards_attachment_id = ?");
         sql.append(" ,isvalid = ?");
         sql.append(" ,approve_result = '?");
       // sql.append(",create_people = ?");
       // sql.append(" ,create_time = ?");
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append("  WHERE id = ?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(awardsName);
        bvals.add(awardsCompany);
        bvals.add(awardsLevel);
        bvals.add(awardsTime);
        bvals.add(awardsAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改个人获奖情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新个人获奖情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改个人获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    
    /**
     * 更新学校获奖情况
     * @Title: addWorkExperience 
     * @Description: TODO
     * @return: void
     */
    private void updateSchoolAward() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String awardsName = dataElement.getChildTextTrim("awards_name");
        String awardsCompany = dataElement.getChildTextTrim("awards_company");
        String workSchool = dataElement.getChildTextTrim("work_school");
        String awardsLevel = dataElement.getChildTextTrim("awards_level");
        String awardsTime = dataElement.getChildTextTrim("awards_time");
        String awardsAttachmentId = dataElement.getChildTextTrim("awards_attachment_id");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(awardsName);
        bvals.add(workSchool);
        bvals.add(awardsCompany);
        bvals.add(awardsLevel);
        bvals.add(awardsTime);
        bvals.add(awardsAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改学校获奖情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新学校获奖情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改学校获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    
    /////////////////////////////////////////////////////////////////////
    /**
     * 
     * @Title: updateAccident 
     * @Description: TODO
     * @return: void
     */
    private void updateAccident() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String id = dataElement.getChildTextTrim("id");
        String awardsName = dataElement.getChildTextTrim("awards_name");
        String awardsCompany = dataElement.getChildTextTrim("awards_company");
        String workSchool = dataElement.getChildTextTrim("work_school");
        String awardsLevel = dataElement.getChildTextTrim("awards_level");
        String awardsTime = dataElement.getChildTextTrim("awards_time");
        String awardsAttachmentId = dataElement.getChildTextTrim("awards_attachment_id");
        
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
        ArrayList bvals = new ArrayList();
        bvals.add(awardsName);
        bvals.add(workSchool);
        bvals.add(awardsCompany);
        bvals.add(awardsLevel);
        bvals.add(awardsTime);
        bvals.add(awardsAttachmentId);
        bvals.add("1");
        bvals.add(approve_result);
        bvals.add(updateUser);
        bvals.add(DatetimeUtil.getNow(""));
        bvals.add(id);
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改学校获奖情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新职称情况异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改学校获奖情况失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    /**
     * 更新学校等级评估
     * @Title: updateGradeEvaluate 
     * @Description: TODO
     * @return: void
     */
    private void updateGradeEvaluate() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
  
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
        List<String> paramList = new ArrayList<String>();
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
  
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            dao.setSql(sql.toString());
            
            for (String childRoomId : paramList) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改学校等级评估成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新学校等级评估异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改学校等级评估失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    /**
     * 更新工作经历
     * @Title: updateWorkHistory 
     * @Description: TODO
     * @return: void
     */
    private void updateWorkHistory() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
  
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
        List<String> paramList = new ArrayList<String>();
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
  
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            dao.setSql(sql.toString());
            
            for (String childRoomId : paramList) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改工作经历评估成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新工作经历异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改工作经历评估失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

     /**
      * 减分处分
      * @Title: updatePunishment 
      * @Description: TODO
      * @return: void
      */
    private void updatePunishment() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
  
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
        List<String> paramList = new ArrayList<String>();
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
  
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            dao.setSql(sql.toString());
            
            for (String childRoomId : paramList) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改减分处分成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新减分处分异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改减分处分失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }

    /**
     * 社会责任
     * @Title: updateSocialDuty 
     * @Description: TODO
     * @return: void
     */
    private void updateSocialDuty() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
  
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
        
        List<String> paramList = new ArrayList<String>();
      
        
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
  
        
        PlatformDao dao = null;
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            dao.setSql(sql.toString());
            
            for (String childRoomId : paramList) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改社会责任成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新社会责任异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改社会责任失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }


    private void updateSchoolReform() {
        Element session = xmlDocUtil.getSession();
        String updateUser = session.getChildText("userid");
        
        Element dataElement = xmlDocUtil.getRequestData();
        String isRefill = dataElement.getChildTextTrim("isRefill");
        String approve_result = (!StringUtil.isEmpty(isRefill) && isRefill.equals("1")) ? "1" : "0";
      
      
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE headmaster_school_award ");
        sql.append("SET");
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
        sql.append(" ,update_people = ?");
        sql.append(",update_time = ?");
        sql.append(" WHERE id =?");
        
  
        
        PlatformDao dao = null;
        
        List<String> paramList = new ArrayList<String>();
        try
        {
            dao = new PlatformDao();
            dao.beginTransaction();
            dao.setSql(sql.toString());
            
            for (String childRoomId : paramList) {
                ArrayList bvals = new ArrayList();
                bvals.add(childRoomId);
                dao.addBatch(bvals);
            }
            dao.executeBatch();
      
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10609","修改学校等级评估成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[更新学校等级评估异常.]"+e.getMessage());
            xmlDocUtil.writeErrorMsg("10610","修改学校等级评估失败");
        }
        finally
        {
            dao.releaseConnection();
        }
    }
    /////////////////////////////////////////////////////////////////////
    
    
    private void saveCurrentOptionNum() {
        Element reqData = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String curUserid = session.getChildText("userid");
        String businessKey = reqData.getChildText("businessKey");
        String current_option_num = reqData.getChildText("current_option_num");

        PlatformDao pDao = null;
        try {
            pDao = new PlatformDao();

            pDao.beginTransaction();
            // 修改学生信息
            Element infoRec = ConfigDocument.createRecordElement("headmaster", "headmaster");
            XmlDocPkgUtil.copyValues(reqData, infoRec, 0, true);
            XmlDocPkgUtil.setChildText(infoRec, "id", businessKey);
            XmlDocPkgUtil.setChildText(infoRec, "current_option_num", current_option_num);
            XmlDocPkgUtil.setChildText(infoRec, "modify_by", curUserid);
            XmlDocPkgUtil.setChildText(infoRec, "modify_date", DatetimeUtil.getNow(null));
            pDao.updateOneRecord(infoRec);
            pDao.commitTransaction();
            // String[] flds = {"userid","usercode","deptid","id"};
            // Element data = XmlDocPkgUtil.createMetaData(flds);
            // data.addContent(XmlDocPkgUtil.createRecord(flds, new
            // String[]{userid,usercode,deptid,headmasterId}));

            xmlDocUtil.setResult("0");
            // xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.writeHintMsg("10645", "修改当前选项卡序号成功");
        } catch (Exception ex) {
            pDao.rollBack();
            ex.printStackTrace();
            log4j.logError(ex);
            xmlDocUtil.writeErrorMsg("10646", "修改当前选项卡序号失败");
        } finally {
            pDao.releaseConnection();
        }
    }


    private void findCurrentOptionNum() {
        Element dataElement = xmlDocUtil.getRequestData();
        String businessKey = dataElement.getChildText("businessKey");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append("SELECT *");
            sqlBuf.append(" FROM headmaster");
            sqlBuf.append(" WHERE id =?");

            bvals.add(businessKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    public MasterReviewService getMasterReviewService() {
        return masterReviewService;
    }

    public void setMasterReviewService(MasterReviewService masterReviewService) {
        this.masterReviewService = masterReviewService;
    }
}
