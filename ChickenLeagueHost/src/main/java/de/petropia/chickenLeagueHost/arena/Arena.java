package de.petropia.chickenLeagueHost.arena;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.chickenbats.BatManager;
import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;
import de.petropia.chickenLeagueHost.firework.GoalFireWork;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.scoreboard.ScoreboardManager;
import de.petropia.chickenLeagueHost.song.SongList;
import de.petropia.chickenLeagueHost.specialItem.SpecialItemManager;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.team.TeamSelectGUI;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import de.petropia.chickenLeagueHost.util.MessageUtil;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class Arena {

    private static final List<Arena> ARENAS = new ArrayList<>();

    private final String name;
    private GameState gamestate;
    private final ArenaMode arenaMode;
    private final List<Player> players = new ArrayList<>();
    private final World world;
    private int maxPlayer;
    private final ChickenLeagueTeam team1;
    private final ChickenLeagueTeam team2;
    private final Location[] team1Spawns;
    private final Location[] team2Spawns;
    private final Location middle;
    private final ChickenLeagueBall ball;
    private boolean registered = false;
    private GameTime gameTime;
    private final ScoreboardManager scoreboradManager;
    private final TeamSelectGUI teamSelectGui;
    private final BatManager batManager = new BatManager();
    private final SpecialItemManager specialItemManager;

    /**
     * Create and register arena right after new instance
     *
     * @param mode Mode of arena
     */
    public Arena(ArenaMode mode) {
        setGamestate(GameState.WAITING);
        this.name = getRandomString();
        this.arenaMode = mode;

        //Cloning worlds
        MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        MVWorldManager worldManager = mvCore.getMVWorldManager();
        if (mode == ArenaMode.FIVE_VS_FIVE) {
            worldManager.cloneWorld("FIVE_VS_FIVE", name);
            maxPlayer = 5 + 5;
        }
        if (mode == ArenaMode.THREE_VS_THREE) {
            worldManager.cloneWorld("THREE_VS_THREE", name);
            maxPlayer = 3 + 3;
        }
        if (mode == ArenaMode.ONE_VS_ONE) {
            worldManager.cloneWorld("ONE_VS_ONE", name);
            maxPlayer = 1 + 1;
        }
        world = Bukkit.getWorld(name);
        applyGameRules();

        //init teams
        team1 = new ChickenLeagueTeam(maxPlayer / 2, Component.text("Team 1").color(NamedTextColor.BLUE), teamGoalCoord("X1", 1), teamGoalCoord("X2", 1), teamGoalCoord("Z1", 1), teamGoalCoord("Z2", 1));
        team2 = new ChickenLeagueTeam(maxPlayer / 2, Component.text("Team 2").color(NamedTextColor.RED), teamGoalCoord("X1", 2), teamGoalCoord("X2", 2), teamGoalCoord("Z1", 2), teamGoalCoord("Z2", 2));

        //spawns init
        team2Spawns = loadSpawns(2);
        team1Spawns = loadSpawns(1);
        middle = loadMiddleLocation();

        //misc init
        ball = new ChickenLeagueBall(this);
        scoreboradManager = new ScoreboardManager(this);
        teamSelectGui = new TeamSelectGUI(this);
        specialItemManager = new SpecialItemManager(this);

        //register arena in db and Arenas list
        registerArena();
    }

    /**
     * Get the coordinates of the team goal
     *
     * @param coordinate Coordinate like x1, z2 etc
     * @param teamNumber Number of team (1 or 2)
     * @return Coordinate as int
     */
    private int teamGoalCoord(String coordinate, int teamNumber) {
        return Constants.config.getInt(arenaMode.name() + ".Team" + teamNumber + ".Goal." + coordinate);
    }

    /**
     * Lead the middle of arena from config
     *
     * @return Location of the middle
     */
    private Location loadMiddleLocation() {
        final double x = Constants.config.getDouble(arenaMode.name() + ".Middle.X");
        final double y = Constants.config.getDouble(arenaMode.name() + ".Middle.Y");
        final double z = Constants.config.getDouble(arenaMode.name() + ".Middle.Z");
        final float yaw = Constants.config.getLong(arenaMode.name() + ".Middle.Yaw");
        final float pitch = Constants.config.getLong(arenaMode.name() + ".Middle.Pitch");
        final Location location = new Location(world, x, y, z, yaw, pitch);
        return location;
    }

    /**
     * Loading spawns for team
     *
     * @param team Team number (1 or 2)
     * @return Array of spawn locations
     */
    private Location[] loadSpawns(int team) {
        final Location[] spawns = new Location[maxPlayer / 2];
        for (int i = 0; i < maxPlayer / 2; i++) {
            int spawn = i + 1;
            final double x = Constants.config.getDouble(arenaMode.name() + ".Team" + team + ".Spawn" + spawn + ".X");
            final double y = Constants.config.getDouble(arenaMode.name() + ".Team" + team + ".Spawn" + spawn + ".Y");
            final double z = Constants.config.getDouble(arenaMode.name() + ".Team" + team + ".Spawn" + spawn + ".Z");
            final float yaw = Constants.config.getLong(arenaMode.name() + ".Team" + team + ".Spawn" + spawn + ".Yaw");
            final float pitch = Constants.config.getLong(arenaMode.name() + ".Team" + team + ".Spawn" + spawn + ".Pitch");
            final Location location = new Location(world, x, y, z, yaw, pitch);
            spawns[i] = location;
        }
        return spawns;
    }

    public World getWorld() {
        return world;
    }

    /**
     * Add arena to arenas list and register in db
     */
    private void registerArena() {
        ARENAS.add(this);
        MySQLManager.addArena(this);
        registered = true;
    }

    /**
     * Assign every player who doesnt have a team a team and kick the rest
     */
    public void assignPlayersToTeams() {
        List<Player> playerWithoutTeams = new ArrayList<>(players);
        for (Player player : team1.getPlayers()) {
            playerWithoutTeams.remove(player);
        }
        for (Player player : team2.getPlayers()) {
            playerWithoutTeams.remove(player);
        }
        Collections.shuffle(playerWithoutTeams);
        for (Player player : playerWithoutTeams) {
            if (team1.addPlayer(player)) {
                continue;
            }
            if (team2.addPlayer(player)) {
                continue;
            }
            Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Du konntest keinen Team zugeordnet werden!").color(NamedTextColor.RED));
            CloudNetAdapter.sendPlayerToLobbyTask(player);
        }
    }

    /**
     * Apply gamerules to the arena world
     */
    private void applyGameRules() {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_INSOMNIA, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        world.setTime(12000);
    }

    /**
     * Add a player to the arena and fire PlayerJoinArenaEvent
     *
     * @param player Player who sould join
     * @return true is join is sucess<
     */
    public boolean addPlayer(Player player) {
        if (players.size() == maxPlayer) {
            return false;
        }
        if (gamestate == GameState.WAITING) {
            setGamestate(GameState.STARTING);
        }
        players.add(player);
        scoreboradManager.addPlayer(player);
        PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return true;
    }

    /**
     * Remove player from arena and send back to lobby
     *
     * @param player Player to remove
     */
    public void removePlayer(Player player) {
        if (players.contains(player)) {
            PlayerQuitArenaEvent event = new PlayerQuitArenaEvent(this, player);
            Bukkit.getServer().getPluginManager().callEvent(event);
            players.remove(player);
            scoreboradManager.removePlayer(player);
            team1.removePlayer(player);
            team2.removePlayer(player);
        }
    }

    /**
     * Check if player is present in arena
     *
     * @param player Player to check
     * @return true if present
     */
    public boolean isPlayerPresent(Player player) {
        return players.contains(player);
    }

    /**
     * Set the winnerteam of arena and trigger firework and gamestatechange
     *
     * @param team Team who won
     */
    public void setWinner(@Nullable ChickenLeagueTeam team) {
        if (gamestate != GameState.INGAME) {
            return;
        }
        scoreboradManager.setWinnerTeam(team);
        final Component subtitle = Component.text("hat gewonnen").color(NamedTextColor.GRAY);
        Component title = Component.text("Niemand").color(NamedTextColor.RED);
        Times times = Times.times(Duration.ofMillis(300), Duration.ofMillis(4000), Duration.ofMillis(300));
        if (team != null) {
            title = team.getName();
        }
        new GoalFireWork(this, team, true);
        ball.kill();
        playSong();
        MessageUtil.INSTANCE.broadcastTitle(this, Title.title(title, subtitle, times), Sound.sound(org.bukkit.Sound.ITEM_GOAT_HORN_PLAY.key(), Sound.Source.NEUTRAL, 200F, 1F));
        setGamestate(GameState.ENDING);
    }

    /**
     * Play random song from {@link SongList} for all players
     */
    private void playSong() {
        Random random = new Random();
        int randInt = random.nextInt(SongList.INSTANCE.getSongs().size());
        Song song = SongList.INSTANCE.getSongs().get(randInt);
        RadioSongPlayer radio = new RadioSongPlayer(song);
        players.forEach(radio::addPlayer);
        radio.setPlaying(true);
    }

    /**
     * Generate a random 5 char String for naming and as id
     *
     * @return random string
     */
    private String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }

    /**
     * Delete Arena from db and arena world
     */
    public void delete() {
        for (Player player : players) {
            CloudNetAdapter.sendPlayerToLobbyTask(player);
        }
        MySQLManager.deleteArena(name);
        ARENAS.remove(this);
        scoreboradManager.deleteScordboardManager();
        Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
            deleteWorld();
        }, 20);
    }

    /**
     * Remove the arena world from Bukkit and server directory
     */
    private void deleteWorld() {
        MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        MVWorldManager worldManager = mvCore.getMVWorldManager();
        worldManager.deleteWorld(name);
    }

    /**
     * Teleport every player to their spawnpoint based on teams
     */
    public void teleportToSpawnPoints() {
        for (int i = 0; i < team1.getPlayers().length; i++) {
            Player player = team1.getPlayers()[i];
            if (player == null) {
                continue;
            }
            player.teleport(team1Spawns[i]);
        }
        for (int i = 0; i < team2.getPlayers().length; i++) {
            Player player = team2.getPlayers()[i];
            if (player == null) {
                continue;
            }
            player.teleport(team2Spawns[i]);
        }
        players.forEach(p -> p.setBedSpawnLocation(middle));
    }

    public String getName() {
        return name;
    }

    public GameState getGameState() {
        return gamestate;
    }

    /**
     * Set the gamestate and trigger GameStateChangeEvent
     *
     * @param gamestate
     */
    public void setGamestate(GameState gamestate) {
        GameStateChangeEvent event = new GameStateChangeEvent(this, this.gamestate, gamestate);
        Bukkit.getServer().getPluginManager().callEvent(event);
        this.gamestate = gamestate;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ArenaMode getArenaMode() {
        return arenaMode;
    }

    public static List<Arena> getArenas() {
        return ARENAS;
    }

    public int getMaxPlayers() {
        return maxPlayer;
    }

    public Location getMiddle() {
        return middle;
    }

    public ChickenLeagueBall getBall() {
        return ball;
    }

    public ChickenLeagueTeam getTeam1() {
        return team1;
    }

    public ChickenLeagueTeam getTeam2() {
        return team2;
    }

    public boolean isRegistered() {
        return registered;
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(GameTime gameTime) {
        this.gameTime = gameTime;
    }

    public TeamSelectGUI getTeamSelectGui() {
        return teamSelectGui;
    }

    public BatManager getBatManager() {
        return batManager;
    }

    public SpecialItemManager getSpecialItemManager() {
        return specialItemManager;
    }
}
