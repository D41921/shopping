package bean;

/**
 * itemテーブルの1レコードを管理するBeanクラス
 * @author tutor
 */
public class ItemBean {

	/**
	 * フィールド
	 */
	private int code;     // 商品番号
	private String name;  // 商品名
	private int price;    // 価格
	private int quantity; // 数量
	private int category_code;
	
	

	

	public int getCategory_code() {
		return category_code;
	}

	public void setCategory_code(int category_code) {
		this.category_code = category_code;
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public ItemBean() {

	}

	/**
	 * コンストラクタ
	 * @param code  商品番号
	 * @param name  商品名
	 * @param price 価格
	 */
	public ItemBean(int code, String name, int price) {
		this.code = code;
		this.name = name;
		this.price = price;
	}

	/**
	 * コンストラクタ
	 * @param code     商品番号
	 * @param name     商品名
	 * @param price    価格
	 * @param quantity 数量
	 */
	public ItemBean(int code, String name, int price, int quantity) {
		this(code, name, price);
		this.quantity = quantity;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
