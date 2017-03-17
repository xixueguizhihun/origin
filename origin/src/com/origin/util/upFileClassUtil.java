package com.origin.util;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.origin.model.gameInfo;

public class upFileClassUtil {
    public Properties upFile(HttpServletRequest request,String path,Properties prop,gameInfo gameU){
    	  //创建文件项工厂
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    		    //创建用于解析请求数据的组件
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	//开始解析数据,返回文件项列表
		try {
			List<FileItem> list =  upload.parseRequest(request);
			
			for(int i = 0;i<list.size();i++){
				//从列表中遍历每一个文件项
				FileItem item = list.get(i);
				if(item.isFormField()){
					String str = item.getFieldName();
					str = str.substring(0,str.length()-1);
					boolean or = str.equals("video")||str.substring(0, str.length()-1).equals("video");
					or = or||str.equals("bgImg")||str.substring(0, str.length()-1).equals("bgImg")||str.equals("imgsr");         
					if(or){
						String filename = item.getName();
						String	extname = filename.substring(filename.lastIndexOf("."));
						String newName = 	UUID.randomUUID().toString();
						String rootpath = request.getServletContext().getRealPath("/../../img"+path);
						if(item.getFieldName().equals("imgsrc")){
							gameU.setImgsrc("img"+path+newName+extname);
							}else{
								prop.setProperty(item.getFieldName(),"img"+path+newName+extname);
							}
						String newpath = rootpath+newName+extname;
						item.write(new File(newpath));
					}
				}
				
			}
			
			
			
			
		} catch (FileUploadException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	
    	
    	return prop;
    }
}
