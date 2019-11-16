# InnoCalendar
A Telegram Bot which provides schedule of Elective Courses Innopolis University. 

**Bot alias: @iellectivebot**

## Problem
Elective courses schedule is available now in Google Spread Sheet and the schedule changes quite often. It is hard to track changes manually. In the current situation it is required to open Google Spread Sheet every day in order to see updates - there is no notifications at all.

## Summary
Addressing the above stated problem, we assume that it will be comfortable to have notifications about upcoming classes and changes in schedule. The system benefits for all masters who wants to do homework on time and be aware of schedule changes.

## Prior research
For implementation we propose to use Telegram Bot. The user will be able to find and subscribe to desired elective courses, while the bot will send notifications about changes in the official Google Spreadsheet and provide information of upcoming classes.   


##How to run

### Database
This application uses Postgres. You should have the database credentials
and a database for the user.

For convinience, a docker compose file is provided to run a configured
postgres database in the host network.

Set them in `src/main/resources/hibernate.cfg.xml`. Put the username
in the `<property name="hibernate.connection.username>` (default is `postgres`)
tag,
put the password in the `<propery name="hibernate.connection.password">`
(default is `postgres`).

Put the database in the `<property name="hibernate.connection.url">` tag in the
connection url. Additionally you can specify other connection details in the
connection url.

### Building
This application can be compiled using (Maven)[https://github.com/apache/maven].

To compile this application, run
```
mvn clean compile assembly:single

```

After this, the JAR file will be available at
`target/innoelectivebot-1.0-SNAPSHOT-jar-with-dependencies.jar`.

### Running
You need Java to run this application.

Run this application by executing the jar file:
```
java -jar target/innoelectivebot-1.0-SNAPSHOT-jar-with-dependencies.jar
```

#### First run and Google Docs Application
During the first run you can be asked to provide access to the Google Docs
application which is used to read the spreadsheet.

The application will print
```
Please open the following address in your browser:
  https://accounts.google.com/o/oauth2/auth...

```

To complete the OAuth2, open the provided link in a web browser and grant
access to the application. After granting the access, you will be redirected
to a page at `localhost:8888/...`. If the bot is started on the same machine
where the web browser is ran, the bot will get the key itself. If, for example,
the application is being deployed on the remote server, OAuth2 link can be
opened on the local machine, but there should be a request sent to the bot
on the remote server which can be done using curl (replace `localhost:8888`
with the final redirect address):
```
curl localhost:8888/...
```

#### Bot configuration
The Telegram ID of the admin user can be set using the `ADMIN_ID` constant
in the `Bot` class (`src/main/java/Bot.java`).
