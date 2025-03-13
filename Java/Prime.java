
public class Prime {
    public static int fib(int n){
        if(n <= 1){
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static void fibo(int n){
        int a = 0;
        int b = 1;
        int c = 0;
        System.out.print(a + " ");
        System.out.print(b + " ");

        for(int i = 2; i <= n; i++){
            c = a + b;
            a = b;
            b = c;
            System.out.print(c + " ");
        }
    }

    public static void main(String[] args) {
        System.out.println(fib(9));
        fibo(9);
    }

}