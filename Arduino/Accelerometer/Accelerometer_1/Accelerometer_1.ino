
// include SoftwareSerial library for wireless communication using xbee pro
#include <SoftwareSerial.h>
// declare pins 5 and 6 on arduino for transmission and reception respectively
SoftwareSerial mySerial(5, 6); // RX, TX
// *****************Global variables*******************
//Y-axis value are sent to the microcontroller on pin A2
const int ypin = A2;
//Variable for storing previous avg for Jerk Calculations
double prevAvg = 0;
//Variable for storing previous and present T for signalling a jump to the java program
unsigned long  int prevT, presentT, gameStartingTime;
unsigned long int maxGameTime = 60000;
// for low-pass filter - init to zero
double dataY[3];

byte score; // to maintain score

//Context for states to decide
struct Context {
  char currentState; // w -> wait State, g-> game-ON State, o-> game Over State
  byte message = 0b00000000;
};
Context Acc_Context;

//***********************Setup***********************
void setup() {
  // initialize the serial communications:
  mySerial.begin(9600);
  // initialize all values to the first reading on y pin
  prevAvg = dataY[0] = dataY[1] = dataY[2] = convertToG(analogRead(ypin));
  // initialize to current time
  presentT = prevT = millis();
  Acc_Context.currentState = 'w'; // initially the current state is wait to start
  score = 0;
}

//***********************Loop***********************
void loop() {
  if (mySerial.available()) { //check for the beginning and end of game
    Acc_Context.message = (byte)mySerial.read(); // read the Data sent as byte
  }
  //State Design Pattern
  switch (Acc_Context.currentState) {
    case 'w':
      if (Acc_Context.message == 0b11111111) //if Java sent 1111 1111 - game started so change state to game on
      {
        Acc_Context.message = 0b01000000; //reset msg for correct scoring
        gameStartingTime = millis(); // set starting Game Time
        Acc_Context.currentState = 'g'; // change state to gameON
      }
      break;

    //Send final point one game over.
    case 'g':
      if (millis()-gameStartingTime > maxGameTime) { // if millis (Current Time) is larger than or equal to the time the game started + 60000 msec
        mySerial.write((byte)0b00000000); // broad cast tgame over to all the devices via dispatcher
        delay(100); // wait 10ms to let Java finish broadcast because java will take the message and broad cast it 10 times
        mySerial.write(score); // send final score to Java
        Acc_Context.currentState = 'o'; // change to wait state
      }
      else { // if no state change - do the processing
        dataY[2] = convertToG(analogRead(ypin));
        // [2]store the time when the value was read
        presentT = millis();

        //[3] calculate moving average (low-pass filter) for smoothening the data
        double avgYG = movingAverage(dataY[0], dataY[1], dataY[2]);

        //[4]Calculate the Jerk
        double jerk = convertToJerk(avgYG, prevAvg);

        // [5]if a jump is detected (i.e jerk>=5) and no signal for jump has been transmitted in the last one second then send a signal
        if (jerk >= 5 && (presentT > (prevT + 1000))) {
          // update score and signal to java so that java can intensify the sound
          evaluateJump();
          // store the present time for sending a byte for the next valid jump
          prevT = presentT;
        } // end jump detected

        delay(10);// constant delay between the readings
      }
    case 'o':
      if (Acc_Context.message == 0b00001111) {
        resetScore();
        Acc_Context.currentState = 'w'; // change to wait state
      }
      break;
  }//end switch
}

//******************Functions**********************


//calculate the average of 3 values
inline double movingAverage(double &data0, double &data1, const double &data2) {
  double avg = (data0 + data1 + data2) / 3.0;
  data0 = data1;
  return data1 = avg; // storing the  current average
}

//convert value to G
inline double convertToG(int value) {
  return (0.01 * value) - 6.2;  //  return  G value;
}

//convert value to jerk
double convertToJerk(double currentAvg, double &prevAvg) {
  double jerk = abs(10 * (currentAvg - prevAvg)); //abs
  // storing the current avg as prevAvg to calculate jerk for next current reading
  prevAvg = currentAvg;
  return jerk;
}

//Evaluate jump
void evaluateJump() {
  if (((Acc_Context.message & B01000000) != 0) and ((Acc_Context.message & B00001111) != 0) ) { //ensure msg is for current user device
    mySerial.write(B00000010); //00001111 XY00ZZZZ -> X represent player ID, Y is the Accelerometer id, 0010-> correct jump
    increaseScore(Acc_Context.message );
  }
  else  {// jumped in non critical region - WRONG
    mySerial.write(B00000001); //00001111 XY00ZZZZ -> X represent player ID, Y is the Accelerometer id, 0001->wrong jump
    decreaseScore();
  }
}
void increaseScore(byte message) { // Increase score depending on speed (multiplier)
  byte multiplier = ((message & 0b00001111) - 0b00000111); // Start multiplier from 1 (for lowest speed)
  if (score + multiplier < 64) { //do not cross max score of 63 due to payload size
    score +=  multiplier;
  }
}
void decreaseScore() {
  if (score != 0) {
    score -= 1; // decrease 1 point for missing jump
  }
}

void resetScore() {
  score = 0; // reset score when playing another round
}
