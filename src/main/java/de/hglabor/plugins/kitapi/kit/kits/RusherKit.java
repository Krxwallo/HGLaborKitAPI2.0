package de.hglabor.plugins.kitapi.kit.kits;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.events.KitEvent;
import de.hglabor.plugins.kitapi.kit.settings.FloatArg;
import de.hglabor.plugins.kitapi.kit.settings.IntArg;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

/**
 * - Maximal 4 Herzen Leben
 * - Doppelte Laufgeschwindigkeit
 * - 2-fachen Schaden
 */
public class RusherKit extends AbstractKit {
    public static final RusherKit INSTANCE = new RusherKit();
    @IntArg
    private final int rusherHealth;
    @FloatArg
    private final float rusherDamageMultiplier;
    @FloatArg
    private final float rusherWalkSpeed;

    private RusherKit() {
        super("Rusher", Material.GOLDEN_SWORD);
        rusherHealth = 8; // 8 = 4 hearts
        rusherDamageMultiplier = 2.0F; // 2.0F = twice as much damage
        rusherWalkSpeed = 4.0F; // 4.0F = twice as fast
    }

    @Override
    public void onEnable(KitPlayer kitPlayer) {
        Optional<Player> playerOptional = kitPlayer.getBukkitPlayer();
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(rusherHealth);
            Bukkit.broadcastMessage("Set to " + rusherHealth);
            player.setWalkSpeed(this.rusherWalkSpeed);
            AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attackDamage != null) {
                attackDamage.setBaseValue(attackDamage.getBaseValue()* rusherDamageMultiplier);
            }
        }
    }

    @Override
    public void onDeactivation(KitPlayer kitPlayer) {
        Optional<Player> playerOptional = kitPlayer.getBukkitPlayer();
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setWalkSpeed(0.2F);
        }
    }

    @Override
    @KitEvent
    public void onPlayerAttacksLivingEntity(EntityDamageByEntityEvent event, KitPlayer attacker, LivingEntity entity) {
        event.setDamage(event.getDamage()* rusherDamageMultiplier);
    }
}
