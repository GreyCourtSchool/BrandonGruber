# Rev Cards

Rev Cards will allow revision cards to be created and used by students and people of all ages to master a subject.

# How to build
From project root, use:
```
mvn clean install
```

# How to run
Either import the pom.xml maven project file into an IDE (Eclipse or Intellij) 

Or from a DOS command prompt, run the compile-and-run.bat batch file:

```
compile-and-run.bat[ENTER]
```
 
This will compile source code into the bin directory, and run the binary code with the libraries in the library directory on the Java classpath

Note: the projects and cards CSV files are created on first launch of the application and are written to the user home directory, for example 
```
C:\Users\[USERNAME] in Windows.
``` 