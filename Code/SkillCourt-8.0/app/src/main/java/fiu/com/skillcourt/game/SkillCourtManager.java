package fiu.com.skillcourt.game;

/**
 * Created by pedrocarrillo on 10/4/16.
 */
public class SkillCourtManager {

    private SkillCourtGame game;

    private static SkillCourtManager ourInstance = new SkillCourtManager();

    public static SkillCourtManager getInstance() {
        return ourInstance;
    }

    private SkillCourtManager() {
        game = new SkillCourtGame();
    }

    public SkillCourtGame getGame() {
        return game;
    }

    //Game related methods
    public void newGame() {
        game = new SkillCourtGame();
    }

    public void restartGame() {
        game.restartGame();
    }

    public void cancelGame() {game.cancelGame();}

    public void addGreenPoint() {
        game.addPoint(1);
        game.addGreen(1);
        game.totalHit();
        game.greenHit();
    }

    public void addRedPoint() {
        game.addRed(1);
        game.subtractPoint(1);
        game.totalHit();
    }

}
