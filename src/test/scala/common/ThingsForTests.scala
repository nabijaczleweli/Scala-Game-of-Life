package common

import com.scala_game_of_life.cell.Material
import com.scala_game_of_life.world.ICellAccess
import xyz.nabijaczleweli.scala_game_of_life.cell.{Material, Cell}
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.CellRegistry

/** @author Jędrzej
  * @since  07.05.14
  */
object ThingsForTests {
	final val testCellID = Short.MaxValue

	private val m = CellRegistry.getClass.getMethod("add", classOf[CellRegistry.key_type], classOf[CellRegistry.stored_type])
	m setAccessible true
	m.invoke(CellRegistry, testCellID.asInstanceOf[Object], new Cell(Material.notAir) {override def draw(onScreenX: Int, onScreenY: Int, worldX: Long, worldY: Long, world: ICellAccess) {}}.asInstanceOf[Object])
}
