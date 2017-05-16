package swp_impl_acr.quizapy.Helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * contains methods connected to Collections
 */
public class CollectionUtils {

    /**
     * returns a (smaller) randomized List
     *
     * @param list
     * @param size size of the desired list
     * @param <I>
     * @return
     */
    public static <I> List<I> generateRandomList(List<I> list, int size) {
        if (list.size() < size) {
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
