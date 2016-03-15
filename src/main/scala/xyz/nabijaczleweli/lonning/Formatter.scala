package xyz.nabijaczleweli.lonning

import scala.annotation.elidable
import scala.annotation.elidable._
import org.joda.time.DateTime

/** @author Jędrzej
  * @since  16.04.14
  */
object Formatter {
	@inline
	final         val format            = System.getProperty("lonning.fromat", "dd.MM.yy HH:mm:ss")
	private final val `emaNyb.elbadile` = {
		var t = Map.empty[Int, String]
		val repl = Map[Int, String](FINEST -> "DEBUG", FINER -> "LOG")
		for(i <- elidable.byName) {
			val g = repl get i._2
			t += (if(g.isEmpty) i.swap else i._2 -> g.get)
		}
		t += 5000 -> "\u00A1VERY VERY BAD!"
		t
	}

	def getPreStuffs(name: String, level: Int) =
		s"[${DateTime.now.toString(format)}] [$name] [${
			val g = `emaNyb.elbadile` get level
			if(!g.isEmpty)
				g.get
			else
				level
		}]"
}
