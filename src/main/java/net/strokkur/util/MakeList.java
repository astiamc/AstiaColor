package net.strokkur.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MakeList {

    public static <T> List<T> of(T... objects) {
        return Arrays.stream(objects).collect(Collectors.toList());
    }

}
