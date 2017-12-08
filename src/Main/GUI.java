package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class GUI extends JFrame
{
    BufferedReader in;
    PrintWriter out;
    GamePanel game;
    ChatPanel chat;

    public GUI(BufferedReader in, PrintWriter out)
    {
        super("Chinese Checkers");
        this.in=in;
        this.out=out;
        setSize(600, 300);
        setVisible(true);
        setLayout(new BorderLayout(1, 1));

        game = new GamePanel();
        chat = new ChatPanel();

        add(game, BorderLayout.CENTER);
        add(chat, BorderLayout.EAST);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                int i=JOptionPane.showConfirmDialog(null, "Are you sure you want to exit Chinese Checkers?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if(i==0)
                {
                    out.println("exit");
                }
                else
                {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        pack();

    }

    public class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(400, 400));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // prostokat
            g2d.drawRect(10, 10, 380, 700);
            // kolo
            g2d.drawOval(10, 10, 380, 380);
        }
    }

    public class ChatPanel extends JPanel
    {
        private JTextField dataField;
        public JTextArea messageArea;

        public ChatPanel()
        {
            dataField = new JTextField(20);
            messageArea = new JTextArea(8, 20);

            dataField.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    out.println(dataField.getText());
                    dataField.selectAll();
                    dataField.setText("");
                }
            });
            messageArea.setEditable(false);
            add(new JScrollPane(messageArea));
            add(dataField);
            setPreferredSize(new Dimension(400, 400));
        }

    }

}
