package edu.poly.Du_An_Tot_Ngiep.Controller;

import java.sql.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.poly.Du_An_Tot_Ngiep.Entity.Customer;
import edu.poly.Du_An_Tot_Ngiep.Entity.User;
import edu.poly.Du_An_Tot_Ngiep.Service.CustomerService;
import edu.poly.Du_An_Tot_Ngiep.Service.UserService;

@Controller
@RequestMapping("")
public class CustomerController {

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	void getName(HttpServletRequest request, ModelMap model) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; ++i) {
			if (cookies[i].getName().equals("accountcustomer")) {
			    //so sánh phần tử i trong cookie với accountuser
				Customer cus = this.customerService.findByPhoneCus(cookies[i].getValue()).get();
				//đưa giá trị vào model
				model.addAttribute("fullname", cus.getFullname());
				model.addAttribute("customerId", cus.getCustomerId());
				break;
			}
		}
	}
	
	//action ql ds customer
	@GetMapping(value = "/manager/listCustomer")
	public String listProduct(ModelMap model, @CookieValue(value = "accountuser", required = false) String phone,
			HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					//so sánh phần tử i trong cookie với accountuser
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					model.addAttribute("listcustomer", this.customerService.findAll());
					model.addAttribute("username", phone);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					return "/manager/users/listCustomer";
				}
			}

		}
		return "redirect:/login";
	}
	
	//action đăng kí 
	@GetMapping(value = "/registration")
	public String registration(ModelMap model) {
		model.addAttribute("registration", new Customer());
		return "/login/registred";
	}

	//action đăng kí
	@PostMapping(value = "/registration")
	public String addProduct(@ModelAttribute(name = "registration") Customer registration, 
	        ModelMap model,
			@RequestParam boolean gender, @RequestParam Date birthday, @RequestParam("phone") String phone) {
	    //đưa giá trị vào model thêm mới 1 cus
		model.addAttribute("registration", new Customer());
		//sd csmS lấy sdt user và customer kiểm tra sdt
		if (customerService.findByPhoneCus(phone).isPresent() 
				|| userService.findByPhone(phone).isPresent()) {
		    //đưa ra tb sđt đã có trong db
			model.addAttribute("error", "Số điện thoại đã tồn tại");
			return "/login/registred";// khi em vừa đăng ký thành công thì nó sẽ trả về trang login
			// mà trang login này nằm ở đâu 
			//giải thích nó nằm ở mục nào .....vvvv
		} else { 
			customerService.save(registration);
			model.addAttribute("succes", "Đăng Ký Thành Công!");
			return "redirect:login";
		}
	}
	
	//action updatecus
	@GetMapping(value = "/updateProfile/{customerId}")
	public String updateCus(ModelMap model, @PathVariable(name = "customerId") int customerId,
			HttpServletRequest request) {
	    //sử dụng pt findall đưa giá trị vào model
		model.addAttribute("listuser", this.customerService.findAll());
		model.addAttribute("customer",
		this.customerService.findById(customerId).isPresent() 
		 ? this.customerService.findById(customerId).get()
						: null);
		//lấy tt
		getName(request, model);
		return "/manager/users/updateProfile";
	}

	//action updatecus
	@PostMapping(value = "/updateProfile")
	public String updateCus(@ModelAttribute(name = "customerId") @Valid Customer customerId,
		@CookieValue(value = "accountcustomer", required = false) String phone, HttpServletRequest request,
			ModelMap model) {
		customerService.save(customerId);
		getName(request, model);
		return "redirect:/index";
	}

}
