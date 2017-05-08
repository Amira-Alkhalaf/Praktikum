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

public class ImportParser {

    public static boolean parseQuestionJSON(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Topic> topics = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("topics")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Topic topic = parseTopic(reader);
                        topics.add(topic);
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

    @NonNull
    private static Topic parseTopic(JsonReader reader) throws IOException {
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
                    Question question = parseQuestion(reader);
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

    @NonNull
    private static Question parseQuestion(JsonReader reader) throws IOException {
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
                    Answer answer = parseAnswer(reader);
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

    @NonNull
    private static Answer parseAnswer(JsonReader reader) throws IOException {
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

    private static void persistToDatabase(List<Topic> topics) {
        try {
            AnswerDataSource answerDataSource = new AnswerDataSource();
            QuestionDataSource questionDataSource = new QuestionDataSource();
            TopicDataSource topicDataSource = new TopicDataSource();
            for (Topic topic:topics) {
                topicDataSource.saveTopic(topic);
                for (Question question:topic.getQuestions()) {
                    question.setTopic(topic.getId());
                    questionDataSource.saveQuestion(question);
                    for (Answer answer:question.getAnswers()) {
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
