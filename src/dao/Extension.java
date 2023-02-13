package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import model.DetailOrder;
import model.Notification;

public class Extension extends ConnectDB {
	// Số lượng noti mặc định hiển thị trên popup
	public String default_notification = "20";

	// Lấy danh sách thông báo
	public ArrayList<Notification> getListNotification(String n, String IDReceiver) {
		ArrayList<Notification> list = new ArrayList<Notification>();
		//
		String sql = "";
		if (n == "*") {
			sql = "SELECT notification.*, account.Name from notification " + "LEFT JOIN account "
					+ "ON notification.IDSender = account.ID " + "WHERE IDReceiver = 0 OR IDReceiver = " + IDReceiver
					+ " ORDER BY ID DESC";
		} else {
			sql = "SELECT notification.*, account.Name from notification " + "LEFT JOIN account "
					+ "ON notification.IDSender = account.ID " + "WHERE IDReceiver = 0 OR IDReceiver = " + IDReceiver
					+ " ORDER BY ID DESC LIMIT " + n;
		}
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Notification noti = new Notification();
				noti.setID(rs.getInt(1));
				noti.setIDSender(rs.getString("Name"));
				noti.setIDReceiver(rs.getString(3));
				noti.setType(rs.getInt(4));
				noti.setMessage(rs.getString(5));
				noti.setIsReaded(rs.getBoolean(6));
				noti.setCreated(rs.getString("Created"));
				noti.setIsSendMail(rs.getBoolean(8));
				noti.setIDOrders(rs.getInt("IDOrders"));
				list.add(noti);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy thông báo chưa đ�?c
	public int getNotificationUnRead(String IDReceiver) {
		ArrayList<Notification> list = new ArrayList<Notification>();
		//
		String sql = "SELECT notification.*, account.Name from notification " + "LEFT JOIN account "
				+ "ON notification.IDSender = account.ID " + "WHERE (IDReceiver = 0 OR IDReceiver = " + IDReceiver
				+ ") AND IsReaded = 0";
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Notification noti = new Notification();
				noti.setID(rs.getInt("ID"));
				list.add(noti);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.size();
	}

	// Thống kê số lượng sản phẩm đã bán trên channel ?
	public int getProductSoldOnChannel(int IDProduct, String Channel) {
		//
		String sql = "Select SUM(Quantity) as Quantity, detailorder.IDOrders, detailorder.IDProduct, detailorder.Name, detailorder.Quantity, "
				+ "orders.Channel " + "from detailorder " + "INNER JOIN orders "
				+ "ON orders.ID = detailorder.IDOrders " + "Where IDProduct = " + IDProduct + " and Channel = '"
				+ Channel + "'";
		//
		int quantity = 0;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				quantity = rs.getInt("Quantity");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return quantity;
	}

	// Thống kê số lượng sản phẩm theo Channel
	public int getChartChanel(String Channel) {
		//
		String sql = "SELECT COUNT(orders.ID) as Quantity, orders.ID as MaDH, detailorder.IDProduct, detailorder.Name, "
				+ " orders.Channel " + " FROM orders " + " INNER JOIN detailorder "
				+ " ON detailorder.IDOrders = orders.ID Where orders.Channel = '" + Channel + "'";
		//
		int quantity = 0;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				quantity = rs.getInt("Quantity");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quantity;
	}

	// Thống kê doanh thu theo năm và từng tháng
	public Double getLineChartPrice(String Month) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
		LocalDateTime now = LocalDateTime.now();
		String year = dtf.format(now);
		//
		String sql = "SELECT SUM(TotalMoney) as Total from orders Where year(orders.Date) = " + year
				+ " and month(orders.Date) = " + Month + "";
		//
		Double revenue = (double) 0;
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				revenue = rs.getDouble("Total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revenue;
	}

	// Lấy số lượng sản phẩm trong Kho theo ID
	public int getProductInStorageByID(int ID) {
		String sql = "SELECT Storage FROM Product Where ID = " + ID;
		//
		int storage = 0;
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				storage = rs.getInt("Storage");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storage;
	}
}
