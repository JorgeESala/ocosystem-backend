package com.ocosur.ocosystem.livechicken.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseDetailResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseFlatRowDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FoodExpenseCreateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FoodExpenseUpdateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FuelExpenseCreateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.FuelExpenseUpdateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.VehicleExpenseCreateDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.VehicleExpenseUpdateDTO;
import com.ocosur.ocosystem.livechicken.expense.mapper.ExpenseMapper;
import com.ocosur.ocosystem.livechicken.route.Route;
import com.ocosur.ocosystem.livechicken.route.RouteRepository;
import com.ocosur.ocosystem.core.cedis.model.Cedis;
import com.ocosur.ocosystem.core.cedis.repository.CedisRepository;
import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;
import com.ocosur.ocosystem.core.vehicle.model.Vehicle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service("liveChickenExpenseService")
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

        private final EntityManager entityManager;
        private final LiveChickenExpenseRepository expenseRepository;
        private final ExpenseMapper expenseMapper;
        private final LiveChikenExpenseCategoryRepository categoryRepository;
        private final FuelExpenseRepository fuelRepository;
        private final FoodExpenseRepository foodRepository;
        private final VehicleRepository vehicleRepository;
        private final VehicleExpenseRepository vehicleExpenseRepository;
        private final EmployeeRepository employeeRepository;
        private final RouteRepository routeRepository;
        private final CedisRepository cedisRepository;

        @Transactional
        public ExpenseResponseDTO createExpense(ExpenseCreateRequestDTO request) {

                // 1. Categoría
                ExpenseCategory category = categoryRepository.findById(request.getCategoryCode())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

                // 2. Expense base
                Expense expense = Expense.builder()
                                .categoryCode(category.getCode())
                                .reason(request.getReason())
                                .amount(request.getAmount())
                                .date(request.getDate())
                                .build();

                expenseRepository.save(expense);

                // 3. Extra por tipo
                switch (category.getExpenseType()) {
                        case FUEL -> createFuelExpense(expense, request.getFuel());
                        case FOOD -> createFoodExpense(expense, request.getFood());
                        case VEHICLE -> createVehicleExpense(expense, request.getVehicle());
                        case GENERIC -> {
                                // no-op
                        }
                        default -> throw new IllegalStateException(
                                        "Unsupported expense type: " + category.getExpenseType());
                }

                return getFlatResponse(expense);
        }

        public ExpenseDetailResponseDTO getById(Long id) {
                Expense expense = expenseRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Expense not found with id: " + id));
                return expenseMapper.toDetailDTO(expense);
        }

        private void createFuelExpense(Expense expense, FuelExpenseCreateDTO dto) {

                if (dto == null) {
                        throw new IllegalArgumentException("Fuel data required");
                }

                FuelExpense fuel = new FuelExpense();
                fuel.setExpense(expense);
                fuel.setVehicle(vehicleRepository.getReferenceById(dto.getVehicleId()));
                fuel.setEmployee(employeeRepository.getReferenceById(dto.getEmployeeId()));
                fuel.setRoute(routeRepository.getReferenceById(dto.getRouteId()));

                fuelRepository.save(fuel);
        }

        private void createFoodExpense(
                        Expense expense,
                        FoodExpenseCreateDTO dto) {

                if (dto == null) {
                        throw new IllegalArgumentException("Food data required");
                }

                Cedis cedis = cedisRepository.findById(dto.getCedisId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Cedis not found: " + dto.getCedisId()));

                FoodExpense food = new FoodExpense();
                food.setExpense(expense);
                food.setCedis(cedis);
                food.setWeight(dto.getWeight());

                foodRepository.save(food);
        }

        private void createVehicleExpense(Expense expense, VehicleExpenseCreateDTO dto) {
                if (dto == null) {
                        throw new IllegalArgumentException("Vehicle data required");
                }

                Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Vehicle not found: " + dto.getVehicleId()));

                Employee employee = employeeRepository.findById(dto.getEmployeeId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Employee not found: " + dto.getEmployeeId()));

                VehicleExpense vehicleExpense = new VehicleExpense();
                vehicleExpense.setExpense(expense);
                vehicleExpense.setVehicle(vehicle);
                vehicleExpense.setEmployee(employee);
                vehicleExpense.setCategory(dto.getCategory());

                vehicleExpenseRepository.save(vehicleExpense);
        }

        public List<ExpenseResponseDTO> getLatest() {
                Pageable pageable = PageRequest.of(0, 15);

                return expenseRepository.findAllByOrderByDateDesc(pageable)
                                .stream()
                                .map(this::getFlatResponse)
                                .toList();
        }

        public List<ExpenseResponseDTO> getBetween(
                        LocalDate start,
                        LocalDate end) {
                return expenseRepository.findFlatBetween(start, end)
                                .stream()
                                .map(expenseMapper::toResponse)
                                .toList();
        }

        @Transactional
        public ExpenseResponseDTO updateExpense(
                        Long expenseId,
                        ExpenseUpdateRequestDTO request) {

                Expense expense = getExpenseOrThrow(expenseId);

                updateBase(expense, request);

                ExpenseType type = expense.getCategoryCode().getType();

                switch (type) {
                        case FOOD -> updateFood(expense, request.getFood());
                        case FUEL -> updateFuel(expense, request.getFuel());
                        case VEHICLE -> updateVehicle(expense, request.getVehicle());
                        case GENERIC -> {
                                // no-op
                        }
                }

                return getFlatResponse(expense);
        }

        private void updateFood(Expense expense, FoodExpenseUpdateDTO dto) {

                validateType(expense, ExpenseType.FOOD);

                if (dto == null) {
                        throw new IllegalArgumentException("Food payload is required");
                }

                FoodExpense food = foodRepository.findById(expense.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "FoodExpense not found for expense " + expense.getId()));

                Cedis cedis = cedisRepository.findById(dto.getCedisId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Cedis not found: " + dto.getCedisId()));

                food.setCedis(cedis);
                food.setWeight(dto.getWeight());
        }

        private void updateFuel(Expense expense, FuelExpenseUpdateDTO dto) {

                validateType(expense, ExpenseType.FUEL);

                if (dto == null) {
                        throw new IllegalArgumentException("Fuel payload is required");
                }

                FuelExpense fuel = fuelRepository.findById(expense.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "FuelExpense not found for expense " + expense.getId()));

                fuel.setVehicle(
                                entityManager.getReference(Vehicle.class, dto.getVehicleId()));
                fuel.setEmployee(
                                entityManager.getReference(Employee.class, dto.getEmployeeId()));
                fuel.setRoute(
                                entityManager.getReference(Route.class, dto.getRouteId()));
        }

        private void updateVehicle(Expense expense, VehicleExpenseUpdateDTO dto) {

                validateType(expense, ExpenseType.VEHICLE);

                if (dto == null) {
                        throw new IllegalArgumentException("Vehicle payload is required");
                }

                VehicleExpense vehicleExpense = vehicleExpenseRepository
                                .findById(expense.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "VehicleExpense not found for expense " + expense.getId()));

                vehicleExpense.setVehicle(
                                entityManager.getReference(Vehicle.class, dto.getVehicleId()));
                vehicleExpense.setEmployee(
                                entityManager.getReference(Employee.class, dto.getEmployeeId()));
                vehicleExpense.setCategory(dto.getCategory());
        }

        private ExpenseResponseDTO getFlatResponse(Expense expense) {
                if (expense == null) {
                        throw new EntityNotFoundException("Expense not found");
                }

                // Food
                FoodExpense fe = expense.getFoodExpense();
                Long cedisId = null;
                String cedisName = null;
                BigDecimal weight = null;
                if (fe != null && fe.getCedis() != null) {
                        cedisId = fe.getCedis().getId();
                        cedisName = fe.getCedis().getName();
                        weight = fe.getWeight();
                }

                // Vehicle
                VehicleExpense ve = expense.getVehicleExpense();
                Long vehicleId = null;
                String vehicleName = null;
                Long employeeId = null;
                String employeeName = null;
                VehicleExpenseCategory vehicleCategory = null;
                if (ve != null) {
                        if (ve.getVehicle() != null) {
                                vehicleId = ve.getVehicle().getId();
                                vehicleName = ve.getVehicle().getName();
                        }
                        if (ve.getEmployee() != null) {
                                employeeId = ve.getEmployee().getId();
                                employeeName = ve.getEmployee().getName();
                        }
                        vehicleCategory = ve.getCategory();
                }

                // Fuel
                FuelExpense fu = expense.getFuelExpense();
                Long routeId = null;
                String routeName = null;
                ;
                if (fu != null && fu.getRoute() != null) {
                        if (fu.getRoute() != null) {
                                routeId = fu.getRoute().getId();
                                routeName = fu.getRoute().getName();
                        }

                        if (fu.getVehicle() != null) {
                                vehicleId = fu.getVehicle().getId();
                                vehicleName = fu.getVehicle().getName();
                        }

                        if (fu.getEmployee() != null) {
                                employeeId = fu.getEmployee().getId();
                                employeeName = fu.getEmployee().getName();
                        }
                }

                ExpenseFlatRowDTO flat = new ExpenseFlatRowDTO(
                                expense.getId(),
                                expense.getCategoryCode(),
                                expense.getCategoryCode() != null
                                                ? categoryRepository.findById(expense.getCategoryCode())
                                                                .map(ExpenseCategory::getName).orElse(null)
                                                : null,
                                expense.getCategoryCode() != null
                                                ? categoryRepository.findById(expense.getCategoryCode())
                                                                .map(ExpenseCategory::getExpenseType).orElse(null)
                                                : null,
                                expense.getReason(),
                                expense.getAmount(),
                                expense.getDate(),
                                cedisId,
                                cedisName,
                                weight != null ? weight : null,
                                vehicleId,
                                vehicleName,
                                employeeId,
                                employeeName,
                                vehicleCategory,
                                routeId,
                                routeName);

                return expenseMapper.toResponse(flat);
        }

        private void validateType(Expense expense, ExpenseType expected) {
                ExpenseType actual = expense.getCategoryCode().getType();
                if (actual != expected) {
                        throw new IllegalStateException(
                                        "Expense " + expense.getId() +
                                                        " is of type " + actual +
                                                        ", expected " + expected);
                }
        }

        private Expense getExpenseOrThrow(Long expenseId) {
                return expenseRepository.findById(expenseId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Expense not found: " + expenseId));
        }

        private void updateBase(Expense expense, ExpenseUpdateRequestDTO request) {
                expense.setReason(request.getReason());
                expense.setAmount(request.getAmount());
                expense.setDate(request.getDate());
        }

}
