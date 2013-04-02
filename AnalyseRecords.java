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
	private BufferedReader br;
	private String oneline;
	public String content[];  //the content of oneline in log.txt
	private String day;  
	private String time;
	private String operation;
	private String file;
	
	public AnalyseRecords() throws FileNotFoundException
	{
		br=new BufferedReader(new FileReader("c:/log.txt"));
	}
	
	public String[] readoneaction() throws IOException
	{
		oneline = br.readLine();
/*		if (oneline == null)
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
		}*/
		if (oneline != null)
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
		return content;
	}
	
/*	public static void main(String[] args) throws IOException
	{
		AnalyseRecords ar = new AnalyseRecords();
		ar.readoneaction();
	}*/
	
}
