package Main;

import Server.Server;
import java.awt.Container;
import javax.swing.JFrame;

/**
 *
 * @author takahito
 */
public class DruagaMain extends JFrame{
    
    public DruagaMain(){
        setTitle("Tower of Druaga");
        
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);
        pack();
    }
    
    public static void main(String[] args) {
        
        Thread server = new Thread(new Server(10000));
        server.start();
        
        DruagaMain frame = new DruagaMain();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
