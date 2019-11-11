package xyz.acrylicstyle.bedwars.tasks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.*;

import java.util.UUID;

public class LobbyTask extends BukkitRunnable {
    private static int countdown = 30;

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() <= 0) {
            BedWars.startedLobbyTask = false;
            countdown = 40;
            this.cancel();
            return;
        }
        BedWars.startedLobbyTask = true;
        if (countdown == 0) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().removeItem(Utils.getModifierItem());
                BedWars.status.put(player.getUniqueId(), PlayerStatus.ALIVE);
                if (BedWars.team.values(Team.RED).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.RED);
                } else if (BedWars.team.values(Team.BLUE).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.BLUE);
                } else if (BedWars.team.values(Team.YELLOW).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.YELLOW);
                } else if (BedWars.team.values(Team.GREEN).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.GREEN);
                } else if (BedWars.team.values(Team.WHITE).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.WHITE);
                } else if (BedWars.team.values(Team.BLACK).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.BLACK);
                } else if (BedWars.team.values(Team.PINK).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.PINK);
                } else if (BedWars.team.values(Team.AQUA).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.AQUA);
                } else throw new StackOverflowError("Players count has overflowed.");
                player.setPlayerListName(BedWars.team.get(player.getUniqueId()).color + player.getName());
                player.setDisplayName(BedWars.team.get(player.getUniqueId()).color + "[" + BedWars.team.get(player.getUniqueId()).name().toUpperCase() + "] " + ChatColor.GRAY + player.getName());
                player.getInventory().setBoots(Utils.getColoredLeatherArmor(Material.LEATHER_BOOTS, BedWars.team.get(player.getUniqueId())));
                player.getInventory().setLeggings(Utils.getColoredLeatherArmor(Material.LEATHER_LEGGINGS, BedWars.team.get(player.getUniqueId())));
                player.getInventory().setChestplate(Utils.getColoredLeatherArmor(Material.LEATHER_CHESTPLATE, BedWars.team.get(player.getUniqueId())));
                player.getInventory().setHelmet(Utils.enchantTool(Utils.getColoredLeatherArmor(Material.LEATHER_HELMET, BedWars.team.get(player.getUniqueId())),  Enchantment.WATER_WORKER, 1));
                player.getInventory().addItem(Utils.unbreakable(Material.WOOD_SWORD));
                Constants.wearingArmor.add(player.getUniqueId(), PlayerArmor.LEATHER);
                if (Utils.teleportMadness) player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16));
                if (Utils.speed != 0) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, Utils.speed-1, false));
                if (Utils.onePunchOneKill) player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 127, false));
                if (Utils.strength != 0) player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, Utils.strength-1, false));
            });
            if (Utils.respawnTime != 5 || Utils.crafting || !Utils.blockProtection || Utils.eventTime != 1 || Utils.maxTeamUpgrades || Utils.onePunchOneKill || Utils.strength != 0) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Game modifiers have been applied to this game!");
                if (Utils.respawnTime != 5) Bukkit.broadcastMessage(ChatColor.GOLD + " - Respawn Time: " + ChatColor.YELLOW + Utils.respawnTime);
                if (Utils.crafting) Bukkit.broadcastMessage(ChatColor.GOLD + " - Enabled crafting");
                if (!Utils.blockProtection) Bukkit.broadcastMessage(ChatColor.GOLD + " - Disable Block Protection");
                if (Utils.eventTime != 1) Bukkit.broadcastMessage(ChatColor.GOLD + " - Event Times: " + ChatColor.YELLOW + Utils.eventTime + "x");
                if (Utils.maxTeamUpgrades) Bukkit.broadcastMessage(ChatColor.GOLD + " - Max Team Upgrades");
                if (Utils.strength != 0) Bukkit.broadcastMessage(ChatColor.GOLD + " - Strength: " + ChatColor.YELLOW + Utils.strength + (Utils.strength >= 127 ? (ChatColor.GOLD + " (One Punch, One Kill)") : ""));
                if (Utils.speed != 0) Bukkit.broadcastMessage(ChatColor.GOLD + " - Speed: " + ChatColor.YELLOW + "Speed " + Utils.speed);
                if (Utils.teleportMadness) Bukkit.broadcastMessage(ChatColor.GOLD + " - Teleport Madness");
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Objective objective = Utils.getObjective(player.getUniqueId());
            UUID uuid = player.getUniqueId();
            Utils.setScoreReplace("Map: " + ChatColor.GREEN + BedWars.map.getString("name", "???"), 4, objective, uuid);
            Utils.setScoreReplace("Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + "/" + (Utils.teamSize*8), 3, objective, uuid);
            Utils.setScoreReplace(" ", 2, objective, uuid);
            Utils.setScoreReplace(ChatColor.YELLOW + BedWars.config.getString("domain", "www.acrylicstyle.xyz"), -1, objective, uuid);
            if (Bukkit.getOnlinePlayers().size() < Utils.minimumPlayers) {
                Utils.setScoreReplace(ChatColor.WHITE + "Waiting...", 1, objective, uuid);
                Utils.setScoreReplace("", 0, objective, uuid);
                return;
            }
            Utils.setScoreReplace("Starting in " + ChatColor.GREEN + Utils.secondsToTime(countdown), 1, objective, uuid);
            Utils.setScoreReplace("", 0, objective, uuid);
            player.setScoreboard(BedWars.scoreboards.get(player.getUniqueId()));
            if (countdown == 20) {
                player.sendTitle(ChatColor.YELLOW + "20", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 20 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 10) {
                player.sendTitle(ChatColor.YELLOW + "10", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 10 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 5) {
                player.sendTitle(ChatColor.YELLOW + "5", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 5 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 4) {
                player.sendTitle(ChatColor.YELLOW + "4", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 4 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 3) {
                player.sendTitle(ChatColor.RED + "3", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 3 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 2) {
                player.sendTitle(ChatColor.RED + "2", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 2 + ChatColor.YELLOW + " seconds!");
            } else if (countdown == 1) {
                player.sendTitle(ChatColor.RED + "1", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
                player.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + 1 + ChatColor.YELLOW + " second!");
            } else if (countdown == 0) {
                player.sendTitle("", "");
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(Utils.getConfigUtils().getTeamSpawnPoint(BedWars.team.get(player.getUniqueId())));
            }
        }
        if (countdown == 0) {
            if (BedWars.team.values(Team.RED).size() > 0) BedWars.aliveTeam.add(Team.RED);
            if (BedWars.team.values(Team.BLUE).size() > 0) BedWars.aliveTeam.add(Team.BLUE);
            if (BedWars.team.values(Team.YELLOW).size() > 0) BedWars.aliveTeam.add(Team.YELLOW);
            if (BedWars.team.values(Team.GREEN).size() > 0) BedWars.aliveTeam.add(Team.GREEN);
            if (BedWars.team.values(Team.WHITE).size() > 0) BedWars.aliveTeam.add(Team.WHITE);
            if (BedWars.team.values(Team.BLACK).size() > 0) BedWars.aliveTeam.add(Team.BLACK);
            if (BedWars.team.values(Team.PINK).size() > 0) BedWars.aliveTeam.add(Team.PINK);
            if (BedWars.team.values(Team.AQUA).size() > 0) BedWars.aliveTeam.add(Team.AQUA);
            Utils.getConfigUtils().getSemiMiddleGenerators().forEach(location -> {
                Hologram hologram = Utils.addHologram(location.toString(), location.clone().add(0, 5, 0));
                hologram.appendTextLine(ChatColor.YELLOW + "Tier " + ChatColor.RED + "I");
                hologram.appendTextLine("" + ChatColor.AQUA + ChatColor.BOLD + "Diamond");
                hologram.appendTextLine(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + "0" + ChatColor.YELLOW + " seconds");
                hologram.appendItemLine(new ItemStack(Material.DIAMOND_BLOCK));
            });
            Utils.getConfigUtils().getMiddleGenerators().forEach(location -> {
                Hologram hologram = Utils.addHologram(location.toString(), location.clone().add(0, 5, 0));
                hologram.appendTextLine(ChatColor.YELLOW + "Tier " + ChatColor.RED + "I");
                hologram.appendTextLine("" + ChatColor.GREEN + ChatColor.BOLD + "Emerald");
                hologram.appendTextLine(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + "0" + ChatColor.YELLOW + " seconds");
                hologram.appendItemLine(new ItemStack(Material.EMERALD_BLOCK));
            });
            GameTask gameTask = new GameTask();
            gameTask.runTaskTimer(Utils.getInstance(), 0, 20);
            Utils.setGameTask(gameTask);
            GeneratorTask generatorTask = new GeneratorTask();
            generatorTask.runTask(Utils.getInstance());
            EventTask eventTask = new EventTask();
            eventTask.scheduleEvents();
            eventTask.runTaskTimer(Utils.getInstance(), 0, 20);
            ShopNPCTask npcTask = new ShopNPCTask();
            npcTask.runTask(Utils.getInstance());
            this.cancel();
            countdown = 30;
            return;
        }
        countdown--;
    }

    public static void setCountdown(int countdown) { // why you don't want to wait?
        LobbyTask.countdown = countdown;
    }
}
