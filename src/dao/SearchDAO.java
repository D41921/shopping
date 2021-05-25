package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ItemBean;

public class SearchDAO {

	private Connection con;

	public SearchDAO() throws DAOException {
		this.getConnection();
	}

	private void getConnection() throws DAOException {
		// データベース接続情報の設定
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql:sample";
		String user = "student";
		String password = "himitu";

		try {
			Class.forName(driver);
			this.con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new DAOException("データベースの接続に失敗しました。");
		}

	}

	@SuppressWarnings({ "resource", "null" })
	public List<ItemBean> findAllItem(String search) throws DAOException {

		// データベース接続関連オブジェクトの初期化
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from item where name like '% ? %' ";
			pstmt.setString(1, search);
			pstmt = this.con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<ItemBean> list = new ArrayList<ItemBean>();
			while (rs.next()) {
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				ItemBean bean = new ItemBean(code, name, price);
				list.add(bean);
			}

			//商品リストを返却
			return list;

		}catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				this.close();
			} catch (Exception e) {
				throw new DAOException("リソースの解放に失敗しました。");
			}
		}


	}

	private void close() throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		if (this.con != null) {
			this.con.close();
			this.con = null;
		}
	}


}
