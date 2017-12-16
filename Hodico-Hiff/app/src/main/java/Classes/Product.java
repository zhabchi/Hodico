package Classes;

public class Product {

	private int id;
	private String product;
	private int price;

	public Product() {

	}

	public Product(int id, String product, int price) {
		this.id = id;
		this.product = product;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
