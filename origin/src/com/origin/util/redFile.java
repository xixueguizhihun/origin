package com.origin.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

public class redFile {
	
	
	//读配置文件方法
     public String redConfig(String path,String naem){
    	 String str = null;
    	 Properties prop =  new  Properties();  
  		//ClassLoader loader = Thread.currentThread().getContextClassLoader();
    	 URL url = Thread.currentThread().getContextClassLoader().getResource("");
 		String pathStr  =url.getFile();
 		pathStr =  pathStr.substring(1, pathStr.length());
 
  		InputStream in = null;
		try {
			in = new FileInputStream(pathStr+"/../../file"+path);    //loader.getResourceAsStream(xString);
			
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}    
  		 try {
			prop.load(in);
			str = prop.getProperty(naem).trim(); 

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//prop.clone();
			try {
				if(in != null){
					in.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
    	 
    	 return str;
     }//读配置文件方法结束
     
     
     public Properties redConfig(String path,boolean is){
    	 Properties prop =  new  Properties();  
  		//ClassLoader loader = Thread.currentThread().getContextClassLoader();
    	 
    	 URL url = Thread.currentThread().getContextClassLoader().getResource("");
  		String pathStr  =url.getFile();
  		pathStr =  pathStr.substring(1, pathStr.length());
    	 
  		InputStream in = null;
		try {
			in = new FileInputStream(pathStr+"/../../file"+path);
			
		} catch (FileNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
  		
  				//loader.getResourceAsStream("/../../file"+path); 
  		 try {
			prop.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		}
    	 
    	 return prop;
     }//读配置文件方法结束
     
     
     
     
     //读文件方法
     public String red(String path){
    	 Reader reader = null;
    	 int tempchar;
    	 String str = ""; 
    	// String line = null;
    	//ClassLoader loader = Thread.currentThread().getContextClassLoader();
   		//InputStream in = loader.getResourceAsStream("/../../file"+path); 
    	 
    	 URL url = Thread.currentThread().getContextClassLoader().getResource("");
   		String pathStr  =url.getFile();
   		pathStr =  pathStr.substring(1, pathStr.length()); 
    	 
    	FileInputStream in = null;
		try {
			in = new FileInputStream(pathStr+"/../../file"+path);
		} catch (FileNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
   		reader = new InputStreamReader(in);
        try {
      // BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      // StringBuilder Bstr = new StringBuilder();
        	
			while ((tempchar = reader.read()) != -1) {  
				if((char)tempchar == '\n'){
					str += "<br>";
				}else if((char)tempchar == '\r'){
					
				}else{
					str += (char)tempchar;
				}
				
			}
			
       /*while ((line = reader.readLine()) != null) {
    	   Bstr.append(line);
    	   Bstr.append("<br>");
       }
       line = Bstr.toString();*/
       
       
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 return str;
     }//读文件方法结束
     
}
