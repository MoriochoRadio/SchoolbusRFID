# SchoolbusRFID — 위치 기반 어린이 하차 태그 애플리케이션

> 어린이 통학버스의 갇힘 사고를 *"RFID 태그로 승하차를 정확히 추적하고 학부모·선생님·운전자가 실시간으로 통학 정보를 공유"* 하는 시스템으로 풀어보려 한 학부 팀 프로젝트입니다. 2024-2 학기 의료IT공학과 *"설계 및 프로젝트 기본 II"* (역설계 최종발표) 에서 4 인 팀으로 진행한 첫 **하드웨어 + 모바일 + 클라우드 3-tier 풀스택** 시도입니다.

![Status](https://img.shields.io/badge/status-preserved-blue) ![Year](https://img.shields.io/badge/year-2024--2-blue) ![Platform](https://img.shields.io/badge/platform-Android%20%2B%20ESP8266-success) ![Stack](https://img.shields.io/badge/Android%20Native-Java-orange) ![Backend](https://img.shields.io/badge/Firebase-Realtime%20DB%20%2B%20Maps-yellow) ![Hardware](https://img.shields.io/badge/RFID-MFRC522-red)

---

## ⚠ 빌드 상태에 관하여

본 레포는 **2024년 9월 시연 시점의 코드를 그대로 보존**합니다. [MedQueue](https://github.com/MoriochoRadio/MedQueue) (Xamarin 지원 종료로 빌드 불가) 와 달리, SchoolbusRFID 는 2024년 신규 스택 (Gradle 8.7, AGP 8.6.0, compileSdk 34, Firebase 21.x) 으로 작성되어 작성 시점에는 빌드 가능했습니다.

다만 향후 빌드 가능성은 다음 이유로 변할 수 있습니다:
- Firebase 의존성 22+ 마이그레이션 권장
- Google Play Services / Android SDK 의 deprecation 정책
- `compileSdk 34` 가 더 이상 최신이 아니게 되는 시점

코드 안의 모든 시크릿 (Firebase Auth Secret, WiFi 자격증명, Google Maps API 키, `google-services.json`) 은 마스킹된 더미로 치환되었으며, 원본 자격증명은 Firebase 콘솔에서 회수·무력화되었습니다. 직접 빌드해보려면 [`src/android/`](src/android/) 의 `google-services.json.example`, `AndroidManifest.xml` 의 Maps 키 자리, `src/firmware/*.ino` 의 WiFi/Firebase 자리를 본인 환경 값으로 채워 넣어야 합니다.

---

## 📚 프로젝트 컨텍스트

| 항목 | 내용 |
|---|---|
| **시기** | 2024학년도 2학기 (발표일: 2024-09-24) |
| **소속** | 건양대학교 의료IT공학과 |
| **과목** | 설계 및 프로젝트 기본 II (역설계 최종발표) |
| **지도교수** | ◯◯◯ · ◯◯◯ 교수님 |
| **팀** | 2조 (4인) — PM ◯◯◯ · 제작 ◯◯◯ · ENG **김태경 (본인)** · ENG ◯◯◯ |
| **본인 (KimTaeKyoung) 역할** | **ENG — Android 앱 백엔드 로직 메인 담당** + 발표 PPT 보조 |
| **본인 메인 담당** | `MainActivity` / `activity_driver_*` / `activity_teacher_*` / `activity_parent_*` / `OperationStartActivity` / `ParentsActivity` 의 백엔드(동작) 로직 |
| **공동 담당** | UI / 레이아웃 XML / `MapActivity` / `ParentsMapActivity` / Firebase 연동 |

---

## ❓ 문제 정의

> *"안전벨·동승보호자 있었는데… 3세 아이 통학버스에 한 시간 갇혀"*
> — 대전일보, 2022.08.11

![Problem](assets/screenshots/problem_news.png)

매년 통학버스 갇힘 사고가 반복됩니다. PPT 슬라이드의 문제 분석을 그대로 옮기면:

![Problem - Why](assets/screenshots/problem_why.png)

1. **다양한 법률을 기반으로 관리하고 있지만 소홀한 체계로 반복적으로 발생**
2. **어른에게도 위험한 갇힘 사고는 체온에 예민한 어린이에게 더욱 위험**
3. **버스 속 산소 부족, 여름철 질식사고, 겨울철 동사사고로 숨지는 경우 발생**

기존의 안전 장치 (안전벨·동승보호자) 가 있어도 사고가 반복되는 이유는 결국 *"승하차 시점에 정확한 카운트가 없다"* 는 데 있다고 보고, 이걸 RFID 태그 카운트로 풀어보려 했습니다.

---

## 💡 솔루션

![Solution](assets/screenshots/solution_overview.png)

> **"학부모, 선생님, 버스운전사가 수시로 통학 정보를 확인할 수 있는 시스템 구현"**

세 역할 모두에게 *"지금 누가 버스에 있는가"* 와 *"버스가 지금 어디 있는가"* 를 실시간으로 보여주는 것이 핵심. 학부모가 *"내 아이가 안전하게 탔는지 / 내렸는지"* 를 직접 확인할 수 있게 됩니다.

---

## 🛠️ 기술 스택

![Tech Stack](assets/architecture/team_and_dev_env.png)

| 영역 | 기술 |
|---|---|
| **모바일** | Android Native (Java) · Gradle 8.7 · AGP 8.6.0 · compileSdk 34 · minSdk 24 |
| **펌웨어** | Arduino C++ · ESP8266 (NodeMCU 핀맵) |
| **RFID 리더** | MFRC522 (Miguel Balboa 라이브러리) |
| **백엔드** | Firebase Realtime DB · Firebase Auth |
| **지도** | Google Maps Android API · Google Play Services Location |
| **개발 환경** | Android Studio · Arduino IDE · Visual Studio Code |

**주요 의존성** (`src/android/app/build.gradle`):
- `firebase-database 21.0.0`, `firebase-auth 21.1.0`
- `play-services-maps 18.0.2`, `play-services-location 21.0.1`
- `MFRC522`, `FirebaseESP8266` (Mobizt), `ArduinoJson` (펌웨어)

---

## 🏗️ 시스템 설계

본 프로젝트의 차별 포인트는 **하드웨어 + 모바일 + 클라우드의 3-tier 풀스택** 입니다. RFID 리더부터 부모 화면까지의 전체 흐름:

```
┌──────────────┐         ┌─────────────────┐         ┌─────────────────────┐
│  RFID 카드   │ ─SPI─▶ │  ESP8266 +      │ ─HTTP─▶│  Firebase           │
│  (학생)      │         │  MFRC522 RFID   │         │  Realtime DB        │
└──────────────┘         │  + WiFi         │         │                     │
                          └─────────────────┘         │  /tag/{uid}/        │
                                                       │   {boarding}        │
┌──────────────┐         ┌─────────────────┐         │  /DriverLocation/   │
│  버스 GPS    │ ───────▶│  Android        │ ─SDK─▶ │   {latitude,        │
│  (실시간)    │         │  MapActivity    │         │    longitude}       │
└──────────────┘         │  (운전자 화면)  │         └──────────┬──────────┘
                          └─────────────────┘                    │
                                                                 │ subscribe
                                          ┌──────────────────────┼────────┐
                                          ▼                      ▼        ▼
                                  ┌──────────────┐    ┌──────────────┐  ┌──────────────┐
                                  │ Operation    │    │ Parents      │  │ ParentsMap   │
                                  │ StartActivity│    │ Activity     │  │ Activity     │
                                  │ (선생님)     │    │ (학부모)     │  │ (학부모)     │
                                  │ 탑승·하차   │    │ 자녀 탑승    │  │ 버스 위치    │
                                  │ 카운트       │    │ 상태         │  │ 지도         │
                                  └──────────────┘    └──────────────┘  └──────────────┘
```

### System Structure
![System Structure](assets/architecture/system_structure.png)

### System Architecture
![System Architecture](assets/architecture/system_architecture.png)

### Flow Chart
![Flow Chart](assets/architecture/flow_chart.png)

---

## ✨ 주요 기능

### 사용 시나리오 (4 step)

![Scenario Overview](assets/screenshots/scenario_overview.png)

1. **운전자가 운행 전, 탑승자의 수를 확인하고 운행 시작을 누른다**
2. **아이가 승차 시 태그를 찍는다** → 운전자와 선생님 화면에서 인원수 카운트 ↑
3. **학부모는 자신의 화면에서 아이의 현재 위치를 확인한다**
4. **아이가 하차 시 태그를 찍고 탑승인원이 0이 되면 선생님이 운행을 종료한다**

### UI — 3 Role 분기

| | |
|---|---|
| **메인 ↔ 로그인 (3 role 진입점)** | **운전자 화면 ↔ 선생님 화면** |
| ![UI 1](assets/screenshots/ui_main_and_login.png) | ![UI 2](assets/screenshots/ui_driver_and_teacher.png) |

### 시연 영상

<video src="assets/videos/software_demo.mp4" poster="assets/videos/software_demo_poster.png" controls width="800"></video>

> 위 영상이 보이지 않으면 [`assets/videos/software_demo.mp4`](assets/videos/software_demo.mp4) 직접 재생 (23 MB, 1:28).

추가 시연: [`software_execution.mov`](assets/videos/software_execution.mov) (3.8 MB, 0:48 — 모바일 세로 화면 녹화)

---

## 👤 본인 기여 (KimTaeKyoung · ENG)

팀 프로젝트이므로 본인이 직접 담당·작성한 부분과 공동 작업한 부분을 명확히 구분합니다.

### 1) Android 백엔드 로직 메인 담당

다음 10 개 .java 파일의 **동작 로직 (이벤트 핸들러 · 상태 전이 · Firebase 호출 · 입력 검증)** 을 본인이 메인으로 담당했습니다:

```
src/android/app/src/main/java/com/example/project01/
├── MainActivity.java              (3-role 분기 진입점)
├── activity_driver_login.java
├── activity_driver_join.java
├── activity_driver_main.java
├── activity_teacher_login.java
├── activity_teacher_join.java
├── activity_parent_login.java
├── activity_parent_join.java
├── OperationStartActivity.java    (선생님 화면 — 탑승/하차 카운트)
└── ParentsActivity.java           (학부모 화면 — 자녀 상태)
```

**공동 담당** (팀과 함께 작업): 모든 layout XML, `MapActivity.java` / `ParentsMapActivity.java` (지도), Firebase 초기 연동 설정.

### 2) 5 스냅샷 동안 단 한 바이트도 바뀌지 않은 코드

본 레포의 `docs/_archive/iterations/` 폴더에는 한 학기 동안의 5 단계 코드 스냅샷이 보존되어 있습니다. 본인이 메인 담당한 파일 중 일부는 **5 스냅샷 모두 비트 동일** — 학기 초 한 번 작성한 후 끝까지 손댈 필요가 없었습니다:

| 파일 | p00 | p01 | p02 | p03 | p04 | 결과 |
|---|---|---|---|---|---|---|
| **MainActivity.java** | 1670 B | = | = | = | = | ★ **5 스냅샷 모두 비트 동일** (`68ce8336`) |
| activity_driver_join.java | 3015 B | = | = | = | = | 5 모두 동일 |
| activity_teacher_join.java | 3027 B | = | = | = | = | 5 모두 동일 |

반면 본인이 점진 성장시킨 파일들:

| 파일 | p00 (초기) | p01 → p04 (성장) |
|---|---|---|
| OperationStartActivity.java | 1477 B (stub) | 3836 → 5685 B |
| ParentsActivity.java | 969 B (stub) | 4844 → 5447 B (5배 성장) |

> 시계열 진화 매트릭스 13 파일 × 5 스냅샷 전체는 [`docs/_archive/iterations/README.md`](docs/_archive/iterations/README.md) 참조.

### 3) 한국어 도메인 주석으로 푼 백엔드 로직

본인이 메인 담당한 파일들에는 한국어 주석이 풍부합니다 (`OperationStartActivity.java` 만 20 개). 도메인 의미를 직접 풀어 쓴 흔적:

```java
// MainActivity.java — 3 role 분기 (5 스냅샷 모두 동일한 코드)
// 부모님
// 선생님
// 운전자

// OperationStartActivity.java:70-91 — Firebase 실시간 동기화
tagRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // 탑승자와 하차자 수 초기화
        boardingCount = 0;
        alightingCount = 0;
        for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
            Boolean boarding = tagSnapshot.child("boarding").getValue(Boolean.class);
            if (boarding != null && boarding) {
                boardingCount++;      // 탑승한 학생
            } else if (boarding != null && !boarding) {
                alightingCount++;     // 하차한 학생
            }
        }
        // UI에 갱신된 값 반영
        boardingCountTextView.setText(String.valueOf(boardingCount));
        alightingCountTextView.setText(String.valueOf(alightingCount));
    }
});
```

```java
// OperationStartActivity.java:104-105 — 도메인 안전 메시지를 다이얼로그에 직접 박음
setTitle("운행 종료")
    .setMessage("탑승자가 남아있습니다.\n다시 한 번 확인해주세요");
```

```java
// ParentsActivity.java — 자녀 정보를 한국어 UI 로 직접 노출
childNameTextView.setText(sname + " 어린이");
boardingStatusTextView.setText(boarding ? "탑승" : "미탑승");
```

---

## 📅 프로젝트 일정

![Gantt Chart](assets/architecture/gantt_chart.png)

학기 일정 간트차트. 한 학기 안에 **하드웨어 (RFID + ESP8266) + 모바일 (Android 3-role) + 클라우드 (Firebase + Maps)** 의 3-tier 시스템을 완성했습니다.

---

## 🎯 기대 효과

![Expected Effects](assets/screenshots/expected_effects.png)

- **안전성**: 학부모가 자녀의 위치 정보를 파악해 안정성을 확보할 수 있다.
- **사고 예방**: 아이의 승하차 및 탑승 현황을 확인해 아이 혼자 남겨지는 사고를 예방할 수 있다.

---

## ✍️ 본인 톤이 살아있는 코드

이 레포의 코드는 *완성된 결과 코드* 가 아니라 *학부 2학년 학생들이 처음 RFID 하드웨어 + Firebase + 멀티 디바이스 앱을 만지며 남긴 흔적* 그대로입니다. 일부러 다듬지 않은 것들:

### 1) 학기 초 첫 라이브러리 이름 타이포

```cpp
// src/firmware/_archive/RFID.ino:12
#include <MFRC552.h>    // ← 정확한 라이브러리는 MFRC522
```

학기 초 첫 RFID 스파이크에서 라이브러리 이름 한 글자를 잘못 적었던 흔적 (`MFRC522` ↔ `MFRC552`). 후속 `.ino` 들에서는 정정되었고, 이 파일은 학습 진정성의 증거로 그대로 보존했습니다.

### 2) `*****설정필요*****` 자리표시자

```cpp
// src/firmware/_archive/Firebase_WIFI.ino:14-17
#define FIREBASE_HOST "*********설정필요*********"
#define FIREBASE_AUTH "*********설정필요*********"
#define WIFI_SSID     "*********설정필요*********"
#define WIFI_PASSWORD "*********설정필요*********"
```

Firebase 통합 가이드를 만들면서 본인이 *"여기 채워야 함"* 을 잊지 않으려고 박아둔 자리표시자. 후속 `.ino` 들이 실제 값을 채워 넣으며 발전했습니다.

### 3) 시연용 더미값 `alightingCount = 6`

```java
// src/android/app/src/main/java/com/example/project01/OperationStartActivity.java:122-124
btnReset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 현재 값 초기화
        boardingCount = 0;
        alightingCount = 6;   // ← 왜 6인지 코드에 설명 없음 (시연용 더미 추정)
        ...
```

리셋 버튼을 누르면 `alightingCount = 6` 으로 초기화되는데 *왜 6인지* 코드에 설명이 없습니다. 발표 시연 시 *"잠시 전 정리 안 한 상태"* 처럼 보이게 하려고 임의로 박았던 더미값으로 추정 — 학부생의 정직한 시연 흔적.

### 4) 만들었지만 한 줄도 안 쓴 빈 파일

```
src/android/app/src/main/java/com/example/project01/activity_join_login.java   (0 bytes)
```

5 스냅샷 모두 동일한 `d41d8cd9` MD5 (= empty file). 학기 초 명명만 결정하고 흐름이 바뀌면서 비어둔 흔적.

### 5) 발표 직전 catch & fix 한 타이포

`docs/_archive/iterations/project03.zip` 안 manifest 에서 발견되는 타이포:

```xml
<!-- project03.zip 안 -->
<activity android:name=".PartentsMapActivity" ... />
                       ^^^^^^^^ ← Partents (sic)

<!-- project04 (= src/android/) 에서 정정됨 -->
<activity android:name=".ParentsMapActivity" ... />
```

발표 직전 누군가 catch & fix 했습니다. 정정한 사람이 본인이었는지 다른 팀원이었는지 명확히 기억나진 않지만 — *시계열 archive 가 정정 자체를 박제해 보여줍니다*. 시계열의 가치는 결과만 보존하는 게 아니라 *결과로 가는 과정* 을 보존하는 데 있습니다.

### 6) 같은 줄을 두 번 적은 manifest

```xml
<!-- src/android/app/src/main/AndroidManifest.xml:18-23 -->
<activity android:name=".ParentsMapActivity" android:exported="false" />
<activity android:name=".ParentsMapActivity" android:exported="false" /> <!-- MapActivity 등록 -->
```

위 5) 의 타이포를 정정했지만 줄 자체를 지우지 못해 같은 activity 가 두 번 등록되어 있습니다. 두 번째 등록의 주석 (`<!-- MapActivity 등록 -->`) 이 *"원래 다른 이름을 넣으려 했던 자리"* 였음을 보여줍니다. 막판 코드 통합의 흔적.

### 7) Firebase 콘솔에서 여러 번 다운로드한 흔적

```
src/android/app/src/google-services (3).json.example
                                  ^^^ ← Firebase 콘솔에서 3번째 다운로드한 사본
```

파일명 자체가 학습 흔적 — Firebase 콘솔에서 `google-services.json` 다운로드 ↔ 드래그를 여러 번 반복하면서 ` (3)` 접미사가 그대로 남은 사본을 src/ 폴더에 끌어다 놓은 자국.

---

## 📂 자료실

| 자료 | 경로 | 비고 |
|---|---|---|
| 🎤 최종 발표 자료 | [`docs/presentation_final_redacted.pptx`](docs/presentation_final_redacted.pptx) | 26 슬라이드 (팀원·교수 이름 마스킹) |
| 🎬 시연 영상 (메인) | [`assets/videos/software_demo.mp4`](assets/videos/software_demo.mp4) | 1:28 / 1920×1080 |
| 🎬 시연 영상 (실행) | [`assets/videos/software_execution.mov`](assets/videos/software_execution.mov) | 0:48 / 세로 화면 녹화 |
| 📦 시계열 코드 스냅샷 | [`docs/_archive/iterations/`](docs/_archive/iterations/) | project00~03 (4 스냅샷) + README |
| 🔌 펌웨어 가이드 | [`src/firmware/README.md`](src/firmware/README.md) | ESP8266 핀맵 + 라이브러리 설치 |

> 📌 본인을 제외한 팀원 3명·교수 2명의 이름은 발표 자료 본문 / 메타에서 `◯◯◯` 으로 마스킹되었습니다. 팀원의 PC 계정명도 `iterations/` 압축본 안에서 제거되었으며, 누가 어느 PC를 썼는지는 `iterations/README.md` 에 `<PC_X>` / *역할 단위*로만 기록되어 있습니다.

---

## 📚 참고 자료 (발표 시 인용)

본 프로젝트가 인용한 자료들:

| # | 출처 |
|---|---|
| [1] | (2021-1) 설계 및 프로젝트 심화 I 6조 *스마트한 승하차 관리 및 원아 일지 시스템* (선행 캡스톤) |
| [2] | 이다온, *안전벨·동승보호자 있었는데… 3세 아이 통학버스에 한 시간 갇혀*, 대전일보, 2022.08.11 |
| [3] | 강민수, *잇따른 통학버스 갇힘사고, 왜 자꾸 반복되나요?*, 아주경제, 2018.07.22 |
| [4] | 송욱, *폭염 속 버스에서 어린아이 또 숨져 — 반복되는 갇힘 사고, 해결책은 없나?*, SBS뉴스, 2018.07.20 |
| [5] | 문형진, *딥러닝 기반의 사용자 인증을 활용한 어린이 버스에서 안전한 승차 및 하차 시스템 설계*, 융합정보논문지, 2020 |
| [6] | 이승재 외 7인, *어린이 운송 차량의 안전사고 방지 방안에 관한 연구*, 한국산학기술학회 추계 학술발표논문집, 2023 |

---

## ✍️ 회고

어린이 통학버스 갇힘 사고라는 실제 안전 문제를, *"RFID 태그로 승하차를 정확히 추적하면 풀 수 있을까"* 라는 가설로 풀어보았습니다.

한 학기 동안 팀 4 명이 5 PC 를 돌아가며 빌드한 흔적이 [`docs/_archive/iterations/`](docs/_archive/iterations/) 의 시계열 archive 에 그대로 남아 있고, 본인 PC 도 학기 중 한 번 교체했습니다 (`neo62` → `Tkkim`). 다른 팀원들도 각자의 PC 에서 같은 코드 베이스를 빌드했고, 그 흔적이 5 단계 스냅샷의 `local.properties` 에 박혀 있었습니다 (현재는 보안 정리되어 본인 외 팀원 PC 는 `<PC_X>` 형태의 매핑 정보로만 보존).

본인은 그 중 **Android 백엔드 로직** — 부모 / 선생님 / 운전자 3 역할의 로그인·메인·운영 시작 화면을 메인 담당했고, UI 와 지도, Firebase 연동은 팀과 함께 만들었습니다. 학기 초에 처음 쓴 `MainActivity.java` 가 5 스냅샷을 거치면서 **단 한 바이트도 바뀌지 않은 것** 이 작은 자부심이지만, 같은 manifest 에서 발표 직전까지 catch 되지 못한 `PartentsMapActivity` 타이포가 project03 안에 남아 있는 것도 같은 학기의 흔적입니다.

[`MedQueue`](https://github.com/MoriochoRadio/MedQueue) (2024-1 학기, *환자 대기 모니터링*) 에서 익힌 Firebase 실시간 동기화 패턴이 본 프로젝트의 *"태그 → 카운트 → 부모 알림"* 흐름의 기반이 되었습니다. 학습 단계에서는 [`study-windows-programming/022_FireBase`](https://github.com/MoriochoRadio/study-windows-programming) 에서 학생 정보 CRUD 만 만져봤던 것이, MedQueue 에서 *"실시간 환자 대기열"* 로 확장되었고, SchoolbusRFID 에서 *"실시간 어린이 안전 모니터링"* 으로 한 번 더 확장된 흐름입니다.

또한 본 프로젝트가 본인이 처음으로 손에 잡아본 **하드웨어 (ESP8266 + MFRC522) + 모바일 (Android 3-role) + 클라우드 (Firebase Realtime DB + Maps) 의 3-tier 풀스택** 이었습니다. 그 경험은 이후 [`ElderCaringApp`](https://github.com/MoriochoRadio/ElderCaringApp) 의 *"노인 케어 모니터링"* 으로 케어 도메인이 확장되었고, [`seed-project`](https://github.com/MoriochoRadio/seed-project) 캡스톤의 팀 협업 기초가 되었습니다.

---

## 🗂️ 관련 레포

### 본 프로젝트가 가능했던 기반 학습
- [`study-java`](https://github.com/MoriochoRadio/study-java) — Android Java 의 OOP / 이벤트 핸들러 / 익명 클래스
- [`study-windows-programming`](https://github.com/MoriochoRadio/study-windows-programming) — Firebase Realtime DB 첫 학습 (`022_FireBase` 챕터)

### 같은 헬스케어·케어 도메인의 형제 프로젝트
- [`MedQueue`](https://github.com/MoriochoRadio/MedQueue) — *환자 대기 모니터링* (Xamarin.Forms · 2024-1)
- [`ElderCaringApp`](https://github.com/MoriochoRadio/ElderCaringApp) — *노인 케어 모니터링*

### 그 이후로 이어진 의료 + AI 프로젝트
- [`AI_Lung_1`](https://github.com/MoriochoRadio/AI_Lung_1) — 폐 의료 영상 AI
- [`sperm-ai`](https://github.com/MoriochoRadio/sperm-ai) — AI 정자 운동성 분석
- [`seed-project`](https://github.com/MoriochoRadio/seed-project) — 캡스톤 (현재 진행 중)

---

## License

[`LICENSE.md`](LICENSE.md) 참조. 본 레포는 학부 팀 프로젝트의 학습 결과물을 포트폴리오 목적으로 공개한 것이며, 상업적 이용은 팀 합의가 필요합니다.

---

*Author: [MoriochoRadio](https://github.com/MoriochoRadio) (KimTaeKyoung) · 건양대학교 의료IT공학과*
