# relay-test-endpoint

## 1. 빌드 정보

| 항목 | 내용 |
|------|------|
| JDK | 1.8 |
| Framework | Spring Boot 2.5.4 |
| Build Tool | Gradle 6.9.4 |
| Port | 18091 |
| Artifact | `relay-test-endpoint-1.0.0.jar` |

### 빌드 방법

프로젝트 루트에서 실행:

```bash
./gradlew clean bootJar
```

빌드 결과물 경로: `build/libs/relay-test-endpoint-1.0.0.jar`

### 서버 배포

빌드된 jar 파일을 서버의 `scripts/` 디렉토리에 복사:

```bash
scp build/libs/relay-test-endpoint-1.0.0.jar user@server:/path/to/scripts/
```

## 2. 실행 방법

`scripts/` 디렉토리에 jar 파일과 쉘 스크립트를 함께 배치한다.

```
scripts/
├── relay-test-endpoint-1.0.0.jar
├── start.sh
├── stop.sh
├── backup.sh
└── backup/          # backup.sh 실행 시 자동 생성
```

### 스크립트 실행 권한 부여

```bash
chmod +x start.sh stop.sh backup.sh
```

### 애플리케이션 시작

```bash
./start.sh
```

- `nohup`으로 백그라운드 실행
- 로그 파일: `scripts/app.log`

### 애플리케이션 중지

```bash
./stop.sh
```

- Graceful shutdown 시도 후, 10초 내 종료되지 않으면 강제 종료

### jar 파일 백업

```bash
./backup.sh
```

- `backup/` 디렉토리에 타임스탬프가 붙은 파일명으로 복사
- 예: `relay-test-endpoint-1.0.0_20260206143000.jar`

### 배포 순서 (권장)

```bash
./stop.sh          # 1. 기존 애플리케이션 중지
./backup.sh        # 2. 기존 jar 백업
# jar 파일 교체      # 3. 새 jar 파일 복사
./start.sh         # 4. 애플리케이션 시작
```

## 3. API 추가 방법

### 3-1. Controller 생성

`src/main/java/kr/co/hectofinancial/mps/api/` 경로에 Controller 클래스를 추가한다.

```java
package kr.co.hectofinancial.mps.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SampleController {

    @PostMapping("/test/sample")
    public String sample(@RequestBody Map<String, Object> body) {
        log.debug("request body: {}", body);
        // 비즈니스 로직 구현
        return "OK";
    }
}
```

### 3-2. 암호화/해시 검증이 필요한 경우

`CryptoUtil`과 `HashValidator`를 활용한다.

```java
@Slf4j
@RestController
public class SampleWithHashController {

    @Value("${encryption.key}")
    private String encryptionKey;

    @Value("${encryption.hash-key}")
    private String hashKey;

    @PostMapping("/test/sample/secure")
    public String secure(@RequestBody Map<String, Object> body) {
        // 복호화
        String decrypted = CryptoUtil.decrypt((String) body.get("encryptedData"), encryptionKey);

        // 해시 검증
        String hash = CryptoUtil.hmacSHA256(hashKey, dataToValidate);

        log.debug("decrypted: {}", decrypted);
        return "OK";
    }
}
```

### 3-3. 빌드 및 배포

```bash
# 로컬에서 빌드
./gradlew clean bootJar

# 서버에 배포
scp build/libs/relay-test-endpoint-1.0.0.jar user@server:/path/to/scripts/
```
