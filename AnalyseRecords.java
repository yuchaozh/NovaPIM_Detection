import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Analyze the modification records in log.txt to out put actual operation.
 *
 * @version  2013/3/20
 * @author  Yuchao Zhou
 */
public class AnalyseRecords 
{
	BufferedReader br;
	String oneline;
	String content[];  //the content of oneline in log.txt
	String day;  
	String time;
	String operation;
	String file;
	
	public AnalyseRecords() throws FileNotFoundException
	{
		br=new BufferedReader(new FileReader("c:/log.txt"));
	}
	
	public void readoneaction() throws IOException
	{
		oneline = br.readLine();
		if (oneline == null)
		{
			System.out.println("ÎÞ²Ù×÷£¡");
		}
		else
		{
			content = oneline.split("\\|"); 
			day = content[0];
			time = content[1];
			operation = content[2];
			file = content[3];
		}
		for (int i = 0; i < content.length; i++)
		{
			System.out.println(content[i]);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		AnalyseRecords ar = new AnalyseRecords();
		ar.readoneaction();
	}
	
}
