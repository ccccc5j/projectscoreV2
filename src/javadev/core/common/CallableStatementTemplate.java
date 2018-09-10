package javadev.core.common;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

public class CallableStatementTemplate {
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public CallableStatementTemplate() {
	}

	public CallableStatementTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* call */
	public void call(String sql) {
		call(sql, null);
	}

	public void call(String sql, final Object[] args) {
		jdbcTemplate.execute(sql, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				setParams(cs, args, -1);
				cs.execute();
				return null;
			}
		});
	}

	/* callForList */

	public List callForList(String sql) {
		return callForList(sql, null);
	}

	public List callForList(String sql, final Object[] args) {
		return callForList(sql, args, 1);
	}
	

	public List callForList(String sql, final Object[] args, final int outParamIndex) {
		return (List) jdbcTemplate.execute(sql, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.registerOutParameter(outParamIndex, oracle.jdbc.OracleTypes.CURSOR);
				setParams(cs, args, outParamIndex);
				cs.execute();
				ResultSet rs = (ResultSet) cs.getObject(outParamIndex);
				List list = (List) new RowMapperResultSetExtractor(new ColumnMapRowMapper()).extractData(rs);
				rs.close();
				return list;
			}

		});
	}

	/* callForMap */

	public Map callForMap(String sql) {
		return callForMap(sql, null);
	}

	public Map callForMap(String sql, final Object[] args) {
		return callForMap(sql, args, 1);
	}

	public Map callForMap(String sql, final Object[] args, final int outParamIndex) {
		List tmp = callForList(sql, args, outParamIndex);
		if (tmp.size() > 0) {
			return (Map) tmp.get(0);
		}
		return null;
	}

	/* callForInt */

	public int callForInt(String sql) {
		return callForInt(sql, null);
	}

	public int callForInt(String sql, final Object[] args) {
		return callForInt(sql, args, 1);
	}

	public int callForInt(String sql, final Object[] args, final int outParamIndex) {
		Integer tmp = (Integer) callForObject(sql, args, outParamIndex, Types.INTEGER);
		return tmp.intValue();
	}

	/* callForLong */

	public long callForLong(String sql) {
		return callForLong(sql, null);
	}

	public long callForLong(String sql, final Object[] args) {
		return callForLong(sql, args, 1);
	}

	public long callForLong(String sql, final Object[] args, final int outParamIndex) {
		Long tmp = (Long) callForObject(sql, args, outParamIndex, Types.BIGINT);
		return tmp.longValue();
	}

	/* callForString */

	public String callForString(String sql) {
		return callForString(sql, null);
	}

	public String callForString(String sql, final Object[] args) {
		return callForString(sql, args, 1);
	}

	public String callForString(String sql, final Object[] args, final int outParamIndex) {
		return (String) callForObject(sql, args, outParamIndex, Types.VARCHAR);
	}

	/**
	 * 通用处理callForInt/callForLong/callForString
	 */
	private Object callForObject(String sql, final Object[] args, final int outParamIndex, final int outParamType) {
		return jdbcTemplate.execute(sql, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.registerOutParameter(outParamIndex, outParamType);
				setParams(cs, args, outParamIndex);
				cs.execute();
				if (outParamType == Types.INTEGER) {
					return new Integer(cs.getInt(outParamIndex));
				} else if (outParamType == Types.BIGINT) {
					return new Long(cs.getLong(outParamIndex));
				} else if (outParamType == Types.VARCHAR) {
					return cs.getString(outParamIndex);
				}
				return cs.getObject(outParamIndex);
			}
		});
	}

	/**
	 * 设置参数
	 * @param skipIndex 跳过输出用参数的索引地址
	 */
	private static void setParams(CallableStatement cs, Object[] args, int skipIndex) throws SQLException {
		if (args != null) {
			int paramIndex = 1;
			for (int i = 0; i < args.length; i++, paramIndex++) {
				// 跳过Out参数的位置
				if (paramIndex == skipIndex) {
					paramIndex++;
				}
				Object inValue = args[i];
				if (inValue == null) {
					cs.setNull(paramIndex, Types.NULL);
				} else {
					if (inValue instanceof StringBuffer || inValue instanceof StringWriter) {
						cs.setString(paramIndex, inValue.toString());
					} else if ((inValue instanceof java.util.Date)
							&& !(inValue instanceof java.sql.Date || inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp)) {
						cs.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
					} else if (inValue instanceof Calendar) {
						Calendar cal = (Calendar) inValue;
						cs.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()));
					} else {
						cs.setObject(paramIndex, inValue);
					}
				}
			}
		}
	}
}
