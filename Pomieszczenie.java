
public abstract class Pomieszczenie {

    protected Pomieszczenie(/*double wsp_odb_podlogi, double wsp_odb_scian, double wsp_odb_sufitu*/) {
//        this.wsp_odb_podlogi = wsp_odb_podlogi;
//        this.wsp_odb_scian = wsp_odb_scian;
//        this.wsp_odb_sufitu = wsp_odb_sufitu;
    }

    public double getWsp_odb_podlogi() {
        return wsp_odb_podlogi;
    }

    public double getWsp_odb_scian() {
        return wsp_odb_scian;
    }

    public double getWsp_odb_sufitu() {
        return wsp_odb_sufitu;
    }

    public void setWsp_odb_podlogi(double wsp_odb_podlogi) {
        this.wsp_odb_podlogi = wsp_odb_podlogi;
    }

    public void setWsp_odb_scian(double wsp_odb_scian) {
        this.wsp_odb_scian = wsp_odb_scian;
    }

    public void setWsp_odb_sufitu(double wsp_odb_sufitu) {
        this.wsp_odb_sufitu = wsp_odb_sufitu;
    }

    double wsp_odb_podlogi;
    double wsp_odb_scian;
    double wsp_odb_sufitu;

//    SiatkaAbstrakcyjna siatka;

}
