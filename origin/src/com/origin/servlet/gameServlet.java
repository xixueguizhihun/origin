package com.origin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.origin.contr.addGameInfo;
import com.origin.contr.gameInfoCtr;
import com.origin.contr.gameInfoCtrMerge;


public class gameServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6195449381164389363L;

	/**
	 * Constructor of the object.
	 */
	public gameServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
		//转换中文乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		//在服务器端设置允许在其他域名下访问，及响应类型、响应头设置
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods","POST");
				response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		
		
		String method = request.getParameter("method");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		addGameInfo upMysql = new addGameInfo();//创建添加和修改商品信息类
		
		gameInfoCtrMerge getAremove = new gameInfoCtrMerge();//创建删除游戏信息与查询多条完整游戏信息
		
		gameInfoCtr sqlInfoCtr = new gameInfoCtr();//创建具体查询方法
		
		//**********************************************************************//
		if(method.equals("schall")){//查询多条完整信息
		out.println(getAremove.allGameInfo(sqlInfoCtr.search(request))+"");
		
		//**********************************************************************//
		}else if(method.equals("schoneall")){//根据id或游戏名,获取一条数据的完整信息
	   out.println(getAremove.allGameInfo(sqlInfoCtr.isUserInfo(request))+"");
	   
	  //**********************************************************************//
		}else if(method.equals("search")){//只查询多条游戏数据库信息
			out.println(JSONArray.fromObject(sqlInfoCtr.search(request))+"");
	   //**********************************************************************//	
		}else if(method.equals("schone")){//只查询一条游戏数据库信息
		out.println(JSONArray.fromObject(sqlInfoCtr.isUserInfo(request))+"");
		
	 //**********************************************************************//	
		}else if(method.equals("scharall")){//模糊查询完整信息
		out.println(getAremove.allGameInfo(sqlInfoCtr.searchChar(request))+"");
	 //**********************************************************************//	
		}else if(method.equals("schar")){//模糊查询数据库信息
		out.println(JSONArray.fromObject(sqlInfoCtr.searchChar(request))+"");
		//**********************************************************************//	
		}else if(method.equals("upInfo")){//修改商品信息
			out.println(upMysql.updataGame(request));
		//**********************************************************************//	
		}else if(method.equals("remov")){//删除游戏完整信息
			out.println(getAremove.removeGameInfo(request));
	    //**********************************************************************//		
		}else if(method.equals("addinfo")){//添加游戏商品信息
			doGet(request, response);
	  //**********************************************************************//
		}else if(method.equals("removeimg")){//删除图片
			getAremove.removeImg(request);
		}else{
			doPost(request, response);
		}
		
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		
		//在服务器端设置允许在其他域名下访问，及响应类型、响应头设置
	      response.setHeader("Access-Control-Allow-Origin", "*");
		  response.setHeader("Access-Control-Allow-Methods","POST");
		  response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		
		
		String method = request.getParameter("method");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
       addGameInfo upMysql = new addGameInfo();//创建添加和修改商品信息类
       if(method==null){//添加游戏商品信息
    	   upMysql.addInfo(request);
		}else {
			doGet(request, response);
		}
       
        
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
