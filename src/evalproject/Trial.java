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
    
    // general variables
    protected int block;
    protected int trial;
    protected String targetChange;
    protected int nonTargetsCount;
    protected Experiment experiment;
    protected int x_dim, y_dim;
    protected boolean hit;
    protected long start_time, completion_time;
    
    // visual object variables
    protected CEllipse target;
    protected CText trialNumber;
    protected CText instructions;
    protected CText instructionsText;
    protected ArrayList<CEllipse> ellipses;
    
    // animation variables
    protected AnimationCircle circleAnimation;
    protected MultipleAnimationCircle multipleCircleAnimation;
    protected ArrayList<MultipleAnimationCircle> multipleAnimationArray;
    
    /*
        Initialize Trial
    */
    public Trial(Experiment exp, int n_block, int n_trial, String tChange, int n_items) {
        block = n_block;
        trial = n_trial;
        targetChange = tChange;
        nonTargetsCount = n_items;
        experiment = exp;
        x_dim = experiment.x_dim;
        y_dim = experiment.y_dim;
    }
    
    /*
        Display instructions before each trials depending
        on which type of trial we are in
    */
    public void displayInstructions() {
        Canvas canvas = experiment.getCanvas();         
        // get trial infos
        trialNumber = new CText(new Point2D.Double(30, 30), "User: " + block + ", trial: " + trial , new Font("Helvetica", Font.BOLD , 26));
        instructionsText = new CText(new Point2D.Double(30, 60), "Instructions" , new Font("Helvetica", Font.BOLD , 26));

        // display instruction test depending on trial type
        if (targetChange.equals("VV1")) {
            instructionsText.setText("instruction for v1");
        }
        else if (targetChange.equals("VV2")) {
            instructionsText.setText("instruction for v2");
        }
        else {
            instructionsText.setText("instruction for v1v2");
        }
        instructions = new CText(new Point2D.Double(30, 120), "Press SPACE to begin test", new Font("Helvetica Neue", Font.BOLD , 18));
        
        // add tags and add instructions to canvas
        instructions.addTag(experiment.getInstructions());
        trialNumber.addTag(experiment.getInstructions());
        instructionsText.addTag(experiment.getInstructions());
        experiment.getCanvas().addShape(trialNumber);
        experiment.getCanvas().addShape(instructionsText);
        experiment.getCanvas().addShape(instructions);
    }
    
    /*
        Hide all instructions (call this function to do that)
    */
    public void hideInstructions() {
        // remove shapes and variables
        experiment.getCanvas().removeShapes(experiment.getInstructions());
        instructions.remove();
        trialNumber.remove();
        instructionsText.remove();
    }
    
    /*
        Start trial
    */
    public void start() {
        experiment.getCanvas().requestFocus();       
    }
    
    /*
        Show shapes and animations depending on
        trial type
    */
    public void showShapes() {
        Canvas canvas = experiment.getCanvas();
        ellipses = new ArrayList<CEllipse>();
        multipleAnimationArray = null;
        
        // debug
        System.out.println("----");
        System.out.println("starting trial with " + nonTargetsCount + " items.");
        
        // get row count
        int items_per_row = (int) Math.sqrt(nonTargetsCount);  
        // add ellipses in even rows
        for (int i = 1; i < (items_per_row) + 1; i++) {
            for (int j = 1; j < (items_per_row) + 1; j++) {
                final CEllipse ellipse = canvas.newEllipse(((i*(x_dim/items_per_row))-20)-(x_dim/(items_per_row * 2)), ((j*(y_dim/items_per_row))-20)-(y_dim/(items_per_row * 2)), 40, 40);
                ellipses.add(ellipse);
                ellipse.addTag(experiment.getExperimentShapes());
                ellipse.setFillPaint(Color.GRAY);
            }
        }
        
        // get a random target
        Random rand = new Random();
        int randomItem = rand.nextInt(((nonTargetsCount-1) - 0) + 1);
        target = ellipses.get(randomItem);
        ellipses.get(randomItem).addTag(experiment.getTarget());
        
        // set target red color
        if (targetChange.equals("VV2")) {
            target.setFillPaint(Color.RED);  
        }      
        // start animation
        else if (targetChange.equals("VV1")) {
            circleAnimation = new AnimationCircle(experiment);
            circleAnimation.setNbLaps(-1);
            circleAnimation.setLapDuration(200);
            circleAnimation.start();
        }
        // start animation and set red color
        else if (targetChange.equals("VV1VV2")) {
            // put animations inside an arrayList (used to stop all of them at the end)
            multipleAnimationArray = new ArrayList<MultipleAnimationCircle>();
            for (int i=0; i<ellipses.size(); i++) {
                // get a random between 0 and 2 (33% chance each)
                int randomColorCircle = rand.nextInt(3);
                // set red color (ellipse still)
                if (randomColorCircle == 1) {
                    ellipses.get(i).setFillPaint(Color.RED);
                }
                // animate ellipses (no color)
                else if (randomColorCircle == 2) {
                    multipleCircleAnimation = new MultipleAnimationCircle(experiment, ellipses.get(i));
                    multipleCircleAnimation.setNbLaps(-1);
                    multipleCircleAnimation.setLapDuration(200);
                    multipleCircleAnimation.start();
                    // add it to array
                    multipleAnimationArray.add(multipleCircleAnimation);
                }
            }
            // eventually set target color and animation
            target.setFillPaint(Color.RED);
            multipleCircleAnimation = new MultipleAnimationCircle(experiment, target);
            multipleCircleAnimation.setNbLaps(-1);
            multipleCircleAnimation.setLapDuration(200);
            multipleCircleAnimation.start();
            // add it to array
            multipleAnimationArray.add(multipleCircleAnimation);
        }
        // set start time
        start_time = System.currentTimeMillis();
    }
    
    /*
        Show white placeholders and stop
        ellipses animation
    */
    public void showPlaceHolders() {
        // set end time
        completion_time = start_time = (System.currentTimeMillis()) - start_time;
        // stop animation if only one is animating
        if (circleAnimation != null && multipleAnimationArray == null) {
            circleAnimation.stop();
        }
        // stop animations if different are animating
        else if (circleAnimation == null && multipleAnimationArray != null) {
            for (MultipleAnimationCircle animation : multipleAnimationArray) {
                animation.stop();
            }
            multipleAnimationArray = null;
        }
        // set all ellipses to white
        for (int i=0; i<ellipses.size(); i++) {
            ellipses.get(i).setFillPaint(Color.WHITE);
        }
    }
          
    /*
        Get current ellipse target
    */
    public CEllipse getTarget() {
        return target;
    }
    
    /*
        Stop trial
    */
    public void stop() {
        experiment.getCanvas().removeShapes(experiment.getInstructions());
        experiment.getCanvas().removeShapes(experiment.getExperimentShapes());
    }
}
