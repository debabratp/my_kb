package com.my.kb.controller;

import com.my.kb.services.KnowledgeBaseJASService;
import com.my.kb.services.KnowledgeBaseService;
import com.my.kb.vo.KnowledgeBaseVO;
import com.my.kb.vo.LoginVO;
import com.my.kb.vo.UserVO;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class PaymentKbController {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private KnowledgeBaseJASService jasService;

    @GetMapping("/create-kb")
    public String create(@ModelAttribute final KnowledgeBaseVO knowledgeBase, Model model) {
        model.addAttribute("knowledgeBase", new KnowledgeBaseVO());
        return "create";
    }

    @PostMapping("/save-kb")
    public String save(KnowledgeBaseVO knowledgeBase, final Model model) {
        knowledgeBase = jasService.create(knowledgeBase);
        model.addAttribute("knowledgeBase", knowledgeBase);
        return "result";
    }

    @GetMapping("/update-kb/{kid}")
    //@RequestMapping(path = "/update-kb/{kid}", method = {RequestMethod.GET, RequestMethod.POST})
    public String update(@PathVariable final String kid, Model model, HttpSession session) {
        KnowledgeBaseVO updateKb = jasService.getKbByID(kid);
        String search = String.valueOf(session.getAttribute("search_str"));

        HashMap<String, KnowledgeBaseVO> attribMap = new LinkedHashMap<>();
        attribMap.put("updateKb", updateKb);
        //attribMap.put("kb", new KnowledgeBaseVO());
        model.addAllAttributes(attribMap);
        //model.addAttribute("updateKb", updateKb);
        return "update";
    }

    @PostMapping("/update-kb")
    public String update(KnowledgeBaseVO kb, Model model) {
        kb = jasService.update(kb);
        model.addAttribute("knowledgeBase", kb);
        return "result";
    }

    @GetMapping("/search-kb")
    public String searchKb(Model model) {
        model.addAttribute("knowledgeBase", new KnowledgeBaseVO());
        return "search";
    }

    @PostMapping("/search-result")
    public String search(final KnowledgeBaseVO knowledgeBase, Model model, HttpSession session) {
        try {
            session.setAttribute("search_str", knowledgeBase.getSearch());
            List<KnowledgeBaseVO> knowledgeBaseList = jasService.search(knowledgeBase);
            model.addAttribute("knowledgeBaseList", knowledgeBaseList);
            model.addAttribute("value", new KnowledgeBaseVO());
            if (knowledgeBaseList.isEmpty()) {
                model.addAttribute("errorSearch", "No search result for " + "'" + knowledgeBase.getSearch() + "'");
            }
            model.addAttribute("knowledgeBase", new KnowledgeBaseVO()); // Place holder to avoid exception
            return "search";
        } catch (Exception e) {
            model.addAttribute("knowledgeBase", new KnowledgeBaseVO());
            model.addAttribute("errorSearch", "No search result for " + "'" + knowledgeBase.getSearch() + "'");
            return "search";
        }

    }

//    @PostMapping("/search-update")
//    public String searchUpdate(KnowledgeBaseVO value, Model model) {
//        System.out.println(value);
//        return "search";
//    }

    @GetMapping(value = "/home")
    public String home(Model model) {
        try {
            List<KnowledgeBaseVO> results = jasService.getKBForHome();
            model.addAttribute("results", results);
            int count = jasService.getKBCount();
            model.addAttribute("count", count);
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", "Error getting data");
            return "error";
        }

    }

    @GetMapping(value = "/kb/getKb/{id}")
    public String getKB(@PathVariable String id, Model model) {
        try {
            KnowledgeBaseVO kb = jasService.getKbByID(id);
            if (null == kb) {
                model.addAttribute("error", "No data found for the given value");
            }
            model.addAttribute("kb", kb);
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", "Internal Server Error");
            return "error";
        }

    }

   /* @GetMapping(value = "/get-count")
    public String getKbCount(Model model){
        model.addAttribute("count", jasService.getKBCount());
        return "home";
    }*/

    @GetMapping(value = "/")
    public String getLoginForm(LoginVO user) {
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@Valid LoginVO loginVO, Errors errors, Model model, HttpSession session, BindingResult result) {
        if (null != errors && errors.getErrorCount() > 0) {
            return "login";
        } else {
            Boolean isValid = jasService.validateUser(loginVO);
            if (isValid) {
                model.addAttribute("successMsg", "Login successful !!");
                return "home";
            } else {
                result.reject("401", "Invalid credential");
                return "login";
            }
        }
    }

    @ModelAttribute("user")
    public UserVO userRegistration() {
        return new UserVO();
    }

    @GetMapping(value = "/create-user")
    public String getCreateUserForm(UserVO userVO) {
        return "createuser";
    }

    @PostMapping(value = "/save-user")
    public String createUser(@Valid UserVO userVO, Errors errors, Model model, BindingResult result) {
        if (null != errors && errors.getErrorCount() > 0) {
            return "createuser";
        } else {
            try {
                if (jasService.isUserExist(userVO.getEmail())) {
                    model.addAttribute("errorCreateUser", "There is already an account registered with that email");
                    result.rejectValue("email", null, "There is already an account registered with that email");
                    return "createuser";
                }
                jasService.saveUser(userVO);
            } catch (JasDBStorageException e) {
                model.addAttribute("errorCreateUser", "Error while creating user");
                return "createuser";
            }
            model.addAttribute("loginVO", new LoginVO());
            return "login";
        }
    }
}
