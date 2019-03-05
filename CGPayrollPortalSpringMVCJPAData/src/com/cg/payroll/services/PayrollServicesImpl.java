package com.cg.payroll.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cg.payroll.beans.Associate;
import com.cg.payroll.daoservices.AssociateDAO;
import com.cg.payroll.exceptions.AssociateDetailsNotFoundException;

@Component("payrollServices")
public class PayrollServicesImpl implements PayrollServices{
	@Autowired
	private AssociateDAO associateDao;
	
	@Override
	public Associate acceptAssociateDetails(Associate associate) {
		associate = associateDao.save(associate);
		return associate;
	}

	@Override
	public int calculateNetSalary(int associateId) throws AssociateDetailsNotFoundException {
		Associate associate = getAssociateDetails(associateId);
		int netSalary=0,grossSalary=0,monthlyTax=0;
		associate.getSalary().setHra((int)((associate.getSalary().getBasicSalary())*0.3));
		associate.getSalary().setConveyenceAllowance((int)((associate.getSalary().getBasicSalary())*0.3));
		associate.getSalary().setPersonalAllowance((int)((associate.getSalary().getBasicSalary())*0.25));
		associate.getSalary().setOtherAllowance((int)((associate.getSalary().getBasicSalary())*0.2));
		grossSalary = associate.getSalary().getBasicSalary()+associate.getSalary().getHra()+associate.getSalary().getCompanyPf()+associate.getSalary().getPersonalAllowance()+associate.getSalary().getOtherAllowance();
		associate.getSalary().setGrossSalary(grossSalary);
		int annualGrossSalary = grossSalary*12;
		int investment = associate.getYearlyInvestmentUnder80C()+associate.getSalary().getCompanyPf()+associate.getSalary().getEpf();
		if(investment>150000)
			investment=150000;
		int taxableAmount = grossSalary;
		if(annualGrossSalary<=250000) {
			associate.getSalary().setMonthlyTax(monthlyTax);
		}
		else if(annualGrossSalary>250000&&annualGrossSalary<=500000) {
			taxableAmount = annualGrossSalary-250000;
			if(taxableAmount>investment) {
				taxableAmount -=investment;
				monthlyTax=(int)(taxableAmount *0.01);
				associate.getSalary().setMonthlyTax(monthlyTax);
			}else
				monthlyTax= 0;
		}
		else if(annualGrossSalary>500000&&annualGrossSalary<=1000000) {
			taxableAmount = annualGrossSalary-250000;
			if(taxableAmount>investment) {
				taxableAmount -=investment;
				monthlyTax=(int)(taxableAmount *0.02);
				associate.getSalary().setMonthlyTax(monthlyTax);
			}else
				monthlyTax= 0;
		}
		else if(annualGrossSalary>1000000) {
			taxableAmount = annualGrossSalary-250000;
			if(taxableAmount>investment) {
				taxableAmount -=investment;
				monthlyTax=(int)(taxableAmount *0.03);
				associate.getSalary().setMonthlyTax(monthlyTax);
			}else
				monthlyTax= 0;
		}	
		netSalary = grossSalary-associate.getSalary().getCompanyPf()-associate.getSalary().getEpf();
		associate.getSalary().setNetSalary(netSalary);
		associateDao.save(associate);
		return netSalary;
	}

	@Override
	public Associate getAssociateDetails(int associateId) throws AssociateDetailsNotFoundException {
		return associateDao.findById(associateId).orElseThrow(()->new AssociateDetailsNotFoundException("Associate Details not found for Associate Id "+associateId));
	}

	@Override
	public List<Associate> getAllAssociatesDetails() {
		return associateDao.findAll();
	}
}
