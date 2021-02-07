import pt.isel.canvas.*

data class Position(val x:Int, val y:Int) //Position class that represents a coordinate

data class Ship(
    val pos:Position, //Position of the ship
    val cooldown:Int, //Shoot cooldown
    val health:Int //Health
    )

data class Shot(val pos: Position) //Shot class with a position

//SHIP Constants:
const val SHIP_W = 28
const val SHIP_H = 30
const val STARTING_Y = HEIGHT - 80
const val SHIP_PIXEL = 4
const val MAX_COOLDOWN = 2

//HEALTH Constants:
const val MAX_HEALTH = 100
const val MID_HEALTH = 60
const val LOW_HEALTH = 30

//SHOT Constants:
const val SHOT_VEL = 10
const val SHOT_W = 4
const val SHOT_H = 8

const val GREY = 0xC0C0C0


/**
 * Draw the ship
 */
fun Canvas.drawShip(s:Ship){
    s.apply {
        drawRect(pos.x, pos.y, SHIP_W, SHIP_H, GREY)
        drawRect(pos.x + SHIP_PIXEL, pos.y - SHIP_PIXEL, SHIP_W - 2*SHIP_PIXEL, SHIP_PIXEL, GREY )
        drawRect(pos.x + 3*SHIP_PIXEL, pos.y - 3*SHIP_PIXEL, SHIP_PIXEL, 2*SHIP_PIXEL, GREY)
        drawRect(pos.x + SHIP_PIXEL, pos.y + SHIP_H, SHIP_W - 2*SHIP_PIXEL, SHIP_PIXEL, GREY )
        drawRect(pos.x + 2*SHIP_PIXEL, pos.y + SHIP_H + SHIP_PIXEL, 3*SHIP_PIXEL, SHIP_PIXEL, RED )
        drawRect(pos.x + 2*SHIP_PIXEL, pos.y + SHIP_H + 2*SHIP_PIXEL, 3*SHIP_PIXEL, SHIP_PIXEL, RED )
        drawRect(pos.x + 3*SHIP_PIXEL, pos.y + SHIP_H + 2*SHIP_PIXEL, SHIP_PIXEL, 3*SHIP_PIXEL, YELLOW )

        drawCircle(pos.x + SHIP_W/2, pos.y + SHIP_H/2, SHIP_PIXEL, RED)

        val barX = pos.x - 3*SHIP_PIXEL/2
        drawLine(barX, pos.y + SHIP_H + 6*SHIP_PIXEL, barX + 10*SHIP_PIXEL, pos.y + SHIP_H + 6*SHIP_PIXEL, WHITE, 4)
        drawLine(barX, pos.y + SHIP_H + 6*SHIP_PIXEL, barX + (s.health / 10)*SHIP_PIXEL, pos.y + SHIP_H + 6*SHIP_PIXEL, heath(), 4)
    }
}


/**
 * Draw the shot
 */
fun Canvas.drawShot(s:Shot){
    s.apply { drawRect(pos.x - SHOT_W/2, pos.y, SHOT_W, SHOT_H, YELLOW) }
}


/**
 * Move the shot
 */
fun Shot.move():Shot = Shot(Position(pos.x, pos.y - SHOT_VEL))


/**
 * Move the ship with the mouse
 */
fun Ship.move(me:MouseEvent):Ship{
    val newX = me.x - SHIP_W / 2
    val newY = me.y - SHIP_H / 2

    return copy(pos = Position(
        if (newX >= 0 && newX + SHIP_W <= WIDTH) newX else pos.x,
        if (newY >= HEIGHT/2 && newY + SHIP_H <= HEIGHT) newY else pos.y
    ))
}


/**
 * Firing
 */
fun Ship.shoot() = Shot(Position(pos.x + SHIP_W / 2, pos.y))


/**
 * Return the color of the health
 */
fun Ship.heath():Int = if (health >= MID_HEALTH) GREEN else if (health >= LOW_HEALTH) YELLOW else RED

