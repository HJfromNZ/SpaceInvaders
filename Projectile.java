package invadem;

import processing.core.PImage;
import java.util.ArrayList;

public class Projectile implements AutoMoves{
    private PImage[] projectileImage;
    private PImage currentImage;
    private int[] coordinates;
    private boolean fromTank; //boolean to determine direction of projectile (i.e. from tank or from invader)

    public Projectile(PImage[] projectileImage, int[] coordinates){
        this.projectileImage = projectileImage;
        this.currentImage = projectileImage[0];
        this.coordinates = coordinates;
        if(this.getY() > 400){ //below barrier
            this.fromTank = true;
        }
        else{
            this.fromTank = false;
        }
    }

    public PImage getCurrentImage() { return currentImage; }

    public void setCurrentImage(PImage image) { this.currentImage = image; }

    public PImage[] getProjectileImage() {
        return projectileImage;
    }

    public int getX() {
        return this.coordinates[0];
    }

    public void setY(int y) {
        this.coordinates[1] = y;
    }

    public int getY() {
        return this.coordinates[1];
    }

    public void move(){
        if(fromTank) {
            this.setY(this.getY() - 1);
        }
        else{
            this.setY(this.getY()+1);
        }
    }

    public boolean getFromTank(){
        return fromTank;
    }

    public boolean collision(GameCharacter obj){
        if(!this.getCurrentImage().equals(this.getProjectileImage()[1])) { //if the projectile's image isn't "empty"
            if(!obj.getCurrentImage().equals(obj.getImagesList()[obj.getImagesList().length - 1])) { //if the object's image isn't "empty"
                if (this.getX() > obj.getX() && this.getY() > obj.getY()) { //position starts in top left corner: only points to the right and bottom are considered
                    if (this.getX() - obj.getX() <= obj.getWidth() && this.getY() - obj.getY() <= obj.getHeight()) { //the "collidable" range depends on the size of the object's image
                        if(this.getProjectileImage().length == 3 && !(obj instanceof Tank)){ //if power projectile and BarrierComponent
                            obj.setLivesRemaining(0); //power projectiles destroy BarrierComponents in one shot
                        }
                        else {
                            obj.lifeLost(); //lose a life
                        }
                        this.setCurrentImage(this.getProjectileImage()[1]);
                        if(obj.getLivesRemaining() == 0) {
                            obj.setCurrentImage(obj.getImagesList()[obj.getImagesList().length - 1]);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

