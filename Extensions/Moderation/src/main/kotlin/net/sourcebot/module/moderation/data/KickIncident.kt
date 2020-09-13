package net.sourcebot.module.moderation.data

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.sourcebot.api.response.WarningResponse

class KickIncident(
    private val sender: Member,
    private val target: Member,
    private val reason: String
) : SourceIncident(Type.KICK) {
    override fun execute(): Throwable? =
        try {
            target.kick(reason).complete(); null
        } catch (ex: Throwable) {
            ex
        }

    override fun sendLog(channel: TextChannel): Long {
        val case = computeId()
        val embed = WarningResponse(
            "Kick - Case #$case",
            """
                **Kicked By:** ${"%#s".format(sender.user)} (${sender.id})
                **Kicked User:** ${"%#s".format(target.user)} (${target.id})
                **Reason:**: $reason
            """.trimIndent()
        ).asEmbed(target.user)
        channel.sendMessage(embed).queue()
        return case
    }
}