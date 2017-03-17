package com.origin.contr;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.origin.model.gameInfo;
import com.origin.util.originUtil;
import com.origin.util.redFile;




public class addGameInfo  {//extends gameInfoCtrMerge
	private originUtil util = new originUtil();//新建字符编码转换工具类
	//private gameInfoCtrMerge merge = new gameInfoCtrMerge();
            //添加游戏商品的完整方法
	public boolean addInfo(HttpServletRequest request){
		gameInfoCtrMerge merge = new gameInfoCtrMerge();
		boolean isAdd = false;
		gameInfo gameU = new gameInfo();//新建游戏信息类
		String filename = merge.addGame("/context/","",".properties"); //创建配置文件
		gameU.setDescr(filename);//把配置文件的路径放到要保存的字段中
		String context;//创建一个字符串中专站
		FileOutputStream oFile = null;//创建文件输出流容器
		 Properties prop = new Properties();//创建Properties配置文件对象
		 try {
			 URL url = Thread.currentThread().getContextClassLoader().getResource("/../../file");//获取web应用的class的文件绝对路径
			String pathStr  =url.getFile()+filename;
			oFile = new FileOutputStream(pathStr, true);//创建文件输出流
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory(); //创建文件项工厂(文件上传)
	    //创建用于解析请求数据的组件
        ServletFileUpload upload = new ServletFileUpload(factory);//用于解析enctype="multipart/form-data"表单数据
       upload.setSizeMax(10002400000l);//设置上传文件的大小.
//开始解析数据,返回文件项列表
try {
	List<FileItem> list =  upload.parseRequest(request);
	for(int i = 0;i<list.size();i++){
		//从列表中遍历每一个文件项
		FileItem item = list.get(i);
		if(item.isFormField()){//根据不同的字段保存信息
			if("gamename".equals(item.getFieldName())){
				gameU.setGamename(util.getEncoding(item.getString()));
			}else if("price".equals(item.getFieldName())){
				gameU.setPrice(Double.parseDouble(item.getString()));
			}else if("classify".equals(item.getFieldName())){
				gameU.setClassify(util.getEncoding(item.getString()));
			}else if("company".equals(item.getFieldName())){
				gameU.setCompany(util.getEncoding(item.getString()));
			}else if("date".equals(item.getFieldName())){
				gameU.setDate(util.getEncoding(item.getString()));
			}else if("sale".equals(item.getFieldName())){
				gameU.setSale(Integer.parseInt(item.getString()));
			}else if("description".equals(item.getFieldName())){
				context = merge.addGame("/file/description/",util.getEncoding(item.getString()),".txt");          
				 prop.setProperty("description",context);
			}else if("overview".equals(item.getFieldName())){
				context = merge.addGame("/file/overview/",util.getEncoding(item.getString()),".txt");
				 prop.setProperty("overview",context);
			}else if("system".equals(item.getFieldName())){
				context = merge.addGame("/file/system/",util.getEncoding(item.getString()),".txt");
				prop.setProperty("system",context);
			}

		}
		//检测到如果上传的是文件,则保存文件
		String str = item.getFieldName();
		str = str.substring(0,str.length()-1);
		boolean or = str.equals("video")||str.substring(0, str.length()-2).equals("video");
		or = or||str.equals("bgImg")||str.substring(0, str.length()-2).equals("bgImg")||str.equals("imgsr");         
		if(or){
			String filename2 = item.getName();//获取上传的文件的名字
			String	extname = filename2.substring(filename2.lastIndexOf("."));//截取上传的文件的后缀名
			String newName = 	UUID.randomUUID().toString();//采用UUID计算文件名,避免文件名发生重复
			String rootpath = merge.getPath("/../../img/logoImg/");//获取上传的文件要保存的位置
			
			if((item.getFieldName()).equals("imgsrc")){//判断是封面图则,单独保存相对路径
				String kkString = "img/logoImg/"+newName+extname;
				gameU.setImgsrc(kkString);
				}else{
					prop.setProperty(item.getFieldName(),"img/logoImg/"+newName+extname);//否则就把相对路径保存在配置文件中
				}
			String newpath = rootpath+newName+extname;//创建文件的绝对路径

			item.write(new File(newpath));//开始保存文件///新建并写入文件
		}
	}
         } catch (FileUploadException e) {
	             e.printStackTrace();
         } catch (Exception e) {
                	e.printStackTrace();
              }
	
    	try {
			prop.store(oFile, "The New properties file");//关闭并写入Properties配置文件
			 oFile.close();//关闭文件输出流
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	gameInfoCtr addmysql = new gameInfoCtr();
    	isAdd = addmysql.add(gameU);//将该加入数据库的东西,加入数据库
		return isAdd;//返回执行结果
	}//添加游戏的完整方法结束
	
	
	
	
	//修改游戏信息完整方法
	public boolean updataGame(HttpServletRequest request){
		gameInfoCtrMerge merge = new gameInfoCtrMerge();
		boolean isUpdata = false;
		gameInfoCtr upCtr =  new gameInfoCtr();
Enumeration<String> pNames=request.getParameterNames();//获取用户请求参数名称,(返回枚举类型);
      	
      	while(pNames.hasMoreElements()){//循环读出用户请求参数名称
      	    String name=(String)pNames.nextElement();
      	    boolean or = name.equals("gamename")||name.equals("price")||name.equals("imgsrc")||name.equals("sale")||name.equals("date")||name.equals("company")||name.equals("descr")||name.equals("classify");
      	    if(or){//判断如果在用户数据库中有这个字段,则保存;
      	    	isUpdata = upCtr.updata(request);
      	    }else{
      	    	ArrayList<gameInfo> gameInfos = upCtr.isUserInfo(request);
      	    	redFile redcon = new redFile();
      	    	if(name.equals("description")){
      	    		String str = redcon.redConfig(gameInfos.get(0).getDescr(),"description");
      	    		isUpdata = merge.writeFileContent(str,util.getEncoding(request.getParameter("description")));
      	    	}else if(name.equals("overview")){
      	    		String str = redcon.redConfig(gameInfos.get(0).getDescr(),"overview");
      	    		isUpdata =	merge.writeFileContent(str,util.getEncoding(request.getParameter("overview")));
				}else if(name.equals("system")){
					String str = redcon.redConfig(gameInfos.get(0).getDescr(),"overview");
					isUpdata =	merge.writeFileContent(str,util.getEncoding(request.getParameter("overview")));
				}
      	    }
      	    
      	    
      	}
		
		return isUpdata;
	}//修改游戏信息完整方法结束
	
	
	
	
}
