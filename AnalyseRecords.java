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
	static SummaryQueue summary;
	
	public AnalyseRecords() throws IOException
	{
		br = new BufferedReader(new FileReader("c:/log.txt"));
		actions = new ArrayList<String>();
		syn = new Synchronize();
		summary = new SummaryQueue();
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
		
		//只有一条记录
		if (actions.size() == 1)
		{
			System.out.println("Only One Action: " + preTime);
			outPutResult(prenum, curnum);
		}
			
		//有 >= 2 条记录
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
			//System.out.println(time);
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
		System.out.println(oneline);
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
		System.out.println(noAction);
		return noAction;
	}
	
	public void outPutResult(int prenum, int curnum) throws IOException
	{
		//Delete action
		if (curnum == prenum)
		{
			String linecontent = actions.get(curnum);
			file = filePath2fileName(linecontent);
			contentInAL = linecontent.split("\\|");
			String time = contentInAL[1];
			String SysOperation = contentInAL[2];
			String path1 = contentInAL[3];
			operation = getOperation(SysOperation);
			System.out.println("操作的时间是： " + time);
			System.out.println("操作的文件是： " + file);
			System.out.println("操作的类型是： " + operation);
			if (operation.equals("CREATE"))
			{
				
			}
			else
			{
				summary.delete(time, file, operation, null, null);
			}
		}
		else
		{
			String linecontent1 = actions.get(prenum);
			String linecontent2 = actions.get(curnum);
			String file1 = filePath2fileName(linecontent1);
			String file2 = filePath2fileName(linecontent2);
			contentInAL = linecontent1.split("\\|");
			String time = contentInAL[1];
			String SysOperation1 = contentInAL[2];
			String path1 = contentInAL[3];
			contentInAL = linecontent2.split("\\|");
			String path2 = contentInAL[3];
			String SysOperation2 = contentInAL[2];
			if (!file1.equals(file2))
			{
				System.out.println(file1 + "  " + file2);
				operation = "Rename";
				System.out.println("操作的文件是： " + path1 + " to: " + path2);
				System.out.println("操作的类型是： " + operation);
				summary.rename(time, file1, operation, path1, path2);
			}
			//move action
			else if (file1.equals(file2) && !path1.equals(path2)) 
			{
				operation  = "Move";
				System.out.println("操作的时间是： " + time);
				System.out.println("操作的文件是： " + file1 + "Path: " + path1 + " to: " + path2);
				System.out.println("操作的类型是： " + operation);
				summary.move(time, file1, operation, path1, path2);
			}
		}
	}
	
	public void conduct() throws IOException
	{
		SummaryNode c;
		if (summary.isEmpty())
		{
			System.out.println("the queue is empty!");
		}
		else
		{
			c = summary.front;
			System.out.println("Time: " + c.getTime() + " File: " + c.getfile() + " Operation: " + c.getoperation() + " Path1: " + c.getpath1() + " path2: " + c.getpath2());
			//Delete
			if (c.getoperation().equals("DELETE"))
			{
				if (inPIMTree(c.getfile()))
				{
					syn.deleteXML(c.getfile());
				}
				if (inHTM(c.getfile()))
				{
					for (int i = 0; i < ReadHTM.ref_htm_path.size(); i++)
					{
						String path = ReadHTM.ref_htm_path.get(i).toString();
						String path1 = c.getfile().replace("\\", "/");
						path1 = path1.replace("c:", "C:");
						syn.deletehtm(path, path1);
					}
					
				}
			}
			//not delete. modified, rename, remove
			else
			{
				/*****************************************************************************************************************************************/
				System.out.println(c.getoperation() + ">>>>>>>>>");
				String path1 = c.getpath1();
				String path2 = c.getpath2();
				if (inPIMTree(c.getfile()))
				{
					//String path1 = c.getpath1();
					//String path2 = c.getpath2();
					path1 = path1.replace("c:", "C:");
					path2 = path2.replace("c:", "C:");
					syn.renameXML(path1, path2);
				}
				if (inHTM(c.getfile()))
				{
					System.out.println("!!!here");
					//for (int i = 0; i < ReadHTM.queue.outPutPath(file).size(); i++)
					for (int i = 0; i < ReadHTM.ref_htm_path.size(); i++)
					{
						System.out.println(ReadHTM.ref_htm_path.size());
						//System.out.println("!!!"+ReadHTM.queue.outPutPath(file).get(i));
						//String path = ReadHTM.ref_htm_path.get(i).toString().replace("\\", "/");
						String path = ReadHTM.ref_htm_path.get(i).toString();
						System.out.println("path: " + path);
						path1 = path1.replace("\\", "/");
						path1 = path1.replace("c:", "C:");
						path2 = path2.replace("\\", "/");
						path2 = path2.replace("c:", "C:");
						System.out.println("path1: " + path1 + " path2: " + path2);
						syn.rewritehtm(path, path1, path2);
					}
					
				}
				
			}
			while (c.getNext() != null)
			{
				c = c.getNext();
				System.out.println("Time: " + c.getTime() + " File: " + c.getfile() + " Operation: " + c.getoperation() + " Path1: " + c.getpath1() + " path2: " + c.getpath2());
				//delete
				if (c.getoperation().equals("DELETE"))
				{
					if (inPIMTree(c.getfile()))
					{
						syn.deleteXML(c.getfile());
					}
					if (inHTM(c.getfile()))
					{
						for (int i = 0; i < ReadHTM.ref_htm_path.size(); i++)
						{
							String path = ReadHTM.ref_htm_path.get(i).toString();
							String path1 = c.getfile().replace("\\", "/");
							path1 = path1.replace("c:", "C:");
							syn.deletehtm(path, path1);
						}
		
					}
				}
				//not delete. modified, rename, remove
				else
				{
					/*****************************************************************************************************************************************/
					System.out.println(c.getoperation() + ">>>>>>>>>");
					String path1 = c.getpath1();
					String path2 = c.getpath2();
					if (inPIMTree(c.getfile()))
					{
						//String path1 = c.getpath1();
						//String path2 = c.getpath2();
						path1 = path1.replace("c:", "C:");
						path2 = path2.replace("c:", "C:");
						syn.renameXML(path1, path2);
					}
					if (inHTM(c.getfile()))
					{
						System.out.println("!!!here");
						//for (int i = 0; i < ReadHTM.queue.outPutPath(file).size(); i++)
						for (int i = 0; i < ReadHTM.ref_htm_path.size(); i++)
						{
							System.out.println(ReadHTM.ref_htm_path.size());
							//System.out.println("!!!"+ReadHTM.queue.outPutPath(file).get(i));
							//String path = ReadHTM.ref_htm_path.get(i).toString().replace("\\", "/");
							String path = ReadHTM.ref_htm_path.get(i).toString();
							System.out.println("path: " + path);
							path1 = path1.replace("\\", "/");
							path1 = path1.replace("c:", "C:");
							path2 = path2.replace("\\", "/");
							path2 = path2.replace("c:", "C:");
							System.out.println("path1: " + path1 + " path2: " + path2);
							syn.rewritehtm(path, path1, path2);
						}
						
					}
					
				}
			}
		}
		
	}
	
	public boolean inPIMTree(String file)
	{
		System.out.println("~~~~AnaluseRecords.java--inPIMTree~~~~");
		String PIMfile = file;
		System.out.println(" changed file: " + PIMfile);
		boolean in = false;
		for (int i = 0; i < ReadPIMTree.reffile.size(); i ++)
		{
			if (PIMfile.equals(ReadPIMTree.reffile.get(i)))
			{
				in = true;
			}
		}
		if (in == false)
			System.out.println("this file is not referenced in PIMTree");
		else
			System.out.println("this file is referenced in PIMTree!");
		return in;
	}
	
	public boolean inHTM(String file)
	{
		System.out.println("~~~~AnaluseRecords.java--inHTM~~~~");
		String htmfile = file;
		System.out.println(" changed file: " + htmfile);
		boolean in = false;
		ReadHTM.queue.outPutPath(htmfile);
		//if (!ReadHTM.queue.outPutPath(htmfile).isEmpty())
		if (!ReadHTM.ref_htm_path.isEmpty())
		{
			System.out.println(ReadHTM.ref_htm_path.size());
			in = true;
		}
			
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
