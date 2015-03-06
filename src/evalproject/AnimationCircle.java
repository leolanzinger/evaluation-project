package evalproject;

import fr.lri.swingstates.animations.Animation;

/**
 * Display animation for the only targeted 
 * circle.
 * @author Leo
 */
 class AnimationCircle extends Animation {
        double translatex, translatey;
        double start_x, start_y;
        Experiment experiment;
 
        public AnimationCircle(Experiment exp) {
                super();
                translatex = 20;
                translatey = 20;
                experiment = exp;
        }
 
        public void step(double t) {
                if (t < 0.5) {
                    experiment.getTarget().translateBy(0, 4);
                }
                else {
                    experiment.getTarget().translateBy(0, -4);
                }
        }
        
        // save starting coords
        public void doStart() {
            start_x = experiment.getTarget().getCenterX();
            start_y = experiment.getTarget().getCenterY();
        }
        
        // put coords back
        public void doStop() {
            experiment.getTarget().translateTo(start_x, start_y);
        }
 }