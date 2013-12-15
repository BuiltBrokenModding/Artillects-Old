package artillects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import artillects.tile.TileEntityTeleporterAnchor;

public class TeleportManager {

	private static HashSet<TileEntityTeleporterAnchor> teleporters = new HashSet<TileEntityTeleporterAnchor>();
	
	public static void addAnchor(TileEntityTeleporterAnchor anch) {
		teleporters.add(anch);
	}
	
	public static void remAnchor(TileEntityTeleporterAnchor anch) {
		teleporters.remove(anch);
	}
	
	public static HashSet<TileEntityTeleporterAnchor> getConnectedAnchors() {
		return teleporters;
	}
	
	public static boolean contains(TileEntityTeleporterAnchor anch) {
		return teleporters.contains(anch);
	}
}
