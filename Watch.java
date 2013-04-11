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

	//���캯��
	public Watch() throws IOException
	{
		service = FileSystems.getDefault().newWatchService();
		//����һ����������
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
		System.out.println("Watch.java  ���ӵ�Ŀ¼������ "+path.size());
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
		System.out.println("������");
		fw = new FileWriter("C:/log.txt");
		SimpleDateFormat dataformate = new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss");
		//String time = dataformate.format(new Date());
		String time;
		System.out.println(ReadPIMTree.dirpath.size());
/*		for (int i = 0; i < ReadPIMTree.dirpath.size(); i++)
		{
			System.out.println("�����ļ���Ŀ¼�� " + ReadPIMTree.dirpath.get(i));
		}*/
		while(true)
	    {
	          //WatchKey key = service.take();    // retrieve the watchkey
			key = service.take();
	          for (WatchEvent event : key.pollEvents())
	          {
	             //System.out.println(event.kind() + ": "+ event.context());  // Display event and file name
	        	  WatchEvent<Path> evt = cast(event); 
	        	  //�����޸��ļ�������
	        	  Path name = evt.context();
	        	  //(Path) key.watchable()���ر��޸ĵĸ���Ŀ¼��resolve�ǽ���ļ�Ŀ¼�͸���Ŀ¼
	        	  child = ((Path) key.watchable()).resolve(name);
/****************referenced files�Ѿ���¼��reffile�ڣ����ԶԱ��ˣ��������ڲ�������ע���ˣ���ɾ************************/
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
