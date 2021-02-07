import pt.isel.canvas.*


fun main() {
    onStart {
        val arena = Canvas(WIDTH, HEIGHT, BLACK)
        var game = Game(
            player = Ship(Position(WIDTH/2, STARTING_Y), MAX_COOLDOWN, MAX_HEALTH),
            shots = emptyList(),
            enemies = newEnemies(),
            score = 0,
            playing = true
        )

        arena.onTimeProgress(10){
            if (game.checkGameOver()) game = game.copy(playing = false)
            if (game.playing) game = game.move()
            arena.drawGame(game)
        }

        arena.onTimeProgress(200){ game = game.updateCooldown() } //Update shot cooldown each 0.2s (200ms)

        arena.onTimeProgress(1000){ game = game.spawnEnemies() } //Spawn enemies each second

        arena.onMouseMove { me-> if (game.playing) game = game.copy(player = game.player.move(me)) } //Move the player

        arena.onKeyPressed { ke-> // Shoot when "Space" key is pressed and the cooldown hits the MAX_COOLDOWN
            if (ke.text == "Space" && game.player.cooldown >= MAX_COOLDOWN && game.playing)
                game = game.copy(player = game.player.copy(cooldown = 0),shots = game.shots + game.player.shoot())
        }
    }
    onFinish { println("Done!") }
}
