package ShopJavaVersion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import ShopJavaVersion.Product;
import java.util.*;

public class Shop {
	private static double cash;
	private static ArrayList<ProductStock> stock;
	private static Scanner scan;

	public Shop(String fileName) {
		stock = new ArrayList<>();
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

			// Print the cash that is stored in the shop.
			// This will be updated after the customer order is completed.
			String[] shopCash = lines.get(0).split(",");
			cash = Double.parseDouble(shopCash[0]);
			// cash = Double.parseDouble(lines.get(0));

			// i am removing at index 0 as it is the only one treated differently
			lines.remove(0);
			for (String line : lines) {
				String[] arr = line.split(",");
				String name = arr[0];
				double price = Double.parseDouble(arr[1]);
				int quantity = Integer.parseInt(arr[2].trim());
				Product p = new Product(name, price);
				ProductStock s = new ProductStock(p, quantity);
				stock.add(s); // Add stock to the array
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Shop [cash=" + cash + ", stock=" + stock + "]";
	}

	public static double getCash() {
		return cash;
	}

	public static ArrayList<ProductStock> getStock() {
		return stock;
	}

	public static void setCash(double cash) {
		Shop.cash = cash;
	}

	// method to update stock
	public void setStock(ArrayList<ProductStock> stock) {
		Shop.stock = stock;
	}

	// Print the contents of the shop CSV file
	public static void printShop() {
		// Reading the CSV
		new Shop("ShopJavaVersion/stock.csv");

		// Output the starting cash of the shop using a getter.
		System.out.printf("The Shop Cash Is : " + "€" + getCash() + "\n\n");

		// Loop that will display the products and quantity from the CSV file
		for (ProductStock productStock : Shop.getStock()) {
			System.out.println("Item          : " + productStock.getProduct().getName());
			System.out.println("Price         : " + "€" + productStock.getProduct().getPrice());
			System.out.println("Current Stock : " + productStock.getQuantity() + "\n");
		}
		System.out.println("--------------------------------------");
	}

	// Find the price, this is used in the print Customer Shopping List below.
	private static double findPrice(String name) {
		for (ProductStock productStock : stock) {
			Product p = productStock.getProduct();
			if (p.getName().equals(productStock.getProduct().getName())) {
				return p.getPrice();
			}
		}
		return -1;
	}

	// Print customer shopping list
	public static void printCustomerShopList() {
		// Used for output to the user.
		double totalCost = 0.0;

		// Read CSV
		new Customer("ShopJavaVersion/customer.csv");

		// Print out the name and budget from the CSV file using gets
		System.out.println("Name      : " + Customer.getName() + "\n");
		System.out.printf("Cus Budget : " + "€" + Customer.getBudget() + "\n\n");

		// Loop that will display the products and quantity from the CSV file
		for (ProductStock productStock : Customer.getShoppingList()) {
			// Locate the details and display in System.out lines
			Product p = productStock.getProduct();
			double price = findPrice(p.getName());
			int q = productStock.getQuantity();
			String n = productStock.getProduct().getName();

			System.out.println("Item             : " + n);
			System.out.println("Quantity         : " + q);
			System.out.println("Price (Each)     : " + "€" + price);
			System.out.println("Total Item Price : " + "€" + price * q + "\n");

			// Calculate the total cost for the customer
			totalCost = totalCost + (q * price);

			p.setPrice(price);
		}

		// Print total cost to the user with two decimal places
		System.out.printf("Total Overall    :  €%.2f\n", totalCost, "\n");

		// Check if the customer has enough money to pay and display if they have or not
		if (Customer.getBudget() - totalCost > 0.0) {
			System.out.printf("You Have The Money To Buy\n");
		} else {
			System.out.printf("\nSorry, You Do Not Have The Money To Buy.\nYou Are In A Minus Of €%.2f ",
					Customer.getBudget() - totalCost, "\n");
		}

		System.out.println("\n\n--------------------------------------");
	}

	// Find the product
	public static String find(String product) {
		for (ProductStock productStock : stock) {
			// check if the shop has the item and return if found. Used gets and .equals
			if (productStock.getProduct().getName().equals(product)) {
				return productStock.getProduct().getName();
			}
		}
		// If not found return NULL
		return "NULL";
	}

	// Process order
	public static void processOrder() {
		// Store total cost
		double orderTotal = 0;

		// Has the shop every item that the customer requires?. This for loop will check
		// and output if not
		for (ProductStock productStock : Customer.getShoppingList()) {
			if ((productStock.getProduct().getName()).equals("NULL")) {
				System.out.printf("\nSorry But We Do Not Stock, %s \n", productStock.getProduct().getName());
				return;
			}
		}

		for (ProductStock productStock : Customer.getShoppingList()) {
			// Calculate the total cost and store in variable
			orderTotal += (productStock.getProduct().getPrice() * productStock.getQuantity());
		}
		// Output total cost to the screen to two decimal places
		System.out.printf("The Total Cost of the order is : €%.2f  \nAnd We Have The Quantity Required", orderTotal,
				" \n \n");

		// Check if the user budget is greater than 1 and if so do they have enough
		// money to pay for the order
		// If the customer budget is greater than 1 continue
		if (Customer.getBudget() >= 1) {

			// Check order total var against the customer budget and if the total is greater
			// then output the following
			if (orderTotal > Customer.getBudget()) {
				System.out.println(
						"\nUnfortunately This Is Greater Than The Budget You Have. Order Can Not Be Processed \n");
				System.out.println("--------------------------------------");
				return;

			}
			// Check if the customer has a budget and if not output the following
		} else if (Customer.getBudget() < 1) {
			System.out.println("\nYou Have No Money\n");
		}

		// Print out the stock details after the customer order is removed.
		System.out.println("\n\n****** New Shop Stock Details ******\n");
		// Has the shop each item of the order
		for (ProductStock productStock : Customer.getShoppingList()) {
			for (ProductStock shopStock : stock) {
				if (productStock.getProduct().getName().equals(shopStock.getProduct().getName())) {

					// Have we enough of each item in the shop and if not output the following to
					// the screen
					if (productStock.getQuantity() > shopStock.getQuantity()) {
						System.out.printf("\nSorry We Do Not Have Enough %s In The Shop.\n",
								productStock.getProduct().getName(), "\n");
						System.out.printf("\n");

						return;
					}
					// Remove what was purchased from the shop and print the updated details
					shopStock.setQuantity(shopStock.getQuantity() - productStock.getQuantity());
					System.out.println("Item          " + productStock.getProduct().getName());
					System.out.println("New Cur Stock " + shopStock.getQuantity() + "\n");
				}
			}
		}
		
		// update and print the customer budget
		Customer.setBudget(Customer.getBudget() - orderTotal);
		System.out.printf("New Customer Budget : " + Customer.getBudget() + "\n \n");
		
		// Add Cash To The Shop and print new cash amount
		setCash(getCash() + orderTotal);
		System.out.printf("New Shop Cash       : " + getCash() + "\n");
	}

	// Live Mode Section
	private static void LiveMode() {
		System.out.println("\n********* Live Shop *************\n");

		// create the scanner to take in user input
		Scanner scan = new Scanner(System.in);

		// Coke Can Was Displaying An Error At Quantity Question, Hence the reason for
		// the Double quotes in position 0 of the Array.
		// This seems to have fixed the issue
		String[] list = new String[] { "", "Coke Can", "Bread", "Spaghetti", "Tomato Sauce", "Big Bags" };
		// System.out.println(Arrays.toString(list));
		boolean found = false;

		// ask the user for what they want to buy and save as string
		System.out.println("\nWhat product do you want to buy?");
		String productName = scan.nextLine();

		// For loop that will output when the input matches something that is stored in
		// the array
		// Having an issue with this for loop, sometimes the correct item name will need
		// to be inserted a few times before the output will be true.
		for (int i = 0; i < list.length; i++) {
			if (productName.equals(list[i])) {
				found = true;
				System.out.println("\nWe have " + productName);
			}

			// When Condition is false output the shop item names and ask user to try again.
			else if (found == false) {
				System.out.println("\nInvalid, PLease Select One Of The Following Products : ");

				// Get and Print Item Details
				new Shop("ShopJavaVersion/stock.csv");

				for (ProductStock productStock : Shop.getStock()) {
					System.out.println("Item          : " + productStock.getProduct().getName());
				}

				// Ask user to try again
				System.out.println("\nWhat product do you want to buy?");
				productName = scan.nextLine();
			}
		}

		// Ask how many they want and save as a integer
		System.out.println("How many " + productName + " do you want?");
		int amount = scan.nextInt();

		// Vat to display the cost of the items.
		double totalCost = 0.0;

		// Loop that is used to pull the required information and compute the
		// calculations that will be outputted to the screen.
		for (ProductStock productStock : Shop.getStock()) {
			if (productName.equals(productStock.getProduct().getName())) {
				double price = productStock.getProduct().getPrice();
				int quantity = amount;
				System.out.println("Quantity         : " + quantity);
				System.out.println("Price (Each)     : " + "€" + price);
				System.out.println("Total Item Price : " + "€" + price * quantity + "\n");

				// Finding total cost that will be used to output to the screen
				totalCost += (quantity * price);
			}
		}

		// Print total cost of order
		System.out.printf("Total Overall    :  €%.2f\n", totalCost, "\n");

		// find out how much money the user has.
		System.out.println("\nHow much money do you have?");
		double cash = scan.nextDouble();

		// Check if the customer has enough money to pay and display if they have or not
		if (cash - totalCost > 0.0) {
			System.out.printf("\nYou Have The Money To Buy\n");
		} else {
			System.out.printf("\nSorry, You Have The Money To Buy.\nYou Are In A Minus Of €%.2f ", cash - totalCost,
					"\n");
		}

		// Print order summary
		System.out.println(
				"\nOrder Review, You want to buy " + amount + " " + productName + " and you have " + cash + ".");
		// Removed the following scan.close() as it was conflicting with the Main Menu Switch Statement
		//scan.close();
	}

	// Main menu section
	public static void main(String[] args) {

		System.out.println("+------------------------------------+");
		System.out.println("|         WELCOME TO MY              |");
		System.out.println("|           MAIN MENU                |");
		System.out.println("+------------------------------------+");

		int choice = 0;
		char quit = 'n';
		scan = new Scanner(System.in);

		while (quit != 'y') {
			System.out.println("  Select One Of The Following Options");
			System.out.println("        1. Shop Contents");
			System.out.println("        2. Cutomer Order");
			System.out.println("        3. Process Order");
			System.out.println("        4. Live Mode");

			choice = scan.nextInt();

			switch (choice) {
			case 1:
				System.out.println("***** Shop Contents ***** \n");
				printShop();
				break;

			case 2:
				System.out.println("***** Customer Order ***** \n");
				printCustomerShopList();
				break;

			case 3:
				System.out.println("***** Process Order ***** \n");
				processOrder();
				break;

			case 4:
				System.out.println("***** Live Mode ***** \n");
				LiveMode();
				break;

			default:
				System.out.println("That is not a choice");
			}
			System.out.print("\nWould you like to quit y/n  ? \n");
			String input = scan.next().toLowerCase();
			quit = input.charAt(0);
			System.out.println("--------------------------------------");
		}
	}
}
