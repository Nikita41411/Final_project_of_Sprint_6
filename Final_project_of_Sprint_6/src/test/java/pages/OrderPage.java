package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;

    private final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    private final By metroDropdown = By.className("select-search__options");
    private final By metroOption = By.xpath(".//div[@class='select-search__option']");

    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.xpath(".//div[@class='Dropdown-option']");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath(".//button[text()='Заказать' and contains(@class, 'Button_Middle')]");

    private final By confirmOrderModal = By.className("Order_Modal__YZ-d3");
    private final By confirmOrderButton = By.xpath(".//button[text()='Да']");
    private final By cancelOrderButton = By.xpath(".//button[text()='Нет']");
    private final By orderConfirmationText = By.xpath(".//div[contains(text(), 'Хотите оформить заказ?')]");

    private final By successModal = By.className("Order_Modal__YZ-d3");
    private final By successMessage = By.xpath(".//div[contains(@class, 'Order_ModalHeader')]");
    private final By successOrderText = By.xpath(".//div[contains(text(), 'Заказ оформлен')]");

    private final By validationError = By.xpath(".//div[contains(@class, 'Input_ErrorMessage')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillFirstPage(String name, String surname, String address, String metro, String phone) {
        System.out.println("Заполняем первую страницу заказа...");

        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);

        fillMetroStation(metro);

        driver.findElement(phoneField).sendKeys(phone);

        System.out.println("Нажимаем кнопку 'Далее'...");
        driver.findElement(nextButton).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(dateField));
        System.out.println("Вторая страница загружена");
    }

    private void fillMetroStation(String metroStation) {
        System.out.println("Выбираем станцию метро: " + metroStation);

        WebElement metroInput = driver.findElement(metroField);

        metroInput.click();

        metroInput.clear();
        metroInput.sendKeys(metroStation);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(metroDropdown));

        List<WebElement> options = driver.findElements(metroOption);
        if (!options.isEmpty()) {
            options.get(0).click();
            System.out.println("Станция метро выбрана");
        } else {
            System.out.println("Станции метро не найдены, используем стрелки");
            metroInput.sendKeys(Keys.ARROW_DOWN);
            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(metroInput));
            metroInput.sendKeys(Keys.ENTER);
        }
    }

    public void fillSecondPage(String date, String period, String color, String comment) {
        System.out.println("Заполняем вторую страницу заказа...");

        WebElement dateInput = driver.findElement(dateField);
        dateInput.clear();
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);
        System.out.println("Дата заполнена: " + date);

        driver.findElement(rentalPeriodField).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(rentalPeriodOptions));

        WebElement periodOption = driver.findElement(By.xpath(".//div[text()='" + period + "']"));
        periodOption.click();
        System.out.println("Период аренды выбран: " + period);

        if ("black".equals(color)) {
            driver.findElement(colorBlack).click();
            System.out.println("Цвет выбран: черный");
        } else if ("grey".equals(color)) {
            driver.findElement(colorGrey).click();
            System.out.println("Цвет выбран: серый");
        }

        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
            System.out.println("Комментарий заполнен: " + comment);
        }

        WebElement orderBtn = driver.findElement(orderButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", orderBtn);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderBtn));

        System.out.println("Нажимаем кнопку 'Заказать'...");
        orderBtn.click();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(confirmOrderModal));
            System.out.println("Модальное окно 'Хотите оформить заказ?' появилось");

        } catch (Exception e) {
            System.out.println("Модальное окно подтверждения НЕ появилось!");
        }
    }

    public boolean isConfirmationModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderConfirmationText));
            return driver.findElement(orderConfirmationText).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmOrder() {
        System.out.println("Подтверждаем заказ в диалоговом окне...");

        String initialText = getModalText();
        System.out.println("Текст модального окна до клика: " + initialText);

        try {
            WebElement yesButton = driver.findElement(confirmOrderButton);

            System.out.println("Пробуем обычный клик...");
            yesButton.click();

            waitForModalChange(initialText);

        } catch (Exception e) {
            System.out.println("Обычный клик не сработал: " + e.getMessage());

            try {
                WebElement yesButton = driver.findElement(confirmOrderButton);
                System.out.println("Пробуем JavaScript клик...");
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
                waitForModalChange(initialText);
            } catch (Exception ex) {
                System.out.println("JavaScript клик также не сработал: " + ex.getMessage());

                try {
                    WebElement yesButton = driver.findElement(confirmOrderButton);
                    System.out.println("Пробуем Actions клик...");
                    new org.openqa.selenium.interactions.Actions(driver)
                            .moveToElement(yesButton)
                            .click()
                            .perform();
                    waitForModalChange(initialText);
                } catch (Exception ex2) {
                    System.out.println("Actions клик также не сработал: " + ex2.getMessage());
                }
            }
        }

        String finalText = getModalText();
        System.out.println("Текст модального окна после клика: " + finalText);
    }

    private String getModalText() {
        try {
            WebElement modalHeader = driver.findElement(successMessage);
            return modalHeader.getText();
        } catch (Exception e) {
            return "Модальное окно не найдено";
        }
    }

    private void waitForModalChange(String initialText) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> {
                        String currentText = getModalText();
                        return !currentText.equals(initialText) && !currentText.equals("Модальное окно не найдено");
                    });
            System.out.println("Модальное окно изменилось после клика");
        } catch (Exception e) {
            System.out.println("Модальное окно не изменилось после клика");
        }
    }

    public boolean isOrderSuccess() {
        System.out.println("Проверяем успешность заказа...");

        String currentText = getModalText();
        System.out.println("Текущий текст модального окна: " + currentText);

        if (currentText.contains("Хотите оформить заказ")) {
            System.out.println("Клик на 'Да' не сработал - модальное окно не изменилось");
            return false;
        }

        if (currentText.contains("Заказ оформлен") ||
                currentText.contains("оформлен") ||
                currentText.contains("номер заказа") ||
                currentText.matches(".*\\d+.*")) {
            System.out.println("Заказ успешно оформлен!");
            return true;
        }

        try {
            List<WebElement> modalElements = driver.findElements(By.xpath(".//div[contains(@class, 'Order_Modal')]//div"));
            for (WebElement element : modalElements) {
                String text = element.getText();
                if (text.matches(".*\\d+.*") && text.length() > 5) {
                    System.out.println("Найден номер заказа: " + text);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Поиск номера заказа не удался: " + e.getMessage());
        }

        System.out.println("Признаки успешного заказа не найдены");
        return false;
    }

    public void clickNextButton() {
        driver.findElement(nextButton).click();
    }

    public boolean isValidationErrorDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(validationError));
            return driver.findElement(validationError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clearNameField() {
        driver.findElement(nameField).clear();
    }

    public void fillNameField(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void forceClickYesButton() {
        System.out.println("Принудительный клик на кнопку 'Да'...");

        String beforeClick = getModalText();
        System.out.println("До клика: " + beforeClick);

        try {
            WebElement yesButton = driver.findElement(confirmOrderButton);

            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yesButton);

            new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(yesButton));

            System.out.println("Пробуем последовательные клики...");

            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
            System.out.println("JavaScript клик выполнен");

            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(driver -> {
                        String currentText = getModalText();
                        return !currentText.equals(beforeClick);
                    });

            String afterClick1 = getModalText();
            System.out.println("После JavaScript клика: " + afterClick1);

            if (!afterClick1.equals(beforeClick)) {
                System.out.println("Успех! Модальное окно изменилось после JavaScript клика");
                return;
            }

            yesButton.click();
            System.out.println("Обычный клик выполнен");

            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(driver -> {
                        String currentText = getModalText();
                        return !currentText.equals(afterClick1);
                    });

            String afterClick2 = getModalText();
            System.out.println("После обычного клика: " + afterClick2);

        } catch (Exception e) {
            System.out.println("Ошибка при клике на 'Да': " + e.getMessage());
        }
    }
}