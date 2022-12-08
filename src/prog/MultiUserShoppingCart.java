package prog;

public class MultiUserShoppingCart {
    public static void main(String[] args){
        /*
         * commands to use this:
         * login <yourusername> => creates a temporary account with that name.
         * add <itemname>       => adds an item to the current temporary user account
         * list                 => shows all the items in the cart
         * save                 => to save current session into the 'database'
         * users                => to print out all the users in the 'database'
         * exit                 => close the current user session. all unsaved data is lost.
         */

        //creates a new shopping cart object
        //this contains the loop for asking for commands too
        ShoppingCartDB cart = new ShoppingCartDB("cardb");

        //opens the shopping cart to accept commands
        //allows us to control where we want the cart to start taking commands. 
        cart.startShell(); 
        

    }
}
