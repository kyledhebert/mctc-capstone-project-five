# Project Five

Project Five is a volunteer management app built as part of the software engineering capstone course at Minneapolis Community and Technical College.

## Goals
My goals for the project were to continue exploring Java and web-application frameworks using the Play Framework, as well as to use JPA and Hibernate for persistence. I also wanted to focus on Test Driven Development and testing with Selenium.

## Features
The app is designed to allow volunteer coordinators for non-profit organization to coordinate volunteer work assignments and schedules.

Volunteer coordinators will be able to:
 - add new jobs and assignments
 - assign volunteers to assignments
 - and post messages to the volunteers (coming soon)

 ## Running the App

 The easiest way to run the app is through [Lightbend Activator](https://www.lightbend.com/activator/docs).

 On a Mac this can be done via homebrew using `brew install activator`. Instructions for other operating systems can be found on the [Play Framework website.](https://www.playframework.com/documentation/2.5.x/Installing)

 After Activator is installed navigate to the root directory of the application and run `activator run`. This will compile the project and start a server at <http://localhost:9000>

 This version uses a H2 in-memory database. The database will be empty the first time the application is run, and will H2 will drop the database when there are no more connections to it.