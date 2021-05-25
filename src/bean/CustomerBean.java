package bean;

/**
 * customerテーブルの1件のレコードを管理するBeanクラス
 * @author tutor
 */
public class CustomerBean {

	/**
	 * フィールド
	 */
	private int code;       // 顧客番号
	private String name;    // 顧客氏名
	private String address; // 住所
	private String tel;     // 電話番号
	private String email;   // 電子メールアドレス

	/**
	 * デフォルトコンストラクタ
	 */
	public CustomerBean() {

	}

	/**
	 * コンストラクタ
	 * @param code    顧客番号
	 * @param name    顧客氏名
	 * @param address 住所
	 * @param tel     電話番号
	 * @param email   電子メールアドレス
	 */
	public CustomerBean(int code, String name, String address, String tel, String email) {
		this.code = code;
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.email = email;
	}

	/**
	 * アクセサメソッド群
	 */

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
