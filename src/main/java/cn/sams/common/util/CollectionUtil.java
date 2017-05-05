package cn.sams.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {

    public static <T> Map<String,T> listToMap(List<T> list, String keyField){
        Map<String,T> map = new HashMap<String,T>();
        if(Chk.emptyCheck(list)){
            for(T t : list){
                String key = ReflectUtil.getValueByGet(keyField,t);
                if(Chk.spaceCheck(key)){
                    map.put(key,t);
                }
            }
        }
        return map;
    }


    public static <T> List<T> mapToList(Map<String,T> m){
        return new ArrayList<T> (m.values());
    }
}
