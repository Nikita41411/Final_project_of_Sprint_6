package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.OrderPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdditionalTests extends BaseTest {
    private MainPage mainPage;
    private OrderPage orderPage;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @Test
    public void testFormValidationErrors() {
        System.out.println("=== ТЕСТ ВАЛИДАЦИИ ФОРМЫ ===");

        mainPage.clickOrderButtonTop();

        orderPage.clearNameField();
        orderPage.clickNextButton();

        // Проверяем, что ошибка валидации отображается
        boolean isErrorDisplayed = orderPage.isValidationErrorDisplayed();
        System.out.println("Ошибка валидации отображается: " + isErrorDisplayed);

        assertTrue(isErrorDisplayed, "Ошибка валидации для пустого имени не отображается");
    }

    @Test
    public void testCompleteOrderWithConfirmation() {
        System.out.println("=== ТЕСТ ПОЛНОГО ЗАКАЗА С ПОДТВЕРЖДЕНИЕМ ===");

        String name = "Лися";
        String surname = "Камушкин";
        String address = "ул. Лунтика, д. 10";
        String metro = "Лубянка";
        String phone = "+79008887766";
        String date = "29.11.2025";
        String period = "трое суток";
        String color = "black";
        String comment = "Позвонить за час";

        mainPage.clickOrderButtonTop();
        orderPage.fillFirstPage(name, surname, address, metro, phone);
        orderPage.fillSecondPage(date, period, color, comment);

        orderPage.confirmOrder();

        boolean isSuccess = orderPage.isOrderSuccess();
        System.out.println("Заказ успешно оформлен: " + isSuccess);

        assertTrue(isSuccess, "Сообщение 'Заказ оформлен' не появилось после подтверждения");
    }
}