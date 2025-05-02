import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function FeaturedProducts() {
  const [products, setProducts] = useState([]);
  const [showPopup, setShowPopup] = useState(false);

  useEffect(() => {
    async function fetchProducts() {
      const res = await fetch("https://dummyjson.com/products/category/furniture?limit=4");
      const data = await res.json();
      setProducts(data.products);
    }
    fetchProducts();
  }, []);

  function addToCart(productId) {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const existing = cart.find((item) => item.id === productId);
    if (existing) {
      existing.quantity += 1;
    } else {
      cart.push({ id: productId, quantity: 1 });
    }
    localStorage.setItem("cart", JSON.stringify(cart));
    window.dispatchEvent(new Event("cartUpdated"));
    setShowPopup(true);
    setTimeout(() => setShowPopup(false), 1500);
  }

  return (
    <section className="py-5 bg-light" id="featured">
      <div className="container">
        <h2 className="text-center mb-5">Featured Products</h2>
        <div className="row g-4">
          {products.length === 0 ? (
            <div className="col-12 text-center text-muted">Loading...</div>
          ) : (
            products.map((product) => (
              <div className="col-md-3" key={product.id}>
                <div className="card h-100">
                  <img
                    src={product.thumbnail}
                    className="card-img-top p-3"
                    alt={product.title}
                    style={{ height: "200px", objectFit: "contain" }}
                  />
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title">{product.title}</h5>
                    <p className="card-text text-danger mb-3">${product.price}</p>
                    <button
                      onClick={() => addToCart(product.id)}
                      className="btn btn-danger mt-auto"
                    >
                      Add to Cart
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
        {showPopup && (
          <div
            style={{
              position: "fixed",
              top: "20px",
              right: "20px",
              background: "#dc3545",
              color: "#fff",
              padding: "1em 2em",
              borderRadius: "8px",
              zIndex: 9999,
              boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
            }}
          >
            Added to cart!
          </div>
        )}
      </div>
    </section>
  );
}

export default FeaturedProducts;
