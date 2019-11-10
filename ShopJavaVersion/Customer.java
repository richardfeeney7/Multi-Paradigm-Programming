package ShopJavaVersion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ShopJavaVersion.Product;
import ShopJavaVersion.ProductStock;

public class Customer {

	private static String name;
	private static double budget;
	private static ArrayList<ProductStock> shoppingList;

	public Customer(String fileName) {
		shoppingList = new ArrayList<>();
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			String[] firstLine = lines.get(0).split(",");
			name = firstLine[0];
			
			// Print the budget that is stored in Customer.
			// This will be updated after the customer order is completed.
			String[] cusBudget = lines.get(0).split(",");
			budget = Double.parseDouble(cusBudget[1]);
			
			// i am removing at index 0 as it is the only one treated differently
			lines.remove(0);
			for (String line : lines) {
				String[] arr = line.split(",");
				String name = arr[0];
				int quantity = Integer.parseInt(arr[1].trim());
				Product p = new Product(name, 0);
				ProductStock s = new ProductStock(p, quantity);
				shoppingList.add(s);
			}

		}

		catch (IOException e) {

			// do something
			e.printStackTrace();
		}
	}
		
	
	public static String getName() {
		return name;
	}

	public static void setBudget(double budget) {
		Customer.budget = budget;
	}

	public static double getBudget() {
		return budget;
	}

	public static ArrayList<ProductStock> getShoppingList() {
		return shoppingList;
	}

	@Override
	public String toString() {
		String ret = "Customer [name=" + name + ", budget=" + budget + ", shoppingList=\n";
		for (ProductStock productStock : shoppingList) {
			ret += productStock.getProduct().getName() + " Quantity: " + productStock.getQuantity() + "\n";
		}
		return ret + "]";
	}
}
