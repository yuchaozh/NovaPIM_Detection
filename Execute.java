import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Execute 
{
	public static void main(String args[]) throws IOException, InterruptedException
	{
		Execute execute = new Execute();
		execute.traverse();
		execute.watch();

	}
	
	public void traverse() throws IOException
	{
		//Path startPath = Paths.get("c:/"); 
		Path startPath = Paths.get("c:/test_file2");
		TraverseDir traversedir = new TraverseDir();
		try 
		{  
	        Files.walkFileTree(startPath, traversedir);  
	    } 
		catch (IOException e) 
	    {  
	        System.err.println(e);  
	    }
		//System.out.println("dircount: "+TraverseDir.dircount);
		//System.out.println("~~~~~~~~~~~~~~~~~ ");
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
	
	public void watch() throws IOException, InterruptedException
	{
		Watch watch = new Watch();
		System.out.println("监视的文件夹： ");
		for(int i = 0; i < TraverseDir.dirpath.size(); i++)
		{
			Object url = "";
			//System.out.println(TraverseDir.dirpath.size());
			 url = TraverseDir.dirpath.get(i);
			 //System.out.println(url.toString());
			 watch.path.add(Paths.get(url.toString()));
		}
		
		for (int i =0; i < watch.path.size(); i++)
		{
			System.out.print(watch.path.get(i) + "; ");
		}
		System.out.println();
		watch.SetPath();
	}

}
