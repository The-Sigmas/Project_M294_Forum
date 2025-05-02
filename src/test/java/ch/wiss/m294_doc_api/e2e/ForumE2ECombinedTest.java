package ch.wiss.m294_doc_api.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.*;

/**
 * End-to-End-Tests für das Forum – 6 getrennte Fälle im selben File (nur Firefox).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)          // 1 Driver für alle Methoden
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)   // Reihenfolge fixieren
public class ForumE2ECombinedTest {

    /* ------------------------------ Treiber ------------------------------ */

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:5173/";

    @BeforeAll
    void setUp() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions fx = new FirefoxOptions();
        // fx.addArguments("--headless");           // CI? Headless einkommentieren.
        driver = new FirefoxDriver(fx);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    void tearDown() {
        if (driver != null) driver.quit();
    }

    /* ----------------------------- Locators ----------------------------- */

    private final By threadCards   = By.cssSelector("div.m-4.bg-gray-800");
    private final By navCreate     = By.linkText("CreateThread");
    private final By navAbout      = By.linkText("About");
    private final By navHome       = By.linkText("Home");
    private final By fieldTitle    = By.id("title");
    private final By fieldContent  = By.id("content");
    private final By btnSubmit     = By.xpath("//button[contains(text(),'Submit Thread')]");
    private final By h1Title       = By.cssSelector("div.bg-gray-800 h1");
    private final By pContent      = By.cssSelector("div.bg-gray-800 h2");
    private final By textareaComm  = By.xpath("//textarea[@placeholder='Write your comment...']");
    private final By btnAddComm    = By.xpath("//button[contains(text(),'Add Comment')]");
    private final By commentBoxes  = By.cssSelector("div.bg-gray-800 p.text-white");
    private final By btnDelete     = By.linkText("Delete Thread");

    /* ----------------------------- Testdaten ---------------------------- */

    private final String UNIQUE  = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                                                    .format(LocalDateTime.now());
    private final String TITLE   = "Selenium-Thread " + UNIQUE;
    private final String BODY    = "Dies ist ein automatisch erstellter Thread.";
    private final String COMMENT = "Automatischer Kommentar.";

    /* ------------------------------ Tests ------------------------------- */

    /**   Startseite lädt – es gibt mindestens ein Thread-Card-Element. */
    @Test @Order(1)
    void shouldLoadHomePage() {
        driver.get(BASE_URL);
        Assertions.assertFalse(driver.findElements(threadCards).isEmpty(),
                "Keine Thread-Karten auf der Startseite gefunden");
    }

    /** 2  Neuen Thread anlegen (bleibt auf /createthread, daher manuell zurück). */
    @Test @Order(2)
    void shouldCreateNewThread() {
        driver.findElement(navCreate).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldTitle));

        driver.findElement(fieldTitle).sendKeys(TITLE);
        driver.findElement(fieldContent).sendKeys(BODY);
        driver.findElement(btnSubmit).click();

        driver.findElement(navHome).click();                    // zurück zur Liste
        wait.until(ExpectedConditions.urlToBe(BASE_URL));

        Assertions.assertTrue(
                driver.findElements(threadCards).stream()
                      .anyMatch(card -> card.getText().contains(TITLE)),
                "Neu erstellter Thread erscheint nicht auf der Startseite");
    }

    /**   Detailseite öffnen und Inhalt prüfen. */
    @Test @Order(3)
    void shouldOpenDetailAndShowContent() {
        openThreadDetail();
        Assertions.assertEquals(TITLE, driver.findElement(h1Title).getText());
        Assertions.assertEquals(BODY,  driver.findElement(pContent).getText());
    }

    /**   Kommentar hinzufügen. */
    @Test @Order(4)
    void shouldAddComment() {
        openThreadDetail();
        int before = driver.findElements(commentBoxes).size();

        driver.findElement(textareaComm).sendKeys(COMMENT);
        driver.findElement(btnAddComm).click();

        wait.until(d -> d.findElements(commentBoxes).size() > before);
    }

    /**   Thread löschen. */
    @Test @Order(5)
    void shouldDeleteThread() {
        openThreadDetail();
        driver.findElement(btnDelete).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL));

        Assertions.assertTrue(
                driver.findElements(threadCards).stream()
                      .noneMatch(card -> card.getText().contains(TITLE)),
                "Thread wurde nicht gelöscht");
    }

    /**   Navigation zwischen About und Home prüfen. */
    @Test @Order(6)
    void shouldNavigateAboutAndHome() {
        driver.findElement(navAbout).click();
        wait.until(ExpectedConditions.urlContains("/about"));

        driver.findElement(navHome).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
    }

    /* ------------------------- Helper-Methode --------------------------- */

    /** Öffnet die Detailseite des Test-Threads – egal, wo wir gerade sind. */
    private void openThreadDetail() {
        driver.findElement(navHome).click();                     // sicher auf Home
        wait.until(ExpectedConditions.urlToBe(BASE_URL));

        driver.findElements(threadCards).stream()
              .filter(card -> card.getText().contains(TITLE))
              .findFirst().orElseThrow().click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(h1Title));
    }
}
