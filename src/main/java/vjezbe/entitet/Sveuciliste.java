package vjezbe.entitet;

import java.util.ArrayList;
import java.util.List;

public class Sveuciliste <T extends ObrazovnaUstanova>{
    private List<T> obrazovneUstanove;

    public Sveuciliste() {
        this.obrazovneUstanove = new ArrayList<>();
    }

    public void dodajObrazovnuUstanovu(T object) {
        obrazovneUstanove.add(object);
    }

    public T dohvatiObrazovnuUstanovu(int i) {
        return obrazovneUstanove.get(i);
    }

    public List<T> getObrazovneUstanove() {
        return new ArrayList<>(obrazovneUstanove);
    }
}
