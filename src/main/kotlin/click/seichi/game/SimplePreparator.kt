package click.seichi.game

import click.seichi.function.warning
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.game.event.PrepareEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class SimplePreparator(
        game: IGame,
        private val readyPlayerSet: MutableSet<UUID>
) : Preparator {

    private val players = game.players
    private var isPrepared = false

    private fun prepare() {
        if (isPrepared) {
            warning("Already complete preparation")
            return
        }
        isPrepared = true
        Bukkit.getPluginManager().callEvent(PrepareEvent(players))
    }

    override fun ready(player: Player) {
        if (!players.contains(player.uniqueId)) return
        readyPlayerSet.add(player.uniqueId)

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()
        ))

        if (players.count() == readyPlayerSet.count()) prepare()
    }

    override fun cancelReady(player: Player) {
        readyPlayerSet.remove(player.uniqueId)
        Bukkit.getPluginManager().callEvent(PlayerCancelReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()))
    }

    override fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player.uniqueId)
    }

}