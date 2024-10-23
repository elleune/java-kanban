import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;
import ru.yandex.practicum.service.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private FileBackedTaskManager fileBackedTaskManager;
    private File file;

    @BeforeEach
    public void BeforeEach() throws IOException {
        File file = File.createTempFile("test", "csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldWorkFromEmptyFile() throws IOException {

        File testFile = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(new File(testFile.getPath()));

        BufferedReader br = new BufferedReader(new FileReader(testFile));
        int currentId = 0;
        while (br.ready()) {
            currentId++;
            return;
        }
        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(currentId, 0);
    }

    @Test
    public void shouldLoadFromEmptyFile() throws IOException {
        //prepare
        File testFile = File.createTempFile("test", ".csv");

        //do
        TaskManager testTaskManager = fileBackedTaskManager.loadFromFile(new File(String.valueOf(testFile)));
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        int i = 0;
        while (br.ready()) {
            i++;
            return;
        }

        //check
        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(i, 0);
    }

    @Test
    public void shouldSaveInFileWhenAddUpdateDeleteAndDeleteAll() throws IOException {
        //prepare
        File testFile = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(new File(testFile.getPath()));
        Task task = new Task(TaskStatus.NEW, 0);
        Task task2 = new Task(TaskStatus.NEW, 0);
        Epic epic = new Epic();
        Subtask subtask = new Subtask(0, TaskStatus.NEW, epic.getId());

        //do
        taskManager.updateTask(task);
        taskManager.updateTask(task2);
        taskManager.updateEpic(epic);
        taskManager.updateSubtask(subtask);

        BufferedReader br = new BufferedReader(new FileReader(testFile));
        int i = 0;
        while (br.ready()) {
            i++;
            return;
        }


        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(i, 5);

        Subtask subtask2 = new Subtask(subtask.getId(),
                TaskStatus.DONE, epic.getId());
        taskManager.updateSubtask(subtask2);
        int epicID = subtask2.getEpicId();
        Epic updateEpic = taskManager.getEpicById(epicID);


        assertEquals(epic, updateEpic, "Эпик не обновился.");
        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(i, 5);


        taskManager.deleteTaskById(task.getId());


        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(i, 4);


        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();


        assertTrue(testFile.canRead() && testFile.exists());
        assertEquals(i, 1);
    }

    @Test
    void save_shouldThrowsExceptionLoadWrongFileA() {
        assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(new File("file")));
    }

    @BeforeEach
    void initFile() {
        file = null;
        try {
            file = java.io.File.createTempFile("backup", "csv");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        fileBackedTaskManager = Managers.getFileBackedTaskManager(file);
        taskManager = fileBackedTaskManager;
    }

    @Test
    void save_shouldThrowsExceptionLoadWrongFile() {
        assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(new File("file")));
    }

    @Test
    public void testException() {
        taskManager.file = new File("invalid", "tasks.csv");
        taskManager = new FileBackedTaskManager(taskManager.file);
        assertThrows(ManagerSaveException.class, () -> {
            Task task = new Task();
            taskManager.createTask(task);
        });
    }

    @Test
    public void loadFromFile() throws Exception {
        File file = File.createTempFile("test", "csv");
        Task task1 = new Task();
        Epic epic3 = new Epic();
        taskManager.createTask(task1);
        taskManager.createEpic(epic3);
        Subtask subtask5 = new Subtask(epic3.getId());
        taskManager.createSubtask(subtask5);
        taskManager.updateEpic(epic3);
        taskManager.updateTask(task1);
        taskManager.updateSubtask(subtask5);

        taskManager = FileBackedTaskManager.loadFromFile(file);

        assertNotNull(taskManager.getTasks(), "Список задач пуст");
        assertNotNull(taskManager.getEpics(), "Список эпиков пуст");
        assertNotNull(taskManager.getSubtasks(), "Список сабтаск пуст");
        assertNotNull(taskManager.getHistory(), "История пуста");
    }

}








