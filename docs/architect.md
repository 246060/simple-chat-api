# architect

- websocekt + redis 는 event 신호 정도만 전달 
![architecture](./arch-smp-chat.png) 

### 고려사항
1. socket.io max connection per node and server
2. socket.io max room per namespace
3. pm2 or docker for node - 어떤 방식을 할지... 
4. docker or not for spring - 어떤 방식을 할지...
5. nginx sticky session max connection   
6. api server 부하 테스트 jmeter? ngrinder? 
