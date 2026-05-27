# Firmware — ESP8266 + RFID MFRC522

ESP8266 (NodeMCU) + MFRC522 RFID 리더 + Firebase Realtime DB 로 구성된 펌웨어.

## 핀맵 (NodeMCU 기준)

| MFRC522 | NodeMCU |
|---|---|
| SDA | D4 |
| SCK | D5 |
| MOSI | D7 |
| MISO | D6 |
| RST | D3 |
| GND | GND |
| 3.3V | 3.3V |

`_archive/RFID.ino` 의 주석 (`P.28 SDA-D4 ...`) 에 남겨진 초기 핀맵 메모를 그대로 보존했습니다.

## 라이브러리 (Arduino Library Manager)

- `MFRC522` (Miguel Balboa)
- `FirebaseESP8266` (Mobizt) — 최신 v.0.3.0 에서 사용
- `FirebaseArduino` — v.0.2.0 에서 사용 (이후 ESP8266 호환 라이브러리로 교체)
- `ArduinoJson`
- `TinyGPS` (Mikal Hart) — `GPSv0.1.0.ino` 에서만 사용
- `SPI`, `SoftwareSerial`, `ESP8266WiFi`, `ESP8266HTTPClient` — 보드 표준

## 파일 진화 흐름

### `/` (최종)
| 파일 | 단계 |
|---|---|
| `RFIDv.0.1.0.ino` | WiFi + RFID 스캔만 |
| `RFID.v.0.2.0.ino` | Firebase 통합 (FirebaseArduino), 더미 GPS 좌표 5 개 |
| `RFIDv.0.3.0.ino` | ★ 최종 — FirebaseESP8266 로 교체, 승하차 토글 + `tag/{uid}/boarding` 업데이트 |
| `GPSv0.1.0.ino` | TinyGPS 라이브러리 예제 그대로 (GPS 기능은 Android 의 FusedLocation 으로 이전됨) |

### `_archive/` (학습 흔적)
| 파일 | 의미 |
|---|---|
| `RFID.ino` | 최초 RFID 스파이크. ★ `#include <MFRC552.h>` 타이포 살아있음 (정확한 라이브러리는 `MFRC522`) |
| `Firebase_WIFI.ino` | ★ `#define FIREBASE_HOST "*********설정필요*********"` 같은 자리표시자 — 학습 가이드 단계 |
| `Connecting.ino` | WiFi 연결 최소 예제 (초기에는 학교 공용 WiFi 환경에서 작동했음 — SSID/PW 모두 마스킹됨) |

## 시크릿 처리

모든 .ino 파일 상단에 보안 안내 주석이 추가되어 있습니다. 실제 빌드 시:
- `<YOUR_WIFI_SSID>`, `<YOUR_WIFI_PASSWORD>` → 본인 환경 값
- `<FIREBASE_HOST_URL>`, `<FIREBASE_AUTH_SECRET>` → 본인 Firebase 콘솔에서 발급

`secrets.h` 별도 파일 또는 환경 변수로 분리하는 것을 권장합니다.
