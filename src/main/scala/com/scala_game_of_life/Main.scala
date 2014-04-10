package com.scala_game_of_life

import scala.swing._
import scala.swing.event.Key
import akka.actor.{Props, ActorSystem}
import com.scala_game_of_life.stuff._
import com.scala_game_of_life.stuff.Tick
import scala.swing.event.KeyPressed
import scala.swing.event.ButtonClicked

/** Main launcher of S-GOL.
  *
  * Extends Reactor and not SimpleSwingApplication nor SwingApplication because
  * I(nabijaczleweli)'ve been getting weird errors about duplicate method&signature.
  *
  * @author Jędrzej
  * @since  22.03.14
  */
object Main extends Reactor {
	val actorSystem        = ActorSystem("S-GOL")
	val gameEngine         = actorSystem.actorOf(Props[GameEngine], name = "engine")
	val world: ICellAccess = new World(0, 0)

	def startup(args: Array[String]) {
		for((s, w) <- MainResources.HelloStrings) {
			println(s)
			Thread sleep w
		}
		world.add(new Chunk(0, 0).cells.toArray, 0, 0)
		val test_json = "{\n\"name\": \"Joe Doe\",\n\"age\": 45,\n\"kids\": [\"Frank\", \"Marta\", \"Joan\"]\n}"
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		val t = top
		if(t.size == new Dimension(0, 0))
			t.pack()
		t.visible = true
	}

	def top: Frame = new MainFrame {
		title = "S-GOL"
		var numclicks = 0
		val button    = new Button {
			text = "Click me for good times!"
		}

		val label = new Label {
			listenTo(button, button.keys)
			listenTo(keys, mouse.clicks)
			reactions += {
				case ButtonClicked(`button`) =>
					numclicks += 1
					text = "Number of button clicks: " + numclicks
				case KeyPressed(_, Key.Escape, _, _) =>
					quit()
				case KeyPressed(_, Key.T, _, _) =>
					gameEngine ! Tick(world)
			}
		}

		contents = new FlowPanel {
			contents += button
			contents += label
			border = Swing.EmptyBorder(5, 5, 5, 5)
		}
	}

	def shutdown(): Unit = {
		for((s, w) <- MainResources.ByeStrings) {
			println(s)
			Thread sleep w
		}
	}

	def quit() {
		shutdown()
		sys.exit(0)
	}

	def main(args: Array[String]) =
		Swing.onEDT {
			startup(args)
		}
}

private object MainResources {
	final lazy val HelloStrings = Array[(String, Long)](("Hello.", 1000), ("What i have fo' you today is something pretty dang cool.", 680), ("And that is...", 1200), ("S-GOL: Scala-GameOfLife", 3456), ("", 200), ("Starting...", 500), ("NOW!", 20), ("", 0), ("", 0))
	final lazy val ByeStrings   = Array[(String, Long)](("So!", 250), ("This is nabijaczleweli standing off.", 850), ("Hope you guys enjoyed the game.", 500), ("And taaaaaaaake it easy!", 1456))
}