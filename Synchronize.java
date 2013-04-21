/**
 *    ��ȡԭ�ļ�����д�����ļ�������Դ�ļ��޷�ɾ�������ļ��޷���������������������������������������������������
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rewite the modifcations into PIMTree.xml and htm files
 *
 * @version  2013/4/20
 * @author 	Yuchao Zhou
 */

public class Synchronize
{
	private static int totleline = 0;
	FileOutputStream fop;
	
	public Synchronize() throws  IOException
	{
		
	}
	
	//deleteXML����
	public void deleteXML(String file) throws IOException
	{
		//String target = "C:\\PIM\\�����¼.htm";
		String target = "C:.PIM.�����¼.htm";
		Pattern p = Pattern.compile(file);
		File outfile = new File("c:/PIMTREE/PIMTree1.xml");
		File infile = new File("c:/PIMTREE/PIMTree.xml");
		fop = new FileOutputStream(outfile);
		Writer out = new OutputStreamWriter(fop, "UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
		String oneline = br.readLine();
		while (oneline != null)
		{
			//System.out.println(oneline);
			Matcher m = p.matcher(oneline);
			boolean result = m.find();
			if (result == true)
			{
				oneline = br.readLine();
				oneline = br.readLine();
			}
			else
			{
				out.write(oneline);
				oneline = br.readLine();
				out.write("\n");
			}
		}
		fop.flush();
		out.close();
		fop.close();
		br.close();
		infile.delete();
		outfile.renameTo(infile);
	}
	
	//renameXML����
	public void renameXML(String prefile, String postfile) throws IOException
	{
		File outfile = new File("c:/PIMTREE/PIMTree1.xml");
		File infile = new File("c:/PIMTREE/PIMTree.xml");
		fop = new FileOutputStream(outfile);
		Writer out = new OutputStreamWriter(fop, "UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
		String oneline = br.readLine();
		while (oneline != null)
		{
			oneline = oneline.replace(prefile, postfile);
			System.out.println(oneline);
			out.write(oneline);
			out.write("\n");
			oneline = br.readLine();
		}
		fop.flush();
		out.close();
		fop.close();
		br.close();
		infile.delete();
		outfile.renameTo(infile);
	}
	
	public void deletehtm(String file, String path) throws IOException
	{
		Pattern p = Pattern.compile(path);
		File bakfile = new File(file + "bak");
		fop = new FileOutputStream(bakfile);
		Writer out = new OutputStreamWriter(fop);
		File readfile = new File(file);
		//System.out.println(readfile);
		BufferedReader br = new BufferedReader(new FileReader(readfile));
		String oneline = br.readLine();
		//System.out.println(oneline);
		System.out.println("Path which would be deleted: " + path);
		//System.out.println("String file: " + file);
		while (oneline != null)
		{
			Matcher m = p.matcher(oneline);
			boolean result = m.find();
			if (result == true)
			{
				//System.out.println(" here: " + oneline);
				oneline = br.readLine();
				
			}
			else
			{
				out.write(oneline);
				oneline = br.readLine();
				out.write("\n");
			}
		}
		fop.flush();
		out.close();
		fop.close();
		br.close();
		readfile.delete();
		bakfile.renameTo(readfile);
	}
	
	public void rewritehtm(String file, String prepath, String postpath) throws IOException
	{
		Pattern p = Pattern.compile(prepath);
		File bakfile = new File(file + "bak");
		fop = new FileOutputStream(bakfile);
		Writer out = new OutputStreamWriter(fop);
		File readfile = new File(file);
		//System.out.println(readfile);
		BufferedReader br = new BufferedReader(new FileReader(readfile));
		String oneline = br.readLine();
		while (oneline != null)
		{
			oneline = oneline.replace(prepath, postpath);
			System.out.println(oneline);
			out.write(oneline);
			out.write("\n");
			oneline = br.readLine();
		}
		fop.flush();
		out.close();
		fop.close();
		br.close();
		readfile.delete();
		bakfile.renameTo(readfile);
	}
	

	public static void main (String[] args) throws IOException
	{
		String file = "C:\\PIM\\�����¼.htm";
		file = file.replace("\\", ".");
		String prefile = "C:\\PIM\\SubPIM\\subpim.htm";
		//prefile = prefile.replace("\\", ".");
		String postfile = "C:\\Users\\yuchaozh\\Desktop\\��ҵ���\\�ο�����\\Yang��s\\A Novel PIM System and its Effective Storage.pdf";
		System.out.println(file);
		Synchronize syn = new Synchronize();
		
		syn.deleteXML(file);
		//syn.renameXML(prefile, postfile);
		String htmfile = "C:/PIM/�����¼.htm";
		String path = "C:/Users/yuchaozh/Desktop/books/GitȨ��ָ��.pdf";
		//syn.deletehtm(htmfile, path);
		String prepath = "C:/Users/yuchaozh/Desktop/books/�㷨����.pdf";
		String postpath = "C:/Users/yuchaozh/Desktop/books/GitȨ��ָ��.pdf";
		//syn.rewritehtm(htmfile, prepath, postpath);
	}
}
