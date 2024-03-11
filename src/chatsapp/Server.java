package chatsapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener{
    JTextField text;
    JPanel textarea;
    static Box vertical=Box.createVerticalBox();
    static JFrame f=new JFrame();
    static DataOutputStream dout;
    
    Server(){
        f.setLayout(null);
        
        JPanel header=new JPanel();
        header.setBackground(Color.black);
        header.setBounds(0,0,600,50);
        header.setLayout(null);
        f.add(header);
        
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2=i1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon i3 =new ImageIcon(i2);
        JLabel back= new JLabel(i3);
        back.setBounds(10,16,20,20);
        header.add(back);
        
        back.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });
        
        JLabel name=new JLabel("USER001");
        name.setBounds(180,12,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SEREF",Font.BOLD,16));
        header.add(name);
        
        JLabel status=new JLabel("Active now");
        status.setBounds(180,28,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SEREF",Font.BOLD,9));
        header.add(status);
        
        textarea=new JPanel();
        textarea.setBounds(5, 50, 590, 570);
        f.add(textarea);
        
        text =new JTextField();
        text.setBounds(5, 655, 430, 40);
        text.setFont(new Font("SAN_SEREF",Font.PLAIN,16));
        f.add(text);
        
        JButton send=new JButton("send");
        send.setBounds(450,655,123,40);
        send.setBackground(Color.BLACK);
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);
        
        f.setSize(615,750);
        f.setLocation(200,70);
        f.getContentPane().setBackground(Color.WHITE);
        
        
        f.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        try{
            String msgtyped = text.getText();
        
        JPanel jp = formatLabel(msgtyped);
        
        textarea.setLayout(new BorderLayout());
        
        JPanel right=new JPanel(new BorderLayout());
        right.add(jp, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));
        
        textarea.add(vertical, BorderLayout.PAGE_START);
        
        dout.writeUTF(msgtyped);
        
        text.setText("");
        
        f.repaint();
        f.invalidate();
        f.validate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static JPanel formatLabel(String msgtyped){
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output= new JLabel("<html><p style=\"width:150px\">"+msgtyped+"</p></html>");
        output.setFont(new Font("Tahoma",Font.PLAIN, 16));
        output.setBackground(Color.LIGHT_GRAY);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));
        
        
        panel.add(output);
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
        
        JLabel timestamp = new JLabel();
        timestamp.setText(sdf.format(c.getTime()));
        
        panel.add(timestamp);
        
        return panel;
    }
    
    public static void main(String[]args){
        new Server();
        
        try{
            ServerSocket skt= new ServerSocket(6001);
            while(true){
                Socket s = skt.accept();
                DataInputStream din=new DataInputStream(s.getInputStream());
                dout=new DataOutputStream(s.getOutputStream());
                
                while(true){
                    String msg=din.readUTF();
                    JPanel panel=formatLabel(msg);
                    JPanel left=new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
