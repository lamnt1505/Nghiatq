package edu.poly.Du_An_Tot_Ngiep.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.poly.Du_An_Tot_Ngiep.Entity.Category;
import edu.poly.Du_An_Tot_Ngiep.Entity.FeedBack;
import edu.poly.Du_An_Tot_Ngiep.Entity.Invoice;
import edu.poly.Du_An_Tot_Ngiep.Entity.InvoiceDetail;
import edu.poly.Du_An_Tot_Ngiep.Entity.Product;
import edu.poly.Du_An_Tot_Ngiep.Entity.User;
import edu.poly.Du_An_Tot_Ngiep.Service.CategoryService;
import edu.poly.Du_An_Tot_Ngiep.Service.FeedBackService;
import edu.poly.Du_An_Tot_Ngiep.Service.ImportService;
import edu.poly.Du_An_Tot_Ngiep.Service.OrderDetailsService;
import edu.poly.Du_An_Tot_Ngiep.Service.OrdersService;
import edu.poly.Du_An_Tot_Ngiep.Service.ProductService;
import edu.poly.Du_An_Tot_Ngiep.Service.StatisticalService;
import edu.poly.Du_An_Tot_Ngiep.Service.UserService;


@Controller
public class ManagerController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private FeedBackService feedBackService;

	@Autowired
	OrdersService oders;

	@Autowired
	OrderDetailsService orderDetailsService;

	@Autowired
	ImportService importService;


	@Autowired
	StatisticalService statisticalService;
	
	//show user
	void getName(HttpServletRequest request, ModelMap model) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; ++i) {
			if (cookies[i].getName().equals("accountuser")) {
			    //so sánh phần tử i trong cookie với accountuser
				User user = this.userService.findByPhone(cookies[i].getValue()).get();
				//sử dụng câu lệnh findbyphone để tìm số đt
				//đưa các giá trị vào model
				model.addAttribute("fullname", user.getFullname());
				model.addAttribute("image", user.getImageBase64());
				break;
			}
		}
	}

	
	@GetMapping(value = "/manager")//kich hoat action pt get
	public String manager(ModelMap model, @CookieValue(value = "accountuser", required = false) String username,
			MultipartFile image, HttpServletRequest request, HttpServletResponse response) {
	    
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {		
				if (cookies[i].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					//đưa các giá trị vào model
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					
					return "redirect:/manager/listCategory";
				}
			}

		}
		return "redirect:/login";
	}

	@GetMapping(value = "/manager/listCategory")//kich hoat action pt get
	public String listCategory(Model model, @CookieValue(value = "accountuser", required = false) String username,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					if (model.asMap().get("success") != null)
						redirect.addFlashAttribute("success", model.asMap().get("success").toString());
					    //sd addFlashAttribute đưa ra tb cho category
					List<Category> list = categoryService.listCategory();
					//goi thuc hien pt findall tra ve ds 
					model.addAttribute("category", list);
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					return "/manager/category/listCategory";
				}

			}
		}
		return "redirect:/login";
	}

	@GetMapping(value = "/manager/addCategory")//kich hoat action pt get
	public String addCategory(ModelMap model, @CookieValue(value = "accountuser", required = false) String username,
			HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {		            
				if (cookies[i].getName().equals("accountuser")) {
					this.userService.findByPhone(cookies[i].getValue()).get();
					//sử dụng serviceimpl để lấy thông tin entity
					model.addAttribute("category", new Category());
					//sử dụng pt addtribute để lấy đối tượng entity
					getName(request, model);
					return "/manager/category/addCategory";
				}
			}
		}
		return "redirect:/login";
	}

	@PostMapping(value = "/manager/addCategory")
	public String addCategory(@ModelAttribute(value = "category") @Valid Category category,
			RedirectAttributes redirect) {

		this.categoryService.save(category);//goi thuc hien pt luu 
		redirect.addFlashAttribute("success", "Thêm mới danh mục thành công!");//dua ra tb da luu entity

		return "redirect:/manager/listCategory";
	}

	@GetMapping(value = "/manager/updateCategory/{idCategory}")
	public String updateCategory(ModelMap model, @PathVariable(name = "idCategory") int idCategory,
			@CookieValue(value = "accountuser", required = false) String username, 
			HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					this.userService.findByPhone(cookies[i].getValue()).get();
					//import cts goi thuc hien pt finbyphone
					getName(request, model);
					model.addAttribute("category", categoryService.findById(idCategory));
					//import cts goi thuc hien pt findbyid
					return "/manager/category/updateCategory";
				}
			}
		}
		return "redirect:/login";
	}
	
	@PostMapping(value = "/manager/updateCategory")
	public String updateCategory(@ModelAttribute(value = "category") @Valid Category category,
			@RequestParam("idCategory") int idCategory, RedirectAttributes redirect) {
		this.categoryService.save(category);//goi thuc hien pt luu 
		redirect.addFlashAttribute("success", "Cập nhập danh mục thành công!");
		//dua ra tb da cập nhật entity
		return "redirect:/manager/listCategory";
	}

	@GetMapping(value = "/manager/deleteCategory/{idCategory}")
	public String deleteCategory(@PathVariable(name = "idCategory") int idCategory,
			@CookieValue(value = "accountuser", required = false) String username, HttpServletRequest request,
			RedirectAttributes redirect) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					this.userService.findByPhone(cookies[i].getValue()).get();
					//goi thực hiện pt find byphone để lấy user
					this.categoryService.deleteById(idCategory);
					//gọi thực hiện deletedbyid truyền vào id xóa theo category id
					redirect.addFlashAttribute("success", "Xóa danh mục thành công!");
					//đưa ra tb đã xóa thành công
					return "redirect:/manager/listCategory";
				}

			}
		}
		return "redirect:/login";
	}

	// table product
	//pt trung giang cho listProductpage
	@GetMapping(value = "/manager/listProduct")
	public String listProduct(Model model, HttpServletRequest request, RedirectAttributes redirect) {
		//đây là phương thức vừa phân trang vừa hiển thị danh sách sản phẩm
	    //khởi tạo ss product
		request.getSession().setAttribute("product", null);
		
		if (model.asMap().get("success") != null)
			redirect.addFlashAttribute("success", model.asMap().get("success").toString());
        //lấy đối tượng FlashAttribute đưa ra tb tất cả action
		return "redirect:/listProduct/page/1";
	}
	
	//phân trang//đây là pt
	@GetMapping(value = "/listProduct/page/{pageNumber}")
	public String showProduct(@CookieValue(value = "accountuser") String username,
	        HttpServletRequest request,
			HttpServletResponse response, @PathVariable int pageNumber, Model model) {
	    
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
				    //lấy số đt người dùng
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					//khởi tạo PagedListHolder 
					PagedListHolder<?> pages = (PagedListHolder<?>) 
						 request.getSession().getAttribute("product");
					int pagesize = 5;
					//giá trị 5 phần từ trên 1 trang
					List<Product> list = productService.listProduct();
					if (pages == null) {
						pages = new PagedListHolder<>(list);//dùng pt PagedListHolder ptrag theo danh sách
						pages.setPageSize(pagesize);//Đặt kích thước trang hiện tại.
					} else {
						final int goToPage = pageNumber - 1;
						if (goToPage <= pages.getPageCount() && goToPage >= 0) {
							pages.setPage(goToPage);
						}
					}
					
					request.getSession().setAttribute("product", pages);
					int current = pages.getPage() + 1;
					int begin = Math.max(1, current - list.size());//bắt đầu
					//thực hiện tính toán kích thước của trang
					//Thiết lập trang mà chúng ta muốn hiện lên View
					int end = Math.min(begin + 5, pages.getPageCount());//kết thúc
					int totalPageCount = pages.getPageCount();//tính toán số trang hiển thị trên view
					String baseUrl = "/listProduct/page/";//
					//hiển thị dữ liệu trở lại trên view
					
					//chuyển các biến sang view
					model.addAttribute("beginIndex", begin);
					model.addAttribute("endIndex", end);
					model.addAttribute("currentIndex", current);
					model.addAttribute("totalPageCount", totalPageCount);
					model.addAttribute("baseUrl", baseUrl);
					model.addAttribute("product", pages);
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					model.addAttribute("");
					return "/manager/product/listProduct";
				}
			}
		}
		return "redirect:/login";
	}
	
	
	//pt get lấy 
	@GetMapping(value = "/manager/addProduct")
	public String addProduct(ModelMap model, @CookieValue(value = "accountuser", required = false) String username,
			HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					//kiểm tra nếu i khác entity
					this.userService.findByPhone(cookies[i].getValue()).get();
					getName(request, model);
					//lấy tên và hình ảnh
					model.addAttribute("product", new Product());
					model.addAttribute("listCategory", categoryService.findAll());

					return "/manager/product/addProduct";
				}
			}
		}
		return "redirect:/login";
	}
	
	@PostMapping(value = "/manager/addProduct") 
	public String addProduct(@RequestParam(value = "image") MultipartFile image,
			@ModelAttribute(name = "product") @Valid Product product, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
		  return "/manager/addProduct";
		} else {
			this.productService.save(product);
			redirect.addFlashAttribute("success", "Thêm mới thông tin sản phẩm thành công!");
			//dua ra tb save thcong
		}
		return "redirect:/manager/listProduct";
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
	
	
	@GetMapping(value = "/manager/updateProduct/{idProduct}")//update sử pt nối chuỗi + với 1 id
	public String updateProduct(ModelMap model, @PathVariable(name = "idProduct") int id,
			@CookieValue(value = "accountuser", required = false) String username, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					this.userService.findByPhone(cookies[i].getValue()).get();
					//sử dụng entityphone để lấy tên người đăng nhập
					model.addAttribute("listCategory", this.categoryService.findAll());
					//sử dụng câu lệnh tìm tất cả sản phẩm
					model.addAttribute("product", this.productService.findById(id)
						.isPresent() ? this.productService.findById(id).get(): null);
					//đưa các giá trị vào model
					getName(request, model);
					return "/manager/product/updateProduct";
				}
			}
		}
		return "redirect:/login";
	}
	
	//action update
	@PostMapping(value = "/manager/updateProduct")
	public String updateProduct(@RequestParam(value = "image") MultipartFile image,
			@ModelAttribute(name = "product") @Valid Product product, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "/manager/updateProduct";
		} else {
			this.productService.save(product);
			redirect.addFlashAttribute("success", "Cập nhập thông tin sản phẩm thành công!");
		}
		
		if (!image.isEmpty()) {//kiểm tra nếu hình ảnh bị trống
			try {
				product.setImage(image.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				//in ra tb lỗi
			}
		} else {
			product.setImage(productService.findById(product.getIdProduct()).get().getImage());
			//lấy tt hình ảnh và tên
		}
		return "redirect:/manager/listProduct";
	}

	@GetMapping(value = "/manager/deleteProduct/{idProduct}")
	public String deleteProduct(@PathVariable(name = "idProduct") int id,
			@CookieValue(value = "accountuser", required = false) String username, HttpServletRequest request,
			RedirectAttributes redirect) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					//tìm số đt user
					this.productService.deleteById(id);
					//sử dụng câu lệnh xóa theo id
					//this.importService.deleteById(id);
					redirect.addFlashAttribute("success", "Xóa sản phẩm thành công!");

					return "redirect:/manager/listProduct";
				}
			}
		}
		return "redirect:/manager/error/404";
	}

	// feedback
	@GetMapping(value = "/manager/feedback")
	public String listFeedBack(ModelMap model, @CookieValue(value = "accountuser") String username,
			HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					//đưa các giá trị vào model
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					//sử dụng câu lệnh tìm tất cả
					this.feedBackService.findAll();
					
					return "/manager/feedback/feedback";
				}

			}
		}
		return "redirect:/login";
	}

	@PostMapping(value = "index/contact")
	public String addFeedBack(@ModelAttribute(name = "feedback") @Valid FeedBack feedBack, BindingResult result) {
		if (result.hasErrors()) {//nếu có lỗi xảy ra 
		    //trả về trag contact
			return "shop/contact";
		}
		this.feedBackService.save(feedBack);//sử dụng câu lệnh save
		return "shop/contact";
	}

	// product Detail
	@GetMapping("/manager/order")
	public String listOrder(ModelMap model, @CookieValue(value = "accountuser", required = false) String username,
			HttpServletRequest request, HttpServletResponse response) {
		//tạo mới 1 cookie 
		Cookie[] cookies = request.getCookies(); 
		if (cookies != null) {//kiểm tra cookie
			for (int i = 0; i < cookies.length; ++i) {//sử dụng vòng lặp for để duyệt qua cookie
				if (cookies[i].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[i].getValue()).get();
					//sử dụng userService lấy tt người đăng nhập
					//đưa các giá trị vào model
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					//hiển thị danh sách hóa đơn
					//sd orderservice gọi đến phương thức tìm tất cả hóa đơn(listInvoice())
					List<Invoice> list = this.oders.listInvoice();
					//đưa các giá trị vào model
					//gọi pt list trong List<Invoice> list = this.oders.listInvoice();
					//trả về listOrder từ listOrder gọi qua thuộc tính th:each="order : ${listOrder}" bên html
					model.addAttribute("listOrder", list);
					//trả về trang order
					return "manager/order/order";
				}
			}
		}
		return "redirect:/login";
	}

	@GetMapping(value = "/manager/orderDetail/{id}")
	//sử dụng phương thức nối chuỗi đến với 1 id hóa đơn
	public String viewOrderdetailsForManager(@PathVariable("id") int id, ModelMap model,
			@CookieValue(value = "accountuser", required = false) String username, HttpServletRequest request,
			HttpServletResponse response) {	
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int j = 0; j < cookies.length; ++j) {
				if (cookies[j].getName().equals("accountuser")) {
					User user = this.userService.findByPhone(cookies[j].getValue()).get();
					//sử dụng userService lấy tt người đăng nhập
					//đưa các giá trị vào model
					model.addAttribute("username", username);
					model.addAttribute("fullname", user.getFullname());
					model.addAttribute("image", user.getImageBase64());
					//hiển thị danh sách hóa đơn chi tiết
					List<InvoiceDetail> list = this.orderDetailsService.findDetailByInvoiceId(id);
					//hiên thị danh sách product
					List<Product> productorder = new ArrayList<>();
					//tạo vòng lặp mới hiển thị ds product đã mua
					for (int i = 0; i < list.size(); i++) {
						//sd vl for để duyệt qua các product,order
						Product odrProduct = productService.findByIdProduct(list.get(i).getProduct().getIdProduct());
						//sử dụng psv tìm theo id product(lấy sản phẩm và id sản phẩm)
						odrProduct.setAmount(list.get(i).getAmount());
						//sử dụng pt odrProduct lấy số lượng và set giá trị
						productorder.add(odrProduct);

					}
					//từ phương thức productorder gọi qua thuộc tính <tr th:each="order : ${listOrderDetail}">
					model.addAttribute("listOrderDetail", productorder);
					//trả về trang orderdetail
					return "manager/order/orderDetail";
				}
			}
		}
		return "redirect:/login";
	}
}
