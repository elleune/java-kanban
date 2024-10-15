import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.service.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DisplayName("Менеджеры")
public class ManagerTest {

    @Test
    @DisplayName("Программа должна корректно собираться")
    public void shouldCorrectlyAssembleProgramm() {
        Assertions.assertNotNull(Managers.getDefault());
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}