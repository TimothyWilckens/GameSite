package gymhum.de.model.tictactoe;

public class Spieler {
    boolean activeplayer;

    public Spieler(boolean spieler){
        setActiveplayer(spieler);
    }
    public void setActiveplayer(boolean activeplayer) {
        this.activeplayer = activeplayer;
    }
    public boolean getActiveplayer() {
        return activeplayer;
    }
}
