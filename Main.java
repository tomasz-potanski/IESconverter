import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class Main {

    public static Integer NUM = 1;
    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy";

    public static Oprawa calculatedLuminaire;
    public static PomieszczenieProstokatne calculatedRoom, actualRoom;
    public static Boolean isLuminaireProperlyCalculated = false;
    public static Boolean isRoomProperlyCalculated = false;


    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Form1 ex = new Form1();
                ex.setVisible(true);
            }
        });
    }

    public static String changeCommaToDot(String previousString) {
        int commaPosition = previousString.indexOf(",");
        String newString;
        if (commaPosition != -1) {
            //there is no need to use StringBuilder class
            newString = previousString.substring(0, commaPosition) +
                    "." + previousString.substring(commaPosition + 1);
        } else {
            newString = previousString;
        }

        return newString;
    }

    public static PomieszczenieProstokatne bryla_na_siatke(String path_to_ies, Pomieszczenie pomieszczenie) throws CalculationException {
        try {
            if (PomieszczenieProstokatne.class.isAssignableFrom(pomieszczenie.getClass())) {
                PomieszczenieProstokatne pom1           = (PomieszczenieProstokatne) pomieszczenie;

                //getting data needed to conduct calculation form Pomieszczenie instance
                int xFieldAmount                        = pom1.getIl_punktow_wzdluz_dlugosci();
                int yFieldAmount                        = pom1.getIl_punktow_wzdluz_szerokosci();
                double length                           = pom1.getDlugosc();
                double width                            = pom1.getSzerokosc();
                double height                           = pom1.getWysokosc();
                double fieldLength                      = length / xFieldAmount;
                double fieldWidth                       = width / yFieldAmount;
                double xLuminairePosition               = pom1.getWspolrzedneOpraw().get(0).pozycjaNaDlugosci;
                double yLuminairePosition               = pom1.getWspolrzedneOpraw().get(0).pozycjaNaSzerokosci;
                double zLuminairePosition               = pom1.getWspolrzedneOpraw().get(0).wysokosc;

                Oprawa opr1                             = new Oprawa(path_to_ies);
                double actualCAngle                     = 0.0;
                double actualGammaAngle                 = 0.0;

                //Algorithm iterates through all fields
                for (int actualYNumber = 0; actualYNumber < yFieldAmount; actualYNumber++) {
                    for (int actualXNumber = 0; actualXNumber < xFieldAmount; actualXNumber++) {
                        //calculating (x,y) position from field numbers
                        double actualXPosition  = actualXNumber * fieldLength + fieldLength / 2.0;
                        double actualYPosition  = actualYNumber * fieldWidth + fieldWidth / 2.0;


                        //calculating delta between actual position for fixed field and position of the luminaire
                        //luminaires position minus center of field
                        double dX               = xLuminairePosition - actualXPosition;
                        double dY               = yLuminairePosition - actualYPosition;
                        double r                = Math.sqrt(dY * dY + dX * dX);

                        //calculating gamma and C angles out of dX and dY
                        if (dX == 0.0 && dY == 0.0) {
                            actualCAngle = 0.0;
                            actualGammaAngle = 0.0;
                        } else if (dX == 0.0) {
                            if (dY > 0.0) {
                                actualCAngle = 90.0;
                            } else {
                                actualCAngle = 270.0;
                            }
                            actualGammaAngle = Math.atan(r / height) * 180.0 / Math.PI;
                        } else if (dY == 0.0) {
                            if (dX > 0.0) {
                                actualCAngle = 0.0;
                            } else {
                                actualCAngle = 180.0;
                            }
                            actualGammaAngle = Math.atan(r / height) * 180.0 / Math.PI;
                        } else {
                            actualGammaAngle = Math.atan(r / height) * 180.0 / Math.PI;
                            if (dX > 0 && dY > 0) {
                                actualCAngle = Math.atan(dY / dX) * 180.0 / Math.PI;
                            } else if (dX > 0 && dY < 0) {
                                actualCAngle = 360.0 + Math.atan(dY / dX) * 180.0 / Math.PI;
                            } else if (dY > 0 && dX < 0) {
                                actualCAngle = 180.0 + Math.atan(dY / dX) * 180.0 / Math.PI;
                            } else if (dX < 0 && dY < 0) {
                                actualCAngle = 180.0 + Math.atan(dY / dX) * 180.0 / Math.PI;
                            } else {
                                throw new CalculationException("unreachable place!");
                            }
                        }

                        //candela value will depend on the the symmetry of luminaire
                        double calculatedAverageCandelaValue = 0.0;
                        //it is a segment between center of the luminaire and center of actual field
                        double dL2 = dX * dX + dY * dY + zLuminairePosition * zLuminairePosition;

                        //forbidden values of gamma and C
                        if (actualGammaAngle < 0.0 || actualCAngle < 0.0) {
                            throw new CalculationException("This value of gamma is not permited in this fixed experiment");
                        }

                        //algorithm checks the type of luminaire's symmetry
                        if (opr1.getNumber_of_horizontal_angles() == 1 &&
                                opr1.getC_angles().get(0) == 0.0) {
                            //luminaire is laterally symmetric in all planes
                            if (opr1.getGamma_angles().contains(actualGammaAngle)) {
                                //get value from angles
                                calculatedAverageCandelaValue = opr1.getCandela_values().get(0.0).get(opr1.getGamma_angles().indexOf(actualGammaAngle));
                            } else {
                                int firstGreater = Arrays.binarySearch(opr1.getGamma_angles().toArray(), actualGammaAngle);
                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dGamma = opr1.getGamma_angles().get(firstGreater) -
                                        opr1.getGamma_angles().get(firstGreater - 1);
                                //weights/importances
                                double m1 =
                                        (opr1.getGamma_angles().get(firstGreater) -
                                                actualGammaAngle) / dGamma;
                                double m2 =
                                        (actualGammaAngle -
                                                opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;

                                calculatedAverageCandelaValue =
                                        (opr1.getCandela_values().get(0.0).get(firstGreater) * m2 +
                                                m1 * opr1.getCandela_values().get(0.0).get(firstGreater - 1));
                            }
                        } else if (opr1.getC_angles().get(opr1.getC_angles().size() - 1) == 90.0) {
                            //luminaire is symmetric in each quadrant
                            if (opr1.getGamma_angles().contains(actualGammaAngle)) {
                                if (opr1.getC_angles().contains(actualCAngle)) {
                                    //algorithm gets value explicitly from inner structures
                                    calculatedAverageCandelaValue = opr1.getCandela_values().get(actualCAngle).get(opr1.getGamma_angles().indexOf(actualGammaAngle));
                                } else {
                                    //calculating corresponding C angle in the first quadrant of the Cartesian coordinate system
                                    //it stems from this symmetric of luminaire
                                    double actualCAngleInFirstQuardrant = actualCAngle;
                                    if (actualCAngle > 90.0 && actualCAngle <= 180.0) {
                                        actualCAngleInFirstQuardrant = 180.0 - actualCAngle;
                                    } else if (actualCAngle > 180.0 && actualCAngle <= 270.0) {
                                        actualCAngleInFirstQuardrant = actualCAngle - 180.0;
                                    } else if (actualCAngle > 270.0 && actualCAngle < 360.0) {
                                        actualCAngleInFirstQuardrant = 90.0 - (actualCAngle - 270.0);
                                    } else {
                                        throw new CalculationException("weird C angle");
                                    }

                                    //calculating siblings for C angle
                                    int firstGreater    = Arrays.binarySearch(opr1.getC_angles().toArray(), actualCAngleInFirstQuardrant);
                                    firstGreater++;
                                    firstGreater = -firstGreater;
                                    double dC           = opr1.getC_angles().get(firstGreater) - opr1.getC_angles().get(firstGreater - 1);
                                    double m1           = (opr1.getC_angles().get(firstGreater) - actualCAngleInFirstQuardrant) / dC;
                                    double m2           = (actualCAngleInFirstQuardrant - opr1.getC_angles().get(firstGreater - 1)) / dC;
                                    //weights/importances
                                    calculatedAverageCandelaValue = (opr1.getCandela_values().get(firstGreater).get(opr1.getGamma_angles().indexOf(actualGammaAngle)) * m2 +
                                            m1 * opr1.getCandela_values().get(firstGreater - 1).get(opr1.getGamma_angles().indexOf(actualGammaAngle)));
                                    //gamma is given explicitly
                                }
                            } else {
                                if (opr1.getC_angles().contains(actualCAngle)) {
                                    //C is given explicitly
                                    //find siblings for gamma
                                    int firstGreater    = Arrays.binarySearch(opr1.getGamma_angles().toArray(), actualGammaAngle);
                                    firstGreater++;
                                    firstGreater        = -firstGreater;
                                    double dGamma       = opr1.getGamma_angles().get(firstGreater) - opr1.getGamma_angles().get(firstGreater - 1);
                                    double m1           = (opr1.getGamma_angles().get(firstGreater) - actualGammaAngle) / dGamma;
                                    double m2           = (actualGammaAngle - opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;
                                    //weights/imprtances
                                    calculatedAverageCandelaValue = (opr1.getCandela_values().get(actualCAngle).get(firstGreater) * m2 +
                                            m1 * opr1.getCandela_values().get(actualCAngle).get(firstGreater - 1));
                                } else {
                                    //find sibling for C and gamma

                                    //gamma
                                    int firstGreater        = Arrays.binarySearch(opr1.getGamma_angles().toArray(),
                                            actualGammaAngle);

                                    firstGreater++;
                                    firstGreater            = -firstGreater;
                                    double dGamma           = opr1.getGamma_angles().get(firstGreater) -
                                            opr1.getGamma_angles().get(firstGreater - 1);

                                    double m1               = (opr1.getGamma_angles().get(firstGreater) -
                                            actualGammaAngle) / dGamma;

                                    double m2               = (actualGammaAngle -
                                            opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;

                                    //C
                                    double actualCAngleInFirstQuardrant = actualCAngle;
                                    if (actualCAngle > 90.0 && actualCAngle <= 180.0) {
                                        actualCAngleInFirstQuardrant    = 180.0 - actualCAngle;
                                    } else if (actualCAngle > 180.0 && actualCAngle <= 270.0) {
                                        actualCAngleInFirstQuardrant    = actualCAngle - 180.0;
                                    } else if (actualCAngle > 270.0 && actualCAngle < 360.0) {
                                        actualCAngleInFirstQuardrant    = 90.0 - (actualCAngle - 270.0);
                                    } else {
                                        throw new CalculationException("weird C angle");
                                    }

                                    int firstGreaterC = Arrays.binarySearch(opr1.getC_angles().toArray(),
                                            actualCAngleInFirstQuardrant);

                                    firstGreaterC++;
                                    firstGreaterC = -firstGreaterC;
                                    double dC = opr1.getC_angles().get(firstGreater) -
                                            opr1.getC_angles().get(firstGreater - 1);

                                    double m1C = (opr1.getC_angles().get(firstGreater) -
                                            actualCAngleInFirstQuardrant) / dC;

                                    double m2C = (actualCAngleInFirstQuardrant -
                                            opr1.getC_angles().get(firstGreater - 1)) / dC;

                                    calculatedAverageCandelaValue = (opr1.getCandela_values().get(firstGreaterC).get(firstGreater) * m2 * m2C +
                                            m2C * m1 * opr1.getCandela_values().get(firstGreaterC).get(firstGreater - 1) +
                                            opr1.getCandela_values().get(firstGreaterC - 1).get(firstGreater) * m2 * m1C +
                                            m1C * m1 * opr1.getCandela_values().get(firstGreaterC - 1).get(firstGreater - 1));

                                }
                            }
                        } else if (opr1.getC_angles().get(opr1.getNumber_of_horizontal_angles() - 1) == 180.0) {
                            //luminaire is assumed to be symmetric about the 0 to 180 degree plane
                            if (opr1.getGamma_angles().contains(actualGammaAngle) && opr1.getC_angles().contains(actualCAngle)) {
                                //C and gamma explicitly
                                calculatedAverageCandelaValue =
                                        opr1.getCandela_values().get(actualCAngle).get(opr1.getGamma_angles().indexOf(actualGammaAngle));
                            } else if (opr1.getGamma_angles().contains(actualGammaAngle)) {
                                //gamma explicitly; siblings for C

                                double actualCAngleInFirstOrSecondQuarter = 360 - actualCAngle;
                                int firstGreater = Arrays.binarySearch(opr1.getC_angles().toArray(),
                                        actualCAngleInFirstOrSecondQuarter);

                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dC = opr1.getC_angles().get(firstGreater) -
                                        opr1.getC_angles().get(firstGreater - 1);

                                double m1 = (opr1.getC_angles().get(firstGreater) -
                                        actualCAngleInFirstOrSecondQuarter) / dC;

                                double m2 = (actualCAngleInFirstOrSecondQuarter -
                                        opr1.getC_angles().get(firstGreater - 1)) / dC;

                                //weights
                                calculatedAverageCandelaValue = (opr1.getCandela_values().get(firstGreater).get(opr1.getGamma_angles().indexOf(actualGammaAngle)) * m2 +
                                        m1 * opr1.getCandela_values().get(firstGreater - 1).get(opr1.getGamma_angles().indexOf(actualGammaAngle)));

                            } else if (opr1.getC_angles().contains(actualCAngle)) {
                                //C explicitly, siblings for gamma
                                int firstGreater = Arrays.binarySearch(opr1.getGamma_angles().toArray(),
                                        actualGammaAngle);

                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dGamma = opr1.getGamma_angles().get(firstGreater) -
                                        opr1.getGamma_angles().get(firstGreater - 1);

                                double m1 = (opr1.getGamma_angles().get(firstGreater) -
                                        actualGammaAngle) / dGamma;

                                double m2 = (actualGammaAngle -
                                        opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;

                                //weights
                                calculatedAverageCandelaValue = (opr1.getCandela_values().get(actualCAngle).get(firstGreater) * m2 +
                                        m1 * opr1.getCandela_values().get(actualCAngle).get(firstGreater - 1));

                            } else {
                                //siblings for gamma and C
                                double actualCAngleInFirstOrSecondQuarter = 360 - actualCAngle;
                                int firstGreaterC = Arrays.binarySearch(opr1.getC_angles().toArray(),
                                        actualCAngleInFirstOrSecondQuarter);

                                firstGreaterC++;
                                firstGreaterC = -firstGreaterC;
                                double dC = opr1.getC_angles().get(firstGreaterC) -
                                        opr1.getC_angles().get(firstGreaterC - 1);

                                double m1C = (opr1.getC_angles().get(firstGreaterC) -
                                        actualCAngleInFirstOrSecondQuarter) / dC;

                                double m2C = (actualCAngleInFirstOrSecondQuarter -
                                        opr1.getC_angles().get(firstGreaterC - 1)) / dC;

                                //gamma
                                int firstGreater = Arrays.binarySearch(opr1.getGamma_angles().toArray(),
                                        actualGammaAngle);

                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dGamma = opr1.getGamma_angles().get(firstGreater) -
                                        opr1.getGamma_angles().get(firstGreater - 1);

                                double m1 = (opr1.getGamma_angles().get(firstGreater) -
                                        actualGammaAngle) / dGamma;

                                double m2 = (actualGammaAngle -
                                        opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;

                                //weights
                                calculatedAverageCandelaValue = (opr1.getCandela_values().get(firstGreaterC).get(firstGreater) * m2C * m2 +
                                        m1C * m2 * opr1.getCandela_values().get(firstGreaterC - 1).get(firstGreater) +
                                        opr1.getCandela_values().get(firstGreaterC).get(firstGreater - 1) * m2C * m1 +
                                        m1C * m1 * opr1.getCandela_values().get(firstGreaterC - 1).get(firstGreater - 1));
                            }
                        } else if (opr1.getC_angles().get(opr1.getNumber_of_horizontal_angles() - 1) == 360.0) {
                            //luminaire is assumed to exhibit no lateral symmetry
                            if (opr1.getGamma_angles().contains(actualGammaAngle) &&
                                    opr1.getC_angles().contains(actualCAngle)) {
                                //get candela value expliciltly
                                calculatedAverageCandelaValue = opr1.getCandela_values().get(actualCAngle).get(opr1.getGamma_angles().indexOf(actualGammaAngle));
                            } else if (opr1.getGamma_angles().contains(actualGammaAngle)) {
                                //gamma explicitly; siblings for C
                                int firstGreaterC = Arrays.binarySearch(opr1.getC_angles().toArray(),
                                        actualCAngle);
                                firstGreaterC++;
                                firstGreaterC = -firstGreaterC;
                                double dC = opr1.getC_angles().get(firstGreaterC) -
                                        opr1.getC_angles().get(firstGreaterC - 1);

                                double m1C = (opr1.getC_angles().get(firstGreaterC) -
                                        actualCAngle) / dC;

                                double m2C = (actualCAngle -
                                        opr1.getC_angles().get(firstGreaterC - 1)) / dC;

                                calculatedAverageCandelaValue = (opr1.getCandela_values().get(opr1.getC_angles().get(firstGreaterC)).get(opr1.getGamma_angles().indexOf(actualGammaAngle)) * m2C +
                                        m1C * opr1.getCandela_values().get(opr1.getC_angles().get(firstGreaterC - 1)).get(opr1.getGamma_angles().indexOf(actualGammaAngle)));

                            } else if (opr1.getC_angles().contains(actualCAngle)) {
                                //C explicitly, siblings for gamma
                                int firstGreater = Arrays.binarySearch(opr1.getGamma_angles().toArray(),
                                        actualGammaAngle);

                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dGamma = opr1.getGamma_angles().get(firstGreater) -
                                        opr1.getGamma_angles().get(firstGreater - 1);

                                double m1 = (opr1.getGamma_angles().get(firstGreater) -
                                        actualGammaAngle) / dGamma;

                                double m2 = (actualGammaAngle -
                                        opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;
                                //weights
                                calculatedAverageCandelaValue = (opr1.getCandela_values().get(actualCAngle).get(firstGreater) * m2 +
                                        m1 * opr1.getCandela_values().get(actualCAngle).get(firstGreater - 1));
                            } else {
                                //siblings for gamma and C
                                int firstGreaterC = Arrays.binarySearch(opr1.getC_angles().toArray(),
                                        actualCAngle);

                                firstGreaterC++;
                                firstGreaterC = -firstGreaterC;
                                double dC = opr1.getC_angles().get(firstGreaterC) -
                                        opr1.getC_angles().get(firstGreaterC - 1);

                                double m1C = (opr1.getC_angles().get(firstGreaterC) -
                                        actualCAngle) / dC;
                                double m2C = (actualCAngle -
                                        opr1.getC_angles().get(firstGreaterC - 1)) / dC;

                                //gamma
                                int firstGreater = Arrays.binarySearch(opr1.getGamma_angles().toArray(),
                                        actualGammaAngle);

                                firstGreater++;
                                firstGreater = -firstGreater;
                                double dGamma = opr1.getGamma_angles().get(firstGreater) -
                                        opr1.getGamma_angles().get(firstGreater - 1);

                                double m1 = (opr1.getGamma_angles().get(firstGreater) -
                                        actualGammaAngle) / dGamma;

                                double m2 = (actualGammaAngle -
                                        opr1.getGamma_angles().get(firstGreater - 1)) / dGamma;

                                //weights
                                calculatedAverageCandelaValue =
                                        opr1.getCandela_values().get(opr1.getC_angles().get(firstGreaterC)).
                                                get(firstGreater) * m2C * m2;
                                calculatedAverageCandelaValue +=
                                        m1C * m2 * opr1.getCandela_values().get(opr1.getC_angles().
                                                get(firstGreaterC - 1)).get(firstGreater);
                                calculatedAverageCandelaValue +=
                                        opr1.getCandela_values().get(opr1.getC_angles().get(firstGreaterC)).
                                                get(firstGreater - 1) * m2C * m1;
                                calculatedAverageCandelaValue +=
                                        m1C * m1 * opr1.getCandela_values().get(opr1.getC_angles().
                                                get(firstGreaterC - 1)).get(firstGreater - 1);
                            }
                        }

                        //the inverse square law
                        pom1.setFieldIlluminance(
                                actualXNumber,
                                actualYNumber,
                                calculatedAverageCandelaValue * Math.cos(actualGammaAngle * Math.PI / 180.0) / dL2);
                    }
                }
                Main.calculatedRoom = pom1;
                Main.isRoomProperlyCalculated = true;

                return pom1;
            } else {
                throw new CalculationException("Not allowed type of class");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CalculationException("IES ->> Illuminance distribution calculation error");
        }
    }

    public static Oprawa siatka_na_bryle(String path_to_csv) throws CalculationException {
        try {
            PomieszczenieProstokatne pom1   = new PomieszczenieProstokatne(path_to_csv);
            Oprawa opr1                     = new Oprawa();

            //setting required keywords for luminaire
            opr1.setTEST("GEN" + NUM.toString());
            opr1.setTESTLAB("Generated by program");

            //current date
            Calendar cal            = Calendar.getInstance();
            SimpleDateFormat sdf    = new SimpleDateFormat(DATE_FORMAT_NOW);
            opr1.setISSUEDATE(sdf.format(cal.getTime()));
            opr1.setMANUFAC("Unknown");

            //for later development: program may ask for additional keywords

            opr1.setTILT("NONE");

            opr1.setNumberOfLamps(1);

            //we did not ask user
            opr1.setLumensPerLamp(-1.0);

            opr1.setMultiplaying_factor(1.0);

            //calculating amount of gamma angles
            int verticalAnglesAmount    = 0;
            double actualVerticalAngle  = 0.0;

            opr1.setNumber_of_horizontal_angles(pom1.getLiczba_plaszczyzn_dla_katow_C() + 1);
            opr1.setPhotometricType(1); //C-gamma
            opr1.setType_of_unit(2); //meters

            //we don't know dimensions and they seem not to be important
            opr1.setWidth(0.0);
            opr1.setLength(0.0);
            opr1.setHeight(0.0);

            opr1.setBalastFactor(1.0);
            //ommiting future use factor

            //we don't know it at this moment
            opr1.setInputWatts(-1.0);

            //adding gamma angles to inner structures of luminaire class instance
            actualVerticalAngle = 0.0;
            while (actualVerticalAngle < 90.0) {
                opr1.addVerticalAngle(actualVerticalAngle);
                verticalAnglesAmount++;
                actualVerticalAngle += pom1.getDelta_gamma();
            }
            verticalAnglesAmount++;
            opr1.addVerticalAngle(90.0);
            opr1.setNumber_of_vertical_angles(verticalAnglesAmount);

            //adding C angle to inner structures of luminaire class instance
            opr1.addC_angle(0.0);
            if (pom1.getLiczba_plaszczyzn_dla_katow_C() == 2) {
                opr1.addC_angle(180.0);
            } else if (pom1.getLiczba_plaszczyzn_dla_katow_C() == 3) {
                opr1.addC_angle(120.0);
                opr1.addC_angle(240.0);
                opr1.addC_angle(360.0);
            } else if (pom1.getLiczba_plaszczyzn_dla_katow_C() == 4) {
                opr1.addC_angle(90.0);
                opr1.addC_angle(180.0);
                opr1.addC_angle(270.0);
                opr1.addC_angle(360.0);
            } else {
                double delta = 360.0 / pom1.getLiczba_plaszczyzn_dla_katow_C();
                double actualCAngle = delta;
                while (actualCAngle <= 360.0) {
                    opr1.addC_angle(actualCAngle);
                    actualCAngle += delta;
                }
            }

            //main algorithm
            WspOprawy wsp1 = pom1.getWspolrzedneOpraw().get(0);
            //luminaire position
            double xLuminairePosition   = wsp1.pozycjaNaDlugosci;
            double yLuminairePosition   = wsp1.pozycjaNaSzerokosci;
            double zLuminairePosition   = wsp1.wysokosc;
            //calculating dimensions of single field
            double fieldLength          = pom1.getDlugosc() / pom1.getIl_punktow_wzdluz_dlugosci();
            double fieldWidth           = pom1.getSzerokosc() / pom1.getIl_punktow_wzdluz_szerokosci();
            //algorithm iterates through all C-gamma angles combination
            for (Double actualC_angle : opr1.getC_angles()) {
                for (Double actualGammaAngle : opr1.getGamma_angles()) {
                    int signDY;
                    int signDX;
                    double r;
                    double dX;
                    double dY;

                    //depending on C-gamma combination, algorithm determines if delta x and y are positive or negative
                    if (actualC_angle == 0.0 || actualC_angle == 180.0) {
                        signDY = 0;
                    } else if (actualC_angle > 0.0 &&
                            actualC_angle < 180.0) {
                        signDY = -1;
                    } else {
                        signDY = 1;
                    }

                    if (actualC_angle == 90.0 ||
                            actualC_angle == 270.0) {
                        signDX = 0;
                    } else if (actualC_angle > 90.0 && actualC_angle < 270) {
                        signDX = 1;
                    } else {
                        signDX = -1;
                    }

                    //calculating delta x and y out of C-gamma combination
                    r = zLuminairePosition * Math.tan(actualGammaAngle * Math.PI / 180.0);
                    dY = r * Math.sin(actualC_angle * Math.PI / 180.0) * (-1.0); //* signDY;
                    dX = r * Math.cos(actualC_angle * Math.PI / 180.0); //* signDX;

                    //position where actual beam intersects the surface
                    double actualX = xLuminairePosition + dX;
                    double actualY = yLuminairePosition + dY;


                    int XNumberField = 0, X2NumberField = 0;
                    int YNumberField = 0, Y2NumberField = 0;
                    boolean XColision = false, YColision = false;

                    //calculating number of field (x,y) on that the beam shines
                    if (actualX % fieldLength == 0) {
                        if (actualX == 0.0) {
                            XNumberField = 0;
                        } else if (actualX == pom1.getDlugosc()) {
                            XNumberField = pom1.getIl_punktow_wzdluz_dlugosci() - 1;
                        } else {
                            XColision = true;
                            XNumberField = (int) (actualX / fieldLength) - 1;
                            X2NumberField = XNumberField + 1;
                        }
                    } else {
                        XNumberField = (int) Math.ceil(actualX / fieldLength) - 1;
                    }

                    if (actualY % fieldWidth == 0) {
                        if (actualY == 0.0) {
                            YNumberField = 0;
                        } else if (actualY == pom1.getSzerokosc()) {
                            YNumberField = pom1.getIl_punktow_wzdluz_szerokosci() - 1;
                        } else {
                            YColision = true;
                            YNumberField = (int) (actualY / fieldLength) - 1;
                            Y2NumberField = YNumberField + 1;
                        }
                    } else {
                        YNumberField = (int) Math.ceil(actualY / fieldWidth) - 1;
                    }

                    double multiplier = (r * r + zLuminairePosition * zLuminairePosition) /
                            Math.cos(actualGammaAngle * Math.PI / 180);
                    if (actualX < 0.0 ||
                            actualX > pom1.getDlugosc() ||
                            actualY < 0.0 ||
                            actualY > pom1.getSzerokosc()) {
                        opr1.addCandelaValues(actualC_angle, 0.0);
                    } else if (XColision && YColision) {
                        //ray shines into the joint between four different fields
                        double E1 = pom1.getIlluminanceFromField(XNumberField, YNumberField);
                        double E2 = pom1.getIlluminanceFromField(X2NumberField, YNumberField);
                        double E3 = pom1.getIlluminanceFromField(XNumberField, Y2NumberField);
                        double E4 = pom1.getIlluminanceFromField(X2NumberField, Y2NumberField);
                        opr1.addCandelaValues(actualC_angle, multiplier * (E1 + E2 + E3 + E4) / 4.0);
                    } else if (XColision) {
                        //ray shines at ba border between two fields
                        double E1 = pom1.getIlluminanceFromField(XNumberField, YNumberField);
                        double E2 = pom1.getIlluminanceFromField(X2NumberField, YNumberField);
                        opr1.addCandelaValues(actualC_angle, multiplier * (E1 + E2) / 2.0);
                    } else if (YColision) {
                        //ray shines at ba border between two fields
                        double E1 = pom1.getIlluminanceFromField(XNumberField, YNumberField);
                        double E2 = pom1.getIlluminanceFromField(XNumberField, Y2NumberField);
                        opr1.addCandelaValues(actualC_angle, multiplier * (E1 + E2) / 2.0);
                    } else {
                        //add candela value explicitly
                        opr1.addCandelaValues(actualC_angle, multiplier * pom1.getIlluminanceFromField(XNumberField, YNumberField));
                    }
                }
            }

            Main.calculatedLuminaire            = opr1;
            Main.actualRoom                     = pom1;
            Main.isLuminaireProperlyCalculated  = true;

            NUM++;

            return opr1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CalculationException("IES calculation failed!");
        }
    }

}
