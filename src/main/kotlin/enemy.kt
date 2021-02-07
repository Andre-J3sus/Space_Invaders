import pt.isel.canvas.*

class Enemy(val pos:Position, val color:Int) //Enemy class with position and color

//ENEMY Constants:
const val ENEMY_PIXEL = 8
const val ENEMY_SIDE = 5 * ENEMY_PIXEL
const val ENEMY_VEL = 2
const val DAMAGE = 10


/**
 * Draw the enemy
 */
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


/**
 * Move the enemy down the sreen
 */
fun Enemy.move():Enemy = Enemy(Position(pos.x, pos.y + ENEMY_VEL), color)


/**
 * Enemy X range
 */
fun Enemy.rangeX() = pos.x .. pos.x + ENEMY_SIDE


/**
 * Returns a list of new enemies with random colors and positions
 */
fun newEnemies():List<Enemy>{
    val color = listOf(RED, YELLOW, CYAN, MAGENTA, BLUE, GREEN).random()
    val newEnemies = mutableListOf<Enemy>()
    val xRange = (0..WIDTH - ENEMY_SIDE)

    when(val numberOfEnemies = (1..3).random()){
        1 -> newEnemies.add(Enemy(Position(xRange.random(), -ENEMY_SIDE), color))
        in 2..3 -> {
            val possibleX = xRange.toMutableList()

            repeat(numberOfEnemies){
                val chosenX = possibleX.random()
                newEnemies.add(Enemy(Position(chosenX, -ENEMY_SIDE), color))
                possibleX.removeIf{ it in (chosenX - ENEMY_SIDE .. chosenX + ENEMY_SIDE) }
            }
        }
    }
    return newEnemies
}


/**
 * Collision between enemy and ship
 */
fun Enemy.collides(p:Ship):Boolean = pos.x <= p.pos.x + SHIP_W &&
            pos.x + ENEMY_SIDE >= p.pos.x &&
            pos.y + ENEMY_SIDE >= p.pos.y &&
            pos.y <= p.pos.y
