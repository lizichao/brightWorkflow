package cn.com.bright.yuexue.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.com.bright.yuexue.sell.preview.DocConverter;

/**
 * <p>Title:���̳ǵ�Ԥ��ת������</p>
 * <p>Description: ���̳ǵ�Ԥ��ת������</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author jiangyh
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author     time             version      desc</p>
 * <p> jiangyh    2015/03/30       1.0          build this moudle </p>
 *     
 */
public class SellPaperInfoConverterTask extends TimerTask {
	private Log log4j = new Log(this.getClass().toString());
	private String transType = (String) BrightComConfig.getConfiguration().getProperty("sell.trans_type");

	public void run() {
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" SellPaperInfoConverterTask run ----------");
		List sellPaperInfoList = getUnconvertedAttachment();
		for (int i = 0; i < sellPaperInfoList.size(); i++) {
			Element sellPaperInfoRec = (Element) sellPaperInfoList.get(i);
			String paper_info_id = sellPaperInfoRec.getChildText("paper_info_id");
			String paper_info_name = sellPaperInfoRec.getChildText("paper_info_name");
			String file_type = sellPaperInfoRec.getChildText("file_type");
			String file_name = sellPaperInfoRec.getChildText("file_name");
			String file_path = sellPaperInfoRec.getChildText("file_path");
			String price = sellPaperInfoRec.getChildText("price");
			String pref_price = sellPaperInfoRec.getChildText("pref_price");
			
			log4j.logInfo("ת����Paper_Info_Name " + paper_info_name);

			File docFile = new File(FileUtil.getWebPath() + file_path);
			if (!docFile.exists()) {
				setSellPaperInfo(paper_info_id, "80", "", "�ļ�������");
			} else {
				String targetType = "swf";
				String srcPath = FileUtil.getWebPath() + file_path;
				String folderPath = file_path.substring(0, file_path.lastIndexOf("/") + 1);
				String fileName = FileUtil.getFileName(srcPath);
				String fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));

				String destPath = folderPath + fileNameNoExt + "." + targetType;

				// �����txt�ļ�Ҫ��ת��UTF-8�ı��������odt�ļ����ϴ� Ҫ���Ϳ��ܻ�������
				if ("doc,docx,xls,xlsx,ppt,pptx,txt".indexOf(file_type.toLowerCase()) > -1) {
					try {
						DocConverter d = new DocConverter(srcPath);
						// ���pref_priceΪ0 ������� ��Ԥ��ȫ��
						if ((new Double(pref_price)).compareTo(new Double(0)) == 0) {
							d.conver(false);
						} else {
							d.conver(true);
						}
						setSellPaperInfo(paper_info_id, "30", destPath, "");
					} catch (Exception e) {
						String strMsg = e.getMessage();
						if (strMsg.length() > 500) {
							strMsg = strMsg.substring(0, 500);
						}
						setSellPaperInfo(paper_info_id, "90", "", e.getMessage());
					}
				} else if ("pdf".equalsIgnoreCase(file_type.toLowerCase())) {
					try {
						DocConverter d = new DocConverter(srcPath, true);
						// ���pref_priceΪ0 ������� ��Ԥ��ȫ��
						if ((new Double(pref_price)).compareTo(new Double(0)) == 0) {
							d.conver(false);
						} else {
							d.conver(true);
						}

						setSellPaperInfo(paper_info_id, "30", destPath, "");
					} catch (Exception e) {
						String strMsg = e.getMessage();
						if (strMsg.length() > 500) {
							strMsg = strMsg.substring(0, 500);
						}
						setSellPaperInfo(paper_info_id, "90", "", e.getMessage());
					}
				} else {
					// ������ֱ�ӽ�file_pathд�뵽preview_path�� ����Ҫת��
					setSellPaperInfo(paper_info_id, "30", file_path, "");
					// setAttachmentInfo(attachment_id, "70", "", "�ݲ�֧��ת�����ļ�����");
				}
			}
		}
	}

	/**
	 * ���ø�����Ϣ
	 * @param attachmentId
	 * @param transStatus
	 * @param accessPath
	 * @param transError
	 */
	public void setSellPaperInfo(String attachmentId, String transStatus, String accessPath, String transError) {
		StringBuffer updateSQL = new StringBuffer();
		updateSQL.append(" update sell_paper_info set trans_status = ?, preview_path = ?, trans_error = ?");
		updateSQL.append(" where paper_info_id = ?");

		ArrayList<Object> updateParam = new ArrayList<Object>();
		updateParam.add(transStatus);
		updateParam.add(accessPath);
		updateParam.add(transError);
		updateParam.add(attachmentId);

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(updateSQL.toString());
			pdao.setBindValues(updateParam);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���̳ǵ�Ԥ��ת��-�޸ĸ�����Ϣ]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}

	/**
	 * ��ȡδת������
	 * @return δת���ĸ����б�
	 */
	public List getUnconvertedAttachment() {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select * ");
		strSQL.append(" from sell_paper_info spi ");
		strSQL.append(" where spi.valid='Y' and spi.trans_status = '10' ");
		strSQL.append(" and '" + transType + "' like concat('%', spi.file_type, '%') ");

		PlatformDao pdao = new PlatformDao();
		try {
			pdao.setSql(strSQL.toString());
			Element result = pdao.executeQuerySql(20, 1);
			List ls = result.getChildren("Record");
	    	for (int i=0;i<ls.size();i++){
	    		Element rec = (Element)ls.get(i);
	    		setSellPaperInfo(rec.getChildText("paper_info_id"),"15","","");
	    	}			
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[���̳ǵ�Ԥ��ת��-ȡ��ת���ĸ����б�]" + e.getMessage());
			return null;
		} finally {
			pdao.releaseConnection();
		}
	}

}
