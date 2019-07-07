package application.controllers;

import application.entities.Product;
import application.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @RequestMapping(path="/create", method = RequestMethod.POST)
    public @ResponseBody String createProduct(@RequestBody Product product){
        productRepository.save(product);
        return "success";
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public @ResponseBody Iterable<Product> getAllProduct(){
        return productRepository.findAll();
    }

    @RequestMapping(path = "/getProduct", method = RequestMethod.GET)
    public @ResponseBody Optional<Product> getProduct(@RequestParam Integer id){
        return productRepository.findById(id);
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

    @RequestMapping(path="/delete", method=RequestMethod.DELETE,params = {"name"})
    public @ResponseBody String deleteProduct(@Param("name") String name){
        try {
            Optional<Product> product=productRepository.findByName(name);
            productRepository.delete(product.get());
            return "success";
        }catch (Exception e){
//            return e.getMessage();
            return "fail:product not found";
        }
    }

    @RequestMapping(path="/update", method=RequestMethod.POST)
    public @ResponseBody String updateProduct(@RequestBody Optional<Product> product){
        if(product.isPresent() && product.get().getId()!=null) {
            Optional<Product> oldProduct=productRepository.findById(product.get().getId());
            if(oldProduct.isPresent()) {
                Product newProduct=oldProduct.get();
                newProduct.setProduct(product.get());
                productRepository.save(newProduct);
                return "success";
            }
        }
        return "Fail";
    }
}
