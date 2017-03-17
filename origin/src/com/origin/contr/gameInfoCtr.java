package com.origin.contr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


import com.origin.model.gameInfo;
import com.origin.util.MysqlLink;
import com.origin.util.originUtil;

public class gameInfoCtr extends MysqlLink{
	
	 private originUtil Util =  new originUtil();
	
	//添加游戏
       public boolean add(gameInfo g){
    	   boolean isAdd = false;
    	String sql = "insert into gameinfo(gamename,price,imgsrc,sale,date,company,descr,classify)values(?,?,?,?,?,?,?,?)";
       	StringBuffer sqlBuffer = new StringBuffer(sql);
       	con = getMysqlCon();
       	try {
			ps = con.prepareStatement(sqlBuffer+"");
			ps.setString(1,g.getGamename());
			ps.setString(2,g.getPrice()+"");
			ps.setString(3,g.getImgsrc());
			ps.setString(4,g.getSale()+"");
			ps.setString(5,g.getDate());
			ps.setString(6,g.getCompany());
			ps.setString(7,g.getDescr());
			ps.setString(8,g.getClassify());
			int num = ps.executeUpdate();
			if(num > 0){
				isAdd = true;
			}else{
				isAdd = false;
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
       	
    	   return isAdd;
       }//添加游戏信息完成
      
       
       //删除游戏信息
       
      public boolean removeGame(HttpServletRequest request){
    	  boolean isRemove = false;
    	  String id = Util.getEncoding(request.getParameter("id"));
      	con = getMysqlCon();
      	String sql =  " delete from gameinfo where id=?";
      	StringBuffer sqlBuffer = new StringBuffer(sql);
      	try {
  			ps = con.prepareStatement(sqlBuffer+"");
  			ps.setString(1, id);
  			int num = ps.executeUpdate();
  			isRemove = num>0?true:false;
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
    	  return isRemove;
      }//删除游戏方法结束
      
      
      
      //修改游戏信息
      public boolean updata(HttpServletRequest request){
      	boolean updataWat = false; 
    	    String id = Util.getEncoding(request.getParameter("id"));
      	String sql = "update gameinfo set ";//声明sql语句容器
      	ArrayList<String> str = new ArrayList<String>();//声明用户请求参数名容器;
      	int count;
      	
      	Enumeration<String> pNames=request.getParameterNames();//获取用户请求参数名称,(返回枚举类型);
      	
      	while(pNames.hasMoreElements()){//循环读出用户请求参数名称
      	    String name=(String)pNames.nextElement();
      	    boolean or = name.equals("gamename")||name.equals("price")||name.equals("imgsrc")||name.equals("sale")||name.equals("date")||name.equals("company")||name.equals("descr")||name.equals("classify");
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
  				
  			String	UserData = Util.getEncoding(request.getParameter(str.get(count-1)));
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
      }//修改游戏信息方法结束
      
      
      
      
      
      
      
      //查询10条游戏数据记录
      public ArrayList<gameInfo> search(HttpServletRequest request){
      	int page = Integer.parseInt(request.getParameter("page"));
      	int count = searchCount(request);
      	ArrayList<gameInfo> arrList = null;
      	int pageId = page==0?0:page*10;
      	if(!(pageId>=count)){
          	con = getMysqlCon();    		
      		String sql = "select * from gameinfo limit "+pageId+",10";
      		StringBuffer sqlBuffer = new StringBuffer(sql);
      		try {
  				ps = con.prepareStatement(sqlBuffer+"");
  				rs = ps.executeQuery();
  				arrList = new ArrayList<gameInfo>(); 
  				while (rs.next()) {
  					gameInfo xInfo = new gameInfo(); 
  					xInfo.setId(rs.getInt("id"));
  					xInfo.setGamename(rs.getString("gamename"));
  					xInfo.setPrice(rs.getDouble("price"));
  					xInfo.setImgsrc(rs.getString("imgsrc"));
  					xInfo.setSale(rs.getInt("sale"));
  					xInfo.setDate(rs.getString("date"));
  					xInfo.setCompany(rs.getString("company"));
  					xInfo.setDescr(rs.getString("descr"));
  					xInfo.setClassify(rs.getString("classify"));
  					arrList.add(xInfo);
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
      	}else {
  			//System.out.println("已是最后一页了");
      		arrList=null;
  		}
  		return arrList;
      }//查询方法结束(10条数据)
      
      
      //查询游戏记录的总数
      public int searchCount(HttpServletRequest request){
      	int schCount = 0;
      	String str = request.getParameter("str")==null?null: Util.getEncoding(request.getParameter("str"));
      	String sql = "SELECT count(*) FROM gameinfo";
      	sql = ((str == null)?sql:("SELECT count(*) FROM gameinfo WHERE  gamename like '%"+str+"%' or classify like '%"+str+"%'"));
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
      
      
      //查询包含字符的游戏数据
      public ArrayList<gameInfo> searchChar(HttpServletRequest request){
      	int page = Integer.parseInt(request.getParameter("page"));
      	int count = searchCount(request);
      	int pageId = page==0?0:page*10;
      	ArrayList<gameInfo> arrList = null;
      	String str = Util.getEncoding(request.getParameter("str"));
      	String sql = "SELECT * FROM gameinfo WHERE gamename like '%"+str+"%' or classify like '%"+str+"%' limit "+pageId+",10 ";
      	StringBuffer sqlBuffer = new StringBuffer(sql);
      	if(!(pageId>=count)){
      	con = getMysqlCon();
      	try {
  			ps = con.prepareStatement(sqlBuffer+"");
  			rs = ps.executeQuery();
  		   arrList = new ArrayList<gameInfo>(); 
  			while (rs.next()) {
  				gameInfo xInfo = new gameInfo(); 
					xInfo.setId(rs.getInt("id"));
					xInfo.setGamename(rs.getString("gamename"));
					xInfo.setPrice(rs.getDouble("price"));
					xInfo.setImgsrc(rs.getString("imgsrc"));
					xInfo.setSale(rs.getInt("sale"));
					xInfo.setDate(rs.getString("date"));
					xInfo.setCompany(rs.getString("company"));
					xInfo.setDescr(rs.getString("descr"));
					xInfo.setClassify(rs.getString("classify"));
					arrList.add(xInfo);
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
      	
      	}else {
  			//System.out.println("已是最后一页了");
      		arrList=null;
  		}
      	return arrList;
      }//查询包含字符的用户函数
      
      
      
    //更具ID或游戏名称查询用户信息
      public ArrayList<gameInfo> isUserInfo(HttpServletRequest request){
    	 ArrayList<gameInfo> arrList = null;
    	 String str = "";
      	String id = request.getParameter("id")==null?null:Util.getEncoding(request.getParameter("id"));
      	String userName = request.getParameter("gamename")==null?null: Util.getEncoding(request.getParameter("gamename"));
      	String classify = request.getParameter("classify")==null?null:Util.getEncoding(request.getParameter("classify"));
      	String sql = null;
      	/* 	String sql = "SELECT * FROM gameinfo WHERE gamename=?";
      	sql  = id==null?sql:"SELECT * FROM gameinfo WHERE id=?";*/
      	if(id==null&&userName==null){
      		sql = "SELECT * FROM gameinfo WHERE classify=?";
      		str = classify;
      	}else if(id==null&&classify==null){
      		sql = "SELECT * FROM gameinfo WHERE gamename=?";
      		str = userName;
      	}else if (classify==null&&userName==null) {
			sql  = "SELECT * FROM gameinfo WHERE id=?";
			str = id;
		}else if(id != null){
			sql  = "SELECT * FROM gameinfo WHERE id=?";
			str = id;
		}else if(userName !=null){
			sql = "SELECT * FROM gameinfo WHERE gamename=?";
      		str = userName;
		}else if(classify != null){
			sql = "SELECT * FROM gameinfo WHERE classify=?";
      		str = classify;
		}
      	StringBuffer sqlBuffer = new StringBuffer(sql);
      	con = getMysqlCon();
      	try {
  			ps = con.prepareStatement(sqlBuffer+"");
  			ps.setString(1,str);
  			rs = ps.executeQuery();
  			arrList = new ArrayList<gameInfo>(); 
  			while(rs.next()) {
  				gameInfo xInfo = new gameInfo(); 
					xInfo.setId(rs.getInt("id"));
					xInfo.setGamename(rs.getString("gamename"));
					xInfo.setPrice(rs.getDouble("price"));
					xInfo.setImgsrc(rs.getString("imgsrc"));
					xInfo.setSale(rs.getInt("sale"));
					xInfo.setDate(rs.getString("date"));
					xInfo.setCompany(rs.getString("company"));
					xInfo.setDescr(rs.getString("descr"));
					xInfo.setClassify(rs.getString("classify"));
					arrList.add(xInfo);
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
      	return arrList;
      }//查询指定游戏信息方法结束
       
      
      
       
}
