package cn.com.bright.yuexue.report;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.Sheet;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.base.BaseFamily;
import cn.com.bright.yuexue.base.BaseObject;

/**
 * ѧ��������Ϣ����ҵ������
 * 
 * @author E40
 *
 */
public class ImportStudent extends AbstractExcel {

	private XmlDocPkgUtil xmlDocUtil = null;
	private Element reqElData=null;
	private BaseObject baseObj = new BaseObject();//ѧ����Ϣ
	private BaseFamily baseFamilyOne = new BaseFamily();//��ͥ��Ա1
	private BaseFamily baseFamilyTwo = new BaseFamily();//��ͥ��Ա2
	private Set<BaseFamily> family = new HashSet<BaseFamily>();
	private PrintWriter logFile = null;
	private int sucCnt = 0;
	private int dupCnt = 0;
	private int errCnt = 0;
	private int singCnt =0;
	private int skipCnt = 0;
	private String upload_by = "",exam_type="";
	private String cur_deptid = "";
	private String classCodes ="";
	//private String curUserIds = xmlDocUtil.getSession().getChildTextTrim("userid");	
	
	@Override
	public void saveExcle(Element reqData) throws Exception {
		try{
			String filenm = reqData.getChildTextTrim("doc_file_url");
			String doc_id = reqData.getChildTextTrim("doc_id");
			cur_deptid = reqData.getChildTextTrim("deptid");
			classCodes = reqData.getChildTextTrim("class_id");
			upload_by = reqData.getChildText("upload_by");
			System.out.println(DatetimeUtil.getNow(null) + ",�����ļ�" + filenm);
			String logname = filenm + ".log";
			logFile = new PrintWriter(FileUtil.getPhysicalPath(logname));
			sucCnt = dupCnt = errCnt = skipCnt = 0;
			
			Sheet sheet = parseExcel(doc_id, FileUtil.getPhysicalPath(filenm));
			int rows = sheet.getRows();
			if(rows>0){
				parseRow( rows,  sheet);
			}
			StringBuffer info = new StringBuffer().append("�ɹ�:").append(
					sucCnt).append(",").append("�ظ�:").append(dupCnt)
					.append(",").append("����:").append(errCnt).append(",")
					.append("����:").append(dupCnt);
			logFile.println("ѧ��������Ϣ����"+DatetimeUtil.getNow(null) + ",�������....");
			logFile.println(info.toString());
			upFlag(doc_id,info.toString());
			uptTempStudent();
			System.out.println(info.toString());
		}catch (Exception ex) {
			try {
				if (null != logFile) {
					ex.printStackTrace(logFile);
				}
			} catch (Exception e) {

			}
			ex.printStackTrace();
			log4j.logError(ex);
		} finally {
			if (null != logFile) {
				try {
					logFile.flush();
					logFile.close();
				} catch (Exception ex) {

				}
				logFile = null;
			}
		}
	}
	
	
	public void parseRow(int rows, Sheet sheet) throws Exception {
		String value = "",dbColName="",xlsColName="",extype="",conversion="",isnull="";
		PlatformDao pdao = new PlatformDao();
		List excelList = getExlElement("student");
		if (excelList.size()<=0) return;
		try {
			a:for (int i = 1; i < rows; i++) {
				Element rec = new Element("RequestData");
				b:for(int j=0;j<excelList.size();j++){  
					 Element ele = (Element)excelList.get(j);
					 dbColName = ele.getName();
					 xlsColName =  ele.getText();
					 extype = ele.getAttributeValue("extype");
					 conversion = ele.getAttributeValue("conversion");
					 isnull = ele.getAttributeValue("isnull");
					 value = sheet.getCell(sheet.findCell(xlsColName).getColumn(), i).getContents();
					if ("N".equals(isnull)) {
						 if(sheet.findCell(xlsColName) == null||"".equals(value)) 
						continue a;
					}
					if (sheet.findCell(xlsColName) != null) {
						if("date".equals(extype)){
							value = sheet.getCell(sheet.findCell(xlsColName).getColumn(), i).getContents();
							value=formatDatetTime(value).toString();
						}
						if (!"N".equals(conversion)) {
							value=AbstractExcel.convesionStr(conversion,value);
						}
						if("exam_type".equals(dbColName)) exam_type=value;
						rec.addContent(new Element(dbColName).setText(value));
					} else {
						continue a;
					}
				} 
				saveTempStudent(rec);
				sucCnt++;
			}
			StringBuffer checkSQL = new StringBuffer();
			checkSQL.append("SELECT t.* FROM temp_base_student t ");
			checkSQL.append("WHERE t.valid='Y' and NOT EXISTS  ");
			checkSQL.append("(SELECT NULL FROM pcmc_user WHERE idnumber = t.sfzjh)");
			pdao.setSql(checkSQL.toString());
			Element tempElement = (Element)pdao.executeQuerySql(0, -1);
			List list= tempElement.getChildren("Record");
			if(list.size()>0){
				for(int k=0;k<list.size();k++){
					saveStudentInfo((Element)list.get(k));
					singCnt++;
				}
			}
			dupCnt = sucCnt-singCnt;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static Object formatDatetTime(String dstr)
	{
		try
		{
			if(dstr.length()== 8){
				dstr = dstr.substring(0,4)+"-"+dstr.substring(4,6)+"-"+dstr.substring(6,8);
			}else{
				dstr = dstr.replaceAll("/", "-").replaceAll("��", "-").replaceAll("��", "-").replaceAll("��", "-").replaceAll("��", "-").replaceAll("��", "-").replaceAll("��", "-").replaceAll("-","-");
				dstr= dstr.replace(".", "-");
			}
			System.out.println(dstr);
			return SqlTypes.getConvertor("Timestamp").convert(dstr,"yyyy-MM-dd");
		}
		catch(Exception ex)
		{
		   ex.printStackTrace();	
		}
		return null;
	}
	
	/**
	 * д����ʱѧ�����ݿ��
	 * @param reqcodeclasData
	 */
	private void saveTempStudent(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			pdao.beginTransaction();
			Element tempRecord = ConfigDocument.createRecordElement("student", "temp_base_student");
			XmlDocPkgUtil.copyValues(objEl, tempRecord, 0 , true);
			pdao.insertOneRecord(tempRecord);
			pdao.commitTransaction();
		} catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();
		}finally{
			pdao.releaseConnection();
		}
	}
	
	/**
	 * �޸���ʱѧ�����ݿ����Ч��ʶ
	 * @param reqcodeclasData
	 */
	private void uptTempStudent(){
		PlatformDao pdao = new PlatformDao();
		try {
			String strSQL ="update temp_base_student set valid='N' where valid='Y'";
			pdao.setSql(strSQL);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public void saveStudentInfo(Element reqcodeclasData){
		Element objEl = reqcodeclasData;
		PlatformDao pdao = new PlatformDao();
		try {
			    pdao.beginTransaction();
			    baseObj.setUserName(objEl.getChildTextTrim("xm"));//����
			    baseObj.setUserGender(objEl.getChildTextTrim("xb"));//�Ա�
			    baseObj.setUserBirth(objEl.getChildTextTrim("csrq"));//��������
			    baseObj.setAreacode(objEl.getChildTextTrim("csdxzq"));//������������������
			    baseObj.setOrigin(objEl.getChildTextTrim("jg"));//����
			    baseObj.setNid(objEl.getChildTextTrim("mz"));//����
			    baseObj.setCid(objEl.getChildTextTrim("gj"));//����
				baseObj.setCtid(objEl.getChildTextTrim("sfzjlx"));//���֤������
				baseObj.setOid(objEl.getChildTextTrim("gatqw"));//�۰�̨����
				baseObj.setHealth(objEl.getChildTextTrim("jkzk"));//����״��
				baseObj.setPsid(objEl.getChildTextTrim("zzmm"));//������ò
				baseObj.setCno(objEl.getChildTextTrim("sfzjh").toUpperCase());//֤������
				baseObj.setHkxz(objEl.getChildTextTrim("hkxz"));//��������
				baseObj.setHouseaid(objEl.getChildTextTrim("hkszdxzqh"));//�������ڵ���������
				baseObj.setClass_no(objEl.getChildTextTrim("bh"));//���
				baseObj.setFirstyear(objEl.getChildTextTrim("rxny"));//��ѧ���
				baseObj.setCwid(objEl.getChildTextTrim("rxfs"));//��ѧ��ʽ
			    baseObj.setStudyway(objEl.getChildTextTrim("jdfs"));//�Ͷ���ʽ
			    baseObj.setAddressnow(objEl.getChildTextTrim("xzz"));//��ס��ַ
			    baseObj.setMailaddress(objEl.getChildTextTrim("txdz"));//ͨ�ŵ�ַ
			    baseObj.setHouseaddress(objEl.getChildTextTrim("jtdz"));//��ͥ��ַ
			    baseObj.setTelephone(objEl.getChildTextTrim("lxdh"));//��ϵ�绰
			    baseObj.setPostcode(objEl.getChildTextTrim("yzbm"));//��������
			    baseObj.setSingleflag(objEl.getChildTextTrim("sfdszn"));//�Ƿ������Ů
			    baseObj.setPreflag(objEl.getChildTextTrim("sfsgxqjy"));//�Ƿ��ܹ�ѧǰ����
			    baseObj.setStayflag(objEl.getChildTextTrim("sflset"));//�Ƿ����ض�ͯ
			    baseObj.setHelpneed(objEl.getChildTextTrim("sfxysqzz"));//�Ƿ���Ҫ��������
			    baseObj.setHelpflag(objEl.getChildTextTrim("sfxsyb"));//�Ƿ�����һ��
			    baseObj.setOrphanflag(objEl.getChildTextTrim("sfge"));//�Ƿ�¶�
			    baseObj.setMartyr(objEl.getChildTextTrim("sfls"));//�Ƿ���ʿ
			    baseObj.setDtance(objEl.getChildTextTrim("sxxjl"));//����ѧ����
			    baseObj.setGoway(objEl.getChildTextTrim("sxxfs"));//����ѧ��ʽ
			    baseObj.setCarflag(objEl.getChildTextTrim("sfxyczxc"));//�Ƿ����У��
			    baseObj.setUserOldName(objEl.getChildTextTrim("zym"));//������
			    baseObj.setEffectdate(objEl.getChildTextTrim("sfzyxq"));//���֤��Ч��
			    baseObj.setBid(objEl.getChildTextTrim("xx"));//Ѫ��
			    baseObj.setSpecialty(objEl.getChildTextTrim("tc"));//�س�
			    baseObj.setRollid(objEl.getChildTextTrim("xjfh"));//ѧ������
			    baseObj.setStu_no(objEl.getChildTextTrim("bnxh"));//����ѧ��
			    baseObj.setSource(objEl.getChildTextTrim("xsly"));//ѧ����Դ
			    baseObj.setUserEmail(objEl.getChildTextTrim("dzxx"));//��������
			    baseObj.setHomepage(objEl.getChildTextTrim("zydz"));//��ҳ��ַ
			    baseObj.setDid(objEl.getChildTextTrim("cjlx"));//�м�����
			    baseObj.setBuydegree(objEl.getChildTextTrim("sfyzfgmxw"));//�Ƿ�����������ѧλ
			    baseObj.setAttendant(objEl.getChildTextTrim("sbjd"));//���Ͷ�
			    //��Ա��Ϣ����Աһ��
			    baseFamilyOne.setName_one(objEl.getChildTextTrim("cy1xm"));//��Ա1����
			    baseFamilyOne.setNuxes_one(objEl.getChildTextTrim("cy1gx"));//��Ա1��ϵ
			    baseFamilyOne.setRelation_one(objEl.getChildTextTrim("cy1gxsm"));//��Ա1��ϵ˵��
			    baseFamilyOne.setAddress_one(objEl.getChildTextTrim("cy1xzz"));//��Ա1��סַ
			    baseFamilyOne.setHouseaid_one(objEl.getChildTextTrim("cy1hkszd"));//��Ա1�������ڵ���������
			    baseFamilyOne.setTelephone_one(objEl.getChildTextTrim("cy1lx"));//��Ա1��ϵ�绰
			    baseFamilyOne.setTutor_one(objEl.getChildTextTrim("cy1sfjhr"));//��Ա1�Ƿ�໤��
			    baseFamilyOne.setCtid_one(objEl.getChildTextTrim("cy1sfzjlx"));//��Ա1���֤������
			    baseFamilyOne.setCno_one(objEl.getChildTextTrim("cy1sfzh"));//��Ա1���֤����
			    baseFamilyOne.setNid_one(objEl.getChildTextTrim("cy1mz"));//��Ա1����
			    baseFamilyOne.setUnit_one(objEl.getChildTextTrim("cy1gzdw"));//��Ա1������λ
			    baseFamilyOne.setDuty_one(objEl.getChildTextTrim("cy1zw"));//��Ա1ְ��
			    family.add(baseFamilyOne);
			    //��Ա��Ϣ����Ա����
			    baseFamilyTwo.setName_one(objEl.getChildTextTrim("cy2xm"));//��Ա2����
			    baseFamilyTwo.setNuxes_one(objEl.getChildTextTrim("cy2gx"));//��Ա2��ϵ
			    baseFamilyTwo.setRelation_one(objEl.getChildTextTrim("cy2gxsm"));//��Ա2��ϵ˵��
			    baseFamilyTwo.setAddress_one(objEl.getChildTextTrim("cy2xzz"));//��Ա2��סַ
			    baseFamilyTwo.setHouseaid_one(objEl.getChildTextTrim("cy2hkszd"));//��Ա2�������ڵ���������
			    baseFamilyTwo.setTelephone_one(objEl.getChildTextTrim("cy2lx"));//��Ա2��ϵ�绰
			    baseFamilyTwo.setTutor_one(objEl.getChildTextTrim("cy2sfjhr"));//��Ա2�Ƿ�໤��
			    baseFamilyTwo.setCtid_one(objEl.getChildTextTrim("cy2sfzjlx"));//��Ա2���֤������
			    baseFamilyTwo.setCno_one(objEl.getChildTextTrim("cy2sfzh"));//��Ա2���֤����
			    baseFamilyTwo.setNid_one(objEl.getChildTextTrim("cy2mz"));//��Ա2����
			    baseFamilyTwo.setUnit_one(objEl.getChildTextTrim("cy2gzdw"));//��Ա2������λ
			    baseFamilyTwo.setDuty_one(objEl.getChildTextTrim("cy2zw"));//��Ա2ְ��
			    family.add(baseFamilyTwo);
			    baseObj.setBaseFamily(family);
			    //�����û���ϵͳ
			    Element userRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user");
				XmlDocPkgUtil.setChildText(userRecord, "username", baseObj.getUserName());
				XmlDocPkgUtil.setChildText(userRecord, "usercode", baseObj.getCno());
				XmlDocPkgUtil.setChildText(userRecord, "userpwd", baseObj.getUserPwd());
				XmlDocPkgUtil.setChildText(userRecord, "idnumber", baseObj.getCno());
				XmlDocPkgUtil.setChildText(userRecord, "phone", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(userRecord, "email", baseObj.getUserEmail());
				XmlDocPkgUtil.setChildText(userRecord, "mobile", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(userRecord, "gender", baseObj.getUserGender());
				XmlDocPkgUtil.setChildText(userRecord, "usertype", "1");
				XmlDocPkgUtil.setChildText(userRecord, "usersource", "1");
				XmlDocPkgUtil.setChildText(userRecord, "modifydt", DatetimeUtil.getNow(""));
				XmlDocPkgUtil.setChildText(userRecord, "state", "1");
			    String pk_userid = (String)pdao.insertOneRecordSeqPk(userRecord);
			    
			    //�û�������ϵ��
			    Element deptRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user_dept");
			    XmlDocPkgUtil.setChildText(deptRecord, "deptid", cur_deptid);
			    XmlDocPkgUtil.setChildText(deptRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(deptRecord, "state", "1");
			    XmlDocPkgUtil.setChildText(deptRecord, "indate", DatetimeUtil.getNow(""));
			    pdao.insertOneRecord(deptRecord);
			    
			    //ϵͳ�û���չ��Ϣ
			    Element userExtRecord = ConfigDocument.createRecordElement("pcmc", "pcmc_user_ext");
			    XmlDocPkgUtil.setChildText(userExtRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(userExtRecord, "birthday", baseObj.getUserBirth());
			    XmlDocPkgUtil.setChildText(userExtRecord, "createuser", upload_by);
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubname", "0");
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubmail","0");
			    XmlDocPkgUtil.setChildText(userExtRecord, "pubphone", "0");
			    pdao.insertOneRecord(userExtRecord);
			    
			    //ѧ��������Ϣ
			    Element stuentRecord = ConfigDocument.createRecordElement("yuexue", "base_studentinfo");
			    XmlDocPkgUtil.setChildText(stuentRecord, "userid", pk_userid);
			    XmlDocPkgUtil.setChildText(stuentRecord, "deptid", cur_deptid);
			    XmlDocPkgUtil.setChildText(stuentRecord, "classid", classCodes);
			    XmlDocPkgUtil.setChildText(stuentRecord, "studentno", baseObj.getStu_no());
			    XmlDocPkgUtil.setChildText(stuentRecord, "state", "1");
			    XmlDocPkgUtil.setChildText(stuentRecord, "oldname", baseObj.getUserOldName());
			    XmlDocPkgUtil.setChildText(stuentRecord, "origin",baseObj.getOrigin());
			    XmlDocPkgUtil.setChildText(stuentRecord, "helpflag", baseObj.getHelpflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "ctid", baseObj.getCtid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "health", baseObj.getHealth());
			    XmlDocPkgUtil.setChildText(stuentRecord, "cid", baseObj.getCid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "psid", baseObj.getPsid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "oid", baseObj.getOid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "hkxz", baseObj.getHkxz());
			    XmlDocPkgUtil.setChildText(stuentRecord, "stbirth", baseObj.getUserBirth());
			    XmlDocPkgUtil.setChildText(stuentRecord, "areacode", baseObj.getAreacode());
			    XmlDocPkgUtil.setChildText(stuentRecord, "csd", baseObj.getCsd());
			    XmlDocPkgUtil.setChildText(stuentRecord, "bid", baseObj.getBid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "studyway", baseObj.getStudyway());
			    XmlDocPkgUtil.setChildText(stuentRecord, "mailaddress", baseObj.getMailaddress());
			    XmlDocPkgUtil.setChildText(stuentRecord, "houseaddress", baseObj.getHouseaddress());
			    XmlDocPkgUtil.setChildText(stuentRecord, "telephone", baseObj.getTelephone());
			    XmlDocPkgUtil.setChildText(stuentRecord, "postcode", baseObj.getPostcode());
			    XmlDocPkgUtil.setChildText(stuentRecord, "singleflag", baseObj.getSingleflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "preflag", baseObj.getPreflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "stayflag", baseObj.getStayflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "orphanflag", baseObj.getOrphanflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "martyr", baseObj.getMartyr());
			    XmlDocPkgUtil.setChildText(stuentRecord, "goway", baseObj.getGoway());
			    XmlDocPkgUtil.setChildText(stuentRecord, "carflag", baseObj.getCarflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "effectdate", baseObj.getEffectdate());
			    XmlDocPkgUtil.setChildText(stuentRecord, "rollid", baseObj.getRollid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "attendant", baseObj.getAttendant());
			    XmlDocPkgUtil.setChildText(stuentRecord, "farmer", baseObj.getFarmer());
			    XmlDocPkgUtil.setChildText(stuentRecord, "houseaid", baseObj.getHouseaid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "did", baseObj.getDid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "cwid", baseObj.getCwid());
			    XmlDocPkgUtil.setChildText(stuentRecord, "hard", baseObj.getHard());
			    XmlDocPkgUtil.setChildText(stuentRecord, "addressnow", baseObj.getAddressnow());
			    XmlDocPkgUtil.setChildText(stuentRecord, "helpneed", baseObj.getHelpneed());
			    XmlDocPkgUtil.setChildText(stuentRecord, "dtance", baseObj.getDtance());
			    XmlDocPkgUtil.setChildText(stuentRecord, "specialty", baseObj.getSpecialty());
			    XmlDocPkgUtil.setChildText(stuentRecord, "homepage", baseObj.getHomepage());
			    XmlDocPkgUtil.setChildText(stuentRecord, "soldierflag", baseObj.getSoldierflag());
			    XmlDocPkgUtil.setChildText(stuentRecord, "buydegree", baseObj.getBuydegree());
			    XmlDocPkgUtil.setChildText(stuentRecord, "source", baseObj.getSource());
			    XmlDocPkgUtil.setChildText(stuentRecord, "first_yearmoth", baseObj.getFirstyear());
			    XmlDocPkgUtil.setChildText(stuentRecord, "valid", "Y");
			    XmlDocPkgUtil.setChildText(stuentRecord, "stu_name", baseObj.getUserName());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_sex", baseObj.getUserGender());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_idnumber",baseObj.getCno());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_email", baseObj.getUserEmail());
				XmlDocPkgUtil.setChildText(stuentRecord, "stu_mobile", baseObj.getTelephone());
				XmlDocPkgUtil.setChildText(stuentRecord, "stbirth", baseObj.getUserBirth());
				XmlDocPkgUtil.setChildText(stuentRecord, "telephone",baseObj.getTelephone());
			    String studinfoIds = (String)pdao.insertOneRecordSeqPk(stuentRecord);
			    
			    //��ͥ��Ա��Ϣ
			    Set<BaseFamily> familyItem =  baseObj.getBaseFamily();
			    if(!familyItem.isEmpty()){
				     Iterator<BaseFamily> familyIterator = familyItem.iterator();
				     Element familyRecord = ConfigDocument.createRecordElement("yuexue", "base_familyinfo");
				     while(familyIterator.hasNext()){
				    	 BaseFamily familyObj = familyIterator.next();
				    	 XmlDocPkgUtil.setChildText(familyRecord, "userid", studinfoIds);
				    	 XmlDocPkgUtil.setChildText(familyRecord, "name", familyObj.getName_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "nid",familyObj.getNid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "nuxes", familyObj.getNuxes_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "relation", familyObj.getRelation_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "ctid", familyObj.getCtid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "cno", familyObj.getCno_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "unit", familyObj.getUnit_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "duty",familyObj.getDuty_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "address", familyObj.getAddress_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "houseaid", familyObj.getHouseaid_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "telephone", familyObj.getTelephone_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "tutor", familyObj.getTutor_one());
				    	 XmlDocPkgUtil.setChildText(familyRecord, "valid", "Y");
				    	 pdao.insertOneRecord(familyRecord);
				     }
			    }
			    pdao.commitTransaction();
			} catch (Exception e) {
				pdao.rollBack();
				e.printStackTrace();
		        xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		        Log log4j = new Log(this.getClass().toString());
		        log4j.logError("[��������-���ӵ���ѧ����Ϣ.]"+e.getMessage());
			} finally {
				pdao.releaseConnection();
			}
	}
}
