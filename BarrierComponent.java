package invadem;

import processing.core.PApplet;
import processing.core.PImage;

public class BarrierComponent extends GameCharacter{

    public BarrierComponent(PImage[] barrierImageList, int[] coordinates){
        super(barrierImageList, coordinates);
        livesRemaining = 3;
        width = 8;
        height = 8;
    }
}
