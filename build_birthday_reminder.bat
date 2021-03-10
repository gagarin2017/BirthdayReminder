@echo off
title -= Birthday Reminder gradle clean build =-
echo Cleaning the project ...
gradlew clean && gradlew cleanEclipse build eclipse --refresh-dependencies && echo Finished cleaning and building the project && pause