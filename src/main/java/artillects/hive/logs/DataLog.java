package artillects.hive.logs;

/** Report used to hold and process data in the hive
 * 
 * @author DarkGuardsman */
public class DataLog
{
    String name;
    String catigory;

    Object[] data;

    public DataLog(String name, String catigory, Object... data)
    {
        this.name = name;
        this.catigory = catigory;
        this.data = data;
    }
}
