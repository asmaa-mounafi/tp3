package ma.enset.activite_pratique_n3.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.activite_pratique_n3.entities.Patient;
import ma.enset.activite_pratique_n3.repository.PatientRepository;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//injection via constrector
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/user/index")
    //public String index(Model model,int page,int size)
     public String index(Model model, @RequestParam (name = "page",defaultValue = "0") int page,
                         @RequestParam (name = "size",defaultValue = "4") int size,
                         @RequestParam (name = "keyword",defaultValue = "") String kw)
    {
        //List<Patient> patientList=patientRepository.findAll();
        // Page<Patient> patientpage=patientRepository.findAll(PageRequest.of(page,size));
        Page<Patient> patientpage=patientRepository.findByNomContains(kw,PageRequest.of(page,size));

        model.addAttribute("listPatients",patientpage.getContent());
        //stocke le total des pages
        model.addAttribute("pages",new int[patientpage.getTotalPages()]);
        //page current
        model.addAttribute("currentPage",page);
        //add keyword
        model.addAttribute("keyword",kw);

        return "patients";

    }

    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //Suppression
    public String delete(Long id,@RequestParam (name = "keyword",defaultValue = "") String keyword,@RequestParam (name = "page",defaultValue = "0") int page)
    {
        patientRepository.deleteById(id);
        //la redurection
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }



    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String formPatients(Model model)
    {
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
    //for adding and updating
    @PostMapping("/admin/save")
    public String save(Model model, @Valid  Patient patient, BindingResult bindingResult,@RequestParam (defaultValue = "0") int page,@RequestParam (defaultValue = "") String keyword)
    {
        if (bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;

    }
    @GetMapping("/admin/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //Suppression
    public String edit(Model model,Long id,@RequestParam (name = "keyword",defaultValue = "") String keyword,@RequestParam (name = "page",defaultValue = "0") int page)
    {
        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient==null)throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);

        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "editpatient";
    }

    @GetMapping("/")
    //Suppression
    public String home()
    {
        return "redirect:/user/index";
    }
}
