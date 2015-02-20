/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.events.VirtualEvent;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
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
    
    protected CEllipse target;
    protected CText trialNumber;
    protected CText instructions;
    
    protected CText transitionText;
    protected CText transitionCountDown;
    
    ArrayList<CEllipse> ellipses;
    AnimationCircle circleAnimation;
    int x_dim, y_dim;
    protected boolean hit;
    protected long start_time, completion_time;
    
    public Trial(Experiment exp, int n_block, int n_trial, String tChange, int n_items) {
        block = n_block;
        trial = n_trial;
        targetChange = tChange;
        nonTargetsCount = n_items;
        experiment = exp;
        x_dim = experiment.x_dim;
        y_dim = experiment.y_dim;
    }
    public void displayInstructions() {
         Canvas canvas = experiment.getCanvas();
         trialNumber = new CText(new Point2D.Double(30, 30), "Trial: " + experiment.currentTrial + "/" + experiment.allTrials.size(), new Font("Helvetica", Font.BOLD , 26));
         instructions = new CText(new Point2D.Double(30, 90), "Press ENTER to begin test", new Font("Helvetica Neue", Font.BOLD , 18));
         instructions.addTag(experiment.getInstructions());
         experiment.getCanvas().addShape(trialNumber);
         experiment.getCanvas().addShape(instructions);
    }
    public void hideInstructions() {
         experiment.getCanvas().removeShapes(experiment.getInstructions());
         instructions.remove();
         trialNumber.remove();
    }
    public void displayTransition() {
         Canvas canvas = experiment.getCanvas();
         transitionText = new CText(new Point2D.Double(100, 250), "The next trial starts in", new Font("Helvetica Neue", Font.BOLD , 26));
         transitionText.addTag(experiment.getTransitions());
         experiment.getCanvas().addShape(transitionText);
         
        // Show numbers for transition
         for (int i = 0; i < 3; i++) {
             String secondLeft = Integer.toString(i+1);
             transitionCountDown = new CText(new Point2D.Double(100, 250), secondLeft , new Font("Helvetica Neue", Font.BOLD , 45));
             transitionCountDown.addTag(experiment.getTransitions());
             experiment.getCanvas().addShape(transitionCountDown);
            /*try {
                Thread.sleep(1000);                 // 1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            experiment.getCanvas().removeShapes(experiment.getTransitions());
        }
        // transition event
        System.out.println("Trying to trigger event");
        experiment.expStateMachine.processEvent(new VirtualEvent("transitionCompleted"));
    }
    public void hideTransition() {
         experiment.getCanvas().removeShapes(experiment.getTransitions());
         transitionText.remove();
         transitionCountDown.remove();
    }
    
    public void start() {
        experiment.getCanvas().requestFocus();       
    }
    
    public void showShapes() {
        Canvas canvas = experiment.getCanvas();
        ellipses = new ArrayList<CEllipse>();
        System.out.println("----");
        System.out.println("starting trial with " + nonTargetsCount + " items.");
        int items_per_row = (int) Math.sqrt(nonTargetsCount);
        for (int i = 1; i < (items_per_row) + 1; i++) {
            for (int j = 1; j < (items_per_row) + 1; j++) {
                final CEllipse ellipse = canvas.newEllipse(((i*(x_dim/items_per_row))-20)-(x_dim/(items_per_row * 2)), ((j*(y_dim/items_per_row))-20)-(y_dim/(items_per_row * 2)), 40, 40);
                ellipses.add(ellipse);
                ellipse.addTag(experiment.getExperimentShapes());
                ellipse.setFillPaint(Color.GRAY);
            }
        }
        Random rand = new Random();
        int randomItem = rand.nextInt(((nonTargetsCount-1) - 0) + 1);
        target = ellipses.get(randomItem);
        ellipses.get(randomItem).addTag(experiment.getTarget());
        
        if (targetChange.equals("VV2")) {
            // set red color
            target.setFillPaint(Color.RED);  
        }     
        else if (targetChange.equals("VV1")) {
            // start animation
            circleAnimation = new AnimationCircle(experiment);
            circleAnimation.setNbLaps(-1);
            circleAnimation.setLapDuration(200);
            circleAnimation.start();
        }
        else if (targetChange.equals("VV1VV2")) {
            // start animation and set red color
            target.setFillPaint(Color.RED);
            circleAnimation = new AnimationCircle(experiment);
            circleAnimation.setNbLaps(-1);
            circleAnimation.setLapDuration(200);
            circleAnimation.start();
        }
        // set start time
        start_time = System.currentTimeMillis();
    }
    
    public void showPlaceHolders() {
        // set end time
        completion_time = start_time = (System.currentTimeMillis()) - start_time;
        // stop animation
        if (circleAnimation != null) {
            circleAnimation.stop();
        }
        System.out.println(completion_time);
        for (int i=0; i<ellipses.size(); i++) {
            ellipses.get(i).setFillPaint(Color.WHITE);
        }
    }
            
    public CEllipse getTarget() {
        return target;
    }
    
    public void stop() {
        experiment.getCanvas().removeShapes(experiment.getInstructions());
        experiment.getCanvas().removeShapes(experiment.getExperimentShapes());
    }
}
