import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Read PIMTree.xml to extract all referenced file's names and paths.
 *
 * @version  2013/3/27
 * @author 	Yuchao Zhou
 */
public class ReadPIMTree extends DefaultHandler
{
	private static String xmlfile = "C:/Eclipse_Jave/eclipse/PIM/Sources/PIMTree.xml";
	public static SAXParserFactory saxParserFactory = null;
	public static SAXParser saxPaser = null;
	private String filename;  //the name of referenced file in PIMTree.xml
	private String disname;  //displayed name in the concept tree of NovaPIM system
	private String filepath;  //the path of referenced file in PIMTree.xml
	private String folderpath;  //the folder path of referenced file in PIMTree.xml
	private String fileextension;  //the extension of file
	private static ArrayList<Path> dirpath;  //ArrayList stores all path of referenced files in the concept tree
	public static ArrayList<String> reffile;  //ArrayList stores all referenced files in the concept tree
	public static ArrayList<String> htmfile;  //ArrayList stores all htm files in the concept tree then IR links in them
	public static ArrayList<Path> htmpath;  //ArrayList stores all path of htm files in the concept tree
	
	public ReadPIMTree() throws ParserConfigurationException, SAXException, IOException
	{
		dirpath = new ArrayList<Path>();
		reffile = new ArrayList<String>();
		htmfile = new ArrayList<String>();
		htmpath = new ArrayList<Path>();
	}
	
	public void startRead() throws SAXException, IOException
	{
		saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			saxPaser = saxParserFactory.newSAXParser();
			ReadPIMTree handler = new ReadPIMTree();
			saxPaser.parse(xmlfile, handler);
/*			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("ReadPIMTree.java 在concept tree中引用的文件的文件: ");
			for (int i = 0; i < reffile.size(); i++)
			{
				System.out.println(reffile.get(i));
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("ReadPIMTree.java htm的文件: ");
			for (int i = 0; i < htmfile.size(); i++)
			{
				System.out.println(htmfile.get(i));
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("ReadPIMTree.java htm的文件的路径: ");
			for (int i = 0; i < htmpath.size(); i++)
			{
				System.out.println(htmpath.get(i));
			}*/
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException
	{
		//attrs.getLength()一行元素中有多少内容
		String type = attrs.getValue("", "type");
		if (type.equals("file"))
		{
			disname = attrs.getValue("", "dispname");
			filepath = attrs.getValue("", "path");
			filename = filePath2fileName(filepath);
			folderpath = filefolder(filepath);
			reffile.add(filename);
			//从String类型转化为Path类型
			dirpath.add(Paths.get(folderpath));
			//System.out.println(filename);
			//System.out.println(filepath);
			//System.out.println(attrs.getValue("", "dispname")+" : "+ attrs.getValue("", "path")+" : "+ attrs.getValue("", "type")+" : "+ attrs.getValue("", "expanded"));
		}
	}
	
	/**
	 * filePath2fileName: return the file of referenced file in PIMTree.xml
	 * @param path the whole path of referenced file in PIMTree.xml
	 */
	public String filePath2fileName(String path) 
	{
		int index = path.lastIndexOf('\\') + 1;
		//System.out.println(index);
		String name = path.substring(index);
		fileextension = filename2fileextension(name);
		//judge whether file are htm file
		if (fileextension.equals("htm"))
		{
			htmfile.add(name);
			htmpath.add(Paths.get(filepath));
		}
		return name;
	}
	
	/**
	 * filename2fileextension: return the file extension of referenced file in PIMTree.xml if it equals "htm" then store 
	 * them in an ArrayList htmfile
	 * @param filename the name of referenced file in PIMTree.xml
	 */
	public String filename2fileextension(String filename)
	{
		int index = filename.lastIndexOf('.') + 1;
		String extension = filename.substring(index);
		//System.out.println(extension);
		return extension;
	}
	
	/**
	 * filefolder: return the path of the folders of all referenced files in PIMTree.xml in order to add detection on them.
	 * @param path the whole path of referenced file in PIMTree.xml
	 */
	public String filefolder(String path)
	{
		//返回最后一个\之前的index
		int index = path.lastIndexOf('\\');
		//返回一个新的字符串public String substring(int beginIndex, int endIndex)
		folderpath = path.substring(0, index);
		//System.out.println(folderpath);
		return folderpath;
	}
}
