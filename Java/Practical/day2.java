package Practical;

import java.util.Scanner;

public class day2 {

    public static void arth() {
        Scanner sc = new Scanner(System.in);
        int a = 10;
        try {
            System.out.println("Enter divisor: ");
            int divisor = sc.nextInt();

            if (divisor == 0)
                throw new ArithmeticException("Invalid divisor");

            System.out.println(a / divisor);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void size() {
        Scanner sc = new Scanner(System.in);
        int arr[] = new int[5];

        try {
            System.out.println("Enter size of array: ");
            int size = sc.nextInt();
            if (size > 5)
                throw new Exception("Array out of bonds");

            System.out.println("Array size in range!!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        arth();
        size();
    }

}
