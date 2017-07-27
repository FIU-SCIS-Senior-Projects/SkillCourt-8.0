# SkillCourt v8.0

[![N|Solid](https://github.com/FIU-SCIS-Senior-Projects/SkillCourt-Ver-6.0/blob/master/SkillCourt%206.0/Code/Fall%202016%20-%20SkillCourt%206.0%20Code/app/src/main/res/drawable/skill_court_header_logo.png?raw=true)](https://www.skillcourt.com)


# Developers
* David Schiumerini
* Richard Rodriguez


# Structure of the main folder

| Direcotry | Purpose |
| ------ | ------ |
| Code | The Code directory includes this file, which explains the directory structure, and the source code |
| Documents | The Documents direcotry includes the Project Documentation file, the daily Scrum Meeting Minutes files, and all other related documents |
| Posters | The Posters directory includes the final copy of the posters for each team member |
| Videos | The Videos directory includes only one file that contains the lik to the four 


# Android Application Structure
Some of the frameworks/libraries being used by SkillCourt are
#### Adapters
>This folder contains all the classes you will use to show lists or options. Right now only contains GamesPlayedRecyclerViewAdapter which you can find on the main dashboard screen.
#### Connection
>Connection contains all the classes related to our wifi connection to our Arduinos. We have a manager that handle each ArduinoConnectionHandler using an Arduino object. All of these belongs to the project. There is no use of a library for this.
#### Entities
>All the classes related to our business logic
#### Game
>This folder contains all the classes used during a game. We have the CountDownTimer class which is the timer. We also have an SkillCourtManager used to handle all the games created while using the application
#### Interfaces
>Creation of java interfaces used all over the application
#### Manager
>Contains the StepManager, which handles the configuration of the dynamic steps
#### IU
>This folder contains all of the UI classes from android. It contains the Activities and Fragments divided by functionality.

**NOTE**: the Adapters and Connections folders will be left out of this package as is proprietary technology and does not comply with the MIT license.  For access to those files, please contact the product owner, Guðmundur Traustason, by email to gummi.traustason@gmail.com

# Arduino application
For the arduino we are using the following libraries:
* **Wifi101**
* **FastLed**: these libraries allows us to connect to the WiFi and change the colors of the LED's

**Structure:** these are libraries embedded in the project.  No need to change them unless we have to update to the latest libraries.


# Firebase
For the android application and storing data, we are using a firebase application.  To learn more about FireBase you can visit them at https://firebase.google.com/docs/ .  The library is embedded within the app and does not need to be updated unless the database structure has been changed.

# Requirements
* 4 SkillCourt Pads (composed of)
    * Arduino MKR1000
    * LEDs
    * Push buttons
* Raspberry Pi 2 Model B
* Android Studio IDE 2.3 or higher
* Android Smartphone


