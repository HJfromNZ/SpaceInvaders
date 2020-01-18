package invadem;

import processing.core.PImage;

public abstract class GameCharacter {
    protected PImage[] imagesList; //each character will have associated images
    protected PImage currentImage;
    protected int[] coordinates;
    protected int width;
    protected int height;
    protected int livesRemaining;


    public GameCharacter(PImage[] imagesList, int[] coordinates){
        this.imagesList = imagesList;
        this.currentImage = imagesList[0];
        this.coordinates = coordinates;
    }

    public PImage[] getImagesList() {
        return imagesList;
    }

    public PImage getCurrentImage() { return currentImage; }

    public void setCurrentImage(PImage currentImage) {
        if(!this.getCurrentImage().equals(this.getImagesList()[this.getImagesList().length-1])) {
            this.currentImage = currentImage;
        }
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setX(int x){
        this.getCoordinates()[0] = x;
    }

    public int getX(){
        return this.getCoordinates()[0];
    }

    public void setY(int y){
        this.getCoordinates()[1] = y;
    }

    public int getY(){
        return this.getCoordinates()[1];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    public void setLivesRemaining(int livesRemaining) {
        this.livesRemaining = livesRemaining;
    }

    public void lifeLost() {
        this.livesRemaining -= 1;
    }

}
