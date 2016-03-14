package com.scala_game_of_life.engine.registries

import scala.collection.mutable

/** Stored: Enter action: <tt>() => [[Unit]]</tt><br />
  * Key:    Screen:       <tt>[[Int]]</tt>
  *
  * @author Jędrzej
  * @since  25.04.14
  */
object EnterActionRegistry extends Registry[() => Unit, Int] {
	private var _idx2func = new mutable.HashMap[key_type, stored_type]

	/** Put things plain. */
	override private[registries] def add(key: EnterActionRegistry.key_type, obj: EnterActionRegistry.stored_type) =
		_idx2func += (key & 0xff) -> obj

	/** Get things using `GameRenderer.drawInfo`. */
	override def get(key: key_type) =
		_idx2func get key & 0xff
}