package Support;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Core.MainWindow;
import Core.Program;

public class InputWindow extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	
	MainWindow 	master;
	JTextArea 	area = new JTextArea();
	JLabel		info = new JLabel("To enter into a new line, please use soft enter (shift + enter)");
	JPanel 		panel = new JPanel();
	boolean		shiftPressed = false;
	
	public InputWindow() {
		this(Program.mainWindow);
	}
	
	public InputWindow(MainWindow master) {
		setSize(600, 200);
		this.master = master;
		setLocationRelativeTo(null);
		setTitle("Message input window");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel.setBackground(new Color(80,123,140));
		setResizable(false);
		setContent();
		setVisible(true);
		setVisible(true);
	}
	
	private void setContent(){
		panel.add(info);
		info.setBounds(30, 15, 540, 20);
			
		panel.add(area);
		area.setBounds(30, 40, 540, 90);
		area.setBackground(new Color(200,200,200));	
		area.addKeyListener(this);
		
		panel.setLayout(null);
		setContentPane(panel);
	}
	
	public void close() {
		this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int code = event.getKeyCode();

		if (code == KeyEvent.VK_ENTER) {
			if(shiftPressed) this.area.append("\n");
			else {
				this.master.processInput(this.area.getText());
			}
		}
		else
		if(code == KeyEvent.VK_ESCAPE) {
			close();
		}
		else
		if(code == KeyEvent.VK_SHIFT) {shiftPressed = true;}
		
	}

	@Override
	public void keyReleased(KeyEvent event) {if (event.getKeyCode() == KeyEvent.VK_SHIFT) {shiftPressed = false;}}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
