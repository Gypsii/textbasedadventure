package util;

import java.io.IOException;

import main.Game;
import main.Zone;
import creatures.Creature;

public class TestEnvironment {
	
	public static double debugCommands(String command) throws IOException{
		String[] split = command.split(" ");
		String commandArg = split.length > 1 ? split[1] : "";
		String commandStart = split[0];
		switch(commandStart){
		case "!clear":
			Game.zone = new Zone();
			Text.viewZone();
			Game.targetIndex = 0;
			Game.doTurnList();
			IO.print("<cyan>Zone cleared.<r>");
			break;
		case "!item":
			Game.zone.addItem(commandArg);
			IO.print("<cyan>Added item.<r>");
			break;
		case "!creature":
			Game.zone.addCreature(commandArg);
			IO.print("<cyan>Added creature.<r>");
			break;
		case "!heal":
			Game.player.hp = Game.player.maxHp;
			IO.print("<cyan>Healed to max HP.<r>");
			break;
		case "!setpiece":
			Game.zone = Zone.generateSetPiece();
			IO.print("<cyan>Replaced zone with random setpiece.<r>");
			Text.viewZone();
			Game.targetIndex = 0;
			Game.doTurnList();
			break;
		case "!calm":
			for(Creature c : Game.zone.creatures){
				c.setHostilityTowardsPlayer(false);
			}
			IO.print("<cyan>Creatures calmed.<r>");
			break;
		case "!rename":
			Game.zone.creatures.get(Game.targetIndex).name = commandArg;
			IO.print("<cyan>Renamed target.<r>");
			break;
		default:
			IO.print("<red>Invalid debug command<r>");
		}
		
		return 0;
	}
	
}
