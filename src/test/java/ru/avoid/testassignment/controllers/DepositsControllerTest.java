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
import ru.avoid.testassignment.DTO.DepositDTO;
import ru.avoid.testassignment.services.DepositsService;
import ru.avoid.testassignment.util.BankNotFoundException;
import ru.avoid.testassignment.util.ClientNotFoundException;
import ru.avoid.testassignment.util.DepositNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class DepositsControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private DepositsService depositsService;

    private List<DepositDTO> depositDTOList;

    private final ObjectMapper objectMapper;

    @Autowired
    public DepositsControllerTest(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        depositDTOList = new ArrayList<>();
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor1");
        depositDTO1.setBankDeposit("bankDeposit1");
        depositDTO1.setInterest(10.00);
        depositDTO1.setDurationInMonths(10);
        depositDTOList.add(depositDTO1);
        DepositDTO depositDTO2 = new DepositDTO();
        depositDTO2.setDepositor("depositor2");
        depositDTO2.setBankDeposit("bankDeposit2");
        depositDTO2.setInterest(15.00);
        depositDTO2.setDurationInMonths(15);
        depositDTOList.add(depositDTO2);
        DepositDTO depositDTO3 = new DepositDTO();
        depositDTO3.setDepositor("depositor3");
        depositDTO3.setBankDeposit("bankDeposit3");
        depositDTO3.setInterest(20.00);
        depositDTO3.setDurationInMonths(20);
        depositDTOList.add(depositDTO3);
    }

    @Test
    public void getAllDepositsTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                0, 0, "", "")).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].depositor").value(depositDTOList.get(1).getDepositor()))
                .andExpect(jsonPath("$[1].bankDeposit").value(depositDTOList.get(1).getBankDeposit()))
                .andExpect(jsonPath("$[1].interest").value(depositDTOList.get(1).getInterest()))
                .andExpect(jsonPath("$[1].durationInMonths").value(depositDTOList.get(1).getDurationInMonths()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].depositor").value(depositDTOList.get(2).getDepositor()))
                .andExpect(jsonPath("$[2].bankDeposit").value(depositDTOList.get(2).getBankDeposit()))
                .andExpect(jsonPath("$[2].interest").value(depositDTOList.get(2).getInterest()))
                .andExpect(jsonPath("$[2].durationInMonths").value(depositDTOList.get(2).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                0, 0, "", "");
    }

    @Test
    public void getAllDepositsEmptyTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                0, 0, "", "")).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                0, 0, "", "");
    }

    @Test
    public void getAllDepositsFilterByDurationInMonthsTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                0, 10, "", "")).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("duration_in_months", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                0, 10, "", "");
    }

    @Test
    public void getAllDepositsFilterByInterestTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                10.00, 0, "", "")).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("interest", "10.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                10.00, 0, "", "");
    }

    @Test
    public void getAllDepositsFilterByClientTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                0, 0, "depositor1", "")).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("client_name", "depositor1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                0, 0, "depositor1", "");
    }

    @Test
    public void getAllDepositsFilterByBankDepositTest() throws Exception {
        when(depositsService.findAll(false, false, false, false,
                0, 0, "", "bankDeposit1")).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("bank_name", "bankDeposit1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, false,
                0, 0, "", "bankDeposit1");
    }


    @Test
    public void getAllDepositsSortByIdTest() throws Exception {
        when(depositsService.findAll(true, false, false, false,
                0, 0, "", "")).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_id", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].depositor").value(depositDTOList.get(1).getDepositor()))
                .andExpect(jsonPath("$[1].bankDeposit").value(depositDTOList.get(1).getBankDeposit()))
                .andExpect(jsonPath("$[1].interest").value(depositDTOList.get(1).getInterest()))
                .andExpect(jsonPath("$[1].durationInMonths").value(depositDTOList.get(1).getDurationInMonths()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].depositor").value(depositDTOList.get(2).getDepositor()))
                .andExpect(jsonPath("$[2].bankDeposit").value(depositDTOList.get(2).getBankDeposit()))
                .andExpect(jsonPath("$[2].interest").value(depositDTOList.get(2).getInterest()))
                .andExpect(jsonPath("$[2].durationInMonths").value(depositDTOList.get(2).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(true, false, false, false,
                0, 0, "", "");
    }

    @Test
    public void getAllDepositsSortBydurationInMonthsTest() throws Exception {
        when(depositsService.findAll(false, false, false, true,
                0, 0, "", "")).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_duration", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].depositor").value(depositDTOList.get(1).getDepositor()))
                .andExpect(jsonPath("$[1].bankDeposit").value(depositDTOList.get(1).getBankDeposit()))
                .andExpect(jsonPath("$[1].interest").value(depositDTOList.get(1).getInterest()))
                .andExpect(jsonPath("$[1].durationInMonths").value(depositDTOList.get(1).getDurationInMonths()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].depositor").value(depositDTOList.get(2).getDepositor()))
                .andExpect(jsonPath("$[2].bankDeposit").value(depositDTOList.get(2).getBankDeposit()))
                .andExpect(jsonPath("$[2].interest").value(depositDTOList.get(2).getInterest()))
                .andExpect(jsonPath("$[2].durationInMonths").value(depositDTOList.get(2).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, false, true,
                0, 0, "", "");
    }

    @Test
    public void getAllDepositsSortByInterestTest() throws Exception {

        when(depositsService.findAll(false, false, true, false,
                0, 0, "", "")).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_interest", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].depositor").value(depositDTOList.get(1).getDepositor()))
                .andExpect(jsonPath("$[1].bankDeposit").value(depositDTOList.get(1).getBankDeposit()))
                .andExpect(jsonPath("$[1].interest").value(depositDTOList.get(1).getInterest()))
                .andExpect(jsonPath("$[1].durationInMonths").value(depositDTOList.get(1).getDurationInMonths()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].depositor").value(depositDTOList.get(2).getDepositor()))
                .andExpect(jsonPath("$[2].bankDeposit").value(depositDTOList.get(2).getBankDeposit()))
                .andExpect(jsonPath("$[2].interest").value(depositDTOList.get(2).getInterest()))
                .andExpect(jsonPath("$[2].durationInMonths").value(depositDTOList.get(2).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, false, true, false,
                0, 0, "", "");
    }


    @Test
    public void getAllDepositsSortByOpeningDateTest() throws Exception {
        when(depositsService.findAll(false, true, false, false,
                0, 0, "", "")).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_opening_date", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$[0].bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$[0].interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$[0].durationInMonths").value(depositDTOList.get(0).getDurationInMonths()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].depositor").value(depositDTOList.get(1).getDepositor()))
                .andExpect(jsonPath("$[1].bankDeposit").value(depositDTOList.get(1).getBankDeposit()))
                .andExpect(jsonPath("$[1].interest").value(depositDTOList.get(1).getInterest()))
                .andExpect(jsonPath("$[1].durationInMonths").value(depositDTOList.get(1).getDurationInMonths()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].depositor").value(depositDTOList.get(2).getDepositor()))
                .andExpect(jsonPath("$[2].bankDeposit").value(depositDTOList.get(2).getBankDeposit()))
                .andExpect(jsonPath("$[2].interest").value(depositDTOList.get(2).getInterest()))
                .andExpect(jsonPath("$[2].durationInMonths").value(depositDTOList.get(2).getDurationInMonths())
                );

        verify(depositsService, times(1)).findAll(false, true, false, false,
                0, 0, "", "");
    }


    @Test
    public void getDepositByIdTest() throws Exception {

        when(depositsService.findOne(1)).thenReturn(depositDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.depositor").value(depositDTOList.get(0).getDepositor()))
                .andExpect(jsonPath("$.bankDeposit").value(depositDTOList.get(0).getBankDeposit()))
                .andExpect(jsonPath("$.interest").value(depositDTOList.get(0).getInterest()))
                .andExpect(jsonPath("$.durationInMonths").value(depositDTOList.get(0).getDurationInMonths()));

        verify(depositsService, times(1)).findOne(1);
    }

    @Test
    public void getDepositByNotExistingIdTest() throws Exception {
        when(depositsService.findOne(anyInt())).thenThrow(new DepositNotFoundException());

        mockMvc.perform(get("/deposits/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Deposit with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositsService, times(1)).findOne(999);
    }


    @Test
    public void createDepositTest() throws Exception {

        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor1");
        depositDTO1.setBankDeposit("bankDeposit1");
        depositDTO1.setInterest(12);
        depositDTO1.setDurationInMonths(11);

        doNothing().when(depositsService).save(any(DepositDTO.class));
        depositDTOList.add(depositDTO1);


        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isOk());

        verify(depositsService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void createDeposit_ValidationError_BankNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor1");
        depositDTO1.setBankDeposit("bankDeposit1");
        depositDTO1.setInterest(12);
        depositDTO1.setDurationInMonths(11);

        doThrow(new BankNotFoundException()).when(depositsService).save(any(DepositDTO.class));
        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Bank with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositsService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void createDeposit_ValidationError_ClientNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor1");
        depositDTO1.setBankDeposit("bankDeposit1");
        depositDTO1.setInterest(12);
        depositDTO1.setDurationInMonths(11);

        doThrow(new ClientNotFoundException()).when(depositsService).save(any(DepositDTO.class));

        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Client with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositsService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void updateDepositTest() throws Exception {


        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor1");
        depositDTO1.setBankDeposit("bankDeposit2");
        depositDTO1.setInterest(13);
        depositDTO1.setDurationInMonths(13);


        doNothing().when(depositsService).update(anyInt(), any(DepositDTO.class));
        depositDTOList.add(depositDTO1);


        mockMvc.perform(MockMvcRequestBuilders.put("/deposits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isOk());

        verify(depositsService, times(1)).update(anyInt(), any(DepositDTO.class));
    }

    @Test
    public void updateDeposit_ValidationError_BankNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setDepositor("depositor2");
        depositDTO1.setBankDeposit("bankDeposit2");
        depositDTO1.setInterest(13);
        depositDTO1.setDurationInMonths(13);

        doThrow(new BankNotFoundException()).when(depositsService).update(anyInt(), any(DepositDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/deposits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Bank with this id wasn't found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositsService, times(1)).update(anyInt(), any(DepositDTO.class));

    }

    @Test
    public void deleteDepositTest() throws Exception {

        doNothing().when(depositsService).delete(anyInt());
        mockMvc.perform(delete("/deposits/1"))
                .andExpect(status().isOk()
                );
        verify(depositsService, times(1)).delete(anyInt());

    }

    @Test
    public void deleteDepositByNotExistingIdTest() throws Exception {

        doThrow(new DepositNotFoundException()).when(depositsService).delete(anyInt());
        mockMvc.perform(delete("/deposits/1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(depositsService, times(1)).delete(anyInt());

    }
}
