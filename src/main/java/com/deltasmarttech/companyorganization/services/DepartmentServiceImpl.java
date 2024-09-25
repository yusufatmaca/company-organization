package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.payloads.Department.*;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeRequest;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.EmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	@Transactional
	public DepartmentDTO assignManager(ManagerDTO managerDTO, Integer companyId, Integer departmentId) {
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		if (!company.isActive()) {
			throw new APIException(company.getName() + " is passive!");
		}

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if (!department.isActive()) {
			throw new APIException(department.getName() + " is passive!");
		}

		if (!department.getCompany().equals(company)) {
			throw new IllegalArgumentException("The department does not belong to the given company.");
		}

		User manager = userRepository.findByEmail(managerDTO.getEmail())
				.orElseThrow(() -> new APIException("This user cannot be found!"));

		if (!manager.isActive() || !manager.isEnabled()) {
			throw new APIException("You cannot make this user a manager because " +
					"he/she is inactive or has not yet confirmed his/her account.");
		}

		if (manager.getRole().getRoleName().name().equalsIgnoreCase("EMPLOYEE")) {
			Department currentDepartment = manager.getDepartment();
			if (currentDepartment != null) {
				manager.setDepartment(null);
			}
		}

		// Set the manager for the department
		department.setManager(manager);
		manager.getManagedDepartments().add(department);
		manager.setDepartment(department);

		// Set the user's role to MANAGER if it's not already
		if (!manager.getRole().getRoleName().equals(AppRole.MANAGER)) {
			Role managerRole = roleRepository.findByRoleName(AppRole.MANAGER)
					.orElseThrow(() -> new APIException("MANAGER role not found"));
			manager.setRole(managerRole);
		}
		userRepository.save(manager);
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

		User manager = department.getManager();

		if (manager == null) {
			throw new APIException("This department does not have a manager. You cannot perform this action.");
		}
		department.setManager(null);
		departmentRepository.save(department);

		List<Department> deptManaged = manager.getManagedDepartments();
		deptManaged.remove(department);

		if (deptManaged.isEmpty()) {
			manager.setRole(roleRepository.findByRoleName(AppRole.valueOf("EMPLOYEE")).orElseThrow(() -> new APIException("")));
		} else {
			manager.setDepartment(deptManaged.get(0));
		}

		userRepository.save(manager);
		return converttoDepartmentDTO(departmentRepository.save(department));
	}

	@Override
	public AddOrRemoveEmployeeResponse addEmployee(AddOrRemoveEmployeeRequest employee,
									 Integer companyId,
									 Integer departmentId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		User employeeUser = userRepository.findByEmail(employee.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", employee.getEmail()));

		if(employeeUser.getDepartment() != null) {
			throw new APIException("First of all, the user must be deleted from " + employeeUser.getDepartment().getName() + ". For this process, please contact the ADMIN or the MANAGER " + (employeeUser.getDepartment().getManager() != null ? employeeUser.getDepartment().getManager().getName() : "") + " working in this department.");
		}

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
				throw new APIException("You can only add users with `EMPLOYEE` role using this service.");
			}
			employeeUser.setDepartment(department);
			userRepository.save(employeeUser);

			department.getEmployees().add(employeeUser);
			departmentRepository.save(department);
		} else {
			throw new APIException("You do not have to perform this action.");
		}

		return convertToAddOrRemoveEmployeeResponse(employeeUser);
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
				employeeUser.getRole().getRoleName().name(),
				employeeUser.getDepartment().getId()
				);
	}

	@Override
	public DepartmentDTO updateDepartment(Integer companyId, Integer departmentId, DepartmentDTO departmentDTO) {

		Company existingCompany = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
		if (!existingCompany.isActive()) {
			throw new APIException("You cannot edit " + existingCompany.getName() + " because it's not active!");
		}

		Department existingDepartment = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));
		if (!existingDepartment.isActive()) {
			throw new APIException("You cannot edit " + existingDepartment.getName() + " because it's not active!");
		}

		if (existingDepartment.getCompany().getId() != existingCompany.getId()) {
			throw new APIException("This department does not belong to this specific company!");
		}

		if (departmentDTO.getName() != null) {
			existingDepartment.setName(departmentDTO.getName());
		}
		if (departmentDTO.getTown() != null && !departmentDTO.getTown().isBlank()) {
			Town town = townRepository.findByName(departmentDTO.getTown())
					.orElseThrow(() -> new ResourceNotFoundException("Town", "name", departmentDTO.getTown()));

			existingDepartment.setTown(town);
		}
		if (departmentDTO.getDepartmentType() != null) {
			DepartmentType departmentType = departmentTypeRepository.findByName(departmentDTO.getDepartmentType().getName())
					.orElseThrow(() -> new ResourceNotFoundException("DepartmentType", "name", departmentDTO.getDepartmentType().getName()));
			existingDepartment.setDepartmentType(departmentType);
		}
		if (departmentDTO.getAddressDetail() != null) {
			existingDepartment.setAddressDetail(departmentDTO.getAddressDetail());
		}

		Department updatedDepartment = departmentRepository.save(existingDepartment);

		return converttoDepartmentDTO(updatedDepartment);
	}

	@Override
	public DepartmentResponse getAllDepartments(
			Integer pageNumber,
			Integer pageSize,
			String sortBy,
			String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Department> departmentsPage = departmentRepository.findAll(pageable);
		List<Department> departmentList = departmentsPage.getContent();
		List<DepartmentDTO> departmentDTOList = departmentList.stream()
				.map(this::converttoDepartmentDTO)
				.collect(Collectors.toList());

		DepartmentResponse departmentResponse = new DepartmentResponse();
		departmentResponse.setContent(departmentDTOList);
		departmentResponse.setPageNumber(departmentsPage.getNumber());
		departmentResponse.setPageSize(departmentsPage.getSize());
		departmentResponse.setTotalElements(departmentsPage.getTotalElements());
		departmentResponse.setTotalPages(departmentsPage.getTotalPages());
		departmentResponse.setLastPage(departmentsPage.isLast());

		return departmentResponse;
	}

	public EmployeeResponse showAllAddableUsers(
			Integer companyId,
			Integer departmentId,
			Integer pageNumber,
			Integer pageSize,
			String sortBy,
			String sortOrder) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new APIException("User not found"));

		// Sorting setup
		Sort sort = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		if (currentUser.getRole().getRoleName() == AppRole.MANAGER) {

			// Get the list of departments managed by the current user
			List<Department> managedDepartments = currentUser.getManagedDepartments();

			// Check if the manager is authorized to query for this department
			boolean managesDepartment = managedDepartments.stream()
					.anyMatch(department -> department.getId().equals(departmentId));

			if (!managesDepartment) {
				throw new APIException("You do not manage this department.");
			}

			// Fetch employees not in the specified department (to be added to it) or with null department
			Page<User> employeePage = userRepository.findByRole_RoleNameAndDepartment_IdNotOrDepartmentIsNull(
					AppRole.EMPLOYEE, departmentId, pageable);

			return createEmployeeResponse(employeePage);

		} else if (currentUser.getRole().getRoleName() == AppRole.ADMIN) {

			// For ADMIN: fetch employees not in the specified department or with null department
			Page<User> employeePage = userRepository.findByRole_RoleNameAndDepartment_IdNotOrDepartmentIsNull(
					AppRole.EMPLOYEE, departmentId, pageable);

			return createEmployeeResponse(employeePage);
		}

		throw new APIException("You are not authorized to perform this action.");
	}

	private EmployeeResponse createEmployeeResponse(Page<User> employeePage) {
		List<AddOrRemoveEmployeeResponse> employeeResponses = employeePage.getContent().stream()
				.map(this::convertToAddOrRemoveEmployeeResponse)
				.collect(Collectors.toList());

		EmployeeResponse response = new EmployeeResponse();
		response.setEmployees(employeeResponses);
		response.setPageNumber(employeePage.getNumber());
		response.setPageSize(employeePage.getSize());
		response.setTotalElements(employeePage.getTotalElements());
		response.setTotalPages(employeePage.getTotalPages());
		response.setLastPage(employeePage.isLast());

		return response;
	}

	private AddOrRemoveEmployeeResponse convertToAddOrRemoveEmployeeResponse(User user) {
		return new AddOrRemoveEmployeeResponse(
				user.getEmail(),
				user.getName(),
				user.getSurname(),
				user.getRole().getRoleName().name(),
				user.getDepartment() != null ? user.getDepartment().getId() : null
		);
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
				.orElseThrow(() -> new APIException("User not found"));

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		if (!company.getDepartments().contains(department)) {
			throw new APIException("Department does not belong to the specified company");
		}

		if (!department.getId().equals(managerOrAdmin.getDepartment().getId())
				&& !managerOrAdmin.getRole().getRoleName().name().equalsIgnoreCase("ADMIN")) {
			throw new APIException("You do not have permission to access this resource.");
		}

		List<User> employees = department.getEmployees();

		// Sort employees by name based on sortOrder
		if (sortOrder.equalsIgnoreCase("asc")) {
			employees.sort(Comparator.comparing(User::getName));
		} else if (sortOrder.equalsIgnoreCase("desc")) {
			employees.sort(Comparator.comparing(User::getName).reversed());
		}

		// Paginate the sorted list
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> employeePage = getPaginatedEmployees(employees, pageable);

		return createEmployeeResponse(employeePage);
	}

	private Page<User> getPaginatedEmployees(List<User> employees, Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> paginatedList;

		if (employees.size() < startItem) {
			paginatedList = List.of(); // Return an empty list if no items to paginate
		} else {
			int toIndex = Math.min(startItem + pageSize, employees.size());
			paginatedList = employees.subList(startItem, toIndex); // Create sublist for pagination
		}

		return new PageImpl<>(paginatedList, pageable, employees.size());
	}

	public DepartmentDTO converttoDepartmentDTO(Department department) {

		DepartmentDTO departmentDTO = new DepartmentDTO();

		departmentDTO.setId(department.getId());
		departmentDTO.setName(department.getName());

		departmentDTO.setCity(department.getTown().getRegion().getCity().getName());
		departmentDTO.setRegion(department.getTown().getRegion().getName());
		departmentDTO.setTown(department.getTown().getName());

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
		departmentDTO.setCompanyId(department.getCompany().getId());
		if (department.getManager() != null) {
			departmentDTO.setManagerId(department.getManager().getId());
			departmentDTO.setManagerEmail(department.getManager().getEmail());
		}

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
