package com.scala_game_of_life.engine.registries

import com.scala_game_of_life.engine.GameRenderer
import scala.collection.mutable

/** Stored: Disallowed sources: <tt>Seq[MetadataChangeSource]</tt><br />
  * Key:    Screen:             <tt>[[Int]]</tt>
  *
  * @author Jędrzej
  * @since  05.05.14
  */
object DisallowedSourcesRegistry extends Registry[Seq[GameRenderer.MetadataChangeSource.MetadataChangeSource], Int] {
	private var _idx2vect = new mutable.HashMap[key_type, stored_type]

	/** Put things plain. */
	override private[registries] def add(key: key_type, obj: stored_type) =
		_idx2vect += (key & 0xff) -> obj

	/** Get things using `GameRenderer.drawInfo`. */
	override def get(key: key_type) =
		_idx2vect get key & 0xff
}
