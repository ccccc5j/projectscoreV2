package test;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {
	private static final String SQL_GET_ALL = "SELECT p.DABH,date_format(p.CHECKUP_DATE ,'%Y-%m-%d %H:%i:%S'),"
			+ "s.SCHOOL_ID,a.AREA_CODE,a.AREA_NAME,"
			+ "c.SCHOOL_ID,c.CLASS_NAME, p.CLASS_ID "
			+ "FROM personal_info p, area a, SCHOOL s, CLASS c "
			+ "WHERE p.checkup_way =2 "
			+ "AND p.is_delete =1 "
			+ "AND p.RES_AREA_ID = a.AREA_ID "
			+ "AND a.AREA_CODE = s.AREA_CODE "
			+ "AND p.CLASS_ID = c.CLASS_ID  "
			// + "AND s.SCHOOL_ID<>c.SCHOOL_ID AND p.DABH='112309220329'";
			+ "AND s.SCHOOL_ID<>c.SCHOOL_ID order by p.DABH  limit 0,10000";
	private final static String sqlFindClass = "SELECT count(CLASS_ID) FROM CLASS "
			+ " WHERE SCHOOL_ID=?  and CLASS_NAME LIKE ?";
	private final static String sqlGetClassId = "SELECT CLASS_ID FROM CLASS "
			+ " WHERE SCHOOL_ID=? and CLASS_NAME LIKE  ?";

	// private final static String sqlGetFileId =
	// "SELECT ORG_ID,OCR_FILE_ID FROM checkup_ocr_result WHERE daBH LIKE ? AND PAGE_NUM=?";
	private final static String sqlGetFileId = "SELECT FILE_ID,ORG_ID,FILE_PATH,FILE_NAME,"
			+ "date_format(FILE_TIME ,'%Y-%m-%d %H:%i:%S') FILE_NAME,STATE  FROM "
			+ "checkup_upload_file WHERE FILE_ID=?";
	// private final static String sqlGetFileId2 =
	// "SELECT a.OCR_FILE_ID FROM `checkup_ocr_result` a, `checkup_ocr_result` b WHERE a.OCR_FILE_ID=b.OCR_FILE_ID and a.OCR_ID<>b.OCR_ID";
	// private final static String sqlGetFileId2 =
	// "SELECT a.OCR_FILE_ID FROM `checkup_ocr_result` a inner join `checkup_ocr_result` b  on   a.OCR_FILE_ID=b.OCR_FILE_ID and a.OCR_ID<>b.OCR_ID";
	private final static String sqlGetFileId3 = "SELECT count(OCR_FILE_ID),date_format(max(OCR_TIME) ,'%Y-%m-%d %H:%i:%S'), "
			+ "date_format(max(CHECK_DATE),'%Y-%m-%d %H:%i:%S')  FROM `checkup_ocr_result` where OCR_FILE_ID=?";
	private final static String sqlGetFileId4 = "SELECT count(OCR_FILE_ID),date_format(max(OCR_TIME) ,'%Y-%m-%d %H:%i:%S') "
			+ " FROM `checkup_ocr_result` where OCR_FILE_ID=?";
	private final static String sqlGetFileId5 = "SELECT DABH,count(DABH),date_format(max(OCR_TIME) ,'%Y-%m-%d %H:%i:%S'), "
			+ " date_format(max(CHECK_DATE),'%Y-%m-%d %H:%i:%S')  FROM `checkup_ocr_result`"
			+ " where PAGE_NUM=? and DABH=?";

	// 各区域体检人数，排除 测试账号所属的区域和被删除的人
	private final static String sqlGetHostNum = "SELECT RES_AREA_ID,count(*)  FROM personal_info   WHERE "
			+ " CHECKUP_ORG_ID<>33 and is_delete=0  group  by RES_AREA_ID";
	// 各区域体检人数，排除 测试账号所属的区域和被删除的人
	private final static String sqlGetHostNum2 = "SELECT p.RES_AREA_ID,a.AREA_NAME,a.AREA_CODE,count(p.DABH)  FROM personal_info p,  AREA a"
			+ " WHERE  CHECKUP_ORG_ID<>33 and is_delete=0  and p.RES_AREA_ID = a.AREA_ID group  by RES_AREA_ID";

	// 各体检点参加体检人数并且根据区域号判断属于该区域的人数(非学生)
	private final static String sqlGetHostNum3 = "SELECT CHECKUP_ORG_ID,count(DABH)  FROM personal_info p,area  a WHERE "
			+ " p.RES_AREA_ID = a.AREA_ID  and  CHECKUP_ORG_ID=? and a.area_code like ?"
			+ "and is_delete=0 and checkup_way<>2";
	// 各体检点参加体检人数并且根据区域号判断属于该区域并已打报告的人数的人数(非学生)
	private final static String sqlGetHostNum4 = "SELECT CHECKUP_ORG_ID,count(DABH)  FROM personal_info p,area  a WHERE "
			+ " p.RES_AREA_ID = a.AREA_ID  and  CHECKUP_ORG_ID=? and a.area_code like ?"
			+ "and is_delete=0 and checkup_way<>2  and REPORT_PRINTED=1";

	// 根据区域号判断不属于体检点单根据常住地址包含体检点负责区域关键字的人数(非学生)
	private final static String sqlGetHostNum5 = "SELECT CHECKUP_ORG_ID,count(DABH)  FROM personal_info p,area  a WHERE "
			+ " p.RES_AREA_ID = a.AREA_ID  and  CHECKUP_ORG_ID=? and a.area_code not like ? and decode(RES_ADDRESS_DETAIL,'scu') like ?"
			+ "and is_delete=0 and checkup_way<>2";
	// 根据区域号判断不属于体检点单根据常住地址包含体检点负责区域关键字的并已打报告的人数(非学生)

	private final static String sqlGetHostNum6 = "SELECT CHECKUP_ORG_ID,count(DABH)  FROM personal_info p,area  a WHERE "
			+ " p.RES_AREA_ID = a.AREA_ID  and  CHECKUP_ORG_ID=? and a.area_code not like ? and decode(RES_ADDRESS_DETAIL,'scu')  like ?"
			+ "and is_delete=0 and checkup_way<>2  and REPORT_PRINTED=1";

	// 不是体检点体检的学生，根据区域号判断不属于体检点，根据常住地址包含体检点负责区域关键字的人数(非学生)
	private final static String sqlGetHostNum7 = "SELECT CHECKUP_ORG_ID,DABH,decode(p.CITIZEN_NAME,'scu') ,"
			+ "decode(p.RES_ADDRESS_DETAIL,'scu')  "
			+ "  FROM personal_info p,area  a WHERE "
			+ " p.RES_AREA_ID = a.AREA_ID  and  CHECKUP_ORG_ID=? and a.area_code not like ? and "
			+ "decode(p.RES_ADDRESS_DETAIL,'scu') not like ?"
			+ "and is_delete=0 and checkup_way<>2";

	private final static String sqlGetHBFaulSchool = "select  p.DABH,decode(p.RES_ADDRESS_DETAIL,'scu'),o.ORG_NAME,"
			+ "a.AREA_NAME,p.CLASS_ID,c.CLASS_NAME  from personal_info p "
			+ "INNER JOIN class c on p.CLASS_ID=c.CLASS_ID  "
			+ "INNER JOIN school s on c.SCHOOL_ID=s.SCHOOL_ID  "
			+ "INNER JOIN organization o on o.ORG_ID=s.ORG_ID  "
			+ "INNER JOIN area a on a.AREA_CODE=s.AREA_CODE where  p.checkup_way=2 and p.HBSAG='阴性' and p.HBSAB='阴性' and "
			+ "s.AREA_CODE in(510781043074,510781043100,510781043099,510781043083,510781043118,510781043116,510781043120,510781043123,510781043122 "
			+ ")order by p.DABH;";

	//
	// OCR_TIME CHECK_DATE 59040
	// 10:02:52 16:24:22
	// FILE_ID,ORG_ID,FILE_PATH, FILE_NAME ,FILE_TIME ,STATE
	public static void main(String[] args) {
		// dealCorrectFileId2();
		// getAreaNum();
		getAreaDetail();

	}

	// 2013年10月27日处理出错的文件ID
	private static void getAreaDetail() {
		int orgIds[] = { 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
				25, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36 };
		String areaCodes[] = { "510703015%", "510703009%", "510703012%",
				"510703017%", "510703019%", "510703016%", "510703013%",
				"510703014%", "510703023%", "510703002%", "510781017%",
				"510781007%", "510781008%", "510781014%", "510781034%",
				"510781019%", "510781032%", "510781030%", "510781005%",
				"510781005%", "510781005%", "510703023%", "510703023%",
				"510703002%" };
		String keys[] = { "%吴家%", "%丰谷%", "%青义%", "%金峰%", "%新皂%", "%杨家%",
				"%龙门%", "%石塘%", "%城郊%", "%城北%", "%武都%", "%青莲%", "%九岭%", "%彰明%",
				"%二郎庙%", "%新安%", "%厚坝%", "%重华%", "%三合%", "%三合%", "%三合%",
				"%城郊%", "%城郊%", "%城北%" };

		// 数据库用户名
		String userName = "crbzx";
		// 密码
		String userPasswd = "hxcrb2013";
		// 数据库名 characterEncoding=utf-8
		String url = "jdbc:mysql://crb.xbitworld.com:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GBK&zeroDateTimeBehavior=convertToNull";
		// 数据库用户名
		/*
		 * String userName = "root"; // 密码 String userPasswd = "qweasd123"; //
		 * 数据库名 characterEncoding=utf-8 String url =
		 * "jdbc:mysql://crb.scu.edu.cn:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull"
		 * ;
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url, userName,
					userPasswd);
			PreparedStatement pstmt = connection
					.prepareStatement(sqlGetHBFaulSchool);
			int orgId = 0;
			String dabh = "";
			String address = "";
			String orgName = "";
			String areaName = "";
			String classID = "";
			String className = "";
			for (int i = 0; i < 6; i++) {
				// for (int i = 117511; i <= 311795; i++) {
				// p.DABH,decode(p.RES_ADDRESS_DETAIL,'scu'),o.ORG_NAME,a.AREA_NAME,p.CLASS_ID,c.CLASS_NAME
				/*
				 * pstmt.setInt(1, orgIds[i]); pstmt.setString(2, areaCodes[i]);
				 * pstmt.setString(3, keys[i]);
				 */
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					// FILE_ID,ORG_ID,FILE_PATH, FILE_NAME ,FILE_TIME ,STATE
					dabh = rs.getString(1);
					address = decry(rs.getString(2));
					orgName = rs.getString(3);
					areaName = rs.getString(4);
					classID = rs.getString(5);
					className = rs.getString(6);

					// toltal += num;
					System.out.print(dabh + "\t" + address + "\t" + orgName
							+ "\t" + areaName + "\t" + classID + "\t"
							+ className + "\t"
							// + decry(name) + "\t" + decry(areaName)
							+ "\n");
					// }
				}
				rs.close();
			}
			pstmt.close();
			connection.close();
			System.out.print("查询完毕！共查询到：");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String decry(String olString) {
		String newStr = "";
		byte[] name = olString.getBytes();
		if (name != null) {
			try {
				newStr = new String(name, "gbk");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newStr;
	}

	// 2013年10月27日处理出错的文件ID
	private static void getAreaNum() {
		int orgIds[] = { 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
				25, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36 };
		String areaCodes[] = { "510703015%", "510703009%", "510703012%",
				"510703017%", "510703019%", "510703016%", "510703013%",
				"510703014%", "510703023%", "510703002%", "510781017%",
				"510781007%", "510781008%", "510781014%", "510781034%",
				"510781019%", "510781032%", "510781030%", "510781005%",
				"510781005%", "510781005%", "510703023%", "510703023%",
				"510703002%" };
		String keys[] = { "%吴家%", "%丰谷%", "%青义%", "%金峰%", "%新皂%", "%杨家%",
				"%龙门%", "%石塘%", "%城郊%", "%城北%", "%武都%", "%青莲%", "%九岭%", "%彰明%",
				"%二郎庙%", "%新安%", "%厚坝%", "%重华%", "%三合%", "%三合%", "%三合%",
				"%城郊%", "%城郊%", "%城北%" };

		// 数据库用户名
		String userName = "crbzx";
		// 密码
		String userPasswd = "hxcrb2013";
		// 数据库名 characterEncoding=utf-8
		String url = "jdbc:mysql://crb.xbitworld.com:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull";
		// 数据库用户名
		/*
		 * String userName = "root"; // 密码 String userPasswd = "qweasd123"; //
		 * 数据库名 characterEncoding=utf-8 String url =
		 * "jdbc:mysql://crb.scu.edu.cn:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull"
		 * ;
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url, userName,
					userPasswd);
			PreparedStatement pstmt = connection
					.prepareStatement(sqlGetHostNum6);
			int areaId = 0;
			String areaName = "";
			int num = 0;
			long toltal = 0;
			for (int i = 0; i < areaCodes.length; i++) {
				// for (int i = 117511; i <= 311795; i++) {
				pstmt.setInt(1, orgIds[i]);
				pstmt.setString(2, areaCodes[i]);
				pstmt.setString(3, keys[i]);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					// FILE_ID,ORG_ID,FILE_PATH, FILE_NAME ,FILE_TIME ,STATE
					areaId = rs.getInt(1);
					// areaName = rs.getString(2);
					num = rs.getInt(2);
					// toltal += num;
					System.out.print("" + areaId + "\t" + num + "\n");
					// }
				}
				rs.close();
			}
			pstmt.close();
			connection.close();
			System.out.print("查询完毕！共查询到：" + toltal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2013年10月27日处理出错的文件ID
	private static void dealCorrectFileId2() {
		int[] pages = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2

		};
		String[] dabhs = { "101308150019", "101308210013", "101308210034",
				"101308210077", "101308210078", "101308210080", "101308210088",
				"101308220001", "101308220024", "101308220029", "101308220047",
				"101308220053", "101308220067", "101308220071", "101308230011",
				"101308230015", "101308230016", "101308230027", "101308230029",
				"101308230031", "101308230039", "101308240001", "101308240002",
				"101308240003", "101308240019", "101308240022", "101308240025",
				"101308260027", "104309110055", "106307190008", "106307190011",
				"106307190013", "106307190020", "106307190023", "106307190024",
				"106307190031", "106307190035", "106307190036", "106307190045",
				"106307190055", "106307190061", "106307220007", "106307220008",
				"106307220015", "106307220016", "106307220020", "106307220027",
				"106307220028", "106307240001", "106307240009", "106307290060",
				"106307290064", "106309120287", "107307300005", "107309020001",
				"107309020002", "107309020010", "107309020039", "107309020052",
				"107309020058", "107309020061", "107309020069", "107309020078",
				"107309020090", "107310170843", "107310230001", "108308060036",
				"108308120021", "109308010005", "109309170044", "109309170059",
				"109309180344", "109309250040", "109309270064", "109310080535",
				"109310080565", "109310100017", "109310100019", "109310100070",
				"111308130039", "111308130060", "111308130074", "111308130096",
				"111308140089", "111308270242", "111308270244", "111310160031",
				"111310160082", "112309291219", "160308150018", "161307190025",
				"161307240038", "161309160276", "161309160312", "161309160354",
				"161309170023", "161309230104", "162310310027", "163309050010",
				"164307230008", "164307230013", "164307230017", "164307230021",
				"164307230028", "164307230029", "164307230031", "164307230034",
				"164307230035", "164307230041", "164307230043", "164307230044",
				"164307230045", "164307230047", "164307230048", "164307230049",
				"164307230051", "164307230055", "164307230056", "164307230066",
				"164307230068", "164307230070", "164307230077", "164307230081",
				"164307230084", "164307240001", "164307240010", "164307240017",
				"164307240022", "164307240023", "164307240040", "164307240085",
				"164307250007", "164307250023", "164307250032", "164307250048",
				"164307250059", "164307260025", "164308110024", "164308150003",
				"164308170043", "164308200016", "164309070010", "164309180006",
				"164309180010", "164309180032", "164309180120", "164309200028",
				"164309230046", "164309230050", "164309230106", "164309270072",
				"164310110309", "164310120508", "164310120587", "164310120631",
				"164310120635", "164310120652", "164310120654", "164310120913",
				"164310130078", "164310130088", "164310130090", "164310130092",
				"164310130108", "164310130185", "164310130186", "164310130195",
				"164310130197", "164310130213", "164310130218", "164310130219",
				"164310130227", "164310130258", "166307170046", "166307170052",
				"166307170081", "166310020011", "166310030029", "167308300037",
				"167309080046", "167309120017", "167309140043", "167309220103",
				"167309300054", "167309300138", "167310090214", "168307230013",
				"168307260035", "168308080044", "168308100091", "168308100119",
				"168308100161", "168308120004", "168308120007", "168308120030",
				"168308120121", "168308160003", "168308170138", "168308170139",
				"168308180016", "168308190058", "168308200013", "168308200025",
				"168308200123", "168308210003", "168308210127", "168308230065",
				"168308290030", "168308290031", "168308290033", "168308290036",
				"168308290043", "168308290048", "168308290049", "168308290100",
				"168308290116", "168308300058", "168308300105", "168309030064",
				"168309040015", "168309200103", "168309200335", "168309230022",
				"168309230129", "168309250675", "168309290132", "168309290235",
				"168309300548", "168309300582", "168309300603", "168310140029",
				"168310210086", "169308200017", "169309060098", "169309260055",
				"170310090085", "170310100055", "170310110156", "101308070046",
				"101308080011", "101308210034", "101308220072", "101308270011",
				"101308280012", "106307190015", "106307190034", "106307190040",
				"106307190045", "106307190057", "106307190061", "106307220005",
				"106307220007", "106307220008", "106307220014", "106307220015",
				"106307220018", "106307220020", "106307220021", "106307220037",
				"106307220040", "106307240001", "106307240002", "106307240037",
				"107307300005", "107308060058", "107310170843", "108307310050",
				"108308060062", "109308010003", "109308140022", "109309170079",
				"109309170080", "109309180043", "109309180351", "109310080028",
				"109310080029", "109310100016", "109310100017", "111308130039",
				"111308270179", "111308270241", "111308270242", "111308270244",
				"111309300086", "160309060039", "161307190021", "164307230029",
				"164307230031", "164307230033", "164307230034", "164307230035",
				"164307230036", "164307230037", "164307230038", "164307250075",
				"164310110309", "164310120508", "164310120587", "164310120652",
				"164310130185", "166307170052", "166307170081", "166309260178",
				"166309290026", "166310040001", "166310040036", "167308300076",
				"167309140033", "167309300092", "167309300167", "167309300207",
				"168307260094", "168308120005", "168308120030", "168308160210",
				"168308170025", "168308170029", "168308170131", "168308170134",
				"168308170139", "168308180017", "168308180067", "168308190060",
				"168308270055", "168308300025", "168308300059", "168308300105",
				"168309160045", "168309200039", "168309200350", "168309230021",
				"168309290881", "168310150112", "169308200032", "169308200065",
				"169309060103", "169309070020", "169309260122", "170309100006",
				"170310090085", "170310110156" };
		// 数据库用户名
		String userName = "crbzx";
		// 密码
		String userPasswd = "hxcrb2013";
		// 数据库名 characterEncoding=utf-8
		String url = "jdbc:mysql://crb.xbitworld.com:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull";
		// 数据库用户名
		/*
		 * String userName = "root"; // 密码 String userPasswd = "qweasd123"; //
		 * 数据库名 characterEncoding=utf-8 String url =
		 * "jdbc:mysql://crb.scu.edu.cn:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull"
		 * ;
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url, userName,
					userPasswd);
			PreparedStatement pstmt = connection
					.prepareStatement(sqlGetFileId5);
			String dabh = "";
			int num = 0;
			String strOCRtime = "";
			String strCheckDate = "";
			for (int i = 0; i < pages.length; i++) {
				// for (int i = 117511; i <= 311795; i++) {
				pstmt.setInt(1, pages[i]);
				pstmt.setString(2, dabhs[i]);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					// FILE_ID,ORG_ID,FILE_PATH, FILE_NAME ,FILE_TIME ,STATE
					dabh = rs.getString(1);
					num = rs.getInt(2);
					// if (num > 1) {
					strOCRtime = rs.getString(3);
					strCheckDate = rs.getString(4);
					System.out.print("" + dabh + "\t" + dabhs[i] + "\t"
							+ pages[i] + "\t" + num + "\t'" + strOCRtime
							+ "\t'" + strCheckDate + "\n");
					// }
				}
				rs.close();
			}
			pstmt.close();
			connection.close();
			System.out.print("查询完毕！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2013年10月27日处理出错的文件ID
	private static void dealErrorFileId() {

		String[] dabhs = { "103310180007", "103310180009", "103310180011",
				"103310180013", "103310180015", "103310180016", "103310180019",
				"103310180022", "103310180023", "103310180024", "103310180025",
				"103310180026", "103310180027", "103310180028", "103310180030",
				"103310180035", "103310180039", "103310180042", "103310180043",
				"103310180044", "103310180045", "103310180046", "103310180051",
				"103310180052", "103310180054", "103310180055", "103310180056",
				"103310180059", "103310180062", "103310180063", "103310180066",
				"103310180067", "103310180069", "103310180070", "103310180072",
				"103310180074", "103310180076", "103310180077", "103310180078",
				"103310180079", "103310180082", "103310180084", "103310180071" };
		int[] pages = { 2, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 2,
				2, 1, 1, 1 };
		// 数据库用户名
		String userName = "crbzx";
		// 密码
		String userPasswd = "hxcrb2013";
		// 数据库名 characterEncoding=utf-8
		String url = "jdbc:mysql://crb.xbitworld.com:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url, userName,
					userPasswd);
			PreparedStatement pstmt = connection.prepareStatement(sqlGetFileId);

			for (int i = 0; i < pages.length; i++) {
				pstmt.setString(1, dabhs[i]);
				pstmt.setInt(2, pages[i]);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					int orgId = rs.getInt(1);
					int fileId = rs.getInt(2);
					System.out.print("" + orgId + "\t" + fileId + "\n");

				}
				rs.close();
			}

			pstmt.close();

			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2013年10月9日处理出错的班级ID
	private static void dealErrorClassId() {
		// 数据库用户名
		String userName = "crbzx";
		// 密码
		String userPasswd = "hxcrb2013";
		// 数据库名 characterEncoding=utf-8
		String url = "jdbc:mysql://crb.xbitworld.com:3306/checkdb?autoReconnect=true&useUnicode=true&characterEncoding=GB2312&zeroDateTimeBehavior=convertToNull";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url, userName,
					userPasswd);
			Statement statementup1 = connection.createStatement();
			Statement statementup2 = connection.createStatement();
			PreparedStatement pstmt = connection.prepareStatement(sqlFindClass);
			PreparedStatement pstmt2 = connection
					.prepareStatement(sqlGetClassId);
			PreparedStatement pstmt0 = connection.prepareStatement(SQL_GET_ALL);
			ResultSet rs = pstmt0.executeQuery();
			while (rs.next()) {
				// CLASS_ID SCHOOL_ID CLASS_NAME GREATE_TIME

				// 169309270185 78 510781043032 江油市长钢鸿源幼儿园 47 大大三班
				/*
				 * 思路：根据正确的SCHOOL_ID和CLASS_NAME查询班级是否存在，不存在就先添加班级，
				 * 然后根据正确的SCHOOL_ID和CLASS_NAME获取CLASS_ID 设置到personal_info表中，
				 */
				// 条码号 正确的学校（地区）号 地区编码 地区名 错误的学校（地区）号 班级名
				String p1 = rs.getString(1);
				String p2 = rs.getString(2);
				String p3 = rs.getString(3);
				String p4 = rs.getString(4);
				String p5 = rs.getString(5);
				String p6 = rs.getString(6);
				String p7 = rs.getString(7);
				System.out.print(p1);
				System.out.print("\t" + p2);
				System.out.print("\t" + p3);
				System.out.print("\t" + p4);
				System.out.print("\t" + p5);
				System.out.print("\t" + p6);
				System.out.print("\t" + p7);
				// System.out.println();

				String time = p2;
				Integer schoolId = new Integer(p3);
				String className = p7;
				// 处理班级信息

				pstmt.setInt(1, schoolId);
				pstmt.setString(2, className);
				ResultSet rs2 = pstmt.executeQuery();
				if (rs2.next()) {
					int count = rs2.getInt(1);
					System.out.print("\tcount=" + count);
					if (count == 0) {
						String sqlInsertClass = "INSERT CLASS(SCHOOL_ID,CLASS_NAME,GREATE_TIME) "
								+ " VALUES("
								+ schoolId
								+ ",'"
								+ className
								+ "',date_format('"
								+ time
								+ "','%Y-%m-%d %H:%i:%S'))";
						int result = statementup1.executeUpdate(sqlInsertClass);
						// System.out.print("\t不存在的className=" + className);
					}
				}
				rs2.close();
				// pstmt.close();
				// ResultSet rs3 = statement.executeQuery(sqlGetClassId);

				pstmt2.setInt(1, schoolId);
				pstmt2.setString(2, className);
				ResultSet rs3 = pstmt2.executeQuery();
				if (rs3.next()) {
					int classId = rs3.getInt(1);
					String sqlUpdateClassId = "update personal_info set CLASS_ID="
							+ classId + " where DABH='" + p1 + "'";
					// 更新personal_info表.
					int result2 = statementup2.executeUpdate(sqlUpdateClassId);
					System.out.println("\tclassId=" + classId + "\tresult="
							+ result2);
				}
				rs3.close();
			}
			rs.close();
			pstmt0.close();
			statementup1.close();
			statementup2.close();
			pstmt.close();
			pstmt2.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
