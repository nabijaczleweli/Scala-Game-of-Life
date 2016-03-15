package xyz.nabijaczleweli.scala_game_of_life.engine.registries

import xyz.nabijaczleweli.scala_game_of_life.world.IEntityAccess

import scala.collection.mutable

/** @author Jędrzej
  * @since  15.05.14
  */
object WorldNameRegistry extends Registry[Class[_ <: ICellAccess with IEntityAccess], String] {
	private val _name2cls = new mutable.HashMap[key_type, stored_type]

	override private[registries] def add(key: key_type, obj: stored_type) =
		_name2cls += key -> obj

	override def get(key: key_type) =
		_name2cls get key
}
