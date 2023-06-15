@echo off
set PATH_TO_FX=C:\javafx\javafx-sdk-11.0.2\lib
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.media -jar .\project_alpha.jar
