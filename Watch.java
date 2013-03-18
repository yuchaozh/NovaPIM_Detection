
/**
 * @author yuchaozh
 *
 */

import java.net.URI;
import java.nio.*;
import java.io.*;
import java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Watch 
{
	public WatchService service;
	public ArrayList<Path> path;

	//构造函数
	public Watch() throws IOException
	{
		service = FileSystems.getDefault().newWatchService();
		//构造一个泛型数组
		path = new ArrayList<Path>();
	}
	
	static <T> WatchEvent<Path> cast(WatchEvent<?> event) {   
		return (WatchEvent<Path>)event;   
	}
	
	public void  SetPath() throws IOException, InterruptedException
	{
		System.out.println("泛型数组的大小为： "+path.size());
		for (int i=0; i < this.path.size(); i++)
		{
			this.path.get(i).register(service,
		            StandardWatchEventKinds.ENTRY_CREATE,
		            StandardWatchEventKinds.ENTRY_MODIFY,
		            StandardWatchEventKinds.ENTRY_DELETE);  // Register the directory
		}
		begin();
	}
	
	public void begin() throws IOException, InterruptedException
	{
		Path child = null;
		System.out.println("操作：");
		while(true)
	       {
	          WatchKey key = service.take();    // retrieve the watchkey
	          for (WatchEvent event : key.pollEvents())
	          {
	             //System.out.println(event.kind() + ": "+ event.context());  // Display event and file name
	        	  WatchEvent<Path> evt = cast(event); 
	        	  //返回修改文件的名称
	        	  Path name = evt.context();
	        	  //(Path) key.watchable()返回被修改的父亲目录，resolve是结合文件目录和父亲目录
	        	  child = ((Path) key.watchable()).resolve(name);
	        	  System.out.format(new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss").format(new Date()) + "|%s|%s\n", event.kind(), child);

	        	  //System.out.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "  %s|%s\n", event.kind(), child);
	          }
	          boolean valid = key.reset();
	          if (!valid)
	          {
	             break; // Exit if directory is deleted
	          }
	       }
	}
}

/*public class Reviced_Watch
{
	public static void main(String args[]) throws IOException, InterruptedException
	{
		for (int i =0; i < TraverseDir.dirpath.size(); i++)
		{
			System.out.print(TraverseDir.dirpath.get(i) + "; ");
		}
		Watch watch = new Watch();
		watch.path.add(Paths.get("c:/test_file2/test_file2_2"));
		watch.path.add(Paths.get("c:/file_test"));
		watch.path.add(Paths.get("c:/test_file2"));
		System.out.println("监视的文件夹： ");
		for (int i =0; i < watch.path.size(); i++)
		{
			System.out.print(watch.path.get(i) + "; ");
		}
		System.out.println();
		watch.SetPath();
	}
}*/