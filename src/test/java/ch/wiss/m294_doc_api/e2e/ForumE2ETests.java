package ch.wiss.m294_doc_api.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForumE2ETests {

    private WebDriver driver;
    private WebDriverWait wait;

    /** Basis-URL:  APP_URL (Env) → -Dapp.url → Fallback http://localhost:8080 */
    private final String baseUrl = System.getenv()
            .getOrDefault("APP_URL",
                    System.getProperty("app.url", "http://localhost:5173"));

    private static String createdTitle;
    private static String createdThreadId;

    /* ---------- Browser ---------- */
    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opts = new FirefoxOptions()
                .addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
        driver = new FirefoxDriver(opts);
        wait  = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach void tearDown() { driver.quit(); }

    /* ---------- 1 Home ---------- */
    @Test @Order(1)
    void homePageShowsThreads() {
        driver.get(baseUrl + "/");
        wait.until(d -> !d.findElements(By.cssSelector("a[href^='/threads/']")).isEmpty());
        Assertions.assertFalse(driver.findElements(By.cssSelector("a[href^='/threads/']")).isEmpty());
    }

    /* ---------- 2 Thread anlegen ---------- */
    @Test @Order(2)
    void createThread() {
        driver.get(baseUrl + "/");

        /* Menü-Link „CreateThread“ (ohne Leerzeichen) */
        driver.findElement(By.xpath("//a[normalize-space()='CreateThread']")).click();

        createdTitle = "Selenium-Thread " + System.currentTimeMillis();
        String content = "Automatischer Test-Content";

        typeInto("title",   createdTitle);
        typeInto("content", content);

        driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();

        /* Seite wurde per window.location.reload() neu geladen → zurück zur Startseite */
        driver.get(baseUrl + "/");

        By newTile = By.xpath(
                "//a[contains(@href,'/threads/') and .//p[normalize-space()='" + createdTitle + "']]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(newTile));

        WebElement link = driver.findElement(newTile);
        String href = link.getAttribute("href");
        if (!href.startsWith("http")) href = baseUrl + href;        // relativer → absoluter Link
        createdThreadId = href.replaceAll(".*/", "");
        Assertions.assertFalse(createdThreadId.isBlank(), "Thread-ID leer");
    }

    /* ---------- 3 Details ---------- */
    @Test @Order(3)
    void viewThreadDetails() {
        openThreadDetail();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[normalize-space()='" + createdTitle + "']")));
    }

    /* ---------- 4 Kommentar ---------- */
    @Test @Order(4)
    void addCommentToThread() {
        openThreadDetail();

        String comment = "Neuer Kommentar " + System.currentTimeMillis();
        driver.findElement(By.tagName("textarea")).sendKeys(comment);
        driver.findElement(By.xpath("//button[contains(.,'Add') and contains(.,'Comment')]")).click();

        By added = By.xpath("//*[contains(@class,'border')]/p[normalize-space()='" + comment + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(added));
    }

    /* ---------- 5 Löschen ---------- */
    @Test @Order(5)
    void deleteThread() {
        openThreadDetail();
        firstPresent(
                By.xpath("//button[contains(.,'Delete')]"),
                By.xpath("//a[contains(.,'Delete')]")
        ).click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        Assertions.assertTrue(driver.findElements(
                By.xpath("//a[.//p[normalize-space()='" + createdTitle + "']]")).isEmpty(),
                "Thread nach Löschen noch vorhanden");
    }

    /* ---------- 6 About ---------- */
    @Test @Order(6)
    void aboutPage() {
        driver.get(baseUrl + "/");
        driver.findElement(By.xpath("//a[normalize-space()='About']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(.,'Hello there') and contains(.,'User')]")));
    }

    /* ---------- Hilfsmethoden ---------- */
    private void openThreadDetail() {
        driver.get(baseUrl + "/threads/" + createdThreadId);
    }

    /** erstes vorhandenes Element aus der Locator-Liste */
    private WebElement firstPresent(By... locators) {
        for (By by : locators) {
            List<WebElement> els = driver.findElements(by);
            if (!els.isEmpty()) return els.get(0);
        }
        throw new NoSuchElementException("Keiner der Locator gefunden");
    }

    /** sendKeys auf id -oder- name -oder- placeholder */
    private void typeInto(String field, String text) {
        firstPresent(
                By.id(field),
                By.name(field),
                By.xpath("//input[@placeholder='" + field + "']"),
                By.xpath("//textarea[@placeholder='" + field + "']")
        ).sendKeys(text);
    }
}
