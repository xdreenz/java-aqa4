import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import com.codeborne.selenide.Condition;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryFormTest {
    private String getForwardDate(int daysForward) {
        return LocalDate.now().plusDays(daysForward).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void shouldBeSubmittedSuccessfully() {
        open("http://localhost:9999");
        $("[data-test-id = city] input").setValue("Пермь");
        String plannedDate = getForwardDate(7);
        $("[data-test-id = date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id = date] input").setValue(plannedDate);
        $("[data-test-id = name] input").setValue("Смирнов Олег");
        $("[data-test-id = phone] input").setValue("+71112223344");
        $("[data-test-id = agreement]").click();
        $("button.button").click(); //А кнопки-то две
        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + plannedDate));

    }

    @Test
    public void shouldBeSubmittedSuccessfullyUsingComplexElements() {
        open("http://localhost:9999");
        $("[data-test-id = city] input").setValue("Пе");    //Город по-прежнему Пермь
        String plannedDate = getForwardDate(7);
        $("[data-test-id = city] input").sendKeys(   //Стрелки вниз и Enter
                Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        $("[data-test-id = date] input").sendKeys(  //Стрелки вправо и Enter
                Keys.ARROW_DOWN, //Сначала фокус на виджет
                Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ENTER);    //7 дней начиная с 3-го
        $("[data-test-id = name] input").setValue("Смирнов Олег");
        $("[data-test-id = phone] input").setValue("+71112223344");
        $("[data-test-id = agreement]").click();
        $("button.button").click();
        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + plannedDate));

    }
}
