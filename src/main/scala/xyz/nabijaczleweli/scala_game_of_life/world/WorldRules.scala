package xyz.nabijaczleweli.scala_game_of_life.world

import xyz.nabijaczleweli.scala_game_of_life.engine.save.Saveable

/** Represen
  *
  * @author Jędrzej
  * @since  11.04.14
  */
@SerialVersionUID(1211111211L)
class WorldRules private(`_ _ _ _ _ _ _ _ _`: () => Unit) extends Saveable {
	var resurrect  = Seq[Int]()
	var stillAlive = Seq[Int]()
	var tickAtOnce = 500

	def this(s: String) {
		this(() => {})
		if(s.length > 1) {
			assume("[0-9]*/[0-9]*".r.findAllIn(s).length == 1)
			stillAlive = for(i <- s.split('/')(0)) yield i.toInt - '0'.toInt
			resurrect = for(i <- s.split('/')(1)) yield i.toInt - '0'.toInt
		}
	}

	def this() =
		this("23/3")

	override def saveName =
		null

	override def saveDomain =
		null
}
