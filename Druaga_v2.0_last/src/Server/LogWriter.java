package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogWriter {
    
    File file;
    FileWriter fr;
    
    public LogWriter(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("MM_dd_hhmmss");
        
        try{
            file = new File("log/" + fmt.format(c.getTime()) + ".txt");
            fr = new FileWriter(file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void write(String msg){
        try{
            System.out.println("log:" + msg);
            fr.write(msg + "\n");
        }catch(IOException e){
            System.err.println("ファイルに書き込めません" + msg);
        }
    }
    
    public void close(){
        try{
            fr.close();
        }catch(IOException e){
            System.err.println("ファイルを閉じれません");
        }
    }
}
