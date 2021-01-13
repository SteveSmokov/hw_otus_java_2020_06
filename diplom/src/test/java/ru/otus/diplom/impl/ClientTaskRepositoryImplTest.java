package ru.otus.diplom.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import ru.otus.diplom.config.Client1DataSourceConfig;
import ru.otus.diplom.enums.Departament;
import ru.otus.diplom.enums.TaskPriority;
import ru.otus.diplom.models.ClientFile;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.properties.Client1DataSourceProp;
import ru.otus.diplom.repositories.impl.ClientTaskRepositoryImpl;
import ru.otus.diplom.services.impl.CipherServiceImpl;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@SpringBootTest(classes = {Client1DataSourceProp.class, CipherServiceImpl.class, ClientTaskRepositoryImpl.class, Client1DataSourceConfig.class})
@PropertySource("classpath:application.properties")
@DisplayName("Тестирование компонента для работы с клиентской БД")
@Disabled
class ClientTaskRepositoryImplTest {

    @Autowired
    private Client1DataSourceProp client1DataSourceProperties;

    @Autowired
    @Qualifier("ClientConnectionPool")
    private DataSource dataSource;

    @Autowired
    private ClientTaskRepositoryImpl clientTaskRepository;

    @Test
    @DisplayName("Запрос списка заявок с клиентской БД ")
    void getEISTasks() {
        List<Task> tasks = clientTaskRepository.getTasks();
        for (Task task : tasks
        ) {
            log.info("Task Id - {}; Title - \"{}\"; Description - \"{}\"", task.getId(), task.getTitle(), task.getText());
        }
        Assertions.assertTrue(tasks != null);
    }

    @Test
    @DisplayName("Запрос заявки с клиентской БД по Id")
    void getEISTaskById() {
        List<ClientFile> tasks = clientTaskRepository.getFileListByTaskID(2L);
        log.info(String.valueOf(tasks));
    }

    @Test
    @DisplayName("Запрос заявки с клиентской БД по Id")
    void getClientFileById() {
        ClientFile file = clientTaskRepository.getFileByID(4L);
        Assertions.assertTrue(((file != null) && (file.getFile_blob() != null)));
        log.info(String.valueOf(file));
    }


    @Test
    @DisplayName("Редактирование клиентской заявки")
    void updateClientTask() {
        boolean result = clientTaskRepository.updateTask(1L,
                Departament.DEVELOPMENT.getValue(),
                TaskPriority.High.getValue(),
                "Детальное описание, вариант 3");
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Загрузка файла в клиентскую БД")
    void updateClientFile() throws FileNotFoundException {
        File initialFile = new File("D:\\Downloads_history\\mergeStreams_1.pdf");
        ClientFile clientFile = ClientFile.builder().id(6L)
                .tsk_id(2L)
                .file_name("test_pdf")
                .file_blob(new FileInputStream(initialFile))
                .build();
        boolean result = clientTaskRepository.updateClientFile(clientFile);
        Assertions.assertTrue(result);
    }
}