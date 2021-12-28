# Warships-sim-io
Online multiplayer Warship turn based game.  
Socket Programming.  
Client Server design.  
Online Play.  
Load up games from txt or create new.  
Destroy enemy ships by shooting(sending coordinates to server)  
Code is in debug mode  

# Usage
```mvn install```  
Server:  
```java -jar target.jar local server 5015```  
Clients:  
```java -jar target.jar local client 5015```  
use board from folder board  
create game and join  
send coordinates (A1-H8) about enemy ships  