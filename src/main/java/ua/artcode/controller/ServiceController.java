package ua.artcode.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.artcode.script.InitCodingBatTaskTrigger;


/**
 * Created by Maxim on 17.03.2016.
 */

@Controller
@RequestMapping(value = "/service")
public class ServiceController {

    private static final Logger LOG = Logger.getLogger(ServiceController.class);

    @RequestMapping(value = "/createDump")
    public ModelAndView createDumpOfDB(RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("redirect:/service-menu");
        try {
            InitCodingBatTaskTrigger.createDumpOfDataBase();
            redirectAttributes.addFlashAttribute("message", "Data base dump has been created successfully.");
        } catch (Exception e) {
            mav.setViewName("main/service-menu");
            mav.addObject("message", "Error. Dump has not been created!");
            LOG.warn("Dump has not been created!");
        }
        return mav;
    }

    @RequestMapping(value = "/restoreDB")
    public ModelAndView restoreDB_FromDump(RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("redirect:/service-menu");
        try {
            InitCodingBatTaskTrigger.restoreDataBaseFromDump();
            redirectAttributes.addFlashAttribute("message", "Data base has been restored from dump successfully.");
        } catch (Exception e) {
            mav.setViewName("main/service-menu");
            mav.addObject("message", "Error. Database has not been restored!");
            LOG.warn("Database has not been restored!");
        }
        return mav;
    }
}
