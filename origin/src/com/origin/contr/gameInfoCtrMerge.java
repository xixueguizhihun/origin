package com.origin.contr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;


import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.origin.model.gameDescr;
import com.origin.model.gameInfo;
import com.origin.util.originUtil;
import com.origin.util.redFile;

public class gameInfoCtrMerge  {//extends redFile

	//private redFile redFileClass = new redFile();
	
	//游戏详情信息获取方法
	public JSONArray allGameInfo(ArrayList<gameInfo> List){
		redFile redFileClass = new redFile();
		JSONArray json = null;
		ArrayList<gameDescr> arrlist = new ArrayList<gameDescr>();
		if(List != null){
		for(int i = 0;i<List.size();i++){
			gameDescr xInfo  = new gameDescr();
			xInfo.setId(List.get(i).getId());
			xInfo.setGamename(List.get(i).getGamename());
			xInfo.setClassify(List.get(i).getClassify());
			xInfo.setDate(List.get(i).getDate());
			xInfo.setImgsrc(List.get(i).getImgsrc());
			xInfo.setPrice(List.get(i).getPrice());
			xInfo.setSale(List.get(i).getSale());
			xInfo.setCompany(List.get(i).getCompany());
			xInfo.setDescription(redFileClass.red(redFileClass.redConfig(List.get(i).getDescr(),"description")));
			xInfo.setOverview(redFileClass.red(redFileClass.redConfig(List.get(i).getDescr(),"overview")));
			xInfo.setSystem(redFileClass.red(redFileClass.redConfig(List.get(i).getDescr(),"system")));
			
			
			Properties prop =  redFileClass.redConfig(List.get(i).getDescr(),true);
			Iterator<?> pro = prop.entrySet().iterator();
			while(pro.hasNext()){
			    Map.Entry entry=(Map.Entry)pro.next();
			    Object key = entry.getKey();
			    Object value = entry.getValue();
			    String mStr = key.toString();
			    boolean Boo1 = key.equals("description")||key.equals("overview")||key.equals("system");
			    if(Boo1){}else{
			    boolean Boo = mStr.substring(0,mStr.length()-1).equals("video")||mStr.substring(0,mStr.length()-2).equals("video");
			  if(Boo){
				  xInfo.setVideo((xInfo.getVideo()+value+",")); 
			  }else{
				  xInfo.setBgImg( (xInfo.getBgImg()+value+",")); 
			  }
			  }

			}
			arrlist.add(xInfo);
			prop.clone();
		

		}
		json =JSONArray.fromObject(arrlist);
		             }
		return json;
	}//游戏详情信息获取方法结束
	
	//创建文件的方法
	public String addGame(String path,String fileContent,String Form){
		/*URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		String pathStr  =url.getFile();
		pathStr =  pathStr.substring(1, pathStr.length());*/
		String pathStr  = getPath("/../../file"+path);
		String filename = (UUID.randomUUID().toString())+Form;
		pathStr = pathStr+filename;
		//File fileDir = new File(“C:/test/”);
		//fileDir.mkdirs(); 
		File file = new File(pathStr);
		if(!file.exists()){
			try {
				file.createNewFile();
				writeFileContent((path+filename),fileContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (path+filename);
	}//创建文件完成
	
	
	//写入文件方法
	public boolean writeFileContent(String path,String newStr){
		boolean isWrite = false;
		path = getPath("/../../file"+path);
		 File file = new File(path);
		 StringBuffer buffer = new StringBuffer();
		 buffer.append(newStr+"\r\n");
		 try {
			FileOutputStream fos =  new FileOutputStream(file);
			 PrintWriter pw = new PrintWriter(fos);
			 pw.write(buffer.toString().toCharArray());
			 pw.flush();
			 isWrite  = true;
			 pw.close();
			 fos.close();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return isWrite;
	}//写文件方法结束
	
	
	//获取绝对路径
	public String getPath(String path){
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		String pathStr  =url.getFile();
		pathStr =  pathStr.substring(1, pathStr.length());
		return pathStr;
	}//获取绝对路径结束
	
	
	//删除文件的方法
	public boolean removeFile(String path,String filename){
		boolean isRemove = false;
		path = getPath(path);
		File file = new File(path+filename);
		try {
            if(file.exists()){
                file.delete();
                isRemove = true;
            }
        } catch (Exception e) {
          
        }
		return isRemove;
	}//删除文件的方法结束
	
	
	//删除游戏信息
	public boolean removeGameInfo(HttpServletRequest request){
		boolean isRemove = false;
		String id = request.getParameter("id");
		gameInfoCtr gameIn = new gameInfoCtr();
		ArrayList<gameInfo>  infos =  gameIn.isUserInfo(request);
		redFile  redF = new redFile();
		Properties prop = redF.redConfig(infos.get(0).getDescr(),true);
		isRemove = removeFile("/../../",infos.get(0).getImgsrc());
		Iterator<?> pro = prop.entrySet().iterator();
		while(pro.hasNext()){
		    Map.Entry entry=(Map.Entry)pro.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    String mStr = key.toString();
		    boolean Boo1 = key.equals("description")||key.equals("overview")||key.equals("system");
		    if(Boo1){
		    	isRemove =removeFile("", value.toString());
		    }else{
		    boolean Boo = mStr.substring(0,mStr.length()-1).equals("video")||mStr.substring(0,mStr.length()-2).equals("video");
		  if(Boo){
			  isRemove = removeFile("/../../", value.toString());
		  }else{
			  isRemove = removeFile("/../../", value.toString());
		  }
		  }

		}
		
		prop.clone();
		
		isRemove = removeFile("",infos.get(0).getDescr());
		isRemove = gameIn.removeGame(request);
		return isRemove;
	}//删除游戏信息结束
	
	
	   //删除图片方法
	public boolean removeImg(HttpServletRequest request){
		boolean isRemove = false;
		originUtil Util =  new originUtil();
		String path = Util.getEncoding(request.getParameter("imgpath"));
		isRemove = removeFile("/../../",path);
		
		 Properties prop = new Properties();
		 String xy = new gameInfoCtr().isUserInfo(request).get(0).getDescr();
		 xy = getPath("")+"/../../file"+xy;
		 try {
			InputStream fis = new FileInputStream(xy);
			prop.load(fis);
			  OutputStream fos = new FileOutputStream(xy);
	          prop.put(request.getParameter("imgname"),"");
	          prop.store(fos, "");
	          fos.close();
	          fis.close();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isRemove;
	}
	
	
}
