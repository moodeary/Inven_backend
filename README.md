# Backend Template

Spring Boot 기반의 개인 인벤토리 관리 시스템 백엔드 템플릿입니다.

## 기술 스택

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Security (JWT 인증)**
- **Spring Data JPA**
- **MariaDB**
- **Gradle**
- **Docker & Docker Compose**

## 주요 기능

### 사용자 관리
- 회원가입/로그인
- JWT 기반 인증
- 사용자 프로필 조회

### 인벤토리 관리
- 개인 아이템 CRUD
- 카테고리별 아이템 조회
- 아이템 검색 및 필터링
- 보유 아이템 개수 조회

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/basic/template/backend_template/
│   │   ├── common/
│   │   │   ├── config/         # 설정 파일들
│   │   │   ├── dto/            # 공통 DTO
│   │   │   ├── exception/      # 예외 처리
│   │   │   ├── security/       # 보안 관련
│   │   │   └── util/           # 유틸리티
│   │   └── domain/
│   │       ├── user/           # 사용자 도메인
│   │       └── inventory/      # 인벤토리 도메인
│   └── resources/
│       └── application*.yml    # 설정 파일
└── test/                       # 테스트 코드
```

## API 응답 형식

모든 API는 일관된 응답 형식을 사용합니다:

```json
{
  "success": true,
  "message": "성공 메시지",
  "data": { ... },
  "errorCode": null
}
```

## 시작하기

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd backend-template
```

### 2. 환경 설정
```bash
# application-dev.yml 파일 생성 및 DB 설정
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml
```

### 3. 실행 방법

#### Docker Compose 사용 (권장)
```bash
docker-compose up -d
```

#### Gradle 직접 실행
```bash
./gradlew bootRun
```

### 4. API 테스트
- Health Check: `GET http://localhost:8080/actuator/health`
- API 문서: `GET http://localhost:8080/swagger-ui.html` (구현 예정)

## 개발 가이드

### 테스트 실행
```bash
./gradlew test
```

### 빌드
```bash
./gradlew build
```

### Docker 이미지 빌드
```bash
docker build -t backend-template .
```

## 환경별 설정

- `application.yml`: 공통 설정
- `application-dev.yml`: 개발 환경
- `application-docker.yml`: Docker 환경
- `application-prod.yml`: 운영 환경 (구현 예정)

## 보안

- JWT 토큰 기반 인증
- CORS 설정
- 입력값 검증
- SQL Injection 방지 (JPA 사용)

## 모니터링

Spring Boot Actuator를 통한 헬스체크 및 메트릭 제공:
- `/actuator/health`: 애플리케이션 상태
- `/actuator/info`: 애플리케이션 정보
- `/actuator/metrics`: 메트릭 정보

## 로깅

- 콘솔 출력 및 파일 저장
- 로그 레벨별 관리
- SQL 쿼리 로깅 지원

## 라이선스

MIT License
