# erd

![erd](./erd-smp-cht.png)

### 고려사항
1. 디비 테이블 별 소프트 fk와 물리 fk 결정
2. 디비 테이블 별 소프트 삭제와 물리 삭제 결정, casecade도 같이 고려
3. message 테이블은 record가 엄청 많을건데... 어떻게 관리하지? table 파티션? nosql scale out? 
