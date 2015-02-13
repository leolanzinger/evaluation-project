/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Leo
 */
class Experiment {
    // experiment.csv
    protected File designFile = null;
    protected PrintWriter pwLog = null;
    protected ArrayList<Trial> allTrials = new ArrayList<Trial>();
    protected int currentTrial = 0;
    protected String participant;
    protected File logFile;

    protected CExtensionalTag experimentShape = new CExtensionalTag() {};
    protected CExtensionalTag instruction = new CExtensionalTag() {};
    protected CExtensionalTag target = new CExtensionalTag() {};

    public int x_dim = 600;
    public int y_dim = 600;
    public JFrame frame;
    public Canvas canvas;
    CStateMachine expStateMachine;

    public Experiment(String participantName, int block, int trial, File designFile) {
        participant = participantName;
        this.designFile = designFile;

        frame = new JFrame();
        canvas = new Canvas(x_dim,y_dim);

        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setStateMachine();
        loadTrials();
        initLog();
        nextTrial();
    }
    public void loadTrials() {
        allTrials.clear();
        // read the design file and keep only the trials to run
        try {
              BufferedReader br = new BufferedReader(new FileReader(designFile));
              String line = br.readLine();
              line = br.readLine();
              while(line != null) {
                   String[] parts = line.split(",");
                   int n_block = Integer.parseInt(parts[2]);
                   int n_trial = Integer.parseInt(parts[3]);
                   String targetChange = parts[4];
                   int n_items = Integer.parseInt(parts[5]);
                   allTrials.add(new Trial(this, n_block, n_trial, targetChange, n_items));
                   line = br.readLine();
         }
              br.close();
        } catch (FileNotFoundException e) {
              e.printStackTrace();
        } catch (IOException e) {
              e.printStackTrace();
        }
    }
    public void trialCompleted() {
           Trial trial = allTrials.get(currentTrial);
           trial.stop();
           log(trial);
           currentTrial++;
           nextTrial();
    }
    public void log(Trial trial) {
        String row = trial.block + "\t"
                   + trial.trial +"\t"
                   + trial.targetChange + "\t"
                   + trial.nonTargetsCount + "\t"
                   + trial.completion_time +"\t"
                   + trial.hit + "\n";
        pwLog.append(row);
        pwLog.flush();
    }
    public void initLog() {
        String logFileName = "log_S"+ participant +"_"+(new Date()).toString()+".csv";
        logFile = new File("logs/" + logFileName);
        try {
              pwLog = new PrintWriter(logFile);
              String header = "Block\t"
                         +"Trial\t"
                         +"TargetChange\t"
                         +"NonTargetsCount\t"
                         +"Duration\t"
                         +"Hit\n";
              pwLog.append(header);
              pwLog.flush();
        } catch (FileNotFoundException e) {
              e.printStackTrace();
        }
    }
    public void stop() {
           // display a "thank you" message
    }

    public void setStateMachine() {
        expStateMachine = new CStateMachine() {
            State instructionsShown = new State() {
                Transition pressSpaceBar = new KeyPress(KeyEvent.VK_ENTER, ">> shapesShown") {
                    public void action() {
                        allTrials.get(currentTrial).hideInstructions();
                        allTrials.get(currentTrial).showShapes();
                    }
                };
            };
            State shapesShown = new State() {
                Transition pressSpaceBar = new KeyPress(KeyEvent.VK_SPACE, ">> placeholdersShown") {
                    public void action() {
                        allTrials.get(currentTrial).showPlaceHolders();
                    }
                };
            };
            State placeholdersShown = new State() {
                Transition pressOnShape = new PressOnShape(">> instructionsShown"){
                    public void action() {
                        CShape shapePressed = getShape();
                        if(shapePressed == allTrials.get(currentTrial).getTarget()) {
                            allTrials.get(currentTrial).hit = true;
                            System.out.println("correct");
                        }
                        else {
                            allTrials.get(currentTrial).hit = false;
                            System.out.println("wrong");
                        }
                        trialCompleted();
                    }
                };
            };
        };

        expStateMachine.attachTo(canvas);
    }

    public void nextTrial() {
            if(currentTrial >= allTrials.size()) {
             stop(); 
            }
            Trial trial = allTrials.get(currentTrial);
            trial.displayInstructions();
            trial.start();
     }

    public Canvas getCanvas() {
        return canvas;
    }

    public CExtensionalTag getExperimentShapes() {
        return experimentShape;
    }

    public CExtensionalTag getInstructions() {
        return instruction;
    }
    
    public CExtensionalTag getTarget() {
        return target;
    }
}
