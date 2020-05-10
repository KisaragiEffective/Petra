package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import click.seichi.game.PlayerLocator
import click.seichi.game.SimpleGameStarter
import click.seichi.game.command.ReadyCommand
import click.seichi.game.listener.PlayerConnectionListener
import click.seichi.game.listener.PlayerGameListener
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.generator.ChunkGenerator
import org.jetbrains.exposed.sql.Table

/**
 * @author tar0ss
 */
class Petra : Plugin() {
    private lateinit var stage: Stage

    // 参加者
    private val players = mutableSetOf<Player>()

    private val gameStarter = SimpleGameStarter(players)
    private val playerLocator = PlayerLocator(players, gameStarter)


    override val configurations: Array<Config> = arrayOf(
            PetraConfig
    )
    override val listeners: Array<Listener> = arrayOf(
            PlayerConnectionListener(playerLocator),
            PlayerGameListener()
    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(
            "ready" to ReadyCommand(gameStarter)
    )
    override val tables: Array<Table> = arrayOf(
    )

    override fun onEnable() {
        super.onEnable()
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
        logger.fine("Load Stage \"${stage.key}\"")
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return StageChunkGenerator(stage)
    }
}