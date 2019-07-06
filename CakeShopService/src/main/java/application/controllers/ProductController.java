package application.controllers;

import application.entities.Product;
import application.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping(path="/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @RequestMapping(path="/create", method = RequestMethod.POST, params = {"name","description","price"})
    public @ResponseBody String createProduct(@Param("name") String name, @Param("description") String description, @Param("price") Integer price){
        Product product=new Product(name,description,price);
        productRepository.save(product);
        return "success";
    }

    @RequestMapping(path="/delete", method=RequestMethod.DELETE)
    public @ResponseBody String deleteProduct(@RequestParam Integer id){
        try {
            productRepository.deleteById(id);
            return "success";
        }catch (Exception e){
            return "fail:product not found";
        }
    }
}
