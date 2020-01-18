package invadem;

import processing.core.PApplet;
import processing.core.PImage;

import processing.core.PFont;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class App extends PApplet {

    protected Tank tank;
    protected ArrayList<Invader> invaders;
    protected BarrierComponent[][] barrierComponents;
    protected ArrayList<Projectile> projectiles;

    protected PImage empty;
    protected PImage gameover;
    protected PImage nextlevel;
    protected PImage heart;

    //PImage arrays for each character
    protected PImage[] tankImageList;
    protected PImage[] projectileImageList;
    protected PImage[] invadersImageList;
    protected PImage[] barriersLeftImageList;
    protected PImage[] barriersRightImageList;
    protected PImage[] barriersTopImageList;
    protected PImage[] barriersSolidImageList;
    protected PImage[] armouredInvadersImageList;
    protected PImage[] powerInvadersImageList;
    protected PImage[] projectile_lgImageList;

    protected boolean[] keysPressed; //array to determine whether left, right or space are pressed
    protected boolean[] keysPressedHomeScreen; //up down left right enter r;
    protected int[] arrowConditionsHomeScreen; //used to make sure that keys in home screen can't be held down

    protected boolean invaderMoves; //boolean to slow down movement of invaders to one pixel every 2 frames

    protected int frameCounter; //counter to hold "gameover" and "next level" images for a certain number of frames

    protected int shootCondition; //used for shooting - one projectile per press and release of the space bar

    protected boolean timeAttack; //determining which game mode
    protected boolean gameModeChosen; //determining if game mode chosen
    protected boolean endGame; //whether to end game

    protected int highScorePoints;
    protected int currentPoints;
    protected double highScoreTime;
    protected double currentTimeTaken; //for tracking hi score
    protected String currentTime; //for displaying time to 2dp

    protected int firingRate; //to adjust invaders' firing rate

    protected int level;
    protected double startTime;
    protected int startTimeReset; //used for the reset in home screen

    protected boolean highscoreNew; //to determine whether the score is a new high score

    @Override
    public void keyPressed() { //overriding the keyPressed() method in PApplet
        if(gameModeChosen) {
            if (keyCode == 39) { //keyCode for right arrow key
                keysPressed[0] = true;
            }
            if(keyCode == 37) { //keyCode for left arrow key
                keysPressed[1] = true;
            }
            if(keyCode == 32){ //keyCode for space bar
                keysPressed[2] = true;
                if(shootCondition == 0){ //condition to prevent shooting by holding down the space bar
                    shootCondition = 1;
                }
            }
        }
        else{
            if(keyCode == 39) {
                keysPressedHomeScreen[0] = true;
                if (arrowConditionsHomeScreen[0] == 0) { //can only be 0 if key previously released
                    arrowConditionsHomeScreen[0] = 1; //1 is checked
                }
            }
            if(keyCode == 37) {
                keysPressedHomeScreen[1] = true;
                if (arrowConditionsHomeScreen[1] == 0) {
                    arrowConditionsHomeScreen[1] = 1;
                }
            }
            if (keyCode == 38) {
                keysPressedHomeScreen[2] = true;
                if (arrowConditionsHomeScreen[2] == 0) {
                    arrowConditionsHomeScreen[2] = 1;
                }
            }
            if (keyCode == 40) {
                keysPressedHomeScreen[3] = true;
                if (arrowConditionsHomeScreen[3] == 0) {
                    arrowConditionsHomeScreen[3] = 1;
                }
            }
            if (key == 114) {
                keysPressedHomeScreen[4] = true;
            }
            if(key == RETURN || key == ENTER){
                keysPressedHomeScreen[5] = true;
            }
        }
    }

    @Override
    public void keyReleased() { //overriding the keyReleased() method in PApplet
        if(gameModeChosen) {
            if (keyCode == 39) {
                keysPressed[0] = false; //stops movement to the right
            }
            if(keyCode == 37) {
                keysPressed[1] = false; //stops movement to the left
            }
            if(keyCode == 32){
                keysPressed[2] = false; //stops shooting
                shootCondition = 0; //projectiles are only able to be shot if shootCondition == 0 => can only be shot again if space bar is released
            }
            if(key == ENTER){
                keysPressedHomeScreen[5] = false;
            }
            if(key == RETURN){
                keysPressedHomeScreen[5] = false;
            }
        }
        else{
            if(keyCode == 39){
                keysPressedHomeScreen[0] = false;
                arrowConditionsHomeScreen[0] = 0;
            }
            if(keyCode == 37){
                keysPressedHomeScreen[1] = false;
                arrowConditionsHomeScreen[1] = 0;
            }
            if(keyCode == 38){
                keysPressedHomeScreen[2] = false;
                arrowConditionsHomeScreen[2] = 0;
            }
            if(keyCode == 40){
                keysPressedHomeScreen[3] = false;
                arrowConditionsHomeScreen[3] = 0;
            }

            if(key == 114){
                keysPressedHomeScreen[4] = false;
            }
        }
    }

    public App() {

    }

    public void writeHighScores(){
        File f = new File("./src/main/resources/highscores.txt"); //only one file to write to
        try{
            PrintWriter pw = new PrintWriter(f);
            pw.println(highScorePoints);
            pw.printf("%.2f", highScoreTime); //2dp
            pw.close();
        }
        catch(FileNotFoundException e){
            text("NO HIGH SCORE FILE", 300, 100);
        }
    }

    public void readHighScores(){
        File f = new File("./src/main/resources/highscores.txt"); //only one file to read from
        try{
            Scanner sc = new Scanner(f);
            this.highScorePoints = Integer.parseInt(sc.nextLine());
            this.highScoreTime = Double.parseDouble(sc.nextLine());
        }
        catch(FileNotFoundException e){
            this.highScorePoints = 10000;
            this.highScoreTime = 500.00;
        }
    }

    public void resetHighScores(){
        this.highScorePoints = 10000;
        this.highScoreTime = 500.00;
        writeHighScores();
    }


    public void invadersSetup(){ //setting up the coordinates for the invaders, and adding them to the invader ArrayList
        ///let's say invaders extend past the boundary -> 300
        int tempCoordX = 195; //180 + 30/2
        int tempCoordY = 50; //set by eyeballing
        for(int i = 0; i<4; i++){
            if(i == 0){
                for(int j = 0; j<10; j++){
                    invaders.add(new ArmouredInvader(armouredInvadersImageList, new int[] {tempCoordX, tempCoordY}));
                    tempCoordX += 26; //should only have 30 gap on sides -> invader row is 250 pixels so gaps are 10
                }
            }
            else if(i == 1){
                for(int j = 0; j<10; j++){
                    invaders.add(new Invader(powerInvadersImageList, new int[] {tempCoordX, tempCoordY}));
                    tempCoordX += 26; //should only have 30 gap on sides -> invader row is 250 pixels so gaps are 10
                }
            }
            else {
                for (int j = 0; j < 10; j++) {
                    invaders.add(new Invader(invadersImageList, new int[]{tempCoordX, tempCoordY}));
                    tempCoordX += 26; //should only have 30 gap on sides -> invader row is 250 pixels so gaps are 10
                }
            }
            tempCoordX = 195;
            tempCoordY += 30;
        }
    }

    public void barrierComponentsSetup(){ //setting up barriers
        int tempCoordX = 200; //must be at least 20 pixels away from the left boundary of 180
        int tempCoordY = 408; //must be at least 10 pixels above the tank

        for(int p = 0; p<3; p++){
            barrierComponents[p][0] = new BarrierComponent(barriersLeftImageList, new int[] {tempCoordX, tempCoordY-16});
            barrierComponents[p][1] = new BarrierComponent(barriersTopImageList, new int[] {tempCoordX+8, tempCoordY-16});
            barrierComponents[p][2] = new BarrierComponent(barriersRightImageList, new int[] {tempCoordX+16, tempCoordY-16});
            barrierComponents[p][3] = new BarrierComponent(barriersSolidImageList, new int[] {tempCoordX, tempCoordY-8});
            barrierComponents[p][4] = new BarrierComponent(barriersSolidImageList, new int[] {tempCoordX+16, tempCoordY-8});
            barrierComponents[p][5] = new BarrierComponent(barriersSolidImageList, new int[] {tempCoordX, tempCoordY});
            barrierComponents[p][6] = new BarrierComponent(barriersSolidImageList, new int[] {tempCoordX+16, tempCoordY});
            tempCoordX += 112; //attempting even spacing between each barrier, with RHS barrier being at least 20 pixels away from right boundary of 460
        }
    }

    public void setup() { //runs once when the game starts
        frameRate(60);
        background(0); //a rgb value of 0 is a black background

        //loading all the images
        PImage barrier_left1 = loadImage("barrier_left1.png");
        PImage barrier_left2 = loadImage("barrier_left2.png");
        PImage barrier_left3 = loadImage("barrier_left3.png");
        PImage barrier_right1 = loadImage("barrier_right1.png");
        PImage barrier_right2 = loadImage("barrier_right2.png");
        PImage barrier_right3 = loadImage("barrier_right3.png");
        PImage barrier_solid1 = loadImage("barrier_solid1.png");
        PImage barrier_solid2 = loadImage("barrier_solid2.png");
        PImage barrier_solid3 = loadImage("barrier_solid3.png");
        PImage barrier_top1 = loadImage("barrier_top1.png");
        PImage barrier_top2 = loadImage("barrier_top2.png");
        PImage barrier_top3 = loadImage("barrier_top3.png");
        PImage invader1 = loadImage("invader1.png");
        PImage invader2 = loadImage("invader2.png");
        PImage projectile = loadImage("projectile.png");
        PImage tank1 = loadImage("tank1.png");
        PImage invader1_armoured = loadImage("invader1_armoured.png");
        PImage invader2_armoured = loadImage("invader2_armoured.png");
        PImage invader1_power = loadImage("invader1_power.png");
        PImage invader2_power = loadImage("invader2_power.png");
        PImage projectile_lg = loadImage("projectile_lg.png");

        empty = loadImage("empty.png");
        gameover = loadImage("gameover.png");
        nextlevel = loadImage("nextlevel.png");
        heart = loadImage("heart.png");

        //font for writing in the game
        PFont gameFont = createFont("PressStart2P-Regular.ttf", 320);
        textFont(gameFont);

        this.projectiles = new ArrayList<>(); //instantiating the ArrayList which will store Projectile instances
        this.invaders = new ArrayList<>(); //instantiating the ArrayList which will store Invader instances
        this.barrierComponents = new BarrierComponent[][] {new BarrierComponent[7], new BarrierComponent[7], new BarrierComponent[7]}; //an Array of BarrierComponents for each of the barriers
        this.invaderMoves = true;

        //initialising the arrays of PImages, which will be arguments for instances of their respective objects
        //most lists contain "empty" so that the images can "disappear" when needed
        this.tankImageList = new PImage[] {tank1, empty};
        this.projectileImageList = new PImage[] {projectile, empty};
        this.projectile_lgImageList = new PImage[] {projectile_lg, empty, empty}; //two empty images placed, so the length of this list is different from the regular projectile's. this way we can differentiate them, without creating a new class
        this.armouredInvadersImageList = new PImage[] {invader1_armoured, invader2_armoured, empty};
        this.powerInvadersImageList = new PImage[] {invader1_power, invader2_power, empty};
        this.invadersImageList = new PImage[] {invader1, invader2, empty};
        this.barriersLeftImageList = new PImage[] {barrier_left1, barrier_left2, barrier_left3, empty};
        this.barriersRightImageList = new PImage[] {barrier_right1, barrier_right2, barrier_right3, empty};
        this.barriersTopImageList = new PImage[] {barrier_top1, barrier_top2, barrier_top3, empty};
        this.barriersSolidImageList = new PImage[] {barrier_solid1, barrier_solid2, barrier_solid3, empty};

        this.tank = new Tank(tankImageList, new int[] {320, 430}); //the original position of the tank is bottom middle

        invadersSetup();

        barrierComponentsSetup();

        this.keysPressed = new boolean[3]; //one boolean for each: (right arrow; left arrow; space bar;)
        this.keysPressedHomeScreen = new boolean[6]; //right left up down r enter/return
        this.arrowConditionsHomeScreen = new int[] {0, 0, 0, 0}; //right left up down;
        this.shootCondition = 0; //starts at 0, so projectile can shoot on first press of space bar (otherwise would need to release then press again)
        this.frameCounter = 0; //starts at 0, so the "main" program can run
        this.gameModeChosen = false; //no game mode chosen at start
        this.timeAttack = false; //starts being false, since home screen starts with "action mode" selected
        this.endGame = false;
        this.firingRate = 5000; //once every 5 seconds initially;
        this.currentPoints = 0;

        readHighScores();

        level = 1;
        startTime = 0.0;
        startTimeReset = -1;

        highscoreNew = false;
    }

    public void nextLevel(){ //this plays the next level of the game, and is called ONLY when the PLAYER wins

        //resetting the tank
        this.tank = new Tank(tankImageList, new int[] {320, 430});

        //resetting the projectile and invader ArrayLists
        this.projectiles.clear();
        this.invaders.clear();

        invadersSetup();
        barrierComponentsSetup();

        this.keysPressed = new boolean[3]; //one boolean for each: (right arrow; left arrow; space bar)
        this.shootCondition = 0; //starts at 0, so projectile can shoot on first press of space bar (otherwise would need to release then press again)
        this.frameCounter = 0; //starts at 0, so the "main" program can run

        if(!timeAttack) { //adjustement for arcade mode only
            writeHighScores();
        }
        else{
            currentTimeTaken -= 5.0; //transition screens hold for 5 seconds, so this must be adjusted
        }

        if (firingRate > 1000) { //fastest rate is one projectile per second
            firingRate -= 1000;
        }

        level++;

    }

    public boolean gameoverMethod(){
        boolean trigger = false; //true if game over
        if (tank.getLivesRemaining() == 0) {
            trigger = true;
        }
        else{
            for (Invader inv : invaders) {
                if (inv.getY() >= 380 && !inv.getCurrentImage().equals(empty)) { //if the invaders reach the barrier
                    trigger = true;
                }
            }
        }
        if(trigger) {
            background(0);
            image(gameover, 260, 180);
            if (timeAttack) {
                currentTimeTaken = 0;
            } else { //if arcade mode
                if (currentPoints == highScorePoints && highscoreNew) {
                    textSize(16);
                    text("NEW HIGH SCORE!!! CONGRATULATIONS", 60, 250);
                    highscoreNew = false;
                }
                currentPoints = 0; //points reset to zero
            }
            frameCounter++; //makes frameCounter non-zero, so the initial frameCounter == 0 condition is false
            endGame = true;
            writeHighScores();
            return true;
        }
        return false;
    }

    public void chooseGameMode(){

        if(frameCounter == 0) {
            background(0);
            textSize(40);
            text("SPACE INVADERS", 50, 75);
            textSize(20);
            text("GAME MODE", 220, 200);
            textSize(12);
            text("ARCADE", 220, 230);
            text("TIME ATTACK", 220, 250);
            for (int i = 0; i < 4; i++) { //up down left right all do the same thing
                if (arrowConditionsHomeScreen[i] == 1) {
                    timeAttack = !timeAttack; //changes game mode
                    for(int j = 0; j<4; j++) {
                        arrowConditionsHomeScreen[i] = 2;
                    }
                    break;
                }
            }
            int y = 230;
            if (timeAttack) {
                y = 250;
            }
            text("<", 388, y);

            //gamemode descriptions
            textSize(8);
            if (timeAttack) {
                text("Complete 5 levels in the", 410, y);
                text("shortest time possible", 410, y+20);
            } else {
                text("Rack up as many points", 410, y);
                text("as possible", 410, y+20);
            } //instructions for the game modes
            text("Press and hold r to reset high scores", 50, 450);

            textSize(12);
            text("-----------------HIGH SCORES-----------------", 50, 350);
            text("ARCADE", 50, 380);
            text(this.highScorePoints, 450, 380);
            text("TIME ATTACK", 50, 410);
            text(Double.toString(this.highScoreTime), 450, 410);

            if (keysPressedHomeScreen[4]) {
                if (startTimeReset == -1) {
                    startTimeReset = second(); //time when "r" first pressed
                }

                int a = second();
                if ((Math.abs(a - startTimeReset) >= 5)) { //"r" must be held at least 5 seconds
                    resetHighScores();
                    startTimeReset = -1;
                } else {
                    int countdown = (5-Math.abs(a-startTimeReset)); //display countdown
                    text(countdown, 500, 450);
                    }
                if (startTimeReset == -1) {
                    text("RESETTING...", 400, 450);
                    frameCounter++; //used to hold the text for 3 seconds
                }

            }

            if (!keysPressedHomeScreen[4]) {
                startTimeReset = -1;
            }

            if (keysPressedHomeScreen[5]) { //if enter or return pressed
                gameModeChosen = true;
            }
        }
        else{
            frameCounter++;
            if(frameCounter == 180){
                frameCounter = 0;
            }
        }
    }

    public int heartsDraw(){
        int i = 0;
        int tempX = 5;
        while (i < tank.getLivesRemaining()) {
            image(heart, tempX, 5);
            tempX += 10;
            i++;
        }
        return i;
    }

    public boolean tankDraw() {
        try {
            image(this.tank.getCurrentImage(), this.tank.coordinates[0], this.tank.coordinates[1]); //drawing the tank
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean invadersDraw(){ //drawing the invaders
        try{
            for (Invader inv : invaders) {
                image(inv.getCurrentImage(), inv.getX(), inv.getY());
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean barriersDraw(){ //drawing the barriers
        try {
            for (BarrierComponent[] bcArray : barrierComponents) {
                for (BarrierComponent bc : bcArray) {
                    image(bc.getCurrentImage(), bc.getX(), bc.getY());
                }
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean invaderMovement(){ //invader movement
        //invader moves once every 2 frames
        if (invaderMoves) {
            for (Invader inv : invaders) {
                inv.move();
                image(inv.getCurrentImage(), inv.getX(), inv.getY());
            }
            invaderMoves = false; //boolean switches so that the invaders move once every 2 frames
            return true; //the invaders move
        } else {
            invaderMoves = true;
            return false; //invaders didn't move
        }
    }


    public int invaderShoot(boolean correctInterval){ //choosing the invader which shoots
        if (correctInterval) { //takes ~16 milliseconds to draw a frame => so this condition is true ~every firingRate/1000 seconds
            int chosenInvInd = (int) (Math.random() * 40); //Math.random() returns a double between 0.0 and 1.0 (exclusive). We manipulate to give us 0 to 39 inclusive
            while (invaders.get(chosenInvInd).getLivesRemaining() == 0) { //keep choosing if the chosen invader is dead
                chosenInvInd = (int) (Math.random() * 40);
            }
            if (chosenInvInd < 20 && chosenInvInd >= 10) {
                projectiles.add(new Projectile(projectile_lgImageList, new int[]{this.invaders.get(chosenInvInd).getX() + 5, this.invaders.get(chosenInvInd).getY() + 6})); //firing a projectile with invader's coordinates
            } else {
                projectiles.add(new Projectile(projectileImageList, new int[]{this.invaders.get(chosenInvInd).getX() + 5, this.invaders.get(chosenInvInd).getY() + 6})); //firing a projectile with invader's coordinates
            }
            return chosenInvInd; //return index of chosen invader
        }
        return -1; //if none chosen
    }

    public boolean invadersAllDead(){
        for (Invader inv : invaders) {
            if (inv.getLivesRemaining() > 0) {
                return false; //if any of the invaders is alive, then not all are dead
            }
        }
        return true;
    }

    public boolean tankShoot(){ //shooting from the tank
        if (keysPressed[2]) {
            if (shootCondition == 1) { //can only be 1 if shootCondition was previously 0
                projectiles.add(new Projectile(projectileImageList, new int[]{this.tank.getX() + tank.getWidth()/2, this.tank.getY() - tank.getHeight()/2})); //shooting from the tank's coordinates
                shootCondition = 2; //becomes !0 and !1, so that the space bar must be released (->shootCondition = 0) before another shot can be fired
                return true; //a projectile was shot
            }
        }
        return false; //no projectile shot
    }

    public boolean invadersDeadGameTransition(){
        if(invadersAllDead()){
            if(!timeAttack) {
                background(0);
                image(nextlevel, 260, 180);
            }
            else{
                background(0);
                if(level != 5) {
                    image(nextlevel, 260, 180);
                    currentTimeTaken -= 5; //since next level frame lasts for 5 seconds
                }
                else{
                    text("Your time was: " + currentTime + " seconds", 160, 200);
                    endGame = true; //ending time attack mode (!= player loses game)
                }

            }
            frameCounter++; //makes frameCounter non-zero, so the initial frameCounter == 0 condition is false
            return true;
        }
        return false;
    }

    public void checkIfNewHighScore(){
        if (currentPoints > highScorePoints) {
            highScorePoints = currentPoints;
            highscoreNew = true; //used so "NEW HIGH SCORE" message doesn't get printed if SAME as current high score
        }
    }

    public void projectileActions(){
        for (Projectile p : projectiles) {
            p.move();
            image(p.getCurrentImage(), p.getX(), p.getY());
            p.collision(tank); //checking collision with tank
            for (BarrierComponent[] bcArray : barrierComponents) { //checking collisions with barrier components
                for (BarrierComponent bc : bcArray) {
                    if (p.collision(bc)) {
                        bc.setCurrentImage(bc.getImagesList()[3 - bc.getLivesRemaining()]); //barrier components' image changes based on lives remaining
                    }
                }
            }
            for (Invader inv : invaders) { //checking collisions with invaders
                if (p.getFromTank()) { //only the tank's projectiles should be considered
                    if (p.collision(inv)) {
                        if (invaders.indexOf(inv) < 20 && inv.getCurrentImage().equals(empty)) { //if power or armoured invader
                            currentPoints += 250;
                        } else if (invaders.indexOf(inv) >= 20) {
                            currentPoints += 100;
                        }
                    }
                }
            }
        }
    }

    public void endGameTransition(){
        frameCounter++; //frameCounter increments by 1 every frame
        if(timeAttack){
            if(level == 5 && frameCounter >=100 && currentTimeTaken < highScoreTime){
                text("NEW HIGH SCORE!!! CONGRATULATIONS", 115, 250);
                highScoreTime = Double.parseDouble(String.format("%.2f", currentTimeTaken));
                writeHighScores();
            }
        }
        if (frameCounter >= 300) { //60 frames per second => 300 frames is 5 seconds
            if (endGame) {
                gameModeChosen = false;
                setup(); //resets arcade mode to level 1
            } else {
                this.nextLevel(); //progresses to next level
            }
        }
    }



    public boolean tick(){
        try{
            heartsDraw();
            tankDraw();
            barriersDraw();
            invadersDraw();

            invaderMovement();
            invaderShoot(Math.abs(millis() - startTime*1000)%firingRate <= 16);

            tank.move(keysPressed);
            tankShoot();

            //projectile moving, drawing, colliding
            projectileActions();

            //ways to end the game
            gameoverMethod();
            invadersDeadGameTransition();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public void settings() {
        size(640, 480);
    }

    public void draw() {

        if(!gameModeChosen) {
            chooseGameMode();
        }

        else {
            if(timeAttack){ //Time Attack mode;
                if (frameCounter == 0) { //used for delaying drawing (see else loop)

                    if(startTime == 0){
                        startTime = millis()/1000.00;
                    }

                    currentTimeTaken = millis()/1000.00 - startTime; //to account for time taken in game selection screen

                    currentTime = String.format("%.2f", currentTimeTaken);
                    background(0); //black background each time
                    text("HIGH SCORE:  " + highScoreTime, 400, 30);
                    text(currentTime, 47, 30);

                    tick();


                } else {
                    endGameTransition();
                }
            }

             else{ //arcade mode;
                 if (frameCounter == 0) { //used for delaying drawing (see else loop)

                     if(startTime == 0){
                         startTime = millis();
                     }

                     background(0); //black background each time
                     checkIfNewHighScore();

                     text("HIGH SCORE:  " + highScorePoints, 400, 30);

                     String pointsString = String.format("%05d", currentPoints);
                     text(pointsString, 47, 30);

                     tick();

                 } else {
                     endGameTransition();
                 }
             } //arcade mode
        }
    }

    public static void main(String[] args) {
        PApplet.main("invadem.App");

    }

}
