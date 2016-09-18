package cn.com.bright.yuexue.base;
import java.util.Set;
import cn.brightcom.jraf.util.PasswordEncoder;

public class BaseObject {
	String loginCode = "";//登录账号
	String userEmail = "";//电子邮箱
	String userOldName = "";//曾用名
	String stu_no = "";//学号
	String class_no="";//班号
	String userName ="";//姓名
	String userPwd = "";//系统初始登录密码
	String userBirth = "";//出生日期
	String areacode ="";//出生地行政区
	String userGender = "";//性别
	String nid = "";//民族
	String origin = "";//籍贯
	String ctid = "";//证件类型
	String cno = "";//证件号码
	String userClass = "";//所在班级
	String health = "";//健康状况
	String cid ="";//国籍
	String psid = "";//政治面貌
	String hkxz = "";//户口性质
	String firstyear = "";//入学年份
	String oid ="";//港澳台侨外
	String csd = "";//出生地
	String bid = "";//血型
	String studyway="";//就读方式
	String mailaddress="";//通信地址
	String houseaddress = "";//家庭地址
	String telephone ="";//联系电话
	String postcode = "";//邮政编码
	String singleflag ="";//是否独生子女
	String preflag = "";//是否受过学前教育
	String stayflag = "";//是否留守儿童
	String helpflag = "";//是否享受一补
	String orphanflag = "";//是否孤儿
	String martyr = "";//是否烈士或优抚子女
	String goway = "";//上学方式
	String carflag="";//是否需要乘坐校车
	String effectdate = "";//身份证有效期
	String rollid = "";//学籍辅号
	String attendant = "";//随班就读
	String farmer = "";//是否进城务工人员随迁子女
	String houseaid = "";//户口所在地行政区
	String did = "";//残疾类型
	String cwid = "";//入学方式
	String hard = "";//困难程度
	String addressnow = "";//现住地址
	String helpneed = "";//是否需要申请资助
	String dtance = "";//上下学距离
	String specialty = "";//特长
	String homepage = "";//主页地址
	String buydegree = "";//是否政府购买学位
	String soldierflag = "";//是否军人子女
	String source = "";//学生来源
	Set<BaseFamily> baseFamily ; //家庭成员
	
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