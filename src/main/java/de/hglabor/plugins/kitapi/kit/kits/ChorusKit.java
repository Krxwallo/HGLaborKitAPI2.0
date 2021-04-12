package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.KitApi;
import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;
import java.util.Random;

public class ChorusKit extends AbstractKit {
    public static final ChorusKit INSTANCE = new ChorusKit();
    @IntArg
    private final int maxUses;
    @FloatArg(min = 0.0F)
    private final float cooldown;
    @FloatArg
    private final float minDistance;
    @FloatArg
    private final float maxDistance;

    private ChorusKit() {
        super("Chorus", Material.CHORUS_FRUIT);
        setMainKitItem(getDisplayMaterial());
        cooldown = 15F;
        maxUses = 3;
        minDistance = 5;
        maxDistance = 15;
    }

    @Override
    public void onDeactivation(KitPlayer kitPlayer) {
        Player player = Bukkit.getPlayer(kitPlayer.getUUID());
        if (player != null) {
            if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                player.removePotionEffect(PotionEffectType.LEVITATION);
            }
        }
    }

    @KitEvent
    @Override
    public void onPlayerRightClickKitItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.teleport(randomNearbyLocation(player.getLocation()));
        KitApi.getInstance().checkUsesForCooldown(player, this, maxUses);
    }

    private Location randomNearbyLocation(Location location) {
        return location.add(randomDouble(minDistance, maxDistance), 0, randomDouble(minDistance, maxDistance)).toHighestLocation().add(0, 1, 0);
    }

    private double randomDouble(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    @Override
    public float getCooldown() {
        return cooldown;
    }
}
