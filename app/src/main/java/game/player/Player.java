package game.player;

/**
 * Created by Andrea on 28/03/2015.
 */
public class Player {

    private String name;
    private PlayerStatus status;

    public Player(PlayerStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name;
    }

}
