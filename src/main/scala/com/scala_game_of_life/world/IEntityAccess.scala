package com.scala_game_of_life.world

import com.scala_game_of_life.entity.{EntityPlayer, EntityFX, Entity}
import scala.ref.{WeakReference, Reference}
import com.scala_game_of_life.engine.registries.{SoundRegistry, ParticleRegistry}
import javax.sound.sampled.{AudioInputStream, AudioSystem}
import com.scala_game_of_life.engine.GameRenderer
import com.scala_game_of_life.entity.AxisAlignedBB

/** @author Jędrzej
  * @since  10.05.14
  */
trait IEntityAccess {
	final def init(world: Reference[ICellAccess]) {
		player = new EntityPlayer(world, new WeakReference[IEntityAccess](this), GameRenderer.cellsInXAxis / 2, GameRenderer.cellsInYAxis / 2)
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
		entities = entities filterNot {ent => ent.getUniqueID.equals(entity.getUniqueID)}

	def collectGarbage() {
		particles = particles filterNot {part => part == null || !part.isEntityAlive}
		entities = entities filterNot {ent => ent == null || !ent.isEntityAlive}
	}

	def hasEntity(entity: Entity) =
		entity match {
			case value: EntityFX =>
				particles contains value
			case value: Entity =>
				entities contains value
		}
}