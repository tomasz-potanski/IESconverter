import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Vector;

public class FormShowE extends JFrame{

    Jezyk actualLanguage;
    PomieszczenieProstokatne actualRoom;


    public FormShowE(Jezyk language, PomieszczenieProstokatne pom){

        super("Illuminance distribution");
        setSize(600, 400);
        setLocationRelativeTo(null);
        actualLanguage = language;
        actualRoom = pom;

        if (actualLanguage == Jezyk.ENGLISH){
            this.setTitle("Illuminance distribution");
        } else {
            this.setTitle("Rozkład natężenia oświetlenia");
        }

        Vector<Vector> rowData = new Vector<Vector>();
        double stepLength = pom.getDlugosc() / pom.getIl_punktow_wzdluz_dlugosci();
        for (int actualRow = 0 ; actualRow < pom.getIl_punktow_wzdluz_szerokosci(); actualRow++){
            Vector<String> rowOne = new Vector<String>();
            for (int i = 0 ; i < pom.getIl_punktow_wzdluz_dlugosci() ; i++){
                rowOne.add(String.format("%.2f", pom.getIlluminanceFromField(i, actualRow)));
            }
            rowData.add(rowOne);
        }


        Vector<String> columnNames = new Vector<String>();
        for (int i = 0 ; i < pom.getIl_punktow_wzdluz_dlugosci() ; i++){
            columnNames.add(String.format("%.2f", (stepLength * i) + stepLength/2));
        }

        JTable table = new JTable(rowData, columnNames);

        JScrollPane jsp = new JScrollPane(table);
//        jsp.getViewport().add(drawPanel);
        this.add(jsp);

    }
}
