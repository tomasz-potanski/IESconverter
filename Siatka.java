public class Siatka extends SiatkaAbstrakcyjna {
    int liczba_prost_na_dlugosci;
    int liczba_prost_na_szerokosci;

    Pole [][] pola;

    public double getIlluminanceFromField(int x, int y){
        if (x < 0 || y < 0 || x >= liczba_prost_na_dlugosci || y >= liczba_prost_na_szerokosci){
            return 0.0;
        } else {
            return pola[x][y].getNatezenie();
        }
    }

    public Siatka(int liczba_prost_na_dlugosci, int liczba_prost_na_szerokosci) {
        this.liczba_prost_na_dlugosci = liczba_prost_na_dlugosci;
        this.liczba_prost_na_szerokosci = liczba_prost_na_szerokosci;

        pola = new Pole[liczba_prost_na_dlugosci][liczba_prost_na_szerokosci];
        for (Pole[] u : pola){
            for (Pole p : u){
                p = new Pole();
            }
        }
    }

//    public double getNatezenie(int wsp_dlugosci, int wsp_szerokosci){
//        if (wsp_dlugosci < liczba_prost_na_dlugosci && wsp_szerokosci < liczba_prost_na_szerokosci){
//            return pola[wsp_dlugosci][wsp_szerokosci].getNatezenie();
//        } else {
//            //!
//            return -2;
//        }
//    }

    public void setNatezenie(int wsp_dlugosci, int wsp_szerokosci, double nowe_natezenie){
//        pola[wsp_dlugosci][wsp_szerokosci].setNatezenie(nowe_natezenie);
        if (wsp_dlugosci >= 0 && wsp_dlugosci < liczba_prost_na_dlugosci && wsp_szerokosci >= 0 && wsp_szerokosci < liczba_prost_na_szerokosci){
            try{
                pola[wsp_dlugosci][wsp_szerokosci] = new Pole();
                pola[wsp_dlugosci][wsp_szerokosci].setNatezenie(nowe_natezenie);
                if (pola[wsp_dlugosci][wsp_szerokosci] == null){
                    System.out.println("NULL!");
                }
            } catch (Exception e){
                System.out.println("Błąd!");
                e.printStackTrace();
    //            System.out.printf("dl: %d; szer: %d\n", wsp_dlugosci, wsp_szerokosci);
            }
        }
    }

    public String returnNetAsString(){
        StringBuilder sb = new StringBuilder();

        for (int akt_szer = 0 ; akt_szer < liczba_prost_na_szerokosci ; akt_szer++){
            for (int akt_dl = 0 ; akt_dl < liczba_prost_na_dlugosci ; akt_dl++){
                sb.append(String.format("%.2f", getIlluminanceFromField(akt_dl, akt_szer)));
                if (akt_dl + 1 < liczba_prost_na_dlugosci){
                    sb.append(";");
                }
            }
            sb.append("\r\n");
        }

        return sb.toString();
    }

    public void wypisz(){
        System.out.printf("Siatka o wymiarach %d na %d:\n\n", liczba_prost_na_dlugosci, liczba_prost_na_szerokosci);

        for (int akt_szer = 0 ; akt_szer < liczba_prost_na_szerokosci ; akt_szer++){
            for (int akt_dl = 0 ; akt_dl < liczba_prost_na_dlugosci ; akt_dl++){
                System.out.print(getIlluminanceFromField(akt_dl, akt_szer));
                System.out.print(" ");
            }
            System.out.printf("\n");
        }
    }
}
