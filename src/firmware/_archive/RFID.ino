/* 
P.28
SDA-D4 
SCK-D5
MOSI-D7
MISO-D6
GND-GND
RST-D3
3.3V-3.3V
*/
#include <SPI.h>
#include <MFRC552.h>
#define RST_PIN D3
#define SS_PIN D4

MFRC522 mfrc(SS_PIN, RST_PIN);

void setup() {
  Serial.begin(9600);
  SPI.begin();
  mfrc.PCD_Init();
}

void loop() {
  if ( !mfrc.PICC_IsNewCardPresent() || !mfrc.PICC_ReadCardSerial() ){
    delay(500);
    return;
  }
  String urRFID = String(mfrc.uid.uidByte[0])+" "+String(mfrc.uid.uidByte[1])+" "+String(mfrc.uid.uidByte[2])+" "+String(mfrc.uid.uidByte[3]);
  Serial.print("your RFID : ");
  Serial.println(urRFID);
}
