package evalproject;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.canvas.CEllipse;
import java.util.ArrayList;

/**
 * Display animation for the only targeted 
 * circle.
 * @author Leo
 */
 class AnimationCircle extends Animation {
        double translatex, translatey;
        Experiment experiment;
        ArrayList start_x;
        ArrayList start_y;
 
        public AnimationCircle(Experiment exp) {
            super();
            translatex = 20;
            translatey = 20;
            experiment = exp;
            start_x = new ArrayList();
            start_y = new ArrayList();
        }
 
        public void step(double t) {
                if (t < 0.5) {
                    experiment.getMovingNonTarget().translateBy(0, 4);
                }
                else {
                    experiment.getMovingNonTarget().translateBy(0, -4);
                }
        }
        
        // save starting coords
        public void doStart() {
            experiment.getMovingNonTarget().reset();
            while(experiment.getMovingNonTarget().hasNext()) {
                start_x.add(experiment.getMovingNonTarget().nextShape().getCenterX());
                start_y.add(experiment.getMovingNonTarget().nextShape().getCenterY());
            }
        }
        
        // put coords back
        public void doStop() {
            experiment.getMovingNonTarget().reset();
            int i = 0;
            while(experiment.getMovingNonTarget().hasNext()) {
                experiment.getMovingNonTarget().nextShape().translateTo((double) start_x.get(i), (double) start_y.get(i));
                i++;
            }
        }
 }