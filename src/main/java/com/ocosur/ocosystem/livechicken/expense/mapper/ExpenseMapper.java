package com.ocosur.ocosystem.livechicken.expense.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.livechicken.expense.Expense;
import com.ocosur.ocosystem.livechicken.expense.FoodExpense;
import com.ocosur.ocosystem.livechicken.expense.FuelExpense;
import com.ocosur.ocosystem.livechicken.expense.VehicleExpense;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseDetailResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseFlatRowDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FoodExpenseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FuelExpenseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.VehicleExpenseDTO;

@Component
public class ExpenseMapper {
    public ExpenseResponseDTO toResponse(ExpenseFlatRowDTO row) {
        return ExpenseResponseDTO.builder()
                .id(row.id())
                .categoryCode(row.categoryCode())
                .categoryName(row.categoryName())
                .expenseType(row.expenseType())
                .reason(row.reason())
                .amount(row.amount())
                .date(row.date())

                .cedisName(row.cedisName())
                .weight(row.weight())

                .vehicleName(row.vehicleName())
                .employeeName(row.employeeName())
                .vehicleCategory(row.vehicleCategory())

                .routeName(row.routeName())
                .build();
    }

    public ExpenseResponseDTO toResponse(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseResponseDTO dto = new ExpenseResponseDTO();

        mapBase(expense, dto);
        mapCategory(expense, dto);
        mapFood(expense, dto);
        mapVehicle(expense, dto);
        mapFuel(expense, dto);

        return dto;
    }

    private void mapBase(Expense expense, ExpenseResponseDTO dto) {
        dto.setId(expense.getId());
        dto.setReason(expense.getReason());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategoryCode(expense.getCategoryCode());
    }

    /*
     * =====================
     * Category
     * ======================
     */

    private void mapCategory(Expense expense, ExpenseResponseDTO dto) {
        if (expense.getCategoryCode() == null) {
            return;
        }

        dto.setCategoryName(expense.getCategoryCode().name());
        dto.setExpenseType(expense.getCategoryCode().getType());
    }

    /*
     * =====================
     * Food
     * ======================
     */

    private void mapFood(Expense expense, ExpenseResponseDTO dto) {
        FoodExpense food = expense.getFoodExpense();
        if (food == null) {
            return;
        }

        if (food.getCedis() != null) {
            dto.setCedisName(food.getCedis().getName());
        }

        dto.setWeight(food.getWeight());
    }

    /*
     * =====================
     * Vehicle
     * ======================
     */

    private void mapVehicle(Expense expense, ExpenseResponseDTO dto) {
        VehicleExpense ve = expense.getVehicleExpense();
        if (ve == null) {
            return;
        }

        if (ve.getVehicle() != null) {
            dto.setVehicleName(ve.getVehicle().getName());
        }

        if (ve.getEmployee() != null) {
            dto.setEmployeeName(ve.getEmployee().getName());
        }

        dto.setVehicleCategory(ve.getCategory());
    }

    /*
     * =====================
     * Fuel
     * ======================
     */

    private void mapFuel(Expense expense, ExpenseResponseDTO dto) {
        FuelExpense fuel = expense.getFuelExpense();
        if (fuel == null) {
            return;
        }

        if (fuel.getRoute() != null) {
            dto.setRouteName(fuel.getRoute().getName());
        }
    }

    public ExpenseDetailResponseDTO toDetailDTO(Expense expense) {

        ExpenseDetailResponseDTO dto = ExpenseDetailResponseDTO.builder()
                .id(expense.getId())
                .categoryCode(expense.getCategoryCode())
                .reason(expense.getReason())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .build();

        if (expense.getFoodExpense() != null) {
            dto.setFood(FoodExpenseDTO.builder()
                    .cedisId(expense.getFoodExpense().getCedis().getId())
                    .weight(expense.getFoodExpense().getWeight())
                    .build());
        }

        if (expense.getFuelExpense() != null) {
            dto.setFuel(FuelExpenseDTO.builder()
                    .vehicleId(expense.getFuelExpense().getVehicle().getId())
                    .employeeId(expense.getFuelExpense().getEmployee().getId())
                    .routeId(expense.getFuelExpense().getRoute().getId())
                    .build());
        }

        if (expense.getVehicleExpense() != null) {
            dto.setVehicle(VehicleExpenseDTO.builder()
                    .vehicleId(expense.getVehicleExpense().getVehicle().getId())
                    .employeeId(expense.getVehicleExpense().getEmployee().getId())
                    .category(expense.getVehicleExpense().getCategory())
                    .build());
        }

        return dto;
    }

}
