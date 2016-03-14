package com.scala_game_of_life.entity.particles

import com.scala_game_of_life.world.{IEntityAccess, ICellAccess}
import scala.ref.Reference
import com.scala_game_of_life.engine.GameRenderer
import java.awt.Color
import com.scala_game_of_life.entity.EntityFX

/** @author Jędrzej
  * @since  12.05.14
  */
final class InvisibilityEffect(par1World: Reference[ICellAccess], par2World: Reference[IEntityAccess], _posX: Float, _posY: Float, initialMomentumX: Float, initialMomentumY: Float) extends EntityFX(par1World, par2World, _posX, _posY, initialMomentumX, initialMomentumY) {
	var drawsExisted = 0
	val treshold     = if(GameRenderer.screenRepainter == null) 30 else (1000 / GameRenderer.screenRepainter.delay) * 2

	override def draw(screenX: Float, screenY: Float) {
		GameRenderer.graph setColor InvisibilityEffect.color
		GameRenderer.graph.drawRect(screenX.toInt, screenY.toInt, 1, 1)
		if(drawsExisted >= treshold)
			setDead()
		drawsExisted += 1
	}

	override def processMotion() {}
}

private object InvisibilityEffect {
	final val color = new Color(204, 1, 1)
}