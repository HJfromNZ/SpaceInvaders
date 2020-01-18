package invadem;

import processing.core.PImage;

public class Tank extends GameCharacter{
    public Tank(PImage[] imagesList, int[] coordinates){
        super(imagesList, coordinates);
        livesRemaining = 3;
        width = 22;
        height = 16;
    }

    public void move(boolean[] keysPressed){ //moving the tank
        if (this.getX() < 460 && keysPressed[0]) { //must be within boundaries; if keysPressed[0] == true => right arrow pressed
            this.setX(this.getX() + 1); //moving to the right
        }
        if (this.getX() > 180 && keysPressed[1]) { //must be within boundaries; if keysPressed[1] == true => left arrow pressed
            this.setX(this.getX() - 1); //moving to the left
        }
    }

}
