/*
 *
 */
package com.nhale.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author LeNha
 */
public class Cart implements Serializable {
    private int productId;
    private String productName;
    private BigDecimal price;
    private int quantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
