package util;

import java.io.IOException;

import gfx.Graphics;
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
			IO.println("<cyan>Zone cleared.<r>");
			Game.zone.inFocus = false;
			Game.zone = new Zone();
			Game.zone.inFocus = true;
			if(Game.GRAPHICS_ENABLED){
				Graphics.updateZoneGraphics();
			}
			Game.targetIndex = 0;
			Game.zone.addCreature(Game.player, Game.player.position);
			Game.doTurnList();
			break;
		case "!item":
			Game.zone.addItem(commandArg);
			IO.println("<cyan>Added item.<r>");
			break;
		case "!creature":
			Game.zone.addCreature(commandArg);
			IO.println("<cyan>Added creature.<r>");
			break;
		case "!heal":
			Game.player.hp = Game.player.maxHp;
			IO.println("<cyan>Healed to max HP.<r>");
			break;
		case "!setpiece":
			IO.println("<cyan>Replaced zone with random setpiece.<r>");
			Game.zone.inFocus = false;
			Game.zone = Zone.generateSetPiece();
			Game.zone.inFocus = true;
			if(Game.GRAPHICS_ENABLED){
				Graphics.updateZoneGraphics();
			}
			Game.targetIndex = 0;
			Game.zone.addCreature(Game.player, Game.player.position);
			Game.doTurnList();
			break;
		case "!calm":
			for(Creature c : Game.zone.creatures){
				c.setHostilityTowardsPlayer(false);
			}
			IO.println("<cyan>Creatures calmed.<r>");
			break;
		case "!rename":
			Game.zone.creatures.get(Game.targetIndex).name = commandArg;
			IO.println("<cyan>Renamed target.<r>");
			break;
		default:
			IO.println("<red>Invalid debug command<r>");
		}
		
		return 0;
	}
	
}
