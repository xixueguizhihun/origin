package com.origin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.origin.contr.userInfoCtr;

public class userServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1833338130818309060L;

	/**
	 * Constructor of the object.
	 */
	public userServlet() {
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
		//在服务器端设置允许在其他域名下访问，及响应类型、响应头设置
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods","POST");
				response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		
              doPost(request, response);
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
				
				userInfoCtr userCtr = new userInfoCtr();//创建用户信息控制类
				
				//*************************************************************//
		         if(method.equals("updata")){//修改
						if(userCtr.updata(request)){
						out.println("true");
						}else{
							out.println("false");
						}
						
				//*************************************************************//
				}else if(method.equals("add")){//添加
					if(userCtr.add(request)){
						out.println("true");
						}else{
						out.println("false");
						}
					//***********************************************************//
				}else if(method.equals("search")){//查询多个用户
					out.println(userCtr.search(request)+"");
					
					//****************************************************//
				}else if(method.equals("schchar")){//查询那个用户信息中包含某字符
					out.println(userCtr.searchChar(request)+"");
					
					//***************************************************//
				}else if(method.equals("isuser")){//判断用户是否存在
					if(userCtr.isUser(request)){
						out.println("true");
					}else {
						out.println("false");
					}
					//*******************************************************8//
				}else if(method.equals("ispwd")){//判断用户名密码是否正确
					if(userCtr.isPwd(request)){
						out.println("true");
					}else {
						out.println("false");
					}
					
					//******************************************************************//
				}else if(method.equals("remove")){//删除用户数据
					if(userCtr.removeUser(request)){
						out.println("true");
					}else {
						out.println("false");
					}
					
		     //******************************************************************//
				}else if(method.equals("schnum")){//查询记录的总数
					out.println(userCtr.searchCount(request));
					
			//************************************************************************//		
				}else if(method.equals("userinfo")){//查询指定用户信息
					String userinfo = userCtr.isUserInfo(request)+"";
					if(userinfo.equals("")){
						out.println("false");
					}else{
						out.println(userinfo);
					}
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
