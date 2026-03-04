package cc.polarastrum.aiyatsbus.core.data.trigger.skill

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.Trigger
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XSound
import taboolib.library.xseries.particles.XParticle
import java.util.*

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.core.data.trigger.skill.Skill
 *
 * @author mical
 * @since 2026/1/27 17:00
 */
data class Skill @JvmOverloads constructor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    val scriptType: ScriptType = ScriptType.valueOf(root.getString("type")?.uppercase() ?: "KETHER"),
    override val handle: String = root.getString("handle") ?: "",
    private val sound: Optional<XSound> = XSound.of(root.getString("sound") ?: ""),
    private val particleType: Optional<XParticle> = XParticle.of(root.getString("particle.type")),
    private val particleAmount: Int = root.getInt("particle.amount", 1),
    private val shiftNeeded: Boolean? = "shift-needed".let { if (root.contains(it)) root.getBoolean(it) else null },
    private val shiftIgnored: Boolean? = "shift-ignored".let { if (root.contains(it)) root.getBoolean(it) else null },
    private val action: ActionType? = root.getString("action")?.uppercase()?.let { ActionType.valueOf(it) },
    private val cooldownVar: String? = root.getString("cooldown.name"), // 这里是变量名
    private val sendCooldown: Boolean? = "cooldown.enable".let { if (root.contains(it)) root.getBoolean(it) else null },
    override val priority: Int = root.getInt("priority", 0) // 按理来说应该是只有一个技能
) : Trigger(root, enchant, scriptType, handle, priority, TriggerType.SKILL) {

    fun isShiftNeeded(): Boolean {
        return shiftNeeded ?: Aiyatsbus.api().getSkillHandler().getSettings().shiftNeeded
    }

    fun isShiftIgnored(): Boolean {
        return shiftIgnored ?: Aiyatsbus.api().getSkillHandler().getSettings().shiftIgnored
    }

    fun getAction(): ActionType {
        return action ?: Aiyatsbus.api().getSkillHandler().getSettings().action
    }

    fun isEnableCooldown(): Boolean {
        return sendCooldown ?: Aiyatsbus.api().getSkillHandler().getSettings().enableCooldown
    }

    fun getCooldownVar(): String {
        return cooldownVar ?: Aiyatsbus.api().getSkillHandler().getSettings().cooldownVar
    }

    fun playSound(player: Player) {
        sound.ifPresent { it.play(player, 100f, 10f) }
    }

    fun spawnParticle(player: Player) {
        particleType.ifPresent {
            player.spawnParticle(
                it.get() ?: return@ifPresent,
                player.location.add(0.0, 2.0, 0.0),
                particleAmount
            )
        }
    }
}