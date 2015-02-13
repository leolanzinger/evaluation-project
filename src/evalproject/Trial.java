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
    protected CText instructions;
    
    ArrayList<CEllipse> ellipses;
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
         instructions = new CText(new Point2D.Double(30, 30), "instructions" + experiment.currentTrial + ", press ENTER to begin test", new Font("Garamond", Font.BOLD , 11));
         instructions.addTag(experiment.getInstructions());
         experiment.getCanvas().addShape(instructions);
    }
    public void hideInstructions() {
         experiment.getCanvas().removeShapes(experiment.getInstructions());
         instructions.remove();
    }
    public void start() {
        experiment.getCanvas().requestFocus();       
    }
    
    public void showShapes() {
        if (targetChange.equals("VV2")) {
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
        }
        Random rand = new Random();
        int randomItem = rand.nextInt(((nonTargetsCount-1) - 0) + 1);

        target = ellipses.get(randomItem);
        target.setFillPaint(Color.RED);       
        
        // set start time
        start_time = System.currentTimeMillis();
    }
    
    public void showPlaceHolders() {
        // set end time
        completion_time = start_time = (System.currentTimeMillis()) - start_time;
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
