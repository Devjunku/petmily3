# 📝 Build & 배포 문서

## ⁉️ 버전 정보 및 설정 정보

- JVM 버전 :  1.8
- 웹 서버 : AWS 배포
- WAS : Apache Tomcat
- VSCode : 1.59.0
- Intellij : 2020.2.2

## ⚠️ 배포 시 특이사항

- Docker로 배포하는 방식이 아닌 직접 빌드한 파일 (JAR 파일)을 실행시켜 배포하는 방식을 채택
- Docker 배포중 docker-yml 설정 오류로 인한 배포 실패

## 📜 DB 정보

- 배포용 DB 정보

  -> Hostname(접속 주소) : 54.180.127.68

  -> Username : root

  -> UserPassword : 1234
  
  ![스크린샷 2021-08-19 오전 11.07.25](petmily Build.assets/스크린샷 2021-08-19 오전 11.07.25.png)

  -> ER-Diagram

![스크린샷 2021-08-19 오전 11.22.40](petmily Build.assets/스크린샷 2021-08-19 오전 11.22.40.png)
