To build app:  
mvn clean install  
To run server on 8080 port:  
java -jar .\server\target\grpc-server.jar   
To run client:  
java -jar .\client\target\grpc-client.jar  
To run python client  (python version: 3.11)
python .\client-python\grpc_client.py

Firstly, the client sent one request to get one response.  
After it the client sent one request to get response stream.



