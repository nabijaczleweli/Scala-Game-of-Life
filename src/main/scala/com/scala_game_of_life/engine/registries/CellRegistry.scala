package com.scala_game_of_life.engine.registries

import com.scala_game_of_life.cell.{Material, Cell}
import scala.collection.mutable
import com.scala_game_of_life.world.ICellAccess
import java.awt.Color
import com.scala_game_of_life.cell.CellAction.CellAction
import com.scala_game_of_life.entity.Entity
import com.scala_game_of_life.util.ColorRainbow

/** Stored: Cell:      <tt>[[Cell]]</tt><br />
  * Key:    Cell's ID: <tt>[[Short]]</tt>
  *
  * @author Jędrzej
  * @since  06.05.14
  */
object CellRegistry extends Registry[Cell, Short] {

	import com.scala_game_of_life.engine.GameEngine.rand

	private final var __idtocell = new mutable.HashMap[key_type, stored_type]
	private final var __celltoid = new mutable.HashMap[stored_type, key_type]
	private final var length     = 0

	add(0.toShort, new Cell(Material.air) {
		override def draw(onScreenX: Int, onScreenY: Int, worldX: Long, worldY: Long, world: ICellAccess) {}

		override def onNeighbourCellChange(x: Long, y: Long, changedCell: (Cell, Long, Long), action: CellAction) {}

		override def onCellAction(x: Long, y: Long, action: CellAction) {}

		override def onUpdate(x: Long, y: Long) {}
	})

	add(1.toShort, new Cell(Material.notAir) {
		override def draw(onScreenX: Int, onScreenY: Int, worldX: Long, worldY: Long, world: ICellAccess) {
			import com.scala_game_of_life.engine.GameRenderer._
			graph setColor (if(world.cellState(worldX, worldY)) Color.blue else Color.black)
			graph.fillRect(onScreenX, onScreenY, cellWidth, cellHeight)
		}
	})
	add(2.toShort, new Cell(Material.notAir) {
		override def draw(onScreenX: Int, onScreenY: Int, worldX: Long, worldY: Long, world: ICellAccess) {
			import com.scala_game_of_life.engine.GameRenderer._
			graph setColor (if(world.cellState(worldX, worldY)) Color.pink else Color.darkGray)
			graph.fillRect(onScreenX, onScreenY, cellWidth, cellHeight)
		}
	})
	add(3.toShort, new Cell(Material.notAir) {
		override def draw(onScreenX: Int, onScreenY: Int, worldX: Long, worldY: Long, world: ICellAccess) {
			import com.scala_game_of_life.engine.GameRenderer._
			graph setColor (if(world.cellState(worldX, worldY)) Color.orange else Color.gray)
			graph.fillRect(onScreenX, onScreenY, cellWidth, cellHeight)
		}
	})
	add(4.toShort, new Cell(Material.notAir) {
		private val rain = new ColorRainbow(128, 128, 128)

		override def draw(onScreenX: Int, onScreenY: Int, worldY: Long, worldX: Long, world: ICellAccess) {
			import com.scala_game_of_life.engine.GameRenderer._
			graph setColor rain
			graph.fillRect(onScreenX, onScreenY, cellWidth, cellHeight)
		}

		override def onEntityWalking(entity: Entity) {
			entity.entityObj.spawnParticle("sparkles", entity.posX, entity.posY, entity.motionX, entity.motionY, entity.worldObj)
		}
	})

	// Only GameRegistry is allowed to put stuff in.
	override private[registries] def add(key: key_type, obj: stored_type) {
		__idtocell += key -> obj
		__celltoid += obj -> key
		length += 1
	}

	override def get(key: key_type): Option[stored_type] =
		__idtocell get key

	def get(key: stored_type): Option[key_type] =
		__celltoid get key

	def get = {
		if(rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) // Low chance of possibly getting air cells
			rand nextInt __idtocell.size
		else {
			rand.nextInt(length - 1) + 1
		}
	}.toShort
}