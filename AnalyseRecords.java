import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jdom.*;
import org.jdom.input.SAXBuilder; 

/**
 * Analyze the modification records in log.txt to out put actual operation.
 *
 * @version  2013/3/20
 * @author  Yuchao Zhou
 */
public class AnalyseRecords 
{
	private BufferedReader br;
	private static boolean end = false;
	private String oneline;  //read oneline in the log.txt
	private String oneAct;  //one line data in the arraylist
	private String content[];  //the content of oneline in log.txt
	private String[] contentInAL;
	private String day;  
	private String time;  //the time information of one line data in the arraylist
	private String preTime;  //the time of last data
	private String operation;
	private String file;
	private ArrayList <String> actions;  //store the data read from log.txt
	private int linenum = 0;
	private int prenum = 0;
	private int curnum = 0;
	static boolean noAction = false;  //if the content of log.txt is null then true
	private Synchronize syn;
	
	public AnalyseRecords() throws IOException
	{
		br = new BufferedReader(new FileReader("c:/log.txt"));
		actions = new ArrayList<String>();
		syn = new Synchronize();
		//oneline = br.readLine();
	}
	
	/**
	 * oneAction: analyses the data in the arraylist and sorts actions by time
	 * @param 
	 * @throws JDOMException 
	 */
	public void oneAction() throws IOException, JDOMException
	{
		int number = 0;
		prenum = number;
		curnum = number;
		preTime = extractTime(number);
		
		while (number <= actions.size()-2)
		{
			number++;
			if ((!preTime.equals(extractTime(number))) && (number >= actions.size()-1))
			{
				curnum = number-1;
				System.out.println("One Action: " + preTime + " from " + prenum + "to " + curnum);
				outPutResult(prenum, curnum);
				//System.out.println("last: "+number);
				prenum = number;
				curnum = prenum;
				preTime = extractTime(number);
				System.out.println("Last Action(diff): " + preTime + " from " + prenum + "to " + curnum);
				outPutResult(prenum, curnum);
				break;
			}
			if (preTime.equals(extractTime(number)) && (number < actions.size()-1))
			{

			}
			if (preTime.equals(extractTime(number)) && (number >= actions.size()-1))
			{
				//curnum = number - 1;
				curnum = number;
				System.out.println("Last Action(same): " + preTime + " from " + prenum + "to " + curnum);
				outPutResult(prenum, curnum);
				break;
			}
			if ((!preTime.equals(extractTime(number))) && (number < actions.size()-1))
			{
				curnum = number-1;
				System.out.println("One Action: " + preTime + " from " + prenum + " to " + curnum);
				outPutResult(prenum, curnum);
				prenum = number;
				preTime = extractTime(number);
				//number++;
			}
		}
	}
	
	/**
	 * extractTime: extract time of each line in the arraylist
	 * @param num: the num line of content in the arraylist
	 */
	public String extractTime(int num)
	{
		if (num == actions.size())
		{
			System.out.println("last one: " + num);
		}
		oneAct = actions.get(num);
		if (oneAct != null)
		{
			content = oneAct.split("\\|");
			time = content[1];
		}
		else
		{
			System.out.println("end");
		}
		return time;
	}
	
	/**
	 * storeContent: store the content of log.txt into arraylist
	 * @param 
	 */
	public boolean storeContent() throws IOException
	{
		oneline = br.readLine();
		if (oneline == null)
		{
			System.out.println("此期间没有操作！");
			noAction = true;
		}
		while (oneline != null)
		{
			actions.add(oneline);
			oneline = br.readLine();
		}
/*		for (int i = 0; i < actions.size(); i++)
		{
			System.out.println(actions.get(i));
		}*/
		System.out.println("AnalyseRecords.java: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		return noAction;
	}
	
	public void outPutResult(int prenum, int curnum) throws JDOMException, IOException
	{
		//Delete action
		if (curnum == prenum)
		{
			String linecontent = actions.get(curnum);
			file = filePath2fileName(linecontent);
			contentInAL = linecontent.split("\\|");
			String SysOperation = contentInAL[2];
			operation = getOperation(SysOperation);
			System.out.println("操作的文件是： " + file);
			System.out.println("操作的类型是： " + operation);
			if (inPIMTree(file))
			{
				syn.deleteXML(file);
			}
			if (inHTM(file))
			{
				for (int i = 0; i < ReadHTM.queue.outPutPath(file).size(); i++)
				{
					syn.deletehtm(file, ReadHTM.queue.outPutPath(file).get(i).toString());
				}
				
			}
			/*******************************************未完成******************************************/
			//还要判断file是不是在concept tree中被引用了。
			//delteNode(linecontent);
		}
		else //rename,remove actions
		{
			String linecontent1 = actions.get(prenum);
			String linecontent2 = actions.get(curnum);
			String file1 = filePath2fileName(linecontent1);
			String file2 = filePath2fileName(linecontent2);
			contentInAL = linecontent1.split("\\|");
			String SysOperation1 = contentInAL[2];
			String path1 = contentInAL[3];
			contentInAL = linecontent2.split("\\|");
			String path2 = contentInAL[3];
			String SysOperation2 = contentInAL[2];
			if (!file1.equals(file2))
			{
				System.out.println(file1 + "  " + file2);
				operation = "rename";
				System.out.println("操作的文件是： " + file1 + " to: " + file2);
				System.out.println("操作的类型是： " + operation);
				if (inPIMTree(file))
				{
					syn.renameXML(file1, file2);
				}
				if (inHTM(file))
				{
					for (int i = 0; i < ReadHTM.queue.outPutPath(file).size(); i++)
					{
						syn.rewritehtm(ReadHTM.queue.outPutPath(file).get(i).toString(), file1, file2);
					}
					
				}
			}
			else if (file1.equals(file2) && !path1.equals(path2)) 
			{
				operation  = "move";
				System.out.println("操作的文件是： " + file1 + "Path: " + path1 + " to: " + path2);
				System.out.println("操作的类型是： " + operation);
				if (inPIMTree(file))
				{
					syn.renameXML(file1, file2);
				}
				if (inHTM(file))
				{
					for (int i = 0; i < ReadHTM.queue.outPutPath(file).size(); i++)
					{
						syn.rewritehtm(ReadHTM.queue.outPutPath(file).get(i).toString(), file1, file2);
					}
					
				}
			}
		}
	}
	
	public boolean inPIMTree(String file)
	{
		boolean in = false;
		for (int i = 0; i < ReadPIMTree.htmfile.size(); i ++)
		{
			//System.out.println(ReadPIMTree.htmfile.get(i));
			if (file.equals(ReadPIMTree.htmfile.get(i)))
				in = true;
		}
		if (in == false)
			System.out.println("this file is not referenced in PIMTree");
		else
			System.out.println("this file is referenced in PIMTree!");
		return in;
	}
	
	public boolean inHTM(String file)
	{
		boolean in = false;
		if (!ReadHTM.queue.outPutPath(file).isEmpty())
			in = true;
		if (in == false)
			System.out.println("this file is not in any htm files");
		else
			System.out.println("this file is in some htm files");
		return in;
	}
	
	
	/**
	 * filePath2fileName: extract the file name
	 * @param linecontent: the whole content of one line stored in the array list 
	 */
	public String filePath2fileName(String linecontent) 
	{
		int index = linecontent.lastIndexOf('\\') + 1;
		//System.out.println(index);
		String name = linecontent.substring(index);
		return name;
	}
	
	/**
	 * getOperation: extract the kind of operation
	 * @param operation: the operation provided by system API 
	 */
	public String getOperation(String operation)
	{
		int index = operation.lastIndexOf('_') + 1;
		String tureOperation = operation.substring(index);
		return tureOperation;
	}
	
	public static void main(String[] args) throws IOException, JDOMException
	{
		AnalyseRecords ar = new AnalyseRecords();
		ar.storeContent();
		if (noAction == false)
			ar.oneAction();
	}
}
