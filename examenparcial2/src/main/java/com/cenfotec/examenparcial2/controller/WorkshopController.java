package com.cenfotec.examenparcial2.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import com.cenfotec.examenparcial2.domain.Activity;
import com.cenfotec.examenparcial2.domain.Category;
import com.cenfotec.examenparcial2.domain.Workshop;
import com.cenfotec.examenparcial2.service.ActivityService;
import com.cenfotec.examenparcial2.service.CategoryService;
import com.cenfotec.examenparcial2.service.WorkshopService;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class WorkshopController {

    @Autowired
    WorkshopService workshopService;

    @Autowired
    ActivityService activityService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/")
    public String home(Model model) {
        return "index";
    }

    @RequestMapping(value = "/insertar", method = RequestMethod.GET)
    public String insertarPage(Model model) {
        model.addAttribute(new Workshop());
        model.addAttribute("category", categoryService.getAll());
        return "insertar";
    }

    @RequestMapping(value = "/insertarCategory", method = RequestMethod.GET)
    public String insertarCategorias(Model model) {
        model.addAttribute(new Category());
        return "insertarCategory";
    }

    @RequestMapping(value = "/insertar", method = RequestMethod.POST)
    public String insertarAction(Workshop workshop, BindingResult result, Model model) {
        workshopService.save(workshop);
        return "index";
    }

    @RequestMapping(value = "/insertarCategory", method = RequestMethod.POST)
    public String insertarCategoria(Category category, BindingResult result, Model model) {
        categoryService.save(category);
        return "index";
    }

    @RequestMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("workshops", workshopService.getAll());
        return "listar";
    }

    @RequestMapping(value = "/buscarNombre", method = RequestMethod.GET)
    public String buscarPage(Model model) {
        model.addAttribute(new Workshop());
        model.addAttribute("category", categoryService.getAll());
        return "buscarNombre";
    }

    @RequestMapping("/listarNombre")
    public String listarNombre(Model model, @RequestParam String name) {
        model.addAttribute("workshopNombre", workshopService.find(name));
        return "listarNombre";
    }

    @RequestMapping("/listarCat")
    public String listarCat(Model model, @RequestParam String category) {
        model.addAttribute("workshopCat", workshopService.findCat(category));
        return "listarCat";
    }

    @RequestMapping("/edit/{id}")
    public String findWorkshopToEdit(Model model, @PathVariable long id) {
        Optional<Workshop> possibleData = workshopService.get(id);
        if (possibleData.isPresent()) {
            model.addAttribute("category", categoryService.getAll());
            model.addAttribute("workshopToEdit", possibleData.get());
            return "edit";
        }
        return "notfound";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String saveEdition(Workshop workshop, Model model, @PathVariable long id) {
        workshopService.save(workshop);
        return "index";
    }

    @RequestMapping(value = "/detalle/{id}")
    public String saveEdition(Model model, @PathVariable long id) {
        Optional<Workshop> possibleData = workshopService.get(id);
        if (possibleData.isPresent()) {
            model.addAttribute("workshopData", possibleData.get());
            return "detalle";
        }
        return "notfound";
    }

    @RequestMapping(value = "/agregarActividad/{id}")
    public String recoverForAddActivity(Model model, @PathVariable long id) {
        Optional<Workshop> workshop = workshopService.get(id);
        Activity newActivity = new Activity();
        if (workshop.isPresent()) {
            newActivity.setWorkshop(workshop.get());
            model.addAttribute("workshop", workshop.get());
            model.addAttribute("activity", newActivity);
            return "agregarActividad";
        }
        return "notfound";
    }

    @RequestMapping(value = "/agregarActividad/{id}", method = RequestMethod.POST)
    public String saveActivity(Activity activity, Model model, @PathVariable long id) {
        Optional<Workshop> antology = workshopService.get(id);
        if (antology.isPresent()) {
            activity.setWorkshop(antology.get());
            activityService.save(activity);
            return "index";
        }
        return "errorArticle";
		/*
		Article newArticle = new Article();
		if (antology.isPresent()) {
			Antology updatedAntology = antology.get();
			updatedAntology.getArticles().add(article);
			anthologyService.save(updatedAntology);
			return "listar";
		}*/
        /*return "notfound";*/
    }

    @RequestMapping("/listarCategorias")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoryService.getAll());
        return "listarCategorias";
    }

    @RequestMapping("/editCat/{id}")
    public String findAnthologyToEdit(Model model, @PathVariable long id) {
        Optional<Category> possibleData = categoryService.get(id);
        if (possibleData.isPresent()) {
            model.addAttribute("category", possibleData.get());
            return "editCat";
        }
        return "notfound";
    }

    @RequestMapping(value = "/editCat/{id}", method = RequestMethod.POST)
    public String saveEdition(Category antology, Model model, @PathVariable long id) {
        categoryService.save(antology);
        return "index";
    }

    @RequestMapping(value = "/word/{id}")
    public String getGeneratedDocument(Model model, @PathVariable long id) throws IOException {
        Optional<Workshop> possibleData = workshopService.get(id);
        if (possibleData.isPresent()) {
            int tiempo=0;
            String actividades="";
            for (Activity act: possibleData.get().getActivities()) {
               tiempo+=act.getTime();
            }
            XWPFDocument document = new XWPFDocument();
            String output = possibleData.get().getName() + ".docx";
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText(possibleData.get().getName());
            titleRun.setColor("000000");
            titleRun.setBold(true);
            titleRun.setFontFamily("Arial");
            titleRun.setFontSize(20);

            XWPFParagraph title2=document.createParagraph();
            title2.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun2 = title2.createRun();
            titleRun2.setText("Duracion: "+tiempo+" minutos");
            titleRun2.setColor("000000");
            titleRun2.setBold(true);
            titleRun2.setFontFamily("Arial");
            titleRun2.setFontSize(20);

            XWPFParagraph subTitle = document.createParagraph();
            subTitle.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subTitleRun = subTitle.createRun();
            subTitleRun.setText("Impartido por :"+possibleData.get().getAuthor());
            titleRun2.setColor("000000");
            titleRun2.setBold(true);
            titleRun2.setFontFamily("Arial");
            titleRun2.setFontSize(20);

            XWPFParagraph sectionTitle = document.createParagraph();
            XWPFRun sectionTRun = sectionTitle.createRun();
            sectionTRun.setText("Categoria "+possibleData.get().getCategory());
            sectionTRun.setColor("000000");
            sectionTRun.setBold(true);
            sectionTRun.setFontFamily("Arial");

            for (Activity act: possibleData.get().getActivities()) {
                XWPFParagraph sub = document.createParagraph();
                sub.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun subTitleRu = sub.createRun();
                subTitleRu.setColor("000000");
                subTitleRu.setFontFamily("Arial");
                subTitleRu.setFontSize(16);
                subTitleRu.setTextPosition(20);
                subTitleRu.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
                actividades= act.getName()+"\n"+
                        "Descripcion "+act.getDescription()+"\n"+
                        "Nota "+act.getText() +"\n";
                subTitleRun.setText(actividades);

            }

            FileOutputStream out = new FileOutputStream(output);
            document.write(out);
            out.close();
            document.close();
            return "index";
        } else {
            return "notfound";
        }
    }
}
