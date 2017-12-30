# CMDLib
A small library for registering commands in java.
# Installation
Include the jar file as a library 
# Configuration
Have a `config.json` file at the run directory of your applicatio
See the `config.json` file in the repository for syntax
# Importing the Library 
```java
import tk.craftxbox.cmdlib.CommandStore
import tk.craftxbox.cmdlib.Command
```
# Creating a Command Store
Make a public static variable at the beginning of your main class like this: 
```java
public static CommandStore cs = new CommandStore()
``` 
# Making Commands
Commands are ECMAScript .js files placed at the directory specified in the `config.json` 
The name of the file determines the name of the command
You may also add commands directly to the CommandStore with the `registerCommand` method
# Executing Commands
To execute a command use the `exec` method in CommandStore with the name of the command to execute
# Dependencies
Google GSON

