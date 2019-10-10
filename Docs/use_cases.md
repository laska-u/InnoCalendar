**Use case scenarios**

| Use case name | User starts the bot |
| --- | --- |
| Actors | Student, Bot |
| Pre-conditions | The student must have a telegram account </br> The elective course schedule should be available on Google sheets |
| Flow of Events | 1.The user (student) sends the initiating command to the Telegram bot </br> 2.The Telegram verifies if the user sent the right command </br> 3.The Telegram bot responds by sending a list of buttons to search for course, view entire elective course sheet, and a button to select course based on Masters or Bachelor&#39;s program, view course schedule </br>4. The user then replies by selecting any of the buttons |
| Post-conditions | The bot replies the user with a list of buttons |
| Alternate flows and exceptions | The user doesn&#39;t send the right command |
| Assumption | The user has a Telegram account |



| Use case name | The bot sends notifications when Admin makes the change on the elective course schedule on the Google Sheet |
| --- | --- |
| Actors | Telegram bot, Admin,Student |
| Pre-condition | Google Sheets to be modified exist </br> Telegram bot must have stored elective course schedule in the database </br> Students must be registered for the course to be notified |
| Flow of Events | 1. The Admin modifies record on the Google Sheet to elective course schedule </br> 2. The Telegram bot at every 12 hours interval scans the elective course google sheet and compares it against the stored information in the database </br> 3.If the Telegram notices any change between the database and the google sheets,it sends a notification to students that registered for the course that was changed |
| Post-conditions | Telegram bot notices the change between the google sheet and the database and sends notification |
| Alternate flows and exceptions | 1.Telegram&#39;s server is down and bots are inactive </br> 2.The user doesn&#39;t receive the notification because his internet connection is turned off and misses the class  |
| Assumption | That Google sheet to be modified by the admin exists and this current information are stored in the database to be accessed by the bot </br> The student&#39;s internet connection is on or is within a range of network connectivity to receive the notification



| Use case name | The bot sends notifications one hour before commencement of elective course |
| --- | --- |
| Actors | Telegram bot(Primary),Student |
| Pre-condition | Students must be registered for the course to be notified |
| Flow of Events | 1.The telegram bot scans the database every hour </br> 2.If after scanning the database, the bot notices that a course is scheduled to start in an hour, the telegram bot sends a notification to students that registered for that course </br> 3.A notification with the course details, venue and time is sent to the user |
| Post-conditions | Notification is sent by telegram bot to user&#39;s(student) phone |
| Alternate flows and exceptions | 1.The user doesnt receive the notification because his internet connection is turned off and misses the class </br> 2.Telegram&#39;s server is down and bots are inactive  |
| Assumption | The user will receive the notification early enough to prepare for class |



| Use case name | View entire elective course schedule |
| --- | --- |
| Actors | Student,Telegram bot |
| Pre-condition | Students sends the right command to initiate communication with bot |
| Flow of Events | 1.The student clicks on the button to view all courses </br> 2.The telegram bot then redirects the user to the google sheet containing elective course  |
| Post-conditions | User views google sheet of entire elective course |
| Alternate flows and exceptions | 1.The user doesn&#39;t send the right command to the telegram bot </br> 2.The user doesn&#39;t have internet connection on his phone and telegram bot opens the link but the page fails to load  |
| Assumption | The user has internet connection on his phone as to load the page containing google sheet |



| Use case name | User (student) wants to register for an elective course in order to get notifications |
| --- | --- |
| Actors | Student,Telegram bot |
| Pre-condition | Students sends the right command to initiate communication with botCourse to be registered by student exists |
| Flow of Events | 1.The student initiates communication with telegram bot </br> 2.Telegram bot sends lists of buttons to view course, search for course </br> 3. When the user is provided with the intended course, the user can register for it by clicking on the register button </br> 4.Telegram bot then inserts user(student) id and course into Google Spreadsheet for notification purposes  |
| Post-conditions | Course is registered for successfully and users id and course details are inserted into the google spreadsheet |
| Alternate flows and exceptions | 1.The course the user wants to register for isn&#39;t found </br> 2.Telegram bot fails to insert user record and course detail into the spreadsheet because the phone was out of network coverage   |
| Assumption | The course intended to be registered for is found |







**Appendix**

**The elective course schedule** - the time table for the elective courses managed by Admin on Google sheets.

**Notification** - An alert message sent by bot to the user on Telegram.

**Telegram Bot** - is an embedded application that run inside Telegram.

**Admin** -  the person who make changes to the schedule on on google sheet.

**Student** - the person who wants to get notified and initiate commands from the bot.
