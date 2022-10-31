import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;
//Color cjoice https://www.canva.com/colors/color-palettes/mermaid-lagoon/
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GraphicsEnvironment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Objects;


class MyWindow extends JFrame implements ActionListener{
    public JComboBox<Month> months= new JComboBox<>();
    public BufferedImage myPicture;
    public JLabel picLabel;
    public boolean reserving=false;
    public boolean notis=true;
    public TextField date;
    public JTextField text;
    public JButton reservebutton;
    public  JButton currentbutton;
    public JButton Table;
    public final ArrayList<JButton> buttons;
    public ArrayList<Integer> booked;


    public MyWindow(){
        this.setLayout(new BorderLayout());
        JPanel choices= new JPanel();
        JPanel Tablechoice = new JPanel();
        JPanel reserve= new JPanel();
        reservebutton=new JButton("Reserve");
        currentbutton=new JButton("Walk in");
        date = new TextField();
        date.addActionListener(this);
        buttons= new ArrayList<>();
        reserve.add(reservebutton);
        reserve.add(currentbutton);
        booked= new ArrayList<>();

        reservebutton.setBackground(Color.decode("#145DA0"));
        currentbutton.setBackground(Color.decode("#145DA0"));
        currentbutton.addActionListener(this);
        currentbutton.setActionCommand("cbutton");
        reservebutton.addActionListener(this);
        currentbutton.setActionCommand("rbutton");
        reservebutton.setPreferredSize(new Dimension(400,100));
        currentbutton.setPreferredSize(new Dimension(400,100));
        this.setSize(getToolkit().getScreenSize());
        String img;
        if(this.getHeight()>1000){
            img = "src/floorplan.png";
        } else {
            img = "src/floorplan2.png";
        }
        //Inspiration från https://www.baeldung.com/java-image
        try {
            myPicture = ImageIO.read(new File(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert myPicture != null;
        picLabel = new JLabel(new ImageIcon(myPicture));
        this.add((picLabel),BorderLayout.LINE_START);
        this.add((reserve),BorderLayout.SOUTH);
        this.add((choices),BorderLayout.NORTH);
        choices.setLayout(new FlowLayout());
        choices.add(months);
        Font newFont= new Font("Serif",Font.BOLD,16);
        text = new JTextField("Date: ");
        text.setFont(newFont);
        text.setEditable(false);
        text.setHighlighter(null);
        text.setSize(20,20);
        choices.add(text);
        choices.setBackground(Color.decode("#0C2D48"));
        choices.add(date);
        //Taget ifrån https://stackoverflow.com/questions/39903266/java-for-loop-to-print-each-month-of-the-year
        for (final Month month : Month.values()) {
            months.addItem(month);
        }



        Tablechoice.setLayout(new GridLayout(4,4,0,0));
        for (int i = 0; i < 16; i++) {
            int value= i+1;
            Table=new JButton(""+value+"");
            Tablechoice.add(Table);
            buttons.add(Table);
            Table.addActionListener(this);
            Table.setActionCommand(""+i+"");
            Table.setBackground(Color.decode("#B1D4E0"));
            Table.setFont(new Font("Serif",Font.BOLD,50));

        }
        this.add((Tablechoice), BorderLayout.LINE_END);
        Tablechoice.setPreferredSize(new Dimension(500,500));


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(true);
        months.addActionListener(this);
        String Date=date.getText();
        months.setActionCommand(Date);
        choiceselection(false);

    }
    private void choiceselection(boolean res){
        months.setEnabled(res);
        date.setEditable(res);
        if(res){
            // When you reserve
            reservebutton.setEnabled(false);
            currentbutton.setEnabled(true);
            text.setFont(new Font("Serif",Font.BOLD,16));
            reserving=true;
            if(notis){
                JOptionPane.showInternalMessageDialog(null, "You need to click ENTER on the date to update the information",  "information", JOptionPane.INFORMATION_MESSAGE);
                notis=false;
            }
        } else{
            // When you take in walk in's
            reservebutton.setEnabled(true);
            currentbutton.setEnabled(false);
            text.setFont(new Font("Serif",Font.PLAIN,16));
            reserving=false;
            LocalDate currentdate=LocalDate.now();
            months.setSelectedIndex(currentdate.getMonthValue()-1);
            date.setText(Integer.toString(currentdate.getDayOfMonth()));

        }
    }
    public void draw(){
        ArrayList <Integer> taken= new ArrayList<Integer>();
        for (int i = 0; i < booked.size(); i+=3) {
            // month, date, tile
            if(months.getSelectedIndex()==booked.get(i) && Integer.parseInt(date.getText())==booked.get(i+1)){
                for (int j = 0; j < 16; j++) {

                    if(j==booked.get(i+2)){
                        buttons.get(j).setBackground(Color.decode("#145DA0"));
                        taken.add(j);
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            if(!taken.contains(i)){
                buttons.get(i).setBackground(Color.decode("#B1D4E0"));
            }
        }
    }
    public void input(int table){

        booked.add(months.getSelectedIndex());
        booked.add(Integer.parseInt(date.getText()));
        booked.add(table);
        draw();
    }
    public void remove(int table){
        for (int i = 0; i < booked.size(); i+=3) {
            if(booked.get(i) ==months.getSelectedIndex() && booked.get(i+1)==Integer.parseInt(date.getText())){
                if(table==booked.get(i+2)){
                    booked.remove(i);
                    booked.remove(i);
                    booked.remove(i);
                }
            }
        }
        draw();
    }
    public void actionPerformed(ActionEvent e) {
        System.out.println(this.getWidth());
        String Select= e.getActionCommand();
        int MaxDate;
        if (months.getSelectedIndex() == 1) {
            MaxDate = 28;
        } else if (months.getSelectedIndex() % 2 == 0 && months.getSelectedIndex() < 8) {
            MaxDate = 31;
        } else if (months.getSelectedIndex() % 2 != 0 && months.getSelectedIndex() >= 8) {
            MaxDate = 31;
        } else {
            MaxDate = 30;
        }
        if(e.getSource()==currentbutton){
            choiceselection(false);
        } else if(e.getSource()==reservebutton){
            choiceselection(true);
        } else if(buttons.contains(e.getSource())) {
            int selectInt = Integer.parseInt(Select);
            if (!reserving) {
                if (buttons.get(selectInt).getBackground().equals(Color.decode("#145DA0"))) {
                    int confirm = confirmation("Are you sure they have left the restaurant, and the table is clean?");
                    if (confirm == 0) {
                        remove(selectInt);
                    }
                } else {
                    input(selectInt);
                }
            } else {
                int dateInt = 0;
                try {
                    dateInt = Integer.parseInt(date.getText());
                } catch (NumberFormatException n) {
                    alert("Input a date");
                    return;
                }
                if (dateInt > MaxDate || dateInt < 0) {
                    alert("Invalid date");
                } else {
                    if (buttons.get(selectInt).getBackground().equals(Color.decode("#145DA0"))) {
                        int confirm = confirmation("Are you sure you would like to un-book this table on " +
                                Objects.requireNonNull(months.getSelectedItem()).toString().toLowerCase() + " the " + date.getText() + "th");

                        if (confirm == 0) {
                            remove(selectInt);
                        }
                    } else {
                        int confirm = confirmation("Are you sure you would like to book this table on " +
                                Objects.requireNonNull(months.getSelectedItem()).toString().toLowerCase() + " the " + date.getText() + "th");
                        if (confirm == 0) {
                            input(selectInt);
                        }
                    }

                }
            }
        } else {
            draw();
        }
    }
    public int confirmation(String str){
        return JOptionPane.showOptionDialog(null, str, "Click a button",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

    }
    public void alert(String str){
        JOptionPane.showMessageDialog(null, str, "alert", JOptionPane.ERROR_MESSAGE);
    }
}
public class main {
    private static MyWindow window;
    public static void main(String[] args) {
        window=new MyWindow();
    }
}
