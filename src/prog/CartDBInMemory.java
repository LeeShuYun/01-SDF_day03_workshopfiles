package prog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Notes: 
 * This class processes the files we're using as a database. 
 * Each object will have a Hashmap instance variable that contains {User : Shopping Cart Data} KV pairs
 * each <username>.db save file this generates contains the shopping cart data of one user only
 */

public class CartDBInMemory {

    // HashMap for storing the data we read from the local mockup DB file. 
    // Key: Value, Username : ArrayList of shopping cart items
    public HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();

    //constructor =========================================================================================================

    public CartDBInMemory(String baseFolder){
        this.loadDataFromFiles(baseFolder);
    }

    //methods =============================================================================================================
    
    // takes the folder we're putting our user data into, picks out the .db files and finds the user
    public void loadDataFromFiles(String baseFolder) {
        //this usage of File creates a new File instance by converting the given string pathname into an abstract pathname
        File f = new File(baseFolder); 

        // retrieving a list of files that end with ".db"
        // FilenameFilter is a functional interface that works by an override on its one abstract method - accept(). we have to define the conditions of the filter
        // filenameFilter then returns the fil1es that satisfy the condition
        // listFiles takes the files in directory, returns array of abstract pathnames
        File[] filteredFiles = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename){
                return filename.endsWith(".db");
            }
        });

        //checks if we found any .db files in our filtered list. If there isn't we just skip out of the function
        if(filteredFiles.length == 0){
            return;
        }

        //replacing all the .db file names with empty space so that we get username key for retrieving data from the hashmap
        for (File file : filteredFiles){
            String userKey = file.getName().replace(".db","");
            //read the content of the file
            this.userMap.put(userKey, ReadFile(f));
            //pass in the actual filename
        }
    }

    //takes in a local .db file, puts every line into an Arraylist, returns the arraylist
    public ArrayList<String> ReadFile(File dbfile){
        //the list to keep all the db data in
        ArrayList<String> dataList = new ArrayList<String>();

        try{
            // FileReader reads from character files and is meant for reading streams of characters. Output is a stream of characters. 
            // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileReader.html

            // BufferedReader https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/BufferedReader.html
            // BufferedReader takes a character stream input, then uses buffering to read a default large amount of data at a time, 
            // then stores this stream of data that is not needed immediately into RAM for later use.
            // so this means FileReader doesn't have to open the file on disk for each line while we're processing it below. 
            // We open the file on disk only once with FileReader, and then use the data stream from the faster RAM afterwards. Efficiency!
            BufferedReader bf = new BufferedReader(new FileReader(dbfile));
            String line;
            //keeps reading, trimming, adding each cleaned line to our arraylist until it hits the End Of File which returns null
            if ((line = bf.readLine())  != null ){
                line = line.trim();
                dataList.add(line);    
            }
            // always close the stream after use to release any system resources. 
            // since all the data has been stored in arraylist we don't need the stream anymore
            bf.close();    

        }catch (FileNotFoundException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }
        //returns our arraylist that contains all the lines we read from the file
        return dataList;
    }
}
