package ua.artcode.script;

import ua.artcode.exception.AppException;

import java.io.IOException;

public class RunInitCodingBatTaskTrigger {

    public static void main(String[] args) throws AppException, IOException, InterruptedException {
//        ApplicationContext context = SpringContext.getContext();
//        Datastore datastore = (Datastore) context.getBean("datastore");
//        CourseDao courseDao=new CourseDaoImpl(datastore);
//        courseDao.addCourse(new Course("artCode",new ArrayList<Lesson>()));
        //TaskRunFacade taskRunFacade = new TaskRunFacade();
        //taskRunFacade.runTask();
//         InitCodingBatTaskTrigger.loadTasksIfNeeded();
//        InitCodingBatTaskTrigger.loadTasksToDataBase();
//        ApplicationContext context = SpringContext.getContext();
        //  Morphia morphia = context.getBean(Morphia.class);
//        morphia.map(CodingBatTask.class);
//        Datastore datastore = (Datastore) context.getBean("datastore");
//        CodingBatTaskDao codingBatTaskDao = new CodingBatTaskDaoMongoImpl(datastore);
//        CodingBatTask task = codingBatTaskDao.findById("p187868");
//        TaskRunFacade taskRunFacade = context.getBean(TaskRunFacade.class);
//        taskRunFacade.runTask(task, task.getTemplate().substring(0, task.getTemplate().length() - 1) + "return(!weekday||vacation);\n}");
//         InitCodingBatTaskTrigger.loadTasksIfNeeded();
          //InitCodingBatTaskTrigger.loadTasksToDataBase();
//        InitCodingBatTaskTrigger.createDumpOfDataBase();

        InitCodingBatTaskTrigger.restoreDataBaseFromDump();

//        UserDao userDao = new UserDaoMongoImpl(datastore);
//        userDao.add(new User("Razer","000000","chernyshov.dev@gmail.com", UserType.ROLE_ADMIN));
//        userDao.add(new User("Maxim","111111","obonemax@gmail.com", UserType.ROLE_ADMIN));
//        InitCodingBatTaskTrigger.createDumpOfDataBase();
//        CodingBatTask codingBatTask = new CodingBatTask();
//        codingBatTask.setCodingBatId("00000");
//        codingBatTaskDao.addTask(codingBatTask);
//        CodingBatTask c2 = new CodingBatTask();
//        c2.setCodingBatId("5555");
//        System.out.println(codingBatTaskDao.isExist(codingBatTask));

    }
}


