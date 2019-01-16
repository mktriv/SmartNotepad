package SmartPad_Editor;



import java.io.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;

import com.inet.jortho.SpellChecker;
import com.inet.jorthodictionaries.*;

public class Smartpad  implements ActionListener, MenuConstants
{

JFrame f;
static JTextArea ta;
JLabel statusBar;
boolean ext=false;
String extens;
static List<String> treffer;


private String fileName="Untitled";
private boolean saved=true;
String applicationName="Smartpad";

String searchString, replaceString;
int lastSearchIndex;

FileOperation fileHandler;
FontChooser fontDialog=null;
FindDialog findReplaceDialog=null; 
JColorChooser bcolorChooser=null;
JColorChooser fcolorChooser=null;
JDialog backgroundDialog=null;
JDialog foregroundDialog=null;
JMenuItem cutItem,copyItem, deleteItem, findItem, findNextItem, replaceItem, gotoItem, selectAllItem;
/****************************/
Smartpad()
{
f=new JFrame(fileName+" - "+applicationName);
ta=new JTextArea(30,60);
statusBar=new JLabel("||       Ln 1, Col 1  ",JLabel.RIGHT);
f.add(new JScrollPane(ta),BorderLayout.CENTER);
f.add(statusBar,BorderLayout.SOUTH);
f.add(new JLabel("  "),BorderLayout.EAST);
f.add(new JLabel("  "),BorderLayout.WEST);
createMenuBar(f);
//f.setSize(350,350);
f.pack();
f.setLocation(100,50);
f.setVisible(true);
f.setLocation(150,50);

f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//cross platform
fileHandler=new FileOperation(this);  


	//System.out.println(fileHandler.getExtension());

//System.out.println("hello");
 


SpellChecker.registerDictionaries( getCodeBase(), "en" );
SpellChecker.registerDictionaries( getCodeBase(), "de" );
SpellChecker.registerDictionaries( getCodeBase(), "it" );
SpellChecker.registerDictionaries( getCodeBase(), "fr" );
SpellChecker.registerDictionaries( getCodeBase(), "ru" );
SpellChecker.registerDictionaries( getCodeBase(), "es" );

SpellChecker.register( ta );






fileHandler=new FileOperation(this);

/////////////////////

ta.addCaretListener(
new CaretListener()
{
public void caretUpdate(CaretEvent e)
{
int lineNumber=0, column=0, pos=0;

try
{
pos=ta.getCaretPosition();
lineNumber=ta.getLineOfOffset(pos);
column=pos-ta.getLineStartOffset(lineNumber);
}catch(Exception excp){}
if(ta.getText().length()==0){lineNumber=0; column=0;}
statusBar.setText("||       Ln "+(lineNumber+1)+", Col "+(column+1));
}
});
//////////////////
DocumentListener myListener = new DocumentListener()
{
public void changedUpdate(DocumentEvent e){fileHandler.saved=false;}
public void removeUpdate(DocumentEvent e){fileHandler.saved=false;}
public void insertUpdate(DocumentEvent e){fileHandler.saved=false;}
};
ta.getDocument().addDocumentListener(myListener);
/////////
WindowListener frameClose=new WindowAdapter()
{
public void windowClosing(WindowEvent we)
{
if(fileHandler.confirmSave())System.exit(0);
}
};
f.addWindowListener(frameClose);

}
private URL getCodeBase() {
	// TODO Auto-generated method stub
	return null;
}
////////////////////////////////////
void goTo()
{
int lineNumber=0;
try
{
lineNumber=ta.getLineOfOffset(ta.getCaretPosition())+1;
String tempStr=JOptionPane.showInputDialog(f,"Enter Line Number:",""+lineNumber);
if(tempStr==null)
	{return;}
lineNumber=Integer.parseInt(tempStr);
ta.setCaretPosition(ta.getLineStartOffset(lineNumber-1));
}catch(Exception e){}
}
///////////////////////////////////
 
public void actionPerformed(ActionEvent ev)
{
String cmdText=ev.getActionCommand();
////////////////////////////////////
if(cmdText.equals(fileNew))
	fileHandler.newFile();
else if(cmdText.equals(fileOpen))
	fileHandler.openFile();
////////////////////////////////////
else if(cmdText.equals(fileSave))
	fileHandler.saveThisFile();
////////////////////////////////////
else if(cmdText.equals(fileSaveAs))
{
	fileHandler.saveAsFile();
	ext=true;
	if(fileHandler.getExtension().equals("java"))
	{
	  treffer = new ArrayList<String>(Arrays.asList("abstract","continue","for","new","switch","assert","default","goto","package","synchronized","boolean","do","if","private","this","break","double","implements","protected","throw","byte","else","import","public","publis","throws","case","enum","instanceof","return","transient","catch","extends","int","short","try","char","final","interface","static","void","class","finally","long","strictfp","volatile","const","float","native","super","while"));
	}if(fileHandler.getExtension().equals("py")){
      treffer=new ArrayList<String>(Arrays.asList("KEYWORD","FALSE","class","finally","is","return","none","continue","for","lambda","try","TRUE","def","from","nonlocal","while","and","del","global","not","with","as","elif","if","or","yield","assert","else","import","pass","break","except","in","raise" )); 
	}if(fileHandler.getExtension().equals("c")){
      treffer=new ArrayList<String>(Arrays.asList("auto","double","int","struct","break","else","long","switch","case","enum","register","typedef","char","extern","return","union","const","float","short","unsigned","continue","for","signed","void","default","goto","sizeof","volatile","do","if","static","while" ));
	}if(fileHandler.getExtension().equals("cpp")){
     treffer =new ArrayList<String>(Arrays.asList("asm","auto","bool","break","case","catch","char","class","const_cast","continue","default","delete","do","double","else","enum","dynamic_cast","extern","false","float","for","union","unsigned","using","friend","goto","if","inline","int","long","mutable","virtual","namespace","new","operator","private","protected","public","register","void","reinterpret_cast","return","short","signed","sizeof","static","static_cast","volatile","struct","switch","template","this","throw","true","try","typedef","typeid","unsigned","wchar_t","while"
));}
Collections.sort(treffer);
autoComplete();
  
//System.out.println(fileHandler.getExtension());
}
////////////////////////////////////
else if(cmdText.equals(fileExit))
	{if(fileHandler.confirmSave())System.exit(0);}
////////////////////////////////////
else if(cmdText.equals(filePrint))
JOptionPane.showMessageDialog(
	Smartpad.this.f,
	"Get ur printer repaired first! It seems u dont have one!",
	"Bad Printer",
	JOptionPane.INFORMATION_MESSAGE
	);
////////////////////////////////////
else if(cmdText.equals(editCut))
	ta.cut();
////////////////////////////////////
else if(cmdText.equals(editCopy))
	ta.copy();
////////////////////////////////////
else if(cmdText.equals(editPaste))
	ta.paste();
////////////////////////////////////
else if(cmdText.equals(editDelete))
	ta.replaceSelection("");
////////////////////////////////////
else if(cmdText.equals(editFind))
{
if(Smartpad.this.ta.getText().length()==0)
	return;	// text box have no text
if(findReplaceDialog==null)
	findReplaceDialog=new FindDialog(Smartpad.this.ta);
findReplaceDialog.showDialog(Smartpad.this.f,true);//find
}
////////////////////////////////////
else if(cmdText.equals(editFindNext))
{
if(Smartpad.this.ta.getText().length()==0)
	return;	// text box have no text

if(findReplaceDialog==null)
	statusBar.setText("Nothing to search for, use Find option of Edit Menu first !!!!");
else
	findReplaceDialog.findNextWithSelection();
}
////////////////////////////////////
else if(cmdText.equals(editReplace))
{
if(Smartpad.this.ta.getText().length()==0)
	return;	// text box have no text

if(findReplaceDialog==null)
	findReplaceDialog=new FindDialog(Smartpad.this.ta);
findReplaceDialog.showDialog(Smartpad.this.f,false);//replace
}
////////////////////////////////////
else if(cmdText.equals(editGoTo))
{
if(Smartpad.this.ta.getText().length()==0)
	return;	// text box have no text
goTo();
}
////////////////////////////////////
else if(cmdText.equals(editSelectAll))
	ta.selectAll();
////////////////////////////////////
else if(cmdText.equals(editTimeDate))
	ta.insert(new Date().toString(),ta.getSelectionStart());
////////////////////////////////////
else if(cmdText.equals(formatWordWrap))
{
JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();
ta.setLineWrap(temp.isSelected());
}
////////////////////////////////////
else if(cmdText.equals(formatFont))
{
if(fontDialog==null)
	fontDialog=new FontChooser(ta.getFont());

if(fontDialog.showDialog(Smartpad.this.f,"Choose a font"))
	Smartpad.this.ta.setFont(fontDialog.createFont());
}
////////////////////////////////////
else if(cmdText.equals(formatForeground))
	showForegroundColorDialog();
////////////////////////////////////
else if(cmdText.equals(formatBackground))
	showBackgroundColorDialog();
////////////////////////////////////

else if(cmdText.equals(viewStatusBar))
{
JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();
statusBar.setVisible(temp.isSelected());
}
////////////////////////////////////
else if(cmdText.equals(helpAboutSmartpad))
{
JOptionPane.showMessageDialog(Smartpad.this.f,aboutText,"Dedicated 2 u!",JOptionPane.INFORMATION_MESSAGE);
}
else
	statusBar.setText("This "+cmdText+" command is yet to be implemented");
}//action Performed
////////////////////////////////////
void showBackgroundColorDialog()
{
if(bcolorChooser==null)
	bcolorChooser=new JColorChooser();
if(backgroundDialog==null)
	backgroundDialog=JColorChooser.createDialog
		(Smartpad.this.f,
		formatBackground,
		false,
		bcolorChooser,
		new ActionListener()
		{public void actionPerformed(ActionEvent evvv){
			Smartpad.this.ta.setBackground(bcolorChooser.getColor());}},
		null);		

backgroundDialog.setVisible(true);
}
////////////////////////////////////
private static void autoComplete() {
    // TODO Auto-generated method stub 
    ta.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void insertUpdate(DocumentEvent ev) {
            // TODO Auto-generated method stub
            String completion;
            int pos = ev.getOffset();
            String content = null;
            try {
                content = ta.getText(0, pos + 1);
            } catch (BadLocationException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

            int w;
            for (w = pos; w >= 0; w--) {
            }
            if (pos - w < 2) {
                return;
            }

            String prefix = content.substring(w + 1);
            int n = Collections.binarySearch(treffer, prefix);
            if (n < 0 && -n <= treffer.size()) {
                String match = treffer.get(-n - 1);
                if (match.startsWith(prefix)) {
                    completion = match.substring(pos - w);
                    SwingUtilities.invokeLater(new CompletionTask(
                            completion, pos + 1));
                }
            }

        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
            // TODO Auto-generated method stub

        }

    });

}
void showForegroundColorDialog()
{
if(fcolorChooser==null)
	fcolorChooser=new JColorChooser();
if(foregroundDialog==null)
	foregroundDialog=JColorChooser.createDialog
		(Smartpad.this.f,
		formatForeground,
		false,
		fcolorChooser,
		new ActionListener()
		{public void actionPerformed(ActionEvent evvv){
			Smartpad.this.ta.setForeground(fcolorChooser.getColor());}},
		null);		

foregroundDialog.setVisible(true);
}

///////////////////////////////////
JMenuItem createMenuItem(String s, int key,JMenu toMenu,ActionListener al)
{
JMenuItem temp=new JMenuItem(s,key);
temp.addActionListener(al);
toMenu.add(temp);

return temp;
}
////////////////////////////////////
JMenuItem createMenuItem(String s, int key,JMenu toMenu,int aclKey,ActionListener al)
{
JMenuItem temp=new JMenuItem(s,key);
temp.addActionListener(al);
temp.setAccelerator(KeyStroke.getKeyStroke(aclKey,ActionEvent.CTRL_MASK));
toMenu.add(temp);

return temp;
}
////////////////////////////////////
JCheckBoxMenuItem createCheckBoxMenuItem(String s, int key,JMenu toMenu,ActionListener al)
{
JCheckBoxMenuItem temp=new JCheckBoxMenuItem(s);
temp.setMnemonic(key);
temp.addActionListener(al);
temp.setSelected(false);
toMenu.add(temp);

return temp;
}
////////////////////////////////////
JMenu createMenu(String s,int key,JMenuBar toMenuBar)
{
JMenu temp=new JMenu(s);
temp.setMnemonic(key);
toMenuBar.add(temp);
return temp;
}
/*********************************/
void createMenuBar(JFrame f)
{
JMenuBar mb=new JMenuBar();
JMenuItem temp;

JMenu fileMenu=createMenu(fileText,KeyEvent.VK_F,mb);
JMenu editMenu=createMenu(editText,KeyEvent.VK_E,mb);
JMenu formatMenu=createMenu(formatText,KeyEvent.VK_O,mb);
JMenu viewMenu=createMenu(viewText,KeyEvent.VK_V,mb);
//share
JMenu shareMenu=createMenu(share,KeyEvent.VK_S,mb);
//end
JMenu helpMenu=createMenu(helpText,KeyEvent.VK_H,mb);

createMenuItem(fileNew,KeyEvent.VK_N,fileMenu,KeyEvent.VK_N,this);
createMenuItem(fileOpen,KeyEvent.VK_O,fileMenu,KeyEvent.VK_O,this);
createMenuItem(fileSave,KeyEvent.VK_S,fileMenu,KeyEvent.VK_S,this);
createMenuItem(fileSaveAs,KeyEvent.VK_A,fileMenu,this);
fileMenu.addSeparator();
temp=createMenuItem(filePageSetup,KeyEvent.VK_U,fileMenu,this);
temp.setEnabled(false);
createMenuItem(filePrint,KeyEvent.VK_P,fileMenu,KeyEvent.VK_P,this);
fileMenu.addSeparator();
createMenuItem(fileExit,KeyEvent.VK_X,fileMenu,this);

temp=createMenuItem(editUndo,KeyEvent.VK_U,editMenu,KeyEvent.VK_Z,this);
temp.setEnabled(false);
editMenu.addSeparator();
cutItem=createMenuItem(editCut,KeyEvent.VK_T,editMenu,KeyEvent.VK_X,this);
copyItem=createMenuItem(editCopy,KeyEvent.VK_C,editMenu,KeyEvent.VK_C,this);
createMenuItem(editPaste,KeyEvent.VK_P,editMenu,KeyEvent.VK_V,this);
deleteItem=createMenuItem(editDelete,KeyEvent.VK_L,editMenu,this);
deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
editMenu.addSeparator();
findItem=createMenuItem(editFind,KeyEvent.VK_F,editMenu,KeyEvent.VK_F,this);
findNextItem=createMenuItem(editFindNext,KeyEvent.VK_N,editMenu,this);
findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
replaceItem=createMenuItem(editReplace,KeyEvent.VK_R,editMenu,KeyEvent.VK_H,this);
gotoItem=createMenuItem(editGoTo,KeyEvent.VK_G,editMenu,KeyEvent.VK_G,this);
editMenu.addSeparator();
selectAllItem=createMenuItem(editSelectAll,KeyEvent.VK_A,editMenu,KeyEvent.VK_A,this);
createMenuItem(editTimeDate,KeyEvent.VK_D,editMenu,this).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));

createCheckBoxMenuItem(formatWordWrap,KeyEvent.VK_W,formatMenu,this);

createMenuItem(formatFont,KeyEvent.VK_F,formatMenu,this);
formatMenu.addSeparator();
createMenuItem(formatForeground,KeyEvent.VK_T,formatMenu,this);
createMenuItem(formatBackground,KeyEvent.VK_P,formatMenu,this);

createCheckBoxMenuItem(viewStatusBar,KeyEvent.VK_S,viewMenu,this).setSelected(true);
/************For Look and Feel, May not work properly on different operating environment***/
LookAndFeelMenu.createLookAndFeelMenuItem(viewMenu,this.f);


temp=createMenuItem(helpHelpTopic,KeyEvent.VK_H,helpMenu,this);
temp.setEnabled(false);
helpMenu.addSeparator();
createMenuItem(helpAboutSmartpad,KeyEvent.VK_A,helpMenu,this);

MenuListener editMenuListener=new MenuListener()
{
 public void menuSelected(MenuEvent evvvv)
	{
	if(Smartpad.this.ta.getText().length()==0)
	{
	findItem.setEnabled(false);
	findNextItem.setEnabled(false);
	replaceItem.setEnabled(false);
	selectAllItem.setEnabled(false);
	gotoItem.setEnabled(false);
	}
	else
	{
	findItem.setEnabled(true);
	findNextItem.setEnabled(true);
	replaceItem.setEnabled(true);
	selectAllItem.setEnabled(true);
	gotoItem.setEnabled(true);
	}
	if(Smartpad.this.ta.getSelectionStart()==ta.getSelectionEnd())
	{
	cutItem.setEnabled(false);
	copyItem.setEnabled(false);
	deleteItem.setEnabled(false);
	}
	else
	{
	cutItem.setEnabled(true);
	copyItem.setEnabled(true);
	deleteItem.setEnabled(true);
	}
	}
 public void menuDeselected(MenuEvent evvvv){}
 public void menuCanceled(MenuEvent evvvv){}
};
editMenu.addMenuListener(editMenuListener);
f.setJMenuBar(mb);
}
/*************Constructor**************/
////////////////////////////////////
public static void main(String[] s)
{

	new Smartpad();
	//System.out.println("heello");
	
}
private static class CompletionTask implements Runnable {
	 
    String completion;

    int position;

    CompletionTask(String completion, int position) {
        this.completion = completion;
        this.position = position;
    }

    public void run() {
        ta.setText(ta.getText() + completion);
        ta.setCaretPosition(position + completion.length());
        ta.moveCaretPosition(position);
    }
}

}
/**************************************/
//public
interface MenuConstants
{
	//share
	final String share="Email";
	//end
final String fileText="File";
final String editText="Edit";
final String formatText="Format";
final String viewText="View";
final String helpText="Help";

final String fileNew="New";
final String fileOpen="Open...";
final String fileSave="Save";
final String fileSaveAs="Save As...";
final String filePageSetup="Page Setup...";
final String filePrint="Print";
final String fileExit="Exit";

final String editUndo="Undo";
final String editCut="Cut";
final String editCopy="Copy";
final String editPaste="Paste";
final String editDelete="Delete";
final String editFind="Find...";
final String editFindNext="Find Next";
final String editReplace="Replace";
final String editGoTo="Go To...";
final String editSelectAll="Select All";
final String editTimeDate="Time/Date";

final String formatWordWrap="Word Wrap";
final String formatFont="Font...";
final String formatForeground="Set Text color...";
final String formatBackground="Set Pad color...";

final String viewStatusBar="Status Bar";
//share

//end

final String helpHelpTopic="Help Topic";
final String helpAboutSmartpad="About Smartpad";

final String aboutText=
	"<html><big>Your Smartpad</big><hr><hr>"
	+"<p align=right>Prepared by a Ducatian!"
	+"<hr><p align=left>I Used jdk1.5 to compile the source code.<br><br>"
	+"<strong>Thanx 4 using Smartpad</strong><br>"
	+"Ur Comments as well as bug reports r very welcome at<p align=center>"
	+"<hr><em><big>radialgoal@gmail.com</big></em><hr><html>";



}