import React, { useEffect, useState } from "react";
import Header from "../Components/Header";
import Footer from "../Components/Footer";

function Products() {
  const [allProducts, setAllProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("all");
  const [sort, setSort] = useState("default");
  const [currentPage, setCurrentPage] = useState(1);
  const [showPopup, setShowPopup] = useState(false);
  const productsPerPage = 12;

  // Fetch products on mount
  useEffect(() => {
    async function fetchProducts() {
      const res = await fetch("https://dummyjson.com/products?limit=100");
      const data = await res.json();
      setAllProducts(data.products);
      setFilteredProducts(data.products);
      setCategories([
        ...new Set(data.products.map((p) => p.category)),
      ]);
    }
    fetchProducts();
  }, []);

  // Filter and sort products
  useEffect(() => {
    let products = [...allProducts];
    if (search) {
      products = products.filter(
        (p) =>
          p.title.toLowerCase().includes(search.toLowerCase()) ||
          p.description.toLowerCase().includes(search.toLowerCase())
      );
    }
    if (category !== "all") {
      products = products.filter((p) => p.category === category);
    }
    switch (sort) {
      case "price-low":
        products.sort((a, b) => a.price - b.price);
        break;
      case "price-high":
        products.sort((a, b) => b.price - a.price);
        break;
      case "name-asc":
        products.sort((a, b) => a.title.localeCompare(b.title));
        break;
      case "name-desc":
        products.sort((a, b) => b.title.localeCompare(a.title));
        break;
      default:
        break;
    }
    setFilteredProducts(products);
    setCurrentPage(1);
  }, [search, category, sort, allProducts]);

  // Pagination
  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);
  const currentProducts = filteredProducts.slice(
    (currentPage - 1) * productsPerPage,
    currentPage * productsPerPage
  );

  // Add to cart
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
    <>
      <Header />
      <section className="py-5">
        <div className="container">
          {/* Filters and Search */}
          <div className="row mb-4">
            <div className="col-md-4">
              <div className="input-group">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Search products..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                />
                <button
                  className="btn btn-outline-danger"
                  type="button"
                  onClick={() => setSearch(search)}
                >
                  <i className="fas fa-search"></i>
                </button>
              </div>
            </div>
            <div className="col-md-4">
              <select
                className="form-select"
                value={sort}
                onChange={(e) => setSort(e.target.value)}
              >
                <option value="default">Sort By</option>
                <option value="price-low">Price: Low to High</option>
                <option value="price-high">Price: High to Low</option>
                <option value="name-asc">Name: A to Z</option>
                <option value="name-desc">Name: Z to A</option>
              </select>
            </div>
            <div className="col-md-4">
              <select
                className="form-select"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
              >
                <option value="all">All Categories</option>
                {categories.map((cat) => (
                  <option value={cat} key={cat}>
                    {cat.charAt(0).toUpperCase() + cat.slice(1)}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Products Grid */}
          <div className="row g-4">
            {currentProducts.length === 0 ? (
              <div className="col-12 text-center text-muted">No products found.</div>
            ) : (
              currentProducts.map((product) => (
                <div className="col-md-3 mb-4" key={product.id}>
                  <div className="card h-100 product-card">
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

          {/* Popup */}
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

          {/* Pagination */}
          <div className="d-flex justify-content-center mt-5">
            <nav aria-label="Product pagination">
              <ul className="pagination">
                <li className={`page-item ${currentPage === 1 ? "disabled" : ""}`}>
                  <button
                    className="page-link"
                    onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                  >
                    Previous
                  </button>
                </li>
                {Array.from({ length: totalPages }, (_, i) => (
                  <li
                    className={`page-item ${currentPage === i + 1 ? "active" : ""}`}
                    key={i}
                  >
                    <button className="page-link" onClick={() => setCurrentPage(i + 1)}>
                      {i + 1}
                    </button>
                  </li>
                ))}
                <li className={`page-item ${currentPage === totalPages ? "disabled" : ""}`}>
                  <button
                    className="page-link"
                    onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
                  >
                    Next
                  </button>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
}

export default Products;