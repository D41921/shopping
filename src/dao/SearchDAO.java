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

	public SearchDAO() throws DAOException{
		getConnection();
	}

	public List<ItemBean> SearchItems(String search) throws DAOException {
		if(con == null)
			getConnection();

		// データベース接続関連オブジェクトの初期化
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			//あいまい検索の入力をSQL文でデータベースに送信
			String sql = "select * from item where name like ? ";
			pstmt = this.con.prepareStatement(sql);
			// プレースホルダを設定
			pstmt.setString(1,  "%" + search  +"%");
			// SQLの実行と結果セットの取得
			rs = pstmt.executeQuery();
			// 結果セットから商品リストを取得
			List<ItemBean> list = new ArrayList<ItemBean>();
			while (rs.next()) {
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int categoryCode = rs.getInt("category_code");

				ItemBean bean = new ItemBean(code, name, price,categoryCode);
				list.add(bean);
			}
			// 商品リストを返却
			return list;

		} catch (SQLException e) {
			//例外処理
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


	private void getConnection() throws DAOException {

		/**
		 * データベース接続情報
		 */
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

	private void close() throws SQLException {
		if (this.con != null) {
			this.con.close();
			this.con = null;
		}
	}

}
