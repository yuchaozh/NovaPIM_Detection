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

/**
 * Traverse the whole file system to find out all directories.
 *
 * @version  2013/3/20
 * @author  Yuchao Zhou
 */
class TraverseDir extends SimpleFileVisitor<Path>
{
	public static int dircount = 0;  //文件夹的数目
	public static ArrayList<Path> dirpath;	//存储文件夹路径的泛型数组
	
	public TraverseDir()
	{
		dirpath = new ArrayList<Path>();
	}
	
	//用preVisitDirectory就会出错
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
    {  

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