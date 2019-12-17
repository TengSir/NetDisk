package edu.hbuas.netdisk.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.hbuas.netdisk.config.NetDiskConfig;

/**
 * userdao是专门操作用户对象的数据库操作类
 * @author Lenovo
 *
 */
public class UserDAO {
	/**
	 * 注册用户的方法
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean registerUser(User user)  throws Exception{
		
			Class.forName(NetDiskConfig.jdbcDriverClass);
			Connection con=DriverManager.getConnection(NetDiskConfig.jdbcURL,NetDiskConfig.jdbcUsername, NetDiskConfig.jdbcPassword);
			
			PreparedStatement  pre=con.prepareStatement("insert into user(username,password,sex,age,email) values(?,?,?,?,?)");
			pre.setString(1, user.getUsername());
			pre.setString(2, user.getPassword());
			pre.setString(3, user.getSex());
			pre.setInt(4, user.getAge());
			pre.setString(5, user.getEmail());
			
			int result=pre.executeUpdate();
			pre.close();
			con.close();
			return result>0?true:false;
		
	}
	/**
	 * 根据传入的用户名和密码查询用户对象
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User  login (String username,String password)  throws Exception{
		User user=null;
		Class.forName(NetDiskConfig.jdbcDriverClass);
		Connection con=DriverManager.getConnection(NetDiskConfig.jdbcURL,NetDiskConfig.jdbcUsername, NetDiskConfig.jdbcPassword);
		
		PreparedStatement  pre=con.prepareStatement("select * from user where username=? and password=?");
		pre.setString(1, username);
		pre.setString(2, password);
		ResultSet rs=pre.executeQuery();
		
		if(rs.next()) {
			user=new User();
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setSex(rs.getString("sex"));
			user.setAge(rs.getInt("age"));
			user.setEmail(rs.getString("email"));
		}
		
		
		return user;
	
}

}
