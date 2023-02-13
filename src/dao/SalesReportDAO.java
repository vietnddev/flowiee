package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalesReportDAO extends ConnectDB {
	// Lấy danh sách kênh bán hàng
	public List<String> getListChannel() {
		List<String> list = new ArrayList<String>();
		try {
			String sql = "Select Name from Category Where Type = " + 5
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort";
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy doanh thu từ các kênh bán hàng
	public List<Double> getRevenueByChannel() {
		List<Double> list = new ArrayList<Double>();
		try {			
			for (int i = 0; i < getListChannel().size(); i++) {				
				String SQL = "Select SUM(TotalMoney) as TotalMoney From Orders Where Channel = '" + getListChannel().get(i) + "'";
				PreparedStatement pstm = con.prepareStatement(SQL);
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {					
					list.add(rs.getDouble("TotalMoney"));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		return list;
	}
}
