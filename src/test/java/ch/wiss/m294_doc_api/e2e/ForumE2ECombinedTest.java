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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForumE2ECombinedTest {

    /* ------------------------------ Treiber ------------------------------ */

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:5173/";

    @BeforeAll
    void setUp() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions fx = new FirefoxOptions();
        // fx.addArguments("--headless");           // Für CI einkommentieren.
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

    private final By threadCards    = By.cssSelector("div.m-4.bg-gray-800");
    private final By navCreate      = By.linkText("CreateThread");
    private final By navAbout       = By.linkText("About");
    private final By navHome        = By.linkText("Home");

    private final By fieldTitle     = By.id("title");
    private final By fieldContent   = By.id("content");
    private final By fieldCategory  = By.id("category");

    private final By btnSubmit      = By.xpath("//button[contains(text(),'Submit')]");
    private final By h1Title        = By.cssSelector("div.bg-gray-800 h1");
    private final By spanVotes      = By.xpath("//span[contains(text(),'Votes')]");
    private final By btnUpvote      = By.xpath("//button[contains(text(),'Upvote')]");

    private final By textareaComm   = By.xpath("//textarea[@placeholder='Write your comment...']");
    private final By btnAddComm     = By.xpath("//button[contains(text(),'Add Comment')]");
    private final By commentBoxes   = By.cssSelector("div.bg-gray-800 p.text-white");
    private final By btnDelete      = By.linkText("Delete Thread");

    /* ----------------------------- Testdaten ---------------------------- */

    private final String UNIQUE     = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                                                       .format(LocalDateTime.now());
    private final String TITLE      = "Selenium-Thread " + UNIQUE;
    private final String BODY       = "Dies ist ein automatisch erstellter Thread.";
    private final String CATEGORY   = "Sports";
    private final String COMMENT    = "Automatischer Kommentar.";

    /* ------------------------------ Tests ------------------------------- */

    /** 1  Startseite lädt – es gibt mindestens ein Thread-Card-Element. */
    @Test @Order(1)
    void shouldLoadHomePage() {
        driver.get(BASE_URL);
        Assertions.assertFalse(driver.findElements(threadCards).isEmpty(),
                "Keine Thread-Karten auf der Startseite gefunden");
    }

    /** 2  Neuen Thread anlegen (bleibt auf /createthread, danach zurück & reload). */
    @Test @Order(2)
    void shouldCreateNewThread() {
        driver.findElement(navCreate).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldTitle));

        driver.findElement(fieldTitle).sendKeys(TITLE);
        driver.findElement(fieldContent).sendKeys(BODY);

        /* --- Kategorie wählen --- */
        new Select(driver.findElement(fieldCategory))
                .selectByVisibleText(CATEGORY);

        driver.findElement(btnSubmit).click();

        /* zurück zur Home-Liste und *hart* reloaden, damit React die Daten neu lädt */
        driver.findElement(navHome).click();                       // React‑Router
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        driver.navigate().refresh();                               // Browser‑Reload
        wait.until(ExpectedConditions.visibilityOfElementLocated(threadCards));

        Assertions.assertTrue(
                driver.findElements(threadCards).stream()
                      .anyMatch(card -> card.getText().contains(TITLE)
                                       && card.getText().contains(CATEGORY)),
                "Neu erstellter Thread fehlt oder falsche Kategorie");
    }

    /** 3  Detailseite öffnen, Inhalt + Kategorie prüfen. */
    @Test @Order(3)
    void shouldOpenDetailAndShowContent() {
        openThreadDetail();

        Assertions.assertEquals(TITLE, driver.findElement(h1Title).getText());

        /* BODY kann je nach UI in <h2>, <p> oder <div> liegen → ganze Page prüfen */
        Assertions.assertTrue(driver.getPageSource().contains(BODY),
                "Thread‑Body nicht gefunden");

        /* Kategorie & initiale Votes */
        Assertions.assertTrue(driver.getPageSource().contains("Category: " + CATEGORY));
        Assertions.assertTrue(driver.getPageSource().contains("Votes: 0"));
    }

    /** 4  Upvote-Button erhöht die Vote-Zahl. */
    @Test @Order(4)
    void shouldUpvoteThread() {
        openThreadDetail();

        int before = extractVotes();
        driver.findElement(btnUpvote).click();

        wait.until(ExpectedConditions.textToBe(spanVotes,
                "Votes: " + (before + 1)));

        int after = extractVotes();
        Assertions.assertEquals(before + 1, after,
                "Upvote hat die Vote-Zahl nicht erhöht");
    }

    /** 5  Kommentar hinzufügen. */
    @Test @Order(5)
    void shouldAddComment() {
        openThreadDetail();
        int before = driver.findElements(commentBoxes).size();

        driver.findElement(textareaComm).sendKeys(COMMENT);
        driver.findElement(btnAddComm).click();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(commentBoxes, before));
    }

    /** 6  Thread löschen. */
    @Test @Order(6)
    void shouldDeleteThread() {
        openThreadDetail();
        driver.findElement(btnDelete).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        driver.navigate().refresh();                               // Sicherheits‑Reload

        Assertions.assertTrue(
                driver.findElements(threadCards).stream()
                      .noneMatch(card -> card.getText().contains(TITLE)),
                "Thread wurde nicht gelöscht");
    }

    /** 7  Navigation zwischen About und Home prüfen. */
    @Test @Order(7)
    void shouldNavigateAboutAndHome() {
        driver.findElement(navAbout).click();
        wait.until(ExpectedConditions.urlContains("/about"));

        driver.findElement(navHome).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
    }

    /* ------------------------- Helper-Methoden --------------------------- */

    /** Öffnet die Detailseite des Test-Threads – egal, wo wir gerade sind. */
    private void openThreadDetail() {
        driver.findElement(navHome).click();                      // sicher auf Home
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        driver.navigate().refresh();                              // sicherstellen, dass Liste aktuell ist
        wait.until(ExpectedConditions.visibilityOfElementLocated(threadCards));

        driver.findElements(threadCards).stream()
              .filter(card -> card.getText().contains(TITLE))
              .findFirst().orElseThrow().click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(h1Title));
    }

    /** Votes aus dem "Votes: n"-Span herausparsen. */
    private int extractVotes() {
        String txt = driver.findElement(spanVotes).getText();     // „Votes: n“
        return Integer.parseInt(txt.replaceAll("\\D", ""));
    }
}
