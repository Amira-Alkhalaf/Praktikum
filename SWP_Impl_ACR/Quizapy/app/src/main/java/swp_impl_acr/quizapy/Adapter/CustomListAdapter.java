package swp_impl_acr.quizapy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.R;

public class CustomListAdapter extends ArrayAdapter<Answer> {
    private ArrayList<Answer> answers;
    private Context context;

    public CustomListAdapter(Context context, ArrayList<Answer> answers){
        super(context, R.layout.list_item_3, answers);
        this.context = context;
        this.answers=answers;
    }


//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Question getItem(int position) {
//        return positions[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_3, parent, false);
            holder.userAnswer = (TextView) convertView.findViewById(R.id.centreTextView);
            holder.questionName = (TextView) convertView.findViewById(R.id.leftTextView);
            holder.correctAnswer = (TextView) convertView.findViewById(R.id.rightTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Answer answer = getItem(position);
        Question question = answer.getQuestion();

        holder.userAnswer.setText(answer.getName());
        holder.questionName.setText(question.getName());
        //holder.questionName.setText(getItem(position).getId()+". Frage");
        try{
            QuestionDataSource qDS = new QuestionDataSource();
            holder.correctAnswer.setText(qDS.getCorrectAnswer(question.getId()).getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView userAnswer;
        TextView questionName;
        TextView correctAnswer;

    }
}
