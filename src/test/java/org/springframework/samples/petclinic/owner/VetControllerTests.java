package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.vet.VetController;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VetController.class)
public class VetControllerTests {

    private WebDriver driver;

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private VetRepository vets;


    @Before
    public void setUp() {
        // Set up WebDriver for Chrome
        System.setProperty("webdriver.chrome.driver", "D:\\Users\\Haroun\\Videos\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void testViewAsXmlButton() throws Exception {
        // Perform the first request to view the page containing the button
        MvcResult result = mockMvc.perform(get("/vets.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("vets/vetList"))
            .andReturn();

        // Extract the HTML content from the response
        String htmlContent = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(htmlContent);
        Element link = doc.selectFirst("a:containsOwn(View as XML)");

        // Assert that the anchor tag is present
        assertThat(link).isNotNull();
        assertThat(link.text()).isEqualTo("View as XML");

        // Extract the URL from the href attribute of the anchor tag
        String xmlUrl = link.attr("href");

        // Perform the second request to view the XML content
        mockMvc.perform(get(xmlUrl))
            .andExpect(status().isOk())
            // It Should XML Instead of Html
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andDo(xmlResult -> {
                // Here you can check the XML response content if needed
                String xmlContent = xmlResult.getResponse().getContentAsString();

            });
    }

    @Test
    public void testViewAsXmlButtonWithSelenium() throws Exception {

        driver.get("http://localhost:8080/vets.html");

        // Find the "View as JSON" link using Selenium
        WebElement viewAsJsonLink = driver.findElement(By.linkText("View as JSON"));

        //Testing View as JSON works
        try {
            Thread.sleep(2000); // Sleep for 2 seconds
            viewAsJsonLink.click();
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception if needed
        }



        //Now Going back to vet List for Testing View as Xml button
        try {
            Thread.sleep(3000); // Sleep for 3 seconds
            driver.get("http://localhost:8080/vets.html");
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception if needed
        }

        // Find the "View as XML" link using Selenium
        WebElement viewAsXmlLink = driver.findElement(By.linkText("View as XML"));

        // Assert that the link is present
        assertNotNull(viewAsXmlLink);

        // Click on the link to view the XML content
        try {
            Thread.sleep(2000); // Sleep for 2 seconds
            viewAsXmlLink.click();
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception if needed
        }


        // it should be instead vets.xml
        assertThat(driver.getCurrentUrl().equals("http://localhost:8080/vets.html"));

    }

    @After
    public void tearDown() {
        // Close the browser after each test
        try {
            Thread.sleep(3000); // Sleep for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception if needed
        }
        driver.quit();
    }

}
