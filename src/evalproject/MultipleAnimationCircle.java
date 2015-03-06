package evalproject;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.canvas.CEllipse;

/**
 * Display animation for a targeted circle
 * use this function for multiple targeted 
 * animated ellipses.
 * @author Leo
 */
class MultipleAnimationCircle extends Animation {
        double translatex, translatey;
        double start_x, start_y;
        Experiment experiment;
        CEllipse ellipse;
 
        public MultipleAnimationCircle(Experiment exp, CEllipse ell) {
                super();
                translatex = 20;
                translatey = 20;
                experiment = exp;
                ellipse = ell;
        }
 
        public void step(double t) {
                if (t < 0.5) {
                    ellipse.translateBy(0, 4);
                }
                else {
                    ellipse.translateBy(0, -4);
                }
        }
        
        // save starting coords
        public void doStart() {
            start_x = ellipse.getCenterX();
            start_y = ellipse.getCenterY();
        }
        
        // put coords back
        public void doStop() {
            ellipse.translateTo(start_x, start_y);
        }
}
