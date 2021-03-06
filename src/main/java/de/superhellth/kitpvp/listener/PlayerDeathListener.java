package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private Kitpvp plugin;

    public PlayerDeathListener(Kitpvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Game game = plugin.getGame(player);

        if ((game == null) || (game.getCurrentPhase() != Phase.FIGHTING) || !(game.getAlive().get(player))) {
            return;
        }
        game.broadcast(player.getDisplayName() + " died and is now out!");
        Location deathLoc = event.getEntity().getLocation();
        game.getAlive().put(player, false);
        player.setGameMode(GameMode.SPECTATOR);
        player.spigot().respawn();
        player.teleport(deathLoc);

        // check if second to last player died
        if (getAlive(game) == 1) {
            game.end(true);
        }
    }

    private int getAlive(Game game) {
        int alive = 0;
        for (Player player : game.getMembers()) {
            if (game.getAlive().get(player)) {
                alive++;
            }
        }

        return alive;
    }

}
