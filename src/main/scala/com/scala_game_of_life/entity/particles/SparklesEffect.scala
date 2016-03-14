package com.scala_game_of_life.entity.particles

import com.scala_game_of_life.entity.EntityFX
import scala.ref.Reference
import com.scala_game_of_life.world.{IEntityAccess, ICellAccess}
import com.scala_game_of_life.engine.GameRenderer
import java.awt.Color
import com.scala_game_of_life.engine.GameRenderer._

/** @author Jędrzej
  * @since  14.05.14
  */
final class SparklesEffect(par1World: Reference[ICellAccess], par2World: Reference[IEntityAccess], _posX: Float, _posY: Float, initialMomentumX: Float, initialMomentumY: Float)extends EntityFX(par1World, par2World, _posX, _posY, initialMomentumX, initialMomentumY) {
	var drawsExisted = 0
	val treshold     = if(GameRenderer.screenRepainter == null) 3000 else (1000 / GameRenderer.screenRepainter.delay) * 100 // Upto 100s

	override def draw(screenX: Float, screenY: Float) {
		import com.scala_game_of_life.engine.GameRenderer._
		graph setColor Color.white
		graph.drawRect(screenX.toInt - drawsExisted / 2, screenY.toInt - drawsExisted / 2, drawsExisted, drawsExisted)
		if(drawsExisted >= treshold || SparklesEffect.`isn'tRenderPlaceOK`(this, screenX, screenY))
			setDead()
		drawsExisted += 1
	}

	override def processMotion() {
		this.prevPosX = posX
		this.prevPosY = posY
		posX += motionX
		posY += motionY
	}
}

object SparklesEffect {
	final def `isn'tRenderPlaceOK`(part: SparklesEffect, screenX: Float, screenY: Float) =
		screenX < -part.drawsExisted || screenX > (frameSize.width + part.drawsExisted) || screenY < -part.drawsExisted || screenY > (frameSize.height + part.drawsExisted)
}