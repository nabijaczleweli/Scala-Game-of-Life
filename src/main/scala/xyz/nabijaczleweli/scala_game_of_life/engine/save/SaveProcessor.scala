package xyz.nabijaczleweli.scala_game_of_life.engine.save

import java.io._

import xyz.nabijaczleweli.lonning.loutput.ConsoleLOutput
import Saveable._
import xyz.nabijaczleweli.scala_game_of_life.engine.GameEngine.nameOfSave

import scala.annotation.elidable
import scala.annotation.elidable._
import scala.reflect.ClassTag

/** Loads and saves things.
  *
  * @author Jędrzej
  * @since  10.04.14
  */
object SaveProcessor extends ConsoleLOutput("S-Saver") {
	final val separator = System.getProperty("file.separator")

	def save[T <: Saveable : ClassTag](toSave: T) {
		@elidable(FINEST)
		val time = System.currentTimeMillis
		var succ = false
		try {
			safeCreatePlaceFor(toSave.saveDomain)
			val oos = new ObjectOutputStream(new FileOutputStream(s"$rootPath${if(nameOfSave != "") s"$separator$nameOfSave" else ""}$separator${toSave.saveDomain}$separator${toSave.saveName}$extension"))
			oos writeObject toSave
			oos.close()
			succ = true
		} catch {
			case _: NullPointerException =>
		}
		@elidable(FINEST)
		val timeAfter = System.currentTimeMillis
		if(succ)
			info(s"Saved \'$toSave\' in \'$nameOfSave\'.")
		else
			warn(s"Type \'${toSave.getClass.getSimpleName}\' cannot be saved!")
		debug(s"Saving: ${timeAfter - time}ms.")
	}

	def load[T <: Saveable : ClassTag](toLoad: T): T = {
		@elidable(FINEST)
		val time = System.currentTimeMillis
		var toRet = null.asInstanceOf[T]
		val saveFile = new File(s"$rootPath${if(nameOfSave != "") s"$separator$nameOfSave" else ""}$separator${toLoad.saveDomain}$separator${toLoad.saveName}$extension")
		if(saveFile.exists) {
			try {
				val ois = new ObjectInputStream(new FileInputStream(saveFile))
				toRet = ois.readObject.asInstanceOf[T]
				ois.close()
				info(s"Loaded \'$toLoad\' from \'$nameOfSave\'.")
			} catch {
				case _: Throwable =>
					toRet = toLoad.getClass.newInstance
					info(s"Loading \'$toLoad\' from \'$nameOfSave\' failed - generated a new one.")
			}
		} else {
			toRet = toLoad.getClass.newInstance
			info(s"Couldn't load \'$toLoad\' from \'$nameOfSave\' - generated a new one.")
		}
		@elidable(FINEST)
		val timeAfter = System.currentTimeMillis
		debug(s"Loading: ${timeAfter - time}ms.")
		toRet
	}

	protected def safeCreatePlaceFor(p: String) {
		val f = new File(s"$rootPath$separator$nameOfSave$separator$p")
		if(f.exists && f.isDirectory)
			return
		if(!f.isDirectory)
			f.delete()
		f.mkdir()
	}
}
