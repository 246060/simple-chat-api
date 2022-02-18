# toy project
구상중...

## simple-chat-api 
event를 통해서는 신호 정도만 전달 받음. 실제 데이터 전달은 api로 전달하는 구조로 구상중.  

- client와 socket 연결은 node의 socket.io 나 spring stomp 둘중 고려중.  
- api 서버는 spring boot 로 결정


### 구상
![architecture](docs/architecture.png)
[이미지 참조](https://velog.io/@tigger/Github-Action%EA%B3%BC-AWS-S3-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0) 

![erd](docs/chat_erd.png)


### 현재 구상한 tech stack
1. spring boot + web mvc, validator, actuator
2. spring data + jpa & querydsl
3. websocket : spring stomp or node socket.io 둘 중 고민중
3. mapstruct
4. h2, mariadb
5. aws ec2, nginx, s3


