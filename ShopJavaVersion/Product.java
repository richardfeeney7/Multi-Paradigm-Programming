package ShopJavaVersion;

public class Product {
	private String name; // Private means hidden away from things outside of the class
	private double price;

	public Product(String name, double price) {
		this.name = name; // Belongs to the object
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + "]";
	}
}
