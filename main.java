
import processing.sound.*;
PFont f; 

import controlP5.*;
ControlP5 cp5;
Slider slider;


int sampleCount = 128;
float[] buffer = new float[sampleCount];

//vol = ellipse(300, 220, 35, 35);
//amp = ellipse(350, 270, 35, 35);

AudioSample wave;
Sound s;
Reverb reverb;

void setup() {
  size(640, 360);
  f = createFont("Arial",16,true);

  sineBuffer();

  s = new Sound(this);
  s.volume(0.2);

  println(buffer);

  wave = new AudioSample(this, buffer, 140*sampleCount);
  wave.loop();
  
  cp5 = new ControlP5(this);
  cp5.addSlider("Volume").setPosition(305,150).setSize(70,150);
  cp5.addSlider("Pitch").setPosition(440,150).setSize(70,150);
  
  
  reverb = new Reverb(this);

}

void draw() {
  color circle = color(#bdc1e7);
  color rectangles = color(#bdd6e7);
  color border2 = color(#e7bdd6);
  color buttons = color(#e7e3bd);
  color main = color(#d6e7bd);
  color border = color(#a0a6dc);
  color screen = color(#121530);
  //defining the colours used in this sketch as variables.
  
  
  //displaying background
  background(main);

//adding the navy screens you see on the synth.
  stroke(0);
  strokeWeight(3);
  fill(rectangles);
  rect(60, 20, 500, 300);
  stroke(border2);
  fill(screen);
  rect(70, 40, 200, 250);

  //displaying the buttons for differnt ways to manipulate sound.  
  fill(circle);
  strokeWeight(3);
  stroke(border);
  ellipse(300, 50, 30, 30); 
  ellipse(360, 50, 30, 30);
  ellipse(300, 95, 30, 30);
  ellipse(360, 95, 30, 30);
 
 

  //dial background lines for volume and sound
  stroke(border2);
  strokeWeight(3);
  fill(0);
  rect(300, 145, 80, 160);
  rect(435, 145, 80, 160);
  
  // smaller screen on sound synthesiser
  stroke(border2);
  strokeWeight(5);
  fill(screen);
  rect(390, 40, 150, 70);

  
  
  // y-axis grid lines for display waves
  stroke(border2);
  strokeWeight(0);
  fill(#d4e4f0);
  rect(72, 50, 197, 1);
  rect(72, 95, 197, 1);
  rect(72, 140, 197, 1);
  rect(72, 162, 197, 5);
  rect(72, 185, 197, 1);
  rect(72, 230, 197, 1);
  rect(72, 275, 197, 1);
  
  
  // x-axis grid lines for display waves
  rect(80, 42, 5, 247);
  rect(116, 42, 1, 247);
  rect(152, 42, 1, 247);
  rect(188, 42, 1, 247);
  rect(224, 42, 1, 247);
  rect(260, 42, 1, 247);
  
  
  //displaying graph co-ords for sinewave.
  text("-1",85,50);
  text("0",85,183);
  text("1",85,285);
  
  //telling user the keys to press if they want to change the setting of wave.
  text(" 'a' = Sawtooth wave", 395,62);
  text(" 'b' = Square wave ", 395,77);
  text(" 'c' = sine wave", 395,95);
  
  
  noFill();
  stroke(border2);
  strokeWeight(6);

//drawing the sinewave
  beginShape();
  for (int i = 0; i < buffer.length; i++) {
    float x = map(i, 0, buffer.length, 84, 268);
    float y = map(buffer[i], -1, 1, 252, 75);
    vertex(x, y);
  }
  endShape();
  
  //labelling the keyboard buttons
  textFont(f,9);                 
  fill(border);                        
  text("Pause",290,30);
  text("Play",290,75);
  text("Restart",340,30);
  text("Timbre",340,75);

  
  //labelling the keyboard buttons 
  text("1",297,51);
  text("2",297,96);
  text("3",357,51);
  text("4",357,96);

  
 // println(slider.getValue());
 // giving the dials for volume and pitch control over the sound / sine wave
 float currentVol = cp5.getController("Volume").getValue() / 100;
 wave.amp(currentVol);
 float currentPitch = cp5.getController("Pitch").getValue() / 100;
 wave.rate(currentPitch);
 
}

//letting the user adjust the sinewave by dragging the mouse  
void mouseDragged() {
    if(mousePressed && 
     mouseX >= 70 && 
     mouseX < 270 && 
     mouseY >= 40 && 
     mouseY < 290) {
    int index = int(map(mouseX, 0, width, 0, sampleCount-1));
      float value = map(mouseY, 0, height, 1, -1);
      if (index < buffer.length) {
        buffer[index] = value;
        wave.write(buffer);
  }
     }

}

void mouseReleased() {
  wave.write(buffer);
}


//defining certain keys the user can manipulate the sound with
//pauses the sound
void keyPressed() {
  if (key =='1') {
    ellipse(300, 50, 30, 30); 
//    noLoop();
    clearBuffer();
  }
  
  //resets the sound to its starting point when teh program is first run.
  if (key =='2') {
    sineBuffer();
    ellipse(300, 95, 30, 30);
  }
  
  //stops the sound 
  if (key =='3') {
    wave.stop();
    ellipse(360, 50, 30, 30);
  }
  
  //adding a reverb filter onto the sound
  if (key == '4') {
    reverb.process(wave);
    reverb.wet(0.5);
    ellipse(360, 95, 30, 30);
  }
  
  //writing different buffers for user, (which will produce different sounds and volumes too.)
  if (key =='c') {
    sineBuffer();
  }

  if (key =='b') {
    squareBuffer();
  }

  if (key == 'a') {
    sawBuffer();
  }

//adjusting the volume using keys is also an option. 
  if (key == 'q') {
    quieter();
  }

  if (key == 'l') {
    louder();
  }
  
  wave.write(buffer);
}



//defining the functions below that are used in the code above. 
void clearBuffer() {
  for (int i = 0; i < buffer.length; i++) {
    buffer[i] = 0;
  }
}

void sineBuffer() {
  for (int i = 0; i < sampleCount; i++) {
    buffer[i] = sin(i/float(sampleCount) * TWO_PI);
  }
}

void squareBuffer() {
  for (int i = 0; i < sampleCount; i++) {
    buffer[i] = i > sampleCount/2 ? -1 : 1;
  }
}

void sawBuffer() {
  for (int i = 0; i < sampleCount; i++) {
    buffer[i] = 1 - map(i, 0, sampleCount-1, 0, 2);
  }
}

void quieter() {
  for (int i = 0; i < sampleCount; i++) {
    buffer[i] = buffer[i] / 2;
  }
  
}

void louder() {
  for (int i = 0; i < sampleCount; i++) {
    buffer[i] = buffer[i] * 2;
  }
  
}