package cn.com.bright.yuexue.base;
import java.util.Set;
import cn.brightcom.jraf.util.PasswordEncoder;

public class BaseObject {
	String loginCode = "";//��¼�˺�
	String userEmail = "";//��������
	String userOldName = "";//������
	String stu_no = "";//ѧ��
	String class_no="";//���
	String userName ="";//����
	String userPwd = "";//ϵͳ��ʼ��¼����
	String userBirth = "";//��������
	String areacode ="";//������������
	String userGender = "";//�Ա�
	String nid = "";//����
	String origin = "";//����
	String ctid = "";//֤������
	String cno = "";//֤������
	String userClass = "";//���ڰ༶
	String health = "";//����״��
	String cid ="";//����
	String psid = "";//������ò
	String hkxz = "";//��������
	String firstyear = "";//��ѧ���
	String oid ="";//�۰�̨����
	String csd = "";//������
	String bid = "";//Ѫ��
	String studyway="";//�Ͷ���ʽ
	String mailaddress="";//ͨ�ŵ�ַ
	String houseaddress = "";//��ͥ��ַ
	String telephone ="";//��ϵ�绰
	String postcode = "";//��������
	String singleflag ="";//�Ƿ������Ů
	String preflag = "";//�Ƿ��ܹ�ѧǰ����
	String stayflag = "";//�Ƿ����ض�ͯ
	String helpflag = "";//�Ƿ�����һ��
	String orphanflag = "";//�Ƿ�¶�
	String martyr = "";//�Ƿ���ʿ���Ÿ���Ů
	String goway = "";//��ѧ��ʽ
	String carflag="";//�Ƿ���Ҫ����У��
	String effectdate = "";//���֤��Ч��
	String rollid = "";//ѧ������
	String attendant = "";//���Ͷ�
	String farmer = "";//�Ƿ��������Ա��Ǩ��Ů
	String houseaid = "";//�������ڵ�������
	String did = "";//�м�����
	String cwid = "";//��ѧ��ʽ
	String hard = "";//���ѳ̶�
	String addressnow = "";//��ס��ַ
	String helpneed = "";//�Ƿ���Ҫ��������
	String dtance = "";//����ѧ����
	String specialty = "";//�س�
	String homepage = "";//��ҳ��ַ
	String buydegree = "";//�Ƿ���������ѧλ
	String soldierflag = "";//�Ƿ������Ů
	String source = "";//ѧ����Դ
	Set<BaseFamily> baseFamily ; //��ͥ��Ա
	
	public Set<BaseFamily> getBaseFamily() {
		return baseFamily;
	}
	public void setBaseFamily(Set<BaseFamily> baseFamily) {
		this.baseFamily = baseFamily;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserBirth() {
		return userBirth;
	}
	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getCtid() {
		return ctid;
	}
	public void setCtid(String ctid) {
		this.ctid = ctid;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getPsid() {
		return psid;
	}
	public void setPsid(String psid) {
		this.psid = psid;
	}
	public String getHkxz() {
		return hkxz;
	}
	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}
	public String getFirstyear() {
		return firstyear;
	}
	public void setFirstyear(String firstyear) {
		this.firstyear = firstyear;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getCsd() {
		return csd;
	}
	public void setCsd(String csd) {
		this.csd = csd;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getStudyway() {
		return studyway;
	}
	public void setStudyway(String studyway) {
		this.studyway = studyway;
	}
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	public String getHouseaddress() {
		return houseaddress;
	}
	public void setHouseaddress(String houseaddress) {
		this.houseaddress = houseaddress;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getSingleflag() {
		return singleflag;
	}
	public void setSingleflag(String singleflag) {
		this.singleflag = singleflag;
	}
	public String getPreflag() {
		return preflag;
	}
	public void setPreflag(String preflag) {
		this.preflag = preflag;
	}
	public String getStayflag() {
		return stayflag;
	}
	public void setStayflag(String stayflag) {
		this.stayflag = stayflag;
	}
	public String getHelpflag() {
		return helpflag;
	}
	public void setHelpflag(String helpflag) {
		this.helpflag = helpflag;
	}
	public String getOrphanflag() {
		return orphanflag;
	}
	public void setOrphanflag(String orphanflag) {
		this.orphanflag = orphanflag;
	}
	public String getMartyr() {
		return martyr;
	}
	public void setMartyr(String martyr) {
		this.martyr = martyr;
	}
	public String getGoway() {
		return goway;
	}
	public void setGoway(String goway) {
		this.goway = goway;
	}
	public String getCarflag() {
		return carflag;
	}
	public void setCarflag(String carflag) {
		this.carflag = carflag;
	}
	public String getEffectdate() {
		return effectdate;
	}
	public void setEffectdate(String effectdate) {
		this.effectdate = effectdate;
	}
	public String getRollid() {
		return rollid;
	}
	public void setRollid(String rollid) {
		this.rollid = rollid;
	}
	public String getAttendant() {
		return attendant;
	}
	public void setAttendant(String attendant) {
		this.attendant = attendant;
	}
	public String getFarmer() {
		return farmer;
	}
	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}
	public String getHouseaid() {
		return houseaid;
	}
	public void setHouseaid(String houseaid) {
		this.houseaid = houseaid;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getCwid() {
		return cwid;
	}
	public void setCwid(String cwid) {
		this.cwid = cwid;
	}
	public String getHard() {
		return hard;
	}
	public void setHard(String hard) {
		this.hard = hard;
	}
	public String getAddressnow() {
		return addressnow;
	}
	public void setAddressnow(String addressnow) {
		this.addressnow = addressnow;
	}
	public String getHelpneed() {
		return helpneed;
	}
	public void setHelpneed(String helpneed) {
		this.helpneed = helpneed;
	}
	public String getDtance() {
		return dtance;
	}
	public void setDtance(String dtance) {
		this.dtance = dtance;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getBuydegree() {
		return buydegree;
	}
	public void setBuydegree(String buydegree) {
		this.buydegree = buydegree;
	}
	public String getSoldierflag() {
		return soldierflag;
	}
	public void setSoldierflag(String soldierflag) {
		this.soldierflag = soldierflag;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getStu_no() {
		return stu_no;
	}
	public void setStu_no(String stu_no) {
		this.stu_no = stu_no;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUserOldName() {
		return userOldName;
	}
	public void setUserOldName(String userOldName) {
		this.userOldName = userOldName;
	}
	public String getUserPwd() {
		return PasswordEncoder.encode("123456");
	}
	
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
}