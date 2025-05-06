import React from "react";

function Sidebar({ onMenuClick, onToggleDarkMode, darkMode }) {
    return (
      <div className="sidebar">
        <div className="sidebar-header">
          <img src="https://placehold.co/100x100" alt="Echo Sphere Logo" className="sidebar-logo" />
          <h2>Echo Sphere</h2>
        </div>
        <ul className="sidebar-menu">
          <li className="active"><a href="#home" onClick={onMenuClick}><i className="fas fa-home"></i><span>Home</span></a></li>
          <li><a href="#explore" onClick={onMenuClick}><i className="fas fa-compass"></i><span>Explore</span></a></li>
          <li><a href="#favorites" onClick={onMenuClick}><i className="fas fa-star"></i><span>Favorites</span></a></li>
          <li><a href="#recent" onClick={onMenuClick}><i className="fas fa-history"></i><span>Recent</span></a></li>
          <li><a href="#settings" onClick={onMenuClick}><i className="fas fa-cog"></i><span>Settings</span></a></li>
        </ul>
        <div className="sidebar-footer">
          <button id="dark-mode-toggle" className="theme-toggle" onClick={onToggleDarkMode}>
            <i className="fas fa-moon"></i>
            <span>{darkMode ? "Light Mode" : "Dark Mode"}</span>
          </button>
        </div>
      </div>
    );
  }
export default Sidebar;
