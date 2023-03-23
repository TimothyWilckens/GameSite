package gymhum.de;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

import gymhum.de.model.minesweeper.minefeld;
import gymhum.de.model.minesweeper.Ende;

@Controller
public class MineSweeperController {

    minefeld[][] minefelder;
    Ende ende;
    int expectedbombs;
    int totalbombs;
    minefeld[] bombs;
    boolean firstReveal;

    Random rand = new Random();
    
    public MineSweeperController(){
        setMinefelder(new minefeld[10][10]);
        setEnde(new Ende());
        setExpectedbombs(20); // Repräsentiert die Gesamtanzahl an Bomben auf dem Spielfeld
        setTotalbombs(0);
        setBombs(new minefeld[expectedbombs]);
        setFirstReveal(true);
        initFeldandBombs();
        Anzeige();
    }

    @GetMapping("/msStartseite")
    public String msStartseite(@RequestParam(name="activePage", required = false, defaultValue = "msStartseite") String activePage, Model model){
        model.addAttribute("activePage", "msStartseite");
        return "index.html";
    }

    @GetMapping("/minesweeper")
    public String MineSweeper(@RequestParam(name="activePage", required = false, defaultValue = "minesweeper") String activePage, Model model){
        model.addAttribute("activePage", "minesweeper");
        model.addAttribute("minefelder", getMinefelder());
        model.addAttribute("ende", getEnde());
        return "index.html";
    }

    @GetMapping("/reveal")
    public String reveal(@RequestParam(name="activePage", required = true, defaultValue = "minesweeper") String activePage, @RequestParam(name="hoehe", required = true) int hoehe, @RequestParam(name="breite", required = true) int breite, Model model){
        getMinefelder()[hoehe][breite].setIstFreigelegt(true);
        System.out.println("Feld " + hoehe + " " + breite +" wurde freigelegt");

        if(firstReveal == true){
            getMinefelder()[hoehe][breite].setIstFreigelegt(true);
            for(int c = -1; c <= 1; c++){
                for(int d = -1; d <= 1; d++){
                    try {
                    if(getMinefelder()[hoehe+c][breite+d].getIstBombe() == false){
                        getMinefelder()[hoehe+c][breite+d].setIstFreigelegt(true);
                    }
                    } catch (Exception e) {
                        System.out.println("Feld ist außerhalb der Grenze");
                    }
                }
            }
            setFirstReveal(false);
        }
        
        if(getMinefelder()[hoehe][breite].getIstBombe() == true){
            getEnde().setVerloren(true);
            for(int a = 0; a < 10; a++){
                for(int b = 0; b < 10; b ++){
                    if(getMinefelder()[a][b].getIstBombe() == true){
                        getMinefelder()[hoehe][breite].setIstFreigelegt(true);
                    }
                }
            }
        }
        return "redirect:/minesweeper";
    }

    @GetMapping("/newMinesweeper") 
    public String newMinesweeper(@RequestParam(name="activePage", required = true, defaultValue = "spiel") String activePage) {
        initFeldandBombs();
        getEnde().setGewonnen(false);
        getEnde().setVerloren(false);
        initFeldandBombs();
        Anzeige();
        setFirstReveal(true);
        System.out.println("Spiel Minesweeper wurde zurückgesetzt");
        return "redirect:/minesweeper";
    }

    private void initFeldandBombs(){
        int counter = 0;
        setTotalbombs(0);
        for(int h = 0; h < 10; h++){
            for(int b = 0; b < 10; b++){
                getMinefelder()[h][b] = new minefeld();
                getMinefelder()[h][b].setHoehe(h);
                getMinefelder()[h][b].setBreite(b);
                
                System.out.println("Minesweeper-Feld mit Höhe " + h + " und Breite " + b +" wurde erstellt " + counter);
                counter += 1;
            }
        }
        for(int i = 1; i <= (expectedbombs); i++){
            int randomhoehe = rand.nextInt(10);
            int randombreite = rand.nextInt(10);
            if(getMinefelder()[randomhoehe][randombreite].getIstBombe() == false){
                getMinefelder()[randomhoehe][randombreite].setIstBombe(true);
                System.out.println("Minesweeper-Feld mit Höhe " + randomhoehe + " und Breite " + randombreite +" wurde in Bombe " + i + " umgewandelt");
            }
        }
    }

    private void Anzeige(){
        for(int hoehe = 0; hoehe < 10; hoehe++){
            for(int breite = 0; breite < 10; breite ++){
                int bombsnearby = 0;
                for(int c = -1; c <= 1; c++){
                    for(int d = -1; d <= 1; d++){
                        try {
                        if(getMinefelder()[hoehe+c][breite+d].getIstBombe() == true){
                            bombsnearby += 1;
                        }
                        } catch (Exception e) {
                            System.out.println("ZU prüfendes Feld ist außerhalb der Grenze");
                        }
                    }
                }
                getMinefelder()[hoehe][breite].setNearBombs(bombsnearby);
                System.out.println("Minefeld " + hoehe + breite + " ist von " + bombsnearby + " umgeben");
            }
        }
    }

    // Setter und Getter
    public void setMinefelder(minefeld[][] minefelder) {
        this.minefelder = minefelder;
    }
    public void setEnde(Ende ende) {
        this.ende = ende;
    }
    public void setExpectedbombs(int expectedbombs) {
        this.expectedbombs = expectedbombs;
    }
    public void setTotalbombs(int totalbombs) {
        this.totalbombs = totalbombs;
    }
    public void setBombs(minefeld[] bombs) {
        this.bombs = bombs;
    }
    public void setFirstReveal(boolean firstReveal) {
        this.firstReveal = firstReveal;
    }
    public minefeld[][] getMinefelder() {
        return minefelder;
    }
    public Ende getEnde() {
        return ende;
    }
    public int getExpectedbombs() {
        return expectedbombs;
    }
    public int getTotalbombs() {
        return totalbombs;
    }
    public minefeld[] getBombs() {
        return bombs;
    }
    public boolean getFirstReveal() {
        return firstReveal;
    }
}
