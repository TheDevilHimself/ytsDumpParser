import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.*;

/**
 * Created by the Devil himself on 26/10/2015.
 */
public class GUI extends JFrame{

    private JPanel jPanel;
    private GridBagConstraints lim;

    private JList<Film> list;
    private DefaultListModel<Film> listModel;
    private JScrollPane jScrollPane;

    private TitledBorder searchBorder;
    private JTextField searchTextField;

    private JButton load;

    private JTextArea jtaFilmInfo;
    private JButton openTorrentButton;

    private ArrayList<Film> filmList;


    public static final String version="0.0.1";

    public GUI()
    {
        super();
        //title
        setTitle("ytsParser"+version);
        //close windows with x
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window location
        setLocation(300,100);
        //initialize main panel
        jPanel=new JPanel(new GridBagLayout());
        //set as main panel
        setContentPane(jPanel);
        //need this to set correctly the UI components
        lim = new GridBagConstraints();


        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        jScrollPane= new JScrollPane(list);

        searchBorder=new TitledBorder("Search");
        searchTextField=new JTextField(15);
        searchTextField.setEditable(true);
        searchTextField.setBorder(searchBorder);
        searchTextField.getDocument().addDocumentListener(new Search(searchTextField,list,listModel));
        searchTextField.addKeyListener(new Enter(searchTextField,list,listModel));

        load=new JButton("Load Film List");
        if(filmList==null)
            filmList=new ArrayList<>();
        load.addActionListener(new LoadFilmListener(filmList,listModel));

        jtaFilmInfo=new JTextArea();
        jtaFilmInfo.setEditable(true);

        openTorrentButton=new JButton("Open Torrent");
        openTorrentButton.addActionListener(new OpenTorrentListener(jtaFilmInfo));

        list.addMouseListener(new ListClick(list,jtaFilmInfo));

        limIns(5, 5, 2, 5);
        lim.weightx=1;
        lim.weighty=1;

        limIns(1,5,1,5);
        addC(0,0,1,1,HORIZONTAL,WEST);
        jPanel.add(searchTextField,lim);

        addC(0,1,1,1,HORIZONTAL,WEST);
        jPanel.add(load,lim);

        lim.weightx=2;
        lim.weighty=2;
        addC(1,0,1,2,BOTH,SOUTH);
        jPanel.add(jScrollPane,lim);

        lim.weightx=1;
        lim.weighty=1;
        addC(0,2,1,1,HORIZONTAL,WEST);
        jPanel.add(openTorrentButton,lim);

        lim.weightx=2;
        lim.weighty=2;
        limIns(5,5,5,5);
        addC(1,2,1,2,BOTH,CENTER);
        jPanel.add(jtaFilmInfo,lim);

        pack();
        list.setVisibleRowCount(10);
        setSize(700,400);
        setVisible(true);

        if(!Files.exists(Paths.get("yts_movie.json")));
        {
            String error = "Put this file in the same folder as yts_movie.json and press load film list, then double click your film";
            JOptionPane.showMessageDialog(null, error, "ytsDumpParser", JOptionPane.WARNING_MESSAGE);
        }
    }

    //layout helper method: set borders
    private void limIns(int top,int right,int bottom,int left)
    {
        lim.insets.top = top;
        lim.insets.bottom = bottom;
        lim.insets.left = left;
        lim.insets.right = right;
    }
    //layout helper method: set position in layout: row,column, height,width,how to fill and where to anchor
    private void addC(int y,int x,int gh,int gw,int fill,int anchor)
    {
        lim.gridy=y;
        lim.gridx=x;
        lim.gridheight=gh;
        lim.gridwidth=gw;
        lim.fill = fill;
        lim.anchor = anchor;
    }

    public static void main(String[] args)
    {
        GUI g=new GUI();
    }
}

/**
 * Listener for the load button
 */
class LoadFilmListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e) {
        Thread s=new Thread(new LoadThread(myAlf,myDlm));
        s.start();
    }

    private ArrayList<Film> myAlf;
    private DefaultListModel<Film> myDlm;

    public LoadFilmListener(ArrayList<Film> alf,DefaultListModel<Film> dlm)
    {
        myAlf=alf;
        myDlm=dlm;
    }
}

class OpenTorrentListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] lines=jta.getText().split("\n");
        for(String s:lines)
        {
            if(s.contains("Torrent : "))
            {
                String fileName=s.substring(10);
                try {
                File torrentFile=new File("./torrents/"+fileName);
                if(torrentFile.exists()&&torrentFile.length()>1)
                    Desktop.getDesktop().open(torrentFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            }
        }
    }

    private JTextArea jta;

    public OpenTorrentListener(JTextArea jta)
    {
        this.jta=jta;
    }
}

/**
 * Listenere for the double click on a film
 */
class ListClick extends MouseAdapter {


    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {

            int index = list.locationToIndex(e.getPoint());
            if(index>=0)
            {
                ListModel dlm = list.getModel();
                if(dlm.getElementAt(index)!=null)
                {
                    Film f =(Film)dlm.getElementAt(index);
                    list.ensureIndexIsVisible(index);

                    SwingUtilities.invokeLater(
                            new Runnable() {
                                public void run() {
                                    jta.setCaretPosition(0);
                                    jta.setText(f.getFormattedInformations());

                                }
                            });
                }
            }
        }
    }


    protected JList list;
    protected JTextArea jta;

    public ListClick(JList l,JTextArea jta)
    {
        list = l;
        this.jta=jta;
    }
}


/**
 * inner working of the search bar
 */
class Search implements DocumentListener
{


    public void changedUpdate(DocumentEvent documentEvent)
    {
        int index=0;
        int max=lm.size();
        boolean found=false;

        if(max>0)
            while(!found&index<max)
            {
                String temp=lm.elementAt(index).toString().toLowerCase();
                if(temp.contains(jtf.getText().toLowerCase()))
                {
                    found=true;
                    l.setSelectedIndex(index);
                    l.ensureIndexIsVisible(index);
                }
                index++;
            }

    }
    public void insertUpdate(DocumentEvent documentEvent)
    {
        int index=0;
        int max=lm.size();
        boolean found=false;

        if(max>0)
            while(!found&index<max)
            {
                String temp=lm.elementAt(index).toString().toLowerCase();
                if(temp.contains(jtf.getText().toLowerCase()))
                {
                    found=true;
                    l.setSelectedIndex(index);
                    l.ensureIndexIsVisible(index);
                }
                index++;
            }

    }
    public void removeUpdate(DocumentEvent documentEvent)
    {
        int index=0;
        int max=lm.size();
        boolean found=false;
        if(max>0)
            while(!found&index<max)
            {
                String temp=lm.elementAt(index).toString().toLowerCase();
                if(temp.contains(jtf.getText().toLowerCase()))
                {
                    found=true;
                    l.setSelectedIndex(index);
                    l.ensureIndexIsVisible(index);
                }
                index++;
            }
    }

    private JTextField jtf;
    private JList<Film> l;
    private DefaultListModel<Film> lm;


    public Search(JTextField j,JList<Film> list,DefaultListModel<Film> listmodel)
    {
        jtf=j;
        l=list;
        lm=listmodel;
    }
}

/**
 * pressing enter goes to the next hit when ou use the search bar
 */
class Enter extends KeyAdapter
{

    public void keyPressed(KeyEvent ke)
    {
        int key = ke.getKeyCode();
        String s=jtf.getText();
        int oldIndex=-1;

        if(s.length()>0)
            if (key == KeyEvent.VK_ENTER)
            {
                int index=l.getSelectedIndex()+1;
                int max=lm.size();
                boolean found=false;
                if(index<0|index>=max)
                    index=0;
                if(index>0)
                    oldIndex=index;

                if(max>0)
                    while(!found&index<max)
                    {
                        String temp=lm.elementAt(index).toString().toLowerCase();
                        if(temp.contains(jtf.getText().toLowerCase()))
                        {
                            l.setSelectedIndex(index);
                            l.ensureIndexIsVisible(index);
                            found=true;
                        }
                        index++;
                    }

                if(!found&oldIndex>0)
                {
                    index=0;
                    while(!found&index<oldIndex)
                    {
                        String temp=lm.elementAt(index).toString().toLowerCase();
                        if(temp.contains(jtf.getText().toLowerCase()))
                        {
                            l.setSelectedIndex(index);
                            l.ensureIndexIsVisible(index);
                            found=true;
                        }
                        index++;
                    }
                }

            }
    }


    private JTextField jtf;
    private JList<Film> l;
    private DefaultListModel<Film> lm;

    public Enter(JTextField j,JList<Film> list,DefaultListModel<Film> listmodel)
    {
        jtf=j;
        l=list;
        lm=listmodel;
    }
}