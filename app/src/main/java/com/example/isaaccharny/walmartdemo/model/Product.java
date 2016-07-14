package com.example.isaaccharny.walmartdemo.model;

public class Product {
    private String id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String price;
    private String productImage;
    private double reviewRating;
    private long reviewCount;
    private boolean inStock;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id: ").append(id).append("\n")
                .append("name: ").append(name).append("\n")
                .append("shortDescription: ").append(shortDescription).append("\n")
                .append("longDescription: ").append(longDescription).append("\n")
                .append("price: ").append(price).append("\n")
                .append("productImage: ").append(productImage).append("\n")
                .append("reviewRating: ").append(reviewRating).append("\n")
                .append("reviewCount: ").append(reviewCount).append("\n")
                .append("inStock: ").append(inStock);

        return stringBuilder.toString();
    }
}
