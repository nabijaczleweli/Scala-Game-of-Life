package xyz.nabijaczleweli.scala_game_of_life.engine.registries

import scala.collection.mutable
import javax.sound.sampled.AudioInputStream

/** @author Jędrzej
  * @since  10.05.14
  */
object SoundRegistry extends Registry[AudioInputStream, String] {
	private var _name2msg = new mutable.HashMap[key_type, stored_type]

	// Only GameRegistry is allowed to put stuff in.
	override private[registries] def add(key: key_type, obj: stored_type) =
		_name2msg += key -> obj

	override def get(key: key_type): Option[stored_type] =
		_name2msg get key
}
