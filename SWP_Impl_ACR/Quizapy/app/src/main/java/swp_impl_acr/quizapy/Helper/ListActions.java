package swp_impl_acr.quizapy.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import swp_impl_acr.quizapy.Database.Entity.Question;

public class ListActions {

    public static List generateRandomQuestionList(List<Question> list, int size){
        if(list.size()<=size){
            return list;
        }
        List<Question> newList = new ArrayList<>();
        Random rand = new Random();
        while(newList.size()<size){
            Question question = list.get(rand.nextInt(list.size()));
            if(!newList.contains(question)){
                newList.add(question);
            }
        }
        return newList;
    }
}
