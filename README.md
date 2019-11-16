# InnoCalendar
A Telegram Bot which provides schedule of Elective Courses Innopolis University. 

## Problem
Elective courses schedule is available now in Google Spread Sheet and the schedule changes quite often. It is hard to track changes manually. In the current situation it is required to open Google Spread Sheet every day in order to see updates - there is no notifications at all.

## Summary
Addressing the above stated problem, we assume that it will be comfortable to have notifications about upcoming classes and changes in schedule. The system benefits for all masters who wants to do homework on time and be aware of schedule changes.

## Prior research
For implementation we propose to use Telegram Bot. The user will be able to find and subscribe to desired elective courses, while the bot will send notifications about changes in the official Google Spreadsheet and provide information of upcoming classes.   

## bot alias: @iellectivebot

##How to run
0)  open solution with intllij idea
1)	add account credits and postgre DB address to hibernate.cfg.xml
2)  for first run uncoment string <property name="hbm2ddl.auto">create</property> in hibernate.cfg.xml
3)  set id of user you want to be admin to ADMIN_ID in Bot.java 
4)  (optional) bot user name can be changed in Bot.java getBotUsername(), api token can be changed by getBotToken() 
4) 	run solution
5) 	during first run you will be asked to login to your google account 
 
