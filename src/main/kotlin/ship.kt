import pt.isel.canvas.*

data class Position(val x:Int, val y:Int)

data class Ship(val pos:Position, val cooldown:Int, val health:Int)

data class Shot(val pos: Position)

//SHIP Constants:
const val SHIP_W = 44
const val SHIP_H = 20
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

fun Ship.rangeX() = pos.x .. pos.x + SHIP_H

fun Canvas.drawShip(s:Ship){
    s.apply {
        drawRect(pos.x, pos.y, SHIP_W, SHIP_H, GREEN)
        drawRect(pos.x + SHIP_PIXEL, pos.y - SHIP_PIXEL, SHIP_W - 2*SHIP_PIXEL, SHIP_PIXEL, GREEN )
        drawRect(pos.x + 5*SHIP_PIXEL, pos.y - 3*SHIP_PIXEL, SHIP_PIXEL, 2*SHIP_PIXEL, GREEN)
    }
}


fun Canvas.drawShot(s:Shot){
    s.apply { drawRect(pos.x - SHOT_W/2, pos.y, SHOT_W, SHOT_H, YELLOW) }
}


fun Shot.move():Shot = Shot(Position(pos.x, pos.y - SHOT_VEL))


fun Ship.move(me:MouseEvent):Ship{
    val newX = me.x - SHIP_W / 2
    val newY = me.y - SHIP_H / 2

    return Ship(
        Position(
            if (newX >= 0 && newX + SHIP_W <= WIDTH) newX else pos.x,
            if (newY >= 0 && newY + SHIP_H <= HEIGHT) newY else pos.y),
        cooldown,
        health
    )
}


fun Ship.shoot() = Shot(Position(pos.x + SHIP_W / 2, pos.y))
