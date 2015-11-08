import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class PomieszczenieProstokatne extends Pomieszczenie {
    double dlugosc;
    double szerokosc;
    double wysokosc;

    public double getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(double dlugosc) {
        this.dlugosc = dlugosc;
    }

    public double getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(double szerokosc) {
        this.szerokosc = szerokosc;
    }

    public double getWysokosc() {
        return wysokosc;
    }

    public void setWysokosc(double wysokosc) {
        this.wysokosc = wysokosc;
    }

    public int getLiczba_plaszczyzn_dla_katow_C() {
        return liczba_plaszczyzn_dla_katow_C;
    }

    public void setLiczba_plaszczyzn_dla_katow_C(int liczba_plaszczyzn_dla_katow_C) {
        this.liczba_plaszczyzn_dla_katow_C = liczba_plaszczyzn_dla_katow_C;
    }

    public double getDelta_gamma() {
        return delta_gamma;
    }

    public void setDelta_gamma(double delta_gamma) {
        this.delta_gamma = delta_gamma;
    }

    public int getIl_punktow_wzdluz_dlugosci() {
        return il_punktow_wzdluz_dlugosci;
    }

    public void setIl_punktow_wzdluz_dlugosci(int il_punktow_wzdluz_dlugosci) {
        this.il_punktow_wzdluz_dlugosci = il_punktow_wzdluz_dlugosci;
    }

    public int getIl_punktow_wzdluz_szerokosci() {
        return il_punktow_wzdluz_szerokosci;
    }

    public void setIl_punktow_wzdluz_szerokosci(int il_punktow_wzdluz_szerokosci) {
        this.il_punktow_wzdluz_szerokosci = il_punktow_wzdluz_szerokosci;
    }

    public int getIlosc_opraw() {
        return ilosc_opraw;
    }

    public void setIlosc_opraw(int ilosc_opraw) {
        this.ilosc_opraw = ilosc_opraw;
    }

    public double getWsp_odb_podlogi() {
        return wsp_odb_podlogi;
    }

    public void setWsp_odb_podlogi(double wsp_odb_podlogi) {
        this.wsp_odb_podlogi = wsp_odb_podlogi;
    }

    public double getWsp_odb_scian() {
        return wsp_odb_scian;
    }

    public void setWsp_odb_scian(double wsp_odb_scian) {
        this.wsp_odb_scian = wsp_odb_scian;
    }

    public double getWsp_odb_sufitu() {
        return wsp_odb_sufitu;
    }

    public void setWsp_odb_sufitu(double wsp_odb_sufitu) {
        this.wsp_odb_sufitu = wsp_odb_sufitu;
    }

    public ArrayList<Oprawa> getOprawy() {
        return oprawy;
    }

    public void setOprawy(ArrayList<Oprawa> oprawy) {
        this.oprawy = oprawy;
    }

    public ArrayList<WspOprawy> getWspolrzedneOpraw() {
        return wspolrzedneOpraw;
    }

    public void setWspolrzedneOpraw(ArrayList<WspOprawy> wspolrzedneOpraw) {
        this.wspolrzedneOpraw = wspolrzedneOpraw;
    }

    public Siatka getSiatka() {
        return siatka;
    }

    public void setSiatka(Siatka siatka) {
        this.siatka = siatka;
    }

    int liczba_plaszczyzn_dla_katow_C = 10;
    double delta_gamma = 5.0; //[degrees]

    int il_punktow_wzdluz_dlugosci;
    int il_punktow_wzdluz_szerokosci;

    int ilosc_opraw;

    double wsp_odb_podlogi;
    double wsp_odb_scian;
    double wsp_odb_sufitu;

    ArrayList<Oprawa> oprawy = new ArrayList<Oprawa>();
    ArrayList<WspOprawy> wspolrzedneOpraw = new ArrayList<WspOprawy>();

    Siatka siatka;

    public double getIlluminanceFromField(int x, int y) {
        return siatka.getIlluminanceFromField(x, y);
    }

    protected PomieszczenieProstokatne(double length,
                                       double width,
                                       double height,
                                       int floorReflectionFactor,
                                       int wallReflectionFactor,
                                       int ceilingReflectionFactor,
                                       int xFieldAmount,
                                       int yFieldAmount,
                                       ArrayList<Oprawa> luminaires,
                                       ArrayList<WspOprawy> luminairesPositions
                                    ) {
        this.dlugosc = length;
        this.szerokosc = width;
        this.wysokosc = height;
        this.wsp_odb_podlogi = (double) floorReflectionFactor / 100.0;
        this.wsp_odb_scian = (double) wallReflectionFactor / 100.0;
        this.wsp_odb_sufitu = (double) ceilingReflectionFactor / 100.0;
        this.il_punktow_wzdluz_dlugosci = xFieldAmount;
        this.il_punktow_wzdluz_szerokosci = yFieldAmount;
        this.oprawy = luminaires;
        this.wspolrzedneOpraw = luminairesPositions;
        this.siatka = new Siatka(xFieldAmount, yFieldAmount);

    }

    protected PomieszczenieProstokatne(String path_to_csv) throws CalculationException {
        load_data_from_csv(path_to_csv);
    }

    public void setFieldIlluminance(int x, int y, double newIlluminanceValue) {
        siatka.setNatezenie(x, y, newIlluminanceValue);
    }

    public void showData() {
        System.out.printf(
                "Dlugosc pomieszczenia: %f\n", this.dlugosc);
        System.out.printf(
                "Szerokosc pomieszczenia: %f\n", this.szerokosc);
        System.out.printf(
                "Wysokosc pomieszczenia: %f\n", this.wysokosc);
        System.out.printf(
                "Wspolczynnik odbicia podlogi: %f\n", this.wsp_odb_podlogi);
        System.out.printf(
                "Wspolczynnik odbicia scian: %f\n", this.wsp_odb_scian);
        System.out.printf(
                "Wspolczynnik odbicia sufitu: %f\n", this.wsp_odb_sufitu);
        System.out.printf(
                "Liczba plaszczyzn dla katow C: %d\n", this.liczba_plaszczyzn_dla_katow_C);
        System.out.printf(
                "Delta gamma: %d\n", this.delta_gamma);
        System.out.printf(
                "Ilosc punktow wzdluz dlugosci: %d\n", this.il_punktow_wzdluz_dlugosci);
        System.out.printf(
                "Ilosc punktow wzdluz szerokosci: %d\n", this.il_punktow_wzdluz_szerokosci);
        System.out.printf(
                "Ilosc opraw: %d\n", this.ilosc_opraw);

        System.out.println("Wspolrzedne opraw:");
        for (WspOprawy w : wspolrzedneOpraw) {
            System.out.printf(
                    "X: %f, Y: %f, Z: %f\n",
                    w.pozycjaNaDlugosci,
                    w.pozycjaNaSzerokosci,
                    w.wysokosc);
        }
        System.out.printf("\n");

        siatka.wypisz();
    }

    public String returnCSVFromDataToString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%.2f;%.2f;%.2f;%d;%d;%d;%d;%.2f;%d;%d;%d;%.2f;%.2f;%.2f",
                dlugosc,
                szerokosc,
                wysokosc,
                Math.round(wsp_odb_podlogi * 100),
                Math.round(wsp_odb_scian * 100),
                Math.round(wsp_odb_sufitu * 100),
                liczba_plaszczyzn_dla_katow_C,
                delta_gamma,
                il_punktow_wzdluz_dlugosci,
                il_punktow_wzdluz_szerokosci,
                ilosc_opraw,
                wspolrzedneOpraw.get(0).pozycjaNaDlugosci,
                wspolrzedneOpraw.get(0).pozycjaNaSzerokosci,
                wspolrzedneOpraw.get(0).wysokosc)
        );

        int ile = il_punktow_wzdluz_dlugosci - 14 - 1;
        while (ile-- > 0) {
            sb.append(";");
        }
        sb.append("\r\n");

        //display the net!
        sb.append(siatka.returnNetAsString());

        return sb.toString();
    }

    public void show_csv_from_data() {
        System.out.printf("%.2f;%.2f;%.2f;%d;%d;%d;%d;%.2f;%d;%d;%d;%.2f;%.2f;%.2f",
                dlugosc,
                szerokosc,
                wysokosc,
                Math.round(wsp_odb_podlogi * 100),
                Math.round(wsp_odb_scian * 100),
                Math.round(wsp_odb_sufitu * 100),
                liczba_plaszczyzn_dla_katow_C,
                delta_gamma,
                il_punktow_wzdluz_dlugosci,
                il_punktow_wzdluz_szerokosci,
                ilosc_opraw,
                wspolrzedneOpraw.get(0).pozycjaNaDlugosci,
                wspolrzedneOpraw.get(0).pozycjaNaSzerokosci,
                wspolrzedneOpraw.get(0).wysokosc
        );

        int ile = il_punktow_wzdluz_dlugosci - 14 - 1;
        while (ile-- > 0) {
            System.out.printf(";");
        }
        System.out.printf("\n");

        //display the net!
        System.out.println(siatka.returnNetAsString());

    }

    public void load_data_from_csv(String path_to_csv) throws CalculationException {
        BufferedReader br = null;
        String cvsSplitBy = ";";
        String line = "";


        try {
            br = new BufferedReader(new FileReader(path_to_csv));
            line = br.readLine();
            String[] tabl = line.split(cvsSplitBy);

            this.dlugosc                        = Double.parseDouble(Main.changeCommaToDot(tabl[0]));
            this.szerokosc                      = Double.parseDouble(Main.changeCommaToDot(tabl[1]));
            this.wysokosc                       = Double.parseDouble(Main.changeCommaToDot(tabl[2]));
            this.wsp_odb_podlogi                = (double) Integer.parseInt(tabl[3]) / 100;
            this.wsp_odb_scian                  = (double) Integer.parseInt(tabl[4]) / 100;
            this.wsp_odb_sufitu                 = (double) Integer.parseInt(tabl[5]) / 100;
            this.liczba_plaszczyzn_dla_katow_C  = Integer.parseInt(tabl[6]);
            this.delta_gamma                    = (int) Double.parseDouble(Main.changeCommaToDot(tabl[7]));
            this.il_punktow_wzdluz_dlugosci     = Integer.parseInt(tabl[8]);
            this.il_punktow_wzdluz_szerokosci   = Integer.parseInt(tabl[9]);
            this.ilosc_opraw                    = Integer.parseInt(tabl[10]);

            WspOprawy noweWsp = new WspOprawy(
                    Double.parseDouble(Main.changeCommaToDot(tabl[11])),
                    Double.parseDouble(Main.changeCommaToDot(tabl[12])),
                    Double.parseDouble(Main.changeCommaToDot(tabl[13])));
            wspolrzedneOpraw.add(noweWsp);

            siatka = new Siatka(il_punktow_wzdluz_dlugosci, il_punktow_wzdluz_szerokosci);

            int akt_szer = 0;
            while ((line = br.readLine()) != null) {
                String[] tabl2 = line.split(cvsSplitBy);
                for (int i = 0; i < il_punktow_wzdluz_dlugosci; i++) {
                    siatka.setNatezenie(i, akt_szer, Double.parseDouble(Main.changeCommaToDot(tabl2[i])));
                }

                akt_szer++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CalculationException("PomeiszczenieProstokatne.load_data_from_csv() errror");

        }
    }
}
