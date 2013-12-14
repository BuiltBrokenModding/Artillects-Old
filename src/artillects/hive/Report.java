package artillects.hive;

/**
 * Report used to hold and process data in the hive
 * 
 * @author Dark
 */
public class Report
{
	String name;
	String catigory;

	Object[] data;

	public Report(String name, String catigory, Object... data)
	{
		this.name = name;
		this.catigory = catigory;
		this.data = data;
	}
}
