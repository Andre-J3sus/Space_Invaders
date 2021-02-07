import pt.isel.canvas.*

/**
 * My version of the classic game Space Invaders in kotlin.
 * The player can move the ship with the mouse and shoot with the space key.
 */
fun main() {
    onStart {
        loadSounds("shoot", "invaderkilled") //Loading game sounds

        val arena = Canvas(WIDTH, HEIGHT, BLACK) //Canvas with size and color

        var game = startingGame()

        arena.onTimeProgress(10){ //Each 10ms:
            game = when{
                game.checkGameOver() -> startingGame() //If the game is over, the game restarts
                game.playing         -> game.move() //If playing is true, the game moves
                else -> game
            }

            arena.drawGame(game) //Draw the game
        }

        arena.onTimeProgress(200){ if (game.playing) game = game.updateCooldown() } //Update shot cooldown each 0.2s (200ms)

        arena.onTimeProgress(1500){ if (game.playing) game = game.spawnEnemies() } //Spawn enemies each 1,5 seconds

        arena.onMouseMove { me-> if (game.playing) game = game.copy(player = game.player.move(me)) } //Move the player

        arena.onKeyPressed { ke->
            when(ke.text){
                // Shoot when "Space" key is pressed and the cooldown hits the MAX_COOLDOWN
                "Space" ->
                    if (game.player.cooldown >= MAX_COOLDOWN && game.playing) {
                        game = game.copy(player = game.player.copy(cooldown = 0), shots = game.shots + game.player.shoot())
                        if (game.sound) playSound("shoot")
                    }

                // Play/Pause the game
                "P"  -> game = game.copy(playing = !game.playing)

                // Toggle the sound
                "S" -> game = game.copy(sound = !game.sound)
            }
        }
    }

    onFinish { println("Done!") }
}
