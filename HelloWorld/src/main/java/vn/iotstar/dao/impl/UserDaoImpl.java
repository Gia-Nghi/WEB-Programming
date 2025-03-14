package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vn.iotstar.configs.DBConnectMySQL;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.models.UserModel;

public class UserDaoImpl extends DBConnectMySQL implements IUserDao{

	public Connection conn = null;
	public PreparedStatement ps = null;
	public ResultSet rs = null;
	
	
	@Override
	public List<UserModel> findAll() {
		
		String sql = "Select * from users";
		
		List<UserModel> list = new ArrayList<>(); //Tao 1 list de truyen du lieu
		
		try {
			conn = super.getDatabaseConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				list.add(
						new UserModel (
								rs.getInt("id"), 
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getString("email"), 
								rs.getString("fullname"), 
								rs.getString("images")
								)
						);
			}
			return list;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	@Override
	public UserModel findById(int id) {
		
String sql = "Select * from users where id = ?";
UserModel user = null;
		
		try {
			conn = super.getDatabaseConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,  id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
						user = new UserModel (
								rs.getInt("id"), 
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getString("email"), 
								rs.getString("fullname"), 
								rs.getString("images")
								);
						
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return user;
	}

	@Override
	public void insert(UserModel user) {
		
		String sql = "INSERT INTO users(id, username, email, password, fullname, images) VALUES (?,?,?,?,?,?)";
		
		
		try {
			conn = super.getDatabaseConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, user.getId());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getFullname());
			ps.setString(6, user.getImages());
			
			ps.executeUpdate();
			
	 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main (String [] args) {
		UserDaoImpl userDao = new UserDaoImpl();
		Scanner scanner = new Scanner(System.in);
		// System.out.println (userDao.findOne(1));
		
		userDao.insert(new UserModel(3, "abc1", "345345", "a@gmail.com", "abc123", ""));
		
		List<UserModel> list = userDao.findAll();
		for (UserModel user : list) {
			System.out.println(user);
		}
		
		
		System.out.print("Nhập ID người dùng bạn muốn tìm: ");
        int id = scanner.nextInt();
		
		UserModel user = userDao.findById(id);
	    if (user != null) {
	        System.out.println(user);
	    } else {
	        System.out.println("Người dùng với id" + id + "không tồn tại.");
	    }
		
	    scanner.close();
	}
	
}
