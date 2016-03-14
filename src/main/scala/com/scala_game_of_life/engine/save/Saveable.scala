package com.scala_game_of_life.engine.save

import java.io.{ObjectInputStream, ObjectOutputStream, IOException, ObjectStreamException}

/** Shortcut for savingness.
  *
  * @author Jędrzej
  * @since  10.04.14
  */
trait Saveable extends Serializable {
	@throws[IOException]
	def writeObject(out: ObjectOutputStream) {
		out.defaultWriteObject()
	}

	@throws[IOException]
	@throws[ClassNotFoundException]
	def readObject(in: ObjectInputStream) {
		in.defaultReadObject()
	}

	@throws[ObjectStreamException]
	def readObjectNoData() {}

	/** If null: shouldn't be saved independently. */
	def saveDomain: String

	/** If null: shouldn't be saved independently. */
	def saveName: String
}

object Saveable {
	final val extension = ".sol"
	final val rootPath  = "S-SOLs"
}