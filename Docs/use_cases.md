# Use case scenarios
This document describes all the use cases and their interactions

## Start bot
|ID|Description|
| --- | --- |
| UC1 | #User starts the bot |
| Actors | Student |
| Pre-conditions | The student must have a telegram account </br> The elective course schedule should be available on Google sheets |
| Flow of Events | 1. The user sends the initiating command to the Telegram bot </br> 2.<a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L85-L111"> Telegram verifies if the user sent the right command </a> </br> 3. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L85-L111"> Telegram bot responds by sending a list of UI elements to choose course, view entire elective course sheet </a>
| Post-conditions | User is registered in InnoCalendat bot |
| Alternate flows and exceptions | The user doesn&#39;t send the right command |
| Assumption | The user has a Telegram account 
| Implementation | Bot.java |

## View elective courses
|ID|Description|
| --- | --- |
| UC2 | #View entire elective course schedule |
| Actors | Student |
| Pre-condition | Students sends the right command to initiate communication with bot |
| Flow of Events | 1. The student clicks on the button to view all courses </br> 2. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L153-L162"> The telegram bot sends a link to the google sheet containing elective course </a> |
| Post-conditions | User gets link for google sheet of entire elective course |
| Alternate flows and exceptions | 1. The user doesn&#39;t send the right command to the telegram bot </br> 2. The user doesn&#39;t have internet connection on his phone and link fails to load  |
| Assumption | The user has internet connection on his phone as to load the page containing google sheet |
| Implementation | Bot.java |

## Choose elective course
|ID|Description|
| --- | --- |
| UC3 | #User  wants to choose an elective courses in order to get notifications |
| Actors | Student |
| Pre-condition | User sends the right command to initiate communication with bot <br> Course to be registered by student exists |
| Flow of Events | 1. The student initiates communication with telegram bot </br> 2. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L188-L200"> Telegram bot sends lists of buttons to choose exact course for notifications </a> </br> 3. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L115-L131"> When the user is provided with the intended course, the user can register for it by clicking on the course button. User can click on course button again to cancel registration for this course. </a> </br> 4. Include UC4.2|
| Post-conditions | Information about user's choice saved in internal database |
| Alternate flows and exceptions | Telegram bot fails to save user's choice details into database because the phone was out of network coverage  |
| Assumption | The course intended to be registered for is found |

## Modify schedule
|ID|Description|
| --- | --- |
| UC4.1 | #The bot sends notifications when Admin makes changes in the elective course schedule on the Google Sheet |
| Actors | Admin, Student |
| Pre-condition | Google Sheets to be modified exist </br> Telegram bot must have stored elective course schedule in the database </br> Students must be registered for the course to be notified |
| Flow of Events | 1. The Admin modifies record on the Google Sheet to elective course schedule </br> 2. <a href="https://github.com/laskaa/InnoCalendar/blob/implementation_mvp/src/main/java/Bot.java#L65-L80"> The Telegram bot at every 30 min interval scans the elective course google sheet and compares it against the stored information in the database </a> </br> 3. <a href="https://github.com/laskaa/InnoCalendar/blob/implementation_mvp/src/main/java/Parser.java#L161-L171"> If the Telegram notices any change </a> between the database and the google sheets, it sends a notification to user about changes |
| Post-conditions | Telegram bot notices the change between the google sheet and the database and sends notification |
| Alternate flows and exceptions | 1. Telegram&#39;s server is down and bots are inactive </br> 2. The user doesn&#39;t receive the notification because his internet connection is turned off|
| Assumption | That Google sheet to be modified by the admin exists and this current information are stored in the database to be accessed by the bot </br> The student&#39;s internet connection is on or is within a range of network connectivity to receive the notification

## Send notification about class
|ID|Description|
| --- | --- |
| UC4.2 | #The bot sends notifications one hour before commencement of elective course |
| Actors | Telegram bot |
| Pre-condition | Students must be registered for the course to be notified |
| Flow of Events | 1. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Parser.java#L128-L195"> The telegram bot scans the database every 5 min </a> </br> 2. <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L44-L49"> If after scanning the database, the bot notices that a course is scheduled to start in an hour, the telegram bot sends a notification to students that registered for that course </br> 3. A notification with the course details are sent to the user </a> |
| Post-conditions | Notification is sent by telegram bot to user&#39;s(student) phone |
| Alternate flows and exceptions | 1. The user doesnt receive the notification because his internet connection is turned off and misses the class </br> 2. Telegram&#39;s server is down and bots are inactive  |
| Assumption | The user will receive the notification early enough to prepare for class |

## Unsubscribe from the bot
|ID|Description|
| --- | --- |
| UC5 | #User  wants to unsubscribe from the bot |
| Actors | Student |
| Pre-condition | Student subscribed to the bot to get notification about the elective course schedule |
| Flow of Events | 1. <a href="https://github.com/laskaa/InnoCalendar/blob/master/src/main/java/Bot.java#L156-L157"> Student initiates deletion </br><a href="https://github.com/laskaa/InnoCalendar/blob/master/src/main/java/Bot.java#L158-L161"> 2. Student confirms the unsubscription </br> 3.Bot will unsubscribe the user </br> 4.Bot deletes student’s data from the database </br> |
| Post-conditions | Chat with Bot will be deleted from Student’s telegram account |
| Alternate flows and exceptions | <a href="https://github.com/laskaa/InnoCalendar/blob/848d3c61fcbc9e4e224872d7ed8fc0e9023a86db/src/main/java/Bot.java#L157-L159"> Student doesn’t confirm the unsubscription |
| Assumption | The student has a Telegram account |
  
## Use case diagram 
![](use-case-diagram.png)

**Appendix**

**Innocalendar System** - it is a system that involves telegram bot, students and university educational department 

**The elective course schedule** - the time table for the elective courses managed by Admin on Google sheets.

**Notification** - An alert message sent by bot to the user on Telegram.

**Telegram Bot** - is an embedded application that run inside Telegram.

**Admin** -  the person who make changes to the schedule on on google sheet.

**User** - the person who wants to get notified and initiate commands from the bot.
