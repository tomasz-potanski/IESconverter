public class Pole {
    public void setNatezenie(double natezenie) {
        this.natezenie = natezenie;
    }

    public double getNatezenie() {
        return natezenie;
    }

    double natezenie = -1; //[lx]; -1 jako wartosc poczatkowa oznacza, ze wartosc nie zostala przypisana

    public Pole() {
    }



    public Pole(double natezenie) {
        this.natezenie = natezenie;
    }

}
