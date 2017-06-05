package swp_impl_acr.quizapy.Helper;

import android.support.annotation.NonNull;
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

/**
 * Class for parsing a JSON list of questions
 */
public class ImportParser {

    /**
     * parses a JSON containing questions and persists them to the database
     * returns true for a reason i dont remember
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static boolean parseQuestionJSON(InputStream in, int schema) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Topic> topics = new ArrayList<>();
        switch(schema){
            case 0: topics = parseJSONOld(reader);
                break;
            case 1: topics = parseJSONNew(reader);
                break;
            default:
        }

        persistToDatabase(topics);

        return true;
    }

    @NonNull
    private static List<Topic> parseJSONNew(JsonReader reader) throws IOException {
        List<Topic> topics = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("Categories")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Topic topic = parseTopicNew(reader);
                        topics.add(topic);
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }  finally {
            reader.close();
        }
        return topics;
    }

    @NonNull
    private static Topic parseTopicNew(JsonReader reader) throws IOException {
        reader.beginObject();
        Topic topic = new Topic();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("CategoryName")){
                topic.setName(reader.nextString());
            } else if(name.equals("Level1")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name2 = reader.nextName();
                    if (name2.equals("Questions")) {
                        reader.beginArray();
                        while(reader.hasNext()){
                            Question question = parseQuestionNew(reader, 1);
                            topic.addQuestion(question);
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if(name.equals("Level2")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name2 = reader.nextName();
                    if (name2.equals("Questions")) {
                        reader.beginArray();
                        while(reader.hasNext()){
                            Question question = parseQuestionNew(reader, 2);
                            topic.addQuestion(question);
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if(name.equals("Level3")){
                reader.beginObject();
                while (reader.hasNext()) {
                    String name2 = reader.nextName();
                    if(name2.equals("Questions")) {
                        reader.beginArray();
                        while(reader.hasNext()){
                            Question question = parseQuestionNew(reader, 3);
                            topic.addQuestion(question);
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return topic;
    }
    @NonNull
    private static Question parseQuestionNew(JsonReader reader, int level) throws IOException {
        reader.beginObject();
        Question question = new Question();
        question.setDifficulty(level);
        while(reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("Question")){
                reader.beginObject();
                while(reader.hasNext()){
                    String name2 = reader.nextName();
                    if(name2.equals("QuestionText")){
                        question.setName(reader.nextString());
                    } else if(name2.equals("RightAnswerText")){
                        Answer answer = parseAnswerNew(reader, true);
                        question.addAnswer(answer);
                    } else if(name2.equals("WrongAnswersText")){
                        reader.beginArray();
                        while (reader.hasNext()) {
                            Answer answer = parseAnswerNew(reader, false);
                            question.addAnswer(answer);
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
        }
        reader.endObject();
        return question;
    }

    @NonNull
    private static Answer parseAnswerNew(JsonReader reader, boolean correctAnswer) throws IOException {
        Answer answer = new Answer();
        answer.setCorrectAnswer(correctAnswer);
        answer.setName(reader.nextString());
        return answer;
    }

    @NonNull
    private static List<Topic> parseJSONOld(JsonReader reader) throws IOException {
        List<Topic> topics = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("topics")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Topic topic = parseTopicOld(reader);
                        topics.add(topic);
                    }
                    reader.endArray();
                }
            }
            reader.endObject();

        } finally {
            reader.close();
        }
        return topics;
    }

    /**
     * parses the topic part of the json
     * @param reader
     * @return
     * @throws IOException
     */
    @NonNull
    private static Topic parseTopicOld(JsonReader reader) throws IOException {
        reader.beginObject();
        Topic topic = new Topic();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                topic.setId(reader.nextInt());
            } else if (name.equals("name")) {
                topic.setName(reader.nextString());
            } else if (name.equals("questions")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Question question = parseQuestionOld(reader);
                    topic.addQuestion(question);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return topic;
    }

    /**
     * parses the question part of the json
     * @param reader
     * @return
     * @throws IOException
     */
    @NonNull
    private static Question parseQuestionOld(JsonReader reader) throws IOException {
        reader.beginObject();
        Question question = new Question();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                question.setId(reader.nextInt());
            } else if (name.equals("name")) {
                question.setName(reader.nextString());
            } else if (name.equals("difficulty")) {
                question.setDifficulty(reader.nextInt());
            } else if (name.equals("answers")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Answer answer = parseAnswerOld(reader);
                    question.addAnswer(answer);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return question;
    }

    /**
     * parses the answer part of the question
     * @param reader
     * @return
     * @throws IOException
     */
    @NonNull
    private static Answer parseAnswerOld(JsonReader reader) throws IOException {
        reader.beginObject();
        Answer answer = new Answer();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                answer.setId(reader.nextInt());
            } else if (name.equals("name")) {
                answer.setName(reader.nextString());
            } else if (name.equals("correctAnswer")) {
                answer.setCorrectAnswer(reader.nextBoolean());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return answer;
    }

    /**
     * saves everything to the database
     * @param topics
     */
    private static void persistToDatabase(List<Topic> topics) {
        try {
            AnswerDataSource answerDataSource = new AnswerDataSource();
            QuestionDataSource questionDataSource = new QuestionDataSource();
            TopicDataSource topicDataSource = new TopicDataSource();
            for (Topic topic:topics) {
                Topic topicWithId = topicDataSource.saveTopic(topic);
                for (Question question:topic.getQuestions()) {
                    question.setTopic(topicWithId);
                    Question questionWithId = questionDataSource.saveQuestion(question);
                    for (Answer answer:question.getAnswers(-1)) {
                        answer.setQuestion(questionWithId);
                        answerDataSource.saveAnswer(answer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
