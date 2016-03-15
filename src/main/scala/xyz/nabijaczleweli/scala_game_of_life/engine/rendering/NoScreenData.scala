package xyz.nabijaczleweli.scala_game_of_life.engine.rendering

/** Marker that this screen doesn't hold any metadata.
  *
  * @author Jędrzej
  * @since  27.04.14
  */
object NoScreenData extends ExtendedScreenData[Unit] {
	override def value {}

	override def value_=(newData: Unit) {}

	override def startup {}
}
