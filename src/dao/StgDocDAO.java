package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import model.Account;
import model.Category;
import model.DocField_DocData;
import model.DocShare;
import model.DocShare_Account;
import model.DocType;
import model.Storage;

@Transactional
public class StgDocDAO extends ConnectDB {
	public List<DocField_DocData> getDocFieldDocData(int IDDoc) {
		/*
		 * Lấy danh sách các trư�?ng thông tin và data kèm theo khi vào xem chi tiết file
		 */
		List<DocField_DocData> list = new ArrayList<DocField_DocData>();

		String SQL = "Select docfield.id, docfield.name as NameDocField, docdata.ID as IDDocData, docdata.Value, docdata.iddoc "
				+ "From docfield left join docdata " + "On docfield.id = docdata.IDDocField " + "Where docdata.IDDoc = "
				+ IDDoc;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DocField_DocData doc = new DocField_DocData();
				doc.setIDDocData(rs.getInt("IDDocData"));
				doc.setNameDocField(rs.getString("NameDocField"));
				doc.setValue(rs.getString("Value"));

				list.add(doc);
				System.out.println("NameDocField: " + doc.getNameDocField() + " // " + "Value: " + doc.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Storage> getIDStgdocByIDDocType(int IDDocType) {
		/*
		 * Lấy id của file theo id của loại tài liệu Khi thêm file 1 trư�?ng thông tin
		 * của loại tài liệu -> Cần chạy function này để insert value rỗng vào docdata
		 */
		List<Storage> list = new ArrayList<Storage>();

		String SQL = "Select ID From Storage Where IDDocType = " + IDDocType;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Storage stgdoc = new Storage();
				stgdoc.setID(rs.getInt("ID"));
				list.add(stgdoc);
				System.out.println("IDStgdoc by IDDocType: " + stgdoc.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getNameDocTypeByID(int IDDocType) {
		/*
		 * Lấy tên của loại tài liệu dựa theo ID được truy�?n vào
		 */
		String SQL = "Select Name From DocType Where ID = " + IDDocType;
		String nameDocType = "";
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				nameDocType = rs.getString("Name");
				System.out.println("Name doctype by id: " + nameDocType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nameDocType;
	}

	public String getStgNameDocByID(int IDDoc) {
		/*
		 * Lấy tên của loại tài liệu dựa theo ID được truy�?n vào
		 */
		String SQL = "Select StgName From Storage Where ID = " + IDDoc;
		String docName = "";
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				docName = rs.getString("StgName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docName;
	}

	public ArrayList<Integer> getListDocShareByIDUser(int IDUser) {
		/*
		 * Lấy danh sách iddoc theo iduser được phân quy�?n từ bảng docshare
		 */
		ArrayList<Integer> list = new ArrayList<Integer>();

		String SQL = "Select IDDoc From DocShare Where IDUser = " + IDUser;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("IDDoc"));
			}
			// System.out.println("List docshare: " + list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Storage> getListDoc(int IDParent, int IDUser) {
		/*
		 * Lấy danh sách doc theo id parent được truy�?n vào Màn hình root -> id = 0
		 */
		ArrayList<Storage> list = new ArrayList<Storage>();
		String SQL = "Select Storage.* " + "From Storage " + "Left Join Docshare On Storage.ID = Docshare.IDDoc "
				+ "Where Storage.IDParent = " + IDParent + " and " + "(Docshare.IDUser = " + IDUser
				+ " or Storage.Author = " + IDUser + ")";
		System.out.println("SQL: " + SQL);
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Storage stg = new Storage();
				stg.setID(rs.getInt("ID"));
				stg.setIDParent(rs.getInt("IDParent"));
				stg.setType(rs.getInt("Type"));
				stg.setName(rs.getString("Name"));
				stg.setStgName(rs.getString("StgName"));
				stg.setDescribes(rs.getString("Describes"));
				stg.setExtension(rs.getString("Extension"));
				stg.setSize(rs.getInt("Size"));
				stg.setAuthor(rs.getString("Author"));
				stg.setPath(rs.getString("Path"));
				stg.setIDDocType(rs.getInt("IDDocType"));
				list.add(stg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Account> GetListAccountDuocPhanQuyen(int IDDoc) {
		/*
		 * Check phân quy�?n trên tài liệu
		 */
		ArrayList<Account> list = new ArrayList<Account>();
		String SQL = "select docshare.id, docshare.IDUser as IDUser, docshare.IDDoc, account.name as Name "
				+ "from docshare " + "join account on account.id = docshare.IDUser " + "where docshare.iddoc = "
				+ IDDoc;
		System.out.println("SQL: " + SQL);
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Account acc = new Account();
				acc.setID(rs.getInt("IDUser"));
				acc.setName(rs.getString("Name")); // tên user
				list.add(acc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Xóa chia sẽ quy�?n xem tài liệu theo IDDoc
	public void DeleteDocShareByIDUser(int IDDoc) {
		String sql = "DELETE FROM DocShare Where IDDoc = " + IDDoc;
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Test
	public int getIDDocShare() {
		String SQL = "SELECT ID FROM DocShare2 order by Id desc limit 1";
		int ID = 0;
		try {
			PreparedStatement pstm = con.prepareStatement(SQL);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ID = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;
	}

	// Test
	public void insertABC() {
		int i = getIDDocShare() + 1;
		while (true) {
			String sql = "INSERT INTO DocShare2 VALUE (" + i + ", " + i + ", " + i + ")";
			try {
				PreparedStatement pstm = con.prepareStatement(sql);
				pstm.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(i);
			i++;
		}
	}
	
	// Lấy danh sách tên doctype -> báo cáo
	public List<DocType> report_getListIDDocType() {
		List<DocType> list = new ArrayList<DocType>();
		String sql = "Select ID, Name From Doctype Where Status = true";
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DocType doc = new DocType();
				doc.setID(rs.getInt("ID"));
				doc.setName(rs.getString("Name"));
				list.add(doc);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy số lượng doctype đã được sử dụng -> báo cáo
	public List<Integer> report_getQuatityDocTypeUsed() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			for (int i = 0; i < report_getListIDDocType().size(); i++) {
				String sql = "Select count(ID) as ID from storage where IDDocType = " + report_getListIDDocType().get(i).getID();
				PreparedStatement pstm = con.prepareStatement(sql);
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {
					list.add(rs.getInt("ID"));
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// Khai báo danh sách màu cho báo cáo biểu đồ Kho
	public List<String> report_getListColor(int index){
		// Khai báo list màu hệ thống
		List<String> list = new ArrayList<String>();
		list.add("'#66CCFF'"); 
		list.add("'#66CC33'");
		list.add("'#FF9999'");
		list.add("'#FF9933'");
		list.add("'#CC9999'");
		list.add("'#9999FF'");
		list.add("'#999933'");
		list.add("'#009999'");
		list.add("'#9966FF'");
		list.add("'#3333FF'");
		list.add("'#000055'");		
		list.add("'#FF3333'");
		list.add("'#8DEEEE'");
		list.add("'#00CD66'");
		list.add("'#8B658B'");		
		
		// Lấy ra list màu theo số lượng doctype được truy�?n vào (index)
		List<String> listResult = new ArrayList<String>();
		for (int i = 0; i < index; i++) {
			listResult.add(list.get(i));			
		}
		
		return listResult;
	}
}