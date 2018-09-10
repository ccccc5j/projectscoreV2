package javadev.core.util;


/**
 * @author Josh Wang
 * @since 2005-4-29
 *  
 */
public class StringUtil {

	/**
	 * 分类时在类Id前补零
	 * 
	 * @param src
	 * @param len
	 * @return
	 */
	public static String fillZero(String nextId, String parentId) {
		int len = parentId.length() + 3;
		if ("0".equals(parentId)) {
			len = 3;
		}
		int srcLen = nextId.length();
		if (srcLen >= len) {
			return nextId;
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - srcLen; i++) {
				sb.append("0");
			}
			sb.append(nextId);
			return sb.toString();
		}
	}

}