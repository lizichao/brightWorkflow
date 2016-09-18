package cn.com.bright.yuexue.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jdom.Element;

import sun.awt.geom.AreaOp.NZWindOp;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.yuexue.base.BaseFamily;
import cn.com.bright.yuexue.base.BaseObject;

/**
 * ѧ��Excelģ�����ݴ���
 * @author E40
 *
 */
public class StudentExcel {
	private ImportFile imporFile = new ImportFile();
	protected Workbook rwb = null;//Excel������
	protected Cell cell = null;//Excel��
	protected Sheet sheet = null;
	WorkbookSettings wbs = null ;
	protected InputStream stream =null;
	private BaseObject baseObj = new BaseObject();
	private BaseFamily baseFamilyOne = new BaseFamily();//��ͥ��Ա1
	private BaseFamily baseFamilyTwo = new BaseFamily();//��ͥ��Ա2
	protected String cur_user = "";//��ǰ�û�
	protected String cur_deptid = "";//��ǰ��֯�������
	protected String file_id = "";//�ļ��ĺ�
	protected String file_name="";//�ļ�����
	protected String file_url = "";//�ļ��洢��ַ
	protected String file_flag ="";//�ļ���ʶ
	private Set<BaseFamily> familySet = new HashSet<BaseFamily>();
	
	private Log log4j = new Log(this.getClass().toString());
	
	
	/**
	 * ���ݴ���������ô����ʶ��
	 * @param val
	 * @return
	 * @throws SQLException 
	 */
	  public HashMap getParamMap (String val) throws SQLException {
          String code = "";
          StringBuffer strSQL = new StringBuffer();
          PlatformDao pdao = new PlatformDao();
          try{
	          HashMap texMap = new HashMap();
	          strSQL.append("SELECT pd.PARAMMEANINGS,pd.PARAMCODE FROM param_detail pd, param_master pm WHERE pd.PARAMID = pm.PARAMID AND pm.paramname ='"+val+"'");
	          pdao.setSql(strSQL.toString());
	          Element rsData = pdao.executeQuerySql(-1, 0);
	          List ls = rsData.getChildren("Record");
	          for (int i=0;i<ls.size();i++){
	              Element rec = (Element)ls.get(i);
	              texMap.put(rec.getChildTextTrim("parammeanings"), rec.getChildTextTrim("paramcode"));                  
	          }
	          return texMap;
          }
          finally{
        	  pdao.releaseConnection();  
          }
      }
	  
	/**
	 * ����ļ��洢·��
	 * @return
	 * @throws SQLException
	 */
	public void getFileURL()throws SQLException{
		PlatformDao pdao = new PlatformDao();
		StringBuffer fileSQL = new StringBuffer();
		
		//��ý�Ҫ�������ļ�
		fileSQL.append("select * from base_doc where doc_status='N' ORDER BY RAND() LIMIT 1 ");
		pdao.setSql(fileSQL.toString());
		Element elDocFile = pdao.executeQuerySql(0, 1);
		List lsFile = elDocFile.getChildren("Record");
		if(lsFile.size()>0){
			Element doc = (Element)lsFile.get(0);
			file_id = doc.getChildTextTrim("doc_id");
			baseObj.setUserClass(doc.getChildTextTrim("class_id"));
			cur_user = doc.getChildTextTrim("upload_by");
			file_url = doc.getChildTextTrim("doc_file_url");
			cur_deptid = doc.getChildTextTrim("deptid");
			file_flag = doc.getChildTextTrim("doc_flag");
		}
	}
	/**
	 * ����Excelѧ������ģ��
	 */
	public void importStudent(){
		ApplicationContext.regSubSys("yuexue"); 
		
		PlatformDao pdao  = new PlatformDao();
		try{
			this.getFileURL();
			File file = new File(FileUtil.getWebPath() + file_url);
			
			if(StringUtil.isNotEmpty(file_url)){
				//֤������
				HashMap zjlxMap = this.getParamMap("ZJLXM");
				//�Ա�
				HashMap genderMap = this.getParamMap("gender");
				//����
				HashMap mzMap = this.getParamMap("MZM");
				//����
				HashMap gjMap = this.getParamMap("GJM");
				//�۰�̨����
				HashMap gatqwMap = this.getParamMap("GATQWM");
				//����״��
				HashMap healthMap = this.getParamMap("JKZKM");
				//������ò
				HashMap zzmmMap = this.getParamMap("ZZMM");
				//��������
				HashMap hkxzMap = this.getParamMap("HKXZM");
				//��ѧ���
				HashMap yearMap = this.getParamMap("global_year");
				//��ѧ��ʽ
				HashMap rxfsMap = this.getParamMap("RXFSM");
				//�Ͷ���ʽ
				HashMap jdfsMap = this.getParamMap("JDFSM");
				//�����
				HashMap booleanMap = this.getParamMap("global_boolean");
				//�Ƿ����ض�ͯ
				HashMap lsetMap = this.getParamMap("SFLSET");
				//����ѧ��ʽ
				HashMap sxfsMap = this.getParamMap("SXXFS");
				//Ѫ��
				HashMap xxMap = this.getParamMap("XXM");
				//ѧ����Դ
				HashMap sourceMap = this.getParamMap("XSLYM");
				//�м�����
				HashMap cjlxMap = this.getParamMap("CJLXM");
				//���Ͷ�
				HashMap sbjdMap = this.getParamMap("SBJDM");
				//��ϵ
				HashMap relationMap = this.getParamMap("relation");
				
				stream = new FileInputStream(FileUtil.getWebPath() + file_url);
				wbs = new WorkbookSettings();
				
				wbs.setLocale(new Locale("zh", "CN"));
				wbs.setEncoding("ISO-8859-1");
					//��ȡExcel�ļ�����
				rwb = Workbook.getWorkbook(stream,wbs);
				Set<BaseFamily> family = new HashSet<BaseFamily>();
				//��ȡ�ļ���ָ�������� Ĭ�ϵĵ�һ��
				sheet = rwb.getSheet(0);
				//д�뵽�û���ʱ���ݿ��
				String strSQL = "insert into temp_base_user (userid,usercode,username,studentno,userpwd,idnumber,email,phone,usertype,usersource,state,gender) " +
		 		" values (?,?,?,?,?,?,?,?,?,?,?,?)";
				
				String stuSQL = "INSERT INTO temp_base_student_attr(studinfoid,origin,ctid,health,cid,psid,nid,oid,hkxz,stbirth,areacode,bid,studyway,mailaddress,houseaddress,telephone,postcode,singleflag,preflag,stayflag,helpflag,orphanflag,martyr,goway,carflag,effectdate,rollid,attendant,farmer,houseaid,did,cwid,helpneed,dtance,specialty,homepage,buydegree,soldierflag,userid,source)	" +
						"VALUES	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				String familySQL = "INSERT INTO base_familyinfo(family_id,userid,name,nid,nuxes,relation,ctid,cno,unit,duty,address,houseaid,telephone,tutor)	" +
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				for(int i=2;i<sheet.getRows();i++){
					//��¼�ʺ�
					 if (sheet.findCell("KD_F00")!=null){
						 baseObj.setLoginCode(sheet.getCell(sheet.findCell("KD_F00").getColumn(), i).getContents());
					 }
					 //����
					 if(sheet.findCell("KD_F02")!=null){
						 baseObj.setUserName(sheet.getCell(sheet.findCell("KD_F02").getColumn(), i).getContents());
					 }
					 //�Ա�
					 if(sheet.findCell("KD_F03")!=null){
						 baseObj.setUserGender((String)genderMap.get(sheet.getCell(sheet.findCell("KD_F03").getColumn(), i).getContents()));
					 }
					 //��������
					 if(sheet.findCell("KD_F04")!=null){
						 baseObj.setUserBirth(sheet.getCell(sheet.findCell("KD_F04").getColumn(), i).getContents());
					 }
					 //��������������
					 if(sheet.findCell("KD_F05")!=null){
						 baseObj.setAreacode(sheet.getCell(sheet.findCell("KD_F05").getColumn(), i).getContents());
					 }
					 //����
					 if(sheet.findCell("KD_F06")!=null){
						 baseObj.setOrigin(sheet.getCell(sheet.findCell("KD_F06").getColumn(), i).getContents());
					 }
					 //����
					 if(sheet.findCell("KD_F07")!=null){
						 baseObj.setNid((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F07").getColumn(), i).getContents()));
					 }
					//����������
					 if(sheet.findCell("KD_F08")!=null){
						 baseObj.setCid((String)gjMap.get(sheet.getCell(sheet.findCell("KD_F08").getColumn(), i).getContents()));
					 }
					 //���֤������
					 if(sheet.findCell("KD_F09")!=null){
						 baseObj.setCtid((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F09").getColumn(), i).getContents()));
					 }
					 //�۰�̨����
					 if(sheet.findCell("KD_F10")!=null){
						 baseObj.setOid((String)gatqwMap.get(sheet.getCell(sheet.findCell("KD_F10").getColumn(), i).getContents()));
					 }
					 //����״��
					 if(sheet.findCell("KD_F11")!=null){
						 baseObj.setHealth((String)healthMap.get(sheet.getCell(sheet.findCell("KD_F11").getColumn(), i).getContents()));
					 }
					 //������ò
					 if(sheet.findCell("KD_F12")!=null){
						 baseObj.setPsid((String)zzmmMap.get(sheet.getCell(sheet.findCell("KD_F12").getColumn(), i).getContents()));
					 }
					 //���֤����
					 if(sheet.findCell("KD_F13")!=null){
						 baseObj.setCno(sheet.getCell(sheet.findCell("KD_F13").getColumn(), i).getContents());
					 }
					 //��������
					 if(sheet.findCell("KD_F14")!=null){
						 baseObj.setHkxz((String)hkxzMap.get(sheet.getCell(sheet.findCell("KD_F14").getColumn(), i).getContents()));
					 }
					 //�������ڵ���������
					 if(sheet.findCell("KD_F15")!=null){
						 baseObj.setHouseaid(sheet.getCell(sheet.findCell("KD_F15").getColumn(), i).getContents());
					 }
					 //��ţ�ѧ�ţ�
					 if(sheet.findCell("KD_F16")!=null){
						 baseObj.setStu_no(sheet.getCell(sheet.findCell("KD_F16").getColumn(), i).getContents());
					 }
					 //��ѧ���
					 if(sheet.findCell("KD_F17")!=null){
						 baseObj.setFirstyear((String)yearMap.get(sheet.getCell(sheet.findCell("KD_F17").getColumn(), i).getContents()));
					 }
					 //��ѧ��ʽ
					 if(sheet.findCell("KD_F18")!=null){
						 baseObj.setCwid((String)rxfsMap.get(sheet.getCell(sheet.findCell("KD_F18").getColumn(), i).getContents()));
					 }
					 //�Ͷ���ʽ
					 if(sheet.findCell("KD_F19")!=null){
						 baseObj.setStudyway((String)jdfsMap.get(sheet.getCell(sheet.findCell("KD_F19").getColumn(), i).getContents()));
					 }
					 //��ס��ַ
					 if(sheet.findCell("KD_F20")!=null){
						 baseObj.setAddressnow(sheet.getCell(sheet.findCell("KD_F20").getColumn(), i).getContents());
					 }
					 //ͨ�ŵ�ַ
					 if(sheet.findCell("KD_F21")!=null){
						 baseObj.setMailaddress(sheet.getCell(sheet.findCell("KD_F21").getColumn(), i).getContents());
					 }
					 //��ͥ��ַ
					 if(sheet.findCell("KD_F22")!=null){
						 baseObj.setHouseaddress(sheet.getCell(sheet.findCell("KD_F22").getColumn(), i).getContents());
					 } 
					 //��ϵ�绰
					 if(sheet.findCell("KD_F23")!=null){
						 baseObj.setTelephone(sheet.getCell(sheet.findCell("KD_F23").getColumn(), i).getContents());
					 }
					 //��������
					 if(sheet.findCell("KD_F24")!=null){
						 baseObj.setPostcode(sheet.getCell(sheet.findCell("KD_F24").getColumn(), i).getContents());
					 }
					 //�Ƿ������Ů
					 if(sheet.findCell("KD_F25")!=null){
						 baseObj.setSingleflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F25").getColumn(), i).getContents()));
					 }
					 //�Ƿ��ܹ�ѧǰ����
					 if(sheet.findCell("KD_F26")!=null){
						 baseObj.setPreflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F26").getColumn(), i).getContents()));
					 }
					 //�Ƿ����ض�ͯ
					 if(sheet.findCell("KD_F27")!=null){
						 baseObj.setStayflag((String)lsetMap.get(sheet.getCell(sheet.findCell("KD_F27").getColumn(), i).getContents()));
					 }
					 //�Ƿ���Ҫ��������
					 if(sheet.findCell("KD_F28")!=null){
						 baseObj.setHelpneed((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F28").getColumn(), i).getContents()));
					 }
					//�Ƿ�����һ��
					 if(sheet.findCell("KD_F29")!=null){
						 baseObj.setHelpflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F29").getColumn(), i).getContents()));
					 }
					 //�Ƿ�¶�
					 if(sheet.findCell("KD_F30")!=null){
						 baseObj.setOrphanflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F30").getColumn(), i).getContents()));
					 }
					 //�Ƿ���ʿ�������Ů
					 if(sheet.findCell("KD_F31")!=null){
						 baseObj.setMartyr((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F31").getColumn(), i).getContents()));
					 }
					 //����ѧ����
					 if(sheet.findCell("KD_F32")!=null){
						 baseObj.setDtance(sheet.getCell(sheet.findCell("KD_F32").getColumn(), i).getContents());
					 }
					//����ѧ��ʽ
					 if(sheet.findCell("KD_F33")!=null){
						 baseObj.setGoway((String)sxfsMap.get(sheet.getCell(sheet.findCell("KD_F33").getColumn(), i).getContents()));
					 }
					 //�Ƿ���Ҫ����У��
					 if(sheet.findCell("KD_F34")!=null){
						 baseObj.setCarflag((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F34").getColumn(), i).getContents()));
					 }
					 //������
					 if(sheet.findCell("KD_F35")!=null){
						 baseObj.setUserOldName(sheet.getCell(sheet.findCell("KD_F35").getColumn(), i).getContents());
					 }
					 //���֤����Ч��
					 if(sheet.findCell("KD_F36")!=null){
						 baseObj.setEffectdate(sheet.getCell(sheet.findCell("KD_F36").getColumn(), i).getContents());
					 }
					//Ѫ��
					 if(sheet.findCell("KD_F37")!=null){
						 baseObj.setBid((String)xxMap.get(sheet.getCell(sheet.findCell("KD_F37").getColumn(), i).getContents()));
					 }
					 //�س�
					 if(sheet.findCell("KD_F38")!=null){
						 baseObj.setSpecialty(sheet.getCell(sheet.findCell("KD_F38").getColumn(), i).getContents());
					 }
					 //ѧϰ����
					 if(sheet.findCell("KD_F39")!=null){
						 baseObj.setRollid(sheet.getCell(sheet.findCell("KD_F39").getColumn(), i).getContents());
					 }
					 //����ѧ��
					 if(sheet.findCell("KD_F40")!=null){
						 baseObj.setStu_no(sheet.getCell(sheet.findCell("KD_F40").getColumn(), i).getContents());
					 }
					//ѧ����Դ
					 if(sheet.findCell("KD_F41")!=null){
						 baseObj.setSource((String)sourceMap.get(sheet.getCell(sheet.findCell("KD_F41").getColumn(), i).getContents()));
					 }
					 //��������
					 if(sheet.findCell("KD_F42")!=null){
						 baseObj.setUserEmail(sheet.getCell(sheet.findCell("KD_F42").getColumn(), i).getContents());
					 }
					 //��ҳ��ַ
					 if(sheet.findCell("KD_F43")!=null){
						 baseObj.setHomepage(sheet.getCell(sheet.findCell("KD_F43").getColumn(), i).getContents());
					 }
					 //�м�����
					 if(sheet.findCell("KD_F44")!=null){
						 baseObj.setDid((String)cjlxMap.get(sheet.getCell(sheet.findCell("KD_F44").getColumn(), i).getContents()));
					 }
					//�Ƿ�����������ѧλ
					 if(sheet.findCell("KD_F45")!=null){
						 baseObj.setBuydegree((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F45").getColumn(), i).getContents()));
					 }
					 //���Ͷ�
					 if(sheet.findCell("KD_F46")!=null){
						 baseObj.setAttendant((String)sbjdMap.get(sheet.getCell(sheet.findCell("KD_F46").getColumn(), i).getContents()));
					 }
					 //��Ա1����
					 if(sheet.findCell("KD_F48")!=null){
						 baseFamilyOne.setName_one(sheet.getCell(sheet.findCell("KD_F48").getColumn(), i).getContents());
					 }
					 //��Ա1��ϵ
					 if(sheet.findCell("KD_F49")!=null){
						 baseFamilyOne.setNuxes_one((String)relationMap.get(sheet.getCell(sheet.findCell("KD_F49").getColumn(), i).getContents()));
					 }
					 //��Ա1��ϵ˵��
					 if(sheet.findCell("KD_F50")!=null){
						 baseFamilyOne.setRelation_one(sheet.getCell(sheet.findCell("KD_F50").getColumn(), i).getContents());
					 }
					 //��Ա1��ס��ַ
					 if(sheet.findCell("KD_F51")!=null){
						 baseFamilyOne.setAddress_one(sheet.getCell(sheet.findCell("KD_F51").getColumn(), i).getContents());
					 }
					 //��Ա1�������ڵ�������
					 if(sheet.findCell("KD_F52")!=null){
						 baseFamilyOne.setHouseaid_one(sheet.getCell(sheet.findCell("KD_F52").getColumn(), i).getContents());
					 }
					 //��Ա1��ϵ�绰
					 if(sheet.findCell("KD_F53")!=null){
						 baseFamilyOne.setTelephone_one(sheet.getCell(sheet.findCell("KD_F53").getColumn(), i).getContents());
					 }
					 //��Ա1�Ƿ�໤��
					 if(sheet.findCell("KD_F54")!=null){
						 baseFamilyOne.setTutor_one((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F54").getColumn(), i).getContents()));
					 }
					 //��Ա1���֤������
					 if(sheet.findCell("KD_F55")!=null){
						 baseFamilyOne.setCtid_one((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F55").getColumn(), i).getContents()));
					 }
					 //��Ա1���֤������
					 if(sheet.findCell("KD_F56")!=null){
						 baseFamilyOne.setCno_one(sheet.getCell(sheet.findCell("KD_F56").getColumn(), i).getContents());
					 }
					 //��Ա1����
					 if(sheet.findCell("KD_F57")!=null){
						 baseFamilyOne.setNid_one((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F57").getColumn(), i).getContents()));
					 }
					 //��Ա1������λ
					 if(sheet.findCell("KD_F58")!=null){
						 baseFamilyOne.setUnit_one(sheet.getCell(sheet.findCell("KD_F58").getColumn(), i).getContents());
					 }
					 //��Ա1ְ��
					 if(sheet.findCell("KD_F59")!=null){
						 baseFamilyOne.setDuty_one(sheet.getCell(sheet.findCell("KD_F59").getColumn(), i).getContents());
					 }
					 family.add(baseFamilyOne);
					 
					 
					//��Ա2����
					 if(sheet.findCell("KD_F60")!=null){
						 baseFamilyTwo.setName_one(sheet.getCell(sheet.findCell("KD_F60").getColumn(), i).getContents());
					 }
					 
					 //��Ա2��ϵ
					 if(sheet.findCell("KD_F61")!=null){
						 baseFamilyTwo.setNuxes_one((String)relationMap.get(sheet.getCell(sheet.findCell("KD_F61").getColumn(), i).getContents()));
					 }
					 //��Ա2��ϵ˵��
					 if(sheet.findCell("KD_F62")!=null){
						 baseFamilyTwo.setRelation_one(sheet.getCell(sheet.findCell("KD_F62").getColumn(), i).getContents());
					 }
					 //��Ա2��ס��ַ
					 if(sheet.findCell("KD_F63")!=null){
						 baseFamilyTwo.setAddress_one(sheet.getCell(sheet.findCell("KD_F63").getColumn(), i).getContents());
					 }
					 //��Ա2�������ڵ�������
					 if(sheet.findCell("KD_F64")!=null){
						 baseFamilyTwo.setHouseaid_one(sheet.getCell(sheet.findCell("KD_F64").getColumn(), i).getContents());
					 }
					 //��Ա2��ϵ�绰
					 if(sheet.findCell("KD_F65")!=null){
						 baseFamilyTwo.setTelephone_one(sheet.getCell(sheet.findCell("KD_F65").getColumn(), i).getContents());
					 }
					 //��Ա2�Ƿ�໤��
					 if(sheet.findCell("KD_F66")!=null){
						 baseFamilyTwo.setTutor_one((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F66").getColumn(), i).getContents()));
					 }
					 //��Ա2���֤������
					 if(sheet.findCell("KD_F67")!=null){
						 baseFamilyTwo.setCtid_one((String)zjlxMap.get(sheet.getCell(sheet.findCell("KD_F67").getColumn(), i).getContents()));
					 }
					 //��Ա2���֤������
					 if(sheet.findCell("KD_F68")!=null){
						 baseFamilyTwo.setCno_one(sheet.getCell(sheet.findCell("KD_F68").getColumn(), i).getContents());
					 }
					 //��Ա2����
					 if(sheet.findCell("KD_F69")!=null){
						 baseFamilyTwo.setNid_one((String)mzMap.get(sheet.getCell(sheet.findCell("KD_F69").getColumn(), i).getContents()));
					 }
					 //��Ա2������λ
					 if(sheet.findCell("KD_F70")!=null){
						 baseFamilyTwo.setUnit_one(sheet.getCell(sheet.findCell("KD_F70").getColumn(), i).getContents());
					 }
					 //��Ա2ְ��
					 if(sheet.findCell("KD_F71")!=null){
						 baseFamilyTwo.setDuty_one(sheet.getCell(sheet.findCell("KD_F71").getColumn(), i).getContents());
					 }
					 family.add(baseFamilyTwo);
					 baseObj.setBaseFamily(family);
					 
					 //�Ƿ��������Ա��Ǩ��Ů
					 if(sheet.findCell("KD_F72")!=null){
						 baseObj.setHelpneed((String)booleanMap.get(sheet.getCell(sheet.findCell("KD_F72").getColumn(), i).getContents()));
					 }
					 
					 //��Excel�ļ���ȡ�����ݴ���BUSI_CHILDREN_INFO���ݿ��
				     ArrayList bvals = new ArrayList();
				     String newUserid = (String) new UUIDHexGenerator().generate(null);
				     bvals.add(newUserid);
				     bvals.add(baseObj.getLoginCode());
				     bvals.add(baseObj.getUserName());
				     bvals.add(baseObj.getStu_no());
				     bvals.add(baseObj.getUserPwd());
				     bvals.add(baseObj.getCno());
				     bvals.add(baseObj.getUserEmail());
				     bvals.add(baseObj.getTelephone());
				     bvals.add("1");//ѧ��
				     bvals.add("1");
				     bvals.add("1");
				     bvals.add(baseObj.getUserGender());
				     
				     pdao.setSql(strSQL);
				     pdao.setBindValues(bvals);
				     pdao.executeTransactionSql();
		    		 /**
		    		  * д��ѧ����Ϣ��
		    		  */
				     ArrayList paramVals = new ArrayList();
				     String studentid = (String)new UUIDHexGenerator().generate(null);
				     paramVals.add(studentid);
				     paramVals.add(baseObj.getOrigin());//����
				     paramVals.add(baseObj.getCtid());//֤������
				     paramVals.add(baseObj.getHealth());//����״��
				     paramVals.add(baseObj.getCid());//����
				     paramVals.add(baseObj.getPsid());//������ò
				     paramVals.add(baseObj.getNid());//����
				     paramVals.add(baseObj.getOid());//�۰�̨����
				     paramVals.add(baseObj.getHkxz());//��������
				     paramVals.add(baseObj.getUserBirth());//��������
				     paramVals.add(baseObj.getAreacode());//������������
				     paramVals.add(baseObj.getBid());//Ѫ��
				     paramVals.add(baseObj.getStudyway());//�Ͷ���ʽ
				     paramVals.add(baseObj.getMailaddress());//ͨ�ŵ�ַ
				     paramVals.add(baseObj.getHouseaddress());//��ͥ��ַ
				     paramVals.add(baseObj.getTelephone());//��ϵ�绰
				     paramVals.add(baseObj.getPostcode());//��������
				     paramVals.add(baseObj.getSingleflag());//�Ƿ������Ů
				     paramVals.add(baseObj.getPreflag());//�Ƿ��ܹ�ѧǰ����
				     paramVals.add(baseObj.getStayflag());//�Ƿ����ض�ͯ
				     paramVals.add(baseObj.getHelpflag());//�Ƿ�����һ��
				     paramVals.add(baseObj.getOrphanflag());//�Ƿ�¶�
				     paramVals.add(baseObj.getMartyr());//�Ƿ���ʿ���Ÿ���Ů
				     paramVals.add(baseObj.getGoway());//��ѧ��ʽ
				     paramVals.add(baseObj.getCarflag());//�Ƿ���Ҫ����У��
				     paramVals.add(baseObj.getEffectdate());//���֤��Ч��
				     paramVals.add(baseObj.getRollid());//ѧ������
				     paramVals.add(baseObj.getAttendant());//���Ͷ�
				     paramVals.add(baseObj.getFarmer());//�Ƿ��������Ա��Ǩ��Ů
				     paramVals.add(baseObj.getHouseaid());//�������ڵ�������
				     paramVals.add(baseObj.getDid());//�м�����
				     paramVals.add(baseObj.getCwid());//��ѧ��ʽ
				     paramVals.add(baseObj.getHelpneed());//�Ƿ���Ҫ��������
				     paramVals.add(baseObj.getDtance());//����ѧ����
				     paramVals.add(baseObj.getSpecialty());//�س�
				     paramVals.add(baseObj.getHomepage());//��ҳ��ַ
				     paramVals.add(baseObj.getBuydegree());//�Ƿ���������ѧλ
				     paramVals.add(baseObj.getSoldierflag());//�Ƿ������Ů
				     paramVals.add(newUserid);//�û����
				     paramVals.add(baseObj.getSource());//ѧ����Դ
				     
				     pdao.setSql(stuSQL);
				     pdao.setBindValues(paramVals);
				     pdao.executeTransactionSql();
				     /**
				      * ѧ�����˼�ͥ��Ա��Ϣ
				      */
				     
				    Set<BaseFamily> familyItem =  baseObj.getBaseFamily();
				    if(!familyItem.isEmpty()){
				     Iterator<BaseFamily> familyIterator = familyItem.iterator();
				     while(familyIterator.hasNext()){
				    	 ArrayList familyVals = new ArrayList();
				    	 BaseFamily familyObj = familyIterator.next();
				    	 familyVals.add(new UUIDHexGenerator().generate(null));
				    	 familyVals.add(studentid);
				    	 familyVals.add(familyObj.getName_one());
				    	 familyVals.add(familyObj.getNid_one());
				    	 familyVals.add(familyObj.getNuxes_one());
				    	 familyVals.add(familyObj.getRelation_one());
				    	 familyVals.add(familyObj.getCtid_one());
				    	 familyVals.add(familyObj.getCno_one());
				    	 familyVals.add(familyObj.getUnit_one());
				    	 familyVals.add(familyObj.getDuty_one());
				    	 familyVals.add(familyObj.getAddress_one());
				    	 familyVals.add(familyObj.getHouseaid_one());
				    	 familyVals.add(familyObj.getTelephone_one());
				    	 familyVals.add(familyObj.getTutor_one());
				    	 
				    	 pdao.setSql(familySQL);
					     pdao.setBindValues(familyVals);
					     pdao.executeTransactionSql();
				     }
				    }
				}
				//�޸�Excle�ļ�����״̬
				StringBuffer updateSQL = new StringBuffer("update base_doc set doc_status='Y' "); 
				updateSQL.append(" where doc_id='"+file_id+"'");
				updateSQL.append(" and   doc_file_url='"+file_url+"'");
				pdao.setSql(updateSQL.toString());
				pdao.executeTransactionSql();
				
				//д����ʽ��
				StringBuffer strSQLBuff = new StringBuffer();
				strSQLBuff.append("insert into pcmc_user ");
				strSQLBuff.append("SELECT t1.userid,t1.usercode,t1.username,'',t1.userpwd ")
					.append(",portrait,description,idnumber,email,phone,mobile,emailbind,'1' as mobilebind,gender,usertype,usersource,modifydt,state ")
					.append("FROM temp_base_user t1 where ")
					.append("not exists (select null from pcmc_user where usercode = t1.usercode)");
				pdao.setSql(strSQLBuff.toString());
				pdao.executeTransactionSql();
				
				//ͬʱд��ѧ����Ϣ��
				StringBuffer strsSQL = new StringBuffer();
				strsSQL.append("insert into base_studentinfo ")
					.append("select sm.userid,'").append(cur_deptid).append("' as 'deptid','").append(baseObj.getUserClass())
					.append("' as 'classid',t1.studentno,NULL as 'firstyear',NULL as 'finishyear',NULL as 'transferdt','1' as 'state',")
					.append(" ss.userid AS 'studinfoid' ").append(",'N' as 'pad_lock',NULL as 'lock_time','Y' AS valid ")
					.append(" from temp_base_user t1,pcmc_user sm,temp_base_student_attr ss where t1.usercode = sm.usercode and t1.userid = ss.userid and not exists (select null from base_studentinfo where userid = sm.userid) ");
				pdao.setSql(strsSQL.toString());
				pdao.executeTransactionSql();
				
				//д��ѧ����չ���Ա�
				StringBuffer studAttrSQL = new StringBuffer();
				studAttrSQL.append("insert into base_studentinfo_attr ").append("select * from temp_base_student_attr t where exists")
				.append(" (select null from base_studentinfo where studinfoid = t.userid)");
				pdao.setSql(studAttrSQL.toString());
				pdao.executeTransactionSql();
				
				//д���û�������λ��
				StringBuffer strDeptSQL = new StringBuffer();
				strDeptSQL.append("insert into pcmc_user_dept ")
					.append("select SUBSTR(sm.userid,10,LENGTH(sm.userid)) AS 'userdeptid','").append(cur_deptid).append("' as 'deptid',sm.userid,")
					.append("'1' as 'state','").append(DatetimeUtil.getNow()).append("' as 'in date',Null as 'outdate' ")
					.append(" from temp_base_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_dept where userid = sm.userid)");
				pdao.setSql(strDeptSQL.toString());
				pdao.executeTransactionSql();
				//д���û���չ���Ա�
				StringBuffer userExtSQL = new StringBuffer();
				userExtSQL.append("insert into pcmc_user_ext ")
					.append("select sm.userid,Null as 'birthday','").append(cur_user)
					.append("' as 'createuser',Null as 'pubname',Null as 'pubmail',Null as 'pubphone',")
					.append(" Null as 'pubbirthday',Null as 'remark' ")
					.append(" from temp_base_user t1,pcmc_user sm where t1.usercode = sm.usercode and not exists (select null from pcmc_user_ext where userid = sm.userid)");
				pdao.setSql(userExtSQL.toString());
				pdao.executeTransactionSql();
			}
		}catch (Exception e) {
			e.printStackTrace();
			String errMng = e.getMessage().toString();
			//��ʽ�����
			StringBuffer errSQL= new StringBuffer("update base_doc set doc_status=?,doc_remark=?");
			errSQL.append(" where doc_id='"+file_id+"'");
			errSQL.append(" and doc_file_url='"+file_url+"'");
			
			ArrayList bvals = new ArrayList();
			bvals.add("E");
			if (StringUtil.isNotEmpty(errMng)){
				errMng ="δ֪����";
			}
			if (errMng.length()>400){
			    bvals.add(errMng.substring(0, 400));
			}
			else{
				bvals.add(errMng);
			}
			
			pdao.setSql(errSQL.toString());
			pdao.setBindValues(bvals);
			try {
				pdao.executeTransactionSql();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
		}finally {	
			try {
				//ɾ����ʱ��
				StringBuffer SQL = new StringBuffer();
				SQL.append("delete from temp_base_user");
				pdao.setSql(SQL.toString());
				pdao.executeTransactionSql();
				/*StringBuffer delSQL = new StringBuffer();
				delSQL.append("delete from temp_base_student_attr");
				pdao.setSql(delSQL.toString());
				pdao.executeTransactionSql();*/
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			pdao.releaseConnection();
			try {
			if(null!=stream)
				stream.close();
			} catch (IOException e) {
				
			}
			
		}
	}
}
