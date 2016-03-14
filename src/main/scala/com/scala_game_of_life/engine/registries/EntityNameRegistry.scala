package com.scala_game_of_life.engine.registries

import scala.collection.mutable
import com.scala_game_of_life.entity.Entity

/** Stored: Entity name:          <tt>[[String]]</tt><br />
  * Key:    Class of said entity: <tt>Class[_ <: [Entity]</tt>
  *
  * @author Jędrzej
  * @since  10.05.14
  */
object EntityNameRegistry extends Registry[String, Class[_ <: Entity]] {
	private var _cls2name = new mutable.HashMap[key_type, stored_type]
	private var _name2cls = new mutable.HashMap[stored_type, key_type]

	// Only GameRegistry is allowed to put stuff in.
	override private[registries] def add(key: key_type, obj: stored_type) = {
		_cls2name += key -> obj
		_name2cls += obj -> key
	}

	override def get(key: key_type): Option[stored_type] =
		_cls2name get key

	def get(key: stored_type): Option[key_type] =
		_name2cls get key
}
