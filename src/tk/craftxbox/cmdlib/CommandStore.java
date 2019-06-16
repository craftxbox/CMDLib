/*
Copyright 2017 craftxbox
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package tk.craftxbox.cmdlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Command store for commands. Loads the configuration and commands
 * @author craftxbox
 */
public class CommandStore {
	/** Every loaded command, by name*/
	public Map<String,Command> commands = new HashMap<>();
	
	/** Configuration*/
	public static JsonObject config = new JsonObject();
	
	/**Creates a command store and loads all commands */
	public CommandStore() throws IOException {
		loadConfig();
		loadCommands();
				
	}
	/**
	 * Executes a command 
	 * @param name Name of the command
	 * @param args Array of arguments to give to the command (Can be null)
	 * @return The output of the command
	 * @throws ScriptException When the command gives a script exception
	 */
	public Object exec(String name, Map<String,Object> args) throws ScriptException {
		if(this.commands.get(name) == null) return "Command doesnt exist";
		return this.commands.get(name).exec(args);
	}
	/**
	 * Registers a new command to the store
	 * @param name Name of the command
	 * @param value Value the command should return or ECMAScript 5 script command should evaluate
	 * @param isScript If the command is a script or not
	 */
	public void registerCommand(String name, String value, boolean isScript) {
		if(!isScript) this.commands.put(name, new Command(name, value, false));
		this.commands.put(name, new Command(name, value, true));
	}
	/** Loads commands from command directory
	 * @see #loadConfig()
	 * @see #reload()
	 * @throws IOException When command directory cannot be read
	 * @throws NullPointerException When Configuration isn't loaded
	 */
	public void loadCommands() throws IOException,NullPointerException {
		// Get the directory in the configuration
		this.commands.clear();
		File f = new File(config.get("dir").getAsString());
		
		//Get everything in the directory
		String[] ta = f.list();
		
		//Loop through files in directory
		for(String i : ta) {
			//If file extension is .js
			if(i.endsWith(".js")) {
				//Split the file name by '.' to get the command name
				String n = i.split("\\.")[0];
				
				f = new File(i);
				
				//If file exists read its contents
				if(f.exists()) {
					String t = "";
					try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"))){
						String str;
						while ((str = in.readLine()) != null) {
							t = t + str + "\n";
						}
						//Register new command
						this.commands.put(n, new Command(n,t,true));
						in.close();
					} 
				}
			}
		}
	}
	/** Loads configuration file 
	 * @throws IOException When configuration cannot be read
	 */
	public static void loadConfig() throws IOException {
		//Get configuration from run directory
		File f = new File("config.json");
		
		//If the configuration doesn't exist, create a default configuration file
		if(!f.exists()) {
			f.createNewFile();
			try(OutputStream s = new FileOutputStream(f)){
				s.write(new String("{\"dir\":\"./\"}").getBytes());
				s.flush();
				s.close();
			}
			
		}
		//Open and read configuration to config
		try(FileInputStream s = new FileInputStream(f)){
			String t = "";
			while(s.available() > 0) {
				t += (char)s.read();
			}
			config = new JsonParser().parse(t).getAsJsonObject();
			s.close();
		}
		
	}
	/** Reload configuration and commands */
	public void reload() throws IOException {
		loadConfig();
		loadCommands();
	}
	
}
