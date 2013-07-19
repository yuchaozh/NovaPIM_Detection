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
import java.nio.file.Paths;
import java.util.ArrayList;

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
	private int offset;
	private int index;  //index of each hyperlinks in all htm files
	private int backindex;
	private ArrayList<Path> htmref;  //refereneced files contain in htm files
	private Path htmrefpath;  //the path of referenced hyperlinks in htm files
	static RefileList queue;
	static ArrayList<Path> ref_htm_path;
	//ArrayList<Path> ref_htm_path;
	
	public ReadHTM()
	{
		htmref = new ArrayList<Path>();
		queue = new RefileList();
	}
	
	/**
	 * readhtm: read the content of all htm files
	 * @param file: htm file
	 */
	public void readhtm(File file) throws IOException
	{
		//读取原始字节流
		input = new FileInputStream(file);
		inputreader = new InputStreamReader(input);
		//用bufferedreader包装了inputstreamreader，更有效率
		br = new BufferedReader(inputreader);
		String info=br.readLine();
		int line = 1;
		int currentlength = 0;
		int prelength = 0;
		while(info!=null)
		{
			//System.out.println("line: " + line);
			//System.out.println(info);   
			//System.out.println("sunm of one line: " + info.length());
			index = info.indexOf(">file:///");
			backindex = info.lastIndexOf("</a>");
			//System.out.println("index: " + index);
			currentlength = info.length();
			if (index != -1)
			{
				//System.out.println(info.substring(index + 9,backindex));
				htmrefpath = Paths.get(info.substring(index + 9,backindex));
				htmref.add(Paths.get(info.substring(index + 9,backindex)));
				index = prelength + index;
				System.out.println("where: " + file.toString());
				System.out.println("totle index: " + index);
				System.out.println(htmrefpath);
				//System.out.println(filePath2fileName(htmrefpath.toString()));
				String htrefile = filePath2fileName(htmrefpath.toString());
				
				
				System.out.println("here(htrefile): " + htrefile);
				queue.add(htrefile, file.toPath());
				System.out.println("here(htmpath): " + file.toPath());
				//queue.add(file.toPath());
				System.out.println("~~");
				//queue.traverse();
			
				
				//queue.equal(htmrefpath);
			}
			info = br.readLine();   
			prelength = prelength + currentlength;
			line++;
		}
		br.close();
		input.close();
		inputreader.close();
	}
	
	public String filePath2fileName(String linecontent) 
	{
		int index = linecontent.lastIndexOf('\\') + 1;
		//System.out.println(index);
		String name = linecontent.substring(index);
		return name;
	}
	
	/**
	 * startread: use the path of htm file to create file instance to read
	 * @param 
	 */
	public void startread() throws IOException
	{
		System.out.println("ReadHTM.java htm文件：~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (int i = 0; i < ReadPIMTree.htmpath.size(); i++)
		{
			//System.out.println(ReadPIMTree.htmpath.get(i));
			Path filepath = ReadPIMTree.htmpath.get(i);
			File htmfile = new File(filepath.toString());
			readhtm(htmfile);
		}
		System.out.println("RefileQueue: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		queue.traverse();
		System.out.println("HtmQueue: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		queue.traversehtm();
		System.out.println("~~~~~~~test~~~~~~");
	}
}



class Node
{
	private String refile;
	private Node1 htmnode;
	private Node next;
	HtmQueue htmqueue;
	
	public Node(String file)
	{
		htmqueue = new HtmQueue();
		this.refile = file;
		this.htmnode = null;
		this.next = null;
	}
	
	public void setRefile(String file)
	{
		this.refile = file;
	}
	
	public void setHtmnode(Node1 n)
	{
		this.htmnode = n;
	}
	
	public void setNext(Node n)
	{
		this.next = n;
	}
	
	public String getrefile()
	{
		return refile;
	}
	
	public Node getNext()
	{
		return next;
	}
	
	public Node1 getHtmnode()
	{
		return htmnode;
	}
}

class RefileList
{
	Node first;
	//ArrayList<Path> ref_htm_path;
	//Node rear;
	//private HtmQueue htmqueue;
	
	public RefileList()
	{
		this.first = null;
		//htmqueue = new HtmQueue();
	}
	
	public RefileList(String file)
	{
		first = new Node(file);
	}
	
	public boolean isEmpty()
	{
		boolean result = false;
		if (first == null)
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public void add(String file, Path path)
	{
		boolean fileequal = false;
		boolean htmequal = false;
		Node c,n;
		Node1 c1,n1;
		n = new Node(file);
		n1 = new Node1(path);
		//refile队列的节点为空
		if (isEmpty())
		{
			first = n;
			first.htmqueue.front = n1;
		}
		
		//refile队列中没有该节点&refile队列中有该节点
		else
		{
			c = first;
			
			//refile队列中的第一个节点等于要插入的refile节点
			if (first.getrefile().equals(file))
			{
				//fileequal = true;
				//if (fileequal == true)
				//{
					//refile节点已经存在，在htmqueue中插入htmpath节点
					if (c.htmqueue.isEmpty())
					{
						c.htmqueue.front = n1;
					}
					else
					{
						c1 = c.htmqueue.front;
						if (c.htmqueue.front.getData().equals(path))
						{
							htmequal = true;
						}
						while (c1.getNext() != null)
						{
							if (c1.getNext().getData().equals(path))
							{
								htmequal = true;
							}
							c1 = c1.getNext();
						}
						if (htmequal == false)
						{
							c1.setNext(n1);
						}
					}
				//}
			}
			else
			{
				
				
				
				while (c.getNext() != null)
				{
					if (c.getNext().getrefile().equals(file))
					{
						fileequal = true;
					}
					c = c.getNext();
					if (fileequal == true)
					{
						//refile节点已经存在，在htmqueue中插入htmpath节点
						if (c.htmqueue.isEmpty())
						{
							c.htmqueue.front = n1;
						}
						else
						{
							c1 = c.htmqueue.front;
							if (c.htmqueue.front.getData().equals(path))
							{
								htmequal = true;
							}
							while (c1.getNext() != null)
							{
								if (c1.getNext().getData().equals(path))
								{
									htmequal = true;
								}
								c1 = c1.getNext();
							}
							if (htmequal == false)
							{
								c1.setNext(n1);
							}
							break;
						}
					}
				}
				
				//refile节点没有存在，插入一个refile节点并且在htmqueue中插入htmpath节点
				if (fileequal == false)
				{
					c.setNext(n);
					//加入htmqueue的节点
					if (n.htmqueue.isEmpty())
					{
						n.htmqueue.front = n1;
					}
					else
					{
						c1 = n.htmqueue.front;
						if (n.htmqueue.front.getData().equals(path))
						{
							htmequal = true;
						}
						while (c1.getNext() != null)
						{
							if (c1.getNext().getData().equals(path))
							{
								htmequal = true;
							}
							c1 = c1.getNext();
						}
						if (htmequal == false)
						{
							c1.setNext(n1);
						}
					}
				}
			}
		
		}	
	}
	
	public void equal(String file)
	{
		Node c,n;
		boolean result = false;
		if (first == null)
		{
			result = false;
		}
		else
		{
			n = first;
			while (n.getNext() != null)
			{
				if (n.getrefile().equals(file))
				{
					result = true;
					break;
				}
				n = n.getNext();
			}
		}
	}
	
	public void traverse()
	{
		Node c;
		if (isEmpty())
		{
			System.out.println("the RefileQueue is empty!");
		}
		else
		{
			c = first;
			System.out.println(c.getrefile());
			//traversehtm();
			while (c.getNext() != null)
			{
				c = c.getNext();
				System.out.println(c.getrefile());
				//traversehtm();
			}
		}
	}
	
	public ArrayList outPutPath(String file)
	{
		//ArrayList<Path> ref_htm_path;
		ReadHTM.ref_htm_path = new ArrayList<Path>();
		boolean contain = false;
		Node c;
		Node1 c1;
		String refile = file;
		//如果Refile队列为空
		if (isEmpty())
		{
			System.out.println("the RefileQueue is empty!");
		}
		else
		{
			c = first;
			//Refile队列的第一项匹配
			if (c.getrefile().equals(refile))
			{
				contain = true;
				//Refile队列的第一项的htm队列为空
				if (c.htmqueue.isEmpty())
				{
					System.out.println("No htm path!");
				}
				else  //Refile队列的第一项的htm队列不为空
				{
					c1 = c.htmqueue.front;
					//System.out.println(c1.getData());
					ReadHTM.ref_htm_path.add(c1.getData());
					while (c1.getNext() != null)
					{
						c1 = c1.getNext();
						//System.out.println(c1.getData());
						ReadHTM.ref_htm_path.add(c1.getData());
					}
				}
			}
			else  //Refile队列不是第一项匹配
			{
				while (c.getNext() != null)
				{
					c = c.getNext();
					if (c.getrefile().equals(refile))
					{
						contain = true;
						if (c.htmqueue.isEmpty())
						{
							System.out.println("No htm path!");
						}
						else
						{
							c1 = c.htmqueue.front;
							//System.out.println(c1.getData());
							ReadHTM.ref_htm_path.add(c1.getData());
							while (c1.getNext() != null)
							{
								c1 = c1.getNext();
								//System.out.println(c1.getData());
								ReadHTM.ref_htm_path.add(c1.getData());
							}
						}
					}
					
				}
				
			}
		}
/*		for (int i = 0; i < ref_htm_path.size(); i ++)
		{
			System.out.println("!!!!" + ref_htm_path.get(i));
		}*/
		System.out.println("!!!ref_htm_path: ");
		for (int i = 0; i < ReadHTM.ref_htm_path.size(); i++)
		{
			
			System.out.println(ReadHTM.ref_htm_path.get(i));
		}

		return ReadHTM.ref_htm_path;
	}
	
	public void traversehtm()
	{
		Node c;
		Node1 c1;
		if (isEmpty())
		{
			System.out.println("refile node is empty!");
		}
		else
		{
			c = first;
			System.out.println("Node: " + c.getrefile() + "~~~");
			if (c.htmqueue.isEmpty())
			{
				System.out.println("No htm path!");
			}
			else
			{
				c1 = c.htmqueue.front;
				System.out.println(c1.getData());
				while (c1.getNext() != null)
				{
					c1 = c1.getNext();
					System.out.println(c1.getData());
				}
			}
			while (c.getNext() != null)
			{
				c = c.getNext();
				System.out.println(" ~~~ Node: " + c.getrefile() + "~~~");
				if (c.htmqueue.isEmpty())
				{
					System.out.println("No htm path!");
				}
				else
				{
					c1 = c.htmqueue.front;
					System.out.println(c1.getData());
					while (c1.getNext() != null)
					{
						c1 = c1.getNext();
						System.out.println(c1.getData());
					}
				}
			}
		}
	}
}

class Node1
{
	private Path htmpath;
	private Node1 next;
	
	public Node1(Path path)
	{
		htmpath = path;
		next = null;
	}
	
	public void setData(Path path)
	{
		htmpath = path;
	}
	
	public void setNext(Node1 n)
	{
		next = n;
	}
	
	public Path getData()
	{
		return htmpath;
	}
	
	public Node1 getNext()
	{
		return next;
	}
}

class HtmQueue
{
	Node1 front;
	
	public HtmQueue()
	{
		front = null;
	}
	
	public HtmQueue(Path path)
	{
		front = new Node1(path);
	}
	
	public boolean isEmpty()
	{
		boolean result = false;
		if (front == null)
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public void add(Path path)
	{
		Node1 c,n;
		n = new Node1(path);
		if (isEmpty())
		{
			front = n;
		}
		else
		{
			c = front;
			while (c.getNext() != null)
			{
				c = c.getNext();
			}
			c.setNext(n);
		}
	}
}


