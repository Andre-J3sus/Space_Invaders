import pt.isel.canvas.*

class Enemy(val pos:Position, val color:Int)

//ENEMY Constants:
const val ENEMY_PIXEL = 10
const val ENEMY_SIDE = 5 * ENEMY_PIXEL
const val ENEMY_VEL = 2
const val DAMAGE = 10

fun Canvas.drawEnemy(e:Enemy){
    e.apply {
        drawRect(pos.x, pos.y, ENEMY_PIXEL, ENEMY_PIXEL, color)
        drawRect(pos.x + 4*ENEMY_PIXEL, pos.y, ENEMY_PIXEL, ENEMY_PIXEL, color)

        drawRect(pos.x + ENEMY_PIXEL, pos.y + ENEMY_PIXEL, 3*ENEMY_PIXEL, ENEMY_PIXEL, color)

        drawRect(pos.x, pos.y + 2*ENEMY_PIXEL, ENEMY_PIXEL, 3*ENEMY_PIXEL, color)
        drawRect(pos.x + 2*ENEMY_PIXEL, pos.y + 2*ENEMY_PIXEL, ENEMY_PIXEL, 3*ENEMY_PIXEL, color)
        drawRect(pos.x + 4*ENEMY_PIXEL, pos.y + 2*ENEMY_PIXEL, ENEMY_PIXEL, 3*ENEMY_PIXEL, color)

        drawRect(pos.x, pos.y + 3*ENEMY_PIXEL, ENEMY_SIDE, ENEMY_PIXEL, color)
    }
}


fun Enemy.move():Enemy = Enemy(Position(pos.x, pos.y + ENEMY_VEL), color)


fun Enemy.rangeX() = pos.x .. pos.x + ENEMY_SIDE


fun newEnemies():List<Enemy>{
    val color = listOf(RED, YELLOW, CYAN, MAGENTA).random()
    val newEnemies = mutableListOf<Enemy>()
    val xRange = (0..WIDTH - ENEMY_SIDE)

    val numberOfEnemies = (1..3).random()

    when(numberOfEnemies){
        1 -> newEnemies.add(Enemy(Position(xRange.random(), -ENEMY_SIDE), color))
        in 2..3 -> {
            var possibleX = xRange.toList()
            repeat(numberOfEnemies){
                val choosedX = possibleX.random()
                newEnemies.add(Enemy(Position(choosedX, -ENEMY_SIDE), color))
                possibleX = possibleX.filter{ it !in (choosedX - ENEMY_SIDE .. choosedX + ENEMY_SIDE) }
            }
        }
    }
    return newEnemies
}


fun Enemy.collides(p:Ship):Boolean = pos.x <= p.pos.x + SHIP_W &&
            pos.x + ENEMY_SIDE >= p.pos.x &&
            pos.y + ENEMY_SIDE >= p.pos.y &&
            pos.y <= p.pos.y
