package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.payloads.Address.AddressDTO;
import com.deltasmarttech.companyorganization.payloads.Department.*;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeRequest;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.EmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private TownRepository townRepository;
	@Autowired
	private DepartmentTypeRepository departmentTypeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public DepartmentDTO createDepartment(
			DepartmentDTO departmentDTO,
			Integer companyId,
			Integer departmentTypeId,
			Integer townId) {

		// Does the company exist?
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Company", "id", companyId));

		// Is the company active?
		if(!company.isActive()) {
			throw new APIException(company.getName() + " is passive!");
		}

		// Does the department type exist?
		DepartmentType departmentType = departmentTypeRepository.findById(departmentTypeId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Department type", "id", departmentTypeId));

		// Is the department type active?
		if(!departmentType.isActive()) {
			throw new APIException(departmentType.getName() + " is passive!");
		}

		// Does the town exist?
		Town town = townRepository.findById(townId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Town", "id", townId));

		// Does the company already have the same dept.?
		boolean isDepartmentNotPresent = company.getDepartments().stream()
				.noneMatch(department -> department.getName().equalsIgnoreCase(departmentDTO.getName()));

		if(isDepartmentNotPresent) {
			Department department = new Department();
			department.setName(departmentDTO.getName());
			department.setAddressDetail(departmentDTO.getAddressDetail());
			department.setCompany(company);
			department.setDepartmentType(departmentType);
			department.setTown(town);
			department.setActive(true);
			department = departmentRepository.save(department);

			company.getDepartments().add(department);
			companyRepository.save(company);

			DepartmentDTO savedDepartmentDTO = converttoDepartmentDTO(department);

			return savedDepartmentDTO;
		} else {
			throw new APIException("Department already exists!");
		}
	}

	@Override
	public DepartmentResponse getAllDepartmentsByCompany(
			Integer companyId,
			Integer pageNumber,
		 	Integer pageSize,
			String sortBy,
			String sortOrder) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Company", "id", companyId));

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Department> pageProducts = departmentRepository.findByCompanyOrderByNameAsc(company, pageDetails);

		List<Department> departments = pageProducts.getContent();

		if(departments.isEmpty()){
			throw new APIException(company.getName() + " company does not have any departments.");
		}

		List<DepartmentDTO> departmentDTOS = departments.stream()
				.map(this::converttoDepartmentDTO)
				.toList();

		DepartmentResponse departmentResponse = new DepartmentResponse();
		departmentResponse.setContent(departmentDTOS);
		departmentResponse.setPageNumber(pageProducts.getNumber());
		departmentResponse.setPageSize(pageProducts.getSize());
		departmentResponse.setTotalElements(pageProducts.getTotalElements());
		departmentResponse.setTotalPages(pageProducts.getTotalPages());
		departmentResponse.setLastPage(pageProducts.isLast());

		return departmentResponse;
	}


	@Override
	public DepartmentDTO deleteDepartment(Integer companyId, Integer departmentId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new APIException("Company not found"));
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new APIException("Department not found"));

		if (!department.getCompany().getId().equals(company.getId())) {
			throw new APIException("The department does not belong to the given company.");
		}

		department.setActive(false);
		department.setDeletedAt(LocalDateTime.now());
		departmentRepository.save(department);

		DepartmentDTO departmentDTO = converttoDepartmentDTO(department);
		return departmentDTO;
	}


	public DepartmentDTO assignManager(ManagerDTO manager, Integer companyId, Integer departmentId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		if(!company.isActive()) {
			throw new APIException(company.getName() + " is passive!");
		}

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if(!department.isActive()) {
			throw new APIException(department.getName() + " is passive!");
		}

		if (!department.getCompany().equals(company)) {
			throw new IllegalArgumentException("The department does not belong to the given company.");
		}

		User userManager = userRepository.findByEmail(manager.getEmail())
				.orElseThrow(() -> new APIException("This user cannot be found!"));

		if(!userManager.isActive() || !userManager.isEnabled()) {
			throw new APIException("You cannot make this user a manager because " +
					"he/she is inactive or has not yet confirmed his/her account.");
		}

		boolean hasManager = department.getEmployees().stream()
				.anyMatch(user -> user.getRole().getRoleName().equals(AppRole.MANAGER));

		if (hasManager) {
			throw new APIException("This department already has a manager.");
		}

		department.getEmployees().add(userManager);
		userManager.setDepartment(department);

		userRepository.save(userManager);
		return converttoDepartmentDTO(departmentRepository.save(department));
	}

	@Override
	public DepartmentDTO deleteManager(Integer companyId, Integer departmentId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		if(!company.isActive()) {
			throw new APIException(company.getName() + " is passive!");
		}

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if(!department.isActive()) {
			throw new APIException(department.getName() + " is passive!");
		}

		if (!department.getCompany().equals(company)) {
			throw new IllegalArgumentException("The department does not belong to the given company.");
		}

		boolean hasManager = department.getEmployees().stream()
				.anyMatch(user -> user.getRole().getRoleName().equals(AppRole.MANAGER));

		if (!hasManager) {
			throw new APIException("This department does not have a manager. You cannot perform this action.");
		}

		List<User> employees = department.getEmployees();
		User manager = null;
		for (User employee : employees) {
			if (employee.getRole().getRoleName().name().equals("MANAGER")) {
				manager = employee;
			}
		}

		department.getEmployees().remove(manager);
		manager.setDepartment(null);

		userRepository.save(manager);
		return converttoDepartmentDTO(departmentRepository.save(department));
	}

	@Override
	public DepartmentDTO addEmployee(AddOrRemoveEmployeeRequest employee,
									 Integer companyId,
									 Integer departmentId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		User employeeUser = userRepository.findByEmail(employee.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", employee.getEmail()));

		if(department.getEmployees().contains(employeeUser)) {
			throw new APIException("This user is already in this department!");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User manager = userRepository.findByEmail(authentication.getName())
				.orElse(null);

		if (
				(authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")) &&
						department.getManager().getId() == manager.getId()) ||
						authentication.getAuthorities().stream()
								.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

			if (employeeUser.getRole().getRoleName().name().equalsIgnoreCase("MANAGER")) {
				throw new APIException("This department has already a manager!");
			}
			employeeUser.setDepartment(department);
			userRepository.save(employeeUser);

			department.getEmployees().add(employeeUser);
			departmentRepository.save(department);
		} else {
			throw new APIException("You do not have to perform this action.");
		}

		return converttoDepartmentDTO(department);

	}

	@Override
	@Transactional
	public AddOrRemoveEmployeeResponse deleteEmployee(Integer employeeId, Integer companyId, Integer departmentId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User managerOrAdmin = userRepository.findByEmail(authentication.getName())
				.orElse(null);

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if (!company.getDepartments().contains(department)) {
			throw new APIException("The department does not belong to the specified company.");
		}

		User employeeUser = userRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", employeeId));

		if (employeeUser.getDepartment() == null) {
			throw new APIException("The employee is not associated with any department");
		}

		if (!employeeUser.getDepartment().equals(department)) {
			throw new IllegalArgumentException("The employee is not associated with the specified department.");
		}

		if (
				(authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")) &&
						department.getManager().getId() == managerOrAdmin.getId()) ||
						authentication.getAuthorities().stream()
								.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

			if (department.getManager() != null && department.getManager().equals(employeeUser)) {
				department.setManager(null);
			}

			employeeUser.setDepartment(null);
			userRepository.save(employeeUser);

		} else {
			throw new APIException("You do not have to perform this action.");
		}

		return new AddOrRemoveEmployeeResponse(employeeUser.getEmail(),
				employeeUser.getName(),
				employeeUser.getSurname(),
				employeeUser.getRole().getRoleName().name());
	}


	@Override
	public EmployeeResponse showAllEmployees(
			Integer companyId,
			Integer departmentId,
			Integer pageNumber,
			Integer pageSize,
			String sortBy,
			String sortOrder) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User managerOrAdmin = userRepository.findByEmail(authentication.getName())
				.orElse(null);

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if (!company.getDepartments().contains(department)) {
			throw new APIException("Department does not belong to the specified company");
		}

		if (!department.getId().equals(managerOrAdmin.getDepartment().getId()) && !managerOrAdmin.getRole().getRoleName().name().equalsIgnoreCase("ADMIN")) {
			throw new APIException("You do not have permission to access this resource.");
		}

		EmployeeResponse employeeResponse = new EmployeeResponse();
		List<AddOrRemoveEmployeeResponse> addOrRemoveEmployeeResponse = department.getEmployees()
				.stream()
				.map(employee -> {
					AddOrRemoveEmployeeResponse response = modelMapper.map(employee, AddOrRemoveEmployeeResponse.class);
					response.setRole(employee.getRole().getRoleName().name());
					return response;
				})
				.toList();
		employeeResponse.setEmployees(addOrRemoveEmployeeResponse);

		return employeeResponse;
	}

	public DepartmentDTO converttoDepartmentDTO(Department department) {

		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setId(department.getId());
		departmentDTO.setName(department.getName());

		AddressDTO address = new AddressDTO();
		address.setCityName(department.getTown().getRegion().getCity().getName());
		address.setRegionName(department.getTown().getRegion().getName());
		address.setTownName(department.getTown().getName());
		address.setId(department.getTown().getId());
		departmentDTO.setAddress(address);

		departmentDTO.setAddressDetail(department.getAddressDetail());
		departmentDTO.setDepartmentType(modelMapper.map(department.getDepartmentType(), DepartmentTypeDTO.class));

		List<AddOrRemoveEmployeeResponse> employees = department.getEmployees()
				.stream().
				map(employee -> {
					AddOrRemoveEmployeeResponse employeeResponse = new AddOrRemoveEmployeeResponse();
					employeeResponse.setEmail(employee.getEmail());
					employeeResponse.setName(employee.getName());
					employeeResponse.setSurname(employee.getSurname());
					employeeResponse.setRole(employee.getRole().getRoleName().name());
					return employeeResponse;
				})
				.toList();

		departmentDTO.setActive(department.isActive());

		return departmentDTO;
	}

	public ManagerDTO getManager(Department department) {

		List<User> employees = department.getEmployees();
		if (employees == null) {
			return null;
		}

		ManagerDTO manager = null;
		for (User employee : employees) {
			if (employee.getRole().getRoleName().name().equals("MANAGER")) {
				manager = modelMapper.map(employee, ManagerDTO.class);
			}
		}
		return manager;
	}
}
