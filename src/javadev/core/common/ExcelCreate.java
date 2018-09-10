package javadev.core.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExcelCreate {
	private static final Log log = LogFactory.getLog(ExcelHelper.class);

	public static int FILE = 0;

	public static int STREAM = 1;

	private String fileName;

	private String sheetName;

	private Map titleMap;

	private Map convertValueMap;

	private List dataList;

	private boolean needTitle;

	private OutputStream outStream;

	private HttpServletResponse response;

	private String font;

	private int fontSize;
    
	private String org_name;
	private String checkup_time;

	public String getCheckup_time() {
		return checkup_time;
	}

	public void setCheckup_time(String checkup_time) {
		this.checkup_time = checkup_time;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
		this.response.setContentType("application/vnd.ms-excel");
		try {
			this.outStream = response.getOutputStream();
		} catch (IOException e) {
			log.debug("get outputStream from response error: " + e);
		}
	}

	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setNeedTitle(boolean needTitle) {
		this.needTitle = needTitle;
	}

	public ExcelCreate() {
		needTitle = true;
		titleMap = new HashMap();
		convertValueMap = new HashMap();
		sheetName = "数据表";
		font = "宋体";
		fontSize = 11;
	}

	/**
	 * 设置标题字段名称（用于把标题中英文转为中文）
	 */
	public void setTitleName(String keyCode, String keyName) {
		titleMap.put(keyCode, keyName);
	}

	/**
	 * 设置结果中需要转换的内容
	 */
	public void addConvertValue(String code, String key, String value) {
		Map codeMap = (Map) convertValueMap.get(code);
		if (codeMap == null) {
			codeMap = new HashMap();
			convertValueMap.put(code, codeMap);
		}
		codeMap.put(key, value);
	}

	/**
	 * 获取转换后的内容
	 */
	private String getConvertValue(String code, String key) {
		Map codeMap = (Map) convertValueMap.get(code);
		if (codeMap != null) {
			String value = (String) codeMap.get(key);
			if (value != null) {
				return value;
			}
		}
		return key;
	}

	public void createExcel(int type) {
		try {
			WritableWorkbook book = null;
			if (type == FILE) {
				book = Workbook.createWorkbook(new File(fileName));
			} else {
				book = Workbook.createWorkbook(outStream);
				if (fileName != null) {
					this.response.addHeader("Content-disposition",
							"filename=\""
									+ URLEncoder.encode(fileName, "UTF-8")
									+ "\"");
				}
			}
			WritableSheet sheet = book.createSheet(sheetName, 0);
			if (dataList == null || dataList.size() == 0) {
				book.write();
				book.close();
				return;
			}
			
			
			/*设置标题的格式  比如字体的大小*/
			WritableFont fonttitle = new WritableFont(WritableFont.createFont("宋体"),14,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.BLACK );
			WritableCellFormat wcfF = new WritableCellFormat(fonttitle);
			wcfF.setAlignment(Alignment.CENTRE);
			
			WritableFont fonttitle1 = new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.RED );
			WritableCellFormat wcfF1 = new WritableCellFormat(fonttitle1);
			wcfF.setAlignment(Alignment.CENTRE);
			
			sheet.mergeCells(0,0,8,0);
			Label label2=new Label(0,0,"示范区健康体检报告发放登记表",wcfF);
			sheet.addCell(label2);
			//sheet.setRowView(0, 10);
			
			Label label3=new Label(0,1,"体检点名称：",wcfF1);
			sheet.addCell(label3);
			
			Label label4=new Label(1,1,org_name,wcfF1);
			sheet.addCell(label4);
			
			Label label5=new Label(2,1,"体检日期：",wcfF1);
			sheet.addCell(label5);
			
			Label label6=new Label(3,1,checkup_time,wcfF1);
			sheet.addCell(label6);
			
			
			
			// 生成标题
			Map firstItem = (Map) dataList.get(0);
			Object[] keys = firstItem.keySet().toArray();
			if (needTitle) {
				WritableCellFormat titleFormat = new WritableCellFormat(
						new WritableFont(WritableFont.createFont(font),
								fontSize, WritableFont.BOLD));
				for (int i = 0; i < keys.length; i++) {
					String tempKey = (String) titleMap.get(keys[i]);
					Label label = new Label(i, 2,
							tempKey == null ? (String) keys[i] : tempKey,
							titleFormat);
					sheet.addCell(label);
				}
			}
			WritableCellFormat dataFormat = new WritableCellFormat(
					new WritableFont(WritableFont.createFont(font), fontSize));
			// 生成数据行
			for (int j = 0; j < dataList.size(); j++) {
				Map item = (Map) dataList.get(j);
				for (int i = 0; i < keys.length; i++) {
					// 设置数据行位置
					int lineNum = j + 1;
					if (!needTitle) {
						lineNum = j;
					}
					Object value = item.get(keys[i]);
					Label label = new Label(i, lineNum+2, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				}
			}
			book.write();
			book.close();
		} catch (Exception e) {
			log.error("Create Excel Failed: " + e);
		}
	}

}