import React from "react";


function Navbar({ onMenuToggle, onSearch }) {
  return (
    <nav className="navbar navbar-expand-lg sticky-top">
      <div className="container-fluid">
        <div className="d-flex align-items-center">
          <button className="btn btn-link text-dark me-3" onClick={onMenuToggle}>
            <i className="fas fa-bars fa-lg"></i>
          </button>
          <div className="search-box d-flex align-items-center">
            <i className="fas fa-search text-muted me-2"></i>
            <input 
              type="text" 
              className="form-control border-0 bg-transparent" 
              placeholder="Search stations..." 
              onChange={onSearch} 
            />
          </div>
        </div>
        <div className="d-flex align-items-center">
          <div className="user-profile d-flex align-items-center">
            <img 
              src="https://placehold.co/40x40" 
              alt="User Profile" 
              className="rounded-circle me-2"
            />
            <span className="fw-medium">Welcome, User!</span>
          </div>
        </div>
      </div>
    </nav>
  );
}
export default Navbar;
