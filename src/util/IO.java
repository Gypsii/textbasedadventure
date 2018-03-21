package util;

import gfx.ConsoleWindow;
import gfx.Graphics;
import main.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.Game.GRAPHICS_ENABLED;

public class IO {

	public static boolean ANSIEnabled = true;
	
	static final String ANSI_BLACK = "\u001B[30m";
	static final String ANSI_RED = "\u001B[31m";
	static final String ANSI_YELLOW = "\u001B[33m";
	static final String ANSI_GREEN = "\u001B[32m";
	static final String ANSI_BLUE = "\u001B[34m";
	static final String ANSI_WHITE = "\u001B[37m";
	static final String ANSI_CYAN = "\u001B[36m";
	static final String ANSI_PURPLE = "\u001B[35m";
	static final String ANSI_LIGHT_RED = "\u001B[91m";
	static final String ANSI_LIGHT_GREEN = "\u001B[92m";
	static final String ANSI_LIGHT_YELLOW = "\u001B[93m";
	static final String ANSI_LIGHT_BLUE = "\u001B[94m";
	static final String ANSI_LIGHT_PURPLE = "\u001B[95m";
	static final String ANSI_LIGHT_CYAN = "\u001B[96m";
	static final String ANSI_RESET_COLOUR = "\u001B[39m";


	static final String ANSI_BOLD = "\u001B[1m";
	static final String ANSI_BLINK = "\u001B[5m";
	static final String ANSI_UNDERLINE = "\u001B[4m";
	static final String ANSI_BOLD_RESET = "\u001B[22m";
	static final String ANSI_BLINK_RESET = "\u001B[25m";
	static final String ANSI_UNDERLINE_RESET = "\u001B[24m";
	static final String ANSI_FORMAT_RESET = "\u001B[21;22;24;25;27;28m";
	
	static Pattern tag = Pattern.compile("<([^>]+)>");
	static Stack<String> colourStack = new Stack<String>();
	static Stack<String> formatStack = new Stack<String>();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	 * Prints the String s, coloured based on the user's settings
	 * @param s
	 */
	public static void print(String s){
		s += "<end>";//this tag is really just so I don't have to read from the last tag to the end
					 //it doesnt actually do anything 
		String s2 = "";
		Matcher matcher = tag.matcher(s);
		
		int i = 0;
		while(matcher.find()){
			s2 += s.substring(i, matcher.start(0)); // Get the plaintext
			i = matcher.end(0);
			if(ANSIEnabled){
				switch(matcher.group(1)){
				//----FORMAT----
				case "bold":
					formatStack.push(ANSI_BOLD);
					s2 += ANSI_BOLD;
					break;
				case "underline":
					formatStack.push(ANSI_UNDERLINE);
					s2 += ANSI_UNDERLINE;
					break;
				case "blink":
					formatStack.push(ANSI_BLINK);
					s2 += ANSI_BLINK;
					break;
				case "-bold":
					formatStack.push(ANSI_BOLD_RESET);
					s2 += ANSI_BOLD_RESET;
					break;
				case "-blink":
					formatStack.push(ANSI_BLINK_RESET);
					s2 += ANSI_BLINK_RESET;
					break;
				case "-underline":
					formatStack.push(ANSI_UNDERLINE_RESET);
					s2 += ANSI_UNDERLINE_RESET;
					break;
				case "fnormal":
					formatStack.push(ANSI_FORMAT_RESET);
					s2 += ANSI_FORMAT_RESET;
					break;
				case "rf":
					formatStack.pop();
					s2 += formatStack.peek();
					break;
					
					
				//----COLOUR----
				case "green":
					colourStack.push(ANSI_GREEN);
					s2 += ANSI_GREEN;
					break;
				case "lgreen":
					colourStack.push(ANSI_LIGHT_GREEN);
					s2 += ANSI_LIGHT_GREEN;
					break;
					
				case "red":
					colourStack.push(ANSI_RED);
					s2 += ANSI_RED;
					break;
				case "lred":
					colourStack.push(ANSI_LIGHT_RED);
					s2 += ANSI_LIGHT_RED;
					break;
					
				case "purple":
					colourStack.push(ANSI_PURPLE);
					s2 += ANSI_PURPLE;
					break;
				case "lpurple":
					colourStack.push(ANSI_LIGHT_PURPLE);
					s2 += ANSI_LIGHT_PURPLE;
					break;	
					
				case "cyan":
					colourStack.push(ANSI_CYAN);
					s2 += ANSI_CYAN;
					break;
				case "lcyan":
					colourStack.push(ANSI_LIGHT_CYAN);
					s2 += ANSI_LIGHT_CYAN;
					break;
					
				case "blue":
					colourStack.push(ANSI_BLUE);
					s2 += ANSI_BLUE;
					break;
				case "lblue":
					colourStack.push(ANSI_LIGHT_BLUE);
					s2 += ANSI_LIGHT_BLUE;
					break;
					
				case "yellow":
					colourStack.push(ANSI_YELLOW);
					s2 += ANSI_YELLOW;
					break;
					
				case "r":
					colourStack.pop();
					s2 += colourStack.peek();
					break;
				case "black"://Sort of a lie, this is just whatever the default text colour is
					//Who in their right mind would use a different colour though..
					colourStack.push(ANSI_RESET_COLOUR);
					s2 += ANSI_RESET_COLOUR;
					break;
				default:
					break;	
				}
			}
		}
		if(GRAPHICS_ENABLED) {
			Graphics.send(s2);
		} else {
			System.out.print(s2);
		}
	}
	
	/**
	 * Prints the String s followed by a new line, coloured based on the user's settings.
	 * @param s
	 */
	public static void println(String s){
		print(s + "\n");
	}
	
	/**
	 * Prints a new line.
	 */
	public static void println(){
		print("\n");
	}

	
	/**
	 * Reads in a line from the {@link BufferedReader}.
	 * @return the read line as a {@code String}
	 * @throws IOException
	 */
	public static String read() throws IOException{
		if(GRAPHICS_ENABLED){
			return Graphics.getLine();
		}else{
			return br.readLine();
		}
	}

	public static CommandAction getAction() throws IOException{
		if(!Commands.actions.isEmpty()) {
			return Commands.actions.poll();
		}
		if(GRAPHICS_ENABLED){
			synchronized (Commands.actions) {
				try {
					Commands.waiting = true;
					Commands.actions.wait();
					Commands.waiting = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return Commands.actions.poll();
			}
		}else{
			CommandAction a = new CommandAction();
			a.cmd = read();
			a.useString = true;
			return a;
		}
	}
	
	/**
	 * Attemts to read in an {@code Integer} within range [{@code min},{@code max}), or -1 from the {@link BufferedReader},
	 * printing {@code error} if the value is out of range or can not be converted to an {@code Integer}. Upon failure,
	 * the next line is read in, until a valid result is obtained.
	 * 
	 * @param min the minimum value for the read {@code Integer}
	 * @param max the maximum value for the read {@code Integer}
	 * @param error the error message to be displayed when out of [{@code min},{@code max}) range
	 * @return the final value input by the user
	 * @throws IOException
	 */
	public static int readInt(int min, int max, String error) throws IOException{
		if(min == max){
			throw new IllegalArgumentException("readInt arguments min and max are equal");
		}
		
		int n;
		while(true){
			try{
				n = Integer.parseInt(read());
				if(n < max && n >= min){
					break;
				}else if(n == -1){
					break;
				}else{
					IO.println(error);
				}
			}catch(NumberFormatException nfe){
				IO.println("<lred>Invalid Format! Enter an integer<r>");
			}
		}
		return n;
	}
	
	/**
	 * readInt(int min, int max, String error) with error defaulting to "Invalid input - out of range"
	 * 
	 */
	public static int readInt(int min, int max) throws IOException{
		return readInt(min, max, "<lred>Invalid input - out of range<r>");
	}
	
	public static void printHorizontalLine(){
		for(int i = 0; i < 60; i++){//Maybe give this a variable that can be adjusted?
			print("-");
		}
		println("");
	}
	
}
