package com.ocosur.ocosystem;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocosur.ocosystem.config.TestSecurityConfig;
import com.ocosur.ocosystem.livechicken.expense.ExpenseCategoryCode;
import com.ocosur.ocosystem.livechicken.expense.VehicleExpenseCategory;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FoodExpenseCreateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FuelExpenseCreateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.VehicleExpenseCreateDTO;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Import(TestSecurityConfig.class)
class ExpenseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

  

    @Test
    void createFoodExpense_shouldPersist() throws Exception {
        ExpenseCreateRequestDTO request = new ExpenseCreateRequestDTO();
        request.setCategoryCode(ExpenseCategoryCode.FOOD);
        request.setReason("Compra pollo");
        request.setAmount(BigDecimal.valueOf(200));
        request.setDate(LocalDate.now());

        FoodExpenseCreateDTO food = new FoodExpenseCreateDTO();
        food.setCedisId(1L);
        food.setWeight(BigDecimal.valueOf(30));
        request.setFood(food);

        mockMvc.perform(post("/api/live-chicken/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.categoryCode").value("FOOD"))
                .andExpect(jsonPath("$.reason").value("Compra pollo"))
                .andExpect(jsonPath("$.amount").value(200));

    }

    @Test
    void createFuelExpense_shouldPersist() throws Exception {
        ExpenseCreateRequestDTO request = new ExpenseCreateRequestDTO();
        request.setCategoryCode(ExpenseCategoryCode.FUEL);
        request.setReason("Gasolina camión");
        request.setAmount(BigDecimal.valueOf(500));
        request.setDate(LocalDate.now());

        FuelExpenseCreateDTO fuel = new FuelExpenseCreateDTO();
        fuel.setVehicleId(1L);
        fuel.setEmployeeId(1L);
        fuel.setRouteId(1L);
        request.setFuel(fuel);

        mockMvc.perform(post("/api/live-chicken/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.categoryCode").value("FUEL"));
    }

    @Test
    void createVehicleExpense_shouldPersist() throws Exception {
        ExpenseCreateRequestDTO request = new ExpenseCreateRequestDTO();
        request.setCategoryCode(ExpenseCategoryCode.VEHICLE);
        request.setReason("Mantenimiento");
        request.setAmount(BigDecimal.valueOf(1200));
        request.setDate(LocalDate.now());

        VehicleExpenseCreateDTO vehicle = new VehicleExpenseCreateDTO();
        vehicle.setVehicleId(1L);
        vehicle.setEmployeeId(1L);
        vehicle.setCategory(VehicleExpenseCategory.MAINTENANCE);
        request.setVehicle(vehicle);

        mockMvc.perform(post("/api/live-chicken/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.categoryCode").value("VEHICLE"));
    }

    @Test
    void getLatest_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/live-chicken/expenses/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getBetweenDates_shouldReturnExpenses() throws Exception {
        mockMvc.perform(get("/api/live-chicken/expenses")
                .param("start", LocalDate.now().minusDays(1).toString())
                .param("end", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateBaseExpense_shouldUpdate() throws Exception {
        // create
        ExpenseCreateRequestDTO create = new ExpenseCreateRequestDTO();
        create.setCategoryCode(ExpenseCategoryCode.FOOD);
        create.setReason("Original");
        create.setAmount(BigDecimal.valueOf(100));
        create.setDate(LocalDate.now());

        String response = mockMvc.perform(post("/api/live-chicken/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(create)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // update
        ExpenseUpdateRequestDTO update = new ExpenseUpdateRequestDTO();
        update.setReason("Actualizado");
        update.setAmount(BigDecimal.valueOf(150));

        mockMvc.perform(put("/api/live-chicken/expenses/{id}/base", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Actualizado"))
                .andExpect(jsonPath("$.amount").value(150));
    }

    @Test
    void updateNonExistingExpense_shouldReturn404() throws Exception {
        ExpenseUpdateRequestDTO update = new ExpenseUpdateRequestDTO();
        update.setReason("Fail");

        mockMvc.perform(put("/api/live-chicken/expenses/{id}/base", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

}
