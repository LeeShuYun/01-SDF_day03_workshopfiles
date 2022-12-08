package prog;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * Some notes:
 * This is the cart API that someone's going to use so catching exceptions and sanitising input is usually must.
 * Saves the shopping cart list of items into a file inside the cartdb database folder.
 * cartdb is organised by one user per "<username>.db" file.
 */

public class ShoppingCartDB {
    //this allows us to change the commands easily instead of hardcoding them 
    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String EXIT = "exit";
    public static final String USERS = "users";

    private CartDBInMemory db;    // the 'database' we're using
    public String currentUser;    // current session user's name
    private String baseFolder;    // the folder we're saving our user's data into 

    //to sanitise our inputs, we check our command inputs against this list
    public static final List<String> VALID_COMMANDS = Arrays.asList(LOGIN, ADD, LIST, SAVE, EXIT, USERS);   

    // constructors ============================================================================================

    public ShoppingCartDB(){
        this.baseFolder = "db";                         // the default database folder name if we don't specify one
        this.setup();                                   // prepwork - checks and creates the cartdb folder if there isn't already one
        this.db = new CartDBInMemory(this.baseFolder);  //cart items storage object. pulls save data from the db folder if there is one
    }

    //for existing database folders
    public ShoppingCartDB(String baseFolder){
        this.baseFolder = baseFolder;
        this.setup();
        this.db = new CartDBInMemory(this.baseFolder);
    }

    // methods ============================================================================================
    //checks if the directory for storing user databases exists already
    public void setup(){
        Path path = Paths.get(this.baseFolder);

        if (Files.isDirectory(path)){
            //skip creation if directory exists
        }else{
            //create the directory folder inside this current one
            try{
                Files.createDirectory(path);
            }catch(IOException e){
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    //the main logic loop for using the shopping cart
    public void startShell(){
        System.out.println("Welcome to MultiUser Shopping Cart.");
        Scanner sc = new Scanner(System.in);
        String line;
        boolean isStopped = false;

        while (!isStopped){
            //capturing and cleaning the command input
            line = sc.nextLine();
            line = line.trim();
            System.out.println("=> " + line);

            //exit the loop with exit command
            if (line.equalsIgnoreCase(EXIT)){
                System.out.println("Exiting!");
                isStopped = true;
            }

            //Validate Command, parse, execute and return correct user feedback
            if (!this.ValidateInput(line)){
                System.out.println("Invalid Input: ^^");
            } else{
                System.out.println("Processing: " + line);
                this.ProcessInput(line);
            }
        } //end of loop

        sc.close(); //always close the stream of data when not using it
    }

    // methods for preprocessing the input ===================================================================
    public boolean ValidateInput(String input){
        String[] parts = input.split(" ");
        String command = parts[0];

        //checking user's command against valid commands const so users can't input anything weird
        return VALID_COMMANDS.contains(command);
    }

    //process the user's commands
    public void ProcessInput(String input){
        //taking input
        Scanner sc = new Scanner(input);
        String command = sc.next().trim();

        switch (command){
            case LOGIN:
                // creates an account for new users, opens and read db file for existing users
                String username = sc.nextLine().trim();
                this.LoginAction(username);
                System.out.println("The current logged in user is:" + this.currentUser);
                break;

            case LIST: 
                // outputs all the items inside the current user's shopping cart
                this.ListAction();
                System.out.println("===== End of List =====");
                break;

            case ADD:
                //cleans input and adds items. Multiple items can be added, must be separated by ","
                String[] items = sc.nextLine().trim().split(",");
                this.AddAction(items);
                System.out.println("Item(s) added!");
                break;

            case SAVE:
                //saves the shopping cart into the cardb folder as a <username>.db file
                this.SaveAction();
                System.out.println("User shopping cart saved!");
                break;

            case USERS:
                //outputs all the users names
                this.ListUsersActions();
                break;
            
            default:
                break;
        }
        sc.close(); 
    }

    //methods to do stuff with inputs ==============================================================================

    // command: login <username>
    public void LoginAction(String username){
        //check if user is already inside the User hashmap, if they are then retrieve their shopping cart data
        if (!this.db.userMap.containsKey(username)){
            this.db.userMap.put(username, new ArrayList<String>());
        }
        //if they are not an existing user, simply record them as current user, we will create a save file for them later if they save
        //otherwise this "loads" the existing user as current user
        this.currentUser = username;
    }

    //command: "add <item1>, <item2>, ... ,<item n>"
    //add items to the current user's shopping cart, as many as they want
    public void AddAction(String[] items){
        for (String item: items){
            this.db.userMap.get(this.currentUser).add(item.trim());
        }
    }
    //command: "list"
    //show the items inside shopping cart for the current user
    public void ListAction(){
        for (String item : this.db.userMap.get(this.currentUser)) {
            System.out.println("Item -> " + item);
        }
    }

    //command: "users"
    //list all the users in the system
    public void ListUsersActions() {
        for (String key : this.db.userMap.keySet()) {
            System.out.println("->" + key);
        }
    }

    // for the commmad: save
    // dump the contents of current user's cart to a file inside db folder. each line is one item
    public void SaveAction(){
        // prepares filepath "db/<username>.db"
        String outputFileName = String.format("%s/%s.db", this.baseFolder, this.currentUser);
        

        try {
            //save the contents of this user into a file
            //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html
            FileWriter fw = new FileWriter(outputFileName);

            //grabs all the String items in this current user's Map and writes each line to a file
            for(String item : this.db.userMap.get(this.currentUser)){
                System.out.println("Writing item -> " + item); // test line
                fw.write(item + "\n"); 
            }
            fw.flush();
            fw.close();
        }catch (IOException e){
            // printStackTrace will print the throwable IOException and backtrace to the standard error stream.
            // backtrace is a list of the function calls that are currently active in a thread. Good to log them for debugging.
            // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html#printStackTrace()
            e.printStackTrace();
        }


    }
}
