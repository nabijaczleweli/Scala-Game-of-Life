package xyz.nabijaczleweli.scala_game_of_life.entity

import xyz.nabijaczleweli.scala_game_of_life.world.IEntityAccess

import scala.ref.Reference

/** @author Jędrzej
  * @since  10.05.14
  */
abstract class EntityFX(par1World: Reference[ICellAccess], par2World: Reference[IEntityAccess], _posX: Float, _posY: Float, initialMomentumX: Float, initialMomentumY: Float) extends Entity(par1World, par2World, _posX, _posY) {
	addVelocity(initialMomentumX, initialMomentumY)

	override protected def entityInit() {}

	final override def onInvisibleDraw() {}
}
