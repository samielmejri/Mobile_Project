package com.example.user_module.ViewModal;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.user_module.AppDatabase;
import com.example.user_module.Dao.ForumDao;
import com.example.user_module.entity.Forum;

import java.util.List;

public class ForumViewModel extends AndroidViewModel {
    private ForumDao forumDao;
    private LiveData<List<Forum>> allForums;

    public ForumViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        forumDao = database.forumDao();
        allForums = forumDao.getAllForums();
    }

    public void insert(Forum forum) {
        new Thread(() -> forumDao.insert(forum)).start();
    }

    public void update(Forum forum) {
        new Thread(() -> forumDao.update(forum)).start();
    }

    public void delete(Forum forum) {
        new Thread(() -> forumDao.delete(forum)).start();
    }

    public LiveData<List<Forum>> getAllForums() {
        return allForums;
    }
}