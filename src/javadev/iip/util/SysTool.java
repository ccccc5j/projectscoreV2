package javadev.iip.util;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 这个类里放置具有全局性的工具方法，工具方法都是静态方法，可以直接通过类名使用
 */
public class SysTool {
	public static final String[] nationality = { "汉族", "壮族", "满族", "回族", "苗族",
			"维吾尔族", "土家族", "彝族", "蒙古族", "藏族", "布依族", "侗族", "瑶族", "朝鲜族", "白族",
			"哈尼族", "哈萨克族", "黎族", "傣族", "畲族", "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族",
			"水族", "佤族", "纳西族", "羌族", "土族", "仫佬族", "锡伯族", "柯尔克孜族", "达斡尔族",
			"景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族", "阿昌族", "普米族", "鄂温克族", "怒族",
			"京族", "基诺族", "德昂族", "保安族", "俄罗斯族", "裕固族", "乌兹别克族", "门巴族", "鄂伦春族",
			"独龙族", "塔塔尔族", "赫哲族", "珞巴族" };
	public static final String TEMP_DIR = "uploadFile/tempFile/";// 临时文件存放文件夹
	public static final String STU_DIR = "uploadFile/stuInfoFile/";// 学生信息存放文件夹
	private final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();
	static {
		zoneNum.put(11, "北京");
		zoneNum.put(12, "天津");
		zoneNum.put(13, "河北");
		zoneNum.put(14, "山西");
		zoneNum.put(15, "内蒙古");
		zoneNum.put(21, "辽宁");
		zoneNum.put(22, "吉林");
		zoneNum.put(23, "黑龙江");
		zoneNum.put(31, "上海");
		zoneNum.put(32, "江苏");
		zoneNum.put(33, "浙江");
		zoneNum.put(34, "安徽");
		zoneNum.put(35, "福建");
		zoneNum.put(36, "江西");
		zoneNum.put(37, "山东");
		zoneNum.put(41, "河南");
		zoneNum.put(42, "湖北");
		zoneNum.put(43, "湖南");
		zoneNum.put(44, "广东");
		zoneNum.put(45, "广西");
		zoneNum.put(46, "海南");
		zoneNum.put(50, "重庆");
		zoneNum.put(51, "四川");
		zoneNum.put(52, "贵州");
		zoneNum.put(53, "云南");
		zoneNum.put(54, "西藏");
		zoneNum.put(61, "陕西");
		zoneNum.put(62, "甘肃");
		zoneNum.put(63, "青海");
		zoneNum.put(64, "宁夏");
		zoneNum.put(65, "新疆");
		zoneNum.put(71, "台湾");
		zoneNum.put(81, "香港");
		zoneNum.put(82, "澳门");
		zoneNum.put(91, "外国");
	}

	private final static int[] PARITYBIT = { '1', '0', 'X', '9', '8', '7', '6',
			'5', '4', '3', '2' };
	private final static int[] POWER_LIST = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7,
			9, 10, 5, 8, 4, 2 };

	/**
	 * 根据身份证号获取性别字符串
	 * 
	 * @param String
	 *            idCarStr,身份证号
	 * @return String,gender-性别 "女"、 "男"、null,null表示身份证号位数错误
	 * @author 陈建
	 * @version 创建时间：2013-1-22
	 */
	public static String getGenderFromID(String idCarStr) {
		String gender = "男";
		char sexChar = '0';
		int genderInt = 0;
		if (idCarStr.length() == 15 || idCarStr.length() == 18) {
			if (idCarStr.length() == 15) {
				sexChar = idCarStr.charAt(14);
			} else {
				sexChar = idCarStr.charAt(16);
			}
			genderInt = (int) sexChar - (int) '0';
			if (0 == genderInt % 2) {
				gender = "女";
			}
		}
		return gender;
	}

	/**
	 * 根据身份证号获取年龄
	 * 
	 * @param String
	 *            idCarStr,身份证号
	 * @return int,age-年龄 -1表示身份证号位数错误或者出生日期信息错误
	 * @author 陈建
	 * @version 创建时间：2013-1-22
	 */
	public static int getAgeFromID(String idCarStr) {
		int age = -1;
		String yearStr, monthStr, dayStr;
		if (idCarStr.length() == 15 || idCarStr.length() == 18) {
			if (idCarStr.length() == 15) {
				yearStr = "19" + idCarStr.substring(6, 8);
				monthStr = idCarStr.substring(8, 10);
				dayStr = idCarStr.substring(10, 12);
			} else {
				yearStr = idCarStr.substring(6, 10);
				monthStr = idCarStr.substring(10, 12);
				dayStr = idCarStr.substring(12, 14);
			}
			Calendar now = Calendar.getInstance();
			Calendar birthday = Calendar.getInstance();
			birthday.set(Integer.parseInt(yearStr),
					Integer.parseInt(monthStr) - 1, Integer.parseInt(dayStr));
			// 先比较年份
			age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
			// 再比较月日
			if ((now.get(Calendar.MONTH) < birthday.get(Calendar.MONTH))
					|| ((now.get(Calendar.MONTH) == birthday
							.get(Calendar.MONTH) && (now.get(Calendar.DATE) < birthday
							.get(Calendar.DATE)))))
				age--;
		}
		return age;
	}

	/**
	 * 根据数据库datetime类型生日信息获取年龄
	 * 
	 * @param java
	 *            .sql.Date date,生日信息
	 * @return int,age-年龄 -1表示错误
	 * @author 陈建
	 * @version 创建时间：2013-1-22
	 */
	public static int getAgeFromBirthday(Timestamp date) {
		int age = -1;
		Calendar now = Calendar.getInstance();

		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(date.getTime());
		// 先比较年份
		age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		// 再比较月日
		if ((now.get(Calendar.MONTH) < birthday.get(Calendar.MONTH))
				|| ((now.get(Calendar.MONTH) == birthday.get(Calendar.MONTH) && (now
						.get(Calendar.DATE) < birthday.get(Calendar.DATE)))))
			age--;
		return age;
	}

	/**
	 * 判断条码号是否准确
	 * 
	 * @param code
	 *            ,条码号
	 * @return boolean,true-正确，false表示错误
	 * @author 陈建
	 * @version 创建时间：2013-7-22
	 */
	public static boolean isRightCodeNum(String code) {
		if (code==null||"".equals(code.trim())||code.trim().length() != 12)
			return false;
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) < '0' || code.charAt(i) > '9')
				return false;
		}
		return true;
	}

	/**
	 * 判断短码是不是8位数字
	 * 
	 * @author fy
	 * 创建时间：2014-1-2
	 * @param code
	 * @return
	 */
	public static boolean isRightShortCodeNum(String code) {
		if (code.trim().length() != 8)
			return false;
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) < '0' || code.charAt(i) > '9')
				return false;
		}
		return true;
	}

	/**
	 * 判断身份号是否准确
	 * 
	 * @param id
	 *            ,条码号
	 * @return boolean,true-正确，false表示错误
	 * @author 陈建
	 * @version 创建时间：2013-8-28
	 */
	public static boolean isIdcard(String s) {
		if (s == null || (s.length() != 15 && s.length() != 18))
			return false;
		final char[] cs = s.toUpperCase().toCharArray();
		// 校验位数
		int power = 0;
		if (s.length() == 18) {
			for (int i = 0; i < cs.length; i++) {
				if (i == cs.length - 1 && cs[i] == 'X')
					break;// 最后一位可以 是X或x
				if (cs[i] < '0' || cs[i] > '9')
					return false;
				if (i < cs.length - 1) {
					power += (cs[i] - '0') * POWER_LIST[i];
				}
			}
		}

		// 校验区位码
		if (!zoneNum.containsKey(Integer.valueOf(s.substring(0, 2)))) {
			return false;
		}

		// 校验年份
		String year = s.length() == 15 ? "19" + s.substring(6, 8) : s
				.substring(6, 10);
		final int iyear = Integer.parseInt(year);
		if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR))
			return false;// 1900年的PASS，超过今年的PASS

		// 校验月份
		String month = s.length() == 15 ? s.substring(8, 10) : s.substring(10,
				12);
		final int imonth = Integer.parseInt(month);
		if (imonth < 1 || imonth > 12) {
			return false;
		}

		// 校验天数

		String day = s.length() == 15 ? s.substring(10, 12) : s.substring(12,
				14);
		final int iday = Integer.parseInt(day);
		if (iday < 1 || iday > 31)
			return false;

		// 校验一个合法的年月日
		if (!isValidDate(iyear, imonth, iday))
			return false;

		// 校验"校验码",15位身份证号无校验
		if (s.length() == 15)
			return true;
		return cs[cs.length - 1] == PARITYBIT[power % 11];
	}

	private static boolean isValidDate(int year, int mon, int day) {
		switch (mon) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: {
			if (day < 1 || day > 31) {
				return false;
			}
		}
			break;
		case 2: {
			if (isLeapYear(year)) {
				if (day > 29)
					return false;
			} else if (day > 28) {
				return false;
			}
		}
			break;
		case 4:
		case 6:
		case 9:
		case 11: {
			if (day < 1 || day > 30) {
				return false;
			}
		}
			break;
		default: {
			return false;
		}
		}
		return true;
	}

	private static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
	}

	/**
	 * 判断是否正的浮点数字符串
	 * 
	 * @param cString
	 *            floatNum
	 * @return boolean,true-正确，false表示错误
	 * @author 陈建
	 * @version 创建时间：2013-7-23
	 */
	public static boolean isPosFloat(String floatNum) {
		float fNum = 0;
		try {
			fNum = Float.parseFloat(floatNum);
		} catch (NumberFormatException e) {
			return false;
		}
		if (fNum < 0)
			return false;
		return true;
	}

	/**
	 * 删除过时的临时文件
	 * 
	 * @param String
	 *            imageFileDir,要删除文件的根目录 int min,删除多少分钟前的文件 String[]
	 *            fileTypes，要删除文件的后缀组成的字符串数组,为null则所有类型都删除
	 * @return void
	 * @author 陈建
	 * @version 创建时间：2013-7-23
	 */
	public static void deleteOldFile(String imageFileDir, int min,
			String[] fileTypes) {
		long modifiedTime = 0;
		long currentTime = 0;
		long interval = 1000 * 60 * min;// min分钟时间

		File imageDir = new File(imageFileDir);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		} else {
			// 删除5分钟之前的标识码图片文件
			File[] array = imageDir.listFiles();
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					if ((array[i].isFile())) {
						if (fileTypes != null) {
							for (int j = 0; j < fileTypes.length; j++) {
								if (array[i].getName()
										.lastIndexOf(fileTypes[j]) > 0) {
									modifiedTime = array[i].lastModified();
									currentTime = System.currentTimeMillis();
									if (currentTime - modifiedTime > interval) {
										array[i].delete();
									}
								}
							}
						} else {
							modifiedTime = array[i].lastModified();
							currentTime = System.currentTimeMillis();
							if (currentTime - modifiedTime > interval) {
								array[i].delete();
							}
						}

					}
				}
			}
		}
	}

	public static boolean isValidNationality(String nationalityName) {
		for (int i = 0; i < nationality.length; i++) {
			if (nationality[i].equals(nationalityName))
				return true;
		}
		return false;
	}

	public static boolean isValidPhoneNum(String phonenumber) {
		if (phonenumber.length() != 11)
			return false;
		for (int i = 0; i < phonenumber.length(); i++) {
			if ((phonenumber.charAt(i) < '0' || phonenumber.charAt(i) > '9'))
				return false;
		}
		return true;
	}

	public static boolean isValidAddress(String address) {
		// 特殊的村
		String[] specialVillages = { "江油市大康镇旧县村" };
		for (int i = 0; i < specialVillages.length; i++) {
			if ((address.length() - address.indexOf(specialVillages[i]) - specialVillages[i]
					.length()) > 1)
				return true;
		}

		if (address.length() < 10)
			return false;
		String[] keyWords = { "居委会", "村", "社区", "镇", "乡", "街道", "江油市", "江油",
				"涪城区", "涪城", "市", "区", "县", "四川省", "四川", "省" };
		for (int i = 0; i < keyWords.length; i++) {
			if (address.endsWith(keyWords[i]))
				return false;
		}

		String[] keyWords1 = { "居委会", "村", "社区" };
		String[] keyWords2 = { "镇", "乡", "街道" };
		String[] keyWords3 = { "江油市", "江油", "涪城区", "涪城", "市", "区", "县" };
		String[] keyWords4 = { "四川省", "四川", "省" };
		for (int i = 0; i < keyWords1.length; i++) {
			if ((address.length() - address.indexOf(keyWords1[i]) - keyWords1[i]
					.length()) < 2)
				return false;
		}
		for (int i = 0; i < keyWords2.length; i++) {
			if ((address.length() - address.indexOf(keyWords2[i]) - keyWords2[i]
					.length()) < 3)
				return false;
		}
		for (int i = 0; i < keyWords3.length; i++) {
			if ((address.length() - address.indexOf(keyWords3[i]) - keyWords3[i]
					.length()) < 5)
				return false;
		}
		for (int i = 0; i < keyWords4.length; i++) {
			if ((address.length() - address.indexOf(keyWords4[i]) - keyWords4[i]
					.length()) < 8)
				return false;
		}
		return true;
	}

	// 验证字符串中间是否有空格
	public static boolean haveSpace(String str) {
		StringTokenizer st = new StringTokenizer(str.trim(), "  　");
		if (st.countTokens() != 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 测试main方法
	 */
	public static void main(String[] args) {
		/*
		 * String s="张 三"; System.out.println(haveSpace(s)); s="张　三";
		 * System.out.println(haveSpace(s)); s="张三　";
		 * System.out.println(haveSpace(s));
		 */
		// TODO Auto-generated method stub
		String[] add = { "绵阳市涪城区玉皇镇2-9", "绵阳市游仙区游仙路198号", "绵阳市涪城区玉皇镇5——7",
				"绵阳市涪城区石洞乡4-5", "涪城区吴家镇五龙村三组", "绵阳市涪城区杨家镇3-7", "涪城区玉皇镇任家村二组",
				"涪城区杨家镇高碑垭村三组", "四川省绵阳市团阳寺村三组", "涪城区杨家镇高山寺村一组", "绵阳市涪城区玉皇8——1",
				"绵阳市涪城区玉皇镇3-5", "四川省绵阳市涪城区杨家镇高山寺五组", "绵阳市涪城区杨家镇中学",
				"绵阳市涪城区玉皇镇8-9", "四川省绵阳市涪城区杨家镇高山寺村九组", "绵阳市涪城区杨家镇中学",
				"四川省绵阳市涪城区石洞乡泡桐树村六组", "四川省绵阳市涪城区杨家镇万和村一组", "绵阳市涪城区石洞二村四社",
				"绵阳市涪城区杨家镇中学", "绵阳市涪城区玉皇5——6", "四川省绵阳市涪城区杨家镇高山寺六组",
				"四川省绵阳市涪城区杨家镇万和村六组", "四川省绵阳市油坊街72号", "绵阳市涪城区杨家镇柏林湾村六组",
				"中江县黄鹿镇青藕村1组", "绵阳市涪城区杨家镇高山寺村十一组", "绵阳市涪城区杨家镇中学",
				"四川省绵阳市涪城区杨家镇万和村一组", "绵阳市涪城区玉皇3——1", "涪城区杨家镇高山寺村八组",
				"中江县永太镇福村4组", "四川省绵阳市涪城区杨家镇高山寺村十一组", "四川省绵阳市涪城区杨家镇柏林湾一组",
				"四川省绵阳市涪城区石洞乡戴家林村四组", "绵阳市红星街115号2栋1单元7--14",
				"四川省绵阳市游仙区紫申小区28-114号", "四川省绵阳市杨家镇中学", "四川省绵阳市涪城区杨家镇罗汉寺村六组",
				"四川省中江县黄鹿镇阁楼村3组68号", "四川省绵阳市游仙区松垭镇方山寺村二组",
				"四川省绵阳市涪城区杨家镇川主庙村二组", "绵阳市涪城区石洞乡3-8", "绵阳市涪城区杨家镇3-8",
				"绵阳市涪城区玉皇3——6", "四川省绵阳市涪城区杨家镇10-9", "绵阳市涪城区玉皇镇3——6",
				"绵阳市涪城区玉皇镇3——6", "四川省绵阳市涪城区杨家镇柏林湾村五组", "绵阳市安县花荄镇红花16组",
				"绵阳市涪城区玉皇镇4-8", "绵阳市涪城区玉皇镇2-----11", "绵阳市涪城区石洞乡4-3",
				"绵阳市涪城区玉皇镇5——1", "广元市苍溪县唤马镇居委会", "四川省绵阳市涪城区石洞镇爱民村二组",
				"四川省绵阳市涪城区杨家镇兴隆村三组", "绵阳市涪城区石洞乡4-1", "四川省绵阳市涪城区杨家镇5-1",
				"绵阳市涪城区石洞乡4-5", "四川省绵阳市涪城区杨家镇10-4", "四川省中江县黄鹿2-4",
				"四川省绵阳市涪城区杨家镇高山寺村5组", "涪城区石洞镇泡桐树村六组", "四川省绵阳市涪城区杨家镇4-8",
				"四川省绵阳市涪城区杨家镇兴隆村四组", "四川省绵阳市涪城区杨家1-3", "绵阳市涪城区玉皇镇5-2",
				"绵阳市涪城区玉皇镇1-1", "四川绵阳涪城区玉皇镇居委会", "绵阳市涪城区玉皇镇1-4",
				"四川省绵阳市涪城区杨家镇1-3", "绵阳市涪城区玉皇镇5-6", "四川省绵阳市涪城区杨家镇8-6",
				"四川省绵阳市涪城区吴家镇中心桥村二组", "绵阳市涪城区石洞乡6-5", "绵阳市涪城区玉皇镇2-4",
				"绵阳市涪城区玉皇镇5——10", "绵阳市涪城区玉皇镇4-1", "四川绵阳市涪城区杨家镇居委会",
				"三台县立新镇二村七组", "四川省.绵阳市.涪城区杨家镇6-3", "四川省绵阳市涪城区杨家镇1-3",
				"绵阳市涪城区玉皇镇4-1", "内江市仙人路二段六号", "绵阳市涪城区杨家镇居委会",
				"四川省绵阳市涪城区杨家镇兴隆村三组", "绵阳市涪城区塘汛镇桃园村5社", "四川省绵阳市涪城区杨家镇1-4",
				"四川省绵阳市涪城区杨家镇1-5", "四川省绵阳市涪城区杨家镇1-4", "四川省绵阳市涪城区杨家镇居委会",
				"四川省绵阳市涪城区杨家镇1-10", "四川省绵阳市涪城区杨家镇11-4", "四川省绵阳市涪城区杨家镇9-6",
				"绵阳市涪城区玉皇镇6-2", "四川省绵阳市涪城区杨家镇8-3", "绵阳市涪城区玉皇镇3-6",
				"绵阳市涪城区御林一队", "四川省绵阳市涪城区柏林湾村五组", "绵阳市涪城区玉皇镇5-7",
				"绵阳市涪城区玉皇镇2-1", "绵阳市涪城区玉皇镇7-1", "四川省绵阳市杨家镇9-2", "绵阳市涪城区石洞乡4-6",
				"绵阳市涪城区玉皇镇5-1", "绵阳市涪城区石洞乡居委会", "绵阳市涪城区石洞乡5-2",
				"四川省绵阳市涪城区杨家镇9-4", "绵阳市涪城区石洞乡4-7", "四川省绵阳市石洞乡六村四组",
				"四川省绵阳市涪城区杨家镇11-1", "绵阳市涪城区玉皇镇3-2", "涪城区石洞乡五村二社",
				"涪城区杨家镇王家村二组", "中江县黄鹿镇铜音村4组", "绵阳市涪城区玉皇镇1-8", "绵阳市涪城区玉皇镇4-7",
				"绵阳市涪城区玉皇镇8-3", "绵阳市涪城区玉皇镇1-7", "四川省绵阳市涪城区杨家镇3-3",
				"绵阳市涪城区玉皇镇1-7", "绵阳市涪城区玉皇镇1-3", "绵阳市涪城区杨家3-3",
				"四川省绵阳市涪城区杨家镇9-5", "四川省绵阳市涪城区杨家镇万和村1组", "绵阳市涪城区玉皇6-4",
				"绵阳市涪城区玉皇镇6-4", "绵阳市涪城区玉皇镇6-6", "四川省绵阳市涪城区杨家镇高山寺村8组",
				"绵阳市涪城区石洞4-2", "绵阳市涪城区玉皇镇2-1", "涪城区杨家镇高山寺村十一组", "绵阳市涪城区玉皇镇2-2",
				"四川省绵阳市涪城区杨家镇8-5", "四川省绵阳市涪城区杨家镇7-10", "四川省绵阳市涪城区杨家镇6-3",
				"绵阳市涪城区玉皇镇4-6", "四川省绵阳市涪城区杨家镇4-5", "四川省绵阳市涪城区杨家镇12-2",
				"绵阳市涪城区石洞乡5-4", "四川省绵阳市涪城区杨家镇5-2", "绵阳市涪城区玉皇镇3-5",
				"四川省绵阳市涪城区杨家镇8-5", "绵阳市涪城区玉皇镇5-4", "绵阳市涪城区玉皇镇3-2",
				"绵阳市涪城区石洞乡4-5", "绵阳市涪城区玉皇镇5-4", "绵阳市涪城区石洞乡1-9",
				"四川省绵阳市涪城区杨家镇高山寺村一组", "四川省绵阳市涪城区杨家镇高山寺五组", "绵阳市涪城区杨家镇云林村八社",
				"四川省绵阳市涪城区杨家镇回龙寺村1组", "四川省绵阳市涪城区杨家镇7-4", "四川省绵阳市涪城区杨家镇回龙寺村3组",
				"四川省绵阳市涪城区万和村7-2", "四川省绵阳市涪城区杨家镇2-5", "四川省绵阳市涪城区杨家镇高山寺村6组",
				"绵阳市涪城区玉皇场镇", "四川省绵阳市涪城区杨家居委会", "四川省江油市厚坝镇犀牛村五组" };
		// for (int i = 0; i < add.length; i++) {
		// System.out.println(isValidAddress(add[i]) + "===" + add[i]);
		// }
		// String s = "四川省绵阳市涪城区杨家镇1-1";
		// isIdcard(s);
		System.out.println(isIdcard("510108198802030017"));
	}
}