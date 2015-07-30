package sun.mybatis.constants;

/**
 * Created by 264929 on 2015/6/15.
 */
public class Constants {
    public static final String NAMESPACE_USER = "sun.demo.mybatis.mapper.UserMapper";

    public static final String NAMESPACE_CLASSES = "sun.demo.mybatis.mapper.classesMapper";

    public static String build(String namespace, String identity) {
        return namespace + "." + identity;
    }
}
