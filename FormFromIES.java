import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class FormFromIES extends JFrame {

    JPanel topContainer;
    JPanel centerContainer;
    JPanel panel;
    JLabel statusLabel;
    JButton saveCSVButton, showEButton ;
    JButton loadIESButton;
    JMenuItem saveCSVItem;
    Oprawa actualLuminaire;
    PomieszczenieProstokatne actualRoom;
    Boolean roomIsCalcultated;
    private JPanel panel1;
    JTextField lengthTextField, widthTextField, heightTextField, dGammaTextField, cAnglesAmountTextField, xFieldAmountTextField, yFieldAmountTextField, floorReflectionFactorTextField, wallReflectionFactorTextField, ceilingReflectionFactorTextField, xLuminaireTextField, yLuminaireTextField, zLuminaireTextField;
    Jezyk actualLanguage;
    JMenu aboutMenu, fileMenu;
    JMenuItem aboutItem, loadIESItem, exitItem;
    JLabel lengthLabel, wallReflectionFactorLabel, ceilingReflectionFactorLabel, floorReflectionFactorLabel, xFieldAmountLabel, yFieldAmountLabel, xLuminaireLabel, yLuminaireLabel, zLuminaireLabel;
    JLabel widthLabel, heightLabel;
    public FormFromIES(final Form1 startForm, Jezyk language){

        super("IES file ->> Illuminance distribution");
        actualLanguage = language;
        if (actualLanguage == Jezyk.ENGLISH){
            setTitle("IES file ->> Illuminance distribution");
        } else {
            setTitle("plik IES ->> rozkład natężenia oświetlenia");
        }

        int formWidth = 400;
        int formHeight = 610;
        setSize(formWidth, formHeight);
        setPreferredSize(new Dimension(formWidth, formHeight));
        setMinimumSize(new Dimension(formWidth, formHeight));
        setMaximumSize(new Dimension(formWidth, formHeight));
        setLocationRelativeTo(null);

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        startForm.setVisible(true);
                    }
                }
        );

        if (actualLanguage == Jezyk.ENGLISH){
            loadIESButton = new JButton("Load IES file");
            saveCSVButton = new JButton("Save illuminance distribution");
            showEButton = new JButton("Show illuminance distribution");
            statusLabel = new JLabel("Waiting for input data...");
        } else {
            loadIESButton = new JButton("Wczytaj IES");
            saveCSVButton = new JButton("Zapisz rozkład natężenia oświetlenia");
            showEButton = new JButton("Pokaż rozkład natężenia oświetlenia");
            statusLabel = new JLabel("Czekam na dane wejściowe...");
        }
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
//        statusLabel.setForeground(new Color(255, 255, 255));
        statusLabel.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        if (actualLanguage == Jezyk.ENGLISH){
            aboutMenu = new JMenu("About");
            fileMenu = new JMenu("File");

            aboutItem = new JMenuItem("About");
            loadIESItem = new JMenuItem("Load IES");
            exitItem = new JMenuItem("Exit");
            saveCSVItem = new JMenuItem("Save CSV");
        } else {
            aboutMenu = new JMenu("O programie");
            fileMenu = new JMenu("Plik");

            aboutItem = new JMenuItem("O programie");
            loadIESItem = new JMenuItem("Wczytaj IES");
            exitItem = new JMenuItem("Wyjście");
            saveCSVItem = new JMenuItem("Zapisz CSV");
        }

        saveCSVItem.setEnabled(false);

        saveCSVItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        loadIESItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));

        loadIESItem.addActionListener(new OpenIESAL());
        loadIESButton.addActionListener(new OpenIESAL());
        saveCSVItem.addActionListener(new SaveCSVAL());
        saveCSVButton.addActionListener(new SaveCSVAL());
        saveCSVButton.setEnabled(false);
        showEButton.setEnabled(false);
        showEButton.addActionListener(new showEAL());

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

        exitItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        aboutMenu.add(aboutItem);
        fileMenu.add(loadIESItem);
        fileMenu.add(saveCSVItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());
//        JLabel background=new JLabel(new ImageIcon(getClass().getResource("8.jpg")));
        JLabel background=new JLabel();
        background.setLayout(new BorderLayout());

        loadIESButton.setFocusable(false);
        saveCSVButton.setFocusable(false);

        topContainer = new JPanel();
        centerContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);
        centerContainer.setOpaque(false);

        topContainer.add(Box.createRigidArea(new Dimension(100, 20)) );
        topContainer.add(loadIESButton);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topContainer.add(statusLabel);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));

        topContainer.add(saveCSVButton);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));
        topContainer.add(showEButton);
        topContainer.add(Box.createRigidArea(new Dimension(100, 20)));


        JPanel lengthPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            lengthLabel = new JLabel("Room length: ");
        } else {
            lengthLabel = new JLabel("Długość pomieszczenia: ");
        }
//        lengthLabel.setForeground(Color.white);
        lengthTextField = new JTextField("3.0");
        lengthTextField.setPreferredSize(new Dimension(60,20));
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthTextField);
        lengthPanel.setOpaque(false);

        JPanel widthPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            widthLabel = new JLabel("Room width: ");
        } else {
            widthLabel = new JLabel("Szerokość pomieszczenia: ");
        }
//        widthLabel.setForeground(Color.white);
        widthTextField = new JTextField("3.0");
        widthTextField.setPreferredSize(new Dimension(60,20));
        widthPanel.add(widthLabel);
        widthPanel.add(widthTextField);
        widthPanel.setOpaque(false);

        JPanel heightPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            heightLabel = new JLabel("Room height: ");
        } else {
            heightLabel = new JLabel("Wysokość pomieszczenia: ");
        }

//        heightLabel.setForeground(Color.white);
        heightTextField = new JTextField("3.0");
        heightTextField.setPreferredSize(new Dimension(60,20));
        heightPanel.add(heightLabel);
        heightPanel.add(heightTextField);
        heightPanel.setOpaque(false);

        JPanel floorReflectionFactorPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            floorReflectionFactorLabel = new JLabel("Room floor reflection factor: ");
        } else {
            floorReflectionFactorLabel = new JLabel("Współczynnik odbicia podłogi: ");
        }
//        floorReflectionFactorLabel.setForeground(Color.white);
        floorReflectionFactorTextField = new JTextField("20");
        floorReflectionFactorTextField.setPreferredSize(new Dimension(60,20));
        floorReflectionFactorPanel.add(floorReflectionFactorLabel);
        floorReflectionFactorPanel.add(floorReflectionFactorTextField);
        floorReflectionFactorPanel.setOpaque(false);

        JPanel wallReflectionFactorPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            wallReflectionFactorLabel = new JLabel("Room wall reflection factor: ");
        } else {
            wallReflectionFactorLabel = new JLabel("Współczynnik odbicia ścian: ");
        }
//        wallReflectionFactorLabel.setForeground(Color.white);
        wallReflectionFactorTextField = new JTextField("50");
        wallReflectionFactorTextField.setPreferredSize(new Dimension(60,20));
        wallReflectionFactorPanel.add(wallReflectionFactorLabel);
        wallReflectionFactorPanel.add(wallReflectionFactorTextField);
        wallReflectionFactorPanel.setOpaque(false);

        JPanel ceilingReflectionFactorPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            ceilingReflectionFactorLabel = new JLabel("Room ceiling reflection factor: ");
        } else {
            ceilingReflectionFactorLabel = new JLabel("Współczynnik odbicia sufitu: ");
        }
//        ceilingReflectionFactorLabel.setForeground(Color.white);
        ceilingReflectionFactorTextField = new JTextField("70");
        ceilingReflectionFactorTextField.setPreferredSize(new Dimension(60,20));
        ceilingReflectionFactorPanel.add(ceilingReflectionFactorLabel);
        ceilingReflectionFactorPanel.add(ceilingReflectionFactorTextField);
        ceilingReflectionFactorPanel.setOpaque(false);

//        JPanel cAnglesAmountPanel = new JPanel(new FlowLayout());
//        JLabel cAnglesAmountLabel = new JLabel("Amount of horizontal angles (C): ");
//        cAnglesAmountLabel.setForeground(Color.white);
//        cAnglesAmountTextField = new JTextField("10");
//        cAnglesAmountPanel.add(cAnglesAmountLabel);
//        cAnglesAmountPanel.add(cAnglesAmountTextField);
//        cAnglesAmountPanel.setOpaque(false);

//        JPanel dGammaPanel = new JPanel(new FlowLayout());
//        JLabel dGammaLabel = new JLabel("Delta of gamma angle: ");
//        dGammaLabel.setForeground(Color.white);
//        dGammaTextField = new JTextField("5.0");
//        dGammaPanel.add(dGammaLabel);
//        dGammaPanel.add(dGammaTextField);
//        dGammaPanel.setOpaque(false);

        JPanel xFieldAmountPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            xFieldAmountLabel = new JLabel("Amount of fields in length: ");
        } else {
            xFieldAmountLabel = new JLabel("Ilość pól siatki wzdłuż długości: ");
        }
//        xFieldAmountLabel.setForeground(Color.white);
        xFieldAmountTextField = new JTextField("3");
        xFieldAmountTextField.setPreferredSize(new Dimension(60,20));

        xFieldAmountPanel.add(xFieldAmountLabel);
        xFieldAmountPanel.add(xFieldAmountTextField);
        xFieldAmountPanel.setOpaque(false);

        JPanel yFieldAmountPanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            yFieldAmountLabel = new JLabel("Amount of fields in width: ");
        } else {
            yFieldAmountLabel = new JLabel("Ilość pól siatki wzdłuż szerokości: ");
        }
//        yFieldAmountLabel.setForeground(Color.white);
        yFieldAmountTextField = new JTextField("3");
        yFieldAmountTextField.setPreferredSize(new Dimension(60,20));
        yFieldAmountPanel.add(yFieldAmountLabel);
        yFieldAmountPanel.add(yFieldAmountTextField);
        yFieldAmountPanel.setOpaque(false);

        JPanel xLuminairePanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            xLuminaireLabel = new JLabel("Position of luminaire in length: ");
        } else {
            xLuminaireLabel = new JLabel("Współrzędna oprawy wzdłuż długości: ");
        }
//        xLuminaireLabel.setForeground(Color.white);
        xLuminaireTextField = new JTextField("1.5");
        xLuminaireTextField.setPreferredSize(new Dimension(60,20));
        xLuminairePanel.add(xLuminaireLabel);
        xLuminairePanel.add(xLuminaireTextField);
        xLuminairePanel.setOpaque(false);

        JPanel yLuminairePanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            yLuminaireLabel = new JLabel("Position of luminaire in width: ");
        } else {
            yLuminaireLabel = new JLabel("Współrzędne oprawy wzdłuż szerokości: ");
        }
//        yLuminaireLabel.setForeground(Color.white);
        yLuminaireTextField = new JTextField("1.5");
        yLuminaireTextField.setPreferredSize(new Dimension(60,20));
        yLuminairePanel.add(yLuminaireLabel);
        yLuminairePanel.add(yLuminaireTextField);
        yLuminairePanel.setOpaque(false);

        JPanel zLuminairePanel = new JPanel(new FlowLayout());
        if (actualLanguage == Jezyk.ENGLISH){
            zLuminaireLabel = new JLabel("Position of luminaire in height: ");
        } else {
            zLuminaireLabel = new JLabel("Współrzędne oprawy wzdłuż wysokości: ");
        }
//        zLuminaireLabel.setForeground(Color.white);
        zLuminaireTextField = new JTextField("3.0");
        zLuminaireTextField.setPreferredSize(new Dimension(60,20));
        zLuminairePanel.add(zLuminaireLabel);
        zLuminairePanel.add(zLuminaireTextField);
        zLuminairePanel.setOpaque(false);

        centerContainer.add(lengthPanel);
//        centerContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        centerContainer.add(widthPanel);
        centerContainer.add(heightPanel);
        centerContainer.add(floorReflectionFactorPanel);
        centerContainer.add(wallReflectionFactorPanel);
        centerContainer.add(ceilingReflectionFactorPanel);
//        centerContainer.add(cAnglesAmountPanel);
//        centerContainer.add(dGammaPanel);
        centerContainer.add(xFieldAmountPanel);
        centerContainer.add(yFieldAmountPanel);
        centerContainer.add(xLuminairePanel);
        centerContainer.add(yLuminairePanel);
        centerContainer.add(zLuminairePanel);

        background.add(topContainer, BorderLayout.NORTH);
        background.add(centerContainer, BorderLayout.CENTER);
        add(background);

        setResizable(true);
        setVisible(true);

    }

    public void saveCSVMethod(){

        JFileChooser filesave = new JFileChooser();
        FileFilter filter;

        if (actualLanguage == Jezyk.ENGLISH){
            filter = new FileNameExtensionFilter("*.csv files", "csv");
        } else {
            filter = new FileNameExtensionFilter("pliki *.csv", "csv");
        }
        filesave.addChoosableFileFilter(filter);

        int ret;
        if (actualLanguage == Jezyk.ENGLISH){
            ret = filesave.showDialog(panel, "Save generated illuminance distribution");
        } else {
            ret = filesave.showDialog(panel, "Zapisz wygenerowany rozkład natężenia oświetlenia");
        }

        if (ret == JFileChooser.APPROVE_OPTION && roomIsCalcultated){
            File file = filesave.getSelectedFile();
            String csvContentToWrite = actualRoom.returnCSVFromDataToString();
            try {
                //Apache Commons IO ;)
                FileUtils.writeStringToFile(file, csvContentToWrite, false);
                if (actualLanguage == Jezyk.ENGLISH){
                    JOptionPane.showMessageDialog(panel,"File successfully saved", "File saved", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel,"Plik został zapisany", "Plik zapisany", JOptionPane.INFORMATION_MESSAGE);
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

    public void openIESMethod(){
        JFileChooser fileopen = new JFileChooser();
        FileFilter filter;

        if (actualLanguage == Jezyk.ENGLISH){
            filter = new FileNameExtensionFilter("*.ies files", "ies");
        } else {
            filter = new FileNameExtensionFilter("pliki *.ies", "ies");
        }
        fileopen.addChoosableFileFilter(filter);

        int ret;
        if (actualLanguage == Jezyk.ENGLISH){
            ret = fileopen.showDialog(panel, "Open file");
        } else {
            ret = fileopen.showDialog(panel, "Otwórz plik");
        }
        if (ret == JFileChooser.APPROVE_OPTION){
            File file = fileopen.getSelectedFile();
            try {
                double length = Double.parseDouble(Main.changeCommaToDot(lengthTextField.getText()));
                double width = Double.parseDouble(Main.changeCommaToDot(widthTextField.getText()));
                double height = Double.parseDouble(Main.changeCommaToDot(heightTextField.getText()));
                int floorReflectionFactor = Integer.parseInt(floorReflectionFactorTextField.getText());
                int wallReflectionFactor = Integer.parseInt(wallReflectionFactorTextField.getText());
                int ceilingReflectionFactor = Integer.parseInt(ceilingReflectionFactorTextField.getText());
//                double deltaGamma = Double.parseDouble(Main.changeCommaToDot(dGammaTextField.getText()));
                int xFieldAmount = Integer.parseInt(xFieldAmountTextField.getText());
                int yFieldAmount = Integer.parseInt(yFieldAmountTextField.getText());

                ArrayList<Oprawa> luminaires = new ArrayList<Oprawa>(); //can be empty
                double xLuminaire = Double.parseDouble(Main.changeCommaToDot(xLuminaireTextField.getText()));
                double yLuminaire = Double.parseDouble(Main.changeCommaToDot(yLuminaireTextField.getText()));
                double zLuminaire = Double.parseDouble(Main.changeCommaToDot(zLuminaireTextField.getText()));
                ArrayList<WspOprawy> luminairesPosition = new ArrayList<WspOprawy>();
                luminairesPosition.add(new WspOprawy(xLuminaire, yLuminaire, zLuminaire));

                PomieszczenieProstokatne pom1 = new PomieszczenieProstokatne(length, width, height, floorReflectionFactor, wallReflectionFactor,
                        ceilingReflectionFactor, /*deltaGamma,*/ xFieldAmount, yFieldAmount, luminaires, luminairesPosition);
                /*PomieszczenieProstokatne pom1 =*/ Main.bryla_na_siatke(file.getAbsolutePath().toString(), pom1);
                actualRoom = pom1;
                if (Main.isRoomProperlyCalculated == true){
                    if (actualLanguage == Jezyk.ENGLISH){
                        statusLabel.setText("Calculation finished");
                    } else {
                        statusLabel.setText("Obliczenia zakończone");
                    }
                    statusLabel.setVisible(true);
                    saveCSVButton.setVisible(true);
                    saveCSVButton.setEnabled(true);
                    showEButton.setEnabled(true);
                    saveCSVItem.setEnabled(true);
                    roomIsCalcultated = true;
                } else {
                    if (actualLanguage == Jezyk.ENGLISH){
                        statusLabel.setText("Calculation failed");
                    } else {
                        statusLabel.setText("Błąd podczas obliczeń");
                    }
                    statusLabel.setVisible(true);
                    saveCSVButton.setEnabled(false);
                    showEButton.setEnabled(false);
                    saveCSVItem.setEnabled(false);
                }
            }
            catch (CalculationException e){
                if (actualLanguage == Jezyk.ENGLISH){
                    statusLabel.setText("Calculation failed");
                } else {
                    statusLabel.setText("Błąd w obliczeniach");
                }
                statusLabel.setVisible(true);
                saveCSVButton.setEnabled(false);
                showEButton.setEnabled(false);
                saveCSVItem.setEnabled(false);
            }
        }
    }

    public class showEAL implements ActionListener{

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
    }

    public class SaveCSVAL implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            saveCSVMethod();
        }
    }

    public class OpenIESAL implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            openIESMethod();
        }
    }

}
