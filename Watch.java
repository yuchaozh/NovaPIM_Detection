import java.net.URI;
import java.nio.*;
import java.io.*;
import java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Add detection to all directories which are found by Traverse.java
 * @version  2013/3/20
 * @author  Yuchao Zhou
 */
class Watch 
{
	public WatchService service;
	public ArrayList<Path> path;  //paths of files which are detected
	public static int quite = 1;
	public static boolean valid;
	public static WatchKey key;
	public static FileWriter fw;

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
	
	/**
	 * SetPath: register StandarWatchEventKinds on all paths
	 * @param 
	 */
	public void  SetPath() throws IOException, InterruptedException
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Watch.java  监视的目录数量： "+path.size());
		for (int i=0; i < this.path.size(); i++)
		{
			this.path.get(i).register(service,
		            StandardWatchEventKinds.ENTRY_CREATE,
		            //StandardWatchEventKinds.ENTRY_MODIFY,
		            StandardWatchEventKinds.ENTRY_DELETE);  // Register the directory
		}
		begin();
	}
	
	/**
	 * begin: start to detect all directories
	 * @param 
	 */
	public void begin() throws IOException, InterruptedException
	{
		Path child = null;
		System.out.println("操作：");
		fw = new FileWriter("C:/log.txt");
		SimpleDateFormat dataformate = new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss");
		//String time = dataformate.format(new Date());
		String time;
		System.out.println(ReadPIMTree.dirpath.size());
/*		for (int i = 0; i < ReadPIMTree.dirpath.size(); i++)
		{
			System.out.println("引用文件的目录： " + ReadPIMTree.dirpath.get(i));
		}*/
		while(true)
	    {
	          //WatchKey key = service.take();    // retrieve the watchkey
			key = service.take();
	          for (WatchEvent event : key.pollEvents())
	          {
	             //System.out.println(event.kind() + ": "+ event.context());  // Display event and file name
	        	  WatchEvent<Path> evt = cast(event); 
	        	  //返回修改文件的名称
	        	  Path name = evt.context();
	        	  //(Path) key.watchable()返回被修改的父亲目录，resolve是结合文件目录和父亲目录
	        	  child = ((Path) key.watchable()).resolve(name);
/****************referenced files已经记录在reffile内，可以对比了，但现在在测试所以注释了，勿删************************/
	        	  //for (int i = 0; i < ReadPIMTree.reffile.size(); i++)
	        	  //{
	        	  	for (int i = 0; i < ReadPIMTree.dirpath.size(); i++)
	        	  	{
	        	  		if (key.watchable().equals(ReadPIMTree.dirpath.get(i)))
	        	  		{
	        	  			System.out.format(new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss").format(new Date()) + "|%s|%s\n", event.kind(), child);
	        	  			time = dataformate.format(new Date());
	        	  			fw.append(time+"|"+event.kind()+"|"+child+"\r\n");
	        	  			break;
	        	  		}
	        	  	}
	        	  //}
	          }

	          //boolean valid = key.reset();
	          valid = key.reset();
	          if (!valid)
	          {
	        	 fw.close();
	        	 System.out.println("Detection END!");
	             break; // Exit if directory is deleted
	          }
	    }	
	}
}
