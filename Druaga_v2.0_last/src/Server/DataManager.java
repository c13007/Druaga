package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author takahito
 */
public class DataManager {
    
    public static int CLIENT = 0;
    public static int TIME = 1;
    public static int STATIC_ID = 2;
    public static int ID = 3;
    public static int FLOOR = 4;
    public static int TAG = 5;

    
    public HashMap<Integer, String> eName;
    public HashMap<Integer, String> iName;
    
    public DataManager() {
        eName = new HashMap<>();
        iName = new HashMap<>();
        load();
    }
    
    private void load(){
        try{
            FileReader fr = new FileReader("csv/EntityList.csv");
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] str;
            while((line = br.readLine()) != null){
                str = line.split(",");
                eName.put(new Integer(str[0]), str[1]);
            }
            
            fr = new FileReader("csv/itemList.csv");
            br = new BufferedReader(fr);
            while((line = br.readLine()) != null){
                str = line.split(",");
                iName.put(new Integer(str[0]), str[1]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String findEntityNameByStaticId(int staticId){
        if(!eName.containsKey(staticId)) return "Unknown Entity";
        return eName.get(staticId);   
    }
    
    public String findItemNameByStaticId(int staticId){
        if(!iName.containsKey(staticId)) return "Unknown Item";
        return iName.get(staticId);
        
    }
}
