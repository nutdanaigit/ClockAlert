package ayp.aug.alarmclock.DataBase;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class DbSchema  {
    public static final class clockTable{
        public static final String NAME ="clocks";
        public static final class Col{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "time";
            public static final String CHECK = "check";
        }
    }
}
