package application.controllers;

import application.adapter.output.MessageOutputAdapter;
import application.entities.Product;
import application.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createProduct(@RequestBody Product product){
        productRepository.save(product);
        return new MessageOutputAdapter(true,"success");
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Product> getAllProduct(){
        return productRepository.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Product> getProduct(@PathVariable Integer id){
        return productRepository.findById(id);
    }

//    @RequestMapping(path="/{id}", method=RequestMethod.DELETE)
//    public @ResponseBody String deleteProduct(@PathVariable Integer id){
//        try {
//            productRepository.deleteById(id);
//            return "success";
//        }catch (Exception e){
//            return "fail:product not found";
//        }
//    }

    @RequestMapping(path="/{name}", method=RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteProduct(@PathVariable String name){
        try {
            Optional<Product> product=productRepository.findByName(name);
            productRepository.delete(product.get());
            return new MessageOutputAdapter(true,"success");
        }catch (Exception e){
            return new MessageOutputAdapter(false,"product not found");
        }
    }

    @RequestMapping(path="/{id}", method=RequestMethod.PATCH)
    public @ResponseBody
    MessageOutputAdapter updateProduct(@PathVariable Integer id, @RequestBody Optional<Product> product){
        if(product.isPresent() && product.get().getId()!=null) {
            Optional<Product> oldProduct=productRepository.findById(product.get().getId());
            if(oldProduct.isPresent()) {
                Product newProduct=oldProduct.get();
                newProduct.setProduct(product.get());
                productRepository.save(newProduct);
                return new MessageOutputAdapter(true,"success");
            }
        }
        return new MessageOutputAdapter(false,"product not found");
    }
}
