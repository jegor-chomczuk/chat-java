# Chat
This project was delived using socket technology, which allows communicating between server side of apllication with client servers. 

Main chat features:
1. Group channel allowing multiple people to communicate with each other (at the start of the application Main channel is created);
2. Private channels allowing two or more people talking;
3. Transferring files between chat users on a given channel;
4. Saving the history of conversations on the server side in a database based on a flat file (when application server is starting file history.txtis created);
5. Possibility to browse the history of conversations from the client level (if user participated in the conversation / was on the channel). 

# Starting the application
In order to make chat work server must be started first. To do so run main method on Server class. New server will start and will create a socket on port 8000.

For each chat user separate client server must be started by running main method on Client class (in case of using IntelliJ it is possible by choosing "Allow multiple instances" option in Run Configurations).
![image](https://user-images.githubusercontent.com/84178074/162778199-dcf90353-b987-4638-b5a1-81c5c54284b1.png)

# Joining the chat
When app server is up and a new client server has started user must provide his/her username:

![image](https://user-images.githubusercontent.com/84178074/162778997-d4d365eb-d4c7-42df-8e35-e7fdc9b077f8.png)

After entering username the communication between both servers will be established:

Client server:
![image](https://user-images.githubusercontent.com/84178074/162779319-cb0d241a-fea5-4f59-a391-a796882a2603.png)

App server:
![image](https://user-images.githubusercontent.com/84178074/162779619-22326a05-f21c-4f96-8828-2cd7254b07d6.png)

# Available commands
User always can check available commands by typing following phrase: "help:". It will display all available commands and its description:
![image](https://user-images.githubusercontent.com/84178074/162780100-46b14ece-899f-4546-a6bf-341332f1f1dc.png)

# Changing channels
Every user entering the chat will be placed in Main channel. But of course switching to other channels is possible by typing "channel:{channel name}" phrase.

![image](https://user-images.githubusercontent.com/84178074/162781101-6ce5a5c8-6772-4a20-853a-9e605abc78da.png)

Each time user switches to anothe channel, that particular channel is being set as his/her current channel. Such information will be present on app server:

![image](https://user-images.githubusercontent.com/84178074/162781502-dd418aa5-00ec-4f2b-8828-6102639831e8.png)

User also can make sure which channels he/she has used before by typing "showmychannels:" phrase:

![image](https://user-images.githubusercontent.com/84178074/162783863-cd0c94c9-1e6f-4534-89c7-9fe6103d6fcf.png)

# Messaging
Sending messages will be broadcasted among users on the same channel. User who are currently using other channels will not receive messages from other people.

![image](https://user-images.githubusercontent.com/84178074/162783212-1c8e04ac-2ed9-408f-aa96-1221769c1ddb.png)

# History
Each message is being stored on history.txt file. User can check history of any channel he/she has been to by typing "history:{channel name}" phrase:

![image](https://user-images.githubusercontent.com/84178074/162784415-d05fe0ed-2742-40c5-8fc1-32a01ed28fed.png)

# Sending files
Each user can send files to other participants of the current channel. Providing command "sendfile:{file path}" will make other users download indicated file, which will be visible in local directory:

![image](https://user-images.githubusercontent.com/84178074/162785578-5374f594-e826-4046-ab6f-cd20897c3880.png)

![image](https://user-images.githubusercontent.com/84178074/162785915-105d2119-281f-4b09-a5c3-f4c3eaff9b17.png)

