import java.util.Scanner;

// Enum for product categories
enum Category {
    ELECTRONICS, CLOTHING, GROCERY
}

// Product class to store product details
class Product {
    String name;
    double price;
    Category category;

    public Product(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

// Cart class to store and manage cart items
class Cart {
    Product[] items = new Product[10]; // Array to store items
    int itemCount = 0; // Track number of items in cart

    void addItem(Product product) {
        if (itemCount < items.length) {
            items[itemCount] = product;
            itemCount++;
            System.out.println(product.name + " added to cart.");
        } else {
            System.out.println("Cart is full!");
        }
    }

    void removeItem(String productName) {
        boolean found = false;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].name.equalsIgnoreCase(productName)) {
                System.out.println(items[i].name + " removed from cart.");
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[itemCount - 1] = null;
                itemCount--;
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found in cart.");
        }
    }

    void viewCart() {
        if (itemCount == 0) {
            System.out.println("Cart is empty.");
        } else {
            System.out.println("Your cart:");
            for (int i = 0; i < itemCount; i++) {
                System.out.println(items[i].name + " - ₹" + items[i].price);
            }
        }
    }

    void checkout() {
        if (itemCount == 0) {
            System.out.println("Your cart is empty. Total payable amount: ₹0");
        } else {
            double total = 0;
            for (int i = 0; i < itemCount; i++) {
                total += items[i].price;
            }
            System.out.println("Total payable amount: ₹" + total);
            itemCount = 0; // Empty cart after checkout
        }
    }
}

// Main class to run the shopping cart program
public class ShoppingCart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();
        Product[] productCatalog = {
            new Product("Mobile", 30000, Category.ELECTRONICS),
            new Product("T-Shirt", 500, Category.CLOTHING),
            new Product("Apple", 200, Category.GROCERY)
        };

        while (true) {
            System.out.println("\n1. Add item\n2. Remove item\n3. View cart\n4. Checkout\n5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter product name: ");
                String addItem = scanner.nextLine();
                boolean found = false;
                for (Product product : productCatalog) {
                    if (product.name.equalsIgnoreCase(addItem)) {
                        cart.addItem(product);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Product not found.");
                }
            } else if (choice == 2) {
                System.out.print("Enter product name to remove: ");
                String removeItem = scanner.nextLine();
                cart.removeItem(removeItem);
            } else if (choice == 3) {
                cart.viewCart();
            } else if (choice == 4) {
                cart.checkout();
            } else if (choice == 5) {
                System.out.println("Exiting the shopping cart system.");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}