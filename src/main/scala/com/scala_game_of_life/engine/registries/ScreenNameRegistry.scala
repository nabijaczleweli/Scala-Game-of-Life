package com.scala_game_of_life.engine.registries

import scala.collection.mutable

/** Stored: Name:   <tt>[[String]]</tt><br />
  * Key:    Screen: <tt>[[Int]]</tt>
  *
  * @author Jędrzej
  * @since  29.04.14
  */
object ScreenNameRegistry extends Registry[String, Int] {
	private val _idx2name = new mutable.HashMap[key_type, stored_type]

	/** Put things plain. */
	override private[registries] def add(key: key_type, obj: stored_type) =
		_idx2name += (key & 0xff) -> obj

	/** Get things using `GameRenderer.drawInfo`. */
	override def get(key: key_type) =
		_idx2name get key & 0xff
}
