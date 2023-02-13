package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryDAO extends ConnectDB {
	//
	public List<Category> getCategoryByID(int Type) {
		/*
		 * Get danh mục từ Category theo ID truy�?n vào
		 * */
		List<Category> list = new ArrayList<Category>();
		String sql = "SELECT * From Category Where Type = " + Type + " and Code = '' and Status = '1' order by Sort";		
		//		
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Category c = new Category();
				c.setID(rs.getInt("ID"));
				c.setName(rs.getString("Name"));				
				list.add(c);				
				System.out.println("Name fieldtype: " + c.getName());
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
