#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// gcc -o shop shop.c
// shop.exe

// A struct is used to group data together.
 
struct Product // Product Struct
{
	char *name;	     // *is a pointer that allow it to expland in memory
	double price;	//  Double = decimal number
};

struct ProductStock  // ProductStock Struct
{
	struct Product product; // Nested Struct

	int quantity;

};

struct Customer // Customer Struct
{
	char *name;

	double budget;

	struct ProductStock shoppingList[10]; // Nested struct - Array

	int index;

};

struct customerOrder  // customerOrder Struct
{
	struct Customer name; // Nested struct

	struct Product product; // Nested struct

	struct Customer budget; // Nested struct

	int quantity;

};

struct Shop // Shop Struct
{
	double cash;

	struct ProductStock stock[20];

	int index;

};


void printProduct(struct Product p)
{
	//Print the product and price. 
	printf("PRODUCT : %s \nPRICE   : %.2f\n", p.name, p.price);

}

void printProduct2(struct Product p)
{
	//Print the product only
	printf("PRODUCT : %s  \n", p.name);

}

void printCustomer(struct Customer c, struct Shop s)	// Two Structs will be used 
{
	// Print customer name and budget
	printf("CUSTOMER NAME: %s \nCUSTOMER BUDGET: %.2f\n", c.name, c.budget);

	printf("------------------------------------\n");

	// Set sum to equal zero
	double sum = 0;

	// Start loop of index 
	for (int i = 0; i < c.index; i++)
	{
		// Print customers item list
		printProduct2(c.shoppingList[i].product);

		// Print how much of each item the customer wants
		printf("Quantity: %d\n\n", c.shoppingList[i].quantity);
		
		// Find total customer cost and store in Var total
		double total = c.shoppingList[i].quantity *c.shoppingList[i].product.price;
		sum = sum + total;

	}
	
	// Print the total cost of the customer items
	printf("TOTAL COST:\t%.2f\n", sum);

	// Once all the customer items are added, minus from the budget
	printf("BUDGET BALANCE:\t%.2f\n", c.budget - sum);
}

void printShop(struct Shop s) 
{
	printf("------------------------------------\n");

	// Print the cash that is stored in the shop CSV file
	printf("Shop has %.2f in cash\n", s.cash);

	printf("------------------------------------\n");

	printf("------------------------------------\n");

	// For loop that displays the stock details (Item, Quantity, Cost)
	for (int i = 0; i < s.index; i++)
	{
		printProduct(s.stock[i].product);

		printf("Quantity: %d\n", s.stock[i].quantity);

		printf("------------------------------------\n");

	}
}

struct Customer cusOrder()
{
	struct Customer customer = { 0 };

	FILE * fp;

	char *line = NULL;

	size_t len = 0;

	ssize_t read;

	char *array[10];

	// Read the customer CSV
	fp = fopen("orderRich.csv", "r");

	// Error exception
	if (fp == NULL)

		exit(EXIT_FAILURE);

	// Idea to completed this helped with the use of https://stackoverflow.com/questions/32884789/import-csv-into-an-array-using-c

	// Read the Order CSV File
	while ((read = getline(&line, &len, fp)) != -1)
	{
		int reset = 0;

		char *r = strtok(line, ",");	// Read until comman is found and the result is NULL
		while (r != NULL)
		{
			char *arr = malloc(sizeof(char) *50);	//create a local token string variable

			// Wont run without this used stockoverflow to find solution
			const char *stripNewline(char *textStr)
			{
				return textStr;

			}

			// Value will be stored in r store in an array and keep look at line is NULL
			strcpy(arr, stripNewline(r));
			array[reset++] = arr;
			r = strtok(NULL, ",");

		}

		// The following IF will be used to display the users name from the CSV
		if (strstr(array[0], "Name") != NULL)
		{
			customer.name = array[1];
		}

		// The following IF will be used to display the users Budget from the CSV
		// Needed to add an else if here as name was coming out at Product
		else if (strstr(array[0], "Budget") != NULL)
		{
			// Use alof to cobvert to a decimal
			double budget = atof(array[1]);
			customer.budget = budget;
		}

		// Use the else to display the contents under Name and Budget,
		else
		{
			// Note that this sectons CSV is the same structure as the shop CSV
			// Hence the array positions. Cost is not shown but the balance is

			// Define items and cost
			int items = atoi(array[2]);

			double cost = atof(array[1]);

			//char product = (array[0]);
			struct Product p = { array[0],
				cost
			};

			// Add to the shopping list
			struct ProductStock shopList = { p,
				items
			};

			//Update the customer shoppinglist
			customer.shoppingList[customer.index++] = shopList;

		}
	}

	// Return to the function
	return customer;
}

// Read in the contents of the CSV file
struct Shop
createAndStockShop()
{
	FILE * fp;

	char *line = NULL;

	size_t len = 0;

	ssize_t read;

	// Open the CSV file and set to read
	fp = fopen("stock.csv", "r");

	// Exception
	if (fp == NULL)

		exit(EXIT_FAILURE);

	// Read in cash value from CSV file.
	getline(&line, &len, fp);

	// ATOF converts converts into a floating point numerical representation
	double cash = atof(line);

	struct Shop shop = { cash
	};

	// Read the remainder of the CSV file and print to the screen
	while ((read = getline(&line, &len, fp)) != -1)
	{
		// Update the CSV file with the shop amount I set
		// Used https://www.geeksforgeeks.org/strtok-strtok_r-functions-c-examples/ for help.
		if (strstr(line, "Cash") != NULL)
		{
			char *n = strtok(line, ",");

			char *c = strtok(NULL, ",");
			// ATOF converts converts into a floating point numerical representation
			double cash = atof(c);

			shop.cash = cash;

		}
		else
		{
			char *n = strtok(line, ",");

			char *p = strtok(NULL, ",");

			char *q = strtok(NULL, ",");

			int quantity = atoi(q);
			// ATOF converts converts into a floating point numerical representation
			double price = atof(p);

			char *name = malloc(sizeof(char) *50);

			strcpy(name, n);

			// Add name and price to product
			struct Product product = { name,
				price
			};

			// add product and quantity ro stockItem
			struct ProductStock stockItem = { product,
				quantity
			};
			
			shop.stock[shop.index++] = stockItem;
		}
	}

	// return to function
	return shop;
}

void reviewOrder(struct Shop s, struct Customer c)
{
	printf("\n%s  ORDER Review \n\n%s");

	// THis for loop will print the customers shopping list
	for (int i = 0; i < c.index; i++)
	{
		printf("%3i. %s", i + 1, c.shoppingList[i].product.name);

		printf(" Quantity = %d\n", c.shoppingList[i].quantity);

	}

	printf("\n%s  Shop Stock Review \n\n%s");
	
	// This for loop will print the shops stock
	// This will look at the customer order and dieplay if they have it or not
	for (int i = 0; i < c.index; i++)
	{
		printf("%3i. %s", i + 1, s.stock[i].product.name);

		printf(" Quantity = %d\n", s.stock[i].quantity);

	}
}

void checkOrder(struct Shop s, struct Customer c)
{
	for (int i = 0; i < c.index; i++)
	{
		short curStock = 0;

		char *ord = malloc(sizeof(char) *50);

		// Copy string 
		strcpy(ord, c.shoppingList[i].product.name); // https://www.tutorialspoint.com/c_standard_library/c_function_strcpy.htm

		for (int i = 0; i < s.index; i++)
		{
			char *shop = malloc(sizeof(char) *50);
			
			// Copy String
			strcpy(shop, s.stock[i].product.name); // https://www.tutorialspoint.com/c_standard_library/c_function_strcpy.htm

			int sh;

			// Check if the stock is in the shopList
			// If True add else desplay error
			if (strstr(ord, shop) != NULL)
			{
				curStock = 1;
				printf("\nFound in shop:\t%s\n", shop);

				int shopQuant = s.stock[i].quantity;
				int cusQuant = c.shoppingList[i].quantity;
				
				// if the customer order is less than the shop quantity then display message
				if (cusQuant < shopQuant)
				{
					printf("In shop %s \n", shop);

				}
				// If greater then display error message
				else
				{
					printf("ERROR: Not enough stock on %s\n", shop);

				}
			}

			//reached the end of the list and item was not found
			if (i == s.index - 1 &!curStock)
			{
				//shopping list item is not is shop
				printf("\nNOT in shop:\t%s\n", ord);

			}
		}
	}

	return;

}

void stockAndCashArranging(struct Shop s, struct Customer c)
{
	printf("\n********* Updated Shop *************\n\n");

	double sum = 0;

	int new = 0;

	// THis for loop will compare the customer order with shop contents
	// It will subtract the customer item value form store and add price to store 
	for (int i = 0; i < c.index; i++)
	{
		char *shop = malloc(sizeof(char) *50);
		strcpy(shop, s.stock[i].product.name);

		// Get the total cost of product and add to store cash
		double total =(c.shoppingList[i].quantity *c.shoppingList[i].product.price);
		sum = sum + total;
	
		// Check if the shop and customer items are a match in the array
		int shopQuant = s.stock[i].quantity;
		int cusQuant = c.shoppingList[i].quantity;

		// If statement that will subtract what the customer ordered from store stock and display on screen
		// This will display a minus number is the customer order is more than what the shop has in stock
		if (shopQuant = cusQuant)
		{
			new = s.stock[i].quantity - c.shoppingList[i].quantity;
			printf("New Amount of %s is %d\n", shop, new);
		}

		// After the shop has subtracted the customer items, add the money received to the shop cash
		printf("Current cash in the Shop is : \t%.2f\n\n",
			s.cash + sum);

	}
	
}

void liveShop(struct Shop s, struct Customer c)
{
	printf("\n********* Live Shop *************\n\n");
	
	
	char n[100];
	char p[100];
	int  b;
	int  q;

	   printf( "Please enter your name?:\n");
	   gets( n );
	   
	   printf( "Please enter product? :");
	   gets( p );
	   
	   printf("Enter your quntity: ");
	   scanf("%d",&q); 
	   
	   printf("Enter your budget: ");
	   scanf("%d",&b); 

	for (int i = 0; i < c.index; i++)
	{
		short curStock = 0;

		char *ord = malloc(sizeof(char) *50);

		// Copy string 
		strcpy(ord, p); // https://www.tutorialspoint.com/c_standard_library/c_function_strcpy.htm

		for (int i = 0; i < s.index; i++)
		{
			char *shop = malloc(sizeof(char) *50);
			
			// Copy String
			strcpy(shop, s.stock[i].product.name); // https://www.tutorialspoint.com/c_standard_library/c_function_strcpy.htm

			int sh;
		
			// Check if the stock is in the shopList
			// If True add else desplay error
			if (strstr(ord, shop) != NULL)
			{
				curStock = 1;
				printf("\nFound %s", shop);

				int shopQuant = s.stock[i].quantity;		
				
				// if the customer order is less than the shop quantity then display message
				if (q < shopQuant)
				{
					// Set sum to equal zero
					double sum = 0;
					
					printf(" and the shops current Quantity is : %d. \nWe have enough to fill your order.", s.stock[i].quantity);
					
					// Find total customer cost and store in Var total
					double total = q *c.shoppingList[i].product.price;
					sum = sum + total;
					
					if (total > b)
					{
						printf("\nTotal cost of purchase is :\t%.2f", sum);
						// Once all the customer items are added, minus from the budget
						printf("\nSorry you do not have enough money for this and have a minus of  : \t%.2f\n", b - sum);
					}
					
					else{
						// Print the total cost of the customer items
						printf("\nTotal cost of purchase is :\t%.2f\n", sum);
						
					}

				}
			
				// If greater then display error message
				else
				{
					printf(" but we do not have enough in stock. \nThe current Quantity in the shop is : %d\n\nPlease try again.\n", s.stock[i].quantity);

					printf("------------------------------------\n");

				}
		
			}
			

			//When reached the end of the shop list and the item was not found output
			if (i == s.index - 1 &!curStock)
			{
				//shopping list item is not is shop
				printf("\nNOT in shop:\t%s\n", ord);

			}
			
		}
	  return ;
	}

	return;

}


// Refereance https://cboard.cprogramming.com/c-programming/151734-[help]-menu-using-while-loop.html 
// Helped with the menu creation
void display_options();

int input();

void output(float);

int main()
{
	printf("\n*******Welcome to the Shop ********\n\n");

	struct Shop shop = createAndStockShop();

	struct Customer order = cusOrder();

	float result;

	int choice, num;	  
				   

	do {
		printf("\n\nPress 1 To View The Shops Contents\n");

		printf("Press 2 To View The Shop and Order Contents\n");

		printf("Press 3 To View The Customer Order Contents and Calculate Cost\n");

		printf("Press 4 To Confirm If Shop Has Your Order\n");

		printf("Press 5 Updated Shop\n");
		
		printf("Press 6 To Use Live Shop Mode\n");

		printf("Press 7 To Exit\n\n");

		printf("Enter your choice : ");

		choice = input();

		switch (choice)
		{
			case 1:

				{
					printShop(shop);

					break;

				}

			case 2:

				{
					reviewOrder(shop, order);

					break;

				}

			case 3:

				{
					printCustomer(order, shop);

					break;

				}

			case 4:

				{
					checkOrder(shop, order);

					break;

				}

			case 5:

				{
					stockAndCashArranging(shop, order);

					break;

				}

			case 6: // live shop mode

				{
					liveShop(shop, order);
					break;
					
				}
				
			case 7:

				{
					return 0;

				}

			default:

				printf("wrong Input, try again\\n");

		}
	}

	while (choice != 8);

	return 0;

}

int
input()
{
	int number;

	scanf("%d", &number);

	return (number);

}