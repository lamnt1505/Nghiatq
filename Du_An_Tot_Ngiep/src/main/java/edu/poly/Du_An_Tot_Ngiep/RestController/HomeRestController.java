package edu.poly.Du_An_Tot_Ngiep.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.poly.Du_An_Tot_Ngiep.Entity.Customer;
import edu.poly.Du_An_Tot_Ngiep.Entity.Imports;
import edu.poly.Du_An_Tot_Ngiep.Entity.Invoice;
import edu.poly.Du_An_Tot_Ngiep.Entity.InvoiceDetail;
import edu.poly.Du_An_Tot_Ngiep.Entity.Product;
import edu.poly.Du_An_Tot_Ngiep.Service.CategoryService;
import edu.poly.Du_An_Tot_Ngiep.Service.CustomerService;
import edu.poly.Du_An_Tot_Ngiep.Service.FeedBackService;
import edu.poly.Du_An_Tot_Ngiep.Service.ImportService;
import edu.poly.Du_An_Tot_Ngiep.Service.OrderDetailsService;
import edu.poly.Du_An_Tot_Ngiep.Service.OrdersService;
import edu.poly.Du_An_Tot_Ngiep.Service.ProductService;
import edu.poly.Du_An_Tot_Ngiep.Service.UserService;

@RestController
public class HomeRestController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderDetailsService orderDetailsServices;

	@Autowired
	private FeedBackService feedBackService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ImportService importService;

	@GetMapping("/index/listProductAjax")
	public ResponseEntity<?> showListProduct() {
	  //s??? d???ng productService l???y danh s??ch s???n ph???m
		return ResponseEntity.ok(this.productService.listProduct());
	}

	@GetMapping("/index/listProductNewBest")
	public ResponseEntity<?> showListProductNewBest() {
	    //s??? d???ng productService t??m s???n ph???m m???i nh???t. 
		return ResponseEntity.ok(this.productService.listProductNewBest());
	}

	@GetMapping("/index/listProductPriceDesc")
	public ResponseEntity<?> showListProductPriceDesc() {
	    //s??? d???ng c??u l???n t??m gi?? s???n ph???m t??? cao ?????n th???p
		return ResponseEntity.ok(this.productService.listProductPriceDesc());
	}

	@GetMapping("/index/listProductPriceAsc")
	public ResponseEntity<?> showListProductPriceAsc() {
	  //s??? d???ng productService th???p ?????n cao. 
		return ResponseEntity.ok(this.productService.listProductPriceAsc());
	}

	@PostMapping("/index/listProductByIdCategoryFilter/{idCategory}")
	@ResponseBody
	public List<Product> showListProductByIdCategory(@PathVariable("idCategory") int id, Product p) {
	    //t??m theo gt id 
		Optional<Product> list = this.productService.findById(id);
		return this.productService.showListProductByIdCategoryFilter(id);
	}

	// add product in cart
	@PostMapping("/insertproduct}")
	@ResponseBody
	public String insertProduct(@RequestParam(name = "idproduct") int idProduct, @RequestParam int amount,
			HttpSession session) {

		Product productOrder = this.productService.findByIdProduct(idProduct);
		//s??? d???ng productService t??m theo id
		
		if (productOrder == null || amount <= 0) {
			return "3";
		}
		
		if (session.getAttribute("cart") != null) {
			List<Product> list = (List<Product>) session.getAttribute("cart");
			boolean flag = false;
			for (int i = 0; i < list.size(); i++) {
				int id_temp = list.get(i).getIdProduct();
				if (id_temp == idProduct) {
					list.get(i).setAmount(list.get(i).getAmount() + amount);
					flag = true;
					return "4";
				}
			}
			if (flag == false) {
				productOrder.setAmount(amount);
			}
			list.add(productOrder);
			session.setAttribute("cart", list);
			return "1";
		} else {
			List<Product> list = new ArrayList<>();
			productOrder.setAmount(amount);
			list.add(productOrder);
			session.setAttribute("cart", list);
			return "1";
		}
	}

	@PostMapping(value = "/updatequantities")
	@ResponseBody
	public String updateQuantity(@RequestParam(name = "idproduct") int idProduct,
			@RequestParam(name = "quantity") int quantity, HttpSession session) {
		// n???u s??? l?????ng nh??? h??n 0
		if (quantity < 0) {
			return "0";
			//tr??? v??? 0
		} else if (quantity == 0) {//ki???m tra n???u sl = 0
			List<Product> list = (List<Product>) session.getAttribute("cart");

			for (int i = 0; i < list.size(); i++) {
				if (idProduct == list.get(i).getIdProduct()) {
					list.remove(i);
					session.setAttribute("cart", list);
					return "2";
				}
			}
			
		} else if (session.getAttribute("cart") != null) {
			List<Product> list = (List<Product>) session.getAttribute("cart");
			for (int i = 0; i < list.size(); i++) {
				if (idProduct == list.get(i).getIdProduct()) {
					list.get(i).setAmount(quantity);
					session.setAttribute("cart", list);
					return "1";
				}
			}
		} else {
			return "0";
		}
		return "0";
	}

	@PostMapping(value = "/orders")
	@ResponseBody
	public String orders(HttpServletRequest request, HttpSession session, ModelMap model) {

		Customer user = null;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; ++i) {
			if (cookies[i].getName().equals("accountcustomer")) {
				user = this.customerService.findByPhoneCus(cookies[i].getValue()).get();
				break;
			}
		}

		if (user == null || user.getCustomerId() <= 0)
			return "0";
		else {

			if (session.getAttribute("cart") != null) {
				List<Product> list = (List<Product>) session.getAttribute("cart");
				Invoice invoice = new Invoice();
				// ----date orders
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);
				// ---total---
				double total = 0;
				Set<InvoiceDetail> setDetail = new HashSet<>();
				for (int i = 0; i < list.size(); i++) {
					// total++
					total += list.get(i).getPrice() * list.get(i).getAmount();
					InvoiceDetail s = new InvoiceDetail();
					s.setProduct(list.get(i));
					setDetail.add(s);
				}
				invoice.setDateorders(date);
				invoice.setStatus("Ch??? duy???t");
				invoice.setTotal(total);
				invoice.setVendor(user);
				System.out.println("------Amount Orders:" + list.size() + "--------");

				invoice.setDetails(setDetail);
				ordersService.save(invoice);
				for (int i = 0; i < list.size(); i++) {
					total += list.get(i).getPrice() * list.get(i).getAmount();
					InvoiceDetail s = new InvoiceDetail();
					s.setProduct(list.get(i));
					s.setAmount(list.get(i).getAmount());
					s.setInvoiceId(invoice);
					s.setPrice(list.get(i).getPrice());
					setDetail.add(s);
					orderDetailsServices.save(s);
				}
				session.setAttribute("cart", new ArrayList<>());
			} else {
				return "-1";
			}
			return "1";
		}
	}

	// Check status
	@PostMapping(value = "/updatestatus")
	@ResponseBody
	public String updateStatusOrder(@RequestParam(name = "orderid") int orderid,
			@RequestParam(name = "status") String status) {

		Invoice acceptInv =  ordersService.findByIdInvoice(orderid);
		//s??? d???ng ph????ng th???c ordersService t??m theo id h??a ????n 
		//m?? h??a ????n(orderid)
		//ph????ng th???c con acceptInv(ch???p nh???n h??a ????n)
		if (acceptInv == null) {
			//n???u h??a ????n kh??ng c?? tr??? v??? 0
			return "0";
		}
		//-set new status
		//l??m m???i status
		acceptInv.setStatus(status);

		// t??m product c?? s??? l?????ng nh??? h??n s??? l?????ng ?????t h??ng
		for (InvoiceDetail detail : acceptInv.getDetails()) {
			Imports oldProduct = importService.findQuatityProduct(detail.getProduct().getIdProduct());
			//g???i pt importService t??m theo s??? l?????ng import theo product l???y product v?? id 
			//check t???n t???i trong kho
			if (oldProduct == null) {
				System.out.println("no order");
				return "-2";
			}

			// so s??nh oldProduct.amount trong kho v?? InvoiceDetail.amount
			if (oldProduct.getQuantity() < detail.getAmount()) {
				System.out.println("no order");
				return "-1";
			}
		}

		System.out.println(status);

		if (status.equals("Ho??n th??nh")) {
			// update quantity
			for (InvoiceDetail detail : acceptInv.getDetails()) {
				Imports oldProduct = importService.findQuatityProduct(detail.getProduct().getIdProduct());
				//g???i pt importService t??m theo s??? l?????ng import theo product l???y product v?? id 
				detail = orderDetailsServices.findInvoiceDetail(detail.getDetailId());
				oldProduct.setQuantity(oldProduct.getQuantity() - detail.getAmount());
				//s??? d???ng pt save g???i t??? import service
				importService.save(oldProduct);
			}
		}
		//k??m theo pt save c???a order lwuu h??a ????n
		ordersService.save(acceptInv);
		//tr??? v??? 1
		return "no order";
	}
}
