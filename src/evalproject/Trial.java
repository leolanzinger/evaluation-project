/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

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
    public Trial() {
        
    }
    public void displayInstructions() {
         // ...
    }
    public void hideInstructions() {
         //experiment.getCanvas().removeShapes(experiment.getInstructions());
    }
    public void start() {
         // ...
         // install the graphical listener and the user input listener
         // call experiment.trialCompleted(); when appropriate
    }
    public void stop() {}
}
