package ua.artcode.dao;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.artcode.exception.AppException;
import ua.artcode.exception.AppValidationException;
import ua.artcode.exception.NoSuchTaskException;
import ua.artcode.model.codingbat.CodingBatTask;
import ua.artcode.model.codingbat.MethodSignature;
import ua.artcode.model.codingbat.TaskTestData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/app-context.xml")
public class CodingBatTaskMongoImplTest {

    private static final Logger LOG = Logger.getLogger(CodingBatTaskMongoImplTest.class);


    @Autowired
    @Qualifier("сodingBatTaskMongoTestImpl")
    private CodingBatTaskDao codingBatTaskDao;

    @Autowired
    @Qualifier("testStore")
    private  Datastore datastore;

    @Value("mongo.test.db")
    private String nameOfTestDb;


    private static final int AMOUNT_OF_ELEMENTS = 100;


    @Before
    public void initializeDB() throws InterruptedException, AppValidationException {
        //String mongoDataPath = AppPropertiesHolder.getProperty("mongo.data.db.path");
        //datastore = (Datastore) context.getBean("testStore");
        //codingBatTaskDao = new CodingBatTaskDaoMongoImpl(datastore);
        try {
            Process process = Runtime.getRuntime().exec("mongod --dbpath /Users/johnsmith/Mongodb/data/db");
            //LOG.warn((getData(process.getInputStream())));
            process.waitFor();
        } catch (IOException e) {
            LOG.error(e);
        }
        String value;
        for (int i = 0; i < AMOUNT_OF_ELEMENTS; i++) {
            value = Integer.toString(i);
            CodingBatTask task = new CodingBatTask("p1000".concat(value), "Title-".concat(value), "Simple description-".concat(value),
                    "methodName(true, false) → false",
                    "public boolean $ome_Method(int arg".concat(value) + ", String arg".concat(value) + ", boolean arg".concat(value) + ") {}", "Group-".concat(value));

            MethodSignature methodSignature = new MethodSignature();
            methodSignature.setReturnType("boolean");
            task.setMethodSignature(methodSignature);


            List<String> inData = new ArrayList<String>();
            inData.add("00".concat(value));
            inData.add("some string");
            inData.add("false");

            String expectedValue = "true";

            TaskTestData taskTestData = new TaskTestData(expectedValue, inData);

            task.getTaskTestDataContainer().addTaskTestData(taskTestData);
            task.getTaskTestDataContainer().addTaskTestData(taskTestData);
            task.getTaskTestDataContainer().addTaskTestData(taskTestData);
            codingBatTaskDao.addTask(task);
        }
    }

    @Test
    public void findByIdTest() throws AppException {
        CodingBatTask task = codingBatTaskDao.findById("p10009");
        assertEquals(task.getCodingBatId(), "p10009");
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

    @Ignore
    @Test
    public void sizeTest() {
        int sizeOfdb = codingBatTaskDao.size();
        assertEquals(sizeOfdb, AMOUNT_OF_ELEMENTS);
    }

    @Test
    public void updateTest() throws AppException {
        CodingBatTask newTask = codingBatTaskDao.findById("p100025");
        CodingBatTask taskToUpdate = codingBatTaskDao.findById("p100017");
        codingBatTaskDao.update("p100017", newTask);
        assertEquals(taskToUpdate.getCodingBatId(), codingBatTaskDao.findById("p100017").getCodingBatId());
        taskToUpdate.setCodingBatId("p1000".concat(String.valueOf(AMOUNT_OF_ELEMENTS + 1)));
        codingBatTaskDao.addTask(taskToUpdate);
    }

    @Test
    public void removeTest() throws AppException {
        CodingBatTask task = codingBatTaskDao.findById("p10005");
        task.setCodingBatId("p666666");
        codingBatTaskDao.addTask(task);
        int sizeBeforeRemove = codingBatTaskDao.size();
        codingBatTaskDao.delete("p666666");
        int sizeAfterDel = codingBatTaskDao.size();
        assertEquals(sizeBeforeRemove, sizeAfterDel + 1);
    }

    @Test
    public void invalidRemoveTest() throws AppValidationException {
        assertFalse(codingBatTaskDao.delete(""));
    }

    @Test
    public void isExistTest() throws AppValidationException {
        assertTrue(codingBatTaskDao.isExist("p10000"));
    }

    @Test
    public void isExistNegativeTest() {
        assertFalse(codingBatTaskDao.isExist("p0"));
    }

    @After
    public void deleteDb() {
        //datastore.getMongo().dropDatabase(nameOfTestDb);
    }

}
