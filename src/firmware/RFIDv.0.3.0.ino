// ============================================================
// ★ 보안 안내 (Phase C 정리 시 추가):
//   본 .ino 의 WiFi/Firebase 자격증명은 학습용 더미입니다.
//   실제 사용 시 secrets.h 또는 환경 변수로 분리하세요.
//   원본 시크릿은 GitHub push 전 마스킹되었으며,
//   Firebase 콘솔에서 무효화 완료되었습니다.
// ============================================================

#define SD_DONT_USE_EXTRAS

#include <SPI.h>
#include <MFRC522.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

#define SS_PIN 2
#define RST_PIN 0

MFRC522 rfid(SS_PIN, RST_PIN);

struct TagStatus {
  String uid;
  bool isBoarding;
};

const int MAX_TAGS = 5;
TagStatus tags[MAX_TAGS];
int tagCount = 0;
const char* ssid = "<YOUR_WIFI_SSID>";
const char* password = "<YOUR_WIFI_PASSWORD>";
const char* firebaseHost = "<FIREBASE_HOST_URL>";
const char* firebaseAuth = "<FIREBASE_AUTH_SECRET>";

FirebaseData firebaseData;

void setup() {
  Serial.begin(115200);
  SPI.begin();
  rfid.PCD_Init();
  connectToWiFi();
  connectToFirebase();
  Serial.println("Scan a RFID tag");
}

void loop() {
  if (rfid.PICC_IsNewCardPresent() && rfid.PICC_ReadCardSerial()) {
    String tagUID = "";
    for (byte i = 0; i < rfid.uid.size; i++) {
      tagUID += String(rfid.uid.uidByte[i], HEX);
    }
    Serial.print("Tag UID: ");
    Serial.println(tagUID);

    bool isRegistered = false;
    int tagIndex = -1;
    for (int i = 0; i < tagCount; i++) {
      if (tags[i].uid == tagUID) {
        isRegistered = true;
        tagIndex = i;
        break;
      }
    }

    if (!isRegistered) {
      if (tagCount < MAX_TAGS) {
        tags[tagCount].uid = tagUID;
        tags[tagCount].isBoarding = true;
        tagCount++;
      } else {
        Serial.println("태그 개수 초과");
      }
    } else {
      tags[tagIndex].isBoarding = !tags[tagIndex].isBoarding;
    }

    updateFirebaseData();

    for (int i = 0; i < tagCount; i++) {
      Serial.print("태그 UID: ");
      Serial.print(tags[i].uid);
      Serial.print(", 승차 여부: ");
      if (tags[i].isBoarding) {
        Serial.println("승차");
      } else {
        Serial.println("하차");
      }
    }

    rfid.PICC_HaltA();
  }
}

//와이파이 연결
void connectToWiFi() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("~~~~~Connected to WiFi~~~~~");
}

// 파이어베이스 연결
void connectToFirebase() {
  Firebase.begin(firebaseHost, firebaseAuth);
  Firebase.reconnectWiFi(true);
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
  Serial.println("~~~~~Connected to Firebase~~~~~");
}

// 태그 값에 따라 firebase update
void updateFirebaseData() {
  for (int i = 0; i < tagCount; i++) {
    String path = "/tag/" + tags[i].uid;
    Firebase.setString(firebaseData, path + "/uid", tags[i].uid);
    Firebase.setBool(firebaseData, path + "/boarding", tags[i].isBoarding);
    if (firebaseData.dataAvailable()) {
      Serial.println("Firebase data updated");
    } else {
      Serial.println("Firebase data update failed");
    }
    delay(1000);
  }
}
