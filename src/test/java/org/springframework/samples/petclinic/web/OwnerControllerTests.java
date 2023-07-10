package org.springframework.samples.petclinic.web;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
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
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
@ActiveProfiles("spring-data-jpa")
public class OwnerControllerTests {

    @Autowired
    private OwnerController ownerController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    public void testGetExistingUser() throws Exception {
        ResultActions actions = mockMvc.perform(get("/vets.json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        actions.andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.vetList[0].id").value(1));
    }


    // Testing Owner Update It creates a new Owner record in DB instead of updating the same record
    @Test
    public void testProcessUpdateOwnerFormSuccess() throws Exception {

        //Updating the the Owner
        mockMvc
            .perform(post("/owners/{ownerId}/edit", 2).param("firstName", "Joe")
                .param("lastName", "Bloggs")
                .param("address", "123 Caramel Street")
                .param("city", "London")
                .param("telephone", "01616291589"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));

        //Finding the owner the same Last Name Above ("Bloggs")
        Collection<Owner> createdOwner= clinicService.findOwnerByLastName("Bloggs");

        //Creating an empty List
        List<Integer> ids=new ArrayList<>();

        // Looping the createdOwner List of Owner with Last Name ("Bloggs")
        for(Owner owner:createdOwner)
        {
            //printing the Id of the Owner
            System.out.println(owner.getId());
            //adding the owner ID to list of Ids
            ids.add(owner.getId());
        }

        //checking if the owner id Above which is 2 is inside the List of IDS
        //should be ture because its now updating the same owner
         assertTrue(ids.contains(2));
//        Assertions.assertThat(ids.contains(2));

    }



    }
