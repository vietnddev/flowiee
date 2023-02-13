package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;

public class QuerySQL extends ConnectDB {

	private String myPath = "E:\\FLOWIEE\\Source\\flowiee\\src\\helper\\QuerySQL.txt";

	public String excute(String SQL) {
		String result = "";
		PreparedStatement pstm = null;
		try {
			pstm = con.prepareStatement(SQL);
			pstm.executeUpdate();
			result = "Thực thi thành công!";
		} catch (Exception e) {
			System.out.println(e.getCause());
			result = "Có lỗi xảy ra!";
		}
		return result;
	}

//	public String readTxtFromFile() {
//		String line = "";
//		try {
//			FileReader fr = new FileReader(myPath);
//			BufferedReader br = new BufferedReader(fr);
//			while (true) {
//				if (br.readLine() == null) {
//					break;
//				}
//				line += br.readLine() + "\n";
//			}
//		} catch (Exception e) {
//			System.out.println(e.getCause());
//		}
//		return line;
//	}

	public String readFile() {
		String s = "";
		try {
//	            FileInputStream fis = new FileInputStream(myPath);
//	            int i = 0;
//	            while ((i = fis.read()) != -1){
//	                s = s + (char) i;
//	            }

			FileReader input = new FileReader(myPath);
			int ch = input.read();
			while (ch != -1) {
				System.out.print((char) ch);
				ch = input.read();
			}
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
		return s;
	}

	public void writeTxtToFile(String txt) {
		try {
			// Ghi vào file nào?
			FileWriter fw = new FileWriter(myPath, true);
			// Chưa hiểu ý nghĩa của BufferedWriter
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(txt);

			bw.close();
			fw.close();
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
	}
}
