package com.zkhaider.bernievshillary.utils;

import java.util.List;

/**
 * Created by ZkHaider on 6/5/16.
 */

public class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

}
