# Chat
This chat project was implemented using socket technology, which allows communication between the application server side and client servers.

Main chat features:
1. Group channel to allow communication between multiple people (a Main channel is created when the application is launched);
2. Private channels allowing two or more people to chat;
3. File transfer between chat users on a given channel;
4. Saving the history of chats on the server side in a database based on a flat file (when starting the application server, a history.txt file is created);
5. Possibility to view the chat history from the client level (if the user participated in the given channel). 

# Starting the application
For the chat to work, the application server must first be started. To do so, run the main method in the Server class. The new server will be started and will create a socket on port 8000.

For each chat user, start a separate client server by running the main method on the Client class (if using IntelliJ, this is possible by selecting "Allow multiple instances" in Run Configurations).

![image](https://user-images.githubusercontent.com/84178074/162778199-dcf90353-b987-4638-b5a1-81c5c54284b1.png)

# Joining the chat
When the application server starts and the new client server starts, the user must enter their username:

![image](https://user-images.githubusercontent.com/84178074/162778997-d4d365eb-d4c7-42df-8e35-e7fdc9b077f8.png)

When the username is provided, communication between the two servers will be established:

Client server:

![image](https://user-images.githubusercontent.com/84178074/162779319-cb0d241a-fea5-4f59-a391-a796882a2603.png)

App server:

![image](https://user-images.githubusercontent.com/84178074/162779619-22326a05-f21c-4f96-8828-2cd7254b07d6.png)

# Available commands
A user can always check the available commands by typing command: "help:". All available commands will be displayed along with their descriptions:

![image](https://user-images.githubusercontent.com/84178074/162780100-46b14ece-899f-4546-a6bf-341332f1f1dc.png)

# Changing channels
Every user entering the chat will be placed on the Main channel. Of course it is possible to switch to other channels by typing command "channel:{channel name}".

![image](https://user-images.githubusercontent.com/84178074/162781101-6ce5a5c8-6772-4a20-853a-9e605abc78da.png)

Each time a user switches to another channel, that particular channel is set as their current channel. This information will appear on the application server:

![image](https://user-images.githubusercontent.com/84178074/162781502-dd418aa5-00ec-4f2b-8828-6102639831e8.png)

Users can also check which channels they have used before by typing command "showmychannels:":

![image](https://user-images.githubusercontent.com/84178074/162783863-cd0c94c9-1e6f-4534-89c7-9fe6103d6fcf.png)

# Messaging
Messages sent will be broadcast to users on the same channel. Users who are currently using other channels will not receive messages from other people.

![image](https://user-images.githubusercontent.com/84178074/162783212-1c8e04ac-2ed9-408f-aa96-1221769c1ddb.png)

# History
Each message is stored in a history.txt file. User can check the history of any channel he was on by typing command "history:{channel name}":

![image](https://user-images.githubusercontent.com/84178074/162784415-d05fe0ed-2742-40c5-8fc1-32a01ed28fed.png)

# Sending files
Any user can send files to other participants in the current channel. Specifying the command "sendfile:{file path}" will cause other users to download the specified file, which will be placed in the local directory:

![image](https://user-images.githubusercontent.com/84178074/162785578-5374f594-e826-4046-ab6f-cd20897c3880.png)

![image](https://user-images.githubusercontent.com/84178074/162785915-105d2119-281f-4b09-a5c3-f4c3eaff9b17.png)

# Exit
A user can leave the chat by typing command "exit:":

![image](https://user-images.githubusercontent.com/84178074/162819520-d62ca04c-7d47-48ee-9931-7b65cf2dabc6.png)
