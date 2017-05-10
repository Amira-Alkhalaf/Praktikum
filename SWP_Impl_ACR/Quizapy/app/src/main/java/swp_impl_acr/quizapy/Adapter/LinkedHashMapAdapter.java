package swp_impl_acr.quizapy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.R;

public class LinkedHashMapAdapter extends BaseAdapter{
    private LinkedHashMap<Question, Answer> data;
    private Question[] positions;
    private Context context;

    public LinkedHashMapAdapter(Context context, LinkedHashMap<Question, Answer> data){
        this.context = context;
        this.data=data;
        positions = data.keySet().toArray(new Question[0]);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Question getItem(int position) {
        return positions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

        holder.userAnswer.setText(data.get(getItem(position)).getName());
        holder.questionName.setText(getItem(position).getName());
        //holder.questionName.setText(getItem(position).getId()+". Frage");
        try{
            QuestionDataSource qDS = new QuestionDataSource();
            holder.correctAnswer.setText(qDS.getCorrectAnswer(getItem(position).getId()).getName());
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
