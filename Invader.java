package invadem;

import processing.core.PImage;

public class Invader extends GameCharacter implements AutoMoves{

    private int direction; //1 if moving to right; -1 if moving left
    private boolean someBoolean;
    private int originalX;
    private int downStepsTaken;

    public Invader(PImage[] invadersImageList, int[] coordinates){
        super(invadersImageList, coordinates);
        this.livesRemaining = 1;
        this.direction = 1; //game starts with invaders moving right
        someBoolean = true;
        this.originalX = this.getX();
        this.downStepsTaken = 0;
        width = 10;
        height = 12;
    }

    public void changeDirection(){
        this.direction = -direction;
    }

    public int getDirection(){
        return this.direction;
    }

    public int getOriginalX() {
        return originalX;
    }

    public int getDownStepsTaken() {
        return downStepsTaken;
    }

    public void setDownStepsTaken(int downStepsTaken) {
        this.downStepsTaken = downStepsTaken;
    }

    public void incrementDownSteps() {
        this.downStepsTaken++;
    }

    public void move(){

        if((Math.abs(this.getX()-this.getOriginalX()) == 30 && someBoolean)){ //if moves 30 away from original, then shift down
            this.setCurrentImage(this.getImagesList()[1]); //set to appropriate sprite
            this.setY(this.getY()+1);
            this.incrementDownSteps();
            if(this.getDownStepsTaken() == 8) {
                someBoolean = false; //to stop moving down, and begin moving in opposite direction
                this.setDownStepsTaken(0);
                this.changeDirection();
            }
        }
        else{
            if(!someBoolean) {
                this.setCurrentImage(this.getImagesList()[0]);
            }
            this.setX(this.getX()+direction);
            someBoolean = true; //true so able to move down again once 30 away from original
        }
    }
}

