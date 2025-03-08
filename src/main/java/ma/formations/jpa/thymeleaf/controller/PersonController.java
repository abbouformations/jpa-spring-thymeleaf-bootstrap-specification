package ma.formations.jpa.thymeleaf.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class PersonController {

    private IService service;

    @GetMapping(value = {"/", "/persons"})
    public String getAll(Model model,
                         @RequestParam(required = false) String firstname,
                         @RequestParam(required = false) String lastname,
                         @RequestParam(required = false) String age,
                         @RequestParam(required = false) String city,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "3") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<PersonDto> personsPage = service.findAll(firstname, lastname, age, city, paging);

            List<PersonDto> persons = personsPage.getContent();

            model.addAttribute("persons", persons);
            model.addAttribute("currentPage", personsPage.getNumber() + 1);
            model.addAttribute("totalItems", personsPage.getTotalElements());
            model.addAttribute("totalPages", personsPage.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("persons", (persons == null) ? new ArrayList<>() : persons);
            if (firstname != null)
                model.addAttribute("firstname", firstname);
            if (lastname != null)
                model.addAttribute("lastname", lastname);
            if (age != null)
                model.addAttribute("age", age);
            if (city != null)
                model.addAttribute("city", city);

        } catch (Exception e) {
            log.error("error in getAll() : {}", e);
            model.addAttribute("message", e.getMessage());
        }
        return "persons";
    }

    @GetMapping("/persons/new")
    public String addPerson(Model model) {
        PersonDto person = new PersonDto();
        person.setMarried(true);
        model.addAttribute("person", person);
        model.addAttribute("pageTitle", "Create new Person");
        return "person_form";
    }

    @PostMapping("/persons/save")
    public String savePerson(PersonDto person, RedirectAttributes redirectAttributes) {
        try {
            service.save(person);
            redirectAttributes.addFlashAttribute("message", "The Person has been saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/persons";
    }

    @GetMapping("/persons/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PersonDto person = service.findById(id);
            model.addAttribute("person", person);
            model.addAttribute("pageTitle", "Edit Person (ID: " + id + ")");
            return "person_form";
        } catch (Exception e) {
            log.error("error in editPerson() : {}", e);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/persons";
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/persons/{id}/married/{status}")
    public String updatePersonStatus(@PathVariable("id") Long id, @PathVariable("status") boolean married, RedirectAttributes redirectAttributes) {
        try {
            PersonDto personFound = service.findById(id);
            personFound.setMarried(married);
            service.save(personFound);
            String status = married ? "married" : "single";
            String message = "The Person id=" + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            log.error("error in updatePersonStatus() : {}", e);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return "redirect:/persons";
    }

    @GetMapping(value = {"/persons/byCity"})
    public String groupByCity(Model model) {
        try {
            List<PersonsByCityDto> personsByCityDto = service.countPersonsByCity();
            model.addAttribute("personsByCityDto", (personsByCityDto == null) ? new ArrayList<>() : personsByCityDto);
        } catch (Exception e) {
            log.error("error in groupByCity() : {}", e);
            model.addAttribute("message", e.getMessage());
        }
        return "personsGroupByCity";
    }

    @GetMapping(value = {"/persons/byAgeAndCity/edit"})
    public String editPagePersonsByAgeAndCity(Model model) {
        model.addAttribute("persons", new ArrayList<>());
        return "personsByAgeAndCity";
    }

    @GetMapping(value = {"/persons/byAgeAndCity"})
    public String personsByAgeAndCity(Model model, @Param("minAge") String minAge, @Param("maxAge") String maxAge, @Param("city") String city) {
        try {
            List<PersonDto> persons = service.personsByAgeAndCity(minAge, maxAge, city);
            model.addAttribute("persons", (persons == null) ? new ArrayList<>() : persons);
            if (minAge != null)
                model.addAttribute("minAge", minAge);
            if (maxAge != null)
                model.addAttribute("maxAge", maxAge);
            if (city != null)
                model.addAttribute("city", city);
        } catch (Exception e) {
            log.error("error in personsByAgeAndCity() : {}", e);
            model.addAttribute("message", e.getMessage());
        }
        return "personsByAgeAndCity";
    }


}