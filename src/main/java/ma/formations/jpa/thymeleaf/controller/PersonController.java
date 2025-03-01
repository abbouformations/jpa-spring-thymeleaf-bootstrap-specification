package ma.formations.jpa.thymeleaf.controller;

import lombok.AllArgsConstructor;
import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.service.IService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PersonController {
    private IService service;

    @GetMapping(value = {"/", "/persons"})
    public String getAll(Model model, @Param("firstname") String firstname,
                         @Param("lastname") String lastname,
                         @Param("age") String age,
                         @Param("city") String city
    ) {
        try {
            Integer ageInt = (age == null || age.trim().equals("")) ? 0 : Integer.parseInt(age);
            List<PersonDto> persons = service.findAll(firstname, lastname, ageInt, city);
            if (firstname != null)
                model.addAttribute("firstname", firstname);
            if (lastname != null)
                model.addAttribute("lastname", lastname);
            if (age != null)
                model.addAttribute("age", age);
            if (city != null)
                model.addAttribute("city", city);
            model.addAttribute("persons", persons);
        } catch (Exception e) {
            e.printStackTrace();
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
            model.addAttribute("personsByCityDto", personsByCityDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
        }
        return "personsGroupByCity";
    }

    @GetMapping(value = {"/persons/byAgeAndCity"})
    public String personsByAgeAndCity(Model model, @Param("minAge") String minAge, @Param("maxAge") String maxAge, @Param("city") String city) {
        List<PersonDto> persons = new ArrayList<>();
        if ((minAge == null || minAge.trim().equals("")) || (maxAge == null || maxAge.trim().equals("")) || (city == null || city.trim().equals(""))) {
            model.addAttribute("persons", persons);
            return "personsByAgeAndCity";
        }

        try {
            persons = service.personsByAgeAndCity(Integer.parseInt(minAge), Integer.parseInt(maxAge), city);
            model.addAttribute("persons", persons);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
        }
        return "personsByAgeAndCity";
    }
}