package cc.polarastrum.aiyatsbus.module.compat.antigrief

import cc.polarastrum.aiyatsbus.core.compat.AntiGrief
import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker
import codes.wasabi.xclaim.api.Claim
import codes.wasabi.xclaim.api.enums.EntityGroup
import codes.wasabi.xclaim.api.enums.Permission
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.compat.antigrief.XClaimComp
 *
 * @author mical
 * @since 2026/3/6 12:30
 */
class XClaimComp : AntiGrief {

    override fun canPlace(player: Player, location: Location): Boolean {
        return Claim.getByChunk(location.chunk)
            ?.getUserPermission(player, Permission.BUILD)
            ?: true
    }

    override fun canBreak(player: Player, location: Location): Boolean {
        return Claim.getByChunk(location.chunk)
            ?.getUserPermission(player, Permission.BREAK)
            ?: true
    }

    override fun canInteract(player: Player, location: Location): Boolean {
        return Claim.getByChunk(location.chunk)
            ?.getUserPermission(player, Permission.INTERACT)
            ?: true
    }

    override fun canInteractEntity(player: Player, entity: Entity): Boolean {
        return Claim.getByChunk(entity.location.chunk)
            ?.getUserPermission(player, Permission.INTERACT)
            ?: true
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        val og = EntityGroup.values().firstOrNull { it.contains(entity) } ?: return true
        val per = when (og) {
            EntityGroup.FRIENDLY -> Permission.ENTITY_DAMAGE_FRIENDLY
            EntityGroup.HOSTILE -> Permission.ENTITY_DAMAGE_HOSTILE
            EntityGroup.VEHICLE -> Permission.ENTITY_DAMAGE_VEHICLE
            EntityGroup.NOT_ALIVE -> Permission.ENTITY_DAMAGE_NL
            EntityGroup.MISC -> Permission.ENTITY_DAMAGE_MISC
        }
        return Claim.getByChunk(entity.chunk)
            ?.getUserPermission(player, per)
            ?: true
    }

    override fun getAntiGriefPluginName(): String {
        return "XClaim"
    }

    companion object {

        @Awake(LifeCycle.ACTIVE)
        fun init() {
            AntiGriefChecker.registerNewCompatibility(XClaimComp())
        }
    }
}