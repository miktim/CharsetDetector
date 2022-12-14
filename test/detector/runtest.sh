#!/bin/bash

echo $(javac -version)
echo $(java -version)
if [ -f ../../dist/java-charset-detector-0.0.2.jar ]; then
  javac -cp ../../dist/java-charset-detector-0.0.2.jar DetectorTest.java
  java  -cp ../../dist/java-charset-detector-0.0.2.jar:. DetectorTest
  rm -f *.class
else
  echo First make the java-charset-detector-0.0.2.jar file.
fi
echo
echo Completed. Press Enter to exit...
read
