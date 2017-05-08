package swp_impl_acr.quizapy.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectionUtils {

    public static <I> List<I> generateRandomList(List<I> list, int size) {
        if (list.size() <= size) {
            return list;
        }
        List<I> newList = new ArrayList<>();
        Random rand = new Random();
        while (newList.size() < size) {
            I item = list.get(rand.nextInt(list.size()));
            if (!newList.contains(item)) {
                newList.add(item);
            }
        }
        return newList;
    }

}
