
## 프로젝트 정보
- 프로젝트명: backend-template
- 생성일: 2025-09-13
- 타입: 백엔드 템플릿 프로젝트

## 개발 규칙 및 가이드라인

### 파일 생성 규칙
- 필요한 경우에만 새 파일 생성
- 기존 파일 수정을 우선시
- 문서 파일(*.md)은 명시적 요청 시에만 생성

### 코드 스타일
- 주석 추가 금지 (요청 시에만)
- 기존 코드 컨벤션 준수
- 보안 모범 사례 준수

## 작업 히스토리
- 2025-09-13: CLAUDE.md 파일 생성
- 2025-09-13: 프로젝트 기본 구조 설정 완료
  - BaseEntity 생성 (생성일, 수정일 자동 관리)
  - JPA Auditing 활성화
  - 도메인별 폴더 구조 설정 (user, inventory)
  - WebConfig CORS 설정
  - SecurityConfig JWT 기반 인증 설정
  - User, InventoryItem 엔티티 설계
- 2025-09-13: 전체 백엔드 템플릿 구현 완료
  - JWT 인증 시스템 (JwtUtil, SecurityConfig)
  - User 도메인 완성 (Repository, Service Interface/Impl, Controller, DTOs)
  - Inventory 도메인 완성 (Repository, Service Interface/Impl, Controller, DTOs)
  - 인터페이스 기반 Service 구조 적용

## 프로젝트 요구사항

### 개인 인벤토리 관리 시스템
게임 인벤토리와 같은 개념으로 각 사용자마다 개인 인벤토리를 보유하며, 다음 기능을 제공:

#### 핵심 기능
- 사용자별 개인 인벤토리 관리
- 물건(아이템) 저장 및 관리
- 아이템 정보 입력 및 수정
- 보유 아이템 상태 확인
- 아이템 정보 조회 및 검색

#### 예상 데이터 구조
- 사용자 (User): ID, 이름, 이메일 등
- 아이템 (Item): 이름, 설명, 카테고리, 상태, 수량, 등록일 등
- 인벤토리 (Inventory): 사용자-아이템 관계 관리

## 향후 요청사항
(추가 요청사항들이 기록될 예정)