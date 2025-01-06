package pbpck;

import java.awt.*;
import static java.awt.Color.*;
import java.util.ArrayList;

/**
 *
 * @author Mohamed Mumtaz & Abdallah El-Karamany 
 */
public class FreeHand extends Shape{
    private ArrayList<Integer>arrX= new ArrayList<>();
    private ArrayList<Integer>arrY= new ArrayList<>();
    
    private boolean erase;
    private int penTipSize;
//-----------------------------------------------------------
    public FreeHand(){
        
    }
    
    //Constructor 
    public FreeHand(int x1, int y1, int x2, int y2, boolean dash, boolean fill,Color c,boolean erase)
    {   
        super(x1, y1, x2, y2, false,false,c,erase);
        penTipSize=erase? 15 : 2;
         this.erase = erase;
    }
//-----------------------------------------------------------
   @Override
    public void drawShape(Graphics2D g2d)
    {
        
            
        g2d.setColor(getCurrentColor());
          
           
            
        arrX.add(getX2());
        arrY.add(getY2());
       
       if (getDash() == true) {
            g2d.setStroke(new BasicStroke(penTipSize));
        } else {
            g2d.setStroke(new BasicStroke(penTipSize));
        }
        for (int i = 0; i < arrX.size() - 1; i++) {
            g2d.drawLine(arrX.get(i), arrY.get(i), arrX.get(i + 1), arrY.get(i + 1));
        }
    }
    
}
