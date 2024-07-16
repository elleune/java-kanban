import org.junit.jupiter.api.Test;
import ru.yandex.practicum.service.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ManagersTest {
    @Test
    void testGetDefault() {
        assertNotNull(Managers.getDefault(), "Менеджер по умолчанию не должен иметь значения ноль");
    }

    @Test
    void testGetDefaultHistory() {

        assertNotNull(Managers.getDefaultHistory(), "Значение диспетчера истории по умолчанию не должно быть равно нулю");
    }
}