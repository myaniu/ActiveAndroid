package com.activeandroid.query;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.util.ReflectionUtils;

public class Join implements Sqlable {
	static enum JoinType {
		LEFT, OUTER, INNER, CROSS
	}

	private From mFrom;
	private Class<? extends Model> mType;
	private String mAlias;
	private JoinType mJoinType;
	private String mOn;
	private String[] mUsing;

	Join(From from, Class<? extends Model> table, JoinType joinType) {
		mFrom = from;
		mType = table;
		mJoinType = joinType;
	}

	public Join as(String alias) {
		mAlias = alias;
		return this;
	}

	public From on(String on) {
		mOn = on;
		return mFrom;
	}

	public From on(String on, Object... args) {
		mOn = on;
		mFrom.addArguments(args);
		return mFrom;
	}

	public From using(String... columns) {
		mUsing = columns;
		return mFrom;
	}

	@Override
	public String toSql() {
		String sql = "";

		if (mJoinType != null) {
			sql += mJoinType.toString() + " ";
		}

		sql += "JOIN " + ReflectionUtils.getTableName(mType) + " ";

		if (mAlias != null) {
			sql += "AS " + mAlias + " ";
		}

		if (mOn != null) {
			sql += "ON " + mOn + " ";
		}
		else if (mUsing != null) {
			sql += "USING (" + TextUtils.join(", ", mUsing) + ") ";
		}

		return sql;
	}
}