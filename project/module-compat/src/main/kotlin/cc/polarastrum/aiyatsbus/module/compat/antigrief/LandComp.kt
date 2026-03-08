package cc.polarastrum.aiyatsbus.module.compat.antigrief

import cc.polarastrum.aiyatsbus.core.compat.AntiGrief
import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import com.mcstarrysky.land.flag.PermBuild
import com.mcstarrysky.land.flag.PermDamageAnimals
import com.mcstarrysky.land.flag.PermDamageGolem
import com.mcstarrysky.land.flag.PermDamageMonster
import com.mcstarrysky.land.flag.PermInteract
import com.mcstarrysky.land.flag.PermPVP
import com.mcstarrysky.land.manager.LandManager
import org.bukkit.Location
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Golem
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.compat.antigrief.LandComp
 *
 * @author mical
 * @since 2026/3/6 12:52
 */
class LandComp : AntiGrief {

    override fun canBreak(player: Player, location: Location): Boolean {
        return isAdmin(player, location) || LandManager.getLand(location)?.hasPermission(player, PermBuild) ?: true
    }

    override fun canPlace(player: Player, location: Location): Boolean {
        return isAdmin(player, location) || LandManager.getLand(location)?.hasPermission(player, PermBuild) ?: true
    }

    override fun canInteract(player: Player, location: Location): Boolean {
        // TODO 还有 PermArmorStand、PermThrow、PermVehicle 要判断，这个权限被分的太细了
        return isAdmin(player, location) || LandManager.getLand(location)?.hasPermission(player, PermInteract) ?: true
    }

    override fun canInteractEntity(player: Player, entity: Entity): Boolean {
        // TODO 可能还有 PermFishingRod
        return isAdmin(player, entity.location) || LandManager.getLand(entity.location)?.hasPermission(player, PermInteract) ?: true
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return isAdmin(player, entity.location) || when (entity) {
            is Player -> LandManager.getLand(entity.location)?.hasPermission(player, PermPVP)
            is Monster -> LandManager.getLand(entity.location)?.hasPermission(player, PermDamageMonster)
            is Golem -> LandManager.getLand(entity.location)?.hasPermission(player, PermDamageGolem)
            is Animals -> LandManager.getLand(entity.location)?.hasPermission(player, PermDamageAnimals)
            else -> null
        } ?: true
    }

    override fun getAntiGriefPluginName(): String {
        return "Land"
    }

    private fun isAdmin(player: Player, location: Location): Boolean {
        return LandManager.getLand(location)?.hasAdminPerm(player) ?: true
    }

    companion object {

        @Awake(LifeCycle.ACTIVE)
        fun init() {
            AntiGriefChecker.registerNewCompatibility("Land") { LandComp() }
        }
    }
}