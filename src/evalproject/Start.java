/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import javax.swing.JButton;

/**
 *
 * @author domingo
 */
class Start {
    
    protected Experiment experiment;
    protected JButton button;
    
    public Start(Experiment exp) {
        experiment = exp;
        button = new JButton("start");
    }
    
    public void removeStart() {
        
    }
}
