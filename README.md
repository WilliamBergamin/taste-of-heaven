# A Taste of Heaven

## Description

### Problem

 Large-scale events tend to have long lines to purchase drinks. These long lines ruin an attendee’s experience of the event and reduce the number of sales that could be made. This is a web-based drink ordering system that aims to reduce the wait time and increase sales.

### Goals

This in order to make this web-based drink ordering system multiple components must be created. The first is an android phone application that allows attendies also refered to as users to place orders. The second is a javaFX application running on the physical dispensing machine, that allows the user and maintence worker to interact with the machine. The third is a cloud infrastructer allowing to manage and process all orders, users, events and machine. The forth component of the project is a Administrative web application that lets admin users maintain events and machines.

## Table of Contents

- [A Taste of Heaven](#a-taste-of-heaven)
  - [Description](#description)
    - [Problem](#problem)
    - [Goals](#goals)
  - [Table of Contents](#table-of-contents)
  - [Source File Structure](#source-file-structure)
    - [REST API](#rest-api)
    - [NGINX](#nginx)
    - [Android Phone Application](#android-phone-application)
    - [MongoDB](#mongodb)
    - [javafx-on-pi](#javafx-on-pi)
    - [Admin_App](#adminapp)
  - [Deployment](#deployment)
  - [Infrastucture Design Schematic](#infrastucture-design-schematic)

## Source File Structure

### REST API

The API consists of a Flask python API, the Source code can be viewed in the ```./API/``` folder. The logs of the application are placed in the ```./API/logs``` folder they are also a volume to the container of the application. The Docker container containing the Flask source code is running it on a UWSGI web server the configuration file of this web server can also be found in the ```./API/``` folder.

More information can be found in the API/README.md file.

### NGINX

The NGINX instance is used as a proxy to the UWSGI webserver running the Flask RESTapi. The config and dockerfile can be found in the ```./Nginx/``` folder.

### Android Phone Application

The Andorid Phone Application source code can be found under ```./App``` folder. It will automaticly connect to the cloud instance when build and deployed to an android phone.

### MongoDB

The mongoDB is deployed using docker and docker-compose, the container is defined in the docker-compose.yml file.

### javafx-on-pi

This is a javaFX application is designed to run on a rasberri pi 4 connected to an stm on the physical drink dispensor. This application allows the user and administrator to interact with the machine. Is communicates with the cloud infrastucter through HTTP and with the stm through serial UART connection. it can be found in the ```./javafx-on-pi``` 

### Admin_App

This component is a REACT based web application running on a Next.js web server. It allows the administrator to manage and view events and machines. It iteracts closly with the cloud infrastructer. 

## Deployment

It is possible to deploy this project on a local machine. To do this you will need:

- Android Studio
- Docker
- Docker-Compose

1. Use Android studio to build the phone application a run it on your device.
2. Run the build.sh script, that uses docker, to build the docker images needed.
3. run ```docker-compose up``` to deploy the cluster on your machine. Dont forget to create the apropiate .env files following the structure of the .env.example file.

## Infrastucture Design Schematic

![Infrastucture Design Schematic](docker_diagram.png)
