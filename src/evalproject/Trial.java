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
    protected int participant;
    protected int block;
    protected int trial;
    protected String targetChange;
    protected int nonTargetsCount;
    protected Experiment experiment;
    
    protected String currentUser;
    
    protected CEllipse target;
    protected CText trialNumber;
    protected CText instructions;
    
    protected CText instructionsText;
    
    ArrayList<CEllipse> ellipses;
    AnimationCircle circleAnimation;
    int x_dim, y_dim;
    protected boolean hit;
    protected long start_time, completion_time;
    
    public Trial(Experiment exp,int n_participant, int n_block, int n_trial, String tChange, int n_items) {
        participant = n_participant;
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
         trialNumber = new CText(new Point2D.Double(30, 30), "User: " + participant + ", trial: " + trial , new Font("Helvetica Neue", Font.BOLD , 26));
         instructionsText = new CText(new Point2D.Double(30, 80), "Instructions" , new Font("Helvetica Neue", Font.BOLD , 18));
         if (targetChange.equals("VV2")) {
             // VV1
             instructionsText.setText("Target object: different hue / color");
         }
         else if (targetChange.equals("VV1")) {
             // VV2
             instructionsText.setText("Target object: moving object");
         }
         else {
             // VV1VV2
             instructionsText.setText("Target object: moving object with different hue / color");
         }
         
         instructions = new CText(new Point2D.Double(30, 140), "Press SPACE to begin test" + '\n' + "and press SPACE again when you recognized the target object.", new Font("Helvetica Neue", Font.BOLD , 12));
         instructions.addTag(experiment.getInstructions());
         trialNumber.addTag(experiment.getInstructions());
         instructionsText.addTag(experiment.getInstructions());
         experiment.getCanvas().addShape(trialNumber);
         experiment.getCanvas().addShape(instructionsText);
         experiment.getCanvas().addShape(instructions);
    }
    public void hideInstructions() {
         experiment.getCanvas().removeShapes(experiment.getInstructions());
         instructions.remove();
         trialNumber.remove();
         instructionsText.remove();
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
            target.addTag(experiment.getMovingNonTarget());
            circleAnimation = new AnimationCircle(experiment);
            circleAnimation.setNbLaps(-1);
            circleAnimation.setLapDuration(200);
            circleAnimation.start();
        }
        else if (targetChange.equals("VV1VV2")) {
            // start animation and set red color
            for (int i = 0; i < ellipses.size(); i++) {
                Random rand_red = new Random();
                int randomRedNumber = rand.nextInt((2) + 1) + 0;
                if (randomRedNumber == 0) {
                    ellipses.get(i).setFillPaint(Color.RED);
                }
                else if (randomRedNumber == 1) {
                    ellipses.get(i).setFillPaint(Color.GRAY);
                }
                else {
                    ellipses.get(i).addTag(experiment.getMovingNonTarget());
                }
            }
            target.setFillPaint(Color.RED);
            target.addTag(experiment.getMovingNonTarget());
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
