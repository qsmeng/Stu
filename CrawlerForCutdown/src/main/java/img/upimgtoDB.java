package img;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.JdbcPoll;

public class upimgtoDB {
	// 向数据库中添加一条记录
	public void Insert() {
		try {
			Connection conn = JdbcPoll.getConn();
			String sql = "insert into tb_photo(name,photo)values(?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			File f = new File("D:/1.png");

			FileInputStream input = new FileInputStream(f);
			ps.setString(1, "杰克逊");
			ps.setBinaryStream(2, input, (int) f.length());
			ps.executeUpdate();
			System.out.println("插入成功");
			ps.close();
			input.close();
		} catch (SQLException e) {
			System.out.println("SQL异常");
			e.printStackTrace();
		} catch (IOException ie) {
			System.out.println("IO异常");
			ie.printStackTrace();
		}
	}

	// 从数据库中读取图片数据
	public void Read() {
		try {
			Connection conn = JdbcPoll.getConn();
			String sql = "select photo from  tb_photo where id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, 1);
			ResultSet rs = ps.executeQuery();
			byte[] b = new byte[10240 * 10];

			while (rs.next()) {
				// 获取photo字段的图片数据
				InputStream in = rs.getBinaryStream("photo");
				// 将数据存储在字节数组b中
				in.read(b);
				// 从数据库获取图片保存的位置
				File f = new File("D:/2.jpg ");
				FileOutputStream out = new FileOutputStream(f);
				out.write(b, 0, b.length);
				out.close();
				System.out.println("成功获取图片");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}
