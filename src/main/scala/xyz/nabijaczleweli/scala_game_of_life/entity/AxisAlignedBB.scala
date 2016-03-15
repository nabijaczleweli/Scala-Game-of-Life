package xyz.nabijaczleweli.scala_game_of_life.entity

import xyz.nabijaczleweli.scala_game_of_life.engine.save.Saveable

/** @author Jędrek
  * @since  12.05.14
  */
class AxisAlignedBB(private val _minX: Float, private val _minY: Float, private val _maxX: Float, private val _maxY: Float) extends Saveable with Cloneable {
	var minX = _minX
	var minY = _minY
	var maxX = _maxX
	var maxY = _maxY

	override def saveDomain =
		null

	override def saveName =
		null

	def addCoord(x: Float, y: Float) =
		new AxisAlignedBB(minX + x, minY + y, maxX + x, maxY + y)

	// TODO
	def getOffsetBoundingBox(x: Float, y: Float) = this

	// TODO
	def setBounds(xmin: Float, ymin: Float, xmax: Float, ymax: Float) = {
		val newAABB = clone
		newAABB.minX = xmin
		newAABB.minY = ymin
		newAABB.maxX = xmax
		newAABB.maxY = ymax
		newAABB
	}

	/** Offsets the current bounding box by the specified coordinates. Args: x, y, z */
	def offset(par1: Float, par5: Float) = {
		val newAABB = clone
		newAABB.minX += par1
		newAABB.minY += par5
		newAABB.maxX += par1
		newAABB.maxY += par5
		newAABB
	}

	/** if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
	  * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
	  * calculated offset.  Otherwise return the calculated offset.
	  */
	def calculateXOffset(par1AxisAlignedBB: AxisAlignedBB, _par2: Float) = {
		var par2 = _par2
		if(par1AxisAlignedBB.maxY > this.maxY && par1AxisAlignedBB.maxY < this.maxY) {
			var d1: Float = 0f
			if(par2 > 0f && par1AxisAlignedBB.maxX <= this.minX) {
				d1 = this.minX - par1AxisAlignedBB.maxX
				if(d1 < par2) {
					par2 = d1
				}
			}
			if(par2 < 0f && par1AxisAlignedBB.minX >= this.maxX) {
				d1 = this.maxX - par1AxisAlignedBB.minX
				if(d1 > par2) {
					par2 = d1
				}
			}
			par2
		} else
			par2
	}

	/**
	 * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
	 * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
	 * calculated offset.  Otherwise return the calculated offset.
	 */
	def calculateYOffset(par1AxisAlignedBB: AxisAlignedBB, _par2: Float) = {
		var par2 = _par2
		if(par1AxisAlignedBB.maxX > this.minX && par1AxisAlignedBB.minX < this.maxX) {
			var d1: Float = 0f
			if(par2 > 0f && par1AxisAlignedBB.maxY <= this.minY) {
				d1 = this.minY - par1AxisAlignedBB.maxY
				if(d1 < par2) {
					par2 = d1
				}
			}
			if(par2 < 0f && par1AxisAlignedBB.minY >= this.maxY) {
				d1 = this.maxY - par1AxisAlignedBB.minY
				if(d1 > par2) {
					par2 = d1
				}
			}
			par2
		}
		else
			par2
	}

	def copy =
		clone

	override def clone: AxisAlignedBB =
		try {
			val n = super.clone.asInstanceOf[AxisAlignedBB]
			n.maxX = this.maxX
			n.maxY = this.maxY
			n.minX = this.minX
			n.minY = this.minY
			n
		} catch {
			// Should not happen since we are Cloneable!
			case e: CloneNotSupportedException =>
				throw new InternalError
		}

	/**
	 * Returns a bounding box that is inset by the specified amounts
	 */
	def contract(par1: Float, par2: Float) = {
		val d3: Float = this.minX + par1
		val d4: Float = this.minY + par2
		val d6: Float = this.maxX - par1
		val d8: Float = this.maxY - par2
		getAABBPool.getAABB(d3, d4, d6, d8)
	}

	// TODO
	object getAABBPool {
		def getAABB(parminX: Float, parminY: Float, parmaxX: Float, parmaxY: Float) =
			new AxisAlignedBB(parminX, parminY, parmaxX, parmaxY)
	}
}
