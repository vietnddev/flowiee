package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Orders;

public class OrderDAO extends ConnectDB {
	private String ID_UnConfirm = "1";
	private String ID_Delivery = "2";
	private String ID_Completed = "3";
	private String ID_Cancel = "4";
	private String name_UnConfirm = "Ch�? xác nhận";
	private String name_Delivery = "�?ang giao hàng";
	private String name_Completed = "�?ã hoàn thành";
	private String name_Cancel = "�?ã hủy";

	public List<Orders> getListByStatus(String status, String IDOrder) {
		/*
		 * Get danh sách đơn hàng theo trạng thái truy�?n vào
		 */
		List<Orders> list = new ArrayList<Orders>();
		String sql = "";

		if (!IDOrder.equals("")) {
			sql = "SELECT * From Orders Where ID = " + IDOrder;
		} else {
			if (status == "") {
				sql = "SELECT * From Orders Order by Date DESC";
			} else {
				sql = "SELECT * From Orders Where Status = '" + status + "' Order by Date DESC";
			}
		}

		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Orders o = new Orders();
				o.setID(rs.getInt("ID"));
				o.setCode(rs.getString("Code"));
				o.setIDCustomer(rs.getInt("IDCustomer"));
				o.setName(rs.getString("Name"));
				o.setPhone(rs.getString("Phone"));
				o.setEmail(rs.getString("Email"));
				o.setAddress(rs.getString("Address"));
				o.setNote(rs.getString("Note"));
				o.setDate(rs.getString("Date"));
				o.setTotalMoney(rs.getDouble("TotalMoney"));
				o.setSales(rs.getString("Sales"));
				o.setChannel(rs.getString("Channel"));
				if (rs.getString("status").equals(ID_UnConfirm)) {
					o.setStatus(name_UnConfirm);
				} else if (rs.getString("status").equals(ID_Delivery)) {
					o.setStatus(name_Delivery);
				} else if (rs.getString("status").equals(ID_Completed)) {
					o.setStatus(name_Completed);
				} else if (rs.getString("status").equals(ID_Cancel)) {
					o.setStatus(name_Cancel);
				}
				list.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
