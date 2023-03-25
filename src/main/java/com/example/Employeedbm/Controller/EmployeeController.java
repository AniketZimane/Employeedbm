package com.example.Employeedbm.Controller;

import com.example.Employeedbm.Dao.EmpRepo;
import com.example.Employeedbm.Entity.Employee;
import com.example.Employeedbm.helper.FileUploader;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    EmpRepo emprepo;
    @Autowired
    FileUploader uploader;
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
    public String SubmitData(Model model, Employee emp, MultipartFile file)
    {
        int curPage=1;
        Employee empNew = emprepo.save(emp);

        String fileNameOld = file.getOriginalFilename();
        String extension = fileNameOld.substring(fileNameOld.indexOf(".") + 1);
        String fileNameNew = empNew.getId() + "." + extension;

        System.out.println("Image New Name is " + fileNameNew);
        uploader.uploadFile(file, fileNameNew);
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
    @GetMapping("/login/")
    public String loginPage(Model model)
    {
        return "login";
    }

}
