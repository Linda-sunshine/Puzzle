package ui;

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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
	
	protected Display display;
	protected Shell shell;
	protected Text text;
	
	protected int line1X = 0; //The first separation line.
	protected int line1Y = 100; //The first separation line.
	protected int length = 10;
	
	protected int line = 1;
	protected int startX = 20;
	protected int startY = 80;
	protected int soluNum = 0;
	protected ParseInput in = new ParseInput(); 
	protected HashMap<Integer, Color> col = new HashMap<Integer, Color>();
	
	//Default value is square, algorithm x.
	protected boolean squareFlag = true;
	protected boolean xFlag = false;
	
	public MainDialog(){
		this.shell = new Shell(display, SWT.CLOSE|SWT.SYSTEM_MODAL);
	}
	
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
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		MenuItem mFile = new MenuItem(menu, SWT.None);
		mFile.setText("File");
		
		Label topLabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);		
		topLabel.setBounds(line1X, line1Y, 1200, 20);
		
		Button multiButton1 = new Button(shell, SWT.RADIO);
		multiButton1.setBounds(20, 20, 30, 20);
		Label square = new Label(shell, SWT.NONE);
		square.setBounds(45, 20, 50, 20);
		square.setText("Square");
		multiButton1.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent e){
				squareFlag = true;
			}
		});
		
		Button multiButton2 = new Button(shell, SWT.RADIO);
		multiButton2.setBounds(110, 20, 20, 20);
		Label hexagon = new Label(shell, SWT.NONE);
		hexagon.setBounds(135, 20, 60, 20);
		hexagon.setText("Hexagon");
		multiButton2.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent e){
				squareFlag = false;
			}
		});
		
		Label pathLabel = new Label(shell, SWT.NONE);
		pathLabel.setBounds(20, 70, 100, 20);
		pathLabel.setText("Load file from :");	
		text = new Text(shell, SWT.BORDER);
		text.setBounds(130, 70, 400, 20);
	
		//Browse the file.
		Button browseButton = new Button(shell, SWT.NONE);//button 1
		browseButton.setBounds(550, 63, 80, 35);
		browseButton.setText("Browse!");
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
		
		//Show tiles.
		Button showTilebutton = new Button(shell, SWT.NONE); //button2
		showTilebutton.setBounds(640, 63, 100, 35);
		showTilebutton.setText("Show Tiles!");		showTilebutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				try{
					drawTile();
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
		targetBoard.setBounds(line1X+80, line1Y+600, 150, 25);
		targetBoard.setText("Show Target Board");
		targetBoard.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				showTarget();
			}
		});
		
		//Add button for choosing brute force.
		Button multiButton3 = new Button(shell, SWT.NONE);
		multiButton3.setBounds(300, line1Y+600, 140, 30);
		multiButton3.setText("Brute Force");
		multiButton3.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent e){
				xFlag = true;
			}
		});
		
		//Add button for choosing algorithm X.
		Button multiButton4 = new Button(shell, SWT.NONE);
		multiButton4.setBounds(450, line1Y+600, 140, 30);
		multiButton4.setText("Algorithm X");
		multiButton4.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent e){
				xFlag = false;
			}
		});
		
		//Add button for showing solutions
		Button solutions = new Button(shell, SWT.NONE);
		solutions.setBounds(600, line1Y+600, 140, 30);
		solutions.setText("Show Solutions");
		solutions.addSelectionListener(new SelectionAdapter(){	
			public void widgetSelected(SelectionEvent e){
				in.setBruteFlag(xFlag);
				in.process(); ////*******parameter!!!
				showSolution(soluNum);
				soluNum++;
			}
		});
		
		// Add button for showing next solution
		Button nextSolu = new Button(shell, SWT.NONE);
		nextSolu.setBounds(750, line1Y+600, 140, 30);
		nextSolu.setText("Next Solution");
		nextSolu.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				showSolution(soluNum);
				soluNum++;
			}
		});
		
		Label labelBottom = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);		
		labelBottom.setBounds(0, 620, 1200, 50);	
		
		Button clearButton = new Button(shell, SWT.NONE);
		clearButton.setBounds(900, line1Y+600, 140, 30);
		clearButton.setText("Clear Screen");
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Clear();
			}
		});
	}

	//Draw tiles!!
	public void drawTile() throws IOException{
		int tmpStartX = this.line1X + 20;
		int tmpStartY = this.line1Y + 80;
//		in = new ParseInput(text.getText());
		GC gc = new GC(shell);
		gc.setLineWidth(2);
		line = 1;
//		ParseInput in = new ParseInput(text.getText());
		for( int i = 0; i < in.tiles.length; i++ ){
			Tile tile = in.tiles[i];
			Color c = display.getSystemColor(i+3);
			col.put(tile.id, c);
			
			HashSet<Unit> nomUnits = tile.raw.nomUnits;
			Iterator<Unit> it = nomUnits.iterator();
			
			while(it.hasNext()){
				Unit u = (Unit)it.next();
				int x = u.getX();
				int y = u.getY();
				//char ch = u.getCh();
				//String s = (new Character(ch)).toString();
				gc.setBackground(c);
				if(squareFlag){
					x = tmpStartX + 10 + x*length;
					y = tmpStartY + y*length;
					gc.fillRectangle(x, y, length, length);
					gc.drawRectangle(x, y, length, length);
					//gc.drawString(s, x + 4, y + 2);
					
				} else{
					double tmpX = 1.5*x*length;
					double tmpY1 = 1.732*y*length;
					double tmpY2 = 0.866*x*length;
					int coorX = tmpStartX + (int) tmpX;
					int coorY = tmpStartY + (int) tmpY1 + (int) tmpY2;
					int pArray[] = new int[12];
					pArray[0] = coorX;
					pArray[1] = coorY;
					pArray[2] = (int) (coorX + 0.5 * length);
					pArray[3] = (int) (coorY - 0.886 * length);
					pArray[4] = (int ) (coorX + 1.5 * length);
					pArray[5] = (int) (coorY - 0.886 * length);
					pArray[6] = coorX + 2 * length;
					pArray[7] = coorY;
					pArray[8] = (int) (coorX + 1.5 * length);
					pArray[9] = (int) (coorY + 0.886 * length);
					pArray[10] = (int) (coorX +  0.5 * length);
					pArray[11] = (int) (coorY + 0.886 * length);
					gc.drawPolygon(pArray);
					gc.fillPolygon(pArray);
				}
			} 
			tmpStartX = tmpStartX + 30 + length * nomUnits.size();	
			if( tmpStartX >= 1200){
				tmpStartX = 20;
				tmpStartY = 100 + 90 * line;
				line++;
			}
		}
		gc.dispose();
	}
	
	private void showTarget(){
		int tmpStartX = 30;
		int tmpStartY = line1Y + 100 + 125 * line;
		
		GC gc = new GC(shell);
		gc.setLineWidth(2);
		gc.drawString(" Target Board :", tmpStartX- 10, tmpStartY - 40);
		gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		HashSet<Unit> nomboard = in.board.raw.nomUnits;
		Iterator<Unit> it = nomboard.iterator();
		while(it.hasNext()){
			Unit u = (Unit)it.next();
			int x = u.getX();
			int y = u.getY();
			//char ch = u.getCh();
			//String s = (new Character(ch)).toString();

			if(squareFlag){
				x = tmpStartX + x*length;
				y = tmpStartY + y*length;
				gc.fillRectangle(x, y, length, length);
				gc.drawRectangle(x, y, length, length);
				//gc.drawString(s, x + 4, y + 2);
			} else{
				double tmpX = 1.5*x*length;
				double tmpY1 = 1.732*y*length;
				double tmpY2 = 0.866*x*length;
				int coorX = tmpStartX + (int) tmpX;
				int coorY = tmpStartY + (int) tmpY1 + (int) tmpY2;
				int pArray[] = new int[12];
				pArray[0] = coorX;
				pArray[1] = coorY;
				pArray[2] = (int) (coorX + 0.5 * length);
				pArray[3] = (int) (coorY - 0.886 * length);
				pArray[4] = (int ) (coorX + 1.5 * length);
				pArray[5] = (int) (coorY - 0.886 * length);
				pArray[6] = coorX + 2 * length;
				pArray[7] = coorY;
				pArray[8] = (int) (coorX + 1.5 * length);
				pArray[9] = (int) (coorY + 0.886 * length);
				pArray[10] = (int) (coorX +  0.5 * length);
				pArray[11] = (int) (coorY + 0.886 * length);
				gc.drawPolygon(pArray);
				gc.fillPolygon(pArray);
			}
		}
		gc.dispose();
	}	
	
	private void Clear(){
		GC gc = new GC(shell);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(0, 10, 1200, 1000);
		gc.drawRectangle(0, 10, 1200, 1000);
		gc.dispose();

		
		startX = 20;
		startY = 80;
		in.process();
		soluNum = 0;
	}
	
	public void showSolution(int m){
		int solnumber = in.soluInterface.size()-1;	
		if(m > solnumber){
			JOptionPane.showMessageDialog(null,"There is no other solutions","ERROR",JOptionPane.WARNING_MESSAGE);
		}

		int tmpStartX = 550;
		int tmpStartY = line1Y + 100 + 125 * line;

		GC gc = new GC(shell);
		gc.setLineWidth(2);
		gc.drawString("There are " + m + "/"+ solnumber + " soultions.", tmpStartX, tmpStartY-30);
		
		HashMap<Integer, ArrayList<Unit>> solu = in.soluInterface.get(m);
		for (int n = 0; n < solu.size(); n++) {
			Color c = col.get(n);
			ArrayList<Unit> tile = solu.get(n);

			for (int k = 0; k < tile.size(); k++) {
				Unit u = tile.get(k);
				int x = u.getX();
				int y = u.getY();
				gc.setBackground(c);

				if (squareFlag) {
					x = tmpStartX + x * length;
					y = tmpStartY + y * length;
					gc.fillRectangle(x, y, length, length);
					gc.drawRectangle(x, y, length, length);

				} else {
					double tmpX = 1.5 * x * length;
					double tmpY1 = 1.732 * y * length;
					double tmpY2 = 0.866 * x * length;
					int coorX = tmpStartX + (int) tmpX;
					int coorY = tmpStartY + (int) tmpY1 + (int) tmpY2;
					int pArray[] = new int[12];
					pArray[0] = coorX;
					pArray[1] = coorY;
					pArray[2] = (int) (coorX + 0.5 * length);
					pArray[3] = (int) (coorY - 0.886 * length);
					pArray[4] = (int) (coorX + 1.5 * length);
					pArray[5] = (int) (coorY - 0.886 * length);
					pArray[6] = coorX + 2 * length;
					pArray[7] = coorY;
					pArray[8] = (int) (coorX + 1.5 * length);
					pArray[9] = (int) (coorY + 0.886 * length);
					pArray[10] = (int) (coorX + 0.5 * length);
					pArray[11] = (int) (coorY + 0.886 * length);
					gc.drawPolygon(pArray);
					gc.fillPolygon(pArray);
				}
			}
		}
		gc.dispose();
	}
		
	/***************Main function.****************/
	public static void main(String[] agrs){
		try{
			MainDialog window = new MainDialog();
			window.open();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
