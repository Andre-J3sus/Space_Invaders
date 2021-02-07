import pt.isel.canvas.*

data class Game(val player:Ship, val shots:List<Shot>, val enemies :List<Enemy>, val score:Int, val playing:Boolean)

const val WIDTH = 400
const val HEIGHT = 600
const val TEXT_Y = HEIGHT - 10
const val TEXT_DISTANCE = 70
const val FONT_SIZE = 22


fun Canvas.drawGame(g:Game){
    erase()

    drawShip(g.player)
    g.shots.forEach { drawShot(it) }
    g.enemies.forEach { drawEnemy(it) }

    g.player.apply {
        drawText(2, TEXT_Y, "Health:$health", if (health >=MID_HEALTH) GREEN else if(health >= LOW_HEALTH) YELLOW else RED, FONT_SIZE) }
    drawText(WIDTH - TEXT_DISTANCE - 20*(g.score.toString().length), TEXT_Y, "Score:${g.score}", WHITE, FONT_SIZE)
}


fun Game.spawnEnemies() = copy(enemies = enemies + newEnemies())


fun Game.updateCooldown() = copy(player = player.copy(cooldown = player.cooldown + 1))


fun Game.move():Game{
    val movedShotsInWindow = shots.map { it.move() }.filter { it.pos.y > -SHOT_H}
    val movedEnemies = enemies.map { it.move() }
    val movedEnemiesInWindow = movedEnemies.filter { it.pos.y < HEIGHT}
    var healthLost = movedEnemies.filter { it.pos.y >= HEIGHT}.count()

    val damageEnemies = mutableListOf<Enemy>()
    val shotsThatCollide = mutableListOf<Shot>()

    var newScore = score

    movedEnemies.forEach { enemy ->
        movedShotsInWindow.forEach { shot ->
            if (shot.pos.y <= enemy.pos.y + ENEMY_SIDE && shot.pos.x in enemy.rangeX()){
                shotsThatCollide.add(shot)
                damageEnemies.add(enemy)
                ++newScore
            }
        }
        if (enemy.collides(player)) {
            damageEnemies.add(enemy)
            ++healthLost
        }
    }

    return copy(
        player = player.copy(health = player.health - DAMAGE*healthLost),
        shots = movedShotsInWindow - shotsThatCollide,
        enemies = movedEnemiesInWindow - damageEnemies,
        score = newScore
    )
}

fun Game.checkGameOver():Boolean = player.health <= 0

