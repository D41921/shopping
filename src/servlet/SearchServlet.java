package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ItemBean;
import dao.DAOException;
import dao.SearchDAO;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//リクエストパラメータの文字コードの設定
		request.setCharacterEncoding("utf-8");

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		//リクエストパラメータの取得
		String search = request.getParameter("search");
        request.setAttribute(search, out);

		try {

			if (search == null || search.length() == 0) {
				//検索バーの内容が未入力
				out.println("文字を入力して下さい");
				return;
			}
			//モデルを使って検索結果を取得する
			SearchDAO dao = new SearchDAO();
			List<ItemBean> list = dao.SearchItems(search);

			//searchlistをリクエストスコープに入れてjspへフォワードする
			request.setAttribute("items", list);


			gotoPage(request, response, "/list.jsp");
			//RequestDispatcher rd = request.getRequestDispatcher("/Search.jsp");
			//rd.forward(request, response);

		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("message", "内部エラーが発生しました");
			RequestDispatcher rd = request.getRequestDispatcher("/errInternal.jsp");
			rd.forward(request, response);

		}
		doGet(request, response);
		}

		private void gotoPage(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
			RequestDispatcher dispatcher = request.getRequestDispatcher(page);
			dispatcher.forward(request, response);
	}

}
