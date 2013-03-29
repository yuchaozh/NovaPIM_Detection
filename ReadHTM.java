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
	
/*	public ReadHTM()
	{

	}*/
	
	/**
	 * readhtm: read the content of htm file
	 * @param file: htm file
	 */
	public void readhtm(File file) throws IOException
	{
		input = new FileInputStream(file);
		inputreader = new InputStreamReader(input);
		br = new BufferedReader(inputreader);
		String info=br.readLine();
		while(info!=null)
		{
			System.out.println(info);   
			info = br.readLine();   
		}
	}
	
	public void findreference()
	{
		
	}
	
	/**
	 * startread: use the path of htm file to create file instance to read
	 * @param 
	 */
	public void startread() throws IOException
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("ReadHTM.java htmÎÄ¼þ£º ");
		for (int i = 0; i < ReadPIMTree.htmpath.size(); i++)
		{
			System.out.println(ReadPIMTree.htmpath.get(i));
			Path filepath = ReadPIMTree.htmpath.get(i);
			File htmfile = new File(filepath.toString());
			readhtm(htmfile);
		}
	}
}
