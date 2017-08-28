package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import main.Game;

public class BotInputReader extends BufferedReader {

	public BotInputReader(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	
	public String readLine() throws IOException {
		if(Game.player.name.equals("bot")){
			br.readLine();
			String action = Bot.takeTurn();
			IO.println("<cyan>" + action + "<r>");
			return action;
		}
        return super.readLine();
    }

}
