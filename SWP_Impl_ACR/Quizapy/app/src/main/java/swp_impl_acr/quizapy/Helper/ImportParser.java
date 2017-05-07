package swp_impl_acr.quizapy.Helper;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.AnswerDataSource;
import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.Entity.Topic;

public class ImportParser {

    public static boolean parseQuestionJSON(InputStream in) throws IOException {
        //todo exctract methods
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Topic> topics = new ArrayList<>();
        try {
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if(name.equals("topics")) {
                    reader.beginArray();
                    while(reader.hasNext()){
                        reader.beginObject();
                        Topic topic = new Topic();
                        while(reader.hasNext()){
                            String name2 = reader.nextName();
                            if(name2.equals("id")){
                                topic.setId(reader.nextInt());
                            } else if(name2.equals("name")){
                                topic.setName(reader.nextString());
                            } else if(name2.equals("questions")){
                                reader.beginArray();
                                while(reader.hasNext()){
                                    reader.beginObject();
                                    Question question = new Question();
                                    while(reader.hasNext()){
                                        String name3 = reader.nextName();
                                        if(name3.equals("id")){
                                            question.setId(reader.nextInt());
                                        } else if(name3.equals("name")){
                                            question.setName(reader.nextString());
                                        } else if(name3.equals("difficulty")){
                                            question.setDifficulty(reader.nextInt());
                                        } else if(name3.equals("answers")){
                                            reader.beginArray();
                                            while(reader.hasNext()){
                                                reader.beginObject();
                                                Answer answer = new Answer();
                                                while(reader.hasNext()){
                                                    String name4 = reader.nextName();
                                                    if(name4.equals("id")){
                                                        answer.setId(reader.nextInt());
                                                    } else if(name4.equals("name")){
                                                        answer.setName(reader.nextString());
                                                    } else if(name4.equals("correctAnswer")){
                                                        answer.setCorrectAnswer(reader.nextBoolean());
                                                    } else {
                                                        reader.skipValue();
                                                    }
                                                }
                                                question.addAnswer(answer);
                                                reader.endObject();
                                            }
                                            reader.endArray();
                                        } else {
                                            reader.skipValue();
                                        }
                                    }
                                    topic.addQuestion(question);
                                    reader.endObject();
                                }
                                reader.endArray();
                            } else {
                                reader.skipValue();
                            }
                        }
                        topics.add(topic);
                        reader.endObject();
                    }
                    reader.endArray();
                }
            }
            reader.endObject();

        } finally {
            reader.close();
        }

        persistToDatabase(topics);

        return true;
    }

    private static void persistToDatabase(List<Topic> topics) {
        try {
            AnswerDataSource answerDataSource = new AnswerDataSource();
            QuestionDataSource questionDataSource = new QuestionDataSource();
            TopicDataSource topicDataSource = new TopicDataSource();
            for(Topic topic:topics){
                topicDataSource.saveTopic(topic);
                for(Question question:topic.getQuestions()){
                    question.setTopic(topic.getId());
                    questionDataSource.saveQuestion(question);
                    for(Answer answer:question.getAnswers()){
                        answer.setQuestion(question.getId());
                        answerDataSource.saveAnswer(answer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
