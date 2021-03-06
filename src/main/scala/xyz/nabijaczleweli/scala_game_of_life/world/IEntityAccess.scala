package xyz.nabijaczleweli.scala_game_of_life.world

import javax.sound.sampled.{AudioInputStream, AudioSystem}

import xyz.nabijaczleweli.scala_game_of_life.engine.GameRenderer
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.{SoundRegistry, ParticleRegistry}
import xyz.nabijaczleweli.scala_game_of_life.entity.{AxisAlignedBB, Entity, EntityFX, EntityPlayer}

import scala.ref.{Reference, WeakReference}

/** @author Jędrzej
  * @since  10.05.14
  */
trait IEntityAccess {
	final def init(world: Reference[ICellAccess]) {
		player = new EntityPlayer(world, new WeakReference[IEntityAccess](this), GameRenderer.cellsInXAxis / 2 * 10000, GameRenderer.cellsInYAxis / 2 * 10000)
	}

	var player: EntityPlayer = null
	var particles            = Vector[EntityFX]()
	var entities             = Vector[Entity]()

	def getCollidingBoundingBoxes(entity: Entity, aabb: AxisAlignedBB, world: Reference[ICellAccess]): List[AxisAlignedBB] = List[AxisAlignedBB]()

	/** Plays a sound at the entity's position. Args: entity, sound, volume (relative to 1.0), and frequency (or pitch, also relative to 1.0). */
	def playSoundAtEntity(entity: Entity, soundName: String, volume: Float, pitch: Float, world: ICellAccess) {
		assume(!entity.isInstanceOf[EntityFX] && hasEntity(entity))
		playSound(entity.posX, entity.posY, soundName, volume, pitch, world)
	}

	/** Plays a sound at the specified position. Args: x, y, sound, volume (relative to 1.0), and frequency (or pitch, also relative to 1.0). */
	def playSound(x: Float, y: Float, soundName: String, volume: Float, pitch: Float, world: ICellAccess) {
		val audioIn = AudioSystem getAudioInputStream (SoundRegistry get soundName getOrElse new AudioInputStream(null))
		val clip = AudioSystem.getClip
		clip open audioIn
		clip.start()
	}

	def spawnParticle(particleName: String, x: Float, y: Float, initialMomentumX: Float, initialMomentumY: Float, world: ICellAccess) {
		val ctr = ParticleRegistry.get(particleName).get.getConstructor(classOf[Reference[ICellAccess]], classOf[Reference[IEntityAccess]], classOf[Float], classOf[Float], classOf[Float], classOf[Float])
		particles +:= ctr.newInstance(new WeakReference[ICellAccess](world).asInstanceOf[AnyRef], new WeakReference[IEntityAccess](this).asInstanceOf[AnyRef], x.asInstanceOf[AnyRef], y.asInstanceOf[AnyRef], initialMomentumX.asInstanceOf[AnyRef], initialMomentumY.asInstanceOf[AnyRef])
	}

	def addEntity(entity: Entity) =
		entities +:= entity

	def removeEntity(entity: Entity) =
		entities = entities filterNot {ent =>
			val flag = ent.getUniqueID equals entity.getUniqueID
			if(flag)
				ent.onEntityRemoval()
			flag
		}

	def collectGarbage() {
		particles = particles filterNot {part =>
			val flag = part == null || !part.isEntityAlive
			if(flag)
				part.onEntityRemoval()
			flag
		}
		entities = entities filterNot {ent =>
			val flag = ent == null || !ent.isEntityAlive
			if(flag)
				ent.onEntityRemoval()
			flag
		}
		player = if(player.isEntityAlive)
			player
		else {
			player.onEntityRemoval()
			null
		}
	}

	def hasEntity(entity: Entity) =
		entity match {
			case value: EntityFX =>
				particles contains value
			case value: Entity =>
				entities contains value
			case _ =>
				throw new IllegalStateException("Who put it here?")
		}
}
