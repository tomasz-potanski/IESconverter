import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Form1 extends JFrame{
    private JPanel panel1;
    JLabel languageLabel;
    JRadioButton angielski, polski;
    JButton b1, b2;
    Jezyk actualLanguage = Jezyk.ENGLISH;


    public Form1(){
        super("IES Converter");
        setTitle("IES Converter");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
//        JLabel background=new JLabel(new ImageIcon(getClass().getResource("1.png")));
        JLabel background=new JLabel();
        add(background);
        background.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        b1 = new JButton("IES ->> Illuminance distribution");
        b2 = new JButton("Illuminance distribution ->> IES");

        JPanel languagePanel = new JPanel();
        ButtonGroup languageGroup = new ButtonGroup();
        polski = new JRadioButton("Polish");
        polski.setSelected(false);

        angielski = new JRadioButton("English");
        angielski.setSelected(true);

        languageLabel = new JLabel("Choose language:");

//        polski.addChangeListener(new PCL());
        polski.addActionListener(new PCL());
        angielski.addActionListener(new ECL());

        languageGroup.add(polski);
        languageGroup.add(angielski);

        languagePanel.add(polski);
        languagePanel.add(angielski);

        b1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        FormFromIES f2 = new FormFromIES(Form1.this, actualLanguage);
                    }
                }

        );

        b2.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        FormFromIlluminance f2 = new FormFromIlluminance(Form1.this, actualLanguage);
                    }
                }

        );

        background.add(b1);
        background.add(b2);
        background.add(languageLabel);
        background.add(languagePanel);

//        background.add(b3);

        setResizable(false);
        setVisible(true);
    }

    public class PCL implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            languageLabel.setText("Wybierz język: ");
            angielski.setText("Angielski");
            polski.setText("Polski");
            b1.setText("IES ->> rozkład natężenia oświetlenia");
            b2.setText("rozkład natężenia oświetlenia ->> IES");
            actualLanguage = Jezyk.POLSKI;
        }
    }

    public class ECL implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            languageLabel.setText("Choose language: ");
            angielski.setText("English");
            polski.setText("Polish");
            b1.setText("IES ->> illuminance distribution");
            b2.setText("illuminance distribution ->> IES");
            actualLanguage = Jezyk.ENGLISH;
        }
    }

}
