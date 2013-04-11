import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
	private static boolean noAction = false;  //if the content of log.txt is null then true
	
	public AnalyseRecords() throws IOException
	{
		br=new BufferedReader(new FileReader("c:/log.txt"));
		actions = new ArrayList<String>();
		//oneline = br.readLine();
	}
	
	/**
	 * oneAction: analyses the data in the arraylist and sorts actions by time
	 * @param 
	 */
	public void oneAction() throws IOException
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
	
	public void outPutResult(int prenum, int curnum)
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
		}
		else
		{
			
		}
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
	
	public static void main(String[] args) throws IOException
	{
		AnalyseRecords ar = new AnalyseRecords();
		ar.storeContent();
		if (noAction == false)
			ar.oneAction();
	}
}
