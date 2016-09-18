package cn.com.bright.yuexue.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class PraxesBean {
	public String title = "";
	public String type = "";
	public String answer = "";
	public String desc = "";
	public int num = 0;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answerFormat(answer);
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public String answerFormat(String answer){
		String option = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		answer = answer.replaceAll("<[/]?(p|P|br|BR|[ovwxpOVWXP]:\\w+)[^>]*?>","").trim();
		if (StringUtils.isEmpty(answer) || answer.startsWith("�ʴ�") || answer.startsWith("�ʴ���")) {
			this.setType("90"); //�ʴ���
		} else {
			if(answer.length()==1){
				if(StringUtils.isEmpty(answer.replaceAll("(��|��)", ""))){
					this.setType("10"); //�ж�
					answer = answer.equals("��") ? "Y":"N";
				} else {
					String answerTemp = answer.replaceAll("(\\s|,|��|��|\\.|��|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)" ,"");
					if(StringUtils.isEmpty(answerTemp)){
						answer = answer.toUpperCase();
						this.setType("20"); //��ѡ
						int num = option.indexOf(answer)+1;
						this.setNum(num>4 ? num:4);
					} else {
						this.setType("40"); //���
					}
				}
			} else {
				String answerTemp = answer.replaceAll("(\\s|,|��|��|\\.|��|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)" ,"");
				if(StringUtils.isEmpty(answerTemp)){
					answerTemp = answer.replaceAll("(\\s|,|��|��|\\.|��)" ,"").toUpperCase(); //��ֹ�������û���ö��Ż������ַ�����
					
					int max = 4;
					List<String> l = new ArrayList<String>();
					for(char c : answerTemp.toCharArray()){
						int num = option.indexOf(String.valueOf(c))+1;
						max = num>max ? num:max;
						l.add(String.valueOf(c));
					}
					this.setNum(max); //����ѡ������
					
					HashSet<String> h = new HashSet<String>(l);
					if(l.size() > h.size()){ //�����ظ�ֵ
						this.setType("40"); //���
					} else {
						this.setType("30"); //��ѡ
						Collections.sort(l); //�Զ�����
						answer = StringUtils.join(l, ",");
					}
				} else {
					this.setType("40"); //���
				}
			}
		}
		
		if("40".equals(this.getType())){ //���
			if(StringUtils.isNotEmpty(this.title)){
				this.setTitle(this.title.replaceAll("(_){3}\\1+", "()"));
				this.setTitle(this.title.replaceAll("��\\s*��", "()"));
			}
		} else {
			if("20".equals(this.getType()) || "30".equals(this.getType())) {
				titleFormat("option");
			} else {
				titleFormat("number");
			}
		}
		return answer;
	}
	
	/*
	 * ��ʽ��word�ĵ�������ɵ����ֻ���ĸ
	 */
	protected void titleFormat(String type){
		String tempTitle = this.getTitle();
		String regular = "<LI><p>(.*?)[^<LI>]*";
        Pattern p = Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(tempTitle);
		int num = 0;
		if("number".equals(type)){
	        while (m.find()) {
	        	String src = m.group();
	        	tempTitle = tempTitle.replace(src, src.replace("<LI><p>", "<p>"+ (++num) +"."));
	        }
		} else {
			String option = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
			String[] options = option.split(",");
	        while (m.find()) {
	        	String src = m.group();
	        	tempTitle = tempTitle.replace(src, src.replace("<LI><p>", "<p>"+options[num++]+"��"));
	        }
	        if(this.getNum() < num){
	        	this.setNum(num);
	        }
		}
		tempTitle = tempTitle.replaceAll("<[/]?(OL|ol:\\w+)[^>]*?>","");
		this.setTitle(tempTitle);
	}
}
