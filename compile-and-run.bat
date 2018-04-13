@echo off

rmdir bin
mkdir bin

javac -d bin -sourcepath src/main/java -cp .;libraries/commons-csv-1.5.jar;libraries/log4j-api-2.10.0.jar;libraries/log4j-core-2.10.0.jar src/main/java/uk/sch/greycourt/richmond/brandongruber/revcards/*.java

java -cp .;libraries/commons-csv-1.5.jar;libraries/log4j-api-2.10.0.jar;libraries/log4j-core-2.10.0.jar;bin uk.sch.greycourt.richmond.brandongruber.revcards.RevCardApplication