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

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Command object. Handles execution and creation of commands
 * @author craftxbox
 *
 */
public class Command {
	/**Name of command, used to find the command in the store*/
	public String name;
	
	/**Returned on execute of script isn't defined*/
	public Object value;
	
	/*ECMAScript based script for the command to run on execute*/
	public String script = null;
		
	/**Creates a command object
	 * @param name Name of the command used to find command in the store
	 * @param value Value of command to return or ECMAScript for the command to run on execute
	 * @param isScript Used to identify if this is a scripted command or not
	 * @throws NullPointerException in value is null
	 */
	public Command(String name, Object value, Boolean isScript) {
		//Sets the name of the command
		this.name = name;
		
		//Throw exception of value is null
		if(value == null) throw new NullPointerException();
		
		//Sets script of command if its a script
		if(isScript) this.script = (String) value; 
		
		//Sets the value of the command if it is not a script
		else this.value = value;
	}
	/**Executes the command
	 * @param args Arguments for scripted command. Can be null.
	 */
	public Object exec(Map<String,Object> args) throws ScriptException {
		//Checks if a value should be returned or a script should be executed
		if(script == null) return value;
				
		//Sets args to an empty map if it is null
		if(args == null) args = new HashMap<String,Object>();
		
		
		//Creates the script engine to execute the command
		ScriptEngineManager f = new ScriptEngineManager();
		ScriptEngine e = f.getEngineByName("javascript");
		
		//Iterate through map entries and pass them to the scriptengine
		for(String i : args.keySet()){
			e.put(i,args.get(i));
		}
		
		//Executes the command and returns the output
		
		e.put("args",args);
		return e.eval(script);
	}
	public String toString() {return this.name;}
}
