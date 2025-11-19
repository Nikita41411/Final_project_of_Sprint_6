package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import pages.MainPage;
import pages.OrderPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest extends BaseTest {
    private MainPage mainPage;
    private OrderPage orderPage;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                Arguments.of("Иван", "Иванов", "ул. Ленина, д. 1", "Сокольники", "+79991234567", "25.11.2025", "сутки", "black", "Тестовый комментарий"),
                Arguments.of("Мария", "Петрова", "пр. Мира, д. 15", "Черкизовская", "+79997654321", "26.11.2025", "двое суток", "grey", "Другой комментарий")
        );
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromTopButton(String name, String surname, String address, String metro,
                                       String phone, String date, String period, String color, String comment) {
        System.out.println("=== ТЕСТ С ВЕРХНЕЙ КНОПКИ ===");
        System.out.println("Данные: " + name + " " + surname + ", " + address + ", " + metro);
        mainPage.clickOrderButtonTop();
        completeOrder(name, surname, address, metro, phone, date, period, color, comment);
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFromBottomButton(String name, String surname, String address, String metro,
                                          String phone, String date, String period, String color, String comment) {
        System.out.println("=== ТЕСТ С НИЖНЕЙ КНОПКИ ===");
        System.out.println("Данные: " + name + " " + surname + ", " + address + ", " + metro);
        mainPage.clickOrderButtonBottom();
        completeOrder(name, surname, address, metro, phone, date, period, color, comment);
    }

    private void completeOrder(String name, String surname, String address, String metro,
                               String phone, String date, String period, String color, String comment) {
        orderPage.fillFirstPage(name, surname, address, metro, phone);
        orderPage.fillSecondPage(date, period, color, comment);

        boolean isConfirmationModalDisplayed = orderPage.isConfirmationModalDisplayed();
        System.out.println("Окно подтверждения 'Хотите оформить заказ?' отображается: " + isConfirmationModalDisplayed);
        assertTrue(isConfirmationModalDisplayed, "Окно подтверждения заказа не отображается");

        orderPage.confirmOrder();

        boolean isSuccess = orderPage.isOrderSuccess();
        System.out.println("Результат заказа: " + (isSuccess ? "УСПЕХ" : "НЕУДАЧА"));

        assertTrue(isSuccess, "Заказ не был создан успешно");
    }

    @Test
    public void testFormValidationErrors() {
        System.out.println("=== ТЕСТ ВАЛИДАЦИИ ФОРМЫ ===");

        mainPage.clickOrderButtonTop();
        orderPage.clearNameField();
        orderPage.clickNextButton();

        boolean isErrorDisplayed = orderPage.isValidationErrorDisplayed();
        System.out.println("Ошибка валидации отображается: " + isErrorDisplayed);

        assertTrue(isErrorDisplayed, "Ошибка валидации для пустого имени не отображается");
    }
}