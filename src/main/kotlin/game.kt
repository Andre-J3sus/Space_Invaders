import pt.isel.canvas.*

data class Game(
    val player:Ship, //Player
    val shots:List<Shot>, //Shots in thw window
    val enemies :List<Enemy>, //Enemies in the window
    val score:Int, //Total score
    val playing:Boolean, //State os the game
    val sound:Boolean //Sound on/off
    )

//Game constants:
const val WIDTH = 400
const val HEIGHT = 600
const val TEXT_Y = HEIGHT - 10
const val TEXT_DISTANCE = 70
const val FONT_SIZE = 22

/**
 * Draw the game
 */
fun Canvas.drawGame(g:Game){
    erase()
    drawShip(g.player)

    if (!g.playing) {
        drawText(WIDTH / 5 + 20, HEIGHT / 2, "PRESS \"P\" TO PLAY!", WHITE, 20)

        drawEnemy(Enemy(Position(WIDTH/2 - ENEMY_SIDE/2, HEIGHT/4), CYAN))
        drawEnemy(Enemy(Position(WIDTH/2 - ENEMY_SIDE/2, HEIGHT/6), MAGENTA))
        drawEnemy(Enemy(Position(WIDTH/2 - ENEMY_SIDE/2, HEIGHT/3), GREEN))
        drawEnemy(Enemy(Position(WIDTH/3 - ENEMY_SIDE/2, HEIGHT/4), YELLOW))
        drawEnemy(Enemy(Position(2*WIDTH/3 - ENEMY_SIDE/2, HEIGHT/4), RED))
    }
    else {
        g.shots.forEach { drawShot(it) }
        g.enemies.forEach { drawEnemy(it) }

        g.player.apply {
            drawText(2, TEXT_Y, "Health:$health", g.player.heath(), FONT_SIZE)
        }
        drawText(WIDTH - TEXT_DISTANCE - 20 * (g.score.toString().length), TEXT_Y, "Score:${g.score}", WHITE, FONT_SIZE)
    }
}


/**
 * Starting game conditions
 */
fun startingGame() = Game( // Variable game
    player = Ship(Position(WIDTH/2 - SHIP_W/2, STARTING_Y), MAX_COOLDOWN, MAX_HEALTH), //Starting ship
    shots = emptyList(), //Zero shots
    enemies = emptyList(), //Zero Enemies
    score = 0, //Zero score
    playing = false,
    sound = true
)


/**
 * Spawn enemies randomly
 */
fun Game.spawnEnemies() = copy(enemies = enemies + newEnemies())


/**
 * Increasing the shoot cooldown
 */
fun Game.updateCooldown() = copy(player = player.copy(cooldown = player.cooldown + 1))


/**
 * Checking if the game is over
 */
fun Game.checkGameOver():Boolean = player.health <= 0


/**
 * Moving the game
 */
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
                if (sound) playSound("invaderkilled")
            }
        }
        if (enemy.collides(player)) {
            damageEnemies.add(enemy)
            ++healthLost
            if (sound) playSound("invaderkilled")
        }
    }

    return copy(
        player = player.copy(health = player.health - DAMAGE * healthLost),
        shots = movedShotsInWindow - shotsThatCollide,
        enemies = movedEnemiesInWindow - damageEnemies,
        score = newScore
    )
}
