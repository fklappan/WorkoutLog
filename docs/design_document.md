# Workout Log

Track your workout results and know your PRs

Workout Log is an app to create workouts and track results for them while working on your PRs. Enter your workouts without any limitations because workouts differ every time.

## Goals

The goals of this project are learning purposes on the one hand but also to be able to track the workouts and create some fancy statistics.

## Language and architecture

As this should be a native Android application and a project for practice, the language is kotlin. 
The architecture is a multi module project following the clean architecture principle implementing the MVVM pattern for the UI.

### Modules
To follow the clean architecture principle, the project will be seperated in multiple modules with a "one way dependency" rule.

#### Domain
Plain Java project with rxJava. This is the inner circle of the clean architecture and has no "in project" dependencies.
The domain module also exposes interface which will be implemented by the dependent projects.

#### Data
Android project with Room. Depends on the domain module.

#### App
Android app project using MVVM pattern with ViewModel, LiveData and NavigationComponents. Depends both on the domain and the data module.

## Libraries / dependencies

The new Android Jetpack libraries are used:
- Room
- LiveData
- NavigationComponents

For practice purposes rxJava is used also.

For dependency injection, dagger is used.

## Further documentation

As this is a agile project, the ongoing architectural decisions are tracked via "Architectural Decision Records" in the adr folder.
Both (adr+design_document) should be kept as clean as possible. So every brainstorming and idea collection are stored otherwise.
