import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class AboutForm extends JDialog {


    public AboutForm() {

        initUI();
    }

    public final void initUI() {

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        JLabel hint = new JLabel("<html><b>Tomasz Potański</b><br><a href='mailto:tomasz@potanski.pl'>tomasz@potanski.pl</a>"
                + "<br> my website: <a href='potanski.pl'>potanski.pl</a>" +
                "</html>");
        hint.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        topPanel.add(hint);

        ImageIcon icon = new ImageIcon(getClass().getResource("zdjecie.jpg"));

        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(label, BorderLayout.EAST);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);

        topPanel.add(separator, BorderLayout.SOUTH);

        basic.add(topPanel);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JTextPane pane = new JTextPane();

        pane.setContentType("text/html");
        String text = "<p><b>Basic information about the program...</b></p>" +
                "<p>Program was written in java using swing GUI widget toolkit.<br>" +
                "The aim of the program is to conduct two basic calculations/convertions:<br>" +
                "1) Generate illuminance distribution from given IES luminaire file and room parameters.<br>" +
                "2) Generate IES luminaire file (and draw it) from given illuminance distribution and room parameters<br>" +
                "<br>It's a program written for Bachelor's Degree at the Warsaw University of Technology (faculty of Electrical Engineering).<br>" +
                "<br>More details about this (and other of my projects) can be found at my website.<br>" +
                "<br>In the future its functionality will also be available on the Android platform and at the website (using probably java spring).<br><br>" +
                "my adviser: dr inż. Sebastian Słomiński<br>" +
                "my reviewer: dr inż. Piotr Pracki<br>" +
                "<br><br>Backgorund image comes from: <a href='http://javierocasio.deviantart.com/art/Stream-of-Light-91150693'>http://javierocasio.deviantart.com/art/Stream-of-Light-91150693</a>" +
                "</p>";
        pane.setText(text);
        pane.setEditable(false);

        JScrollPane paneScrollable = new JScrollPane();
        paneScrollable.getViewport().add(pane);

        pane.setCaretPosition(0);
        textPanel.add(paneScrollable);

        basic.add(textPanel);

        JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

        basic.add(boxPanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton close = new JButton("Close");
        close.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AboutForm.this.dispose();
                    }
                }
        );

        bottom.add(close);
        basic.add(bottom);

        bottom.setMaximumSize(new Dimension(450, 0));

        setTitle("About");

        Dimension dialogSize = new Dimension(450, 400);
        setSize(dialogSize);
        setPreferredSize(dialogSize);
        setMaximumSize(dialogSize);
        setMinimumSize(dialogSize);

        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
