package ru.avoid.testassignment.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.avoid.testassignment.DTO.ClientDTO;
import ru.avoid.testassignment.services.ClientsService;
import ru.avoid.testassignment.services.LegalFormService;
import ru.avoid.testassignment.util.ClientNotFoundException;
import ru.avoid.testassignment.util.LegalFormNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
class ClientsControllerTest {

    private final MockMvc mockMvc;



    @MockitoBean
    private ClientsService clientsService;

    private ClientDTO clientDTO;

    private List<ClientDTO> clientDTOList;

    private final ObjectMapper objectMapper;

    private LegalFormService legalFormService;


    @Autowired
    public ClientsControllerTest(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }



    @BeforeEach
    void setUp() {
        clientDTOList = new ArrayList<>();
        clientDTO = new ClientDTO();
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setName("Client1");
        clientDTO1.setShortName("c1");
        clientDTO1.setAddress("c1_address");
        clientDTO1.setLegalFormName("ООО");
        clientDTOList.add(clientDTO1);
        ClientDTO clientDTO2 = new ClientDTO();
        clientDTO2.setName("Client2");
        clientDTO2.setShortName("c2");
        clientDTO2.setAddress("c2_address");
        clientDTO2.setLegalFormName("ООО");
        clientDTOList.add(clientDTO2);
        ClientDTO clientDTO3 = new ClientDTO();
        clientDTO3.setName("Client3");
        clientDTO3.setShortName("c3");
        clientDTO3.setAddress("c3_address");
        clientDTO3.setLegalFormName("ИП");
        clientDTOList.add(clientDTO3);
    }

    @Test
    void getAllClients() throws Exception {

        when(clientsService.findAll(false, false, false,
                false, "", "", "", "")).thenReturn(clientDTOList);

        mockMvc.perform(get("/clients")).andExpect(status().isOk()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(0).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(0).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(0).getLegalFormName()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[1].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[1].legalFormName").value(clientDTOList.get(1).getLegalFormName()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[2].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[2].legalFormName").value(clientDTOList.get(2).getLegalFormName())

                );
        verify(clientsService, times(1)).findAll( false, false, false,
                false, "", "", "", "");
    }

    @Test
    public void getAllClientsEmptyTest() throws Exception {
        when(clientsService.findAll( false, false, false,
                false, "", "", "", "")).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                false, "", "", "", "");
    }

    @Test
    public void getAllClientsFilterByNameTest() throws Exception {
        when(clientsService.findAll( false, false, false,
                false, "Client1", "", "", "")).thenReturn(List.of(clientDTOList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("name", "Client1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(1).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                false, "Client1", "", "", "");
    }

    @Test
    public void getAllClientsFilterByShortNameTest() throws Exception {
        when(clientsService.findAll( false, false, false,
                false, "", "c2", "", "")).thenReturn(List.of(clientDTOList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("short_name", "c2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(1).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                false, "", "c2", "", "");
    }

    @Test
    public void getAllClientsFilterByAddressTest() throws Exception {
        when(clientsService.findAll( false, false, false,
                false, "", "", "c3_address", "")).thenReturn(List.of(clientDTOList.get(2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("address", "c3_address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(2).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                false, "", "", "c3_address", "");
    }

    @Test
    public void getAllClientsFilterByLegalForm() throws Exception {
        when(clientsService.findAll( false, false, false,
                false, "", "", "", "ООО")).thenReturn(List.of(clientDTOList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("legal_form", "ООО"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(1).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                false, "", "", "", "ООО");
    }

    @Test
    public void getAllClientsSortByIdTest() throws Exception {
        when(clientsService.findAll( true, false, false,
                false, "", "", "", "")).thenReturn(clientDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("sort_by_id", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(0).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(0).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(0).getLegalFormName()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[1].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[1].legalFormName").value(clientDTOList.get(1).getLegalFormName()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[2].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[2].legalFormName").value(clientDTOList.get(2).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( true, false, false,
                false, "", "", "", "");
    }


    @Test
    public void getAllClientsSortByNameTest() throws Exception {

        when(clientsService.findAll( false, true, false,
                false, "", "", "", "")).thenReturn(clientDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("sort_by_name", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(0).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(0).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(0).getLegalFormName()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[1].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[1].legalFormName").value(clientDTOList.get(1).getLegalFormName()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[2].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[2].legalFormName").value(clientDTOList.get(2).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, true, false,
                false, "", "", "", "");
    }

    @Test
    public void getAllClientsSortByShortNameTest() throws Exception {

        when(clientsService.findAll( false, false, true,
                false, "", "", "", "")).thenReturn(clientDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("sort_by_short_name", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(0).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(0).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(0).getLegalFormName()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[1].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[1].legalFormName").value(clientDTOList.get(1).getLegalFormName()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[2].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[2].legalFormName").value(clientDTOList.get(2).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, true,
                false, "", "", "", "");
    }

    @Test
    public void getAllClientsSortByAddressTest() throws Exception {

        when(clientsService.findAll( false, false, false,
                true, "", "", "", "")).thenReturn(clientDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .param("sort_by_address", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].shortName").value(clientDTOList.get(0).getShortName()))
                .andExpect(jsonPath("$[0].address").value(clientDTOList.get(0).getAddress()))
                .andExpect(jsonPath("$[0].legalFormName").value(clientDTOList.get(0).getLegalFormName()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(clientDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].shortName").value(clientDTOList.get(1).getShortName()))
                .andExpect(jsonPath("$[1].address").value(clientDTOList.get(1).getAddress()))
                .andExpect(jsonPath("$[1].legalFormName").value(clientDTOList.get(1).getLegalFormName()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(clientDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].shortName").value(clientDTOList.get(2).getShortName()))
                .andExpect(jsonPath("$[2].address").value(clientDTOList.get(2).getAddress()))
                .andExpect(jsonPath("$[2].legalFormName").value(clientDTOList.get(2).getLegalFormName())
                );

        verify(clientsService, times(1)).findAll( false, false, false,
                true, "", "", "", "");
    }

    @Test
    public void getClientByIdTest() throws Exception {

        when(clientsService.findOne(1)).thenReturn(clientDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").value(clientDTOList.get(0).getName()))
                .andExpect(jsonPath("$.shortName").value((clientDTOList.get(0).getShortName())))
                .andExpect(jsonPath("$.address").value((clientDTOList.get(0).getAddress())))
                .andExpect(jsonPath("$.legalFormName").value((clientDTOList.get(0).getLegalFormName())));


        verify(clientsService, times(1)).findOne(1);
    }

    @Test
    public void getClientByNotExistingIdTest() throws Exception {
        when(clientsService.findOne(anyInt())).thenThrow(new ClientNotFoundException());

        mockMvc.perform(get("/clients/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Client with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(clientsService, times(1)).findOne(999);
    }

    @Test
    public void createClientTest() throws Exception {

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("name");
        clientDTO.setShortName("shortName");
        clientDTO.setAddress("address");
        clientDTO.setLegalFormName("ООО");

        doNothing().when(clientsService).save(any(ClientDTO.class));
        clientDTOList.add(clientDTO);


        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk());

        verify(clientsService, times(1)).save(any(ClientDTO.class));
    }

    @Test
    public void createClient_ValidationError_ClientNotCreatedExceptionTest() throws Exception {

        clientDTO = new ClientDTO();
        clientDTO.setName("");
        clientDTO.setShortName("");
        clientDTO.setAddress("");

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("name isn't valid"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("short_name isn't valid"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("address isn't valid")));

    }

    @Test
    public void createClient_ValidationError_LegalFormNotFoundException() throws Exception {
        clientDTO = new ClientDTO();
        clientDTO.setName("name");
        clientDTO.setShortName("shortName");
        clientDTO.setAddress("address");

        doThrow(new LegalFormNotFoundException()).when(clientsService).save(any(ClientDTO.class));
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("LegalForm with this name wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(clientsService, times(1)).save(any(ClientDTO.class));
    }


    @Test
    public void updateClientTest() throws Exception {

        clientDTO = new ClientDTO();
        clientDTO.setName("name");
        clientDTO.setShortName("shortName");
        clientDTO.setAddress("address");
        clientDTO.setLegalFormName("ООО");

        doNothing().when(clientsService).update(anyInt(), any(ClientDTO.class));
        clientDTOList.add(clientDTO);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk());

        verify(clientsService, times(1)).update(anyInt(), any(ClientDTO.class));
    }

    @Test
    public void updateClient_ValidationError_ClientNotUpdatedExceptionTest() throws Exception {

        clientDTO = new ClientDTO();
        clientDTO.setName("");
        clientDTO.setShortName("");
        clientDTO.setAddress("");

        doNothing().when(clientsService).update(anyInt(), any(ClientDTO.class));

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("name isn't valid")))  // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("short_name isn't valid"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("address isn't valid")));
    }

    @Test
    public void updateClient_ValidationError_LegalFormNotFoundException() throws Exception {
        clientDTO = new ClientDTO();
        clientDTO.setName("Имя");
        clientDTO.setShortName("Имя");
        clientDTO.setAddress("Адрес");

        doThrow(new LegalFormNotFoundException()).when(clientsService).update(anyInt(), any(ClientDTO.class));
        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("LegalForm with this name wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(clientsService, times(1)).update(anyInt(), any(ClientDTO.class));
    }


    @Test
    public void deleteClientTest() throws Exception {

        doNothing().when(clientsService).delete(anyInt());
        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isOk()
                );
        verify(clientsService, times(1)).delete(anyInt());

    }

    @Test
    public void clientBank_ClientNotFoundExceptionTest() throws Exception {
        int bankId = 999;
        doThrow(new ClientNotFoundException()).when(clientsService).delete(bankId);

        mockMvc.perform(delete("/clients/999", bankId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clientsService, times(1)).delete(bankId);
    }

}
