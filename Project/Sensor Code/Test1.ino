/*
********************************************
14CORE ULTRASONIC DISTANCE SENSOR CODE TEST
********************************************
*/
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <string>
#include <String>

#define TRIGGER 5
#define ECHO    4
#define led 14
// NodeMCU Pin D1 > TRIGGER | Pin D2 > ECHO

const char ssid[] = "AndroidAP";
const char password[] = "password";

long change=0;
const char* host = "project.coen396chetan.info";

void setup() {
  
  Serial.begin (115200);
  pinMode(TRIGGER, OUTPUT);
  pinMode(ECHO, INPUT);
   
  Serial.println("HTTP server started");
  Serial.print("Connecting to ");
  Serial.println(ssid);
      
  WiFi.begin(ssid, password);
  
}

void loop() {
  
  long duration, distance;
  digitalWrite(TRIGGER, LOW);  // Added this line
  delayMicroseconds(2); // Added this line
  digitalWrite(TRIGGER, HIGH);

  delayMicroseconds(10); // Added this line
  digitalWrite(TRIGGER, LOW);
  duration = pulseIn(ECHO, HIGH);
  distance = (duration/2) / 29.1;
    digitalWrite(led,HIGH); // When the Red condition is met, the Green LED should turn off
    Serial.print(distance);
    Serial.println(" cm");
    
    if(distance!=change){
      WiFiClient client;
      const int httpPort = 80;
      while(!client.connect(host, 80)){
        //Serial.println("CONNECTION FAILED");
      }
      Serial.println(distance+" cm");
      
      String url = "/index.php?dist="+distance;
      
      // This will send the request to the server
      /*client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                   "Host: " + host + "\r\n" + 
                   "Connection: close\r\n\r\n");*/

//       String temp = String(distance);
//       String result = "GET /index.php?dist=" + distance + " HTTP/1.1";
//       client.println(result);

      client.print("GET /index.php?dist=");
      client.print(distance);      
      client.print(" HTTP/1.1");
      client.println("");

//      client.println("GET /index.php?dist=99 HTTP/1.1");
      client.print("Host: ");
      client.println(host);
      client.println("Connection: close");
      client.println();
              
      while(client.available()){
        String line = client.readStringUntil('\r');
        Serial.print(line);
      }
      change=distance;
      client.stop();
    }
    delay(1000);
   
}
