package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

//    @GetMapping
//    public String displayJobs(@RequestParam(required = false) Integer employerId, Model model) {
//
//        if (employerId == null) {
//            model.addAttribute("title", "All Jobs");
//            model.addAttribute("events", jobRepository.findAll());
//        } else {
//            Optional<Employer> result = employerRepository.findById(employerId);
//            if (result.isEmpty()) {
//                model.addAttribute("title", "Invalid Employer ID: " + employerId);
//            } else {
//                Employer employer = result.get();
//                model.addAttribute("title", "Events in category: " + employer.getName());
//                model.addAttribute("events", employer.getJobs());
//            }
//        }
//
//        return "index";
//    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,@RequestParam int employerId, @RequestParam List<Integer> skills, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("employers", employerRepository.findAll());
            model.addAttribute("skills", skillRepository.findAll());

            return "add";
        }
        Optional<Employer> optEmployer = employerRepository.findById(employerId);
        Employer employer = (Employer) optEmployer.get();
        newJob.setEmployer(employer);

        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        jobRepository.save(newJob);
        model.addAttribute("jobs", jobRepository.findAll());
        return "index";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> optJob = jobRepository.findById(jobId);

        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect:../";
        }
    }
}
