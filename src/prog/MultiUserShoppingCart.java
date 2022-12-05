package prog;

public class MultiUserShoppingCart {
    public static void main(String[] args){
        System.out.println("Welcome to MultiUser Shopping Cart.");

        //creates a new shopping cart and assigns it to a variable named cart
        ShoppingCartDB cart = new ShoppingCartDB("cardb");

        //the function we need for this cart

        //creates the 
        cart.setup();
        cart.startShell(); 
        
        //default folder: "db"
        //cart loop
    }
}
