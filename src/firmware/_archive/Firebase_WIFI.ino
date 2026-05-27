// ============================================================
// ★ 보안 안내 (Phase C 정리 시 추가):
//   본 .ino 의 WiFi/Firebase 자격증명은 학습용 더미입니다.
//   실제 사용 시 secrets.h 또는 환경 변수로 분리하세요.
//   원본 시크릿은 GitHub push 전 마스킹되었으며,
//   Firebase 콘솔에서 무효화 완료되었습니다.
// ============================================================

#include <ESP8266WiFi.h> 
#include <SoftwareSerial.h> 
#include <FirebaseArduino.h> 
#include <ArduinoJson.h> 
#include <ESP8266HTTPClient.h> 
#define FIREBASE_HOST "*********설정필요*********"
#define FIREBASE_AUTH "*********설정필요*********" 
#define WIFI_SSID "*********설정필요*********"  // ssid (wifi 이름) 제공 
#define WIFI_PASSWORD "*********설정필요*********"   // wifi 비밀번호(없다면 “”사이에 작성하지x)

void setup() {
  Serial.begin(9600);
  Wifi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("연결 성공");
  while (WiFi.status () != WL_CONNECTED){
    Serial.print(".");
    delay(500);
  }
  Serial.println (); 
  Serial.print ( "연결됨 :"); 
  Serial.println (WiFi.localIP ()); 
  Firebase.begin (FIREBASE_HOST, FIREBASE_AUTH);
  if (Firebase.failed ()){
    Serial.print(Firebase.error ());
  }
  else {
    Serial.print ( "Firebase 연결됨"); 
    Firebase.setString ("Senura / Value","1"); 
  }
}

void loop() {
  for (int i = 0; i <= 100; i ++) { 
    Firebase.setInt ("Senura / Value", i); 
    delay (1000); 
  }
}