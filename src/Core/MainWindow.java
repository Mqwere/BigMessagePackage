package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import Core.Program;
import Support.BinaryOperator;
import Support.FileControler;
import Support.Entities.InputWindow;
import Support.Entities.Payload;
import Support.Entities.RegisterEntry;


public class MainWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JPanel panel = new JPanel();
	InputWindow slave;
	
	JTextArea area = new JTextArea();
	JScrollPane pane = new JScrollPane(area);
	
	JButton loadFile = new JButton("Load"),
			compare	 = new JButton("Compare"),
			writeTo	 = new JButton("WriteTo"),
			translate= new JButton("Translate"),
			givePLD	 = new JButton("Add Payload");
	
	public MainWindow() {
		setSize(200+Program.AREA_WIDTH, 640);
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
		component.setBounds(rec.width/32, ((1 + 15*multiplier++)*rec.height)/80,  rec.width-Program.AREA_WIDTH-(rec.width/16)+((20*Program.AREA_WIDTH)/320), (rec.height*6)/40); 
		
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
		setUp(givePLD);
		
		panel.setLayout(null);
		setContentPane(panel);		
	}
	
	public void lockAllButtons(boolean dewIt) {
		if(dewIt) {
			loadFile	.setEnabled(false);
			compare		.setEnabled(false);
			writeTo		.setEnabled(false);
			translate	.setEnabled(false);
			givePLD		.setEnabled(false);
		}
		else {
			loadFile.setEnabled(true);
			if(Program.register.size()>0) {
				writeTo		.setEnabled(true);
				translate	.setEnabled(true);
				givePLD		.setEnabled(true);
			}
			if(Program.register.size()>1) 
				compare		.setEnabled(true);
		}
		
	}
	
	public void processInput(String input) {
		this.slave.close();
		//Program.sysp(input);
		ArrayList<Byte> internal;
		RegisterEntry prepImg = Program.getLastImage();
		if(input.length()>Program.imageCharsLimit) {
			Program.error("The message is too long for the given image, which can take up to "+Program.imageCharsLimit+" charas.");
			if(input.length()>0) internal  = BinaryOperator.strToBin("emp"+input.substring(0, Program.imageCharsLimit)+'\4');
			else internal = null;
		}
		else {
			internal  = BinaryOperator.strToBin("emp"+input+'\4');
		}
		if(internal!=null)FileControler.saveToFile(this,BinaryOperator.writeTo(prepImg, new Payload("emp",internal)));
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
			String output = BinaryOperator.translate(Program.getLastImage());
			if(output!=null) Program.write("\n[Big Message Package Content]:\n"+output);
			else Program.write("AMADEUS AMADEUS. AMADEUS.");
		}
		else
		if(source == givePLD) {
			Payload payload = FileControler.loadPayload(this);
			Program.sleep(1000);
			if(payload!=null) {
				if(payload.size()>Program.imageCharsLimit*2) Program.error("Payload is too big for the image to fit in");
				else {
					payload.sign();
					RegisterEntry entity = BinaryOperator.writeTo(Program.getLastImage(), payload);
					FileControler.saveToFile(this,entity);
				}
			}
			else Program.error("Could not load payload.");
		}
		
		lockAllButtons(false);
	}
}
