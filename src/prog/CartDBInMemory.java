package prog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartDBInMemory {

    public HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();

    public CartDBInMemory(){
        this.userMap = this.loadDataFromFiles();
    }

    // 
    public HashMap<String, ArrayList<String>> loadDataFromFiles() {
        File f = new File(baseFolder); //the directory
        File[] filteredFiles = f.listFiles(new FilenameFilter());

        //replacing all the db file names with empty space so that we get usernames
        for (File file : filteredFiles){
            String userKey = file.getName().replace(".db","");
            //read the content of the file
            this.userMap.put(userKey, ReadFile(f));//pass in the actual filename
        }
    }

    public ArrayList<String> ReadFile(File f){
        try{
            BufferedReader bf = new BufferedReader(new FileReader(f));

            if ((line = bf.readLine())  != null ){

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }

        //file will be empty if the list is 
    }
}
