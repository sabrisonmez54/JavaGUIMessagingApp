import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainWindow implements ActionListener 
{
    public  String              ipName;
    public  String              portName;
    public static String        message;

    public JTextArea            chatBox;
    public static JButton       sendMessage;
    public static JButton       closeButton;
    public static JTextField    messageBox;
    public static JTextField    usernameChooser;

    public static InetAddress   myAddress;
    public static InetAddress   sendAddress;
    public static DatagramPacket packet;
    private static Socket       mySocket;

    public MainWindow(String ipText, String portText, Socket socket) 
    {
        ipName = ipText;
        portName = portText;
        mySocket = socket;
    }

	public void display() 
    {
        String appName = "IP: [ " + ipName + " ]  " + " Port: [ " + portName + " ] ";
        JFrame appFrame = new JFrame(appName);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.RED);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(this);

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() 
        { //close button pressed
            @Override
            public void actionPerformed(ActionEvent arg0) 
            {
                try 
                {//get address to send
                    sendAddress = InetAddress.getByName(ipName.trim());
                }
                catch (UnknownHostException e1) 
                {
                    e1.printStackTrace();
                }
                Test.getMap().remove(sendAddress);

               appFrame.dispose();
                //newFrame.isVisible(f);
            }
        });

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);
        southPanel.add(closeButton, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        appFrame.add(mainPanel);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(500, 300);
        appFrame.setVisible(true);
        appFrame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //get message
        message = messageBox.getText();
        try 
        {//get address to send
            sendAddress = InetAddress.getByName(ipName.trim());
        }
        catch (UnknownHostException e1) 
        {
            e1.printStackTrace();
        }
        //send message, append to chatbox and clear message box
			mySocket.send(message, sendAddress, 64000);
            chatBox.append("Me: "+message + "\n") ;
            System.out.println( message + " Sent to "  + sendAddress);
            messageBox.setText("");
    }
    public JTextArea getChatArea() {
		return this.chatBox;
	}
}