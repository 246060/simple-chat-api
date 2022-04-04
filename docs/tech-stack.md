# Tech stack
 
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