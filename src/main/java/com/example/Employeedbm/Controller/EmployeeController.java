package com.example.Employeedbm.Controller;

import com.example.Employeedbm.Dao.EmpRepo;
import com.example.Employeedbm.Entity.Employee;
import org.apache.catalina.util.ErrorPageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    EmpRepo emprepo;
    @GetMapping("/")
    public String RegisterEmp()
    {
        return "index";
    }

    @GetMapping("/home")
    public String showData(String name, Model model)
    {
        int curPage=1;
        Pageable pageable = PageRequest.of(curPage-1, maxSize, Sort.by("id").descending());
        Page<Employee> page = emprepo.findAll(pageable);
        int totalPages = page.getTotalPages();
        List<Employee> listEmp = page.toList();

        model.addAttribute("listEmp", listEmp);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("curPage", curPage);
        return "EmpReg";
    }
    @PostMapping("/emps/")//after saving data
    public String SubmitData(Model model,Employee emp)
    {
        int curPage=1;
        emprepo.save(emp);
        Pageable pageable = PageRequest.of(curPage-1, maxSize, Sort.by("id").descending());
        Page<Employee> page = emprepo.findAll(pageable);
        int totalPages = page.getTotalPages();
        List<Employee> listEmp = page.toList();

        model.addAttribute("listEmp", listEmp);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("curPage", curPage);
        model.addAttribute("msg","Employee saved successfully");
        return "EmpReg";
    }

    @GetMapping("/emps/delete/{empID}/")
    public String SubmitData(Model model, @PathVariable long empID)
    {
        int curPage=1;
        emprepo.deleteById(empID);
        Pageable pageable = PageRequest.of(curPage-1, maxSize, Sort.by("id").descending());
        Page<Employee> page = emprepo.findAll(pageable);
        int totalPages = page.getTotalPages();
        List<Employee> listEmp = page.toList();

        model.addAttribute("listEmp", listEmp);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("curPage", curPage);
        model.addAttribute("msg","Employee deleted successfully");
        return "EmpReg";
    }
    int maxSize = 4;

    @GetMapping("/emp/reg/{curPage}/")
    public String empReg(@PathVariable int curPage,Model model)
    {
        Pageable pageable = PageRequest.of(curPage-1, maxSize, Sort.by("id").descending());
        Page<Employee> page = emprepo.findAll(pageable);
        int totalPages = page.getTotalPages();
        List<Employee> listEmp = page.toList();

        model.addAttribute("listEmp", listEmp);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("curPage", curPage);
        return "empReg";
    }


}
