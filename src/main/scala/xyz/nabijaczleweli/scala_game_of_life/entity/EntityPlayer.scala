package xyz.nabijaczleweli.scala_game_of_life.entity

import java.awt.Color
import xyz.nabijaczleweli.scala_game_of_life.engine.{GameEngine, GameRenderer}
import xyz.nabijaczleweli.scala_game_of_life.world.{ICellAccess, IEntityAccess}

import scala.ref.Reference

/** @author Jędrzej
  * @since  10.05.14
  */
class EntityPlayer(par1World: Reference[ICellAccess], par2World: Reference[IEntityAccess], _posX: Float, _posY: Float) extends Entity(par1World, par2World, _posX, _posY) {
	override protected def entityInit() {
		step = .18f
	}

	@deprecated("Do not use this method - use `draw()` instead.", "0.9.8")
	override def draw(screenX: Float, screenY: Float) {}

	override def onInvisibleDraw() {
		if(GameEngine.rand.nextBoolean() && GameEngine.rand.nextBoolean())
			entityObj.spawnParticle("invisibility", posX + GameEngine.rand.nextInt(100).toFloat / 100f, posY + GameEngine.rand.nextInt(100).toFloat / 100f, 0f, 0f, worldObj)
	}

	def draw() {
		import GameRenderer._
		graph setColor Color.red
		graph.fillRect((cellsInXAxis * cellWidth) / 2 + ((posX - Math.floor(posX)) * cellWidth).toInt - cellWidth, (cellsInYAxis * cellHeight) / 2 + ((posY - Math.floor(posY)) * cellHeight).toInt - cellHeight, cellWidth * 2, cellHeight * 2)
	}
}
