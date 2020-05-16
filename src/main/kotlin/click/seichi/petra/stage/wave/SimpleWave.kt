package click.seichi.petra.stage.wave

import click.seichi.function.debug
import click.seichi.petra.stage.Wave
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.util.Timer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class SimpleWave(private val monsterNum: Int, seconds: Int) : Wave {

    private val subject: Subject<Unit> = PublishSubject.create()

    private lateinit var spawnProxy: SpawnProxy
    private lateinit var world: World

    private val timer = Timer(
            seconds,
            onStart = {
                debug("start wave")
                spawn(monsterNum)
            },
            onComplete = {
                subject.onNext(Unit)
            }
    )

    override fun start(spawnProxy: SpawnProxy, world: World) {
        this.spawnProxy = spawnProxy
        this.world = world
        timer.start()
    }

    private fun spawn(n: Int) {
        (1..n).forEach { _ ->
            spawnProxy.spawn(world, EntityType.ZOMBIE.entityClass as Class<out Entity>, Consumer { e: Entity ->
                val zombie = e as Zombie
                zombie.setShouldBurnInDay(false)
            })
        }
    }

    override fun endAsObservable(): Observable<Unit> = subject
}