package com.zbartholomew.springdemo.controller.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.zbartholomew.springdemo.domain.Address;

@Controller
public class ModelAttributeDemoController {

	private static Logger LOGGER = LoggerFactory.getLogger(ModelAttributeDemoController.class);

	@RequestMapping(value = "/home")
	public String home() {
		LOGGER.info("INSIDE home: " + System.currentTimeMillis());
		return "modelAttributeHome";
	}

	// version 2 of our home() method
	@RequestMapping(value = "/home2")
	public ModelAndView home2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("modelAttributeHome");
		modelAndView.addObject("command", new Address());
		return modelAndView;
	}

	// version 3 of our home() method
	@RequestMapping(value = "/home3")
	public ModelAndView home3() {
		ModelAndView modelAndView = new ModelAndView("modelAttributeHome");
		modelAndView.addObject("anAddress", new Address());
		return modelAndView;
	}

	// version 4 of our home() method
	@RequestMapping(value = "/home4")
	public ModelAndView home4() {
		return new ModelAndView("modelAttributeHome", "anAddress", new Address("New York", "98989"));
	}

	// version 5 of our home() method
	@RequestMapping(value = "/home5")
	public String home5(Model model) {
		model.addAttribute("anAddress", new Address("Dallas", "78989"));
		return "modelAttributeHome";
	}

	/*
	 * Test series to determine the nature of the @ModelAttribute annotation (on a
	 * method)
	 */

	// Test 1: Demonstrating the usage of @ModelAttribute annotation (on a method)
	// to add multiple attributes
	@ModelAttribute
	public void modelAttributeTest1(Model model) {
		LOGGER.info("INSIDE modelAttributeTest1: " + System.currentTimeMillis());
		model.addAttribute("testdata1a", "Welcome to the @ModelAttribute Test Bed!");
		model.addAttribute("testdata1b",
				"We will test both usages of the @ModelAttribute, on methods and on method arguments");
	}

	// Test 1: Demonstrating the usage of the 'name' attribute of the
	// @ModelAttribute annotation (on method)
	@ModelAttribute(name = "testdata2")
	public String modelAttributeTest2() {
		LOGGER.info("INSIDE modelAttributeTest2");
		return "We will conduct a series of test here.";
	}

	// Test 3: Demonstrating the usage of the @ModelAttribute annotation (on a
	// method) to implicitly add an attribute
	// by returning it and also demonstrating the usage of the 'value' attribute of
	// the @ModelAttribute annotation (on a method)
	@ModelAttribute(value = "testdata3")
	public Address modelAttributeTest3() {
		LOGGER.info("INSIDE modelAttributeTest3");
		return new Address("Cedar Park", "78613");
	}

	// Test 4: Demonstrating the default naming strategy of the @ModelAttribute
	// annotation (on a method)
	@ModelAttribute
	public Address modelAttributeTest4() {
		LOGGER.info("INSIDE modelAttributeTest4");
		return new Address("Houston", "72749");
	}

	// Test 5: Testing the @ModelAttribute with 'value' attribute and default
	// binding
	@RequestMapping(value = "/test5", method = RequestMethod.POST)
	public String modelAttributeTest4(@ModelAttribute(value = "anAddress") Address anAddress, ModelMap model) {
		LOGGER.info("INSIDE modelAttributeTest5");
		model.addAttribute("testdata5a", anAddress.getCity());
		model.addAttribute("testdata5b", anAddress.getZipCode());
		return "modelAttributeTest";
	}

	// Test 6: Test to determine narture of how the @ModelAttribute (on method)
	// and @RequestMapping work with no explicit logival view name
	@RequestMapping(value = "/modelAttributeTest")
	@ModelAttribute("testdata6")
	public Address modelAttributeTest6() {
		return new Address("San Fran", "90210");
	}
}
