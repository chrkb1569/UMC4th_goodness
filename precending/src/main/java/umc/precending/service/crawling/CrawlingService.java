package umc.precending.service.crawling;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import umc.precending.dto.post.PostCrawlingResponseDto;
import umc.precending.exception.post.PostNewsNotSupportedException;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    @Value("${crawling.driver}")
    private String driverUrl;

    public PostCrawlingResponseDto crawlingData(String accessUrl) {
        if(accessUrl.contains("naver")) return crawlingNaver(accessUrl);
        else if(accessUrl.contains("daum")) return crawlingDaum(accessUrl);
        throw new PostNewsNotSupportedException(accessUrl + "은 지원하지 않는 플랫폼입니다.");
    }

    private PostCrawlingResponseDto crawlingNaver(String accessUrl) {
        WebDriver driver = getChromDriver();

        try {
            driver.get(accessUrl);
            Thread.sleep(1000);

            List<WebElement> elements = driver.findElements(By.id("dic_area"));
            List<WebElement> images = driver.findElements(By.id("img1"));

            String content = elements.get(0).getText();
            String image = images.get(0).getAttribute("src");

            driver.close();
            driver.quit();

            return new PostCrawlingResponseDto(content, image);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private PostCrawlingResponseDto crawlingDaum(String accessUrl) {
        WebDriver driver = getChromDriver();

        try {
            driver.get(accessUrl);
            Thread.sleep(1000);

            List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"mArticle\"]/div[2]"));
            List<WebElement> images = driver.findElements(By.xpath("//*[@id=\"mArticle\"]/div[2]/div[2]/section/figure[1]/p/img"));

            String content = elements.get(0).getText();
            String image = images.get(0).getAttribute("src");

            driver.close();
            driver.quit();

            return new PostCrawlingResponseDto(content, image);

        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private WebDriver getChromDriver() {
        System.setProperty("webdriver.chrome.driver", driverUrl);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setCapability("ignoreProtectedModeSettings", true);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        return driver;
    }
}