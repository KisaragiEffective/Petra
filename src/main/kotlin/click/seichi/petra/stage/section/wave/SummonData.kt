package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.petra.stage.raider.ISummoner

/**
 * @author tar0ss
 */
data class SummonData(
        val summoner: ISummoner,
        val message: Message
)