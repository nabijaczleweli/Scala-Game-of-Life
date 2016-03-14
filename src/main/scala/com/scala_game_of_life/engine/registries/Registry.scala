package com.scala_game_of_life.engine.registries

/** @author Jędrzej
  * @since  22.04.14
  */
trait Registry[__stored_type, __key_type] {
	type stored_type = __stored_type
	type key_type = __key_type

	// Only GameRegistry is allowed to put stuff in.
	private[registries] def add(key: key_type, obj: stored_type)

	def get(key: key_type): Option[stored_type]
}
