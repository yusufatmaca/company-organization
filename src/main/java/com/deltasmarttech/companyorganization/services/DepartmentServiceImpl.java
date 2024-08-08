package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
	public DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Integer companyId, Integer departmentTypeId, Integer townId) {

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
		if(departmentType.isActive() == false) {
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

			DepartmentDTO savedDepartmentDTO = modelMapper.map(department, DepartmentDTO.class);
			savedDepartmentDTO.setAddress(modelMapper.map(department.getTown().getRegion().getCity(), CityDTO.class));

			return savedDepartmentDTO;
		} else {
			throw new APIException("Department already exists!");
		}
	}

	@Override
	public DepartmentResponse getAllDepartmentsByCompany(Integer companyId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


		return null;
	}

	@Override
	public DepartmentDTO deleteDepartment(Integer companyTypeId, Integer departmentId) {

		return null;
	}

	@Override
	public DepartmentDTO assignManager(ManagerDTO manager, Integer companyId, Integer departmentId) {


		Company company = companyRepository.findById(companyId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Company", "id", companyId));

		if(!company.isActive()) {
			throw new APIException(company.getName() + " is passive!");
		}

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Department", "id", departmentId));

		if(!department.isActive()) {
			throw new APIException(department.getName() + " is passive!");
		}

		if (department.getManager() != null) {
			throw new APIException(department.getName() + " already has a manager!");
		}

		User user = userRepository.findByEmail(manager.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", manager.getEmail()));

		Role managerRole = roleRepository.findByRoleName(AppRole.MANAGER)
				.orElseGet(() -> {
					Role newManagerRole = new Role(AppRole.MANAGER);
					return roleRepository.save(newManagerRole);
				});
		user.setRole(managerRole);
		user.setDepartment(department);
		userRepository.save(user);


		department.setManager(user);
		departmentRepository.save(department);

		company.getDepartments().add(department);

		DepartmentDTO deptWithManager = modelMapper.map(department, DepartmentDTO.class);
		deptWithManager.setManagerName(department.getManager().getFirstName());
		return deptWithManager;
	}

	@Override
	public Department addEmployee(User employee, Department department, Company company) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User manager = userRepository.findByEmail(authentication.getName())
				.orElse(null);
		if (!authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")) ||
				department.getManager().getId() != manager.getId()) {
			throw new APIException("You are not authorized to perform this action!");
		}

		employee.setDepartment(department);

		department.getEmployees().add(employee);
		departmentRepository.save(department);

		companyRepository.save(company);

		return department;
	}

	@Override
	public Department deleteEmployee(User employee, Department department, Company company) {



		return department;
	}

	@Override
	public DepartmentDTO processEmployee(
			EmployeeDTO employeeDTO,
			Integer companyId,
			Integer departmentId,
			Integer operationType) {

		User employee = userRepository.findByEmail(employeeDTO.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", employeeDTO.getEmail()));

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

		Department updatedDepartment;

		if (operationType == 1) {
			updatedDepartment = addEmployee(employee, department, company);
		} else if (operationType == 2) {
			updatedDepartment = deleteEmployee(employee, department, company);
		} else {
			throw new APIException("Please enter an invalid operation type!");
		}

		return modelMapper.map(updatedDepartment, DepartmentDTO.class);
	}
}
