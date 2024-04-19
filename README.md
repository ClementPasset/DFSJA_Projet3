# APIChatop

This project is the backend for the Chatop application.
Follow the steps beneath in order to install the project and run it.

## Cloning the project

    git clone https://github.com/ClementPasset/DFSJA_Projet3.git

## Go to the folder of the project

    cd DFSJA_Projet3

## Run the application

    mvn spring-boot:run

## Environment variables

In order to run this application properly, you will need to connect it to your database, and settup a JWT key for security purposes.
For this, you will need the following environment variables :
DBDRIVER
DBURL
DBUSERNAME
DBPASSWORD
JWTKEY

## API Documentation

You will find the documentation of the API at the following URL, once the application has been started :
http://localhost:3001/api/swagger-ui/index.html#/