import java.nio.file.Path;

/**
 * 
 */

/**
 * Class description goes here.
 *
 * @version  2013/5/6
 * @author 	Yuchao Zhou
 */
public class SummaryQueue 
{
	SummaryNode front;
	
	public SummaryQueue()
	{
		front = null;
	}
	
	public SummaryQueue(String time, String file, String operation, String path1, String path2)
	{
		front = new SummaryNode(time, file, operation, path1, path2);
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
	
	public void add(String time, String file, String operation, String path1, String path2)
	{
		SummaryNode c,n;
		n = new SummaryNode(time, file, operation, path1, path2);
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
	
	public void delete(String time, String file, String operation, String path1, String path2)
	{
		boolean exsit = false;
		SummaryNode c,n;
		n = new SummaryNode(time, file, operation, path1, path2);
		if (isEmpty())
		{
			front = n;
		}
		else
		{
			c = front;
			if (c.getpath2() != null)
			{
				if (file.equals(filePath2fileName(c.getpath2())))
				{
					exsit = true;
					c.setTime(time);
					c.setOperation("DELETE");
					c.setPath2("null");
				}
				
			}
			while (c.getNext() != null)
			{
				c = c.getNext();
				if (c.getpath2() != null)
				{
					if (file.equals(filePath2fileName(c.getpath2())))
					{
						exsit = true;
						c.setTime(time);
						c.setOperation("DELETE");
						c.setPath2("null");
					} 
				}
			}
			if (exsit == false)
			{
				c.setNext(n);
			}
		}
	}
	
	public void move(String time, String file, String operation, String path1, String path2)
	{
		boolean exsit = false;
		SummaryNode c,n;
		n = new SummaryNode(time, file, operation, path1, path2);
		if (isEmpty())
		{
			front = n;
		}
		else
		{
			c = front;
			if (c.getpath2() != null)
			{
				//如果传入的file等于path2的file，则确定是被修改的文件
				if (file.equals(filePath2fileName(c.getpath2())))
				{
					exsit = true;
					//现在文件名等于原始文件名，即没有重命名过
					if (file.equals(c.getfile()))
					{
						c.setTime(time);
						c.setOperation("Move");
						c.setPath2(path2);
					}
					else
					{
						c.setTime(time);
						c.setOperation("Modified");
						c.setPath2(path2);
					}
				}
			}
			while (c.getNext() != null)
			{
				c = c.getNext();
				if (c.getpath2() != null)
				{
					if (file.equals(filePath2fileName(c.getpath2())))
					{
						exsit = true;
						if (file.equals(c.getfile()))
						{
							c.setTime(time);
							c.setOperation("Move");
							c.setPath2(path2);
						}
						else
						{
							c.setTime(time);
							c.setOperation("Modified");
							c.setPath2(path2);
						}
					}
				}
			}
			if (exsit == false)
			{
				c.setNext(n);
			}
		}
	}
	
	public void rename(String time, String file, String operation, String path1, String path2)
	{
		boolean exsit = false;
		SummaryNode c,n;
		n = new SummaryNode(time, file, operation, path1, path2);
		if (isEmpty())
		{
			front = n;
		}
		else
		{
			c = front;
			if (c.getpath2() != null)
			{
				//如果传入的file等于path2的file，则确定是被修改的文件
				if (file.equals(filePath2fileName(c.getpath2())))
				{
					exsit = true;
					//如果path1的目录等于path2的目录，则没有move过
					if (filefolder(c.getpath1()).equals(filefolder(path2)))
					{
						c.setTime(time);
						c.setOperation("Rename");
						c.setPath2(path2);
					}
					else
					{
						c.setTime(time);
						c.setOperation("Modified");
						c.setPath2(path2);
					}
				}
			}
			while (c.getNext() != null)
			{
				c = c.getNext();
				if (c.getpath2() != null)
				{
					if (file.equals(filePath2fileName(c.getpath2())))
					{
						exsit = true;
						//如果path1的目录等于path2的目录，则没有move过
						if (filefolder(c.getpath1()).equals(filefolder(path2)))
						{
							c.setTime(time);
							c.setOperation("Rename");
							c.setPath2(path2);
						}
						else
						{
							c.setTime(time);
							c.setOperation("Modified");
							c.setPath2(path2);
						}
					}
				}
			}
			if (exsit == false)
			{
				c.setNext(n);
			}
		}
	}
	
	public String filefolder(String path)
	{
		//返回最后一个\之前的index
		int index = path.lastIndexOf('\\');
		//返回一个新的字符串public String substring(int beginIndex, int endIndex)
		String folderpath = path.substring(0, index);
		//System.out.println(folderpath);
		return folderpath;
	}
	
	public String filePath2fileName(String linecontent) 
	{
		int index = linecontent.lastIndexOf('\\') + 1;
		//System.out.println(index);
		String name = linecontent.substring(index);
		return name;
	}
	
	public void traverse()
	{
		SummaryNode c;
		if (isEmpty())
		{
			System.out.println("the queue is empty!");
		}
		else
		{
			c = front;
			System.out.println("Time: " + c.getTime() + " File: " + c.getfile() + " Operation: " + c.getoperation() + " Path1: " + c.getpath1() + " path2: " + c.getpath2());
			while (c.getNext() != null)
			{
				c = c.getNext();
				System.out.println("Time: " + c.getTime() + " File: " + c.getfile() + " Operation: " + c.getoperation() + " Path1: " + c.getpath1() + " path2: " + c.getpath2());
			}
		}
	}
	public String getAction(SummaryNode c)
	{
		String action = "Time: " + c.getTime() + " File: " + c.getfile() + " Operation: " + c.getoperation() + " Path1: " + c.getpath1() + " path2: " + c.getpath2();
		return action;
	}

}
