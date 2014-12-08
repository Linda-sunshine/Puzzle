package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import process.ParseInput;
import data.Tile;
import data.Unit;

public class MainDialog {
	
	private Shell shell;
	Display display;
	private Text text;
	int line = 1;
	int startX = 20;
	int startY = 80;
	int soluNum = 0;
	ParseInput in = new ParseInput(); 
	HashMap<Integer, Color> col = new HashMap<Integer, Color>();

    //Open the window.	
	public void open(){
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}	
	}
	
	// Create the contents of the window.
	public void createContents(){
		
		shell = new Shell();
		shell.setSize(1200, 1000);
		shell.setText("Puzzle Application");
		shell.setLayout(null);
		
//		Menu menu = new Menu(shell, SWT.BAR);
//		shell.setMenuBar(menu);
//		
//		MenuItem mFile = new MenuItem(menu, SWT.None);
//		mFile.setText("File");
		
		Label topLabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);		
		topLabel.setBounds(0, 100, 1200, 20);
		
		Button multiButton1 = new Button(shell, SWT.CHECK);
		multiButton1.setBounds(20, 20, 30, 20);
		Label square = new Label(shell, SWT.NONE);
		square.setBounds(45, 20, 50, 20);
		square.setText("Square");
		
		Button multiButton2 = new Button(shell, SWT.CHECK);
		multiButton2.setBounds(110, 20, 20, 20);
		Label hexagon = new Label(shell, SWT.NONE);
		hexagon.setBounds(135, 20, 60, 20);
		hexagon.setText("Hexagon");
		
		Button multiButton3 = new Button(shell, SWT.CHECK);
		multiButton3.setBounds(200, 20, 50, 20);
		Label brute = new Label(shell, SWT.NONE);
		brute.setBounds(225, 20, 100, 20);
		brute.setText("Brute Force");
		
		Button multiButton4 = new Button(shell, SWT.CHECK);
		multiButton4.setBounds(305, 20, 20, 20);
		Label algorithmX = new Label(shell, SWT.NONE);
		algorithmX.setBounds(330, 20, 150, 20);
		algorithmX.setText("Algorithm X");
		
		Label pathLabel = new Label(shell, SWT.NONE);
		pathLabel.setBounds(20, 70, 100, 20);
		pathLabel.setText("Load file from :");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(130, 70, 400, 20);
	
		//Browse the file.
		Button browseButton = new Button(shell, SWT.NONE);//button 1
		browseButton.setBounds(550, 63, 80, 35);
		browseButton.setText("Browse!");
		
		Button showTilebutton = new Button(shell, SWT.NONE); //button2
		showTilebutton.setBounds(640, 63, 100, 35);
		showTilebutton.setText("Show Tiles!");
		
		browseButton.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent e){
				line = 1;
				FileDialog fileD= new FileDialog(shell, SWT.OPEN);
				fileD.setText("Open");
				fileD.setFilterPath("C:/");
				String[] filterExt = {"*.txt", "*.doc", "*.rtf"};
				
				fileD.setFilterExtensions(filterExt);
				String selected = fileD.open();
				text.setText(selected);
				in.parse(text.getText());
			}
		});
		
		//Show tiles!!
		showTilebutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				try{
					drawTileSquare();
				}catch (IOException e1){
					e1.printStackTrace();
				}
			}
		});
		
		Label tiles = new Label(shell, SWT.NONE);
		tiles.setBounds(20, 130, 50, 20);
		tiles.setText("Tiles :");
		
		//Add button for showing target board
		Button targetBoard = new Button(shell, SWT.NONE);
		targetBoard.setBounds(80, 655, 150, 25);
		targetBoard.setText("Show Target Board");
		targetBoard.addSelectionListener(new SelectionAdapter(){
			
			public void widgetSelected(SelectionEvent e){
				showTarget();
			}
		});
		
		//Add button for showing solutions
		Button solutions = new Button(shell, SWT.NONE);
		solutions.setBounds(530, 655, 150, 25);
		solutions.setText("Show Solutions");
		solutions.addSelectionListener(new SelectionAdapter(){
			
			public void widgetSelected(SelectionEvent e){
				in.process();
				showSolution(soluNum);
				soluNum++;
			}
		});
		
		// Add button for showing next solution
		Button nextSolu = new Button(shell, SWT.NONE);
		nextSolu.setBounds(680, 655, 150, 25);
		nextSolu.setText("Next Solution");
		nextSolu.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				showSolution(soluNum);
				soluNum++;
			}
		});
		
		Label labelBottom = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);		
		labelBottom.setBounds(0, 620, 1200, 50);	
		
	}
	
	public void drawTileSquare() throws IOException{
		
		int tmpStartX = 20;
		int tmpStartY = 80;
//		in = new ParseInput(text.getText());
		int length = 18;
		GC gc = new GC(shell);
		gc.setLineWidth(2);
		line = 1;
//		ParseInput in = new ParseInput(text.getText());
		for( int i = 0; i < in.tiles.length; i++ ){
			Tile tile = in.tiles[i];
			
			//construct id - color map
			Color c = display.getSystemColor(i+3);
			col.put(tile.id, c);
			
			HashSet<Unit> nomUnits = tile.raw.nomUnits;
			Iterator it = nomUnits.iterator();
			
			while(it.hasNext()){

				Unit u = (Unit)it.next();
				int x = u.getX();
				int y = u.getY();
				char ch = u.getCh();
				
				x = tmpStartX + x*length;
				y = tmpStartY + y*length;
				
				String s = (new Character(ch)).toString();
				
				
				gc.setBackground(c);
//				gc.fillRectangle(startX + x * length, startY + y * length, length, length);
//				gc.drawRectangle(startX + x * length, startY + y * length, length, length);
				gc.fillRectangle(x, y, length, length);
				gc.drawRectangle(x, y, length, length);
				gc.drawString(s, x + 4, y + 2);
			} 
			
			tmpStartX = tmpStartX + length * nomUnits.size();	
			if( tmpStartX >= 1200){
				tmpStartX = 20;
				tmpStartY = 100 + 90 * line;
				line++;
			}
		}		
	}
	private void drawTileHexagon(){
		
	}
	
	private void Clear(){
		
		GC gc = new GC(shell);
		
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(-1, 60, 800, 520);
		gc.drawRectangle(-1, 60, 800, 520);
		text.setText("");
		startX = 20;
		startY = 80;
		
	}

	private void showTarget(){
		
		int tmpStartX = 20;
		int tmpStartY = 100 + 125 * line;
		int length = 25;
		
		GC gc = new GC(shell);
		gc.setLineWidth(2);
		gc.drawString(" Target Board :", tmpStartX, tmpStartY - 30);
		
		HashSet<Unit> nomboard = in.board.raw.nomUnits;
		Iterator it = nomboard.iterator();
		
		while(it.hasNext()){
			int m = 0;
			Unit u = (Unit)it.next();
			int x = u.getX();
			int y = u.getY();
			char ch = u.getCh();
			
			x = tmpStartX + x * length;
			y = tmpStartY + y * length;
			
			String s = (new Character(ch)).toString();
			
			gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_YELLOW));
			gc.fillRectangle(x, y, length, length);
			
			gc.drawRectangle(x, y, length, length);
			gc.drawString(s, x + 4, y + 2);
		}
	}	
		
	public void showSolution( int m ){
		
		final int solnumber = in.soluInterface.size();
//		System.out.println(soluNum);
//		System.out.println(solnumber);
		
		int tmpStartX = 550;
		int tmpStartY = 100 + 125 * line;
		int length = 25;
		
		GC gc = new GC(shell);
		gc.setLineWidth(2);
		gc.drawString("There are " + m + "/"+ solnumber + " soultions.", tmpStartX, tmpStartY-30);
		
		if( solnumber - 1 < m ){
			JOptionPane.showMessageDialog(null,"There is no other solutions","ERROR",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
//		for(int m = 0; m < in.soluInterface.size(); m++){
			//HashMap<Integer, ArrayList<Unit>>;
			HashMap<Integer, ArrayList<Unit>> solu = in.soluInterface.get(m);
			
			for(int n = 0; n < solu.size(); n++){
				int tileId = n;
				Color c = col.get(n);
				ArrayList<Unit> tile = solu.get(n);
				
				for( int k = 0; k < tile.size(); k++ ) {
					Unit u = tile.get(k);
					int x = u.getX();
					int y = u.getY();
					char ch = u.getCh();
					
					x = tmpStartX + x * length;
					y = tmpStartY + y * length;
					
					String s = (new Character(ch)).toString();
					
					gc.setBackground(c);
					gc.fillRectangle(x, y, length, length);
					
					gc.drawRectangle(x, y, length, length);
					gc.drawString(s, x + 4, y + 2);
				}
			}
		HashSet<Unit> nomboard = in.board.raw.nomUnits;
		Iterator it = nomboard.iterator();	
	}
		
	private BufferedReader reader(String file){
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return reader;
	}	
	//Main function.
	public static void main(String[] agrs){
		try{
			MainDialog window = new MainDialog();
			window.open();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
