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
  
class TraverseDir extends SimpleFileVisitor<Path>
{
	//public static int filecount = 0;
	//�ļ��е���Ŀ
	public static int dircount = 0;
	//�洢�ļ���·���ķ�������
	public static ArrayList<Path> dirpath;
	
	public TraverseDir()
	{
		dirpath = new ArrayList<Path>();
	}
	
	//��preVisitDirectory�ͻ����
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