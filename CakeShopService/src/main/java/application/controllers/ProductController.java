package application.controllers;

import application.ResponseFactory;
import application.RepositoryFacade;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.ObjectOutputAdapter;
import application.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/product")
public class ProductController {
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private ResponseFactory responseFactory;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createProduct(@RequestParam String sessionKey, @RequestParam String sessionValue, @RequestBody Product product){
       if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
       else {
           facade.createProduct(product);
           return new ObjectOutputAdapter(true, "success", product.getId());
       }
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody MessageOutputAdapter getAllProduct(){
        return responseFactory.outputData(facade.findProduct());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody MessageOutputAdapter getProduct(@PathVariable Integer id){
        Optional<Product>product= facade.findProduct(id);
        if(product.isPresent())
            return responseFactory.outputData(product.get());
        else
            return responseFactory.dataNotFound("product");
    }

    @RequestMapping(path="/{id}", method=RequestMethod.DELETE)
    public @ResponseBody MessageOutputAdapter deleteProduct(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable Integer id){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else {
            facade.deleteProduct(id);
            return responseFactory.success();
        }
    }

    @RequestMapping(path="/{id}", method=RequestMethod.PATCH)
    public @ResponseBody
    MessageOutputAdapter updateProduct(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable Integer id, @RequestBody Product productData){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else {
            Optional<Product> oldProduct = facade.findProduct(id);
            if (oldProduct.isPresent()) {
                Product newProduct = oldProduct.get();
                newProduct.setProduct(productData);
                facade.createProduct(newProduct);
                return responseFactory.outputData(newProduct.getId());
            } else {
                facade.createProduct(productData);
                return responseFactory.outputData(productData.getId());
            }
        }
    }
}
