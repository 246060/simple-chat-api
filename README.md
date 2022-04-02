# [Toy Project] simple-chat-api 
- 간단한 채팅 서버
- 유저 클라이언트 제외 

## Tech stack 
1. api server 
    - spring boot mvc 
    - spring validator 
    - spring actuator
    - spring data jpa
    - querydsl
    - mapstruct
    - h2, mysql
        - mysql replication(rw, ro) 
    - spring data redis
    - nginx : RR
2. socket server
    - node + express
    - socket.io
    - nginx : sticky session
3. Redis
    - Cluster (try? nope) 
4. Github Repo + Action, Codedeploy
5. NoSQL(Try to manage chat messages by NoSQL? nope) 
6. docker    
    - 로컬 개발환경 인프라 구성 docker-compose
    - mysql replication
    - redis
    - nginx * 2

## 아키텍처 설계
- websocekt + redis 는 event 신호 정도만 전달 
![architecture](docs/arch-smp-chat.png) 

### 고려사항
1. socket.io max connection per node and server
2. socket.io max room per namespace
3. pm2 or docker for node - 어떤 방식을 할지... 
4. docker or not for spring - 어떤 방식을 할지...
5. nginx sticky session max connection   
6. api server 부하 테스트 jmeter? ngrinder? 

## ERD
![erd](docs/erd-smp-cht.png)

### 고려사항
1. 디비 테이블 별 소프트 fk와 물리 fk 결정
2. 디비 테이블 별 소프트 삭제와 물리 삭제 결정, casecade도 같이 고려
3. message 테이블은 record가 엄청 많을건데... 어떻게 관리하지? table 파티션? nosql scale out? 


## api server 
[READ](docs/api-server/README.md)

## socket server 
[READ](docs/socket-server/README.md)
