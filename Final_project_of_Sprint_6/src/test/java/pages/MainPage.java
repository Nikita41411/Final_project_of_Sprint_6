package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;

    /*
     Список элементов главной страницы (для поиска локаторов):
     - Кнопка «Заказать» (верхняя)
     - Кнопка «Заказать» (нижняя)
     - Блок «Вопросы о важном» — список вопросов (стрелочки-аккордеоны)
     - Блок «Вопросы о важном» — текст ответов
     - Логотип «Самокат»
     - Логотип «Яндекс»
     - Поле ввода номера заказа
     - Кнопка поиска заказа "Go!"
     - Сообщение «Не найдено» на странице статуса заказа
    */

    // Кнопки «Заказать» - простые и надежные локаторы
    private final By orderButtonTop = By.xpath("(.//button[text()='Заказать'])[1]");
    private final By orderButtonBottom = By.xpath("(.//button[text()='Заказать'])[2]");

    // Вопросы о важном
    private final By questionButton(int index) {
        return By.id("accordion__heading-" + index);
    }

    private final By answerText(int index) {
        return By.xpath(".//div[@id='accordion__panel-" + index + "']/p");
    }

    // Логотипы
    private final By scooterLogo = By.className("Header_LogoScooter__3lsAR");
    private final By yandexLogo = By.className("Header_LogoYandex__3TSOI");

    // Поле ввода номера заказа и кнопка поиска
    private final By orderNumberInput = By.xpath(".//input[@placeholder='Введите номер заказа']");
    private final By searchOrderButton = By.xpath(".//button[text()='Go!']");
    private final By notFoundOrderMessage = By.xpath(".//div[contains(text(), 'Не найдено')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOrderButtonTop() {
        WebElement button = driver.findElement(orderButtonTop);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public void clickOrderButtonBottom() {
        WebElement button = driver.findElement(orderButtonBottom);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public By getOrderButtonBottomLocator() {
        return orderButtonBottom;
    }

    public void clickQuestion(int index) {
        WebElement question = driver.findElement(questionButton(index));

        // Прокручиваем к элементу
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);

        // Ждем, пока элемент станет кликабельным
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(question));

        // Используем JavaScript для клика, чтобы избежать перекрытия
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", question);

        // Ждем немного после клика
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getAnswerText(int index) {
        try {
            // Ждем, пока ответ станет видимым
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(answerText(index)));
            return driver.findElement(answerText(index)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickScooterLogo() {
        driver.findElement(scooterLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();
    }

    // ДОБАВЛЕННЫЕ МЕТОДЫ ДЛЯ ДОПОЛНИТЕЛЬНЫХ ТЕСТОВ

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void searchOrder(String orderNumber) {
        WebElement input = driver.findElement(orderNumberInput);
        input.clear();
        input.sendKeys(orderNumber);
        driver.findElement(searchOrderButton).click();
    }

    public boolean isOrderNotFound() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(notFoundOrderMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}