#include <FastLED.h>
#include <WiFi101.h>
#include <SPI.h>
#define DATA_PIN 6
#define NUM_LEDS 80
CRGB leds[NUM_LEDS];

char ssid[] = "pedro";
char pass[] = "jojojojo";
int keyIndex = 0;            // your network key Index number (needed only for WEP)
int status = WL_IDLE_STATUS;
int inputPin = 7;
int buttonVal = 0;
WiFiServer server(23);
boolean alreadyConnected = false; // whether or not the client was connected previously
boolean conn = false;
boolean routine = false;
boolean hit = false; // THIS IS Temp. 
char currentValue =  '0';

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600); 
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present"); 
    // don't continue:
    while(true);
  } 
  
  // attempt to connect to Wifi network:
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:    
    status = WiFi.begin(ssid, pass);
    // wait 10 seconds for connection:
    delay(10000);
  } 
  // start the server:
  server.begin();
  // you're connected now, so print out the status:
  printWifiStatus();
  // pinMode(pin, mode): Configures the specified pin to behave as an input or an output
  FastLED.addLeds<NEOPIXEL, DATA_PIN>(leds, NUM_LEDS);
  
  // set initial values
  ledsInitial();
  
  //Initializes button
  
}

void loop() {
 
  // wait for a new client:
  WiFiClient client = server.available();
  // when the client sends the first byte, say hello:
  if (client) {
    if (!alreadyConnected) {
      // clead out the input buffer:
      client.flush();    
      Serial.println("We have a new client");
      alreadyConnected = true;
    } 
    if (client.available() > 0) {
      // read the bytes incoming from the client:
      char value = client.read();
      currentValue = value;
      Serial.println(value);
      // echo the bytes back to the client:
      if (value == '0') {
        Serial.println("changed to green");
        ledsGreen();
      } else if (value == '1') {
        Serial.println("changed to red");
        ledsRed();
      } else if (value == '4') {
        Serial.println("game finish");
        ledsEnd();
      } else if (value == '3') {
        ledsOff();
      } else if (value == '5') {
        ledsInitial();
      } else if (value == '6') {
        setLed(CRGB(254,254,254));
      }
//      server.write(thisChar);
//      // echo the bytes to the server as well:
//      Serial.write(thisChar);
    }
     //read the input on analog pin 0
     /*
        if (analogRead(A0) > 0) {
              int sensorValue = analogRead(A0);
              if (sensorValue >= 1023 && !hit) {
                hit = true;
                client.println("HIT");
                Serial.println(sensorValue);
              } else if (sensorValue != 1023){
                hit = false;
              }
              Serial.println(sensorValue);
      }
      */
      
      buttonVal = analogRead(A0);
      if (buttonVal > 1000) {
        Serial.println(buttonVal);
        Serial.println(currentValue);
        client.println("HIT");
        delay(300);
      }
  }
}
void ledsGreen() {
  setLed(CRGB::Green);
}
void ledsRed() {
  setLed(CRGB::Red);
}
void ledsInitial() {
  setLed(CRGB(0,0,254));
}
void ledsOff() {
  setLed(CRGB(0,0,0));
}
void ledsEnd() {
  ledsInitial();
  delay(500);
  ledsOff();
  delay(500);
  ledsInitial();
  delay(500);
  ledsOff();
  delay(500);
  ledsInitial();
  delay(500);
  ledsOff();
  delay(500);
  ledsInitial();
}
void setLed(CRGB color) {
    fill_solid(leds, NUM_LEDS, color);
    FastLED.show();  
}
void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

