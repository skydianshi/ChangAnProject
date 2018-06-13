package com.changan.changanproject.util;

public class CommonTool {
	
	public static final int  ST_FRM_MAX_ID = 0x7FF;

	public static boolean IsHexChar(char ch) {
		if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f')
				|| (ch >= 'A' && ch <= 'F')) {
			return true;
		}
		return false;
	}

}
