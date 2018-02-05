import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.management.relation.InvalidRelationServiceException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MineSweeperGame 
{
	
	static int size,cBomb;
	static JFrame frame=new JFrame();
	static JPanel gui=new JPanel(new BorderLayout());
	static int bombCount=0;
	static JPanel p1;
	static JPanel start = new JPanel();
	static JButton[][] bt=new JButton[8][8];
	static JLabel ms = new JLabel("Select Your level to Play MineSweeper");
	static JLabel ms1 = new JLabel("  :-                                 ");
	static JLabel tLab = new JLabel();
	static ImageIcon img1 = new ImageIcon("mine1.jpg");
	static ImageIcon img2 = new ImageIcon("mine2.jpg");
	static ImageIcon bmb = new ImageIcon("mine1.png");
	static ImageIcon mark = new ImageIcon("mark.png");
	
	public static void main(String[] args) 
	{
		JButton beg= new JButton("Beginner");
		JButton itm= new JButton("Intermediate");
		JButton adv= new JButton("Advance");
		beg.setBackground(Color.YELLOW);
		itm.setBackground(Color.YELLOW);
		adv.setBackground(Color.YELLOW);
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(ms);
        tools.add(ms1);
        tools.add(beg);
        tools.add(itm); 
        tools.add(adv);
        
		frame.setSize(1000,1000);
		
		JButton star=new JButton();
		ImageIcon logo = new ImageIcon("min.jpg");
	    star.setIcon(logo);
	    start.add(star);
	    gui.add(start);
	    frame.add(gui);
	    frame.setVisible(true);
	    beg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				 size=8;
				cBomb=10;
				mineBoard(size,cBomb);
				new GameTimer(60, tLab);
			}
		});
		itm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 size=14;
				cBomb=36;
				mineBoard(size,cBomb);
				new GameTimer(180, tLab);
			}
		});
		adv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 size=24;
				cBomb=91;
				mineBoard(size,cBomb);
				new GameTimer(600, tLab);
			}
		});

	}
	static void mineBoard(int size,int cBomb)
	{
		p1=new JPanel(new GridLayout(size,size));
		bt=new JButton[size][size];
        
		gui.removeAll();
		timeSetter();
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				JButton b=new JButton();
				
				bt[i][j]=b;
				bt[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
				bt[i][j].setBackground(Color.BLACK);
				bt[i][j].setForeground(Color.black);
				p1.add(bt[i][j]);
			}
		}
		gui.add(p1);
		bombSet(size,cBomb);
		int fsize=size*62;
		frame.setSize(fsize, fsize);
		fillBombCount(size);
		frame.add(gui);
		frame.setVisible(true);
	}
	private static void timeSetter() {
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);
		tools.add(tLab);
	}
	static void fillBombCount(int size) 
	{
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				if(bt[i][j].getIcon() != null)
				{
					
				}
				else
				{
					int count = countNighbours(i,j-1,size)+countNighbours(i+1,j-1,size)
								+countNighbours(i+1,j,size)+countNighbours(i+1,j+1,size)
								+countNighbours(i,j+1,size)+countNighbours(i-1,j+1,size)
								+countNighbours(i-1,j,size)+countNighbours(i-1,j-1,size);
					bt[i][j].setText(Integer.toString(count));
				}
				
			}
		}
		for(int x=0;x<size;x++)
		{
			final int i=x;
			for(int y=0;y<size;y++)
			{
				
				final int j=y;
				bt[i][j].addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
					
						
						
						if(bt[i][j].getIcon()!=null)
						{
							if(bt[i][j].getIcon().equals(img2) || bt[i][j].getText().isEmpty())
							{
								
								bt[i][j].setIcon(bmb);
								explode();
							}
								
						}
						if(!(bt[i][j].getText().isEmpty()))
						{
							bt[i][j].setIcon(null);
						}
						String val=bt[i][j].getText();
					 	if(val.equals("0") && bt[i][j].getBackground().equals(Color.WHITE))
							callEmptyNigh(i,j);
						bt[i][j].setBackground(Color.WHITE);
					 	winmsg();
								
					}

					private void winmsg() {
						// TODO Auto-generated method stub
						int whit=0;
						for(int l=0;l<size;l++)
						{
							for(int m=0;m<size;m++)
							{
								if(bt[l][m].getBackground().equals(Color.WHITE))
								{
									whit++;
								}
							}
						}
						if(whit==(size*size)-cBomb)
						{
							JOptionPane.showMessageDialog(null, "  Congratulations You Won The Game");
							gui.removeAll();
							System.exit(0);
						}
					}

					private void callEmptyNigh(int i,int j) {
						// TODO Auto-generated method stub
						
						int c=openEmptyCell(i,j-1)+openEmptyCell(i+1,j-1)+openEmptyCell(i+1,j)+
								openEmptyCell(i+1,j+1)+openEmptyCell(i,j+1)+openEmptyCell(i-1,j+1)+
								openEmptyCell(i-1,j)+openEmptyCell(i-1,j-1);
						
					}

					private int openEmptyCell(int i, int j) {
						// TODO Auto-generated method stub
						if(i<0) return 0;
						else if(j<0) return 0;
						else if(j>size) return 0;
						else if(i>size) return 0;
						else
						{
							try 
							{
								String val=bt[i][j].getText();
								if(val.equals("0" ) && (bt[i][j].getBackground().equals(Color.BLACK)))
								{
									
									bt[i][j].setBackground(Color.WHITE);
									bt[i][j].setIcon(null);
									callEmptyNigh(i,j);
								}
							}
							catch(Exception e)
							{
								return 0;
							}
						}
						return 0;
					}

				});
				
				bt[i][j].addMouseListener(new MouseAdapter() {
					
					public void mouseClicked(MouseEvent e)
					{
						
						if (e.getButton() == 3) 
						{
							
							if(bt[i][j].getIcon() != null)
							{
								if(bt[i][j].getIcon().equals(mark) && !(bt[i][j].getText().isEmpty()))
								{
									bt[i][j].setIcon(null);
								}
								else if(bt[i][j].getIcon().equals(img2) || !(bt[i][j].getText().isEmpty()))
								{
									bt[i][j].setIcon(mark);
								}
								else
								{
									bt[i][j].setIcon(img2);
								}
							}
							else
							{
								if(!(bt[i][j].getBackground().equals(Color.WHITE)))
								bt[i][j].setIcon(mark);
							}
						    
						   
						}
					}
				});
			}
		}
		
	}
	static int countNighbours(int i, int j,int size) 
	{
		if(i<0) return 0;
		else if(j<0) return 0;
		else if(j>size) return 0;
		else if(i>size) return 0;
		else
		{
			try 
			{
				if(bt[i][j].getIcon() != null)
				{
					return 1;
				}
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		return 0;
	}
	public static void explode() {
		
		for(int x=0;x<size;x++)
		{
			for(int y=0;y<size;y++)
			{
				if(bt[x][y].getIcon() != null)
				{
					if(bt[x][y].getIcon().equals(mark) && !(bt[x][y].getText().isEmpty()))
					{
						bt[x][y].setIcon(null);
						bt[x][y].setBackground(Color.WHITE);
						bt[x][y].setForeground(Color.RED);
						bt[x][y].setText("X");
					}
					else if(bt[x][y].getIcon().equals(img2) )
					{
					    bt[x][y].setIcon(img1);
					    bt[x][y].setBackground(Color.WHITE);
					}
					
					
				}
			}
		}
		playSound();
		JOptionPane.showMessageDialog(null, "  GameOver\n Better Luck Next Time");
		gui.removeAll();
		System.exit(0);
		 
	}
	public static void playSound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sn.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {}
	}
	// Static method which set the Bombs in the Board
	static void bombSet(int size,int cBomb)
	{	
		Random rm	= new Random();
		
		while(bombCount<cBomb)
		{
			int iloc	= rm.nextInt(size);
			int jloc	= rm.nextInt(size);
			
			JButton bz=new JButton();
			bz=bt[iloc][jloc];
			if(bt[iloc][jloc].getIcon() != null)
			{
				continue;
			}
			else
			{
				
			    bt[iloc][jloc].setIcon(img2);
				bombCount++;
				
			}
		}
	}

}
//The Class which runs the Timer for the Game
class GameTimer implements ActionListener {
	int seconds = 60;
	JLabel label = null;
    
	
	public GameTimer(int seconds, JLabel label) {
		this.seconds = seconds;
		this.label = label;
		label.setText("" + seconds);
		Timer timer = new Timer(1000, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		
		seconds--;
		label.setText("" + seconds);
		if (seconds == 0) 
		{
			
			JOptionPane.showMessageDialog(null, "Time Limit Over ");
			MineSweeperGame.explode();
			System.exit(0);

		}
	}
}