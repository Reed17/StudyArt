package ua.artcode.dao;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.springframework.context.ApplicationContext;
import ua.artcode.exception.AppException;
import ua.artcode.exception.NoSuchTaskException;
import ua.artcode.model.codingbat.CodingBatTask;
import ua.artcode.utils.SpringContext;
import ua.artcode.utils.io.AppPropertiesHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.artcode.script.InitCodingBatTaskTrigger.getData;

/**
 * Created by Razer on 14.12.15.
 */
public class CodingBatTaskMongoImplTest {
    private static final Logger LOG = Logger.getLogger(CodingBatTaskMongoImplTest.class);
    private static CodingBatTaskDao codingBatTaskDao;
    private static ApplicationContext context;
    private static Datastore datastore;
    private static final int AMOUNT_OF_ELEMENTS = 1000;

    @BeforeClass
    public static void initializeDB() throws InterruptedException {
        context = SpringContext.getContext();
        String mongoDataPath = AppPropertiesHolder.getProperty("mongo.data.db.path");
        try {
            //TODO show commandline result of start server
            Process process = Runtime.getRuntime().exec("mongod --dbpath " + mongoDataPath);
            LOG.debug((getData(process.getErrorStream())));
            process.waitFor();
        } catch (IOException e) {
            LOG.error(e);
        }
        datastore = (Datastore) context.getBean("testStore");
        codingBatTaskDao = new CodingBatTaskDaoMongoImpl(datastore);
        String value;
        for (int i = 0; i < AMOUNT_OF_ELEMENTS; i++) {
            value = Integer.toString(i);
            codingBatTaskDao.addTask(new CodingBatTask("1000".concat(value), value, value, value, value, value));
        }
    }

    @Test
    public void findByIdTest() {
        CodingBatTask task = null;
        CodingBatTask taskTofind = new CodingBatTask("3247", "0", "0", "0", "0", "0");
        codingBatTaskDao.addTask(taskTofind);

        try {
            task = codingBatTaskDao.findById("3247");
        } catch (NoSuchTaskException e) {
            LOG.error(e);
        }
        assertEquals(taskTofind.getId(), task.getId());
        codingBatTaskDao.delete("3247");


    }

    @Test(expected = NoSuchTaskException.class)
    public void findByIdExceptionTest() throws NoSuchTaskException {
        codingBatTaskDao.findById(" ");
    }

    @Test
    public void getAllTest() {
        List<CodingBatTask> codingBatTasks = null;
        try {
            codingBatTasks = codingBatTaskDao.getAll();
        } catch (AppException e) {
            LOG.error(e);
        }
        int sizeOfList = codingBatTasks.size();
        int sizeOfDb = codingBatTaskDao.size();
        assertEquals(sizeOfDb, sizeOfList);
    }

    @Test
    public void sizeTest() {
        int sizeOfdb = codingBatTaskDao.size();
        assertEquals(sizeOfdb, AMOUNT_OF_ELEMENTS);
    }

    @Test
    public void removeTest() {
        codingBatTaskDao.addTask(new CodingBatTask("123785", "1", "1", "1", "1", "1"));
        int sizeBeforeRemove = codingBatTaskDao.size();
        codingBatTaskDao.delete("123785");
        int sizeAfterDel = codingBatTaskDao.size();
        assertEquals(sizeBeforeRemove, sizeAfterDel + 1);
    }

    @Test
    public void invalidRemoveTest() {
        codingBatTaskDao.addTask(new CodingBatTask("45678", "1", "1", "1", "1", "1"));
        int sizeBeforeRemove = codingBatTaskDao.size();
        codingBatTaskDao.delete("");
        int sizeAfterDel = codingBatTaskDao.size();
        assertEquals(sizeBeforeRemove, sizeAfterDel);
        codingBatTaskDao.delete("45678");
    }

    @Test
    public void isExistTest() {
        CodingBatTask codingBatTask = new CodingBatTask();
        codingBatTask.setCodingBatId("1771");
        codingBatTaskDao.addTask(codingBatTask);
        boolean resultOfExist = codingBatTaskDao.isExist(codingBatTask);
        assertTrue(resultOfExist);
        codingBatTaskDao.delete("1771");
    }

    @AfterClass
    public static void deleteDb() {
        String nameOfTestDb = AppPropertiesHolder.getProperty("mongo.test.db");
        datastore.getMongo().dropDatabase(nameOfTestDb);
    }

}