package com.origin.contr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.origin.model.userInfo;
import com.origin.util.MysqlLink;
import com.origin.util.originUtil;

public class userInfoCtr extends MysqlLink {
	
    private originUtil Util =  new originUtil();
    
    //添加用户
    public boolean add(HttpServletRequest request){
    	String userName = request.getParameter("username");
    	String pwd = Util.getEncoding(request.getParameter("pwd"));
    	String email = Util.getEncoding(request.getParameter("email"));
    	String country = request.getParameter("country");
    	String birth = Util.getEncoding(request.getParameter("birth"));
    	String birthDay = request.getParameter("birthday");
    	String sql = "insert into userinfo(username,pwd,email,country,birth,birthDay)values(?,?,?,?,?,?)";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
        boolean  addWat = false;
          try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1,userName);
			ps.setString(2,pwd);
			ps.setString(3,email);
			ps.setString(4,country);
			ps.setString(5,birth);
			ps.setString(6,birthDay);
			
			int num = ps.executeUpdate();
			if(num > 0){
				System.out.println("IP:"+request.getRemoteAddr()+" 主机名:"+request.getRemoteHost()+" 操作:添加用户成功!");
				addWat = true;
			}else{
				System.out.println("IP:"+request.getRemoteAddr()+" 主机名:"+request.getRemoteHost()+" 操作:添加用户失败!");
				addWat = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
          return addWat;
    	
    }//添加用户的方法结束.
    
    //修改用户信息
    public boolean updata(HttpServletRequest request){
    	boolean updataWat = false; 
  	    String id = request.getParameter("id");
    	//System.out.println(Arrays.asList(request.getParameterNames()));
    	String sql = "update userinfo set ";//声明sql语句容器
    	ArrayList<String> str = new ArrayList<String>();//声明用户请求参数名容器;
    	int count;
    	
    	Enumeration<String> pNames=request.getParameterNames();//获取用户请求参数名称,(返回枚举类型);
    	
    	while(pNames.hasMoreElements()){//循环读出用户请求参数名称
    	    String name=(String)pNames.nextElement();
    	    boolean or = name.equals("username")||name.equals("pwd")||name.equals("email")||name.equals("country")||name.equals("birth")||name.equals("birthDay");
    	    if(or){//判断如果在用户数据库中有这个字段,则保存;
    	    	sql +=(name+"=?,");
    	    	str.add(name);
    	    }
    	}
    	sql = sql.substring(0,sql.length()-1);//去除字符串拼接时多出来的","逗号
    	sql += " where id=?";//加上修改条件
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			for(count = 1;count-1<str.size();count++){//循环向sql语句中加入刚才保存的合法参数的值.
				
			String	UserData = request.getParameter(str.get(count-1));
			ps.setString(count,UserData);
			}
			ps.setString(count,id);
			int num = ps.executeUpdate();
			if(num > 0){
				System.out.println("IP:"+request.getRemoteAddr()+" 主机名:"+request.getRemoteHost()+" 操作:修改用户成功!");	
				updataWat = true;
			}else{
				System.out.println("IP:"+request.getRemoteAddr()+" 主机名:"+request.getRemoteHost()+" 操作:修改用户失败!");
				updataWat  =false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}

    	return updataWat;
    }//修改方法结束
    
    //查询10条数据
    public JSONArray search(HttpServletRequest request){
    	int page = Integer.parseInt(request.getParameter("page"));
    	int count = searchCount(request);
    	JSONArray json = null;
    	int pageId = page==0?0:page*10;
    	if(!(pageId>=count)){
        	con = getMysqlCon();    		
    		String sql = "select * from userinfo limit "+pageId+",10";
    		StringBuffer sqlBuffer = new StringBuffer(sql);
    		try {
				ps = con.prepareStatement(sqlBuffer+"");
				rs = ps.executeQuery();
				ArrayList<userInfo> arrList = new ArrayList<userInfo>(); 
				while (rs.next()) {
					userInfo xInfo = new userInfo(); 
					xInfo.setId(rs.getInt("id"));
					xInfo.setUsernane(rs.getString("username"));
					xInfo.setEmail(rs.getString("email"));
					xInfo.setCountry(rs.getString("country"));
					xInfo.setPwd(rs.getString("pwd"));
					xInfo.setBirth(rs.getString("birth"));
					xInfo.setBirthday(rs.getString("birthday"));
					arrList.add(xInfo);
				}
				json = JSONArray.fromObject(arrList);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					con.close();
					ps.close();
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();//异常捕获
				}
			}
    	}else {
			//System.out.println("已是最后一页了");
			json = new JSONArray();
			json.add(false);
		}
		return json;
    }//查询方法结束(10条数据)
    
    //查询记录的总数
    public int searchCount(HttpServletRequest request){
    	int schCount = 0;
    	String str = request.getParameter("str")==null?null: request.getParameter("str");
    	String sql = "SELECT count(*) FROM userinfo";
    	sql = ((str == null)?sql:("SELECT count(*) FROM userinfo WHERE  username like '%"+str+"%' or email like '%"+str+"%'"));
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			rs = ps.executeQuery();
			if(rs.next()){
				schCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}
    	return schCount;
    }//查询总共有多少条数据
    
    
    //查询包含字符的用户
    public JSONArray searchChar(HttpServletRequest request){
    	int page = Integer.parseInt(request.getParameter("page"));
    	int count = searchCount(request);
    	int pageId = page==0?0:page*10;
    	JSONArray json = null;
    	String str = request.getParameter("str");
    	String sql = "SELECT * FROM userinfo WHERE username like '%"+str+"%' or email like '%"+str+"%' limit "+pageId+",10 ";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	if(!(pageId>=count)){
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			rs = ps.executeQuery();
			ArrayList<userInfo> arrList = new ArrayList<userInfo>(); 
			while (rs.next()) {
				userInfo xInfo = new userInfo(); 
				xInfo.setId(rs.getInt("id"));
				xInfo.setUsernane(rs.getString("username"));
				xInfo.setEmail(rs.getString("email"));
				xInfo.setCountry(rs.getString("country"));
				xInfo.setPwd(rs.getString("pwd"));
				xInfo.setBirth(rs.getString("birth"));
				xInfo.setBirthday(rs.getString("birthday"));
				arrList.add(xInfo);
			}
			json = JSONArray.fromObject(arrList);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}
    	
    	}else {
			//System.out.println("已是最后一页了");
			json = new JSONArray();
			json.add(false);
		}
    	return json;
    }//查询包含字符的用户函数
    
    
    //查询用户是否存在
    public boolean isUser(HttpServletRequest request){
    	boolean isWat = false;
    	String userName = request.getParameter("username");
    	String sql = "SELECT * FROM userinfo WHERE username=?";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1,userName);
			rs = ps.executeQuery();
			if(rs.next()){
				isWat = (rs.getString("id") == null?false:true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}
    	return isWat;
    }//判断用户是否存在方法结束
    
    
    //判断密码是否正确.
    public boolean isPwd(HttpServletRequest request){
    	boolean isWat = false;
    	String pwd = request.getParameter("pwd");
    	String userName = request.getParameter("username");
    	String sql = "SELECT * FROM userinfo WHERE username=?";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1,userName);
			rs = ps.executeQuery();
			if(rs.next()){
				isWat = pwd.equals(rs.getString("pwd"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}
    	return isWat;
    }//判断密码是否正确方法结束
    
    //删除用户操作
    
    public boolean removeUser(HttpServletRequest request){
    	boolean isRemov = false;
    	String id = request.getParameter("id");
    	con = getMysqlCon();
    	String sql =  " delete from userinfo where id=?";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1, id);
			int num = ps.executeUpdate();
			isRemov = num>0?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();//异常捕获
			}
		}
    	
    	return isRemov;
    }//删除数据操作结束
    
    
    //更具ID或用户名查询用户信息
    public JSONArray isUserInfo(HttpServletRequest request){
    	JSONArray json = null;
    	String id = request.getParameter("id")==null?null:request.getParameter("id");
    	String userName = request.getParameter("username")==null?null: Util.getEncoding(request.getParameter("username"));
    	String sql = "SELECT * FROM userinfo WHERE username=?";
    	sql  = id==null?sql:"SELECT * FROM userinfo WHERE id=?";
    	StringBuffer sqlBuffer = new StringBuffer(sql);
    	con = getMysqlCon();
    	try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1,(userName==null?id:userName));
			rs = ps.executeQuery();
			userInfo u  = new userInfo();
			if(rs.next()){
				u.setId(rs.getInt("id"));
				u.setUsernane(rs.getString("username"));
				u.setPwd(rs.getString("pwd"));
				u.setEmail(rs.getString("email"));
				u.setCountry(rs.getString("country"));
				u.setBirth(rs.getString("birth"));
				u.setBirthday(rs.getString("birthday"));
				json =JSONArray.fromObject(u);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				con.close();
				ps.close();
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();//异常捕获
			}
		}
    	return json;
    }//查询指定用户信息方法结束
    
    
    
}
