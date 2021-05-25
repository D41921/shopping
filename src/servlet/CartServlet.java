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
import bean.ItemBean;
import dao.DAOException;
import dao.ItemDAO;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// パラメータの解析：actionキーを取得して処理を分岐
		String action = request.getParameter("action");
		// actionキーが「show」またはパラメータがない場合はカート画面に遷移
		if (action == null || action.length() == 0 || action.equals("show")) {
			this.gotoPage(request, response, "cart.jsp");

		// actionキーが「add」の場合：カート内商品リストに商品を追加してカート画面に遷移（自画面遷移）
		} else if (action.equals("add")) {
			try {
				// リクエストパラメータを取得
				String itemCode = request.getParameter("item_code");
				int code = Integer.parseInt(itemCode);
				String quantityString = request.getParameter("quantity");
				int quantity = Integer.parseInt(quantityString);

				// セッションからカートを取得
				HttpSession session = request.getSession();
				CartBean cart = (CartBean) session.getAttribute("cart");
				// 取得したカートがnullの場合：初めて商品をカートに追加
				if (cart == null) {
					cart = new CartBean();
					session.setAttribute("cart", cart);
				}
				// 商品個番号の商品を取得
				ItemDAO dao = new ItemDAO();
				ItemBean bean = dao.findByPrimariKey(code);
				// カートに追加する
				cart.addCart(bean, quantity);
				// 自画面遷移
				this.gotoPage(request, response, "cart.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				request.setAttribute("message", "内部エラーが発生しました。");
				gotoPage(request, response, "errInternal.jsp");
			}

		// actionキーが「delete」の場合：カートから指定された商品を削除
		} else if (action.equals("delete")) {
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
			String itemCode = request.getParameter("item_code");
			int code = Integer.parseInt(itemCode);

			// カートから商品を削除
			cart.deleteCart(code);
			this.gotoPage(request, response, "cart.jsp");
		}
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
