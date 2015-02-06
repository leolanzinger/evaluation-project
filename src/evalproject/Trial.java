/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import oracle.jrockit.jfr.JFR;

/**
 *
 * @author Leo
 */
class Trial {
    protected int block;
    protected int trial;
    protected String targetChange;
    protected int nonTargetsCount;
    protected Experiment experiment;
    
    int x_dim = 600;
    int y_dim = 600;
    
    public Trial() {
        
    }
    public void displayInstructions() {
         // ...
    }
    public void hideInstructions() {
         //experiment.getCanvas().removeShapes(experiment.getInstructions());
    }
    public void start() {
        JFrame frame = new JFrame();
        Canvas canvas = new Canvas(x_dim,y_dim);
        final CEllipse ellipse = canvas.newEllipse((x_dim/2)-20, (y_dim/2)-20, 40, 40);
        ellipse.setFillPaint(Color.GRAY);

        CStateMachine pressOnCircle = new CStateMachine() {
            State state = new State() {
                Transition pressOnShape = new PressOnShape(){
                    public void action() {
                        CShape shapePressed = getShape();
                        if(shapePressed == ellipse) {
                           shapePressed.setFillPaint(Color.WHITE);
                        }
                    }
                };
                Transition pressSpaceBar = new KeyPress(KeyEvent.VK_SPACE) {
                    public void action() {
                        
                    }
                };
            };
        };
                 
        pressOnCircle.attachTo(canvas);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.requestFocus();
        
         
    }
    public void stop() {}
}
