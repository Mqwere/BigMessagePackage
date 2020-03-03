package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Core.Program;
import Support.FileControler;


public class MainWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JPanel panel = new JPanel();
	
	JTextArea area = new JTextArea();
	JScrollPane pane = new JScrollPane(area);
	
	JButton loadFile = new JButton("Load");
	JButton compare	 = new JButton("Compare");
	JButton whiteOut = new JButton("WhiteOut");
	
	public MainWindow() {
		setSize(640, 640);
		setLocationRelativeTo(null);
		setTitle("Name Compiler");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.panel.setBackground(new Color(50, 93,110));
		setResizable(false);
		setContent();
		setVisible(true);
	}
	
	public void addToArea(String input) {
		area.append(input);
	}
	
	public void addToArea(char input) {
		String temp = new String(""+input);
		this.addToArea(temp);
	}
	
	private void setContent(){
		Rectangle rec = this.getBounds();
		area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
		panel.add(pane); pane.setBounds
		(rec.width-Program.AREA_WIDTH+(20*Program.AREA_WIDTH)/320, (rec.height)/80,  (Program.AREA_WIDTH*7)/8, (rec.height*36)/40); 
		area.setEditable(false); area.setBackground(new Color(200,200,200));
		
		panel.add(loadFile); 
		loadFile.setBounds(rec.width/32, (rec.height)/80,  rec.width-Program.AREA_WIDTH-(rec.width/16)+((20*Program.AREA_WIDTH)/320), (rec.height*5)/40); 
		loadFile.addActionListener(this);
		
		panel.add(compare); 
		compare.setBounds(rec.width/32, (rec.height*15)/80,  rec.width-Program.AREA_WIDTH-(rec.width/16)+((20*Program.AREA_WIDTH)/320), (rec.height*5)/40); 
		compare.addActionListener(this);
		compare.setEnabled(false);
		
		panel.add(whiteOut); 
		whiteOut.setBounds(rec.width/32, (rec.height*29)/80,  rec.width-Program.AREA_WIDTH-(rec.width/16)+((20*Program.AREA_WIDTH)/320), (rec.height*5)/40); 
		whiteOut.addActionListener(this);
		whiteOut.setEnabled(false);
		
		panel.setLayout(null);
		setContentPane(panel);		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source == loadFile) {
			Program.register.add(FileControler.fileToByteArray(this));
			if(Program.register.size()>0) whiteOut.setEnabled(true);
			if(Program.register.size()>1) compare.setEnabled(true);
			
			ArrayList<Byte> temp = Program.register.get(Program.register.size()-1);			
		}
		else
		if(source == compare) {
			FileControler.compare(Program.register.get(Program.register.size()-2), Program.register.get(Program.register.size()-1));
		}
		else
		if(source == whiteOut) {
			FileControler.saveToDefault(FileControler.whiteOut(Program.register.get(Program.register.size()-1)));
		}
	}
}
