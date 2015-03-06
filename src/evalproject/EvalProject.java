package evalproject;

import java.io.File;

/**
 * @author Leo
 */
public class EvalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Experiment experiment = new Experiment("0", 1, 1, new File("experiment.csv"));
        
        // uncomment next lines to load debug version of the csv starting from VV
        //Experiment experiment = new Experiment("0", 1, 1, new File("experiment_debug.csv"));
        //Experiment experiment = new Experiment("0", 1, 1, new File("experiment_debug_2.csv"));
    }
}
