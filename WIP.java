////Nick Ferro
//// Project 3   11-7-15    Prototyping
//// Pool table elastic collisions
//// objects and loops
Ball a,b,c,d,e,q;
PTable t;
Button one, two, three, four;
Bird brd;

//SETUP: object stuff
void setup() {
  size( 720, 480 );
  t= new PTable();
  t.left=   50;
  t.right=  width-50;
  t.top=    150;
  t.bottom= height-50;
  t.middle= t.left + (t.right-t.left) / 2;
  t.wall=true;
  //
  a=  new Ball();
  a.r=255;
  a.b=255;
  a.name=  "1";
  //
  b=  new Ball();
  b.g=255;
  b.name=  "2";
  //
  c=  new Ball();
  c.g=255;
  c.b=255;
  c.name=  "3";
  //
  d=  new Ball();
  d.g=127;
  d.b=127;
  d.name=  "4";
  //
  e=  new Ball();
  e.r=127;
  e.name=  "5";
  //
  q=  new Ball();
  q.r=255;
  q.g=255;
  q.b=255;
  q.name=  " ";
  //
  one= new Button(50,5);
  one.words= "Reset";
  //
  two= new Button(140,5);
  two.words= "Wall";
  //
  three= new Button(230,5);
  three.words= "Bird";
  //
  four= new Button(320,5);
  four.words= "Rat";
  //
  brd= new Bird();
  brd.y = 90;
  brd.by = 95;
  
  
  reset();
}

//RESET function for balls
void reset() {
  a.reset();
  b.reset();
  c.reset();
  d.reset();
  e.reset();
  q.resetCue();
  t.wall = true;
}
 
//main DRAW function
void draw() {
  background( 250,250,200 );
  t.tableDisplay();
  balls();
  buttons();
  birds();
  fill(0);
  if (brd.drop == false){
  text("0", 50, height-50);
  }else{
  text("1", 50, height-50); 
  }
}

// handles the COLLISION, MOVEMENT, and DISPLAY of BALLS
void balls() {
  collision( a, b );
  collision( a, c );
  collision( a, d );
  collision( a, e );
  collision( a, q );
  //
  collision( b, c );
  collision( b, d );
  collision( b, e );
  collision( b, q );
  //
  collision( c, d );
  collision( c, e );
  collision( c, q );
  //
  collision( d, e );
  collision( d, q );
  collision( e, q );
  //
  a.move();
  b.move();
  c.move();
  d.move();
  e.move();
  q.move();
  //
  a.show();
  b.show();
  c.show();
  d.show();
  e.show();
  q.show();
}
// SWAP VELOCITIES for the collisions
void collision( Ball p, Ball q ) {
  if ( p.hit( q.x,q.y ) ) {
    float tmp;
    tmp=p.dx;  p.dx=q.dx;  q.dx=tmp;     
    tmp=p.dy;  p.dy=q.dy;  q.dy=tmp; 
  }
}


//DISPLAY BUTTONS
void buttons(){
  one.buttonDisplay();
  two.buttonDisplay();
  three.buttonDisplay();
  three.buttonBirdBuffer();
  four.buttonDisplay();
}

void birds(){
  brd.moveBird();
  brd.showBird();
  brd.bombDrop();
}
// KEY PRESSED stuff: exit, reset, other buttons
void keyPressed() {
  if (key == 'q') exit();
  if (key == 'r') reset();
  if (key == 'w') t.wall = false;
  if (key == 'b') brd.fly = true;
}

//MOUSEPRESSED: hits for the button functions
void mousePressed() {
 one.buttonReset();
 two.buttonWall();
 three.buttonBird();
}


//BALL CLASS
class Ball {
  // PROPERTIES
  float x,y, dx,dy;
  int r,g,b;
  String name="";
  
  // METHODS
  void show() {
    stroke(0);
    strokeWeight(1);
    fill(r,g,b);
    ellipse( x,y, 30,30 );
    fill(0);
    text( name, x-5,y );
  }
  void move() {
    if (t.wall) {
      if (x>t.right-4 || x<t.middle+20) {  dx=  -dx; }    //where the balls bounce off of depending on
    }else{                                                //whether the wall is engaged or not
      if (x>t.right-4 || x<t.left+4) {  dx=  -dx; }
    }
    if (y>t.bottom-4 || y<t.top+4) {  dy=  -dy; }
    x=  x+dx;
    y=  y+dy;
  }
  void reset() {                //resets the ball on right side random position with random velocity
    x=  random( (width/2)+50, t.right );    
    y=  random( t.top, t.bottom );
    dx=  random( -5,5 );
    dy=  random( -3,3 );
  }
  void resetCue() {            //reset for the cue ball on the left side centered with no velocity
    x= width/4;
    y= (t.bottom+t.top)/2;
    dx= 0; dy= 0;
  }
  boolean hit( float x, float y ) {                        //if distance between two balls is less than thirty, set hit = true
    if (  dist( x,y, this.x,this.y ) < 30 ) return true;   //if hit = true, triggers the collision function which swaps velocities
    else return false;
  }
}

//PTABLE CLASS
class PTable {
  //PROPERTIES
  float left, right, top, bottom, middle;
  boolean wall;
  
  //METHODS
  void tableDisplay(){
    fill( 100, 250, 100 );    
    strokeWeight(20);
    stroke( 127, 0, 0 );      // walls of pool talbe
    rectMode( CORNERS );
    rect( left-20, top-20, right+20, bottom+20 );
    stroke(0);
    strokeWeight(1);
    
    if (wall) {
      stroke( 0, 127, 0 );
      strokeWeight(10);
      line( middle,top-4, middle,bottom+4 );
    }
  }
} 

class Bird {
  float x,y,by,bDY;
  int frame;
  boolean fly, drop;
  
  Bird() {
    fly = false;
    drop = false;
    x = -50;
    bDY = 2;
  }
  // movement for the bird. when the bird crosses the right edge of the screen, it 
  // resets a ton of variables all tied to the bird flight and bomb droping mechanic
  void moveBird(){
    if (fly)
    x +=4;
    if (x>width+50){
      x= -50;
      fly = false;             //causes the bird to fly when true
      drop =false;             //causes the bomb to drop when true
      three.counter = false;   //when true, starts the buffer counter
      three.buffer = 0;        //counts up when counter is true, enables drop to use the same button as fly on subsequent clicks
      by = 90;
      bDY = 2;
      
    }
  }
  //Animation and display for birds. Frame count is for wing animation
  void showBird(){                 
    frame +=1;
    stroke(0);
    strokeWeight(1);
    fill(192);
    ellipse(x,y,35,13);
    ellipse(x+17, y-5, 15,10);
    triangle(x+24, y-6, x+30, y-4, x+24, y-2);
    triangle(x-18, y-2, x-30, y-4, x-19, y);
    if(frame/15 % 4 == 0){
      triangle(x-6, y-3, x+18, y-17, x+5, y);
    }else if(frame/15 % 4 == 1){
      triangle(x-7, y-3, x, y-1, x+6, y);
    }else if(frame/15 % 4 == 2){
      triangle(x-6, y-3, x-18, y+17, x+5, y);
    }else if(frame/15 % 4 == 3){
     triangle(x-4, y-8, x-13, y+7, x+7, y-5);
    }
  }
  //draws bomb and sends it downward with increasing velocity
  void bombDrop(){
    if (drop == true){
        noStroke();
        fill(105);
        rect(x,by,10,10);
        ellipse(x+5,by,10,10);
        ellipse(x+5,by+10,10,10);
        triangle(x+5,by, x,by-13, x+10,by-13);
        by += bDY;
        bDY += .25;
    }
  }
      
  
}
class Button {
  //PROPERTIES
  float x,y;
  String words;
  boolean counter;
  int buffer;
  //CONSTRUCTOR
  Button(float tempX, float tempY) {
    x = tempX;
    y = tempY;
    counter = false;
    buffer = 0;
  }
  //METHODS
  void buttonDisplay(){
    fill(0);
    strokeWeight(4);
    stroke(255);
    rectMode( CORNER );
    rect(x, y, 80, 40);
    fill(255);
    text( words, x+15, y+20);
  }
  //resets balls and wall
  void buttonReset(){
    if (mouseX >x && mouseX<x+80 && mouseY>y && mouseY<y+40){
    reset();
  }
 }
 //disables wall
 void buttonWall(){
  if (mouseX >x && mouseX<x+80 && mouseY>y && mouseY<y+40){
    t.wall = false;
  }
 }
 //multifunctional button. Sends the bird flying on the first click
 //and drops a bomb on the second click, then does nothing untill the
 //bird completes its flight.
 void buttonBird(){
   if (mouseX >x && mouseX<x+80 && mouseY>y && mouseY<y+40){
     brd.fly = true;
     counter = true;
     if (buffer > 1){
       brd.drop = true;
     }
   }
 }
 //Buffer counter for the bird button. Button will only enable drop if at least
 //two frames have passed since fly was enabled.
 void buttonBirdBuffer(){
   if (counter == true) {
     buffer +=1;
       }   
     }
   }
 

  
