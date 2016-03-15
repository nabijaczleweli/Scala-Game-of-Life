package xyz.nabijaczleweli.scala_game_of_life.engine.registries

import scala.collection.mutable

/** Stored: Maximal metadata: <tt>[[Int]]</tt><br />
  * Key:    Screen:           <tt>[[Int]]</tt>
  *
  * @author Jędrzej
  * @since  25.04.14
  */
object MaxMetadataRegistry extends Registry[Int, Int] {
	private val _idx2mm = new mutable.HashMap[key_type, stored_type]

	/** Put things plain. */
	override private[registries] def add(key: key_type, obj: stored_type) =
		_idx2mm += (key & 0xff) -> obj

	/** Get things using `GameRenderer.drawInfo`. */
	override def get(key: key_type) =
		_idx2mm get key & 0xff
}
