package com.tsystems.javaschool.controller;

import com.tsystems.javaschool.controller.exception.ExceptionTreatmentNotFound;
import com.tsystems.javaschool.model.dto.*;
import com.tsystems.javaschool.service.api.PatientService;
import com.tsystems.javaschool.service.api.TherapyService;
import com.tsystems.javaschool.service.api.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/treatment")
public class TreatmentController {

    private final TreatmentService treatmentService;
    private final PatientService patientService;
    private final TherapyService therapyService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService, PatientService patientService, TherapyService therapyService) {
        this.treatmentService = treatmentService;
        this.patientService = patientService;
        this.therapyService = therapyService;
    }

    @ModelAttribute("treatment")
    public TreatmentDto treatmentDto() {
        return new TreatmentDto();
    }

    @ModelAttribute("treatmentList")
    public List<TreatmentDto> treatmentsDto() {
        return treatmentService.findAll();
    }

    @GetMapping("/all")
    public String index(@ModelAttribute("treatmentList") @Valid List<TreatmentDto> treatmentList) {
        return "treatment/treatment-all";
    }

    @GetMapping("/patient/{id}")
    public String patientTreatments(@PathVariable("id") int id, Model model) {
        List<TreatmentDto> treatmentList = treatmentService.findByPatientId(id);
        model.addAttribute("treatmentList", treatmentList);
        return "treatment/treatment-patient";
    }

    @GetMapping("/add/{id}")
    public String newTreatment(@PathVariable("id") int id, Model model) {
        PatientDto patient = patientService.findById(id);
        model.addAttribute("patient", patient);
        return "treatment/treatment-add";
    }

    @PostMapping(value = "/add/{id}")
    public String addTreatment(@PathVariable("id") int id, @ModelAttribute("treatment") TreatmentDto treatment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();
        treatmentService.save(treatment, email, id);
        return "redirect:/treatment/all";
    }

    @GetMapping("/{id}")
    public String showTreatment(@PathVariable("id") int id, Model model) throws ExceptionTreatmentNotFound {
        TreatmentDto treatment = treatmentService.findById(id);

        List<TherapyDto> therapies = therapyService.findByTreatmentId(id);

        model.addAttribute("treatment", treatment);
        model.addAttribute("therapies", therapies);

        List<TherapyDaysDto> therapyDaysDto = new ArrayList<>();
        therapyDaysDto.add(new TherapyDaysDto("MONDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("TUESDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("WEDNESDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("THURSDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("FRIDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("SATURDAY", null));
        therapyDaysDto.add(new TherapyDaysDto("SUNDAY", null));

        TherapyDto therapyDto = new TherapyDto();
        DaysWrapper form = new DaysWrapper();
        form.setDays(therapyDaysDto);
        therapyDto.setWrapper(form);

        model.addAttribute("therapyPost", therapyDto);
        return "treatment/treatment-show";
    }

    @PostMapping(value = "/{id}")
    public String addTherapy(@PathVariable("id") int id,
                             @ModelAttribute("therapyPost") TherapyDto therapyDto) {
        treatmentService.addTherapy(id, therapyDto);
        return "redirect:/treatment/"+id;
    }

}
