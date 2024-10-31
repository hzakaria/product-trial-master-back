package product.trial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.trial.api.ProductApi;
import product.trial.exception.BadRequestException;
import product.trial.exception.ResourceNotFoundException;
import product.trial.model.Product;
import product.trial.service.ProductService;

import java.util.List;
import java.util.Objects;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductService productService;

    @Override
    @Operation(summary = "Créer un nouveau produit", description = "Ajoute un produit dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || Objects.isNull(product.getPrice())) {
            throw new BadRequestException("Le nom et le prix du produit sont requis");
        }
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @Override
    @Operation(summary = "Lister tous les produits", description = "Récupère une liste de tous les produits disponibles")
    @ApiResponse(responseCode = "200", description = "Liste de produits récupérée avec succès")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Obtenir un produit par ID", description = "Récupère les détails d'un produit spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit trouvé"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec l'ID " + id + " non trouvé"));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Supprimer un produit par ID", description = "Supprime un produit spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit supprimé"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec l'ID " + id + " non trouvé"));
        productService.deleteProduct(product.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Mettre à jour un produit par ID", description = "Modifie les détails d'un produit spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit mis à jour"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        if (productDetails.getName() == null || Objects.isNull(productDetails.getPrice())) {
            throw new BadRequestException("Le nom et le prix du produit sont requis");
        }
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Vérifier le statut de l'API", description = "Renvoie un message indiquant que l'API est opérationnelle")
    @ApiResponse(responseCode = "200", description = "API opérationnelle")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("API is up and running", HttpStatus.OK);
    }
}
