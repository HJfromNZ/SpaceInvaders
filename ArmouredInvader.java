package invadem;

import processing.core.PImage;

public class ArmouredInvader extends Invader {
    public ArmouredInvader(PImage[] invadersImageList, int[] coordinates) {
        super(invadersImageList, coordinates);
        this.livesRemaining = 3;
    }

}
