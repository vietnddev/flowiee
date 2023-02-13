package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import model.Role;

public class RoleDAO extends ConnectDB {

	// Khai báo mã quy�?n - Sản phẩm
	public int ID_MP = 6; // Page SP
	public int ID_MPI = 8; // Thêm SP
	public int ID_MPU = 7; // Cập nhật SP
	// public int ID_MPD = ; // Xóa SP
	public int ID_MPd = 27; // Xem chi tiết SP
	
	// Khai báo mã quy�?n - Kho lưu trữ
	public int ID_MS = 28;

	// Khai báo mã quy�?n - Khách hàng
	public int ID_MCu = 10; // ok
	public int ID_MCuI = 13; // Thêm KH
	public int ID_MCuU = 14; // Cập nhật KH
	public int ID_MCuD = 15; // Xóa Kh

	// Khai báo mã quy�?n - �?ơn hàng
	public int ID_MO = 9; // Page �?ơn
	public int ID_MOU = 11; // Cập nhật �?ơn
	public int ID_MOD = 12; // Xóa đơn
	public int ID_MOd = 26; // Xem chi tiết đơn
	public int ID_MOUn = 22; // Xem list Ch�? xác nhận
	public int ID_MODe = 23; // Xem list �?ang giao
	public int ID_MOCo = 24; // Xem list �?ã hoàn thành
	public int ID_MOCa = 25; // Xem list �?ã hủy

	// Khai báo mã quy�?n - Tài khoản hệ thống
	public int ID_MA = 1; // Page Account
	public int ID_MAI = 2; // Thêm Acc
	public int ID_MAU = 3; // Cập nhật Acc
	public int ID_MAD = 4; // Xóa Acc

	// Khai báo mã quy�?n - Log
	public int ID_ML = 5;

	// Khai báo mã quy�?n - Danh mục hệ th
	public int ID_MCa = 16;

	// Khai báo mã quy�?n - Cấu hình hệ thống
	public int ID_MCo = 17;

	// Khai báo mã quy�?n - Role
	public int ID_MR = 18;

	public boolean enableRole = false;

	// Xóa quy�?n user trước khi update
	public void deleteUserRole(int IDUser) {		
		String sql = "DELETE FROM Userrole Where IDUser = " + IDUser;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	public int checkRole(int IDUser, int IDRole) {
		String sql = "SELECT COUNT(ID) as ID  FROM Userrole Where IDUser = " + IDUser + " AND IDRole = " + IDRole;
		//
		int ID = 0;
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ID = rs.getInt("ID");
				//System.out.println("checkRole: " + ID);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;
	}
}