/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

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
    
    public Experiment(String participantName, int block, int trial, File designFile) {
        participant = participantName;
        this.designFile = designFile;
        loadTrials();
        //initLog();
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
                   int test = Integer.parseInt(parts[5]);
                   allTrials.add(new Trial());
                   line = br.readLine();
                   System.out.println(test);
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
    }
    public void initLog() {
        String logFileName = "log_S"+ participant +"_"+(new Date()).toString()+".csv";
        File logFile = new File(logFileName);
        try {
              pwLog = new PrintWriter(logFile);
              String header = "Block\t"
                         +"Trial\t"
                         +"TargetChange\t"
                         +"NonTargetsCount\t"
                         +"Duration\t"
                         +"Hit\n";
              pwLog.print(header);
              pwLog.flush();
        } catch (FileNotFoundException e) {
              e.printStackTrace();
        }
    }
    public void stop() {
           // display a "thank you" message
    }
    public void nextTrial() {
           if(currentTrial >= allTrials.size()) {
            stop(); 
           }
           Trial trial = allTrials.get(currentTrial);
           trial.displayInstructions();
           trial.start();
    }
}
