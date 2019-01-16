package SmartPad_Editor;


import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.Vector;

public class predict extends JFrame{
    JTextField city = new JTextField(10);
    String enteredName = null;
    
    String[] cities = {"abstract","continue","for","new","switch","assert","default","goto","package","synchronized","boolean","do","if","private","this","break","double","implements","protected","throw","byte","else","import","public","throws","case","enum","instanceof","return","transient","catch","extends","int","short","try","char","final","interface","static","void","class","finally","long","strictfp","volatile","const","float","native","super","while"};
    JList list = new JList();
    JScrollPane pane = new JScrollPane();
    ResultWindow r = new ResultWindow();
//------------------------------------------------------------------------------
    public static void main(String[] args) {
        new predict();
    }
//------------------------------------------------------------------------------
    public String getWord(JTextField jf)
    {
    	String word="";
    	 int i=0;
    	while(jf.getText().charAt(i)!=' ')
    	{
    		word=word+jf.getText().charAt(i);
    	}
    	return word;
    }
    public predict(){
        setLayout(new java.awt.FlowLayout());
        setVisible(true);
        add(city);
//      add(pane);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        city.addKeyListener(new TextHandler());
    }
//------------------------------------------------------------------------------
    public void initiateSearch(String lookFor){
        Vector<String> matches = new Vector<>();
        lookFor = lookFor.toLowerCase();
        for(String each : cities){
            if(each.contains(lookFor)){
                matches.add(each);
                System.out.println("Match: " + each);
            }
        }
        this.repaint();

        if(matches.size()!=0){
            list.setListData(matches);
            r.searchResult = list;
            r.pane = pane;
            r.initiateDisplay();
        }else{
            matches.add("No Match Found");
            list.setListData(matches);
            r.searchResult = list;
            r.pane = pane;
            r.initiateDisplay();
        }

    }
//------------------------------------------------------------------------------
    public class ResultWindow extends JWindow{
        public JScrollPane pane;
        public JList searchResult;
//------------------------------------------------------------------------------
        public ResultWindow(){

        }
//------------------------------------------------------------------------------
        public void initiateDisplay(){
            pane.setViewportView(searchResult);
            add(pane);
            pack();
            this.setLocation(predict.this.getX() + 2, 
                    predict.this.getY()+
                    predict.this.getHeight());

//          this.setPreferredSize(city.getPreferredSize());
            this.setVisible(true);
        }
    }
//------------------------------------------------------------------------------

    class TextHandler implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e){

        }

        @Override
        public void keyPressed(KeyEvent e){
            if(r.isVisible()){
                r.setVisible(false);
            }
            
                initiateSearch(city.getText());
                if(e.getKeyChar() == ' '){
                initiateSearch(city.getText());
            }
        }

        @Override
        public void keyReleased(KeyEvent e){

        }
    }
//------------------------------------------------------------------------------
}
