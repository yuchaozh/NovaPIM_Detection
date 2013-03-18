import java.nio.file.FileVisitResult;  
import java.nio.file.Files;  
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;  
import java.io.PrintWriter;
import java.nio.file.Path;  
import java.nio.file.Paths;  
import java.nio.file.SimpleFileVisitor;  
import java.nio.file.attribute.BasicFileAttributes;  
import java.util.ArrayList;
  
/*public class Traverse
{
	public static void main(String args[]) throws IOException
	{
		//Path startPath = Paths.get("c:/"); 
		Path startPath = Paths.get("c:/test_file2");
		TraverseDir walk = new TraverseDir();
		try 
		{  
	        Files.walkFileTree(startPath, walk);  
	    } 
		catch (IOException e) 
	    {  
	        System.err.println(e);  
	    }
		System.out.println("dircount: "+TraverseDir.dircount);
		System.out.println("~~~~~~~~~~~~~~~~~ ");
		System.out.println("DirPath Count: "+TraverseDir.dirpath.size());
		//创建txt文档存文件夹的路径
		FileWriter fw = new FileWriter("C:/log.txt");
		for(int i = 0; i < TraverseDir.dirpath.size(); i++)
		{
			fw.write(TraverseDir.dirpath.get(i)+ "\r\n");
		}
		//关闭txt
		fw.close();
	}
}*/


class TraverseDir extends SimpleFileVisitor<Path>
{
	//public static int filecount = 0;
	//文件夹的数目
	public static int dircount = 0;
	//存储文件夹路径的泛型数组
	public static ArrayList<Path> dirpath;
	
	public TraverseDir()
	{
		dirpath = new ArrayList<Path>();
	}
	
	//用preVisitDirectory就会出错
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
    {  
        //System.out.println(">>>>Dir : " + dir);  
        dirpath.add(dir);
        dircount++;
        return FileVisitResult.CONTINUE;  
    }  
	
    public FileVisitResult visitFileFailed(Path file, IOException exc) 
    {  
        System.out.println(exc);  
        return FileVisitResult.CONTINUE;
    }

}