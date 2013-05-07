/**
 * 
 */

/**
 * Class description goes here.
 *
 * @version  2013/3/20
 * @author 	Yuchao Zhou
 */
public class SummaryNode 
{
	private String time;
	private String file;
	private String operation;
	private String path1;
	private String path2;
	private SummaryNode next;
	
	public SummaryNode(String time, String file, String operation, String path1, String path2)
	{
		this.time = time;
		this.file = file;
		this.operation = operation;
		this.path1 = path1;
		this.path2 = path2;
		next = null;
	}
	
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	
	public void setTime(String time)
	{
		this.time = time;
	}
	
	public void setPath1(String path1)
	{
		this.path1 = path1;
	}
	
	public void setPath2(String path2)
	{
		this.path2 = path2;
	}
	
	public void setNext(SummaryNode n)
	{
		next = n;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public String getfile()
	{
		return this.file;
	}
	
	public String getoperation()
	{
		return this.operation;
	}
	
	public String getpath1()
	{
		return this.path1;
	}
	
	public String getpath2()
	{
		return this.path2;
	}
	
	public SummaryNode getNext()
	{
		return next;
	}
}
