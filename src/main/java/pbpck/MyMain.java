package pbpck;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Abdallah El-Karamany & Mohamed Mumtaz
 */
public class MyMain {

    public static void main(String[] args) {
        JFrame frame=new JFrame();
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Paint Brush");
        frame.setSize(800,600);
        
        //frame.setLocation(50, 50);
       // frame.setResizable(false);
        
        Buttons panel = new Buttons();
        
        frame.add(panel);
        
        frame.setVisible(true);
        
    }
}
