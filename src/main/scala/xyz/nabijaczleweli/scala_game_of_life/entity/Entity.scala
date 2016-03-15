package xyz.nabijaczleweli.scala_game_of_life.entity

import java.util.UUID
import xyz.nabijaczleweli.scala_game_of_life.cell.Material.Material
import xyz.nabijaczleweli.scala_game_of_life.cell.{Cell, Material}
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.{CellRegistry, EntityNameRegistry}
import xyz.nabijaczleweli.scala_game_of_life.engine.save.Saveable
import xyz.nabijaczleweli.scala_game_of_life.entity.Effect.Effect
import xyz.nabijaczleweli.scala_game_of_life.util.DataUtil
import xyz.nabijaczleweli.scala_game_of_life.world.{ICellAccess, IEntityAccess}

import scala.ref.Reference

/** Its name iss a put to cell, which works likes so: cell -> płytka [for example]nazębna -> plaque.
  * <br />
  * Represents Minecraft's Entity.
  *
  * @author Jędrzej
  * @since  09.05.14
  */
@SerialVersionUID(133113111121L)
abstract class Entity private() extends Saveable {
	protected var entityId: Int = 0
	var renderDistanceWeight: Double = 0D

	final override def saveDomain =
		null

	final override def saveName =
		null

	/**
	 * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
	 * prevent spawning.
	 */
	var preventEntitySpawning: Boolean = false
	var forceSpawn           : Boolean = false

	/** Reference to the World object. */
	var worldObj : ICellAccess   = null
	var entityObj: IEntityAccess = null
	var prevPosX : Float         = 0f
	var prevPosY : Float         = 0f

	/** Entity position X */
	var posX: Float = 0f

	/** Entity position Z */
	var posY: Float = 0f

	/** Entity motion X */
	var motionX: Float = 0f

	/** Entity motion Z */
	var motionY: Float = 0f

	/** Axis aligned bounding box. */
	final var boundingBox: AxisAlignedBB = null

	/**
	 * True if after a move this entity has collided with something on X- or Z-axis
	 */
	var isCollidedHorizontally: Boolean = false

	/**
	 * True if after a move this entity has collided with something either vertically or horizontally
	 */
	var isCollided     : Boolean = false
	var velocityChanged: Boolean = false
	var field_70135_K  : Boolean = false

	/**
	 * Gets set by setDead, so this must be the flag whether an Entity is dead (inactive may be better term)
	 */
	var isDead: Boolean = false

	/** How wide this entity is considered to be */
	var width: Float = 0f

	/**
	 * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
	 */
	var lastTickPosX: Float = 0f

	/**
	 * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
	 */
	var lastTickPosY: Float = 0f

	/**
	 * Reduces the velocity applied by entity collisions by the specified percent.
	 */
	var entityCollisionReduction: Float = 0f

	/** How many ticks has this entity had ran since being alive */
	var ticksExisted: Int = 0

	/** Remaining time an entity will be "immune" to further damage after being hurt. */
	var hurtResistantTime: Int = 0
	protected var isImmuneToFire: Boolean = false

	/** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
	var dimension: String = "Overworld"
	protected var entityUniqueID: UUID = null

	var rotationYaw  : Float = 0f
	var rotationPitch: Float = 0f

	var effects  : Vector[Effect] = Vector[Effect]()
	var sprinting: Boolean        = false

	var step: Float = 0f

	def this(par1World: Reference[ICellAccess], par2World: Reference[IEntityAccess], _posX: Float, _posY: Float) {
		this
		Entity.nextEntityID += 1
		this.entityId = Entity.nextEntityID
		this.renderDistanceWeight = 1.0D
		this.boundingBox = new AxisAlignedBB(0f, 0f, 0f, 0f)
		this.field_70135_K = true
		this.width = 0.6F
		this.entityUniqueID = UUID.randomUUID
		this.worldObj = par1World()
		this.entityObj = par2World()
		this.setPosition(_posX, _posY)
		if(par1World != null)
			this.dimension = par1World.toString()
		this.entityInit()
		//MinecraftForge.EVENT_BUS.post(new EntityEvent.EntityConstructing(this))
	}

	protected def entityInit()

	override def hashCode: Int =
		this.entityId

	/**
	 * Will get destroyed next tick.
	 */
	def setDead() {
		this.isDead = true
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected def setSize(par1: Float) {
		var f2: Float = 0f
		if(par1 != this.width) {
			f2 = this.width
			this.width = par1
			this.boundingBox.maxX = this.boundingBox.minX + this.width
			this.boundingBox.maxY = this.boundingBox.minY + this.width
			if(this.width > f2)
				this.moveEntity(f2 - this.width, f2 - this.width)
		}
	}

	/**
	 * Sets the rotation of the entity
	 */
	protected def setRotation(par1: Float, par2: Float) {
		this.rotationYaw = par1 % 360f
		this.rotationPitch = par2 % 360f
	}

	/**
	 * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
	 */
	def setPosition(par1: Float, par2: Float) {
		this.posX = par1
		this.posY = par2
		val f: Float = this.width / 2.0F
		this.boundingBox.setBounds(par1 - f, par2 - f, par1 + f, par2 + f)
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	def onUpdate()() {
		this.onEntityUpdate()
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	def onEntityUpdate() {
		this.prevPosX = this.posX
		this.prevPosY = this.posY
		if(this.sprinting) {
			val j: Int = Math.floor(this.posX).toInt
			val k: Int = Math.floor(this.posY).toInt
			val cell: Cell = this.worldObj.cellAtCoords(j, k, create = true)
			if(cell.material != Material.air)
				this.entityObj.spawnParticle(s"cellcrack_${CellRegistry.get(cell).getOrElse("air")}_${this.worldObj.cellMeta(j, k)}", this.posX + GameEngine.rand.nextFloat() -.5f * this.width, this.boundingBox.minY + .1f, -this.motionX * 4f, -this.motionY * 4f, worldObj)
		}
	}

	/**
	 * Tries to moves the entity by the passed in displacement. Args: x, y, z
	 */
	def moveEntity(_par1: Float, _par2: Float) {
		/*
				var par1 = _par1
				var par2 = _par2
				var d6: Float = par1
				var d8: Float = par2
				val flag: Boolean = this.isInstanceOf[EntityPlayer]
				if(flag) {
					var d9: Float = .05f
					while(par1 != 0f && this.entityObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, 0f), worldObj).isEmpty) {
						if(par1 < d9 && par1 >= -d9)
							par1 = 0f
						else if(par1 > 0f)
							par1 -= d9
						else
							par1 += d9
						d6 = par1
					}
					while(par2 != 0.0D && this.entityObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0f, par2), worldObj).isEmpty) {
						if(par2 < d9 && par2 >= -d9)
							par2 = 0f
						else if(par2 > 0f)
							par2 -= d9
						else
							par2 += d9
						d8 = par2
					}
					while(par1 != 0f && par2 != 0f && this.entityObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, par2), worldObj).isEmpty) {
						if(par1 < d9 && par1 >= -d9)
							par1 = 0f
						else if(par1 > 0f)
							par1 -= d9
						else
							par1 += d9
						if(par2 < d9 && par2 >= -d9)
							par2 = 0f
						else if(par2 > 0f)
							par2 -= d9
						else
							par2 += d9
						d6 = par1
						d8 = par2
					}
				}

				val list = this.entityObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(par1, par2), worldObj)

				if(!this.field_70135_K) {
					par2 = 0f
					par1 = 0f
				}
				for(i <- 0 until list.size)
					par1 = list(i).calculateXOffset(this.boundingBox, par1)
				this.boundingBox.offset(par1, 0f)
				if(!this.field_70135_K && d6 != par1) {
					par2 = 0f
					par1 = 0f
				}
				for(i <- 0 until list.size)
					par2 = list(i).calculateYOffset(this.boundingBox, par2)
				this.boundingBox.offset(0f, par2)
				if(!this.field_70135_K && d8 != par2) {
					par2 = 0f
					par1 = 0f
				}
				this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2f
				this.posY = (this.boundingBox.minY + this.boundingBox.maxY) / 2f
				this.isCollidedHorizontally = d6 != par1 || d8 != par2
				this.isCollided = this.isCollidedHorizontally
				if(d6 != par1)
					this.motionX = 0f
				if(d8 != par2)
					this.motionY = 0f*/
		////////////////////////////////////////////////////////////////////////////////////////////////
		this.posX = _par1
		this.posY = _par2
	}

	/*
		protected def func_145780_a(p_145780_1_ : Int, p_145780_2_ : Int, p_145780_3_ : Int, p_145780_4_ : Block) {
			var soundtype: Cell.SoundType = p_145780_4_.stepSound
			if(this.worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) eq Blocks.snow_layer) {
				soundtype = Blocks.snow_layer.stepSound
				this.playSound(soundtype.getStepResourcePath, soundtype.getVolume * 0.15F, soundtype.getPitch)
			}
			else if(!p_145780_4_.getMaterial.isLiquid) {
				this.playSound(soundtype.getStepResourcePath, soundtype.getVolume * 0.15F, soundtype.getPitch)
			}
		}*/

	def playSound(par1Str: String, par2: Float, par3: Float) {
		this.entityObj.playSoundAtEntity(this, par1Str, par2, par3, worldObj)
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected def canTriggerWalking =
		true

	/**
	 * returns the bounding box for this entity
	 */
	def getBoundingBox: AxisAlignedBB =
		boundingBox

	/**
	 * Checks if the current block the entity is within of the specified material type
	 */
	def isInsideOfMaterial(par1Material: Material): Boolean =
		this.worldObj.cellAtCoords(Math.floor(this.posX).toInt, Math.floor(this.posY).toInt, create = true).material == par1Material

	/**
	 * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	def setPositionAndRotation(par1: Float, par2: Float, par7: Float, par8: Float) {
		this.posX = par1
		this.prevPosX = this.posX
		this.posY = par2
		this.prevPosY = this.posY
		this.rotationYaw = par7
		this.rotationPitch = par8
		this.setPosition(this.posX, this.posY)
		this.setRotation(par7, par8)
	}

	/** Sets the location and Yaw/Pitch of an entity in the world */
	def setLocationAndAngles(par1: Float, par2: Float, par7: Float, par8: Float) {
		this.posX = par1
		this.prevPosX = this.posX
		this.lastTickPosX = this.prevPosX
		this.posY = par2
		this.prevPosY = this.posY
		this.lastTickPosY = this.prevPosY
		this.rotationYaw = par7
		this.rotationPitch = par8
		this.setPosition(this.posX, this.posY)
	}

	/**
	 * Returns the distance to the entity. Args: entity
	 */
	def getDistanceToEntity(par1Entity: Entity): Float = {
		val f: Float = this.posX - par1Entity.posX
		val f2: Float = this.posY - par1Entity.posY
		Math.sqrt((f * f + f2 * f2).toDouble)
	}.toFloat

	/**
	 * Gets the squared distance to the position. Args: x, y, z
	 */
	def getDistanceSq(par1: Float, par2: Float): Float = {
		val d3: Float = this.posX - par1
		val d5: Float = this.posY - par2
		d3 * d3 + d5 * d5
	}

	/**
	 * Gets the distance to the position. Args: x, y, z
	 */
	def getDistance(par1: Float, par2: Float): Float = {
		val d3: Float = this.posX - par1
		val d5: Float = this.posY - par2
		Math.sqrt(d3 * d3 + d5 * d5).toFloat
	}

	/**
	 * Returns the squared distance to the entity. Args: entity
	 */
	def getDistanceSqToEntity(par1Entity: Entity): Float = {
		val d0: Float = this.posX - par1Entity.posX
		val d2: Float = this.posY - par1Entity.posY
		d0 * d0 + d2 * d2
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	def onCollideWithPlayer(par1EntityPlayer: EntityPlayer) {}

	/**
	 * Applies a velocity to each of the entities pushing them away from each other. Args: entity
	 */
	def applyEntityCollision(par1Entity: Entity) {
		var d0: Float = par1Entity.posX - this.posX
		var d1: Float = par1Entity.posY - this.posY
		var d2: Float = Math.abs(d0) max Math.abs(d1)
		if(d2 >= 0.009999999776482582D) {
			d2 = Math.sqrt(d2).toFloat
			d0 /= d2
			d1 /= d2
			var d3: Float = (1f / d2) min 1f
			d0 *= d3
			d1 *= d3
			d0 *= 0.05000000074505806D.toFloat
			d1 *= 0.05000000074505806D.toFloat
			d0 *= 1f - this.entityCollisionReduction
			d1 *= 1f - this.entityCollisionReduction
			this.addVelocity(-d0, -d1)
			par1Entity.addVelocity(d0, d1)
		}
	}

	/**
	 * Adds to the current velocity of the entity. Args: x, y, z
	 */
	def addVelocity(par1: Float, par2: Float) {
		this.motionX += par1
		this.motionY += par2
	}

	/**
	 * Sets that this entity has been attacked.
	 */
	protected def setBeenAttacked() {
		this.velocityChanged = true
	}

	/**
	 * Called when the entity is attacked.
	 */
	def attackEntityFrom(/*par1DamageSource: DamageSource, */ par2: Float): Boolean = {
		if(!this.isEntityInvulnerable)
			this.setBeenAttacked()
		false
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	def canBeCollidedWith: Boolean =
		false

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	def canBePushed: Boolean =
		false

	protected def shouldSetPosAfterLoading: Boolean =
		true

	/**
	 * Returns the string that identifies this Entity's class
	 */
	protected final def getEntityString: String =
		EntityNameRegistry.get(getClass).get

	/**
	 * Checks whether target entity is alive.
	 */
	def isEntityAlive: Boolean =
		!this.isDead

	/**
	 * Checks if this entity is inside of an opaque block
	 */
	def isEntityInsideOpaqueBlock: Boolean = {
		for(i <- 0 until 8) {
			val f: Float = (((i >> 0) % 2).toFloat - 0.5F) * this.width * 0.8F
			val f2: Float = (((i >> 2) % 2).toFloat - 0.5F) * this.width * 0.8F
			val j: Int = Math.floor(this.posX + f.toDouble).toInt
			val l: Int = Math.floor(this.posY + f2.toDouble).toInt
			if(this.worldObj.isCellAt(j, l))
				return true
		}
		false
	}

	/*
		/**
		 * First layer of player interaction
		 */
		def interactFirst(par1EntityPlayer: EntityPlayer): Boolean = {
			return false
		}*/

	def getCollisionBorderSize: Float =
		.1f

	/*/***
		 * returns a (normalized) vector of where this entity is looking
		 */
		def getLookVec: Vec3 =
			null*/
	/*
		/**
		 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
		 */
		def setCurrentItemOrArmor(par1: Int, par2ItemStack: ItemStack) {}*/

	def isInvisible: Boolean =
		this.effects contains Effect.invisibility

	def setInvisible(par1: Boolean) =
		if(par1)
			this.effects :+= Effect.invisibility
		else
			while(isInvisible)
				this.effects = DataUtil.removeFromVector[Effect](this.effects, this.effects.indexOf(Effect.invisibility))

	def isInvurable: Boolean =
		this.effects contains Effect.invisibility

	def setInvurable(par1: Boolean) =
		if(par1)
			this.effects :+= Effect.invisibility
		else
			while(isInvurable)
				this.effects = DataUtil.removeFromVector[Effect](this.effects, this.effects.indexOf(Effect.invurability))

	/*
		/**
		 * This method gets called when the entity kills another one.
		 */
		def onKillEntity(par1EntityLivingBase: EntityLivingBase) {}*/

	/**
	 * Gets the name of this command sender (usually username, but possibly "Rcon")
	 */
	def getCommandSenderName: String =
		s"entity.${EntityNameRegistry get getClass getOrElse "generic"}.name"

	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	def isEntityEqual(par1Entity: Entity): Boolean =
		this == par1Entity

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	def canAttackWithItem: Boolean =
		true

	/**
	 * Called when a player attacks an entity. If this returns true the attack will not happen.
	 */
	def hitByEntity(par1Entity: Entity): Boolean =
		false

	override def toString: String =
		f"${this.getClass.getSimpleName}%s[\'${this.getCommandSenderName}%s\'/${this.entityId}%d, l=\'${if(this.worldObj == null || this.worldObj == null) "~NULL~" else this.worldObj.toString()}%s\', x=${this.posX}%.2f, y=${this.posY}%.2f]"

	/**
	 * Return whether this entity is invulnerable to damage.
	 */
	def isEntityInvulnerable: Boolean =
		this.effects contains Effect.invurability

	/**
	 * Sets this entity's location and angles to the location and angles of the passed in entity.
	 */
	def copyLocationAndAnglesFrom(par1Entity: Entity) {
		this.setLocationAndAngles(par1Entity.posX, par1Entity.posY, par1Entity.posY, par1Entity.rotationYaw)
	}

	/**
	 * Teleports the entity to another dimension. Params: Dimension number to teleport to
	 */
	def travelToDimension(refWorld: Reference[ICellAccess], refEnt: Reference[IEntityAccess]) {
		assume(refWorld().toString() != this.dimension)
		if(this.isEntityAlive) {
			this.dimension = refWorld().toString()
			this.isDead = true
			this.entityObj.removeEntity(this)
			this.isDead = false
			this.worldObj = refWorld.apply()
			this.entityObj = refEnt.apply()
			this.entityObj.addEntity(this)
		}
	}


	def getUniqueID: UUID =
		this.entityUniqueID

	/**
	 * Reset the entity ID to a new value. Not to be used from Mod code
	 */
	final def resetEntityId() {
		this.entityId = Entity.nextEntityID
		Entity.nextEntityID += 1
	}

	def processMotion() {
		this.prevPosX = posX
		this.prevPosY = posY
		if(step == 0f) {
			posX += motionX
			posY += motionY
			motionX = 0
			motionY = 0
		} else {
			if(motionX > 0f) {
				posX += motionX min step
				motionX -= motionX min step
			} else if(motionX < 0f) {
				posX += motionX max -step
				motionX -= motionX max -step
			}
			if(motionY > 0f) {
				posY += motionY min step
				motionY -= motionY min step
			} else if(motionY < 0f) {
				posY += motionY max -step
				motionY -= motionY max -step
			}
		}
		if(canTriggerWalking)
			worldObj.cellAtCoords(posX.toInt, posY.toInt, create = true) onEntityWalking this
	}

	def draw(screenX: Float, screenY: Float): Unit

	def onInvisibleDraw(): Unit = {}

	def tick(): Unit = {
		ticksExisted += 1
		onEntityUpdate()
		processMotion()
	}

	def onEntityRemoval(): Unit = {}
}

object Entity {
	var nextEntityID: Int = 0
}
