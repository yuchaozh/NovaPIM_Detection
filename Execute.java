import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * The execution of the file system detecting function.
 * 没有完成循环功能，停止检测的时候有问题=退出程序，如何再次监视？
 * @version  2013/3/20
 * @author  Yuchao Zhou
 */
public class Execute 
{
	private static String xmlfile = "PIMTree.xml";
	
	public static void main(String args[]) throws IOException, InterruptedException, ParserConfigurationException, SAXException
	{
		Timer timer = new Timer(true);
		Execute execute = new Execute();
		timer.schedule(new task(), 60*1000);
		//timer.schedule(finish(), 60*1000);
		execute.traverse();
		execute.readxml();
		execute.readhtm();
		execute.watch();
	}
	
	/**
	 * traverse: traverse the whole file system and find out all folders in it
	 * @param 
	 */
	public void traverse() throws IOException
	{
		Path startPath = Paths.get("c:/PIM"); 
		//Path startPath = Paths.get("c:/test_file2");
		TraverseDir traversedir = new TraverseDir();
		try 
		{  
	        Files.walkFileTree(startPath, traversedir);  
	    } 
		catch (IOException e) 
	    {  
	        System.err.println(e);  
	    }
		//System.out.println("dircount: "+TraverseDir.dircount);
		//System.out.println("~~~~~~~~~~~~~~~~~ ");
		//System.out.println("DirPath Count: "+TraverseDir.dirpath.size());
		//创建txt文档存文件夹的路径
		FileWriter fw = new FileWriter("C:/DetectedDir.txt");
		for(int i = 0; i < TraverseDir.dirpath.size(); i++)
		{
			fw.write(TraverseDir.dirpath.get(i)+ "\r\n");
		}
		//关闭txt
		fw.close();
	}
	
	/**
	 * watch: watching all modifications occurred in the file system.
	 * @param 
	 */
	public void watch() throws IOException, InterruptedException
	{
		Watch watch = new Watch();
		//System.out.println("监视的文件夹： ");
		for(int i = 0; i < TraverseDir.dirpath.size(); i++)
		{
			Object url = "";
			//System.out.println(TraverseDir.dirpath.size());
			 url = TraverseDir.dirpath.get(i);
			 //System.out.println(url.toString());
			 watch.path.add(Paths.get(url.toString()));
		}
		
/*		for (int i =0; i < watch.path.size(); i++)
		{
			System.out.print(watch.path.get(i) + "; ");
		}*/
		System.out.println();
		watch.SetPath();
	}
	
	/**
	 * watch: read referenced files in the concept tree
	 * @param 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void readxml() throws ParserConfigurationException, SAXException, IOException
	{
		ReadPIMTree readxml = new ReadPIMTree();
		readxml.startRead();
	}
	
	/**
	 * readhtm: read all htm files and find the referenced file in them
	 * @param 
	 */
	public void readhtm() throws IOException
	{
		ReadHTM readhtm = new ReadHTM();
		readhtm.startread();
	}
}

/****************退出有点问题*******************************/
class task extends TimerTask
{
	public void run()
	{
		Watch.quite = -1;
		//Watch.key.cancel();
		Watch.valid = false;
		//System.out.println("quite is setted! "+ Watch.quite);
		System.out.println("Detection END!");
		try {
			Watch.fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			framebuild();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.exit(-1);
	}
	
	public void framebuild() throws IOException
	{
		final JFrame frame = new JFrame();
		GridBagLayout gr = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		frame.setLayout(gr);
		frame.setBounds(300,300,550,300);
		
		JLabel label1 = new JLabel("对文件的操作： ");
		JTextArea text1 = new JTextArea();
		text1.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(text1);
		JButton button = new JButton("OK");
		
		gc.fill = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 10;
		gc.weighty = 5;
		gr.setConstraints(label1, gc);
		gc.fill = GridBagConstraints.BOTH;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 10;
		gc.weighty = 50;
		gr.setConstraints(scrollPane,gc);
		gc.fill = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 2;
		gc.weightx = 10;
		gc.weighty = 5;
		gr.setConstraints(button,gc);
		
		frame.add(scrollPane);
		frame.add(label1);
		frame.add(button);
		//frame.add(text1);
		frame.setVisible(true);
		//DISPOSE_ON_CLOSE隐藏并释放窗体
		//EXIT_ON_CLOSE使用system exit方法退出应用程序
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
			}
		});
		
		BufferedReader br=new BufferedReader(new FileReader("c:/log.txt"));
		String oneline = br.readLine();
		while (oneline != null)
		{
			text1.append(oneline + "\n");
			oneline = br.readLine();
		}
	}
}
