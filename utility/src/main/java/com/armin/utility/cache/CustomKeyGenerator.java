package com.armin.utility.cache;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.repository.orm.service.FilterBase;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 **/
public class CustomKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        FilterBase filter = null;
        String[] include = new String[]{};

        StringBuilder key = new StringBuilder(
                target.getClass().getSimpleName()
                        + "_"
                        + method.getName());

        if (Arrays.stream(params).anyMatch(item -> item instanceof FilterBase)) {
            filter = (FilterBase) Arrays.stream(params).filter(item -> item instanceof FilterBase).findFirst().get();
            Class curClass = filter.getClass();
            Method[] allMethods = curClass.getMethods();
            List<Method> getters = new ArrayList<>();
            for (Method met : allMethods) {
                if (met.getName().startsWith("get") && !met.getName().startsWith("getClass") && !met.getName().startsWith("getSort")) {
                    getters.add(met);
                } else if (met.getName().startsWith("getSort")) {
                    try {
                        List<SortOption> sort = (List<SortOption>) met.invoke(filter);
                        if (sort != null) {
                            for (SortOption option : sort) {
                                key.append("_")
                                        .append(option.getColumn())
                                        .append("_")
                                        .append(option.getType());
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
            for (Method get : getters) {
                try {
                    if (get.invoke(filter) != null) {
                        key.append("_").append(get.getName().substring(3).toLowerCase()).append("=").append(get.invoke(filter));
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {

                }
            }
        }
        for (Object param : params) {
            if (param instanceof FilterBase){}else{
              key.append("_").append(param);
            }
        }
        boolean checkInclude = false;
        for (Object o : params) {
            if (o == null) {
                checkInclude = false;
                break;
            }
            checkInclude = true;
        }
        if (checkInclude && Arrays.stream(params).anyMatch(item -> item.getClass().equals(include.getClass()))) {
            String[] sendInclude = (String[]) Arrays.stream(params).filter(item -> item.getClass().equals(include.getClass())).findFirst().get();
            key.append(String.join("_", sendInclude));
        }

        return key.toString();
    }
}
