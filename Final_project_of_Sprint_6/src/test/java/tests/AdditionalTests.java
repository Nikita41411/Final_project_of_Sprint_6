package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.MainPage;
import pages.OrderPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdditionalTests {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://qa-scooter.praktikum-services.ru/");
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

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}