package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import Core.Program;
import Support.BinaryOperator;
import Support.Converter;
import Support.FileControler;
import Support.Entities.InputWindow;
import Support.Entities.RegisterEntry;
import Support.Enums.FileType;


public class MainWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JPanel panel = new JPanel();
	InputWindow slave;
	
	JTextArea area = new JTextArea();
	JScrollPane pane = new JScrollPane(area);
	
	JButton loadFile = new JButton("Load"),
			compare	 = new JButton("Compare"),
			writeTo	 = new JButton("WriteTo"),
			translate= new JButton("Translate");
	
	public MainWindow() {
		setSize(640, 640);
		setLocationRelativeTo(null);
		setTitle("Big Message Package");
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
	
	private int multiplier = 0;
	private void setUp(JComponent component) {
		Rectangle rec = this.getBounds();
		panel.add(component); 
		component.setBounds(rec.width/32, ((1 + 15*multiplier++)*rec.height)/80,  rec.width-Program.AREA_WIDTH-(rec.width/16)+((20*Program.AREA_WIDTH)/320), (rec.height*5)/40); 
		
		if(component.getClass()==JButton.class) {
			JButton button = (JButton) component;
			button.addActionListener(this);
			component.setEnabled(false);
		}
	}
	
	private void setContent(){
		Rectangle rec = this.getBounds();
		area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
		panel.add(pane); 
		pane.setBounds(rec.width-Program.AREA_WIDTH+(20*Program.AREA_WIDTH)/320, (rec.height)/80,  (Program.AREA_WIDTH*7)/8, (rec.height*36)/40); 
		area.setEditable(false); area.setBackground(new Color(200,200,200));
		
		setUp(loadFile);
		loadFile.setEnabled(true);
		
		setUp(compare);
		setUp(writeTo);
		setUp(translate); 
		
		panel.setLayout(null);
		setContentPane(panel);		
	}
	
	public void lockAllButtons(boolean dewIt) {
		if(dewIt) {
			loadFile	.setEnabled(false);
			compare		.setEnabled(false);
			writeTo		.setEnabled(false);
			translate	.setEnabled(false);
		}
		else {
			loadFile.setEnabled(true);
			if(Program.register.size()>0) {
				writeTo	.setEnabled(true);
				translate.setEnabled(true);
			}
			if(Program.register.size()>1) compare.setEnabled(true);
		}
		
	}
	
	public void processInput(String input) {
		this.slave.close();
		//Program.sysp(input);
		ArrayList<Byte> internal;
		RegisterEntry prepImg = Program.getLastImage();
		if(input.length()>Program.imageCharsLimit) {
			Program.error("The message is too long for the given image, which can take up to "+Program.imageCharsLimit+" charas.");
			if(input.length()>0) internal  = BinaryOperator.strToBin(input.substring(0, Program.imageCharsLimit-1)+'\4');
			else internal = null;
		}
		else {
			internal  = BinaryOperator.strToBin(input+'\4');
		}
		if(internal!=null)FileControler.saveToFile(this,BinaryOperator.writeTo(prepImg, internal));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		lockAllButtons(true);
		if(source == loadFile) {
			RegisterEntry entry = FileControler.fileToRegister(this);
			if(entry!=null) Program.register.add(entry);
			else Program.error("Given Image file was not added to the register. Please try another image.");
			if(Program.register.size()>0) {
				writeTo.setEnabled(true);
				translate.setEnabled(true);
			}
			if(Program.register.size()>1) compare.setEnabled(true);
		}
		else
		if(source == compare) {
			FileControler.compare(Program.register.get(Program.register.size()-2), Program.register.get(Program.register.size()-1));
		}
		else
		if(source == writeTo) {
			slave = new InputWindow();
		}
		else
		if(source == translate) {
			Program.write("\n[Big Message Package Content]:\n"+BinaryOperator.translate(Program.getLastImage()));
		}
		
		lockAllButtons(false);
	}
}
