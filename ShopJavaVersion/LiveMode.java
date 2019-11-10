package ShopJavaVersion;

import java.util.*;

public class LiveMode {
	public static void main(String args[]) {
		System.out.println("\n********* Live Shop *************\n");

		// create the scanner to take in user input
		Scanner scan = new Scanner(System.in);

		// Coke Can Was Displaying An Error At Quantity Question, Hence the reason for the Double quotes in position 0 of the Array.
		// This seems to have fixed the issue
		String[] list = new String[] { "","Coke Can", "Bread", "Spaghetti", "Tomato Sauce", "Big Bags" };
		// System.out.println(Arrays.toString(list));
		boolean found = false;

		// ask the user for what they want to buy and save as string
		System.out.println("\nWhat product do you want to buy?");
		String productName = scan.nextLine();

		for (int i = 0; i < list.length; i++) {
			if (productName.equals(list[i])) {
				found = true;
				System.out.println("\nWe have " + productName);
			}

			else if (found == false) {
				System.out.println("\nInvalid, PLease Select One Of The Following Products : ");

				new Shop("ShopJavaVersion/stock.csv");

				for (ProductStock productStock : Shop.getStock()) {
					System.out.println("Item          : " + productStock.getProduct().getName());
				}

				System.out.println("\nWhat product do you want to buy?");
				productName = scan.nextLine();
			}
		}

		// Ask how many they want and save as a integer
		System.out.println("How many " + productName + " do you want?");
		int amount = scan.nextInt();
		double totalCost = 0.0;

		// Loop that will display the products and quantity from the CSV file

		for (ProductStock productStock : Shop.getStock()) {
			if (productName.equals(productStock.getProduct().getName())) {
				double price = productStock.getProduct().getPrice();
				int q = amount;
				System.out.println("Quantity         : " + q);
				System.out.println("Price (Each)     : " + "€" + price);
				System.out.println("Total Item Price : " + "€" + price * q + "\n");

				totalCost = totalCost + (q * price);
			}
		}

		// Print total
		System.out.printf("Total Overall    :  €%.2f\n", totalCost, "\n");

		// find out how much money they have to pay you with
		System.out.println("\nHow much money do you have?");
		double cash = scan.nextDouble();

		// Check if the customer has enough money to pay and display if they have or not
		if (cash - totalCost > 0.0) {
			System.out.printf("\nYou Have The Money To Buy\n");
		} else {
			System.out.printf("\nSorry, You Have The Money To Buy.\nYou Are In A Minus Of €%.2f ", cash - totalCost,
					"\n");
		}
		// Print the information
		System.out.println(
				"\nOrder Review, You want to buy " + amount + " " + productName + " and you have " + cash + ".");
		scan.close();

	}

}