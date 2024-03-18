# HiveMind

## Description
The project is an instant messaging application. It allows the user to register, who can then create a new server or join an existing one. Each server is a collection of thematic channels. These channels are simply group chats where you can post content.

## Instructions for use

Before starting the server, you must provide such data as the port on which the server is to run and all necessary data related to the database.

Attention! The server only works with MySQL.

![Server](https://github.com/ravdal24/HiveMind/blob/master/screenshots/server.PNG)

After starting the server and connecting to the database, you can create an account

![Registration](https://github.com/ravdal24/HiveMind/blob/master/screenshots/registration.PNG)

Once we create an account, you can log in

![Log](https://github.com/ravdal24/HiveMind/blob/master/screenshots/log.PNG)

Both when logging in and registering, you must provide the server IP and port on which it is running

![Client](https://github.com/ravdal24/HiveMind/blob/master/screenshots/client.PNG)

After logging in, we will see the main screen. At the top we can see the list of servers we belong to and on the left the list of channels on the selected server. In the middle there are news on a given channel. A new server can be added using the green button with a plus sign at the top.

## JDK
I worked on the project using JDK 20

## Project status
Currently in Development.
- ~~Server~~
- ~~Database structure~~
- ~~Client GUI~~
- ~~Registration~~
- ~~Login~~
- ~~Creating a new server~~
- ~~Joining an existing server~~
- ~~Creating a new channel~~
- ~~External CSS~~
- Moderation
- Adding images
- Private messages

## Tech stack
- Java Core(Sockets, Streams, Threads, JDBC)
- Maven
- JavaFX
- CSS
- MySQL
- IntelliJ IDEA
- Apache

## Info
The structure of the database with which the Server cooperates is located in the databaseStructure.txt file. To use the server, you must first create a database in MySQL

