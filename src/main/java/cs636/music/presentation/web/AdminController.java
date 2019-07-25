package cs636.music.presentation.web;

import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cs636.music.config.MusicSystemConfig;
import cs636.music.presentation.SystemTest;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

@Controller
@SessionAttributes("admin")
public class AdminController {

	public static final String Admin_url = "/adminController/";
	public static final String Admin_jsp_dir = "/admin/";
	
	@Autowired
	private SalesService salesService;
	@Autowired
	private CatalogService catalogService;
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
	
	@RequestMapping(Admin_url+"adminWelcome.html")
	public String welcomeadmin() {
		String url = Admin_jsp_dir + "adminWelcome";
		return url;
	}

	@RequestMapping(Admin_url+"loginAdmin.html")
	public String displayWelcome(Model model,@RequestParam(value = "firstName", required = false) String firstname,
			@RequestParam(value="password"
			, required=false) String password,HttpServletRequest request) throws ServletException{
		
			System.out.println("Starting admin detail checkup:"+firstname+"/t"+password);
		
			AdminBean admin=(AdminBean)request.getSession().getAttribute("admin");
			System.out.println(admin);
			if(admin==null)
				admin=new AdminBean();
			if(admin!=null)
				admin.setFirstname(firstname);
			if(admin.getfirstname()!=null)
				request.getSession().setAttribute("admin",admin);
				try {			
						System.out.println("Entering the condition:");			
						if(salesService.checkAdminLogin(firstname, password)) {
								
						System.out.println("Successfully entered:");
						System.out.println("Successful Login:-");
						model.addAttribute("firstName", firstname);
						return Admin_jsp_dir + "thanksadmin";	
					
						}
							else {
								System.out.print("Wrong Admin Details:");
								return Admin_jsp_dir + "adminWelcome";
							}
						}catch(Exception e)
						{
							throw new ServletException("Problem with direction");	
						}
	}
	
	@RequestMapping(Admin_url+"ShowReport.html")
	public String DisplayReport(Model model) {
		return Admin_jsp_dir+"ShowReport";
	}
	
	@RequestMapping(Admin_url+"listVariables.html")
	public String listVariables(Model model) {	
		model.addAttribute("dbUrl", dbUrl);
		System.out.println("dbUrl from application.properties: " + dbUrl);
		String url = Admin_jsp_dir+"listVariables";
		return url;
	}
	
	@RequestMapping(Admin_url + "initializeDB.html")
	public String adminInitDB(Model model) {
		String info;
		try {
			salesService.initializeDB();
			catalogService.initializeDB();
			SystemTest test = new SystemTest(catalogService, salesService);
			test.runSystemTest();
			info = "Initialize db: success";
		} catch (Exception e) {
			info = "Initialize db: failed " + MusicSystemConfig.exceptionReport(e);
		}
		model.addAttribute("info", info);
		String url = Admin_jsp_dir + "initializeDB";
		return url;
	}
	
	@RequestMapping(Admin_url+"logout.html")
	public String logout(Model model, HttpServletRequest request) {	
		request.getSession().invalidate();  // drop session
		String url = Admin_jsp_dir+"logout";
		return url;
	}
	
	@RequestMapping(Admin_url+"thanksadmin.html")
	public String thanks() {	
		String url = Admin_jsp_dir+"thanksadmin";
		return url;
	}
	
	@RequestMapping(Admin_url+"ListOfDownloads.html")
	public String showAllDownloads(Model model, @RequestParam(value = "command", required = false) String command){
		Set<DownloadData> allDownload=null;
		String url = null;
		try {
			allDownload=catalogService.getListofDownloads();
			
		}
		catch(Exception e) {System.out.println(e);}
		
		if(allDownload!=null) {
			model.addAttribute("allDown",allDownload);
			url=Admin_jsp_dir+"ListOfDownloads";
			}
			else {
				String nothing="There are no downloads to show";
				model.addAttribute("nothing", nothing);
				url=Admin_jsp_dir+"ListOfDownloads";
			}
		return url;
	}
	
	@RequestMapping(Admin_url+"process")
	public String Process(Model model,@RequestParam(value="id" , required = false) String id ,HttpServletRequest request) {
		String url=null;
		System.out.println("Processing invoice with id :"+ id);
		long id1=Long.parseLong(id);
		try {
			if(id!=null) {
			salesService.processInvoice(id1);
			url=Admin_jsp_dir+"ProcessInvoice";
			}
			else {
				url="forward:processInvoice";
			}
		}
		catch(Exception e) {
			System.out.println(e);
			url="forward:processInvoice";
		}
		return url;
	}
	
	@RequestMapping(Admin_url+"processInvoice")
	public String ProcessInvoice(Model model, @RequestParam(value = "command", required = false) String command,HttpServletRequest request) {
		Set<InvoiceData> invoices = null;
		String ForwardUrl = null;
		
		
		try {
			invoices=salesService.getListofUnprocessedInvoices();
			
		}
		catch(Exception e) {System.out.println(e);}
		
		if(invoices!=null && command==null) {
			model.addAttribute("Invoices",invoices);
			ForwardUrl=Admin_jsp_dir+"ProcessInvoice";
			}
		else {
				String nothing="There are no downloads to show";
				model.addAttribute("nothing", nothing);
				ForwardUrl=Admin_jsp_dir+"ProcessInvoice";
			}
		
		
		return ForwardUrl;
	}

	
	@RequestMapping(Admin_url+"forinvoiceprocess.html")
	public String ToProcessInvoice(Model model, @RequestParam(value = "command", required = false) String command){
		Set<InvoiceData> invoices = null;
		String url = null;
		try {
			invoices=salesService.getListofUnprocessedInvoices();
			
		}
		catch(Exception e) {System.out.println(e);}
		
		if(invoices!=null && command==null) {
			model.addAttribute("Invoices",invoices);
			url=Admin_jsp_dir+"forinvoiceprocess";
			
			}
		else {
				String nothing="There are no downloads to show";
				model.addAttribute("nothing", nothing);
				url=Admin_jsp_dir+"forinvoiceprocess";
			}
		
		
		return url;
	}
	
}
