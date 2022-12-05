package prog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//this is the API that someone's going to use
// so we have to think about how users will use this
//catches exceptions and sanitises inputs
public class ShoppingCartDB {
    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String EXIT = "exit";
    public static final String USERS = "users";

    private CartDBInMemory db;
    public String currentUser;
    private String baseFolder;

    public static final List<String> VALID_COMMANDS = Arrays.asList("login","add","list", "exit", "users");   

    public ShoppingCartDB(){
        this.db = new CartDBInMemory();
        this.baseFolder = "db"; 
    }
    public ShoppingCartDB(String baseFolder){
        this.db = new CartDBInMemory();
        this.baseFolder = baseFolder;
    }

    public void setup(){
        Path p = Paths.get(this.baseFolder);
        File f = new File(this.baseFolder);
        if (f.isDirectory()){
            //skip if directory exists
        }else{
            try{
            //create the directory
            Files.createDirectory(p);
        }catch(IOException e){
                System.out.println("Error" + e.getMessage());

            }
        }
    }
    public void startShell(){

        //scanner is reading from system input
        Scanner sc = new Scanner(System.in);
        // sc.nextLine(); //current line till end of line
        // sc.next(); //first word till whitespace

        // String command = sc.next();
        // String inputs = sc.nextLine();

        String line;
        // while ((line = sc.nextLine()) != null){
        //     System.out.println(line);

        //     //this is the switch part of the previous cart
        //     if (line.equalsIgnoreCase("exit")){
        //         break;
        //     }
        // }
        boolean stop = false;
        while (!stop){
            line = sc.nextLine();
            line = line.trim();
            System.out.println("=> " + line);
            //this is the switch part of the previous cart
            if (line.equalsIgnoreCase("exit")){
                System.out.println("Exit!");
                stop = true;
            }

            //Validate Command
            if (!this.ValidateInput(line)){
                System.out.println("Invalid Input: ^^");
            } else{
                System.out.println("Processing: " + line);
                this.ProcessInput(line);
            }
            //Process command
        }
        sc.close();

    }

    public boolean ValidateInput(String input){
        String[] parts = input.split(" ");
        String command = parts[0];

        //defining our valid command sos users can't input anything weird
        //Arrays.asList("login","add","list", "exit", "users");
        return VALID_COMMANDS.contains(command);
    }

    //process command
    public void ProcessInput(String input){
        //taking in input
        Scanner sc = new Scanner(input);
        String command = sc.next().trim();

        //becase switch has to use strings and not constants
        //we use if method instead
        switch (command){
            case LOGIN:
                String username = sc.nextLine().trim();
                this.LoginAction(username);
                System.out.println("print - current logged in user" + currentUser);
                break;

            case LIST: 
                this.ListAction();
                break;

            case ADD:
                String[] items = sc.nextLine().trim().split(",");
                this.AddAction(items);
                break;

            case SAVE:
                this.SaveAction();
                break;

            case USERS:
                //outputs all the users names
                this.ListUsersActions();
                break;
            
            default:
                break;
        }
    }

    // command: login <username>
    // login function
    public void LoginAction(String username){
        //check if user is already inside hashmap
        //if they are then retrieve their data
        if (!this.db.userMap.containsKey(username)){
            this.db.userMap.put(username, new ArrayList<String>());
            //no action needed
        }
        //if they are not add them
        this.currentUser = username;

        
    }

    //command add <item1><item2>
    //add items function
    public void AddAction(String[] items){
        //we add each item from the list into the database under the currentUser
        for (String item: items){
            this.db.userMap.get(this.currentUser).add(item);
        }
    }
    //command list
    //list items fnction show the items added for the current user
    public void ListAction(){
        for (String item : this.db.userMap.get(this.currentUser)) {
            System.out.println("Item -> " + item);
        }
    }

    //command users
    //list all the users in the system

    //commnad :save
    // save function: dump the contents of current user to a file base_folder/username.db
    public void SaveAction(){
        // Get the current username
        //String username = this.currentUser;

        //prep filepath "db/username.db"
        String outputFilename = String.format("%s/%s.db", this.baseFolder, this.currentUser);
        

        try {
            //save the contents of this data struct into a file
            FileWriter fw = new FileWriter (outputFileName);

            //this is the actual saving part itself. 
            //grabs all the items in this current user's Map and writes to a file
            for(String item : this.db.userMap.get(this.currentUser)){
                // System.out.println("Item -> " + item); // prints it out. leave it here
                fw.write(item + "\n"); //writes each line of item into file
            }
            fw.flush();
            fw.close();
        }catch (IOException e){
            //printStackTrace will print the throwable IOException and backtrace to the standard error stream.
            //backtrace is a list of the function calls that are currently active in a thread. Good to log them for debugging.
            //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html#printStackTrace()
            e.printStackTrace();
        }


    }

    public void ListUsersActions(){
        //outputs all the users names
    }
}
