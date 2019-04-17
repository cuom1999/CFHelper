import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CFHelper extends JFrame{

	FlowLayout fl              	  = new FlowLayout();
	JLabel	   homeDirLabel       = new JLabel("Home Directory: ");
	JTextField homeDirTextField   = new JTextField("", 25);
	JButton    submitButton[]     = new JButton[8];
	JLabel     submitLabel 		  = new JLabel("Submit: ");
	JLabel 	   contestIDLabel	  = new JLabel("Contest ID:    ");
	JTextField contestIDTextField = new JTextField("", 12);
	JButton    genContestButton   = new JButton("Generate Contest");
	JLabel     openLabel 		  = new JLabel("Open: ");
	JButton    openButton[]		  = new JButton[8];

	// Constructor
	public CFHelper() throws Exception{
		super("CFHelper");

		setIconImage((new ImageIcon("helper.png")).getImage());

		setLayout(fl);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);

		add(homeDirLabel);
		homeDirTextField.setText(new Scanner(new File("homeDir.txt")).next());
		add(homeDirTextField);

		add(contestIDLabel);
		add(contestIDTextField);

		genContestButton.addActionListener(new GenButtonAction());
		add(genContestButton);

		String s = openLabel.getText();
		for (int i = 0; i < 90; i++) s += ' ';
			openLabel.setText(s);

		add(openLabel);
		addOpenButtons();

		s = submitLabel.getText();
		for (int i = 0; i < 90; i++) s += ' ';
			submitLabel.setText(s);

		add(submitLabel);
		addSubmitButtons();
		

		setVisible(true);
	}

	public void addSubmitButtons() {
		for (int i = 0; i < 8; i++) {
			char c = (char) ((int) 'A' + i);
			submitButton[i] = new JButton(Character.toString(c));
			submitButton[i].addActionListener(new SubmitButtonAction(c));
			add(submitButton[i]);
		}
	}

	public void addOpenButtons() {
		for (int i = 0; i < 8; i++) {
			char c = (char) ((int) 'A' + i);
			openButton[i] = new JButton(Character.toString(c));
			openButton[i].addActionListener(new OpenButtonAction(c));
			add(openButton[i]);
		}
	}

	public class OpenButtonAction implements ActionListener {
		char problem;
		public OpenButtonAction(char problem) {
			this.problem = problem;
		}
		public void actionPerformed (ActionEvent event) {
			try {
				String contestID = contestIDTextField.getText().trim();

				String homeDir   = homeDirTextField.getText().trim() + contestID + "\\" + contestID + Character.toString(problem) + "\\";
				
				String fileDir = contestID + Character.toString(problem) + ".cpp";
				String inputDir = "input.txt";

				ProcessBuilder pb = new ProcessBuilder();
				pb.directory(new File(homeDir));
				pb.command("cmd.exe", "/c", "\"D:\\Program Files\\Sublime Text 3\\sublime_text\"", fileDir);
				pb.start();
				
				pb.command("cmd.exe", "/c", "\"D:\\Program Files\\Sublime Text 3\\sublime_text\"", inputDir);
				pb.start();
			}
			catch (Exception exception) {
				System.out.println("Fail to open files");
			}
		}
	}


	public class SubmitButtonAction implements ActionListener {
		char problem;
		public SubmitButtonAction(char problem) {
			this.problem = problem;
		}
		public void actionPerformed (ActionEvent event) {
			try {
				String contestID = contestIDTextField.getText().trim();
				String homeDir   = homeDirTextField.getText().trim() + contestID + "\\" + contestID + Character.toString(problem) + "\\";
				String fileDir = contestID + Character.toString(problem) + ".cpp";
				
				ProcessBuilder pb = new ProcessBuilder();
				pb.directory(new File(homeDir));

				pb.command("cmd.exe", "/c", "cf", "submit", contestID, Character.toString(problem), fileDir);
				pb.start();
				
				Thread.sleep(1000);
				pb.command("cmd.exe", "/c", "start", "chrome", String.format("http://codeforces.com/contest/%s/my", contestID));
				pb.start();
			}
			catch (Exception exception) {
				System.out.println("Fail to submit");
			}
		}
	}

	public class GenButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String homeDir   = homeDirTextField.getText().trim();
				String drive     = homeDir.substring(0, 2);
				String contestID = contestIDTextField.getText().trim();
				System.out.println(contestID.length());

				ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c");
				pb = pb.directory(new File(homeDir));
				pb.command("cmd.exe", "/c", "md", contestID);
				pb.start();
				Thread.sleep(300);

				pb.directory(new File(homeDir + "\\" + contestID));
				for (int i = 0; i < 8; i++) {
					char problem = (char) ((int) 'A' + i);
					pb.command("cmd.exe", "/c", "md", contestID + problem); 
					pb.start();
				}
			}
			catch (Exception e) {
				System.out.println("Fail to generate contest");
			}
		} 
	}

	public static void main(String[] args) throws Exception{
		CFHelper app = new CFHelper();

	} 

}
