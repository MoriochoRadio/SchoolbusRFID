// ============================================================
// ★ 보안 안내 (Phase C 정리 시 추가):
//   본 .ino 의 WiFi/Firebase 자격증명은 학습용 더미입니다.
//   실제 사용 시 secrets.h 또는 환경 변수로 분리하세요.
//   원본 시크릿은 GitHub push 전 마스킹되었으며,
//   Firebase 콘솔에서 무효화 완료되었습니다.
// ============================================================

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
const char* ssid = "<YOUR_WIFI_SSID>";
const char* password = "<YOUR_WIFI_PASSWORD>";
WiFiServer server(80);
WiFiClient client;

void setup() {
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
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

void loop() {
  // put your main code here, to run repeatedly:
}
