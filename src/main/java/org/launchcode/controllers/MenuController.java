package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "menu")

public class MenuController {


    @Autowired
    MenuDao menuDao;

    @Autowired
    CheeseDao cheeseDao;


    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Cheese Festival Menus");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "New Menu Item");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors){

        if (errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);

        return "redirect:/menu/view/" + menu.getId();
    }


    @RequestMapping(value = "view/{theMenuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int theMenuId){
        Menu aNewMenu = menuDao.findById(theMenuId).get();

        model.addAttribute("menu", aNewMenu);
        model.addAttribute("title", aNewMenu.getName());

/*        model.addAttribute("title", aNewMenu.getTheMenuName());
        model.addAttribute("cheeses", aNewMenu.getCheeses());
        model.addAttribute("menuId", aNewMenu.getId());*/

//        model.addAttribute(menuDao.findById(theMenuId));
        return "menu/view";
    }


    @RequestMapping(value="add-item/{theMenuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int theMenuId){
        Menu brandNewMenu = menuDao.findById(theMenuId).orElse(null);
        AddMenuItemForm newForm = new AddMenuItemForm(
                cheeseDao.findAll(), brandNewMenu);
//        AddMenuItemForm aNewMenuItemForm = AddMenuItemForm(brandNewMenu, cheeseDao.findAll());

        model.addAttribute("title", "Add item to menu: " + brandNewMenu.getName());
        model.addAttribute("form", newForm);

        return "menu/add-item";

    }


    @RequestMapping(value="add-item/{theMenuId}", method = RequestMethod.POST)
    public String addItem(Model model,
                          @ModelAttribute @Valid AddMenuItemForm aForm,
                          Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("form", aForm);
            return "menu/add-item";
        }

/*        Cheese theCheese = cheeseDao.findAllById(aForm.getCheeseId());
        Menu theMenu = menuDao.findById(aForm.getMenuId());*/
        Cheese theCheese = cheeseDao.findById(aForm.getCheeseId()).get();
        Menu theMenu = menuDao.findById(aForm.getMenuId()).get();
        theMenu.addItem(theCheese);
        menuDao.save(theMenu);


        return "redirect:/menu/view/" + theMenu.getId();

    }


}
