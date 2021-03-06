package xyz.nabijaczleweli.scala_game_of_life.engine.registries

import xyz.nabijaczleweli.lonning.loutput.ConsoleLOutput
import xyz.nabijaczleweli.scala_game_of_life.engine.GameRenderer
import GameRenderer.MetadataChangeSource.MetadataChangeSource
import xyz.nabijaczleweli.scala_game_of_life.util.NumberUtil
import xyz.nabijaczleweli.scala_game_of_life.world.{ICellAccess, IEntityAccess}

/** Contains functions to register things.
  *
  * @author Jędrzej
  * @since  02.04.14
  */
object GameRegistry extends ConsoleLOutput("S-GameRegistry") {
	def registerWorld[T <: ICellAccess with IEntityAccess](par: Class[T], lookupfunc: Int => TraversableOnce[(Long, Long)]) = {
		log(s"Registering \'${par.toString split "class " apply 1}\'.")
		val world = par.newInstance
		WorldFuncRegistry.add(s"$world", lookupfunc)
		WorldNameRegistry.add(s"$world", par)
	}

	def registerScreen(enterfunc: => Unit, maxMetadata: Int, name: String, disallowedSources: Seq[MetadataChangeSource], screen: Int) {
		assume(disallowedSources.nonEmpty)
		log(s"Registering enter action and maximal metadata (${maxMetadata & 0xf}) for screen #${NumberUtil setbit screen} named \'$name\', whose metadata cannot be changed by ${var str = ""; for(i <- disallowedSources.indices) str += s"${disallowedSources(i)}${if(i != disallowedSources.length - 1) ", " else ""}"; str}.")
		MaxMetadataRegistry.add(screen, maxMetadata)
		EnterActionRegistry.add(screen, () => enterfunc)
		ScreenNameRegistry.add(screen, name)
		DisallowedSourcesRegistry.add(screen, disallowedSources)
	}
}
