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

public class StaticExcel {
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

	public StaticExcel() {
		needTitle = false;
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
			WritableFont fonttitle = new WritableFont(WritableFont.createFont("宋体"),14,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.BLACK );
			WritableCellFormat wcfF = new WritableCellFormat(fonttitle);
			wcfF.setAlignment(Alignment.CENTRE);
			
			WritableFont fonttitle1 = new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.BLACK );
			WritableCellFormat wcfF1 = new WritableCellFormat(fonttitle1);
			wcfF1.setAlignment(Alignment.CENTRE);//水平对齐设为居中
			wcfF1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直对齐设为居中
			wcfF1.setWrap(true);//自动换行


			
			WritableFont fonttitle2 = new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.BLACK );
			WritableCellFormat wcfF2 = new WritableCellFormat(fonttitle2);
			wcfF2.setAlignment(Alignment.CENTRE);
			wcfF2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直对齐设为居中
			
			WritableFont fonttitle3 = new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE, Colour.RED );
			WritableCellFormat wcfF3 = new WritableCellFormat(fonttitle3);
			wcfF.setAlignment(Alignment.CENTRE);
			
			sheet.mergeCells(0,0,3,0);
			Label label2=new Label(0,0,"市区县统计",wcfF);
			sheet.addCell(label2);
			//sheet.setRowView(0, 10);
			
			sheet.mergeCells(4,0,7,0);
			Label label3=new Label(4,0,"社区/乡镇统计",wcfF);
			sheet.addCell(label3);
			
			sheet.mergeCells(8,0,21,0);
			Label label1=new Label(8,0,"体检点统计",wcfF);
			sheet.addCell(label1);
			
			sheet.mergeCells(0,1,0,2);
			Label label4=new Label(0,1,"市区县",wcfF1);
			sheet.addCell(label4);
			
			sheet.mergeCells(1,1,1,2);
			Label label5=new Label(1,1,"常住人口总数",wcfF1);
			sheet.addCell(label5);
			
			sheet.mergeCells(2,1,2,2);
			Label label6=new Label(2,1,"完成百分比（按常住人口80%计）",wcfF1);
			sheet.addCell(label6);
			
			sheet.mergeCells(3,1,3,2);
			Label label7=new Label(3,1,"完成百分比（按常住人口60%计）",wcfF1);
			sheet.addCell(label7);
			
			sheet.mergeCells(4,1,4,2);
			Label label8=new Label(4,1,"社区/乡镇",wcfF1);
			sheet.addCell(label8);
			
			sheet.mergeCells(5,1,5,2);
			Label label9=new Label(5,1,"常住人口数",wcfF1);
			sheet.addCell(label9);
			
			sheet.mergeCells(6,1,6,2);
			Label label10=new Label(6,1,"完成百分比（按常住人口80%计）",wcfF1);
			sheet.addCell(label10);
			
			sheet.mergeCells(7,1,7,2);
			Label label11=new Label(7,1,"完成百分比（按常住人口60%计）",wcfF1);
			sheet.addCell(label11);
			
			sheet.mergeCells(8,1,8,2);
			Label label12=new Label(8,1,"体检点",wcfF1);
			sheet.addCell(label12);
			
			sheet.mergeCells(9,1,9,2);
			Label label13=new Label(9,1,"今日体检人数",wcfF1);
			sheet.addCell(label13);
			
			sheet.mergeCells(10,1,10,2);
			Label label14=new Label(10,1,"累计体检人数",wcfF1);
			sheet.addCell(label14);
			
			sheet.mergeCells(11,1,12,1);
			Label label15=new Label(11,1,"疑似肺结核",wcfF1);
			sheet.addCell(label15);
			Label label17=new Label(11,2,"人数",wcfF1);
			sheet.addCell(label17);
			Label label18=new Label(12,2,"疑似肺结核率（分母：累计体检人数）",wcfF1);
			sheet.addCell(label18);
			
			sheet.mergeCells(13,1,14,1);
			Label label16=new Label(13,1,"HBsAg阳性+HBsAb阴性",wcfF1);
			sheet.addCell(label16);
			Label label19=new Label(13,2,"人数",wcfF1);
			sheet.addCell(label19);
			Label label20=new Label(14,2,"HBsAg阳性率（分母：累计体检人数）",wcfF1);
			sheet.addCell(label20);
			
			sheet.mergeCells(15,1,15,2);
			Label label21=new Label(15,1,"HBsAg阳性+HBsAb阳性人数",wcfF1);
			sheet.addCell(label21);
			
			sheet.mergeCells(16,1,17,1);
			Label label22=new Label(16,1,"HBsAg阴性+HBsAb阴性",wcfF1);
			sheet.addCell(label22);
			Label label23=new Label(16,2,"人数",wcfF1);
			sheet.addCell(label23);
			Label label24=new Label(17,2,"HBsAg与HBsAb均阴性率（分母：累计体检人数）",wcfF1);
			sheet.addCell(label24);
			
			sheet.mergeCells(18,1,19,1);
			Label label25=new Label(18,1,"HBsAg阴性+HBsAb阳性",wcfF1);
			sheet.addCell(label25);
			Label label26=new Label(18,2,"人数",wcfF1);
			sheet.addCell(label26);
			Label label27=new Label(19,2,"HBsAb阳性率（分母：累计体检人数）",wcfF1);
			sheet.addCell(label27);
			
			sheet.mergeCells(20,1,20,2);
			Label label28=new Label(20,1,"HBsAg阳性+ALT升高人数",wcfF1);
			sheet.addCell(label28);
			
			sheet.mergeCells(21,1,21,2);
			Label label29=new Label(21,1,"HBsAg阳性+ALT 2倍升高人数",wcfF1);
			sheet.addCell(label29);
			
			
			Label label30=new Label(0,3,"绵阳市",wcfF2);
			sheet.addCell(label30);
			Label label33=new Label(1,3,"504399",wcfF2);
			sheet.addCell(label33);
			Label label36=new Label(4,3,"全部社区/乡镇",wcfF2);
			sheet.addCell(label36);
			Label label37=new Label(5,3,"504399",wcfF2);
			sheet.addCell(label37);
			
			Label label38=new Label(8,3,"全部体检点",wcfF2);
			sheet.addCell(label38);
			
			
			sheet.mergeCells(0,4,0,16);
			Label label31=new Label(0,4,"涪城区",wcfF2);
			sheet.addCell(label31);
			sheet.mergeCells(1,4,1,16);
			Label label34=new Label(1,4,"257921",wcfF2);
			sheet.addCell(label34);
			sheet.mergeCells(2,4,2,16);
			sheet.mergeCells(3,4,3,16);
			
			Label label39=new Label(4,4,"吴家镇",wcfF2);
			sheet.addCell(label39);
			Label label40=new Label(5,4,"16712",wcfF2);
			sheet.addCell(label40);
			Label label41=new Label(4,5,"丰谷镇",wcfF2);
			sheet.addCell(label41);
			Label label42=new Label(5,5,"12741",wcfF2);
			sheet.addCell(label42);
			Label label43=new Label(4,6,"青义镇",wcfF2);
			sheet.addCell(label43);
			Label label44=new Label(5,6,"21172",wcfF2);
			sheet.addCell(label44);
			Label label45=new Label(4,7,"金峰镇",wcfF2);
			sheet.addCell(label45);
			Label label46=new Label(5,7,"8441",wcfF2);
			sheet.addCell(label46);
			Label label47=new Label(4,8,"新皂镇",wcfF2);
			sheet.addCell(label47);
			Label label48=new Label(5,8,"16960",wcfF2);
			sheet.addCell(label48);
			Label label49=new Label(4,9,"杨家镇",wcfF2);
			sheet.addCell(label49);
			Label label50=new Label(5,9,"11791",wcfF2);
			sheet.addCell(label50);
			Label label51=new Label(4,10,"龙门镇",wcfF2);
			sheet.addCell(label51);
			Label label52=new Label(5,10,"13134",wcfF2);
			sheet.addCell(label52);
			Label label53=new Label(4,11,"石塘镇",wcfF2);
			sheet.addCell(label53);
			Label label54=new Label(5,11,"40369",wcfF2);
			sheet.addCell(label54);
			//sheet.mergeCells(4,12,4,14);
			Label label55=new Label(4,12,"西山社区",wcfF2);
			sheet.addCell(label55);
			Label labe278=new Label(4,13,"城北社区",wcfF2);
			sheet.addCell(labe278);
			Label labe279=new Label(4,14,"朝阳社区",wcfF2);
			sheet.addCell(labe279);
			Label labe280=new Label(4,16,"人民医院",wcfF2);
			sheet.addCell(labe280);
			Label labe281=new Label(4,15,"高新社区",wcfF2);
			sheet.addCell(labe281);
			//sheet.mergeCells(5,12,5,14);
			//Label label56=new Label(5,12,"95912",wcfF2);
			//sheet.addCell(label56);
			
			
			
			sheet.mergeCells(0,17,0,27);
			Label label32=new Label(0,17,"江油市",wcfF2);
			sheet.addCell(label32);
			sheet.mergeCells(1,17,1,27);
			Label label35=new Label(1,17,"266478",wcfF2);
			sheet.addCell(label35);
			sheet.mergeCells(2,17,2,27);
			sheet.mergeCells(3,17,3,27);
			
			Label label63=new Label(4,17,"武都镇",wcfF2);
			sheet.addCell(label63);
			Label label64=new Label(5,17,"36350",wcfF2);
			sheet.addCell(label64);
			Label label66=new Label(4,18,"青莲镇",wcfF2);
			sheet.addCell(label66);
			Label label65=new Label(5,18,"14676",wcfF2);
			sheet.addCell(label65);
			
			Label label60=new Label(4,19,"九岭镇",wcfF2);
			sheet.addCell(label60);
			Label label59=new Label(5,19,"15655",wcfF2);
			sheet.addCell(label59);
			
			Label label58=new Label(4,20,"彰明镇",wcfF2);
			sheet.addCell(label58);
			Label label57=new Label(5,20,"14640",wcfF2);
			sheet.addCell(label57);
			
			Label label61=new Label(4,21,"二郎庙镇",wcfF2);
			sheet.addCell(label61);
		
			Label label62=new Label(5,21,"23853",wcfF2);
			sheet.addCell(label62);
			//sheet.mergeCells(4,20,4,22);
			Label label67=new Label(4,22,"新安镇",wcfF2);
			sheet.addCell(label67);
			//sheet.mergeCells(5,20,5,22);
			Label label68=new Label(5,22,"13569",wcfF2);
			sheet.addCell(label68);
			
			Label label72=new Label(4,23,"厚坝镇",wcfF2);
			sheet.addCell(label72);
			Label label73=new Label(5,23,"20135",wcfF2);
			sheet.addCell(label73);
			//sheet.mergeCells(4,23,4,24);
			Label label69=new Label(4,24,"重华镇",wcfF2);
			sheet.addCell(label69);
			//sheet.mergeCells(5,23,5,24);
			Label label70=new Label(5,24,"11688",wcfF2);
			sheet.addCell(label70);
			
			sheet.mergeCells(4,25,4,27);
			
			Label label71=new Label(4,25,"三合镇",wcfF2);
			sheet.addCell(label71);
			sheet.mergeCells(5,25,5,27);
			Label label76=new Label(5,25,"95912",wcfF2);
			sheet.addCell(label76);
			
			
			// 生成标题
			//System.out.println(dataList.get(0).getClass());
			List ls = (List) dataList.get(0) ;
			//System.out.println(ls.get(0).getClass());
			//System.out.println(ls.get(0));
			//System.out.println(ls.get(48));
			Map firstItem = new HashMap() ;
			firstItem = (Map) ls.get(0);
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
			/*for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				for (int i = 0; i < keys.length; i++) {
					// 设置数据行位置
					int lineNum = j + 4;
					
					Object value = item.get(keys[i]);
					Label label = new Label(i+8, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				}
			}*/
			
			
			/*	Map item10 = (Map) ls.get(0);
				
					// 体检点完成率
					
					Object value199 = item10.get("today_exam");
					Label label199 = new Label(6, 4, getConvertValue(
							(String) keys[0], value199 == null ? "" : value199
									.toString()), dataFormat);
					sheet.addCell(label199);
				
			}*/
			for (int j = 0; j < 24; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 体检点
					int lineNum = j + 4;
					
					Object value = item.get("task");
					Label label = new Label(i+6, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 24; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 体检点
					int lineNum = j + 4;
					
					Object value = item.get("org_name");
					Label label = new Label(i+8, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 24; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 今日体检量
					int lineNum = j + 4;
					
					Object value = item.get("today_exam");
					Label label = new Label(i+9, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 24; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 累计体检量
					int lineNum = j + 4;
					
					Object value = item.get("exam");
					Label label = new Label(i+10, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 疑似肺结核人数
					int lineNum = j + 4;
					
					Object value = item.get("tb");
					Label label = new Label(i+11, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 疑似肺结核百分比
					int lineNum = j + 4;
					
					Object value = item.get("tb_percent");
					Label label = new Label(i+12, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 阳阴
					int lineNum = j + 4;
					
					Object value = item.get("org_hb_gupbdown");
					Label label = new Label(i+13, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 阳阴百分比
					int lineNum = j + 4;
					
					Object value = item.get("hb_gupbdown_per");
					Label label = new Label(i+14, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 乙肝双阳性人数
					int lineNum = j + 4;
					
					Object value = item.get("org_hb_bothup");
					Label label = new Label(i+15, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 双阴性
					int lineNum = j + 4;
					
					Object value = item.get("hbs");
					Label label = new Label(i+16, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 双阴性百分比
					int lineNum = j + 4;
					
					Object value = item.get("hbs_percent");
					Label label = new Label(i+17, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 阴阳
					int lineNum = j + 4;
					
					Object value = item.get("org_hb_gdownbup");
					Label label = new Label(i+18, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 阴阳百分比
					int lineNum = j + 4;
					
					Object value = item.get("hb_gdownbup_per");
					Label label = new Label(i+19, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					//阳性+ALT升高
					int lineNum = j + 4;
					
					Object value = item.get("hbup");
					Label label = new Label(i+20, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			for (int j = 0; j < 25; j++) {
				Map item = (Map) ls.get(j);
				int i=0;
					// 阳性+ALT2倍升高
					int lineNum = j + 4;
					
					Object value = item.get("hbup2");
					Label label = new Label(i+21, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
				
			}
			
				Map item = (Map) ls.get(48);
				int i=0;
					// 绵阳市总数统计
					int lineNum =  3;
					
					Object value = item.get("total_tb");
					Label label = new Label(11, lineNum, getConvertValue(
							(String) keys[i], value == null ? "" : value
									.toString()), dataFormat);
					sheet.addCell(label);
					
					Object value203 = item.get("total_tb_per");
					Label label203 = new Label(12, lineNum, getConvertValue(
							(String) keys[i], value203 == null ? "" : value203
									.toString()), dataFormat);
					sheet.addCell(label203);
					
					Object value204 = item.get("total_hb_gupbdown");
					Label label204 = new Label(13, lineNum, getConvertValue(
							(String) keys[i], value204 == null ? "" : value204
									.toString()), dataFormat);
					sheet.addCell(label204);
					Object value205 = item.get("total_hb_gupbdown_per");
					Label label205 = new Label(14, lineNum, getConvertValue(
							(String) keys[i], value205 == null ? "" : value205
									.toString()), dataFormat);
					sheet.addCell(label205);
					
					Object value206 = item.get("total_hb_bothup");
					Label label206 = new Label(15, lineNum, getConvertValue(
							(String) keys[i], value206 == null ? "" : value206
									.toString()), dataFormat);
					sheet.addCell(label206);
					
					Object value207 = item.get("total_hbs");
					Label label207 = new Label(16, lineNum, getConvertValue(
							(String) keys[i], value207 == null ? "" : value207
									.toString()), dataFormat);
					sheet.addCell(label207);
					
					Object value208 = item.get("total_hbs_per");
					Label label208 = new Label(17, lineNum, getConvertValue(
							(String) keys[i], value208 == null ? "" : value208
									.toString()), dataFormat);
					sheet.addCell(label208);
					
					Object value209 = item.get("total_hb_gdownbup");//阴阳
					Label label209 = new Label(18, lineNum, getConvertValue(
							(String) keys[i], value209 == null ? "" : value209
									.toString()), dataFormat);
					sheet.addCell(label209);
					Object value210 = item.get("total_hb_gdownbup_per");//阴阳百分比
					Label label210 = new Label(19, lineNum, getConvertValue(
							(String) keys[i], value210 == null ? "" : value210
									.toString()), dataFormat);
					sheet.addCell(label210);
					
					Object value211 = item.get("total_hbup");//up1
					Label label211 = new Label(20, lineNum, getConvertValue(
							(String) keys[i], value211 == null ? "" : value211
									.toString()), dataFormat);
					sheet.addCell(label211);
					Object value212 = item.get("total_hb2up");//up2
					Label label212 = new Label(21, lineNum, getConvertValue(
							(String) keys[i], value212 == null ? "" : value212
									.toString()), dataFormat);
					sheet.addCell(label212);
					
					
					Object value202 = item.get("total_today_exams");
					Label label202 = new Label(9, 3, getConvertValue(
							(String) keys[0], value202 == null ? "" : value202
									.toString()), dataFormat);
					sheet.addCell(label202);
					Object value201 = item.get("total_exams");
					Label label201 = new Label(10, 3, getConvertValue(
							(String) keys[0], value201 == null ? "" : value201
									.toString()), dataFormat);
					sheet.addCell(label201);
					
					Object value214 = item.get("total_80_per");
					Label label214 = new Label(6, 3, getConvertValue(
							(String) keys[0], value214 == null ? "" : value214
									.toString()), dataFormat);
					sheet.addCell(label214);
					Object value215 = item.get("total_60_per");
					Label label215 = new Label(7, 3, getConvertValue(
							(String) keys[0], value215 == null ? "" : value215
									.toString()), dataFormat);
					sheet.addCell(label215);
					Label label216 = new Label(3, 3, getConvertValue(
							(String) keys[0], value215 == null ? "" : value215
									.toString()), dataFormat);
					sheet.addCell(label216);
					Label label217 = new Label(2, 3, getConvertValue(
							(String) keys[0], value214 == null ? "" : value214
									.toString()), dataFormat);
					sheet.addCell(label217);
					
					Label label278=new Label(1,30,"截止日期：",wcfF3);
					sheet.addCell(label278);
					Object value216 = item.get("date");
					Label label279=new Label(2,30,value216.toString(),wcfF3);
					sheet.addCell(label279);
					sheet.mergeCells(1,31,10,31);
					Label label277=new Label(1,31,"注：表格中统计的数据均为通过审核的部分，未包括已上传未审核部分。",wcfF3);
					sheet.addCell(label277);
			
			book.write();
			book.close();
		} catch (Exception e) {
			log.error("Create Excel Failed: " + e);
			e.printStackTrace();
		}
	}

}