/**
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Read all htm files to find out the hyperlinks and offset
 *
 * @version  2013/3/28
 * @author 	Yuchao Zhou
 */
public class ReadHTM 
{
	FileInputStream input;
	InputStreamReader inputreader;
	BufferedReader br;
	int offset;
	private int index;
	private int backindex;
	private ArrayList<Path> htmref;
	private Path htmrefpath;
	
	public ReadHTM()
	{
		htmref = new ArrayList<Path>();
	}
	
	/**
	 * readhtm: read the content of all htm files
	 * @param file: htm file
	 */
	public void readhtm(File file) throws IOException
	{
		//读取原始字节流
		input = new FileInputStream(file);
		inputreader = new InputStreamReader(input);
		//用bufferedreader包装了inputstreamreader，更有效率
		br = new BufferedReader(inputreader);
		String info=br.readLine();
		int line = 1;
		int currentlength = 0;
		int prelength = 0;
		while(info!=null)
		{
			//System.out.println("line: " + line);
			//System.out.println(info);   
			//System.out.println("sunm of one line: " + info.length());
			index = info.indexOf(">file:///");
			backindex = info.lastIndexOf("</a>");
			//System.out.println("index: " + index);
			currentlength = info.length();
			if (index != -1)
			{
				//System.out.println(info.substring(index + 9,backindex));
				htmrefpath = Paths.get(info.substring(index + 9,backindex));
				htmref.add(Paths.get(info.substring(index + 9,backindex)));
				index = prelength + index;
				//System.out.println("totle index: " + index);
			}
			info = br.readLine();   
			prelength = prelength + currentlength;
			line++;
		}
	}
	
	
	/**
	 * startread: use the path of htm file to create file instance to read
	 * @param 
	 */
	public void startread() throws IOException
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("ReadHTM.java htm文件： ");
		for (int i = 0; i < ReadPIMTree.htmpath.size(); i++)
		{
			System.out.println(ReadPIMTree.htmpath.get(i));
			Path filepath = ReadPIMTree.htmpath.get(i);
			File htmfile = new File(filepath.toString());
			readhtm(htmfile);
		}
		for (int i = 0; i < htmref.size(); i++)
		{
			System.out.println(htmref.get(i));
		}

	}
}
