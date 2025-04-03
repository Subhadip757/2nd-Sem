package Practical;

import java.util.Scanner;

public class day1 {
    public static boolean prime(int range) {
        int i = 2;
        for (i = 2; i * i <= range; i++) {
            if (range % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the range: ");
        int range = s.nextInt();

        for (int i = 2; i <= range; i++) {
            if (prime(i)) {
                System.out.println(i);
            }
        }
    }
}