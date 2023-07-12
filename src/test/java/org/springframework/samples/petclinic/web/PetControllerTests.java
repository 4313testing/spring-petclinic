package org.springframework.samples.petclinic.web;//package org.springframework.samples.petclinic.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
@ActiveProfiles("spring-data-jpa")
public class PetControllerTests {

    @Autowired
    private PetController petController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }
//
//    @Test
//    public void testGetExistingUser() throws Exception {
//        ResultActions actions = mockMvc.perform(get("/vets.json").accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk());
//        actions.andExpect(content().contentType("application/json;charset=UTF-8"))
//            .andExpect(jsonPath("$.vetList[0].id").value(1));
//    }
//
//    @Test
//    public void testProcessUpdateOwnerFormSuccess() throws Exception {
//        mockMvc
//            .perform(post("/owners/{ownerId}/edit", 2).param("firstName", "Joe")
//                .param("lastName", "Bloggs")
//                .param("address", "123 Caramel Street")
//                .param("city", "London")
//                .param("telephone", "01616291589"))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/owners/{ownerId}"));
//
//
//        Collection<Owner> createdOwner= clinicService.findOwnerByLastName("Bloggs");
//
//        List<Integer> ids=new ArrayList<>();
//        for(Owner owner:createdOwner)
//        {
//            System.out.println(owner.getId());
//         ids.add(owner.getId());
//        }
//        assertFalse(ids.contains(2));
////        Assertions.assertThat(ids.contains(2));
//
//    }
//
//    @Test
//    public void testProcessUpdateFormHasErrors() throws Exception {
//        mockMvc
//            .perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).param("name", "Betty")
//                .param("birthDate", "2015/02/12"))
//            .andExpect(model().attributeHasNoErrors("owner"))
//            .andExpect(model().attributeHasErrors("pet"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("pets/createOrUpdatePetForm"));
//    }
//


    // showing 400 error code instead of Invalid.Date Message
    @Test
    public void testProcessUpdateFormHasErrors_Invalid_birthDate_400Code_Error() throws Exception {
        mockMvc
            .perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1)
                .param("name", "Betty")
                //Wrong Date format ("mm-dd-yyyy")  instead of ("yyyy-mm-dd")
                .param("birthDate", "02/12/2015")
            )
            //printing the Http Response
            .andDo(print())
            //Expecting error message in Pet model
            .andExpect(model().attributeHasErrors("pet"))
            // Expecting 200 OK Error Code
            .andExpect(status().isOk());

    }

    }
