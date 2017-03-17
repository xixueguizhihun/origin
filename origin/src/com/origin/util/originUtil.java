package com.origin.util;

import java.io.UnsupportedEncodingException;

public class originUtil {

public String getEncoding(String value){
		
		try {
			value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
			
		} catch (UnsupportedEncodingException e) {
		
			e.printStackTrace();
		}
		
		return value;
	}
	
	
}
