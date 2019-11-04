
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
unsigned long  int prevT, presentT;
// for low-pass filter - init to zero
double dataY[3];
//check to see if game has been started


struct Context{
  char currentState;
  bool gameStarted ;
  byte message;
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
  Acc_Context.currentState='w'; // initially the current state is wait to start
  Acc_Context.gameStarted= false;
  
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

//keep running forever
void loop() {

  if (mySerial.available()) { //check for the beginning and end of game
     Acc_Context.message = (byte)mySerial.read(); // read the Data sent as byte - can read from other end node
    }
  
  switch(Acc_Context.currentState){
    case 'w':
    if(Acc_Context.message == 0b11111111) //if Java sent 1111 1111 - game started so send accelerometer data
    {  Acc_Context.currentState ='g';
    
    }
    break;
    
  //Send data to Java once Game started, otherwise keep checking
  case 'g':
  if (Acc_Context.message == 0b00000000) { //if Java sent 0 - game ended so don't send any data
   Acc_Context.currentState ='w';
  }
  else {
    dataY[2] = convertToG(analogRead(ypin));

    // [2]store the time when the value was read
    presentT = millis();

    //[3] calculate moving average (low-pass filter) for smoothening the data
    double avgYG = movingAverage(dataY[0], dataY[1], dataY[2]);

    //[4]Calculate the Jerk
    double jerk = convertToJerk(avgYG, prevAvg);

    // [5]if a jump is detected (i.e jerk>=5) and no signal for jump has been transmitted in the last one second then send a signal
    if (jerk >= 5 && (presentT > (prevT + 1000))) {

      //send a byte wirelessly using mySerial
      mySerial.write(B00001111); //00001111 XY00ZZZZ -> X represent player ID, Y is the Accelerometer id, 1111->successful jump

      // store the present time for sending a byte for the next valid jump
      prevT = presentT;
    }
    //delay between the readings
    delay(100);
  }
  
  }
}
1111); //00001111 XY00ZZZZ -> X represent player ID, Y is the Accelerometer id, 1111->successful jump

      // store the present time for sending a byte for the next valid jump
      prevT = presentT;
    }
    //delay between the readings
    delay(100);
  }
  
  }
}
