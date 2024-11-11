package com.example.user_module;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.user_module.Dao.CourseDao;
import com.example.user_module.Dao.ForumDao;
import com.example.user_module.Dao.ProgramDao;
import com.example.user_module.Dao.QuestionDao;
import com.example.user_module.Dao.QuizDao;
import com.example.user_module.Dao.TestDao;
import com.example.user_module.Dao.UserDao;
import com.example.user_module.entity.Course;
import com.example.user_module.entity.Forum;
import com.example.user_module.entity.Post;
import com.example.user_module.entity.Program;
import com.example.user_module.entity.Question;
import com.example.user_module.entity.Quiz;
import com.example.user_module.entity.Test;
import com.example.user_module.entity.User;

@Database(entities = {User.class, Forum.class, Post.class, Program.class, Course.class, Test.class, Quiz.class, Question.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract ForumDao forumDao();
    public abstract ProgramDao programDao();
    public abstract CourseDao courseDao();
    public abstract TestDao testDao();
    public abstract QuizDao quizDao();
    public abstract QuestionDao questionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "mobileApp.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
