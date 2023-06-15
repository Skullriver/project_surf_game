#!/bin/bash
rm -rf beans; mkdir beans
find . -name "*.java" > sourcefiles
javac --module-path $JAVAFX --add-modules javafx.controls,javafx.media -s src/ -d beans/ @sourcefiles
rm sourcefiles
