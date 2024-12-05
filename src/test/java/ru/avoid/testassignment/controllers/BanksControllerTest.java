package ru.avoid.testassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.avoid.testassignment.DTO.BankDTO;
import ru.avoid.testassignment.services.BanksService;
import ru.avoid.testassignment.util.BankNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class BanksControllerTest {
    private final MockMvc mockMvc;

    @MockitoBean
    private BanksService banksService;

    private BankDTO bankDTO;

    private List<BankDTO> bankDTOList;

    private final ObjectMapper objectMapper;

    @Autowired
    public BanksControllerTest(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void setUp() {
        bankDTOList = new ArrayList<>();
        bankDTO = new BankDTO();
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setBankName("Bank1");
        bankDTO1.setBankCode("123456789");
        bankDTOList.add(bankDTO1);
        BankDTO bankDTO2 = new BankDTO();
        bankDTO2.setBankName("Bank2");
        bankDTO2.setBankCode("123456788");
        bankDTOList.add(bankDTO2);
        BankDTO bankDTO3 = new BankDTO();
        bankDTO3.setBankName("Bank3");
        bankDTO3.setBankCode("123456768");
        bankDTOList.add(bankDTO3);

    }

    @Test
    public void getAllBanksTest() throws Exception {
        when(banksService.findAll(false, false,
                false, "", "")).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(0).getBankCode()))

                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].bankName").value(bankDTOList.get(1).getBankName()))
                .andExpect(jsonPath("$[1].bankCode").value(bankDTOList.get(1).getBankCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].bankName").value(bankDTOList.get(2).getBankName()))
                .andExpect(jsonPath("$[2].bankCode").value(bankDTOList.get(2).getBankCode())

                );

        verify(banksService, times(1)).findAll(false, false,
                false, "", "");
    }

    @Test
    public void getAllBanksEmptyTest() throws Exception {
        when(banksService.findAll(false, false,
                false, "", "")).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(banksService, times(1)).findAll(false, false,
                false, "", "");
    }

    @Test
    public void getAllBanksFilterByBankNameTest() throws Exception {
        when(banksService.findAll(false, false,
                false, "Bank1", "")).thenReturn(List.of(bankDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("bank_name", "Bank1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(0).getBankCode())
                );

        verify(banksService, times(1)).findAll(false, false,
                false, "Bank1", "");
    }

    @Test
    public void getAllBanksFilterByCodeTest() throws Exception {
        when(banksService.findAll(false, false,
                false, "", "123456788")).thenReturn(List.of(bankDTOList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("bank_code", "123456788"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(1).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(1).getBankCode())
                );

        verify(banksService, times(1)).findAll(false, false,
                false, "", "123456788");
    }

    @Test
    public void getAllBanksSortByIdTest() throws Exception {
        when(banksService.findAll(true, false,
                false, "", "")).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_id", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(0).getBankCode()))

                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].bankName").value(bankDTOList.get(1).getBankName()))
                .andExpect(jsonPath("$[1].bankCode").value(bankDTOList.get(1).getBankCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].bankName").value(bankDTOList.get(2).getBankName()))
                .andExpect(jsonPath("$[2].bankCode").value(bankDTOList.get(2).getBankCode())
                );

        verify(banksService, times(1)).findAll(true, false,
                false, "", "");
    }

    @Test
    public void getAllBanksSortByBankNameTest() throws Exception {
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setBankName("SomeBank");
        bankDTO1.setBankCode("111111111");
        bankDTOList.add(0, bankDTO1);

        when(banksService.findAll(false, true,
                false, "", "")).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_bank_name", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(0).getBankCode()))

                .andExpect(jsonPath("$[1].bankName").value(bankDTOList.get(1).getBankName()))
                .andExpect(jsonPath("$[1].bankCode").value(bankDTOList.get(1).getBankCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].bankName").value(bankDTOList.get(2).getBankName()))
                .andExpect(jsonPath("$[2].bankCode").value(bankDTOList.get(2).getBankCode()))

                .andExpect(jsonPath("$[3].id").doesNotExist())
                .andExpect(jsonPath("$[3].bankName").value(bankDTOList.get(3).getBankName()))
                .andExpect(jsonPath("$[3].bankCode").value(bankDTOList.get(3).getBankCode())
                );

        verify(banksService, times(1)).findAll(false, true,
                false, "", "");
    }

    @Test
    public void getAllBanksSortByCodeTest() throws Exception {
        BankDTO temp = bankDTOList.get(0); // 1-й элемент
        bankDTOList.set(0, bankDTOList.get(2)); // Установка 3-го элемента на 1-ю позицию
        bankDTOList.set(2, temp);

        when(banksService.findAll(false, false,
                true, "", "")).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_bank_code", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$[0].bankCode").value(bankDTOList.get(0).getBankCode()))

                .andExpect(jsonPath("$[1].bankName").value(bankDTOList.get(1).getBankName()))
                .andExpect(jsonPath("$[1].bankCode").value(bankDTOList.get(1).getBankCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].bankName").value(bankDTOList.get(2).getBankName()))
                .andExpect(jsonPath("$[2].bankCode").value(bankDTOList.get(2).getBankCode())

                );

        verify(banksService, times(1)).findAll(false, false,
                true, "", "");
    }

    @Test
    public void getAllBanksFilterByNotExistingNameTest() throws Exception {

        when(banksService.findAll(false, false,
                false, "", "")).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("filter_by_name", "qqqqqqqqqqqqq"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(banksService, times(1)).findAll(false, false,
                false, "", "");
    }

    @Test
    public void getAllBanksFilterByNotExistingCodeTest() throws Exception {

        when(banksService.findAll(false, false,
                false, "", "code")).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("bank_code", "code"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(banksService, times(1)).findAll(false, false,
                false, "", "code");
    }

    @Test
    public void getBankByIdTest() throws Exception {

        when(banksService.findOne(1)).thenReturn(bankDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.bankName").value(bankDTOList.get(0).getBankName()))
                .andExpect(jsonPath("$.bankCode").value(bankDTOList.get(0).getBankCode()));

        verify(banksService, times(1)).findOne(1);
    }

    @Test
    public void getBankByNotExistingIdTest() throws Exception {
        when(banksService.findOne(anyInt())).thenThrow(new BankNotFoundException());

        mockMvc.perform(get("/banks/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Bank with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(banksService, times(1)).findOne(999);
    }

    @Test
    public void createBankTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankName("name");
        bankDTO.setBankCode("123454321");


        doNothing().when(banksService).save(any(BankDTO.class));
        bankDTOList.add(bankDTO);


        mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isOk());

        verify(banksService, times(1)).save(any(BankDTO.class));
    }

    @Test
    public void createBank_ValidationError_BankNotCreatedExceptionTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankName("");
        bankDTO.setBankCode("123456");


        mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("name isn't valid"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("bankCode isn't valid")));// Ожидаем сообщение об ошибке
    }

    @Test
    public void updateBankTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankName("name");
        bankDTO.setBankCode("123454321");

        doNothing().when(banksService).update(anyInt(), any(BankDTO.class));
        bankDTOList.add(bankDTO);

        mockMvc.perform(put("/banks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isOk());

        verify(banksService, times(1)).update(anyInt(), any(BankDTO.class));
    }

    @Test
    public void updateBank_ValidationError_BankNotUpdatedExceptionTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankName("");
        bankDTO.setBankCode("123456");

        doNothing().when(banksService).update(anyInt(), any(BankDTO.class));

        mockMvc.perform(put("/banks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("name isn't valid"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.error").value(containsString("bankCode isn't valid")));// Ожидаем сообщение об ошибке
    }

    @Test
    public void deleteBankTest() throws Exception {

        doNothing().when(banksService).delete(anyInt());
        mockMvc.perform(delete("/banks/1"))
                .andExpect(status().isOk()
                );
        verify(banksService, times(1)).delete(anyInt());

    }

    @Test
    public void deleteBank_BankNotFoundExceptionTest() throws Exception {
        int bankId = 999;
        doThrow(new BankNotFoundException()).when(banksService).delete(bankId);

        mockMvc.perform(delete("/banks/999", bankId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(banksService, times(1)).delete(bankId);
    }
}
