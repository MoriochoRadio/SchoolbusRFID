# Iterations — 한 학기 코드 진화 흔적

본 폴더의 4개 zip 은 SchoolbusRFID 프로젝트의 한 학기 동안의 Android 코드 스냅샷입니다.
**최신 스냅샷 (project04) 은 풀려서 본체 `src/android/` 가 되었습니다.**

## 시계열

| 스냅샷 | 당시 빌드 PC 계정명 | 빌드자 | 주요 변화 / 흔적 |
|---|---|---|---|
| `project00.zip` (≈ 2024-09-12) | `<PC_A>` | 팀원 ◯◯◯ | 초기 stub Java. ★ `.Parents` 라는 미사용 activity 가 manifest 에 등록됨 — 후속 스냅샷에서 `.ParentsActivity` 로 정정 |
| `project01.zip` (2024-09-12) | `<PC_A>` | 팀원 ◯◯◯ | activity 구조 정착, `MainActivity` 백엔드 로직 1차 작성 (이후 5 스냅샷 모두 비트 동일 — 본인 메인 담당) |
| `project02.zip` (2024-09-19) | `neo62` | 김태경 (본인, 메인 PC) | Gradle 8.0.1 → 8.6.0, AGP 동기 업그레이드 |
| `project03.zip` (2024-09-20) | `Tkkim` | 김태경 (본인, 다른 PC) | Google Maps Android API 통합, ★ `PartentsMapActivity` 라는 새 타이포 등장 (정확한 이름은 `ParentsMapActivity`) |
| ─ project04 → `src/android/` ─ | `<PC_B>` | 팀원 ◯◯◯ (PM) | `PartentsMapActivity` 타이포 정정, 최종 발표 시점 사본 |

한 학기에 팀 4 명이 5 PC 를 돌아가며 빌드했습니다. 본인(김태경)은 학기 중에 PC 를 한 번 교체 (`neo62` → `Tkkim`). 본인 외 팀원의 PC 계정명·이름은 `<PC_X>` / `◯◯◯` 으로 마스킹되었습니다.

## ⚠ 보안 정리됨

본 zip 4 개는 정리 작업 시점 (2026-05) 에 다음이 모두 마스킹·제거된 사본입니다:
- Firebase API 키 → `<YOUR_FIREBASE_API_KEY>`
- Firebase 프로젝트 ID·번호·앱 ID → 더미
- Google Maps Android API 키 → `<GOOGLE_MAPS_API_KEY>`
- `local.properties` (Windows PC 계정명 포함) → 삭제
- `.gradle/`, `.idea/`, `.git/`, `app/build/` 캐시 → 삭제 (시크릿이 캐시에 누적되어 있었음)

원본 시크릿은 사용자 측에서 Firebase 콘솔에서 모두 무효화되었습니다.

따라서 원본 압축 시점의 timestamp / 파일 구성과 다를 수 있습니다. 본 zip 들은 **"보안 정리된 시계열 archive"** 입니다.

## 본인 톤 자산 — 의도적으로 보존된 흔적

| zip | 위치 | 흔적 |
|---|---|---|
| project00.zip | `app/src/main/AndroidManifest.xml` | `android:name=".Parents"` — 사용 안 된 activity 등록 흔적 |
| project03.zip | `app/src/main/AndroidManifest.xml` | `android:name=".PartentsMapActivity"` — project04 에서 catch & fix 된 타이포 |

이 두 흔적은 **"코드 진화 중 시도 → 정정"** 의 솔직한 학습 과정을 보여주는 자산으로 의도적으로 보존했습니다.

## 펼치는 방법

```bash
# 특정 시점 코드 확인
unzip -d /tmp/project01 project01.zip
ls /tmp/project01/project01/app/src/main/java/com/example/project01/
```
