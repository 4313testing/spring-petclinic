package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private VetRepository vets;


    @Test
    public void testViewAsXmlButton_fixed() throws Exception {
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
            // Return XMl Content Type as Expected
            .andExpect(content().contentType("application/xml"))
            .andDo(xmlResult -> {
                // Here you can check the XML response content if needed
                String xmlContent = xmlResult.getResponse().getContentAsString();

            });
    }

}
