package ua.artcode.dao;

import ua.artcode.db.DataBaseManager;
import ua.artcode.exception.AppException;
import ua.artcode.exception.NoSuchTaskException;
import ua.artcode.model.codingbat.CodingBatTask;

import java.util.List;

/**
 * Created by Razer on 09.11.15.
 */
public class SimpleTaskDaoMongoImpl implements SimpleTaskDao {
    private DataBaseManager dataBaseManager;

    public SimpleTaskDaoMongoImpl(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    @Override
    public CodingBatTask create(CodingBatTask task) {
        dataBaseManager.addTask(task);
        return task;
    }

    @Override
    public CodingBatTask findById(String id) throws NoSuchTaskException {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public int size() {
        return dataBaseManager.size();
    }

    @Override
    public CodingBatTask update(CodingBatTask task) {
        return null;
    }

    @Override
    public List<CodingBatTask> getAll() throws AppException {
        return null;
    }

    @Override
    public List<CodingBatTask> getAllByGroup(String groupName) throws AppException {
        return null;
    }

    @Override
    public CodingBatTask addTask(CodingBatTask codingBatTask) {
        return null;
    }

    @Override
    public List<CodingBatTask> searchByName(String name) {
        return null;
    }

    @Override
    public List<CodingBatTask> searchById(String id) {
        return null;
    }

}
