import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Oprawa {

    String sciezka_do_ies;
    boolean ies_loaded = false;

    public String getSciezka_do_ies() {
        return sciezka_do_ies;
    }

    public void setSciezka_do_ies(String sciezka_do_ies) {
        this.sciezka_do_ies = sciezka_do_ies;
    }

    public boolean isIes_loaded() {
        return ies_loaded;
    }

    public void setIes_loaded(boolean ies_loaded) {
        this.ies_loaded = ies_loaded;
    }

    public String getTEST() {
        return TEST;
    }

    public void setTEST(String TEST) {
        this.TEST = TEST;
    }

    public String getMANUFAC() {
        return MANUFAC;
    }

    public void setMANUFAC(String MANUFAC) {
        this.MANUFAC = MANUFAC;
    }

    public String getTILT() {
        return TILT;
    }

    public void setTILT(String TILT) {
        this.TILT = TILT;
    }

    public String getTESTLAB() {
        return TESTLAB;
    }

    public void setTESTLAB(String TESTLAB) {
        this.TESTLAB = TESTLAB;
    }

    public String getISSUEDATE() {
        return ISSUEDATE;
    }

    public void setISSUEDATE(String ISSUEDATE) {
        this.ISSUEDATE = ISSUEDATE;
    }

    public int getNumberOfLamps() {
        return numberOfLamps;
    }

    public void setNumberOfLamps(int numberOfLamps) {
        this.numberOfLamps = numberOfLamps;
    }

    public double getLumensPerLamp() {
        return lumensPerLamp;
    }

    public void setLumensPerLamp(double lumensPerLamp) {
        this.lumensPerLamp = lumensPerLamp;
    }

    public int getPhotometricType() {
        return photometricType;
    }

    public void setPhotometricType(int photometricType) {
        this.photometricType = photometricType;
    }

    public double getInputWatts() {
        return inputWatts;
    }

    public void setInputWatts(double inputWatts) {
        this.inputWatts = inputWatts;
    }

    public double getBalastFactor() {
        return balastFactor;
    }

    public void setBalastFactor(double balastFactor) {
        this.balastFactor = balastFactor;
    }

    public int getInitial_rated_lumens() {
        return initial_rated_lumens;
    }

    public void setInitial_rated_lumens(int initial_rated_lumens) {
        this.initial_rated_lumens = initial_rated_lumens;
    }

    public double getMultiplaying_factor() {
        return multiplaying_factor;
    }

    public void setMultiplaying_factor(double multiplaying_factor) {
        this.multiplaying_factor = multiplaying_factor;
    }

    public int getNumber_of_vertical_angles() {
        return number_of_vertical_angles;
    }

    public void setNumber_of_vertical_angles(int number_of_vertical_angles) {
        this.number_of_vertical_angles = number_of_vertical_angles;
    }

    public int getNumber_of_horizontal_angles() {
        return number_of_horizontal_angles;
    }

    public void setNumber_of_horizontal_angles(int number_of_horizontal_angles) {
        this.number_of_horizontal_angles = number_of_horizontal_angles;
    }

    public int getType_of_unit() {
        return type_of_unit;
    }

    public void setType_of_unit(int type_of_unit) {
        this.type_of_unit = type_of_unit;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public ArrayList<Double> getGamma_angles() {
        return gamma_angles;
    }

    public void setGamma_angles(ArrayList<Double> gamma_angles) {
        this.gamma_angles = gamma_angles;
    }

    public ArrayList<Double> getC_angles() {
        return C_angles;
    }

    public void setC_angles(ArrayList<Double> c_angles) {
        C_angles = c_angles;
    }

    public HashMap<Double, ArrayList<Double>> getCandela_values() {
        return candela_values;
    }

    public void setCandela_values(HashMap<Double, ArrayList<Double>> candela_values) {
        this.candela_values = candela_values;
    }

    public HashMap<String, String> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashMap<String, String> keywords) {
        this.keywords = keywords;
    }

    //IESNA91
    String TEST = "";
    String MANUFAC = "";
    String TILT = "";
    String TESTLAB = "";
    String ISSUEDATE = "";

    int numberOfLamps = -1;
    double lumensPerLamp = -1;
    int photometricType = -1;
//    int futureUse;
    double inputWatts = -1;
    double balastFactor = -1;


    //1
    int initial_rated_lumens = -1; //(absolute photometry is used)
    double multiplaying_factor = 1;
    int number_of_vertical_angles; //gamma
    int number_of_horizontal_angles; //C
    //1
    int type_of_unit = 2; //2 - meters; 1 - feet
    double width = 0.0; double length = 0.0; double height = 0.0;
    //1.0 1.0 0.0
    ArrayList<Double> gamma_angles = new ArrayList<Double>();//set of vertical angles
    ArrayList<Double> C_angles = new ArrayList<Double>();//set of horizontal angles
    HashMap<Double, ArrayList<Double> > candela_values = new HashMap<Double, ArrayList<Double>>();//candela values (hash map C_angle ->> ArrayList of candela values for following gamma_angles)
    HashMap<String, String> keywords = new HashMap<String, String>();

    public void addVerticalAngle(Double angle){
        gamma_angles.add(angle);
    }

    public void addC_angle(Double angle){
        C_angles.add(angle);
    }

    public void addKeyword(String key, String value){
        keywords.put(key, value);
    }

    public void addCandelaValues(Double C_angle, Double new_value){
        if (!candela_values.containsKey(C_angle)){
            candela_values.put(C_angle, new ArrayList<Double>());
        }

        candela_values.get(C_angle).add(new_value);
    }

    private void load_data_from_ies(String sciezka_do_ies) throws CalculationException{
        //not implemented yet
//        BufferedReader br = null;
        String line = "";
        Scanner br = null;

//        System.out.printf("Loadind data from file...\n");

        try {
//            br = new BufferedReader(new FileReader(sciezka_do_ies));
            br = new Scanner(new File(sciezka_do_ies));
            br.useLocale(Locale.US);

//            while(br.hasNextLine()){
//                line = br.nextLine();
//                System.out.printf("Wczytana linia: %s\n", line);
//            }

            line = br.nextLine();
//            System.out.println(line);
            if (line.equals("IESNA91")){
                //Only for test purpose
                //description is available here: http://docs.autodesk.com/AMECH_PP/2013/ENU/index.html?url=filesACD/GUID-45CAAF1C-7C9D-49A7-B18D-00CA5E2ED380.htm,topicNumber=ACDd30e154002
                TEST = br.nextLine().substring(7);
                System.out.printf("TEST: %s\n", TEST);
                MANUFAC = br.nextLine().substring(10);
                System.out.printf("MANUFAC: %s\n", MANUFAC);
                TILT=br.nextLine().substring(5);
                System.out.printf("TILT: %s\n", TILT);
                if (!TILT.equals("NONE")){
                    throw new Exception();
                }
                br.nextLine();
                initial_rated_lumens = Integer.parseInt(br.nextLine());
                multiplaying_factor = Integer.parseInt(br.nextLine());
                number_of_vertical_angles = Integer.parseInt(br.nextLine());
                number_of_horizontal_angles = Integer.parseInt(br.nextLine());
                br.nextLine();
                type_of_unit = Integer.parseInt(br.nextLine());
                br.nextLine(); //ommiting width, length and heigh of luminous opening
                br.nextLine();


                for (int i = 0 ; i < number_of_vertical_angles ; i++){
                    Double d = br.nextDouble();
//                    System.out.printf("Wczytano: %f\n", d);
                    gamma_angles.add(d);
                    if (i + 1 < number_of_vertical_angles && br.hasNextDouble() == false){
                        //CHECK THIS OUT!
                        System.out.println("experyment");
                        System.out.println(br.next());
                    }
                }

                for (int i = 0 ; i < number_of_horizontal_angles ; i++){
                    Double d = br.nextDouble();
                    C_angles.add(d);
//                    System.out.printf("Wczytano (C): %f \n", d);
                }

                //candela values
                int akt_C_number = 0;
//                System.out.println(C_angles[0]);
                for (double aktCAngle : C_angles){
                    ArrayList<Double> ar = new ArrayList<Double>();
                    for (int i = 0 ; i < number_of_vertical_angles ;  i++){
//                        candela_values[aktCAngle].
                    Double d = br.nextDouble();
                    ar.add(d);
//                    System.out.printf("\tWczytuje kat: %f\n", d);
                    }
                    candela_values.put(aktCAngle, ar);
                }


            } else if (line.equals("IES LM-63-1986")){

            } else if (line.equals("IES LM-63-1991")){

            } else if (line.equals("IES LM-63-1995")){

            } else if (line.equals("IESNA:LM-63-2002")){
                //According to: http://www.cn-hopu.com/upload/file/IES.pdf
//                System.out.printf("Mamy nowy IES\n");
                line = br.nextLine();

                //Keywords section
                while(line.charAt(0) == '['){
                    int positionOfSecondBracket = line.indexOf(']');
                    String keyword = line.substring(1, positionOfSecondBracket);
                    String value = line.substring(positionOfSecondBracket+1);

                    //check if keyword is among required keywords
                    if (keyword.equals("TEST")){
                        TEST = value;
                    } else if (keyword.equals("TESTLAB")){
                        TESTLAB = value;
                    } else if (keyword.equals("ISSUEDATE")){
                        ISSUEDATE = value;
                    } else if (keyword.equals("MANUFAC")){
                        MANUFAC = value;
                    } else {
                        //keyword is not among required keywords
                        keywords.put(keyword, value);
                    }
                    line = br.nextLine();
                }

                //TILT section
                TILT = line.substring(5);
//                System.out.printf("Tilt: %s\n", TILT);

                if (TILT.equals("INCLUDE")){
                    //read extra data (aprox. 4 lines)
                }

                numberOfLamps = br.nextInt();
//                System.out.printf("numberOfLamps: %d\n", numberOfLamps);
                lumensPerLamp = br.nextDouble();
//                System.out.printf("lumensPerLamp: %f\n", lumensPerLamp);
                multiplaying_factor = br.nextDouble();
//                System.out.printf(":multiplaying_factor %f\n", multiplaying_factor);
                number_of_vertical_angles = br.nextInt();
//                System.out.printf("number_of_vertical_angles: %d\n", number_of_vertical_angles);
                number_of_horizontal_angles = br.nextInt();
//                System.out.printf("number_of_horizontal_angles: %d\n", number_of_horizontal_angles);
                photometricType = br.nextInt();
//                System.out.printf("photometricType: %d\n", photometricType);
                if (photometricType != 1){
                    throw new CalculationException("not a type C photometric! Other types are not implemented yet!");
                }
                type_of_unit = br.nextInt();
//                System.out.printf("Type of unit: %d\n", type_of_unit);
                width = br.nextDouble();
//                System.out.printf("Width: %f\n", width);
                length = br.nextDouble();
//                System.out.printf("Length: %f\n", length);
                height = br.nextDouble();
//                System.out.printf("Height: %f\n", height);
                balastFactor = br.nextDouble();
//                System.out.printf("BalastFactor: %f\n", balastFactor);
                br.next(); //ommiting future use factor
                inputWatts = br.nextDouble();
//                System.out.printf("InputWatts: %f\n", inputWatts);

                for (int i = 0 ; i < number_of_vertical_angles ; i++){
                    Double d = br.nextDouble();
//                    System.out.printf("Wczytano: %f\n", d);
                    gamma_angles.add(d);
                    if (i + 1 < number_of_vertical_angles && br.hasNextDouble() == false){
                        //CHECK THIS OUT!
                        System.out.println("experyment");
                        System.out.println(br.next());
                    }
                }

                for (int i = 0 ; i < number_of_horizontal_angles ; i++){
                    Double d = br.nextDouble();
                    C_angles.add(d);
//                    System.out.printf("Wczytano (C): %f \n", d);
                }

                //candela values
                int akt_C_number = 0;
//                System.out.println(C_angles[0]);
                for (double aktCAngle : C_angles){
                    ArrayList<Double> ar = new ArrayList<Double>();
                    for (int i = 0 ; i < number_of_vertical_angles ;  i++){
//                        candela_values[aktCAngle].
                        Double d = br.nextDouble();
                        ar.add(d);
//                        System.out.printf("\tWczytuje kat: %f\n", d);
                    }
                    candela_values.put(aktCAngle, ar);
                }

            }

        } catch (Exception e) {
//            e.printStackTrace();
            throw new CalculationException(e.getMessage().toString());
        }
    }

    public String returnIESToString(){

        StringBuilder sb = new StringBuilder();

        sb.append("IESNA:LM-63-2002\r\n");
        sb.append(String.format("[TEST] %s\r\n", TEST));
        sb.append(String.format("[TESTLAB] %s\r\n", TESTLAB));
        sb.append(String.format("[MANUFAC] %s\r\n", MANUFAC));
        sb.append(String.format("[ISSUEDATE] %s\r\n", ISSUEDATE));
        for (Map.Entry e : keywords.entrySet()) {
            sb.append(String.format("[%s] %s\r\n", e.getKey(), e.getValue()));
        }
        sb.append(String.format("TILT=%s\r\n", TILT));
        if (TILT.equals("INCLUDE")){
            //write extra data (aprox. 4 lines)
        }
        sb.append(String.format("%d %.2f %.2f %d %d %d %d %.3f %.3f %.3f\r\n", numberOfLamps, lumensPerLamp, multiplaying_factor, number_of_vertical_angles, number_of_horizontal_angles, photometricType, type_of_unit, width, length, height));
        sb.append(String.format("%.2f %f %.2f\r\n", balastFactor, 1.0, inputWatts));

        //TODO: reduce line width to 255
        int printed_amount = 0;
        for (Double angle : gamma_angles){
            sb.append(String.format("%.2f ", angle));
            printed_amount++;
            if (printed_amount == 10){
                printed_amount = 0;
                sb.append("\n");
            }
        }
        if (printed_amount != 0){
            sb.append("\r\n");
        }

        //TODO: reduce line width to 255
        printed_amount = 0;
        for (Double angle : C_angles){
            sb.append(String.format("%.2f ", angle));
            printed_amount++;
            if (printed_amount == 10){
                printed_amount = 0;
                sb.append("\r\n");
            }
        }
        if (printed_amount != 0){
            sb.append("\r\n");
        }

        //TODO: reduce line width to 255
        if (candela_values.size() > 0){
            for (Double angle : C_angles){
                printed_amount = 0;
                if (candela_values.containsKey(angle)){
                    for (Double can_val : candela_values.get(angle)){
                        sb.append(String.format("%.2f ", can_val));
                        printed_amount++;
                        if (printed_amount == 10){
                            printed_amount = 0;
                            sb.append("\r\n");
                        }
                    }
                    if (printed_amount != 0){
                        sb.append("\r\n");
                    }
                } else {
                    sb.append("???");
                }
            }
        }

        return sb.toString();
    }

    public void show_ies_from_data(){
        System.out.println(this.returnIESToString());
    }

    public void show_data(){
        System.out.printf("There are %d vertical angles: \n", number_of_vertical_angles);
        for (Double angle : gamma_angles){
            System.out.printf("%.2f ", angle);
        }
        System.out.printf("\n");

        System.out.printf("There are/is %d C angles: \n", number_of_horizontal_angles);
        for (Double angle : C_angles){
            System.out.printf("%.2f ", angle);
        }
        System.out.printf("\n");

    }

    public Oprawa(String sciezka_do_ies) throws  CalculationException{ //konstruktor uzywany do generowania siatki nat. osw. z bryly foto.
        this.sciezka_do_ies = sciezka_do_ies;
        load_data_from_ies(sciezka_do_ies);
        ies_loaded = true;
    }

    public Oprawa(){ //konstruktor uzywany do gen. bryly foto. z siat. nat. osw.
        ies_loaded = false;
    }



}
