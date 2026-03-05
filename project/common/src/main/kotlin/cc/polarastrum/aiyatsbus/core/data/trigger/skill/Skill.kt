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
 * 技能触发器
 *
 * 由玩家动作触发的技能型附魔逻辑，支持音效、粒子、冷却、动作限定等配置。
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

    /** 是否要求下蹲才能触发技能 */
    fun isShiftNeeded(): Boolean {
        return shiftNeeded ?: Aiyatsbus.api().getSkillHandler().getSettings().shiftNeeded
    }

    /** 下蹲时是否忽略技能触发 */
    fun isShiftIgnored(): Boolean {
        return shiftIgnored ?: Aiyatsbus.api().getSkillHandler().getSettings().shiftIgnored
    }

    /** 获取当前动作触发类型（若未配置则读取全局默认） */
    fun getAction(): ActionType {
        return action ?: Aiyatsbus.api().getSkillHandler().getSettings().action
    }

    /** 是否启用冷却提示/检查 */
    fun isEnableCooldown(): Boolean {
        return sendCooldown ?: Aiyatsbus.api().getSkillHandler().getSettings().enableCooldown
    }

    /** 获取冷却变量名 */
    fun getCooldownVar(): String {
        return cooldownVar ?: Aiyatsbus.api().getSkillHandler().getSettings().cooldownVar
    }

    /** 播放技能音效（若已配置） */
    fun playSound(player: Player) {
        sound.ifPresent { it.play(player, 100f, 10f) }
    }

    /** 生成技能粒子效果（若已配置） */
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
