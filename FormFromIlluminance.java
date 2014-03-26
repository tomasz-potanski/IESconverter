import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class FormFromIlluminance extends JFrame {

    JPanel panel;
    JLabel statusLabel;
    JButton saveB;
    JMenuItem saveIES;
    JPanel topContainer;
    JDrawPanel iesDrawer;
    JPanel iesDrawerContainer;
    JMySlider slider;
    Oprawa actualLuminaire;
    PomieszczenieProstokatne actualRoom;
    Boolean luminaireIsCalculated = false;
    int actualGlobalCAngle = 0; //NUMBER!
    Jezyk actualLanguage;
    JButton showEDistr;

    private void drawIESGeneral(Oprawa luminaire){
        actualLuminaire = luminaire;
        luminaireIsCalculated = true;

        iesDrawerContainer.remove(iesDrawer);
        iesDrawerContainer.revalidate();
        iesDrawerContainer.repaint();
        iesDrawerContainer.add(iesDrawer, BorderLayout.CENTER);
    }

    private void drawIESFixedC(Oprawa luminaire, int cAngleNumber){
        //TODO: update slider!!
//        slider.setValue(cAngleNumber);
        actualGlobalCAngle = cAngleNumber;
//        iesDrawer.drawLuminaire(luminaire, cAngleNumber);
//        iesDrawer.repaint();

        actualLuminaire = luminaire;
        luminaireIsCalculated = true;

        iesDrawerContainer.remove(iesDrawer);
        iesDrawerContainer.revalidate();
        iesDrawerContainer.repaint();
        iesDrawerContainer.add(iesDrawer, BorderLayout.CENTER);
    }

    public FormFromIlluminance(final Form1 startForm, Jezyk language){
       super("Illuminance distribution ->> IES");
        setSize(400, 600);
        setLocationRelativeTo(null);
        actualLanguage = language;
        if (actualLanguage == Jezyk.POLSKI){
            setTitle("Rozkład natężenia oświetlenia ->> IES");
        } else {
            setTitle("Illuminance distribution ->> IES");
        }
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        startForm.setVisible(true);
                    }
                }
        );


        setLayout(new BorderLayout());
//        JLabel background=new JLabel(new ImageIcon(getClass().getResource("9.jpg")));
        JLabel background=new JLabel();
//        background.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
//        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.setLayout(new BorderLayout());

        if (actualLanguage == Jezyk.ENGLISH){
            statusLabel = new JLabel("Waiting for input data...");
        } else {
            statusLabel = new JLabel("Czekam na dane wejściowe...");
        }
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
//        statusLabel.setForeground(new Color(255, 255, 255));

        statusLabel.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        JMenu aboutMenu;
        JMenuItem aboutItem;

        if (actualLanguage == Jezyk.ENGLISH){
            aboutMenu = new JMenu("About");
            aboutItem = new JMenuItem("About");
        } else {
            aboutMenu = new JMenu("O programie");
            aboutItem = new JMenuItem("O programie");
        }

        aboutItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                AboutForm ex = new AboutForm();
                                ex.setVisible(true);
                            }
                        });
                    }
                }
        );

        JMenu fileMenu;
        if (actualLanguage == Jezyk.ENGLISH){
            fileMenu = new JMenu("File");
        } else {
            fileMenu = new JMenu("Plik");
        }

        JMenuItem loadCSV;
        if (actualLanguage == Jezyk.ENGLISH){
            loadCSV = new JMenuItem("Load illuminance distribution csv file");
        } else {
            loadCSV = new JMenuItem("Wczytaj rozkład natężenia oświetlenia");
        }

        loadCSV.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
//        loadCSV.setMnemonic(KeyEvent.VK_O);
        loadCSV.addActionListener(new OpenCSVAL());

        if (actualLanguage == Jezyk.ENGLISH){
            saveIES = new JMenuItem("Save calculated IES");
        } else {
            saveIES = new JMenuItem("Zapisz wygenerowany IES");
        }



        saveIES.setEnabled(false);
        saveIES.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        saveIES.addActionListener(new SaveIES());

        JMenuItem exitItem;
        if (actualLanguage == Jezyk.ENGLISH){
            exitItem = new JMenuItem("Exit");
        } else {
            exitItem = new JMenuItem("Wyjście");
        }

        exitItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        aboutMenu.add(aboutItem);
        fileMenu.add(loadCSV);
        fileMenu.add(saveIES);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        if (actualLanguage == Jezyk.ENGLISH){
            saveB = new JButton("Save calculated IES");
        } else {
            saveB = new JButton("Zapisz wygenerowany IES");
        }

        saveB.setVisible(true);
        saveB.setEnabled(false);
        //saveB.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
//        saveB.setSize(300, 50);
        saveB.addActionListener(new SaveIES());

        if (actualLanguage == Jezyk.ENGLISH){
            showEDistr = new JButton("Show illuminance distribution");
        } else {
            showEDistr = new JButton("Pokaż rozkład natężenia oświetlenia");
        }
        showEDistr.setEnabled(false);

        showEDistr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FormShowE ex = new FormShowE(actualLanguage, actualRoom);
                        ex.setVisible(true);
                    }
                });
            }
        });


//        background.add(Box.createRigidArea(new Dimension(50, 20)));

        JButton openb;
        if (actualLanguage == Jezyk.ENGLISH){
            openb = new JButton("Load illuminance distribution csv file");
        } else {
            openb = new JButton("Wczytaj rozkład natężenia oświetlenia z pliku");
        }

        openb.addActionListener(new OpenCSVAL());


        openb.setFocusable(false);
        saveB.setFocusable(false);
//        background.add(openb);
//        background.add(Box.createRigidArea(new Dimension(50, 20)));
//        background.add(statusLabel);
//        background.add(Box.createRigidArea(new Dimension(50, 20)));
//        background.add(saveB);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)) );
        topContainer.add(openb);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topContainer.add(statusLabel);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));
//        saveB.setBorder(BorderFactory.createEmptyBorder(0, 65, 0, 0));

        topContainer.add(saveB);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));
        topContainer.add(showEDistr);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));

        slider = new JMySlider(JSlider.HORIZONTAL, 0,0,0);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new SliderChangeValue());

//        slider.setSize(200, 50);
        iesDrawer = new JDrawPanel();
        iesDrawerContainer = new JPanel();
        JPanel container2 = new JPanel();
        iesDrawer.setSize(new Dimension(300, 300));
        iesDrawer.setPreferredSize(new Dimension(300, 300));
        iesDrawer.setMaximumSize(new Dimension(300, 300));
//        iesDrawer.setBackground(new Color(205, 50, 50));
        iesDrawer.setBackground(new Color(255, 255, 255, 150));
        iesDrawer.setOpaque(true);
        iesDrawer.setMinimumSize(new Dimension(300, 300));

//        slider.setOpaque(true);
        slider.setBackground(new Color(255, 255, 255));
//        slider.setPreferredSize(new Dimension(0, 100));

        iesDrawerContainer.setLayout(new BorderLayout());
        iesDrawerContainer.add(slider, BorderLayout.NORTH);
        iesDrawerContainer.add(iesDrawer, BorderLayout.CENTER);
        int iesDrawerContainerY = 330;
        iesDrawerContainer.setSize(300, iesDrawerContainerY);
        iesDrawerContainer.setPreferredSize(new Dimension(300, iesDrawerContainerY));
        iesDrawerContainer.setMaximumSize(new Dimension(300, iesDrawerContainerY));
//        iesDrawerContainer.setBackground(new Color(255, 255, 255, 150));
        iesDrawerContainer.setOpaque(false);

        container2.add(iesDrawerContainer);
//        container2.setBackground(new Color(255, 255, 255, 150));
        container2.setOpaque(false);

//        background.getActionMap().put("openCSV", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                openFile();
//            }
//        });

//        InputMap inputMap = background.getInputMap();
//        KeyStroke controlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK);
//        inputMap.put(controlO, "openCSV");

        background.add(topContainer, BorderLayout.NORTH);
        background.add(container2, BorderLayout.CENTER);
        add(background);

        iesDrawer.repaint();

        setResizable(false);
        setVisible(true);
    }

    public void saveIESMethod(){
       JFileChooser filesave = new JFileChooser();
       FileFilter filter;

       if (actualLanguage == Jezyk.ENGLISH){
           filter = new FileNameExtensionFilter("*.ies files", "ies");
       } else {
           filter = new FileNameExtensionFilter("pliki *.ies", "ies");
       }

       filesave.addChoosableFileFilter(filter);

       int ret;
       if (actualLanguage == Jezyk.ENGLISH){
           ret = filesave.showDialog(panel, "Save generated IES file");
       } else {
           ret = filesave.showDialog(panel, "Zapisz wygenerowany plik IES");
       }

       if (ret == JFileChooser.APPROVE_OPTION && luminaireIsCalculated){
           File file = filesave.getSelectedFile();
           String iesContentToWrite = actualLuminaire.returnIESToString();
           try {
               //Apache Commons IO ;)
               FileUtils.writeStringToFile(file,iesContentToWrite,  false);
               if (actualLanguage == Jezyk.ENGLISH){
                   JOptionPane.showMessageDialog(panel,"File successfully saved", "File saved", JOptionPane.INFORMATION_MESSAGE);
               } else {
                   JOptionPane.showMessageDialog(panel,"Plik pomyślnie zapisano", "Plik zapisano", JOptionPane.INFORMATION_MESSAGE);
               }
           }
           catch (Exception e){
               if (actualLanguage == Jezyk.ENGLISH){
               JOptionPane.showMessageDialog(panel, "Could not save file",
                       "Error", JOptionPane.ERROR_MESSAGE);
               } else {
                   JOptionPane.showMessageDialog(panel, "Nie można zapisać pliku",
                           "Błąd", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }

    public Boolean openFile(){

        JFileChooser fileopen = new JFileChooser();
        FileFilter filter;
        if (actualLanguage == Jezyk.ENGLISH){
            filter = new FileNameExtensionFilter("*.csv files", "csv");
        } else {
            filter = new FileNameExtensionFilter("pliki *.csv", "csv");
        }
        fileopen.addChoosableFileFilter(filter);

        Boolean result = false; //success?

        int ret;
        if (actualLanguage == Jezyk.ENGLISH){
            ret = fileopen.showDialog(panel, "Open file");
        } else {
            ret = fileopen.showDialog(panel, "Otwórz plik");
        }

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            try {
                Oprawa opr1 = Main.siatka_na_bryle(file.getAbsolutePath());
                actualRoom = Main.actualRoom;
                if (Main.isLuminaireProperlyCalculated == true){
                    if (actualLanguage == Jezyk.ENGLISH){
                        statusLabel.setText("Calculation finished");
                    } else {
                        statusLabel.setText("Obliczenia zakończone");
                    }

                    statusLabel.setVisible(true);
                    saveB.setVisible(true);
                    saveB.setEnabled(true);
                    saveIES.setEnabled(true);
                    showEDistr.setEnabled(true);

                    int cAngleAmount = opr1.getNumber_of_horizontal_angles();
//                    System.out.println(cAngleAmount);
                    slider.setMinimum(0);
                    slider.setMaximum(cAngleAmount - 1);
                    slider.setValue(0);

                    Hashtable<Integer, JLabel> dict = new Hashtable<Integer, JLabel>();
                    dict.put(0, new JLabel(String.format("C%d", (int)Math.ceil(opr1.getC_angles().get(0)))));
                    dict.put(cAngleAmount - 1, new JLabel(String.format("C%d", (int)Math.ceil(opr1.getC_angles().get(cAngleAmount - 1)))));
                    slider.setLabelTable(dict);

                    result = true;
                    drawIESGeneral(opr1);
                } else {
                    if (actualLanguage == Jezyk.ENGLISH){
                        statusLabel.setText("Calculation failed!");

                    } else {
                        statusLabel.setText("Błąd w obliczeniach!");
                    }

                    statusLabel.setVisible(true);
                    saveB.setEnabled(false);
                    showEDistr.setEnabled(false);
                    saveIES.setEnabled(false);
                    result = false;
                }
            }
            catch (CalculationException e){
                if (actualLanguage == Jezyk.ENGLISH){
                    statusLabel.setText("Calculation failed!");
                    e.printStackTrace();
                } else {
                    statusLabel.setText("Obliczenia zakończone!");
                }
                statusLabel.setVisible(true);
                saveB.setEnabled(false);
                showEDistr.setEnabled(false);
                saveIES.setEnabled(false);
                result = false;
            }
        }

        return result;
    }

    public class SliderChangeValue implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent event){
            int v = slider.getValue();
//            double newCAngle = actualLuminaire.getC_angles().get(v);
            drawIESFixedC(actualLuminaire, v);
        }
    }

    public class OpenCSVAL implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event) {
            openFile();
        }

    }

    public class SaveIES implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            saveIESMethod();
        }
    }

    public class JDrawPanel extends JPanel{
//        public JDrawPanel(){
//            super();
//        }


        public void drawLuminaire(Oprawa luminaire, int cAngleNumber, Graphics g){
            if (luminaire != null){
            //we need maximum candela value for fixed cAngleNumber and all vertical (gamma) angles
            double maximum = -1.0;
            for (Double actualCandelaValue : luminaire.getCandela_values().get(luminaire.getC_angles().get(cAngleNumber))){
                if (actualCandelaValue > maximum){
                    maximum = actualCandelaValue;
                }
            }

            Graphics2D gg = (Graphics2D) g;
            gg.setColor(Color.red);
            gg.setStroke(new BasicStroke(3));

            //painting
//            System.out.printf("Maximum: %f\n", maximum);
            double R = 180.0;
            int gammaAngleNumber = 0;
            double xMiddle = JDrawPanel.this.getWidth() / 2.0;
            double yMiddle = JDrawPanel.this.getHeight() / 2.0;
            ArrayList<Double> xToConnect = new ArrayList<Double>();
            ArrayList<Double> yToConnect = new ArrayList<Double>();
            ArrayList<Double> x2ToConnect = new ArrayList<Double>();
            ArrayList<Double> y2ToConnect = new ArrayList<Double>();
//            xToConnect.add(xMiddle);
//            yToConnect.add(yMiddle);
            double lastCAngle = luminaire.getC_angles().get(luminaire.getC_angles().size() - 1);
            boolean isFirst = true;
            for (Double actualCandelaValue : luminaire.getCandela_values().get(luminaire.getC_angles().get(cAngleNumber))){
                double actualGammaAngle = luminaire.getGamma_angles().get(gammaAngleNumber);
                double xEndPoint = xMiddle - R * (actualCandelaValue / maximum) * Math.cos((270.0 - actualGammaAngle) * Math.PI / 180.0);
                //we have reversed y axis in our cartesian coordinate system
                double yEndPoint = yMiddle - R * (actualCandelaValue / maximum) * Math.sin((270.0 - actualGammaAngle) * Math.PI / 180.0);

                xToConnect.add(xEndPoint);
                yToConnect.add(yEndPoint);
                if (isFirst){
                    isFirst = false;
                    x2ToConnect.add(xEndPoint);
                    y2ToConnect.add(yEndPoint);
                }
//                System.out.printf("actualCandelaValue: %f\n", actualCandelaValue);
//                System.out.printf("actualGammaAngle: %f\n", actualGammaAngle);
                gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));

                //drowing other quadrant
                if (lastCAngle == 0.0 || lastCAngle == 90.0){
                    //we draw in the same way
                    xEndPoint = xMiddle - R * (actualCandelaValue / maximum) * Math.cos((270.0 + actualGammaAngle) * Math.PI / 180.0);
                    //we have reversed y axis in our cartesian coordinate system
                    yEndPoint = yMiddle - R * (actualCandelaValue / maximum) * Math.sin((270.0 + actualGammaAngle) * Math.PI / 180.0);
                    x2ToConnect.add(xEndPoint);
                    y2ToConnect.add(yEndPoint);
                    gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));

                } else if (lastCAngle == 180.0){
                    //we get candela values explicitly
                    if (cAngleNumber == 0){
                        //draw for 180
                        double correlatedCandelaValue = luminaire.getCandela_values().get(luminaire.getC_angles().get(luminaire.getCandela_values().size() - 1)).get(gammaAngleNumber);
                        xEndPoint = xMiddle - R * (actualCandelaValue / maximum) * Math.cos((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        //we have reversed y axis in our cartesian coordinate system
                        yEndPoint = yMiddle - R * (actualCandelaValue / maximum) * Math.sin((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        x2ToConnect.add(xEndPoint);
                        y2ToConnect.add(yEndPoint);
                        gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));

                    } else {
                        //symmetry
                        //TODO: check and correct - with output generated in the program it will work, in general not always
                        double correlatedCandelaValue = luminaire.getCandela_values().get(luminaire.getC_angles().get(luminaire.getCandela_values().size() - 1 - cAngleNumber)).get(gammaAngleNumber);
                        xEndPoint = xMiddle - R * (actualCandelaValue / maximum) * Math.cos((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        //we have reversed y axis in our cartesian coordinate system
                        yEndPoint = yMiddle - R * (actualCandelaValue / maximum) * Math.sin((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        x2ToConnect.add(xEndPoint);
                        y2ToConnect.add(yEndPoint);
                        gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));
                    }
                } else if (lastCAngle == 360.0){
//                    //mayby we are able to get values explicitly
                    if (luminaire.getC_angles().size() % 2 == 1){
//                        //fantastic symmetry
//                        int dCNumber = (luminaire.getC_angles().size() - 1) / 2; //its even!
                        int dCNumber = ((luminaire.getC_angles().size() - 1) / 2 + cAngleNumber) % (luminaire.getC_angles().size() - 1); //its even!
//                        System.out.println(luminaire.getC_angles().get(dCNumber));
////                        System.out.printf("dCNumber: %d\n", dCNumber);
////                        System.out.printf("size: %d\n", luminaire.getC_angles().size());
////                        System.out.printf("size of gamma: %d\n", luminaire.getCandela_values().get(luminaire.getC_angles().get(dCNumber)).size());
                        double correlatedCandelaValue = luminaire.getCandela_values().get(luminaire.getC_angles().get(dCNumber)).get(gammaAngleNumber);
                        xEndPoint = xMiddle - R * (correlatedCandelaValue / maximum) * Math.cos((270.0 + actualGammaAngle) * Math.PI / 180.0);
//                        //we have reversed y axis in our cartesian coordinate system
                        yEndPoint = yMiddle - R * (correlatedCandelaValue / maximum) * Math.sin((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        x2ToConnect.add(xEndPoint);
                        y2ToConnect.add(yEndPoint);
                        gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));
//
                    } else {
                        //we have to calculate approx candela values
                        int firstC = ((int)Math.floor((luminaire.getC_angles().size() - 1 ) / 2) + cAngleNumber) % (luminaire.getC_angles().size() - 1);
                        double correlatedCandelaValue = (luminaire.getCandela_values().get(luminaire.getC_angles().get(firstC)).get(gammaAngleNumber) + luminaire.getCandela_values().get(luminaire.getC_angles().get((firstC+1) % (luminaire.getC_angles().size() - 1))).get(gammaAngleNumber))/2.0;
                        xEndPoint = xMiddle - R * (correlatedCandelaValue / maximum) * Math.cos((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        //we have reversed y axis in our cartesian coordinate system
                        yEndPoint = yMiddle - R * (correlatedCandelaValue / maximum) * Math.sin((270.0 + actualGammaAngle) * Math.PI / 180.0);
                        x2ToConnect.add(xEndPoint);
                        y2ToConnect.add(yEndPoint);
                        gg.drawLine((int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint), (int)Math.ceil(xEndPoint), (int)Math.ceil(yEndPoint));


                    }
                }

                gammaAngleNumber++;
            }
//            xToConnect.add(xMiddle);
//            yToConnect.add(yMiddle);
            x2ToConnect.add(xMiddle);
            y2ToConnect.add(yMiddle);

            //conect points
            gg.setStroke(new BasicStroke(2));
            for (int i = 0 ; i < xToConnect.size() -1 ; i++){
                gg.drawLine((int)Math.ceil(xToConnect.get(i)), (int)Math.ceil(yToConnect.get(i)), (int)Math.ceil(xToConnect.get(i+1)), (int)Math.ceil(yToConnect.get(i+1)));
            }
            for (int i = 0 ; i < x2ToConnect.size() -1 ; i++){
                gg.drawLine((int)Math.ceil(x2ToConnect.get(i)), (int)Math.ceil(y2ToConnect.get(i)), (int)Math.ceil(x2ToConnect.get(i+1)), (int)Math.ceil(y2ToConnect.get(i+1)));
            }

            //draw Texts
            double distance = JDrawPanel.this.getWidth()/2;
            double diameterStep = distance / 5.0;
            gg.setColor(Color.blue);
            gg.setStroke(new BasicStroke(1));
            for (double diameter = 0.0 ; diameter <= distance ; diameter += diameterStep){
                gg.drawString(String.format("%.2f", maximum * diameter/R), (int)Math.ceil(xMiddle), (int)Math.ceil(yMiddle + diameter));
            }
        }
        }

        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));

//            g2d.setComposite(AlphaComposite.getInstance(
//                    AlphaComposite.SRC_OVER, 0.3f));

            double width = JDrawPanel.this.getWidth();
            double height = JDrawPanel.this.getHeight();
            double xMiddle = JDrawPanel.this.getWidth() / 2.0;
            double yMiddle = JDrawPanel.this.getHeight() / 2.0;

            //drawing concentric circles
            double diameterStep = width / 5.0;
            for (double diameter = diameterStep ; diameter <= width ; diameter += diameterStep){
                Ellipse2D.Double circle = new Ellipse2D.Double(xMiddle - diameter/2.0, yMiddle - diameter/2.0, diameter, diameter);
                g2d.draw(circle);
            }

            //drawing lines from the middle
            //we go counterclockwise - from the first quadrant of Cartesian coordinate system to the fourth
            double degreeStep = 10.0;
            double r = 190.0;
            double minR = width / 10.0;
            for (double actualDegree = 0.0 ; actualDegree < 360.0 ;  actualDegree += degreeStep){

                double dX = r * Math.cos(actualDegree * Math.PI / 180.0);
                double dY = r * Math.sin(actualDegree * Math.PI / 180.0);
                double dminX = minR * Math.cos(actualDegree * Math.PI / 180.0);
                double dminY = minR * Math.sin(actualDegree * Math.PI / 180.0);

                //drawing to many lines from the center darkens the graphs - we draw from center only lines for multiplicities of 90.0 degree
                if (actualDegree % 90.0 == 0){
                    dminX = 0;
                    dminY = 0;
                }

                double xEnd = xMiddle + dX;
                double yEnd = yMiddle + dY;

                g2d.drawLine((int)(xMiddle + dminX), (int)(yMiddle + dminY), (int)xEnd, (int)yEnd);
            }

            if (luminaireIsCalculated  && null != actualLuminaire){
                //actualGlobalCAngle is integer number!
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(Color.blue);
                g2d.drawString(String.format("C%d", (int)Math.ceil(actualLuminaire.getC_angles().get(actualGlobalCAngle))), 20, 20);
            }
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            doDrawing(g);
            if (luminaireIsCalculated){
                drawLuminaire(actualLuminaire, actualGlobalCAngle, g);
            }
        }

    }

    public class JMySlider extends JSlider{

        public JMySlider(int a, int b, int c, int d){
            super(a, b, c, d);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        }
    }

}
