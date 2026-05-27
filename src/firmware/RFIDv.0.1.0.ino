// ============================================================
// ★ 보안 안내 (Phase C 정리 시 추가):
//   본 .ino 의 WiFi/Firebase 자격증명은 학습용 더미입니다.
//   실제 사용 시 secrets.h 또는 환경 변수로 분리하세요.
//   원본 시크릿은 GitHub push 전 마스킹되었으며,
//   Firebase 콘솔에서 무효화 완료되었습니다.
// ============================================================

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SPI.h>
#include <MFRC522.h>
#define RST_PIN D3
#define SS_PIN D4
const char* ssid = "<YOUR_WIFI_SSID>"; // 와이파이 이름
const char* password = "<YOUR_WIFI_PASSWORD>"; // 와이파이 비밀번호
WiFiServer server(80);
WiFiClient client;

// String myRFID[] = {};

MFRC522 mfrc(SS_PIN, RST_PIN);

void setup() {
  Serial.begin(115200);
  Wifi_connecct();
  SPI.begin();
  mfrc.PCD_Init();
}

void loop() {
  if(WiFi.status() != WL_CONNECTED){ //연결이 이상할시에
    Serial.println("try reconnect");
    Wifi_connecct();
  }

    delay(5000);

    if ( !mfrc.PICC_IsNewCardPresent() || !mfrc.PICC_ReadCardSerial() ){
    delay(500);
    return;
  }
  String urRFID = String(mfrc.uid.uidByte[0])+" "+String(mfrc.uid.uidByte[1])+" "+String(mfrc.uid.uidByte[2])+" "+String(mfrc.uid.uidByte[3]);
  Serial.print("your RFID : ");
  Serial.println(urRFID);
}

void Wifi_connecct(){
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("test");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("hello");
  Serial.println("Connecting to");
  Serial.println(ssid);
  Serial.print("IP address : ");
  Serial.println(WiFi.localIP());
  server.begin();
  Serial.println("Server started");
}