
#include <FastLED.h>
#include <Wire.h>
#include <SparkFun_APDS9960.h>
#include <SoftwareSerial.h>
SoftwareSerial mySerial(4, 3); // RX, TX
// Pins Gesture Sensor Interrupt
#define APDS9960_INT    2 // Selected to be  an interrupt pin for the gesture sensor

// Global Variables
SparkFun_APDS9960 apds = SparkFun_APDS9960();
int isr_flag = 0; //initial interrupt flag



// the number of LEDS in our strip

#define NUM_LEDS 119

#define DATA_PIN 7 // LED Data PIN

//variables for LED control
int index = (rand() + 11) % NUM_LEDS; //  selecting random position where the red light will begin to move
int speed = 50; //starting speed
//constants for loop control
bool gameStarted = false; // to start Game when Java sends data
int maxSpeed = 10; //maximize the speed by minimizing the  delay between transitions is 10 ms
int minSpeed = 60; // minimize the speed by setting  delay to max value ->60msec
int topFocusIndex = 58; //the index at top focal centre  of loop
bool clockwise = false;
CRGB leds[NUM_LEDS]; // define array of 119 LEDS  for individual addressing in the loop


void setup() {

  //LED setup by inserting model, data pin and controls
  LEDS.addLeds<WS2812, DATA_PIN, RGB>(leds, NUM_LEDS);
  LEDS.setBrightness(140); // adjust the LED brightness to 140
  FastLED.clear(); //clear any old data in the LEDs
  preGameAnimation(); // animation before Game starts
  // Gesture Sensor set-up
  // Set interrupt pin as input
  pinMode(APDS9960_INT, INPUT);

  // Initialize Serial port to begin wireless communication
  mySerial.begin(9600);

  // Initialize interrupt service routine
  attachInterrupt(0, interruptRoutine, FALLING);

  // Initialize APDS-9960 (configure I2C and initial values)
  if ( !apds.init() ) { //if failed to init
    mySerial.write((byte)B00000000); // will be implemented in future
  }

  // Start running the APDS-9960 gesture sensor engine
  if ( !apds.enableGestureSensor(true) ) { //if failed to enable
    mySerial.write((byte)B00000000); // will be implemented in future
  }
}


void loop() {
  if (mySerial.available()) { //check for the beginning and end of game
    byte statusCode = (byte)mySerial.read(); // read the Data sent as byte - can read from other end node
    if (statusCode == B11111111) { //if Java sent 1111 1111 - game started - turn on LEDs
      gameStarted = true;
    } else if (statusCode == B00000000) { //if Java sent 0 - game ended so stopLED
      gameStarted = false;
      preGameAnimation();
    }
  }
  if (gameStarted) {
    //ensure index is valid
    index = (index + NUM_LEDS) % NUM_LEDS; // to continue looping correctly

    // in clockwise motion index 4= start of critical region and in anticlockwise motion index 115= critical region beginning
    if ((!clockwise && index == 115) || (clockwise && index == 4)) {  // critical region is  where the player must jump to score max  points
      mySerial.write(getByteCode(speed)); // XY00ZZZZ  X -Player Id =0 for player 1, Y - 1 as LED , ZZZZ = 1's for enter crit. region
    }else if ((clockwise && index == 115) || (!clockwise && index == 4)){
      mySerial.write((byte)B01000010); // XY00ZZZZ  X -Player Id =0 for player 1, Y - 1 as LED , ZZZZ = 1010's for exiting crit. region
    }
    //if a gesture is detected, handle it
    if ( isr_flag == 1 ) { // if interrupt flag has been raised  to 1 = a gesture is detected
      detachInterrupt(0);
      handleGesture(); // checks what gesture is detected and takes  appropriate action based on the  type of the gesture
      isr_flag = 0; // after handling the gesture reset interrupt flag to 0 for the next gesture recognition
      attachInterrupt(0, interruptRoutine, FALLING);
    }

    //light up 3 leds at the index
    lightLED(index);
    //Delay controls the speed of the loop motion
    delay(speed);
    if (clockwise) {
      --index;
    } else {
      ++index;
    }
  }
}


//Light up 7 LEDS at index and reset for next iteration
//CRGB( GREEN, RED , BLUE )
void lightLED(int index) { // takes the index and turns the led of the index and 3 leds before and after the index to Deep Red
  fill_solid( leds, NUM_LEDS, CRGB(224, 176, 230));
  for (int k = index - 3; k <= index + 3; k++) {
    leds[(k + NUM_LEDS) % NUM_LEDS] = CRGB(0, 255, 0); // Deep RED
  }

  FastLED.show();// flushes the color on the led strip immediately
  // clear this led for the next time around the loop
  for (int k = index - 3; k <= index + 3; k++) {
    leds[(k + NUM_LEDS) % NUM_LEDS] = CRGB::Black;
  }
}

//Interrupt routine for Gesture Sensor
void interruptRoutine() {
  isr_flag = 1;
}


//Function to handle Gestures

void handleGesture() {
  if ( apds.isGestureAvailable() ) {
    switch ( apds.readGesture() ) {
      case DIR_UP: // if an up gesture is detected  then decide direction based on the position of the index in the strip
        if (index > topFocusIndex) { // and index is above the topfocus, then  to move upwards  set motion direction to clockwise
          clockwise = true;
        } else {  // and if  index is below the topfocus, then  to move upwards  set motion direction to anti clockwise
          clockwise = false;
        }
        break;

      case DIR_DOWN:  // if an up gesture is detected  then decide direction based on the position of the index in the strip
        if (index > topFocusIndex) {
          clockwise = false; // and index is above the topfocus, then  to move downwards  set motion direction to anti-clockwise(i.e !clockwise)
        } else {
          clockwise = true;
        }
        break;

      case DIR_LEFT: // left gesture means reduce speed
        if (speed < minSpeed) {
          speed += 10; //increase delay by 5ms
        }
        break;

      case DIR_RIGHT: // right gesture means increase speed
        if (speed > maxSpeed) {
          speed -= 10; //decrease delay by 5ms
        }
        break;

    }
  }
}

//****************************Functions********************************

// 
byte getByteCode(int speed) {
if (speed== 10)
	return (byte)B01001101;
	
	else if (speed==20)
	return (byte)B01001100;
	
	else if (speed==30)
	return (byte)B01001011;
	
	else if (speed==40)
	return (byte)B01001010;
	
	else if (speed==50)
	return (byte)B01001001;
	
	else if (speed==60)
	return (byte)B01001000;
	
	
}

//Pre game animation
void preGameAnimation() {

  static uint8_t hue = 0; // color intensity
  // First slide the led in one direction
  for (int i = 0; i < NUM_LEDS; i++) {
    // Set the i'th led to red
    leds[i] = CHSV(hue++, 255, 255);
    // Show the leds
    FastLED.show();
    // now that we've shown the leds, reset the i'th led to black
    // leds[i] = CRGB::Black;
    fadeall();
    // Wait a little bit before we loop around and do it again
    delay(10);
  }

  // Now go in the other direction.
  for (int i = (NUM_LEDS) - 1; i >= 0; i--) {
    // Set the i'th led to red
    leds[i] = CHSV(hue++, 255, 255);
    // Show the leds
    FastLED.show();
    // now that we've shown the leds, reset the i'th led to black
    // leds[i] = CRGB::Black;
    fadeall();
    // Wait a little bit before we loop around and do it again
    delay(10);
  }
  fill_solid( leds, NUM_LEDS, CRGB(10, 150, 10)); //Fill color after animation -> sets all LEDs to white
  FastLED.show();
}
void fadeall() {
  for (int i = 0; i < NUM_LEDS; i++) {
    leds[i].nscale8(250);
  }
}
