# 서하대학교 🏫
![로고_1 (1)](https://github.com/jylee102/seoha_univ/assets/101354244/8f47f149-3409-47ae-9568-cc8e0e0318ee)

### 도메인 주소
https://seohauni.kro.kr/

### 시연영상(YouTube)
주소입력란입니다.

## 목차
- [소개](#소개)
- [프로젝트 정보](#프로젝트-정보)
- [ERD & Structure](#erd--structure)
- [화면구성](#화면구성)
- [사용기술](#사용기술)

## 소개
서하대학교 프로젝트는 대학교의 다양한 관리 업무를 웹 애플리케이션으로 통합하여 제공하는 시스템입니다. 학생 정보 관리, 수업 시간표 관리, 학업 성적 추적 등 다양한 기능을 통해 대학 행정 업무의 효율성을 높입니다.

## 프로젝트 정보
#### 제작기간
> 2024년 5월 21일 ~ 2024년 6월 18일 
#### 참여인원
| 이름   |      역할     |  담당 기능     |
|:--------:|:-----------------:|------------|
| [이지영](https://github.com/jylee102) | **팀장** <br> **[Back / Front]** | - 사용자 생성 <br> - 비밀번호 변경 <br> - 교수 강의계획서 CRUD <br> - 교직원 강의실 배정 및 강의 개설 <br> - 메시지 <br> - 강의계획서 PDF 기능 <br> - 사용자, 강의계획서 생성시 엑셀 기능 <br> - CSS 업무 |
| [박정민](https://github.com/parkjeongmin1)  | **팀원** <br> **[Back / Front]** | - 학생, 교수, 교직원 마이페이지 <br> - 학생 휴학신청 기능 <br> - 교직원 휴학신청 처리 기능 <br> - 공지사항 CRUD <br> - 학사일정 CRUD <br> - 로고 제작 및 CSS 업무 | 
| [이승민](https://github.com/sm9940)  | **팀원** <br> **[Back / Front]** | - 학생 성적 입력 <br> - 학생 출결 정보 등록 <br> - 금학기 성적 조회 <br> - 학기별 성적 조회 <br> - 누계 성적 조회 <br> - CSS 업무 |
| [최은석](https://github.com/volkinuna) | **팀원** <br> **[Back / Front]** | - 로그인 화면 구성 <br> - 로그인 기능 <br> - 비밀번호 초기화 기능 <br> - 학생 수강신청 <br> - 내 강의 조회 및 시간표 <br> - CSS 업무 |

## 사용기술
> #### Back-end 
> Java / Spring Boot <br> Spring Data JPA <br> Spring Security
 
> #### Front-end
> HTML / CSS / JavaScript

> #### DB
> MySQL

> #### Library
```
<!-- DayPilot JS -->
    <script src="/js/daypilot/daypilot-all.min.js"></script>
```
```
<!-- flatpickr 라이브러리-->
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr@latest/dist/l10n/ko.js"></script> <!-- 한국어-->
```
```
dependencies {
	runtimeOnly 'com.h2database:h2'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	// Querydsl add(spring boot 3)
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"

	// thymeleaf layout
	implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect'

	// spring security
	implementation 'org.springframework.boot:spring-boot-starter-security'
	testImplementation 'org.springframework.security:spring-security-test' //시큐리티 테스트 코드에서 사용
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6' //타임리프에서 시큐리티 객체 사용

	// modelmapper
	implementation group: 'org.modelmapper', name: 'modelmapper', version: '2.4.2'

	// email 기능
    implementation 'org.springframework.boot:spring-boot-starter-mail'

	// 엑셀파일을 통해 객체 생성하기
	implementation 'org.apache.poi:poi:5.2.3'
	implementation 'org.apache.poi:poi-ooxml:5.2.3'

	// WebSocket and STOMP support
	implementation 'org.springframework.boot:spring-boot-starter-websocket'

	// iTextPDF 라이브러리 추가
	implementation 'com.itextpdf:html2pdf:4.0.2'
	implementation 'com.itextpdf:kernel:7.2.0'
	implementation 'com.itextpdf:io:7.2.0'

	// jasyp
	implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5'
}
``` 

## ERD & Structure

<details>
  <summary>ERD</summary>
  
  자세한 내용은 [ERD](https://www.erdcloud.com/d/ZqPhoyxMsndkxBeMJ) 링크를 참고하세요.
</details>

<details>
  <summary>Structure</summary> 
  
  ![Structure](https://github.com/jylee102/seoha_univ/assets/101354244/6a30a3ec-7628-4416-848e-67ee6945e7af)
</details>

## 화면구성

<details>
  <summary>FIGMA</summary>
  
  자세한 내용은 [FIGMA](https://www.figma.com/design/ReFlk7qdJ02ze86EnuuGIk/Untitled?node-id=0-1&t=Gs8N523LbywdK1Av-1) 링크를 참고하세요.
</details>

## 기능
### 차별화된 기능
- 구성원 등록 엑셀 폼 다운로드 및 엑셀파일 업로드시 일괄 등록
- 강의계획서 엑셀로 업로드
- 강의 등록시 강의계획서 PDF 생성 및 열람
- 휴학 처리, 강의 개설 등 변경사항 알림 메시지를 발신 및 수신

<details>
  <summary>
    <b>공통</b>
  </summary>
<br>
  
**로그인**
- 비밀번호 초기화
- 아이디 저장
<br>

**개인정보**
- 마이페이지 정보 조회
- 마이페이지 정보 수정
- 비밀번호 변경
<br>

**공지사항 및 학사일정**
- 공지사항 조회
- 학사일정 조회
<br>

**메시지**
- 메시지 조회
</details>

<details>
  <summary><b>학생</b></summary>
 <br>
  
**휴학**
- 휴학 신청
- 휴학 내역 조회
<br>

**수강신청**
- 수강신청
- 수강신청 내역 조회
- 강의 시간표 조회
<br>

**성적 조회**
- 금학기 성적 조회
- 학기별 성적 조회
- 누계 성적

</details>

<details>
  <summary><b>교수</b></summary>
<br>
  
**강의**
- 강의 계획서 등록
- 내 강의 조회
- 강의별 학생리스트 조회
<br>
  
**출결 관리**
- 출결 관리
<br>

**학생 성적 등록**
- 성적 기입 및 수정

</details>

<details>
<summary><b>교직원</b></summary>
<br>
  
**등록 관리**
- 사용자(학생, 교수, 교직원) 계정 생성
- 구성원 명단 조회
- 강의 개설
- 휴학내역 처리
<br>
  
**학사 관리**
- 공지사항 관리
- 학사일정 관리
  
</details>


