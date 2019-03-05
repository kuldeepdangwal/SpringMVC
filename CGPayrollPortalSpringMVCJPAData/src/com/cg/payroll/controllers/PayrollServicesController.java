package com.cg.payroll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cg.payroll.beans.Associate;
import com.cg.payroll.services.PayrollServices;

@Controller
public class PayrollServicesController {
	@Autowired
	PayrollServices payrollServices;

	@RequestMapping
	public ModelAndView registerAssociate(@ModelAttribute Associate associate) {
		associate = payrollServices.acceptAssociateDetails(associate);
		return new ModelAndView("registrationSuccessPage","associate",associate);
	}
}
