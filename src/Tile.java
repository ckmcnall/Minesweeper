
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Connor McNally
 */
public class Tile extends JButton {
    
    boolean isSelected;
    boolean isFlagged;
    boolean hasBomb;
    int number;
    int i;
    int j;
    
    public Tile(int I, int J){
        
        isSelected = false;
        isFlagged = false;
        hasBomb = false;
        number = 0;
        this.setRolloverEnabled(false);
        this.setMargin(new Insets(1,1,1,1));
        this.setFocusPainted(false);
        i = I;
        j = J;
             
                
    }
      
           
    
}
