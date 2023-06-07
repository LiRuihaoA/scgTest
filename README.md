# scgTest
test scg throughput for different route number.  
JDK17  
1000 is route number.  

**resource service**  

`cd SimpleService`  
`./mvnw clean package`  
`java -jar target/SimpleService-0.0.1-SNAPSHOT.jar`  
`wrk -t 1 -c 10 -d 10s http://localhost:8000/test`  

**SCG(Before)**  
`cd scg`  
`./mvnw clean package`  
`java -jar target/scg-0.0.1-SNAPSHOT.jar 1000`  
`wrk -t 1 -c 10 -d 10s http://localhost:8083/test`  
  

**SCG(After)**  
`cd newSCG`   
`./mvnw clean package`  
`java -jar target/newSCG-0.0.1-SNAPSHOT.jar 1000`  
`wrk -t 1 -c 10 -d 10s http://localhost:8082/test`  
