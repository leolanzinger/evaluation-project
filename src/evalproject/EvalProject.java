/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evalproject;

import java.io.File;

/**
 *
 * @author Leo
 */
public class EvalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Experiment experiment = new Experiment("0", 1, 1, new File("experiment.csv"));
    }
}
