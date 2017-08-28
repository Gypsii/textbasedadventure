package util;

import java.io.IOException;

import main.Game;
import main.Zone;
import creatures.Creature;

public class TestEnvironment {
	
	public static double debugCommands() throws IOException{
		String command = "";
		IO.print("<cyan>Enter debug command:<r>");
		command = IO.read();
		switch(command){
		case "clear":
			Game.zone = new Zone();
			Text.viewZone();
			Game.targetIndex = 0;
			Game.doTurnList();
			IO.print("<cyan>Zone cleared.<r>");
			break;
		case "item":
			IO.print("<cyan>Enter item ID:<r>");
			Game.zone.addItem(IO.read());
			IO.print("<cyan>Added item.<r>");
			break;
		case "creature":
			IO.print("<cyan>Enter creature ID:<r>");
			Game.zone.addCreature(IO.read());
			IO.print("<cyan>Added creature.<r>");
			break;
		case "heal":
			Game.player.hp = Game.player.maxHp;
			IO.print("<cyan>Healed to max HP.<r>");
			break;
		case "setpiece":
			Game.zone = Zone.generateSetPiece();
			IO.print("<cyan>Replaced zone with random setpiece.<r>");
			Text.viewZone();
			Game.targetIndex = 0;
			Game.doTurnList();
			break;
		case "calm":
			for(Creature c : Game.zone.creatures){
				c.setHostilityTowardsPlayer(false);
			}
			IO.print("<cyan>Creatures calmed.<r>");
			break;
		case "rename":
			IO.print("<cyan>Enter new name:<r>");
			Game.zone.creatures.get(Game.targetIndex).name = IO.read();
			IO.print("<cyan>Renamed target.<r>");
			break;
		default:
			IO.print("<red>Invalid debug command<r>");
		}
		
		return 0;
	}
	
}
