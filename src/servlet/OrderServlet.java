package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.CartBean;
import bean.CustomerBean;
import dao.DAOException;
import dao.OrderDAO;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストの文字コードを設定
		request.setCharacterEncoding("utf-8");

		// セッションからカートを取得
		HttpSession session = request.getSession(false);	// すでにセッションに登録されている属性を取得するので引数はfalse

		// セッションがない場合：不正なアクセスが含まれている場合もあるのでエラーページに強制的に遷移
		if (session == null) {
			request.setAttribute("message", "セッションが切れています。もう一度トップページより操作してください。");
			this.gotoPage(request, response, "errInternal.jsp");
			return;
		}

		// カートがない場合：不正アクセスである可能性があるのでエラーページに強制的に遷移
		CartBean cart = (CartBean) session.getAttribute("cart");
		if (cart == null) {
			request.setAttribute("message", "正しく操作してください。");
			this.gotoPage(request, response, "errInternal.jsp");
			return;
		}

		// リクエストパラメータの取得
		String action = request.getParameter("action");

		// actionキーが「input_customer」またはパラメータが存在しない場合：お客様情報入力画面に遷移
		if (action == null || action.length() == 0 || action.equals("input_customer")) {
			this.gotoPage(request, response, "customerInfo.jsp");

		// actionキーが「confirm」の場合：入力情報確認画面に遷移
		} else if (action.equals("confirm")) {
			// リクエストパラメータの取得
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String tel1 = request.getParameter("tel1");
			String tel2 = request.getParameter("tel2");
			String tel3 = request.getParameter("tel3");
			String email1 = request.getParameter("email1");
			String email2 = request.getParameter("email2");
			CustomerBean customer = new CustomerBean();

			//何も入力しなかった場合の例外処理
			if (name == null || name.length() == 0 || address == null || address.length() == 0 ||
					tel1 == null || tel1.length() == 0 || tel2 == null || tel2.length() == 0 ||
					tel3 == null || tel3.length() == 0 ||
					email1 == null || email1.length() ==0 || email2 == null || email2.length() == 0) {
				//未入力データあり
				request.setAttribute("message", "顧客情報を入力してください。");
				this.gotoPage(request, response, "/errInternal.jsp");
				return;
			}

			try {
				@SuppressWarnings("unused")
				//整数以外をはじくためにｔｒｙ内でのみ文字列をint型に変換
				int intTel1 = Integer.parseInt(tel1);
				@SuppressWarnings("unused")
				int intTel2 = Integer.parseInt(tel2);
				@SuppressWarnings("unused")
				int intTel3 = Integer.parseInt(tel3);

				//実際にスコープに登録するのは文字型(String)にする→0から始まる数値も認識させるため
				@SuppressWarnings("unused")
				String tel = tel1 + "-" + tel2 + "-" + tel3;
				customer.setTel(tel);

				String email = email1 + "@" + email2;
				customer.setEmail(email);


				// セッションスコープに顧客情報を登録
				session.setAttribute("customer", customer);
				// 確認画面に遷移
				//this.gotoPage(request, response, "/confirm.jsp");

				}catch (Exception e) {
				request.setAttribute("message", "不正な値が入力されました。");
				this.gotoPage(request, response, "/errInternal.jsp");
				return;
				}

			try {
				@SuppressWarnings("unused")
				int intName = Integer.parseInt(name);
				request.setAttribute("message", "不正な値が入力されました。");
				this.gotoPage(request, response, "/errInternal.jsp");
				return;

				}catch (Exception e) {
				customer.setName(name);
				//セッションスコープに顧客情報を登録
				session.setAttribute("customer", customer);
				//画面遷移
				//this.gotoPage(request, response, "/confirm.jsp");
				}

			try {
				@SuppressWarnings("unused")
				int intAddress = Integer.parseInt(address);
				request.setAttribute("message", "不正な値が入力されました。");
				this.gotoPage(request, response, "/errInternal.jsp");
				return;
			}catch(Exception e) {
				customer.setAddress(address);
				//セッションスコープに顧客情報を登録
				session.setAttribute("customer", customer);
				//確認画面に遷移
				this.gotoPage(request, response, "/confirm.jsp");
			}



		// actionキーが「order」の場合：完了画面に遷移
		} else if (action.equals("order")) {
			// 顧客情報を取得
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			// 顧客情報がない場合：：不正アクセスである可能性があるのでエラーページに強制的に遷移
			if (customer == null) {
				request.setAttribute("message", "正しく操作してください。");
				this.gotoPage(request, response, "/errInternal.jsp");
				return;
			}

			try {
				// 注文を確定
				OrderDAO dao = new OrderDAO();
				int orderNumber = dao.saveOrder(customer, cart);
				// リクエストスコープに注文番号を登録
				request.setAttribute("orderNumber", orderNumber);
				// 完了画面に遷移
				this.gotoPage(request, response, "/order.jsp");
			} catch (DAOException e) {
				e.printStackTrace();
				request.setAttribute("message", "内部エラーが発生しました。");
				this.gotoPage(request, response, "/errInternal.jsp");
			}
		} else {
			request.setAttribute("message", "正しく操作してください。");
			this.gotoPage(request, response, "/errInternal.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * 指定されたURLに遷移する。
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param page 遷移先URL
	 * @throws ServletException
	 * @throws IOException
	 */
	private void gotoPage(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}
}
