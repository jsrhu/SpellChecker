/*
 * 2XB3 Spell Checker
 * By Joshua Hu, Nicholas Lago, Connor Sheehan
 * 
 * This application uses algorithmic solutions to solve the common problem
 * of incorrect spelling in digital applications.
 */

package gui;

import spellcheck.*;
import typocheck.Correction;
import typocheck.TypoChecker2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/*
 * Main class of the application. Holds all GUI objects and back-end spell/typo checking objects
 */
public class SpellCheckGui extends JFrame {

	/*
	 * This thread handles backend creation to speed up GUI drawing
	 * The GUI does not require backend to be completely instantiated to draw to the screen
	 * Therefore the GUI will draw itself at the same time as the backend components are created
	 */
	static class DatabaseCreatorThread implements Runnable{
		public void run(){
			try {
				sc = new SpellCheck();
				tc = new TypoChecker2();
				gui.noUnderLine("READY!");
				Thread.currentThread().join();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(InterruptedException ie){}
		}
	}
	
												//Date:
	private static final long serialVersionUID = 02042015L;
	
	/*
	 * Components of the SpellCheckGUI GUI
	 */
	private JPanel mainWindow;
	private JPanel inputs;
	private JPanel outputs;
	private JTextPane outputHighlight;
	private JScrollPane highlightScroll, correctionScroll;
	private JTextArea outputCorrection;
	private JTextField stringInput;
	private JButton startChecker;
	private JButton addWord;
	private JButton addTypo;
	private JButton browseButton;
	
	//File to be read from to spell check
	//Will be used to save spell checked input back to file
	private File fileInput;

	
	//SpellChecker and TypoChecker
	
	private static SpellCheck sc;
	private static TypoChecker2 tc;
	
	//static GUI object
	private static SpellCheckGui gui;
	
	/*
	 * Constructs the spell check GUI
	 */
	public SpellCheckGui(){
		
		//Create separate thread to start database creation and run it, then continue with GUI drawing
		new Thread(new DatabaseCreatorThread()).start();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//1 create the main panel
		this.mainWindow = new JPanel();
		this.mainWindow.setLayout(new BorderLayout());
		this.mainWindow.setBorder(new EmptyBorder(0,0,0,0));
		
		this.inputs = new JPanel(new GridLayout());
		
		//TODO find out how the actual text box works and implement
		//2
		this.stringInput = new JTextField("Enter a string to spell-check");
		this.stringInput.setBounds(0, 0, this.getWidth(), 100);
		
		//3
		this.browseButton = new JButton("Browse");
		
		//4
		this.startChecker = new JButton("GO");
		
		//5
		this.addWord = new JButton("Add Word");
		this.addTypo = new JButton("Add Typo");
		
		//6 Add all smaller elements to main window
		this.inputs.add(browseButton);
		this.inputs.add(startChecker);
		this.inputs.add(addWord);
		this.inputs.add(addTypo);
		
		
		this.outputHighlight = new JTextPane();
		this.outputHighlight.setBounds(0, 0, this.getWidth(), 600);

		this.outputCorrection = new JTextArea();
		
		this.correctionScroll = new JScrollPane(this.outputCorrection);
		this.correctionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.outputs = new JPanel(new GridLayout());
		
		
		this.highlightScroll = new JScrollPane(this.outputHighlight);
		this.highlightScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.outputs.add(this.correctionScroll);
		this.outputs.add(this.highlightScroll);
		this.mainWindow.add(this.stringInput, BorderLayout.NORTH);
		this.mainWindow.add(this.outputs, BorderLayout.CENTER);
		this.mainWindow.add(this.inputs, BorderLayout.SOUTH);

		
		//Add main window to JFrame and finish drawing
		this.add(mainWindow);
		this.setTitle("2XB3 Spell Checker - Check yo self before ya wreck yo self");
		this.mainWindow.setVisible(true);
		this.setBounds(0, 0, 600, 500);
		this.setResizable(false);
		
		
		
		//7.
		//this button opens a browse dialog
		this.browseButton.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent evt){
				if(!gui.fileLoaded()){
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents", "txt");
					fc.setFileFilter(filter);
					int returnVal = fc.showOpenDialog(getParent());
					if(returnVal == JFileChooser.APPROVE_OPTION){
						gui.fileInput = fc.getSelectedFile();
						gui.browseButton.setText("Remove File");
						gui.clearOutputScreens();
						gui.outputCorrection.append(gui.fileInput.getAbsolutePath() + "\nhas been loaded!");
					}
				}else{
					gui.fileInput = null;
					gui.browseButton.setText("Browse");
				}
				
			}
		});
		
		//this button starts the spell checker
		this.startChecker.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent evt){
				gui.clearOutputScreens();
				if (gui.fileLoaded()){
					try {
						Scanner input = new Scanner(gui.fileInput);
						gui.noUnderLine(gui.fileInput.getAbsolutePath() + ":\n****************************************\n");
						while (input.hasNextLine()){
							String line = input.nextLine();
							if(line.length() >= 1) gui.analyzeAndPrintLine(line);
						}
						input.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//TODO spell check the string and print it out
					String str = gui.stringInput.getText();
					try {
						gui.analyzeAndPrintLine(str);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		//this button adds a typo
		this.addTypo.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent evt){
				gui.clearOutputScreens();
				String add = gui.stringInput.getText();
				if(Pattern.matches("^([\\x20-\\x7E])+(->)(([\\x20-\\x7E])+(\\,)(\\s))*([\\x20-\\x7E])+", add)){
					gui.addToTypoChecker(add);
					gui.outputCorrection.append("Correct form. " + add + " added.");
				}else gui.outputCorrection.append("Incorrect form. Must be:\nWord->Correction1, Correction2, CorrectionN");
			}
		});
		
		//this button adds a word
		this.addWord.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent evt){
				String add = gui.stringInput.getText();
				gui.addToDictionary(add);
			}
		});
		
	}
	
	//prints out to the GUI underlined with the correct color
	private void underLine(boolean isCorrection, String out) throws BadLocationException{
		SimpleAttributeSet set = new SimpleAttributeSet();
		if (isCorrection) 	StyleConstants.setForeground(set, Color.BLUE);
		else {
			StyleConstants.setForeground(set, Color.RED);
			this.outputCorrection.append("No suggestion for " + out + "\n");
		}
		Document currentState = this.outputHighlight.getStyledDocument();
		currentState.insertString(currentState.getLength(), out + " ", set);
	}
	
	//prints out to the GUI with no underline
	private void noUnderLine(String out) throws BadLocationException{
		Document currentState = this.outputHighlight.getStyledDocument();
		currentState.insertString(currentState.getLength(), out + " ", new SimpleAttributeSet());
	}
	
	/*
	 * We simply write to the correction pane and add a newline
	 */
	private void drawCorrection(String key){
		String c = tc.getCorrectionString(key);
		this.outputCorrection.append(c + "\n");
	}
	
	/*
	 * Clears the outputs for new analysis
	 */
	private void clearOutputScreens(){
		gui.outputCorrection.setText("");
		gui.outputHighlight.setText("");
	}
	
	//Takes in a full line and prints to the screen
	private void analyzeAndPrintLine(String line) throws BadLocationException{
		String[] 	words = line.split(" ");
		boolean[] 	errors = sc.check(words);
		String[][] 	corrections = tc.searchLine(words);
		
		for(int i = 0; i < words.length; i++){
				if(errors[i]) {
					this.noUnderLine(words[i]);
				}else{
					if(corrections[i] == null) {
						this.underLine(false, words[i]);
					}else{
						this.underLine(true, words[i]);
						this.drawCorrection(words[i]);
					}
				}
		}this.noUnderLine("\n");
	}
	
	/*
	 * This method adds a new word to the data set
	 */
	private void addToDictionary(String newWord){
		try {
			sc.addWord(newWord);
			sc = new SpellCheck();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//add to typochecker
	private void addToTypoChecker(String add){
		String[] corrections = add.split("->");
		String typo = corrections[0].toString();
		corrections = corrections[1].split(",");
		for(int i = 0; i < corrections.length; i++) corrections[i] = corrections[i].trim();
		tc.addCorrection(new Correction(typo, corrections));
		try {
			tc.saveData();
		} catch (InterruptedException e) {
		}
	}
	
	//returns t/f for "Is there a file loaded to spell check?"
	private boolean fileLoaded(){
		return this.fileInput != null; 
	}
	
	
	//Run the program
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					gui = new SpellCheckGui();
					gui.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

}
